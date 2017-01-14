package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public class FacadeBuilder extends BaseBuilder {

	public final static void generator(Module module, Table table, Map<String, Object> model) {
		// Facade interface
		String idaoPath = module.getProjectJavaSrcPath() + "facade/" + model.get("facadeClassName") + ".java";
		String tplPath = StringUtils.getTplPath("facade.vm");
		VelocityUtils.generator(model, idaoPath, tplPath);

		// Facade implements
		//String daoPath = module.getProjectJavaSrcPath() + "facade/impl/" + model.get("facadeClassName") + "Impl.java";
		//tplPath = StringUtils.getTplPath("facadeImpl.vm");
		//VelocityUtils.generator(model, daoPath, tplPath);
	}

}
