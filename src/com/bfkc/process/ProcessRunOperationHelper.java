/**
 * 
 */
package com.bfkc.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.util.EString;

/**
 * @author tianshisheng
 *
 */
public class ProcessRunOperationHelper {
    public static final String UPDATE_FIELD_START_TAG = "{updateFieldsStart:";
    public static final String UPDATE_FIELD_END_TAG = "updateFieldsEnd}";
	
	private static String[] arrVarNames=new String[]{"username","user","role","branchid","date","dataset","username","splitbranchid","usercount","userip","userlogindate","userlogindatehm"};
	private static String[] arrVarValues=new String[]{"SYS_STRCURUSERNAME","SYS_STRCURUSER","SYS_STRROLECODE","SYS_STRBRANCHID","SYS_CURDATE","DATASET","SYS_STRCURUSERNAME","SYS_BRANCHID_SPLIT","SYS_USER_COUNT","SYS_STRCURUSER_IP","SYS_USER_LOGIN_DATE","SYS_CURDATE"};
	
	/**
	 * 更新节点配置值
	 * @param _request
	 * @param _strkey
	 * @param _strField
	 * @param _strRunId
	 */
//	1500260373394$true$T_DQYZGZP.S_GLYQM,true,false,{user}|T_DQYZGZP.S_GLYQMSJ,true,false,{date}|T_DQYZGZP.S_ZHXGSJ,true,false,{date}|T_DQYZGZP.S_ZHXGR,true,false,{user}
	public void updateTabByFlowSet(HttpServletRequest _request,String _strkey,String _strField,String _strRunId,StringBuffer _sr){
		DBFactory dbf = new DBFactory();
		String strField = getNodeReplaceVal(_request, _strkey, _strField);
		Map<String,String> map = new HashMap<String, String>();
		Map<String,String> mapCon = new HashMap<String, String>();
		
		//用户手动选择节点, 出现分支情况
		String strCustomNodeId = _request.getParameter("NO_custom_node_id");
		List<String> updateColumns = new ArrayList<String>();
		Map<String, String> rollBackMap = new HashMap<>();

		if("".equals(_strField)){return;}
		String[] strArrayTable = strField.split("`");
		
		TableEx ex = null;
		String rollBackStr = null;
		try {
			ex = new TableEx("s_rollback", "t_sys_flow_log"," S_RUN_ID='" + _strRunId + "' order by s_aud_date desc");
			rollBackStr = getColString("s_rollback", ex.getRecord(0));
			if (rollBackStr != null && rollBackStr.length() > 0) {
				String rollBack = rollBackStr.substring(rollBackStr.indexOf(UPDATE_FIELD_START_TAG)+UPDATE_FIELD_START_TAG.length(),rollBackStr.indexOf(UPDATE_FIELD_END_TAG));
				String[] rollBackArray = rollBack.split("#");
				
				for (int k = 0; k < rollBackArray.length; k++) {
					String[] rollBackValue = rollBackArray[k].split("-");
					rollBackMap.put(rollBackValue[0], rollBackValue[1]);
				}
			}
		} catch (Exception e) {
		    //do nothing if exception occurs
		} finally {
			if (ex != null) {
				ex.close();
			}
		}
		
		for(int a=0,b=strArrayTable.length;a<b;a++){
			String strTemp = strArrayTable[a];
//			strTemp = strTemp.substring(strTemp.lastIndexOf("$")+1,strTemp.length());
			strTemp = strTemp.substring(strTemp.indexOf("e$")+2,strTemp.length());
			if("".equals(strTemp))
			{
				continue;
			}
			String[] strArrayField = strTemp.split("\\|");
		
			for(int i=0,j=strArrayField.length;i<j;i++){
				String[] strArrayItem  = strArrayField[i].split(",",-1);
				String strWhere = "";
				
				String strTemp1 = strArrayItem[0];
				int ind = strTemp1.indexOf(".");
				if(ind<0){
					continue;
				}
				String strCou = strTemp1.substring(ind+1,strTemp1.length());//字段名称
				String strTabName =strTemp1.substring(0, ind);//表名
				
//				String strTabName =strArrayItem[0].substring(0, strArrayItem[0].indexOf("."));//表名
//				String strCou = strArrayItem[0].substring(strArrayItem[0].indexOf("."),strArrayItem[0].length());//字段名称
				String strVal = strArrayItem[4];
				if(strVal==null||"".equals(strVal)){
					continue;
				}
			
				String strAuditState = _request.getParameter("NO_sys_flow_state");//99撤回0驳回1通过
				String strAuditState2 = (String) _request.getAttribute("NO_sys_flow_state");
				if ("99".equals(strAuditState2)) {
					if(rollBackMap.containsKey(strCou)){
						strVal = rollBackMap.get(strCou);
					}else{
						continue;
					}
				}
				else if("0".equals(strAuditState)){//驳回取值
					if(strVal.indexOf("{no:")>-1){
						strVal = strVal.substring(strVal.indexOf("{no:")+4,strVal.indexOf(":end}"));
					}else if(strVal.indexOf("{request:")>-1){
						strVal = strVal.replace("{request:", "");//strAuditComment {request:strAuditComment}
						strVal = strVal.replace("}", "");
						
						Object _obj = _request.getParameter(strVal+"");
						strVal = (_obj==null?"":_obj.toString());
						
					}else{
						continue;
					}
				}else{
					if(strVal.indexOf("{no:")>-1){
						String _strValTemp= strVal.substring(strVal.indexOf("{no:")+4,strVal.indexOf(":end}"));
						strVal = strVal.replace("{no:"+_strValTemp+":end}", "");
						if("".equals(strVal)){
							continue;
						}
					}
					
					if (strCustomNodeId != null && !"".equals(strCustomNodeId)
							&& strVal.indexOf("{branch:") > -1) {
						//{branch:11-GZPZT053#12-GZPZT054#13-GZPZT055}
						strVal = strVal.replace("{branch:", "");
						strVal = strVal.replace("}", "");
						
						String[] branches = strVal.split("#");
						Map<String, String> branchesMap = new HashMap<>();
						for (int k = 0; k < branches.length; k++) {
							String[] branchIds = branches[k].split("-");
							branchesMap.put(branchIds[0], branchIds[1]);
						}
						
						strVal = branchesMap.get(strCustomNodeId);
					}
					
					if(strVal.indexOf("{number:")>-1){
						strVal = strVal.replace("{", "");
						strVal = strVal.replace("}", "");
						String[] strArrayNum = strVal.split(":",-1);
						//操作票号根据专业区分
						String zy = _request.getParameter("T_CZPSC$S_ZY");
						String xlhId = strArrayNum[1];
						if (zy != null && !"".equals(zy)) {
							//{number:rl-1504603191000#qj-1504603191001:待定:待定}
							String[] czpZyIds = xlhId.split("#");
							Map<String, String> czpZyIdMap = new HashMap<>();
							for (int k = 0; k < czpZyIds.length; k++) {
								String[] czpXlhIds = czpZyIds[k].split("-");
								czpZyIdMap.put(czpXlhIds[0], czpXlhIds[1]);
							}
							
							xlhId = czpZyIdMap.get(zy);
						}
						
						if(!"".equals(strArrayNum[3])){//{number:143214235:待定:待定}
//							T_DQYZGZP.S_GZPBH,true,false,{number:1504603191000:待定:待定}
//							{number:1504603191000:待定:待定}
							strWhere =strWhere+ " and "+strCou +"=''";
							strVal = com.yulongtao.util.SerialUtil.getSerialNum(xlhId,_request);//TODO  序列号
						}else{//{number:143214235:待定:}
							strWhere =strWhere+  "and "+strCou+" like '%" +strArrayNum[2]+"'";
							strVal = com.yulongtao.util.SerialUtil.getSerialNum(xlhId,_request);//TODO  序列号
						}
						
						if (zy != null && !"".equals(zy)) {
							strVal = zy.toUpperCase() + strVal;
						}
						//MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "the BH number is:" + strVal);
					}else if(strVal.indexOf("{request:")>-1){
						strVal = strVal.replace("{request:", "");//strAuditComment {request:strAuditComment}
						strVal = strVal.replace("}", "");
						
						Object _obj = _request.getParameter(strVal+"");
//						new String(_obj.toString().getBytes("iso8859-1"),"UTF-8");
						String str = _obj.toString();
						try{
							strVal = (_obj==null?"":(new String(str.getBytes("iso8859-1"),"UTF-8")));
						}catch (Exception e) {
						    MantraLog.fileCreateAndWrite(e);
						}
					}else if(strVal.indexOf("{dataset:")>-1){
						strVal = strVal.replace("{dataset:", "");
						strVal = strVal.replace("}", "");
						String[] strValArry = strVal.split("\\|");
						for(int a1=0,b1=strValArry.length;a1<b1;a1++){
//							strValArry[a1];
						}
						//执行数据集
					}
				}
				mapCon.put(strTabName, strWhere);
				map.put(strTabName,(map.get(strTabName)==null?"":(map.get(strTabName).toString()+" , "))+strCou+" = '"+strVal+"' ");//字段名
				updateColumns.add(strCou);
			}
		}
		

//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "mapCon: " + mapCon);
//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "map: " + map);
		try {
			_sr.append(UPDATE_FIELD_START_TAG);
			for (String key : map.keySet()) {
			    	if(dbf==null){
			    	    dbf=new DBFactory();
			    	}
    			
			    //load the old data for the object table
		    	TableEx ex2 = null;
				try {
					ex2 = new TableEx("*", key," S_RUN_ID='" + _strRunId + "' "+mapCon.get(key));
					for (Iterator iterator = updateColumns.iterator(); iterator.hasNext();) {
						String colName = (String) iterator.next();
						String oldValue = getColString(colName, ex2.getRecord(0));
						if (oldValue != null) {
							_sr.append(colName + "-" + oldValue + "#");
						}
					}
				} catch (Exception e) {
				    //do nothing if exception occurs
				} finally {
					if (ex2 != null) {
						ex2.close();
					}
				}
			    
			    //	update T_DQYZGZP set S_GZPZT = 'GZPZT022' , S_GZXKRQM_NAME = '刘小锋' , S_GZXKRQM = 'liuxiaofeng' where S_RUN_ID='5NwQQiikQlq7Dk4PVReLoQ'
			    //MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "update " + key + " set " + map.get(key) + " where S_RUN_ID='" + _strRunId + "' "+mapCon.get(key));
				dbf.sqlExe("update " + key + " set " + map.get(key) + " where S_RUN_ID='" + _strRunId + "' "+mapCon.get(key), true);
				//dbf.sqlExe("update T_DQYZGZP set S_GZPZT = 'GZPZT022' , S_GZXKRQM_NAME = '刘小锋' , S_GZXKRQM = 'liuxiaofeng' where S_RUN_ID='5NwQQiikQlq7Dk4PVReLoQ'", true);
			}
			_sr.append(UPDATE_FIELD_END_TAG);
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
// 			String[] strArrayFlowLog22 = {"333","","",new Date()+"","","","updateTabByFlowSet",getErrorInfoFromException(e)};
// 			insertFlowLog("1", strArrayFlowLog22);
			e.printStackTrace();
		}finally{
			if(dbf!=null){dbf.close();}
		}
	}
	
	/**
	 * 操作流程
	 * @param _strFlowId 流程号
	 * @param _strFlowRunId 节点号
	 * @param _strVersion 版本号
	 * @param _strType 1:挂起 0:启用 3:作废
	 * @return
	 */
	public final static boolean processFlowHand(String _strFlowId,String _strFlowRunId,String _strVersion,String _strType){
		return ProcessRunOperationDao.processFlowHand(_strFlowId, _strFlowRunId, _strVersion, _strType);
	}
	
	/**
	 * 更新运行日志T_SYS_FLOW_RUN
	 * @param _strArrayFlowRun
	 * @param _strType 1:插入 2:更新 3:更新 4:更新父
	 */
	public final static void updateFlowRun(String[] _strArrayFlowRunVal,String _strType){
		ProcessRunOperationDao.updateFlowRun(_strArrayFlowRunVal, _strType);
	}
	
	/**
	 * 
	 * @param _strCol
	 * @param rd
	 * @return
	 */
	public String getColString(String _strCol,Record rd){
		return ProcessRunOperationDao.getColString(_strCol, rd);
	}
	
	/**
	 * 查询运行表T_SYS_FLOW_RUN
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @return
	 */
	public final static TableEx queryFlowRun(String _strFlowId,String _strVersion,String _strFlowRunId){
		return ProcessRunOperationDao.queryFlowRun(_strFlowId, _strVersion, _strFlowRunId);
	}
	
	/**
	 * 查询并列子流程
	 * @param strFlowRunId
	 * @param strFlowParentId
	 * @return
	 */
	public final static boolean queryFlowRunIsOverSameLevel(String strFlowRunId, String strFlowParentId) {
		return ProcessRunOperationDao.queryFlowRunIsOverSameLevel(strFlowRunId, strFlowParentId);
	}
	
	//数组定义:节点名称,节点替换值
	public String getNodeReplaceVal(HttpServletRequest _request,String _strkey,String _strField){
		HttpSession session = _request.getSession();
		int iLength=arrVarNames.length;
		Object strValue = "";
		for(int i=0;i<iLength;i++){
			if("date".equals(arrVarNames[i])){
				strValue=EString.getCurDateHH();
			}else {
				strValue=session.getAttribute(arrVarValues[i]);
			}
			strValue = (strValue==null||"".equals(strValue))?"":strValue;
			_strField=_strField.replace("{"+arrVarNames[i]+"}",strValue.toString());
		}
		return _strField;
	}
}
