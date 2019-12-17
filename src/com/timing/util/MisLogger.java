/**
 * 
 */
package com.timing.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.Debug;
import com.timing.impcl.MantraLog;

/**
 * @author tianshisheng
 *
 */
public class MisLogger {
	public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Class className = MisLogger.class;
	
	public MisLogger() {
		// TODO Auto-generated constructor stub
	}

	public MisLogger(Class className) {
		this.className = className;
	}
	
	/**
	 * 
	 * @param message
	 */
	public void debug(String message) {
		Debug.println(strSdfYmdHms.format(new Date()) + " " + className.getName() + ": " + message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public void debug(String message, Exception e) {
		Debug.println(strSdfYmdHms.format(new Date()) + " " + className.getName() + ": " + message + " - " + e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * 
	 * @param message
	 */
	public void debugToProcessFile(String message) {
		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, strSdfYmdHms.format(new Date()) + " " + className.getName() + ": " + message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public void info(String message) {
		System.out.println(strSdfYmdHms.format(new Date()) + " " + className.getName() + ": " + message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public void info(String message, Exception e) {
		System.out.println(strSdfYmdHms.format(new Date()) + " " + className.getName() + ": " + message + " - " + e.getMessage());
		e.printStackTrace();
	}
}
