package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public class ServiceBuilder extends BaseBuilder {

	public final static void generator(Module module, Table table, Map<String, Object> model) {
		// Service interface
		String idaoPath = module.getProjectJavaSrcPath() + "service/" + model.get("serviceClassName") + ".java";
		String tplPath = StringUtils.getTplPath("service.vm");
		VelocityUtils.generator(model, idaoPath, tplPath);

		// Service implements
		String daoPath = module.getProjectJavaSrcPath() + "service/impl/" + model.get("serviceClassName") + "Impl.java";
		tplPath = StringUtils.getTplPath("serviceImpl.vm");
		VelocityUtils.generator(model, daoPath, tplPath);
	}

}
