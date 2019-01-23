package com.bfkc.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class ProcessStart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			ProcessRunOperation processRunOperation = new ProcessRunOperation();
			/**接收数据*/
			String strStartUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//发起人
			String strStartUserRole = request.getSession().getAttribute("SYS_STRROLECODE").toString();//发起人角色
			String strStartUserBranch = request.getSession().getAttribute("SYS_STRBRANCHID").toString();//发起人组织
			String strFlowId = request.getParameter("S_FLOW_ID");//流程ID
			String strVersion = request.getParameter("S_VERSION");//版本号
			String strFlowRunId = request.getParameter("S_RUN_ID");//运行
			
				/**0 查询流程节点*/
				Map<String,Object> mapNodes = new TreeMap<String, Object>();//节点ID，对象
				String strStartNode = "";//发起节点NODE
				String strStartNodeBak = "";//发起节点NODE
				TableEx tableEx = processRunOperation.queryFlowNodeInfo(strFlowId,strVersion,"","");
				int iCount = tableEx.getRecordCount();
				/**1 查找开始节点*/
				Record record = null;
				for(int i=0;i<iCount;i++){
					record =  tableEx.getRecord(i);//1 动作 3 开始   2网关  4结束
//					String strNodeId = record.getFieldByName("I_NODE_ID").value.toString();
//					mapNodes.put(strNodeId, processRunOperation.recordToMap(record, map));//节点ID，对象
					if("3".equals(record.getFieldByName("I_TYPE").value.toString())){//开始数量
						String strStartUserId = record.getFieldByName("S_AUDIT_USER").value.toString();
						String strStartUserRoles =record.getFieldByName("S_AUDIT_ROLE").value.toString();
						String strStartUserBranchs =record.getFieldByName("S_AUDIT_BRANCH").value.toString();
						//多个开始节点判断是否发节点
						if(processRunOperation.queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,strStartUserBranchs,strStartUserRoles,strStartUserId)==true){
							strStartNode = record.getFieldByName("I_NODE_ID").value.toString();	
						}
						strStartNodeBak = record.getFieldByName("I_NODE_ID").value.toString();	
					}
				}
				strStartNode= ("".equals(strStartNode)?strStartNodeBak:strStartNode);//找不到开始节点则任意一个
				/**2 开始节点赋值*/
				String strAuditArrayyq=",";//逾期
				String strAuditNodes=strStartNode;//所有节点
				String strAuditState="3";//运行状态 3：提交
				String strNodeIdNext="";//运行节点
				String strNextAuditUser="";//节点审批人
				String strAuditOther =",,,";//其他
				String strNownewDate = processRunOperation.strSdfYmdHms.format(new Date());//发起日期
				int strNextAuditUserIndex =1;//下一审批节点索引,发起人默认为1
				String strAuditUsers = strStartUser;//所有审批人
				String strAuditYq ="";//所有逾期
				String strAuditYqOpt ="";//所有逾期操作
				String strAuditMsgs = "";//所有消息模版
				String strAuditNode = "";//节点
				String strAuditTg = "";//是否跳过
				String strAuditReject = "";//所有审批驳回
				String strAuditRejectOpt = "";//所有审批驳回处理
				String strSonFlow = "";//子流程
				String strIsOver = "0";//是否结束
				Record rd = null;//获取下一节点对象
				String strEndFlag = "";
				/**3 循环拼接参数*/
				while(!"4".equals(strEndFlag)){
						rd = processRunOperation.getNextNodeByCondition(request,strStartNode,mapNodes,tableEx);
						if(rd==null){break;}
						String strBranch = rd.getFieldByName("S_AUDIT_BRANCH").value==null?"":rd.getFieldByName("S_AUDIT_BRANCH").value.toString();
						String strRole =rd.getFieldByName("S_AUDIT_ROLE").value==null?"":rd.getFieldByName("S_AUDIT_ROLE").value.toString();
						String strUserId = rd.getFieldByName("S_AUDIT_USER").value==null?"":rd.getFieldByName("S_AUDIT_USER").value.toString();
						String strNodeAudit = processRunOperation.queryAuditPerson(strStartUser,strStartUserBranch,strBranch,strRole,strUserId);
						
						
						
						strAuditYq = rd.getFieldByName("S_AUDIT_YQTS").value==null?"":rd.getFieldByName("S_AUDIT_YQTS").value.toString();//所有逾期
						strAuditYqOpt = rd.getFieldByName("S_AUDIT_YQTSCL").value==null?"":rd.getFieldByName("S_AUDIT_YQTSCL").value.toString();//所有逾期操作
						strAuditArrayyq =(strAuditArrayyq+"|") +strAuditYq+","+strAuditYqOpt;
						
						strAuditMsgs =(strAuditMsgs+"|")+processRunOperation.getColString("S_AUDIT_TZXX", rd);//所有消息模版
						strAuditNode = rd.getFieldByName("I_NODE_ID").value.toString();//节点
						strAuditNodes = (strAuditNodes+"|")+strAuditNode;
		
						strAuditTg =rd.getFieldByName("S_AUDIT_TG").value==null?"":rd.getFieldByName("S_AUDIT_TG").value.toString();//是否跳过
						strAuditReject =rd.getFieldByName("S_AUDIT_THJD").value==null?"":rd.getFieldByName("S_AUDIT_THJD").value.toString();//所有审批驳回
						strAuditRejectOpt =rd.getFieldByName("S_AUDIT_THJDZD").value==null?"":rd.getFieldByName("S_AUDIT_THJDZD").value.toString();//所有审批驳回处理
						strSonFlow =rd.getFieldByName("S_TZLC").value==null?"":rd.getFieldByName("S_TZLC").value.toString();//子流程
						strAuditOther =(strAuditOther+"|") +strAuditTg+","+strAuditReject+","+strAuditRejectOpt+","+strSonFlow;
						strStartNode =  rd.getFieldByName("I_NODE_ID").value.toString();
						
						if("".equals(strNodeAudit)){
							strNodeAudit = ("1".equals(strAuditTg))?"T":strNodeAudit;
						}
						strAuditUsers = (strAuditUsers+"|")+strNodeAudit;
						
						strEndFlag = rd.getFieldByName("I_TYPE").value.toString();
				}
					/**4 跳岗 1:是*/
					strNextAuditUserIndex = processRunOperation.getNodesInfoRun(strAuditUsers,strAuditOther,strNextAuditUserIndex,strStartUser,strAuditMsgs.split("\\|",-1));
					strNextAuditUser =strAuditUsers.split("\\|",-1)[strNextAuditUserIndex];
					strNodeIdNext =strAuditNodes.split("\\|",-1)[strNextAuditUserIndex];
					/**5 插入运行表*/
					String[] strArrayFlowRun = {strFlowId,strFlowRunId,strNodeIdNext,strVersion,strNownewDate,strStartUser,strNextAuditUser,strNextAuditUserIndex+"",strAuditMsgs,strStartUserBranch,strAuditArrayyq,strAuditUsers,strAuditNodes,strIsOver,strAuditOther};
					processRunOperation.updateFlowRun(strArrayFlowRun,"1");
				
					String strDate = processRunOperation.strSdfYmdHms.format(new Date());
					String  strAuditComment = "";
					/**插入流程日志*/
					String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment};
					processRunOperation.insertFlowLog("1", strArrayFlowLog);
					/**发送消息*/
					String strMsgId =strAuditMsgs.split("!")[0];//下一节点消息ID
					String[] strArrayMsg={strNextAuditUser};//接收人
					processRunOperation.sendMsg(strMsgId,strArrayMsg,strAuditState,strIsOver);
					out.print("提交成功");
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	 
}
