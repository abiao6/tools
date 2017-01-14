package com.banksteel.tools.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banksteel.tools.model.Column;
import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;

/**
 * 数据构建父类
 * @author bin.yin 2015.08.14
 */
public abstract class BaseBuilder {
	private static final Logger log = LoggerFactory.getLogger(BaseBuilder.class);

	public static Map<String, Object> fillVelocity(Module module, Table table) {
		log.debug("###### fill velocity begin....");
		
		List<Column> columnIntable = table.getColumns();
		if (null == columnIntable || columnIntable.isEmpty()) {
			return null;
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("stringUtils", StringUtils.getInstance());
		// 数据库表
		model.put("tableName", table.getName());
		model.put("tableComments", table.getComments());
		
		List<String> ignoreColumns = module.getIgnoreColumns();
		model.put("ignoreColumns", ignoreColumns);
		
		// 列集合(去掉检查字段,BaseDto中已有)
		List<Column> columnList = new ArrayList<Column>();
		for (Column column : table.getColumns()) {
			if (!ignoreColumns.contains(column.getName())) {
				columnList.add(column);
			}
		}
		
		model.put("columnList", columnList);
		
		model.put("logData", module.getLogData() ? 1 : 0);
		model.put("pkType", StringUtils.msNull(module.getPkType()));
		List<Column> pkColumnList = table.getPkColumns();
		model.put("pkColumnList", pkColumnList);
		// 插入字段集合
		List<Column> insertColumnList = new ArrayList<Column>();
		List<Column> updateColumnList = new ArrayList<Column>();
		for (int j = 0, size = columnList.size(); j < size; j++) {
			Column c = (Column) columnList.get(j);
			if (StringUtils.isCheckColumn(c.getName().toUpperCase())) {
				continue;
			}
			
			if (!pkColumnList.contains(c)) {// 非主键
				updateColumnList.add(c);
			}
			insertColumnList.add(c);
		}
		model.put("insertColumnList", insertColumnList);
		model.put("updateColumnList", updateColumnList);
		
		// 版本号
		model.put("serialVersionUID", System.currentTimeMillis());
		
		String prefix = module.getTablePrefix();
		if (table.getName().toLowerCase().startsWith(prefix) && table.getName().indexOf("_") > 0){
			module.setTabName(table.getName().substring(prefix.length()+1, (table.getName().indexOf("_", prefix.length()+1) < 0 ? table.getName().length() : table.getName().indexOf("_", prefix.length()+1))).toLowerCase());
		} else {
			module.setTabName(table.getName().indexOf("_") < 0 ? table.getName().toLowerCase() : table.getName().substring(0, table.getName().indexOf("_")).toLowerCase());
		}
		
		model.put("tabName", module.getTabName());
		model.put("moduleName", module.getName());
		model.put("prefixPackage", module.getPackagePrefixBeforeModule());
		String modulePackage = StringUtils.getModulePackage(module);
		model.put("modulePackage", modulePackage);
		
		// 包路径
		model.put("entityPackage", modulePackage + ".entity");
		model.put("daoPackage", modulePackage + ".dao");
		model.put("servicePackage", modulePackage + ".service");
		model.put("facadePackage", modulePackage + ".facade");
		model.put("restPackage", modulePackage + ".rest");
		model.put("exceptionPackage", modulePackage + ".exception");

		// Entity类名
		String className = StringUtils.tableName2ClassNamePrefix(table.getName());
		if (!module.getIgnoreTables().contains(table.getName())){
			//className = className.substring(prefix.length());
		}
		model.put("className", className);
		model.put("lowerClassName", StringUtils.lowerFirstChar(className));
		model.put("entityClass", new StringBuffer((String)model.get("entityPackage")).append(".").append(className).toString());

		// Dao 类名
		String daoClassName = className + "Dao";
		model.put("daoClassName", daoClassName);
		model.put("lowerDaoClassName", StringUtils.lowerFirstChar(daoClassName));
		model.put("daoClass", new StringBuffer((String)model.get("daoPackage")).append(".").append(daoClassName).toString());
		model.put("daoImplClass", new StringBuffer((String)model.get("daoPackage")).append(".impl.").append(daoClassName).append("Impl").toString());

		// Log Dao 类名
		String logDaoClassName = className + "LogDao";
		model.put("logDaoClassName", logDaoClassName);
		model.put("lowerLogDaoClassName", StringUtils.lowerFirstChar(logDaoClassName));
		model.put("logDaoClass", new StringBuffer((String)model.get("daoPackage")).append(".").append(logDaoClassName).toString());
		model.put("logDaoImplClass", new StringBuffer((String)model.get("daoPackage")).append(".impl.").append(logDaoClassName).append("Impl").toString());

		// Service 类名
		String serviceClassName = className + "Service";
		model.put("serviceClassName", serviceClassName);
		model.put("lowerServiceClassName", StringUtils.lowerFirstChar(serviceClassName));
		model.put("serviceClass", new StringBuffer((String)model.get("servicePackage")).append(".").append(serviceClassName).toString());

		// Facade 类名
		String facadeClassName = className + "Facade";
		model.put("facadeClassName", facadeClassName);
		model.put("lowerFacadeClassName", StringUtils.lowerFirstChar(facadeClassName));
		model.put("facadeClass", new StringBuffer((String)model.get("facadePackage")).append(".").append(facadeClassName).toString());
		
		// Exception 类名
		String exceptionClassName = /*"Vipcos" +*/ StringUtils.upperFirstChar(module.getName()) + "Exception";//className + "Exception";
		model.put("exceptionClassName", exceptionClassName);
		model.put("exceptionClass", new StringBuffer((String)model.get("exceptionPackage")).append(".").append(exceptionClassName).toString());
		
		return model;
	}
}
