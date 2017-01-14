package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public final class EntityBuilder extends BaseBuilder {

	public static final void generator(Module module, Table table, Map<String, Object> model) {
		// entity
		String entityPath = module.getProjectJavaSrcPath() + "entity/" + model.get("className") + ".java";
		String tplPath = StringUtils.getTplPath("entity.vm");
		VelocityUtils.generator(model, entityPath, tplPath);
	}
}
