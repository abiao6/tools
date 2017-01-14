package com.banksteel.tools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.banksteel.tools.utils.StringUtils;

/**
 * Table
 * @author bin.yin 2015.08.14
 */
public class Table implements Serializable {
	private static final long serialVersionUID = -2167362360117790732L;

	private String name = null;
	private String comments = null;

	private List<Column> columns = null;
	private List<Column> pkColumns = new ArrayList<Column>();
	private List<List<Column>> data = null;

	public Table(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public void addPkColumn(Column pk) {
		this.pkColumns.add(pk);
	}
	public List<Column> getPkColumns() {
		return pkColumns;
	}
	public List<List<Column>> getData() {
		return data;
	}
	public void setData(List<List<Column>> data) {
		this.data = data;
	}
	
	protected String getInsertColumn() {
		StringBuffer buf = new StringBuffer();
		for(Column column : this.columns){
			//if(!(freedomPrimaryKey(column, "DICT_LIST", "DICT_TYPE") || freedomPrimaryKey(column, "DICT_DATA", "DICT_TYPE", "DICT_CODE"))){
				buf.append(column.getName()).append(",");
			//}
		}
		return buf.toString().replaceAll(",$", "");
	}
	public List<Map<String, String>> getExportData() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(List<Column> columns : this.data){
			Map<String, String> expdata = new TreeMap<String, String>();
			StringBuffer ibuf = new StringBuffer();
			StringBuffer ubuf = new StringBuffer();
			StringBuffer wbuf = new StringBuffer();
			for(Column column : columns){
				boolean freedom = true;
				//if(!(freedomPrimaryKey(column, "DICT_LIST", "DICT_TYPE") || freedomPrimaryKey(column, "DICT_DATA", "DICT_TYPE", "DICT_CODE"))){
					freedom = false;
					ibuf.append(column.getValue()).append(",");
				//}

				if(freedom || (column.isPk() && StringUtils.isNotEmpty(column.getValue()))){
					if(StringUtils.isNotEmpty(wbuf.toString())){
						wbuf.append(" AND ");
					}
					wbuf.append(column.getName()+"="+column.getValue());
				}else{
					ubuf.append(column.getName()+"="+column.getValue()).append(",");
				}
			}
			expdata.put("insert", getInsertPrefix() + ibuf.toString().replaceAll(",$", "") + " FROM DUAL WHERE NOT EXISTS(SELECT 1 FROM "+getName()+" WHERE " + (StringUtils.isNotEmpty(wbuf.toString())?wbuf.toString():"1<>1")+")");
			expdata.put("update", getUpdatePrefix() + ubuf.toString().replaceAll(",$", " WHERE ") + (StringUtils.isNotEmpty(wbuf.toString())?wbuf.toString():"1<>1"));
			list.add(expdata);
		}
		return list;
	}
	
	protected String getInsertPrefix(){
		return "INSERT INTO "+getName()+"("+getInsertColumn()+") SELECT ";
	}
	protected String getUpdatePrefix(){
		return "UPDATE " + getName() + " SET ";
	}

	public boolean freedomPrimaryKey(Column column, String multiTable, String... multiColumn){
		boolean flag = false;
		for(String columnName : multiColumn){
			if(multiTable.equalsIgnoreCase(getName()) && columnName.equalsIgnoreCase(column.getName())){
				flag = true;
				break;
			}
		}
		return flag;
	}

	public String toString() {
		try {
			return ToStringBuilder.reflectionToString(this);
		} catch (Exception e) {
			try {
				return BeanUtils.describe(this).toString();
			} catch (Exception e2) {
				return null;
			}
		}
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
