package com.banksteel.tools.builder;

import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.model.Table;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public class DaoBuilder extends BaseBuilder {

	public final static void generator(Module module, Table table, Map<String, Object> model) {
		// dao interface
		String idaoPath = module.getProjectJavaSrcPath() + "dao/"+ model.get("daoClassName") + ".java";
		String itplPath = StringUtils.getTplPath("dao.vm");
		VelocityUtils.generator(model, idaoPath, itplPath);
		

		// dao implements
		String daoPath = module.getProjectJavaSrcPath() + "dao/impl/" + model.get("daoClassName") + "Impl.java";
		String tplPath = StringUtils.getTplPath("daoImpl.vm");
		VelocityUtils.generator(model, daoPath, tplPath);
	}
}
