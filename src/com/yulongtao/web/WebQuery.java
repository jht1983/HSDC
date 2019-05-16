package com.yulongtao.web;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Debug;
import com.bfkc.hzp.viewrcs;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Query;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Pub;
import com.yulongtao.sys.Dic;
import com.yulongtao.sys.QRSC;
import com.yulongtao.task.GraphData;
import com.yulongtao.util.EFile;
import com.yulongtao.util.EString;
import com.yulongtao.util.ExcelManager;
import com.yulongtao.util.MD5;
import com.yulongtao.util.db.DefaultETL;
import com.yulongtao.util.db.IETL;
import com.yulongtao.web.component.ComponentInvoke;
import com.yulongtao.web.component.WebComponent;

import net.sourceforge.osexpress.parser.ExpressLexer;

public class WebQuery
{
    private DBFactory dbf;
    private TableEx tableEx;
    private int iRecordCount;
    public String hrefname;
    public String href;
    private String sViewHref;
    public static Hashtable hashDic;
    public boolean bViewTitle;
    public String strIsSplit;
    public String strIsStructs;
    public String iStyle;
    public String strDelParam;
    public String strEditParam;
    public String strTrEvent;
    public String strClearValue;
    private String[] strArrTdSize;
    public String strScript;
    public String strOnclickFun;
    public String strSize;
    public String strAddPage;
    public boolean bIsDic;
    public String strQueryField;
    public String strSelfUrl;
    public String strDefault;
    public String strEditPage;
    public int iEditWidth;
    public int iEditHeight;
    public HttpServletRequest request;
    public int pageSize;
    public boolean bIsSplit;
    public String bIsTrColor;
    public String strElementTitle;
    public String strWidth;
    public String strUseMod;
    public String[] arrStrFileSize;
    public String[] arrStrIsDic;
    public String[] arrFieldType;
    public Hashtable hashFieldsRights;
    public boolean bIsToExcel;
    public HttpServletResponse response;
    public String strDataSetId;
    public String strISCOLTOROW;
    public HashMap<String, String[]> hashColToCols;
    public HashMap<String, String> hashColToColsDic;
    public boolean bIsMutle;
    public String sys_strTrStyle;
    public String strPageCode;
    private boolean bIsWash;
    private String strSortField;
    private String strSortMethod;
    private Hashtable hashNoFiter;
    public String strDBFunName;
    public boolean bIsInvoke;
    public ComponentInvoke comInvoke;
    public StringBuffer sbOp;
    public String sysStrRowClick;
    private String[] arrStrSubBttn;
    boolean bIsOtherCol;
    private Hashtable hashHiddenTd;
    private HashMap<String, Integer> hashSplitColsCount;
    EFile eFileList;
    public String strCurRoleCode;
    private String strSeachCon;
    public String strLineCount;
    
    static {
        WebQuery.hashDic = null;
    }
    
    public WebQuery() {
        this.hrefname = "";
        this.href = "";
        this.sViewHref = "";
        this.bViewTitle = true;
        this.strIsSplit = "0";
        this.strIsStructs = "";
        this.iStyle = "1";
        this.strDelParam = "";
        this.strEditParam = "";
        this.strTrEvent = "";
        this.strClearValue = "";
        this.strArrTdSize = null;
        this.strScript = "";
        this.strOnclickFun = "";
        this.strSize = "";
        this.strAddPage = "";
        this.bIsDic = true;
        this.strQueryField = "";
        this.strSelfUrl = "";
        this.strDefault = "";
        this.strEditPage = "";
        this.iEditWidth = 900;
        this.iEditHeight = 700;
        this.request = null;
        this.pageSize = 30;
        this.bIsSplit = true;
        this.bIsTrColor = "true";
        this.strElementTitle = "";
        this.strWidth = "100%";
        this.strUseMod = "false";
        this.arrStrFileSize = null;
        this.arrStrIsDic = null;
        this.arrFieldType = null;
        this.hashFieldsRights = new Hashtable();
        this.bIsToExcel = false;
        this.response = null;
        this.strDataSetId = "";
        this.strISCOLTOROW = "";
        this.hashColToCols = new HashMap<String, String[]>();
        this.hashColToColsDic = new HashMap<String, String>();
        this.bIsMutle = false;
        this.sys_strTrStyle = "";
        this.strPageCode = "";
        this.bIsWash = false;
        this.strSortField = "";
        this.strSortMethod = "";
        this.hashNoFiter = new Hashtable();
        this.strDBFunName = "";
        this.bIsInvoke = false;
        this.sbOp = null;
        this.sysStrRowClick = "";
        this.arrStrSubBttn = new String[] { "1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024" };
        this.bIsOtherCol = true;
        this.hashHiddenTd = new Hashtable();
        this.hashSplitColsCount = new HashMap<String, Integer>();
        this.eFileList = null;
        this.strCurRoleCode = "-1";
        this.strSeachCon = "";
        this.strLineCount = "";
    }
    
    public void setRights() {
    }
    
    public void setTdSize(final String[] aStrArr) {
        this.strArrTdSize = aStrArr;
    }
    
    public Record getRecord(final String aStrField, final String aStrTable, final String aStrWhere) {
        Record vResult = null;
        this.generDBF();
        try {
            final TableEx tableEx = this.dbf.query(new Query(aStrField, aStrTable, aStrWhere));
            final int iRecordCount = tableEx.getRecordCount();
            if (iRecordCount > 0) {
                vResult = tableEx.getRecord(0);
            }
        }
        catch (Exception ex) {
            return vResult;
        }
        finally {
            if (this.dbf != null) {
                this.dbf.close();
                this.dbf = null;
            }
        }
        if (this.dbf != null) {
            this.dbf.close();
            this.dbf = null;
        }
        return vResult;
    }
    
    private void generDBF() {
        if (this.dbf == null) {
            if (this.strDBFunName.equals("")) {
                this.dbf = new DBFactory();
            }
            else {
                this.dbf = new DBFactory(this.strDBFunName, 1);
            }
        }
    }
    
    public StringBuffer getAllCustMsg(final Query aQuery, final String[] aStrHeadCode, final String[] aTableHeads) {
        boolean bHaveGraph = false;
        boolean bIsDoTrStyle = false;
        String strDoTrStyleFiled = "";
        String strDoTrStyleValue = "";
        String strDoTrStylColor = "";
        String strDoTrStyleIco = "";
        if (!this.sys_strTrStyle.equals("")) {
            final String[] arrStrTrStyle = this.sys_strTrStyle.split("\\$");
            strDoTrStyleFiled = arrStrTrStyle[0];
            strDoTrStyleValue = arrStrTrStyle[1];
            if (strDoTrStyleValue.startsWith("session_")) {
                strDoTrStyleValue = this.request.getSession().getAttribute(strDoTrStyleValue.substring(8)).toString();
            }
            strDoTrStylColor = arrStrTrStyle[2];
            strDoTrStyleIco = arrStrTrStyle[3];
            bIsDoTrStyle = true;
        }
        String str_NO_Fiter_Condition = this.request.getParameter("NO_FITER_CONDITION");
        if (str_NO_Fiter_Condition == null) {
            str_NO_Fiter_Condition = "";
        }
        else {
            str_NO_Fiter_Condition = EString.encoderStr(str_NO_Fiter_Condition);
            final String[] arrNoFiter = str_NO_Fiter_Condition.split(" and ");
            for (int iNoFiter = arrNoFiter.length, i = 0; i < iNoFiter; ++i) {
                final String[] arrFiterFieldMsg = arrNoFiter[i].split(" in");
                this.hashNoFiter.put(arrFiterFieldMsg[0].trim(), "");
            }
        }
        final StringBuffer sbGraph = new StringBuffer();
        if (this.arrStrIsDic == null) {
            this.bIsDic = false;
        }
        IETL ietl = null;
        if (!this.strDataSetId.equals("")) {
            ietl = new DefaultETL();
            ietl.setRequest(this.request);
            this.bIsWash = ietl.init(this.strDataSetId);
        }
        final StringBuffer vResult = new StringBuffer();
        final WebInput webInput = new WebInput();
        if (!this.strScript.equals("")) {
            final String[] arrTab = this.strScript.split(",");
            final int iSize = arrTab.length;
            try {
                for (int j = 0; j < iSize; ++j) {
                    webInput.generInputScript(arrTab[j]);
                }
            }
            catch (Exception ex) {}
        }
        String strMoseMoveColor = "  onmouseover='sysMoseOverTr(this);' onmouseout='sysMoseOutTr(this);' ";
        if (this.bIsTrColor.equals("false")) {
            strMoseMoveColor = "";
        }
        boolean bIsSplit = true;
        final String[] arrIsSplit = this.strIsSplit.split(":");
        if (arrIsSplit[0].equals("0")) {
            bIsSplit = false;
        }
        else {
            this.pageSize = Integer.parseInt(arrIsSplit[1]);
            aQuery.bIsSplit = true;
        }
        String strContentTagStart = "<td class=\"td" + this.iStyle + "\" >";
        final String strContentTagGraphStart = "<td class=\"td" + this.iStyle + "graph\" >";
        String strContentTagEnd = "</td>";
        boolean bIsUseMod = false;
        String[] strArrStyle = null;
        int iStyeCount = 0;
        final String strStyleTemp = this.iStyle;
        if (this.strUseMod.equals("true")) {
            strContentTagStart = "";
            strContentTagEnd = "";
            bIsUseMod = true;
            strArrStyle = (String[]) QRSC.HASHSTYLE.get(this.iStyle);
            iStyeCount = strArrStyle.length;
        }
        final Hashtable hashQueryField = new Hashtable();
        if (this.bIsToExcel) {
            if (this.request != null) {
                String strSerchCon = this.request.getParameter("SYSSERCACHCON");
                if (strSerchCon != null) {
                    try {
                        strSerchCon = EString.encoderStr(strSerchCon);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    aQuery.addCon(strSerchCon);
                }
                final String strFiterCondition = this.request.getParameter("NO_FITER_CONDITION");
                if (strFiterCondition != null && !strFiterCondition.equals("")) {
                    aQuery.addHaving(EString.encoderStr(strFiterCondition));
                }
            }
            this.toExcel(aQuery, aStrHeadCode, aTableHeads);
        }
        if (!this.strQueryField.equals("") || !this.strAddPage.equals("") || !this.strDelParam.equals("") || (!this.strEditPage.equals("*") && !this.strEditPage.equals(""))) {
            this.genOp(aQuery, vResult, hashQueryField);
        }
        if (this.strQueryField.equals("") && this.request != null) {
            final String strSerchCon = this.request.getParameter("SYSSERCACHCON");
            if (strSerchCon != null) {
                aQuery.addCon(URLDecoder.decode(strSerchCon));
            }
        }
        final String strFiterCondition2 = this.request.getParameter("NO_FITER_CONDITION");
        if (strFiterCondition2 != null && !strFiterCondition2.equals("")) {
            aQuery.addHaving(EString.encoderStr(strFiterCondition2));
        }
        String strFiterSort = this.request.getParameter("NO_SORT_CONDITION");
        if (strFiterSort == null) {
            final Cookie cookieSortField = WebComponent.getCookieByName(this.request, String.valueOf(this.strPageCode) + "_NO_SORT_CONDITION");
            if (cookieSortField != null) {
                strFiterSort = URLDecoder.decode(cookieSortField.getValue());
            }
        }
        if (strFiterSort != null) {
            if (!strFiterSort.equals("")) {
                aQuery.addSort(EString.encoderStr(strFiterSort));
                final String[] arrStrSortMsg = strFiterSort.split(" ");
                this.strSortField = arrStrSortMsg[3];
                this.strSortMethod = arrStrSortMsg[4];
            }
            else {
                this.strSortField = "";
                this.strSortMethod = "";
            }
        }
        final int iHeadCount = aTableHeads.length;
        String strFiterParam = "";
        
        try {
            this.generDBF();
            final String strTr = "<tr onmousedown=\"$_sysMSDown(this,event);\"  onmouseup=\"$_sysMSUp(this);\"  onmousemove=\"$_sysMSMove(this,event);\"  class=\"tr" + this.iStyle + "\"  " + this.strTrEvent + strMoseMoveColor + " ondblclick=\"$_dbl(this);\"  onClick=\"sysTableRowClick(this,event);\">";
            String strTrMod = "<tr onmousedown=\"$_sysMSDown(this,event);\"  onmouseup=\"$_sysMSUp(this);\"  onmousemove=\"$_sysMSMove(this,event);\" class=\"tr" + this.iStyle + "_1\"  " + this.strTrEvent + strMoseMoveColor + " ondblclick=\"$_dbl(this);\"  onClick=\"sysTableRowClick(this,event);\">";
            final String strTrLock = "<tr onmousedown=\"$_sysMSDown(this,event);\"  onmouseup=\"$_sysMSUp(this);\"  onmousemove=\"$_sysMSMove(this,event);\"  class=\"" + strDoTrStylColor + "\"  " + this.strTrEvent + strMoseMoveColor + " ondblclick=\"$_dbl(this);\"  onClick=\"sysTableRowClick(this,event);\">";
            final String strTrHavEvent = "<tr onmousedown=\"$_sysMSDown(this,event);\"  onmouseup=\"$_sysMSUp(this);\"  onmousemove=\"$_sysMSMove(this,event);\"  class=\"tr" + this.iStyle + "\"  " + this.strTrEvent + strMoseMoveColor;
            final String strTrHavEventMod = "<tr onmousedown=\"$_sysMSDown(this,event);\"  onmouseup=\"$_sysMSUp(this);\"  onmousemove=\"$_sysMSMove(this,event);\" class=\"tr" + this.iStyle + "_1\"  " + this.strTrEvent + strMoseMoveColor;
            Query query = aQuery;
            String strSplit = "";
            if (bIsSplit && this.request != null) {
                final String strUrl = this.request.getRequestURL().toString();
                String strCount = this.request.getParameter("RECORDCOUNT");
                int iTotalPage = 0;
                if (strCount == null) {
                    System.out.println("===================================================" + this.bIsInvoke);
                    TableEx tableCount;
                    if (this.bIsInvoke) {
                        tableCount = this.comInvoke.doFun(aQuery.toString());
                    }
                    else {
                        tableCount = this.dbf.query(aQuery);
                    }
                    strCount = new StringBuilder(String.valueOf(tableCount.getRecordCount())).toString();
                }
                iTotalPage = Integer.parseInt(strCount) / this.pageSize;
                if (Integer.parseInt(strCount) % this.pageSize > 0) {
                    ++iTotalPage;
                }
                final String strTempCT = this.request.getParameter("CURRENTPAGE");
                int iCurrentPage = 1;
                if (strTempCT != null) {
                    iCurrentPage = Integer.parseInt(strTempCT);
                }
                String strCtrFunction = this.request.getParameter("OPESTR");
                if (strCtrFunction == null) {
                    strCtrFunction = "SHOUYE";
                }
                if (strCtrFunction.equals("SHOUYE")) {
                    iCurrentPage = 1;
                }
                if (strCtrFunction.equals("XIAYE")) {
                    ++iCurrentPage;
                }
                if (strCtrFunction.equals("SHANGYE")) {
                    --iCurrentPage;
                }
                if (strCtrFunction.equals("WEIYE")) {
                    iCurrentPage = iTotalPage;
                }
                if (strCtrFunction.equals("GO")) {
                    iCurrentPage = Integer.parseInt(this.request.getParameter("GOPAGE"));
                }
                if (iCurrentPage > iTotalPage) {
                    iCurrentPage = iTotalPage;
                }
                if (iCurrentPage < 1) {
                    iCurrentPage = 1;
                }
                final int iStartCount = (iCurrentPage - 1) * this.pageSize;
                System.out.println("*************************" + aQuery.conditionStr);
                if (aQuery.conditionStr.equals("")) {
                    if (query.bIsHaving) {
                        query = new Query(aQuery.fields, aQuery.tables, aQuery.conditionStr);
                        query.strHavingSplit = " limit " + iStartCount + "," + this.pageSize;
                    }
                    else {
                        query = new Query(aQuery.fields, String.valueOf(aQuery.tables) + " limit " + iStartCount + "," + this.pageSize, "");
                    }
                }
                else if (query.bIsHaving) {
                    query = new Query(aQuery.fields, aQuery.tables, aQuery.conditionStr);
                    query.strHavingSplit = " limit " + iStartCount + "," + this.pageSize;
                }
                else {
                    query = new Query(aQuery.fields, aQuery.tables, String.valueOf(aQuery.conditionStr) + " limit " + iStartCount + "," + this.pageSize);
                }
                System.out.println("*************************" + query.getSql());
                query.bIsHaving = aQuery.bIsHaving;
                query.strHaving = aQuery.strHaving;
                String strFormParam = "";
                final Enumeration paramNames = this.request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    final String paraName = paramNames.nextElement().toString();
                    if (!paraName.equals("OPESTR") && !paraName.equals("CURRENTPAGE")) {
                        if (paraName.equals("GOPAGE")) {
                            continue;
                        }
                        String paraValue = this.request.getParameter(paraName);
                        if (paraValue == null) {
                            paraValue = "";
                        }
                        strFormParam = String.valueOf(strFormParam) + "<input type='hidden' name='" + paraName + "' value=\"" + EString.encoderStr(paraValue) + "\">";
                        if (paraName.equals("NO_SORT_CONDITION") || paraName.equals("NO_FITER_CONDITION")) {
                            continue;
                        }
                        strFiterParam = String.valueOf(strFiterParam) + "&" + paraName + "=" + EString.encoderStr(paraValue);
                    }
                }
                String strSy = "<label class='fypagenum' onclick=\"document.postForm.OPESTR.value='SHOUYE';postForm.submit();\">&laquo;</label>";
                String Wy = "<label class='fypagenum' onclick=\"document.postForm.OPESTR.value='WEIYE';postForm.submit();\">&raquo;</label>";
                String Syy = "&nbsp;&nbsp;<label class='fytext' onclick=\"document.postForm.OPESTR.value='SHANGYE';postForm.submit();\"><img onmouseover='sys_spit_mouseover(this,3)' onmouseout='sys_spit_mouseout(this,3)' src='images/split/syy.png'></label>";
                String Xyy = "&nbsp;&nbsp;<label class='fytext' onclick=\"document.postForm.OPESTR.value='XIAYE';postForm.submit();\"><img onmouseover='sys_spit_mouseover(this,4)' onmouseout='sys_spit_mouseout(this,4)' src='images/split/xyy.png'></label>";
                if (iCurrentPage == 1) {
                    strSy = "<label class='fypagenum' style='cursor:default;'>&laquo;</label>";
                    Syy = "&nbsp;&nbsp;<label class='fytext1'><img src='images/split/syy1.png'></label>";
                }
                if (iCurrentPage == iTotalPage) {
                    Wy = "<label class='fypagenum' style='cursor:default;'>&raquo;</label>";
                    Xyy = "&nbsp;&nbsp;<label class='fytext1'><img src='images/split/xyy1.png'></label>";
                }
                String strSplitPage = "";
                int iStartPage = 1;
                int iEndePage = 10;
                if (iEndePage > iTotalPage) {
                    iEndePage = iTotalPage;
                }
                if (iCurrentPage > 9) {
                    iEndePage = iCurrentPage + 5;
                    if (iEndePage > iTotalPage) {
                        iEndePage = iTotalPage;
                    }
                    iStartPage = iEndePage - 10;
                    if (iStartPage < 1) {
                        iStartPage = 1;
                    }
                }
                for (int k = iStartPage; k <= iEndePage; ++k) {
                    if (k == iCurrentPage) {
                        strSplitPage = String.valueOf(strSplitPage) + "<label class='fypagenum fyactive' onclick=\"document.postForm.GOPAGE.value='" + k + "';document.postForm.OPESTR.value='GO';postForm.submit();\">" + k + "</label>";
                    }
                    else {
                        strSplitPage = String.valueOf(strSplitPage) + "<label class='fypagenum' onclick=\"document.postForm.GOPAGE.value='" + k + "';document.postForm.OPESTR.value='GO';postForm.submit();\">" + k + "</label>";
                    }
                }
                strSplit = " <div id=\"divfypanel\" class='divfypanel'><table id='fytable' class=\"fy\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" align=\"center\" ><form name=\"postForm\" style=\"margin:0px;\" method=post action=\"" + strUrl + "?CURRENTPAGE=" + iCurrentPage + "\">" + strFormParam + "<tr>" + "<td align='left'>\u663e\u793a" + iStartCount + "\uff0d" + (iStartCount + this.pageSize) + "\u6761,\u5171" + strCount + "\u6761</td><td align='right'><label id='sys_table_label_other_msg'></label>" + strSy + strSplitPage + "<input type='hidden' name='GOPAGE' value='" + iCurrentPage + "'>" + Wy + "</td>" + "<input name=\"RECORDCOUNT\" type=\"hidden\" value=\"" + strCount + "\">" + "<input name=\"OPESTR\" type=\"hidden\" value=\"\"></form>" + "</table></div>";
                strSplit = String.valueOf(strSplit) + "<script>var i_sys_Dtata_Count=" + strCount + ";</script>";
            }
            if (this.bIsInvoke) {
                this.tableEx = this.comInvoke.doFun(aQuery.toString());
            }
            else {
                this.tableEx = this.dbf.query(query);
            }
            final String strLine = "";
            if (!bIsUseMod || this.strIsStructs.equals("1")) {
                final StringBuffer sbTableHead = new StringBuffer();
                final StringBuffer sbTableTrueHead = new StringBuffer();
                if (this.bViewTitle && !bIsUseMod) {
                    this.generTableHead(aStrHeadCode, aTableHeads, hashQueryField, iHeadCount, sbTableHead, sbTableTrueHead);
                }
                final String strQuereyTable = "<table id='tbtitle' class=\"table" + this.iStyle + "\" width=\"" + this.strWidth + "\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style='table-layout:fixed;'>";
                final String strQuereyTable2 = "<table id='tb' class=\"table" + this.iStyle + "\" width=\"" + this.strWidth + "\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style='table-layout:fixed;'>";
                vResult.append("<table border='0' width='100%' border='0' cellpadding='0' cellspacing='0'><tr><td id='sys_datatd'>");
                vResult.append("<div id='titlediv' style='position:relative;overflow:hidden;'>");
                vResult.append(strQuereyTable);
                vResult.append(sbTableHead);
                vResult.append("</table></div>");
                vResult.append("<div id='tablediv' onmousewheel='sytScrollGL(event);' style='position:relative;overflow-y:hidden;overflow-x:auto;' onscroll='synTitle(this)'>");
                vResult.append("<div id='sys_query_select_div' class='sys_query_select_div' style='display:none;' onclick=\"$_sys_MS_simpleUpdate();\" onmouseup=\"$_sysMSUp(this);\"></div>");
                vResult.append(strQuereyTable2);
                vResult.append(sbTableTrueHead);
            }
            if (bIsUseMod) {
                final Object objStartTemp = QRSC.HASHSTYLE.get(String.valueOf(strStyleTemp) + "___START");
                if (objStartTemp != null) {
                    vResult.append(objStartTemp);
                }
            }
            this.iRecordCount = this.tableEx.getRecordCount();
            final int iCodeRecord = aStrHeadCode.length;
            String strFunction = "";
            final String regexClick = "<<([^<^>]*)>>";
            final Pattern paClick = Pattern.compile(regexClick);
            final Matcher maClick = paClick.matcher(this.strOnclickFun);
            final String strTempStyle = this.iStyle;
            int iTempOtherTd = 0;
            if (this.bIsOtherCol) {
                iTempOtherTd = 1;
            }
            if (this.iRecordCount <= 0) {
                if (!this.strDelParam.equals("") || this.bIsMutle) {
                    vResult.append("<tr class='tr1'><td colspan='" + (iHeadCount + 1 + iTempOtherTd) + "' class='td1' style='text-align:center;'>\u4f60\u6240\u67e5\u8be2\u7684\u8bb0\u5f55\u4e3a\u7a7a\uff01</td></tr>");
                }
                else {
                    vResult.append("<tr class='tr1'><td colspan='" + (iHeadCount + iTempOtherTd) + "' class='td1' style='text-align:center;'>\u4f60\u6240\u67e5\u8be2\u7684\u8bb0\u5f55\u4e3a\u7a7a\uff01</td></tr>");
                }
            }
            boolean bIsTree = false;
            String strTreeIdFiled = "";
            String strTreePIdFiled = "";
            String strOpenImg = "<img style='cursor:hand' src='images/tree/openfoldericon1.png' onclick='tableTreeClick(this)'>&nbsp;";
            String strCloseImg = "foldericon1.png";
            String strFileImg = "<img src='images/tree/file1.png'>&nbsp;";
            if (!this.hrefname.equals("")) {
                final String[] aTreeHref = this.hrefname.split(",");
                final int iTreeConfigLength = aTreeHref.length;
                if (iTreeConfigLength < 3) {
                    throw new Exception("\u6811\u5f62\u8868\u683c\u6570\u636e\u914d\u7f6e\u9519\u8bef\uff01");
                }
                strTreeIdFiled = aTreeHref[0];
                strTreePIdFiled = aTreeHref[1];
                this.hrefname = aTreeHref[2];
                if (iTreeConfigLength > 3) {
                    strOpenImg = "<img style='cursor:hand' src='images/tree/" + aTreeHref[3] + "' onclick='tableTreeClick(this)'>&nbsp;";
                }
                if (iTreeConfigLength > 4) {
                    strCloseImg = aTreeHref[4];
                }
                if (iTreeConfigLength > 5) {
                    strFileImg = "<img src='images/tree/" + aTreeHref[5] + "'>&nbsp;";
                }
                if (this.bIsWash) {
                    this.tableEx = new TableEx(this.tableEx, strTreeIdFiled, strTreePIdFiled, this.hrefname, ietl, this.strDataSetId);
                }
                else {
                    this.tableEx = new TableEx(this.tableEx, strTreeIdFiled, strTreePIdFiled, this.hrefname);
                }
                bIsTree = true;
            }
            if (!this.strISCOLTOROW.equals("")) {
                this.tableEx = new TableEx(this.tableEx, this.strISCOLTOROW);
                this.iRecordCount = this.tableEx.iColChangRowCount;
            }
            boolean bIsHaveRowClick = false;
            Matcher maHaveRowClick = null;
            if (!this.sysStrRowClick.equals("")) {
                bIsHaveRowClick = true;
                final String regex = "<<([^<^>]*)>>";
                final Pattern pa = Pattern.compile(regex);
                maHaveRowClick = pa.matcher(this.sysStrRowClick);
            }
            for (int k = 0; k < this.iRecordCount; ++k) {
                final Record record = this.tableEx.getRecord(k);
                final int iModValue = k % 2;
                String strSciptContent = strTr;
                if (bIsHaveRowClick) {
                    String strMsg = this.sysStrRowClick;
                    while (maHaveRowClick.find()) {
                        final String aStrParam = maHaveRowClick.group(1);
                        strMsg = strMsg.replaceAll("<<" + aStrParam + ">>", record.getFieldByName(aStrParam).value.toString());
                    }
                    strSciptContent = String.valueOf(strTrHavEvent) + " " + strMsg + ">";
                    strTrMod = String.valueOf(strTrHavEventMod) + " " + strMsg + ">";
                    maHaveRowClick.reset();
                }
                if (iModValue != 0) {
                    strSciptContent = strTrMod;
                }
                boolean bIsChangeLockStatus = false;
                if (bIsDoTrStyle) {
                    final Object objTempValue = record.getFieldByName(strDoTrStyleFiled).value;
                    if (objTempValue != null && objTempValue.toString().equals(strDoTrStyleValue)) {
                        strSciptContent = strTrLock;
                        bIsChangeLockStatus = true;
                    }
                }
                if (bIsTree) {
                    final String strParent = record.getFieldByName(strTreePIdFiled).value.toString();
                    final String strSelfId = record.getFieldByName(strTreeIdFiled).value.toString();
                    if (iModValue != 0) {
                        strSciptContent = "<tr id='tr" + strSelfId + "' class='trtree1_1' parcode='" + strParent + "' closeimg='" + strCloseImg + "'  ondblclick=\"$_dbl(this);\"  onClick=\"sysTableRowClick(this,event);\">";
                    }
                    else {
                        strSciptContent = "<tr id='tr" + strSelfId + "' class='trtree1' parcode='" + strParent + "' closeimg='" + strCloseImg + "'  ondblclick=\"$_dbl(this);\"  onClick=\"sysTableRowClick(this,event);\">";
                    }
                }
                if (!this.strScript.equals("")) {
                    final String strReAction = "";
                    String strTemp = "";
                    final int iVecSize = webInput.vecText.size();
                    final String strParent2 = "parent.add.";
                    strTemp = this.generSetValueScript(webInput, record, strTemp, iVecSize, strParent2);
                    strFunction = String.valueOf(strFunction) + "function autoTable" + k + "(){" + strTemp + "}";
                    strSciptContent = strSciptContent.replaceAll("attab", "autoTable" + k + "();");
                }
                if (k > 0) {
                    vResult.append(strLine);
                }
                if (this.strLineCount.equals("")) {
                    if (!bIsUseMod || this.strIsStructs.equals("1")) {
                        vResult.append(strSciptContent);
                    }
                }
                else {
                    final int iLineCount = Integer.parseInt(this.strLineCount);
                    if ((!bIsUseMod || this.strIsStructs.equals("1")) && k % iLineCount == 0) {
                        if (k == 0) {
                            vResult.append(strSciptContent);
                        }
                        else {
                            vResult.append("</tr>" + strSciptContent);
                        }
                    }
                }
                if (bIsUseMod) {
                    if (this.strIsStructs.equals("1")) {
                        vResult.append("<td class=\"td" + this.iStyle + "\" >");
                    }
                }
                else if (!this.strDelParam.equals("") || this.bIsMutle || (!this.strEditPage.equals("*") && !this.strEditPage.equals(""))) {
                    if (bIsChangeLockStatus) {
                        vResult.append(String.valueOf(strContentTagStart) + "<img src='images/icotr/" + strDoTrStyleIco + "'></td>");
                    }
                    else {
                        vResult.append("<td class=\"td" + this.iStyle + " tdcheckbox\" >" + "<input value='" + k + "' type='checkbox' name='syscheckbox' class='inputCheck'></td>");
                    }
                    if (!this.strDelParam.equals("")) {
                        String strMsg2 = this.strDelParam;
                        final String regex2 = "<<([^<^>]*)>>";
                        final Pattern pa2 = Pattern.compile(regex2);
                        final Matcher ma = pa2.matcher(this.strDelParam);
                        final String[] re = new String[2];
                        while (ma.find()) {
                            final String aStrParam2 = ma.group(1);
                            strMsg2 = strMsg2.replaceAll("<<" + aStrParam2 + ">>", record.getFieldByName(aStrParam2).value.toString());
                        }
                        strMsg2 = strMsg2.replaceAll("%", "\uff05");
                        vResult.append("<input  id='sysdelvalue" + k + "' type='hidden' value=\"" + strMsg2 + "\">");
                    }
                    if (!this.strEditPage.equals("*") && !this.strEditPage.equals("")) {
                        String strMsg2 = this.strEditPage;
                        final String regex2 = "<<([^<^>]*)>>";
                        final Pattern pa2 = Pattern.compile(regex2);
                        final Matcher ma = pa2.matcher(this.strEditPage);
                        final String[] re = new String[2];
                        while (ma.find()) {
                            final String aStrParam2 = ma.group(1);
                            strMsg2 = strMsg2.replaceAll("<<" + aStrParam2 + ">>", record.getFieldByName(aStrParam2).value.toString());
                        }
                        vResult.append("<input  id='syseditvalue" + k + "' type='hidden' value='" + strMsg2 + "'>");
                    }
                }
                for (int l = 0; l < iCodeRecord; ++l) {
                    if (this.hashFieldsRights.get(aStrHeadCode[l]) == null) {
                        final Integer iSplitColToColsCount = this.hashSplitColsCount.get(aStrHeadCode[l]);
                        if (iSplitColToColsCount != null) {
                            final String strValue = record.getFieldByName(aStrHeadCode[l]).value.toString();
                            final int iValueLength = strValue.length();
                            for (int m = 0; m < iSplitColToColsCount; ++m) {
                                String strSplitDicValue = "";
                                String strColorStyle = "";
                                if (m < iValueLength) {
                                    final char c = strValue.charAt(m);
                                    if (c == '0') {
                                        strColorStyle = " style='color:#f88080;'";
                                    }
                                    strSplitDicValue = this.hashColToColsDic.get(String.valueOf(aStrHeadCode[l]) + "__" + c);
                                    if (strSplitDicValue == null) {
                                        strSplitDicValue = new StringBuilder(String.valueOf(c)).toString();
                                    }
                                }
                                vResult.append("<td class='td1'" + strColorStyle + ">" + strSplitDicValue + "</td>");
                            }
                        }
                        else {
                            if (bIsUseMod && l < iStyeCount) {
                                vResult.append(strArrStyle[l]);
                            }
                            if (l == iCodeRecord - 1) {
                                this.iStyle = String.valueOf(strTempStyle) + "end";
                            }
                            else {
                                this.iStyle = strTempStyle;
                            }
                            if (aStrHeadCode[l].startsWith("$TR")) {
                                final String strMsg3 = aStrHeadCode[l].substring(3);
                                vResult.append(String.valueOf(strContentTagStart) + EString.getSX(record.getFieldByName(strMsg3).value.toString()) + strContentTagEnd);
                            }
                            else if (aStrHeadCode[l].startsWith("$AHREF")) {
                                String strMsg3 = aStrHeadCode[l].substring(6);
                                if (strMsg3.startsWith("_")) {
                                    strMsg3 = strMsg3.substring(1);
                                    if (record.getFieldByName("S_PID").value.toString().equals("")) {
                                        vResult.append(String.valueOf(strContentTagStart) + "&nbsp;" + strContentTagEnd);
                                        continue;
                                    }
                                }
                                final String regex3 = "<<([^<^>]*)>>";
                                final Pattern pa3 = Pattern.compile(regex3);
                                final Matcher ma2 = pa3.matcher(strMsg3);
                                final String[] re2 = new String[2];
                                while (ma2.find()) {
                                    final String aStrParam3 = ma2.group(1);
                                    Object objDbValue = null;
                                    try {
                                        objDbValue = record.getFieldByName(aStrParam3).value;
                                    }
                                    catch (Exception ex2) {}
                                    if (objDbValue == null) {
                                        objDbValue = this.request.getParameter(aStrParam3);
                                    }
                                    if (objDbValue == null) {
                                        objDbValue = this.request.getSession().getAttribute(aStrParam3);
                                    }
                                    if (objDbValue == null) {
                                        objDbValue = "";
                                        Debug.println("\u8bf7\u68c0\u67e5\u662f\u5426\u4f20\u5165\u4e86\u53c2\u6570[" + aStrParam3 + "]!");
                                    }
                                    strMsg3 = strMsg3.replaceAll("<<" + aStrParam3 + ">>", objDbValue.toString());
                                }
                                strMsg3 = strMsg3.replaceAll("#&", "%23&");
                                vResult.append(String.valueOf(strContentTagStart) + strMsg3 + strContentTagEnd);
                            }
                            else if (aStrHeadCode[l].startsWith("$ALINK")) {
                                String strMsg3 = aStrHeadCode[l].substring(6);
                                final String regex3 = "<<([^<^>]*)>>";
                                final Pattern pa3 = Pattern.compile(regex3);
                                final Matcher ma2 = pa3.matcher(strMsg3);
                                final String[] re2 = new String[2];
                                boolean bIsDic = false;
                                String strDicTypeId = "";
                                String strTrueParam = "";
                                while (ma2.find()) {
                                    String aStrParam4 = ma2.group(1);
                                    bIsDic = false;
                                    if (aStrParam4.startsWith("dic.")) {
                                        final String[] arr = aStrParam4.split("\\.");
                                        strTrueParam = aStrParam4;
                                        aStrParam4 = arr[2];
                                        strDicTypeId = arr[1];
                                        bIsDic = true;
                                    }
                                    Object objValue = null;
                                    objValue = record.getFieldByName(aStrParam4).value;
                                    if (objValue == null) {
                                        objValue = this.request.getSession().getAttribute(aStrParam4);
                                    }
                                    if (bIsDic) {
                                        objValue = "<font color='" + Dic.hashColor.get(String.valueOf(strDicTypeId) + "_" + objValue) + "'>" + Dic.hash.get(String.valueOf(strDicTypeId) + "_" + objValue) + "</font>";
                                        strMsg3 = strMsg3.replaceAll("<<" + strTrueParam + ">>", objValue.toString());
                                    }
                                    else {
                                        strMsg3 = strMsg3.replaceAll("<<" + aStrParam4 + ">>", objValue.toString());
                                    }
                                }
                                final String[] arrLinkValue = strMsg3.split("\\$");
                                Object objTempValue2 = null;
                                try {
                                    objTempValue2 = record.getFieldByName(arrLinkValue[0]).value;
                                }
                                catch (Exception ex3) {}
                                if (objTempValue2 == null) {
                                    objTempValue2 = arrLinkValue[0];
                                }
                                final String[] arrStrLink = arrLinkValue[2].split("`");
                                if (objTempValue2.toString().equals(arrLinkValue[1])) {
                                    vResult.append(String.valueOf(strContentTagStart) + arrStrLink[0] + strContentTagEnd);
                                }
                                else {
                                    final String strDicTypeCode = this.arrStrIsDic[l];
                                    Object objCode = null;
                                    if (!strDicTypeCode.equals("0")) {
                                        objCode = Dic.hash.get(String.valueOf(strDicTypeCode) + "_" + arrStrLink[1]);
                                    }
                                    if (objCode == null) {
                                        objCode = arrStrLink[1];
                                    }
                                    vResult.append(String.valueOf(strContentTagStart) + objCode + strContentTagEnd);
                                }
                            }
                            else if (aStrHeadCode[l].startsWith("$GRAPH")) {
                                String strMsg3 = aStrHeadCode[l].substring(6);
                                final String regex3 = "<<([^<^>]*)>>";
                                final Pattern pa3 = Pattern.compile(regex3);
                                final Matcher ma2 = pa3.matcher(strMsg3);
                                final String[] re2 = new String[2];
                                while (ma2.find()) {
                                    final String aStrParam3 = ma2.group(1);
                                    Object objFieldTempValue = record.getFieldByName(aStrParam3).value;
                                    if (objFieldTempValue == null) {
                                        objFieldTempValue = "";
                                    }
                                    strMsg3 = strMsg3.replaceAll("<<" + aStrParam3 + ">>", objFieldTempValue.toString());
                                }
                                final String[] arrGraphValue = strMsg3.split("\\$");
                                vResult.append(String.valueOf(strContentTagGraphStart) + "<div id='chart" + arrGraphValue[0] + "' align='center'></div>" + strContentTagEnd);
                                bHaveGraph = true;
                                sbGraph.append(GraphData.generLed("HLinearGauge.swf", "chart" + arrGraphValue[0], "100%", 55, arrGraphValue[1]));
                            }
                            else if (aStrHeadCode[l].startsWith("$SUB_")) {
                                final String strMsg3 = aStrHeadCode[l].substring(5);
                                final String[] arrstrMsg = strMsg3.split("\\$");
                                final Object objBeiSubValue = record.getFieldByName(arrstrMsg[0]).value;
                                Object objSubValue = record.getFieldByName(arrstrMsg[1]).value;
                                if (objSubValue == null) {
                                    objSubValue = "0";
                                }
                                String strSubBttn = "";
                                if (objBeiSubValue != null) {
                                    final int iSubValue = Integer.parseInt(objBeiSubValue.toString()) - Integer.parseInt(objSubValue.toString());
                                    if (iSubValue != 0) {
                                        strSubBttn = this.generSubBttn(iSubValue);
                                    }
                                }
                                vResult.append(String.valueOf(strContentTagStart) + strSubBttn + strContentTagEnd);
                            }
                            else if (aStrHeadCode[l].startsWith("$UPDATE_")) {
                                final String strMsg3 = aStrHeadCode[l].substring(8);
                                final String[] arrstrMsg = strMsg3.split("\\$");
                                final int iMsgCount = arrstrMsg.length;
                                Object objUPValue = record.getFieldByName(arrstrMsg[1]).value;
                                if (objUPValue == null) {
                                    objUPValue = "";
                                }
                                vResult.append(strContentTagStart).append("<input type='text' style='width:95%;' parchangevalue='").append(objUPValue).append("' value='").append(objUPValue).append("' onchange=\"sys_do_queryUpdate(this,").append(k + 1);
                                if (iMsgCount > 2) {
                                    for (int p = 2; p < iMsgCount; ++p) {
                                        vResult.append(",'").append(record.getFieldByName(arrstrMsg[p]).value).append("'");
                                    }
                                }
                                vResult.append(")\">").append(strContentTagEnd);
                            }
                            else if (aStrHeadCode[l].startsWith("$IFLINK")) {
                                String strMsg3 = aStrHeadCode[l].substring(7);
                                final String regex3 = "<<([^<^>]*)>>";
                                final Pattern pa3 = Pattern.compile(regex3);
                                final Matcher ma2 = pa3.matcher(strMsg3);
                                boolean bIsDic2 = false;
                                String strDicTypeId2 = "";
                                String strTrueParam2 = "";
                                while (ma2.find()) {
                                    String aStrParam5 = ma2.group(1);
                                    bIsDic2 = false;
                                    if (aStrParam5.startsWith("dic.")) {
                                        final String[] arr2 = aStrParam5.split("\\.");
                                        strTrueParam2 = aStrParam5;
                                        aStrParam5 = arr2[2];
                                        strDicTypeId2 = arr2[1];
                                        bIsDic2 = true;
                                    }
                                    Object objValue2 = null;
                                    objValue2 = record.getFieldByName(aStrParam5).value;
                                    if (objValue2 == null) {
                                        objValue2 = this.request.getSession().getAttribute(aStrParam5);
                                    }
                                    if (bIsDic2) {
                                        objValue2 = "<font color='" + Dic.hashColor.get(String.valueOf(strDicTypeId2) + "_" + objValue2) + "'>" + Dic.hash.get(String.valueOf(strDicTypeId2) + "_" + objValue2) + "</font>";
                                        strMsg3 = strMsg3.replaceAll("<<" + strTrueParam2 + ">>", objValue2.toString());
                                    }
                                    else {
                                        strMsg3 = strMsg3.replaceAll("<<" + aStrParam5 + ">>", objValue2.toString());
                                    }
                                }
                                final String[] arrLinkValue2 = strMsg3.split("\\$");
                                final FieldEx fieldEx = record.getFieldByName(arrLinkValue2[0]);
                                Object objTempValue2 = "";
                                if (fieldEx == null) {
                                    objTempValue2 = arrLinkValue2[0];
                                }
                                else {
                                    objTempValue2 = fieldEx.value;
                                    if (objTempValue2 == null) {
                                        objTempValue2 = arrLinkValue2[0];
                                    }
                                }
                                String strOpt = arrLinkValue2[1];
                                final String[] strArrValue = arrLinkValue2[2].split("`");
                                if (strOpt.startsWith("IN:")) {
                                    strOpt = strOpt.substring(3);
                                    final String[] strArrCon = strOpt.split("`");
                                    final int iConCount = strArrCon.length;
                                    boolean bISMZ = false;
                                    for (int k2 = 0; k2 < iConCount; ++k2) {
                                        if (strArrCon[k2].equals(objTempValue2.toString())) {
                                            bISMZ = true;
                                            break;
                                        }
                                    }
                                    if (bISMZ) {
                                        vResult.append(String.valueOf(strContentTagStart) + strArrValue[0] + strContentTagEnd);
                                    }
                                    else {
                                        vResult.append(String.valueOf(strContentTagStart) + strArrValue[1] + strContentTagEnd);
                                    }
                                }
                                else if (strOpt.startsWith("NOTIN:")) {
                                    strOpt = strOpt.substring(6);
                                    final String[] strArrCon = strOpt.split("`");
                                    final int iConCount = strArrCon.length;
                                    boolean bISMZ = false;
                                    for (int k2 = 0; k2 < iConCount; ++k2) {
                                        if (strArrCon[k2].equals(objTempValue2.toString())) {
                                            bISMZ = true;
                                            break;
                                        }
                                    }
                                    if (bISMZ) {
                                        vResult.append(String.valueOf(strContentTagStart) + strArrValue[1] + strContentTagEnd);
                                    }
                                    else {
                                        vResult.append(String.valueOf(strContentTagStart) + strArrValue[0] + strContentTagEnd);
                                    }
                                }
                                else if (strOpt.startsWith("CASE:")) {
                                    strOpt = strOpt.substring(5);
                                    final String[] strArrCon = strOpt.split("`");
                                    final int iConCount = strArrCon.length;
                                    boolean bISMZ = false;
                                    for (int k2 = 0; k2 < iConCount; ++k2) {
                                        if (strArrCon[k2].equals(objTempValue2.toString())) {
                                            vResult.append(String.valueOf(strContentTagStart) + strArrValue[k2] + strContentTagEnd);
                                            bISMZ = true;
                                            break;
                                        }
                                    }
                                    if (!bISMZ) {
                                        vResult.append(String.valueOf(strContentTagStart) + strArrValue[strArrValue.length - 1] + strContentTagEnd);
                                    }
                                }
                            }
                            else if (aStrHeadCode[l].equals("$EDIT")) {
                                String strMsg3 = this.strEditPage;
                                final String regex3 = "<<([^<^>]*)>>";
                                final Pattern pa3 = Pattern.compile(regex3);
                                final Matcher ma2 = pa3.matcher(this.strEditPage);
                                final String[] re2 = new String[2];
                                while (ma2.find()) {
                                    final String aStrParam3 = ma2.group(1);
                                    strMsg3 = strMsg3.replaceAll("<<" + aStrParam3 + ">>", record.getFieldByName(aStrParam3).value.toString());
                                }
                                vResult.append(String.valueOf(strContentTagStart) + "<img src=\"" + Dic.strCurRoot + "/images/eve/xg.png\" alt=\"\u70b9\u51fb\u4fee\u6539\u6b64\u6761\u4fe1\u606f\uff01\" border=\"0\" onclick=\"miniWin('\u4fee\u6539\u4fe1\u606f','','" + strMsg3 + "'," + this.iEditWidth + "," + this.iEditHeight + ",'','');\">" + strContentTagEnd);
                            }
                            else if (aStrHeadCode[l].equals("$DEL")) {
                                String strMsg3 = this.strDelParam;
                                final String regex3 = "<<([^<^>]*)>>";
                                final Pattern pa3 = Pattern.compile(regex3);
                                final Matcher ma2 = pa3.matcher(this.strDelParam);
                                final String[] re2 = new String[2];
                                while (ma2.find()) {
                                    final String aStrParam3 = ma2.group(1);
                                    strMsg3 = strMsg3.replaceAll("<<" + aStrParam3 + ">>", record.getFieldByName(aStrParam3).value.toString());
                                }
                                strMsg3 = strMsg3.replaceAll("%", "\uff05");
                                if (this.hashHiddenTd.get(new StringBuilder(String.valueOf(l)).toString()) != null) {
                                    vResult.append("<td class=\"td" + this.iStyle + "_none\">" + "<a href=\"javascript:del('" + strMsg3 + "')\"><img src=\"" + Dic.strCurRoot + "/images/eve/gb.png\" alt=\"\u70b9\u51fb\u5220\u9664\u6b64\u6761\u4fe1\u606f\uff01\" border=\"0\" /></a>" + strContentTagEnd);
                                }
                                else {
                                    vResult.append(String.valueOf(strContentTagStart) + "<a href=\"javascript:del('" + strMsg3 + "')\"><img src=\"" + Dic.strCurRoot + "/images/eve/gb.png\" alt=\"\u70b9\u51fb\u5220\u9664\u6b64\u6761\u4fe1\u606f\uff01\" border=\"0\" /></a>" + strContentTagEnd);
                                }
                                vResult.append("<input  id='sysdelvalue" + k + "' type='hidden' value='" + strMsg3 + "'>");
                            }
                            else if (aStrHeadCode[l].equals("$INDEX")) {
                                vResult.append(String.valueOf(strContentTagStart) + (k + 1) + strContentTagEnd);
                            }
                            else if (this.hrefname.equals(aStrHeadCode[l])) {
                                this.sViewHref = this.href;
                                String strTeeeImg = strOpenImg;
                                if (record.bIsLeaf) {
                                    strTeeeImg = strFileImg;
                                }
                                Object objName = record.getFieldByName(aStrHeadCode[l]).value;
                                if (this.arrStrIsDic != null) {
                                    final String strDicTypeCode2 = this.arrStrIsDic[l];
                                    if (!strDicTypeCode2.equals("0")) {
                                        final Object objDicTemName = Dic.hash.get(String.valueOf(strDicTypeCode2) + "_" + objName);
                                        if (objDicTemName != null) {
                                            objName = objDicTemName;
                                        }
                                    }
                                }
                                vResult.append("<td class='td1' style='padding:0em " + record.iLevel * 2 + "em;'>" + strTeeeImg + objName + strContentTagEnd);
                            }
                            else {
                                String strClickTemp = "";
                                if (!this.strOnclickFun.equals("")) {
                                    strClickTemp = this.strOnclickFun;
                                    maClick.reset();
                                    while (maClick.find()) {
                                        final String aStrParam6 = maClick.group(1);
                                        strClickTemp = strClickTemp.replaceAll("<<" + aStrParam6 + ">>", record.getFieldByName(aStrParam6).value.toString());
                                    }
                                    strClickTemp = " onclick=\"" + strClickTemp + "\" ";
                                }
                                Object objValue3 = record.getFieldByName(aStrHeadCode[l]).value;
                                if (objValue3 == null) {
                                    objValue3 = "";
                                }
                                if (this.bIsWash) {
                                    objValue3 = ietl.getEtlValue(this.strDataSetId, aStrHeadCode[l], objValue3, record);
                                }
                                if (this.bIsDic) {
                                    Object objCode2 = null;
                                    Object objCodeColor = null;
                                    final String strDicTypeCode3 = this.arrStrIsDic[l];
                                    if (!strDicTypeCode3.equals("0")) {
                                        final String[] arrObjValue = objValue3.toString().split(",");
                                        final int iValueCount = arrObjValue.length;
                                        String strObjCodeDicValue = "";
                                        String strDicSplit = "";
                                        for (int k3 = 0; k3 < iValueCount; ++k3) {
                                            final Object objDicByTypeValue = Dic.hash.get(String.valueOf(strDicTypeCode3) + "_" + arrObjValue[k3]);
                                            if (objDicByTypeValue != null) {
                                                strObjCodeDicValue = String.valueOf(strObjCodeDicValue) + strDicSplit + objDicByTypeValue;
                                                strDicSplit = ",";
                                            }
                                        }
                                        if (!strObjCodeDicValue.equals("")) {
                                            objCode2 = strObjCodeDicValue;
                                        }
                                        objCodeColor = Dic.hashColor.get(String.valueOf(strDicTypeCode3) + "_" + objValue3.toString());
                                    }
                                    if (objCode2 != null) {
                                        if (objCodeColor != null) {
                                            objCodeColor = "bgcolor='" + objCodeColor + "'";
                                        }
                                        if (bIsUseMod) {
                                            vResult.append(objCode2);
                                        }
                                        else {
                                            vResult.append("<td " + objCodeColor + " " + strClickTemp + "class=\"td" + this.iStyle + "\">" + objCode2 + "</td>");
                                        }
                                    }
                                    else {
                                        objValue3 = this.getSplitValue(objValue3, l);
                                        objValue3 = this.getTypeValue(objValue3, l);
                                        if (bIsUseMod) {
                                            vResult.append(objValue3);
                                        }
                                        else if (this.hashHiddenTd.get(new StringBuilder(String.valueOf(l)).toString()) != null) {
                                            vResult.append("<td  " + strClickTemp + "class=\"td" + this.iStyle + "_none\">" + objValue3 + "</td>");
                                        }
                                        else {
                                            vResult.append("<td  " + strClickTemp + "class=\"td" + this.iStyle + "\">" + objValue3 + "</td>");
                                        }
                                    }
                                }
                                else {
                                    objValue3 = this.getSplitValue(objValue3, l);
                                    objValue3 = this.getTypeValue(objValue3, l);
                                    if (bIsUseMod) {
                                        vResult.append(objValue3);
                                    }
                                    else if (this.hashHiddenTd.get(new StringBuilder(String.valueOf(l)).toString()) != null) {
                                        vResult.append("<td " + strClickTemp + "class=\"td" + this.iStyle + "_none\">" + objValue3 + "</td>");
                                    }
                                    else {
                                        vResult.append("<td  " + strClickTemp + "class=\"td" + this.iStyle + "\">" + objValue3 + "</td>");
                                    }
                                }
                            }
                        }
                    }
                }
                if (bIsUseMod) {
                    if (iStyeCount > iCodeRecord) {
                        vResult.append(strArrStyle[iCodeRecord]);
                    }
                    if (!bIsUseMod || this.strIsStructs.equals("1")) {
                        vResult.append("</td>");
                    }
                }
                else if (this.bIsOtherCol) {
                    vResult.append("<td class='td1'>&nbsp;</td>");
                }
                if (this.strLineCount.equals("") && (!bIsUseMod || this.strIsStructs.equals("1"))) {
                    vResult.append("</tr>");
                }
            }
            if (!bIsUseMod || this.strIsStructs.equals("1")) {
                vResult.append("</table></div>");
                vResult.append("</td><td width='18px' height='100%' id='vbartd'><div id='vbar' style='width:18px;height:100%;DISPLAY: block; OVERFLOW-Y: auto;'   onscroll='sys_synVScroll();' ><div style='height:100%;width:1px;'></div></div></td></tr></table>");
            }
            if (bIsUseMod) {
                this.strIsStructs.equals("1");
            }
            if (bIsUseMod) {
                final Object objEndTemp = QRSC.HASHSTYLE.get(String.valueOf(strStyleTemp) + "___END");
                if (objEndTemp != null) {
                    vResult.append(objEndTemp);
                }
            }
            vResult.append(strSplit);
            vResult.append("<script language=\"javascript\">");
            vResult.append(strFunction);
            vResult.append("</script>");
        }
        catch (Exception e2) {
            Debug.println("\u751f\u6210\u67e5\u8be2\u9519\u8bef!" + e2 + "==>" + aQuery.toString());
        }
        finally {
            this.dbf.close();
            this.dbf = null;
        }
        
        if (bHaveGraph) {
            vResult.append("<script>");
            vResult.append(sbGraph);
            vResult.append("</script>");
        }
        if (this.bIsMutle) {
            vResult.append("<script>" + this.strClearValue + "</script>");
        }
        String strForm_Fiter_Url = strFiterParam;
        String strPageView = "View";
        if (!strForm_Fiter_Url.equals("")) {
            if (strForm_Fiter_Url.indexOf("sys_pg") != -1) {
                strPageView = "ViewFrame";
            }
            strForm_Fiter_Url = String.valueOf(strPageView) + "?" + strForm_Fiter_Url.substring(1);
        }
        final int iRecordCountIndex = strForm_Fiter_Url.indexOf("RECORDCOUNT=");
        if (iRecordCountIndex != -1) {
            final String strTempRecord = strForm_Fiter_Url.substring(iRecordCountIndex);
            final int iEndIndex = strTempRecord.indexOf("&");
            if (iEndIndex != -1) {
                strForm_Fiter_Url = String.valueOf(strForm_Fiter_Url.substring(0, iRecordCountIndex)) + strTempRecord.substring(iEndIndex);
            }
            else {
                strForm_Fiter_Url = strForm_Fiter_Url.substring(0, iRecordCountIndex);
            }
        }
        String strMenuPageCode = this.request.getParameter("SPAGECODE");
        if (strMenuPageCode == null) {
            strMenuPageCode = this.strPageCode;
        }
        vResult.append("<div id='query_list_pop_menu' class='file_list_pop_menu' style='display:none;border:1px solid #b0aeae;'><form id='sys_form_fiter_con' action='" + strForm_Fiter_Url + "' method='post'><input type='hidden' name='NO_SORT_CONDITION' value=''><textarea name='NO_FITER_CONDITION' id='NO_FITER_CONDITION' style='display:none;'>" + str_NO_Fiter_Condition + "</textarea>").append(Pub.getPopMenu(new String[] { "gjcx.png" }, new String[] { Pub.getPopWin("\u9ad8\u7ea7\u67e5\u8be2", "view.do?id=600" + strMenuPageCode + this.getPageParameter(this.request), "1000,450").toString() }, new String[] { "\u9ad8\u7ea7\u67e5\u8be2" })).append("<iframe id='sys_frame_singer_col' name='sys_frame_singer_col' src='' width='100%' height='300px'  frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe><table cellpadding='0' cellspacing='0' border='0' class='bttoparea' width='200px;'><tr><td align='center'><button onclick='sys_singer_col_fiter_query();'>\u786e \u5b9a</button></td><td align='center'><button onclick='sys_singer_col_fiter_query_Clear();'>\u6e05\u9664\u6761\u4ef6</button></td></tr></table></form></div>");
        vResult.append("<script>var sys_StrFiterParam=\"" + strFiterParam + "\";</script><form id='sys_form_fiter_con_view_col' action='' method='post' target='sys_frame_singer_col'><textarea name='sys_frame_view_condition' id='sys_frame_view_condition' style='display:none;'>" + str_NO_Fiter_Condition + "</textarea></form>");
        vResult.append("<div intinpuspanivigr='true' id='sys_int_input_div' class='sys_int_input_div' style='display:none;'><table intinpuspanivigr='true' cellpadding='5'><tr><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)' intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,1)'>1</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,2)'>2</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,3)'>3</span></td></tr><tr><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,4)'>4</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,5)'>5</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,6)'>6</span></td></tr><tr><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,7)'>7</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,8)'>8</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,9)'>9</span></td></tr><tr><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out(this)' onclick='sys_gener_IntInputClick(this,0)'>0</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span_clear' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out_Clear(this)' onclick='sys_gener_IntInputClick_Clear(this)'>\u6e05\u9664</span></td><td  intinpuspanivigr='true' class='sys_int_input_div_td'><span onmousedown='sys_gener_IntInputDown(this)'   intinpuspanivigr='true' class='sys_int_input_div_span_ok' onmouseover='sys_gener_IntInput_Over(this)' onmouseout='sys_gener_IntInput_Out_Ok(this)' onclick='sys_gener_IntInputClick_Ok(this)'>\u786e\u5b9a</span></td></tr></table></div>");
        return vResult;
    }
    
    public String getPageParameter(final HttpServletRequest _request) {
        String strFiterParam = "";
        final Enumeration paramNames = _request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            final String paraName = paramNames.nextElement().toString();
            if (!paraName.equals("OPESTR") && !paraName.equals("CURRENTPAGE") && !paraName.equals("GOPAGE")) {
                if (paraName.equals("gs_upl_kc")) {
                    continue;
                }
                String paraValue = _request.getParameter(paraName);
                if (paraValue == null) {
                    paraValue = "";
                }
                if (paraName.equals("NO_SORT_CONDITION") || paraName.equals("NO_FITER_CONDITION")) {
                    continue;
                }
                strFiterParam = String.valueOf(strFiterParam) + "&" + paraName + "=" + EString.encoderStr(paraValue);
            }
        }
        return strFiterParam;
    }
    
    private String generSubBttn(final int _iSubValue) {
        String vResult = "";
        if (_iSubValue > 0) {
            vResult = String.valueOf(vResult) + "<input type='text' intinpuspanivigr='true' value='" + _iSubValue + "' onclick='sys_gener_IntInput(this," + _iSubValue + ")' class='sys_int_input'>";
        }
        return vResult;
    }
    
    private void generTableHead(final String[] aStrHeadCode, final String[] aTableHeads, final Hashtable hashQueryField, final int iHeadCount, final StringBuffer sbTableHead, final StringBuffer sbTableTrueHead) {
        sbTableHead.append("<tr id=\"headrownew\">");
        sbTableTrueHead.append("<tr id=\"headrow\">");
        final String strTh = "<th onmousemove='sys_columStatus(event,this);' class=\"th" + this.iStyle + "\"";
        final String strThQuery = "<th onmousemove='sys_columStatus(event,this);' class=\"thquery" + this.iStyle + "\"";
        final Hashtable tbMsg = this.tableEx.getTableMsg();
        this.bIsOtherCol = true;
        if (!this.strDelParam.equals("") || this.bIsMutle || (!this.strEditPage.equals("*") && !this.strEditPage.equals(""))) {
            sbTableHead.append(String.valueOf(strTh) + " width='20px'><input type='checkbox' onclick=\"selAllCheckBox(this,'syscheckbox')\"  class='inputCheck'></th>");
            sbTableTrueHead.append("<th  class='th1_none' width='20px'></th>");
        }
        final Cookie cookeiFieldSize = WebComponent.getCookieByName(this.request, String.valueOf(this.strPageCode) + "_NO_PAGE_COL_WIDTH");
        final Hashtable<String, String> hashColSizeMap = new Hashtable<String, String>();
        if (cookeiFieldSize != null) {
            final String[] arrStrCookieFieldSize = URLDecoder.decode(cookeiFieldSize.getValue()).split(":");
            for (int iCookFieldSizeLength = arrStrCookieFieldSize.length, i = 0; i < iCookFieldSizeLength; ++i) {
                final String[] arrSizeMsg = arrStrCookieFieldSize[i].split("=");
                hashColSizeMap.put(arrSizeMsg[0], arrSizeMsg[1]);
            }
        }
        this.hashSplitColsCount = new HashMap<String, Integer>();
        for (int j = 0; j < iHeadCount; ++j) {
            if (this.hashFieldsRights.get(aStrHeadCode[j]) == null) {
                final String[] arrSplitToCols = this.hashColToCols.get(aStrHeadCode[j]);
                if (arrSplitToCols != null) {
                    final int iSplitColsCount = arrSplitToCols.length;
                    this.hashSplitColsCount.put(aStrHeadCode[j], iSplitColsCount);
                    String strSplitColsStyle = ">";
                    if (this.arrStrFileSize != null) {
                        strSplitColsStyle = " style='width:" + this.arrStrFileSize[j] + "px;'>";
                    }
                    for (int k = 0; k < iSplitColsCount; ++k) {
                        sbTableHead.append(strTh).append(strSplitColsStyle).append(arrSplitToCols[k]).append("</th>");
                        sbTableTrueHead.append("<th class='th1_none' ").append(strSplitColsStyle).append("</th>");
                    }
                }
                else {
                    if (this.hashNoFiter.get(aStrHeadCode[j]) != null) {
                        sbTableHead.append(strThQuery);
                    }
                    else {
                        sbTableHead.append(strTh);
                    }
                    if (!aStrHeadCode[j].startsWith("$")) {
                        sbTableHead.append(" oncontextmenu=\"return sys_rClick_queryhead(event,this,'" + aStrHeadCode[j] + "'," + j + ");\" ");
                    }
                    sbTableTrueHead.append("<th class='th1_none' ");
                    String strViewThSize = "";
                    if (this.arrStrFileSize != null) {
                        String strTdFieldSize = this.arrStrFileSize[j];
                        final String strCookieFieldSize = hashColSizeMap.get(aStrHeadCode[j]);
                        if (strCookieFieldSize != null) {
                            strTdFieldSize = strCookieFieldSize;
                        }
                        int iFieldSize = Integer.parseInt(strTdFieldSize) - 15;
                        if (iFieldSize < 0) {
                            iFieldSize = 0;
                        }
                        strViewThSize = new StringBuilder(String.valueOf(iFieldSize)).toString();
                        if (!strTdFieldSize.equals("1")) {
                            if (strTdFieldSize.equals("0")) {
                                sbTableHead.append(" style='padding:0em 0.0em;border:0px;overflow-x:hidden;width:" + strTdFieldSize + "px;'");
                                sbTableTrueHead.append(" style='padding:0em 0.0em;border:0px;overflow-x:hidden;width:" + strTdFieldSize + "px;'");
                                this.hashHiddenTd.put(new StringBuilder(String.valueOf(j)).toString(), "");
                            }
                            else {
                                sbTableHead.append(" style='width:" + strTdFieldSize + "px;'");
                                sbTableTrueHead.append(" style='width:" + strTdFieldSize + "px;'");
                            }
                        }
                        else {
                            strViewThSize = "100%";
                            this.bIsOtherCol = false;
                        }
                    }
                    String strSortView = "&nbsp;";
                    if (this.strSortField.equals(aStrHeadCode[j])) {
                        if (this.strSortMethod.equals("DESC")) {
                            sbTableHead.append(" sort='1' ");
                            strSortView = "&nbsp;\u25bc";
                        }
                        else {
                            sbTableHead.append(" sort='0' ");
                            strSortView = "&nbsp;\u25b2";
                        }
                    }
                    sbTableHead.append(" onmousedown=\"sys_columDown(event,this,'");
                    if (aStrHeadCode[j].startsWith("$")) {
                        sbTableHead.append("NOOP");
                    }
                    else {
                        sbTableHead.append(aStrHeadCode[j]);
                    }
                    sbTableHead.append("');\">");
                    sbTableTrueHead.append(">");
                    final String strHeadName = aTableHeads[j];
                    sbTableHead.append("<div style='width:" + strViewThSize + "px;overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;'>");
                    sbTableHead.append(strHeadName);
                    sbTableHead.append("<span author='yulongtao'>" + strSortView + "</span>");
                    sbTableHead.append("</div>");
                    if (hashQueryField.get(aStrHeadCode[j]) != null) {
                        final FieldEx fdEx = (FieldEx) tbMsg.get(aStrHeadCode[j]);
                        sbTableHead.append("&nbsp;&nbsp;<img onclick=\"winQuery('/dialog/querycon.jsp?name=" + aTableHeads[j] + "&code=" + aStrHeadCode[j] + "&iType=" + fdEx.fieldType + "')\" style='cursor:hand' height='15' width='15' src='images/sx.gif' align='absmiddle'>");
                    }
                    sbTableHead.append("</th>");
                    sbTableTrueHead.append("</th>");
                }
            }
        }
        if (this.bIsOtherCol) {
            sbTableHead.append("<th  class='th1'><div style='overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;'>&nbsp;</div></th>");
            sbTableTrueHead.append("<th  class='th1_none'>&nbsp;</th>");
        }
        sbTableHead.append("</tr>");
        sbTableTrueHead.append("</tr>");
    }
    
    private Object getTypeValue(Object objValue, final int j) {
        Object vResult = objValue;
        if (objValue == null) {
            objValue = "";
        }
        if (objValue.toString().equals("")) {
            return objValue;
        }
        if (this.arrFieldType != null) {
            if (this.arrFieldType[j].equals("PIC")) {
                vResult = "<img src='upload/" + objValue + "' class='query_pic'>";
            }
            else if (this.arrFieldType[j].equals("DONLOAD")) {
                vResult = "<a href='DownLoad?filepathname=" + objValue + "'>@\u4e0b\u8f7d</a>";
            }
            else if (this.arrFieldType[j].equals("BAR")) {
                vResult = "<img src='ViewBarCode?code=" + objValue + "&barType=CODE39&checkCharacter=n&checkCharacterInText=n'>";
            }
            else if (this.arrFieldType[j].equals("FILE")) {
                if (this.eFileList == null) {
                    this.eFileList = new EFile();
                }
                vResult = this.eFileList.getEFiles(String.valueOf(Dic.strCurPath) + "upload\\" + objValue, "*", objValue.toString());
            }
        }
        return vResult;
    }
    
    private String generSetValueScript(final WebInput webInput, final Record record, String strTemp, int iVecSize, final String strParent) throws Exception {
        for (int k = 0; k < iVecSize; ++k) {
            final String strFieldItem = webInput.vecText.get(k).toString();
            final String[] arrFieldItem = strFieldItem.split("\\$");
            strTemp = String.valueOf(strTemp) + strParent + strFieldItem + ".value='" + record.getFieldByName(arrFieldItem[1]).value + "';";
        }
        iVecSize = webInput.vecSel.size();
        for (int k = 0; k < iVecSize; ++k) {
            final String strFieldItem = webInput.vecSel.get(k).toString();
            final String[] arrFieldItem = strFieldItem.split("\\$");
            strTemp = String.valueOf(strTemp) + strParent + strFieldItem + ".value='" + record.getFieldByName(arrFieldItem[1]).value + "';";
        }
        iVecSize = webInput.vecRad.size();
        for (int k = 0; k < iVecSize; ++k) {
            final String strFieldItem = webInput.vecRad.get(k).toString();
            final String[] arrFieldItem = strFieldItem.split("\\$");
            strTemp = String.valueOf(strTemp) + "for(var i=0;i<" + strParent + strFieldItem + ".length;i++)if(" + strParent + strFieldItem + "[i].value=='" + record.getFieldByName(arrFieldItem[1]).value + "')" + strParent + strFieldItem + "[i].checked=true;";
        }
        return strTemp;
    }
    
    private void genOp(final Query aQuery, final StringBuffer vResult, final Hashtable hashQueryField) {
        String strViewBttn = this.request.getParameter("NO_VIEWBTTN");
        if (strViewBttn == null) {
            strViewBttn = "";
        }
        final StringBuffer sbSerch = new StringBuffer();
        vResult.append("<form id='sysqueryform' action='" + this.strSelfUrl + "' method='post'>");
        int iIsBr = 0;
        if (!this.strQueryField.equals("")) {
            iIsBr = this.generSerch(aQuery, sbSerch);
        }
        vResult.append("<table id='sysbttoparea' cellpadding='0' cellspacing='0' border='0' class='bttoparea2'>");
        vResult.append("<tr>");
        String strMenuPageCode = this.request.getParameter("SPAGECODE");
        if (strMenuPageCode == null) {
            strMenuPageCode = this.strPageCode;
        }
        if (iIsBr >= 1) {
            vResult.append("<td align='left' class='tdquerycontainer'>");
            vResult.append("<table id='quicksearch' class='tbquerybg'><tr class='trquerybg'>");
            vResult.append(sbSerch);
            vResult.append("<td align='left' colspan='100' style='padding-left:20px;'>");
            vResult.append(Pub.getBttn(Pub.getBttnText("query.gif", "\u67e5\u8be2"), "sysqueryform.submit();", "leftbutton blue"));
            vResult.append("</td>");
            vResult.append("</tr></table></td></tr><tr>");
        }
        
        vResult.append("<td align='right' class='tdbttnscontainer'>");
        if (strViewBttn.indexOf("1") == -1) {
            vResult.append("<div onclick=\"" + Pub.getPopWin("\u9ad8\u7ea7\u67e5\u8be2", "view.do?id=602" + strMenuPageCode + this.getPageParameter(this.request), "1050,620").toString()).append("\" class='sys_query_bttn'><img src='images/eve/wquery.png?v=" + Pub.iVer + "'>\u9ad8\u7ea7\u67e5\u8be2</div>");
        }
        
        if (strViewBttn.equals("0")) {
            vResult.append("</td></tr></table></form>");
            return;
        }
        
        final String[] arrStrSize = this.strSize.split(":");
        final int iSizeLength = arrStrSize.length;
        final Pub pub = new Pub();
        boolean bIsHaveUpload = false;
        if (this.request.getParameter("NO_SYS_OPTYPE") != null) {
            this.strAddPage = "";
        }
        String strAddSize = "";
        final StringBuffer sbOtherBttn = new StringBuffer();
        if (!this.strAddPage.equals("")) {
            final String[] arrStrAddPage = this.strAddPage.split(",");
            for (int iButtonCount = arrStrAddPage.length, i = 0; i < iButtonCount; ++i) {
                this.strSize = "";
                if (i < iSizeLength) {
                    this.strSize = arrStrSize[i];
                }
                final String strBttnCode = arrStrAddPage[i].split("\\$")[0];
                if (this.hashFieldsRights.get(MD5.getMessageDigest(strBttnCode.getBytes())) == null) {
                    String strBttnClass = "button green";
                    String strBttnIco = "add.png";
                    String strSysAddPages = arrStrAddPage[i];
                    if (strSysAddPages.startsWith("SYSUPLOAD:_import_")) {
                        if (strViewBttn.indexOf("4") == -1) {
                            final String[] strUploadBttnMsg = strSysAddPages.substring(18).split(":");
                            sbOtherBttn.append(pub.getUploadBttn_FreePlan(strUploadBttnMsg[1], this.request.getParameter("v1"), this.request.getParameter("v2"), Integer.parseInt(strUploadBttnMsg[0])));
                            bIsHaveUpload = true;
                        }
                    }
                    else if (strSysAddPages.startsWith("SYSUPLOAD:importLock")) {
                        sbOtherBttn.append(pub.getUploadBttn_FreePlan("\u9501\u5b9a\u6784\u4ef6", this.request.getParameter("pid"), "", 11));
                        bIsHaveUpload = true;
                    }
                    else {
                        if (strSysAddPages.startsWith("SYSCLASS:")) {
                            final String[] arrStrRedirectPage = strSysAddPages.split("\\|");
                            final String[] arrBttnStyle = arrStrRedirectPage[0].split(":");
                            final int iStyleLength = arrBttnStyle.length;
                            strSysAddPages = arrStrRedirectPage[1];
                            if (iStyleLength > 1 && !arrBttnStyle[1].trim().equals("")) {
                                strBttnClass = arrBttnStyle[1].trim();
                            }
                            if (iStyleLength > 2 && !arrBttnStyle[2].trim().equals("")) {
                                strBttnIco = arrBttnStyle[2].trim();
                            }
                        }
                        final String[] arrPg = strSysAddPages.split("\\$");
                        String strRedirectPage = arrPg[0];
                        boolean bIsEvent = false;
                        if (strRedirectPage.startsWith("SYSCLICK:")) {
                            strRedirectPage = strRedirectPage.substring(9);
                            bIsEvent = true;
                        }
                        else if (!strRedirectPage.startsWith("/")) {
                            if (arrPg[0].startsWith("TOEXCEL")) {
                                if (strViewBttn.indexOf("5") != -1) {
                                    continue;
                                }
                                strRedirectPage = String.valueOf(Dic.strCurRoot) + "/DownLoad?SPAGECODE=" + arrPg[0].substring(7) + this.strSeachCon;
                                final String strFiterCondition = this.request.getParameter("NO_FITER_CONDITION");
                                if (strFiterCondition != null && !strFiterCondition.equals("")) {
                                    strRedirectPage = String.valueOf(strRedirectPage) + "&NO_FITER_CONDITION=" + URLEncoder.encode(EString.encoderStr(strFiterCondition));
                                }
                                strBttnIco = "toexcel.png";
                            }
                            else {
                                strRedirectPage = String.valueOf(Dic.strCurRoot) + "/View?SPAGECODE=" + arrPg[0] + this.strSeachCon;
                            }
                        }
                        else {
                            strRedirectPage = String.valueOf(Dic.strCurRoot) + strRedirectPage;
                        }
                        if (arrPg.length > 2) {
                            if (arrPg[2].equals("A")) {
                                sbOtherBttn.append(Pub.getLinkBttn(Pub.getBttnText(strBttnIco, arrPg[1]), strRedirectPage, strBttnClass));
                            }
                            else {
                                sbOtherBttn.append(Pub.getBttn(Pub.getBttnText("printset.png", "\u6253\u5370\u8bbe\u7f6e"), "strPrintPage='" + strRedirectPage + "';printSetUp();", strBttnClass));
                                sbOtherBttn.append(Pub.getBttn(Pub.getBttnText("print.png", "\u6253 \u5370"), "strPrintPage='" + strRedirectPage + "';prn1_preview();", strBttnClass));
                                sbOtherBttn.append("<object id=\"LODOP\" classid=\"clsid:2105C259-1E0C-4534-8141-A753534CB4CA\" width=0 height=0><embed id=\"LODOP_EM\" type=\"application/x-print-lodop\" width=0 height=0 pluginspage=\"install_lodop.exe\"></embed></object>");
                            }
                        }
                        else if (bIsEvent) {
                            sbOtherBttn.append(Pub.getBttn(Pub.getBttnText(strBttnIco, arrPg[1]), strRedirectPage, strBttnClass));
                        }
                        else {
                            if (strViewBttn.indexOf("2") == -1) {
                                vResult.append(Pub.getBttn(Pub.getBttnText(strBttnIco, arrPg[1]), "miniWin('" + arrPg[1] + "','','" + strRedirectPage + "&SSIZE=" + this.strSize + "'," + this.strSize + ",'','');", strBttnClass));
                            }
                            strAddSize = this.strSize;
                        }
                    }
                }
            }
        }
        if (this.request != null && this.request.getParameter("sys_op_edit_bttn") == null) {
            final String strEditSize = this.request.getParameter("sys_edit_size");
            if (strEditSize != null) {
                strAddSize = strEditSize;
            }
            if (this.hashFieldsRights.get("$EDIT") == null && !this.strEditPage.equals("*") && !this.strEditPage.equals("")) {
                if (!strAddSize.equals("")) {
                    vResult.append(Pub.getBttn(Pub.getBttnText("edit.png", "\u4fee\u6539"), "sys_EditForm(" + strAddSize + ");", "button blue"));
                    if (strViewBttn.indexOf("3") == -1) {
                        vResult.append(Pub.getBttn(Pub.getBttnText("copy.png", "\u590d\u5236"), "sys_CopyForm(" + strAddSize + ");", "button blue"));
                    }
                }
                else {
                    vResult.append(Pub.getBttn(Pub.getBttnText("edit.png", "\u4fee\u6539"), "sys_EditForm(" + this.iEditWidth + "," + this.iEditHeight + ");", "button blue"));
                    if (strViewBttn.indexOf("3") == -1) {
                        vResult.append(Pub.getBttn(Pub.getBttnText("copy.png", "\u590d\u5236"), "sys_CopyForm(" + this.iEditWidth + "," + this.iEditHeight + ");", "button blue"));
                    }
                }
            }
        }
        if (this.request != null && this.request.getParameter("sys_op_del_bttn") == null && this.hashFieldsRights.get("$DEL") == null && !this.strDelParam.equals("")) {
            vResult.append(Pub.getBttn(Pub.getBttnText("del.png", "\u5220\u9664"), "delSysAll();", "button red"));
        }
        vResult.append(sbOtherBttn);
        vResult.append("</td><td id='sys_table_op_right_msg'>");
        if (this.sbOp != null) {
            vResult.append(this.sbOp);
        }
        vResult.append("</td></tr></table></form>");
        if (bIsHaveUpload) {
            vResult.append(pub.getUploadScript());
        }
    }
    
    /**
     * 页面渲染和搜索逻辑.
     * 
     * @param aQuery
     * @param sbSerch
     * @return
     */
    private int generSerch(final Query aQuery, final StringBuffer sbSerch) {
        final String[] arrQF = this.strQueryField.split(",");
        final int iQFCount = arrQF.length;
        String strTempCon = "";
        final viewrcs viewParams = new viewrcs();
        final Calendar calendarDate = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Dic dic = new Dic();
        final int iBrCount = Integer.parseInt(arrQF[0]);
        
        //显示行数
        int iCols = (iQFCount - 1) / iBrCount;
        if ((iQFCount - 1) % iBrCount != 0) {
            iCols = (iQFCount - 1) / (iBrCount - 1);
        }
        
        String strScript = "";
        for (int i = 1; i < iQFCount; ++i) {
            if ("<<BR>>".equals(arrQF[i])) {
            	sbSerch.append("</tr><tr class='trquerybg'>");
            	continue;
            }
            
            final String[] arrParamNames = arrQF[i].split(":");
            String strFieldName = arrParamNames[0];
            String strParamValue = this.request.getParameter(strFieldName);
            String strParamValue2 = this.request.getParameter(String.valueOf(strFieldName) + "1");
            if (strParamValue2 == null) {
                strParamValue2 = "";
            }
            if (!strFieldName.startsWith("VAR_")) {
                if (strFieldName.startsWith("$_")) {
                    strFieldName = this.request.getParameter("VAR" + strFieldName.substring(1));
                }
                if (strParamValue != null && !strParamValue.toString().equals("")) {
                    try {
                        strParamValue = EString.encoderStr(strParamValue.toString());
                    }
                    catch (Exception ex) {
                    	//do nothing
                    }
                    
                    if (strFieldName.startsWith("$_")) {
                        strFieldName = this.request.getParameter(strFieldName.substring(2));
                    }
                    
                    if (strParamValue.startsWith("SYS_")) {
                        final String[] arrStrDate = strParamValue.split("_");
                        if (arrStrDate[2].equals("Y")) {
                            calendarDate.add(1, -1 * Integer.parseInt(arrStrDate[1]));
                        }
                        else if (arrStrDate[2].equals("M")) {
                            calendarDate.add(2, -1 * Integer.parseInt(arrStrDate[1]));
                        }
                        else if (arrStrDate[2].equals("W")) {
                            calendarDate.add(5, -7 * Integer.parseInt(arrStrDate[1]));
                        }
                        else if (arrStrDate[2].equals("D")) {
                            calendarDate.add(5, -1 * Integer.parseInt(arrStrDate[1]));
                        }
                        final String strDate = sdf.format(calendarDate.getTime());
                        if (arrStrDate.length > 3) {
                            strTempCon = String.valueOf(strTempCon) + " AND " + strFieldName + " <= '" + strDate + "'";
                        }
                        else {
                            strTempCon = String.valueOf(strTempCon) + " AND " + strFieldName + " > '" + strDate + "'";
                        }
                    }
                    else if (arrParamNames[4].equals("LIKE")) {
                        strTempCon = String.valueOf(strTempCon) + " AND " + strFieldName + " like '%" + strParamValue + "%'";
                    }
                    else if (arrParamNames[4].equals("ARE")) {
                        try {
                            strParamValue2 = EString.encoderStr(strParamValue2.toString());
                        }
                        catch (Exception ex2) {}
                        
                        if (!"".equals(strParamValue2)) {
                        	strTempCon = String.valueOf(strTempCon) + " AND (" + strFieldName + " >= '" + strParamValue + "' AND " + strFieldName + " <= '" + strParamValue2 + "')";
						}
                        else {
                        	strTempCon = String.valueOf(strTempCon) + " AND (" + strFieldName + " >= '" + strParamValue + "')";
                        }
                    }
                    else {
                        strTempCon = String.valueOf(strTempCon) + " AND " + strFieldName + " " + arrParamNames[4] + " '" + strParamValue + "'";
                    }
                }
                else {
                    strParamValue = "";
                    
                    if (!"".equals(strParamValue2) && arrParamNames[4].equals("ARE")) {
                        try {
                            strParamValue2 = EString.encoderStr(strParamValue2.toString());
                        }
                        catch (Exception ex2) {}
                        strTempCon = String.valueOf(strTempCon) + " AND (" + strFieldName + " <= '" + strParamValue2 + "')";
                    }
                }
            }
            else if (strParamValue == null) {
                strParamValue = "";
            }
            
            if (iBrCount > 1 && i % iCols == 1 && i != iQFCount - 1 && i != 1) {
                sbSerch.append("</tr><tr class='trquerybg'>");
            }
            
            StringBuffer strEndSearch = null;
            String strAreLable = "";
            final String strArelableEnd = "<td align='left' class='tdquerybgsing2' width='10'>&nbsp;&nbsp;\u81f3&nbsp;&nbsp;</td>";
            if (arrParamNames[4].equals("ARE")) {
                strEndSearch = new StringBuffer();
                strAreLable = "&nbsp;&nbsp;";
            }
            
            if (iBrCount > 1) {
            	if (arrParamNames[1].equals("boolean")) {
            		sbSerch.append("<td align='right' class='tdquerybg' nowrap='nowrap' style='min-width:25px;padding-left:20px;'>" + arrParamNames[2] + "</td>");
				}
            	else {
            		sbSerch.append("<td align='left' class='tdquerybg' nowrap='nowrap' style='min-width:50px;padding-left:20px;'>&nbsp;" + arrParamNames[2] + ":" + strAreLable + "</td>");
            	}
            }
            else {
            	if (arrParamNames[1].equals("boolean")) {
            		sbSerch.append("<td align='right' class='tdquerybgsing2' nowrap='nowrap' style='min-width:25px;padding-left:20px;'>" + arrParamNames[2] + "</td>");
				}
            	else {
            		sbSerch.append("<td align='left' class='tdquerybgsing2' nowrap='nowrap' style='min-width:50px;padding-left:20px;'>&nbsp;" + arrParamNames[2] + ":" + strAreLable + "</td>");
            	}
            }
            
            //显示规则
            String inputWidth = "180px";
            if (arrParamNames[1].equals("0")) {
                sbSerch.append("<td align='left'><input type='text' name='" + arrParamNames[0] + "' value='" + strParamValue + "' style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    strEndSearch.append(String.valueOf(strArelableEnd) + "<td align='left'>&nbsp;<input type='text' name='" + arrParamNames[0] + "1' value='" + strParamValue2 + "' style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[1].equals("boolean")) {
            	sbSerch.append("<td align='left'><input type='checkbox' name='" + arrParamNames[0] + "' value='true' class='inputCheck' " + ("true".equals(strParamValue)?"checked":"") +"></td>");
                if (strEndSearch != null) {
                    strEndSearch.append(String.valueOf(strArelableEnd) + "<td align='left'>&nbsp;<input type='checkbox' name='" + arrParamNames[0] + "1' value='true' class='inputCheck' " + ("true".equals(strParamValue2)?"checked":"") +"></td>");
                }
            }
            else if (arrParamNames[1].equals("SYS_CURYEAR")) {
                sbSerch.append("<td align='left'><input  id='sys_date" + i + "' readonly value='" + strParamValue + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy'})\"    name='" + arrParamNames[0] + "'  style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    strEndSearch.append(String.valueOf(strArelableEnd) + "<td align='left'><input  id='sys_date" + i + "_1' readonly value='" + strParamValue2 + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy'})\"    name='" + arrParamNames[0] + "1'  style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[1].equals("SYS_CURYM")) {
                sbSerch.append("<td align='left'><input  id='sys_date" + i + "' readonly value='" + strParamValue + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM'})\"    name='" + arrParamNames[0] + "'  style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    strEndSearch.append(String.valueOf(strArelableEnd) + "<td align='left'><input  id='sys_date" + i + "_1' readonly value='" + strParamValue2 + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM'})\"    name='" + arrParamNames[0] + "1'  style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[1].equals("SYS_CURYMD")) {
                sbSerch.append("<td align='left'><input  id='sys_date" + i + "' readonly value='" + strParamValue + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})\"    name='" + arrParamNames[0] + "'  style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    sbSerch.append(String.valueOf(strArelableEnd) + "<td align='left'><input  id='sys_date" + i + "_1' readonly value='" + strParamValue2 + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})\"    name='" + arrParamNames[0] + "1'  style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[1].equals("SYS_CURYMDH")) {
                sbSerch.append("<td align='left'><input  id='sys_date" + i + "' readonly value='" + strParamValue + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH'})\"    name='" + arrParamNames[0] + "'  style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    sbSerch.append(String.valueOf(strArelableEnd) + "<td align='left'><input  id='sys_date" + i + "_1' readonly value='" + strParamValue2 + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH'})\"    name='" + arrParamNames[0] + "1'  style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[1].equals("SYS_CURYMDHM")) {
                sbSerch.append("<td align='left'><input  id='sys_date" + i + "' readonly value='" + strParamValue + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH:mm'})\"    name='" + arrParamNames[0] + "'  style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    sbSerch.append(String.valueOf(strArelableEnd) + "<td align='left'><input  id='sys_date" + i + "_1' readonly value='" + strParamValue2 + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH:mm'})\"    name='" + arrParamNames[0] + "1'  style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[1].equals("SYS_CURYMDHMS")) {
                sbSerch.append("<td align='left'><input  id='sys_date" + i + "' readonly value='" + strParamValue + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})\"    name='" + arrParamNames[0] + "'  style='width:"+inputWidth+";'></td>");
                if (strEndSearch != null) {
                    sbSerch.append(String.valueOf(strArelableEnd) + "<td align='left'><input  id='sys_date" + i + "_1' readonly value='" + strParamValue2 + "' type='text' class='Wdate' onClick=\"WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})\"    name='" + arrParamNames[0] + "1'  style='width:"+inputWidth+";'></td>");
                }
            }
            else if (arrParamNames[3].equals("0")) {
                if (arrParamNames[1].startsWith("SYS_")) {
                    final String strParamId = arrParamNames[1].substring(4);
                    sbSerch.append("<td align='left' id='param" + arrParamNames[1] + "' selname='" + arrParamNames[0] + "' parvalue='" + strParamValue + "'>&nbsp;</td>");
                    strScript = String.valueOf(strScript) + "changeSel('" + strParamId + "',$('sys_sel_" + strParamId + "'),'" + strParamValue + "');";
                }
                else {
                    viewParams.request = this.request;
                    final StringBuffer sbParam = viewParams.viewtype(arrParamNames[1], "id='sys_sel_" + arrParamNames[1] + "' name='" + arrParamNames[0] + "' value='" + strParamValue + "' style='width:"+inputWidth+";'", strParamValue);
                    sbSerch.append("<td align='left'>" + (Object)sbParam + "</td>");
                }
            }
            else {
                dic.strValue = strParamValue;
                sbSerch.append("<td align='left'>" + dic.generSelect(arrParamNames[1], "id='" + arrParamNames[0] + "' name='" + arrParamNames[0] + "' value='" + strParamValue + "' style='width:"+inputWidth+";'") + "</td>");
                if (strEndSearch != null) {
                    strEndSearch.append(String.valueOf(strArelableEnd) + "<td align='left'>" + dic.generSelect(arrParamNames[1], "name='" + arrParamNames[0] + "1' value='" + strParamValue2 + "' style='width:"+inputWidth+";'") + "</td>");
                }
            }
            
            if (strEndSearch != null) {
                sbSerch.append(strEndSearch);
            }
        }
        
        if (!strTempCon.equals("")) {
            strTempCon = "(" + strTempCon.substring(4) + ")";
            aQuery.addCon(strTempCon);
            this.strSeachCon = "&SYSSERCACHCON=" + URLEncoder.encode(strTempCon);
        }
        
        if (!strScript.equals("")) {
            sbSerch.append("<script>" + strScript + "</script>");
        }
        return iBrCount;
    }
    
    private boolean cerRights() {
        return this.request != null && this.request.getSession().getAttribute("SYS_STRCURUSER").toString().equals("888");
    }
    
    public String getSplitValue(Object _strValue, final int _iIndex) {
        if (_strValue == null) {
            _strValue = "";
        }
        final String vResult = _strValue.toString();
        return vResult;
    }
    
    public StringBuffer toExcel(final Query aQuery, final String[] aStrHeadCode, final String[] aTableHeads) {
        final ExcelManager eM = new ExcelManager();
        try {
            final OutputStream objOut = (OutputStream)this.response.getOutputStream();
            final byte[] byteData = eM.queryToExcel(aQuery, aStrHeadCode, aTableHeads, this.arrStrIsDic, this.arrStrFileSize);
            objOut.write(byteData);
        }
        catch (Exception e) {
            System.out.println("err:" + e);
        }
        return null;
    }
}
