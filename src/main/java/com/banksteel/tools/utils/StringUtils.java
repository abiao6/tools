package com.banksteel.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banksteel.tools.model.Module;

/**
 * StringUtils
 * @author bin.yin 2015.08.14
 */
public final class StringUtils {
	private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

	public static final String TABLE_PEFIX = "NTL_";

	private static StringUtils instance = new StringUtils();
	
	/**
	 * 不需要引入的类
	 */
	private static List<String> noImportType = new ArrayList<String>();

	static {
		noImportType.add("int");
		noImportType.add("float");
		noImportType.add("double");
		noImportType.add("boolean");
		noImportType.add("java.lang.String");
		noImportType.add("java.lang.Integer");
		noImportType.add("java.lang.Long");
		noImportType.add("java.lang.Float");
		noImportType.add("java.lang.Double");
		noImportType.add("java.lang.Boolean");
	}
	
	/**
	 * 
	 * 构造函数
	 */
	private StringUtils() {

	}
	
	/**
	 * 
	 * @Description:得到唯一实例 
	 * @Author:bin.yin
	 * @Create Time: 2010-4-1 上午08:46:41
	 * @returnStringUtils
	 */
	public final static StringUtils getInstance(){
		return StringUtils.instance;
	}

	/**
	 * 
	 * @Description: 是否检查字段
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 下午04:21:44
	 * @param columnName
	 * @return
	 */
	public final static boolean isCheckColumn(String columnName) {
		return "ID".equals(columnName);//null == Tools.checkColunms ? false : Tools.checkColunms.contains(columnName);
	}

	/**
	 * 
	 * @Description: 数据库表名转换成类名前缀，如：LTMR_CUSTOMER 转换结果 Customer
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:55:01
	 * @param tableName
	 * @returnString
	 */
	public final static String tableName2ClassNamePrefix(String tableName) {
		if (tableName.startsWith(TABLE_PEFIX, 0)) {
			tableName = tableName.substring(4);
		}
		tableName = camelName(tableName);

		return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
	}

	/**
	 * 
	 * @Description: 类型缩写，如：java.lang.String 为String
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:55:49
	 * @param javaType
	 * @returnString
	 */
	public final static String abbreviateJavaType(String javaType) {
		int index = javaType.lastIndexOf(".");
		return javaType.substring(index + 1);
	}

	/**
	 * 
	 * @Description: 
	 *               是否需要引入类，对于java.lang包下的类型和基本类型(int/long/double/boolean等)不需要引入
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:56:32
	 * @param javaType
	 * @returnint
	 */
	public final static int needImport(String javaType) {
		return (noImportType.contains(javaType)) ? 0 : 1;

		// return (javaType.indexOf("java.lang") != -1) ? 0 : 1;
	}

	/**
	 * 
	 * @Description: 根据字段名称得到setter
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:57:32
	 * @param columnName
	 * @returnString
	 */
	public final static String setMethodName(String columnName) {
		//columnName = camelName(columnName);
		columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
		return "set" + columnName;
	}

	/**
	 * 
	 * @Description: 根据字段名称得到getter
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:57:48
	 * @param columnName
	 * @returnString
	 */
	public final static String getMethodName(String columnName) {
		//columnName = camelName(columnName);
		columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
		return "get" + columnName;
	}

	/**
	 * 
	 * @Description: 根据字段名称得到field骆驼式名称
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:58:05
	 * @param columnName
	 * @returnString
	 */
	public final static String camelName(String columnName) {
		columnName = columnName.toLowerCase();
		while (true) {
			int index = columnName.indexOf("_");
			if (index == -1) {
				break;
			}
			columnName = columnName.substring(0, index)
					+ columnName.substring(index + 1, index + 2).toUpperCase()
					+ columnName.substring(index + 2);
		}
		columnName = columnName.replaceAll("_", "");
		return columnName;
	}
	
	/**
	 * 
	 * @Description: 根据字段名称得到field骆驼式名称转换为类名
	 * @Author:xiechengcheng
	 * @Create Time: 2013-3-6 
	 * @param columnName
	 * @returnString
	 */
	public final static String BeanName(String columnName){
		String str=camelName(columnName);
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}

	/**
	 * 
	 * @Description:包路径转物理路径
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:17:13
	 * @param packagePath
	 * @returnString
	 */
	public final static String package2Path(String packagePath) {
		return packagePath.replaceAll("\\.", "/");
	}

	/**
	 * 
	 * @Description:得到模块的包路径
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:43:45
	 * @param module
	 * @returnString
	 */
	public final static String getModulePackage(Module module) {
		String packagePath = module.getPackagePrefixBeforeModule();
		if (!packagePath.endsWith(".")) {
			packagePath += ".";
		}
		return packagePath + module.getName();
	}

	/**
	 * 
	 * @Description: 得到文件物理路径
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午11:19:18
	 * @param projectJavaSrcPath
	 *            工程java代码物理路径
	 * @param packagePath
	 *            包路径
	 * @returnString
	 */
	public final static String getFilePath(String projectJavaSrcPath,
			String packagePath) {
		String filePath = projectJavaSrcPath;
		if (!filePath.endsWith("/")) {
			filePath += "/";
		}
		filePath += StringUtils.package2Path(packagePath.toString());
		if (!filePath.endsWith("/")) {
			filePath += "/";
		}
		return filePath;
	}

	/**
	 * 
	 * @Description:首字符小写
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午11:00:36
	 * @param str
	 * @returnString
	 */
	public final static String lowerFirstChar(String str) {
		return str = str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * 
	 * @Description:首字符小写
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午11:00:36
	 * @param str
	 * @returnString
	 */
	public final static String upperFirstChar(String str) {
		return str = str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 
	 * @Description:得到模板路径
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:59:09
	 * @param tplName
	 * @returnString
	 */
	public final static String getTplPath(String tplName) {
		String tplPath = StringUtils.package2Path(StringUtils.class.getPackage().getName());
		tplPath += "/template/" + tplName;
		return tplPath;
	}

	/**
	 * 
	 * @Description:得到配置文件路径
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午09:59:57
	 * @param module
	 * @returnString
	 */
	public final static String getConfigDir(Module module) {
		String path = module.getProjectConfigPath();
		if (!path.endsWith("/")) {
			path += "/";
		}
		return path;
	}

	/**
	 * 
	 * @Description: 得到biz文件目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:02:51
	 * @param module
	 * @returnString
	 */
	public final static String getBizFileDir(Module module) {
		return getConfigDir(module) + "biz/";
	}
	
	/**
	 * 
	 * @Description: 得到HTML文件目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:02:51
	 * @param module
	 * @returnString
	 */
	public final static String getHTMLDir(Module module) {
		return getModuleHTMLDir(module);
	}


	/**
	 * 
	 * @Description:得到Service源码物理目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:06:31
	 * @param module
	 * @returnString
	 */
	public final static String getServiceSrcDir(Module module) {
		return getModuleJavaSrcDir(module) + StringUtils.package2Path(StringUtils.getModulePackage(module)) + "/service/";
	}

	/**
	 * 
	 * @Description:得到Action源码物理目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:06:31
	 * @param module
	 * @returnString
	 */
	public final static String getActionSrcDir(Module module) {
		return getModuleJavaSrcDir(module) + StringUtils.package2Path(StringUtils.getModulePackage(module)) + "/biz/action/";
	}


	/**
	 * 
	 * @Description:得到Dao源码物理目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:06:16
	 * @param module
	 * @returnString
	 */
	public final static String getDaoSrcDir(Module module) {
		return getModuleJavaSrcDir(module) +StringUtils.package2Path(StringUtils.getModulePackage(module))
				+ "/integration/dao/";
	}

	/**
	 * 
	 * @Description:得到Dto源码物理目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:05:41
	 * @param module
	 * @returnString
	 */
	public final static String getDtoSrcDir(Module module) {
		return getModuleJavaSrcDir(module) + StringUtils.package2Path(StringUtils.getModulePackage(module)) + "/entity/";
	}

	/**
	 * 
	 * @Description: 得到模块Java源码物理目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:04:57
	 * @param module
	 * @returnString
	 */
	public final static String getModuleJavaSrcDir(Module module) {
		String path = module.getProjectJavaSrcPath();
		if (!path.endsWith("/")) {
			path += "/";
		}
			
		return path;
	}
	
	/**
	 * 
	 * @Description: 得到模块HTML源码物理目录
	 * @Author:bin.yin
	 * @Create Time: 2010-3-31 上午10:04:57
	 * @param module
	 * @returnString
	 */
	public final static String getModuleHTMLDir(Module module) {
		String path = module.getProjectHTMLPath();
		if (!path.endsWith("/")) {
			path += "/";
		}
			
		return path;
	}
	
	public final static String toLowerCase(String str){
		return str.toLowerCase();
	}
	public final static String setMethodName2(String columnName) {
		columnName = columnName.substring(0, 1).toUpperCase()
				+ columnName.substring(1).toLowerCase();
		return "set" + columnName;
	}
	public final static String getMethodName2(String columnName) {
		columnName = columnName.substring(0, 1).toUpperCase()
				+ columnName.substring(1).toLowerCase();
		return "get" + columnName;
	}

	public static String msNull(String str) {
		return isEmpty(str) ? "" : str;
	}
	public static boolean isEmpty(CharSequence... cs) {
		boolean flag = false;
		if(cs != null){
			for(CharSequence c : cs){
				if(c == null || c.length() == 0){
					flag = true;
					break;
				}
			}
		}else{
			flag = true;
		}
        return flag;
    }
    public static boolean isNotEmpty(CharSequence... cs) {
        return !isEmpty(cs);
    }

    public static String msNumber(String str){
    	if(isNotEmpty(str)){
			return str.replaceAll("[\\+%,]+", "");
		}
    	return msNull(str);
    }

    public static int toInteger(String str){
    	return toInteger(str, 0);
    }
	public static int toInteger(String str, int defaultValue){
		if(isEmpty(str)) return defaultValue;
		try {
            return Integer.parseInt(msNumber(str));
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
	}
	public static long toLong(String str){
		return toLong(str, 0L);
	}
	public static long toLong(String str, long defaultValue){
		if(isEmpty(str)) return defaultValue;
		try {
            return Long.parseLong(msNumber(str));
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
	}
	public static float toFloat(String str){
		return toFloat(str, 0.0f);
	}
	public static float toFloat(String str, float defaultValue) {
		if(isEmpty(str)) return defaultValue;
		try {
			return Float.parseFloat(msNumber(str));
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}
	public static double toDouble(String str) {
		return toDouble(str, 0.0d);
	}
	public static double toDouble(String str, double defaultValue) {
		if(isEmpty(str)) return defaultValue;
		try {
			return Double.parseDouble(msNumber(str));
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}
	
	public static String getDatePattern() {
        return "yyyy-MM-dd";
    }

	public static String getDateTimePattern() {
        return "yyyy-MM-dd HH:mm:ss";
    }

	public static String getNow(String format){
		return format(new Date(), format);
	}
	public static String getDateNow(){
		return format(new Date());
	}
	public static String getDateTimeNow(){
		return getNow(getDateTimePattern());
	}
	public static String format(Date date){
		return format(date, getDatePattern());
	}
	public static String format(Date date, String pattern){
		if(date != null){
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return "";
	}
	public static String format(String str){
		if(isNotEmpty(str)){
			if(str.trim().length() > 10){
				return format(parse(str), getDateTimePattern());
			}
			return format(parse(str));
		}
		return "";
	}
	public static String format(String str, String pattern){
		if(isNotEmpty(str) && isNotEmpty(pattern)){
			return format(parse(str), getDatePattern());
		}
		return "";
	}
	public static Date parse(String str){
		if(isNotEmpty(str)){
			Matcher m = Pattern.compile("^(\\d{4})[^\\d]?\\s?(\\d{1,2})\\s?[^\\d]?\\s?(\\d{1,2})\\s?[^\\d]?([^\\d]+(\\d{1,2})[^\\d]+(\\d{1,2})[^\\d]+(\\d{1,2})[^\\d]?)?$").matcher(str);
			if(m.matches()){
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, toInteger(m.group(1)));
				c.set(Calendar.MONTH, toInteger(m.group(2)) - 1);
				c.set(Calendar.DATE, toInteger(m.group(3)));
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				if(isNotEmpty(m.group(5), m.group(6), m.group(7))){
					c.set(Calendar.HOUR_OF_DAY, toInteger(m.group(5)));
					c.set(Calendar.MINUTE, toInteger(m.group(6)));
					c.set(Calendar.SECOND, toInteger(m.group(7)));
				}
				return c.getTime();
			}else{
				log.error("invalid date string:"+str);
			}
		}
		return null;
	}
	public static Date parse(String str, String pattern){
		if(isNotEmpty(str)){
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			try {
				return df.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String addWorkDate(Date date, int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
			c.add(Calendar.DATE, day > 0 ? 2 : -2);
		}
		return format(c.getTime(), getDatePattern());
	}
	public static String getWorkDate(){
		Calendar now = Calendar.getInstance();
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == Calendar.SATURDAY){
			now.add(Calendar.DATE, -1);
		}else if(dayOfWeek == Calendar.SUNDAY){
			now.add(Calendar.DATE, -2);
		}
		return format(now.getTime(), getDatePattern());
	}

	public static long getTimeNow(){
		return new Date().getTime();
	}
	public static long getTime(String str){
		Date date = parse(str);
		if(date != null){
			return date.getTime();
		}
		return 0L;
	}

	public static void main(String[] args) throws Exception {
		
	}
}