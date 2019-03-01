	package com.bfkc.process;
	
	import java.io.PrintWriter;
	import java.io.StringWriter;
	import java.lang.reflect.InvocationTargetException;
	import java.lang.reflect.Method;
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
	import java.util.UUID;
	
	import javax.script.ScriptEngine;
	import javax.script.ScriptException;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpSession;
	
	import com.timing.impcl.MantraLog;
	import com.yulongtao.db.DBFactory;
	import com.yulongtao.db.FieldEx;
	import com.yulongtao.db.Record;
	import com.yulongtao.db.TableEx;
	import com.yulongtao.util.EString;
	import com.timing.impcl.MantraUtil;
	
	public class ProcessRunOperation {
		
		public static SimpleDateFormat strSdfYmd =  new SimpleDateFormat("yyyy-MM-dd");
		public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		private static String[] arrVarNames=new String[]{"username","user","role","branchid","date","dataset","username","splitbranchid","usercount","userip","userlogindate","userlogindatehm"};
		private static String[] arrVarValues=new String[]{"SYS_STRCURUSERNAME","SYS_STRCURUSER","SYS_STRROLECODE","SYS_STRBRANCHID","SYS_CURDATE","DATASET","SYS_STRCURUSERNAME","SYS_BRANCHID_SPLIT","SYS_USER_COUNT","SYS_STRCURUSER_IP","SYS_USER_LOGIN_DATE","SYS_CURDATE"};
		
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
	//		for(int i=0,j=arrVarValues.length;i<j;i++){
	//			if(_str.equals(arrVarValues[i])){
	//				strVal = session.getAttribute(arrVarValues[i]);
	//				break;
	//			}
	//		}
			return strVal==null?_str:strVal.toString();
		}
		
		public StringBuffer sb = new StringBuffer();
		
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

			if("".equals(_strField)){return;}
			String[] strArrayTable = strField.split("`");
			
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
				
					String strAuditState = _request.getParameter("NO_sys_flow_state");//0驳回1通过
					if("0".equals(strAuditState)){//驳回取值
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
				}
			}
			

//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "mapCon: " + mapCon);
//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "map: " + map);
			try {
			
    			for (String key : map.keySet()) {
    			    	if(dbf==null){
    			    	    dbf=new DBFactory();
    			    	}
    			    //	update T_DQYZGZP set S_GZPZT = 'GZPZT022' , S_GZXKRQM_NAME = '刘小锋' , S_GZXKRQM = 'liuxiaofeng' where S_RUN_ID='5NwQQiikQlq7Dk4PVReLoQ'
    			    //MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "update " + key + " set " + map.get(key) + " where S_RUN_ID='" + _strRunId + "' "+mapCon.get(key));
    				dbf.sqlExe("update " + key + " set " + map.get(key) + " where S_RUN_ID='" + _strRunId + "' "+mapCon.get(key), true);
    				
    				//dbf.sqlExe("update T_DQYZGZP set S_GZPZT = 'GZPZT022' , S_GZXKRQM_NAME = '刘小锋' , S_GZXKRQM = 'liuxiaofeng' where S_RUN_ID='5NwQQiikQlq7Dk4PVReLoQ'", true);
    			}
    			
    		} catch (Exception e) {
    		    MantraLog.fileCreateAndWrite(e);
    // 			String[] strArrayFlowLog22 = {"333","","",new Date()+"","","","updateTabByFlowSet",getErrorInfoFromException(e)};
    // 			insertFlowLog("1", strArrayFlowLog22);
    			e.printStackTrace();
    		}finally{
    			if(dbf!=null){dbf.close();}
    		}
		}
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
						if("3".equals(getColString("I_TYPE", record))){//开始数量
							strAuditMsgs = getColString("S_AUDIT_TZXX", record);
							strStartNodeBak = getColString("I_NODE_ID", record);
							//多个开始节点判断是否发节点
							if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,getColString("S_AUDIT_BRANCH", record),getColString("S_AUDIT_ROLE", record),getColString("S_AUDIT_USER", record))==true){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
							}else if(querySqRole(getColString("S_AUDIT_SQRYATTR", record),getColString("S_AUDIT_SQRY", record), null, strFlowRunId,strStartUserRole)){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
							}
						}else if("4".equals(getColString("I_TYPE", record))){
							strEndNodes=strEndNodes+getColString("I_NODE_ID", record)+",";
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
	//				String strAuditTg = "";//是否跳过
					String strIsOver = "0";//是否结束
					Record rd = null;//获取下一节点对象
					String strEndFlag = "";
					String strAudSel = "";//自选流程节点
					String strFlowPj = "";//附加票据
					/**3 循环拼接参数*/
					while(!"4".equals(strEndFlag)){
							rd = getNextNodeByCondition(null,strStartNode,tableEx,"2",strFlowRunId);
							if(rd==null){break;}
							strEndFlag = getColString("I_TYPE", rd);
							String strCustomNodeIds = getColString("S_AUDIT_SEL", rd);//手动选择节点
							if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//网关判断是否手动选择节点
								strAudSel = strAudSel+strCustomNodeIds;
								break;					
							}
							strAudSel = strAudSel+"|"+"";
							String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),null,rd,strFlowRunId);
							if("5".equals(strEndFlag)){//子流程,存储子流程流程号/版本号运行号/表单ID
								strNodeAudit = "S";
							}
							strSonFlow = (strSonFlow+"|")+getColString("S_FLOW_SON", rd);
							strAuditArrayyq =(strAuditArrayyq+"|") +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//所有逾期  所有逾期操作
							strAuditMsgs =(strAuditMsgs+"|")+getColString("S_AUDIT_TZXX", rd);//所有消息模版
							strAuditNodes = (strAuditNodes+"|")+getColString("I_NODE_ID", rd);
							strFlowPj =(strFlowPj+"|")+getColString("S_AUDIT_FSPJ", rd);//附加票据
							//是否跳过 所有审批驳回 所有审批驳回处理 子流程
							//类型,值5,通过人数6,驳回人数7|
							strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";
							strStartNode = getColString("S_CHILD_ID", rd);
							if("".equals(strNodeAudit)){
								strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//是否跳岗
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
						updateFlowRun(strArrayFlowRun,"1");
					
						String strDate = strSdfYmdHms.format(new Date());
						String  strAuditComment = "";
						/**插入流程日志*/
						String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment};
						insertFlowLog("1", strArrayFlowLog);
						/**发送消息*/
						sendMsg(strAuditMsgs.split("\\|",-1)[0],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,null,strFlowType,strPageCode,strTab);
	//					if(strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
	//						reflectMothedInvoke(strClassName, strMethodName, request);
	//					}
			}catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				b=false;
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,"","start",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}finally{
				tableEx.close();
				tableEx1.close();
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
				/*
				insertFlowLog("1", strArrayFlowLog1);
				*/
	//			DBFactory dbf = new DBFactory("orcl","extop.tpddns.cn" , "gnnczs", 2, "gnnczs", "1521");
	//			DBFactory db = new DBFactory(aStrDBName, aStrDBIP, aDBUser, iDBTypeTemp, aStrPassword, aStrDBPort);
				/**接收数据*/
				String strStartUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//发起人
				String strStartUserRole = request.getSession().getAttribute("SYS_STRROLECODE").toString();//发起人角色
				String strStartUserBranch = request.getSession().getAttribute("SYS_STRBRANCHID").toString();//发起人组织
				
				strFlowId = request.getParameter("NO_sys_flow_id");//流程ID
				strVersion = request.getParameter("NO_sys_flow_Ver");//版本号
				 strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//运行
				String strFlowType = request.getParameter("NO_sys_S_flow_type");//0:子流程 1:主流程 默认主流程
				
				//删除通知消息
				DelMsg( strFlowRunId);
				
				/**子流程*/
				strFlowType = "1";
				
				String strPageCode = "";//页面代码
				if(_strSonFlowId!=null&&!"".equals(_strSonFlowId)){
					strFlowId = _strSonFlowId;
					String[] strArraySon = queryFlowMaiByFlowId(_strSonFlowId,"").split(",",-1);
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
					//_sb.append(" iCount: "+iCount);
					//_sb.append("sql:"+sb.toString());
					if(iCount<1){
						return false;
					}
					/**1 查找开始节点*/
					Record record = null;
					for(int i=0;i<iCount;i++){
						record =  tableEx.getRecord(i);//1 动作 3 开始   2网关  4结束
						if("3".equals(getColString("I_TYPE", record))){//开始数量
							strAuditMsgs = getColString("S_AUDIT_TZXX", record);
							strStartNodeBak = getColString("I_NODE_ID", record);
							//多个开始节点判断是否发节点
							if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,getColString("S_AUDIT_BRANCH", record),getColString("S_AUDIT_ROLE", record),getColString("S_AUDIT_USER", record))==true){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
								if(strPageCode==null||"".equals(strPageCode)){
									strPageCode = getColString("S_PAGECODE", record);
								}
							}else if(querySqRole(getColString("S_AUDIT_SQRYATTR", record),getColString("S_AUDIT_SQRY", record), request, strFlowRunId,strStartUserRole)){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
								if(strPageCode==null||"".equals(strPageCode)){
									strPageCode = getColString("S_PAGECODE", record);
								}
							}
	//						if("".equals(strStartNode)||strStartNode==null){
	//							strStartNodeBak = getColString("I_NODE_ID", record);
	//						}
						}else if("4".equals(getColString("I_TYPE", record))){
							strEndNodes=strEndNodes+getColString("I_NODE_ID", record)+",";
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
					String strClassName = getColString("S_AUDIT_PAGENAME", tableEx1.getRecord(0));
					String strMethodName = getColString("S_AUDIT_CLASSNAME", tableEx1.getRecord(0));
					String strField = getColString("S_AUDIT_TABLECONTROL", tableEx1.getRecord(0));
					 _sb.append(strField);
					if(strField!=null&&!"".equals(strField)){
						updateTabByFlowSet(request, "", strField, strFlowRunId,_sb);//strNodeIdNow
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
	//				String strAuditTg = "";//是否跳过
					String strIsOver = "0";//是否结束
					Record rd = null;//获取下一节点对象
					String strEndFlag = "";
					String strAudSel = "";//自选流程节点
					String strFlowPj = "";//附加票据
					/**3 循环拼接参数*/
					while(!"4".equals(strEndFlag)){
							rd = getNextNodeByCondition(request,strStartNode,tableEx,"1",strFlowRunId);
							if(rd==null){break;}
							strEndFlag = getColString("I_TYPE", rd);
							String strCustomNodeIds = getColString("S_AUDIT_SEL", rd);//手动选择节点
							if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//网关判断是否手动选择节点
								strAudSel = strAudSel+strCustomNodeIds;
								break;					
							}
							strAudSel = strAudSel+"|"+"";
							String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),request,rd,strFlowRunId);
							if("5".equals(strEndFlag)){//子流程,存储子流程流程号/版本号运行号/表单ID
								strNodeAudit = "S";
							}
							strSonFlow = (strSonFlow+"|")+getColString("S_FLOW_SON", rd);
							strAuditArrayyq =(strAuditArrayyq+"|") +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//所有逾期  所有逾期操作
							strAuditMsgs =(strAuditMsgs+"|")+getColString("S_AUDIT_TZXX", rd);//所有消息模版
							strAuditNodes = (strAuditNodes+"|")+getColString("I_NODE_ID", rd);
							strFlowPj =(strFlowPj+"|")+getColString("S_AUDIT_FSPJ", rd);//附加票据
							//是否跳过 所有审批驳回 所有审批驳回处理 子流程
							//类型,值5,通过人数6,驳回人数7|
							strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";
							strStartNode = getColString("S_CHILD_ID", rd);
							if("".equals(strNodeAudit)){
								strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//是否跳岗
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
						updateFlowRun(strArrayFlowRun,"1");
					
						String strDate = strSdfYmdHms.format(new Date());
						String  strAuditComment = "";
						/**插入流程日志*/
						String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment};
						insertFlowLog("1", strArrayFlowLog);
						/**发送消息*/
						sendMsg(strAuditMsgs.split("\\|",-1)[0],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowType,strPageCode,strTab);
						if("S".equals(strNextAuditUser)){//判断是否子流程---当前节点审批人是否S
							//启动子流程
	//						processStart(request, _sb,strSonFlow.split("\\|",-1)[strNextAuditUserIndex+1],strFlowId);
						}
						//修改值
	//					String strField1 = getColString("S_AUDIT_TABLECONTROL", queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow).getRecord(0));
	//					if(strField1!=null&&!"".equals(strField1)){
	//						updateTabByFlowSet(request, "", strField1, strFlowRunId,_sb);//strNodeIdNow
	//						processSave(request);
	//					}
						if(strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
							reflectMothedInvoke(strClassName, strMethodName, request);
						}
			}catch (Exception e) {
				MantraLog.fileCreateAndWrite(e);
				b=false;
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,"","start",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
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
	//		if("".equals(_strNextAuditUser)||_strNextAuditUser==null){
				try {
					exFlowNode = queryFlowNodeInfo(_strFlowId, _strVersion, _strNodeIdNext);
					String strExCon="";
					strExCon = getColString("S_AUDIT_THDJR", exFlowNode.getRecord(0));
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
	//		}
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
	//			strRolePd 表名|字段名|主键名称
				String[] strArrayPd = strRolePd.split("\\|",-1);
				ex = dbf.query(new StringBuffer().append(" select ").append(strArrayPd[1]).append(" from ").append(strArrayPd[0]).append(" where 1=1 and ").append("S_RUN_ID").append("='").append(_strFlowRunId).append("'").toString());
				strRole = getColString(strArrayPd[1], ex.getRecord(0));
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
		 * 节点运行
		 * @param request
		 * @return
		 */
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
				exRun = queryFlowRun(strFlowId,strVersion,strFlowRunId);
				//_sb.append(strFlowId+"    "+strVersion+"    "+strFlowRunId);
				Record record = exRun.getRecord(0);
				String strMsgs=getColString("S_AUDIT_MSG",record);
				String strYqs=record.getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
				String strAuditUsers=record.getFieldByName("S_AUDIT_ARRAY").value.toString();
				String strNodes=record.getFieldByName("S_AUDIT_NODES").value.toString();
				int index = Integer.parseInt(record.getFieldByName("S_AUDIT_INDEX").value.toString());//索引
				String strLaunchUser = record.getFieldByName("S_LAUNCH_USER").value.toString();
	//			String strLaunchBranch = record.getFieldByName("S_LAUNCH_BRANCH").value.toString();
				String strLaunchDate = record.getFieldByName("S_LAUNCH_DATE").value.toString();
				String strNodeIdNow = record.getFieldByName("S_NODE_CODE").value.toString();//当前节点
				String strOther = record.getFieldByName("S_AUDIT_OTHER").value.toString();//其他
				String strIsOverRun = record.getFieldByName("I_ISOVER").value.toString();//是否完成
				String strAudSel = record.getFieldByName("S_AUDIT_SEL").value.toString();//手动选择子节点
				String strAudOver = record.getFieldByName("S_AUD_OVER").value.toString();//结束节点
				String strFlowSon = record.getFieldByName("S_FLOW_SON").value.toString();//子流程
				String strFlowtype = record.getFieldByName("S_FLOW_TYPE").value.toString();//流程类型
				String strTab = getColString("S_TAB", record);//表名
				boolean bTranFlowSonFlag=false;
				
		
				
				if("1".equals(strIsOverRun)){//判断完成返回
					return b;
				}
				
				/**更新表单*/
				exRun1 = queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow);
				String strClassName = getColString("S_AUDIT_PAGENAME", exRun1.getRecord(0));
				String strMethodName = getColString("S_AUDIT_CLASSNAME", exRun1.getRecord(0));
				String strAuditStateBak = request.getParameter("NO_sys_flow_state");
				String strField = getColString("S_AUDIT_TABLECONTROL", exRun1.getRecord(0));
	//			String[] strArrayFlowLog1 = {"333","333","1","1","1","1","1",strField};
	//			insertFlowLog("1", strArrayFlowLog1);
	
				if(strField!=null&&!"".equals(strField)){
					updateTabByFlowSet(request, "", strField, strFlowRunId,_sb);//strNodeIdNow
					if(strCustomNodeId==null||"".equals(strCustomNodeId)){
		            	processSave(request);
		            	TableEx exRunGet =queryFlowRun(strFlowId,strVersion,strFlowRunId);
		            	strAuditUsers=exRunGet.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
		            	if(exRunGet!=null){
		            	    exRunGet.close();
		            	}
					    
		            }
				}
				
				if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){
					exRun2 = queryFlowNodeInfo(strFlowId, strVersion, strCustomNodeId);
	           	 //节点名称相同-类型为2,手动选择节点
	if(strCustomNodeId.equals(getColString("I_NODE_ID", exRun2.getRecord(0)))&&"2".equals(getColString("I_TYPE", exRun2.getRecord(0)))&&!"".equals(getColString("S_AUDIT_SEL", exRun2.getRecord(0)))){
						 //更新运行表
						 String strDef = getColString("S_AUDIT_SEL",exRun2.getRecord(0));
						 String[] strRunAudSelArry = strAudSel.split("\\|",-1);
						 strRunAudSelArry[index]=strDef;
						 String[] strArrayFlowRunVal = {strFlowId,strFlowRunId,getStringArryToString(strRunAudSelArry)};
						 updateFlowRun(strArrayFlowRunVal, "6");
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
	//							String strType ="T";
	//							String strSql ="update T_DQEZGZP set T_DQEZGZP.S_SPJG = -*-BHG-*- where T_DQEZGZP.S_ID=-*-<<T_DQEZGZP.S_ID>>-*- ";
	//							String strCon ="T_DQEZGZP.S_ID";
								String strType =getColString("S_CHILD_TYPE", rd).trim();
								String strSql =getColString("S_CHILD_TRANSQL", rd).trim();
								String strCon =getColString("S_CHILD_TRANCON", rd).trim();
								
	//							strSql = strSql.replace("-*-", "'");
	//							dbf = new DBFactory();
	//							dbf.sqlExe("update T_DQEZGZP set T_DQEZGZP.S_SPJG = 'BHG' where T_DQEZGZP.S_RUN_ID='"+strFlowRunId+"'", true);
								
								if("T".equals(strType)){//事物
									bTranFlowSonFlag = true;
									index = index+1;
									while("S".equals(strArrayAuditUsers[index])&&"T".equals(strType)){
										strSql = strSql.replace("-*-", "'");
										updateFormSql(strSql,strCon,strType,strFlowRunId);
										index++;
										if(exNode!=null){exNode.close();exNode = null;}
										exNode = queryFlowNodeInfo(strFlowId, strVersion, strArrayNodes[index]);
										strSql = getColString("S_CHILD_TRANSQL", exNode.getRecord(0)).trim();
										strCon = getColString("S_CHILD_TRANCON", exNode.getRecord(0)).trim();
										strType = getColString("S_CHILD_TYPE", exNode.getRecord(0)).trim();
									}
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strAuditUser = strArrayAuditUsers[iNextAuditUserIndex];
								}else{//子流程
									index++;
									iNextAuditUserIndex = index;
									strAuditUser = "子流程启动";
									//启动子流程
	//							processStart(request, _sb,strFlowSon.split("\\|",-1)[index],strFlowId);
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
	//										strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";
	
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
	//										String strAuditReject = strOtherArrayNow[1];//驳回
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
				updateFlowRun(strArrayFlowRun,"2");
				/**插入流程日志*/
				String strNowDate = strSdfYmdHms.format(new Date());
				if(bTranFlowSonFlag){
					strAuditUser =request.getSession().getAttribute("SYS_STRCURUSER").toString();
				}
				String[] strArrayFlowLog = {strFlowId,strFlowRunId,strNodeIdNow,strNowDate,strVersion,strAuditUser,strAuditState,strAuditComment};
				insertFlowLog("1", strArrayFlowLog);
				/**更新当前审批日志为空*/
				updateSendMsgZt(dbf,strFlowRunId,strAuditUser,strFlowId);
				String strPageCode="";
				if("1".equals(strIsOver)){
					String strFlowParentId= getColString("S_FLOW_PARENT_ID", exRun.getRecord(0)); //当前子流程父流程ID
					if(strFlowParentId!=null&&!"".equals(strFlowParentId)){//子流程结束,0:子流程 1:主流程 默认主流程
						//查询运行ID号,父流程号相等的所有子流程是否全部完成,如果全部完成,则父流程进行下一步,否则,保持不变
						boolean bIsOverSameFlow = queryFlowRunIsOverSameLevel(strFlowRunId,strFlowParentId);//true:完成 false:未完成
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
							updateFlowRun(strArrayFlowRunParent,"4");
	//						发送父流程消息
							strPageCode  = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOverParent,strFlowId,strVersion,strFlowRunId,strNodesParent[indexParent],request,strFlowtype,"",strTab);
						}else{
							//子流程完成,并列子流程未完成
							strNextAuditUser = strArrayAuditUsers[0];//子流程完成,并列子流程未完成,发送消息给发起人
							strFlowtype = "0";//当前流程为子流程
							strPageCode = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOverParent,strFlowId,strVersion,strFlowRunId,"",request,strFlowtype,"",strTab);
						}
	
					}else{//主流程结束
						strNextAuditUser=strArrayAuditUsers[0];//审批结束,接收人
						strPageCode = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowtype,"",strTab);					
					}
				}else{
					strPageCode = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowtype,"",strTab);
				}
				
				if("1".equals(strAuditStateBak)&&strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
					reflectMothedInvoke(strClassName, strMethodName, request);
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
// else if("1516168904786".equals(strPageCode)){//检修计划分解-外委
// 						String strSid = request.getParameter("S_ID");
// 						DBFactory df = new DBFactory();
// 						TableEx wwEx = df.query("select * from T_JXZJHFJ  where S_ZJ='"+strSid+"'");
// 						Record rd = wwEx.getRecord(0);
// 						if("true".equals(getColString("S_SFWW", rd))){//外委
// 							String uuid = EString.generId();
// 							String strJxlx =getColString("S_JXLX", rd);//检修类型
// 							StringBuffer sbr = new StringBuffer();
// 							String strFlowVersion = new com.timing.impcl.MantraUtil().getFlowVer(strPageCode,getColString("S_ZZ", rd));
// 	sbr.append("insert into T_WWXM (SYS_FLOW_VER,S_RUN_ID,S_HTH,S_XMMC,S_GQKSSJ,S_GQJSSJ,S_WWDW,S_WWDWID,S_YFFZR,S_JFFZR,S_JXJG,S_ZZ,S_BGRQ,S_ZDR,S_ZDSJ,S_XGR,S_XGSJ,S_BS_JD,S_ID,S_FJID) ");
// 	sbr.append("select '"+strFlowVersion+"' AS 'SYS_FLOW_VER', '"+EString.generId()+"' AS 'S_RUN_ID','' AS 'S_HTH',S_MC  AS 'S_XMMC',S_KSSJ  AS 'S_GQKSSJ',S_JSSJ AS 'S_GQJSSJ',S_WWDW AS 'S_WWDW',S_WWDWID AS 'S_WWDWID',S_YFZR AS 'S_YFFZR',S_FZR AS 'S_JFFZR','true' AS 'S_JXJG',S_ZZ AS 'S_ZZ','"+EString.getCurDate()+"'  AS 'S_BGRQ',S_ZDR AS 'S_ZDR','"+EString.getCurDate()+"' AS 'S_ZDSJ',S_ZHXGR AS 'S_XGR','"+EString.getCurDate()+"' AS 'S_XGSJ','' AS 'S_BS_JD','"+uuid+"' AS 'S_ID',S_ZJ AS 'S_FJID' from T_JXZJHFJ where T_JXZJHFJ.S_ZJ='");
// 	sbr.append(strSid);
// 	sbr.append("';");
// 	df.sqlExe(sbr.toString(), true);
// 	sbr = new StringBuffer();
// 	sbr.append("insert into T_WWXMFB (S_CBXM,S_SGSD,S_Z,S_ID,S_RID,S_CBDW,S_LXFS,S_XTLL) ");
// 	sbr.append("select S_JXXM AS 'S_CBXM',S_KSRQ AS 'S_SGSD',S_JSQR AS 'S_Z',S_ID AS 'S_ID','"+uuid+"' AS 'S_RID','"+getColString("S_WWDW", rd)+"' AS 'S_CBDW','"+getColString("S_LXFS", rd)+"' AS 'S_LXFS','"+getColString("S_XTLXT", rd)+"' AS 'S_XTLL' from T_ZYJXJHF where T_ZYJXJHF.S_FIDHZFJ='");
// 	sbr.append(strSid);
// 	sbr.append("';");
// 							df.sqlExe(sbr.toString(), true);
// 							wwEx.close();
// 							df.close();
// 						}
// 					}
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
				// 	else if("1515723789958".equals(strPageCode)){
				// 		new com.page.method.Fun().MeasuresToolEntr(request);
				// 	}
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
				
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,strAuditUser,"run",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
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
	//			_dbf.sqlExe("update t_msg_records set S_ZT=1 where S_YXID='"+_strFlowRunId+"'", true);
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
	//			if(_dbf!=null){_dbf.close();}
				// String[] strArrayFlowLog22 = {"333","333","updateSendMsgZt","updateSendMsgZt","updateSendMsgZt","_strFlowRunId",_strFlowRunId,getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}
		}
	
		public void updateFormSql(String strSql, String strCon, String strType,String strFlowRunId) {
			//查询表单
			DBFactory dbf = new DBFactory();
			TableEx exForm = null;
			String sql = "";
			try {
	//			T_DQEZGZP.S_ID
				strCon = strCon.trim();
				String strTable = strCon.substring(0, strCon.indexOf("."));
				String strConTem = strCon.replace(strTable+".", "");
				exForm = dbf.query("select "+strConTem +" from "+strTable+" where S_RUN_ID='"+strFlowRunId+"'");
				//替换数据
				String[] strArrayCon = strConTem.split(",");
				for(int s1 = 0,s2 = strArrayCon.length;s1<s2;s1++){
					strSql = strSql.replace("<<"+strTable+"."+strArrayCon[s1]+">>", getColString(strArrayCon[s1].trim(),exForm.getRecord(0)));
				}
				//执行sql
				String[] strArryCon = strSql.split(";");
				for(int s3=0,s4=strArryCon.length;s3<s4;s3++){
					sql = strArryCon[s3];
					if(!"".equals(sql)){
						dbf.sqlExe(sql, true);
					}
				}
				String[] strArrayFlowLog22 = {"333","333","",new Date()+"","运行ID"+strFlowRunId,"","updateFormSql",sql};
				insertFlowLog("1", strArrayFlowLog22);
				
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333","333","",new Date()+"","运行ID"+strFlowRunId,"","updateFormSql",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
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
		
		/**
		 * 查询并列子流程
		 * @param strFlowRunId
		 * @param strFlowParentId
		 * @return
		 */
		private boolean queryFlowRunIsOverSameLevel(String strFlowRunId, String strFlowParentId) {
			TableEx exParent = null;
			boolean bIsOver = true;
			try {
				exParent = new TableEx("I_ISOVER","T_SYS_FLOW_RUN"," S_RUN_ID='"+strFlowRunId+"' and S_FLOW_PARENT_ID='"+strFlowParentId+"'");
				int iCount = exParent.getRecordCount();
				for(int i=0;i<iCount;i++){
					if("0".equals(getColString("I_ISOVER", exParent.getRecord(i)))){
						bIsOver = false;
					}
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				if(exParent!=null)
					exParent.close();
			}
			return bIsOver;
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
					exRun = queryFlowRun(strFlowId,strVersion,strFlowRunId);
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
					int strNextAuditUserIndex = index+1;//下一审批节点索引,发起人默认为1
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
					boolean bFlag = true;
					 if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){//
						 strStartNode =strCustomNodeId;
						 //当前节点是网关且手动选择
						 //查询流程节点
	//					 int iCount = tableEx.getRecordCount();
	//					 Record rd1 = null;
	//					 for(int i=0;i<iCount;i++){
	//						 rd1 = tableEx.getRecord(i);
	//						 //节点名称相同-类型为2,手动选择节点
	//						 if(strCustomNodeId.equals(getColString("I_NODE_ID", rd1))&&"2".equals(getColString("I_TYPE", rd1))&&!"".equals(getColString("S_AUDIT_SEL", rd1))){
	//							 //更新运行表
	//							 String strDef = getColString("S_AUDIT_SEL",rd1);
	//							 String[] strRunAudSelArry = strRunAudSel.split("\\|",-1);
	//							 strRunAudSelArry[index]=strDef;
	//							 String[] strArrayFlowRunVal = {strFlowId,strFlowRunId,getStringArryToString(strRunAudSelArry)};
	//							 exRun.close();
	//							 updateFlowRun(strArrayFlowRunVal, "6");
	//							 bFlag = false;
	//							 break;
	//						 }
	//					 }
					 }else{
				            strStartNode = strNodesArray[index+1];
					 }
	//				 if(!bFlag){
	//					 return false;
	//				 }
					 //第一次选择节点,没问题 |||7,6,8||||12,13,14,15
					 //第一次选择完了节点,出现了问题
					 //|||
					while(!"4".equals(strEndFlag)){
							rd = getNextNodeByCondition(request,strStartNode,tableEx,"2",strFlowRunId);
							if(rd==null){break;}
	
							strEndFlag = getColString("I_TYPE", rd);
							String strCustomNodeIds = getColString("S_AUDIT_SEL", rd);//手动选择节点
							if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//网关判断是否手动选择节点
								strAudSel = strAudSel+strCustomNodeIds;
								break;
							}
							String strNodeAudit = queryAuditPerson(strLaunchUser,strLaunchBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),request,rd,strFlowRunId);

							if("5".equals(strEndFlag)){//子流程,存储子流程流程号/版本号运行号/表单ID
								strNodeAudit = "S";
							}
							strAudSonFlow = (icount==0?"":(strAudSonFlow+"|"))+getColString("S_FLOW_SON", rd);
							strAudSel = (icount==0?"":(strAudSel+"|"))+strCustomNodeIds;
							strAuditArrayyq =(icount==0?"":(strAuditArrayyq+"|")) +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//所有逾期  所有逾期操作
							strAuditMsgs =(icount==0?"":(strAuditMsgs+"|"))+getColString("S_AUDIT_TZXX", rd);//所有消息模版
							strAuditNodes = (icount==0?"":(strAuditNodes+"|"))+getColString("I_NODE_ID", rd);
							strAuditOther =(icount==0?"":(strAuditOther+"|")) +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";;
							strStartNode = getColString("S_CHILD_ID", rd);
							strAudFlowPj =(icount==0?"":(strAudFlowPj+"|"))+getColString("S_AUDIT_FSPJ", rd);//附加票据
							//是否跳过 所有审批驳回 所有审批驳回处理 子流程
							if("".equals(strNodeAudit)){
								strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//跳岗
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
					System.out.println(strRunAudSel);
						/**6 跳岗 1:是*/
	//					strNextAuditUserIndex = getNodesInfo(strAuditUsersRun,strOther,strNextAuditUserIndex);
	//					strNextAuditUser =strAuditUsersRun.split("\\|",-1)[strNextAuditUserIndex];
	//					strNodeIdNext =strNodes.split("\\|")[strNextAuditUserIndex];
						/**流程是否结束*/
	//					if("".equals(strAudSel.split("\\|",-1)[index])){//非子节点停滞,判断是否结束
	//						strIsOver = ((strNextAuditUserIndex+1) == strNodesArray.length)?"1":strIsOver;
	//					}
					/**判断当前节点审批人是否表单取值*/
					String strNextAuditUser = queryAuditPersonIsColumn(strAuditUsersRun.split("\\|",-1)[index+1], strFlowRunId, strFlowId, strVersion, strNodes.split("\\|",-1)[index+1]);
					String[] strArryAuditUsers = strAuditUsersRun.split("\\|",-1);
					

					strArryAuditUsers[index+1]=strNextAuditUser;
					strAuditUsersRun = getStringArryToString(strArryAuditUsers);

						/**7 更新运行表 4个数组*/
						String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strMsgs,strYqs,strAuditUsersRun,strNodes,strOther,strIsOver,strRunAudSel,strSonFlow,strFlowPj};
						updateFlowRun(strArrayFlowRun,"3");
			}catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				b = false;
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,"","save",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				
				e.printStackTrace();
			}finally{
				exRun.close();
				if(tableEx!=null){tableEx.close();}
				return b;
			}
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
				exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
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
			
	//		("\r|\n", "");
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
	//			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
				if(exRun.getRecordCount()>0){
					String strCustomNodes = getColString("S_AUDIT_SEL", exRun.getRecord(0));
					String strNodeIds = getColString("S_AUDIT_NODES", exRun.getRecord(0));
					int index = Integer.parseInt(getColString("S_AUDIT_INDEX", exRun.getRecord(0)));
					strCustomNodes = strCustomNodes.split("\\|",-1)[index];
					if(!"".equals(strCustomNodes)){
						exTRGXX = new TableEx("I_NODE_ID,S_NODE_NAME,I_TYPE","T_SYS_FLOW_NODE","1=1  and I_NODE_ID in("+strCustomNodes+")" +"and S_FLOW_ID='"+strFlowId+"' and S_AUDIT_VERSION ='"+strVersion+"'");
						Record rd = null;
						for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
							rd = exTRGXX.getRecord(i);
							strResult = ("".equals(strResult)?"":(strResult+"|"))+getColString("I_NODE_ID", rd)+","+getColString("S_NODE_NAME", rd)+","+getColString("I_TYPE", rd);
						}
					}
	//				else if("S".equals(strNodeIds.split("\\|",-1)[index+1])){
	//					String[] strFlowSonArray = getColString("S_FLOW_SON", exRun.getRecord(0)).split("\\|",-1);
	//					index++;
	//					strResult = "SON"+"|"+strFlowSonArray[index]+"|"+strFlowRunId+"|"+queryFlowMaiByFlowId(strFlowSonArray[index],request.getParameter("strOrgId"));
	//				}
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
	//			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
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
//        			    strReject = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString().split("\\|",-1)[index].split(",")[1];
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
	//			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
				if(exRun.getRecordCount()>0){
					//S_AUDIT_OTHER S_AUDIT_ARRAYYQ S_AUDIT_INDEX
					int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
					String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
					String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
					String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
	//				String strLaunchBranch = exRun.getRecord(0).getFieldByName("S_LAUNCH_BRANCH").value.toString();
					String strNodeIdNow = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
					String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//其他
	//				strFlowPj = exRun.getRecord(0).getFieldByName("S_AUDIT_FSPJ").value.toString();//附属票据
					String[] strArrayAud = strAuditUsers.split("\\|",-1);
					
					int iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strNodeIdNow);
					
					if(strArrayAud[iNextAuditUserIndex].split(",").length>1){//多个审批人
						if("ZD".equals(strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4])){//指定模式
							exTRGXX = new TableEx("SYGZW,SYGMC","T_RGXX","1=1  and SYGZW in("+strArrayAud[iNextAuditUserIndex]+")");//and SROLECODE in("+_strRoleIds+")
							Record  rd = null;
							for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
								rd = exTRGXX.getRecord(i);
								strResult.append(getColString("SYGZW", rd)).append(",").append(getColString("SYGMC", rd)).append("|");
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
//				_strFlowRunId=request.getParameter("NO_sys_flow_id");
			}
	//		SYS_STRBRANCHID机构ＩＤ
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
//switch (_strType) {// 瀹℃牳鐘舵��:0鍚�1鏄�2浣滃簾3鎻愪氦4閫炬湡5閫炬湡浣滃簾6閫炬湡閫�鍥�7璺冲矖
//			case "0":
//				_strType="椹冲洖";
//				break;
//			case "1":	
//				_strType="瀹℃牳閫氳繃";
//				break;
//			case "2":
//				_strType="浣滃簾";
//				break;
//			case "3":
//				_strType="鎻愪氦";
//				break;
//			case "4":
//				_strType="閫炬湡瀹℃壒";
//				break;
//			case "5":
//				_strType="閫炬湡浣滃簾";
//				break;
//			case "6":
//				_strType="閫炬湡閫�鍥�";
//				break;
//			case "7":
//				_strType="璺冲矖";
//				break;
//			case "8":
//				_strType="缁撴潫";
//				break;
//		}
//		if("1".equals(_strIsOver)){
//			_strType="娴佺▼缁撴潫";
//		}

        
		String strMsgContent = queryMsgTemplet(_strArrayMsgIds);
//测试编码 
        
       
	
			TableEx exRun = queryFlowRun(_strFlowId, _strVersion, _strFlowRunId);
			exRun.close();
			
	//		String[] strArrayUserIds = _strArrayUserIds.split(",");
	//		for(int i=0,j=strArrayUserIds.length;i<j;i++){
	//			if("".equals(strArrayUserIds[i])){continue;}
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
	//				String[] strArraySon = queryFlowMaiByFlowId(request.getParameter("NO_sys_flow_id"),"").split(",",-1);
					strPageCode=strArraySon[1];
				}
				String[] strArray = setMsgParVal(sid, bmid, stype, djh, strPageCode, _strFlowRunId,dbf,request,_strIsOver);
				sid = strArray[0];
				bmid = strArray[1];
				djh = strArray[2];

				String[] strArrayValues={strPageCode,_strVersion,"system",strSdfYmdHms.format(new Date()),strNumberId,_strNodeId,_strArrayUserIds,_strFlowId,"0",_strArrayMsgIds,"system",strMsgContent,_strFlowRunId,"0",_strFlowType,sid,bmid,stype,djh};

				updateMsgs("1",strArrayValues);

				dbf.close();
				return strPageCode;

		}
		private String[] setMsgParVal(String _strSid,String _strBmid,String _strType,String _strDjh,String _strPageCode,String _strRunId,DBFactory _dbf,HttpServletRequest request,String _strIsOver){
			TableEx ex = null;
			TableEx exForm = null;
			String strTableName = "";
			String[] strArray=new String[3];
			try{
	//			ex = _dbf.query("select S_SID,S_ZZ,S_TYPE,S_DJH,S_TABLE from T_SYS_FLOW_PAR where S_SPAGECODE='"+_strPageCode+"'");
				ex = _dbf.query("select * from T_SYS_FLOW_PAR where S_SPAGECODE='"+_strPageCode+"'");
	//			ex = new TableEx("S_SID,S_ZZ,S_TYPE,S_DJH,S_TABLE", "T_SYS_FLOW_PAR", "S_SPAGECODE='"+_strPageCode+"'");
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
	//				_strType = getColString("S_TYPE", rd);
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
//				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+request+"---------"+request.getParameter("NO_sys_flow_state"));
				if(!"".equals(strTableName)){
					if(!"".equals(strTableNameF)){
						String strCol = _strSid+","+_strBmid+","+_strDjh+","+strTableNameF+"."+strSidF+" AS 'sidf',"+strTableNameF+"."+strDjhF+" AS 'djhf' ";
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+strCol+"---------");
						exForm = _dbf.query("select "+strCol+" from "+ strTableName+","+strTableNameF+" where S_RUN_ID='"+_strRunId+"' and "+strRelation);
					}else{
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+strTableName+"---------"+_strRunId+"---");
						exForm = new TableEx("*",strTableName, "S_RUN_ID='"+_strRunId+"'");
					}
	//				exForm = _dbf.query("select * from "+ strTableName+" where S_RUN_ID='"+_strRunId+"'");
					if(exForm.getRecordCount()>0){
						Record rd1 = exForm.getRecord(0);
						strArray[0] = getColString(_strSid, rd1);
						strArray[1] = getColString(_strBmid, rd1);
	//					_strType = getColString(_strType, rd);
						strArray[2] = getColString(_strDjh, rd1);
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+strArray[0] +"---------"+strArray[1]+"---"+strArray[2]);
						if("1".equals(_strIsOver)){
						    //recordRel(String S_ORGANISATION,String S_LEFT_PAGECODE,String LEFT_ID,String LEFT_NAME,String S_RIGHT_PAGECODE,String RIGHT_ID,String RIGHT_NAME)
							//new com.timing.impcl.MantraUtil().recordRel(strArray[0],strTableName,"".equals(strSidF)?"":getColString("sidf", rd1),"".equals(strTableNameF)?"":getColString(strTableNameF, rd1));
						}
					}
				}
				if(!"".equals(strMethod)&&!"".equals(strClass)&&"1".equals(_strIsOver)&&request!=null){
					reflectMothedInvoke(strClass, strMethod, request);
				}
			}catch (Exception e) {
				MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333",_strRunId,_strSid,new Date()+"",_strPageCode,"","setMsgParVal",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				if(ex!=null){ex.close();}
				if(exForm!=null){exForm.close();}
				e.printStackTrace();
			}finally{
				if(ex!=null){ex.close();}
				if(exForm!=null){exForm.close();}
				return strArray;
			}
		}
		public void updateMsgs(String _strType,String[] _strArrayValues){
			DBFactory dbf = new DBFactory();
			try {
				if("1".equals(_strType)){
					
					String strTabCol ="(S_PAGECODE,S_BBH,S_FSR,S_FSSJ,S_ID,S_JDID,S_JSR,S_LCID,S_SCBS,S_XXID,S_XXLX,S_XXNR,S_YXID,S_ZT,S_FLOW_TYPE,S_SID,S_BMID,S_TYPE,S_DJH)";
					_strArrayValues = arrayAddSingleQuotes(_strArrayValues);
					String strTabVal = Arrays.toString(_strArrayValues);
					strTabVal = strTabVal.substring(1,strTabVal.length()-1);
					//2018-04-19 16:26:04   注释
					//MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"insert into T_MSG_RECORDS "+strTabCol+" values("+strTabVal+")");
					dbf.sqlExe("insert into T_MSG_RECORDS "+strTabCol+" values("+strTabVal+")", true);
				}else{
				}
			} catch (Exception e) {
				MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333",_strType,_strType,new Date()+"",_strType,"","updateMsgs",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			} finally {
				dbf.close();
			}
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
						sql =strNodeArray[i].replace(strTable+"$"+strCol,"'"+getColString(strCol, ex.getRecord(0))+"'"); 
					} catch (Exception e) {
					    MantraLog.fileCreateAndWrite(e);
						e.printStackTrace();
				// 		String[] strArrayFlowLog22 = {"333","333","appendConditionSql","appendConditionSql",_strCon,"appendConditionSql",_strType,st+"----"+getErrorInfoFromException(e)};
				// 		insertFlowLog("1", strArrayFlowLog22);
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
			strGw = getColString("S_AUDIT_GW", rd);
	//		String strGw = getColString("S_AUDIT_YHZ", rd);
			if(!"".equals(_strUserIds)){
				strAuditIds =_strUserIds;
			}
	//		else if(strGw!=null&&!"".equals(strGw)){
	//			strAuditIds = queryUserByGw(strGw);
	//		}
			else if(_strAttr!=null&&!"".equals(_strAttr)){
				//选择了三权人员
				//查询人所在部门，_strLanuchBranchId, 根据发起人所在部门ID，依次向上查询部门下有此角色的人
	//			t_SYS_ROLE SROLECODE(角色代码)
	//			t_SYS_BRANCH S_CODE(部门编号)
	//			t_RGXX SYGZW(账号) SROLECODE（角色代码） SROLEBH（角色编号） SBRANCHID(组织编号)
	         
				strAuditIds = queryUserIdBySqRoles(_strAttr,_strLanuchBranchId,_strZj,_request,_strFlowRunId);
		
			}else if(("".equals(_strBranchIds))&&("".equals(_strUserIds))&&(_strRoleIds!=null&&!"".equals(_strRoleIds))){
				//选择了角色（可能多个）,机构、人为空
				//查询人所在部门，_strLanuchBranchId, 根据发起人所在部门ID，依次向上查询部门下有此角色的人
	//			t_SYS_ROLE SROLECODE(角色代码)
	//			t_SYS_BRANCH S_CODE(部门编号)
	//			t_RGXX SYGZW(账号) SROLECODE（角色代码） SROLEBH（角色编号） SBRANCHID(组织编号)
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
							if(compareArrayRepeat(_strRoleIds,getColString("SROLECODE", rd1))){//包含角色
								strAuditIds = ("".equals(strAuditIds)?"":(strAuditIds+","))+getColString("SYGZW", rd1);
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
					sr.append(getColString("SYGZW", rd)).append(",");
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
						_strRoleIds.append(getColString("S_JSDM", rd)).append(",");
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
	//			CBL:XKR-T_DQYZGZP|S_BPDD|S_ID
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
	//			exSq = dbf.query("select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strSqRole+"' and S_BMID='"+_strLanuchBranchId+"'");
				Record rd = null;
				for(int i=0,j=exSq.getRecordCount();i<j;i++){
					rd = exSq.getRecord(i);
					_strRoleIds.append(getColString("S_JSDM", rd).trim()).append(",");
					_strLanuchBranchId = getColString("S_BMID", rd);
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
	//		String strBranchCodes = _strLanuchBranchId;
			String strBranchCodes = getParentBranchCode(_strLanuchBranchId);
			TableEx exTRGXX = null;
			//查询人所在父级部门的所有人,并通过角色筛选得到map< 角色代码，人ID>
			try {
	//			exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes+"%'  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
				
				//old Code  2018-07-08 15:44:34
				//sanquanrenyuan
				//exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%' ");//and SROLECODE in("+_strRoles+")
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
					if(compareArrayRepeat(strRole.toString(),getColString("SROLECODE",rd).trim())){//角色包含
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
				if(strAuditIds!=null&&!"".equals(strAuditIds)){
	//				break;
				}
			}
			
	
			
				
	//		 return strAuditIds;
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
				/**流程定义,下级部门不能审批*/
				//exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID in("+strBranchCodes+")  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
				/**流程定义本组织全部可以审批*/
				exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%'  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
	//			exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and ( SBRANCHID like '"+_strLanuchBranchId+"%' or SBRANCHID in("+strBranchCodes+"))  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
				int iCount = exTRGXX.getRecordCount();
				Record rd = null;
				for(int i=0;i<iCount;i++){
					rd = exTRGXX.getRecord(i);
					if(compareArrayRepeat(_strRoleIds,getColString("SROLECODE",rd))){//角色包含
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
				if(strAuditIds!=null&&!"".equals(strAuditIds)){
	//				break;
				}
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
		 * 查询所有节点审批人
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strNodeId
		 * @param _strStartUser
		 * @param _strStartRole
		 * @param _strStartBranch
		 * @return
		 */
	//	public Map<String,String> queryAllNodesAudit(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId,String _strLaunchhUser,String _strLaunchRole,String _strLaunchBranch){
	//		Map<String,String> mapNodes = new HashMap<String, String>();//节点ID，审批人
	//		TableEx tableEx =null;
	//		try {
	//			tableEx =queryFlowNodeInfo(_strFlowId,_strVersion,"");
	//			int iCount = tableEx.getRecordCount();
	//			//判断开始节点数量
	//			for(int i=0;i<iCount;i++){
	//				Record rd = tableEx.getRecord(i);//1 动作 3 开始   2网关  4结束
	//				String strNodeId = rd.getFieldByName("I_NODE_ID").value.toString();
	//				if("1".equals(rd.getFieldByName("I_TYPE").value.toString())){//动作
	//					String strNodeAudit = queryAuditPerson(_strLaunchhUser,_strLaunchBranch,rd.getFieldByName("S_AUDIT_BRANCH").value.toString(),rd.getFieldByName("S_AUDIT_ROLE").value.toString(),rd.getFieldByName("S_AUDIT_USER").value.toString(),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),request);
	//					mapNodes.put(strNodeId, strNodeAudit);
	//				}
	//			}
	//			
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}finally{
	//			tableEx.close();
	//		}
	//		return mapNodes;
	//	}
	
	
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
				String strNodeType = getColString("I_TYPE", objNowRd);
				if(("3").equals(strNodeType)){//开始-找到子节点-自调用
					record = getNextNodeByCondition(request,getColString("S_CHILD_ID", objNowRd),_tabEx,_strType,_strRunId);
				}else if("1".equals(strNodeType)){//动作-赋值-返回record
					record = objNowRd;
				}else if("5".equals(strNodeType)){//子流程
					record = objNowRd;
	//				getColString("S_FLOW_TYPE", objNowRd);//
				}else if("2".equals(strNodeType)){//网关-根据网关条件判断获取下一节点-自调用
					String strCustomNodeIds = getColString("S_AUDIT_SEL", objNowRd);//手动选择节点
					if("".equals(strCustomNodeIds)){
						//审批页面,查询下一节点是否网关&审批节点不为空	
						String strNextNodeId = appendConditionSql(request,getColString("S_CONDITION", objNowRd),_strType,_strRunId);
						//默认字段
						strNextNodeId = ("".equals(strNextNodeId)?getColString("S_AUDIT_DEF", objNowRd):strNextNodeId);
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
				// String[] strArrayFlowLog22 = {"333",_strNowNode,"",new Date()+"",_strNowNode,"","getNextNodeByCondition",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
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
				String[] strArrayFlowLog22 = {"333","333","queryFlowNodeInfo","queryFlowNodeInfo","流程:"+_strFlowId,"节点:"+_strNodeId,"版本:"+_strVersion,getErrorInfoFromException(e)};
				insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}
			return tableEx;
		}
		
		/**
		 * 查询运行表T_SYS_FLOW_RUN
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @return
		 */
		public TableEx queryFlowRun(String _strFlowId,String _strVersion,String _strFlowRunId){
			TableEx ex = null;
			try {
				StringBuffer sr = new StringBuffer();
				sr.append(" 1=1");
				sr.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_ID='"+_strFlowId+"'"));
				sr.append((_strVersion==null||"".equals(_strVersion))?"":(" and S_AUDIT_VERSION ='"+_strVersion+"'"));
				sr.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID ='"+_strFlowRunId+"'"));
				ex = new TableEx("*", "T_SYS_FLOW_RUN", sr.toString());
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
			    
				e.printStackTrace();
			}
			return ex;
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
				exRun = queryFlowRun(_strFlowId, _strVersion, _strFlowRunId);
				int index =Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
				if(index==0)return bFlag;
				String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString().split("\\|",-1)[index-1];
				String strNexAuditUser = queryFlowLogBeforeNodeAuditUser(_strFlowId,_strVersion, _strFlowRunId, strNodes);
				//2018-07-09 13:20:18  addcode
				String strNexUserArr = exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString().split("\\|",-1)[index-1];
				String strNodeIdNow = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
				if(strNexAuditUser.equals(_userCode)){
				    //String[] strArrayFlowRunVal = {_strFlowId,_strFlowRunId,strNexAuditUser,strNodes,(index-1)+""};
				    //2018-07-09 13:20:18  addcode
					String[] strArrayFlowRunVal = {_strFlowId,_strFlowRunId,strNexUserArr,strNodes,(index-1)+""};
					updateFlowRun(strArrayFlowRunVal, "5");
					bFlag = true;
				}
				
				/**更新表单*/
				exRun1 = queryFlowNodeInfo(_strFlowId, _strVersion, strNodeIdNow);
				String strField = getColString("S_AUDIT_TABLECONTROL", exRun1.getRecord(0));
	
				if(strField!=null&&!"".equals(strField)){
					updateTabByFlowSet(_request, "", strField, _strFlowRunId,new StringBuffer());//strNodeIdNow
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
		 * 操作流程
		 * @param _strFlowId 流程号
		 * @param _strFlowRunId 节点号
		 * @param _strVersion 版本号
		 * @param _strType 1:挂起 0:启用 3:作废
		 * @return
		 */
		public boolean processFlowHand(String _strFlowId,String _strFlowRunId,String _strVersion,String _strType){
			DBFactory dbf = new DBFactory();
			TableEx exRun = null;
			try {
				exRun = queryFlowRun(_strFlowId,_strVersion,_strFlowRunId);
				String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
				if("1".equals(_strType)){//挂起 更新人为空,状态为2
					dbf.sqlExe("update t_sys_flow_run set I_ISOVER='2',S_AUD_USER='' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
				}else if("0".equals(_strType)){//回复人,状态为0 
					dbf.sqlExe("update t_sys_flow_run set I_ISOVER='0',S_AUD_USER='"+strAuditUsers.split("\\|",-1)[index]+"' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
				}else if("3".equals(_strType)){
					dbf.sqlExe("update t_sys_flow_run set I_ISOVER='3',S_AUD_USER='' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				dbf.close();
				if(exRun!=null){exRun.close();}
				e.printStackTrace();
			}finally{
				dbf.close();
				if(exRun!=null){exRun.close();}
				return true;
			}
		}
		
		/**
		 * 更新运行日志T_SYS_FLOW_RUN
		 * @param _strArrayFlowRun
		 * @param _strType 1:插入 2:更新 3:更新 4:更新父
		 */
		public void updateFlowRun(String[] _strArrayFlowRunVal,String _strType){
			DBFactory dbf = new DBFactory();
			try {
				if("1".equals(_strType)){
					String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_CODE,S_AUDIT_VERSION,S_LAUNCH_DATE,S_LAUNCH_USER,S_AUD_USER,S_AUDIT_INDEX,S_AUDIT_MSG,S_LAUNCH_BRANCH,S_AUDIT_ARRAYYQ,S_AUDIT_ARRAY,S_AUDIT_NODES,I_ISOVER,S_AUDIT_OTHER,S_AUDIT_SEL,S_AUD_OVER,S_FLOW_SON,S_FLOW_TYPE,S_FLOW_PARENT_ID,S_AUDIT_FSPJ,S_TAB)";
					_strArrayFlowRunVal = arrayAddSingleQuotes(_strArrayFlowRunVal);
					String strTabVal = Arrays.toString(_strArrayFlowRunVal);
					strTabVal = strTabVal.substring(1,strTabVal.length()-1);
					//重新发起流程:删除----插入
					dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID="+_strArrayFlowRunVal[0] +" and S_RUN_ID="+_strArrayFlowRunVal[1]+" and S_AUDIT_VERSION="+_strArrayFlowRunVal[3]+"",false);
					dbf.sqlExe("insert into T_SYS_FLOW_RUN "+strTabCol+" values("+strTabVal+")", true);
				}else if("2".equals(_strType)){
	
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_NODE_CODE","S_AUD_USER","S_AUDIT_INDEX","I_ISOVER","S_AUDIT_OTHER","S_AUDIT_ARRAY"};
					String strTabVal = "";
					for(int i=3,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", false);
				
				 	}else if("3".equals(_strType)){
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_AUDIT_MSG","S_AUDIT_ARRAYYQ","S_AUDIT_ARRAY","S_AUDIT_NODES","S_AUDIT_OTHER","I_ISOVER","S_AUDIT_SEL","S_FLOW_SON","S_AUDIT_FSPJ"};
					for(int i=3,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", false);
			        
			       
				}else if("4".equals(_strType)){
	//				{strFlowParentId,strFlowRunId,strNextAuditUser,strNodesParent[indexParent],indexParent+"",strIsOverParent}
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUD_USER","S_NODE_CODE","S_AUDIT_INDEX","I_ISOVER","S_AUDIT_ARRAY"};
					for(int i=2,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
				}else if("5".equals(_strType)){
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUD_USER","S_NODE_CODE","S_AUDIT_INDEX"};
					for(int i=2,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
				}else if("6".equals(_strType)){
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_SEL"};
					for(int i=2,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333","333","flowrun","flowrun","flowrun","flowrun",_strType,getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				
				e.printStackTrace();
			} finally {
				if(dbf!=null){dbf.close();}
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
					String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_ID,S_AUD_DATE,S_AUDIT_VERSION,S_AUD_USER,S_AUD_STAUS,S_AUD_COMMENT)";
					_strArrayValues = arrayAddSingleQuotes(_strArrayValues);
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
	//			sb.append(" order by T_SYS_FLOW_LOG.S_AUD_DATE desc");//审批节点可能重复,不影响功能,放开则审批人节点可能丢失
				ex = new TableEx("T_SYS_FLOW_LOG.S_NODE_ID,t_rgxx.SYGMC,T_SYS_FLOW_NODE.S_NODE_NAME", "T_SYS_FLOW_LOG ,T_SYS_FLOW_NODE ,T_RGXX",sb.toString());
				int iCount = ex.getRecordCount(); 
				int flag = -1;
				for(int i=0;i<iCount;i++){
					strReturn = ("".equals(strReturn)?"":(strReturn+"|"))+getColString("S_NODE_ID",  ex.getRecord(i))+","+getColString("SYGMC",  ex.getRecord(i))+","+getColString("S_NODE_NAME",  ex.getRecord(i));
					if(_strNodeId.equals(getColString("S_NODE_ID",  ex.getRecord(i)))){
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
					if(_strNodeId.equals(getColString("S_NODE_ID",  rd))){
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
				strBeforeUser = getColString("S_AUD_USER", exFlowLog.getRecord(0));
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
	//			sb.append("".equals(_strNodeId)?"":" GROUP BY S_NODE_ID  ");
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
		public String getColString(String _strCol,Record rd){
			String strReturn = "";
			try {
				FieldEx ex = rd.getFieldByName(_strCol);
				Object obj= ((ex==null||"".equals(ex))?"":(ex.value));
				strReturn = (obj==null||"".equals(obj))?"":obj.toString();
			} catch (Exception e) {
				strReturn = "";
				MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333","","",new Date()+"",_strCol,_strCol,"getColString",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}finally{
				return strReturn ;
			}
		}
		
		/**
		 * 数组元素添加单引号---拼接sql语句
		 * @param _array
		 * @return
		 */
		public String[] arrayAddSingleQuotes(String[] _array){
			for(int i=0,j=_array.length;i<j;i++){
				_array[i]="'"+_array[i]+"'";
			}
			return _array;
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
		public static void reflectMothedInvoke(String strClassName,String strMethodName,Object... obj){
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
		public void getTest(HttpServletRequest req){
			System.out.println("*****************");
			String[] strArrayFlowLog22 = {"333","xx","xx",new Date()+"","xx","","start","xx"};
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
    	
		public void DelMsg( String S_RUN_ID) {
    		DBFactory dbf = new DBFactory();
    		try {
    			dbf.sqlExe("delete from T_MSG_RECORDS where   S_YXID='"+S_RUN_ID+"' ;", false);
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
        
    	
		public static void main(String[] args) {
			HttpServletRequest req = null;
			reflectMothedInvoke("com.bfkc.process.ProcessRunOperation","getTest",req);
		}	
	}
