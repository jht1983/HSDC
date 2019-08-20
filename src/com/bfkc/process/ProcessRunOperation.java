package com.bfkc.process;
	
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.timing.impcl.MantraLog;
import com.yonyou.mis.util.ApplicationUtils;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

	
public class ProcessRunOperation {
	public static SimpleDateFormat strSdfYmd =  new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private ProcessRunOperationHelper helper = new ProcessRunOperationHelper();
	
//	SYS_STRCURUSER //用户代码
//	SYS_STRCURUSERNAME //用户名称
//	SYS_STRBRANCHID //组织机构
//	SYS_BRANCHID_SPLIT //跨部门
//	SYS_STRROLECODE //用户角色
//	SYS_USER_COUNT //登录次数
//	SYS_STRCURUSER_IP //登录IP
//	SYS_USER_LOGIN_DATE //登录日期 年-月-日
//	SYS_CURDATE //登录时间 时:分
	/**
	 * 替换session,request值
	 * @param _request
	 * @param _str
	 */
	public String replaceRequestVal(HttpServletRequest _request,String _str){
		HttpSession session = _request.getSession();
		Object strVal = session.getAttribute(_str);
		
		return strVal==null?_str:strVal.toString();
	}
	
	public StringBuffer sb = new StringBuffer();
	
	public Boolean processStartNoAudit(String _strFlowId,String _strRunId,String _strVersion,String _strPageCode){
		TableEx tableEx =null;
		TableEx tableEx1 =null;
		Boolean b = true;
		String strFlowRunId = "";
		String strFlowId ="";
		String strVersion="";
		try{
			/**接收数据*/
			String strStartUser = "system";//发起人
			String strStartUserRole = "";//发起人角色
			String strStartUserBranch = "";//发起人组织
			String strPageCode=_strPageCode;
			
			strFlowId = _strFlowId;//流程ID
			strVersion = _strVersion;//版本号
			strFlowRunId = _strRunId;//运行
			String strFlowType = "1";//0:子流程 1:主流程 默认主流程
			
			/**0 查询流程节点*/
			String strStartNode = "";//发起节点NODE
			String strStartNodeBak = "";//发起节点NODE
			String strEndNodes = ",";//所有结束节点
			String strAuditMsgs = "";//所有消息模版
			String strTab="";//表名
			tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
			int iCount = tableEx.getRecordCount();
			if(iCount<1){
				return false;
			}
			
			/**1 查找开始节点*/
			Record record = null;
			for(int i=0;i<iCount;i++){
				record =  tableEx.getRecord(i);//1 动作 3 开始   2网关  4结束
				if("3".equals(helper.getColString("I_TYPE", record))){//开始数量
					strAuditMsgs = helper.getColString("S_AUDIT_TZXX", record);
					strStartNodeBak = helper.getColString("I_NODE_ID", record);
					//多个开始节点判断是否发节点
					if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,helper.getColString("S_AUDIT_BRANCH", record),helper.getColString("S_AUDIT_ROLE", record),helper.getColString("S_AUDIT_USER", record))==true){
						strStartNode = strStartNodeBak;
						strTab = helper.getColString("S_TAB", record);
					}else if(querySqRole(helper.getColString("S_AUDIT_SQRYATTR", record),helper.getColString("S_AUDIT_SQRY", record), null, strFlowRunId,strStartUserRole)){
						strStartNode = strStartNodeBak;
						strTab = helper.getColString("S_TAB", record);
					}
				}else if("4".equals(helper.getColString("I_TYPE", record))){
					strEndNodes=strEndNodes+helper.getColString("I_NODE_ID", record)+",";
				}
			}
			strStartNode= ("".equals(strStartNode)?strStartNodeBak:strStartNode);//找不到开始节点则任意一个
			if("".equals(strStartNode)){
				return false;
			}
			String strNodeIdNow = strStartNode;//找不到节点,跑出异常
			
			/**2 开始节点赋值*/
			String strAuditArrayyq=",";//逾期
			String strAuditNodes=strStartNode;//所有节点
			String strAuditState="3";//运行状态 3：提交
			String strNodeIdNext="";//运行节点
			String strNextAuditUser="";//节点审批人
			String strAuditOther =",,,,,,,";//其他
			String strNownewDate = strSdfYmdHms.format(new Date());//发起日期
			int strNextAuditUserIndex =1;//下一审批节点索引,发起人默认为1
			String strAuditUsers = strStartUser;//所有审批人
			String strSonFlow = "";
			String strIsOver = "0";//是否结束
			Record rd = null;//获取下一节点对象
			String strEndFlag = "";
			String strAudSel = "";//自选流程节点
			String strFlowPj = "";//附加票据
			/**3 循环拼接参数*/
			while(!"4".equals(strEndFlag)){
				rd = getNextNodeByCondition(null,strStartNode,tableEx,"2",strFlowRunId);
				if(rd==null){break;}
				strEndFlag = helper.getColString("I_TYPE", rd);
				String strCustomNodeIds = helper.getColString("S_AUDIT_SEL", rd);//手动选择节点
				if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//网关判断是否手动选择节点
					strAudSel = strAudSel+strCustomNodeIds;
					break;					
				}
				strAudSel = strAudSel+"|"+"";
				String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,helper.getColString("S_AUDIT_BRANCH", rd),helper.getColString("S_AUDIT_ROLE", rd),helper.getColString("S_AUDIT_USER", rd),helper.getColString("S_AUDIT_SQRYATTR", rd),helper.getColString("S_AUDIT_SQRY", rd),null,rd,strFlowRunId);
				if("5".equals(strEndFlag)){//子流程,存储子流程流程号/版本号运行号/表单ID
					strNodeAudit = "S";
				}
				strSonFlow = (strSonFlow+"|")+helper.getColString("S_FLOW_SON", rd);
				strAuditArrayyq =(strAuditArrayyq+"|") +helper.getColString("S_AUDIT_YQTS", rd)+","+helper.getColString("S_AUDIT_YQTSCL", rd);//所有逾期  所有逾期操作
				strAuditMsgs =(strAuditMsgs+"|")+helper.getColString("S_AUDIT_TZXX", rd);//所有消息模版
				strAuditNodes = (strAuditNodes+"|")+helper.getColString("I_NODE_ID", rd);
				strFlowPj =(strFlowPj+"|")+helper.getColString("S_AUDIT_FSPJ", rd);//附加票据
				//是否跳过 所有审批驳回 所有审批驳回处理 子流程
				//类型,值5,通过人数6,驳回人数7|
				strAuditOther =(strAuditOther+"|") +helper.getColString("S_AUDIT_TG", rd)+","+helper.getColString("S_AUDIT_THJD", rd)+","+helper.getColString("S_AUDIT_THJDZD", rd)+","+helper.getColString("S_TZLC", rd)+","+helper.getColString("S_AUDIT_PREEMPTION", rd)+","+helper.getColString("S_AUD_VAL", rd)+","+",";
				strStartNode = helper.getColString("S_CHILD_ID", rd);
				if("".equals(strNodeAudit)){
					strNodeAudit = ("1".equals(helper.getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//是否跳岗
				}
				strAuditUsers = (strAuditUsers+"|")+strNodeAudit;
			}
			
			/**4 跳岗 1:是*/
			strNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strAuditOther,strNextAuditUserIndex,strStartUser,strAuditMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strAuditNodes.split("\\|",-1)[strNextAuditUserIndex]);
			strNextAuditUser =strAuditUsers.split("\\|",-1)[strNextAuditUserIndex];
			strNodeIdNext =strAuditNodes.split("\\|",-1)[strNextAuditUserIndex];
			/**判断当前节点审批人是否表单取值*/
			strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowId, strVersion, strNodeIdNext);
			
			String[] strArryAuditUsers = strAuditUsers.split("\\|",-1);
			strArryAuditUsers[strNextAuditUserIndex]=strNextAuditUser;
			strAuditUsers = getStringArryToString(strArryAuditUsers);
			/**5 插入运行表*/
			String[] strArrayFlowRun = {strFlowId,strFlowRunId,strNodeIdNext,strVersion,strNownewDate,strStartUser,strNextAuditUser,strNextAuditUserIndex+"",strAuditMsgs,strStartUserBranch,strAuditArrayyq,strAuditUsers,strAuditNodes,strIsOver,strAuditOther,strAudSel,strEndNodes,strSonFlow,strFlowType,"",strFlowPj,strTab};
			helper.updateFlowRun(strArrayFlowRun,"1");
		
			String strDate = strSdfYmdHms.format(new Date());
			String  strAuditComment = "";
			/**插入流程日志*/
			String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment,""};
			insertFlowLog("1", strArrayFlowLog);
			/**发送消息*/
			helper.sendMsg(strAuditMsgs.split("\\|",-1)[0],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,null,strFlowType,strPageCode,strTab);
		}catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			b=false;
			e.printStackTrace();
		}finally{
			tableEx.close();
		}
		return b;
	}
	
	/**
	 * 发起节点
	 * @param request
	 * @return
	 */
	public Boolean processStart(HttpServletRequest request,StringBuffer _sb,String _strSonFlowId,String _strFlowParentId){
		TableEx tableEx =null;
		TableEx tableEx1 =null;
		Boolean b = true;
		String strFlowRunId = "";
		String strFlowId ="";
		String strVersion="";
		try{
			/**接收数据*/
			String strStartUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//发起人
			String strStartUserRole = request.getSession().getAttribute("SYS_STRROLECODE").toString();//发起人角色
			String strStartUserBranch = request.getSession().getAttribute("SYS_STRBRANCHID").toString();//发起人组织
			
			strFlowId = request.getParameter("NO_sys_flow_id");//流程ID
			strVersion = request.getParameter("NO_sys_flow_Ver");//版本号
			 strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//运行
			String strFlowType = request.getParameter("NO_sys_S_flow_type");//0:子流程 1:主流程 默认主流程
			
			//删除通知消息
			helper.delMsg( strFlowRunId);
			
			/**子流程*/
			strFlowType = "1";
			
			String strPageCode = "";//页面代码
			if(_strSonFlowId!=null&&!"".equals(_strSonFlowId)){
				strFlowId = _strSonFlowId;
				String[] strArraySon = helper.queryFlowMaiByFlowId(_strSonFlowId,"").split(",",-1);
				strVersion = strArraySon[0];
				strPageCode=strArraySon[1];
				strFlowType = "0";
			}

			/**0 查询流程节点*/
			String strStartNode = "";//发起节点NODE
			String strStartNodeBak = "";//发起节点NODE
			String strEndNodes = ",";//所有结束节点
			String strAuditMsgs = "";//所有消息模版
			String strTab="";//表名
			tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
			int iCount = tableEx.getRecordCount();
			if(iCount<1){
				return false;
			}
			/**1 查找开始节点*/
			Record record = null;
			for(int i=0;i<iCount;i++){
				record =  tableEx.getRecord(i);//1 动作 3 开始   2网关  4结束
				if("3".equals(helper.getColString("I_TYPE", record))){//开始数量
					strAuditMsgs = helper.getColString("S_AUDIT_TZXX", record);
					strStartNodeBak = helper.getColString("I_NODE_ID", record);
					//多个开始节点判断是否发节点
					if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,helper.getColString("S_AUDIT_BRANCH", record),helper.getColString("S_AUDIT_ROLE", record),helper.getColString("S_AUDIT_USER", record))==true){
						strStartNode = strStartNodeBak;
						strTab = helper.getColString("S_TAB", record);
						if(strPageCode==null||"".equals(strPageCode)){
							strPageCode = helper.getColString("S_PAGECODE", record);
						}
					}else if(querySqRole(helper.getColString("S_AUDIT_SQRYATTR", record),helper.getColString("S_AUDIT_SQRY", record), request, strFlowRunId,strStartUserRole)){
						strStartNode = strStartNodeBak;
						strTab = helper.getColString("S_TAB", record);
						if(strPageCode==null||"".equals(strPageCode)){
							strPageCode = helper.getColString("S_PAGECODE", record);
						}
					}
				}else if("4".equals(helper.getColString("I_TYPE", record))){
					strEndNodes=strEndNodes+helper.getColString("I_NODE_ID", record)+",";
				}
			}
			strStartNode= ("".equals(strStartNode)?strStartNodeBak:strStartNode);//找不到开始节点则任意一个
			if("".equals(strStartNode)){
				_sb.append("当前角色无权限发起流程");
				return false;
			}
			String strNodeIdNow = strStartNode;//找不到节点,跑出异常
			
			/**提交表单更新数据*/
			tableEx1 = queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow);
			String strClassName = helper.getColString("S_AUDIT_PAGENAME", tableEx1.getRecord(0));
			String strMethodName = helper.getColString("S_AUDIT_CLASSNAME", tableEx1.getRecord(0));
			String strField = helper.getColString("S_AUDIT_TABLECONTROL", tableEx1.getRecord(0));
			 _sb.append(strField);
			if(strField!=null&&!"".equals(strField)){
				helper.updateTabByFlowSet(request, "", strField, strFlowId, strFlowRunId,_sb);//strNodeIdNow
			}
			
			/**2 开始节点赋值*/
			String strAuditArrayyq=",";//逾期
			String strAuditNodes=strStartNode;//所有节点
			String strAuditState="3";//运行状态 3：提交
			String strNodeIdNext="";//运行节点
			String strNextAuditUser="";//节点审批人
			String strAuditOther =",,,,,,,";//其他
			String strNownewDate = strSdfYmdHms.format(new Date());//发起日期
			int strNextAuditUserIndex =1;//下一审批节点索引,发起人默认为1
			String strAuditUsers = strStartUser;//所有审批人
			String strSonFlow = "";
			String strIsOver = "0";//是否结束
			Record rd = null;//获取下一节点对象
			String strEndFlag = "";
			String strAudSel = "";//自选流程节点
			String strFlowPj = "";//附加票据
			/**3 循环拼接参数*/
			while(!"4".equals(strEndFlag)){
				rd = getNextNodeByCondition(request,strStartNode,tableEx,"1",strFlowRunId);
				if(rd==null){break;}
				strEndFlag = helper.getColString("I_TYPE", rd);
				String strCustomNodeIds = helper.getColString("S_AUDIT_SEL", rd);//手动选择节点
				if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//网关判断是否手动选择节点
					strAudSel = strAudSel+strCustomNodeIds;
					break;					
				}
				strAudSel = strAudSel+"|"+"";
				String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,helper.getColString("S_AUDIT_BRANCH", rd),helper.getColString("S_AUDIT_ROLE", rd),helper.getColString("S_AUDIT_USER", rd),helper.getColString("S_AUDIT_SQRYATTR", rd),helper.getColString("S_AUDIT_SQRY", rd),request,rd,strFlowRunId);
				if("5".equals(strEndFlag)){//子流程,存储子流程流程号/版本号运行号/表单ID
					strNodeAudit = "S";
				}
				strSonFlow = (strSonFlow+"|")+helper.getColString("S_FLOW_SON", rd);
				strAuditArrayyq =(strAuditArrayyq+"|") +helper.getColString("S_AUDIT_YQTS", rd)+","+helper.getColString("S_AUDIT_YQTSCL", rd);//所有逾期  所有逾期操作
				strAuditMsgs =(strAuditMsgs+"|")+helper.getColString("S_AUDIT_TZXX", rd);//所有消息模版
				strAuditNodes = (strAuditNodes+"|")+helper.getColString("I_NODE_ID", rd);
				strFlowPj =(strFlowPj+"|")+helper.getColString("S_AUDIT_FSPJ", rd);//附加票据
				//是否跳过 所有审批驳回 所有审批驳回处理 子流程
				//类型,值5,通过人数6,驳回人数7|
				strAuditOther =(strAuditOther+"|") +helper.getColString("S_AUDIT_TG", rd)+","+helper.getColString("S_AUDIT_THJD", rd)+","+helper.getColString("S_AUDIT_THJDZD", rd)+","+helper.getColString("S_TZLC", rd)+","+helper.getColString("S_AUDIT_PREEMPTION", rd)+","+helper.getColString("S_AUD_VAL", rd)+","+",";
				strStartNode = helper.getColString("S_CHILD_ID", rd);
				if("".equals(strNodeAudit)){
					strNodeAudit = ("1".equals(helper.getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//是否跳岗
				}
				strAuditUsers = (strAuditUsers+"|")+strNodeAudit;
			}
			
			_sb.append("sql:"+sb.toString());
			/**4 跳岗 1:是*/
			strNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strAuditOther,strNextAuditUserIndex,strStartUser,strAuditMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strAuditNodes.split("\\|",-1)[strNextAuditUserIndex]);
			strNextAuditUser =strAuditUsers.split("\\|",-1)[strNextAuditUserIndex];
			strNodeIdNext =strAuditNodes.split("\\|",-1)[strNextAuditUserIndex];
			/**判断当前节点审批人是否表单取值*/
			strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowId, strVersion, strNodeIdNext);
			
			String[] strArryAuditUsers = strAuditUsers.split("\\|",-1);
			strArryAuditUsers[strNextAuditUserIndex]=strNextAuditUser;
			strAuditUsers = getStringArryToString(strArryAuditUsers);
			/**5 插入运行表*/
			String[] strArrayFlowRun = {strFlowId,strFlowRunId,strNodeIdNext,strVersion,strNownewDate,strStartUser,strNextAuditUser,strNextAuditUserIndex+"",strAuditMsgs,strStartUserBranch,strAuditArrayyq,strAuditUsers,strAuditNodes,strIsOver,strAuditOther,strAudSel,strEndNodes,strSonFlow,strFlowType,_strFlowParentId,strFlowPj,strTab};
			helper.updateFlowRun(strArrayFlowRun,"1");
			String updateValueColumnStr = _sb.substring(_sb.indexOf(ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_START_TAG) + ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_START_TAG.length(), _sb.indexOf(ProcessRunOperationHelper.UPDATE_VALUE_COLUMN_END_TAG));
			ProcessRunOperationDao.updateValueColumns(updateValueColumnStr, strFlowRunId, strFlowId);
			
			String strDate = strSdfYmdHms.format(new Date());
			String  strAuditComment = "";
			/**插入流程日志*/
			String updateMessage = _sb.substring(_sb.indexOf(ProcessRunOperationHelper.UPDATE_FIELD_START_TAG), _sb.indexOf(ProcessRunOperationHelper.UPDATE_FIELD_END_TAG) + ProcessRunOperationHelper.UPDATE_FIELD_END_TAG.length());
			String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment, updateMessage};
			insertFlowLog("1", strArrayFlowLog);
			/**发送消息*/
			helper.sendMsg(strAuditMsgs.split("\\|",-1)[0],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowType,strPageCode,strTab);

			if(strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
				helper.reflectMothedInvoke(strClassName, strMethodName, request);
			}
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			b=false;
			_sb.append(e);
			e.printStackTrace();
	    }finally{
			if (tableEx != null) {
				tableEx.close();
			}
			if (tableEx1 != null) {
				tableEx1.close();
			}
		}

	    return b;
	}
	
	/**
	 * 查询当前节点审批人是否取自表单
	 * @param _strNextAuditUser
	 * @param _strFlowRunId
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strNodeIdNext
	 * @return
	 */
	private String queryAuditPersonIsColumn(String _strNextAuditUser,String _strFlowRunId,String _strFlowId,String _strVersion,String _strNodeIdNext){
		TableEx exFlowNode = null;
		String strAuditEx = "";
		try {
			exFlowNode = queryFlowNodeInfo(_strFlowId, _strVersion, _strNodeIdNext);
			String strExCon="";
			strExCon = helper.getColString("S_AUDIT_THDJR", exFlowNode.getRecord(0));
			if(strExCon!=null&&!"".equals(strExCon)){
				strAuditEx = queryBusinessDataByCon(strExCon, "",_strFlowRunId);
			}
			if(strAuditEx!=null&&!"".equals(strAuditEx)){
				_strNextAuditUser = strAuditEx;
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
		    
			e.printStackTrace();
		}finally{
			exFlowNode.close();
		}
		return _strNextAuditUser;
	}
	
	/**
	 * 根据配置查询业务表数据
	 * @param strRolePd:条件
	 * @param _strZj 主键值,未用
	 * * @param _strFlowRunId运行ID
	 * @return
	 */
	private String queryBusinessDataByCon(String strRolePd, String _strZj,String _strFlowRunId) {
		DBFactory dbf = new DBFactory();
		TableEx ex = null;
		String strRole= "";
		try {
			//strRolePd 表名|字段名|主键名称
			String[] strArrayPd = strRolePd.split("\\|",-1);
			ex = dbf.query(new StringBuffer().append(" select ").append(strArrayPd[1]).append(" from ").append(strArrayPd[0]).append(" where 1=1 and ").append("S_RUN_ID").append("='").append(_strFlowRunId).append("'").toString());
			strRole = helper.getColString(strArrayPd[1], ex.getRecord(0));
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			ex.close();
			dbf.close();
		}
		return strRole;
	}

	/**
	 * @param request
	 * @param _sb
	 * @return
	 */
	@SuppressWarnings("finally")
	public Boolean processRun(HttpServletRequest request,StringBuffer _sb){
		TableEx exRun =null;
		TableEx exRun1 =null;
		TableEx exRun2 =null;
		TableEx exNode = null;
		TableEx exHZ = null;
		TableEx exParent = null;//当前流程父流程对象
		Boolean b=true;
		DBFactory dbf  = new DBFactory();
		TableEx exForm= null;
		String strFlowRunId = "";
		String strFlowId ="";
		String strVersion="";
		String strCustomNodeId = "";
		String strAuditUser = "";
		try{
			request.setCharacterEncoding("UTF-8");
			/**接收数据*/
			strAuditUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//审批人
			String strAuditState = request.getParameter("NO_sys_flow_state");//审核状态 审核状态:0驳回1通过2作废3提交4逾期5逾期作废6逾期退回
			String strAuditComment = request.getParameter("strAuditComment");//备注

			
			if(strAuditComment!=null){
				 strAuditComment = new String(strAuditComment.getBytes("iso8859-1"),"UTF-8");
			}
			/*** 审批人指定节点*/
			String strAuditChoiceNode = request.getParameter("NO_sys_flow_choicenode");
			strFlowId = request.getParameter("NO_sys_flow_id");
            strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");
            strVersion = request.getParameter("NO_sys_flow_Ver");
            
            _sb.append("---审批人指定退回节点:"+strAuditChoiceNode);
            String strAuditUserId = request.getParameter("auditUserId");//多个审核人,指定审核人
            _sb.append("---指定下一节点审批人:"+strAuditUserId);
            strCustomNodeId = request.getParameter("NO_custom_node_id");//用户手动选择节点
            _sb.append("---用户选择节点:"+strCustomNodeId);
            
            if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){
            	processSave(request);
            }
            
            _sb.append("replay init");
			String strIsOver = "0";
			/**查询流程运行信息*/
			exRun = helper.queryFlowRun(strFlowId,strFlowRunId);
			Record record = exRun.getRecord(0);
			String strMsgs=helper.getColString("S_AUDIT_MSG",record);
			String strYqs=record.getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
			String strAuditUsers=record.getFieldByName("S_AUDIT_ARRAY").value.toString();
			String strNodes=record.getFieldByName("S_AUDIT_NODES").value.toString();
			int index = Integer.parseInt(record.getFieldByName("S_AUDIT_INDEX").value.toString());//索引
			String strLaunchUser = record.getFieldByName("S_LAUNCH_USER").value.toString();
			String strLaunchDate = record.getFieldByName("S_LAUNCH_DATE").value.toString();
			String strNodeIdNow = record.getFieldByName("S_NODE_CODE").value.toString();//当前节点
			String strOther = record.getFieldByName("S_AUDIT_OTHER").value.toString();//其他
			String strIsOverRun = record.getFieldByName("I_ISOVER").value.toString();//是否完成
			String strAudSel = record.getFieldByName("S_AUDIT_SEL").value.toString();//手动选择子节点
			String strAudOver = record.getFieldByName("S_AUD_OVER").value.toString();//结束节点
			String strFlowSon = record.getFieldByName("S_FLOW_SON").value.toString();//子流程
			String strFlowtype = record.getFieldByName("S_FLOW_TYPE").value.toString();//流程类型
			String strTab = helper.getColString("S_TAB", record);//表名
			boolean bTranFlowSonFlag=false;
			
			if("1".equals(strIsOverRun)){//判断完成返回
				return b;
			}
			
			/**更新表单*/
			exRun1 = queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow);
			String strClassName = helper.getColString("S_AUDIT_PAGENAME", exRun1.getRecord(0));
			String strMethodName = helper.getColString("S_AUDIT_CLASSNAME", exRun1.getRecord(0));
			String strAuditStateBak = request.getParameter("NO_sys_flow_state");
			String strField = helper.getColString("S_AUDIT_TABLECONTROL", exRun1.getRecord(0));

			if(strField!=null&&!"".equals(strField)){
				helper.updateTabByFlowSet(request, "", strField, strFlowId, strFlowRunId,_sb);//strNodeIdNow
				if(strCustomNodeId==null||"".equals(strCustomNodeId)){
	            	processSave(request);
	            	TableEx exRunGet =helper.queryFlowRun(strFlowId,strFlowRunId);
	            	strAuditUsers=exRunGet.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
	            	if(exRunGet!=null){
	            	    exRunGet.close();
	            	}
				    
	            }
			}
			
			if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){
				exRun2 = queryFlowNodeInfo(strFlowId, strVersion, strCustomNodeId);
           	    //节点名称相同-类型为2,手动选择节点
				if(strCustomNodeId.equals(helper.getColString("I_NODE_ID", exRun2.getRecord(0)))&&"2".equals(helper.getColString("I_TYPE", exRun2.getRecord(0)))&&!"".equals(helper.getColString("S_AUDIT_SEL", exRun2.getRecord(0)))){
					 //更新运行表
					 String strDef = helper.getColString("S_AUDIT_SEL",exRun2.getRecord(0));
					 String[] strRunAudSelArry = strAudSel.split("\\|",-1);
					 strRunAudSelArry[index]=strDef;
					 String[] strArrayFlowRunVal = {strFlowId,strFlowRunId,getStringArryToString(strRunAudSelArry)};
					 helper.updateFlowRun(strArrayFlowRunVal, "6");
					 return true;
			    }
           }            
			
			 _sb.append("form end");
			String[] strArrayAuditUsers =strAuditUsers.split("\\|",-1);//审批人数组
			String[] strArrayNodes = strNodes.split("\\|",-1);//节点数组
			String[] strArrayMsgs = strMsgs.split("\\|",-1);//消息数组
			
			/**判断当前登录人是否包含运行节点审批人*/
			String[] strArrayAuditUsersNow = strArrayAuditUsers[index].split(",");
			boolean flag = false;
			for(int i=0,j=strArrayAuditUsersNow.length;i<j;i++){
				if(strAuditUser.equals(strArrayAuditUsersNow[i])){
					flag = true;
					break;
				}
			}
			if(flag==false){
				return b; 
			}
			
			String strNodeIdNext = "";//下一节点
			String strNextAuditUser = "";//下一审批人
			int iNextAuditUserIndex = index;//下一审批人索引
			String strMsgId = strArrayMsgs[index];//节点消息ＩＤ
			String strAudMod="";//审批 指定/  抢占模式
			String[] strOtherArrayNow = strOther.split("\\|",-1)[index].split(",",-1);
			boolean bFlag=false;//驳回&结束流程
			/**是否逾期*/
			if(!isYuQi(strLaunchDate, strYqs,index)){
				String strYqOpt = strYqs.split("\\|",-1)[index].split(",",-1)[1];
				switch (strYqOpt) {
					case "ZF"://作废
						strAuditState = "5";
						strIsOver ="1";//流程结束
						break;
					case "TGJD"://自动跳过
						index = index +1;
						/**跳岗*/
						iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);					
						break;
					case "ZDTH"://自动退回初始节点
						index = 0;
						strAuditState = "6";
						strIsOver="1";
						iNextAuditUserIndex = 0;				
						break;
					case "JS"://结束
						index = 0;
						strAuditState = "0";
						strIsOver="1";
						iNextAuditUserIndex = 0;				
						break;
				}
			}else{
				 _sb.append("当前审批人长度:"+strArrayAuditUsersNow.length+"---strAuditState---"+strAuditState);
				/**判断是否审核通过*/
				switch (strAuditState) {
					case "1"://审核通过
						if("S".equals(strArrayAuditUsers[index+1])){//判断是否子流程---当前节点审批人是否S
							if(exRun1!=null){exRun1.close();}
							exNode = queryFlowNodeInfo(strFlowId, strVersion, strArrayNodes[index+1]);
							Record rd = exNode.getRecord(0);
							String strType =helper.getColString("S_CHILD_TYPE", rd).trim();
							String strSql =helper.getColString("S_CHILD_TRANSQL", rd).trim();
							String strCon =helper.getColString("S_CHILD_TRANCON", rd).trim();

							if("T".equals(strType)){//事物
								bTranFlowSonFlag = true;
								index = index+1;
								while("S".equals(strArrayAuditUsers[index])&&"T".equals(strType)){
									strSql = strSql.replace("-*-", "'");
									updateFormSql(strSql,strCon,strType,strFlowRunId);
									index++;
									if(exNode!=null){exNode.close();exNode = null;}
									exNode = queryFlowNodeInfo(strFlowId, strVersion, strArrayNodes[index]);
									strSql = helper.getColString("S_CHILD_TRANSQL", exNode.getRecord(0)).trim();
									strCon = helper.getColString("S_CHILD_TRANCON", exNode.getRecord(0)).trim();
									strType = helper.getColString("S_CHILD_TYPE", exNode.getRecord(0)).trim();
								}
								iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
								strAuditUser = strArrayAuditUsers[iNextAuditUserIndex];
							}else{//子流程
								index++;
								iNextAuditUserIndex = index;
								strAuditUser = "子流程启动";
								//启动子流程
								//是否有子流程-无子流程iNextAuditUserIndex++,没有子流程进行下一节点
								if(!queryFlowRunSon(strFlowId, "",strFlowRunId)){
									index++;
									//跳岗
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strAuditUser = strArrayAuditUsers[iNextAuditUserIndex];
								}
								//子流程结束,启动主流程
							}
						}else if(strArrayAuditUsersNow.length>1){//当前为多用户审批,判断审批模式
							String strAudModNow = strOtherArrayNow[4];//当前节点模式
							//类型,值5,通过人数6,驳回人数7|
							_sb.append("当前节点strother:"+strOther.split("\\|",-1)[index]);
							double dHQ = Double.parseDouble((strOtherArrayNow[5]==null||"".equals(strOtherArrayNow[5])?"0":strOtherArrayNow[5]));//会签值
							int iHQCount = strArrayAuditUsersNow.length;//总人数
							int iPasscount = Integer.parseInt("".equals(strOtherArrayNow[6])?"0":strOtherArrayNow[6]);//通过次数
							//修改数组 strOther
							_sb.append("修改strOther数组 :"+strOther);
							_sb.append("修改strOtherArrayNow数组 :"+strOtherArrayNow);
							_sb.append("修改index数组 :"+index);
							_sb.append("修改iPasscount数组 :"+iPasscount);
							strOther = getAuditOtherPass(strOther,strOtherArrayNow,index,iPasscount);
							 _sb.append("当前模式:"+strAudModNow);
							switch (strAudModNow) {
								case "QZ"://抢占--索引+1,判断节点跳岗,下一节点模式判断
									index = index+1;
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//下一节点模式:抢占?指定
									break;
								case "ZD"://指定--索引+1,判断节点跳岗,下一节点模式判断
									index = index+1;
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//下一节点模式:抢占?指定
									break;
								case "HQ"://会签
									//通过人数5--驳回人数6,会签值1,通过比例0.5,
									if(((iPasscount+1)*100/iHQCount)>=dHQ){//通过比例>=录入值,执行通过操作
										index = index+1;
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//下一节点模式:抢占?指定
									}
									break;
								default:
									index = index+1;
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//下一节点模式:抢占?指定
									break;
							}
						}else{//一个用户,正常流程
							index = index+1;
							iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
							strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//下一节点模式:抢占?指定
						}
						/**跳岗*/
						//会签---,2个字段  会签状态,,|,,|,,|,,
						break;
					case "2"://会签
						break;
					case "0"://驳回
						String strAudModNow = strOtherArrayNow[4];//当前节点模式
						System.out.println("节点模式:"+strAudModNow);
						_sb.append("节点模式:"+strAudModNow);
						if(strArrayAuditUsersNow.length>1){//当前为多用户审批,判断审批模式
							String strAuditReject = strOtherArrayNow[1];//驳回
							System.out.println("驳回代码:"+strAuditReject);
							double dHQ = Double.parseDouble((strOtherArrayNow[5]==null||"".equals(strOtherArrayNow[5])?"0":strOtherArrayNow[5]));//会签值
							int iHQCount = strArrayAuditUsersNow.length;//总人数
							int iRejectcount = Integer.parseInt("".equals(strOtherArrayNow[7])?"0":strOtherArrayNow[7]);//驳回次数
							_sb.append("驳回节点模式:"+strAudModNow);
							switch (strAudModNow) {
								case "QZ"://抢占模式
									/**查询当前节点信息*/
									switch (strAuditReject) {
									case "1"://上一节点
										String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
										_sb.append("上一节点:"+strBeforeNodeId+"---strArrayNodes---"+strArrayNodes);
										index = getChoiceNode(strArrayNodes,strBeforeNodeId);
										iNextAuditUserIndex = index;
										break;
									case "2"://初始节点
										index = 0;
										iNextAuditUserIndex=0;
										break;
									case "3"://指定节点
										String strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
										index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
										/**跳岗*///TODO
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										break;
									case "4"://审批人指定
										index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
										iNextAuditUserIndex = index;
										strAuditState = "0";
										break;
									case "5"://作废
										strAuditState = "2";
										strIsOver = "1";						
										break;
									case "6"://审批人指定-结束流程
										strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
										index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
										strAuditState = "0";
										strIsOver = "1";
										iNextAuditUserIndex  = index;
										bFlag =  true;
										break;
									}
									break;
								case "ZD"://指定模式
									/**查询当前节点信息*/
									switch (strAuditReject) {
									case "1"://上一节点
										String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
										index = getChoiceNode(strArrayNodes,strBeforeNodeId);
										iNextAuditUserIndex = index;
										break;
									case "2"://初始节点
										index = 0;
										iNextAuditUserIndex=0;
										break;
									case "3"://指定节点
										String strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
										index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
										/**跳岗*///TODO
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										break;
									case "4"://审批人指定
										index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
										iNextAuditUserIndex = index;
										strAuditState = "0";
										break;
									case "5"://作废
										strAuditState = "2";
										strIsOver = "1";						
										break;
									case "6"://审批人指定-结束流程
										strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
										index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
										strAuditState = "0";
										strIsOver = "1";
										iNextAuditUserIndex  = index;
										bFlag =  true;
										break;
									}
									break;
								case "HQ"://会签
									//通过人数5--驳回人数6,会签值1,通过比例0.4,
									if(((iRejectcount+1)*100/iHQCount)>=(100-dHQ)){//驳回比例>=1-录入值,执行驳回操作
										/**查询当前节点信息*/
										switch (strAuditReject) {
										case "1"://上一节点
											String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
											index = getChoiceNode(strArrayNodes,strBeforeNodeId);
											strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);//index-1,index
											iNextAuditUserIndex = index;
											break;
										case "2"://初始节点
											index = 0;
											iNextAuditUserIndex=0;
											strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
											break;
										case "3"://指定节点
											String strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
											index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
											/**跳岗*///TODO
											iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
											strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
											break;
										case "4"://审批人指定
											index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
											iNextAuditUserIndex = index;
											strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
											strAuditState = "0";
											break;
										case "5"://作废
											strOther = getAuditOtherReject(strOther,strOtherArrayNow,index,iRejectcount);
											strAuditState = "2";
											strIsOver = "1";						
											break;
										case "6"://审批人指定-结束流程
											strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
											index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
											strAuditState = "0";
											strIsOver = "1";
											bFlag =  true;
											iNextAuditUserIndex  = index;
											break;
										}
									}else{
										strOther = getAuditOtherReject(strOther, strOtherArrayNow, iNextAuditUserIndex, iRejectcount);
									}
									break;
							}
						}else{//正常流程
							/**查询当前节点信息*/ //驳回,当前节点之后清空 ,,,,,,|,,,,HQ,,|,,,,,,|,,,,,, //类型,值5,通过人数6,驳回人数7|
							String strAuditReject = strOtherArrayNow[1];//驳回
							switch (strAuditReject) {
							case "1"://上一节点
								String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
								index = getChoiceNode(strArrayNodes,strBeforeNodeId);
								iNextAuditUserIndex = index;
								strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
								break;
							case "2"://初始节点
								index = 0;
								iNextAuditUserIndex=0;
								strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
								break;
							case "3"://指定节点
								String strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
								index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
								/**跳岗*///TODO
								iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
								strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
								break;
							case "4"://审批人指定
								index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
								iNextAuditUserIndex = index;
								strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
								strAuditState = "0";
								break;
							case "5"://作废
								strAuditState = "2";
								strIsOver = "1";						
								break;
							case "6"://审批人指定-结束流程
								strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
								index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
								strAuditState = "0";
								strIsOver = "1";
								bFlag =  true;
								iNextAuditUserIndex  = index;
								break;
							}
						}
						break;
				}
			}
			strNodeIdNext = strArrayNodes[iNextAuditUserIndex];
			strNextAuditUser = "ZD".equals(strAudMod)?strAuditUserId:strArrayAuditUsers[iNextAuditUserIndex];//指定下一节点审批人
			/**流程是否结束*/
			if(strAudOver.indexOf(","+strNodeIdNext+",")>-1){
				strIsOver = "1";
			}
			
			/**判断当前节点审批人是否表单取值*/
			strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowId, strVersion, strNodeIdNext);
			String[] strArryAuditUsers = strAuditUsers.split("\\|",-1);
			strArryAuditUsers[iNextAuditUserIndex]=strNextAuditUser;
			strAuditUsers = getStringArryToString(strArryAuditUsers);
			
			/**驳回结束*/
			if(bFlag){
				strNextAuditUser = queryFlowLogBeforeNodeAuditUser(strFlowId,strVersion, strFlowRunId, strNodeIdNext);
			}
			
			/**更新流程运行信息*/
			String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strNodeIdNext,"2".equals(strAuditState)?"":strNextAuditUser,iNextAuditUserIndex+"",strIsOver,strOther,strAuditUsers};
			helper.updateFlowRun(strArrayFlowRun,"2");
			/**插入流程日志*/
			String strNowDate = strSdfYmdHms.format(new Date());
			if(bTranFlowSonFlag){
				strAuditUser =request.getSession().getAttribute("SYS_STRCURUSER").toString();
			}
			String updateMessage = _sb.substring(_sb.indexOf(ProcessRunOperationHelper.UPDATE_FIELD_START_TAG), _sb.indexOf(ProcessRunOperationHelper.UPDATE_FIELD_END_TAG) + ProcessRunOperationHelper.UPDATE_FIELD_END_TAG.length());
			String[] strArrayFlowLog = {strFlowId,strFlowRunId,strNodeIdNow,strNowDate,strVersion,strAuditUser,strAuditState,strAuditComment,updateMessage};
			insertFlowLog("1", strArrayFlowLog);
			/**更新当前审批日志为空*/
			updateSendMsgZt(dbf,strFlowRunId,strAuditUser,strFlowId);
			String strPageCode="";
			if("1".equals(strIsOver)){
				String strFlowParentId= helper.getColString("S_FLOW_PARENT_ID", exRun.getRecord(0)); //当前子流程父流程ID
				if(strFlowParentId!=null&&!"".equals(strFlowParentId)){//子流程结束,0:子流程 1:主流程 默认主流程
					//查询运行ID号,父流程号相等的所有子流程是否全部完成,如果全部完成,则父流程进行下一步,否则,保持不变
					boolean bIsOverSameFlow = helper.queryFlowRunIsOverSameLevel(strFlowRunId,strFlowParentId);//true:完成 false:未完成
					String strIsOverParent = "0";
					if(bIsOverSameFlow){
						//查询父流程,判断父流程是否结束,发送父流程消息
						exParent = new TableEx("*","T_SYS_FLOW_RUN"," S_RUN_ID='"+strFlowRunId+"' and S_FLOW_ID='"+strFlowParentId+"'");
						String[] strAuditUsersParent=exParent.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString().split("\\|",-1);
						String[] strNodesParent=exParent.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString().split("\\|",-1);
						int indexParent = Integer.parseInt(exParent.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString())+1;				
						String strAudOverParent = exParent.getRecord(0).getFieldByName("S_AUD_OVER").value.toString();//结束节点
						String strParentVersion = exParent.getRecord(0).getFieldByName("S_AUDIT_VERSION").value.toString();//版本号
						strTab = exParent.getRecord(0).getFieldByName("S_TAB").value.toString();//表名
						strNextAuditUser = strAuditUsersParent[indexParent];
						//判断跳岗TODO
						//判断父流程是否结束
						if(strAudOverParent.indexOf(","+strNodesParent[indexParent]+",")>-1){
							strIsOverParent = "1";
							strNextAuditUser = "";
						}
						strFlowtype = "1";
						/**判断当前节点审批人是否表单取值*/
						strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowParentId, strParentVersion, strNodesParent[indexParent]);
						strAuditUsersParent[indexParent]=strNextAuditUser;
						//更新父流程
						String[] strArrayFlowRunParent ={strFlowParentId,strFlowRunId,strNextAuditUser,strNodesParent[indexParent],indexParent+"",strIsOverParent,getStringArryToString(strAuditUsersParent)};
						helper.updateFlowRun(strArrayFlowRunParent,"4");
						//发送父流程消息
						strPageCode  = helper.sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOverParent,strFlowId,strVersion,strFlowRunId,strNodesParent[indexParent],request,strFlowtype,"",strTab);
					}else{
						//子流程完成,并列子流程未完成
						strNextAuditUser = strArrayAuditUsers[0];//子流程完成,并列子流程未完成,发送消息给发起人
						strFlowtype = "0";//当前流程为子流程
						strPageCode = helper.sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOverParent,strFlowId,strVersion,strFlowRunId,"",request,strFlowtype,"",strTab);
					}

				}else{//主流程结束
					strNextAuditUser=strArrayAuditUsers[0];//审批结束,接收人
					strPageCode = helper.sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowtype,"",strTab);					
				}
			}else{
				strPageCode = helper.sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowtype,"",strTab);
			}
			
			if("1".equals(strAuditStateBak)&&strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
				helper.reflectMothedInvoke(strClassName, strMethodName, request);
			}
			
			/**审批通过&流程结束执行操作*/
			if("1".equals(strIsOver)&"1".equals(strAuditState)){
				DBFactory db = new DBFactory();
				flowOverDelMsg(strFlowRunId,strFlowId,strVersion);
            
				if("1510196651437".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1513048527561".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1505896107531".equals(strPageCode)){//点检计划
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("15175860658900".equals(strPageCode)){//技改-完成项目等级更新汇总-辅
					db.sqlExe("update T_XMZH_F set S_ZT='1' where S_ID=(select S_FZFBID from T_WCXMDJB where S_ID='"+request.getParameter("S_ID")+"')",true);
					if(db==null){db = new DBFactory();}
					db.sqlExe("update T_WCXMDJB set S_FLAG='1' where S_ID='"+request.getParameter("S_ID")+"'",true);
				}else if("1500457059214".equals(strPageCode)){//技改-完成项目评分S_DJID
					new DBFactory().sqlExe("update T_XMPFB set S_FLAG='1' where S_ID='"+request.getParameter("S_ID")+"'",true);
					new DBFactory().sqlExe("update T_WCXMDJB set S_FLAG='2' where S_ID=(select S_DJID from T_XMPFB where S_ID='"+request.getParameter("S_ID")+"')",true);
				}else if("1500460289462".equals(strPageCode)){//技改-项目后评价报告
					db.sqlExe("update T_XMPFB set S_FLAG='2' where S_ID=(select S_PFID from T_XMHPJBG where S_ID='"+request.getParameter("S_ID")+"')",true);
				}else if("1516247158225".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("151766214254010023".equals(strPageCode)){
					db.sqlExe("update T_JGGH set S_FLAG='2' where S_ID=(select S_PFID from T_XMHPJBG where S_ID='"+request.getParameter("S_ID")+"')",true);
				}else if("1516166904515".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}
                else if("1516587805543".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516167932917".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516602563575".equals(strPageCode)){//重复
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516606174518".equals(strPageCode)){//重复
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516613463357".equals(strPageCode)){//重复
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516587886146".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("15175538437610".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516602563575".equals(strPageCode)){//重复
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516606174518".equals(strPageCode)){//重复
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1516613463357".equals(strPageCode)){//重复
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1506310525794".equals(strPageCode)){
					new com.page.method.Fun().MeasuresToolEntr(request);
				}
				else if("1522727526758".equals(strPageCode)){//技术创新突出贡献上报
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1522732741869".equals(strPageCode)){//优秀技改创新成果上报
					new com.page.method.Fun().MeasuresToolEntr(request);
				}else if("1522719345443".equals(strPageCode)){//合理化建议上报
					new com.page.method.Fun().MeasuresToolEntr(request);
				}

				if(db!=null){db.close();}
			}
		}catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			b = false;
			e.printStackTrace();
		}finally{
			if(exRun!=null){exRun.close();}
			if(exRun1!=null){exRun1.close();}
			if(exRun2!=null){exRun2.close();}
			if(exParent!=null){exParent.close();}
			if(exForm!=null){exForm.close();}
			if(exNode!=null){exNode.close();}
			if(exHZ!=null){exHZ.close();}
			if(dbf!=null){dbf.close();}
		
			return b;
		}
	}
	
	/**
	 * 更新消息状态
	 * @param _dbf
	 * @param _strFlowRunId
	 * @param _strAuditUser
	 */
	private void updateSendMsgZt(DBFactory _dbf, String _strFlowRunId,String _strAuditUser,String _strFlowId) {
		try {
			//删除消息表:条件流程ID,运行ID,版本号
			_dbf.sqlExe("delete from t_msg_records where S_YXID='"+_strFlowRunId+"'", true);
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
	}

	public void updateFormSql(String strSql, String strCon, String strType,String strFlowRunId) {
		//查询表单
		DBFactory dbf = new DBFactory();
		TableEx exForm = null;
		String sql = "";
		try {
			strCon = strCon.trim();
			String strTable = strCon.substring(0, strCon.indexOf("."));
			String strConTem = strCon.replace(strTable+".", "");
			exForm = dbf.query("select "+strConTem +" from "+strTable+" where S_RUN_ID='"+strFlowRunId+"'");
			//替换数据
			String[] strArrayCon = strConTem.split(",");
			for(int s1 = 0,s2 = strArrayCon.length;s1<s2;s1++){
				strSql = strSql.replace("<<"+strTable+"."+strArrayCon[s1]+">>", helper.getColString(strArrayCon[s1].trim(),exForm.getRecord(0)));
			}
			//执行sql
			String[] strArryCon = strSql.split(";");
			for(int s3=0,s4=strArryCon.length;s3<s4;s3++){
				sql = strArryCon[s3];
				if(!"".equals(sql)){
					dbf.sqlExe(sql, true);
				}
			}
			String[] strArrayFlowLog22 = {"333","333","",new Date()+"","运行ID"+strFlowRunId,"","updateFormSql",sql,""};
			insertFlowLog("1", strArrayFlowLog22);
			
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			if(exForm!=null){exForm.close();}
			dbf.close();
		}
	}

	public String getErrorInfoFromException(Exception e) {
		String sr = "";
		try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sr = "\r\n" + sw.toString() + "\r\n";
            sw.close();
            pw.close();
        } catch (Exception e2) {
            return "ErrorInfoFromException";
        }finally{
        	return sr;
        }
    }
	
	public String getStringArryToString(String[] _arry){
		StringBuffer sr = new StringBuffer();
		for(int i=0,j=_arry.length;i<j;i++){
			sr.append(_arry[i]).append("|");
		}
		return sr.deleteCharAt(sr.length()-1).toString();
	}

	private String getAuditOtherReject(String strOther, String[] strOtherArrayNow, int index, int iRejectcount) {
		StringBuffer sbOther = new StringBuffer();
		sbOther.append(strOtherArrayNow[0]).append(",").append(strOtherArrayNow[1]).append(",").append(strOtherArrayNow[2]).append(",").append(strOtherArrayNow[3]).append(",").append(strOtherArrayNow[4]).append(",");
		sbOther.append(strOtherArrayNow[5]).append(",");
		sbOther.append(strOtherArrayNow[6]).append(",").append(iRejectcount+1);
		System.out.println("getAuditOtherReject修改后的strOther:"+sbOther);
		String[] strOtherArray = strOther.split("\\|",-1);
		strOtherArray[index]=sbOther.toString();
		String strOterTemp =strOtherArray[0];
		for(int i=1,j=strOtherArray.length;i<j;i++){
			strOterTemp=strOterTemp+"|"+strOtherArray[i];
		}
		return strOterTemp;
	}

	//,,,,,,,|,1,,,,,1,|,1,,,,,,|,1,,,,,,|,1,,,,,|,1,,,,,|,1,,,,,
	public String getAuditOtherPass(String strOther,String[] strOtherArrayNow,int index,int iCount) {//,1,,,,,
		StringBuffer sbOther = new StringBuffer();
		sbOther.append(strOtherArrayNow[0]).append(",").append(strOtherArrayNow[1]).append(",").append(strOtherArrayNow[2]).append(",").append(strOtherArrayNow[3]).append(",").append(strOtherArrayNow[4]).append(",");
		sbOther.append(strOtherArrayNow[5]).append(",");
		sbOther.append(iCount+1).append(",").append(strOtherArrayNow[6]);
		System.out.println("getAuditOtherPass修改后的strOther:"+sbOther);
		String[] strOtherArray = strOther.split("\\|",-1);
		strOtherArray[index]=sbOther.toString();
		String strOterTemp =strOtherArray[0];
		for(int i=1,j=strOtherArray.length;i<j;i++){
			strOterTemp=strOterTemp+"|"+strOtherArray[i];
		}
		return strOterTemp;
	}

	/**
	 * 当前字符串指定索引之后改为默认
	 * @param strOther
	 * @param index
	 * @return
	 */
	public String getAuditStrArryDefault(String strOther, int index) {
		StringBuffer sbOther = new StringBuffer();
		String[] strOtherArray = strOther.split("\\|",-1);
		for(int i=index,j=strOtherArray.length;i<j;i++){
			String[] strOtherArraySon = strOtherArray[i].split(",",-1);
			sbOther.append(strOtherArraySon[0]).append(",");
			sbOther.append(strOtherArraySon[1]).append(",").append(strOtherArraySon[2]).append(",");
			sbOther.append(strOtherArraySon[3]).append(",").append(strOtherArraySon[4]).append(",");
			sbOther.append(strOtherArraySon[5]).append(",").append(",");
			sbOther.append(i==strOtherArray.length-1?"":"|");
		}		
		return sbOther.toString();
	}

	/**
	 * 表单修改重新初始化流程
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public Boolean processSave(HttpServletRequest request){
		Boolean b = true;
		TableEx exRun =null;
		TableEx tableEx =null;
		String strFlowRunId = "";
		String strFlowId ="";
		String strVersion="";
		try{
			/**1接收数据*/
			strFlowId = request.getParameter("NO_sys_flow_id");//流程ID
			strVersion = request.getParameter("NO_sys_flow_Ver");//版本号
			strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//运行
			
			/**2查询运行表*/
			exRun = helper.queryFlowRun(strFlowId,strFlowRunId);
			String strIsOverRun = exRun.getRecord(0).getFieldByName("I_ISOVER").value.toString();//是否完成
			if("1".equals(strIsOverRun)){//判断完成返回
				return b;
			}
			
			String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
			String strYqs=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
			String strAuditUsersRun=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
			String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString();
			String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
			String strLaunchBranch = exRun.getRecord(0).getFieldByName("S_LAUNCH_BRANCH").value.toString();
			String strStartNode = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
			String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//其他
			int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
			String strRunAudSel = exRun.getRecord(0).getFieldByName("S_AUDIT_SEL").value.toString();//手动选择子节点
			String strSonFlow =  exRun.getRecord(0).getFieldByName("S_FLOW_SON").value.toString();//子流程
			String strFlowPj =  exRun.getRecord(0).getFieldByName("S_AUDIT_FSPJ").value.toString();//附加票据
			/**3 查询流程节点*/
			tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
			
			/**4 循环拼接参数*/
			String strAuditArrayyq="";//逾期
			String strAuditNodes="";//所有节点
			String strAuditOther ="";//其他
			String strAuditUsers = "";//所有审批人
			String strAuditMsgs = "";//所有消息模版
			String strIsOver = "0";//是否结束
			Record rd = null;//获取下一节点对象
			String strEndFlag = "";
			String strAudSel = "";//自定义节点
			String strAudFlowPj = "";
			int icount = 0;
			String strAudSonFlow ="";//子流程
			String[] strNodesArray = strNodes.split("\\|",-1);
			String strCustomNodeId = request.getParameter("NO_custom_node_id");//用户手动选择节点
			
			if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){//
				 strStartNode =strCustomNodeId;
			}else{
		         strStartNode = strNodesArray[index+1];
			}

			 //第一次选择节点,没问题 |||7,6,8||||12,13,14,15
			 //第一次选择完了节点,出现了问题
			 //|||
			while(!"4".equals(strEndFlag)){
				rd = getNextNodeByCondition(request,strStartNode,tableEx,"2",strFlowRunId);
				if(rd==null){break;}

				strEndFlag = helper.getColString("I_TYPE", rd);
				String strCustomNodeIds = helper.getColString("S_AUDIT_SEL", rd);//手动选择节点
				if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//网关判断是否手动选择节点
					strAudSel = strAudSel+strCustomNodeIds;
					break;
				}
				String strNodeAudit = queryAuditPerson(strLaunchUser,strLaunchBranch,helper.getColString("S_AUDIT_BRANCH", rd),helper.getColString("S_AUDIT_ROLE", rd),helper.getColString("S_AUDIT_USER", rd),helper.getColString("S_AUDIT_SQRYATTR", rd),helper.getColString("S_AUDIT_SQRY", rd),request,rd,strFlowRunId);

				if("5".equals(strEndFlag)){//子流程,存储子流程流程号/版本号运行号/表单ID
					strNodeAudit = "S";
				}
				strAudSonFlow = (icount==0?"":(strAudSonFlow+"|"))+helper.getColString("S_FLOW_SON", rd);
				strAudSel = (icount==0?"":(strAudSel+"|"))+strCustomNodeIds;
				strAuditArrayyq =(icount==0?"":(strAuditArrayyq+"|")) +helper.getColString("S_AUDIT_YQTS", rd)+","+helper.getColString("S_AUDIT_YQTSCL", rd);//所有逾期  所有逾期操作
				strAuditMsgs =(icount==0?"":(strAuditMsgs+"|"))+helper.getColString("S_AUDIT_TZXX", rd);//所有消息模版
				strAuditNodes = (icount==0?"":(strAuditNodes+"|"))+helper.getColString("I_NODE_ID", rd);
				strAuditOther =(icount==0?"":(strAuditOther+"|")) +helper.getColString("S_AUDIT_TG", rd)+","+helper.getColString("S_AUDIT_THJD", rd)+","+helper.getColString("S_AUDIT_THJDZD", rd)+","+helper.getColString("S_TZLC", rd)+","+helper.getColString("S_AUDIT_PREEMPTION", rd)+","+helper.getColString("S_AUD_VAL", rd)+","+",";
				strStartNode = helper.getColString("S_CHILD_ID", rd);
				strAudFlowPj =(icount==0?"":(strAudFlowPj+"|"))+helper.getColString("S_AUDIT_FSPJ", rd);//附加票据
				//是否跳过 所有审批驳回 所有审批驳回处理 子流程
				if("".equals(strNodeAudit)){
					strNodeAudit = ("1".equals(helper.getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//跳岗
				}
				strAuditUsers = (icount==0?"":(strAuditUsers+"|"))+strNodeAudit;

				icount=1;
			}
			
			/**5组装参数,中间保存从当前节点修改审批信息*/
			strAuditUsersRun = getAuditStrArrySave(strAuditUsersRun,strAuditUsers,index);//0,1,2,3,4,5  运行index:2 ,传入index3,当前节点之前值(包含当前)+ 当前节点之后值(包含当前)
				
			strYqs = getAuditStrArrySave(strYqs,strAuditArrayyq,index);
			strMsgs = getAuditStrArrySave(strMsgs,strAuditMsgs,index);
			strSonFlow = getAuditStrArrySave(strSonFlow,strAudSonFlow,index);
			strNodes = getAuditStrArrySave(strNodes,strAuditNodes,index);
			strOther= getAuditStrArrySave(strOther,strAuditOther,index);
			strRunAudSel = getAuditStrArrySave(strRunAudSel,strAudSel,index);
			strFlowPj = getAuditStrArrySave(strFlowPj,strAudFlowPj,index);
			
			/**判断当前节点审批人是否表单取值*/
			String strNextAuditUser = queryAuditPersonIsColumn(strAuditUsersRun.split("\\|",-1)[index+1], strFlowRunId, strFlowId, strVersion, strNodes.split("\\|",-1)[index+1]);
			String[] strArryAuditUsers = strAuditUsersRun.split("\\|",-1);
			

			strArryAuditUsers[index+1]=strNextAuditUser;
			strAuditUsersRun = getStringArryToString(strArryAuditUsers);

			/**7 更新运行表 4个数组*/
			String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strMsgs,strYqs,strAuditUsersRun,strNodes,strOther,strIsOver,strRunAudSel,strSonFlow,strFlowPj};
			helper.updateFlowRun(strArrayFlowRun,"3");
		}catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			b = false;
			e.printStackTrace();
		}finally{
			exRun.close();
			if(tableEx!=null){tableEx.close();}
			return b;
		}
	}
	
	public Map<String,String> processAudAll(HttpServletRequest request){
		Map<String,String> map = new HashMap<String, String>();
		String processAudCustomNodeIds = "";
		String processAuditSelectNode = "";
		String processNodeAudit = "";
		String strFlowPj = "";
		TableEx exRun =null;
		TableEx exRunNowNode =null;
		String nowNodeName="";
		String nowNodeId="";
		String strAuditFlag = "";
		/**接收数据*/
		String strFlowId = request.getParameter("s_flow_id");
        String strFlowRunId = request.getParameter("sys_flow_run_id");
        String strVersion = request.getParameter("flow_ver");
		
		/**查询流程运行信息*/
		try {
			exRun = helper.queryFlowRun(strFlowId,strFlowRunId);
			processAudCustomNodeIds = processAudCustomNodeIds(request, exRun);
			processAuditSelectNode = processAuditSelectNode(request, exRun);
			processNodeAudit = processNodeAudit(request, exRun);
			int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
			strFlowPj = exRun.getRecord(0).getFieldByName("S_AUDIT_FSPJ").value.toString().split("\\|",-1)[index];//附属票据;
			
			nowNodeId = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();
			strAuditFlag = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString().split("\\|",-1)[index].split(",",-1)[1];
			exRunNowNode = new TableEx("S_NODE_NAME","t_sys_flow_node"," s_flow_id='"+strFlowId+"' and S_AUDIT_VERSION='"+strVersion+"' and I_NODE_ID='"+nowNodeId+"'");
			nowNodeName = exRunNowNode.getRecord(0).getFieldByName("S_NODE_NAME").value.toString();
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exRun.close();
			exRunNowNode.close();
		}
		
		map.put("processAudCustomNodeIds", processAudCustomNodeIds.replaceAll("\r|\n", ""));
		map.put("processAuditSelectNode", processAuditSelectNode.replaceAll("\r|\n", ""));
		map.put("processNodeAudit", processNodeAudit.replaceAll("\r|\n", ""));
		map.put("strFlowPj", strFlowPj+"|");
		map.put("nowNodeName", nowNodeName.replaceAll("\r|\n", ""));
		map.put("auditFlag",("".equals(strAuditFlag)||"7".equals(strAuditFlag)||"0".equals(strAuditFlag))?"":"1");
		return map;
	}
	
	/**
	 * 自定义选择节点
	 * @param request
	 * @return
	 */
	public String processAudCustomNodeIds(HttpServletRequest request,TableEx exRun){
		TableEx exTRGXX =null;
		String strResult = "";
		try{
			/**接收数据*/
			String strFlowId = request.getParameter("s_flow_id");
            String strFlowRunId = request.getParameter("sys_flow_run_id");
            String strVersion = request.getParameter("flow_ver");
			
			/**查询流程运行信息*/
			if(exRun.getRecordCount()>0){
				String strCustomNodes = helper.getColString("S_AUDIT_SEL", exRun.getRecord(0));
				int index = Integer.parseInt(helper.getColString("S_AUDIT_INDEX", exRun.getRecord(0)));
				strCustomNodes = strCustomNodes.split("\\|",-1)[index];
				String audDef = "";
				TableEx nodeEx = null;
				try {
					nodeEx = new TableEx("S_AUDIT_DEF","T_SYS_FLOW_NODE"," S_CHILD_ID='"+strCustomNodes +"' and S_FLOW_ID='"+strFlowId+"' and S_AUDIT_VERSION ='"+strVersion+"'");
					audDef = helper.getColString("S_AUDIT_DEF", nodeEx.getRecord(0));
				} catch (Exception e) {
				    MantraLog.fileCreateAndWrite(e);
					e.printStackTrace();
				} finally{
					if(nodeEx!=null){nodeEx.close();}
				}
				
				if(!"".equals(strCustomNodes)){
					exTRGXX = new TableEx("I_NODE_ID,S_NODE_NAME,I_TYPE","T_SYS_FLOW_NODE"," I_NODE_ID in("+strCustomNodes+")" +"and S_FLOW_ID='"+strFlowId+"' and S_AUDIT_VERSION ='"+strVersion+"'");
					Record rd = null;
					for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
						rd = exTRGXX.getRecord(i);
						strResult = ("".equals(strResult)?"":(strResult+"|"))+helper.getColString("I_NODE_ID", rd)+","+helper.getColString("S_NODE_NAME", rd)+","+helper.getColString("I_TYPE", rd)+","+audDef;
					}
				}
			}
		}catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			if(exTRGXX!=null){exTRGXX.close();}
			return strResult+"|";
		}
	}
	
	/**
	 * 审批驳回选择节点
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public String processAuditSelectNode(HttpServletRequest request,TableEx exRun){
		String strResult = "";
		try{
			/**接收数据*/
			String strFlowId = request.getParameter("s_flow_id");
            String strFlowRunId = request.getParameter("sys_flow_run_id");
            String strVersion = request.getParameter("flow_ver");
			
			/**查询流程运行信息*/
			if(exRun.getRecordCount()>0){
			    if (exRun.getRecord(0).getFieldByName("S_NODE_CODE").value==null ||exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value==null) { // 过滤空指针

    				return strResult;
    			}
    			
				String strRunNode = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
				
				String strReject ="";
				if (exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value==null ) { // 过滤空指针
    				strReject="";
    			}else{
    				String[] temp = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString().split("\\|",-1)[index].split(",");
    				if (temp != null && temp.length > 1) {
    					strReject = temp[1];
					}
    			}
				if("4".equals(strReject)){
					strResult = queryFlowLogBeforeAll(strFlowId, strVersion, strFlowRunId,strRunNode);
				}
			}
		}catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			return strResult;
		}
	}
	
	/**
	 * 当前节点指定审批人-多人
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public String processNodeAudit(HttpServletRequest request,TableEx exRun){
		TableEx exTRGXX =null;
		StringBuffer strResult  = new StringBuffer();
		try{
			/**接收数据*/
			String strFlowId = request.getParameter("s_flow_id");
            String strFlowRunId = request.getParameter("sys_flow_run_id");
            String strVersion = request.getParameter("flow_ver");
			/**查询流程运行信息*/
			if(exRun.getRecordCount()>0){
				//S_AUDIT_OTHER S_AUDIT_ARRAYYQ S_AUDIT_INDEX
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
				String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
				String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
				String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
				String strNodeIdNow = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
				String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//其他
				String[] strArrayAud = strAuditUsers.split("\\|",-1);
				
				int iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strNodeIdNow);
				
				if(strArrayAud[iNextAuditUserIndex].split(",").length>1){//多个审批人
					if("ZD".equals(strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4])){//指定模式
						exTRGXX = new TableEx("SYGZW,SYGMC","T_RGXX","1=1  and SYGZW in("+strArrayAud[iNextAuditUserIndex]+")");//and SROLECODE in("+_strRoleIds+")
						Record  rd = null;
						for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
							rd = exTRGXX.getRecord(i);
							strResult.append(helper.getColString("SYGZW", rd)).append(",").append(helper.getColString("SYGMC", rd)).append("|");
						}
					}
				}
			}
		}catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			if(exTRGXX!=null){exTRGXX.close();}
			return strResult.toString();
		}
	}
	
	public String getRequestParam(HttpServletRequest _request, String _strReplaceStr) {
		Enumeration enu = _request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			_strReplaceStr = _strReplaceStr.replace(paraName, "'"+_request.getParameter(paraName)+"'");
		}
		return _strReplaceStr;
	}
	
	/**
	 * 流程条件判断
	 * @param _strCon
	 * @return
	 */
	public String appendConditionSql(HttpServletRequest _request,String _strCon,String _strType,String _strRunId){//网关条件判断,没有条件返回空
//		2:  T_table1.qjts_2 <  3  $6:  T_table1.qjts_2 >=  3 
//		30:   T_DQEZGZP.S_QF_SMGLYQM  <>  -*--*-   $29:   T_DQEZGZP.S_QF_SMGLYQM  =  -*--*-    $
//		25:   T_DQEZGZP.S_SPJG  =  -*-BHG-*-   $24:   T_DQEZGZP.S_SPJG  =  ‘HG’   $
//		108:   T_DQYZGZP.S_GLYQM_NAME  <>  -*--*-   $118:   T_DQYZGZP.S_GLYQM  =  -*--*-   $
//		节点代码:  条件  分割符$ ......
		DBFactory dbf = new DBFactory();
		_strCon = _strCon.replace("-*-", "'");
		_strCon = _strCon.replace("^", "%");
		String[] strNodeArray = _strCon.split("\\$");
		
		String strTable = "";
		String sql = "";
		String sqlCon = "";
		String strNode="";
		String st="";
		TableEx ex = null;
		for(int i=0,j=strNodeArray.length;i<j;i++){
			if(strNodeArray[i].trim().length()==0){continue;}
			String strNodeId = strNodeArray[i].substring(0,strNodeArray[i].indexOf(":")).trim();//节点ＩＤ
			strNodeArray[i] = strNodeArray[i].substring(strNodeArray[i].indexOf(":")+1,strNodeArray[i].length());//去掉'冒号'
			strTable = strNodeArray[i].substring(0,strNodeArray[i].indexOf("."));//得到表名
			strNodeArray[i] = strNodeArray[i].replace(strTable+".", strTable+"$");//.替换为$
			if("2".equals(_strType)){
				String str = strNodeArray[i].trim();
				strTable = strTable.trim();
				try {
					String strCol = str.substring(str.indexOf(strTable)+strTable.length()+1,str.indexOf(" "));
					if(_request!=null){
						_strRunId=_request.getParameter("NO_sys_S_RUN_ID");
					}
					st = "select "+strCol+" from "+strTable+" where S_RUN_ID='"+_strRunId+"'";
					ex = dbf.query("select "+strCol+" from "+strTable+" where S_RUN_ID='"+_strRunId+"'");
					sql =strNodeArray[i].replace(strTable+"$"+strCol,"'"+helper.getColString(strCol, ex.getRecord(0))+"'"); 
				} catch (Exception e) {
				    MantraLog.fileCreateAndWrite(e);
					e.printStackTrace();
					if(ex!=null){ex.close();}
				}finally{
					if(ex!=null){ex.close();}
				}
			}else{
				sql = getRequestParam(_request,strNodeArray[i]);
			}
			
			sqlCon = queryConditionSql(sql);
			if("1".equals(sqlCon)){//1:true
				strNode = strNodeId;
				break;
			}
 			sb.append("sql:"+sql+  "  sqlCon  "+sqlCon);
		}
		dbf.close();
		return strNode;
	}
	
	private String queryConditionSql(String _sql){
		String strResult = "";
		TableEx ex = null;
		try {
			ex = new TableEx(_sql +" as 'xx'", "T_CONDITION","1=1");
			strResult = ex.getRecord(0).getFieldByName("xx").value.toString();
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return strResult;
	}
	
	/**
	 * true:正常 false:逾期
	 * @param strLaunchDate
	 * @param strYqs
	 * @param index
	 * @return
	 */
	public boolean isYuQi(String strLaunchDate,String strYqs,int index){
		boolean b = true;
		String[] strArrayYq = strYqs.split("\\|",-1)[index].split(",",-1);
		if(!"".equals(strArrayYq[0])){//逾期
			if(new Date().getTime()>dateCal(strLaunchDate,Integer.parseInt(strArrayYq[0])).getTime()){
				b = false;
			}
		}
		return b;
	}
	
	//2017-08-18 15:06:44 ,|1,TGJD|1,TGJD|1,ZDTH|1,TGJD|,TGJD|,  2
	/**
	 * 开始节点判断是否发起人
	 * @param _strStartUserId 前台传入
	 * @param _strStartRole
	 * @param _strStartBranchId
	 * @param _strBranchIds 节点取值
	 * @param _strRoleIds
	 * @param _strUserIds
	 * @return
	 */
	public boolean queryFlowStartPerson(String _strStartUserId,String _strStartRole,String _strStartBranchId,String _strBranchIds,String _strRoleIds,String _strUserIds){
		if(compareArrayRepeat(_strStartRole,_strRoleIds)){//---发起人角色与节点角色一致
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 查找节点审批人
	 * @param _strLanuchUserId发起人
	 * @param _strLanuchBranchId
	 * @param _strBranchIds 节点
	 * @param _strRoleIds
	 * @param _strUserIds
	 * @return
	 */
	public String queryAuditPerson(String _strLanuchUserId,String _strLanuchBranchId,String _strBranchIds,String _strRoleIds,String _strUserIds,String _strAttr,String _strZj,HttpServletRequest _request,Record rd,String _strFlowRunId){
		String strAuditIds = "";
		//选择了人------------直接返回人
		String strGw = "";
		strGw = helper.getColString("S_AUDIT_GW", rd);
		if(!"".equals(_strUserIds)){
			strAuditIds =_strUserIds;
		}
		else if(_strAttr!=null&&!"".equals(_strAttr)){
			//选择了三权人员
			//查询人所在部门，_strLanuchBranchId, 根据发起人所在部门ID，依次向上查询部门下有此角色的人
			strAuditIds = queryUserIdBySqRoles(_strAttr,_strLanuchBranchId,_strZj,_request,_strFlowRunId);
		}else if(("".equals(_strBranchIds))&&("".equals(_strUserIds))&&(_strRoleIds!=null&&!"".equals(_strRoleIds))){
			//选择了角色（可能多个）,机构、人为空
			//查询人所在部门，_strLanuchBranchId, 根据发起人所在部门ID，依次向上查询部门下有此角色的人
			strAuditIds = queryUserIdByRoles(_strRoleIds,_strLanuchBranchId);
		}else if((!"".equals(_strBranchIds))&&("".equals(_strUserIds))&&(_strRoleIds!=null&&!"".equals(_strRoleIds))){
			//选择了机构(可能多个）  角色   人为空,根据机构和角色查询所在人
			TableEx exTRGXX = null ;
			try {
				exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","T_RGXX","1=1  and SBRANCHID in("+_strBranchIds+")");//and SROLECODE in("+_strRoleIds+")
				if(exTRGXX!=null&&!"".equals(exTRGXX)){
					int iCount = exTRGXX.getRecordCount();
					for(int i=0;i<iCount;i++){
						Record rd1 = exTRGXX.getRecord(i);
						if(compareArrayRepeat(_strRoleIds,helper.getColString("SROLECODE", rd1))){//包含角色
							strAuditIds = ("".equals(strAuditIds)?"":(strAuditIds+","))+helper.getColString("SYGZW", rd1);
						}
					}
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exTRGXX.close();
			}
		}
		return strAuditIds;
	}
	
	private String queryUserByGw(String strGw) {
		TableEx exTRGXX = null;
		StringBuffer sr =new StringBuffer();
		//查询人所在父级部门的所有人,并通过角色筛选得到map< 角色代码，人ID>
		try {
			exTRGXX = new TableEx("SYGZW","t_RGXX","1=1 and S_GW in("+strGw+") ");//and SROLECODE in("+_strRoles+")
			int iCount = exTRGXX.getRecordCount();
			Record rd = null;
			for(int i=0;i<iCount;i++){
				rd = exTRGXX.getRecord(i);
				sr.append(helper.getColString("SYGZW", rd)).append(",");
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exTRGXX.close();
		}
		return sr.deleteCharAt(sr.length()-1).toString();
	}
	
	/**
	 * 流程启动判断开始节点
	 * @param _strAttr
	 * @param _strZj
	 * @param _request
	 * @param _strFlowRunId
	 * @return
	 */
	public boolean querySqRole(String _strAttr,String _strZj,HttpServletRequest _request,String _strFlowRunId,String _strStartRole){
		/**添加表单字段与配置对应 start*/
		//表名,字段名|主键'
		boolean b = false;
		String sql ="";
		if(_strAttr.indexOf("CL:")>-1){//常量
			_strAttr = _strAttr.replace("CL:", "");
			sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
		}else if(_strAttr.indexOf("BL:")>-1){
			_strAttr = _strAttr.replace("BL:", "");
			_strAttr = queryBusinessDataByCon(_strAttr,"",_strFlowRunId);
			sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
		}else if(_strAttr.indexOf("AL:")>-1){
			_strAttr = _strAttr.replace("AL:", "");
			String[] strArryAttr = _strAttr.split("-");
			String strFormVal = queryBusinessDataByCon(strArryAttr[1],"",_strFlowRunId);
			sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+strArryAttr[0]+"' and S_ATTR2='"+strFormVal+"' and T_JSYHZ.S_ID='"+_strZj+"'";
		}
		
		if ("".equals(sql)) {
			return b;
		}
		
		/**添加表单字段与配置对应 end*/
		StringBuffer _strRoleIds = new StringBuffer();
		DBFactory dbf = new DBFactory();
		TableEx exSq = null;
		try {
			exSq = dbf.query(sql);
			Record rd = null;
			int j = exSq.getRecordCount();
			if(j==0){
				b=false;
			}else{
				for(int i=0;i<j;i++){
					rd = exSq.getRecord(i);
					_strRoleIds.append(helper.getColString("S_JSDM", rd)).append(",");
				}
				if(_strRoleIds.length()==0){
					
				}else{
					_strRoleIds = _strRoleIds.deleteCharAt(_strRoleIds.length()-1);
				}
				if(compareArrayRepeat(_strRoleIds.toString(), _strStartRole)){
					b = true;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try{
					exSq.close();
					dbf.close();
			}catch(Exception e){
				
			}
			return b;
		}
	}
	
	public String queryUserIdBySqRoles(String _strAttr,String _strLanuchBranchId,String _strZj,HttpServletRequest _request,String _strFlowRunId){
		/**添加表单字段与配置对应 start*/
		//表名,字段名|主键
		String sql = "";
		String strBchId = _strLanuchBranchId;
		
		//CL: 和 AL: 为三权 需要在三权中选择人员;
		if(_strAttr.indexOf("CL:")>-1){//常量
			_strAttr = _strAttr.replace("CL:", "");
			sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
		}else if(_strAttr.indexOf("BL:")>-1){
			_strAttr = _strAttr.replace("BL:", "");
			_strAttr = queryBusinessDataByCon(_strAttr,"",_strFlowRunId);
			sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
		}else if(_strAttr.indexOf("AL:")>-1){
			_strAttr = _strAttr.replace("AL:", "");
			String[] strArryAttr = _strAttr.split("-");
			//CBL:XKR-T_DQYZGZP|S_BPDD|S_ID
			String strSttr = strArryAttr[0];
			String strFormVal = queryBusinessDataByCon(strArryAttr[1],"",_strFlowRunId);
			sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+strSttr+"' and S_ATTR2='"+strFormVal+"' and T_JSYHZ.S_ID='"+_strZj+"'";
		}
		
		/**添加表单字段与配置对应 end*/
		StringBuffer _strRoleIds = new StringBuffer();
		DBFactory dbf = new DBFactory();
		TableEx exSq = null;
		try {
			exSq = dbf.query(sql);
			Record rd = null;
			for(int i=0,j=exSq.getRecordCount();i<j;i++){
				rd = exSq.getRecord(i);
				_strRoleIds.append(helper.getColString("S_JSDM", rd).trim()).append(",");
				_strLanuchBranchId = helper.getColString("S_BMID", rd);
			}
			if(_strRoleIds.length()==0){
				
			}else{
				_strRoleIds = _strRoleIds.deleteCharAt(_strRoleIds.length()-1);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try{
					exSq.close();
					dbf.close();
			}catch(Exception e){
				
			}
		}
		
		String strRole = _strRoleIds.toString();
		Map<String,String> map = new LinkedHashMap<String, String>();
		String strAuditIds= "";//001001001---001,001001,001001001
		String strBranchCodes = getParentBranchCode(_strLanuchBranchId);
		TableEx exTRGXX = null;
		//查询人所在父级部门的所有人,并通过角色筛选得到map< 角色代码，人ID>
		try {
            String Sqyy = selDAteRet("S_TSSX","T_JSYHZ","S_ID='"+_strZj+"'");
            MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---Sqyy---"+Sqyy);
            MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---Sqyy---"+strRole);
            
            if("SQYY".equals(Sqyy)){
	            exTRGXX = new TableEx("t_rgxx.SROLECODE SROLECODE,t_rgxx.SYGZW SYGZW,t_rgxx.SBRANCHID SBRANCHID","T_SQRYDJB LEFT JOIN t_rgxx ON T_SQRYDJB.S_RYBM=t_rgxx.sygzw "," 1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%' ");//and SROLECODE in("+_strRoles+")
	        }else{
	            exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%' ");//and SROLECODE in("+_strRoles+")
	        }
	 
			int iCount = exTRGXX.getRecordCount();
			Record rd = null;
			for(int i=0;i<iCount;i++){
				rd = exTRGXX.getRecord(i);
				if(compareArrayRepeat(strRole.toString(),helper.getColString("SROLECODE",rd).trim())){//角色包含
					String strBranchId = rd.getFieldByName("SBRANCHID").value.toString();
					String strUserId =rd.getFieldByName("SYGZW").value.toString();

					map.put(strBranchId, map.get(strBranchId)==null?strUserId:map.get(strBranchId)+","+strUserId);
				}
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exTRGXX.close();
		}
		//获取最近的部门

		StringBuffer sr = new StringBuffer();
		 for (Map.Entry<String, String> entry : map.entrySet()) {
			 String strAuditIds1 = entry.getValue();
			 sr.append(strAuditIds1).append(",");
		}
		
		 if(sr.length()==0){
			 strAuditIds = "";
		 }else{
			 strAuditIds =sr.deleteCharAt(sr.length()-1).toString();
		 }
		 
		 MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---strAuditIds---"+strAuditIds);
		return strAuditIds;
	}
	
	/**
	 * 查询用户组织树角色
	 * @param _strRoleIds
	 * @param _strLanuchBranchId
	 * @return
	 */
	public String queryUserIdByRoles(String _strRoleIds,String _strLanuchBranchId){
		String strBchId = _strLanuchBranchId;
		Map<String,String> map = new LinkedHashMap<String, String>();
		String strAuditIds= "";//001001001---001,001001,001001001
		String strBranchCodes = getParentBranchCode(_strLanuchBranchId);
		TableEx exTRGXX = null;
		//查询人所在父级部门的所有人,并通过角色筛选得到map< 角色代码，人ID>
		try {
			/**流程定义本组织全部可以审批*/
			exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%'  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
			int iCount = exTRGXX.getRecordCount();
			Record rd = null;
			for(int i=0;i<iCount;i++){
				rd = exTRGXX.getRecord(i);
				if(compareArrayRepeat(_strRoleIds,helper.getColString("SROLECODE",rd))){//角色包含
					String strBranchId = rd.getFieldByName("SBRANCHID").value.toString();
					String strUserId =rd.getFieldByName("SYGZW").value.toString();
					map.put(strBranchId, map.get(strBranchId)==null?strUserId:map.get(strBranchId)+","+strUserId);
				}
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exTRGXX.close();
		}
		//获取最近的部门
		StringBuffer sr = new StringBuffer();
		 for (Map.Entry<String, String> entry : map.entrySet()) {
			 String strAuditIds1 = entry.getValue();
			 sr.append(strAuditIds1).append(",");
		}
		 if(sr.length()==0){
			 strAuditIds = "";
		 }else{
			 strAuditIds =sr.deleteCharAt(sr.length()-1).toString();
		 }
		return strAuditIds;
	}
	
	/**
	 * 获取当前编号的所有上级节点
	 * @param _strBranchCodes
	 * @return
	 */
	public String getParentBranchCode(String _strBranchCodes){
		String str = _strBranchCodes;
		while(_strBranchCodes.length()>3){
			_strBranchCodes = _strBranchCodes.substring(0,_strBranchCodes.length()-3);
			str = str+","+_strBranchCodes;
		}
		return str;
	}

	/**
	 * 运行-跳岗
	 * @param _strUsers
	 * @param _strOther
	 * @param index
	 * @param _strLaunchUser
	 * @param _strArrayMsg
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strNodeId
	 * @return
	 */
	public int getNodesInfoRun(String _strUsers,String _strOther,int index,String _strLaunchUser,String[] _strArrayMsg,String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
		String[] strArrayUsers = _strUsers.split("\\|",-1);
		//审批人为空&&跳岗为1，则++
		while("T".equals(strArrayUsers[index])){
			/**发送消息*/
			index++;
		}
		return index;
	}
	
	/**
	 * 获取下一节点
	 * @param request
	 * @param _strNowNode
	 * @param _map
	 * @return
	 */
	public Record getNextNodeByCondition(HttpServletRequest request,String _strNowNode,TableEx _tabEx,String _strType,String _strRunId){
		Record record =null;
		try {
			// 获取当前记录
			Record objNowRd = getRecordByNodeId(_strNowNode,_tabEx);
			//判断节点类型
			String strNodeType = helper.getColString("I_TYPE", objNowRd);
			if(("3").equals(strNodeType)){//开始-找到子节点-自调用
				record = getNextNodeByCondition(request,helper.getColString("S_CHILD_ID", objNowRd),_tabEx,_strType,_strRunId);
			}else if("1".equals(strNodeType)){//动作-赋值-返回record
				record = objNowRd;
			}else if("5".equals(strNodeType)){//子流程
				record = objNowRd;
			}else if("2".equals(strNodeType)){//网关-根据网关条件判断获取下一节点-自调用
				String strCustomNodeIds = helper.getColString("S_AUDIT_SEL", objNowRd);//手动选择节点
				if("".equals(strCustomNodeIds)){
					//审批页面,查询下一节点是否网关&审批节点不为空	
					String strNextNodeId = appendConditionSql(request,helper.getColString("S_CONDITION", objNowRd),_strType,_strRunId);
					//默认字段
					strNextNodeId = ("".equals(strNextNodeId)?helper.getColString("S_AUDIT_DEF", objNowRd):strNextNodeId);
					//手动选择--查询当前节点的下一节点是否是网关&手动选择
					record = getNextNodeByCondition(request,strNextNodeId,_tabEx,_strType,_strRunId);
				}else{//手动选择节点
					record = objNowRd;
				}

			}else if("4".equals(strNodeType)){//结束-赋值-返回record
				record = objNowRd;
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
		return record;
	}

	
	public Record getRecordByNodeId(String _strNodeId, TableEx _tabEx) {
		Record rd = null;
		try {
			int iCount = _tabEx.getRecordCount();
			for(int i=0;i<iCount;i++){
				if(_strNodeId.equals(_tabEx.getRecord(i).getFieldByName("I_NODE_ID").value.toString())){
					rd = _tabEx.getRecord(i);
					break;
				}
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
		return rd;
	}

	/**
	 * 查询流程节点信息
	 * @param _strFlowId
	 * @param _strFlowRunId
	 * @param _strNodeId
	 * @return
	 */
	public TableEx queryFlowNodeInfo(String _strFlowId,String _strVersion,String _strNodeId){
		TableEx tableEx = null;
		try {
			String strWhere = " 1=1 ";
			strWhere = strWhere+((_strFlowId==null||"".equals(_strFlowId))?" ":(" and S_FLOW_ID='"+_strFlowId+"' "));
			strWhere = strWhere+((_strVersion==null||"".equals(_strVersion))?" ":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
			strWhere = strWhere+((_strNodeId==null||"".equals(_strNodeId))?" ":(" and I_NODE_ID='"+_strNodeId+"' "));
			tableEx = new TableEx("*", "t_sys_flow_node", strWhere);
		} catch (Exception e) {
			String[] strArrayFlowLog22 = {"333","333","queryFlowNodeInfo","queryFlowNodeInfo","流程:"+_strFlowId,"节点:"+_strNodeId,"版本:"+_strVersion,getErrorInfoFromException(e),""};
			insertFlowLog("1", strArrayFlowLog22);
			e.printStackTrace();
		}
		return tableEx;
	}

	/**
	 * true:有子流程  false:无子流程
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strParentFlowId
	 * @return
	 */
	public boolean queryFlowRunSon(String _strFlowId,String _strVersion,String _strFlowRunId){
		TableEx ex = null;
		boolean bSonFlow = false;
		try {
			StringBuffer sr = new StringBuffer();
			sr.append((_strVersion==null||"".equals(_strVersion))?"":(" and S_AUDIT_VERSION ='"+_strVersion+"'"));
			sr.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID ='"+_strFlowRunId+"'"));
			sr.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_PARENT_ID ='"+_strFlowId+"'"));
			ex = new TableEx("*", "T_SYS_FLOW_RUN", sr.toString());
			if(ex.getRecordCount()>0){
				bSonFlow = true;
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
			
		}finally{
			if(ex!=null){ex.close();}
		}
		return bSonFlow;
	}
	
	/**
	 * 流程已提交,尚未审批,撤回操作
	 * @param _strFlowId
	 * @param _strFlowRunId
	 * @param _strVersion
	 * @param _userCode
	 * @return
	 */
	public boolean backFlowRun(HttpServletRequest _request, String _strFlowId,String _strFlowRunId,String _strVersion,String _userCode){
		TableEx exRun = null;
		TableEx exRun1 = null;
		boolean bFlag = false;
		try {
			exRun = helper.queryFlowRun(_strFlowId, _strFlowRunId);
			int index =Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
			if(index==0)return bFlag;
			
			String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString().split("\\|",-1)[index-1];
			String strNexAuditUser = queryFlowLogBeforeNodeAuditUser(_strFlowId,_strVersion, _strFlowRunId, strNodes);
			String strNexUserArr = exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString().split("\\|",-1)[index-1];
			String strNodeIdNow = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
			if(strNexAuditUser.equals(_userCode)){
				String[] strArrayFlowRunVal = {_strFlowId,_strFlowRunId,strNexUserArr,strNodes,(index-1)+""};
				helper.updateFlowRun(strArrayFlowRunVal, "5");
				bFlag = true;

				/**更新表单*/
				exRun1 = queryFlowNodeInfo(_strFlowId, _strVersion, strNodeIdNow);
				String strField = helper.getColString("S_AUDIT_TABLECONTROL", exRun1.getRecord(0));
	
				if(strField!=null&&!"".equals(strField)){
					_request.setAttribute("NO_sys_flow_state", "99");
					helper.updateTabByFlowSet(_request, "", strField, _strFlowId, _strFlowRunId,new StringBuffer());//strNodeIdNow
				}
			}
		} catch (NumberFormatException e) {
		    MantraLog.fileCreateAndWrite(e);	
			e.printStackTrace();
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exRun.close();
			return bFlag;
		}
	}
	
	/**
	 * 插入运行日志表T_SYS_FLOW_LOG
	 * @param _type 1:插入 2:更新
	 * @param _strArrayValues
	 */
	public void insertFlowLog(String _type,String[] _strArrayValues){
		DBFactory dbf = new DBFactory();
		try {
			if("1".equals(_type)){
				String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_ID,S_AUD_DATE,S_AUDIT_VERSION,S_AUD_USER,S_AUD_STAUS,S_AUD_COMMENT,S_ROLLBACK)";
				_strArrayValues = ApplicationUtils.arrayAddSingleQuotes(_strArrayValues);
				String strTabVal = Arrays.toString(_strArrayValues);
				strTabVal = strTabVal.substring(1,strTabVal.length()-1);
				dbf.sqlExe("insert into T_SYS_FLOW_LOG "+strTabCol+" values("+strTabVal+")", true);
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
	 * 审批人选择节点-查询当前节点之前所有节点
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strNodeIds
	 * @return
	 */
	public String queryFlowLogBeforeAll(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
		String strReturn = "";
		TableEx ex = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" 1=1 ").append("  and T_SYS_FLOW_LOG.S_NODE_ID=T_SYS_FLOW_NODE.I_NODE_ID ").append(" and T_SYS_FLOW_LOG.S_AUD_USER=t_rgxx.SYGZW ");
			sb.append(" and T_SYS_FLOW_LOG.S_FLOW_ID=T_SYS_FLOW_NODE.S_FLOW_ID and T_SYS_FLOW_LOG.S_AUDIT_VERSION=T_SYS_FLOW_NODE.S_AUDIT_VERSION");
			sb.append(" and T_SYS_FLOW_LOG.S_FLOW_ID='"+_strFlowId+"'");
			sb.append(" and T_SYS_FLOW_LOG.S_AUDIT_VERSION='"+_strVersion+"'");
			sb.append(" and T_SYS_FLOW_LOG.S_RUN_ID='"+_strFlowRunId+"'");
			sb.append(" and (T_SYS_FLOW_LOG.S_AUD_STAUS='1' or T_SYS_FLOW_LOG.S_AUD_STAUS='3')");
			sb.append(" GROUP BY S_NODE_ID");
			ex = new TableEx("T_SYS_FLOW_LOG.S_NODE_ID,t_rgxx.SYGMC,T_SYS_FLOW_NODE.S_NODE_NAME", "T_SYS_FLOW_LOG ,T_SYS_FLOW_NODE ,T_RGXX",sb.toString());
			int iCount = ex.getRecordCount(); 
			int flag = -1;
			for(int i=0;i<iCount;i++){
				strReturn = ("".equals(strReturn)?"":(strReturn+"|"))+helper.getColString("S_NODE_ID",  ex.getRecord(i))+","+helper.getColString("SYGMC",  ex.getRecord(i))+","+helper.getColString("S_NODE_NAME",  ex.getRecord(i));
				if(_strNodeId.equals(helper.getColString("S_NODE_ID",  ex.getRecord(i)))){
					flag = i;
				}
			}
			if(flag > -1){//有
				String[] strArrayReturn = strReturn.split("\\|",-1);
				String strReturnTemp = strArrayReturn[0];
				for(int i=1,j=strArrayReturn.length-1;i<j;i++){
					strReturnTemp =strReturnTemp+"|"+strArrayReturn[i];
				}
				strReturn = strReturnTemp;
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return strReturn+"|";
	}
	
	/**
	 * 查询上一节点
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strNodeId
	 * @return
	 */
	public String queryFlowLogBeforeNodeId(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
		String strBeforeNodeId = "";
		TableEx exFlowLog = null;
		try {
			exFlowLog = queryFlowLog(_strFlowId,_strVersion,_strFlowRunId,_strNodeId);
			int iCount = exFlowLog.getRecordCount(); 
			int flag = -1;
			Record rd = null;
			for(int i=0;i<iCount;i++){
				rd =  exFlowLog.getRecord(i);
				if(_strNodeId.equals(helper.getColString("S_NODE_ID",  rd))){
					flag = i;
				}
			}
			if(flag == -1){//无
				strBeforeNodeId =exFlowLog.getRecord(0).getFieldByName("S_NODE_ID").value.toString();
			}else{//有
				strBeforeNodeId =exFlowLog.getRecord(flag+1).getFieldByName("S_NODE_ID").value.toString();
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exFlowLog.close();
		}
		return strBeforeNodeId;
	}
	
	public String queryFlowLogBeforeNodeAuditUser(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){//查询人出了问题
		String strBeforeUser = "";
		TableEx exFlowLog = null;
		try {
			exFlowLog = queryFlowLog(_strFlowId,_strVersion,_strFlowRunId,_strNodeId);
			strBeforeUser = helper.getColString("S_AUD_USER", exFlowLog.getRecord(0));
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			exFlowLog.close();
		}
		return strBeforeUser;
	}
	
	/**
	 * 流程日志之前节点
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @return
	 */
	public TableEx queryFlowLogBefore(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
		TableEx ex = null;
		try {
			//1,查询日志 状态为1:审核通过 3:提交 是否有数据
			String strWhere = " 1=1 and (S_AUD_STAUS='1' or S_AUD_STAUS='3') ";
			strWhere = strWhere+((_strFlowId==null||"".equals(_strFlowId))?" ":(" and S_FLOW_ID='"+_strFlowId+"' "));
			strWhere = strWhere+((_strFlowRunId==null||"".equals(_strFlowRunId))?" ":(" and S_RUN_ID='"+_strFlowRunId+"' "));
			strWhere = strWhere+((_strVersion==null||"".equals(_strVersion))?" ":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
			strWhere = strWhere+((_strNodeId==null||"".equals(_strNodeId))?" ":(" and I_NODE_ID='"+_strNodeId+"' "));
			strWhere = strWhere+" order by T_SYS_FLOW_LOG.S_AUD_DATE desc";
			ex = new TableEx("*", "T_SYS_FLOW_LOG",strWhere);
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return ex;
	}
	
	/**
	 * 流程日志查询
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @return
	 */
	public TableEx queryFlowLog(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
		TableEx ex = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("1=1 ");
			sb.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_ID='"+_strFlowId+"' "));
			sb.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID='"+_strFlowRunId+"' "));
			sb.append((_strVersion==null||"".equals(_strVersion))?"":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
			sb.append(" and (T_SYS_FLOW_LOG.S_AUD_STAUS='1' or T_SYS_FLOW_LOG.S_AUD_STAUS='3')");
			sb.append(" order by T_SYS_FLOW_LOG.S_AUD_DATE desc");
			ex = new TableEx("*", "T_SYS_FLOW_LOG",sb.toString());
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
		return ex;
	}
	
	/**
	 * 判断节点ID在数组中的索引位置
	 * @param _strArrayNodeIds
	 * @param _strNodeId
	 * @return
	 */
	public int getChoiceNode(String[] _strArrayNodeIds,String _strNodeId){
		int index = -1;
		String[] strArrayNode = _strNodeId.split("-");
		for(int a=strArrayNode.length-1,b=0;a>=b;a--){
			if("".equals(strArrayNode[a])){
				continue;
			}
			for(int i=0,j=_strArrayNodeIds.length;i<j;i++){
				if(strArrayNode[a].equals(_strArrayNodeIds[i])){
					index = i;
					break;
				}
			}
		}
		return index;
	}
	
	/**
	 * 字符串数组是否有交集
	 * @param _str1
	 * @param _str2
	 * @return
	 */
	public boolean compareArrayRepeat(String _str1,String _str2){
		if(_str1==null||"".equals(_str1)||_str2==null||"".equals(_str2)){
			return false;
		}else{
			Set<String> set1 = new HashSet<String>(Arrays.asList(_str1.trim().split(",")));
			Set<String> set2 = new HashSet<String>(Arrays.asList(_str2.trim().split(",")));
			set1.retainAll(set2);
			return (set1.size()>0);
		}
	}
	
	/**
	 * 当前日期+day
	 * 
	 * @param date
	 * @param day
	 * @param format
	 * @return
	 */
	public static Date dateCal(String date, int day) {

		Date d = null;
		try {
			d = strSdfYmd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	/**
	 * 字符串截取，重新拼接
	 * @param _str
	 * @param _strReplace
	 * @param index
	 * @return
	 */
	public String getAuditStrArry(String _str,String _strReplace,int index){
		String[] strArray = _str.split("\\|", -1);
		String strResult = strArray[0];
		for(int i=1;i<index;i++){
			strResult = strResult +"|"+strArray[i];
		}
		
		return strResult+"|"+_strReplace;
	}
	
	public String getAuditStrArrySave(String _str,String _strReplace,int index){
		String[] strArray = _str.split("\\|", -1);
		String strResult = strArray[0];
		for(int i=1;i<=index;i++){
			strResult = strResult +"|"+strArray[i];
		}
		
		return strResult+"|"+_strReplace;
	}
	
	public Object convertToCode(ScriptEngine engine, String _str) {
		Object result = null;
		try {
			result = engine.eval(_str);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:"+ result);
		return result;
	}  
	 
	public void shanchutable(String _strTable){
		DBFactory dbf = new DBFactory();
		try {
				dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID='"+_strTable+"'",true);
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			dbf.close();
		}
	}
	
	public void getTest(HttpServletRequest req){
		System.out.println("*****************");
		String[] strArrayFlowLog22 = {"333","xx","xx",new Date()+"","xx","","start","xx",""};
		insertFlowLog("1", strArrayFlowLog22);
	}
	
	public void flowOverDelMsg( String S_RUN_ID, String S_FLOW_ID, String S_AUTO_VER) {
		DBFactory dbf = new DBFactory();
		try {
			dbf.sqlExe("delete from T_MSG_RECORDS where S_LCID='"+S_FLOW_ID+"' AND S_YXID='"+S_RUN_ID+"' ;", false);
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(dbf!=null) {
				dbf.close();
			}
		}
	}
	
    public String selDAteRet(String col,String tableName,String cond) {
		String retStr="";
		TableEx tb = null;
		Record record=null;
		try {
			tb = new TableEx(col,tableName,cond);
			if(tb.getRecordCount()>0) {
				record = tb.getRecord(0);
				 if (record.getFieldByName(col).value != null) {
					 retStr = record.getFieldByName(col).value.toString();
					}
			}
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			tb.close();
		}
		return retStr;
	}
}
