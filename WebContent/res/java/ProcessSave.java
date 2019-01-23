package com.bfkc.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class ProcessSave extends HttpServlet {
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
			/**1接收数据*/
			String strFlowId = request.getParameter("S_FLOW_ID");//流程ID
			String strVersion = request.getParameter("S_VERSION");//版本号
			String strFlowRunId = request.getParameter("S_RUN_ID");//运行
			
				/**2查询运行表*/
				TableEx exRun = processRunOperation.queryFlowRun(strFlowId,strVersion,strFlowRunId);
				String strIsOverRun = exRun.getRecord(0).getFieldByName("I_ISOVER").value.toString();//是否完成
				if("1".equals(strIsOverRun)){//判断完成返回
					return ;
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
				/**3 查询流程节点*/
				TableEx tableEx = processRunOperation.queryFlowNodeInfo(strFlowId,strVersion,"","");
				Map<String,Object> mapNodes = new TreeMap<String, Object>();//节点ID，对象
				int iCount = tableEx.getRecordCount();
				for(int i=0;i<iCount;i++){
					Record rd = tableEx.getRecord(i);//1 动作 3 开始   2网关  4结束
					mapNodes.put(rd.getFieldByName("I_NODE_ID").value.toString(), rd);//节点ID，对象
				}
				
				/**4 循环拼接参数*/
				String strAuditArrayyq="";//逾期
				String strAuditNodes="";//所有节点
				String strNodeIdNext="";//运行节点
				String strAuditOther ="";//其他
				int strNextAuditUserIndex = index+1;//下一审批节点索引,发起人默认为1
				String strAuditUsers = "";//所有审批人
				String strAuditYq ="";//所有逾期
				String strAuditYqOpt ="";//所有逾期操作
				String strAuditMsgs = "";//所有消息模版
				String strAuditNode = "";//节点
				String strAuditTg = "";//是否跳过
				String strAuditReject = "";//所有审批驳回
				String strAuditRejectOpt = "";//所有审批驳回处理
				String strSonFlow = "";//子流程
				String strIsOver = "0";//是否结束
				String strNextAuditUser = "";//下一节点审批人
				Record rd = null;//获取下一节点对象
				String strEndFlag = "";
				int icount = 0;
				while(!"4".equals(strEndFlag)){
						rd = processRunOperation.getNextNodeByCondition(request,strStartNode,mapNodes,tableEx);
						
						String strBranch = rd.getFieldByName("S_AUDIT_BRANCH").value==null?"":rd.getFieldByName("S_AUDIT_BRANCH").value.toString();
						String strRole =rd.getFieldByName("S_AUDIT_ROLE").value==null?"":rd.getFieldByName("S_AUDIT_ROLE").value.toString();
						String strUserId = rd.getFieldByName("S_AUDIT_USER").value==null?"":rd.getFieldByName("S_AUDIT_USER").value.toString();
						String strNodeAudit = processRunOperation.queryAuditPerson(strLaunchUser,strLaunchBranch,strBranch,strRole,strUserId);
						
						strAuditUsers = (icount==0?"":(strAuditUsers+"|"))+strNodeAudit;
						
						strAuditYq = rd.getFieldByName("S_AUDIT_YQTS").value==null?"":rd.getFieldByName("S_AUDIT_YQTS").value.toString();//所有逾期
						strAuditYqOpt = rd.getFieldByName("S_AUDIT_YQTSCL").value==null?"":rd.getFieldByName("S_AUDIT_YQTSCL").value.toString();//所有逾期操作
						strAuditArrayyq =(icount==0?"":(strAuditArrayyq+"|")) +strAuditYq+","+strAuditYqOpt;
						
						strAuditMsgs =(icount==0?"":(strAuditMsgs+"|"))+processRunOperation.getColString("S_AUDIT_TZXX", rd);//所有消息模版
						
						strAuditNode = rd.getFieldByName("I_NODE_ID").value.toString();//节点
						strAuditNodes = (icount==0?"":(strAuditNodes+"|"))+strAuditNode;
		
						strAuditTg =rd.getFieldByName("S_AUDIT_TG").value==null?"":rd.getFieldByName("S_AUDIT_TG").value.toString();//是否跳过
						strAuditReject =rd.getFieldByName("S_AUDIT_THJD").value==null?"":rd.getFieldByName("S_AUDIT_THJD").value.toString();//所有审批驳回
						strAuditRejectOpt =rd.getFieldByName("S_AUDIT_THJDZD").value==null?"":rd.getFieldByName("S_AUDIT_THJDZD").value.toString();//所有审批驳回处理
						strSonFlow =rd.getFieldByName("S_TZLC").value==null?"":rd.getFieldByName("S_TZLC").value.toString();//子流程
						strAuditOther =(icount==0?"":(strAuditOther+"|")) +strAuditTg+","+strAuditReject+","+strAuditRejectOpt+","+strSonFlow;
						strStartNode =  rd.getFieldByName("I_NODE_ID").value.toString();
						strEndFlag = rd.getFieldByName("I_TYPE").value.toString();
						icount=1;
				}
				/**5组装参数*/
				strAuditUsersRun = processRunOperation.getAuditStrArry(strAuditUsersRun,strAuditUsers,index);
				strYqs = processRunOperation.getAuditStrArry(strYqs,strAuditArrayyq,index);
				strMsgs = processRunOperation.getAuditStrArry(strMsgs,strAuditMsgs,index);
				strNodes = processRunOperation.getAuditStrArry(strNodes,strAuditNodes,index);
				strOther= processRunOperation.getAuditStrArry(strOther,strAuditOther,index);
				    
					/**6 跳岗 1:是*/
//					strNextAuditUserIndex = processRunOperation.getNodesInfo(strAuditUsersRun,strOther,strNextAuditUserIndex);
//					strNextAuditUser =strAuditUsersRun.split("\\|",-1)[strNextAuditUserIndex];
//					strNodeIdNext =strNodes.split("\\|")[strNextAuditUserIndex];
					/**流程是否结束*/
					strIsOver = ((strNextAuditUserIndex+1) == strNodes.split("\\|",-1).length)?"1":strIsOver;
					/**7 更新运行表 4个数组*/
					String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strMsgs,strYqs,strAuditUsersRun,strNodes,strOther,strIsOver};
					processRunOperation.updateFlowRun(strArrayFlowRun,"3");
					out.print("保存成功");
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	 
}
