/**
 * 
 */
package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bfkc.process.ProcessRunOperationDao;
import com.bfkc.process.ProcessRunOperationHelper;
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
		
		TableEx tableEx = null;
		TableEx tableExBar = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		Record recordBar = null;
		ProcessRunOperationHelper processRunOperationHelper = new ProcessRunOperationHelper();
		
		try {
			String sql = "select S_QXBH,S_QXZT,S_JZ,S_QXLB,S_SBMC,S_SBBM,S_SSZY,S_XQDW,S_FXR,S_FXRBM,S_FXSJ," + 
		                 "S_FXRSSBM_NAME,S_FXRSSBM,S_FXRSSBZ,S_GZPPZ,S_GZXXSM,S_GZFZR,S_GZFZRBM,S_QXYY,S_WHZGBM" + 
					     " from T_QXJL where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			tableExBar = dbf.query("select * from T_SYS_FLOW_PAR where S_SPAGECODE='1516606174518'");
			if (tableExBar.getRecordCount() > 0) {
				recordBar = tableExBar.getRecord(0);
			}
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat strSdfYmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        final String strCurDate = sdf.format(new Date());
		        String sId = EString.generId();
		        String runId = YLUUID.getUId();
		        String bmId = "001017";
		        
				String sql_new = "INSERT INTO T_YHDJPG (S_ID,S_DJRQ,S_ZZ,S_RUN_ID,S_QXBH,S_QXZT,S_JZ,S_QXLB,S_SBMC," + 
				                 "S_SBBM,S_SSZY,S_XQDW,S_FXR,S_FXRBM,S_FXSJ,S_FXRSSBM_NAME,S_FXRSSBM,S_FXRSSBZ," + 
						         "S_GZPPZ,S_GZXXSM,S_GZFZR,S_GZFZRBM,S_QXYY,S_YHFXRBM) " + 
				                 " VALUES (" + 
						         "'" + sId + "'," +
						         "'" + strCurDate + "'," +
						         "'" + bmId + "'," +
						         "'" + runId + "'," +
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
						         "'" + String.valueOf(record.getFieldByName("S_QXYY").value) +"'," +
						         "'" + String.valueOf(record.getFieldByName("S_WHZGBM").value) + "'" +
						         ")";
				dbf.sqlExe(sql_new, false);
				
				String strMsgContent = processRunOperationHelper.queryMsgTemplet("157767830186245574");
				if (StringUtils.isEmpty(strMsgContent)) {
					strMsgContent = "\u4e8b\u4ef6:${pagename},\u5185\u5bb9\uff1a${T_QXJL.S_GZXXSM} ${username} ${active} \u5355\u636e";
				}
				if (recordBar != null) {
					strMsgContent = strMsgContent.replace("${pagename}", String.valueOf(recordBar.getFieldByName("S_BZ").value));
				}
				else {
					strMsgContent = strMsgContent.replace("${pagename}", "\u9690\u60a3\u7b49\u7ea7\u8bc4\u4f30"); //隐患等级评估
				}
				strMsgContent = strMsgContent.replace("${username}", "\u7cfb\u7edf"); //系统
				strMsgContent = strMsgContent.replace("${active}", "\u751f\u6210"); //生成
				
				//replace the message content with the TABLE.COLUMN
				try {
					String regexStr = "\\$\\{[A-Za-z0-9\\._-]*\\}";
					Pattern pattern = Pattern.compile(regexStr);
					Matcher matcher = pattern.matcher(strMsgContent);
					
					while(matcher.find()) {
						String group = matcher.group();
						String tab = group.substring(2, group.length() - 1);

						String[] tabs = tab.split("\\.");
						String value = ProcessRunOperationDao.queryTableValue(tabs[0], tabs[1], "S_ID", primaryKey);
						strMsgContent = strMsgContent.replace(group, value);
					}
				} catch (Exception e) {
					//do nothing
				}
				
				insertMsg(runId, String.valueOf(record.getFieldByName("S_WHZGBM").value), strMsgContent, "1516606174518", sId, bmId,
						strSdfYmdHms.format(new Date()));
				
//				mu.recordRel(T_YHDJPG__S_ZZ, "1516606174518", _planPk, "T_YHDJPG", "1516613463357", timeStamp+"","T_YHZL");
			}
		}catch(Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(tableEx!=null)
				tableEx.close();
			if(tableExBar!=null)
				tableExBar.close();
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
    
    /**
     * 
     * @param runId
     * @param getUserCode
     * @param msgCong
     * @param pagecode
     * @param S_ID
     * @param org
     * @param date
     */
	private void insertMsg(String runId, String getUserCode, String msgCong, String pagecode, String S_ID, String org,
			String date) {
		String sql = " INSERT INTO T_MSG_RECORDS ( S_ID, S_YXID, S_JSR, S_XXLX, S_XXNR, S_PAGECODE, S_SID, S_BMID, S_CFLB, S_ZT, S_FSSJ ) "
				+ "VALUES" + " ( '"+EString.generId()+"', " + "'" + runId + "', " + "'" + getUserCode + "', " + "'system',"
				+ " '" + msgCong + "'," + " '" + pagecode + "', " + "'" + S_ID + "'," + " '" + org + "', " + "'6S', "
				+ "'0', " + "'" + date + "' );";
		DBFactory dbf = new DBFactory();
		try {
			dbf.sqlExe(sql, false);
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
		}
	}
}
