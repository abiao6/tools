package com.banksteel.tools.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.stereotype.Component;

/**
 * VelocityUtils
 * @author bin.yin 2015.08.14
 */
@Component("velocityUtils")
public final class VelocityUtils {

	public final static void generator(Map<String, Object> model,
			String filePath, String templatePath) {
		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			Velocity.init();
			// 初始化上下文
			VelocityContext ctx = new VelocityContext();
			if (null != model && !model.isEmpty()) {
				Iterator<String> iter = model.keySet().iterator();
				Object key = null;
				while (iter.hasNext()) {
					key = iter.next();
					if (null == key || !(key instanceof String)) {
						continue;
					}
					ctx.put(key.toString(), model.get(key));
				}
			}
			ctx.put("create_date", new DateTool().get("yyyy.MM.dd"));
			// 加载模板
			ClassLoader contextClassLoader = Thread.currentThread()
					.getContextClassLoader();
			reader = new BufferedReader(new InputStreamReader(
					contextClassLoader.getResourceAsStream(templatePath)));
			// 生成的文件
			File file = new File(filePath);
			File parentFile = file.getParentFile();
			if (null != parentFile && !parentFile.exists()) {
				parentFile.mkdirs();
			}
			if (file.exists()) {// 存在，先备份原文件
				LogUtils.fatal(" 文件已经存在  filePath=" + filePath);
				file.delete();
				//file.renameTo(new File(filePath + ".bak_" + System.currentTimeMillis()));
			}
			writer = new BufferedWriter(new FileWriter(filePath));

			Velocity.evaluate(ctx, writer, "GC", reader);
		} catch (Exception e) {
			LogUtils.error("VelocityUtils.generator(Map, String, String)", e);
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
				if (null != writer) {
					writer.close();
				}
			} catch (IOException e) {
				LogUtils.error("VelocityUtils.generator(Map, String, String)", e);
			}
		}
	}
}
