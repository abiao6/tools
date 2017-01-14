package com.banksteel.tools.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.banksteel.tools.model.Column;
import com.banksteel.tools.model.Table;

/**
 * DBUtils
 * @author bin.yin 2015.08.14
 */
public class DBUtils {
	public static final String TABLE_NAME = "TABLE_NAME";
	public static final String TABLE_COMMENT = "TABLE_COMMENT";
	public static final String COLUMN_NAME = "COLUMN_NAME";
	public static final String COLUMN_COMMENT = "COLUMN_COMMENT";

	@Autowired
	private DataSource dataSource;

	@Value("${select.sql.prefix}")
	private String selectSqlPrefix;//select * from
	@Value("${select.tab.schema}")
	private String tableSchema;//属主
	@Value("${select.tab.comments}")
	private String selectTabComments;//读取表结构
	@Value("${select.col.comments}")
	private String selectColComments;//读取列结构
	
	private String tableComments;
	private Map<String, String> columnComments = new HashMap<String, String>();
	private Map<String, String> replaceTypeMap;

	public Table getTable(String tableName) {
		Table table = new Table(tableName);

		this.getTableComment(tableName);
		this.getColumnComments(tableName);
		List<Column> columns = this.getColumns(table);
		if (null != columns) {
			table.setColumns(columns);
		}
		table.setComments(tableComments);

		return table;
	}
	
	public void getTableComment(String tableName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(selectTabComments);
			pstmt.setString(1, tableName);
			pstmt.setString(2, tableSchema);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				tableComments = rs.getString(TABLE_COMMENT);
			}
			rs.close();
		} catch (SQLException e) {
			LogUtils.error("getTableComment(Table:"+tableName+")", e);
		} finally {
			close(null, pstmt, conn);
		}
	}

	public void getColumnComments(String tableName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(selectColComments);
			pstmt.setString(1, tableName);
			pstmt.setString(2, tableSchema);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				columnComments.put(rs.getString(TABLE_NAME) + "." + rs.getString(COLUMN_NAME), rs.getString(COLUMN_COMMENT));
			}
			rs.close();
		} catch (SQLException e) {
			LogUtils.error("getColumnComments(Table:"+tableName+")", e);
		} finally {
			close(null, pstmt, conn);
		}
	}

	public List<Column> getColumns(Table table) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;
		
		String tableName = table.getName();
		List<Column> columns = new ArrayList<Column>();
		List<String> pks = this.getPrimaryKeys(tableName);
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(selectSqlPrefix + tableSchema + "." + tableName);
			pstmt.execute();
			rsmd = pstmt.getMetaData();

			int count = rsmd.getColumnCount();
			if (count > 0) {
				for (int i = 1; i <= count; i++) {
					Column c = new Column();
					// 名称
					c.setName(rsmd.getColumnName(i));
					
					// 数据库类型
					c.setJdbcType(rsmd.getColumnTypeName(i));
					
					// 长度
					c.setPrecision(rsmd.getPrecision(i));
					
					// 精度
					c.setScale(rsmd.getScale(i));
					
					// 能否为NULL
					c.setNullable(ResultSetMetaData.columnNullable == rsmd.isNullable(i));

					// java类型
					c.setJavaType(this.handleJavaType(rsmd.getColumnClassName(i), c.getScale()));

					// 是否主键判断
					if (null != pks && pks.contains(c.getName())) {
						c.setPk(true);
						table.addPkColumn(c);
					}
					
					//取注释
					c.setComment(columnComments.get(tableName + "." + c.getName()));

					columns.add(c);
				}
			}
		} catch (SQLException e) {
			LogUtils.error("DBUtils.getColumns(Table:"+tableName+")", e);
		} finally {
			close(null, pstmt, conn);
		}

		return columns;
	}

	private String handleJavaType(String tmp, int scale) {
		if (this.replaceTypeMap!=null && this.replaceTypeMap.containsKey(tmp)) {
			tmp = this.replaceTypeMap.get(tmp).toString();
		}
		if ("java.math.BigDecimal".equalsIgnoreCase(tmp)) {// NUMBER类型
			if (scale <= 0) {// 没有精度
				tmp = "int";
			} else {
				tmp = "float";
			}
		}
		return tmp;
	}

	public List<String> getPrimaryKeys(String tableName) {
		List<String> pks = new ArrayList<String>();
		Connection conn = null;
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			dbmd = conn.getMetaData();
			rs = dbmd.getPrimaryKeys(null, tableSchema, tableName);
			while (rs.next()) {
				pks.add(rs.getString(COLUMN_NAME));
			}
		} catch (SQLException e) {
			LogUtils.error("getPrimaryKeys(Table:"+tableName+")",e);
		} finally {
			close(rs, null, conn);
		}
		return pks;
	}

	public Map<String, String> getReplaceTypeMap() {
		return replaceTypeMap;
	}
	public void setReplaceTypeMap(Map<String, String> replaceTypeMap) {
		this.replaceTypeMap = replaceTypeMap;
	}
	
	public Table getTable(String tableName, String condition) {
		Table table = new Table(tableName);
		getColumns(table, condition);
		return table;
	}
	
	public List<Column> getColumns(Table table, String condition) {
		List<Column> columns = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		String tableName = table.getName();

		try {
			getTableComment(tableName);
			List<String> primaryKeys = getPrimaryKeys(tableName);
			getColumnComments(tableName);
			
			String sql = selectSqlPrefix + tableName + condition;
			
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rsmd = pstmt.getMetaData();

			int count = rsmd.getColumnCount();
			if (count > 0) {
				columns = new ArrayList<Column>();
				for (int i = 1; i <= count; i++) {
					Column col = new Column();
					col.setName(rsmd.getColumnName(i));
					col.setJdbcType(rsmd.getColumnTypeName(i));
					if(!"CLOB".equals(col.getJdbcType())){
						col.setPrecision(rsmd.getPrecision(i));
						col.setScale(rsmd.getScale(i));
					}
					if (ResultSetMetaData.columnNullable == rsmd.isNullable(i)) {
						col.setNullable(true);
					}
					col.setJavaType(handleJavaType(rsmd.getColumnClassName(i), col.getScale()));
					// nullValue表达式(数据库字段类型NUMBER且nullable，如果没有默认值，ibatis会报错，需要设置nullValue)
					if ("NUMBER".equalsIgnoreCase(col.getJdbcType())) {
						col.setNullValueExpression("nullValue=\"0\"");
					}
					if (null!=primaryKeys && primaryKeys.contains(col.getName())) {
						col.setPk(true);
						table.addPkColumn(col);
					}
					
					//取注释
					String key = tableName + "." + col.getName();
					col.setComment(columnComments.get(key));

					System.out.println(col);
					columns.add(col);
				}
				table.setComments(tableComments);
				table.setColumns(columns);
				System.out.println("columns:"+columns);
				
				List<List<Column>> data = new ArrayList<List<Column>>();
				while(rs.next()){
					int size = columns.size();
					List<Column> columnData = new ArrayList<Column>();
					for(int i=0; i<size; i++){
						Column col = (Column)BeanUtils.cloneBean(columns.get(i));
						col.setValue(markData(col.getJdbcType(), StringUtils.msNull(rs.getString(col.getName()))));
						columnData.add(col);
					}
					data.add(columnData);
				}
				table.setData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.error("DatabaseUtils.getColumns(Connection, Table:"+tableName+")",e);
		} finally {
			close(rs, pstmt, conn);
		}
		return columns;
	}

	private void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if (null != rs) {
			try {
				rs.close(); rs = null;
			} catch (SQLException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}
		if (null != pstmt) {
			try {
				pstmt.close(); pstmt = null;
			} catch (SQLException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}
		if (null != conn) {
			try {
				conn.close(); conn = null;
			} catch (SQLException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}
	}

	private static String markData(String jdbcType, String value){
		if("VARCHAR2".equals(jdbcType)){
			return "'" + value + "'";
		}else if("DATE".equals(jdbcType)){
			return "TO_DATE('"+value.substring(0, 19)+"', 'YYYY-MM-DD HH24:MI:SS')";
		}else if("CLOB".equals(jdbcType)){
			return "NULL";
		}
		return "".equals(value)?"NULL":value;
	}

	//@Scheduled(cron="0 0/20 * * * ?")
	public void schedule(){
		System.out.println("hi");
	}
}
