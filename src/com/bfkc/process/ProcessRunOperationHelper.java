package com.bfkc.process;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.timing.impcl.MantraLog;
import com.timing.util.MisLogger;
import com.yonyou.mis.util.ApplicationUtils;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.sys.WebCommand;
import com.yulongtao.util.EString;

/**
 * @author tianshisheng
 *
 */
public class ProcessRunOperationHelper {
	private static MisLogger logger = new MisLogger(ProcessRunOperationHelper.class);
	public static SimpleDateFormat strSdfYmd =  new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    public static final String UPDATE_FIELD_START_TAG = "{updateFieldsStart:";
    public static final String UPDATE_FIELD_END_TAG = "updateFieldsEnd}";
    public static final String UPDATE_VALUE_COLUMN_START_TAG = "{updateValueColumnStrStart:";
    public static final String UPDATE_VALUE_COLUMN_END_TAG = "updateValueColumnStrEnd}";
	
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
	public void updateTabByFlowSet(HttpServletRequest _request,String _strkey,String _strField,String _strFlowId,String _strRunId,StringBuffer _sr){
		DBFactory dbf = new DBFactory();
		String strField = getNodeReplaceVal(_request, _strkey, _strField);
		Map<String,String> map = new HashMap<String, String>();
		Map<String,String> mapCon = new HashMap<String, String>();
		
		//用户手动选择节点, 出现分支情况
		String strCustomNodeId = _request.getParameter("NO_custom_node_id");
		List<String> updateColumns = new ArrayList<String>();
		Set<String> updateValueColumns = new HashSet<String>();
		String updateValueColumnStr = null;
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
					if (rollBackValue.length < 2) {
						rollBackMap.put(rollBackValue[0], "");
					}
					else {
						rollBackMap.put(rollBackValue[0], rollBackValue[1]);
					}
				}
			}
		} catch (Exception e) {
		    logger.debug("ERR: ", e);
		} finally {
			if (ex != null) {
				ex.close();
			}
		}
		
		TableEx exRun = null;
		try {
			exRun = queryFlowRun(_strFlowId, _strRunId);
			Record record = exRun.getRecord(0);
			updateValueColumnStr = record.getFieldByName("S_UPVALUE_COLS").value.toString();
			//there's no fucking log...
			System.out.println((new Date()) + "; updateValueColumnStr: " + updateValueColumnStr + "; flow run ID: " + _strRunId);
			
			if (StringUtils.isNotEmpty(updateValueColumnStr)) {
				String[] columns = updateValueColumnStr.split("\\|");
				for (int k = 0; k < columns.length; k++) {
					updateValueColumns.add(columns[k]);
				}
			}
			else {
				updateValueColumnStr = "";
			}
		} catch (Exception e) {
		    logger.debug("ERR: ", e);
		} finally {
			if (exRun != null) {
				exRun.close();
			}
		}
		
	    logger.debug("==>>==>>==>>==>>==>>==>>==>>==>>字段回写开始");
	    logger.debug("_strkey=" + _strkey);
	    logger.debug("strField=" + strField);
		for(int a=0,b=strArrayTable.length;a<b;a++){
			String strTemp = strArrayTable[a];
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
				
				String strVal = strArrayItem[4];
				boolean once = false;
				
				if(strVal==null||"".equals(strVal)){
					continue;
				}
				
				if (strArrayItem.length > 5) {
					once = Boolean.parseBoolean(strArrayItem[5]);
				}
				
				//only set the value to the column one time
				if (once && updateValueColumns.contains(strCou)) {
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
					
					
					//new add 2019-9-11 {hqbh:001001}:{user}
					if (strVal.indexOf("{hqbh:") > -1) {
						strVal = strVal.replace("{hqbh:", "");
						strVal = strVal.replace("}", "");
						String[] arrStrBranchWrite = strVal.split(":");
						if(_request.getSession().getAttribute("SYS_STRBRANCHID").toString().equals(arrStrBranchWrite[0])) {
							if(arrStrBranchWrite.length>2)
								strVal=arrStrBranchWrite[1]+":"+arrStrBranchWrite[2];
							else
								strVal=arrStrBranchWrite[1];
						}else
							continue;
						
					}
					if (strVal.indexOf("{hqteam:") > -1) {
						strVal = strVal.replace("{hqteam:", "");
						strVal = strVal.replace("}", "");
						String[] arrStrBranchWrite = strVal.split(":");
						
						if(getTeamMsg("S_PEOPLE_CODE='"+_request.getSession().getAttribute("SYS_STRCURUSER")+"'",true).equals(arrStrBranchWrite[0])) {
							if(arrStrBranchWrite.length>2)
								strVal=arrStrBranchWrite[1]+":"+arrStrBranchWrite[2];
							else
								strVal=arrStrBranchWrite[1];
						}else
							continue;
						
					}
					//new add end
					
					
					if(strVal.indexOf("{number:")>-1){
						strVal = strVal.replace("{", "");
						strVal = strVal.replace("}", "");
						String[] strArrayNum = strVal.split(":",-1);
						String xlhId = strArrayNum[1];
						
						//操作票号根据专业区分
						String zy = _request.getParameter("T_CZPSC$S_ZY");
						String yhdj = _request.getParameter("T_YHDJPG$S_PG_PGDJ");
						
						if (zy != null && !"".equals(zy)) {
							//{number:rl-1504603191000#qj-1504603191001:待定:待定}
							try {
								String[] czpZyIds = xlhId.split("#");
								Map<String, String> czpZyIdMap = new HashMap<>();
								for (int k = 0; k < czpZyIds.length; k++) {
									String[] czpXlhIds = czpZyIds[k].split("-");
									czpZyIdMap.put(czpXlhIds[0], czpXlhIds[1]);
								}
								
								xlhId = czpZyIdMap.get(zy);
							} catch (Exception e) {
								//do nothing
							}
						}
						else if (yhdj != null && !"".equals(yhdj)) {
							//{number:YHDJ01-1504603191000#YHDJ02-1504603191001:待定:待定}
							try {
								String[] yhdjIds = xlhId.split("#");
								Map<String, String> yhdjIdMap = new HashMap<>();
								for (int k = 0; k < yhdjIds.length; k++) {
									String[] yhdjXlhIds = yhdjIds[k].split("-");
									yhdjIdMap.put(yhdjXlhIds[0], yhdjXlhIds[1]);
								}
								
								xlhId = yhdjIdMap.get(yhdj);
							} catch (Exception e) {
								//do nothing
							}
						}
						
						if(!"".equals(strArrayNum[3])){
							//{number:143214235:待定:待定}
							//T_DQYZGZP.S_GZPBH,true,false,{number:1504603191000:待定:待定}
							//{number:1504603191000:待定:待定}
							strWhere =strWhere+ " and "+strCou +"=''";
							strVal = com.yulongtao.util.MisSerialUtil.getSerialNum(xlhId,_request);//TODO  序列号
						}else{
							//{number:143214235:待定:}
							strWhere =strWhere+  "and "+strCou+" like '%" +strArrayNum[2]+"'";
							strVal = com.yulongtao.util.MisSerialUtil.getSerialNum(xlhId,_request);//TODO  序列号
						}
						
						if (zy != null && !"".equals(zy)) {
							strVal = zy.toUpperCase() + strVal;
						}
						//MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "the BH number is:" + strVal);
					}else if(strVal.indexOf("{request:")>-1){
						strVal = strVal.replace("{request:", "");//strAuditComment {request:strAuditComment}
						strVal = strVal.replace("}", "");
						
						String str = _request.getParameter(strVal);
						try{
							strVal = (str==null?"":(new String(str.getBytes("iso8859-1"),"UTF-8")));
						}catch (Exception e) {
						    MantraLog.fileCreateAndWrite(e);
						}
					}else if(strVal.indexOf("{dataset:")>-1){
                        strVal = strVal.replace("{dataset:", "");
                        strVal = strVal.replace("}", "").trim();
						//{dataset:098}
				        final WebCommand webCommad = new WebCommand();
						(webCommad.hashFieldValue = new HashMap()).put("SYS_TB", strTabName);
                        webCommad.hashFieldValue.put("SYS_FD", strCou);
                        webCommad.hashFieldValue.put("SYS_RID", _strRunId);
                        webCommad.doCommand(strVal, _request);
                        continue;
					}
				}
				
			    logger.debug("strCou=" + strCou + "; strVal=" + strVal);
				
				mapCon.put(strTabName, strWhere);
				map.put(strTabName,(map.get(strTabName)==null?"":(map.get(strTabName).toString()+" , "))+strCou+" = '"+strVal+"' ");//字段名
				updateColumns.add(strCou);
			}
		}

	    logger.debug("mapCon=" + mapCon);
	    logger.debug("map=" + map);
	    logger.debug("updateColumns=" + updateColumns);
		try {
			if (updateValueColumnStr == null) {
				updateValueColumnStr = "";
			}
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
						if (updateValueColumnStr.indexOf(colName) == -1) {
							updateValueColumnStr += "|" + colName;
						}
					}
				} catch (Exception e) {
				    //do nothing if exception occurs
				} finally {
					if (ex2 != null) {
						ex2.close();
					}
				}
			    
				dbf.sqlExe("update " + key + " set " + map.get(key) + " where S_RUN_ID='" + _strRunId + "' "+mapCon.get(key), true);
			}
			_sr.append(UPDATE_FIELD_END_TAG);

			//used for start process, because the flow run is not existing for now
			_sr.append(UPDATE_VALUE_COLUMN_START_TAG);
			_sr.append(updateValueColumnStr);
			_sr.append(UPDATE_VALUE_COLUMN_END_TAG);
			
			dbf.sqlExe("update T_SYS_FLOW_RUN set S_UPVALUE_COLS='" + updateValueColumnStr + "' where S_RUN_ID='" + _strRunId + "' and S_FLOW_ID='" +_strFlowId + "'", true);

		    logger.debug("==>>==>>==>>==>>==>>==>>==>>==>>字段回写结束");
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			if(dbf!=null){dbf.close();}
		}
	}
	
	/**
	 * 发送消息
	 * @param _strArrayMsgIds
	 * @param _strArrayUserIds
	 * @param _strType
	 * @param _strIsOver
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strNodeId
	 * @param request
	 * @param _strFlowType
	 */
	public String sendMsg(String _strArrayMsgIds,String _strArrayUserIds,String _strType,String _strIsOver,String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId,HttpServletRequest request,String _strFlowType,String _strPageCode,String _strTab){
		DBFactory dbf = new DBFactory();
		String strLoginUserName ="";
		String strPageCode  ="";
		Object strLoginBranchName  ="";
	
		if(request==null){
			strPageCode =_strPageCode;
			strLoginUserName="system";
			strLoginBranchName="系统";
		}else{
			strLoginUserName = request.getSession().getAttribute("SYS_STRCURUSERNAME").toString();//登录人名称
			strPageCode = request.getParameter("SPAGECODE");//页面代码
			strLoginBranchName = request.getSession().getAttribute("SYS_STRBRANCHNAME");//登录人部门名称
		}
		
	    //SYS_STRBRANCHID机构ＩＤ
		switch (_strType) {// 审核状态:0否1是2作废3提交4逾期5逾期作废6逾期退回7跳岗
			case "0":
				_strType="驳回";
				break;
			case "1":	
				_strType="审核通过";
				break;
			case "2":
				_strType="作废";
				break;
			case "3":
				_strType="提交";
				break;
			case "4":
				_strType="逾期审批";
				break;
			case "5":
				_strType="逾期作废";
				break;
			case "6":
				_strType="逾期退回";
				break;
			case "7":
				_strType="跳岗";
				break;
			case "8":
				_strType="结束";
				break;
		}
		if("1".equals(_strIsOver)){
			_strType="流程结束";
		}
	
		String strMsgContent = queryMsgTemplet(_strArrayMsgIds);
		TableEx exRun = queryFlowRun(_strFlowId, _strFlowRunId);
		exRun.close();

		String sid  = ""; // request.getParameter("S_ID");
		String bmid  = ""; // request.getParameter("BMID");
		String stype  = ""; // request.getParameter("STYPE");
		String djh = ""; //request.getParameter("DJH");
		String strNumberId = System.currentTimeMillis()+"";
		
		strMsgContent = strMsgContent.replace("${username}", strLoginUserName);//${username} ${active}单据,单据运行号:${numberid} ${branchname}
		strMsgContent = strMsgContent.replace("${active}", _strType);
		strMsgContent = strMsgContent.replace("${numberid}", _strFlowRunId);
		strMsgContent = strMsgContent.replace("${branchname}", (strLoginBranchName==null||"".equals(strLoginBranchName))?"":strLoginBranchName.toString());
		
		strPageCode = strPageCode==null?_strPageCode:strPageCode;
		if(strPageCode==null||"".equals(strPageCode)){
			String[] strArraySon = queryFlowMaiByFlowId(_strFlowId,"").split(",",-1);
			strPageCode=strArraySon[1];
		}
		
		String[] strArray = setMsgParVal(sid, bmid, stype, djh, strPageCode, _strFlowRunId,dbf,request,_strIsOver);
		sid = strArray[0];
		bmid = strArray[1];
		djh = strArray[2];
		String pkName = strArray[3];
		String pageName = strArray[4];
		strMsgContent = strMsgContent.replace("${pagename}", pageName);
		
		//replace the message content with the TABLE.COLUMN
		try {
			String regexStr = "\\$\\{[A-Za-z0-9\\._-]*\\}";
			Pattern pattern = Pattern.compile(regexStr);
			Matcher matcher = pattern.matcher(strMsgContent);
			
			while(matcher.find()) {
				String group = matcher.group();
				String tab = group.substring(2, group.length() - 1);

				String[] tabs = tab.split("\\.");
				String value = ProcessRunOperationDao.queryTableValue(tabs[0], tabs[1], pkName, sid);
				strMsgContent = strMsgContent.replace(group, value);
			}
		} catch (Exception e) {
			//do nothing
		}

		String[] strArrayValues={strPageCode,_strVersion,"system",strSdfYmdHms.format(new Date()),strNumberId,_strNodeId,_strArrayUserIds,_strFlowId,"0",_strArrayMsgIds,"system",strMsgContent,_strFlowRunId,"0",_strFlowType,sid,bmid,stype,djh};
		updateMsgs("1",strArrayValues);

		dbf.close();
		return strPageCode;
	}
	
	/**
	 * 操作流程
	 * @param _strFlowId 流程号
	 * @param _strFlowRunId 节点号
	 * @param _strVersion 版本号
	 * @param _strType 1:挂起 0:启用 3:作废
	 * @return
	 */
	public boolean processFlowHand(String _strFlowId,String _strFlowRunId,String _strVersion,String _strType){
		return ProcessRunOperationDao.processFlowHand(_strFlowId, _strFlowRunId, _strType);
	}
	
	/**
	 * 更新运行日志T_SYS_FLOW_RUN
	 * @param _strArrayFlowRun
	 * @param _strType 1:插入 2:更新 3:更新 4:更新父
	 */
	public void updateFlowRun(String[] _strArrayFlowRunVal,String _strType){
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
	public TableEx queryFlowRun(String _strFlowId,String _strFlowRunId){
		return ProcessRunOperationDao.queryFlowRun(_strFlowId, _strFlowRunId);
	}
	
	/**
	 * 查询并列子流程
	 * @param strFlowRunId
	 * @param strFlowParentId
	 * @return
	 */
	public boolean queryFlowRunIsOverSameLevel(String strFlowRunId, String strFlowParentId) {
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
	
	/**
	 * 查询消息模版
	 * @param _strMsgIds
	 * @return
	 */
	public String queryMsgTemplet(String _strMsgIds){
		TableEx ex = null;
		String strMsgTem = "";
		try {
			ex = new TableEx("S_MBNR", "T_XXGL"," 1=1 and S_ID ='"+_strMsgIds+"'");
			if(ex.getRecordCount()>0){
				strMsgTem= ex.getRecord(0).getFieldByName("S_MBNR").value.toString();
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return strMsgTem;
	}
	
	/**
	 * 根据流程ID查询流程主表最大流程版本
	 * @param _strFlowId
	 * @param _strOrgId
	 * @return
	 */
	public String queryFlowMaiByFlowId(String _strFlowId,String _strOrgId){
		String strVersion = "";
		TableEx exFlowMain =null;
		try {
			exFlowMain = queryFlowMainTableEx(_strFlowId, _strOrgId);
			Record rd = exFlowMain.getRecord(0);
			strVersion = getColString("S_AUDIT_VERSION", rd)+","+getColString("S_FORMS", rd);
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exFlowMain.close();
		}
		return strVersion;
	}
	
	public TableEx queryFlowMainTableEx(String _strFlowId,String _strOrgId){
		TableEx exFlowMain =null;
		try {
			exFlowMain = new TableEx("S_AUDIT_VERSION,S_FORMS", "T_SYS_FLOW_MAIN", "S_FLOWID='"+_strFlowId+"' and I_FLOWSTATUS='0' "+((_strOrgId==null||"".equals(_strOrgId))?"":(" and S_ORG_ID='"+_strOrgId+"'"))+" ORDER BY S_AUDIT_VERSION DESC");
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			if(exFlowMain!=null){exFlowMain.close();}
			e.printStackTrace();
		}
		return exFlowMain;
	}
	
	public void updateMsgs(String _strType,String[] _strArrayValues){
		DBFactory dbf = new DBFactory();
		try {
			if("1".equals(_strType)){
				
				String strTabCol ="(S_PAGECODE,S_BBH,S_FSR,S_FSSJ,S_ID,S_JDID,S_JSR,S_LCID,S_SCBS,S_XXID,S_XXLX,S_XXNR,S_YXID,S_ZT,S_FLOW_TYPE,S_SID,S_BMID,S_TYPE,S_DJH)";
				_strArrayValues = ApplicationUtils.arrayAddSingleQuotes(_strArrayValues);
				String strTabVal = Arrays.toString(_strArrayValues);
				strTabVal = strTabVal.substring(1,strTabVal.length()-1);
				
				dbf.sqlExe("insert into T_MSG_RECORDS "+strTabCol+" values("+strTabVal+")", true);
			}else{
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			dbf.close();
		}
	}
	
	/**
	 * 
	 * @param _strSid
	 * @param _strBmid
	 * @param _strType
	 * @param _strDjh
	 * @param _strPageCode
	 * @param _strRunId
	 * @param _dbf
	 * @param request
	 * @param _strIsOver
	 * @return
	 */
	private String[] setMsgParVal(String _strSid,String _strBmid,String _strType,String _strDjh,String _strPageCode,String _strRunId,DBFactory _dbf,HttpServletRequest request,String _strIsOver){
		TableEx ex = null;
		TableEx exForm = null;
		String strTableName = "";
		String[] strArray=new String[5];
		try{
			ex = _dbf.query("select * from T_SYS_FLOW_PAR where S_SPAGECODE='"+_strPageCode+"'");
			Record rd = null;
			String strSidF = "";
			String strTableNameF="";
			String strDjhF="";
			String strBz="";
			String strBzF="";
			String strClass="";
			String strMethod="";
			String strRelation="";
			if(ex.getRecordCount()>0){
				rd = ex.getRecord(0);
				_strSid = getColString("S_SID", rd);
				_strBmid = getColString("S_ZZ", rd);
				_strDjh = getColString("S_DJH", rd);
				strTableName = getColString("S_TABLE", rd);
				strSidF = getColString("S_IDF", rd);
				strTableNameF = getColString("S_TABLEF", rd);
				strDjhF = getColString("S_DJHF", rd);
				strBz = getColString("S_BZ", rd);
				strBzF = getColString("S_BZF", rd);
				strClass = getColString("S_CLASSPATH", rd);
				strMethod = getColString("S_METHOD", rd);
				strRelation = getColString("S_OTHER", rd);
			}
			String strAuditState = "1";//审核状态 审核状态:0驳回1通过2作废3提交4逾期5逾期作废6逾期退回
			if(request!=null){
				strAuditState = request.getParameter("NO_sys_flow_state");//审核状态 审核状态:0驳回1通过2作废3提交4逾期5逾期作废6逾期退回
			}
			
			if(!"".equals(strTableName)){
				if(!"".equals(strTableNameF)){
					String strCol = _strSid+","+_strBmid+","+_strDjh+","+strTableNameF+"."+strSidF+" AS 'sidf',"+strTableNameF+"."+strDjhF+" AS 'djhf' ";
					exForm = _dbf.query("select "+strCol+" from "+ strTableName+","+strTableNameF+" where S_RUN_ID='"+_strRunId+"' and "+strRelation);
				}else{
					exForm = new TableEx("*",strTableName, "S_RUN_ID='"+_strRunId+"'");
				}
				if(exForm.getRecordCount()>0){
					Record rd1 = exForm.getRecord(0);
					strArray[0] = getColString(_strSid, rd1);
					strArray[1] = getColString(_strBmid, rd1);
					strArray[2] = getColString(_strDjh, rd1);
					strArray[3] = _strSid;
					strArray[4] = strBz;
				}
			}
			if(!"".equals(strMethod)&&!"".equals(strClass)&&"1".equals(_strIsOver)&&request!=null){
				reflectMothedInvoke(strClass, strMethod, request);
			}
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			if(ex!=null){ex.close();}
			if(exForm!=null){exForm.close();}
			e.printStackTrace();
		}finally{
			if(ex!=null){ex.close();}
			if(exForm!=null){exForm.close();}
			return strArray;
		}
	}
	
	/**
	 * 
	 * @param strClassName
	 * @param strMethodName
	 * @param obj
	 */
	public void reflectMothedInvoke(String strClassName,String strMethodName,Object... obj){
        try {
			Class<?> class1 = null;
			class1 = Class.forName(strClassName);
			Method[] methods = class1.getDeclaredMethods();  
			Class<?>[] class2 =null;
			for (int i = 0; i < methods.length; i++) {  
				if(strMethodName.equals(methods[i].getName())){
					int l = methods[i].getParameterTypes().length;
					class2 = new Class<?>[l] ;
					for(int j=0;j<l;j++){
						class2[j] = methods[i].getParameterTypes()[j];
					}
				}
			} 
			Method method = class1.getMethod(strMethodName,class2); 
			method.invoke(class1.newInstance(),obj);  
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * 
	 * @param S_RUN_ID
	 */
	public void delMsg( String S_RUN_ID) {
		ProcessRunOperationDao.DelMsg(S_RUN_ID);
	}
    
	public static void main(String[] args) {
		HttpServletRequest req = null;
		ProcessRunOperationHelper helper = new ProcessRunOperationHelper();
//		helper.reflectMothedInvoke("com.bfkc.process.ProcessRunOperation","getTest",req);
		
		String _sb = ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_START_TAG + "1234567890" + ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_END_TAG;
		String updateValueColumnStr = _sb.substring(_sb.indexOf(ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_START_TAG) + ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_START_TAG.length(), _sb.indexOf(ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_END_TAG));
		System.out.println(updateValueColumnStr);
	}
	//new add 2019-9-11
	protected String getTeamMsg(String _strCondition,boolean _bIsGetTeam) {
    	String vResult="";
    	TableEx tableEx=null;
    	try {
			tableEx=new TableEx("S_USER_CODE,S_PEOPLE_CODE","T_BZRYB",_strCondition);
			int iRecordCount=tableEx.getRecordCount();
			if(iRecordCount>0) {
				if(_bIsGetTeam)
					return tableEx.getRecord(0).getFieldByName("S_USER_CODE").value.toString();
				else {
					String strSplit="";
					Record record;
					for(int i=0;i<iRecordCount;i++) {
						record=tableEx.getRecord(i);
						vResult+=strSplit+record.getFieldByName("S_PEOPLE_CODE").value;
						strSplit=",";
					}
				}
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(tableEx!=null)
				tableEx.close();
		}
    	return vResult;
    }
}
