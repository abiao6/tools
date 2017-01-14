package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;

public class ExceptionBuilder extends BaseBuilder {

	public final static void generator(Module module, Table table, Map<String, Object> model) {
		// exception
		//String idaoPath = module.getProjectJavaSrcPath() + "exception/"+ model.get("exceptionClassName") + ".java";
		//String itplPath = StringUtils.getTplPath("exception.vm");
		//VelocityUtils.generator(model, idaoPath, itplPath);
		

		// exception code
		//String daoPath = module.getProjectJavaSrcPath() + "exception/" + model.get("exceptionClassName") + "Code.java";
		//String tplPath = StringUtils.getTplPath("exceptionCode.vm");
		//VelocityUtils.generator(model, daoPath, tplPath);
	}
}
