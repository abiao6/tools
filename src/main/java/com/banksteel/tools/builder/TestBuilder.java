package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public final class TestBuilder extends BaseBuilder {

	public static final void generator(Module module, Table table, Map<String, Object> model) {
		// test
		String testPath = module.getProjectJavaSrcPath() + "test/Test" + model.get("serviceClassName") + ".java";
		String tplPath = StringUtils.getTplPath("test.vm");

		VelocityUtils.generator(model, testPath, tplPath);
	}
}
