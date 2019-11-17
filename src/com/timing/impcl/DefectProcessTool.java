/**
 * 
 */
package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.util.EString;
import com.yulongtao.util.YLUUID;

/**
 * @author tianshisheng
 *
 */
public final class DefectProcessTool {
	/**
	 * 生技部专业主管开口批准后，自动生成一张隐患等级评估单.
	 * 
	 * @param request
	 */
	public void generateHiddenDangerForm(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		MantraUtil mu = new MantraUtil();
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_QXBH,S_QXZT,S_JZ,S_QXLB,S_SBMC,S_SBBM,S_SSZY,S_XQDW,S_FXR,S_FXRBM,S_FXSJ," + 
		                 "S_FXRSSBM_NAME,S_FXRSSBM,S_FXRSSBZ,S_GZPPZ,S_GZXXSM,S_GZFZR,S_GZFZRBM,S_QXYY" + 
					     " from T_QXJL where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		        final String strCurDate = sdf.format(new Date());
				String sql_new = "INSERT INTO T_YHDJPG (S_ID,S_DJRQ,S_ZZ,S_RUN_ID,S_QXBH,S_QXZT,S_JZ,S_QXLB,S_SBMC," + 
				                 "S_SBBM,S_SSZY,S_XQDW,S_FXR,S_FXRBM,S_FXSJ,S_FXRSSBM_NAME,S_FXRSSBM,S_FXRSSBZ," + 
						         "S_GZPPZ,S_GZXXSM,S_GZFZR,S_GZFZRBM,S_QXYY) " + 
				                 " VALUES (" + 
						         "'" + EString.generId() + "'," +
						         "'" + strCurDate + "'," +
						         "'001017'," +
						         "'" + YLUUID.getUId() + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_QXBH").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_QXZT").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_JZ").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_QXLB").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_SBMC").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_SBBM").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_SSZY").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_XQDW").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_FXR").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_FXRBM").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_FXSJ").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_FXRSSBM_NAME").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_FXRSSBM").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_FXRSSBZ").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_GZPPZ").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_GZXXSM").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_GZFZR").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_GZFZRBM").value) + "'," +
						         "'" + String.valueOf(record.getFieldByName("S_QXYY").value) + "'" +
						         ")";
				dbf.sqlExe(sql_new, false);
				
//				mu.recordRel(T_YHDJPG__S_ZZ, "1516606174518", _planPk, "T_YHDJPG", "1516613463357", timeStamp+"","T_YHZL");
			}
		}catch(Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 
	 * @param _planPk
	 * @return
	 */
    public void dealResult(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
	    
	    DBFactory dbf = new DBFactory();
	     TableEx tableZi = null;
	     String sql = "select S_QXBH,S_SJJSSJ,S_CLQK,S_SGYSYJ,S_XKKSSJ,S_SGYSR,S_SGYSRBM,S_SGYSSJ from T_QXJL where S_ID = '" +primaryKey + "'";
	     
	     try{
	    	 tableZi = dbf.query(sql);
	    	 Record record = tableZi.getRecord(0);
	    	 
	    	 String sql_update = "update T_YHZL set " + 
	                 " S_SJJSSJ='" + String.valueOf(record.getFieldByName("S_SJJSSJ").value) + "'," +
	                 " S_CLQK='" + String.valueOf(record.getFieldByName("S_CLQK").value) + "'," +
	                 " S_SGYSYJ='" + String.valueOf(record.getFieldByName("S_SGYSYJ").value) + "'," +
	                 " S_XKKSSJ='" + String.valueOf(record.getFieldByName("S_XKKSSJ").value) + "'," +
	                 " S_SGYSR='" + String.valueOf(record.getFieldByName("S_SGYSR").value) + "'," +
	                 " S_SGYSRBM='" + String.valueOf(record.getFieldByName("S_SGYSRBM").value) + "'," +
	                 " S_SGYSSJ='" + String.valueOf(record.getFieldByName("S_SGYSSJ").value) + "'" + 
	                 " WHERE S_QXBH='" + String.valueOf(record.getFieldByName("S_QXBH").value) + "'";
	    	 
	    	 dbf.sqlExe(sql_update, false);
	     }catch (Exception e) {
	    	MantraLog.fileCreateAndWrite(e);
		} finally {
		    if (tableZi != null)
				tableZi.close();
			if (dbf != null)
				dbf.close();
		}
    }
}
