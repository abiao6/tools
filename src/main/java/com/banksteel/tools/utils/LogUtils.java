package com.banksteel.tools.utils;

import org.apache.log4j.Logger;

/**
 * LogUtils
 * @author bin.yin 2015.08.14
 */
public class LogUtils {
	private static Logger log = Logger.getLogger(LogUtils.class);

	public static void debug(String message) {
		log.debug(message);
	}
	public static void debug(String message, Throwable t) {
		log.debug(message, t);
	}

	public static void info(String message) {
		log.info(message);
	}
	public static void info(String message, Throwable t) {
		log.info(message, t);
	}

	public static void error(String message) {
		log.error(message);
	}
	public static void error(String message, Throwable t) {
		log.error(message, t);
	}

	public static void fatal(String message) {
		log.fatal(message);
	}
	public static void fatal(String message, Throwable t) {
		log.fatal(message, t);
	}

}
