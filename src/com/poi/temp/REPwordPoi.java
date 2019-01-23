package com.poi.temp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.timing.impcl.MantraLog;

import org.apache.poi.openxml4j.opc.OPCPackage;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

public class REPwordPoi {
	public static ByteArrayOutputStream readwriteWord(String filePath, Map<String, String> data ,List<HashMap> tableDate) {
	    try{
        	FileInputStream fin = new FileInputStream(filePath);
        		XWPFDocument document = new XWPFDocument(OPCPackage.open(fin));
        
        		Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
        		while (itPara.hasNext()) {
        			XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
        			Set<String> set = data.keySet();
        			Iterator<String> iterator = set.iterator();
        			while (iterator.hasNext()) {
        				String key = iterator.next();
        				List<XWPFRun> run = paragraph.getRuns();
        				for (int i = 0; i < run.size(); i++) {
        					if (run.get(i).getText(run.get(i).getTextPosition()) != null
        							&& run.get(i).getText(run.get(i).getTextPosition()).equals(key)) {
        						/**
        						 * 参数0表示生成的文字是要从哪一个地方开始放置,设置文字从位置0开始 就可以把原来的文字全部替换掉了
        						 */
        						run.get(i).setText(data.get(key), 0);
        					}
        				}
        			}
        		}
        
        		String tempC = "";
        		String textValue = "";
        		String value = "";
        		XWPFParagraph newPara = null;
        		Iterator<XWPFTable> itTable = document.getTablesIterator();
     	        while (itTable.hasNext()) {
        			XWPFTable table = (XWPFTable) itTable.next();
        			int count = table.getNumberOfRows();
        			for (int i = 0; i < count; i++) {
        				XWPFTableRow row = table.getRow(i);
        				List<XWPFTableCell> cells = row.getTableCells();
        				for (XWPFTableCell cell : cells) {
        					for(XWPFParagraph paragraph:cell.getParagraphs()) {
        						for(XWPFRun run: paragraph.getRuns()) {
        							textValue = run.getText(0);
        							 while (textValue.indexOf("${") >= 0&&textValue.indexOf("}")>0) {
        								 tempC = textValue.substring(textValue.indexOf("${"),
        								 textValue.indexOf("}")+1);
        								 value = data.get(tempC) == null ? "" : data.get(tempC);
        								 textValue = textValue.replace(tempC, value);
        							 }
        							run.setText("",0);
        							 String[] text = textValue.split("\n");
        		                      for(int f = 0 , j = text.length ; f<j ; f++) {
        		                    	  run.setText(text[f]);
        		                    	  if(f+1!=j){
        			                    		// run.addCarriageReturn();//硬回车
        		                    		  run.addBreak();
        		                    	  }
        		                      }
        							 
        							// run.setText("中国\n标\r语/r3/n33",0);
        						}
        						
        					}
        				}
        			}
        		}
        
        		// ----------------------增加多行--------------------
        
         			List tables = document.getTables();
         		XWPFTable xTable = (XWPFTable) tables.get(0);
         		int  index= 7;
         		
                XWPFTableRow tableTemp = xTable.getRow(6);
                
                //XWPFTableRow tableOneRowTwo2 = xTable.insertNewTableRow(index);
                
         		for (HashMap<String, String> hm : tableDate) {
         			
		        //-------------------
         			XWPFTableRow targetRow = xTable.insertNewTableRow(index);
         			//复制行属性
         			targetRow.getCtRow().setTrPr(tableTemp.getCtRow().getTrPr());
         			List<XWPFTableCell> cellList = tableTemp.getTableCells();
         			if (null == cellList) {
         			   System.out.println("没有");
         			}
         			//复制列及其属性和内容
         			XWPFTableCell targetCell = null;
         			for(int t = 0 ; t<cellList.size();t++) {
         				XWPFTableCell sourceCell = cellList.get(t);
         				targetCell = targetRow.addNewTableCell();
         			    //列属性
         			    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
         			    //段落属性
         			    if(sourceCell.getParagraphs()!=null&&sourceCell.getParagraphs().size()>0){                     
         			    	targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
         		            if(sourceCell.getParagraphs().get(0).getRuns()!=null&&sourceCell.getParagraphs().get(0).getRuns().size()>0){
         		            	XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
         		    	        cellR.setText(hm.get("${"+t+"}"));
         		    	        cellR.setBold(sourceCell.getParagraphs().get(0).getRuns().get(0).isBold());
         		            }else{
         		            	
         		            	targetCell.setText(hm.get("${"+t+"}"));
         		            }
         		        }else{
         		        	
         		        	targetCell.setText(hm.get("${"+t+"}"));
         		        }
   
         			}	index++;
         		}
          		index--;
          		
          		
          	
          		 mergeCellsVertically(xTable, 0, 6, index);
        
        
        
        
        
        			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        	
        			document.write(ostream);
        			 return ostream;
        			
        

		} catch (Exception e) {
		
			 MantraLog.fileCreateAndWrite(e);
		}
		return null;
	}
	//水平合并单元格
	public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
		for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
			XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
			if (cellIndex == fromCell) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}
	//垂直合并单元格
	public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}
}
