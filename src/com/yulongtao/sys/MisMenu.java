package com.yulongtao.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.Debug;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.page.method.Fun;
import com.timing.impcl.MantraLog;
import com.yulongtao.InitFactory;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Query;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.BimGener;
import com.yulongtao.pub.Node;
import com.yulongtao.pub.Pub;
import com.yulongtao.pub.TreeData;
import com.yulongtao.util.DataApi;
import com.yulongtao.util.EFile;
import com.yulongtao.util.EString;
import com.yulongtao.util.EncryptString;
import com.yulongtao.util.HttpUtil;
import com.yulongtao.util.MisUser;
import com.yulongtao.util.User;
import com.yulongtao.util.excel.ParseInitModel;
import com.yulongtao.web.ABSElement;
import com.yulongtao.web.YLTree;
import com.yulongtao.web.component.WebComponent;
import com.yulongtao.web.entrance.Face;
import com.yulongtao.web.event.ChangeModle;

public class MisMenu extends HttpServlet
{
    private DBFactory dbf;
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    public static String strRegUrl;
    private Vector vecCode;
    private Vector vecValue;
    
    static {
        MisMenu.strRegUrl = "http://115.28.33.76/gd/view.do?";
    }
    
    public MisMenu() {
        this.dbf = null;
        this.vecCode = new Vector();
        this.vecValue = new Vector();
    }
    
    public void init() throws ServletException {
    }
    
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=GBK");
        final String strType = request.getParameter("O_SYS_TYPE");
        if (strType == null) {
            this.viewMain(request, response);
        }
        else if (strType.equals("right")) {
            this.doRight_xlb(request, response);
        }
        else if (strType.equals("login")) {
            this.login(request, response);
        }
        else if (strType.equals("mlogin")) {
            this.memberLogin(request, response);
        }
        else if (strType.equals("edp")) {
            this.updatePass(request, response);
        }
        else if (strType.equals("applogin")) {
            if (request.getParameter("mem") != null) {
                this.appMemLogin(request, response);
            }
            else {
                this.appLogin(request, response);
            }
        }
        else if (strType.equals("loginfo")) {
            this.loginFo(request, response);
        }
        else if (strType.equals("change")) {
            this.changePass(request, response);
        }
        else if (strType.equals("changelogin")) {
            this.changePassByLogin(request, response);
        }
        else if (strType.equals("dochange")) {
            this.doChangePass(request, response);
        }
        else if (strType.equals("flat")) {
            this.viewFlatMan(request, response);
        }
        else if (strType.equals("flatmenu")) {
            this.viewFlatMenu(request, response);
        }
        else if (strType.equals("printmenu")) {
            this.viewPrint(request, response);
        }
        else if (strType.equals("sys")) {
            this.viewSys(request, response);
        }
        else if (strType.equals("dic")) {
            this.doDic(request, response);
        }
        else if (strType.equals("viewdic")) {
            this.viewDic(request, response);
        }
        else if (strType.equals("publish")) {
            this.viewPublishMsg(request, response);
        }
        else if (strType.equals("publishtz")) {
            this.viewPublishMsgTz(request, response);
        }
        else if (strType.equals("publishread")) {
            this.viewPublishMsgRead(request, response);
        }
        else if (strType.equals("isdebug")) {
            this.doDebug(request, response);
        }
        else if (strType.equals("isdebugmod")) {
            this.setDebug(request);
        }
        else if (strType.equals("refieldiscf")) {
            this.fieldIsCF(request, response);
        }
        else if (strType.equals("vmd")) {
            this.doModle(request, response);
        }
        else if (strType.equals("lisdomodle")) {
            this.listenerModle(request, response);
        }
        else if (strType.equals("viewmodlelist")) {
            this.viewModleList(request, response, "domodle");
        }
        else if (strType.equals("domodle")) {
            this.listenerModleJk(request, response);
        }
        else if (strType.equals("changemodlestatus")) {
            new ChangeModle(true).run(request.getParameter("pid"), request, response);
        }
        else if (strType.equals("curcon")) {
            this.getCurCon(response);
        }
        else if (strType.equals("defmod")) {
            this.setDefMod(request, response);
        }
        else if (strType.equals("regu")) {
            this.gegU(request, response);
        }
        else if (strType.equals("sgjs")) {
            this.saveConfig(request, response);
        }
        else if (strType.equals("cgname")) {
            this.changeFileName(request, response);
        }
        else if (strType.equals("delmupfile")) {
            this.delMupFile(request);
        }
        else if (strType.equals("restart")) {
            this.restartServer();
        }
        else if (strType.equals("getuholdmsgcsdrt")) {
            response.getWriter().print("<div style='display:none'>" + Debug.strCNMsg + "</div>");
        }
        else if (strType.equals("svpla")) {
            this.savePla(request, response.getWriter());
        }
        else if (strType.equals("repla")) {
            this.rePla(request, response.getWriter());
        }
        else if (strType.equals("auditst")) {
            this.setAditSt(request, response.getWriter());
        }
        else if (strType.equals("doaudit")) {
            this.doAditSt(request, response.getWriter());
        }
        else if (strType.equals("dofun")) {
            this.doFun(request, response);
        }
        else if (strType.equals("sndmsg")) {
            this.sndMsg(request, response.getWriter());
        }
        else if (strType.equals("weather")) {
            getWeather(request, response.getWriter());
        }
        else if (strType.equals("regusr")) {
            if (request.getParameter("mem") != null) {
                this.regMemUsr(request, response.getWriter());
            }
            else {
                this.regUsr(request, response.getWriter());
            }
        }
        else if (strType.equals("setpath")) {
            Dic.strCurRoot = this.getServletContext().getContextPath();
            Dic.strCurPath = this.getServletContext().getRealPath("/");
            Debug.strCNMsg = "88ab4226092f51b6794a84a16e14e7a9";
            Debug.bIsDoDebug = false;
            new InitFactory();
            response.getWriter().print("ok");
        }
        else if (strType.equals("resstory")) {
            final String strRes = request.getParameter("res");
            if (strRes == null) {
                this.restory(request, response.getWriter());
            }
            else {
                this.doStyleRes(request, response, strRes);
            }
        }
        else {
            this.viewMenu(request, response);
        }
    }
    
    private static void getWeather(final HttpServletRequest request, final PrintWriter _out) {
        final String[] arrStrCity = { "101100101", "101100201", "101100301", "101100401", "101100501", "101100601", "101100701", "101100801", "101100901", "101101001", "101101100" };
        final int iCityCount = arrStrCity.length;
        String strJsonWeather = "";
        try {
            for (int i = 0; i < iCityCount; ++i) {
                strJsonWeather = new String(HttpUtil.httpGet("http://www.weather.com.cn/data/cityinfo/" + arrStrCity[i] + ".html"), "utf-8");
                final ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(strJsonWeather);
                final String strStockJson = node.get("weatherinfo").toString();
                node = mapper.readTree(strStockJson);
                _out.println("<div class=\"divtablecontainer\"> <table width=\"100%\"> <tr> <td colspan=\"2\"><span class=\"divtabletitle\">" + node.get("city") + "</span><span style=\"float:right;\">" + node.get("ptime") + "</span></td> </tr>");
                _out.println("<tr><td colspan=\"2\"><span class=\"divtabletitle\">\u6700\u4f4e\u6c14\u6e29:</span>" + node.get("temp1") + "</td></tr>");
                _out.println("<tr><td colspan=\"2\"><span class=\"divtabletitle\">\u6700\u9ad8\u6c14\u6e29:</span>" + node.get("temp2") + "</td></tr>");
                _out.println("<tr><td colspan=\"2\"><span class=\"divtabletitle\">\u5929\u6c14:</span>" + node.get("weather") + "</td></tr>");
                _out.println("</table></div>");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        getWeather(null, null);
    }
    
    private void viewDicBySql(final String _strSql, final HttpServletRequest request, final HttpServletResponse response, final boolean _bIsMutSel) {
        final StringBuffer vResult = new StringBuffer("<table id='sys_dic_mutSelTab' class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'>");
        if (_bIsMutSel) {
            vResult.append("<tr class='tr1'><th class='th1'><input type='checkbox'></th><th class='th1'>\u4ee3\u7801</th><th class='th1'>\u540d\u79f0</th></tr>");
        }
        else {
            vResult.append("<tr class='tr1'><th class='th1'>\u4ee3\u7801</th><th class='th1'>\u540d\u79f0</th></tr>");
        }
        String strDicCode = "";
        String strTrClass = "tr1";
        try {
            final String[] arrDicMsg = _strSql.split(":");
            this.dbf = new DBFactory();
            final ABSElement absElement = new ABSElement();
            final TableEx tableEx = this.dbf.query(absElement.getFilterData(arrDicMsg[2], request));
            final String strCodeField = arrDicMsg[0];
            final String strNameField = arrDicMsg[1];
            final int iDicCount = tableEx.getRecordCount();
            Object objDicName = "";
            for (int i = 0; i < iDicCount; ++i) {
                final Record record = tableEx.getRecord(i);
                strDicCode = record.getFieldByName(strCodeField).value.toString();
                if (i % 2 == 0) {
                    strTrClass = "tr1";
                }
                else {
                    strTrClass = "tr1_1";
                }
                objDicName = record.getFieldByName(strNameField).value;
                if (_bIsMutSel) {
                    vResult.append("<tr onclick=\"sys_doMutSetConValue(this)\" class='" + strTrClass + "' onmouseover=\"sysMoseOverTr(this);\" onmouseout=\"sysMoseOutTr(this);\"><td class='td1' style='width:50px;'><input type='checkbox'></td><td class='td1'>").append(strDicCode).append("</td><td class='td1'>").append(objDicName).append("</td></tr>");
                }
                else {
                    vResult.append("<tr onclick=\"sys_doSetConValue('" + strDicCode + "','" + objDicName + "')\" class='" + strTrClass + "' onmouseover=\"sysMoseOverTr(this);\" onmouseout=\"sysMoseOutTr(this);\"><td class='td1'>").append(strDicCode).append("</td><td class='td1'>").append(objDicName).append("</td></tr>");
                }
            }
            final PrintWriter out = response.getWriter();
            out.print(Pub.STR_IMPORT);
            final String strWinId = request.getParameter("gs_upl_kc");
            String strParField = request.getParameter("v");
            final String strParHiddenField = strParField.replaceAll("fv", "fvh");
            if (strParHiddenField.equals(strParField)) {
                strParField = String.valueOf(strParField) + "_view";
            }
            out.print("<script>var strWinId='" + strWinId + "';function closeWin(){parent.closeWinById(strWinId);}function sys_doSetConValue(_strValue,_strText){var objParentWin=parent.getOpenPage(strWinId);objParentWin.$('" + strParField + "').value=_strText;objParentWin.$('" + strParHiddenField + "').value=_strValue;closeWin();}</script>");
            out.print(vResult.append("</table>"));
            if (_bIsMutSel) {
                out.println("<div style='width:100%;height:51px;'></div><div style='position:fixed;text-align:center;bottom:0px;width:100%;height:50px;background:#fff;'><table height='100%' align='center'><tr><td><button style='height:25px;' class='sys_form_btn' onclick=\"sys_do_dic_mutSel('" + strParField + "','" + strParHiddenField + "');\">\u786e\u5b9a</button></td><td><button style='height:25px;' class='button blue' onclick='closeWin();'>\u53d6\u6d88</button></td></tr></table></div>");
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }
        finally {
            this.dbf.close();
        }
        this.dbf.close();
    }
    
    private void viewDic(final HttpServletRequest request, final HttpServletResponse response) {
        final String _strDicType = request.getParameter("d");
        final Object objGroupSql = Dic.hashGroupCon.get(_strDicType);
        boolean bIsMutSel = false;
        final String strL = request.getParameter("l");
        if (strL.equals("in")) {
            bIsMutSel = true;
        }
        if (objGroupSql != null) {
            this.viewDicBySql(objGroupSql.toString(), request, response, bIsMutSel);
            return;
        }
        final ArrayList arrList = Dic.hashDicCode.get(_strDicType);
        final StringBuffer vResult = new StringBuffer("<table id='sys_dic_mutSelTab' class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'>");
        if (bIsMutSel) {
            vResult.append("<tr class='tr1'><th class='th1'><input type='checkbox'></th><th class='th1'>\u4ee3\u7801</th><th class='th1'>\u540d\u79f0</th></tr>");
            vResult.append("<tr class='tr1'><td class='td1'><input type='checkbox' style='display:none;'></td><td class='td1'><input type='text' id='qinputcode' style='width:100%;height:100%;border:0px;' onkeyup='sys_queryFilter(1);'></td><td class='td1'><input type='text' id='qinputname' style='width:100%;height:100%;border:0px;' onkeyup='sys_queryFilter(1);'></td></tr>");
        }
        else {
            vResult.append("<tr class='tr1'><th class='th1'>\u4ee3\u7801</th><th class='th1'>\u540d\u79f0</th></tr>");
            vResult.append("<tr class='tr1'><td class='td1'><input type='text' id='qinputcode' style='width:100%;height:100%;border:0px;' onkeyup='sys_queryFilter(0);'></td><td class='td1'><input type='text' id='qinputname' style='width:100%;height:100%;border:0px;' onkeyup='sys_queryFilter(0);'></td></tr>");
        }
        final int iDicCount = arrList.size();
        String strDicCode = "";
        Object objDicName = "";
        String strTrClass = "tr1";
        for (int i = 0; i < iDicCount; ++i) {
            strDicCode = arrList.get(i).toString();
            if (i % 2 == 0) {
                strTrClass = "tr1";
            }
            else {
                strTrClass = "tr1_1";
            }
            objDicName = Dic.hash.get(String.valueOf(_strDicType) + "_" + strDicCode);
            if (bIsMutSel) {
                vResult.append("<tr onclick=\"sys_doMutSetConValue(this)\" class='" + strTrClass + "' onmouseover=\"sysMoseOverTr(this);\" onmouseout=\"sysMoseOutTr(this);\"><td class='td1' style='width:50px;'><input type='checkbox'></td><td class='td1'>").append(strDicCode).append("</td><td class='td1'>").append(objDicName).append("</td></tr>");
            }
            else {
                vResult.append("<tr onclick=\"sys_doSetConValue('" + strDicCode + "','" + objDicName + "')\" class='" + strTrClass + "' onmouseover=\"sysMoseOverTr(this);\" onmouseout=\"sysMoseOutTr(this);\"><td class='td1'>").append(strDicCode).append("</td><td class='td1'>").append(objDicName).append("</td></tr>");
            }
        }
        try {
            final PrintWriter out = response.getWriter();
            out.print(Pub.STR_IMPORT);
            final String strWinId = request.getParameter("gs_upl_kc");
            String strParField = request.getParameter("v");
            final String strParHiddenField = strParField.replaceAll("fv", "fvh");
            if (strParHiddenField.equals(strParField)) {
                strParField = String.valueOf(strParField) + "_view";
            }
            out.print("<script>var strWinId='" + strWinId + "';function closeWin(){parent.closeWinById(strWinId);}function sys_doSetConValue(_strValue,_strText){var objParentWin=parent.getOpenPage(strWinId);objParentWin.$('" + strParField + "').value=_strText;objParentWin.$('" + strParHiddenField + "').value=_strValue;closeWin();}</script>");
            out.print(vResult.append("</table>"));
            if (bIsMutSel) {
                out.println("<div style='width:100%;height:51px;'></div><div style='position:fixed;text-align:center;bottom:0px;width:100%;height:50px;background:#fff;'><table height='100%' align='center'><tr><td><button style='height:25px;' class='sys_form_btn' onclick=\"sys_do_dic_mutSel('" + strParField + "','" + strParHiddenField + "');\">\u786e\u5b9a</button></td><td><button style='height:25px;' class='button blue' onclick='closeWin();'>\u53d6\u6d88</button></td></tr></table></div>");
            }
        }
        catch (Exception ex) {}
    }
    
    private void doFun(final HttpServletRequest request, final HttpServletResponse response) {
        final String strParams = request.getParameter("spm");
        final String[] arrStrFun = strParams.split(",");
        final int iParamCount = arrStrFun.length;
        final Fun fun = new Fun();
        fun.request = request;
        fun.response = response;
        final Class objClass = fun.getClass();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (iParamCount > 1) {
                final Class[] classParam = new Class[iParamCount - 1];
                final String[] paramValue = new String[iParamCount - 1];
                for (int i = 1; i < iParamCount; ++i) {
                    classParam[i - 1] = String.class;
                    paramValue[i - 1] = arrStrFun[i];
                }
                out.print(objClass.getMethod(arrStrFun[0], (Class[])classParam).invoke(fun, (Object[])paramValue));
            }
            else {
                out.print(Class.forName("com.page.method.Fun").getMethod(arrStrFun[0], (Class<?>[])new Class[0]).invoke(fun, new Object[0]));
            }
        }
        catch (Exception e) {
            if (out != null) {
                out.print(e);
            }
        }
    }
    
    private void appMemLogin(final HttpServletRequest request, final HttpServletResponse response) {
        response.setContentType("text/javascript charset=GBK");
        final String path = request.getContextPath();
        final String basePath = String.valueOf(request.getScheme()) + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        TableEx tableEx = null;
        try {
            final PrintWriter out = response.getWriter();
            final String strPhone = request.getParameter("user");
            tableEx = new TableEx("S_PHONE,S_PWD,I_CUR_PASS,S_NAME,S_PURVIEW", "t_sys_member", "S_PHONE='" + strPhone + "'");
            if (tableEx.getRecordCount() <= 0) {
                out.print("alert('\u6ca1\u6709\u8be5\u7528\u6237\uff01');setLoginBttn(0);");
            }
            else {
                final Record record = tableEx.getRecord(0);
                if (request.getParameter("password").equals(record.getFieldByName("S_PWD").value.toString())) {
                    final HttpSession session = request.getSession();
                    final String strName = record.getFieldByName("S_NAME").value.toString();
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_ID", (Object)strPhone);
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_NAME", (Object)strName);
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_CURPASS", (Object)record.getFieldByName("I_CUR_PASS").value.toString());
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_DO_CURPASS", (Object)record.getFieldByName("I_CUR_PASS").value.toString());
                    WebComponent.setUserCode(response, strPhone);
                    out.println("window.localStorage.userid='" + strPhone + "';");
                    out.println("window.localStorage.pwd='" + request.getParameter("password") + "';");
                    out.println("window.location='" + basePath + "ph.w?sys_c_a_y_l_p_l_a_u=" + strPhone + "&n=" + strName + "&v=" + record.getFieldByName("S_PURVIEW").value + "';");
                }
                else {
                    out.print("alert('\u5bc6\u7801\u9519\u8bef\uff01');setLoginBttn(0);");
                }
            }
        }
        catch (Exception ex) {
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
    
    public void doStyleRes(final HttpServletRequest request, final HttpServletResponse response, final String _strRes) throws ServletException, IOException {
        try {
            System.out.println("IP:" + request.getRemoteAddr());
            final DiskFileItemFactory facotry = new DiskFileItemFactory();
            final ServletFileUpload upload = new ServletFileUpload((FileItemFactory)facotry);
            upload.setHeaderEncoding("UTF-8");
            upload.setFileSizeMax(1073741824L);
            upload.setSizeMax(-2147483648L);
            final boolean bb = ServletFileUpload.isMultipartContent(request);
            if (!bb) {
                return;
            }
            final List<FileItem> items = (List<FileItem>)upload.parseRequest(request);
            String strUnZipFile = Dic.strCurPath;
            if (_strRes.equals("page")) {
                strUnZipFile = String.valueOf(strUnZipFile) + "WEB-INF" + Dic.strPathSplit + "style" + Dic.strPathSplit;
            }
            else if (_strRes.equals("res")) {
                strUnZipFile = String.valueOf(strUnZipFile) + Dic.strPathSplit;
            }
            else if (_strRes.equals("classes")) {
                strUnZipFile = String.valueOf(strUnZipFile) + "WEB-INF" + Dic.strPathSplit;
            }
            for (final FileItem item : items) {
                if (item.isFormField()) {
                    final String fieldName = item.getFieldName();
                    final String fieldValue = item.getString("UTF-8");
                    System.out.println(String.valueOf(fieldName) + "=" + fieldValue);
                }
                else {
                    String fileName = item.getName();
                    if (fileName == null) {
                        continue;
                    }
                    if (fileName.trim().equals("")) {
                        continue;
                    }
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    final InputStream in = item.getInputStream();
                    EFile.unzip(in, strUnZipFile);
                    in.close();
                    item.delete();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", (Object)"\u4e0a\u4f20\u5931\u8d25");
            request.getRequestDispatcher("/message.jsp").forward((ServletRequest)request, (ServletResponse)response);
        }
    }
    
    private void restory(final HttpServletRequest request, final PrintWriter _out) {
        String vResult = "faild";
        final String strDb = request.getParameter("pid");
        try {
            final Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec("mysql -uroot -pbfkcsxh --default-character-set=utf8 " + strDb);
            final OutputStream outputStream = process.getOutputStream();
            request.setCharacterEncoding("utf8");
            final BufferedReader reader = request.getReader();
            final char[] buf = new char[512];
            int len = 0;
            final StringBuffer contentBuffer = new StringBuffer();
            while ((len = reader.read(buf)) != -1) {
                contentBuffer.append(buf, 0, len);
            }
            final String str = contentBuffer.toString();
            final OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
            writer.write(str);
            writer.flush();
            outputStream.close();
            writer.close();
            vResult = "ok";
        }
        catch (Exception e) {
            System.out.println("err" + e);
        }
        _out.write(vResult);
    }
    
    private void regMemUsr(final HttpServletRequest request, final PrintWriter _out) {
        final String strPhone = request.getParameter("uid");
        this.dbf = new DBFactory();
        try {
            final TableEx tab = this.dbf.query("select * from t_sys_member where S_PHONE='" + strPhone + "'");
            if (tab.getRecordCount() > 0) {
                _out.print("alert('\u8be5\u7528\u6237\u5df2\u5b58\u5728\uff0c\u8bf7\u68c0\u67e5\u624b\u673a\u53f7\uff01');");
                this.dbf.close();
                return;
            }
            final TableEx tableEx = new TableEx("t_sys_member");
            final Record record = new Record();
            record.addField(new FieldEx("S_PHONE", strPhone));
            record.addField(new FieldEx("S_PWD", request.getParameter("pwd")));
            tableEx.addRecord(record);
            this.dbf.solveTable(tableEx, true);
            _out.print("alert('\u6ce8\u518c\u6210\u529f\uff01');");
        }
        catch (Exception e) {
            _out.print("alert('\u6ce8\u518c\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\uff01');");
            return;
        }
        finally {
            this.dbf.close();
        }
        this.dbf.close();
    }
    
    private void regUsr(final HttpServletRequest request, final PrintWriter _out) {
        final String strPhone = request.getParameter("uid");
        final String strVerCode = request.getParameter("ver");
        final HttpSession session = request.getSession();
        final Object objTrueVer = session.getAttribute(strPhone);
        if (objTrueVer == null || !objTrueVer.toString().equals(strVerCode)) {
            _out.print("errcode");
        }
        else {
            this.dbf = new DBFactory();
            try {
                final TableEx tableEx = new TableEx("t_sys_member");
                final Record record = new Record();
                record.addField(new FieldEx("S_PHONE", strPhone));
                record.addField(new FieldEx("S_PWD", request.getParameter("pwd")));
                tableEx.addRecord(record);
                this.dbf.solveTable(tableEx, true);
                _out.print("ok");
            }
            catch (Exception e) {
                _out.print("err");
                return;
            }
            finally {
                this.dbf.close();
            }
            this.dbf.close();
        }
    }
    
    private void sndMsg(final HttpServletRequest request, final PrintWriter _out) {
        final String str = "0123456789";
        final StringBuilder sbVerCode = new StringBuilder(4);
        for (int i = 0; i < 4; ++i) {
            final char ch = str.charAt(new Random().nextInt(str.length()));
            sbVerCode.append(ch);
        }
        final String strPhone = request.getParameter("uid");
        final String strVerCode = sbVerCode.toString();
        request.getSession().setAttribute(strPhone, (Object)strVerCode);
        _out.println("yltPhone._v='" + strVerCode + "';");
        if (request.getParameter("msg_ver") != null) {
            new DataApi().sendSms(strPhone, "TMS\u4e1a\u4e3b\u5e73\u53f0", "SMS_138077012", strVerCode, "LTAIHjTa5ZjRyvRq", "WnwwZehokctqXig7Dij2Qhf3LmJr4T");
        }
        else {
            new DataApi().sendMsg("9b79dd4ca9cf4d108ea58b38db838847", strPhone, "\u5317\u4eac\u6821\u53cb\u4f1a\u5b98\u7f51", "SMS_115135027", strVerCode);
        }
    }
    
    private void getNodes(final String _strMindId) {
        final DBFactory dbf = new DBFactory();
        final String strCurUser = "888";
        final LinkedHashMap<String, Hashtable> lhmNodes = new LinkedHashMap<String, Hashtable>();
        try {
            final TableEx tableEx = dbf.query("select * from t_sys_splitnode where S_PLA_CODE='" + _strMindId + "' order by I_LEVEL");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strNodeCode = record.getFieldByName("S_PLA_NODE_CODE").value.toString();
                lhmNodes.put(strNodeCode, (Hashtable)record.clone());
            }
        }
        catch (Exception ex) {
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private void reAllPla(final HttpServletRequest request, final PrintWriter _out) {
        final DBFactory dbf = new DBFactory();
        final String strTid = request.getParameter("tid");
        Object objCurUser = request.getSession().getAttribute("SYS_STRCURUSER");
        if (objCurUser == null) {
            try {
                final TableEx tableIsShare = dbf.query("select S_SHARE from t_projiect WHERE S_PROJIECTID='" + strTid + "'");
                if (tableIsShare.getRecordCount() > 0 && !tableIsShare.getRecord(0).getFieldByName("S_SHARE").value.toString().equals("")) {
                    objCurUser = "";
                }
            }
            catch (Exception ex) {}
        }
        final String strCurUser = objCurUser.toString();
        String strDataField = request.getParameter("sys_data");
        String strDefaultData = request.getParameter("sys_def_data");
        boolean bIsHaveData = false;
        String[] arrDataFields = null;
        if (strDataField != null) {
            bIsHaveData = true;
            arrDataFields = strDataField.split(",");
        }
        else {
            strDataField = "";
            strDefaultData = "";
        }
        try {
            final TableEx tableUser = dbf.query("SELECT distinct S_USER,S_HEADPIC,SBRANCHID from t_sys_splitnode,t_rgxx where S_PLA_CODE='" + strTid + "' and S_USER=SYGZW");
            _out.print("var sys_user_head={};var sys_user_name={};var sys_user_branch={};");
            for (int iRecordUserCount = tableUser.getRecordCount(), i = 0; i < iRecordUserCount; ++i) {
                final Record recordUser = tableUser.getRecord(i);
                final String strUser = recordUser.getFieldByName("S_USER").value.toString();
                _out.print("sys_user_head['" + strUser + "']='" + recordUser.getFieldByName("S_HEADPIC").value + "';");
                _out.print("sys_user_name['" + strUser + "']='" + Dic.hash.get("T_RGXX_" + strUser) + "';");
                _out.print("sys_user_branch['" + strUser + "']='" + Dic.hash.get("t_sys_branch_" + recordUser.getFieldByName("SBRANCHID").value) + "';");
            }
            final String strMindId = request.getParameter("tid");
            final TableEx tableEx = dbf.query("select DISTINCT t_sys_splitnode.* from t_sys_splitnode  where S_PLA_CODE='" + strTid + "'");
            final int iRecordCount = tableEx.getRecordCount();
            _out.print("yltMindDraw.bIsViewMind=true;yltMindDraw.strCreUser='" + strCurUser + "';");
            _out.print("var sys_data_field=\"" + strDataField + "\";var sys_default_data=\"" + strDefaultData + "\";");
            for (int j = 0; j < iRecordCount; ++j) {
                final Record record = tableEx.getRecord(j);
                final String strNodeCode = record.getFieldByName("S_PLA_NODE_CODE").value.toString();
                String strPCode = record.getFieldByName("S_PLA_PNODE_CODE").value.toString();
                if (strPCode.equals("")) {
                    _out.print("var objNode_" + strNodeCode + "=yltMindDraw._initRoot({");
                    _out.print("label:'" + record.getFieldByName("S_PLA_NODE_NM").value + "',");
                    _out.print("S_USER:'" + record.getFieldByName("S_USER").value + "',");
                    _out.print("S_CRE_USER:'" + record.getFieldByName("S_CRE_USER").value + "',");
                    _out.print("I_PERNUM:'" + record.getFieldByName("I_PERNUM").value + "',");
                    _out.print("S_START_DATE:'" + record.getFieldByName("S_START_DATE").value + "',");
                    _out.print("S_END_DATE:'" + record.getFieldByName("S_END_DATE").value + "',");
                    _out.print("background:'" + record.getFieldByName("S_BG_COLOR").value + "'});");
                }
                else {
                    strPCode = "objNode_" + strPCode;
                    _out.print("var objNode_" + strNodeCode + " =yltMindDraw.addNode('" + strNodeCode + "'," + strPCode + ",{label:'" + record.getFieldByName("S_PLA_NODE_NM").value + "',");
                    _out.print("S_USER:'" + record.getFieldByName("S_USER").value + "',");
                    _out.print("S_CRE_USER:'" + record.getFieldByName("S_CRE_USER").value + "',");
                    _out.print("I_PERNUM:'" + record.getFieldByName("I_PERNUM").value + "',");
                    _out.print("S_START_DATE:'" + record.getFieldByName("S_START_DATE").value + "',");
                    _out.print("S_END_DATE:'" + record.getFieldByName("S_END_DATE").value + "',");
                    _out.print("background:'" + record.getFieldByName("S_BG_COLOR").value + "'},false);");
                }
            }
            if (iRecordCount < 1) {
                final TableEx tablePro = dbf.query("select S_PROJIECTNAME from t_projiect where S_PROJIECTID='" + strTid + "'");
                _out.print("yltMindDraw._initRoot({");
                _out.print("label:'" + tablePro.getRecord(0).getFieldByName("S_PROJIECTNAME").value + "',");
                final String strUser2 = request.getParameter("u");
                final String strSD = request.getParameter("sd");
                final String strEd = request.getParameter("ed");
                _out.print("S_USER:'" + strUser2 + "',");
                _out.print("S_START_DATE:'" + strSD + "',");
                _out.print("S_END_DATE:'" + strEd + "',");
                _out.print("background:yltMindDraw.arrNodeColor[8]});");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private boolean getMindIsChange(final String strMindId, final String _strUser) {
        boolean vResult = false;
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("S_MID", "t_sys_changemind", "S_MID='" + strMindId + "' and S_USER='" + _strUser + "' and I_TYPE=1");
            if (tableEx.getRecordCount() > 0) {
                vResult = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return vResult;
        }
        finally {
            tableEx.close();
        }
        tableEx.close();
        return vResult;
    }
    
    private void generPlaData(final PrintWriter _out, final String[] _arrFields, final Record _record) {
        final StringBuffer vResult = new StringBuffer();
        final int iFieldCount = _arrFields.length;
        _out.print("data:{");
        String strSplit = "";
        for (final String strField : _arrFields) {
            _out.print(String.valueOf(strSplit) + strField + ":'" + _record.getFieldByName(strField).value + "'");
            strSplit = ",";
        }
        _out.print("},");
    }
    
    private void generPlaDefData(final PrintWriter _out, final String[] _arrFields, final String[] _arrFielDefValues) {
        final StringBuffer vResult = new StringBuffer();
        final int iFieldCount = _arrFields.length;
        _out.print("data:{");
        String strSplit = "";
        for (int i = 0; i < iFieldCount; ++i) {
            final String strField = _arrFields[i];
            _out.print(String.valueOf(strSplit) + strField + ":'" + _arrFielDefValues[i] + "'");
            strSplit = ",";
        }
        _out.print("},");
    }
    
    private void rePla(final HttpServletRequest request, final PrintWriter _out) {
        final String strAll = request.getParameter("viewtype");
        if (strAll != null) {
            this.reAllPla(request, _out);
            return;
        }
        final DBFactory dbf = new DBFactory();
        final String strTid = request.getParameter("tid");
        final String strCurUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();
        final String strTB = request.getParameter("tb");
        String strDataField = request.getParameter("sys_data");
        String strDefaultData = request.getParameter("sys_def_data");
        String strProTbName = "t_projiect";
        String strProFdName = "S_PROJIECTNAME";
        String strProFdCode = "S_PROJIECTID";
        final String strMindTable = "t_sys_splitnode";
        String strNodeSql = "select DISTINCT " + strMindTable + ".* from " + strMindTable + " ,(" + "SELECT S_PLA_CODE,S_PLA_NODE_CODE,S_PLA_NODE_NM from " + strMindTable + " where S_PLA_CODE='" + strTid + "' and I_ISLEAF=1  and (s_USER='" + strCurUser + "' or S_CRE_USER='" + strCurUser + "')" + ") t_leaf  where " + strMindTable + ".S_PLA_CODE='" + strTid + "' and " + strMindTable + ".S_PLA_NODE_CODE=left(t_leaf.S_PLA_NODE_CODE,length(" + strMindTable + ".S_PLA_NODE_CODE))  order by I_LEVEL,S_PLA_NODE_CODE";
        boolean bIsChange = false;
        if (this.getMindIsChange(strTid, strCurUser)) {
            strNodeSql = "select * from t_sys_splitnode_temp where S_PLA_CODE='" + strTid + "' and S_CHANGE_USER='" + strCurUser + "'";
            strDataField = "S_FINISH_TIME,I_FINISH_PER,S_CHANGE_USER,I_AUDIT_STATUS";
            strDefaultData = ",0," + strCurUser + ",0";
            bIsChange = true;
        }
        boolean bIsHaveData = false;
        String[] arrDataFields = null;
        if (strDataField != null) {
            bIsHaveData = true;
            arrDataFields = strDataField.split(",");
        }
        else {
            strDataField = "";
            strDefaultData = "";
        }
        if (strTB != null) {
            strProTbName = "t_sys_remind";
            strProFdName = "SREMINDNAME";
            strProFdCode = "SREMINDID";
            strNodeSql = "select * from t_sys_mindmod where S_PLA_CODE='" + strTid + "' order by I_LEVEL,S_PLA_NODE_CODE";
            _out.print("var str_sys_mod_tb='" + strTB + "';");
            bIsChange = false;
        }
        else {
            _out.print("var str_sys_mod_tb='';");
        }
        if (bIsChange) {
            _out.print("var str_sys_bIsChange=true;");
        }
        else {
            _out.print("var str_sys_bIsChange=false;");
        }
        try {
            final TableEx tableUser = dbf.query("SELECT distinct S_USER,S_HEADPIC,SBRANCHID from( SELECT S_USER,S_HEADPIC,SBRANCHID from t_sys_splitnode,t_rgxx where S_PLA_CODE='" + strTid + "' and S_USER=SYGZW union" + " select SYGZW,S_HEADPIC,SBRANCHID from t_rgxx where SROLECODE like '" + request.getSession().getAttribute("SYS_STRROLECODE") + "%' and length(SROLECODE)=length('" + request.getSession().getAttribute("SYS_STRROLECODE") + "')+3 union" + " select  S_RES_CODE,S_HEADPIC,SBRANCHID from t_pro_res,t_rgxx where S_PID='" + strTid + "' and S_RES_CODE=SYGZW)A");
            _out.print("var sys_user_head={};var sys_user_name={};var sys_user_branch={};");
            for (int iRecordUserCount = tableUser.getRecordCount(), i = 0; i < iRecordUserCount; ++i) {
                final Record recordUser = tableUser.getRecord(i);
                final String strUser = recordUser.getFieldByName("S_USER").value.toString();
                _out.print("sys_user_head['" + strUser + "']='" + recordUser.getFieldByName("S_HEADPIC").value + "';");
                _out.print("sys_user_name['" + strUser + "']='" + Dic.hash.get("T_RGXX_" + strUser) + "';");
                _out.print("sys_user_branch['" + strUser + "']='" + Dic.hash.get("t_sys_branch_" + recordUser.getFieldByName("SBRANCHID").value) + "';");
            }
            final String strMindId = request.getParameter("tid");
            final TableEx tableEx = dbf.query(strNodeSql);
            final int iRecordCount = tableEx.getRecordCount();
            _out.print("var sys_data_field=\"" + strDataField + "\";var sys_default_data=\"" + strDefaultData + "\";");
            _out.print("yltMindDraw.strCreUser='" + strCurUser + "';");
            for (int j = 0; j < iRecordCount; ++j) {
                final Record record = tableEx.getRecord(j);
                final String strNodeCode = record.getFieldByName("S_PLA_NODE_CODE").value.toString();
                String strPCode = record.getFieldByName("S_PLA_PNODE_CODE").value.toString();
                if (strPCode.equals("")) {
                    _out.print("var objNode_" + strNodeCode + "=yltMindDraw._initRoot({");
                    _out.print("label:'" + record.getFieldByName("S_PLA_NODE_NM").value + "',");
                    _out.print("S_USER:'" + record.getFieldByName("S_USER").value + "',");
                    _out.print("S_CRE_USER:'" + record.getFieldByName("S_CRE_USER").value + "',");
                    _out.print("I_PERNUM:'" + record.getFieldByName("I_PERNUM").value + "',");
                    _out.print("S_START_DATE:'" + record.getFieldByName("S_START_DATE").value + "',");
                    _out.print("S_END_DATE:'" + record.getFieldByName("S_END_DATE").value + "',");
                    if (bIsHaveData) {
                        this.generPlaData(_out, arrDataFields, record);
                    }
                    _out.print("background:'" + record.getFieldByName("S_BG_COLOR").value + "'});");
                }
                else {
                    strPCode = "objNode_" + strPCode;
                    _out.print("var objNode_" + strNodeCode + " =yltMindDraw.addNode('" + strNodeCode + "'," + strPCode + ",{label:'" + record.getFieldByName("S_PLA_NODE_NM").value + "',");
                    _out.print("S_USER:'" + record.getFieldByName("S_USER").value + "',");
                    _out.print("S_CRE_USER:'" + record.getFieldByName("S_CRE_USER").value + "',");
                    _out.print("I_PERNUM:'" + record.getFieldByName("I_PERNUM").value + "',");
                    _out.print("S_START_DATE:'" + record.getFieldByName("S_START_DATE").value + "',");
                    _out.print("S_END_DATE:'" + record.getFieldByName("S_END_DATE").value + "',");
                    if (bIsHaveData) {
                        this.generPlaData(_out, arrDataFields, record);
                    }
                    _out.print("background:'" + record.getFieldByName("S_BG_COLOR").value + "'},false);");
                }
            }
            if (iRecordCount < 1) {
                final TableEx tablePro = dbf.query("select " + strProFdName + " from " + strProTbName + " where " + strProFdCode + "='" + strTid + "'");
                _out.print("yltMindDraw._initRoot({");
                _out.print("label:'" + tablePro.getRecord(0).getFieldByName(strProFdName).value + "',");
                String strUser2 = request.getParameter("u");
                if (strTB != null) {
                    strUser2 = strCurUser;
                }
                final String strSD = request.getParameter("sd");
                final String strEd = request.getParameter("ed");
                _out.print("S_USER:'" + strUser2 + "',");
                _out.print("S_START_DATE:'" + strSD + "',");
                _out.print("S_END_DATE:'" + strEd + "',");
                if (bIsHaveData) {
                    this.generPlaDefData(_out, arrDataFields, strDefaultData.split(",", -1));
                }
                _out.print("background:yltMindDraw.arrNodeColor[8]});");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private void setAditSt(final HttpServletRequest request, final PrintWriter _out) {
        final String strTid = request.getParameter("tid");
        final String strNid = request.getParameter("nid");
        final String strCurUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();
        final DBFactory dbf = new DBFactory();
        try {
            final TableEx tableEx = dbf.query("select distinct S_CRE_USER from t_sys_splitnode where S_PLA_CODE='" + strTid + "' and S_PLA_NODE_CODE=left('" + strNid + "',length(S_PLA_NODE_CODE))  and S_CRE_USER<>'" + strCurUser + "' order by I_LEVEL desc");
            final int iRecordCount = tableEx.getRecordCount();
            String strCurAuditUser = "";
            String strAuditUsers = "";
            String strSplit = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                if (i == 0) {
                    strCurAuditUser = record.getFieldByName("S_CRE_USER").value.toString();
                }
                strAuditUsers = String.valueOf(strAuditUsers) + strSplit + record.getFieldByName("S_CRE_USER").value;
                strSplit = ",";
            }
            if (strCurAuditUser.equals("")) {
                dbf.sqlExe("update t_sys_splitnode set I_FINISH_PER=100,S_FINISH_TIME='" + EString.getCurDate() + "',I_AUDIT_STATUS =2,S_CUR_AUDIT_USR='',S_AUDIT_USERS=''  where S_PLA_CODE='" + strTid + "' and  S_PLA_NODE_CODE='" + strNid + "'", false);
                this.compGress(strTid, strNid);
                dbf.sqlExe("INSERT INTO t_sys_mind_audit_log (S_NAME,S_USER,S_TIME,I_AUD_TYPE,I_LOG,S_MID,S_NID) VALUES ('\u65e0\u9700\u5ba1\u6279','" + strCurUser + "','" + EString.getCurDate_MM() + "',1,'','" + strTid + "','" + strNid + "');", true);
            }
            else {
                dbf.sqlExe("update t_sys_splitnode set I_AUDIT_STATUS =1,S_CUR_AUDIT_USR='" + strCurAuditUser + "',S_AUDIT_USERS='" + strAuditUsers + "'  where S_PLA_CODE='" + strTid + "' and  S_PLA_NODE_CODE='" + strNid + "'", false);
                dbf.sqlExe("INSERT INTO t_sys_mind_audit_log (S_NAME,S_USER,S_TIME,I_AUD_TYPE,I_LOG,S_MID,S_NID) VALUES ('\u63d0\u4ea4\u5ba1\u6838','" + strCurUser + "','" + EString.getCurDate_MM() + "',0,'','" + strTid + "','" + strNid + "');", true);
            }
            _out.print("true");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private void doAditSt(final HttpServletRequest request, final PrintWriter _out) {
        final String strType = request.getParameter("itype");
        final String strTid = request.getParameter("tid");
        final String strNid = request.getParameter("nid");
        final String strCurUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();
        final String strLog = EString.encoderStr(request.getParameter("textlog"), "utf-8");
        final DBFactory dbf = new DBFactory();
        try {
            final String strAuditTime = EString.getCurDate_MM();
            if (strType.equals("2")) {
                dbf.sqlExe("update t_sys_splitnode set I_FINISH_PER=0,I_AUDIT_STATUS =4,S_CUR_AUDIT_USR='',S_AUDIT_USERS=''  where S_PLA_CODE='" + strTid + "' and  S_PLA_NODE_CODE='" + strNid + "'", false);
            }
            else {
                final TableEx tableEx = dbf.query("select S_AUDIT_USERS from t_sys_splitnode where S_PLA_CODE='" + strTid + "' and S_PLA_NODE_CODE='" + strNid + "'");
                final int iRecordCount = tableEx.getRecordCount();
                final Record record = tableEx.getRecord(0);
                String strCurAuditUser = "";
                final String[] arrAuditUsers = record.getFieldByName("S_AUDIT_USERS").value.toString().split(",");
                for (int iLength = arrAuditUsers.length, i = 0; i < iLength; ++i) {
                    if (arrAuditUsers[i].equals(strCurUser) && i + 1 < iLength) {
                        strCurAuditUser = arrAuditUsers[i + 1];
                        break;
                    }
                }
                if (strCurAuditUser.equals("")) {
                    dbf.sqlExe("update t_sys_splitnode set I_FINISH_PER=100,S_FINISH_TIME='" + EString.getCurDate() + "',I_AUDIT_STATUS =2,S_CUR_AUDIT_USR='" + strCurAuditUser + "'  where S_PLA_CODE='" + strTid + "' and  S_PLA_NODE_CODE='" + strNid + "'", false);
                    this.compGress(strTid, strNid);
                }
                else {
                    dbf.sqlExe("update t_sys_splitnode set S_CUR_AUDIT_USR='" + strCurAuditUser + "'  where S_PLA_CODE='" + strTid + "' and  S_PLA_NODE_CODE='" + strNid + "'", false);
                }
                dbf.sqlExe("update t_sys_mind_audit_log set S_SUCC_TIME='" + strAuditTime + "',I_IS_SUCC=1  where S_MID='" + strTid + "' and S_NID='" + strNid + "' and I_AUD_TYPE=2 and S_SUCC_TIME='' and S_USER='" + strCurUser + "'", false);
            }
            dbf.sqlExe("INSERT INTO t_sys_mind_audit_log (S_NAME,S_USER,S_TIME,I_AUD_TYPE,I_LOG,S_MID,S_NID) VALUES ('\u5ba1\u6838','" + strCurUser + "','" + strAuditTime + "'," + strType + ",'" + strLog + "','" + strTid + "','" + strNid + "');", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            dbf.close();
        }
        
        if (request.getParameter("isapp") != null) {
            _out.print("<script>my.closePageByResult('');</script>");
        }
        else {
            _out.print("<script>parent.closeById('" + request.getParameter("gs_upl_kc") + "');</script>");
        }
    }
    
    private void doCompGress(final TreeData _treeData, final String _strParentId) {
        final ArrayList arrList = _treeData.getListChildIds(_strParentId);
        final int iListSize = arrList.size();
        final int iTrueSize = iListSize - 1;
        for (int i = 0; i < iListSize; ++i) {
            final Node node = _treeData.getChid(arrList.get(i).toString());
            final ArrayList arrChildList = _treeData.getListChildIds(node.strNodeId);
            final int iChidCount = arrChildList.size();
            if (iChidCount > 0) {
                this.doCompGress(_treeData, node.strNodeId);
            }
            else {
                final Hashtable hashData = (Hashtable)node.objNodeValue;
                node.dOther = Double.parseDouble(hashData.get("I_FINISH_PER").toString());
            }
        }
        double dThisFinishPer = 0.0;
        for (int j = 0; j < iListSize; ++j) {
            final Node node2 = _treeData.getChid(arrList.get(j).toString());
            final Hashtable hashData = (Hashtable)node2.objNodeValue;
            dThisFinishPer += node2.dOther * Double.parseDouble(hashData.get("I_PERNUM").toString()) / 100.0;
        }
        if (!_strParentId.equals("")) {
            _treeData.getChid(_strParentId).dOther = dThisFinishPer;
        }
    }
    
    private void compGress(final String _strTId, final String _strNId) {
        final DBFactory dbf = new DBFactory();
        try {
            final TableEx tableEx = dbf.query("select S_PLA_NODE_CODE,S_PLA_PNODE_CODE,I_PERNUM,I_FINISH_PER,I_AUDIT_STATUS from t_sys_splitnode where S_PLA_CODE='" + _strTId + "'");
            final TreeData treeData = new TreeData();
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                treeData.addListChild(record.getFieldByName("S_PLA_NODE_CODE").value.toString(), "", record.clone(), record.getFieldByName("S_PLA_PNODE_CODE").value.toString());
            }
            this.doCompGress(treeData, "");
            for (int iUpdateCount = _strNId.length() / 3 - 1, j = 0; j < iUpdateCount; ++j) {
                final String strUpdateId = _strNId.substring(0, (j + 1) * 3);
                final double dFinish = treeData.getChid(strUpdateId).dOther;
                if (dFinish >= 100.0) {
                    dbf.sqlExe("update t_sys_splitnode set I_FINISH_PER =" + dFinish + ",S_FINISH_TIME='" + EString.getCurDate() + "'  where S_PLA_CODE='" + _strTId + "' and  S_PLA_NODE_CODE='" + strUpdateId + "'", true);
                }
                else {
                    dbf.sqlExe("update t_sys_splitnode set I_FINISH_PER =" + dFinish + "  where S_PLA_CODE='" + _strTId + "' and  S_PLA_NODE_CODE='" + strUpdateId + "'", true);
                }
            }
        }
        catch (Exception ex) {
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    public void initCompGress(final String _strTId) {
        final DBFactory dbf = new DBFactory();
        final DBFactory dbfUpdate = new DBFactory();
        try {
            final TableEx tableEx = dbf.query("select S_PLA_NODE_CODE,S_PLA_PNODE_CODE,I_PERNUM,I_FINISH_PER,S_FINISH_TIME,I_AUDIT_STATUS from t_sys_splitnode where S_PLA_CODE='" + _strTId + "'");
            final TreeData treeData = new TreeData();
            final int iRecordCount = tableEx.getRecordCount();
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                treeData.addListChild(record.getFieldByName("S_PLA_NODE_CODE").value.toString(), "", record.clone(), record.getFieldByName("S_PLA_PNODE_CODE").value.toString());
            }
            this.doCompGress(treeData, "");
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strUpdateId = record.getFieldByName("S_PLA_NODE_CODE").value.toString();
                String strFinishTime = record.getFieldByName("S_FINISH_TIME").value.toString();
                final double dFinish = treeData.getChid(strUpdateId).dOther;
                if (dFinish >= 100.0) {
                    if (strFinishTime.equals("")) {
                        strFinishTime = EString.getCurDate();
                    }
                    dbfUpdate.sqlExe("update t_sys_splitnode set I_FINISH_PER =" + dFinish + ",S_FINISH_TIME='" + strFinishTime + "'  where S_PLA_CODE='" + _strTId + "' and  S_PLA_NODE_CODE='" + strUpdateId + "'", true);
                }
                else {
                    dbfUpdate.sqlExe("update t_sys_splitnode set I_FINISH_PER =" + dFinish + ",S_FINISH_TIME='' where S_PLA_CODE='" + _strTId + "' and  S_PLA_NODE_CODE='" + strUpdateId + "'", true);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }
        finally {
            dbf.close();
            dbfUpdate.close();
        }
        dbf.close();
        dbfUpdate.close();
    }
    
    private void savePla(final HttpServletRequest request, final PrintWriter _out) {
        final String strMindId = request.getParameter("tid");
        final String[] arrNodeCode = request.getParameter("c").split(",");
        final String[] arrNodePCode = request.getParameter("p").split(",", -1);
        final String[] arrNodeName = EString.encoderStr(request.getParameter("n"), "utf-8").split(",", -1);
        final String[] arrBGColor = request.getParameter("b").split(",", -1);
        final String[] arrStrLevel = request.getParameter("l").split(",", -1);
        final String[] arrStrUser = request.getParameter("u").split(",", -1);
        final String[] arrStrPer = request.getParameter("per").split(",", -1);
        final String[] arrStrCreUser = request.getParameter("cu").split(",", -1);
        final String[] arrStartDate = request.getParameter("sd").split(",", -1);
        final String[] arrEndDate = request.getParameter("ed").split(",", -1);
        final String[] arrStrLeaf = request.getParameter("lf").split(",", -1);
        String[] arrDataField = null;
        String[] arrData = null;
        int iFieldCount = 0;
        final String strDataField = request.getParameter("f");
        boolean bIsSolveDataField = false;
        System.out.println("strDataField========" + strDataField);
        if (strDataField != null) {
            bIsSolveDataField = true;
            arrDataField = strDataField.split(",", -1);
            arrData = request.getParameter("df").split(",", -1);
            iFieldCount = arrDataField.length;
        }
        final int iNodeCount = arrNodeCode.length;
        final String strCurUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();
        TableEx tableEx = null;
        final DBFactory dbf = new DBFactory();
        final String strTB = request.getParameter("tb");
        String strProTbName = "t_sys_splitnode";
        if (this.getMindIsChange(strMindId, strCurUser)) {
            strProTbName = "t_sys_splitnode_temp";
        }
        if (strTB != null && !strTB.equals("")) {
            strProTbName = "t_sys_mindmod";
        }
        boolean bIsChange = false;
        if (strProTbName.equals("t_sys_splitnode_temp")) {
            bIsChange = true;
        }
        try {
            tableEx = new TableEx(strProTbName);
            for (int i = 0; i < iNodeCount; ++i) {
                final Record record = new Record();
                record.addField(new FieldEx("S_PLA_CODE", strMindId));
                record.addField(new FieldEx("S_PLA_NODE_CODE", arrNodeCode[i]));
                record.addField(new FieldEx("S_PLA_PNODE_CODE", arrNodePCode[i]));
                record.addField(new FieldEx("S_CRE_USER", arrStrCreUser[i]));
                record.addField(new FieldEx("I_PERNUM", arrStrPer[i]));
                record.addField(new FieldEx("I_LEVEL", arrStrLevel[i]));
                System.out.println("count:" + bIsSolveDataField);
                record.addField(new FieldEx("S_PLA_NODE_NM", arrNodeName[i]));
                record.addField(new FieldEx("S_BG_COLOR", arrBGColor[i]));
                record.addField(new FieldEx("F_PLA_TOTAL", 1));
                record.addField(new FieldEx("F_PLA_COUNT", 1));
                record.addField(new FieldEx("S_USER", arrStrUser[i]));
                record.addField(new FieldEx("S_START_DATE", arrStartDate[i]));
                record.addField(new FieldEx("S_END_DATE", arrEndDate[i]));
                record.addField(new FieldEx("I_ISLEAF", arrStrLeaf[i]));
                if (bIsSolveDataField) {
                    final String[] arrExFieldData = arrData[i].split("\\$", -1);
                    for (int j = 0; j < iFieldCount; ++j) {
                        record.addField(new FieldEx(arrDataField[j], arrExFieldData[j]));
                    }
                }
                tableEx.addRecord(record);
            }
            if (bIsChange) {
                dbf.sqlExe("delete from " + strProTbName + " where S_PLA_CODE='" + strMindId + "' and S_CHANGE_USER='" + strCurUser + "'", false);
            }
            else {
                dbf.sqlExe("delete from " + strProTbName + " where S_PLA_CODE='" + strMindId + "' and S_CRE_USER='" + strCurUser + "'", false);
            }
            dbf.solveTable(tableEx, true);
            this.initMindLeafData(strProTbName, strMindId, dbf);
            this.initMindData(strMindId);
            if (request.getParameter("dopub") != null) {
                dbf.sqlExe("INSERT INTO t_sys_pubmind (S_MID,S_USER,S_DATE) VALUES ('" + strMindId + "','" + strCurUser + "','" + EString.getCurDate() + "')", true);
            }
            _out.print("ok");
        }
        catch (Exception ex) {
            return;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
            dbf.close();
        }
        if (tableEx != null) {
            tableEx.close();
        }
        dbf.close();
    }
    
    public void initMindLeafData(final String _strProTbName, final String _strMindId, final DBFactory _dbf) throws Exception {
        _dbf.sqlExe("update " + _strProTbName + " set I_IS_TRUE_LEAF=0 where  S_PLA_CODE='" + _strMindId + "'", false);
        final TableEx tablePCode = _dbf.query("select distinct S_PLA_PNODE_CODE from " + _strProTbName + " where S_PLA_CODE='" + _strMindId + "'");
        final int iCount = tablePCode.getRecordCount();
        String strNotIn = "";
        String strSplit = "";
        for (int i = 0; i < iCount; ++i) {
            final Record record = tablePCode.getRecord(i);
            strNotIn = String.valueOf(strNotIn) + strSplit + "'" + record.getFieldByName("S_PLA_PNODE_CODE").value + "'";
            strSplit = ",";
        }
        _dbf.sqlExe("update " + _strProTbName + " set I_IS_TRUE_LEAF=1 where  S_PLA_CODE='" + _strMindId + "' and S_PLA_NODE_CODE not in(" + strNotIn + ")", false);
    }
    
    public void initMindData(final String _strTId) {
        final DBFactory dbf = new DBFactory();
        try {
            final TableEx tableEx = dbf.query("select S_PLA_NODE_CODE,S_PLA_PNODE_CODE,S_PLA_NODE_NM,I_PERNUM,I_FINISH_PER,I_AUDIT_STATUS from t_sys_splitnode where S_PLA_CODE='" + _strTId + "'");
            final TreeData treeData = new TreeData();
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                treeData.addListChild(record.getFieldByName("S_PLA_NODE_CODE").value.toString(), record.getFieldByName("S_PLA_NODE_NM").value.toString(), record.clone(), record.getFieldByName("S_PLA_PNODE_CODE").value.toString());
            }
            this.initMindNodeData(treeData, "", "", 100.0, dbf, _strTId);
        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private void initMindNodeData(final TreeData _treeData, final String _strParentId, final String _strParenFullName, final double _dPerNum, final DBFactory _dbf, final String _strMindId) throws Exception {
        final ArrayList arrList = _treeData.getListChildIds(_strParentId);
        final int iListSize = arrList.size();
        final int iTrueSize = iListSize - 1;
        for (int i = 0; i < iListSize; ++i) {
            final Node node = _treeData.getChid(arrList.get(i).toString());
            final Hashtable hashData = (Hashtable)node.objNodeValue;
            String strFullName = String.valueOf(_strParenFullName) + "/" + node.strNodeName;
            final double dOther = _dPerNum * Double.parseDouble(hashData.get("I_PERNUM").toString()) / 100.0;
            if (_strParenFullName.equals("")) {
                strFullName = node.strNodeName;
            }
            _dbf.sqlExe("update t_sys_splitnode set F_PRO_PER=" + dOther + ",S_PLA_NODE_FULLNM='" + strFullName + "' where  S_PLA_CODE='" + _strMindId + "' and S_PLA_NODE_CODE ='" + node.strNodeId + "'", false);
            final ArrayList arrChildList = _treeData.getListChildIds(node.strNodeId);
            final int iChidCount = arrChildList.size();
            if (iChidCount > 0) {
                this.initMindNodeData(_treeData, node.strNodeId, strFullName, dOther, _dbf, _strMindId);
            }
        }
    }
    
    private void delMupFile(final HttpServletRequest request) {
        final String strPath = String.valueOf(Dic.strCurPath) + "uploads" + Dic.strPathSplit + request.getParameter("p") + Dic.strPathSplit + request.getParameter("f");
        final EFile eFile = new EFile();
        eFile.delFile(strPath);
    }
    
    private StringBuffer restartServer() {
        final StringBuffer vResult = new StringBuffer();
        final Runtime rt = Runtime.getRuntime();
        try {
            final Process p = rt.exec("/usr/local/tomcat8/bin/restart.sh");
            final InputStream is = p.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                vResult.append(line);
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
        }
        catch (Exception e) {
            vResult.append("\u5f02\u5e38" + e);
        }
        return vResult;
    }
    
    private void changeFileName(final HttpServletRequest request, final HttpServletResponse response) {
        final EFile eFile = new EFile();
        boolean bIsSucces = eFile.changeFileName(String.valueOf(Dic.strCurPath) + "uploads" + Dic.strPathSplit + request.getParameter("path"), request.getParameter("nm"), request.getParameter("nnm"));
        final String strCom = request.getParameter("com");
        if (strCom != null && bIsSucces && !strCom.equals("-1")) {
            System.out.println("=======1111111111");
            final WebCommand wcommand = new WebCommand();
            bIsSucces = wcommand.doCommand(strCom, request);
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(bIsSucces);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        finally {
            out.close();
        }
        out.close();
    }
    
    private void saveConfig(final HttpServletRequest request, final HttpServletResponse response) {
        String strJs = request.getParameter("code");
        final EFile eFile = new EFile();
        String strPass = request.getParameter("pass");
        final String strCharSet = request.getParameter("charset");
        if (strCharSet != null) {
            strJs = EString.encoderStr(strJs, strCharSet);
        }
        String strPath = String.valueOf(Dic.strCurPath) + "js" + Dic.strPathSplit + "ee_pass";
        if (strPass == null) {
            strPass = new StringBuilder(String.valueOf(eFile.getFilesCount(strPath) + 1)).toString();
        }
        else if (strPass.length() > 3) {
            strPath = String.valueOf(Dic.strCurPath) + "js" + Dic.strPathSplit + "ee_work_pass";
        }
        if (strPass.equals("res")) {
            eFile.writeFile(String.valueOf(Dic.strCurPath) + "js" + Dic.strPathSplit + "ylgameres.js", new StringBuffer(strJs), false, "utf-8");
        }
        else {
            eFile.writeFile(String.valueOf(strPath) + Dic.strPathSplit + strPass + ".js", new StringBuffer(strJs), false, "utf-8");
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("ok_" + strPass);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        finally {
            out.close();
        }
        out.close();
    }
    
    private void gegU(final HttpServletRequest request, final HttpServletResponse respons) {
        final String strPwd = EncryptString.compute(request.getParameter("pwd"));
        this.dbf = new DBFactory();
        PrintWriter out = null;
        try {
            out = respons.getWriter();
            this.dbf.sqlExe("INSERT INTO t_rgxx (SBRANCHID,SYGMC,SYGMM,SYGZW,SROLECODE,FDISCOUNT,SROLEBH,S_LOCKTIME) VALUES ('001','" + EString.encoderStr(request.getParameter("nm"), "utf-8") + "','" + strPwd + "','" + request.getParameter("uid") + "','yk',0,'" + request.getParameter("em") + "','" + request.getParameter("pho") + "')", true);
            out.print("ok");
        }
        catch (Exception e) {
            out.print("\u5df2\u5b58\u5728\u8be5\u8d26\u6237\uff01");
            return;
        }
        finally {
            this.dbf.close();
        }
        this.dbf.close();
    }
    
    private void setDefMod(final HttpServletRequest request, final HttpServletResponse response) {
        this.dbf = new DBFactory();
        final HttpSession session = request.getSession();
        final String strPid = request.getParameter("pid");
        final String strPPId = request.getParameter("ppid");
        try {
            this.dbf.sqlExe("update t_rgxx set S_DEF_MOD='" + strPPId + ":" + strPid + "' where SYGZW='" + session.getAttribute("SYS_STRCURUSER") + "'", true);
            session.setAttribute("pid", (Object)strPid);
            session.setAttribute("ppid", (Object)strPPId);
            response.getWriter().print("ok");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            this.dbf.close();
        }
        this.dbf.close();
    }
    
    private void loginFo(final HttpServletRequest request, final HttpServletResponse response) {
        response.setContentType("text/javascript charset=GBK");
        final HttpSession session = request.getSession();
        TableEx tableEx = null;
        PrintWriter out = null;
        try {
            out = response.getWriter();
            tableEx = new TableEx("*", "t_fo_user", "S_UID='" + request.getParameter("user") + "'");
            if (tableEx.getRecordCount() == 0) {
                out.print("0");
            }
            else {
                final Record record = tableEx.getRecord(0);
                final String strPwd = record.getFieldByName("S_PWD").value.toString();
                if (strPwd.equals(request.getParameter("pwd"))) {
                    session.setAttribute("SYS_FO_UID", (Object)record.getFieldByName("S_UID").value.toString());
                    session.setAttribute("SYS_FO_UNAME", (Object)record.getFieldByName("S_UNAME").value.toString());
                    session.setAttribute("SYS_FO_READCOUNT", (Object)record.getFieldByName("I_READCOUNT").value.toString());
                    session.setAttribute("SYS_USER_LOGIN_DATE", (Object)EString.getCurDate());
                    out.print("ok");
                }
                else {
                    out.print("1");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            tableEx.close();
        }
        tableEx.close();
    }
    
    private void appLogin(final HttpServletRequest request, final HttpServletResponse response) {
        response.setContentType("text/javascript charset=GBK");
        final HttpSession session = request.getSession();
        final User user = new User(request);
        final String path = request.getContextPath();
        final String basePath = String.valueOf(request.getScheme()) + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        try {
            final PrintWriter out = response.getWriter();
            final int iResult = user.login();
            switch (iResult) {
                case 0: {
                    out.println("alert('\u6ca1\u6709\u8be5\u7528\u6237\uff01');");
                    break;
                }
                case 1: {
                    out.println("alert('\u5bc6\u7801\u9519\u8bef\uff01');");
                    break;
                }
                case 3: {
                    WebComponent.setUserCode(response, user.strUser);
                    String strDefPId = user.getUserMsg("S_DEF_MOD");
                    if (strDefPId.equals("")) {
                        strDefPId = user.getDefaultProjiect();
                    }
                    final String[] arrStrDefPid = strDefPId.split(":");
                    final String strPPid = arrStrDefPid[0];
                    String strPid = "";
                    if (arrStrDefPid.length > 1) {
                        strPid = arrStrDefPid[1];
                    }
                    out.println("window.localStorage.userid='" + request.getParameter("user") + "';");
                    out.println("window.localStorage.pwd='" + request.getParameter("password") + "';");
                    String strHome = "sc_home.app";
                    final String strAppHome = request.getParameter("hmp");
                    if (strAppHome != null) {
                        strHome = strAppHome;
                        String strBranchId = user.getUserMsg("SBRANCHID");
                        if (strBranchId.endsWith("0000")) {
                            strBranchId = strBranchId.substring(0, 2);
                        }
                        else if (strBranchId.endsWith("00")) {
                            strBranchId = strBranchId.substring(0, 4);
                        }
                        out.println("window.location='" + basePath + strHome + "?sys_c_a_y_l_p_l_a_u=" + user.strUser + "&t=" + EString.getCurDateParent() + "&n=" + user.getUserMsg("SYGMC") + "&b=" + strBranchId + "&tb=" + user.getUserMsg("SBRANCHID") + "&t1=" + user.getUserMsg("S_DEF_MOD") + "&t2=" + user.getUserMsg("S_OPENID") + "&r=" + user.getUserMsg("SROLECODE") + "';");
                        break;
                    }
                    out.println("window.location='" + basePath + strHome + "?u=" + user.strUser + "&t=" + EString.getCurDate() + "&p=" + strPid + "&ppid=" + strPPid + "&r=" + user.getUserMsg("SROLECODE") + "';");
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void getCurCon(final HttpServletResponse response) {
        try {
            final PrintWriter out = response.getWriter();
            out.println(EString.getCurCon());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void listenerModle(final HttpServletRequest request, final HttpServletResponse response) {
        final String strTid = request.getParameter("tid");
        if (strTid != null) {
            this.listenerModle(request, response, strTid);
            return;
        }
        PrintWriter out = null;
        final Pub pub = new Pub();
        final String strPid = request.getParameter("poid");
        try {
            out = response.getWriter();
            out.print("<!doctype html> <html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no,maximum-scale=1.0\"> <title></title> </head> <style type=\"text/css\">  html, body { margin:0px; height:100%; padding:0px;}  .cardpanel{width:100%;height:100%;border-radius:10px;color:white;background:#84ac45;text-align:right;font-size:16pt;font-family:\u96b6\u4e66;}  .cardtitle{height:74%;font-family:\u9ed1\u4f53;font-size:16pt;padding-left:50px;padding-top:10px;} .cardscript{height:26%;color:black;padding-left:30px;} .divstatus{width:80px; text-align: center; color: yellow; font-family: \u9ed1\u4f53; cursor: pointer;} .tdstatus{width:100px;} .toexcelbttn1{background: url('images/bttn/bttn_bg.png') ;border:1px solid #b3b3b3;width:90;height:22px;line-height:22px;text-align:center;vertical-align:middle;float:left;margin-right:5px;font-family:'\u5fae\u8f6f\u96c5\u9ed1,\u9ed1\u4f53';letter-spacing:5px;font-size:14px;}.file {display: none;}.button {display: inline-block; position: relative; margin-left: 20px; padding: 0 10px; text-align: center; text-decoration: none; text-shadow: 1px 1px 1px rgba(255,255,255,.22); font: bold 12px/25px \u9ed1\u4f53; -webkit-border-radius: 20px; -moz-border-radius: 20px; border-radius: 3px;  -webkit-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); -moz-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44);  -webkit-transition: all 0.15s ease; -moz-transition: all 0.15s ease; -o-transition: all 0.15s ease; -ms-transition: all 0.15s ease; transition: all 0.15s ease; float:left;}.blue {color: #19667d;  background: #70c9e3; /* Old browsers */ background: -moz-linear-gradient(top,  #70c9e3 0%, #39a0be 100%); /* FF3.6+ */ background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#70c9e3), color-stop(100%,#39a0be)); /* Chrome,Safari4+ */ background: -webkit-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Chrome10+,Safari5.1+ */ background: -o-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Opera 11.10+ */ background: -ms-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* IE10+ */ background: linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* W3C */ }</style>  <base href=\"\"> <title>\u6a21\u578b\u76d1\u63a7</title>");
            out.print("<script>Global = {};Global.baseDir =\"md\";var baseJsDir =\"md/js/\";var baseCssDir =\"md/css/\";var baseRes=\"md/res/\";var strPid=\"" + strPid + "\";");
            out.print("var strBaseUrl = \"http://");
            out.print(request.getServerName());
            out.print(":");
            out.print(request.getServerPort());
            out.print("/bim5\";var bIsJk=false;</script>");
            out.println("<script src=\"md/js/settings.js?_v=1469159954229\" class=\"lazyload\" charset=\"utf-8\"></script> <link href=\"md/css/bimviews.min.css?_v=0.0.51\" rel=\"stylesheet\" class=\"lazyload\" charset=\"utf-8\"> <script src=\"md/js/bimserverapi.js?_v=0.0.115\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/bimviews.js?_v=0.0.51\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/hammer.js\"></script> <script src=\"md/js/bimsu/BIMSURFER.js\"></script> <script src=\"md/js/bimsu/scenejs/scenejs.js\"></script> <script src=\"md/js/bimsu/SceneJS.js\"></script> <script src=\"md/js/bimsu/Constants.js\"></script> <script src=\"md/js/bimsu/ProgressLoader.js\"></script> <script src=\"md/js/bimsu/Types/Light.js\"></script> <script src=\"md/js/bimsu/Types/Light/Ambient.js\"></script> <script src=\"md/js/bimsu/Types/Light/Sun.js\"></script> <script src=\"md/js/bimsu/Control.js\"></script> <script src=\"md/js/bimsu/Control/ClickSelect.js\"></script> <script src=\"md/js/bimsu/Control/LayerList.js\"></script> <script src=\"md/js/bimsu/Control/ProgressBar.js\"></script> <script src=\"md/js/bimsu/Control/PickFlyOrbit.js\"></script> <script src=\"md/js/bimsu/Control/ObjectTreeView.js\"></script> <script src=\"md/js/bimsu/Events.js\"></script> <script src=\"md/js/bimsu/StringView.js\"></script> <script src=\"md/js/bimsu/GeometryLoader.js\"></script> <script src=\"md/js/bimsu/AsyncStream.js\"></script> <script src=\"md/js/bimsu/DataInputStream.js\"></script> <script src=\"md/js/bimsu/Viewer.js\"></script> <script src=\"md/js/bimsu/Util.js\"></script> </head> <body style=\"margin: 0; padding: 0;overflow:hidden;\"> <header> <div style=\"background:#f9f9f9;widht:100%;height:30px;\"> <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" height=\"100%\"> <tr>");
            out.println("<td><input type=\"file\" class=\"file\" onchange=\"doUploadMd(event);\"/>" + Pub.getBttn("\u4e0a\u4f20\u6a21\u578b", "selModFile();", "button blue") + "</td>");
            out.println("<td align=\"center\"><div id=\"labgjmsg\" style=\"color:#006600;font-weight:bold;text-align:center;\"></div></td></tr> </table> </div> </header> <div class=\"my-fluid-container\" style=\"padding: 0px; padding-bottom: 0px; margin-left: auto; margin-right: auto;\"> <div class=\"row\" style=\"margin-left: 0px; margin-right: 0px;\"> <div class=\"indexcontainer col-md-12\" style=\"padding-left: 0px; padding-right: 0px;\"></div> </div> </div> </body></html> <script src=\"md/js/ylbim.js\"></script> ");
            out.println("<script type='text/javascript' src='js/evenfunction.js'></script>");
            out.println(pub.getUploadScript());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void vieModleApp(final String _strTid, final HttpServletResponse response, final String strPid) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("<!doctype html> <html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no,maximum-scale=1.0\"> <title></title> </head> <style type=\"text/css\">  html, body { margin:0px; height:100%; padding:0px;}  .cardpanel{width:100%;height:100%;border-radius:10px;color:white;background:#84ac45;text-align:right;font-size:16pt;font-family:\u96b6\u4e66;}  .cardtitle{height:74%;font-family:\u9ed1\u4f53;font-size:16pt;padding-left:50px;padding-top:10px;} .cardscript{height:26%;color:black;padding-left:30px;} .divstatus{width:80px; text-align: center; color: yellow; font-family: \u9ed1\u4f53; cursor: pointer;} .tdstatus{width:100px;} .toexcelbttn1{background: url('images/bttn/bttn_bg.png') ;border:1px solid #b3b3b3;width:90;height:22px;line-height:22px;text-align:center;vertical-align:middle;float:left;margin-right:5px;font-family:'\u5fae\u8f6f\u96c5\u9ed1,\u9ed1\u4f53';letter-spacing:5px;font-size:14px;}.file {display: none;}.button {display: inline-block; position: relative; margin-left: 20px; padding: 0 10px; text-align: center; text-decoration: none; text-shadow: 1px 1px 1px rgba(255,255,255,.22); font: bold 12px/25px \u9ed1\u4f53; -webkit-border-radius: 20px; -moz-border-radius: 20px; border-radius: 3px;  -webkit-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); -moz-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44);  -webkit-transition: all 0.15s ease; -moz-transition: all 0.15s ease; -o-transition: all 0.15s ease; -ms-transition: all 0.15s ease; transition: all 0.15s ease; float:left;}.blue {color: #19667d;  background: #70c9e3; /* Old browsers */ background: -moz-linear-gradient(top,  #70c9e3 0%, #39a0be 100%); /* FF3.6+ */ background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#70c9e3), color-stop(100%,#39a0be)); /* Chrome,Safari4+ */ background: -webkit-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Chrome10+,Safari5.1+ */ background: -o-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Opera 11.10+ */ background: -ms-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* IE10+ */ background: linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* W3C */ }</style>  <base href=\"\"> <title>\u6a21\u578b\u76d1\u63a7</title>");
            out.print("<script>Global = {};Global.baseDir =\"md\";var baseJsDir =\"md/js/\";var baseCssDir =\"md/css/\";var baseRes=\"md/res/\";var strPid=\"" + strPid + "\";");
            out.print("var strBaseUrl = \"http://bim.cst360.com:8080");
            out.print("/bim5\";var bIsJk=false;var strTid=\"" + _strTid + "\";</script>");
            out.print("<link href='css/ylphone.css' rel='stylesheet' type='text/css'></link><script language=javascript src='js/ylphone.js?v=0.02'></script>");
            out.println("<script src=\"md/js/settings.js?_v=1469159954229\" class=\"lazyload\" charset=\"utf-8\"></script> <link href=\"md/css/bimviews.min.css?_v=0.0.51\" rel=\"stylesheet\" class=\"lazyload\" charset=\"utf-8\"> <script src=\"md/js/bimserverapi.js?_v=0.0.115\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/bimviews.js?_v=0.0.51\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/hammer.js\"></script> <script src=\"md/js/bimsu/BIMSURFER.js\"></script> <script src=\"md/js/bimsu/scenejs/scenejs.js\"></script> <script src=\"md/js/bimsu/SceneJS.js\"></script> <script src=\"md/js/bimsu/Constants.js\"></script> <script src=\"md/js/bimsu/ProgressLoader.js\"></script> <script src=\"md/js/bimsu/Types/Light.js\"></script> <script src=\"md/js/bimsu/Types/Light/Ambient.js\"></script> <script src=\"md/js/bimsu/Types/Light/Sun.js\"></script> <script src=\"md/js/bimsu/Control.js\"></script> <script src=\"md/js/bimsu/Control/ClickSelect.js\"></script> <script src=\"md/js/bimsu/Control/LayerList.js\"></script> <script src=\"md/js/bimsu/Control/ProgressBar.js\"></script> <script src=\"md/js/bimsu/Control/PickFlyOrbit.js\"></script> <script src=\"md/js/bimsu/Control/ObjectTreeView.js\"></script> <script src=\"md/js/bimsu/Events.js\"></script> <script src=\"md/js/bimsu/StringView.js\"></script> <script src=\"md/js/bimsu/GeometryLoader.js\"></script> <script src=\"md/js/bimsu/AsyncStream.js\"></script> <script src=\"md/js/bimsu/DataInputStream.js\"></script> <script src=\"md/js/bimsu/Viewer.js\"></script> <script src=\"md/js/bimsu/Util.js\"></script> </head> <body style=\"margin: 0; padding: 0;overflow:hidden;\"><header><div class='headertitle'> <div  onclick=\"yltPhone.closeLink('m_mbcg.w');\" class='headerleft'> <img class='headimg' src='images/phone/ico/fh.png'> </div><input type='text' style='border:1px solid #efefef;' onblur=\"queryGj(this.value)\"> </div></header>");
            out.println("<div id=\"labgjmsg\" style=\"color:#006600;font-weight:bold;text-align:center;\"></div><div class=\"my-fluid-container\" style=\"padding: 0px; padding-bottom: 0px; margin-left: auto; margin-right: auto;\"> <div class=\"row\" style=\"margin-left: 0px; margin-right: 0px;\"> <div class=\"indexcontainer col-md-12\" style=\"padding-left: 0px; padding-right: 0px;\"></div></div></div></body></html> <script src=\"md/js/ylbim.js?v=1.2\"></script> ");
            out.println("<script type='text/javascript' src='js/evenfunction.js'></script>");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listenerModle(final HttpServletRequest request, final HttpServletResponse response, final String _strTid) {
        PrintWriter out = null;
        final Pub pub = new Pub();
        String strPid = request.getParameter("poid");
        final String strDo = request.getParameter("do");
        if (strDo != null && strPid.equals("")) {
            strPid = new StringBuilder(String.valueOf(new BimGener().doFun(request, _strTid))).toString();
        }
        if (request.getParameter("tag") != null) {
            this.vieModleApp(_strTid, response, strPid);
            return;
        }
        try {
            out = response.getWriter();
            out.print("<!doctype html> <html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no,maximum-scale=1.0\"> <title></title> </head> <style type=\"text/css\">  html, body { margin:0px; height:100%; padding:0px;}  .cardpanel{width:100%;height:100%;border-radius:10px;color:white;background:#84ac45;text-align:right;font-size:16pt;font-family:\u96b6\u4e66;}  .cardtitle{height:74%;font-family:\u9ed1\u4f53;font-size:16pt;padding-left:50px;padding-top:10px;} .cardscript{height:26%;color:black;padding-left:30px;} .divstatus{width:80px; text-align: center; color: yellow; font-family: \u9ed1\u4f53; cursor: pointer;} .tdstatus{width:100px;} .toexcelbttn1{background: url('images/bttn/bttn_bg.png') ;border:1px solid #b3b3b3;width:90;height:22px;line-height:22px;text-align:center;vertical-align:middle;float:left;margin-right:5px;font-family:'\u5fae\u8f6f\u96c5\u9ed1,\u9ed1\u4f53';letter-spacing:5px;font-size:14px;}.file {display: none;}.button {display: inline-block; position: relative; margin-left: 20px; padding: 0 10px; text-align: center; text-decoration: none; text-shadow: 1px 1px 1px rgba(255,255,255,.22); font: bold 12px/25px \u9ed1\u4f53; -webkit-border-radius: 20px; -moz-border-radius: 20px; border-radius: 3px;  -webkit-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); -moz-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44);  -webkit-transition: all 0.15s ease; -moz-transition: all 0.15s ease; -o-transition: all 0.15s ease; -ms-transition: all 0.15s ease; transition: all 0.15s ease; float:left;}.blue {color: #19667d;  background: #70c9e3; /* Old browsers */ background: -moz-linear-gradient(top,  #70c9e3 0%, #39a0be 100%); /* FF3.6+ */ background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#70c9e3), color-stop(100%,#39a0be)); /* Chrome,Safari4+ */ background: -webkit-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Chrome10+,Safari5.1+ */ background: -o-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Opera 11.10+ */ background: -ms-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* IE10+ */ background: linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* W3C */ }</style>  <base href=\"\"> <title>\u6a21\u578b\u76d1\u63a7</title>");
            out.print("<script>Global = {};Global.baseDir =\"md\";var baseJsDir =\"md/js/\";var baseCssDir =\"md/css/\";var baseRes=\"md/res/\";var strPid=\"" + strPid + "\";");
            out.print("var strBaseUrl = \"http://127.0.0.1:80");
            out.print("/bim5\";var bIsJk=false;var strTid=\"" + _strTid + "\";</script>");
            out.println("<style>");
            out.println(".rdtbttn{margin-left:20px;cursor:pointer;margin-right:5px;float:left;text-align:center;line-height:30px;border:1px solid #2885d5;color:#2885d5;height:30px;width:150px;}");
            out.println(".rdtbttn:hover{background:#2885d5;color:#fff;}");
            out.println(".rdtselbttn{margin-left:20px;background:#2885d5;cursor:pointer;margin-right:5px;float:left;text-align:center;line-height:30px;border:1px solid #2885d5;color:#fff;height:30px;width:150px;}");
            out.println(".divcheckbox{margin-left:20px;cursor:pointer;margin-right:5px;float:left;text-align:center;line-height:30px;color:#2885d5;height:30px;width:150px;}");
            out.println(".ylcheckbox + label::before { content: \"\\a0\"; display: inline-block; vertical-align:1px; width:20px; height:20px; border:1px solid silver; background-color: #fafafa; text-indent: .15em; line-height:20px; } .ylcheckbox:checked + label::before { content: \"\\2713\"; background-color: #fff; border:1px solid #2885d5; color: #2885d5; } .ylcheckbox { width:20px; height:20px; position: absolute; clip: rect(0, 0, 0, 0); }");
            out.println("</style>");
            out.println("<script src=\"md/js/settings.js?_v=1469159954229\" class=\"lazyload\" charset=\"utf-8\"></script> <link href=\"md/css/bimviews.min.css?_v=0.0.51\" rel=\"stylesheet\" class=\"lazyload\" charset=\"utf-8\"> <script src=\"md/js/bimserverapi.js?_v=0.0.115\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/bimviews.js?_v=0.0.51\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/hammer.js\"></script> <script src=\"md/js/bimsu/BIMSURFER.js\"></script> <script src=\"md/js/bimsu/scenejs/scenejs.js\"></script> <script src=\"md/js/bimsu/SceneJS.js\"></script> <script src=\"md/js/bimsu/Constants.js\"></script> <script src=\"md/js/bimsu/ProgressLoader.js\"></script> <script src=\"md/js/bimsu/Types/Light.js\"></script> <script src=\"md/js/bimsu/Types/Light/Ambient.js\"></script> <script src=\"md/js/bimsu/Types/Light/Sun.js\"></script> <script src=\"md/js/bimsu/Control.js\"></script> <script src=\"md/js/bimsu/Control/ClickSelect.js\"></script> <script src=\"md/js/bimsu/Control/LayerList.js\"></script> <script src=\"md/js/bimsu/Control/ProgressBar.js\"></script> <script src=\"md/js/bimsu/Control/PickFlyOrbit.js\"></script> <script src=\"md/js/bimsu/Control/ObjectTreeView.js\"></script> <script src=\"md/js/bimsu/Events.js\"></script> <script src=\"md/js/bimsu/StringView.js\"></script> <script src=\"md/js/bimsu/GeometryLoader.js\"></script> <script src=\"md/js/bimsu/AsyncStream.js\"></script> <script src=\"md/js/bimsu/DataInputStream.js\"></script> <script src=\"md/js/bimsu/Viewer.js\"></script> <script src=\"md/js/bimsu/Util.js\"></script> </head> <body style=\"margin: 0; padding: 0;overflow:hidden;\"> <header> <div style=\"background:#f9f9f9;widht:100%;height:60px;\"> <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" height=\"100%\"> <tr>");
            out.println("<td style='background:#fafafa;height:60px;' valign='middle'>");
            if (strDo != null) {
                out.println("<div id=\"rdtbttn0\" class=\"rdtselbttn\">\u6a21\u578b</div>");
                out.println("<div id=\"rdtbttn0\" onclick=\"window.location='subresult.v?tid=" + _strTid + "&nid=000&poid=" + strPid + "';\" class=\"rdtbttn\">\u6587\u6863</div>");
                out.println("<div style='float:left;width:100px;margin-left:50px;height:30px;line-height:30px;'><input type=\"file\" class=\"file\" onchange=\"doUploadMd(event);\"/><a href=\"javascript:selModFile();\">@\u4e0a\u4f20\u6a21\u578b</a></div></td>");
                out.println(pub.getUploadScript());
            }
            else {
                out.println("<div id=\"rdtbttn0\" onclick=\"window.location='mbkb.v?tid=" + _strTid + "&poid=" + strPid + "';\" class=\"rdtbttn\">\u76ee\u6807\u770b\u677f</div>");
                out.println("<div id=\"rdtbttn0\" onclick=\"window.location='rykb.v?tid=" + _strTid + "&poid=" + strPid + "';\" class=\"rdtbttn\">\u4eba\u5458\u770b\u677f</div>");
                out.println("<div id=\"rdtbttn0\" onclick=\"window.location='mbzb.v?tid=" + _strTid + "&poid=" + strPid + "';\" class=\"rdtbttn\">\u76ee\u6807\u62a5\u544a</div>");
                out.println("<div id=\"rdtbttn0\" class=\"rdtselbttn\">\u76ee\u6807\u6210\u679c</div><div class='divcheckbox'><input class=\"ylcheckbox\" type=\"radio\" name=\"radioseltype\" id=\"radiomodle\" checked /><label for=\"radiomodle\"> \u67e5\u770b\u6a21\u578b</label></div><div class=\"divcheckbox\" onclick=\"window.location='viewresult.v?tid=" + _strTid + "&nid=000&poid=" + strPid + "';\"><input class=\"ylcheckbox\" type=\"radio\" name=\"radioseltype\" id=\"radiodoc\" /><label for=\"radiodoc\"> \u67e5\u770b\u6587\u6863</label></div></td>");
            }
            out.println("<td align=\"center\"><div id=\"labgjmsg\" style=\"color:#006600;font-weight:bold;text-align:center;\"></div></td></tr> </table> </div> </header> <div class=\"my-fluid-container\" style=\"padding: 0px; padding-bottom: 0px; margin-left: auto; margin-right: auto;\"> <div class=\"row\" style=\"margin-left: 0px; margin-right: 0px;\"> <div class=\"indexcontainer col-md-12\" style=\"padding-left: 0px; padding-right: 0px;\"></div> </div> </div> </body></html> <script src=\"md/js/ylbim.js?v=1.2\"></script> ");
            out.println("<script type='text/javascript' src='js/evenfunction.js'></script>");
            out.println(pub.getUploadScript());
            out.println("<script>parent.lxleft.strCurl='Menu?O_SYS_TYPE=lisdomodle&';</script>");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private StringBuffer getComStatus() {
        final StringBuffer vResult = new StringBuffer("var sys_objZz=new Object(),sys_objHj=new Object(),sys_objTx=new Object(),sys_objDm=new Object(),sys_objFf=new Object();");
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("S_GJH,I_GJ_ZZSL,I_GJHJSL,I_GJTXSL,I_GJDMSL,I_GJFFSL", "t_com", "");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final Object objGjh = record.getFieldByName("S_GJH").value;
                final int iZZSl = Integer.parseInt(record.getFieldByName("I_GJ_ZZSL").value.toString());
                final int iHJSl = Integer.parseInt(record.getFieldByName("I_GJHJSL").value.toString());
                final int iTXSl = Integer.parseInt(record.getFieldByName("I_GJTXSL").value.toString());
                final int iDMSl = Integer.parseInt(record.getFieldByName("I_GJDMSL").value.toString());
                final int iFFSl = Integer.parseInt(record.getFieldByName("I_GJFFSL").value.toString());
                if (iZZSl != 0) {
                    vResult.append("sys_objZz['").append(objGjh).append("']=").append(iZZSl).append(";");
                }
                if (iHJSl != 0) {
                    vResult.append("sys_objHj['").append(objGjh).append("']=").append(iHJSl).append(";");
                }
                if (iTXSl != 0) {
                    vResult.append("sys_objTx['").append(objGjh).append("']=").append(iTXSl).append(";");
                }
                if (iDMSl != 0) {
                    vResult.append("sys_objDm['").append(objGjh).append("']=").append(iDMSl).append(";");
                }
                if (iFFSl != 0) {
                    vResult.append("sys_objFf['").append(objGjh).append("']=").append(iFFSl).append(";");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
    
    private void listenerModleJk(final HttpServletRequest request, final HttpServletResponse response) {
        PrintWriter out = null;
        final Pub pub = new Pub();
        final String strPid = request.getParameter("poid");
        final String strTarget = request.getParameter("tag");
        String strDivMsg = "";
        String strSolvDivMsg = "";
        String strDivStatus;
        if (strTarget != null) {
            strDivStatus = ".divstatus{width:50px; text-align: center; color: yellow; font-family: \u9ed1\u4f53; cursor: pointer;border-radius:5px;}";
            strDivMsg = "<tr>";
            strSolvDivMsg = "<div id=\"labgjmsg\" style=\"background:#f9f9f9;z-index:1000;position: absolute;left:0px;top:30px;color:#006600;font-weight:bold;text-align:center;\"></div>";
        }
        else {
            strDivStatus = ".divstatus{width:80px; text-align: center; color: yellow; font-family: \u9ed1\u4f53; cursor: pointer;}";
            strDivMsg = "<tr><td align=\"center\"><div id=\"labgjmsg\" style=\"color:#006600;font-weight:bold;text-align:center;\"></div></td>";
        }
        try {
            out = response.getWriter();
            out.print("<!doctype html> <html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no,maximum-scale=1.0\"> <title></title> </head> <style type=\"text/css\">  html, body { margin:0px; height:100%; padding:0px;}  .cardpanel{width:100%;height:100%;border-radius:10px;color:white;background:#84ac45;text-align:right;font-size:16pt;font-family:\u96b6\u4e66;}  .cardtitle{height:74%;font-family:\u9ed1\u4f53;font-size:16pt;padding-left:50px;padding-top:10px;} .cardscript{height:26%;color:black;padding-left:30px;}");
            out.print(strDivStatus);
            out.print(".tdstatus{width:100px;} .toexcelbttn1{background: url('images/bttn/bttn_bg.png') ;border:1px solid #b3b3b3;width:90;height:22px;line-height:22px;text-align:center;vertical-align:middle;float:left;margin-right:5px;font-family:'\u5fae\u8f6f\u96c5\u9ed1,\u9ed1\u4f53';letter-spacing:5px;font-size:14px;}.file {display: none;}.button {display: inline-block; position: relative; margin-left: 20px; padding: 0 10px; text-align: center; text-decoration: none; text-shadow: 1px 1px 1px rgba(255,255,255,.22); font: bold 12px/25px \u9ed1\u4f53; -webkit-border-radius: 20px; -moz-border-radius: 20px; border-radius: 3px;  -webkit-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); -moz-box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44); box-shadow: 1px 1px 1px rgba(0,0,0,.29), inset 1px 1px 1px rgba(255,255,255,.44);  -webkit-transition: all 0.15s ease; -moz-transition: all 0.15s ease; -o-transition: all 0.15s ease; -ms-transition: all 0.15s ease; transition: all 0.15s ease; float:left;}.blue {color: #19667d;  background: #70c9e3; /* Old browsers */ background: -moz-linear-gradient(top,  #70c9e3 0%, #39a0be 100%); /* FF3.6+ */ background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#70c9e3), color-stop(100%,#39a0be)); /* Chrome,Safari4+ */ background: -webkit-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Chrome10+,Safari5.1+ */ background: -o-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* Opera 11.10+ */ background: -ms-linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* IE10+ */ background: linear-gradient(top,  #70c9e3 0%,#39a0be 100%); /* W3C */ }</style>  <base href=\"\"> <title>\u6a21\u578b\u76d1\u63a7</title>");
            out.print("<script>Global = {};Global.baseDir =\"md\";var baseJsDir =\"md/js/\";var baseCssDir =\"md/css/\";var baseRes=\"md/res/\";var strPid=\"" + strPid + "\";");
            out.print("var strBaseUrl = \"http://");
            out.print(request.getServerName());
            out.print(":");
            out.print(request.getServerPort());
            out.print("/bim5\";var bIsJk=true;");
            out.println(this.getComStatus());
            out.println("</script>");
            out.println("<script src=\"md/js/settings.js?_v=1469159954229\" class=\"lazyload\" charset=\"utf-8\"></script> <link href=\"md/css/bimviews.min.css?_v=0.0.51\" rel=\"stylesheet\" class=\"lazyload\" charset=\"utf-8\"> <script src=\"md/js/bimserverapi.js?_v=0.0.96\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/bimviews.js?_v=0.0.51\" class=\"lazyload\" charset=\"utf-8\"></script> <script src=\"md/js/hammer.js\"></script> <script src=\"md/js/bimsu/BIMSURFER.js\"></script> <script src=\"md/js/bimsu/scenejs/scenejs.js\"></script> <script src=\"md/js/bimsu/SceneJS.js\"></script> <script src=\"md/js/bimsu/Constants.js\"></script> <script src=\"md/js/bimsu/ProgressLoader.js\"></script> <script src=\"md/js/bimsu/Types/Light.js\"></script> <script src=\"md/js/bimsu/Types/Light/Ambient.js\"></script> <script src=\"md/js/bimsu/Types/Light/Sun.js\"></script> <script src=\"md/js/bimsu/Control.js\"></script> <script src=\"md/js/bimsu/Control/ClickSelect.js\"></script> <script src=\"md/js/bimsu/Control/LayerList.js\"></script> <script src=\"md/js/bimsu/Control/ProgressBar.js\"></script> <script src=\"md/js/bimsu/Control/PickFlyOrbit.js\"></script> <script src=\"md/js/bimsu/Control/ObjectTreeView.js\"></script> <script src=\"md/js/bimsu/Events.js\"></script> <script src=\"md/js/bimsu/StringView.js\"></script> <script src=\"md/js/bimsu/GeometryLoader.js\"></script> <script src=\"md/js/bimsu/AsyncStream.js\"></script> <script src=\"md/js/bimsu/DataInputStream.js\"></script> <script src=\"md/js/bimsu/Viewer.js\"></script> <script src=\"md/js/bimsu/Util.js\"></script> </head>");
            out.println("<body style=\"margin: 0; padding: 0;overflow:hidden;\">");
            out.println(strSolvDivMsg);
            out.println("<header> <div style=\"background:#f9f9f9;widht:100%;height:30px;\"> <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" height=\"100%\">");
            out.println(strDivMsg);
            out.println("<td class=\"tdstatus\"><div class=\"divstatus\" style=\"background:#ff001f;color:yellow;\" onclick=\"doSingGjColor(1);\"> \u7ec4 \u88c5 </div></td> <td class=\"tdstatus\"><div class=\"divstatus\" style=\"background:#001fff;color:yellow;\" onclick=\"doSingGjColor(2);\"> \u710a \u63a5 </div></td> <td class=\"tdstatus\"><div class=\"divstatus\" style=\"background:#00ffff;color:#196504;\" onclick=\"doSingGjColor(3);\"> \u8c03 \u4fee </div></td> <td class=\"tdstatus\"><div class=\"divstatus\" style=\"background:#ff00f4;color:#001fff;\" onclick=\"doSingGjColor(4);\"> \u6253 \u78e8 </div></td> <td class=\"tdstatus\"><div class=\"divstatus\" style=\"background:#f4ff00;color:#ff001f;\" onclick=\"doSingGjColor(5);\"> \u9632 \u8150 </div></td></tr>");
            out.println(" </table> </div> </header> <div class=\"my-fluid-container\" style=\"padding: 0px; padding-bottom: 0px; margin-left: auto; margin-right: auto;\"> <div class=\"row\" style=\"margin-left: 0px; margin-right: 0px;\"> <div class=\"indexcontainer col-md-12\" style=\"padding-left: 0px; padding-right: 0px;\"></div> </div> </div> </body></html> <script src=\"md/js/ylbim.js\"></script> ");
            out.println("<script type='text/javascript' src='js/evenfunction.js'></script>");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listenerModle_Cur_Bak(final HttpServletRequest request, final HttpServletResponse response) {
        PrintWriter out = null;
        final Pub pub = new Pub();
        try {
            final String strPid = request.getParameter("pid");
            out = response.getWriter();
            out.println("<script>var strPid='" + strPid + "';</script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/mod.js\"></script>");
            out.print("<script language=\"VBScript\" src=\"js/mod/modvb.js\"></script>");
            out.print("<script type='text/javascript' src='js/evenfunction.js'></script>");
            out.print("<script language=\"JavaScript\" FOR=\"WebViewer\" EVENT=\"OnDownloadPositionChanged(position, decompressed, total)\" src=\"js/mod/modprogress.js\"></script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/modinit.js\"></script>");
            out.print("<body background=\"images/def_pg.gif\" marginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\" onLoad=\"\"> <table width=\"100%\" height=\"100%\" style=\"background-image:url(images/content/opbg.png);border-left:1px solid #e6e6e6;border-right:1px solid #e6e6e6;\"> <tr>");
            out.print("<td width=\"100px\" height=\"25px\">" + pub.getUploadBttn_Modle_NoInvoke("\u5bfc\u5165\u6a21\u578b", strPid, request.getParameter("type")) + "</td>");
            out.print("<td align=\"left\" height=\"20px\" width=\"350px\">");
            out.print("<button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_PAN;\">\u5e73\u79fb</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_ROTATE;\">\u89d2\u5ea6</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_FLY;\">\u98de\u884c</button>&nbsp;&nbsp; <button onclick=\"WebViewer.CenterCamera();setStatus(str_cameracentered);return true;\">\u5c45\u4e2d</button>&nbsp;&nbsp; <button onclick=\"WebViewer.moveCameraToHome(); setStatus(str_homeviewset); return true;\">\u8fd8\u539f</button>");
            out.print("</td> <td> <table> <tr> <td>\u52a0\u8f7d\uff1a</td> <td><div style=\"border:1px solid red;width:100px;height:18px;\"><div  id=\"prediv\" style=\"background-color:green;width:0px;height:100%;\"></div></td> <td>\u72b6\u6001\uff1a</td>");
            out.print("</tr> </table> </td> </tr> <tr> <td colspan=\"3\"> <OBJECT ID=\"WebViewer\" WIDTH=\"100%\" height=\"100%\" CLASSID=\"CLSID:640373B0-6978-4FA5-A9FC-420ECBBC61C7\" CODEBASE=\"dll/zkitlib.dll#Version=1,1,26,20\"></OBJECT> </td> </tr> </body>");
            out.println("<script>window.onbeforeunload=function(){};window.onload=function(){initialize();};</script>");
            out.println(pub.getUploadScript());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listenerModleJk_bak(final HttpServletRequest request, final HttpServletResponse response) {
        PrintWriter out = null;
        final Pub pub = new Pub();
        try {
            final String strPid = request.getParameter("pid");
            final ParseInitModel etd = new ParseInitModel();
            final String strGx = request.getParameter("gx");
            if (strGx != null) {
                etd.initModelSplit(strPid, Integer.parseInt(strGx));
            }
            else {
                etd.initModelGrees(strPid);
            }
            out = response.getWriter();
            out.println("<script>var strPid='" + strPid + "_JK';</script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/mod.js\"></script>");
            out.print("<script language=\"VBScript\" src=\"js/mod/modvb.js\"></script>");
            out.print("<script type='text/javascript' src='js/evenfunction.js'></script>");
            out.print("<script language=\"JavaScript\" FOR=\"WebViewer\" EVENT=\"OnDownloadPositionChanged(position, decompressed, total)\" src=\"js/mod/modprogress.js\"></script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/modinit.js\"></script>");
            out.print("<body background=\"images/def_pg.gif\" marginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\" onLoad=\"\"> <table width=\"100%\" height=\"100%\" style=\"background-image:url(images/content/opbg.png);border-left:1px solid #e6e6e6;border-right:1px solid #e6e6e6;\"> <tr>");
            out.print("<td align=\"left\" height=\"20px\" width=\"350px\">");
            out.print("<button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_PAN;\">\u5e73\u79fb</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_ROTATE;\">\u89d2\u5ea6</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_FLY;\">\u98de\u884c</button>&nbsp;&nbsp; <button onclick=\"WebViewer.CenterCamera();setStatus(str_cameracentered);return true;\">\u5c45\u4e2d</button>&nbsp;&nbsp; <button onclick=\"WebViewer.moveCameraToHome(); setStatus(str_homeviewset); return true;\">\u8fd8\u539f</button>");
            out.print("</td> <td> <table> <tr> <td>\u52a0\u8f7d\uff1a</td> <td><div style=\"border:1px solid red;width:100px;height:18px;\"><div  id=\"prediv\" style=\"background-color:green;width:0px;height:100%;\"></div></td> <td>\u72b6\u6001\uff1a</td>");
            out.print("<td><div onclick=\"window.location='Menu?O_SYS_TYPE=domodle&pid=" + strPid + "&gx=2';\" style='cursor:pointer;color:yellow;font-family:\u9ed1\u4f53;background-color:#c10000;width:100px;text-align:center;'>\u7ec4\u88c5</div></td> <td><div  onclick=\"window.location='Menu?O_SYS_TYPE=domodle&pid=" + strPid + "&gx=3';\" style='cursor:pointer;color:yellow;background-color:#3aa538;width:100px;text-align:center;font-color:red;'>\u710a\u63a5</div></td> <td><div onclick=\"window.location='Menu?O_SYS_TYPE=domodle&pid=" + strPid + "&gx=4';\" style='cursor:pointer;color:yellow;background-color:#0039b4;width:100px;text-align:center;'>\u8c03\u4fee</div></td>");
            out.print("<td><div  onclick=\"window.location='Menu?O_SYS_TYPE=domodle&pid=" + strPid + "&gx=5';\" style='cursor:pointer;color:yellow;font-family:\u9ed1\u4f53;background-color:#3db7c3;width:100px;text-align:center;'>\u6253\u78e8</div></td> <td><div onclick=\"window.location='Menu?O_SYS_TYPE=domodle&pid=" + strPid + "&gx=6';\" style='cursor:pointer;color:yellow;background-color:#b2b22a;width:100px;text-align:center;font-color:red;'>\u9632\u8150</div></td>");
            out.print("</tr> </table> </td> </tr> <tr> <td colspan=\"3\"> <OBJECT ID=\"WebViewer\" WIDTH=\"100%\" height=\"100%\" CLASSID=\"CLSID:640373B0-6978-4FA5-A9FC-420ECBBC61C7\" CODEBASE=\"dll/zkitlib.dll#Version=1,1,26,20\"></OBJECT> </td> </tr> </body>");
            out.println("<script>window.onbeforeunload=function(){};window.onload=function(){initialize();};</script>");
            out.println(pub.getUploadScript());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listenerModle_Jk(final HttpServletRequest request, final HttpServletResponse response) {
        PrintWriter out = null;
        final Pub pub = new Pub();
        try {
            final String strPid = request.getParameter("pid");
            out = response.getWriter();
            out.println("<script>var strPid='" + strPid + "_JK';</script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/mod.js\"></script>");
            out.print("<script language=\"VBScript\" src=\"js/mod/modvb.js\"></script>");
            out.print("<script language=\"JavaScript\" FOR=\"WebViewer\" EVENT=\"OnDownloadPositionChanged(position, decompressed, total)\" src=\"js/mod/modprogress.js\"></script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/modinit.js\"></script>");
            out.print("<body background=\"images/def_pg.gif\" marginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\" onLoad=\"\"> <table width=\"100%\" height=\"100%\" style=\"background-image:url(images/content/opbg.png);border-left:1px solid #e6e6e6;border-right:1px solid #e6e6e6;\"> <tr>");
            out.print("<td width=\"100px\" height=\"25px\">" + pub.getUploadBttn_FreeOP_NoInvoke("\u5bfc\u5165\u6a21\u578b", strPid, request.getParameter("type")) + "</td>");
            out.print("<td align=\"left\" height=\"20px\" width=\"350px\">");
            out.print("<button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_PAN;\">\u5e73\u79fb</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_ROTATE;\">\u89d2\u5ea6</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_FLY;\">\u98de\u884c</button>&nbsp;&nbsp; <button onclick=\"WebViewer.CenterCamera();setStatus(str_cameracentered);return true;\">\u5c45\u4e2d</button>&nbsp;&nbsp; <button onclick=\"WebViewer.moveCameraToHome(); setStatus(str_homeviewset); return true;\">\u8fd8\u539f</button>");
            out.print("</td> <td> <table> <tr> <td>\u52a0\u8f7d\uff1a</td> <td><div style=\"border:1px solid red;width:100px;height:18px;\"><div  id=\"prediv\" style=\"background-color:green;width:0px;height:100%;\"></div></td> <td>\u72b6\u6001\uff1a</td>");
            out.print("<td><div style='color:yellow;font-family:\u9ed1\u4f53;background-color:#c10000;width:100px;text-align:center;'>\u5236\u4f5c\u5b8c</div></td> <td><div style='color:yellow;background-color:#3aa538;width:100px;text-align:center;font-color:red;'>\u96c6\u5c97\u5b8c</div></td> <td><div style='color:yellow;background-color:#0039b4;width:100px;text-align:center;'>\u5df2\u6536\u8d27</div></td>");
            out.print("</tr> </table> </td> </tr> <tr> <td colspan=\"2\"> <OBJECT ID=\"WebViewer\" WIDTH=\"100%\" height=\"100%\" CLASSID=\"CLSID:640373B0-6978-4FA5-A9FC-420ECBBC61C7\" CODEBASE=\"dll/zkitlib.dll#Version=1,1,26,20\"></OBJECT> </td> </tr> </body>");
            out.println("<script>window.onbeforeunload=function(){};window.onload=function(){initialize();};</script>");
            out.println(pub.getUploadScript());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void viewModleList(final HttpServletRequest request, final HttpServletResponse response, final String _strType) {
        TableEx tableEx = null;
        final String strPID = request.getParameter("pid");
        final String strParId = request.getParameter("parid");
        String _strClick = "";
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(Pub.STR_IMPORT);
            tableEx = new TableEx("SMODCODE,SMODNAME,IGJCOUNT", "t_modtree", "SPID='" + strPID + "' and SMODCODE like '" + strParId + "%'");
            final int iRecordCount = tableEx.getRecordCount();
            final EFile eFile = new EFile();
            String strParent = "";
            if (_strType.equals("domodle")) {
                strParent = "parent.";
            }
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strModId = String.valueOf(strPID) + "_" + record.getFieldByName("SMODCODE").value;
                final int iGJCount = Integer.parseInt(record.getFieldByName("IGJCOUNT").value.toString());
                if (iGJCount > 0) {
                    final int iSplitCount = iGJCount / ChangeModle.iMaxGJCount;
                    for (int k = 0; k < iSplitCount; ++k) {
                        final int iStart = k * ChangeModle.iMaxGJCount;
                        final int iEnd = iStart + ChangeModle.iMaxGJCount;
                        _strClick = "  onclick=\"document.body.innerHTML='\u6b63\u5728\u751f\u6210\u6a21\u578b...';" + strParent + "window.location='Menu?O_SYS_TYPE=" + _strType + "&pid=" + strModId + "&s=" + iStart + "&e=" + iEnd + "';\"";
                        out.print("<div style='float:left' " + _strClick + "><table cellpadding='0' cellspacing='0' class='nav_a' id='tabdomutclick'  onmouseover=\"this.background='images/menu/nav_a_hover_1.png';\" onmouseout=\"this.background='';\"> <tr> <td align='center'><img src='images/console/rep.png' class='menuico'/></td> </tr> <tr> <td align='center'class='menutitle' style='color:black;'>" + record.getFieldByName("SMODNAME").value + "_(" + (k + 1) + ")" + "</td> </tr> </table></div>");
                    }
                    final int iSplitModCount = iGJCount % ChangeModle.iMaxGJCount;
                    if (iSplitModCount > 0) {
                        final int iStart = iSplitCount * ChangeModle.iMaxGJCount;
                        final int iEnd = iStart + ChangeModle.iMaxGJCount;
                        _strClick = "  onclick=\"document.body.innerHTML='\u6b63\u5728\u751f\u6210\u6a21\u578b...';" + strParent + "window.location='Menu?O_SYS_TYPE=" + _strType + "&pid=" + strModId + "&s=" + iStart + "&e=" + iEnd + "';\"";
                        out.print("<div style='float:left' " + _strClick + "><table cellpadding='0' cellspacing='0' class='nav_a' id='tabdomutclick'  onmouseover=\"this.background='images/menu/nav_a_hover_1.png';\" onmouseout=\"this.background='';\"> <tr> <td align='center'><img src='images/console/rep.png' class='menuico'/></td> </tr> <tr> <td align='center'class='menutitle' style='color:black;'>" + record.getFieldByName("SMODNAME").value + "_(" + (iSplitCount + 1) + ")" + "</td> </tr> </table></div>");
                    }
                }
            }
        }
        catch (Exception ex) {
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
    
    private void doModle(final HttpServletRequest request, final HttpServletResponse response) {
        final String strType = request.getParameter("type");
        if (strType.equals("jk")) {
            this.viewModleList(request, response, "changemodlestatus");
            return;
        }
        PrintWriter out = null;
        final Pub pub = new Pub();
        try {
            final String strPid = request.getParameter("pid");
            out = response.getWriter();
            out.println("<script>var strPid='" + strPid + "';</script>");
            out.print("<script type='text/javascript' src='js/evenfunction.js'></script>");
            out.print("<body style='width:100%;height:100%;overflow:hidden;' background=\"images/def_pg.gif\" onload=\"initialize();\" marginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\">");
            out.print("<table border='0' width=\"100%\" height=\"25px\" style=\"background-image:url(images/content/opbg.png);border-left:1px solid #e6e6e6;border-right:1px solid #e6e6e6;\"> <tr>");
            out.print("<td width=\"100px\" height=\"25px\">" + pub.getUploadBttn_FreeOP_NoInvoke("\u5bfc\u5165\u6a21\u578b", String.valueOf(strPid) + "_" + request.getParameter("parid"), request.getParameter("type")) + "</td>");
            out.print("<td align=\"left\" height=\"25px\">");
            out.print("<button onclick=\"miniWin('\u65b0\u5efa\u8282\u70b9','','View?SPAGECODE=1416459156028&pid=" + strPid + "&parid=" + request.getParameter("parid") + "&SSIZE=500,200',500,200,'','');\">\u65b0\u5efa\u8282\u70b9</button>");
            out.print("</td></tr></table>");
            out.print("<iframe src='Menu?O_SYS_TYPE=viewmodlelist&parid=" + request.getParameter("parid") + "&pid=" + strPid + "' width='100%' height='100%'  frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='no'></iframe></body>");
            out.print(pub.getUploadScript());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void viewModle(final HttpServletRequest request, final HttpServletResponse response) {
        PrintWriter out = null;
        final Pub pub = new Pub();
        try {
            final String strPid = request.getParameter("pid");
            out = response.getWriter();
            out.println("<script>var strPid='" + strPid + "';</script>");
            out.print("<script language=\"JavaScript\" src=\"js/mod/mod.js\"></script> <script language=\"VBScript\" src=\"js/mod/modvb.js\"></script> <script language=\"JavaScript\" src=\"js/mod/modinit.js\"></script> <script language=\"JavaScript\" FOR=\"WebViewer\" EVENT=\"OnDownloadPositionChanged(position, decompressed, total)\" src=\"js/mod/modprogress.js\"></script> <script language=\"JavaScript\" FOR=\"WebViewer\" EVENT=\"OnNamedLocationsChanged(itemId)\" src=\"js/mod/modlocation.js\"></script> <script LANGUAGE=\"JavaScript\" FOR=\"WebViewer\" EVENT=\"OnInteractorModeChanged()\" src=\"js/mod/modchange.js\"></script>");
            out.print("<body background=\"images/def_pg.gif\" marginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\" onLoad=\"initialize()\"> <table width=\"100%\" height=\"100%\" style=\"background-image:url(images/content/opbg.png);border-left:1px solid #e6e6e6;border-right:1px solid #e6e6e6;\"> <tr> <td align=\"left\" height=\"20px\" width=\"350px\">");
            out.print("<button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_PAN;\">\u5e73\u79fb</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_ROTATE;\">\u89d2\u5ea6</button>&nbsp;&nbsp; <button onclick=\"WebViewer.interactorMode = INTERACTOR_MODE_PRE_FLY;\">\u98de\u884c</button>&nbsp;&nbsp; <button onclick=\"WebViewer.CenterCamera();setStatus(str_cameracentered);return true;\">\u5c45\u4e2d</button>&nbsp;&nbsp; <button onclick=\"WebViewer.moveCameraToHome(); setStatus(str_homeviewset); return true;\">\u8fd8\u539f</button>");
            out.print("</td> <td> <table> <tr> <td>\u52a0\u8f7d\uff1a</td> <td><div style=\"border:1px solid red;width:100px;height:18px;\"><div  id=\"prediv\" style=\"background-color:green;width:0px;height:100%;\"></div></td> <td>&nbsp;</td>");
            out.print("<td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td>");
            out.print("</tr> </table> </td> </tr> <tr> <td colspan=\"2\"> <OBJECT ID=\"WebViewer\" WIDTH=\"100%\" height=\"100%\" CLASSID=\"CLSID:640373B0-6978-4FA5-A9FC-420ECBBC61C7\" CODEBASE=\"dll/zkitlib.dll#Version=1,1,26,20\"></OBJECT> </td> </tr> </body>");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void fieldIsCF(final HttpServletRequest request, final HttpServletResponse response) {
        final String[] arrStrTableMsg = request.getParameter("sys_tables").split("\\$");
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("*", arrStrTableMsg[0], String.valueOf(arrStrTableMsg[1]) + "='" + request.getParameter("sys_tabvalue") + "'");
            if (tableEx.getRecordCount() > 0) {
                response.getWriter().print("ok");
            }
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
    
    private void doRight(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final PrintWriter out = response.getWriter();
            final String strChildType = request.getParameter("sys_childtype");
            if (strChildType == null) {
                final String strRoleCode = request.getParameter("rolecode");
                final String strRoleName = EString.encoderStr(request.getParameter("rolename"));
                Pub.importHead(out, request, "");
                out.println("<html><HEAD><TITLE>\u6743\u9650\u7ba1\u7406</TITLE>");
                out.println("<table cellpadding='0' cellspacing='0' border='0' width='100%' height='100%'><tr><td colspan='2' height='28px'>");
                out.println("<table cellpadding='0' cellspacing='0' border='0' class='bttoparea' height='28' width='100%'><tr><td align='left'><b>&nbsp;&nbsp;\u4e3a\u89d2\u8272[" + EString.encoderStr(request.getParameter("rolename")) + "]\u6388\u6743</b></td><td align='right' width='100px'>" + Pub.getBttn(Pub.getBttnText("save.png", "\u4fdd\u5b58"), "doRights('" + request.getParameter("rolecode") + "');", "button") + "</td></tr></table>");
                out.println("</td></tr>");
                out.println("<tr><td width='50%' height='28px'><table class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'><tr class='tr1'><th class='th1'>\u529f\u80fd\u6388\u6743</th></tr></table></td>");
                out.println("<td><table class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'><tr class='tr1'><th class='th1'>\u5df2\u9009\u6743\u9650</th></tr></table></td></tr>");
                out.println("<tr><td width='50%'>");
                this.generModRight(request, out);
                out.println("</td><td><div id='divhashrights' style='width:100%;height:100%;overflow:auto;'></div>");
                out.println("</td></tr></table>");
                out.println("<script>");
                out.println("function doRights(_strRoleCode){var strParam='rolecode='+_strRoleCode;for(var k in sys_dic_tree_tree.nodeHash){var node=sys_dic_tree_tree.nodeHash[k];if(node.checked==1||node.checked==2){var value=node.attributes['nodeCode'];if(value!='')strParam+='&modecode='+value+','+node.checked}};var vResult=getTx(strParam,'Menu?O_SYS_TYPE=right&sys_childtype=save');if(vResult=='ok')alert('\u4fdd\u5b58\u6210\u529f\uff01');else alert('\u4fdd\u5b58\u5931\u8d25!'+vResult)};var objParRightsCodes;var objRights;var strGenerRightsHtml;function do_sysTreeCheckEvent(){initRights()};function initRights(){objParRightsCodes=new Object();objRights=new Object();strGenerRightsHtml='';for(var k in sys_dic_tree_tree.nodeHash){var node=sys_dic_tree_tree.nodeHash[k];if(node.checked==1||node.checked==2){var value=node.attributes['nodeCode'];objRights[value]=node.attributes['text'];var strParCode=node.attributes['PCODE'];if(objParRightsCodes[strParCode]==null){objParRightsCodes[strParCode]=new Array();objParRightsCodes[strParCode][0]=value}else{var iLength=objParRightsCodes[strParCode].length;objParRightsCodes[strParCode][iLength]=value}}};generHashRights(objParRightsCodes,'');divhashrights.innerHTML=strGenerRightsHtml};function generHashRights(objParRightsCodes,_parCode){var arrStrChilds=objParRightsCodes[_parCode];if(arrStrChilds!=null){if(_parCode=='')strGenerRightsHtml+=\"<li class='rightsli'>\u529f\u80fd\u6743\u9650<ul class='rightsul'>\";else strGenerRightsHtml+=\"<li class='rightsli'>\"+objRights[_parCode]+\"<ul>\";var iCodes=arrStrChilds.length;for(var i=0;i<iCodes;i++){var strCode=arrStrChilds[i];generHashRights(objParRightsCodes,strCode)};strGenerRightsHtml+=\"</ul></li>\"}else{if(_parCode==\"\")strGenerRightsHtml+=\"<li class='rightsli'>\u529f\u80fd\u6743\u9650</li>\";else strGenerRightsHtml+=\"<li class='rightsli'>\"+objRights[_parCode]+\"</li>\"}}");
                out.println("initRights();</script>");
                out.println("</HTML>");
            }
            else if (strChildType.equals("top")) {
                out.print("<link href='css/table.css' rel='stylesheet' type='text/css'/>");
            }
            else if (strChildType.equals("left")) {
                Pub.importHead(out, request, "");
                this.generModRight(request, out);
            }
            else if (strChildType.equals("save")) {
                this.saveRights(request, response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void doRightAndFields(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final PrintWriter out = response.getWriter();
            final String strChildType = request.getParameter("sys_childtype");
            if (strChildType == null) {
                final String strRoleCode = request.getParameter("rolecode");
                final String strRoleName = EString.encoderStr(request.getParameter("rolename"));
                out.println("<html><HEAD><TITLE>\u6743\u9650\u7ba1\u7406</TITLE>");
                out.println("<FRAMESET border='0' name='main' frameSpacing='0' rows='28,*' frameBorder='no' cols='*'>");
                out.println("<FRAME id='lxtop' name=lxtop src='Menu?O_SYS_TYPE=right&sys_childtype=top&rolename=" + strRoleName + "&rolecode=" + strRoleCode + "' noResize scrolling='no'>");
                out.println("<FRAMESET border='0' name='main' frameSpacing='0' rows='*' frameBorder='no' cols='300,7,*'>");
                out.println("<FRAME id='lxleft' name='lxleft' src='Menu?O_SYS_TYPE=right&sys_childtype=left&rolecode=" + strRoleCode + "' noResize>");
                out.println("<FRAME id='lineFrame' name='lineFrame' src='line.html' noResize scrolling='no'>");
                out.println("<FRAME id='lxmain ' name='lxmain' src='Menu?O_SYS_TYPE=right&sys_childtype=main&rolecode=" + strRoleCode + "' noResize>");
                out.println("</FRAMESET>");
                out.println("</FRAMESET>");
                out.println("</HTML>");
            }
            else if (strChildType.equals("top")) {
                out.print("<link href='css/table.css' rel='stylesheet' type='text/css'/>");
                out.println("<table cellpadding='0' cellspacing='0' border='0' class='bttoparea' height='28'><tr><td align='left'><b>\u4e3a\u89d2\u8272[" + EString.encoderStr(request.getParameter("rolename")) + "]\u6388\u6743</b></td><td align='right'>&nbsp;&nbsp;<button id='addbtn' type='button' onclick=\"parent.lxleft.doRights('" + request.getParameter("rolecode") + "');\"><img src='/pin/images/eve/save.png' align='absmiddle'>&nbsp;\u4fdd\u5b58</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr></table>");
            }
            else if (strChildType.equals("left")) {
                Pub.importHead(out, request, "");
                this.generModRight(request, out);
            }
            else if (strChildType.equals("main")) {
                Pub.importHead(out, request, "");
                this.generFieldRight(request, out);
            }
            else if (strChildType.equals("save")) {
                this.saveRights(request, response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void generFieldRight(final HttpServletRequest request, final PrintWriter out) {
        TableEx tableEx = null;
        final TableEx tableChecked = null;
        out.println("<table class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'><tr class='tr1'><th class='th1'>\u5b57\u6bb5\u6388\u6743<a id='sys_fieldindex' href=''></a></th></tr></table>");
        try {
            tableEx = new TableEx("SMODCODE,SMODNAME,SURL,left(SMODCODE,length(SMODCODE)-3) PCODE", "t_sys_mod", "");
            final int iRecordCount = tableEx.getRecordCount();
            for (int i = iRecordCount - 1; i >= 0; --i) {
                final Record record = tableEx.getRecord(i);
                final Object objModUrl = record.getFieldByName("SURL").value;
                final String strModCode = record.getFieldByName("SMODCODE").value.toString();
                final String strModName = record.getFieldByName("SMODNAME").value.toString();
                if (objModUrl != null && !objModUrl.equals("")) {
                    this.addRightsChild("", objModUrl.toString(), out, strModCode, strModName);
                }
            }
        }
        catch (Exception ex) {
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
    
    private void addRightsChild(final String _strPid, final String _strUrl, final PrintWriter out, final String _strModCode, final String _strModName) {
        final int iPageIndex = _strUrl.indexOf("View?SPAGECODE=");
        if (iPageIndex != -1) {
            int iPageEnd = _strUrl.indexOf("&");
            if (iPageEnd == -1) {
                iPageEnd = _strUrl.length();
            }
            final String strPageCode = _strUrl.substring(iPageIndex + 15, iPageEnd);
            final Object objHQRC = QRSC.HASHQRSC.get(strPageCode);
            if (objHQRC != null) {
                final Hashtable hashHQRC = (Hashtable)objHQRC;
                final String strPageType = hashHQRC.get("SPAGETYPE").toString();
                if (strPageType.equals("1")) {
                    this.getRightsBttn(_strPid, hashHQRC, out, _strModCode, _strModName);
                }
                else if (strPageType.equals("10")) {
                    this.getFrameRights(_strPid, hashHQRC, hashHQRC.get("SQUERYFIELD").toString(), out, _strModCode, _strModName);
                    this.getFrameRights(_strPid, hashHQRC, hashHQRC.get("SQLFIELD").toString(), out, _strModCode, _strModName);
                }
            }
        }
    }
    
    private void getFrameRights(final String _strPid, final Hashtable _hashHQRC, final String _strPages, final PrintWriter out, final String _strModCode, final String _strModName) {
        final String[] arrStrPages = _strPages.split(",");
        for (int iPageCount = arrStrPages.length, i = 0; i < iPageCount; ++i) {
            if (!arrStrPages[i].equals("") && !arrStrPages[i].startsWith("$PARAM")) {
                final Hashtable hashHQRCTemp = (Hashtable) QRSC.HASHQRSC.get(arrStrPages[i]);
                final String strTypeTemp = hashHQRCTemp.get("SPAGETYPE").toString();
                if (strTypeTemp.equals("1")) {
                    this.getRightsBttn(_strPid, hashHQRCTemp, out, _strModCode, _strModName);
                }
            }
        }
    }
    
    private void getRightsBttn(final String _strPid, final Hashtable _hashHQRC, final PrintWriter out, final String _strModCode, final String _strModName) {
        final String[] arrcode = _hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrname = _hashHQRC.get("SFIELDNAME").toString().split(",");
        final int iCodeLength = arrcode.length;
        out.println("<div style='width:100%;background-image:url(images/content/titleblankbg.gif);'><table border='0' cellpadding='0' cellspacing='0'> <tr><td style='height:29px;color:white;background-image:url(images/content/titlebg.gif);'><b><a name='#" + _strModCode + "'></a>&nbsp;<input type='checkbox'>");
        out.println(_strModName);
        out.println("</b></td><td><img src='images/content/titleborder.gif'></td></tr></table></div>");
        for (int i = 0; i < iCodeLength; ++i) {
            out.println("<br><input type='checkbox'><lable>");
            out.println(arrname[i]);
            out.println("</lable>");
        }
        out.println("<br><br>");
    }
    
    private void saveRights_xlb(final HttpServletRequest request, final HttpServletResponse response) {
        final String[] arrStrRightCodes = request.getParameterValues("modecode");
        final String strRoleCode = request.getParameter("rolecode");
        int iRecordCount = 0;
        if (arrStrRightCodes != null) {
            iRecordCount = arrStrRightCodes.length;
        }
        TableEx tableEx = null;
        TableEx tableExDel = null;
        final DBFactory dbf = new DBFactory();
        try {
            tableEx = new TableEx("t_roleright");
            tableExDel = new TableEx("t_roleright");
            Record record = new Record();
            record.addField(new FieldEx("SROLECODE", strRoleCode, true));
            boolean bIsNoRights = true;
            tableExDel.addRecord(record);
            for (int i = 0; i < iRecordCount; ++i) {
                record = new Record();
                record.addField(new FieldEx("SROLECODE", strRoleCode));
                final String[] arrStrCode = arrStrRightCodes[i].replaceAll("@", "&").split(",");
                record.addField(new FieldEx("SRIGHTCODE", arrStrCode[0]));
                if (arrStrCode.length > 1) {
                    record.addField(new FieldEx("IMODSTATUS", arrStrCode[1]));
                }
                else {
                    record.addField(new FieldEx("IMODSTATUS", "1"));
                }
                tableEx.addRecord(record);
                bIsNoRights = false;
            }
            dbf.deleteRecord(tableExDel, true);
            if (!bIsNoRights) {
                dbf.solveTable(tableEx, true);
            }
            System.out.println("==============OK");
            response.getWriter().print("ok");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
        if (tableEx != null) {
            tableEx.close();
        }
    }
    
    private void saveRights(final HttpServletRequest request, final HttpServletResponse response) {
        final String[] arrStrRightCodes = request.getParameterValues("modecode");
        final String strRoleCode = request.getParameter("rolecode");
        int iRecordCount = 0;
        if (arrStrRightCodes != null) {
            iRecordCount = arrStrRightCodes.length;
        }
        TableEx tableEx = null;
        TableEx tableExDel = null;
        final DBFactory dbf = new DBFactory();
        try {
            tableEx = new TableEx("t_roleright");
            tableExDel = new TableEx("t_roleright");
            Record record = new Record();
            record.addField(new FieldEx("SROLECODE", strRoleCode, true));
            boolean bIsNoRights = true;
            tableExDel.addRecord(record);
            for (int i = 0; i < iRecordCount; ++i) {
                record = new Record();
                record.addField(new FieldEx("SROLECODE", strRoleCode));
                final String[] arrStrCode = arrStrRightCodes[i].split(",");
                record.addField(new FieldEx("SRIGHTCODE", arrStrCode[0]));
                record.addField(new FieldEx("IMODSTATUS", arrStrCode[1]));
                tableEx.addRecord(record);
                bIsNoRights = false;
            }
            dbf.deleteRecord(tableExDel, bIsNoRights);
            if (!bIsNoRights) {
                dbf.solveTable(tableEx, true);
            }
            response.getWriter().print("ok");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (dbf != null) {
            dbf.close();
        }
        if (tableEx != null) {
            tableEx.close();
        }
    }
    
    private void doRight_xlb(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final PrintWriter out = response.getWriter();
            final String strChildType = request.getParameter("sys_childtype");
            if (strChildType == null) {
                final String strRoleCode = request.getParameter("rolecode");
                final String strRoleName = EString.encoderStr(request.getParameter("rolename"));
                Pub.importHead(out, request, "");
                out.println("<html><HEAD><TITLE>\u6743\u9650\u7ba1\u7406</TITLE>");
                out.println("<table cellpadding='0' cellspacing='0' border='0' width='100%' height='100%'><tr><td colspan='2' height='28px'>");
                out.println("<table cellpadding='0' cellspacing='0' border='0' class='bttoparea' height='28' width='100%'><tr><td align='left'><b>&nbsp;&nbsp;\u4e3a\u89d2\u8272[" + EString.encoderStr(request.getParameter("rolename")) + "]\u6388\u6743</b></td><td align='right' width='160px'>" + Pub.getBttn(Pub.getBttnText("save.png", "\u4fdd\u5b58"), "doRights('" + request.getParameter("rolecode") + "');", "button") + "</td></tr></table>");
                out.println("</td></tr>");
                out.println("<tr><td width='50%' height='28px'><table class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'><tr class='tr1'><th class='th1'>\u529f\u80fd\u6388\u6743</th></tr></table></td>");
                out.println("<td><table class='table1' width='100%'  border='0' cellpadding='0' cellspacing='0'><tr class='tr1'><th class='th1'>\u5df2\u9009\u6743\u9650</th></tr></table></td></tr>");
                out.println("<tr><td width='50%'>");
                this.generModRight_xlb(request, out);
                out.println("</td><td><div id='divhashrights' style='width:100%;height:100%;overflow:auto;'></div>");
                out.println("</td></tr></table>");
                out.println("<script>");
                out.println("function doRights(_strRoleCode){var strParam='rolecode='+_strRoleCode;for(var k in sys_dic_tree_tree.nodeHash){var node=sys_dic_tree_tree.nodeHash[k];if(node.checked==1||node.checked==2){var value=node.attributes['nodeCode'];if(value!='')strParam+='&modecode='+value.replace(new RegExp('&','gm'),'@'); +','+node.checked}};var vResult=getTx(strParam,'Menu?O_SYS_TYPE=right&sys_childtype=save');if(vResult=='ok')alert('\u4fdd\u5b58\u6210\u529f\uff01');else alert('\u4fdd\u5b58\u5931\u8d25!'+vResult)};var objParRightsCodes;var objRights;var strGenerRightsHtml;function do_sysTreeCheckEvent(){initRights()};function initRights(){objParRightsCodes=new Object();objRights=new Object();strGenerRightsHtml='';for(var k in sys_dic_tree_tree.nodeHash){var node=sys_dic_tree_tree.nodeHash[k];if(node.checked==1||node.checked==2){var value=node.attributes['nodeCode'];objRights[value]=node.attributes['text'];var strParCode=node.attributes['PCODE'];if(objParRightsCodes[strParCode]==null){objParRightsCodes[strParCode]=new Array();objParRightsCodes[strParCode][0]=value}else{var iLength=objParRightsCodes[strParCode].length;objParRightsCodes[strParCode][iLength]=value}}};generHashRights(objParRightsCodes,'');divhashrights.innerHTML=strGenerRightsHtml};function generHashRights(objParRightsCodes,_parCode){var arrStrChilds=objParRightsCodes[_parCode];if(arrStrChilds!=null){if(_parCode=='')strGenerRightsHtml+=\"<li class='rightsli'>\u529f\u80fd\u6743\u9650<ul class='rightsul'>\";else strGenerRightsHtml+=\"<li class='rightsli'>\"+objRights[_parCode]+\"<ul>\";var iCodes=arrStrChilds.length;for(var i=0;i<iCodes;i++){var strCode=arrStrChilds[i];generHashRights(objParRightsCodes,strCode)};strGenerRightsHtml+=\"</ul></li>\"}else{if(_parCode==\"\")strGenerRightsHtml+=\"<li class='rightsli'>\u529f\u80fd\u6743\u9650</li>\";else strGenerRightsHtml+=\"<li class='rightsli'>\"+objRights[_parCode]+\"</li>\"}}");
                out.println("initRights();</script>");
                out.println("</HTML>");
            }
            else if (strChildType.equals("top")) {
                out.print("<link href='css/table.css' rel='stylesheet' type='text/css'/>");
            }
            else if (strChildType.equals("left")) {
                Pub.importHead(out, request, "");
                this.generModRight_xlb(request, out);
            }
            else if (strChildType.equals("save")) {
                this.saveRights_xlb(request, response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void generModRight_xlb(final HttpServletRequest request, final PrintWriter out) {
        TableEx tableEx = null;
        TableEx tableChecked = null;
        try {
            final String strRoleCode = request.getParameter("rolecode");
            final String strCurRoleCode = request.getSession().getAttribute("SYS_STRROLECODE").toString();
            String strLenFunction = "";
            if (DBFactory.DBTYPE == 2) {
                strLenFunction = "len";
            }
            else {
                strLenFunction = "length";
            }
            if (strCurRoleCode.equals("")) {
                tableEx = new TableEx("SMODCODE,SMODNAME,SURL,left(SMODCODE," + strLenFunction + "(SMODCODE)-3) PCODE", "t_sys_mod", "1=1 order by ISQL");
            }
            else {
                tableEx = new TableEx("SMODCODE,SMODNAME,SURL,left(SMODCODE," + strLenFunction + "(SMODCODE)-3) PCODE", "t_sys_mod,t_roleright", "t_roleright.SRIGHTCODE=t_sys_mod.SMODCODE and SROLECODE='" + strCurRoleCode + "'  order by ISQL");
            }
            tableChecked = new TableEx("SRIGHTCODE ID,IMODSTATUS CHECKED", "t_roleright", "SROLECODE='" + strRoleCode + "'");
            out.println("<div id='sys_dic_tree' style='width:100%;height:100%;overflow:auto;'></div>");
            final YLTree ylTree = new YLTree(tableEx, "SMODCODE", "PCODE", "SMODNAME");
            ylTree.initChecked(tableChecked);
            ylTree.request = request;
            ylTree.setRootName("sys_dic_tree", "\u529f\u80fd\u6743\u9650");
            final Object objCurRoleRights = request.getSession().getAttribute("SYS_CUR_ROLE_FIELDRIGHTS");
            System.out.println("++++++++++++++" + objCurRoleRights);
            if (objCurRoleRights != null) {
                ylTree.objCurRoleRights = (Hashtable)objCurRoleRights;
            }
            ylTree.strCurRoleCode = strCurRoleCode;
            ylTree.setOnClick("reInvok(node);");
            out.print("<script>function reInvok(_objNode){initRights();}");
            out.print(ylTree.getTreeDatas());
            out.print("</script>");
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
    
    private void generModRight(final HttpServletRequest request, final PrintWriter out) {
        TableEx tableEx = null;
        TableEx tableChecked = null;
        try {
            final String strRoleCode = request.getParameter("rolecode");
            final String strCurRoleCode = request.getSession().getAttribute("SYS_STRROLECODE").toString();
            if (strCurRoleCode.equals("")) {
                tableEx = new TableEx("SMODCODE,SMODNAME,SURL,left(SMODCODE,length(SMODCODE)-3) PCODE", "t_sys_mod", "");
            }
            else {
                tableEx = new TableEx("SMODCODE,SMODNAME,SURL,left(SMODCODE,length(SMODCODE)-3) PCODE", "t_sys_mod,t_roleright", "t_roleright.SRIGHTCODE=t_sys_mod.SMODCODE and SROLECODE='" + strCurRoleCode + "'");
            }
            tableChecked = new TableEx("SRIGHTCODE ID,IMODSTATUS CHECKED", "t_roleright", "SROLECODE='" + strRoleCode + "'");
            out.println("<div id='sys_dic_tree' style='width:100%;height:100%;overflow:auto;'></div>");
            final YLTree ylTree = new YLTree(tableEx, "SMODCODE", "PCODE", "SMODNAME");
            ylTree.initChecked(tableChecked);
            ylTree.request = request;
            ylTree.setRootName("sys_dic_tree", "\u529f\u80fd\u6743\u9650");
            ylTree.setOnClick("reInvok(node);");
            out.print("<script>function reInvok(_objNode){initRights();}");
            out.print(ylTree.getTreeDatas());
            out.print("</script>");
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
    
    private void generModAndFieldRight(final HttpServletRequest request, final PrintWriter out) {
        TableEx tableEx = null;
        TableEx tableChecked = null;
        try {
            final String strRoleCode = request.getParameter("rolecode");
            tableEx = new TableEx("SMODCODE,SMODNAME,SURL,left(SMODCODE,length(SMODCODE)-3) PCODE", "t_sys_mod", "");
            tableChecked = new TableEx("SRIGHTCODE ID,IMODSTATUS CHECKED", "t_roleright", "SROLECODE='" + strRoleCode + "'");
            out.println("<div id='sys_dic_tree' style='width:100%;height:100%;overflow:auto;'></div>");
            final YLTree ylTree = new YLTree(tableEx, "SMODCODE", "PCODE", "SMODNAME");
            ylTree.initChecked(tableChecked);
            ylTree.request = request;
            ylTree.setRootName("sys_dic_tree", "\u529f\u80fd\u6743\u9650");
            ylTree.setOnClick("reInvok(node);");
            out.print("<script>function reInvok(_objNode){if(_objNode.attributes.nodeCode!=''){parent.lxmain.sys_fieldindex.href='#'+_objNode.attributes.nodeCode;parent.lxmain.sys_fieldindex.click();}}");
            out.print(ylTree.getTreeDatas());
            out.print("</script>");
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
    
    private void setDebug(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        if (request.getParameter("sys_is_desiner") == null) {
            session.setAttribute("SYS_ISDESINERMOD", (Object)"DESINER");
        }
        else {
            session.removeAttribute("SYS_ISDESINERMOD");
        }
    }
    
    private void doChangePass(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        final Object objUserId = request.getSession().getAttribute("TEMP_SYS_USER");
        
        if (objUserId == null) {
            out.println("<table width='100%' height='100%'><tr><td align='center' valign='middle'>\u6ca1\u6709\u8fd9\u4e2a\u7528\u6237\uff01</td></tr></table>");
            return;
        }
        final String strPwd = request.getParameter("pwd");
        final String strEnPwd = EncryptString.compute(strPwd);
        final DBFactory dbf = new DBFactory();
        boolean bISucces = false;
        
        try {
            dbf.sqlExe("UPDATE T_RGXX SET SYGMM='" + strEnPwd + "' WHERE SYGZW_NEW='" + objUserId + "'", true);
            bISucces = true;
        }
        catch (Exception e) {
            Debug.println("\u4fee\u6539\u5931\u8d25\uff01" + e);
        }
        finally {
            dbf.close();
        }
        
        if (bISucces) {
            out.println("<table width='100%' height='100%'><tr><td align='center' valign='middle'>\u4fee\u6539\u6210\u529f\uff01</td></tr></table>");
        }
        else {
            out.println("<table width='100%' height='100%'><tr><td align='center' valign='middle'>\u4fee\u6539\u5931\u8d25\uff01</td></tr></table>");
        }
    }
    
    private void viewPublishMsgRead(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        out.print(Pub.STR_IMPORT);
        TableEx tableExHasRead = null;
        try {
            final String strPubLishId = request.getParameter("id");
            tableExHasRead = new TableEx("S_READ_DATE,SYGMC", "t_sys_publish_read,t_rgxx", "S_ID='" + strPubLishId + "' and t_rgxx.SYGZW=t_sys_publish_read.s_user order by S_READ_DATE desc");
            final int iRecordCount = tableExHasRead.getRecordCount();
            out.println("<table cellpadding='0' cellspacing='0' class='table1' align='center' width='100%'><tr><td class='th1'>\u59d3\u540d</td><td class='th1'>\u9605\u8bfb\u65f6\u95f4</td></tr>");
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableExHasRead.getRecord(i);
                out.print("<tr><td class='td1'>");
                out.print(record.getFieldByName("SYGMC").value);
                out.print("</td><td class='td1'>");
                out.print(record.getFieldByName("S_READ_DATE").value);
                out.print("</td></tr>");
            }
            out.print("</table>");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (tableExHasRead != null) {
                tableExHasRead.close();
            }
        }
        if (tableExHasRead != null) {
            tableExHasRead.close();
        }
    }
    
    private void viewPublishMsgTz(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        out.print(Pub.STR_IMPORT);
        final DBFactory dbf = new DBFactory();
        TableEx tableExRead = null;
        TableEx tableExHasRead = null;
        TableEx tableEx = null;
        try {
            final String strPubLishId = request.getParameter("id");
            tableExRead = new TableEx("t_sys_publish_read");
            final Object objReaduser = request.getSession().getAttribute("SYS_STRCURUSER");
            if (objReaduser != null) {
                final Record recordAdd = new Record();
                recordAdd.addField(new FieldEx("S_ID", strPubLishId));
                recordAdd.addField(new FieldEx("S_USER", objReaduser));
                recordAdd.addField(new FieldEx("S_READ_DATE", EString.getCurDateHH()));
                tableExRead.addRecord(recordAdd);
                dbf.solveIgnoreTable(tableExRead, true);
            }
            tableEx = new TableEx("t_035.*,t_rgxx.SBRANCHID,t_rgxx.SYGMC", "t_035,t_rgxx", "S_ID='" + strPubLishId + "' and t_rgxx.SYGZW=t_035.fbr_1");
            tableExHasRead = new TableEx("count(*) readcount", "t_sys_publish_read", "S_ID='" + strPubLishId + "'");
            final Record record = tableEx.getRecord(0);
            final Object objTitle = record.getFieldByName("bt_6").value;
            out.println("<br><br><table width='100%'><tr><td align='center' style='font-size:16pt;'><b>" + objTitle + "</b></td></tr>");
            out.println("<tr><td align='center' style='font-family:\u9ed1\u4f53;'><br>\u90e8\u95e8\uff1a" + Dic.hash.get("t_sys_branch_" + record.getFieldByName("SBRANCHID").value) + "&nbsp;&nbsp;&nbsp;&nbsp;\u53d1\u5e03\u4eba\uff1a" + record.getFieldByName("SYGMC").value + "&nbsp;&nbsp;&nbsp;&nbsp;\u65e5\u671f\uff1a" + record.getFieldByName("fbrq_2").value + "&nbsp;&nbsp;&nbsp;&nbsp;\u9605\u8bfb\uff1a<font color='blue' style='cursor:pointer;' onclick=\"miniWin('\u9605\u8bfb\u4eba\u6b21','','Menu?O_SYS_TYPE=publishread&id=" + strPubLishId + "',350,500,'','')\">&nbsp;&nbsp;" + tableExHasRead.getRecord(0).getFieldByName("readcount").value + "&nbsp;&nbsp;</font>\u4eba</td></tr>");
            out.println("<tr><td align='left'><br><br><br>");
            out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            out.println(record.getFieldByName("nr_8").value);
            out.println("</td></tr>");
            out.println("<tr><td align='left'>");
            final EFile eFileList = new EFile();
            final Object objFile = record.getFieldByName("xgfj_7").value;
            final String strFiles = eFileList.getEFiles(String.valueOf(Dic.strCurPath) + "upload\\" + objFile, "*", objFile.toString()).toString();
            if (!strFiles.equals("")) {
                out.print("<br><b>\u76f8\u5173\u9644\u4ef6</b><br>");
                out.println(strFiles);
            }
            out.println("</td></tr>");
            out.println("</table>");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
            if (tableExRead != null) {
                tableExRead.close();
            }
            if (tableExHasRead != null) {
                tableExHasRead.close();
            }
            dbf.close();
        }
        if (tableEx != null) {
            tableEx.close();
        }
        if (tableExRead != null) {
            tableExRead.close();
        }
        if (tableExHasRead != null) {
            tableExHasRead.close();
        }
        dbf.close();
    }
    
    private void viewPublishMsg(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        out.print(Pub.STR_IMPORT);
        final DBFactory dbf = new DBFactory();
        TableEx tableExRead = null;
        TableEx tableExHasRead = null;
        TableEx tableEx = null;
        try {
            final String strPubLishId = request.getParameter("id");
            tableExRead = new TableEx("t_sys_publish_read");
            final Object objReaduser = request.getSession().getAttribute("SYS_STRCURUSER");
            if (objReaduser != null) {
                final Record recordAdd = new Record();
                recordAdd.addField(new FieldEx("S_ID", strPubLishId));
                recordAdd.addField(new FieldEx("S_USER", objReaduser));
                recordAdd.addField(new FieldEx("S_READ_DATE", EString.getCurDateHH()));
                tableExRead.addRecord(recordAdd);
                dbf.solveIgnoreTable(tableExRead, true);
            }
            tableEx = new TableEx("t_030.*,t_rgxx.SBRANCHID,t_rgxx.SYGMC", "t_030,t_rgxx", "S_ID='" + strPubLishId + "' and t_rgxx.SYGZW=t_030.fbr_3");
            tableExHasRead = new TableEx("count(*) readcount", "t_sys_publish_read", "S_ID='" + strPubLishId + "'");
            Record record = tableEx.getRecord(0);
            final String strZT = record.getFieldByName("ksrq_1").value + "&nbsp;&nbsp;\u81f3&nbsp;&nbsp;" + record.getFieldByName("jsrq_2").value + "&nbsp;&nbsp;\u4f1a\u8bae\u5b89\u6392";
            out.println("<br><br><table width='100%'><tr><td align='center' style='font-size:16pt;'><b>" + strZT + "</b></td></tr>");
            out.println("<tr><td align='center' style='font-family:\u9ed1\u4f53;'><br>\u90e8\u95e8\uff1a" + Dic.hash.get("t_sys_branch_" + record.getFieldByName("SBRANCHID").value) + "&nbsp;&nbsp;&nbsp;&nbsp;\u53d1\u5e03\u4eba\uff1a" + record.getFieldByName("SYGMC").value + "&nbsp;&nbsp;&nbsp;&nbsp;\u65e5\u671f\uff1a" + record.getFieldByName("fbrq_4").value + "&nbsp;&nbsp;&nbsp;&nbsp;\u9605\u8bfb\uff1a<font color='blue' style='cursor:pointer;' onclick=\"miniWin('\u9605\u8bfb\u4eba\u6b21','','Menu?O_SYS_TYPE=publishread&id=" + strPubLishId + "',350,500,'','')\">&nbsp;&nbsp;" + tableExHasRead.getRecord(0).getFieldByName("readcount").value + "&nbsp;&nbsp;</font>\u4eba</td></tr>");
            out.println("<tr><td align='center'><br><table cellpadding='0' cellspacing='0' class='table1' align='center' style='width:740px;table-layout:fixed;'>");
            out.println("<tr><td class='th1' style='width:80px;'>\u65e5\u671f</td><td class='th1' style='width:50px;'>\u661f\u671f</td><td class='th1' style='width:50px;'>\u65f6\u95f4</td><td class='th1' style='width:200px;'>\u5185\u5bb9</td><td class='th1' style='width:80px;'>\u4e3b\u6301\u4eba</td><td class='th1' style='width:80px;'>\u8d23\u4efb\u4eba</td><td class='th1' style='width:150px;'>\u53c2\u52a0\u5355\u4f4d\u53ca\u4eba\u5458</td><td class='th1' style='width:50px;'>\u4f1a\u8bae\u5730\u70b9</td></tr>");
            tableEx.close();
            tableEx = new TableEx("*", "t_030001", "S_ID='" + request.getParameter("id") + "' order by hykssj_1");
            final int iRecordCount = tableEx.getRecordCount();
            final String strCurDate = EString.getCurDate();
            for (int i = 0; i < iRecordCount; ++i) {
                record = tableEx.getRecord(i);
                final String[] arrStrDate = record.getFieldByName("hykssj_1").value.toString().split(" ");
                String strColor = "";
                final int iDateCompare = EString.compareDate(strCurDate, arrStrDate[0]);
                String strFontColor = "";
                String strFColor = "";
                if (iDateCompare == 0) {
                    strColor = " style='background:#8df83e;'";
                }
                else if (iDateCompare == 1) {
                    strFontColor = " style='color:green;'";
                    strFColor = "color:green;";
                }
                out.println("<tr" + strColor + "><td class='td1'" + strFontColor + ">" + arrStrDate[0] + "</td><td class='td1'" + strFontColor + ">" + EString.getWeekByDay(arrStrDate[0]) + "</td><td class='td1'" + strFontColor + ">" + arrStrDate[1] + "</td><td class='td1'  style='word-break:break-all; word-wrap:break-word;" + strFColor + "'>" + record.getFieldByName("hynr_2").value + "</td><td class='td1'" + strFontColor + ">" + record.getFieldByName("zcr_3").value + "</td><td class='td1'" + strFontColor + ">" + record.getFieldByName("zrr_4").value + "</td><td class='td1' style='word-break:break-all; word-wrap:break-word;" + strFColor + "'>" + record.getFieldByName("cjry_5").value + "</td><td class='td1'" + strFontColor + ">" + Dic.hash.get("T_003_" + record.getFieldByName("hydd_6").value) + "</td></tr>");
            }
            out.println("</table></td></tr>");
            out.println("</table>");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
            if (tableExRead != null) {
                tableExRead.close();
            }
            if (tableExHasRead != null) {
                tableExHasRead.close();
            }
            dbf.close();
        }
        if (tableEx != null) {
            tableEx.close();
        }
        if (tableExRead != null) {
            tableExRead.close();
        }
        if (tableExHasRead != null) {
            tableExHasRead.close();
        }
        dbf.close();
    }
    
    private void changePassByLogin(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        final HttpSession session = request.getSession();
        session.setAttribute("TEMP_SYS_USER", session.getAttribute("SYS_STRCURUSER"));
        out.println("<!DOCTYPE html><HTML style='height:100%;'><HEAD><TITLE>\u4fee\u6539\u5bc6\u7801</TITLE>");
        out.println("<link href=\"css/menuchild.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<link href=\"css/win.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<BODY leftMargin=0 topMargin=0 style='background:#fff;height:100%;'>");
        out.println(" <form id=\"chpwd\" name=\"chpwd\" mehtod=\"post\" onsubmit=\"if(pwd.value==''){alert('\u8bf7\u586b\u5199\u5bc6\u7801!');pwd.focus();return false;}else if(pwd.value!=repwd.value){repwd.focus();alert('\u4e24\u6b21\u5bc6\u7801\u4e0d\u4e00\u6837!');return false;}else {return true;}\" action=\"Menu\">");
        out.println("<TABLE border=0 cellPadding=0 cellSpacing=0 align='center'>");
        out.println("<th class=\"th1\" colspan=\"2\" align='center'><br>\u8bf7\u8f93\u5165\u65b0\u5bc6\u7801!<br><br></th>");
        out.println("<tr class=\"tr1\"><td class=\"td1\" style='height:35px;' align=\"right\">\u65b0\u5bc6\u7801:&nbsp;</td><td class=\"td1\" style='padding-left:10px;'><input type=\"password\" name=\"pwd\" style='height:25px;width:210px;padding-left:5px;'></td></tr>");
        out.println("<tr class=\"tr1\"><td class=\"td1\" style='height:35px;' align=\"right\">\u786e&nbsp;\u8ba4:&nbsp;</td><td class=\"td1\" style='padding-left:10px;'><input type=\"password\" name=\"repwd\" style='height:25px;width:210px;padding-left:5px;'></td></tr>");
        out.println("<th><input type='hidden' name='O_SYS_TYPE' value='dochange'></th><th class=\"th1\" align=\"center\"><br><div style='float:left;width:70px;height:10px;'></div><button class='leftbutton' type='submit'>&nbsp;&nbsp;\u786e&nbsp;\u8ba4&nbsp;&nbsp;</button><br><br></th>");
        out.println("</table></form></body>");
    }
    
    private void changePass(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><HTML style='height:100%;'><HEAD><TITLE>\u4fee\u6539\u5bc6\u7801</TITLE>");
        out.println("<link href=\"css/menuchild.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<link href=\"css/win.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<BODY leftMargin=0 topMargin=0 style='background:#fff;height:100%;'>");
        out.println(" <form id=\"chpwd\" name=\"chpwd\" mehtod=\"post\" onsubmit=\"if(pwd.value==''){alert('\u8bf7\u586b\u5199\u5bc6\u7801!');pwd.focus();return false;}else if(pwd.value!=repwd.value){repwd.focus();alert('\u4e24\u6b21\u5bc6\u7801\u4e0d\u4e00\u6837!');return false;}else {return true;}\" action=\"Menu\">");
        out.println("<TABLE border=0 cellPadding=0 cellSpacing=0 align='center'>");
        out.println("<tr><th class=\"th1\" colspan=\"2\" align='center'><br>\u4f60\u73b0\u5728\u4f7f\u7528\u7684\u662f\u539f\u59cb\u5bc6\u7801\uff0c\u8bf7\u7acb\u5373\u4fee\u6539!<br><br></th></tr>");
        out.println("<tr class=\"tr1\"><td  style='height:35px;' class=\"td1\" align=\"right\">\u65b0\u5bc6\u7801:&nbsp;</td><td class=\"td1\" style='padding-left:10px;'><input type=\"password\" name=\"pwd\" style='height:25px;width:210px;padding-left:5px;'></td></tr>");
        out.println("<tr class=\"tr1\"><td  style='height:35px;' class=\"td1\" align=\"right\">\u786e&nbsp;\u8ba4:&nbsp;</td><td class=\"td1\" style='padding-left:10px;'><input type=\"password\" name=\"repwd\" style='height:25px;width:210px;padding-left:5px;'></td></tr>");
        out.println("<th><input type='hidden' name='O_SYS_TYPE' value='dochange'></th><th class=\"th1\" align=\"center\"><br><div style='float:left;width:70px;height:10px;'></div><button class='leftbutton' type='submit'>&nbsp;&nbsp;\u786e&nbsp;\u8ba4&nbsp;&nbsp;</button><br><br></th>");
        out.println("</table></form></body>");
    }
    
    private void updatePass(final HttpServletRequest request, final HttpServletResponse response) {
        final HttpSession session = request.getSession();
        final DBFactory dbf = new DBFactory();
        try {
            final PrintWriter out = response.getWriter();
            if (session.getAttribute("SYS_CUR_LOGIN_MEMBER_TASK") != null) {
                out.print("v");
                dbf.close();
                return;
            }
            int iPass = Integer.parseInt(session.getAttribute("SYS_CUR_LOGIN_MEMBER_DO_CURPASS").toString());
            final int iTatlPass = Integer.parseInt(session.getAttribute("SYS_CUR_LOGIN_MEMBER_CURPASS").toString());
            final String strVerN = request.getParameter("n");
            if (PreloadJS.hashVer.get(strVerN) == null) {
                out.println("n");
            }
            else {
                ++iPass;
                PreloadJS.hashVer.remove(strVerN);
                if (iPass > iTatlPass) {
                    dbf.sqlExe("update t_sys_member set I_CUR_PASS=" + iPass + " where S_PHONE='" + session.getAttribute("SYS_CUR_LOGIN_MEMBER_ID") + "'", true);
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_CURPASS", (Object)iPass);
                }
                session.setAttribute("SYS_CUR_LOGIN_MEMBER_DO_CURPASS", (Object)iPass);
                out.print("v");
            }
        }
        catch (Exception ex) {
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private void memberLogin(final HttpServletRequest request, final HttpServletResponse response) {
        PrintWriter out = null;
        TableEx tableEx = null;
        try {
            out = response.getWriter();
            final String strPhone = request.getParameter("uid");
            tableEx = new TableEx("S_PHONE,S_PWD,I_CUR_PASS,S_NAME", "t_sys_member", "S_PHONE='" + strPhone + "'");
            if (tableEx.getRecordCount() <= 0) {
                out.print("\u6ca1\u6709\u8be5\u7528\u6237\uff01");
            }
            else {
                final Record record = tableEx.getRecord(0);
                if (request.getParameter("pwd").equals(record.getFieldByName("S_PWD").value.toString())) {
                    final HttpSession session = request.getSession();
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_ID", (Object)strPhone);
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_NAME", (Object)record.getFieldByName("S_NAME").value.toString());
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_CURPASS", (Object)record.getFieldByName("I_CUR_PASS").value.toString());
                    session.setAttribute("SYS_CUR_LOGIN_MEMBER_DO_CURPASS", (Object)record.getFieldByName("I_CUR_PASS").value.toString());
                    Object objRedirect = session.getAttribute("SYS_LOGIN_REDIRECT_QUERYSTR");
                    if (objRedirect == null) {
                        objRedirect = Face.strLgRedirectPage;
                    }
                    out.print("ok:" + objRedirect);
                }
                else {
                    out.print("\u5bc6\u7801\u9519\u8bef\uff01");
                }
            }
        }
        catch (Exception ex) {
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
    
    private void login(final HttpServletRequest request, final HttpServletResponse response) {
        final MisUser user = new MisUser(request);
        try {
            final PrintWriter out = response.getWriter();
            final int iResult = user.login();
            switch (iResult) {
                case 0: {
                    out.print("messageBox('\u63d0\u793a\u4fe1\u606f','\u6ca1\u6709\u8be5\u7528\u6237\uff01');");
                    break;
                }
                case 1: {
                    out.print("messageBox('\u63d0\u793a\u4fe1\u606f','\u5bc6\u7801\u9519\u8bef\uff01');");
                    break;
                }
                case 2: {
                    request.getSession().setAttribute("TEMP_SYS_USER", (Object)request.getParameter("user"));
                    out.print("winBox(\"\u4fee\u6539\u5bc6\u7801\",\"Menu?O_SYS_TYPE=change\",350,230);");
                    break;
                }
                case -1: {
                    out.print("messageBox('\u63d0\u793a\u4fe1\u606f','\u5bc6\u7801\u9519\u8bef\uff01');");
                    break;
                }
                case -2: {
                    out.print("messageBox('\u63d0\u793a\u4fe1\u606f','\u9a8c\u8bc1\u7801\u9519\u8bef\uff01');");
                    break;
                }
                case 3: {
                    WebComponent.setUserCode(response, user.strUser);
                    if (request.getParameter("jzmm").equals("true")) {
                        WebComponent.setPassword(response, user.strPassword);
                    }
                    else {
                        WebComponent.setPassword(response, "");
                    }
                    out.print("OK");
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void doDic(final HttpServletRequest request, final HttpServletResponse response) {
        final String strItem = request.getParameter("sitem");
        TableEx tableEx = null;
        try {
            final PrintWriter out = response.getWriter();
            tableEx = new TableEx("*", "T_SYSDIC", "SDICTYPEID='" + strItem + "'");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                out.println("objSelf.options[objSelf.length] = new Option('" + record.getFieldByName("SDICNAME").value + "', '" + record.getFieldByName("SDICID").value + "');");
            }
        }
        catch (Exception e) {
            Debug.println("\u751f\u6210\u5173\u8054\u6570\u636e\u9519\u8bef\uff01" + e);
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
    
    private void viewMenu(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>\u83dc\u5355\u680f</TITLE>");
        out.println("<link href=\"css/menuchild.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<BODY leftMargin=0 topMargin=0>");
        out.println("<div class=\"menu1\">");
        out.println("<div style=\"margin-left:0px;\">");
        out.println("<TABLE border=0 cellPadding=0 cellSpacing=0 width=\"100%\" class=\"menutb\">");
        out.println("  <TBODY>");
        final String MOD_CODE = request.getParameter("MOD_CODE");
        try {
            this.vecCode = (Vector)request.getSession().getAttribute("VECCODE");
            this.vecValue = (Vector)request.getSession().getAttribute("VECVALUE");
            final int iMocCount = this.vecCode.size();
            boolean bIsStart = true;
            for (int i = 0; i < iMocCount; ++i) {
                if (this.vecCode.get(i).toString().startsWith(MOD_CODE)) {
                    final Hashtable record = (Hashtable) this.vecValue.get(i);
                    final Object oModName = record.get("SMODNAME").toString().replaceAll("'", "\u2018");
                    final Object oModCode = record.get("SMODCODE");
                    final Object oModUrl = record.get("SURL");
                    String strModUrl = "";
                    if (oModUrl != null) {
                        strModUrl = oModUrl.toString();
                        if (strModUrl.indexOf("?") != -1) {
                            strModUrl = String.valueOf(strModUrl) + "&MOD_CODE=" + oModCode + "&MOD_NAME=" + oModName;
                        }
                        else {
                            strModUrl = String.valueOf(strModUrl) + "?MOD_CODE=" + oModCode + "&MOD_NAME=" + oModName;
                        }
                    }
                    if (bIsStart) {
                        out.print("<tr><td align=\"center\" class=\"menusplitstart\"></td>");
                        bIsStart = false;
                    }
                    else {
                        out.print("<tr><td align=\"center\" class=\"menusplit\"></td>");
                    }
                    out.print("<tr><td align=\"center\" onclick=\"sysmenu" + i + ".click();\" class=\"menutd\" onmouseover=\"this.className='menutdover'\" onmouseout=\"this.className='menutd'\"><img class='menuchico' src='images/menu/menuchico.gif' align='absmiddle'>&nbsp;<a id='sysmenu" + i + "' href=\"" + strModUrl + "\" target=\"framemain\">");
                    out.print(oModName);
                    out.print("</a></td></tr>");
                }
            }
        }
        catch (Exception e) {
            Debug.println("\u8bfb\u53d6\u83dc\u5355\u4fe1\u606f\u9519\u8bef\uff01" + e);
        }
        out.println(" </TBODY></TABLE>");
        out.println("</BODY></HTML>");
    }
    
    private HashMap<String, StringBuffer> getChildMenu(final String _strUser) {
        final HashMap<String, StringBuffer> hashChildMenu = new HashMap<String, StringBuffer>();
        try {
            this.dbf = new DBFactory();
            Query query = new Query("*", "T_SYS_MOD", "length(SMODCODE)=6 order by ISQL");
            if (!_strUser.equals("888")) {
                query = new Query("*", "T_SYS_MOD,t_roleright", "length(SMODCODE)=6 SMODCODE=SRIGHTCODE and SROLECODE='' order by ISQL");
            }
            final TableEx tableEx = this.dbf.query(query);
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strModCode = record.getFieldByName("SMODCODE").value.toString();
                final String strParModCode = strModCode.substring(0, 3);
                final String strUrl = record.getFieldByName("SURL").value.toString();
                StringBuffer sbChild = hashChildMenu.get(strParModCode);
                if (sbChild == null) {
                    sbChild = new StringBuffer("<tr id=\"menu" + strParModCode + "\" style=\"display:none;\"><td align=\"center\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"menuchild\">");
                }
                sbChild.append("<tr><td onclick=\"parent.contentframe.location='").append(strUrl).append("';\" class=\"menuchilditem\">+ ").append(record.getFieldByName("SMODNAME").value).append("</td></tr>");
                sbChild.append("<tr><td class='menuchildsplit'></td></tr>");
                hashChildMenu.put(strParModCode, sbChild);
            }
        }
        catch (Exception e) {
            Debug.println("\u8bfb\u53d6\u83dc\u5355\u4fe1\u606f\u9519\u8bef\uff01" + e);
            return hashChildMenu;
        }
        finally {
            if (this.dbf != null) {
                this.dbf.close();
            }
        }
        if (this.dbf != null) {
            this.dbf.close();
        }
        return hashChildMenu;
    }
    
    private void viewMain(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><link href=\"css/menu.css\" rel=\"stylesheet\" type=\"text/css\"/><script language=javascript src='js/menu1.js'></script><html> <body scroll=\"no\"> <table class=\"menucontainer\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        try {
            this.dbf = new DBFactory();
            final String strUserCode = "888";
            final HashMap<String, StringBuffer> HashChild = this.getChildMenu(strUserCode);
            Query query = new Query("*", "T_SYS_MOD", "length(SMODCODE)=3 order by ISQL");
            if (!strUserCode.equals("888")) {
                query = new Query("*", "T_SYS_MOD,t_roleright", "length(SMODCODE)=3 SMODCODE=SRIGHTCODE and SROLECODE='" + request.getSession().getAttribute("SYS_STRROLECODE") + "' order by ISQL");
            }
            final TableEx tableEx = this.dbf.query(query);
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strModCode = record.getFieldByName("SMODCODE").value.toString();
                final String strUrl = record.getFieldByName("SURL").value.toString();
                out.print("<tr> <td align=\"center\" class=\"menumain\" onclick=\"doMainMenu(this,'" + strModCode + "');\">");
                out.print(record.getFieldByName("SMODNAME").value);
                out.print("</td> </tr><tr><td class=\"mainmenusplit\"></td></tr>");
                final StringBuffer sbChild = HashChild.get(strModCode);
                if (sbChild != null) {
                    out.print(HashChild.get(strModCode));
                    out.print("</table> </td> </tr>");
                }
            }
        }
        catch (Exception e) {
            Debug.println("\u8bfb\u53d6\u83dc\u5355\u4fe1\u606f\u9519\u8bef\uff01" + e);
        }
        finally {
            if (this.dbf != null) {
                this.dbf.close();
            }
        }
        if (this.dbf != null) {
            this.dbf.close();
        }
        
        out.print("</table> </body> </html>\t");
    }
    
    private void viewFlatMan(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        final Object objFlatType = request.getParameter("nopic");
        String strNoPic = "";
        if (objFlatType != null) {
            strNoPic = "&nopic=" + objFlatType;
        }
        out.println("<HTML><HEAD><TITLE>\u83dc\u5355\u680f</TITLE>");
        out.println("<link href=\"css/menu.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<script language=JAVASCRIPT src=\"js/check.js\"></script>");
        out.println("<BODY leftMargin=0 topMargin=0 onload=\"useScroll()\">");
        out.println("<table border='0' cellpadding='0' cellspacing='0' align='center'><tr><td><a href='Menu?O_SYS_TYPE=flat" + strNoPic + "'><img src='images/menu/1-1.png' border='0'></a></td><td><a href='Menu?O_SYS_TYPE=sys" + strNoPic + "'><img src='images/menu/2-1.png' border='0'></a></td></tr></table>");
        out.println("<table class='menutb' border='0' align='center'>");
        final int iIndex = 0;
        
        try {
            this.dbf = new DBFactory();
            final String strUserCode = request.getSession().getAttribute("SYS_STRCURUSER").toString();
            Query query = new Query("*", "T_SYS_MOD", "char_length(SMODCODE)=3 order by ISQL");
            if (!strUserCode.equals("888")) {
                query = new Query("*", "T_SYS_MOD,t_roleright", "char_length(SMODCODE)=3 and SMODCODE=SRIGHTCODE and SROLECODE='" + request.getSession().getAttribute("SYS_STRROLECODE") + "' order by ISQL");
            }
            final TableEx tableEx = this.dbf.query(query);
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                if (objFlatType == null) {
                    out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img src='" + record.getFieldByName("SPIC").value + "' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='Menu?O_SYS_TYPE=flatmenu&mod_code=" + record.getFieldByName("SMODCODE").value + "' target='mainFrame'>" + record.getFieldByName("SMODNAME").value + "</a></td></tr></table></td></tr></table></td></tr>");
                }
                else {
                    out.println("<tr class='menutr'><td class='menutd' valign='middle'><a href='Menu?nopic=" + objFlatType + "&O_SYS_TYPE=flatmenu&mod_code=" + record.getFieldByName("SMODCODE").value + "' target='mainFrame'>" + record.getFieldByName("SMODNAME").value + "</a></td></tr>");
                }
            }
        }
        catch (Exception e) {
            Debug.println("\u8bfb\u53d6\u83dc\u5355\u4fe1\u606f\u9519\u8bef\uff01" + e);
        }
        finally {
            if (this.dbf != null) {
                this.dbf.close();
            }
        }
        if (this.dbf != null) {
            this.dbf.close();
        }
        
        final String STRCURUSER = User.getUserCode(request.getSession());
        out.println("</table></BODY></HTML>");
    }
    
    private void viewFlatMenu(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>\u83dc\u5355\u680f</TITLE>");
        out.println("<link href=\"css/menuchild.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<script>var strOldId='';function tabBttnClick(_aId,_aStrUrl){if(strOldId!=''){document.getElementById(\"line1\"+strOldId).className='tabline';document.getElementById(\"line2\"+strOldId).className='tabline';document.getElementById(\"line3\"+strOldId).className='tabline';}document.getElementById(\"line1\"+_aId).className='tabnocolor';document.getElementById(\"line2\"+_aId).className='tabnocolor';document.getElementById(\"line3\"+_aId).className='tabnocolor';strOldId=_aId;syschildframe.location=_aStrUrl;}function childclick(_aId){systabtr.style.display='';document.getElementById(\"line1\"+_aId).className='tabnocolor';document.getElementById(\"line2\"+_aId).className='tabnocolor';document.getElementById(\"line3\"+_aId).className='tabnocolor';strOldId=_aId;}</script>");
        out.println("<BODY scroll='no'>");
        final StringBuffer sb = new StringBuffer();
        final StringBuffer sbTab = new StringBuffer("<table cellpadding='0' cellspacing='0' border='0' width='100%'><tr>");
        sbTab.append("<td valign='bottom' width='20'><table cellpadding='0' cellspacing='0' border='0' width='100%'><tr><td class='tabline' height='2'  width='20'></td></tr></table></td>");
        sb.append("<table class='menutb' align='center'>");
        final Object objFlatType = request.getParameter("nopic");
        final String _strModCode = request.getParameter("mod_code");
        
        try {
            this.dbf = new DBFactory();
            final String strUserCode = request.getSession().getAttribute("SYS_STRCURUSER").toString();
            Query query = new Query("*", "T_SYS_MOD", "char_length(SMODCODE)>3 and SMODCODE like '" + _strModCode + "%' order by ISQL");
            if (!strUserCode.equals("888")) {
                query = new Query("*", "T_SYS_MOD,t_roleright", "char_length(SMODCODE)>3 and SMODCODE like '" + _strModCode + "%' and SMODCODE=SRIGHTCODE and SROLECODE='" + request.getSession().getAttribute("SYS_STRROLECODE") + "' order by ISQL");
            }
            final TableEx tableEx = this.dbf.query(query);
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                if (i == 0) {
                    sb.append("<tr class='menutr'>");
                }
                else if (i % 3 == 0) {
                    sb.append("</tr><tr class='menutr'>");
                }
                final Object oModUrl = record.getFieldByName("SURL").value;
                final Object oModCode = record.getFieldByName("SMODCODE").value;
                final Object oModName = record.getFieldByName("SMODNAME").value;
                String strModUrl = "";
                if (oModUrl != null) {
                    strModUrl = oModUrl.toString();
                    if (strModUrl.indexOf("?") != -1) {
                        strModUrl = String.valueOf(strModUrl) + "&MOD_CODE=" + oModCode + "&MOD_NAME=" + oModName;
                    }
                    else {
                        strModUrl = String.valueOf(strModUrl) + "?MOD_CODE=" + oModCode + "&MOD_NAME=" + oModName;
                    }
                }
                if (objFlatType == null) {
                    sb.append("<td><table><tr><td class='menutd' align='center'><a href='" + strModUrl + "' onclick=\"parent.childclick('" + i + "')\"><img src='" + record.getFieldByName("SPIC").value + "' border='0'><br>" + oModName + "</></td></tr></table></td>");
                }
                else {
                    sb.append("<td><table><tr><td class='menutd' align='center' valign='middle'><a href='" + strModUrl + "' onclick=\"parent.childclick('" + i + "')\">" + oModName + "</></td></tr></table></td>");
                }
                sbTab.append("<td style='cursor:hand;width:119' onclick=\"tabBttnClick('" + i + "','" + strModUrl + "');\">");
                sbTab.append("<table cellpadding='0' cellspacing='0' border='0' width='100%'><tr>");
                sbTab.append("<td width='7' height='30' background='images/menu/1.png' valign='bottom'>");
                sbTab.append("<table cellpadding='0' cellspacing='0' border='0' width='100%'><tr><td id='line1" + i + "' class='tabline' height='2'></td></tr></table>");
                sbTab.append("</td>");
                sbTab.append("<td background='images/menu/2.png' align='center'>");
                sbTab.append("<table cellpadding='0' cellspacing='0' border='0' width='100%' height='100%'><tr><td align='center'><a href='#'>" + oModName + "</a></td></tr><tr><td id='line2" + i + "' class='tabline' height='2'></td></tr></table>");
                sbTab.append("</td>");
                sbTab.append("<td width='7' height='30' background='images/menu/3.png' valign='bottom'>");
                sbTab.append("<table cellpadding='0' cellspacing='0' border='0' width='100%'><tr><td id='line3" + i + "'  class='tabline' height='2'></td></tr></table>");
                sbTab.append("</td>");
                sbTab.append("</tr></table>");
                sbTab.append("</td>");
                sbTab.append("<td valign='bottom' width='15'><table cellpadding='0' cellspacing='0' border='0' width='100%'><tr><td  class='tabline' height='2'  width='15'></td></tr></table></td>");
            }
        }
        catch (Exception e) {
            Debug.println("\u8bfb\u53d6\u83dc\u5355\u4fe1\u606f\u9519\u8bef\uff01" + e);
        }
        finally {
            if (this.dbf != null) {
                this.dbf.close();
            }
        }
        if (this.dbf != null) {
            this.dbf.close();
        }
        
        out.println("<table width='100%' height='100%'><tr id='systabtr' style='display:none;'><td height='35'>");
        out.println(sbTab);
        out.println("<td valign='bottom'><table cellpadding='0' cellspacing='0' border='0' width='100%'><tr><td class='tabline' height='2'  width='20'></td></tr></table></td>");
        out.println("<td valign='bottom'><table cellpadding='0' cellspacing='0' border='0' width='100%'><tr><td class='tabline' height='2'  width='100%'></td></tr></table></td>");
        out.println("</tr></table>");
        sb.append("</tr></table>");
        out.println("</td></tr><tr><td><iframe id='syschildframe' src='Menu?O_SYS_TYPE=printmenu' width='100%' height='100%' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe></td></tr></table>");
        request.getSession().setAttribute("$SYSMENU", (Object)sb);
        out.println("</BODY></HTML>");
    }
    
    private void viewPrint(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>\u83dc\u5355\u680f</TITLE>");
        out.println("<link href=\"css/menuchild.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<BODY><table width='100%' height='100%'><tr><td valign='top'>");
        out.println(request.getSession().getAttribute("$SYSMENU"));
        out.println("</td><td align='center' valign='bottom' width='10'><img src='images/frame/shu.png'></td></tr></table></BODY></HTML>");
    }
    
    private void viewSys(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        final Object objFlatType = request.getParameter("nopic");
        String strNoPic = "";
        if (objFlatType != null) {
            strNoPic = "&nopic=" + objFlatType;
        }
        out.println("<HTML><HEAD><TITLE>\u83dc\u5355\u680f</TITLE>");
        out.println("<link href=\"css/menu.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<BODY leftMargin=0 topMargin=0>");
        out.println("<table class='menutb' align='center' border='0'>");
        final String STRCURUSER = User.getUserCode(request.getSession());
        if (objFlatType == null) {
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/img3.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='ModMain' target='mainFrame'>\u6a21 \u5757 \u7ba1 \u7406</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/tu4.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='jianbiao/mytable.jsp' target='mainFrame'>\u57fa\u7840\u4fe1\u606f\u7ba1\u7406</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/img4.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='DicManager' target='mainFrame'>\u5b57 \u5178 \u7ba1 \u7406</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/sjj.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='View?SPAGECODE=1318149967328' target='mainFrame'>\u6570\u636e\u96c6\u7ba1\u7406</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/bgx.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='View?SPAGECODE=1318241811046' target='mainFrame'>\u8868\u5173\u7cfb\u8bbe\u7f6e</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/rwdd.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='sys/cspz.jsp' target='mainFrame'>\u53c2 \u6570 \u914d \u7f6e</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/ymys.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='sys/pagemanager.jsp' target='mainFrame'>\u9875 \u9762 \u5143 \u7d20</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/img5.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='component/comhome.jsp' target='mainFrame'>\u529f \u80fd \u6784 \u4ef6</a></td></tr></table></td></tr></table></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='bottom'><table border='0' width='100%'><tr><td align='right' class='pictd'><img  src='images/menu/tu3.png' border='0'></td><td align='left' valign='bottom'><table height='28' border='0'><tr><td><a href='sys/pagesmanager.jsp' target='mainFrame'>\u9875 \u9762 \u7ba1 \u7406</a></td></tr></table></td></tr></table></td></tr>");
        }
        else {
            out.println("<tr class='menutr'><td class='menutd' valign='middle'><a href='ModMain' target='mainFrame'>\u6a21 \u5757 \u7ba1 \u7406</a></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='middle'><a href='jianbiao/mytable.jsp' target='mainFrame'>\u57fa\u7840\u4fe1\u606f\u7ba1\u7406</a></td></tr>");
            out.println("<tr class='menutr'><td class='menutd' valign='middle'><a href='DicManager' target='mainFrame'>\u5b57 \u5178 \u7ba1 \u7406</a></td></tr>");
        }
        out.println("</table></BODY></HTML>");
    }
    
    private void doDebug(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>\u8bbe\u8ba1\u6a21\u5f0f</TITLE>");
        out.println("<link href=\"css/menu.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<BODY leftMargin=0 topMargin=0>");
        out.println("<table class='menutb' align='center' border='0' width='100%' height='100%'>");
        out.println("<tr><td align='center' valign='middle'>");
        final HttpSession session = request.getSession();
        final String strDesinerOp = request.getParameter("debug");
        if (strDesinerOp != null) {
            if (strDesinerOp.equals("1")) {
                session.setAttribute("SYS_ISDESINERMOD", (Object)"DESINER");
            }
            else {
                session.removeAttribute("SYS_ISDESINERMOD");
            }
        }
        final Object objIsDebug = session.getAttribute("SYS_ISDESINERMOD");
        if (objIsDebug == null) {
            out.println("<a id='href' href='Menu?O_SYS_TYPE=isdebug&debug=1'></a>");
            out.println("<button onclick='href.click();'><font color='green'>\u5f00\u542f\u8bbe\u8ba1\u6a21\u5f0f</font></button>");
        }
        else {
            out.println("<a id='href' href='Menu?O_SYS_TYPE=isdebug&debug=2'></a>");
            out.println("<button onclick='href.click();'><font color='red'>\u53d6\u6d88\u8bbe\u8ba1\u6a21\u5f0f</font></button>");
        }
        out.println("</td></tr>");
        out.println("</table>>");
    }
    
    public void destroy() {
    }
}
