package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public class SQLMappingBuilder extends BaseBuilder {

	public final static void generator(Module module, Table table, Map<String, Object> model) {
		String mappingPath = module.getProjectJavaSrcPath() + "sqlmap/" + model.get("lowerClassName")+"-mapper.xml";
		String tplPath = StringUtils.getTplPath("sqlmap-mapping.vm");
		VelocityUtils.generator(model, mappingPath, tplPath);
	}
}
