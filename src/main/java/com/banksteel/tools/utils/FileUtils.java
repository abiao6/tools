package com.banksteel.tools.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	public static void main(String[] args) throws IOException {
		
		new File("D:\\bin.yin\\workspacegbk\\banksteel-rongzi-vipcos-omgtapp\\.settings - 副本").renameTo(new File("D:\\bin.yin\\workspacegbk\\banksteel-rongzi-vipcos-omgtapp\\.settings"));
		/*Collection<File> files = org.apache.commons.io.FileUtils.listFiles(new File("D:\\bin.yin\\workspace\\banksteel-rongzi-vipcos-webapp\\src"), new String[]{"java"}, true);
		for (File file : files) {
			String path = file.getAbsolutePath();
			System.out.println("####create bak file..." + path);
			
			List<String> content = org.apache.commons.io.FileUtils.readLines(file, "GBK");
			file.delete();
			System.out.println("####file delete...");
			
			org.apache.commons.io.FileUtils.writeLines(new File(path), "UTF-8", content);
			System.out.println("####write file success...");
		}*/
	}
}
