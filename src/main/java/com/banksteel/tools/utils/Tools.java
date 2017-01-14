package com.banksteel.tools.utils;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.banksteel.tools.builder.BaseBuilder;
import com.banksteel.tools.builder.ContainerBuilder;
import com.banksteel.tools.builder.DaoBuilder;
import com.banksteel.tools.builder.EntityBuilder;
import com.banksteel.tools.builder.ExceptionBuilder;
import com.banksteel.tools.builder.FacadeBuilder;
import com.banksteel.tools.builder.SQLMappingBuilder;
import com.banksteel.tools.builder.ServiceBuilder;
import com.banksteel.tools.builder.TestBuilder;
import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;

/**
 * Tools
 * @author bin.yin 2015.08.14
 */
@Component("tools")
public final class Tools {

	@Autowired
	private Module module = null;
	@Autowired
	private DBUtils dbUtils = null;

	public String autosql(String tableName, String queryCondition) {
		StringBuffer buf = new StringBuffer();
		Table table = this.dbUtils.getTable(tableName, StringUtils.isNotEmpty(queryCondition)?" WHERE "+queryCondition:"");
		if (table != null) {
			List<Map<String, String>> data = table.getExportData();
			for(Map<String, String> expdata : data){
				buf.append(expdata.get("update")).append(";\n");
				buf.append(expdata.get("insert")).append(";\n");
			}
		}
		return buf.toString();
	}
	
	public void generator() {
		List<String> tableNameList = module.getTableNames();
		if (tableNameList != null && !tableNameList.isEmpty()) {
			for (String tableName : tableNameList) {
				Table table = this.dbUtils.getTable(tableName);
				if (null == table) {
					continue;
				}
				
				Map<String, Object> model = BaseBuilder.fillVelocity(module, table);
				
				EntityBuilder.generator(module, table, model);
				DaoBuilder.generator(module, table, model);
				ServiceBuilder.generator(module, table, model);
				FacadeBuilder.generator(module, table, model);
				ExceptionBuilder.generator(module, table, model);
				SQLMappingBuilder.generator(module, table, model);
				TestBuilder.generator(module, table, model);

				module.addTable(table);
				module.addModel(model);
			}
			
			ContainerBuilder.generator(module);
		}
	}
}
