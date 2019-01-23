package com.timing.impcl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MantraLog {
    
    public static final String LOG_PROGRESS="LOG_PROGRESS.txt";
    public static final String PROGRESS="PROGRESS_monitor.txt";
    
	String thisClassPath =this.getClass().getResource("").getPath();
	
    public static void WriteProgress(String fileName ,String _value) {//按照名称写入内容
		FileWriter fileWritter=null;
		BufferedWriter bufferWrittern=null;
		MantraLog log = new MantraLog();
		String fileNameAndPath = log.thisClassPath+fileName;
	
		File file = new File (fileNameAndPath);
        
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			fileWritter = new FileWriter(file,true);
			bufferWrittern = new BufferedWriter(fileWritter);
			bufferWrittern.write(" \r\n "+_value);
			if(bufferWrittern!=null) {
				bufferWrittern.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static StringBuffer getFileStrByFileName(String fileName) {//按照名称得到文件内容
		StringBuffer sbf = new StringBuffer();
		MantraLog log = new MantraLog();
		String fileNameAndPath = log.thisClassPath+fileName;
		File file = null;
		try {
			file = new File(fileNameAndPath);
			if(!file.exists()) {
				return sbf.append("无内�?");
			}
			BufferedReader inBuffer = new BufferedReader(new FileReader(file));
			String str = "";
			while ((str=inBuffer.readLine()) != null) {
				sbf.append(str);
	        }
			inBuffer.close();
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}
		return sbf;
	}
	
	public static void deleteLog(String fileName) {  //按照名称清空  文件内容
		MantraLog log = new MantraLog();
		String fileNameAndPath = log.thisClassPath+fileName;
		File file = new File (fileNameAndPath);
		try {
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void fileCreateAndWrite(Exception ex) {
		FileWriter fileWritter=null;
		BufferedWriter bufferWrittern=null;
		MantraLog log = new MantraLog();
		String fileNameAndPath = log.thisClassPath+"config_log_err.txt";
		StackTraceElement [] messages = ex.getStackTrace();
		File file = new File (fileNameAndPath);
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append(ex.toString());
		for(StackTraceElement message : messages) {
			strBuf.append(" \r\n ");
			strBuf.append(message.toString());
		}
		
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			fileWritter = new FileWriter(file,true);
			bufferWrittern = new BufferedWriter(fileWritter);
			bufferWrittern.write(strBuf.toString());
			if(bufferWrittern!=null) {
				bufferWrittern.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    public static StringBuffer getFileErr() {
		StringBuffer sbf = new StringBuffer();
		MantraLog log = new MantraLog();
		String fileNameAndPath = log.thisClassPath+"config_log_err.txt";
		File file = null;
		try {
			file = new File(fileNameAndPath);
			if(!file.exists()) {
				return sbf.append("无错�?");
			}
			BufferedReader inBuffer = new BufferedReader(new FileReader(file));
			String str = "";
			while ((str=inBuffer.readLine()) != null) {
				sbf.append(str);
	        }
			
			inBuffer.close();
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}
		
		
		return sbf;
	}
	
	
	public static void deleteLog() {
		MantraLog log = new MantraLog();
		String fileNameAndPath = log.thisClassPath+"config_log_err.txt";
		File file = new File (fileNameAndPath);
		try {
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
		
	public static StringBuffer exParse(Exception ex) {//解析错误
		StackTraceElement [] messages = ex.getStackTrace();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ex.toString());
		for(StackTraceElement message : messages) {
			strBuf.append(" \r\n ");
			strBuf.append(message.toString());
		}
	    return strBuf;
	}
	
}
