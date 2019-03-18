/**
 * 
 */
package com.yulongtao.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Debug;
import com.page.method.Fun;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.engine.Api.FlowApi;
import com.yulongtao.engine.flow.AuditFlow;
import com.yulongtao.sys.SumToSub;
import com.yulongtao.sys.XssClear;
import com.yulongtao.util.EString;
import com.yulongtao.util.exp.ExpressManager;
import com.yulongtao.util.msg.DlMsg;

/**
 * @author tianshisheng
 *
 */
public class MisWebAction extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    private HttpServletRequest requestFlow;
    private String strAuitMsg;
    DBFactory dbfExp;
    
    public MisWebAction() {
        this.requestFlow = null;
        this.strAuitMsg = "";
        this.dbfExp = null;
    }
    
    public void init() throws ServletException {
    }
    
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    
    private void doFlow(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        try {
            final TableEx tableEx = new TableEx("t_sys_flow_log");
            final String strFlowId = request.getParameter("NO_sys_flow_id");
            final String strRunId = request.getParameter("NO_sys_run_id");
            final String strAudUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();
            final String strCurNode = request.getParameter("NO_sys_curnode");
            final String strAudStatus = request.getParameter("NO_sys_flow_status");
            final Record record = new Record();
            record.addField(new FieldEx("S_FLOW_ID", strFlowId));
            record.addField(new FieldEx("S_RUN_ID", strRunId));
            record.addField(new FieldEx("S_NODE_ID", strCurNode));
            record.addField(new FieldEx("S_AUD_USER", strAudUser));
            record.addField(new FieldEx("S_AUD_DATE", EString.getCurDate_HH()));
            record.addField(new FieldEx("S_AUD_STAUS", strAudStatus));
            record.addField(new FieldEx("S_AUD_COMMENT", EString.encoderStr(request.getParameter("NO_sys_aud_comment"))));
            tableEx.addRecord(record);
            final DBFactory dbf = new DBFactory();
            dbf.solveTable(tableEx, true);
            if (strAudStatus.equals("-1")) {
                this.updateFlowRunToReturn(strFlowId, strRunId);
            }
            else {
                this.updateFlowRun(strFlowId, strRunId, strAudUser, request.getParameter("NO_sys_launchbrh"), strCurNode, request);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final String strWinId = request.getParameter("NO_UPL_KC");
        if (strWinId != null) {
            out.println("<script laguage='javascript'>alert('\u6d41\u8f6c\u6210\u529f\uff01');parent.parent.closeById('" + strWinId + "')</script>");
        }
        else {
            response.sendRedirect("Redirect");
        }
    }
    
    private void updateFlowRunToReturn(final String _strFlowId, final String _strRunId) {
        DBFactory dbf = null;
        try {
            dbf = new DBFactory();
            final TableEx tableFlowContent = new TableEx("t_sys_flow_run");
            final Record record = new Record();
            record.addField(new FieldEx("S_FLOW_ID", _strFlowId, true));
            record.addField(new FieldEx("S_RUN_ID", _strRunId, true));
            record.addField(new FieldEx("S_NODE_CODE", ""));
            record.addField(new FieldEx("S_AUD_USER", ""));
            record.addField(new FieldEx("I_ISOVER", "1"));
            tableFlowContent.addRecord(record);
            dbf.updateTable(tableFlowContent, true);
        }
        catch (Exception e) {
            System.out.println("\u4fee\u6539\u6d41\u7a0b\u5185\u5bb9\u5931\u8d25\uff01" + e);
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
    }
    
    private void updateFlowRun(final String _strFlowId, final String _strRunId, final String _strAudUser, final String _strUserBranch, final String _strCurNode, final HttpServletRequest _request) {
        DBFactory dbf = null;
        final String strFlowRunField = _request.getParameter("NO_sys_flow_Run_Field");
        try {
            dbf = new DBFactory();
            final TableEx tableFlowContent = new TableEx("t_sys_flow_run");
            final String[] arrStrNexdNode = this.getNextNode(_strFlowId, _strRunId, _strUserBranch, _strCurNode);
            final Record record = new Record();
            record.addField(new FieldEx("S_FLOW_ID", _strFlowId, true));
            record.addField(new FieldEx("S_RUN_ID", _strRunId, true));
            record.addField(new FieldEx("S_NODE_CODE", arrStrNexdNode[0]));
            record.addField(new FieldEx("S_AUD_USER", arrStrNexdNode[1]));
            record.addField(new FieldEx("I_ISOVER", arrStrNexdNode[2]));
            tableFlowContent.addRecord(record);
            dbf.updateTable(tableFlowContent, true);
        }
        catch (Exception e) {
            System.out.println("\u4fee\u6539\u6d41\u7a0b\u5185\u5bb9\u5931\u8d25\uff01" + e);
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
    }
    
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String strIsApp = request.getParameter("NO_isapp");
        response.setContentType("text/html; charset=GBK");
        final String strIsFlowId = request.getParameter("NO_sys_flow_id");
        if (request.getParameter("NO_sys_run_id") != null) {
            this.doFlow(this.requestFlow = request, response);
            return;
        }
        final PrintWriter out = response.getWriter();
        final HttpSession session = request.getSession();
        try {
            final MisRequestEx requestEx = new MisRequestEx(request);
            requestEx.session = session;
            final String strDb = request.getParameter("NO_sys_data_db_nm");
            if (strDb != null) {
                requestEx.setDb(strDb);
            }
            final String strType = request.getParameter("NO_OPTYPE");
            final String strCondition = request.getParameter("NO_CONDITION");
            final String strPage = request.getParameter("NO_BATCH_PAGE");
            if (strPage != null) {
                requestEx.soleveBatch(strPage, strType);
            }
            else if (strCondition != null) {
                requestEx.updateTable(request.getParameter("NO_TABLE"), strCondition);
            }
            else if (strType == null) {
                requestEx.solveTable();
            }
            else if (strType.equals("1")) {
                requestEx.updateTable();
            }
            else {
                if (strType.equals("0")) {
                    requestEx.solveSimpleTable();
                }
                if (strType.startsWith("2:")) {
                    requestEx.delRecord(strType.substring(2), false);
                    requestEx.solveTable();
                }
                if (strType.startsWith("3:")) {
                    requestEx.updateSolveTable(strType.substring(2));
                }
            }
            if (strIsApp != null) {
                out.println("<script language='javascript' src='js/ylphone.js'></script><script>yltPhone.closeLinkByResult('','ok');</script>");
                return;
            }
            if ((strType != null && strType.equals("0")) || request.getParameter("NO_RETURN_OK_MSG") != null) {
                out.print("ok");
                return;
            }
            if (strIsFlowId != null) {
                final String strDoFlow = request.getParameter("NO_SYS_IS_DO_FLOW_START");
                if (strDoFlow != null && strDoFlow.equals("1")) {
                    final Fun fun = new Fun();
                    fun.doFlow(request, response);
                    return;
                }
            }
            final String strDoScript = request.getParameter("NO_DOSCRIPT");
            System.out.println("::::::::::::\u6311\u8f6c" + strDoScript);
            if (strDoScript != null) {
                out.println("<script>");
                out.println(strDoScript);
                out.println("</script>");
                return;
            }
            String strWinId = request.getParameter("NO_UPL_KC");
            if (strWinId != null) {
                strWinId = XssClear.clear(strWinId);
                String strNextAdd = request.getParameter("NO_sys_is_continue_add");
                if (strNextAdd != null) {
                    if (strNextAdd.indexOf("&NO_sys_is_continue_add=1") == -1) {
                        strNextAdd = String.valueOf(strNextAdd) + "&NO_sys_is_continue_add=1";
                    }
                    final HttpSession hSession = request.getSession();
                    final Object objPageModel = hSession.getAttribute("SYS_FORWARDTYPE_PAGEACTION");
                    if (objPageModel == null || !objPageModel.equals("")) {
                        out.println("<body onload=\"parent.refreshParentPage('" + strWinId + "');document.getElementById('href').click();\"><a id=\"href\" href=\"" + strNextAdd + "\"></a></body>");
                    }
                    else {
                        final Object objOpMsg = hSession.getAttribute("SYS_OPMSG");
                        String strFlushTree = "";
                        if (objOpMsg != null) {
                            final String strOpMsg = objOpMsg.toString();
                            if (strOpMsg.indexOf("yltTree.prototype.fushTree") != -1) {
                                strFlushTree = "parent.getOpenPage('" + strWinId + "').parent.lxleft.yltTree.prototype.fushTree('testtree0');";
                            }
                        }
                        out.println("<body onload=\"" + strFlushTree + "parent.refreshParentPage('" + strWinId + "');document.getElementById('href').click();\"><a id=\"href\" href=\"" + strNextAdd + "\"></a></body>");
                    }
                }
                else {
                    final HttpSession hSession = request.getSession();
                    final Object objPageModel = hSession.getAttribute("SYS_FORWARDTYPE_PAGEACTION");
                    System.out.println("::::::::::::::" + objPageModel);
                    if (objPageModel == null || !objPageModel.equals("") || strPage != null) {
                        out.println("<script laguage='javascript'>alert('\u4fdd\u5b58\u6210\u529f\uff01');parent.parent.closeById('" + strWinId + "')</script>");
                    }
                    else {
                        final Object objOpMsg = hSession.getAttribute("SYS_OPMSG");
                        if (objOpMsg != null) {
                            try {
                                hSession.setAttribute("SYS_OPMSG", (Object)new ABSElement().getFilterData(objOpMsg.toString(), request));
                            }
                            catch (Exception ex) {}
                        }
                        response.sendRedirect("Redirect");
                    }
                }
            }
            else {
                final Object objOpMsg2 = request.getSession().getAttribute("SYS_OPMSG");
                if (objOpMsg2 != null) {
                    try {
                        request.getSession().setAttribute("SYS_OPMSG", (Object)new ABSElement().getFilterData(objOpMsg2.toString(), request));
                    }
                    catch (Exception ex2) {}
                }
                response.sendRedirect("Redirect");
            }
        }
        catch (Exception e) {
            Debug.println("\u6eb6\u89e3\u8868\u9519\u8bef!" + e);
            String strWinId2 = request.getParameter("NO_UPL_KC");
            if (strWinId2 != null) {
                strWinId2 = XssClear.clear(strWinId2);
                out.println("<script laguage='javascript'>alert('\u4fdd\u5b58\u5931\u8d25\uff01" + e + "');parent.parent.closeById('" + strWinId2 + "')</script>");
            }
            else {
                out.println("\u6eb6\u89e3\u8868\u9519\u8bef!" + e);
            }
        }
    }
    
    private void doBatch(final HttpServletRequest request, final HttpServletResponse response) {
        final SumToSub sts = new SumToSub();
        final String strMc = EString.encoderStr(request.getParameter("mc"), "utf-8");
        final String strGG = request.getParameter("gg");
        final String strCz = request.getParameter("cz");
        final String strCd = request.getParameter("cd");
        final String strKd = request.getParameter("kd");
        final String strSl = request.getParameter("icount");
        final String strWhere = "mc_3='" + strMc + "' and " + "gg_4='" + strGG + "' and " + "cz_5='" + strCz + "' and " + "cd_7='" + strCd + "' and " + "kd_6='" + strKd + "'";
        final String strSolve = "INSERT INTO t_004 (ylsj_1,ylr_2,clmc_3,gg_4,cz_5,kd_6,cd_7,sl_8) VALUES ('" + EString.getCurDate() + "','" + request.getSession().getAttribute("SYS_STRCURUSER") + "','" + strMc + "','" + strGG + "','" + strCz + "','" + strKd + "','" + strCd + "'," + strSl + ")";
    }
    
    private void solveLog(final String _strRunId, final String _strFlowId, final String _strUser) {
        TableEx tableEx = null;
        DBFactory dbf = null;
        try {
            dbf = new DBFactory();
            tableEx = new TableEx("t_flowcg");
            final Record record = new Record();
            record.addField(new FieldEx("S_RUNID", _strRunId));
            record.addField(new FieldEx("SYS_FLOWID", _strFlowId));
            record.addField(new FieldEx("S_USERID", _strUser));
            tableEx.addRecord(record);
            dbf.solveTable(tableEx, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
    }
    
    private boolean getIsUpdate(final String flowId) {
        boolean vResult = false;
        TableEx tableFlowContent = null;
        try {
            tableFlowContent = new TableEx("*", "T_FLOWCONTENT", "SFLOWRUNID='" + flowId + "'");
            final int iRecordCount = tableFlowContent.getRecordCount();
            if (iRecordCount > 0) {
                vResult = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return vResult;
        }
        finally {
            if (tableFlowContent != null) {
                tableFlowContent.close();
            }
        }
        if (tableFlowContent != null) {
            tableFlowContent.close();
        }
        return vResult;
    }
    
    private boolean doNodeCondition(final String _strForm, final String _strCon, final String _strRunId) throws Exception {
        boolean vResult = false;
        final String strSql = "select count(*) acout from " + _strForm + " where S_ID='" + _strRunId + "' and (" + _strCon + ")";
        final TableEx tableEx = this.dbfExp.query(strSql);
        if (Integer.parseInt(tableEx.getRecord(0).getFieldByName("acout").value.toString()) > 0) {
            vResult = true;
        }
        return vResult;
    }
    
    private String[] getNextNode(final String _strFlowId, String _strFlowRunId, final String _strUserBranch, final String _strCurNode) {
        final String[] vResult = { "", "", "" };
        final DBFactory dbf = new DBFactory();
        try {
            TableEx tableNode = null;
            if (_strFlowRunId.equals("")) {
                tableNode = dbf.query("select I_TYPE,I_NODE_ID,S_AUDIT_ROLE,S_AUDIT_USER,S_AUDIT_BRANCH,S_CHILD_ID,S_CONDITION FROM  t_sys_flow_node WHERE I_NODE_ID =(SELECT  S_CHILD_ID from t_sys_flow_node where S_FLOW_ID='" + _strFlowId + "' and I_TYPE=3) and S_FLOW_ID='" + _strFlowId + "'");
            }
            else {
                tableNode = dbf.query("select I_TYPE,I_NODE_ID,S_AUDIT_ROLE,S_AUDIT_USER,S_AUDIT_BRANCH,S_CHILD_ID,S_CONDITION FROM  t_sys_flow_node WHERE I_NODE_ID =(SELECT  S_CHILD_ID from t_sys_flow_node where S_FLOW_ID='" + _strFlowId + "' and I_NODE_ID=" + _strCurNode + ") and S_FLOW_ID='" + _strFlowId + "'");
            }
            Record record = tableNode.getRecord(0);
            String strNodeType = record.getFieldByName("I_TYPE").value.toString();
            String strNodeId = record.getFieldByName("I_NODE_ID").value.toString();
            if (strNodeType.equals("2")) {
                String strForm = this.requestFlow.getParameter("NO_sys_formid");
                final String strChildIds = record.getFieldByName("S_CHILD_ID").value.toString();
                final String strCondition = record.getFieldByName("S_CONDITION").value.toString();
                final String[] arrCondition = strCondition.split("\\$");
                final int iConCount = arrCondition.length;
                this.dbfExp = new DBFactory();
                try {
                    String strNextNode = "";
                    String strDefaultNode = "";
                    if (strForm == null) {
                        strForm = this.requestFlow.getParameter("NO_sys_flow_Run_Field").split("\\$")[0];
                    }
                    for (int i = 0; i < iConCount; ++i) {
                        final String[] strNodeCon = arrCondition[i].split(":");
                        if (!strNodeCon[1].equals("")) {
                            final boolean bIsTrue = this.doNodeCondition(strForm, strNodeCon[1], _strFlowRunId);
                            if (bIsTrue) {
                                strNextNode = strNodeCon[0];
                                break;
                            }
                        }
                        else {
                            strDefaultNode = strNodeCon[0];
                        }
                    }
                    if (strNextNode.equals("")) {
                        strNextNode = strDefaultNode;
                    }
                    tableNode = dbf.query("select I_TYPE,I_NODE_ID,S_AUDIT_ROLE,S_AUDIT_USER,S_AUDIT_BRANCH,S_CHILD_ID,S_CONDITION FROM  t_sys_flow_node WHERE I_NODE_ID =" + strNextNode + " and S_FLOW_ID='" + _strFlowId + "'");
                    record = tableNode.getRecord(0);
                    strNodeType = record.getFieldByName("I_TYPE").value.toString();
                    strNodeId = record.getFieldByName("I_NODE_ID").value.toString();
                }
                catch (Exception e) {
                    System.out.println("\u6267\u884c\u6761\u4ef6\u9519\u8bef!" + e);
                }
                finally {
                    this.dbfExp.close();
                }
            }
            final String strAuRole = record.getFieldByName("S_AUDIT_ROLE").value.toString();
            final String strAuUser = record.getFieldByName("S_AUDIT_USER").value.toString();
            final String strAuBranch = record.getFieldByName("S_AUDIT_BRANCH").value.toString();
            vResult[0] = strNodeId;
            if (!strAuUser.equals("")) {
                vResult[1] = strAuUser;
            }
            else {
                vResult[1] = this.getAuditUser(strAuRole, strAuBranch, _strUserBranch);
                if (vResult[1].equals("") && !strNodeType.equals("4")) {
                    if (_strFlowRunId.equals("")) {
                        final String strFlowRunField = this.requestFlow.getParameter("NO_sys_flow_Run_Field");
                        _strFlowRunId = this.requestFlow.getParameter(strFlowRunField);
                    }
                    return this.getNextNode(_strFlowId, _strFlowRunId, _strUserBranch, strNodeId);
                }
            }
            vResult[2] = strNodeType;
        }
        catch (Exception e2) {
            System.out.println("\u67e5\u8be2\u4e0b\u4e00\u8282\u70b9\u5931\u8d25\uff01" + e2);
            return vResult;
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult;
    }
    
    private String getAuditUser(final String _strAuRole, String _strAuBranch, final String _strUserBranch) {
        String vResult = "";
        if (_strAuBranch.equals("")) {
            _strAuBranch = _strUserBranch;
        }
        final DBFactory dbf = new DBFactory();
        try {
            final TableEx tableNode = dbf.query("select SYGZW  from t_rgxx where SROLECODE='" + _strAuRole + "' and SBRANCHID='" + _strAuBranch + "'");
            if (tableNode.getRecordCount() < 1) {
                return vResult;
            }
            final Record record = tableNode.getRecord(0);
            vResult = record.getFieldByName("SYGZW").value.toString();
        }
        catch (Exception e) {
            System.out.println("\u67e5\u8be2\u5ba1\u6838\u4eba\u5931\u8d25\uff01" + e);
            return vResult;
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult;
    }
    
    private void addFlowRun(final String _strFlowId, final String _strUserId, final String _strUserBranch, final HttpServletRequest _request) {
        DBFactory dbf = null;
        final String strFlowRunField = _request.getParameter("NO_sys_flow_Run_Field");
        try {
            dbf = new DBFactory();
            final TableEx tableFlowContent = new TableEx("t_sys_flow_run");
            final String[] arrStrNexdNode = this.getNextNode(_strFlowId, "", _strUserBranch, "");
            final Record record = new Record();
            final String strIsUpdate = _request.getParameter("NO_sys_flow_id_update");
            if (strIsUpdate != null) {
                record.addField(new FieldEx("S_FLOW_ID", _strFlowId, true));
                record.addField(new FieldEx("S_RUN_ID", _request.getParameter(strFlowRunField), true));
                record.addField(new FieldEx("S_NODE_CODE", arrStrNexdNode[0]));
                record.addField(new FieldEx("S_AUD_USER", arrStrNexdNode[1]));
                record.addField(new FieldEx("I_ISOVER", arrStrNexdNode[2]));
                tableFlowContent.addRecord(record);
                dbf.updateTable(tableFlowContent, true);
            }
            else {
                record.addField(new FieldEx("S_FLOW_ID", _strFlowId));
                record.addField(new FieldEx("S_RUN_ID", _request.getParameter(strFlowRunField)));
                record.addField(new FieldEx("S_NODE_CODE", arrStrNexdNode[0]));
                record.addField(new FieldEx("S_AUD_USER", arrStrNexdNode[1]));
                record.addField(new FieldEx("S_LAUNCH_USER", _strUserId));
                record.addField(new FieldEx("S_LAUNCH_DATE", EString.getCurDateHH()));
                record.addField(new FieldEx("S_LAUNCH_BRANCH", _strUserBranch));
                record.addField(new FieldEx("I_ISOVER", arrStrNexdNode[2]));
                tableFlowContent.addRecord(record);
                dbf.solveTable(tableFlowContent, true);
            }
        }
        catch (Exception e) {
            System.out.println("\u4fee\u6539\u6d41\u7a0b\u5185\u5bb9\u5931\u8d25\uff01" + e);
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
    }
    
    private void updateFlowContent(final String _strFlowId, final String _strFlowRunId, final String _strUserId, final String _strCurNodeId, String _strDoFlow, final String _strIsTh, final HttpServletRequest request) {
        DBFactory dbf = null;
        try {
            dbf = new DBFactory();
            final TableEx tableFlowContent = new TableEx("T_FLOWCONTENT");
            final Record record = new Record();
            if (_strDoFlow != null) {
                record.addField(new FieldEx("SFLOWID", _strFlowId, true));
                record.addField(new FieldEx("SFLOWRUNID", _strFlowRunId, true));
            }
            else {
                record.addField(new FieldEx("SFLOWID", _strFlowId));
                final boolean bIsUpdate = this.getIsUpdate(_strFlowRunId);
                if (bIsUpdate) {
                    record.addField(new FieldEx("SFLOWID", _strFlowId, true));
                    record.addField(new FieldEx("SFLOWRUNID", _strFlowRunId, true));
                    _strDoFlow = "";
                }
                else {
                    record.addField(new FieldEx("SFLOWRUNID", _strFlowRunId));
                    record.addField(new FieldEx("SUSER", _strUserId));
                    record.addField(new FieldEx("SROLE", request.getSession().getAttribute("SYS_STR_STAN_ROLECODE")));
                    record.addField(new FieldEx("SUSERBRANCH", request.getSession().getAttribute("SYS_STRBRANCHID")));
                    record.addField(new FieldEx("SFQSJ", EString.getCurDateHH()));
                }
            }
            String strNextNode = "";
            if (_strIsTh.equals("1")) {
                strNextNode = this.getNextNode(_strCurNodeId, _strFlowId, request);
                record.addField(new FieldEx("SDONODE", strNextNode));
                record.addField(new FieldEx("SCURNODEID", _strCurNodeId));
            }
            else {
                final String[] arrStrNodeMsg = this.getThMsg(_strCurNodeId, _strFlowId, _strFlowRunId, request.getParameter("t_flowhis$IPARSEQ"), request.getParameter("NO_DOSJHTJD"));
                record.addField(new FieldEx("SDONODE", arrStrNodeMsg[1]));
                record.addField(new FieldEx("SCURNODEID", arrStrNodeMsg[0]));
            }
            tableFlowContent.addRecord(record);
            final String strIsHq = request.getParameter("NO_SYS_HQUSERS");
            if (strIsHq != null) {
                record.addField(new FieldEx("S_HQZT", strIsHq));
            }
            if (_strDoFlow != null) {
                dbf.updateTable(tableFlowContent, true);
            }
            else {
                dbf.solveTable(tableFlowContent, true);
            }
            if (_strIsTh.equals("1") && !strNextNode.equals("over") && strIsHq == null) {
                final boolean bIsJump = this.doJump(_strFlowId, _strFlowRunId);
                if (!bIsJump) {
                    Debug.println("==========\u6709\u5c97\u65e0\u4eba");
                    this.updateFlowContent(_strFlowId, _strFlowRunId, _strUserId, strNextNode, "doFlow", _strIsTh, request);
                }
                else {
                    Debug.println("==========\u65e0\u9700\u8df3\u8f6c");
                }
            }
            if (strNextNode.equals("over")) {
                this.doServices(_strFlowId, request);
            }
        }
        catch (Exception e) {
            System.out.println("\u6d41\u7a0b\u53d8\u66f4\u9519\u8bef\uff01" + e);
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
    }
    
    private boolean doJump(final String flowId, final String flowRunId) {
        boolean vResult = true;
        try {
            final AuditFlow aF = (AuditFlow)Class.forName("com.bfkc.hzp.frameflow").newInstance();
            vResult = aF.getAuditRole(flowId, flowRunId);
            if (vResult) {
                final DlMsg dlMsg = new DlMsg();
                System.out.println("\u6709\u4eba:" + aF.strMob);
                dlMsg.sendMsg(aF.strMob, aF.strFlowName);
            }
            Debug.println("===============================\u662f\u5426\u6709\u4eba" + vResult);
        }
        catch (Exception e) {
            Debug.println("================================\u8df3\u8f6c\u9519\u8bef!" + e);
        }
        return vResult;
    }
    
    public void inVoke(final String _strFun, final HttpServletRequest _request) {
        try {
            final FlowApi flowApi = (FlowApi)Class.forName(FlowApi.strClass).newInstance();
            flowApi.request = _request;
            final Class clazz = flowApi.getClass();
            final Method method = clazz.getDeclaredMethod(_strFun, (Class[])new Class[0]);
            method.invoke(flowApi, new Object[0]);
        }
        catch (Exception e) {
            Debug.println(e);
            e.printStackTrace();
        }
    }
    
    private void doServices(final String flowId, final HttpServletRequest _request) {
        String vResult = "";
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("SFUN", "t_flowmain", "SFLOWID='" + flowId + "'");
            vResult = tableEx.getRecord(0).getFieldByName("SFUN").value.toString();
            this.inVoke(vResult, _request);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
    }
    
    private String[] getThMsg(final String curNodeId, final String flowId, final String flowRunId, final String _strParSeq, final String _strHTNode) {
        final String[] vResult = new String[2];
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("IFHFS,IHTFW", "t_flowdetail", "SID='" + flowId + "' and SNODEID='" + curNodeId + "'");
            Record record = tableEx.getRecord(0);
            final String strFHFS = record.getFieldByName("IFHFS").value.toString();
            final String strHTFW = record.getFieldByName("IHTFW").value.toString();
            if (strHTFW.equals("0")) {
                tableEx = new TableEx("SNODECODE,SPARNODE", "t_flowhis", "ISEQ='" + _strParSeq + "' and SRUNID='" + flowRunId + "'");
                final int iRecordCount = tableEx.getRecordCount();
                if (iRecordCount > 0) {
                    record = tableEx.getRecord(0);
                    System.out.println(record.getFieldByName("SPARNODE").value + ":" + record.getFieldByName("SNODECODE").value);
                    vResult[0] = record.getFieldByName("SPARNODE").value.toString();
                    vResult[1] = record.getFieldByName("SNODECODE").value.toString();
                }
                else {
                    vResult[1] = (vResult[0] = "wkn3");
                }
            }
            else if (strHTFW.equals("1")) {
                vResult[1] = (vResult[0] = "wkn3");
            }
            else {
                vResult[0] = this.getParNode(_strHTNode, flowId);
                vResult[1] = _strHTNode;
            }
        }
        catch (Exception ex) {
            return vResult;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
        return vResult;
    }
    
    private String getCons(final String _strCons, final HttpServletRequest request) {
        String vResult = "over";
        TableEx tableEx = null;
        final String[] arrCons = _strCons.split(",");
        final int iConSize = arrCons.length;
        this.strAuitMsg = "\u5171:" + iConSize + "\u4e2a\u6761\u4ef6<br>";
        String strWhere = "";
        final Hashtable hashCon = new Hashtable();
        for (int i = 0; i < iConSize; ++i) {
            final String[] arrConValue = arrCons[i].split(":");
            final String strWinId = arrConValue[0];
            final String strConId = arrConValue[1];
            if (strConId.equals("SYS_DEFAULT_FLOW_NODE")) {
                vResult = strWinId;
            }
            else {
                hashCon.put(strConId, strWinId);
                strWhere = "or sconid='" + strConId + "' " + strWhere;
            }
        }
        if (!strWhere.equals("")) {
            try {
                strWhere = strWhere.substring(3);
                tableEx = new TableEx("SCONID,S_EN_EXP", "t_sys_formexp", strWhere);
                for (int iConCount = tableEx.getRecordCount(), j = 0; j < iConCount; ++j) {
                    final Record record = tableEx.getRecord(j);
                    final String strCons = record.getFieldByName("S_EN_EXP").value.toString();
                    final boolean bIsTrue = this.isConNode(strCons, request);
                    if (bIsTrue) {
                        vResult = hashCon.get(record.getFieldByName("SCONID").value.toString()).toString();
                    }
                }
            }
            catch (Exception ex) {
                return vResult;
            }
            finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            if (tableEx != null) {
                tableEx.close();
            }
        }
        return vResult;
    }
    
    private String getNextNode(final String _strNodeId, final String _strFlowId, final HttpServletRequest request) {
        String strVresult = "over";
        TableEx tableEx = null;
        this.strAuitMsg = "";
        try {
            tableEx = new TableEx("*", "t_flowdetail", "SPARNODEID='" + _strNodeId + "' and SID='" + _strFlowId + "'");
            final int iRecordCount = tableEx.getRecordCount();
            if (iRecordCount != 0) {
                for (int i = 0; i < iRecordCount; ++i) {
                    final Record record = tableEx.getRecord(i);
                    final String strNodeType = record.getFieldByName("INODETYPE").value.toString();
                    final String strNodeId = record.getFieldByName("SNODEID").value.toString();
                    final String strCons = "";
                    if (strNodeType.equals("2")) {
                        strVresult = this.getCons(record.getFieldByName("SCONS").value.toString(), request);
                        if (strVresult.equals("wkn2")) {
                            strVresult = "over";
                        }
                    }
                    else {
                        strVresult = strNodeId;
                    }
                }
            }
        }
        catch (Exception ex) {
            return strVresult;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
        return strVresult;
    }
    
    private boolean isConNode(String _strCon, final HttpServletRequest request) {
        boolean vResult = false;
        _strCon = _strCon.replaceAll("@", "&");
        this.strAuitMsg = String.valueOf(this.strAuitMsg) + "\u6761\u4ef6:" + _strCon + "&nbsp;&nbsp;\u503c\uff1a";
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(_strCon);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            Object objValue = request.getParameter(aStrParam);
            if (objValue == null) {
                objValue = request.getSession().getAttribute(aStrParam);
            }
            if (objValue == null) {
                objValue = "0";
            }
            final String strReparam = aStrParam.replaceAll("\\$", "\\\\\\$");
            _strCon = _strCon.replaceAll("<<" + strReparam + ">>", objValue.toString());
        }
        System.out.println("\u6761\u4ef6:" + _strCon);
        vResult = ExpressManager.isTrue(_strCon);
        this.strAuitMsg = String.valueOf(this.strAuitMsg) + _strCon + "&nbsp;&nbsp;<font color='red'>" + vResult + "</font><br>";
        return vResult;
    }
    
    private String getParNode(final String _strNodeId, final String _strFlowId) {
        String strVresult = "over";
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("*", "t_flowdetail", "SNODEID='" + _strNodeId + "' and SID='" + _strFlowId + "'");
            final int iRecordCount = tableEx.getRecordCount();
            if (iRecordCount != 0) {
                strVresult = tableEx.getRecord(0).getFieldByName("SPARNODEID").value.toString();
            }
        }
        catch (Exception ex) {
            return strVresult;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
        return strVresult;
    }
    
    public void destroy() {
    }
}
