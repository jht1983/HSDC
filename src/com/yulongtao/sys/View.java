package com.yulongtao.sys;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Debug;
import com.timing.impcl.MantraLog;
import com.yulongtao.db.Query;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Pub;
import com.yulongtao.util.EString;
import com.yulongtao.web.ABSElement;
import com.yulongtao.web.ABSFile;
import com.yulongtao.web.ABSGraph;
import com.yulongtao.web.ABSPage;
import com.yulongtao.web.WebInput;
import com.yulongtao.web.WebQuery;
import com.yulongtao.web.chart.CharView;
import com.yulongtao.web.chart.ChartData;
import com.yulongtao.web.chart.ChartRes;
import com.yulongtao.web.component.TabPage;
import com.yulongtao.web.component.WebElement;
import com.yulongtao.web.entrance.Face;

public class View extends HttpServlet
{
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    public static String debug_absPage;
    public SimpleDateFormat sdf;
    private String strDefault;
    private String strMutFormSize;
    private String strSelf;
    
    static {
        View.debug_absPage = null;
    }
    
    public View() {
        this.strDefault = "";
        this.strMutFormSize = "";
        this.strSelf = "";
    }
    
    public void init() throws ServletException {
    }
    
    public void debugPage() throws Exception {
        final ABSPage absPage = (ABSPage)Class.forName(View.debug_absPage).newInstance();
        absPage.initPage();
    }
    
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=GBK");
        final PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'>");
        final String strPageId = request.getParameter("SPAGECODE");
        final Object objPage = QRSC.HASHQRSC.get(strPageId);
        final Hashtable hashHQRC = (Hashtable)objPage;
        final String strType = hashHQRC.get("SPAGETYPE").toString();
        this.strMutFormSize = request.getParameter("sys_mainform_size");
        final int iType = Integer.parseInt(strType);
        if (iType < 10 || iType > 80) {
            Pub.importHead(out, request, strPageId);
        }
        if (iType == 1 && (Face.iCsType == 3 || Face.iCsType == 6 || Face.iCsType == 8)) {
            out.print("<style>body,html{background:#f4f4f8;background-color:#f4f4f8;}</style>");
        }
        if (iType != 10 && iType != 18) {
            if (iType == 4) {
                out.println("<body scroll='no' style='width:100%;height:100%;overflow:hidden;'>");
            }
            else if (request.getParameter("NO_FILTER_FIELD") == null && iType != 9) {
                out.println("<body scroll='no' style='width:100%;height:100%;overflow:hidden;'>");
            }
            else if (this.strMutFormSize != null && iType == 9) {
                out.println("<body scroll='no' style='width:100%;height:100%;overflow:hidden;'>");
            }
            else {
                out.println("<body style='width:100%;height:100%;'>");
            }
        }
        if (strPageId.equals("1462272567870") || strPageId.equals("1462273276808")) {
            out.println("<table><tr><td><img src='QRCode?key=" + request.getParameter("pcid") + "&size=150'></td></tr></table>");
        }
        this.generSrc(strPageId, request, out, hashHQRC, iType, response);
        if (iType != 10) {
            out.println("</body>");
        }
    }
    
    private String setHistory(final HttpServletRequest request, final String _strPageName) {
        String strVresult = "";
        final Hashtable hashHistory = new Hashtable();
        final HttpSession session = request.getSession();
        final Object objHistory = session.getAttribute("SYS_HISTORY_PATH");
        List listHistory = new ArrayList();
        if (objHistory != null && request.getParameter("MOD_CODE") == null) {
            listHistory = (List)objHistory;
        }
        final int iIndex = listHistory.indexOf(_strPageName);
        if (iIndex == -1) {
            listHistory.add(_strPageName);
            hashHistory.put(_strPageName, Pub.getCurUrl(request));
        }
        else {
            listHistory = listHistory.subList(0, iIndex + 1);
        }
        for (int iHistorySize = listHistory.size(), i = 0; i < iHistorySize; ++i) {
            final String strHisName = listHistory.get(i).toString();
            strVresult = String.valueOf(strVresult) + " > <label onclick=\\\"parent.mainFrame.location='" + hashHistory.get(strHisName) + "';\\\" style='cursor:hand;'>" + strHisName + "</label>";
        }
        session.setAttribute("SYS_HISTORY_PATH", (Object)listHistory);
        return strVresult;
    }
    
    public void generSrc(final String strPageId, final HttpServletRequest request, final PrintWriter out, final HttpServletResponse aResponse) {
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(strPageId);
        if (hashHQRC != null) {
            final String strType = hashHQRC.get("SPAGETYPE").toString();
            final int iType = Integer.parseInt(strType);
            this.generSrc(strPageId, request, out, hashHQRC, iType, aResponse);
        }
    }
    
    private void generJs(final String _strPageJs, final PrintWriter out) {
        final File file = new File(String.valueOf(Dic.strCurPath) + "\\js\\" + _strPageJs);
        if (file.exists()) {
            out.println("<script language='javascript' src='js/" + _strPageJs + "'></script>");
        }
    }
    
    public void generSrc(final String strPageId, final HttpServletRequest request, final PrintWriter out, final Hashtable hashHQRC, int iType, final HttpServletResponse aResponse) {
        final ABSElement absElement = new ABSElement();
        final HttpSession session = request.getSession();
        final String strImportJs = hashHQRC.get("S_JS").toString();
        boolean bIsEndJs = false;
        if (!strImportJs.equals("")) {
            if (strImportJs.startsWith("rowClick:")) {
                absElement.sysStrRowClick = strImportJs.substring(9);
            }
            else if (strImportJs.startsWith("endjs:")) {
                bIsEndJs = true;
            }
            else {
                out.println("<script type='text/javascript' src='" + absElement.getFilterData(strImportJs, request) + "'></script>");
            }
        }
        final Object objIsDebug = session.getAttribute("SYS_ISDESINERMOD");
        boolean bIsDesiner = false;
        if (objIsDebug != null) {
            bIsDesiner = true;
        }
        final Object objPageCode = hashHQRC.get("SPAGECODE");
        if (request.getParameter("sys_bed") != null) {
            absElement.bIsEditPage = true;
        }
        else {
            absElement.bIsEditPage = false;
        }
        String strReturn = request.getParameter("RETURN");
        if (strReturn != null && iType == 1) {
            iType = 4;
        }
        switch (iType) {
            case 1: {
                Pub.setforward(request);
                final String strValue = request.getParameter("INPUTQUERYFIELD");
                if (strValue != null) {
                    try {
                        this.strDefault = EString.encoderStr(strValue);
                    }
                    catch (Exception e) {
                        Debug.println("\u7f16\u7801\u9519\u8bef\uff01" + e);
                    }
                }
                else {
                    this.strDefault = "";
                }
                final String strCurModName = request.getParameter("MOD_NAME");
                try {
                    absElement.setStrIsSplit("true");
                    absElement.setQueryField(this.strDefault);
                    if (bIsDesiner) {
                        out.println("<div id='desinertip'style='display:none;height:10;background-color:yellow;position:absolute;left:0px;top:0px;z-index:1000;'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='query/queryset1.jsp?SPAGECODE=" + objPageCode + "'>\u5b9a\u4f4d\u5143\u7d20</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='sys/dataset.jsp?sconid=" + hashHQRC.get("SCONID") + "'>\u5b9a\u4f4d\u6570\u636e\u96c6</a>&nbsp;&nbsp;&nbsp;&nbsp;</div><script>desinertip.style.display='';</script>");
                    }
                    final String strIsHaveForm = hashHQRC.get("SFORM").toString();
                    if (!strIsHaveForm.equals("")) {
                        absElement.bIsQueryForm = true;
                        final String[] arrFormMsg = strIsHaveForm.split("\\$");
                        final Hashtable hashForm = (Hashtable) QRSC.HASHQRSC.get(arrFormMsg[0]);
                        out.println("<div class='sys_query_container'>");
                        if (arrFormMsg[2].equals("1")) {
                            absElement.bIsDoMerFormQuery = true;
                            out.println("<div id='sys_form_size_panel' style='height:" + arrFormMsg[1] + "px;'>");
                            out.println(absElement.generInput(hashForm, request));
                            out.println("</div>");
                            out.println(absElement.generQuery(hashHQRC, request));
                            absElement.bIsDoMerFormQuery = false;
                        }
                        else {
                            out.println(absElement.generQuery(hashHQRC, request));
                            out.println("<div id='sys_form_size_panel' style='height:" + arrFormMsg[1] + "px;'>");
                            out.println(absElement.generInput(hashForm, request));
                            out.println("</div>");
                        }
                        final String strFormRedirect = hashForm.get("SJUMPPAGE").toString();
                        if (strFormRedirect.equals("$ACTION:0:s:0:0")) {
                            Pub.setRedirect(hashForm, request);
                        }
                        absElement.bIsQueryForm = false;
                        out.println("</div>");
                    }
                    else if ((Face.iCsType == 6 || Face.iCsType == 8) && request.getParameter("NO_FILTER_FIELD") == null) {
                        out.print("<div style='width:100%;margin-top:15px;margin-left:15px;height:39px;line-height:39px;'><div style='float:left;color:#0161b3;height:39px;line-height:39px;padding-left:20px;padding-right:20px;background:#fff;border-top-left-radius: 5px;border-top-right-radius: 5px;'>" + hashHQRC.get("SPAGENAME") + "</div></div>");
                        out.println("<div style='background:#fff;margin-left:15px;'>");
                        out.println(absElement.generQuery(hashHQRC, request));
                        out.println("</div>");
                    }
                    else {
                        out.println("<div class='sys_query_container'>");
                        out.println(absElement.generQuery(hashHQRC, request));
                        out.println("</div>");
                    }
                    this.generJs("p_" + objPageCode + ".js", out);
                }
                catch (Exception ex) {
                	//do nothing
                }
                
                final String strJs = hashHQRC.get("SJSSCRIPT").toString();
                if (!strJs.equals("")) {
                    out.print("<script>");
                    try {
                        out.print(absElement.getFilterData(strJs, request));
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    out.print("</script>");
                    break;
                }
                break;
            }
            case 2: {
                try {
                    final String strIsHaveForm2 = hashHQRC.get("SFORM").toString();
                    absElement.strFlowMainForm = "";
                    if (!strIsHaveForm2.equals("")) {
                        out.println(absElement.generInputMult(hashHQRC, request, "", strIsHaveForm2));
                    }
                    else {
                        final String strIsPanel = request.getParameter("sys_flat");
                        if (strIsPanel != null) {
                            absElement.bIsQueryForm = true;
                            out.print("<style>body,html{background:#f4f4f8;background-color:#f4f4f8;}</style>");
                            out.println("<div class='sys_query_container'>");
                            out.println(absElement.generInput(hashHQRC, request));
                            out.println("</div>");
                        }
                        else {
                            out.println(absElement.generInput(hashHQRC, request));
                        }
                    }
                    if (bIsDesiner) {
                        out.println("<div id='desinertip'style='display:none;height:10;background-color:yellow;position:absolute;left:0px;top:0px;'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='input/inputset2.jsp?SPAGECODE=" + objPageCode + "'>\u5b9a\u4f4d\u5143\u7d20</a>&nbsp;&nbsp;&nbsp;&nbsp;</div><script>desinertip.style.display='';</script>");
                    }
                }
                catch (Exception ex2) {}
                Pub.setRedirect(hashHQRC, request);
                break;
            }
            case 9: {
                final Hashtable hashMainHQRC = (Hashtable) QRSC.HASHQRSC.get(hashHQRC.get("SFIELDCODE"));
                final String[] arrStrBachKey = hashHQRC.get("SFIELDSIZE").toString().split(",");
                final int iBachKeyCount = arrStrBachKey.length;
                final HashMap hashBatchKeyMsg = new HashMap();
                for (int i = 0; i < iBachKeyCount; ++i) {
                    final String[] arrKeyMsg = arrStrBachKey[i].split("=");
                    hashBatchKeyMsg.put(arrKeyMsg[0], arrKeyMsg[1]);
                }
                if (this.strMutFormSize != null) {
                    absElement.iMainFormHeight = Integer.parseInt(this.strMutFormSize);
                    absElement.iChildFormHeight = 100 - absElement.iMainFormHeight;
                }
                absElement.strFlowMainForm = strPageId;
                absElement.hashBatchKeyMsg = hashBatchKeyMsg;
                absElement.bIsHaveChildForm = true;
                absElement.arrChildFormPage = hashHQRC.get("SFIELDNAME").toString().split(",");
                absElement.arrChildOrderField = hashHQRC.get("SQLFIELD").toString().split(",");
                absElement.strIsEmtyIgnore = hashHQRC.get("SQUERYFIELD").toString();
                try {
                    out.println(absElement.generInput(hashMainHQRC, request));
                }
                catch (Exception e3) {
                    e3.printStackTrace();
                }
                Pub.setRedirect(hashHQRC, request);
                break;
            }
            case 3: {
                try {
                    absElement.bIsEditPage = true;
                    out.println(absElement.generInput(hashHQRC, request));
                    if (bIsDesiner) {
                        out.println("<div id='desinertip'style='display:none;height:10;background-color:yellow;position:absolute;left:0px;top:0px;'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='input/inputset3.jsp?SPAGECODE=" + objPageCode + "'>\u5b9a\u4f4d\u5143\u7d20</a>&nbsp;&nbsp;&nbsp;&nbsp;</div><script>desinertip.style.display='';</script>");
                    }
                }
                catch (Exception e4) {
                    Debug.println("\u751f\u6210\u4fee\u6539\u9875\u9762\u9519\u8bef\uff01" + e4);
                }
                Pub.setRedirect(hashHQRC, request);
                break;
            }
            case 5: {
                try {
                    final ABSGraph abg = new ABSGraph();
                    abg.setStrRange(hashHQRC.get("SFIELDCODE").toString());
                    abg.setStrCaliber(hashHQRC.get("SFIELDNAME").toString());
                    abg.setStrData(hashHQRC.get("SGLFIELD").toString());
                    abg.setStrPageCode(hashHQRC.get("SQUERYFIELD").toString());
                    abg.setStrGraphType(hashHQRC.get("SFIELDSIZE").toString());
                    abg.setStrGraphTitle(hashHQRC.get("SPAGENAME").toString());
                    abg.setStrGraphWidth(hashHQRC.get("SCONID").toString());
                    abg.setStrGraphHeight(hashHQRC.get("SEDITPAGE").toString());
                    out.println(abg.generGraph(hashHQRC.get("SQUERYFIELD").toString(), request));
                    final String strIsFull = request.getParameter("sys_full");
                    if (strIsFull != null) {
                        out.println("<script>chart_ylChart0.setAttribute('height', document.body.clientHeight+''); chart_ylChart0.setAttribute('width', document.body.clientWidth+'');chart_ylChart0.render('ylChart0Div');</script>");
                    }
                }
                catch (Exception e4) {
                    Debug.println("\u751f\u6210\u56fe\u5f62\u9519\u8bef\uff01" + e4);
                }
                break;
            }
            case 4: {
                final String strAction = request.getParameter("STRACTION");
                if (strAction != null) {
                    this.generActionSelSet(hashHQRC, out, strAction, request);
                    break;
                }
                if (strReturn == null) {
                    strReturn = "";
                }
                this.generSelSet(hashHQRC, out, strReturn, request);
                if (bIsDesiner) {
                    out.println("<div id='desinertip'style='display:none;height:10;background-color:yellow;position:absolute;left:0px;top:0px;z-index:1000;'>&nbsp;&nbsp;&nbsp;&nbsp;<a href='query/queryset4.jsp?SPAGECODE=" + objPageCode + "'>\u5b9a\u4f4d\u5143\u7d20</a>&nbsp;&nbsp;&nbsp;&nbsp;</div><script>desinertip.style.display='';</script>");
                }
                this.generJs("p_" + objPageCode + ".js", out);
                final String strSelJs = hashHQRC.get("SJSSCRIPT").toString();
                if (!strSelJs.equals("")) {
                    out.print("<script>");
                    try {
                        out.print(absElement.getFilterData(strSelJs, request));
                    }
                    catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    out.print("</script>");
                    break;
                }
                break;
            }
            case 10: {
                try {
                    out.println(absElement.generFrame(aResponse, hashHQRC, strPageId, request));
                }
                catch (Exception ex3) {}
                break;
            }
            case 18: {
                this.generFile(request, out, hashHQRC, strPageId);
                break;
            }
            case 88: {
                final WebElement tabPage = new TabPage(hashHQRC, request);
                out.println(tabPage.getComponent());
                break;
            }
            case 12: {
                out.println(ChartRes.strCharthHead);
                out.println(ChartRes.strChartHeadEnd);
                final CharView cV = new CharView(out);
                final ChartData chartData = new ChartData(strPageId, request);
                cV.setData(chartData);
                cV.view();
                out.println(ChartRes.strChartPageEnd);
                break;
            }
        }
        if (bIsEndJs) {
            out.println("<script type='text/javascript' src='" + absElement.getFilterData(strImportJs.substring(6), request) + "'></script>");
        }
    }
    
    private void generFile(final HttpServletRequest request, final PrintWriter out, final Hashtable hashHQRC, final String _strPageId) {
        out.println("<link href=\"" + request.getContextPath() + "/css/table.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<link href=\"" + request.getContextPath() + "/css/win.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<script language=javascript src=\"" + request.getContextPath() + "/js/evenfunction.js\"></script>");
        final ABSFile abdddf = new ABSFile();
        abdddf.strOpButton = "<button >\u521b\u5efa\u6587\u4ef6\u5939</button>  <button>\u5411\u4e0a</button>  <button  >\u4e0a\u4f20</button>";
        abdddf.strFileType = hashHQRC.get("SSQLCON").toString();
        abdddf.strPath = hashHQRC.get("STRANS").toString();
        abdddf.strFileSize = hashHQRC.get("SISLAYOUT").toString();
        final String strOp = request.getParameter("sys_file_op");
        if (strOp == null) {
            abdddf.strOp = hashHQRC.get("SMOD").toString();
        }
        else {
            abdddf.strOp = strOp;
        }
        abdddf.strFileName = "";
        abdddf.strStyle = "1";
        abdddf.request = request;
        abdddf.out = out;
        abdddf.strDoPage = "View?SPAGECODE=" + _strPageId;
        try {
            abdddf.onDoAction();
        }
        catch (Exception e) {
            Debug.println("\u6587\u4ef6\u64cd\u4f5c\u5931\u8d25\uff01" + e);
        }
    }
    
    private void generEdit(final Hashtable hashHQRC, final PrintWriter aOut, final HttpServletRequest request) throws Exception {
        String strEditCon = hashHQRC.get("SDELCON").toString();
        final String strEditTable = hashHQRC.get("SQUERYTABLE").toString();
        TableEx tableEdit = null;
        Record recordEdit = null;
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(strEditCon);
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            strEditCon = strEditCon.replaceAll("<<" + aStrParam + ">>", request.getParameter(aStrParam));
        }
        tableEdit = new TableEx("*", strEditTable, strEditCon);
        recordEdit = tableEdit.getRecord(0);
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strCurDate = this.sdf.format(new Date());
        final String strSize = request.getParameter("SSIZE");
        String strWidth = "100%";
        if (strSize != null) {
            strWidth = strSize.split(",")[0];
        }
        aOut.println("<form id=\"add\" method=\"post\"><table width=\"" + strWidth + "\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"table1\">");
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrname = hashHQRC.get("SFIELDNAME").toString().split(",");
        final String[] arrSelPage = hashHQRC.get("SQUERYFIELD").toString().split(",");
        final String[] arrReturn = hashHQRC.get("SGLFIELD").toString().split(",");
        final String[] arrType = hashHQRC.get("SDELCON").toString().split(",");
        final Object objTableOpType = hashHQRC.get("SQLFIELD");
        for (int iCount = arrcode.length, i = 0; i < iCount; ++i) {
            if (!arrcode[i].equals("")) {
                final String strType = " type=\"text\" ";
                final String strTrStyle = "";
                Object objArrField = "";
                if (!arrcode[i].startsWith("NO_")) {
                    objArrField = recordEdit.getFieldByName(arrcode[i]).value;
                }
                if (objArrField == null) {
                    objArrField = "";
                }
                aOut.println("<tr " + strTrStyle + " class=\"tr10\"><td class=\"td10\">" + arrname[i] + ":</td><td class=\"td10\"><input value=\"" + objArrField + "\"" + strType + " rule=\"bxtx\" ruleTip=\"" + arrname[i] + "\u5fc5\u987b\u586b\u5199\uff01\" name=\"" + arrcode[i] + "\"  style=\"width:120;\">");
                if (!arrSelPage[i].equals("0")) {
                    aOut.println(EString.generSelDig("\u9009\u62e9\u5bf9\u8bdd\u6846", arrSelPage[i], "add." + arrReturn[i], false));
                }
                aOut.println("</td><td class=\"td10\">&nbsp;&nbsp;*&nbsp;&nbsp;</td></tr>");
            }
        }
        final WebInput webInput = new WebInput();
        final String[] arrTable = hashHQRC.get("SQERYCON").toString().split(",");
        for (int iCount = arrTable.length, j = 0; j < iCount; ++j) {
            try {
                webInput.recordEdit = recordEdit;
                aOut.println(webInput.generInput(arrTable[j]));
            }
            catch (Exception ex) {}
        }
        aOut.println("<textarea name='NO_CONDITION' style='display:none'>" + strEditCon + "</textarea>");
        aOut.println("<textarea name='NO_TABLE' style='display:none'>" + strEditTable + "</textarea>");
        aOut.println("<table width=\"" + strWidth + "\" border=\"0\"><tr><td align=\"right\"><br><button type=\"submit\">\u786e \u5b9a</button>&nbsp;&nbsp;<button onclick=\"closeWin();\">\u53d6 \u6d88</button></td></tr></table></form>");
        final String strOptFunc = "init(add)";
        String strFocus = "";
        if (!arrcode[0].equals("")) {
            strFocus = "add." + arrcode[0] + ".focus();";
        }
        aOut.println("<script language=\"javascript\">" + strFocus + strOptFunc + ";</script>");
        final String strRedirect = request.getParameter("NO_ISOPEN");
        if (strRedirect == null) {
            request.getSession().removeAttribute("REDIREACTION");
            request.getSession().removeAttribute("REDIRECTURL");
            request.getSession().removeAttribute("REDIRECTAUIT");
            request.getSession().removeAttribute("REDIRECTMSG");
            request.getSession().setAttribute("REDIREACTION", (Object)"parent.location.reload();");
        }
    }
    
    private void generSelSet(final Hashtable hashHQRC, final PrintWriter aOut, String aStrReturn, final HttpServletRequest request) {
        String strSqlField = hashHQRC.get("SQLFIELD").toString();
        if (strSqlField.equals("")) {
            strSqlField = "*";
        }
        final ABSElement abs = new ABSElement();
        String strCondition = hashHQRC.get("SQERYCON").toString();
        try {
            strCondition = abs.getFilterData(strCondition, request);
        }
        catch (Exception ex) {}
        final Query query = new Query(strSqlField, hashHQRC.get("SQUERYTABLE").toString(), strCondition);
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrname = hashHQRC.get("SFIELDNAME").toString().split(",");
        final WebQuery webQuery = new WebQuery();
        final String strIsDic = hashHQRC.get("STRANS").toString();
        if (!strIsDic.equals("")) {
            webQuery.arrStrIsDic = strIsDic.split(",");
        }
        final String strIsSelectMutle = request.getParameter("sys_select_type");
        final String strClearFieldValue = aStrReturn;
        final String strReturnEvent = aStrReturn;
        if (strIsSelectMutle != null) {
            webQuery.bIsMutle = true;
            aStrReturn = aStrReturn.replaceAll("=", ".value+=strSplit+_objRow.childNodes[");
        }
        else {
            aStrReturn = aStrReturn.replaceAll("=", ".value=this.childNodes[");
        }
        final String[] strArrReturn = aStrReturn.split(";");
        final String[] arrClearFieldValue = strClearFieldValue.split(";");
        final int iReturnCount = strArrReturn.length;
        String strAction = "";
        String strScript = "";
        String strClearValue = "";
        for (int i = 0; i < iReturnCount; ++i) {
            if (strArrReturn[i].startsWith("AUTO:")) {
                strAction = String.valueOf(strAction) + "attab";
                strScript = String.valueOf(strScript) + strArrReturn[i].substring(5) + ",";
            }
            else {
                if (webQuery.bIsMutle) {
                    strClearValue = String.valueOf(strClearValue) + "parent.getOpenPage(gs_upl_kc)." + arrClearFieldValue[i].split("=")[0] + ".value='';";
                }
                strAction = String.valueOf(strAction) + "parent.getOpenPage(gs_upl_kc)." + strArrReturn[i] + "].innerText;";
            }
        }
        final Object objFiledSize = hashHQRC.get("SFIELDSIZE");
        if (objFiledSize != null && !objFiledSize.toString().equals("")) {
            webQuery.arrStrFileSize = objFiledSize.toString().split(",");
            double dWidth = 0.0;
            for (int j = webQuery.arrStrFileSize.length - 1; j >= 0; --j) {
                dWidth += Double.parseDouble(webQuery.arrStrFileSize[j]);
            }
            webQuery.strWidth = new StringBuilder(String.valueOf(dWidth)).toString();
        }
        webQuery.strScript = strScript;
        final String strValue = request.getParameter("INPUTQUERYFIELD");
        if (strValue != null) {
            try {
                this.strDefault = EString.encoderStr(strValue);
            }
            catch (Exception e) {
                Debug.println("\u7f16\u7801\u9519\u8bef\uff01" + e);
            }
        }
        else {
            this.strDefault = "";
        }
        final String strQueryField = hashHQRC.get("SQUERYFIELD").toString();
        webQuery.strQueryField = strQueryField;
        webQuery.strDefault = this.strDefault;
        webQuery.request = request;
        final ABSElement absElement = new ABSElement();
        try {
            webQuery.strAddPage = absElement.getFilterData(hashHQRC.get("SGLFIELD").toString(), request);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        final Object objTempValue = hashHQRC.get("SDELCON");
        webQuery.strDelParam = objTempValue.toString();
        if (webQuery.bIsMutle) {
            webQuery.strTrEvent = " onclick=\"if(event.srcElement.tagName!=='INPUT'){var objCheckInput=this.childNodes[0].childNodes[0];objCheckInput.checked=!objCheckInput.checked;}sys_setHaveSelect(this,'" + request.getParameter("gs_upl_kc") + "','" + strIsSelectMutle + "');\" ";
            if (strReturnEvent.startsWith("_event")) {
                webQuery.strClearValue = "function setFiledValue(){" + strReturnEvent.substring(6) + "('" + request.getParameter("gs_upl_kc") + "');}";
            }
            else {
                final String strMutSplit = request.getParameter("sys_mut_split");
                String strFunName = "sys_SetOpenSelValuesToForm";
                if (strMutSplit != null) {
                    strFunName = "sys_SetOpenSelValuesToMutSplitForm";
                }
                webQuery.strClearValue = "function setFiledValue(){" + strFunName + "(" + request.getParameter("sys_name_index") + ",'" + strReturnEvent + "','" + request.getParameter("gs_upl_kc") + "');}";
            }
        }
        else {
            webQuery.strTrEvent = " onclick=\"sys_SetOpenSinSelValuesToForm(this.childNodes," + request.getParameter("sys_name_index") + ",'" + strReturnEvent + "','" + request.getParameter("gs_upl_kc") + "');closeWin();\" ";
        }
        webQuery.strISCOLTOROW = hashHQRC.get("SISCOLCHANGEROW").toString();
        String strHashSplit = hashHQRC.get("SISSPLIT").toString();
        if (strHashSplit.equals("")) {
            strHashSplit = "0";
        }
        webQuery.strIsSplit = strHashSplit;
        aOut.println(webQuery.getAllCustMsg(query, arrcode, arrname));
        aOut.println("<script>sys_HaveSelect_ReView('" + request.getParameter("gs_upl_kc") + "','" + strIsSelectMutle + "');</script>");
    }
    
    private void generActionSelSet(final Hashtable hashHQRC, final PrintWriter aOut, String aStrReturn, final HttpServletRequest request) {
        String strCondition = hashHQRC.get("SQERYCON").toString();
        final ABSElement abs = new ABSElement();
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrname = hashHQRC.get("SFIELDNAME").toString().split(",");
        final WebQuery webQuery = new WebQuery();
        try {
            strCondition = abs.getFilterData(strCondition, request);
            aStrReturn = EString.encoderStr(aStrReturn);
        }
        catch (Exception ex) {}
        final Query query = new Query("*", hashHQRC.get("SQUERYTABLE").toString(), strCondition);
        aStrReturn = aStrReturn.replaceAll("\uff0b", "+");
        aStrReturn = aStrReturn.replaceAll("\uff1d", "=");
        System.out.println(aStrReturn);
        webQuery.strTrEvent = " onclick=\"" + aStrReturn + "parent.closeWin();\" ";
        aOut.println(webQuery.getAllCustMsg(query, arrcode, arrname));
    }
    
    public void desClose(final HttpServletRequest aRequest) {
        aRequest.getSession().removeAttribute("REDIREACTION");
        aRequest.getSession().removeAttribute("REDIRECTURL");
        aRequest.getSession().removeAttribute("REDIRECTAUIT");
        aRequest.getSession().removeAttribute("REDIRECTMSG");
    }
    
    public void destroy() {
    }
}
