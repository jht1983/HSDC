package com.bfkc.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

/**
 * 审批人选择审批节点
 * @author Administrator
 *
 */
public class ProcessAuditSelectNode extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
			String strFlowId = request.getParameter("S_FLOW_ID");//流程ID
			String strVersion = request.getParameter("S_VERSION");//版本号
			String strFlowRunId = request.getParameter("S_RUN_ID");//运行ID
			
			/**查询流程运行信息*/
			TableEx exRun = processRunOperation.queryFlowRun(strFlowId,strVersion,strFlowRunId);
			String[] strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString().split("\\|");
			int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
			String strNodesBefore ="";
			for(int i=0;i<index;i++){
				if("".equals(strNodesBefore)){strNodesBefore = strNodes[i];}
				else{
					strNodesBefore = strNodesBefore+","+strNodes[i];
				}
			}
			String strResult = processRunOperation.queryFlowLogBeforeAll(strFlowId, strVersion, strFlowRunId, strNodesBefore);
			out.print(strResult);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
}
