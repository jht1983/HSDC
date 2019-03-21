package com.poi.java;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import com.yulongtao.sys.Dic;
import com.timing.impcl.MantraLog;

public class PoiUtill {
	
		
	static String strPath = new StringBuffer(Dic.strCurPath).append("uploads").append(Dic.strPathSplit).append("tempExcel").append(Dic.strPathSplit).toString();
	static HSSFFont titleFont = null;
	static HSSFFont contentFont = null;
	static HSSFCellStyle titleStyle = null;
	static HSSFCellStyle contentStyle = null;
	public void init(int i,HSSFWorkbook wb) {
	
		File AttachFile = new File(strPath);
		if (!AttachFile.exists() && !AttachFile.isDirectory()) {
			AttachFile.mkdir();
		}
		
		switch (i) {
		case 0:
			titleFont = wb.createFont();
			titleFont.setFontName("黑体");
			titleFont.setFontHeightInPoints((short)13);
			
			contentFont = wb.createFont();
			contentFont.setFontName("宋体");
			contentFont.setFontHeightInPoints((short)12);
			
			titleStyle = wb.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			titleStyle.setFont(titleFont);
			titleStyle.setBorderBottom((short)1);
			titleStyle.setBorderLeft((short)1);
			titleStyle.setBorderRight((short)1);
			titleStyle.setBorderTop((short)1);
			
			contentStyle = wb.createCellStyle();
			contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			contentStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			contentStyle.setFont(contentFont);
			contentStyle.setBorderBottom((short)1);
			contentStyle.setBorderLeft((short)1);
			contentStyle.setBorderRight((short)1);
			contentStyle.setBorderTop((short)1);
			
			break;

		default:
			break;
		}
	}
	public void createExcel() {

		try {
			String [] ExcelL = {"组织","缺陷编号","缺陷类别","缺陷性质","缺陷状态","设备编码"};
			String [] ExcelL2 = {"组织12321321","缺111陷编号","缺陷类别","缺陷性质","缺陷状态","设备编码"};
			HSSFWorkbook wb = new HSSFWorkbook();//创建一个整体的文件对象
			init(0,wb);
			HSSFSheet sheet = wb.createSheet("first sheet"); //创建第一个页签
			sheet.setDefaultColumnWidth(30); //设置宽
			
			HSSFRow row = sheet.createRow(0);   //得到第一行
			for(int i = 0 , j =  ExcelL.length ; i<j ; i ++) {
				row.createCell(i).setCellValue(ExcelL[i]);
				row.getCell(i).setCellStyle(titleStyle);
			}
			
			row = sheet.createRow(1);   //得到第二行
			for(int i = 0 , j =  ExcelL2.length ; i<j ; i ++) {
				row.createCell(i).setCellValue(ExcelL2[i]);
				row.getCell(i).setCellStyle(contentStyle);
			}
			row = sheet.createRow(2);   //得到第二行
			for(int i = 0 , j =  ExcelL2.length ; i<j ; i ++) {
				row.createCell(i).setCellValue(ExcelL2[i]);
				row.getCell(i).setCellStyle(contentStyle);
			}
			wb.write(new FileOutputStream(strPath+"po1i.xls"));
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}

	}
	

	
}
