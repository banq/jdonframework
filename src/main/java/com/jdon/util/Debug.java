/*
 * $Id: Debug.java,v 1.2 2005/01/31 05:27:54 jdon Exp $
 *
 *  Copyright (c) 2001, 2002 The Open For Business Project - www.ofbiz.org
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 *  OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 *  OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 *  THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jdon.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configurable Debug logging wrapper class
 * 
 * @author <a href="mailto:jonesde@ofbiz.org">David E. Jones</a>
 * @author <a href="mailto:jaz@zsolv.com">Andy Zeneski</a>
 * @version 1.0
 * @created July 1, 2001
 */
public final class Debug {

	public final static String LOG = "log.level";
	public final static String LOG4J = "log.log4j";

	public final static String SETUPNAME = "setup";
	public final static String SETUPVALUE = "true";

	public static boolean useLog4J = false;
	public static DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	private final static PropsUtil propsUtil = new PropsUtil("log.xml");

	public static final int ALWAYS = 0;
	public static final int VERBOSE = 1;
	public static final int TIMING = 2;
	public static final int INFO = 3;
	public static final int IMPORTANT = 4;
	public static final int WARNING = 5;
	public static final int ERROR = 6;
	public static final int FATAL = 7;

	public static int conf_level = -1;

	public static final String[] levels = { "Always", "Verbose", "Timing", "Info", "Important", "Warning", "Error", "Fatal" };
	public static final String[] levelProps = { "", "print.verbose", "print.timing", "print.info", "print.important", "print.warning", "print.error",
			"print.fatal" };
	public static final Level[] levelObjs = { Level.INFO, Level.DEBUG, Level.DEBUG, Level.INFO, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL };

	protected static PrintStream printStream = System.out;
	protected static PrintWriter printWriter = new PrintWriter(printStream);

	static {
		try {
			String levelStrs = propsUtil.getProperty(LOG);
			if (levelStrs != null) {
				conf_level = Integer.parseInt(levelStrs);
			}

			String log4jStrs = propsUtil.getProperty(LOG4J);
			if (log4jStrs != null)
				if (log4jStrs.equalsIgnoreCase("true"))
					useLog4J = true;

		} catch (Exception e) {
			System.err.print("getLogLevel e");
			conf_level = 1;
			useLog4J = false;
		}
	}

	public static PrintStream getPrintStream() {
		return printStream;
	}

	public static void setPrintStream(PrintStream printStream) {
		Debug.printStream = printStream;
		Debug.printWriter = new PrintWriter(printStream);
	}

	public static PrintWriter getPrintWriter() {
		return printWriter;
	}

	public static org.apache.logging.log4j.Logger getLogger(String module) {
		if (module != null && module.length() > 0) {
			return LogManager.getLogger(module);
		} else {
			return LogManager.getLogger(Debug.class);
		}
	}

	public static void log(int level, Throwable t, String msg, String module) {
		if (msg == null)
			msg = "[jdonframework]";
		log(level, t, msg, module, "com.jdon.util.Debug");
	}

	private static void log(int level, Throwable t, String msg, String module, String callingClass) {
		if (level < conf_level)
			return;
		if (useLog4J) {
			uselog4j(level, t, msg, module, callingClass);
		} else {
			noLog4J(level, t, msg, module, callingClass);
		}

	}

	private static void uselog4j(int level, Throwable t, String msg, String module, String callingClass) {
		Logger logger = getLogger(module);
		if (logger.isDebugEnabled())
			logger.debug(msg, t);
		else
			uselog4j2(t, msg, module);
	}

	private static void uselog4j2(Throwable t, String msg, String module) {
		Logger logger = getLogger(module);
		if (logger.isErrorEnabled())
			logger.error(msg, t);
		else
			uselog4j3(t, msg, module);
	}

	private static void uselog4j3(Throwable t, String msg, String module) {
		Logger logger = getLogger(module);
		if (logger.isInfoEnabled())
			logger.info(msg, t);
		else
			uselog4j4(t, msg, module);
	}

	private static void uselog4j4(Throwable t, String msg, String module) {
		Logger logger = getLogger(module);
		if (logger.isWarnEnabled())
			logger.warn(msg, t);
		else if (logger.isFatalEnabled())
			logger.fatal(msg, t);
		else if (logger.isTraceEnabled())
			logger.trace(msg, t);
	}

	private static void noLog4J(int level, Throwable t, String msg, String module, String callingClass) {
		StringBuilder prefixBuf = new StringBuilder();
		prefixBuf.append(dateFormat.format(new java.util.Date()));
		prefixBuf.append(" [Debug");
		if (module != null) {
			prefixBuf.append(":");
			prefixBuf.append(module);
		}
		prefixBuf.append(":");
		prefixBuf.append(levels[level]);
		prefixBuf.append("] ");
		if (msg != null) {
			getPrintStream().print(prefixBuf.toString());
			getPrintStream().println(msg);
		}
		if (t != null) {
			getPrintStream().print(prefixBuf.toString());
			getPrintStream().println("Received throwable:");
			t.printStackTrace(getPrintStream());
		}
	}

	public static boolean isOn(int level) {
		return (level == Debug.ALWAYS);
	}

	public static void log(String msg) {
		log(Debug.ALWAYS, null, msg, null);
	}

	public static void log(String msg, String module) {
		log(Debug.ALWAYS, null, msg, module);
	}

	public static void log(Throwable t) {
		log(Debug.ALWAYS, t, t.getMessage(), null);
	}

	public static void log(Throwable t, String msg) {
		log(Debug.ALWAYS, t, msg, null);
	}

	public static void log(Throwable t, String msg, String module) {
		log(Debug.ALWAYS, t, msg, module);
	}

	public static boolean verboseOn() {
		return isOn(Debug.VERBOSE);
	}

	public static void logVerbose(String msg) {
		log(Debug.VERBOSE, null, msg, null);
	}

	public static void logVerbose(String msg, String module) {
		log(Debug.VERBOSE, null, msg, module);
	}

	public static void logVerbose(Throwable t) {
		log(Debug.VERBOSE, t, t.getMessage(), null);
	}

	public static void logVerbose(Throwable t, String msg) {
		log(Debug.VERBOSE, t, msg, null);
	}

	public static void logVerbose(Throwable t, String msg, String module) {
		log(Debug.VERBOSE, t, msg, module);
	}

	public static boolean timingOn() {
		return isOn(Debug.TIMING);
	}

	public static void logTiming(String msg) {
		log(Debug.TIMING, null, msg, null);
	}

	public static void logTiming(String msg, String module) {
		log(Debug.TIMING, null, msg, module);
	}

	public static void logTiming(Throwable t) {
		log(Debug.TIMING, t, t.getMessage(), null);
	}

	public static void logTiming(Throwable t, String msg) {
		log(Debug.TIMING, t, msg, null);
	}

	public static void logTiming(Throwable t, String msg, String module) {
		log(Debug.TIMING, t, msg, module);
	}

	public static boolean infoOn() {
		return isOn(Debug.INFO);
	}

	public static void logInfo(String msg) {
		log(Debug.INFO, null, msg, null);
	}

	public static void logInfo(String msg, String module) {
		log(Debug.INFO, null, msg, module);
	}

	public static void logInfo(Throwable t) {
		log(Debug.INFO, t, t.getMessage(), null);
	}

	public static void logInfo(Throwable t, String msg) {
		log(Debug.INFO, t, msg, null);
	}

	public static void logInfo(Throwable t, String msg, String module) {
		log(Debug.INFO, t, msg, module);
	}

	public static boolean importantOn() {
		return isOn(Debug.IMPORTANT);
	}

	public static void logImportant(String msg) {
		log(Debug.IMPORTANT, null, msg, null);
	}

	public static void logImportant(String msg, String module) {
		log(Debug.IMPORTANT, null, msg, module);
	}

	public static void logImportant(Throwable t) {
		log(Debug.IMPORTANT, t, t.getMessage(), null);
	}

	public static void logImportant(Throwable t, String msg) {
		log(Debug.IMPORTANT, t, msg, null);
	}

	public static void logImportant(Throwable t, String msg, String module) {
		log(Debug.IMPORTANT, t, msg, module);
	}

	public static boolean warningOn() {
		return isOn(Debug.WARNING);
	}

	public static void logWarning(String msg) {
		log(Debug.WARNING, null, msg, null);
	}

	public static void logWarning(String msg, String module) {
		log(Debug.WARNING, null, msg, module);
	}

	public static void logWarning(Throwable t) {
		log(Debug.WARNING, t, t.getMessage(), null);
	}

	public static void logWarning(Throwable t, String msg) {
		log(Debug.WARNING, t, msg, null);
	}

	public static void logWarning(Throwable t, String msg, String module) {
		log(Debug.WARNING, t, msg, module);
	}

	public static boolean errorOn() {
		return isOn(Debug.ERROR);
	}

	public static void logError(String msg) {
		log(Debug.ERROR, null, msg, null);
	}

	public static void logError(String msg, String module) {
		log(Debug.ERROR, null, msg, module);
	}

	public static void logError(Throwable t) {
		log(Debug.ERROR, t, t.getMessage(), null);
	}

	public static void logError(Throwable t, String msg) {
		log(Debug.ERROR, t, msg, null);
	}

	public static void logError(Throwable t, String msg, String module) {
		log(Debug.ERROR, t, msg, module);
	}

	public static boolean fatalOn() {
		return isOn(Debug.FATAL);
	}

	public static void logFatal(String msg) {
		log(Debug.FATAL, null, msg, null);
	}

	public static void logFatal(String msg, String module) {
		log(Debug.FATAL, null, msg, module);
	}

	public static void logFatal(Throwable t) {
		log(Debug.FATAL, t, t.getMessage(), null);
	}

	public static void logFatal(Throwable t, String msg) {
		log(Debug.FATAL, t, msg, null);
	}

	public static void logFatal(Throwable t, String msg, String module) {
		log(Debug.FATAL, t, msg, module);
	}
}
