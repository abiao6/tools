package com.banksteel.tools.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.banksteel.tools.model.Module;
import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.VelocityUtils;

public final class ContainerBuilder extends BaseBuilder {

	public static final void generator(Module module) {
		// container
		List<Map<String, Object>> modelList = module.getModelList();
		if (modelList != null && !modelList.isEmpty()) {
			String testPath = module.getProjectJavaSrcPath() + "container" + ".xml";
			String tplPath = StringUtils.getTplPath("container.vm");
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("modelList", modelList);
			VelocityUtils.generator(model, testPath, tplPath);
		}
	}
}
