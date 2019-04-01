package com.yulongtao.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.ArrayUtils;

import com.Debug;
import com.page.method.Fun;
import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Query;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Pub;
import com.yulongtao.sys.Dic;
import com.yulongtao.sys.QRSC;
import com.yulongtao.sys.SqlStaticCon;
import com.yulongtao.sys.View;
import com.yulongtao.util.EFile;
import com.yulongtao.util.EString;
import com.yulongtao.util.YLUUID;
import com.yulongtao.web.component.ComponentInvoke;
import com.yulongtao.web.component.TabPage;

public class ABSElement extends BodyTagSupport
{
    private String strPageCode;
    private String strIsSplit;
    private String strElementTitle;
    private String isTrColor;
    private String strStyle;
    private String width;
    private String strUseMod;
    private String strParams;
    private String strPut;
    private String strLineCount;
    private String strDic;
    private String strErrMsg;
    public boolean bIsExcel;
    public HttpServletResponse response;
    private FormItem fItem;
    public SimpleDateFormat sdf;
    private Hashtable hashParams;
    public static String debug_absPage;
    private String strDefault;
    Dic dic;
    private Hashtable hashTableCon;
    public boolean bIsUpdate;
    private String strUpdateField;
    private String strUpdateInput;
    private Hashtable hashMutTableCon;
    private StringBuffer sbMutTableUpdateCon;
    private HashMap mapNoCopyField;
    private HashMap mapNoCopyTab;
    private HashMap mapNeedCopyField;
    public boolean bIsDoMerFormQuery;
    public boolean bIsQueryForm;
    private String strGlobQDBttn;
    public boolean bIsEditPage;
    public String sysStrRowClick;
    public boolean bIsHaveChildForm;
    public String strIsEmtyIgnore;
    public String[] arrChildFormPage;
    public String[] arrChildOrderField;
    private String strUrlParams;
    private boolean bIsCopy;
    HashMap hashFormFields;
    private String strChildTableId;
    public int iCols;
    private int iViewCount;
    StringBuffer sbScript;
    String strInitScript;
    public StringBuffer sbUpdateConfig;
    public HashMap hashBatchKeyMsg;
    public String strFlowMainForm;
    public int iMainFormHeight;
    public int iChildFormHeight;
    public String strTreeSelectId;
    private boolean bIsGetBatchUpdate;
    Vector vecUploadIds;
    String strSetFocusScript;
    int iFilePath;
    private boolean bIsViewEdit;
    private EFile eFile;
    public StringBuffer sbSingUpload;
    public StringBuffer sbMupUpload;
    private StringBuffer sbFuText;
    private int iUploadBttn;
    public boolean bIsMutiUpload;
    private boolean bIsFuText;
    public boolean bIsSingleUpload;
    private String strFilePath;
    
    static {
        ABSElement.debug_absPage = null;
    }
    
    public ABSElement() {
        this.strPageCode = "";
        this.strIsSplit = "";
        this.strElementTitle = "";
        this.isTrColor = "";
        this.strStyle = "1";
        this.width = "100%";
        this.strUseMod = "false";
        this.strParams = "";
        this.strPut = "";
        this.strLineCount = "";
        this.strDic = "";
        this.strErrMsg = "";
        this.bIsExcel = false;
        this.response = null;
        this.fItem = new FormItem();
        this.hashParams = new Hashtable();
        this.strDefault = "";
        this.dic = new Dic();
        this.hashTableCon = null;
        this.bIsUpdate = false;
        this.strUpdateField = "";
        this.strUpdateInput = "";
        this.hashMutTableCon = null;
        this.sbMutTableUpdateCon = new StringBuffer();
        this.mapNoCopyField = new HashMap();
        this.mapNoCopyTab = new HashMap();
        this.mapNeedCopyField = new HashMap();
        this.bIsDoMerFormQuery = false;
        this.bIsQueryForm = false;
        this.strGlobQDBttn = "";
        this.bIsEditPage = false;
        this.sysStrRowClick = "";
        this.bIsHaveChildForm = false;
        this.strIsEmtyIgnore = "";
        this.arrChildFormPage = null;
        this.arrChildOrderField = null;
        this.strUrlParams = "";
        this.bIsCopy = false;
        this.hashFormFields = new HashMap();
        this.strChildTableId = "";
        this.iCols = 1;
        this.iViewCount = 0;
        this.sbUpdateConfig = new StringBuffer();
        this.hashBatchKeyMsg = null;
        this.strFlowMainForm = "";
        this.iMainFormHeight = 60;
        this.iChildFormHeight = 40;
        this.strTreeSelectId = "";
        this.bIsGetBatchUpdate = false;
        this.vecUploadIds = new Vector();
        this.strSetFocusScript = "";
        this.iFilePath = 0;
        this.bIsViewEdit = true;
        this.eFile = new EFile();
        this.sbSingUpload = new StringBuffer();
        this.sbMupUpload = new StringBuffer();
        this.sbFuText = new StringBuffer();
        this.iUploadBttn = 0;
        this.bIsMutiUpload = false;
        this.bIsFuText = false;
        this.bIsSingleUpload = false;
        this.strFilePath = "";
    }
    
    public int doStartTag() throws JspTagException {
        return 1;
    }
    
    public int doEndTag() throws JspTagException {
        this.doAction();
        return 6;
    }
    
    public void setStrPageCode(final String strPageCode) {
        this.strPageCode = strPageCode;
    }
    
    public String getStrPageCode() {
        return this.strPageCode;
    }
    
    public void debugPage() throws Exception {
        final ABSPage absPage = (ABSPage)Class.forName(ABSElement.debug_absPage).newInstance();
        absPage.initPage();
    }
    
    public void setQueryField(final String _aStr) {
        this.strDefault = _aStr;
    }
    
    public void doAction() {
        ABSElement.debug_absPage = View.debug_absPage;
        if (ABSElement.debug_absPage != null) {
            try {
                this.debugPage();
            }
            catch (Exception e2) {
                System.out.println("DEBUG\u9519\u8bef\uff01");
            }
        }
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(this.strPageCode);
        final String strType = hashHQRC.get("SPAGETYPE").toString();
        final HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        final JspWriter out = this.pageContext.getOut();
        final int iType = Integer.parseInt(strType);
        if (iType != 5 && !this.strParams.equals("")) {
            final String[] strArrParams = this.strParams.split("&");
            for (int iParamsCount = strArrParams.length, i = 0; i < iParamsCount; ++i) {
                final String[] strArrParamsValue = strArrParams[i].split("=");
                this.hashParams.put(strArrParamsValue[0], strArrParamsValue[1]);
            }
        }
        switch (iType) {
            case 1: {
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
                try {
                    out.println((Object)this.generQuery(hashHQRC, request));
                }
                catch (Exception e) {
                    Debug.println("\u751f\u6210\u67e5\u8be2\u9519\u8bef\uff01" + e);
                }
                break;
            }
            case 2: {
                try {
                    out.println((Object)this.generInput(hashHQRC, request));
                }
                catch (Exception e) {
                    Debug.println("\u751f\u6210\u5f55\u5165\u9519\u8bef\uff01" + e);
                }
                break;
            }
            case 3: {
                try {
                    out.println((Object)this.generInput(hashHQRC, request));
                }
                catch (Exception e) {
                    Debug.println("\u751f\u6210\u4fee\u6539\u9519\u8bef\uff01" + e);
                }
                break;
            }
            case 5: {
                try {
                    this.generCheck(hashHQRC, out, request);
                }
                catch (Exception e) {
                    Debug.println("\u751f\u6210\u68c0\u6d4b\u9519\u8bef\uff01" + e);
                }
                break;
            }
        }
    }
    
    private void generCheck(final Hashtable hashHQRC, final JspWriter aOut, final HttpServletRequest aRequest) throws Exception {
        String strSqlField = hashHQRC.get("SQLFIELD").toString();
        if (strSqlField.equals("")) {
            strSqlField = "*";
        }
        String strCondition = hashHQRC.get("SQERYCON").toString();
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        Matcher ma = pa.matcher(strCondition);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            final String strReValue = aRequest.getParameter(aStrParam);
            if (strReValue != null) {
                strCondition = strCondition.replaceAll("<<" + aStrParam + ">>", strReValue);
            }
            else {
                final Object objSessionValue = aRequest.getSession().getAttribute(aStrParam);
                if (objSessionValue != null) {
                    strCondition = strCondition.replaceAll("<<" + aStrParam + ">>", objSessionValue.toString());
                }
                else {
                    Debug.println("[\u4fe1\u606f]:\u5728\u6807\u7b7e\u5c5e\u6027,request,session\u91cc\u90fd\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam + "],\u8bf7\u68c0\u67e5\u662f\u5426\u4f20\u5165\u4e86\u6b64\u53c2\u6570\uff01");
                }
            }
        }
        final Query query = new Query(strSqlField, hashHQRC.get("SQUERYTABLE").toString(), strCondition);
        DBFactory dbfQuery = null;
        try {
            dbfQuery = new DBFactory();
            TableEx tb = dbfQuery.query(query);
            Record recordMsg = null;
            int iVresult = 0;
            if (tb.getRecordCount() > 0) {
                if (this.strParams.equals("HaveData")) {
                    recordMsg = tb.getRecord(0);
                    iVresult = 1;
                }
                else {
                    recordMsg = tb.getRecord(0);
                    ma = pa.matcher(this.strParams);
                    while (ma.find()) {
                        final String aStrParam2 = ma.group(1);
                        final Object objValue = aRequest.getSession().getAttribute(aStrParam2);
                        String strReValue2;
                        if (objValue != null) {
                            strReValue2 = objValue.toString();
                        }
                        else {
                            strReValue2 = aRequest.getParameter(aStrParam2);
                        }
                        if (strReValue2 != null) {
                            this.strParams = this.strParams.replaceAll("<<" + aStrParam2 + ">>", strReValue2);
                        }
                        else {
                            final Object objSessionValue2 = recordMsg.getFieldByName(aStrParam2).value;
                            if (objSessionValue2 == null) {
                                throw new Exception("\u5728\u6807\u7b7e\u5c5e\u6027,request,session\u91cc\u90fd\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam2 + "],\u8bf7\u68c0\u67e5\u662f\u5426\u4f20\u5165\u4e86\u6b64\u53c2\u6570\uff01");
                            }
                            this.strParams = this.strParams.replaceAll("<<" + aStrParam2 + ">>", objSessionValue2.toString());
                        }
                    }
                    ma = pa.matcher(this.strElementTitle);
                    while (ma.find()) {
                        final String aStrParam2 = ma.group(1);
                        final Object objValue = aRequest.getSession().getAttribute(aStrParam2);
                        String strReValue2;
                        if (objValue != null) {
                            strReValue2 = objValue.toString();
                        }
                        else {
                            strReValue2 = aRequest.getParameter(aStrParam2);
                        }
                        if (strReValue2 != null) {
                            this.strElementTitle = this.strElementTitle.replaceAll("<<" + aStrParam2 + ">>", strReValue2);
                        }
                        else {
                            final Object objSessionValue2 = recordMsg.getFieldByName(aStrParam2).value;
                            if (objSessionValue2 == null) {
                                throw new Exception("\u5728\u6807\u7b7e\u5c5e\u6027,request,session\u91cc\u90fd\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam2 + "],\u8bf7\u68c0\u67e5\u662f\u5426\u4f20\u5165\u4e86\u6b64\u53c2\u6570\uff01");
                            }
                            this.strElementTitle = this.strElementTitle.replaceAll("<<" + aStrParam2 + ">>", objSessionValue2.toString());
                        }
                    }
                }
            }
            if (!this.strParams.equals("HaveData")) {
                tb = dbfQuery.query("select " + this.strParams + " vresult");
                final Record record = tb.getRecord(0);
                iVresult = Integer.parseInt(record.getFieldByName("vresult").value.toString());
            }
            final String[] strCheckText = this.strElementTitle.split("\\$");
            if (iVresult == 0) {
                aOut.println(strCheckText[1]);
            }
            else {
                if (!this.strPut.equals("")) {
                    final String[] strArrParams = this.strPut.split("&");
                    for (int iParamsCount = strArrParams.length, i = 0; i < iParamsCount; ++i) {
                        final String[] strArrParamsValue = strArrParams[i].split("=");
                        aRequest.getSession().setAttribute(strArrParamsValue[0], (Object)recordMsg.getFieldByName(strArrParamsValue[1]).value.toString());
                    }
                }
                if (strCheckText[0].startsWith("URL_")) {
                    aOut.println("<a id=\"href\" href=\"" + strCheckText[0].substring(4) + "\"></a><script>href.click();</script>");
                }
                else {
                    aOut.println(strCheckText[0]);
                }
            }
        }
        catch (Exception e) {
            Debug.println("\u751f\u6210\u68c0\u6d4b\u9519\u8bef\uff01\u8bf7\u68c0\u67e5\u68c0\u68c0\u6d4b\u6761\u4ef6\u53ca\u67e5\u8be2\u8bed\u53e5\u662f\u5426\u6709\u9519\uff01" + e);
            return;
        }
        finally {
            if (dbfQuery != null) {
                dbfQuery.close();
            }
        }
        if (dbfQuery != null) {
            dbfQuery.close();
        }
    }
    
    public void initMutCon(final String _strCon, final HttpServletRequest request) throws Exception {
        if (!this.bIsEditPage) {
            this.bIsUpdate = false;
            return;
        }
        this.bIsUpdate = true;
        this.hashMutTableCon = new Hashtable();
        this.strUpdateField = "";
        this.strUpdateInput = "";
        final Hashtable hashCon = new Hashtable();
        final Vector vecTable = new Vector();
        final String strCon = this.getFilterData(_strCon, request);
        final String[] arrStrCon = strCon.split(",");
        for (int iSize = arrStrCon.length, i = 0; i < iSize; ++i) {
            final String[] arrStr = arrStrCon[i].split("\\$");
            final String[] arrInputValue = arrStrCon[i].split("\\=");
            final String strTableCode = arrStr[0].trim();
            final Object objTableCon = hashCon.get(strTableCode);
            if (objTableCon == null) {
                hashCon.put(strTableCode, arrStr[1]);
                vecTable.add(strTableCode);
            }
            else {
                hashCon.put(strTableCode, objTableCon + " AND " + arrStr[1]);
            }
        }
        final int iSize = vecTable.size();
        final Hashtable hashOrd = new Hashtable();
        for (int iTableOrderFieldCount = this.arrChildOrderField.length, j = 0; j < iTableOrderFieldCount; ++j) {
            final String strChildOrdField = this.arrChildOrderField[j].trim();
            if (!strChildOrdField.equals("") && !strChildOrdField.equals("*")) {
                final String[] arrTableOrd = strChildOrdField.split("\\$");
                final String strTableCode2 = arrTableOrd[0].trim();
                final Object objTableOrd = hashOrd.get(strTableCode2);
                if (objTableOrd == null) {
                    hashOrd.put(strTableCode2, arrTableOrd[1].trim());
                }
            }
        }
        for (int j = 0; j < iSize; ++j) {
            final String strTable = vecTable.get(j).toString();
            String strTableCon = hashCon.get(strTable).toString();
            final Object objOrd = hashOrd.get(strTable);
            if (objOrd != null) {
                strTableCon = String.valueOf(strTableCon) + " order by " + objOrd;
            }
            this.sbMutTableUpdateCon.append("<textarea name='NO_sys_ud_con_" + strTable + "' style='display:none;'>" + strTableCon + "</textarea>");
            TableEx tableEx = null;
            try {
                tableEx = new TableEx("*", strTable, strTableCon);
                System.out.println(String.valueOf(strTable) + "\u6761\u4ef6" + strTableCon);
                final Record record = null;
                if (tableEx.getRecordCount() > 0) {
                    this.hashMutTableCon.put(strTable, tableEx);
                }
                else {
                    this.bIsUpdate = false;
                }
            }
            catch (Exception e) {
                System.out.println("\u6761\u4ef6\u67e5\u8be2\u62a5\u9519:" + e);
            }
        }
    }
    
    public void initCon(final String _strCon, final HttpServletRequest request) throws Exception {
        if (!this.bIsEditPage) {
            this.bIsUpdate = false;
            return;
        }
        this.bIsUpdate = true;
        this.hashTableCon = new Hashtable();
        this.strUpdateField = "";
        this.strUpdateInput = "";
        final Hashtable hashCon = new Hashtable();
        final Vector vecTable = new Vector();
        final String strCon = this.getFilterData(_strCon, request);
        final String[] arrStrCon = strCon.split(",");
        for (int iSize = arrStrCon.length, i = 0; i < iSize; ++i) {
            final String[] arrStr = arrStrCon[i].split("\\$");
            final String[] arrInputValue = arrStrCon[i].split("\\=");
            this.mapNoCopyField.put(arrInputValue[0].trim(), "");
            this.strUpdateField = String.valueOf(this.strUpdateField) + ";" + arrInputValue[0].trim();
            this.strUpdateInput = String.valueOf(this.strUpdateInput) + "<textarea name='" + arrInputValue[0].trim() + "' style='display:none;'>" + arrInputValue[1].replaceAll("'", "") + "</textarea>";
            final String strTableCode = arrStr[0].trim();
            final Object objTableCon = hashCon.get(strTableCode);
            if (objTableCon == null) {
                hashCon.put(strTableCode, arrStr[1]);
                vecTable.add(strTableCode);
            }
            else {
                hashCon.put(strTableCode, objTableCon + " AND " + arrStr[1]);
            }
        }
        for (int iSize = vecTable.size(), i = 0; i < iSize; ++i) {
            final String strTable = vecTable.get(i).toString();
            final String strTableCon = hashCon.get(strTable).toString();
            TableEx tableEx = null;
            try {
                tableEx = new TableEx("*", strTable, strTableCon);
                Record record = null;
                if (tableEx.getRecordCount() > 0) {
                    record = tableEx.getRecord(0);
                    this.hashTableCon.put(strTable, record);
                }
                else {
                    this.bIsUpdate = false;
                }
            }
            catch (Exception ex) {
                continue;
            }
            finally {
                tableEx.close();
            }
            tableEx.close();
        }
    }
    
    public StringBuffer generBatchInput(final Hashtable hashHQRC, final HttpServletRequest request) throws Exception {
        final String strSourcePageCode = hashHQRC.get("SFIELDSIZE").toString();
        final String strMutilFields = hashHQRC.get("SFIELDNAME").toString();
        final Hashtable hashSourceHQRC = (Hashtable) QRSC.HASHQRSC.get(strSourcePageCode);
        return this.generInputMult(hashSourceHQRC, request, strMutilFields, hashHQRC.get("SPAGECODE"));
    }
    
    public StringBuffer generInput(final Hashtable hashHQRC, final HttpServletRequest request) throws Exception {
        return this.generInputMult(hashHQRC, request, "", "");
    }
    
    public StringBuffer generInputMult(final Hashtable hashHQRC, final HttpServletRequest request, final String _strMults, final Object _objPageCode) throws Exception {
        this.bIsMutiUpload = false;
        this.bIsSingleUpload = false;
        this.bIsFuText = false;
        this.iUploadBttn = 0;
        this.sbSingUpload = new StringBuffer();
        this.sbMupUpload = new StringBuffer();
        this.sbFuText = new StringBuffer();
        this.strSetFocusScript = "";
        this.strUrlParams = this.getUrlParams(request);
        this.strErrMsg = "";
        if (request.getParameter("sys_copy") != null) {
            final String strNoCopyField = request.getParameter("sys_no_copy");
            if (strNoCopyField != null) {
                final String[] arrStrNoCoyp = strNoCopyField.split(",");
                for (int iNoCopyCount = arrStrNoCoyp.length, i = 0; i < iNoCopyCount; ++i) {
                    this.mapNoCopyField.put(arrStrNoCoyp[i], "");
                }
            }
            final String strNoCopyTab = request.getParameter("sys_no_copy_tab");
            if (strNoCopyTab != null) {
                final String[] arrStrNoCoyp2 = strNoCopyTab.split(",");
                for (int iNoCopyCount2 = arrStrNoCoyp2.length, j = 0; j < iNoCopyCount2; ++j) {
                    this.mapNoCopyTab.put(arrStrNoCoyp2[j], "");
                }
            }
            this.bIsCopy = true;
        }
        String strQDBttn = request.getParameter("NO_QDBTTN");
        String strQXBttn = request.getParameter("NO_QXBTTN");
        if (strQDBttn != null) {
            strQDBttn = EString.encoderStr(strQDBttn);
            if (strQDBttn.equals("no")) {
                strQDBttn = "";
            }
            else {
                strQDBttn = Pub.getBttn(Pub.getBttnText("bc.png", strQDBttn), "$('bttnsysaddsubmit').click();", "sys_form_btn");
            }
        }
        else {
            strQDBttn = Pub.getBttn(Pub.getBttnText("bc.png", "\u4fdd\u5b58"), "$('bttnsysaddsubmit').click();", "sys_form_btn\" id=\"sys_form_display_bttn");
        }
        if (strQXBttn != null) {
            strQXBttn = EString.encoderStr(strQXBttn);
            if (strQXBttn.equals("no")) {
                strQXBttn = "";
            }
            else {
                strQXBttn = Pub.getBttn(Pub.getBttnText("gb.png", strQXBttn), "closeWin();", "sys_form_btn");
            }
        }
        else {
            strQXBttn = Pub.getBttn(Pub.getBttnText("gb.png", "\u53d6\u6d88"), "closeWin();", "sys_form_btn");
        }
        final Object objICOS = hashHQRC.get("SCONID");
        if (objICOS != null) {
            final String strICOS = objICOS.toString();
            if (!strICOS.equals("") && !strICOS.equals("*")) {
                this.iCols = Integer.parseInt(strICOS);
            }
            else {
                this.iCols = 1;
            }
        }
        else {
            this.iCols = 1;
        }
        final StringBuffer vResult = new StringBuffer();
        final String strWidth = "100%";
        vResult.append("<form id=\"add\" method=\"post\" style=\"width:100%;height:100%;\">");
        final String strFieldCodes = this.getFilterData(hashHQRC.get("SFIELDCODE").toString(), request);
        String[] arrcode = strFieldCodes.split(",");
        String[] arrname = hashHQRC.get("SFIELDNAME").toString().replaceAll(" ", "&nbsp;").split(",");
        String[] arrSelPage = hashHQRC.get("SQUERYFIELD").toString().split(",");
        String[] arrSelPageSize = hashHQRC.get("SSIZE").toString().split(":");
        String[] arrReturn = hashHQRC.get("SGLFIELD").toString().split(",");
        String[] arrType = this.getFilterData(hashHQRC.get("SDELCON").toString(), request).split(",");
        String[] arrFieldsSize = hashHQRC.get("SFIELDSIZE").toString().split(",");
        String[] arrStrRules = hashHQRC.get("SHREFIELD").toString().split(",");
        final Object objTableOpType = hashHQRC.get("SQLFIELD");
        final String strCon = hashHQRC.get("SEDITPAGE").toString();
        if (!strCon.equals("") && !strCon.equals("*")) {
            this.initCon(strCon, request);
        }
        else {
            this.bIsUpdate = false;
        }
        final Object objIsBatch = hashHQRC.get("SFREE1");
        int iCount = arrcode.length;
        if (this.bIsDoMerFormQuery) {
            vResult.append("<table id='sys_form_frame' width='" + strWidth + "' border='0' align='center' cellpadding='0' cellspacing='0'><tr><td valign='top'><div id='sysformwindiv_merger' style='overflow:auto;width:100%;'>");
        }
        else {
            vResult.append("<table id='sys_form_frame' width='" + strWidth + "' border='0' align='center' cellpadding='0' cellspacing='0' height='100%'><tr><td valign='top'><div id='sysformwindiv' style='overflow:auto;width:100%;'>");
        }
        if (this.bIsHaveChildForm) {
            vResult.append("<div id='div_mainform'  style='width:100%;height:" + this.iMainFormHeight + "%;overflow:auto;'>");
        }
        String strFlowFromCode = this.strFlowMainForm;
        if (strFlowFromCode.equals("")) {
            strFlowFromCode = hashHQRC.get("SPAGECODE").toString();
        }
        final String strFlowVerMsg = this.getFormFlowMsg(strFlowFromCode, request);
        Object objFlowId = null;
        String strFlowVer = "";
        if (!strFlowVerMsg.equals("")) {
            final String[] arrFlowVerMsg = strFlowVerMsg.split("_");
            objFlowId = arrFlowVerMsg[0];
            if (arrFlowVerMsg.length > 1) {
                strFlowVer = arrFlowVerMsg[1];
            }
        }
        boolean bIsFlowForm = false;
        this.hashFormFields = new HashMap();
        final String strFrontFlowRunId = request.getParameter("sys_flow_run_id");
        
        if (strFrontFlowRunId != null) {
            TableEx tbFlowRunVer = null;
            try {
                tbFlowRunVer = new TableEx("S_AUDIT_VERSION", "t_sys_flow_run", "S_FLOW_ID='" + objFlowId + "' and S_RUN_ID='" + strFrontFlowRunId + "'");
                if (tbFlowRunVer.getRecordCount() > 0) {
                    strFlowVer = tbFlowRunVer.getRecord(0).getFieldByName("S_AUDIT_VERSION").value.toString();
                }
            }
            catch (Exception ex) {
                //do nothing
            }
            finally {
                if (tbFlowRunVer != null) {
                    tbFlowRunVer.close();
                }
            }
        }
        
        if (objFlowId != null) {
            bIsFlowForm = true;
            this.bIsViewEdit = false;
            this.hashFormFields = this.getFlowFormMsg(objFlowId, strFlowVer, strFrontFlowRunId, request);
            final Object objHashFields = this.hashFormFields.get(hashHQRC.get("SPAGECODE").toString());
            if (iCount > 0 && objHashFields != null) {
                final HashMap hashFields = (HashMap)objHashFields;
                for (int k = 0; k < iCount; ++k) {
                    if (hashFields.get(String.valueOf(arrcode[k]) + "___NOVIEW") != null) {
                        arrcode = (String[])ArrayUtils.remove((Object[])arrcode, k);
                        arrname = (String[])ArrayUtils.remove((Object[])arrname, k);
                        arrType = (String[])ArrayUtils.remove((Object[])arrType, k);
                        arrFieldsSize = (String[])ArrayUtils.remove((Object[])arrFieldsSize, k);
                        arrSelPage = (String[])ArrayUtils.remove((Object[])arrSelPage, k);
                        arrSelPageSize = (String[])ArrayUtils.remove((Object[])arrSelPageSize, k);
                        arrReturn = (String[])ArrayUtils.remove((Object[])arrReturn, k);
                        arrStrRules = (String[])ArrayUtils.remove((Object[])arrStrRules, k);
                        iCount = arrcode.length;
                        --k;
                    }
                    else if (hashFields.get(String.valueOf(arrcode[k]) + "___DOEDIT") != null) {
                        arrStrRules[k] = "_" + arrStrRules[k];
                        if (hashFields.get(String.valueOf(arrcode[k]) + "___DOBXTX") != null) {
                            arrStrRules[k] = "_bxtx";
                        }
                    }
                }
            }
        }
        if (iCount > 0 && arrcode[0].equals("$TAB")) {
            final TabPage tabPage = new TabPage(true);
            final boolean bIsSecPage = false;
            int iFormCount = 1;
            final String strFromTabId = "sys_form_content";
            String strTrueFromTableId = "sys_form_content";
            for (int l = 0; l < iCount; ++l) {
                if (arrcode[l].equals("$TAB")) {
                    final StringBuffer sbPage = new StringBuffer("<table id='" + strTrueFromTableId + "' width='" + strWidth + "' border='0' align='center' cellpadding='0' cellspacing='0' class='inputtb" + this.strStyle + "'>");
                    strTrueFromTableId = String.valueOf(strFromTabId) + iFormCount;
                    ++iFormCount;
                    final int iStartIndex = l + 1;
                    int iEndIndex = -1;
                    for (int m = iStartIndex; m < iCount; ++m) {
                        if (arrcode[m].equals("$TAB")) {
                            iEndIndex = m;
                            break;
                        }
                    }
                    String strTableEnd = "</tr></table>";
                    if (iEndIndex == -1) {
                        iEndIndex = iCount;
                        strTableEnd = "";
                    }
                    l = iEndIndex - 1;
                    final String[] arrTempCodes = Arrays.copyOfRange(arrcode, iStartIndex, iEndIndex);
                    sbPage.append(this.generInputField(arrTempCodes.length, arrTempCodes, Arrays.copyOfRange(arrname, iStartIndex, iEndIndex), Arrays.copyOfRange(arrType, iStartIndex, iEndIndex), Arrays.copyOfRange(arrFieldsSize, iStartIndex, iEndIndex), request, Arrays.copyOfRange(arrSelPage, iStartIndex, iEndIndex), Arrays.copyOfRange(arrSelPageSize, iStartIndex, iEndIndex), Arrays.copyOfRange(arrReturn, iStartIndex, iEndIndex), Arrays.copyOfRange(arrStrRules, iStartIndex, iEndIndex)));
                    tabPage.addPage(arrname[iStartIndex - 1], sbPage.append(strTableEnd));
                }
            }
            vResult.append(tabPage.getComponent());
        }
        else {
            this.strGlobQDBttn = strQDBttn;
            vResult.append("\r\n<table id='sys_form_content' width='" + strWidth + "' border='0' align='center' cellpadding='0' cellspacing='0' class='inputtb" + this.strStyle + "'>");
            vResult.append(this.generInputField(iCount, arrcode, arrname, arrType, arrFieldsSize, request, arrSelPage, arrSelPageSize, arrReturn, arrStrRules));
        }
        final String strTempBatchTables = this.getBatchTableCodes(iCount, arrcode);
        if (!strTempBatchTables.equals("")) {
            vResult.append("<input type='hidden' name='NO_ISBACH' value='" + strTempBatchTables + "'>");
        }
        final WebInput webInput = new WebInput();
        webInput.strStyle = this.strStyle;
        webInput.iCols = this.iCols;
        final String[] arrTable = hashHQRC.get("SQERYCON").toString().split(",");
        iCount = arrTable.length;
        
        try {
            for (int i2 = 0; i2 < iCount; ++i2) {
                if (this.bIsUpdate) {
                    webInput.recordEdit = (Record) this.hashTableCon.get(arrTable[i2]);
                }
                vResult.append(webInput.generInput(arrTable[i2]));
            }
        }
        catch (Exception ex2) {
            //do nothing
        }
        finally {
            webInput.close();
        }
        
        if (this.bIsUpdate && !this.bIsCopy) {
            vResult.append(this.strUpdateInput);
            vResult.append("<input name='NO_OPTYPE' type='hidden' value='1'>");
            vResult.append("<textarea name='NO_CON' style='display:none'>" + this.strUpdateField + "</textarea>");
        }
        vResult.append("</table>");
        if (this.bIsHaveChildForm) {
            vResult.append("</div><div id='div_childform' style='width:100%;height:" + this.iChildFormHeight + "%;overflow:hidden;'>");
            final boolean bIsUpdateChildCon = this.bIsUpdate;
            final String strBatch = this.generChildFrom(request, vResult, bIsFlowForm);
            if (!strBatch.equals("")) {
                vResult.append("<input type='hidden' name='NO_ISBACH' value='" + strBatch + "'>");
                if (bIsUpdateChildCon && !this.bIsCopy) {
                    vResult.append(this.sbMutTableUpdateCon);
                }
            }
            vResult.append("</div>");
            this.bIsUpdate = bIsUpdateChildCon;
        }
        String strOptFunc = "init(add)";
        final String strdb = hashHQRC.get("SDATADB").toString();
        if (!strdb.equals("")) {
            final String strFunDB = request.getParameter("sys_data_db_nm");
            if (strFunDB == null) {
                System.out.println("\u8be5\u8868\u5355\u4e3a\u52a8\u6001\u5e93\uff0c\u8bf7\u68c0\u67e5\u662f\u5426\u4f20\u5165\u4e86\u52a8\u6001\u5e93\uff01");
            }
            else {
                vResult.append("<input name='NO_sys_data_db_nm' type='hidden' value='" + strFunDB + "'>");
            }
        }
        String strAddNextRecord = "";
        if (!this.bIsUpdate && !this.bIsQueryForm) {
            if (request.getParameter("NO_sys_is_continue_add") == null) {
                strAddNextRecord = "<input id='NO_sys_is_continue_add' value='" + Pub.getCurUrl(request) + "' name='NO_sys_is_continue_add' type='checkbox' class='inputCheck' style='vertical-align:middle;'>&nbsp;<label for='NO_sys_is_continue_add' style='vertical-align:middle;font-family:\u9ed1\u4f53;color:#254422;'>\u4fdd\u5b58\u540e\u7ee7\u7eed\u6dfb\u52a0\u4e0b\u4e00\u6761</label>";
            }
            else {
                strAddNextRecord = "<input id='NO_sys_is_continue_add' checked value='" + Pub.getCurUrl(request) + "' name='NO_sys_is_continue_add' type='checkbox' class='inputCheck' style='vertical-align:middle;'>&nbsp;<label for='NO_sys_is_continue_add' style='vertical-align:middle;font-family:\u9ed1\u4f53;color:#254422;'>\u4fdd\u5b58\u540e\u7ee7\u7eed\u6dfb\u52a0\u4e0b\u4e00\u6761</label>";
            }
        }
        if (this.bIsHaveChildForm && !this.strIsEmtyIgnore.equals("")) {
            vResult.append("<input name='NO_sys_Emty_Ignore' type='hidden' value='" + this.strIsEmtyIgnore + "'>");
        }
        int iFormBttnWidth = 260;
        boolean bIsNoRunFlow = true;
        if (objFlowId != null) {
            final String strFlowRunId = request.getParameter("sys_flow_run_id");
            final String strFqFlowBttn = request.getParameter("NO_VIEW_FLOW_BTTN");
            iFormBttnWidth = 400;
            if (strFlowRunId != null) {
                bIsNoRunFlow = false;
            }
            if (bIsNoRunFlow && strFqFlowBttn == null) {
                strQDBttn = "<input type='hidden' id='NO_SYS_IS_DO_FLOW_START' name='NO_SYS_IS_DO_FLOW_START' value='0'>" + Pub.getBttn(Pub.getBttnText("qd.png", "\u53d1\u8d77\u7533\u8bf7"), "$('NO_SYS_IS_DO_FLOW_START').value='1';$('bttnsysaddsubmit').click();", "button blue\" id=\"sys_form_display_laun_bttn") + strQDBttn;
            }
            final HashMap hashTables = new HashMap();
            final String[] arrStrFiledCode = hashHQRC.get("SFIELDCODE").toString().split(",");
            for (int iFiledCount = arrStrFiledCode.length, i3 = 0; i3 < iFiledCount; ++i3) {
                final String[] arrTableField = arrStrFiledCode[i3].split("\\$");
                if (!arrTableField[0].equals("")) {
                    hashTables.put(arrTableField[0], "");
                }
            }
            final Iterator itTables = hashTables.keySet().iterator();
            final Object objFlowVer = strFlowVer;
            if (this.bIsUpdate && !this.bIsCopy) {
                String strUpdateRunId = "";
                while (itTables.hasNext()) {
                    final Object obj = itTables.next();
                    strUpdateRunId = this.getEditValue(obj + "$S_RUN_ID");
                    strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='" + obj + "$S_RUN_ID' value='" + strUpdateRunId + "'>";
                    strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='" + obj + "$SYS_FLOW_VER' value='" + objFlowVer + "'>";
                }
                strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='NO_sys_S_RUN_ID' value='" + strUpdateRunId + "'>";
            }
            else {
                final String strUpdateRunId = YLUUID.getUId();
                while (itTables.hasNext()) {
                    final Object obj = itTables.next();
                    System.out.println("runidtable:" + obj);
                    strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='" + obj + "$S_RUN_ID' value='" + strUpdateRunId + "'>";
                    strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='" + obj + "$SYS_FLOW_VER' value='" + objFlowVer + "'>";
                }
                strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='NO_sys_S_RUN_ID' value='" + strUpdateRunId + "'>";
            }
            strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='NO_sys_flow_id' value='" + objFlowId + "'>";
            strQDBttn = String.valueOf(strQDBttn) + "<input type='hidden' name='NO_sys_flow_Ver' value='" + objFlowVer + "'>";
        }
        vResult.append("</div></td></tr>");
        vResult.append("<tr><td height='29' valign='bottom'>");
        if (!this.bIsQueryForm) {
            vResult.append("<table id='sys_form_opbttn' class='bttformoparea' width=\"680\" border=\"0\"><tr><td>" + strAddNextRecord + "&nbsp;<button style='width:0px;display:none;' id='bttnsysaddsubmit' type='submit'></button></td><td align='right' width='" + iFormBttnWidth + "px'>" + strQDBttn + strQXBttn + "</td></tr></table>");
            this.strGlobQDBttn = "";
        }
        vResult.append("</td></tr></table></form>");
        if (objTableOpType != null && !objTableOpType.toString().equals("")) {
            final String strOpType = objTableOpType.toString();
            if (!strOpType.equals("*")) {
                strOptFunc = "initOther(add,'" + strOpType + "')";
            }
        }
        this.generHC(hashHQRC, vResult, "");
        if (this.bIsUpdate) {
            vResult.append("<script language=\"javascript\">" + strOptFunc + ";</script>");
        }
        else {
            vResult.append("<script language=\"javascript\">" + strOptFunc + ";</script>");
        }
        final String strRedirect = request.getParameter("NO_ISOPEN");
        if (strRedirect == null) {
            request.getSession().removeAttribute("REDIREACTION");
            request.getSession().removeAttribute("REDIRECTURL");
            request.getSession().removeAttribute("REDIRECTAUIT");
            request.getSession().removeAttribute("REDIRECTMSG");
            request.getSession().setAttribute("REDIREACTION", (Object)"parent.location.reload();");
        }
        if (!this.strErrMsg.equals("")) {
            vResult.append("");
        }
        if (this.bIsMutiUpload) {
            vResult.append("<script type='text/javascript' src='js/yluploader.js'></script>");
            vResult.append("<script>var uploadUrl=\"uploadFile?\";");
            vResult.append(this.sbMupUpload);
            vResult.append("</script>");
            if (this.bIsSingleUpload) {
                vResult.append("<script>");
                vResult.append(this.sbSingUpload);
                vResult.append("</script>");
            }
        }
        else if (this.bIsSingleUpload) {
            vResult.append("<script>var fileSavePath=\"upload/" + this.strFilePath + "/\";var uploadUrl=\"uploadFile?\";</script>");
            vResult.append("<script type='text/javascript' src='js/yluploader.js'></script>");
            vResult.append("<script>");
            vResult.append(this.sbSingUpload);
            vResult.append("</script>");
        }
        if (this.bIsFuText) {
            vResult.append(" <script type='text/javascript' charset='gbk' src='ed/ueditor.config.js'></script> <script type='text/javascript' charset='gbk' src='ed/ueditor.all.min.js'> </script> <script type='text/javascript' charset='gbk' src='ed/lang/zh-cn/zh-cn.js'></script>");
            vResult.append("<script>").append(this.sbFuText).append("</script>");
        }
        vResult.append(this.strSetFocusScript);
        final String strJs = hashHQRC.get("SJSSCRIPT").toString();
        if (!strJs.equals("")) {
            vResult.append("<script>");
            try {
                vResult.append(this.getFilterData(strJs, request));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            vResult.append("</script>");
        }
        return vResult;
    }
    
    private HashMap getFlowFormMsg(final Object objFlowId, final String strFlowVer, final String _strFlowRunId, final HttpServletRequest _request) {
        TableEx tableEx = null;
        final HashMap vResult = new HashMap();
        try {
            if (_strFlowRunId == null) {
                final String strNodeId = new Fun().getFlowStartNodeId(_request, objFlowId.toString(), strFlowVer);
                tableEx = new TableEx("S_AUDIT_TABLECONTROL", "t_sys_flow_node", "S_FLOW_ID='" + objFlowId + "' and S_AUDIT_VERSION='" + strFlowVer + "' and I_TYPE=3 and I_NODE_ID=" + strNodeId);
            }
            else {
                tableEx = new TableEx("S_AUDIT_TABLECONTROL", "t_sys_flow_run,t_sys_flow_node", "t_sys_flow_node.S_FLOW_ID='" + objFlowId + "' and t_sys_flow_node.S_AUDIT_VERSION='" + strFlowVer + "' and S_RUN_ID='" + _strFlowRunId + "' " + " and t_sys_flow_run.S_FLOW_ID=t_sys_flow_node.S_FLOW_ID and t_sys_flow_run.S_AUDIT_VERSION=t_sys_flow_node.S_AUDIT_VERSION" + " and t_sys_flow_run.S_NODE_CODE=t_sys_flow_node.I_NODE_ID");
            }
            final String[] arrFormTableRight = tableEx.getRecord(0).getFieldByName("S_AUDIT_TABLECONTROL").value.toString().split("`");
            for (int iFormTableCount = arrFormTableRight.length, i = 0; i < iFormTableCount; ++i) {
                final String[] arrStrFormMsg = arrFormTableRight[i].split("\\$");
                if (arrStrFormMsg[1].equals("true")) {
                    if (arrStrFormMsg.length > 2 && !arrStrFormMsg[2].equals("")) {
                        final HashMap hashMap = new HashMap();
                        final String[] arrStrFields = arrStrFormMsg[2].split("\\|");
                        for (int iFieldCount = arrStrFields.length, j = 0; j < iFieldCount; ++j) {
                            final String[] arrFieldMsg = arrStrFields[j].split(",");
                            final String strFieldCode = arrFieldMsg[0].replaceAll("\\.", "\\$");
                            if (arrFieldMsg[1].equals("false")) {
                                hashMap.put(String.valueOf(strFieldCode) + "___NOVIEW", "");
                            }
                            else if (arrFieldMsg[2].equals("true")) {
                                hashMap.put(String.valueOf(strFieldCode) + "___DOEDIT", "");
                                if (arrFieldMsg.length > 3 && arrFieldMsg[3].equals("true")) {
                                    hashMap.put(String.valueOf(strFieldCode) + "___DOBXTX", "");
                                }
                            }
                        }
                        vResult.put(arrStrFormMsg[0], hashMap);
                    }
                }
                else {
                    vResult.put(String.valueOf(arrStrFormMsg[0]) + "___NOVIEW", "");
                }
            }
        }
        catch (Exception e) {
            System.out.println("===" + e);
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
    
    private String getFormFlowMsg(final String _strFromId, final HttpServletRequest _request) {
        String vResult = "";
        final Hashtable hashTemp = QRSC.sys_Hash_Flow_HashForms.get(_strFromId);
        final String strUsrBranChid = _request.getSession().getAttribute("SYS_STRBRANCHID").toString();
        if (hashTemp != null) {
            final Enumeration e = hashTemp.keys();
            while (e.hasMoreElements()) {
                final String strFlowBranchId = e.nextElement().toString();
                if (strFlowBranchId.length() <= strUsrBranChid.length() && strUsrBranChid.substring(0, strFlowBranchId.length()).equals(strFlowBranchId)) {
                    vResult = hashTemp.get(strFlowBranchId).toString();
                    break;
                }
            }
        }
        return vResult;
    }
    
    public String generChildFrom(final HttpServletRequest request, final StringBuffer _vResult, final boolean _bIsFlowForm) {
        String strBatchTable = "";
        if (this.bIsHaveChildForm) {
            _vResult.append("<table id='sys_form_child' width='100%' height='100%' border='0' align='center' cellpadding='0' cellspacing='0' class='inputtb" + this.strStyle + "'><tr><td>");
            final int iChildFormCount = this.arrChildFormPage.length;
            String strSplit = "";
            final TabPage tabPage = new TabPage(true);
            for (int i = 0; i < iChildFormCount; ++i) {
                this.strChildTableId = this.arrChildFormPage[i];
                if (this.strChildTableId.startsWith("URL:")) {
                    try {
                        final String[] arrUrlMsg = this.strChildTableId.split(":");
                        tabPage.addPage(arrUrlMsg[1], this.getFilterNoTip(arrUrlMsg[2], request));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (!_bIsFlowForm || this.hashFormFields.get(String.valueOf(this.strChildTableId) + "___NOVIEW") == null) {
                    final Hashtable hashChildHQRC = (Hashtable) QRSC.HASHQRSC.get(this.strChildTableId);
                    this.iCols = -1;
                    final String strFieldCodes = this.getFilterData(hashChildHQRC.get("SFIELDCODE").toString(), request);
                    String[] arrcode = strFieldCodes.split(",");
                    String[] arrname = hashChildHQRC.get("SFIELDNAME").toString().replaceAll(" ", "&nbsp;").split(",");
                    String[] arrSelPage = hashChildHQRC.get("SQUERYFIELD").toString().split(",");
                    String[] arrSelPageSize = hashChildHQRC.get("SSIZE").toString().split(":");
                    String[] arrReturn = hashChildHQRC.get("SGLFIELD").toString().split(",");
                    String[] arrType = this.getFilterData(hashChildHQRC.get("SDELCON").toString(), request).split(",");
                    String[] arrFieldsSize = hashChildHQRC.get("SFIELDSIZE").toString().split(",");
                    String[] arrStrRules = hashChildHQRC.get("SHREFIELD").toString().split(",");
                    int iCount = arrcode.length;
                    final String strTempBatchTables = this.getBatchTableCodes(iCount, arrcode);
                    if (!strTempBatchTables.equals("")) {
                        strBatchTable = String.valueOf(strBatchTable) + strSplit + strTempBatchTables;
                        strSplit = ",";
                    }
                    try {
                        String strCon = hashChildHQRC.get("SEDITPAGE").toString();
                        if (this.mapNoCopyTab.get(this.strChildTableId) != null) {
                            strCon = "*";
                        }
                        if (!strCon.equals("") && !strCon.equals("*")) {
                            this.initMutCon(strCon, request);
                        }
                        else {
                            this.bIsUpdate = false;
                        }
                        final Object objHashFields = this.hashFormFields.get(this.strChildTableId);
                        if (iCount > 0 && objHashFields != null) {
                            final HashMap hashFields = (HashMap)objHashFields;
                            for (int j = 0; j < iCount; ++j) {
                                if (hashFields.get(String.valueOf(arrcode[j]) + "___NOVIEW") != null) {
                                    arrcode = (String[])ArrayUtils.remove((Object[])arrcode, j);
                                    arrname = (String[])ArrayUtils.remove((Object[])arrname, j);
                                    arrType = (String[])ArrayUtils.remove((Object[])arrType, j);
                                    arrFieldsSize = (String[])ArrayUtils.remove((Object[])arrFieldsSize, j);
                                    arrSelPage = (String[])ArrayUtils.remove((Object[])arrSelPage, j);
                                    arrSelPageSize = (String[])ArrayUtils.remove((Object[])arrSelPageSize, j);
                                    arrReturn = (String[])ArrayUtils.remove((Object[])arrReturn, j);
                                    arrStrRules = (String[])ArrayUtils.remove((Object[])arrStrRules, j);
                                    iCount = arrcode.length;
                                    --j;
                                }
                                else if (hashFields.get(String.valueOf(arrcode[j]) + "___DOEDIT") != null) {
                                    arrStrRules[j] = "_" + arrStrRules[j];
                                    if (hashFields.get(String.valueOf(arrcode[j]) + "___DOBXTX") != null) {
                                        arrStrRules[j] = "_bxtx";
                                    }
                                }
                            }
                        }
                        final StringBuffer sbPage = new StringBuffer("<div style='width:100%;height:100%;overflow:auto;'><table width='100%' id='sys_form_content" + this.strChildTableId + "'>").append(this.generInputField(iCount, arrcode, arrname, arrType, arrFieldsSize, request, arrSelPage, arrSelPageSize, arrReturn, arrStrRules)).append("</table></div>");
                        tabPage.addPage(hashChildHQRC.get("SPAGENAME").toString(), sbPage);
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            _vResult.append(tabPage.getComponent()).append("</td></tr></table>");
        }
        return strBatchTable;
    }
    
    public String getBatchTableCodes(final int iCount, final String[] arrcode) {
        String strTempBatchTables = "";
        if (this.iCols == -1) {
            final HashMap hashTempMap = new HashMap();
            String strTempSplit = "";
            for (final String strTempCode : arrcode) {
                final String strTable = strTempCode.split("\\$")[0];
                if (hashTempMap.get(strTable) == null) {
                    strTempBatchTables = String.valueOf(strTempBatchTables) + strTempSplit + strTable;
                    hashTempMap.put(strTable, "");
                    strTempSplit = ",";
                }
            }
        }
        return strTempBatchTables;
    }
    
    private Object generBatchInputFiled(final String _strChildPageCode, final HttpServletRequest _request) throws Exception {
        final StringBuffer sbBatch = new StringBuffer("<table><tr>");
        final Hashtable hashForm = (Hashtable) QRSC.HASHQRSC.get(_strChildPageCode);
        final String[] arrcode_ch = this.getFilterData(hashForm.get("SFIELDCODE").toString(), _request).split(",");
        final String[] arrname_ch = hashForm.get("SFIELDNAME").toString().replaceAll(" ", "&nbsp;").split(",");
        final String[] arrSelPage_ch = hashForm.get("SQUERYFIELD").toString().split(",");
        final String[] arrReturn_ch = hashForm.get("SGLFIELD").toString().split(",");
        final String[] arrType_ch = this.getFilterData(hashForm.get("SDELCON").toString(), _request).split(",");
        final String[] arrFieldsSize_ch = hashForm.get("SFIELDSIZE").toString().split(",");
        final String[] arrStrRules_ch = hashForm.get("SHREFIELD").toString().split(",");
        for (int iChildCount = arrcode_ch.length, i = 0; i < iChildCount; ++i) {
            final boolean bIsViewTd = true;
            final String strLable = arrname_ch[i];
            final String strRule = this.getRule(arrStrRules_ch[i], strLable);
            String strLableTd = "<td class=\"inputth" + this.strStyle + "\" width='100'";
            if (arrType_ch[i].startsWith("4")) {
                strLableTd = String.valueOf(strLableTd) + " style='display:none;'>";
            }
            else {
                strLableTd = String.valueOf(strLableTd) + ">";
            }
            if (!strRule.trim().equals("")) {
                strLableTd = String.valueOf(strLableTd) + "<label class='tdrequired'>*</label>" + strLable + "</td>";
            }
            else {
                strLableTd = String.valueOf(strLableTd) + strLable + "</td>";
            }
            sbBatch.append(strLableTd);
        }
        sbBatch.append("</tr></table>");
        return sbBatch;
    }
    
    private StringBuffer getAuditLog(final String strFlowId, final String strFlowRunId) {
        final StringBuffer vResult = new StringBuffer("<table id='sys_form_flow_panel' width='100%'><tr><td class='th1' width='100px'>\u52a8\u4f5c</td><td class='th1' width='100px'>\u5ba1\u6838\u4eba</td><td class='th1' width='100px'>\u5ba1\u6838\u65f6\u95f4</td><td class='th1' width='100px'>\u5ba1\u6838\u72b6\u6001</td><td class='th1'>\u5ba1\u6279\u610f\u89c1</td></tr>");
        TableEx tableEx = null;
        
        try {
            tableEx = new TableEx("t_sys_flow_log.*,S_NODE_NAME,SYGMC", "t_sys_flow_log,t_sys_flow_node,t_rgxx", "t_sys_flow_node.S_FLOW_ID=t_sys_flow_log.S_FLOW_ID and t_sys_flow_log.S_NODE_ID=t_sys_flow_node.I_NODE_ID and t_sys_flow_log.S_AUD_USER=t_rgxx.sygzw and t_sys_flow_log.S_FLOW_ID='" + strFlowId + "' and t_sys_flow_log.S_RUN_ID='" + strFlowRunId + "'");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                String strAudStatus = record.getFieldByName("S_AUD_STAUS").value.toString();
                if (strAudStatus.equals("1")) {
                    strAudStatus = "<font color='green'><b>\u901a\u8fc7</b></font>";
                }
                else {
                    strAudStatus = "<font color='red'></b>\u9000\u56de</b></font>";
                }
                vResult.append("<tr>");
                vResult.append("<td class='td1'>").append(record.getFieldByName("S_NODE_NAME").value).append("</td>");
                vResult.append("<td class='td1'>").append(record.getFieldByName("SYGMC").value).append("</td>");
                vResult.append("<td class='td1'>").append(record.getFieldByName("S_AUD_DATE").value).append("</td>");
                vResult.append("<td class='td1'>").append(strAudStatus).append("</td>");
                vResult.append("<td class='td1'>").append(record.getFieldByName("S_AUD_COMMENT").value).append("</td>");
                vResult.append("</tr>");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        
        vResult.append("</table>");
        return vResult;
    }
    
    private String getNextNode(final String _strNodeId, final String _strFlowId) {
        String strVresult = "";
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("*", "t_flowdetail", "SPARNODEID='" + _strNodeId + "' and SID='" + _strFlowId + "' and INODETYPE=3");
            if (tableEx.getRecordCount() > 0) {
                strVresult = tableEx.getRecord(0).getFieldByName("SROLES").value.toString();
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
    
    private StringBuffer getAuditMsg(String _strBttn, final String _strRunId, final String strFlowRunId, final HttpServletRequest request, final String strDoFlow) {
        final StringBuffer vResult = new StringBuffer();
        vResult.append("<table width='100%' class='tab2' border='0' cellpadding='0' cellspacing='0'><tr class='tr2'><td class='th2'>\u5ba1\u6279\u4eba</td><td class='th2'>\u5ba1\u6279\u6b65\u9aa4</td><td class='th2'>\u5ba1\u6279\u65f6\u95f4</td><td class='th2'>\u5ba1\u6279\u610f\u89c1</td><td class='th2'>\u5ba1\u6279\u72b6\u6001</td></tr>");
        TableEx tableEx = null;
        
        try {
            tableEx = new TableEx("t_flowhis.*,SYGMC", "T_rgxx ,t_flowhis", "t_flowhis.suser=T_rgxx.sygzw and t_flowhis.SRUNID='" + strFlowRunId + "' order by ISEQ");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                vResult.append("<tr class='tr1'><td class='td1'>" + record.getFieldByName("SYGMC").value + "</td><td class='td1'>" + (i + 1) + "</td><td class='td1'>" + record.getFieldByName("SOPDATE").value + "</td><td class='td1'>" + record.getFieldByName("SSPYJ").value);
                if (record.getFieldByName("ISTATUS").value.toString().equals("1")) {
                    vResult.append("</td><td style='color:red;' class='td1'>\u9000\u56de</td></tr>");
                }
                else {
                    vResult.append("</td><td style='color:green;' class='td1'>\u901a\u8fc7</td></tr>");
                }
            }
            vResult.append("</table>");
        }
        catch (Exception ex) {
            //do nothing
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        
        vResult.append("<table width='100%' class='tab3' border='0' cellpadding='0' cellspacing='0'><tr class='tr3'><td class='td4' width='100'>\u5ba1\u6279\u610f\u89c1:</td><td class='td5'><textarea name='t_flowhis$SSPYJ' style='width:100%;height:100;'></textarea></td></tr>");
        final String strISZDHT = request.getParameter("NO_ISZDHT");
        if (strISZDHT != null) {
            vResult.append("<tr class='tr3'><td class='td4'><br>\u6307\u5b9a\u56de\u9000\u8282\u70b9\uff1a<br></td><td align='left' class='td5'><br><select name='NO_DOSJHTJD'>" + this.getHTJD(request.getParameter("NO_sflowid"), request.getParameter("NO_backnodeid")) + "</select><br></td></tr></table>");
        }
        vResult.append("<tr><td>&nbsp;</td><td align='center'>" + _strBttn + "</td></tr></table>");
        vResult.append("<input name='t_flowhis$SRUNID' type='hidden' value='" + strFlowRunId + "'>");
        vResult.append("<input name='t_flowhis$SOPDATE' type='hidden' value='" + EString.getCurDateHH() + "'>");
        vResult.append("<input name='t_flowhis$SUSER' type='hidden' value='" + request.getSession().getAttribute("SYS_STRCURUSER") + "'>");
        vResult.append("<input name='t_flowhis$ISTATUS' type='hidden' value='0'>");
        vResult.append("<input name='t_flowhis$SNODECODE' type='hidden' value='" + request.getParameter("NO_scurnodeid") + "'>");
        vResult.append("<input name='t_flowhis$SPARNODE' type='hidden' value='" + request.getParameter("NO_sparnode") + "'>");
        vResult.append("<input name='t_flowhis$IPARSEQ' type='hidden' value='" + request.getParameter("NO_sparseq") + "'>");
        TableEx tableFQ = null;
        
        try {
            tableFQ = new TableEx("SROLE", "t_flowcontent", "SFLOWRUNID='" + strFlowRunId + "'");
            final Record record2 = tableFQ.getRecord(0);
            vResult.append("<input name='NO_FQROLE' type='hidden' value='" + record2.getFieldByName("SROLE").value + "'>");
        }
        catch (Exception ex2) {
            //do nothing
        }
        finally {
            if (tableFQ != null) {
                tableFQ.close();
            }
        }
        
        _strBttn = "<button type=\"submit\" class=\"button" + this.strStyle + "\">&nbsp;\u6d41 \u8f6c&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button  type=\"submit\" class=\"button" + this.strStyle + "\" onclick=\"add.NO_doflowIsTH.value='" + request.getParameter("NO_backnodeid") + "';" + "add.t_flowhis$ISTATUS.value='1';\">&nbsp;a\u9000 \u56de &nbsp;</button>";
        return vResult;
    }
    
    private String getHTJD(final String _strFlowId, final String _strNodes) {
        String vResult = "";
        TableEx tableEx = null;
        if (_strNodes.equals("")) {
            return "<option value='wkn3'>\u6ca1\u6709\u6307\u5b9a\u8303\u56f4\u8fd4\u56de\u53d1\u8d77\u8005</option>";
        }
        try {
            tableEx = new TableEx("SNODEID,SNODENAME", "t_flowdetail", "SID='" + _strFlowId + "' and INSTR('" + _strNodes + "',concat(SNODEID,','))<>0");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                vResult = String.valueOf(vResult) + "<option value='" + record.getFieldByName("SNODEID").value + "'>" + record.getFieldByName("SNODENAME").value + "</option>";
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
    
    private void generHC(final Hashtable hashHQRC, final StringBuffer vResult, final String _strMults) {
        final String strHcParams = hashHQRC.get("STRANS").toString();
        vResult.append("<script>function initHcParams(_strCount){var strSysParamTemp=\"\";");
        if (!strHcParams.equals("")) {
            final String[] arrHcParams = strHcParams.split(",");
            final String[] arrStrCodes = hashHQRC.get("SFIELDCODE").toString().split(",");
            for (int iLength = arrHcParams.length, i = 0; i < iLength; ++i) {
                if (!arrHcParams[i].equals("0")) {
                    if (_strMults.indexOf(arrHcParams[i]) != -1) {
                        vResult.append("strSysParamTemp+=\"&" + arrStrCodes[i] + "=\"+document.getElementById(\"" + arrStrCodes[i] + "\"+_strCount).value;");
                    }
                    else {
                        vResult.append("strSysParamTemp+=\"&" + arrStrCodes[i] + "=\"+add." + arrStrCodes[i] + ".value;");
                    }
                }
            }
        }
        vResult.append("return strSysParamTemp;}</script>");
    }
    
    public String getRule(final String _strRule, final String _strLable) {
        String vResult = " ";
        if (!_strRule.equals("0")) {
            vResult = " rule=\"" + _strRule + "\" ruleTip=\"" + _strLable + "\" placeholder=\"" + _strLable + "\" ";
        }
        return vResult;
    }
    
    public Hashtable generNoStyleInput(final Hashtable hashHQRC, final HttpServletRequest request) throws Exception {
        final Hashtable hashVResult = new Hashtable();
        this.iViewCount = 0;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strCurDate = this.sdf.format(new Date());
        this.sbScript = new StringBuffer("<script>function changeSelData(_strItemName){switch(_strItemName){");
        this.strInitScript = "";
        final String[] arrcode = this.getFilterData(hashHQRC.get("SFIELDCODE").toString(), request).split(",");
        final String[] arrname = hashHQRC.get("SFIELDNAME").toString().split(",");
        final String[] arrSelPage = hashHQRC.get("SQUERYFIELD").toString().split(",");
        final String[] arrSelPageSize = hashHQRC.get("SSIZE").toString().split(":");
        final String[] arrReturn = hashHQRC.get("SGLFIELD").toString().split(",");
        final String[] arrType = this.getFilterData(hashHQRC.get("SDELCON").toString(), request).split(",");
        final String[] arrFieldsSize = hashHQRC.get("SFIELDSIZE").toString().split(",");
        final String[] arrStrRules = hashHQRC.get("SHREFIELD").toString().split(",");
        final Object objTableOpType = hashHQRC.get("SQLFIELD");
        final String strCon = hashHQRC.get("SEDITPAGE").toString();
        if (!strCon.equals("") && !strCon.equals("*")) {
            this.initCon(strCon, request);
        }
        else {
            this.bIsUpdate = false;
        }
        final Object objIsBatch = hashHQRC.get("SFREE1");
        final int iCount = arrcode.length;
        this.iCols = 3;
        for (int i = 0; i < iCount; ++i) {
            final boolean bIsViewTd = true;
            final String strTrStyle = "";
            final String strLable = arrname[i];
            final String strLableTd = "";
            final String strDataTd = "";
            final String strTrEnd = "";
            final String strTdEnd = "";
            final String strRule = this.getRule(arrStrRules[i], strLable);
            hashVResult.put(arrcode[i], this.genSingField(arrcode[i], arrType[i], arrFieldsSize[i], request, arrSelPage[i], arrSelPageSize[i], arrReturn[i], strCurDate, strRule, bIsViewTd, strTrStyle, strLable, strLableTd, strDataTd, strTrEnd, strTdEnd));
        }
        this.sbScript.append("}}" + this.strInitScript + "</script>");
        this.generSaveScript(hashHQRC, objTableOpType, "");
        hashVResult.put("sys_script", this.sbScript);
        return hashVResult;
    }
    
    private void generSaveScript(final Hashtable hashHQRC, final Object objTableOpType, final String _strMutls) {
        String strOptFunc = "init(add)";
        if (objTableOpType != null && !objTableOpType.toString().equals("")) {
            final String strOpType = objTableOpType.toString();
            if (!strOpType.equals("*")) {
                strOptFunc = "initOther(add,'" + strOpType + "')";
            }
        }
        this.generHC(hashHQRC, this.sbScript, _strMutls);
        if (this.bIsUpdate) {
            this.sbScript.append("<script language=\"javascript\">" + strOptFunc + ";</script>");
        }
        else {
            this.sbScript.append("<script language=\"javascript\">" + strOptFunc + ";</script>");
        }
    }
    
    private void generSaveScript(final Hashtable hashHQRC, final Object objTableOpType, final String _strMutls, final String _strPageCode) {
        this.generHC(hashHQRC, this.sbScript, _strMutls);
        this.sbScript.append("<script language=\"javascript\">initBatch(add,'" + _strPageCode + "','" + objTableOpType + "');</script>");
    }
    
    public StringBuffer generInputModField(final int iCount, final String[] arrcode, final String[] arrname, final String[] arrType, final String[] arrFieldsSize, final HttpServletRequest request, final String[] arrSelPage, final String[] arrSelPageSize, final String[] arrReturn, final String _strModStyle, final String[] _arrStrRule) throws Exception {
        this.iViewCount = 0;
        final StringBuffer sbBatchData = new StringBuffer();
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strCurDate = this.sdf.format(new Date());
        this.sbScript = new StringBuffer("<script>function changeSelData(_strItemName){switch(_strItemName){");
        this.strInitScript = "";
        final String[] strArrStyle = (String[]) QRSC.HASHSTYLE.get(_strModStyle);
        final int iStyeCount = strArrStyle.length;
        this.iCols = 3;
        for (int i = 0; i < iCount; ++i) {
            final boolean bIsViewTd = true;
            final String strTrStyle = "";
            final String strLable = arrname[i];
            final String strLableTd = "";
            final String strDataTd = "";
            final String strTrEnd = "";
            final String strTdEnd = "";
            final String strRule = this.getRule(_arrStrRule[i], strLable);
            if (i < iStyeCount) {
                sbBatchData.append(strArrStyle[i]);
            }
            sbBatchData.append(this.genSingField(arrcode[i], arrType[i], arrFieldsSize[i], request, arrSelPage[i], arrSelPageSize[i], arrReturn[i], strCurDate, strRule, bIsViewTd, strTrStyle, strLable, strLableTd, strDataTd, strTrEnd, strTdEnd));
        }
        if (iStyeCount > iCount) {
            sbBatchData.append(strArrStyle[iCount]);
        }
        this.sbScript.append("}}" + this.strInitScript + "</script>");
        sbBatchData.append(this.sbScript);
        return sbBatchData;
    }
    
    public String[] generInputField_View(final Hashtable hashHQRC, final HttpServletRequest request) throws Exception {
        if (request.getParameter("sys_bed") != null) {
            this.bIsEditPage = true;
            final String strCon = hashHQRC.get("SEDITPAGE").toString();
            if (!strCon.equals("") && !strCon.equals("*")) {
                this.initCon(strCon, request);
                (this.sbUpdateConfig = new StringBuffer()).append(this.strUpdateInput);
                this.sbUpdateConfig.append("<input name='NO_OPTYPE' type='hidden' value='1'>");
                this.sbUpdateConfig.append("<textarea name='NO_CON' style='display:none'>" + this.strUpdateField + "</textarea>");
            }
            else {
                this.bIsUpdate = false;
            }
        }
        final String[] arrcode = this.getFilterData(hashHQRC.get("SFIELDCODE").toString(), request).split(",");
        final String[] arrname = hashHQRC.get("SFIELDNAME").toString().replaceAll(" ", "&nbsp;").split(",");
        final String[] arrSelPage = hashHQRC.get("SQUERYFIELD").toString().split(",");
        final String[] arrSelPageSize = hashHQRC.get("SSIZE").toString().split(":");
        final String[] arrReturn = hashHQRC.get("SGLFIELD").toString().split(",");
        final String[] arrType = this.getFilterData(hashHQRC.get("SDELCON").toString(), request).split(",");
        final String[] arrFieldsSize = hashHQRC.get("SFIELDSIZE").toString().split(",");
        final String[] arrStrRules = hashHQRC.get("SHREFIELD").toString().split(",");
        final int iCount = arrcode.length;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strCurDate = this.sdf.format(new Date());
        this.sbScript = new StringBuffer("<script>function changeSelData(_strItemName){switch(_strItemName){");
        this.strInitScript = "";
        final String[] arrInputFields = new String[iCount];
        this.iCols = 3;
        for (int i = 0; i < iCount; ++i) {
            final boolean bIsViewTd = true;
            final String strTrStyle = "";
            final String strLable = "";
            final String strDataTd = "";
            final String strTrEnd = "";
            final String strTdEnd = "";
            final String strRule = this.getRule(arrStrRules[i], arrname[i]);
            final String strLableTd = "";
            arrInputFields[i] = this.genSingField(arrcode[i], arrType[i], arrFieldsSize[i], request, arrSelPage[i], arrSelPageSize[i], arrReturn[i], strCurDate, strRule, bIsViewTd, strTrStyle, strLable, strLableTd, strDataTd, strTrEnd, strTdEnd);
        }
        this.sbScript.append("}}" + this.strInitScript + "</script>");
        return arrInputFields;
    }
    
    public StringBuffer generInputField(final int iCount, final String[] arrcode, final String[] arrname, final String[] arrType, final String[] arrFieldsSize, final HttpServletRequest request, final String[] arrSelPage, final String[] arrSelPageSize, final String[] arrReturn, final String[] _arrStrRule) throws Exception {
        this.iViewCount = 0;
        final StringBuffer sbBatchData = new StringBuffer();
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strCurDate = this.sdf.format(new Date());
        this.sbScript = new StringBuffer("<script>function changeSelData(_strItemName){switch(_strItemName){");
        this.strInitScript = "";
        this.bIsGetBatchUpdate = false;
        if (this.iCols == -1) {
            final StringBuffer sbTrHead = new StringBuffer("\r\n<tr style='cursor:pointer;'>");
            final StringBuffer sbTrInput = new StringBuffer();
            String strAddRowHtml = "<a href=\"javascript:SYS_ADD_BATCH_ROW('" + this.strChildTableId + "');\" style='float:right;margin-right:5px;line-height:33px;color:#539ddd;font-weight:bold;font-size:20px;'>\uff0b</a><span onclick=\"SYS_CHANGE_BATCH_TAB();\" style='line-height:33px;color:#539ddd;font-weight:bold;font-size:20px;cursor:pointer;float:right;margin-right:5px;'>\u25a1</span>";
            final String strInsertRowHtml = "<span onclick=\"SYS_INSERT_BATCH_ROW(this,'" + this.strChildTableId + "');\" style='line-height:33px;color:#539ddd;font-weight:bold;font-size:20px;cursor:pointer;'> \uff0b </span>";
            final String strDelRowHtml = "<span onclick=\"SYS_DEL_BATCH_ROW(this,'" + this.strChildTableId + "');\" style='line-height:33px;color:#539ddd;font-weight:bold;font-size:20px;cursor:pointer;'> x </span>";
            final String[][] arrStringInputParams = new String[iCount][8];
            final HashMap mapTableTemp = new HashMap();
            this.strTreeSelectId = "";
            this.vecUploadIds = new Vector();
            for (int i = 0; i < iCount; ++i) {
                final boolean bIsViewTd = true;
                final String strLable = arrname[i];
                String _strRule = _arrStrRule[i];
                if (_strRule.startsWith("_")) {
                    _strRule = _strRule.substring(1);
                }
                String strRule = this.getRule(_strRule, strLable);
                final String strTrStyle = "";
                final String strLableTd = "";
                final String strCode = arrcode[i];
                String strType = arrType[i];
                if (this.hashBatchKeyMsg != null) {
                    final Object objKeyFather = this.hashBatchKeyMsg.get(strCode);
                    if (objKeyFather != null) {
                        strType = "4$GET_" + objKeyFather;
                    }
                }
                String strHeadTd = strLable;
                if (!strRule.trim().equals("")) {
                    strHeadTd = "<label class='tdrequired'>*</label>" + strLable;
                }
                if (strType.startsWith("CD")) {
                    strHeadTd = "<label class='tdrequired'><input type='checkbox' class='ylradio'style='vertical-align:middle;' onclick=\"_formCheckBoxClick('NO_check" + strType.split("\\$")[1] + "');\"></label>" + strLable;
                }
                if (_arrStrRule[i].startsWith("_")) {
                    strRule = "_" + strRule;
                }
                String strHeadStyle = "";
                if (!strType.startsWith("4")) {
                    strHeadTd = String.valueOf(strHeadTd) + strAddRowHtml;
                    strAddRowHtml = "";
                }
                else {
                    strHeadStyle = " style='display:none;' ";
                }
                final String strDataTd = "<td class=\"inputtd" + this.strStyle + "\">";
                final String strTrEnd = "";
                final String strTdEnd = "</td>";
                sbTrHead.append("<td class='th1'" + strHeadStyle + " style='white-space: nowrap;'>").append(strHeadTd).append(strTdEnd);
                if (this.bIsUpdate) {
                    mapTableTemp.put(strCode.split("\\$")[0], "");
                    arrStringInputParams[i][0] = strCode;
                    arrStringInputParams[i][1] = strType;
                    arrStringInputParams[i][2] = arrFieldsSize[i];
                    arrStringInputParams[i][3] = arrSelPage[i];
                    arrStringInputParams[i][4] = arrReturn[i];
                    arrStringInputParams[i][5] = strRule;
                    arrStringInputParams[i][6] = strLable;
                    arrStringInputParams[i][7] = arrSelPageSize[i];
                    this.bIsUpdate = false;
                    sbTrInput.append(this.genSingField(strCode, strType, arrFieldsSize[i], request, arrSelPage[i], arrSelPageSize[i], arrReturn[i], strCurDate, strRule, bIsViewTd, strTrStyle, strLable, strLableTd, strDataTd, strTrEnd, strTdEnd));
                    this.bIsUpdate = true;
                }
                else {
                    sbTrInput.append(this.genSingField(strCode, strType, arrFieldsSize[i], request, arrSelPage[i], arrSelPageSize[i], arrReturn[i], strCurDate, strRule, bIsViewTd, strTrStyle, strLable, strLableTd, strDataTd, strTrEnd, strTdEnd));
                }
            }
            String strInitSelTreeId = "<script>var arrInitSelTreeId" + this.strChildTableId + "=[];</script>";
            if (!this.strTreeSelectId.equals("")) {
                this.strTreeSelectId = this.strTreeSelectId.substring(1);
                strInitSelTreeId = "<script>var arrInitSelTreeId" + this.strChildTableId + "=['" + this.strTreeSelectId.replaceAll(",", "','") + "'];</script>";
            }
            sbTrHead.append("<td class='th1' style='white-space: nowrap;'>\u63d2\u5165</td><td class='th1' style='white-space: nowrap;'>\u5220\u9664</td></tr>");
            sbTrHead.append("<script>var arrUploadIds=[];");
            for (int iUploadIsLength = this.vecUploadIds.size(), k = 0; k < iUploadIsLength; ++k) {
                sbTrHead.append("arrUploadIds[" + k + "]='" + this.vecUploadIds.get(k) + "';");
            }
            sbTrHead.append("</script>");
            sbTrInput.append("<td align='center' style='width:50px;'>").append(strInsertRowHtml).append("</td><td align='center' style='width:50px;'>").append(strDelRowHtml).append("</td>");
            final String strAddRowValueId = "NO_SYS_BATCH_ROW" + this.strChildTableId;
            if (this.bIsUpdate) {
                final String strDataTd2 = "<td class=\"inputtd" + this.strStyle + "\">";
                final Set setTableCode = mapTableTemp.entrySet();
                final TableEx tableExTemp = (TableEx) this.hashMutTableCon.get(arrStringInputParams[0][0].split("\\$")[0]);
                final int iRecordCount = tableExTemp.getRecordCount();
                final StringBuffer sbTrUpdateInput = new StringBuffer();
                for (int j = 0; j < iRecordCount; ++j) {
                    for (final Map.Entry entry : (Set<Map.Entry>) setTableCode) {
                        final String strTbCode = entry.getKey().toString();
                        final TableEx tableEx = (TableEx) this.hashMutTableCon.get(strTbCode);
                        this.hashTableCon.put(strTbCode, tableEx.getRecord(j));
                    }
                    sbTrUpdateInput.append("<tr onmouseover=\"this.style.background='#f1f1f1';\" onmouseout=\"this.style.background='#ffffff';\">");
                    this.bIsGetBatchUpdate = true;
                    for (int l = 0; l < iCount; ++l) {
                        sbTrUpdateInput.append(this.genSingField(arrStringInputParams[l][0], arrStringInputParams[l][1], arrStringInputParams[l][2], request, arrStringInputParams[l][3], arrStringInputParams[l][7], arrStringInputParams[l][4], strCurDate, arrStringInputParams[l][5], true, "", arrStringInputParams[l][6], "", strDataTd2, "", "</td>"));
                    }
                    this.bIsGetBatchUpdate = false;
                    sbTrUpdateInput.append("<td align='center' style='width:50px;'>").append(strInsertRowHtml).append("</td><td align='center' style='width:50px;'>").append(strDelRowHtml).append("</td>");
                    sbTrUpdateInput.append("</tr>");
                }
                sbBatchData.append(sbTrHead).append(sbTrUpdateInput).append("<textarea id='" + strAddRowValueId + "' style='display:none;'>").append(sbTrInput.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("</textarea>");
                for (final Map.Entry entry2 : (Set<Map.Entry>) setTableCode) {
                    final String strTbCode2 = entry2.getKey().toString();
                    final TableEx tableEx2 = (TableEx) this.hashMutTableCon.get(strTbCode2);
                    tableEx2.close();
                }
            }
            else {
                sbBatchData.append(sbTrHead).append("<tr onmouseover=\"this.style.background='#f1f1f1';\" onmouseout=\"this.style.background='#ffffff';\">").append(sbTrInput).append("</tr>").append("<textarea id='" + strAddRowValueId + "' style='display:none;'>").append(sbTrInput.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("</textarea>");
            }
            sbBatchData.append(strInitSelTreeId);
        }
        else {
            for (int m = 0; m < iCount; ++m) {
                final boolean bIsViewTd2 = true;
                String strTrStyle2 = "\r    <tr class=\"inputtr" + this.strStyle + "\">";
                final String strLable2 = arrname[m];
                String strDataTd3 = "<td class=\"inputtd" + this.strStyle + "\" style='width:" + arrFieldsSize[m] + "px;'>";
                String strTrEnd2 = "</tr>";
                final String strTdEnd2 = "</td>";
                String _strRule2 = _arrStrRule[m];
                if (_strRule2.startsWith("_")) {
                    _strRule2 = _strRule2.substring(1);
                }
                String strRule2 = this.getRule(_strRule2, strLable2);
                final String strFieldCode = arrcode[m];
                boolean bIsSplitMsg = false;
                boolean bIsSplitPag = false;
                if (strFieldCode.equals("$SPLIT")) {
                    bIsSplitMsg = true;
                    strDataTd3 = "<td class=\"tdsplit\">";
                }
                if (strFieldCode.startsWith("$PAG")) {
                    bIsSplitPag = true;
                    strDataTd3 = "<td class=\"tdsplit\">";
                }
                String strLableTd2 = "<td class=\"inputth" + this.strStyle + "\" width='100'>" + strLable2 + ":</td>";
                if (!strRule2.trim().equals("")) {
                    strLableTd2 = "<td class=\"inputth" + this.strStyle + "\" width='100'><label class='tdrequired'>*</label>" + strLable2 + ":</td>";
                }
                if (_arrStrRule[m].startsWith("_")) {
                    strRule2 = "_" + strRule2;
                }
                if (bIsSplitMsg) {
                    strLableTd2 = "<td class=\"thsplit\" width='100'>" + strLable2 + "</td>";
                }
                if (bIsSplitPag) {
                    strLableTd2 = "";
                }
                if (this.iCols != 1) {
                    strTrEnd2 = "";
                    final String strFiledType = arrType[m];
                    final String strFieldWidth = arrFieldsSize[m];
                    if (strFiledType.startsWith("8") || strFiledType.startsWith("9") || strFiledType.startsWith("PIC") || strFiledType.startsWith("MUP") || strFiledType.startsWith("MAP") || strFieldWidth.equals("0") || bIsSplitMsg || bIsSplitPag || strFiledType.startsWith("CD")) {
                        if (bIsSplitMsg) {
                            strDataTd3 = "<td class='thsplit' align='left' colspan='" + this.iCols * 2 + "'>";
                        }
                        else if (bIsSplitPag) {
                            strDataTd3 = "<td class='tdsplit' align='left' colspan='" + this.iCols * 2 + "'>";
                        }
                        else {
                            strDataTd3 = "<td class='inputtd" + this.strStyle + "' colspan='" + (this.iCols * 2 - 1) + "'>";
                        }
                        int iOtherCols = 0;
                        String strOtherCols = "";
                        if (this.iViewCount != 0) {
                            iOtherCols = this.iViewCount % this.iCols;
                            if (iOtherCols != 0) {
                                iOtherCols = this.iCols - iOtherCols;
                                strOtherCols = "<td colspan='" + iOtherCols * 2 + "' class='tdother'>&nbsp;</td>";
                            }
                        }
                        if (m != 0) {
                            strTrStyle2 = String.valueOf(strOtherCols) + "\r    </tr><tr class=\"inputtr" + this.strStyle + "\">";
                        }
                        else {
                            strTrStyle2 = "\r    <tr class=\"inputtr" + this.strStyle + "\">";
                        }
                        this.iViewCount += this.iCols + iOtherCols;
                    }
                    else if (this.iViewCount % this.iCols == 0) {
                        if (m != 0) {
                            strTrStyle2 = "\r    </tr><tr class=\"inputtr" + this.strStyle + "\">";
                        }
                        else {
                            strTrStyle2 = "\r    <tr class=\"inputtr" + this.strStyle + "\">";
                        }
                        ++this.iViewCount;
                    }
                    else {
                        strTrStyle2 = "";
                        ++this.iViewCount;
                    }
                }
                if (strFieldCode.equals("$BTTN")) {
                    sbBatchData.append("<td colspan='2' align='center'><button style='width:0px;display:none;' id='bttnsysaddsubmit' type='submit'></button><div style='float:left;padding-left:15px;'>" + this.strGlobQDBttn + "</div></td>");
                }
                else if (bIsSplitMsg) {
                    sbBatchData.append(strTrStyle2).append(strDataTd3).append("<table style='width:100%;'><tr><td width='200' style='white-space: nowrap;'>" + strLable2 + "</td><td><hr class='hrsplit'></td></tr></table>").append(strTdEnd2).append(strTrEnd2);
                }
                else if (bIsSplitPag) {
                    final String strViewParam = this.getFilterData(strFieldCode.substring(4), request);
                    sbBatchData.append(strTrStyle2).append(strLableTd2).append(strDataTd3).append("<iframe src='View?SPAGECODE=" + strViewParam + "'  width='100%' height='" + arrFieldsSize[m] + "px' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe>").append(strTdEnd2).append(strTrEnd2);
                }
                else {
                    sbBatchData.append(this.genSingField(arrcode[m], arrType[m], arrFieldsSize[m], request, arrSelPage[m], arrSelPageSize[m], arrReturn[m], strCurDate, strRule2, bIsViewTd2, strTrStyle2, strLable2, strLableTd2, strDataTd3, strTrEnd2, strTdEnd2));
                }
            }
        }
        this.sbScript.append("}}" + this.strInitScript + "</script>");
        sbBatchData.append(this.sbScript);
        return sbBatchData;
    }
    
    private String genSingField(final String _strCode, final String _strType, String _strFieldsSize, final HttpServletRequest request, final String _strSelPage, final String _strSelSize, final String _strReturn, String strCurDate, String strRule, boolean bIsViewTd, String strTrStyle, final String strLable, String strLableTd, final String strDataTd, final String strTrEnd, final String strTdEnd) throws Exception {
        boolean bIsDoUpdate = true;
        if (this.bIsCopy && this.mapNoCopyField.get(_strCode) != null) {
            bIsDoUpdate = false;
        }
        boolean bIsEdit = this.bIsViewEdit;
        if (strRule.startsWith("_")) {
            strRule = strRule.substring(1);
            bIsEdit = true;
        }
        final String strTempFieldSize = String.valueOf(_strFieldsSize) + "px";
        _strFieldsSize = "100%;min-width:" + strTempFieldSize;
        String strvResult = "";
        boolean bIsBatch = false;
        String strSelfCode = "";
        if (strTrStyle.equals("$")) {
            bIsBatch = true;
            strSelfCode = strLableTd;
            strTrStyle = "";
            strLableTd = "";
        }
        String strType = " type=\"text\" ";
        if (this.strSetFocusScript.equals("") && (_strType.startsWith("0") || _strType.startsWith("8"))) {
            this.strSetFocusScript = "<script>add." + _strCode + ".focus();</script>";
        }
        if (_strType.startsWith("1")) {
            if (!bIsBatch) {
                strType = " readonly value=\"" + EString.generId() + "\"  type=\"text\" ";
            }
            else {
                strType = " readonly value=\"" + EString.generId() + "_'+_icount+'\"  type=\"text\" ";
            }
        }
        else if (_strType.startsWith("2")) {
            final String[] arrTF = _strType.substring(1).split("\\$");
            if (arrTF.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u9012\u589e\u7c7b\u578b\u672a\u53d1\u73b0\u76f8\u5173\u8868\u548c\u5b57\u6bb5\uff01");
            }
            strType = " readonly value=\"" + EString.generId(arrTF[0], arrTF[1]) + "\"  type=\"text\" ";
        }
        else if (_strType.startsWith("3")) {
            final String[] arrTF = _strType.split("\\$");
            if (arrTF.length > 1) {
                strType = " class=\"inputreadonlly\" readonly type=\"text\" value=\"" + arrTF[1] + "\" ";
            }
            else {
                strType = " class=\"inputreadonlly\" readonly value=\"\"  type=\"text\" ";
            }
        }
        else if (_strType.startsWith("4")) {
            final String[] arrTF = _strType.split("\\$");
            if (arrTF.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u8be5\u53c2\u6570\u672a\u53d1\u73b0\u9ed8\u8ba4\u503c\uff01");
            }
            String strHidValue = arrTF[1].trim();
            if (strHidValue.startsWith("TNGLOB_")) {
                final String[] arrStrTbMsg = _strCode.split("\\$");
                final DBFactory dbfField = new DBFactory();
                strHidValue = "TNGLOB_" + arrTF[4] + "$" + dbfField.getPcodeName(arrTF[2], arrStrTbMsg[0], arrStrTbMsg[1], arrTF[3]);
            }
            else if (strHidValue.startsWith("GUID_")) {
                final String strStartFlag = strHidValue.substring(5);
                if (!bIsBatch) {
                    strHidValue = String.valueOf(strStartFlag) + EString.generId();
                }
                else {
                    strHidValue = String.valueOf(strStartFlag) + EString.generId() + "_'+_icount+'";
                }
            }
            else if (strHidValue.startsWith("INCRE_")) {
                strHidValue = EString.generId();
            }
            else if (strHidValue.startsWith("TIME_")) {
                strHidValue = EString.getCurDate_SS();
            }
            else if (strHidValue.startsWith("INCREMOD_")) {
                strHidValue = _strType;
            }
            else if (strHidValue.startsWith("GET_")) {
                strHidValue = String.valueOf(strHidValue) + "$" + arrTF[2].trim();
            }
            else if (strHidValue.startsWith("PY_") || strHidValue.startsWith("NPY_")) {
                strHidValue = String.valueOf(strHidValue) + "$" + arrTF[2].trim();
            }
            else if (strHidValue.startsWith("RE_")) {
                final String strReValue = request.getParameter(strHidValue.substring(3));
                if (strReValue != null) {
                    strHidValue = EString.encoderStr(strReValue);
                }
                else {
                    final Object objTempValue = request.getSession().getAttribute(strHidValue.substring(3));
                    if (objTempValue == null) {
                        this.strErrMsg = "\u5728request,session\u91cc\u90fd\u672a\u627e\u5230\u53c2\u6570[" + strHidValue.substring(3) + "]";
                        strHidValue = "";
                    }
                    else {
                        strHidValue = objTempValue.toString();
                    }
                }
            }
            strType = " readonly value=\"" + strHidValue + "\" type=\"hidden\" ";
            if (this.iCols == 1 && !bIsBatch) {
                strTrStyle = "\r    <tr style=\"display:none;\" class=\"inputtr" + this.strStyle + "\">";
            }
            else {
                strTrStyle = "";
                bIsViewTd = false;
                --this.iViewCount;
            }
        }
        else if (_strType.startsWith("5")) {
            final String[] arrTF = _strType.split("\\$");
            if (arrTF.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u8be5\u53c2\u6570\u672a\u53d1\u73b0\u9ed8\u8ba4\u503c\uff01");
            }
            String strHidValue = arrTF[1].trim();
            if (strHidValue.startsWith("RE_")) {
                final String strReValue = request.getParameter(strHidValue.substring(3));
                if (strReValue == null) {
                    strHidValue = new StringBuilder().append(request.getSession().getAttribute(strHidValue.substring(3))).toString();
                }
                else {
                    strHidValue = EString.encoderStr(strReValue);
                }
            }
            else if (strHidValue.startsWith("GET_")) {
                strHidValue = String.valueOf(strHidValue) + "$" + arrTF[2].trim();
            }
            strType = " readonly value=\"" + strHidValue + "\" type=\"text\" ";
        }
        else if (_strType.startsWith("6")) {
            String strClick = "onClick=\"WdatePicker()\" ";
            if (!bIsEdit) {
                strClick = "style='background:#f1f1f1;' ";
            }
            if (this.bIsUpdate && bIsDoUpdate) {
                strType = " readonly value=\"" + this.getEditValue(_strCode) + "\" type=\"text\" class='Wdate' " + strClick;
            }
            else {
                strType = " readonly value=\"" + strCurDate + "\" type='text' class='Wdate' " + strClick;
            }
        }
        else if (_strType.startsWith("HH_")) {
            String strClick = "onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH'})\" ";
            if (!bIsEdit) {
                strClick = "style='background:#f1f1f1;' ";
            }
            if (this.bIsUpdate && bIsDoUpdate) {
                strType = " readonly value=\"" + this.getEditValue(_strCode) + "\" type=\"text\" class='Wdate' " + strClick;
            }
            else {
                strCurDate = EString.getCurDateHh();
                strType = " readonly value=\"" + strCurDate + "\" type='text' class='Wdate' " + strClick;
            }
        }
        else if (_strType.startsWith("time_")) {
            String strClick = "onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})\" ";
            if (!bIsEdit) {
                strClick = "style='background:#f1f1f1;' ";
            }
            if (this.bIsUpdate && bIsDoUpdate) {
                strType = " readonly value=\"" + this.getEditValue(_strCode) + "\" type=\"text\" class='Wdate' " + strClick;
            }
            else {
                strCurDate = EString.getCurDateHH();
                strType = " readonly value=\"" + strCurDate + "\" type='text' class='Wdate' " + strClick;
            }
        }
        else if (_strType.startsWith("7")) {
            strType = " readonly value=\"\" type=\"text\" ";
        }
        if (_strType.startsWith("D")) {
            String strValue = "";
            String strOldValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strOldValue = this.getEditValue(_strCode);
                strValue = " value=\"" + strOldValue + "\" ";
            }
            final String[] arrTF2 = _strType.split("\\$");
            if (arrTF2.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u5b57\u5178\u7c7b\u578b\u672a\u53d1\u73b0\u7ed1\u5b9a\u76f8\u5173\u5b57\u5178\uff01");
            }
            StringBuffer sbDicSelect = new StringBuffer(String.valueOf(strTrStyle) + strLableTd + strDataTd + "<select onchange=\"changeSelData('" + _strCode + "')\"  " + strValue + strRule + " name='" + _strCode + "' id='" + _strCode + "'  style=\"width:" + _strFieldsSize + ";\" >");
            if (!bIsEdit) {
                sbDicSelect = new StringBuffer(String.valueOf(strTrStyle) + strLableTd + strDataTd + "<select  onfocus=\"this.defaultIndex=this.selectedIndex;\" onchange=\"this.selectedIndex=this.defaultIndex;\" " + strValue + strRule + " name='" + _strCode + "' id='" + _strCode + "'  style=\"width:" + _strFieldsSize + ";background:#f1f1f1;\" >");
            }
            final ArrayList aL = Dic.hashDicCode.get(arrTF2[1]);
            final ArrayList aLName = Dic.hashDicName.get(arrTF2[1]);
            final int iDicCount = aL.size();
            String strSelect = "";
            sbDicSelect.append("<option value=''>--\u8bf7\u9009\u62e9--</option>");
            for (int d = 0; d < iDicCount; ++d) {
                final String strCode = aL.get(d).toString();
                if (strOldValue.equals(strCode)) {
                    strSelect = " selected ";
                }
                else {
                    strSelect = "";
                }
                sbDicSelect.append("<option").append(strSelect).append(" value='").append(strCode).append("'>").append(aLName.get(d)).append("</option>");
            }
            sbDicSelect.append("</select>");
            strvResult = String.valueOf(strvResult) + sbDicSelect.toString();
        }
        else if (_strType.startsWith("CD")) {
            String strValue = "";
            String strOldValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strOldValue = this.getEditValue(_strCode);
                strValue = " value=\"" + strOldValue + "\" ";
            }
            String strCD = String.valueOf(strTrStyle) + strLableTd + strDataTd + "<input type='hidden' " + strValue + strRule + " name='" + _strCode + "' id='" + _strCode + "'  style=\"width:" + _strFieldsSize + ";\" ";
            if (!bIsEdit) {
                strCD = String.valueOf(strTrStyle) + strLableTd + strDataTd + "<input type='hidden' " + strValue + strRule + " name='" + _strCode + "' id='" + _strCode + "'  style=\"width:" + _strFieldsSize + ";background:#f1f1f1;\" ";
            }
            final String[] arrTF3 = _strType.split("\\$");
            if (arrTF3.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u5b57\u5178\u7c7b\u578b\u672a\u53d1\u73b0\u7ed1\u5b9a\u76f8\u5173\u5b57\u5178\uff01");
            }
            if (bIsEdit) {
                strvResult = String.valueOf(strvResult) + (Object)Dic.getCheck(arrTF3[1], strOldValue, " onclick=\"sys_doCheckClick(this,'" + arrTF3[1] + "','" + _strCode + "');\" ", strCD);
            }
            else {
                strvResult = String.valueOf(strvResult) + (Object)Dic.getCheck(arrTF3[1], strOldValue, " onclick=\"return false;\" ", strCD);
            }
        }
        else if (_strType.startsWith("RD")) {
            String strValue = "";
            String strOldValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strOldValue = this.getEditValue(_strCode);
                strValue = " value=\"" + strOldValue + "\" ";
            }
            String strRD = String.valueOf(strTrStyle) + strLableTd + strDataTd + "<input type='hidden' " + strValue + strRule + " name='" + _strCode + "' id='" + _strCode + "'  style=\"width:" + _strFieldsSize + ";\" ";
            if (!bIsEdit) {
                strRD = String.valueOf(strTrStyle) + strLableTd + strDataTd + "<input type='hidden' " + strValue + strRule + " name='" + _strCode + "' id='" + _strCode + "'  style=\"width:" + _strFieldsSize + ";background:#f1f1f1;\" ";
            }
            final String[] arrTF3 = _strType.split("\\$");
            if (arrTF3.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u5b57\u5178\u7c7b\u578b\u672a\u53d1\u73b0\u7ed1\u5b9a\u76f8\u5173\u5b57\u5178\uff01");
            }
            if (bIsEdit) {
                strvResult = String.valueOf(strvResult) + (Object)Dic.getRadio(arrTF3[1], strOldValue, " onclick=\"sys_doRadioClick(this,'" + arrTF3[1] + "','" + _strCode + "');\" ", strRD);
            }
            else {
                strvResult = String.valueOf(strvResult) + (Object)Dic.getRadio(arrTF3[1], strOldValue, " onclick=\"return false;\" ", strRD);
            }
        }
        else if (_strType.startsWith("P$")) {
            String strValue = "";
            String strOldValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strOldValue = this.getEditValue(_strCode);
                strValue = " value=\"" + strOldValue + "\" ";
            }
            final String[] arrTF2 = _strType.split("\\$");
            if (arrTF2.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u672a\u53d1\u73b0\u7ed1\u5b9a\u76f8\u5173\u53c2\u6570\uff01");
            }
            this.fItem.strViewName = _strCode;
            this.fItem.strViewValue = strOldValue;
            this.fItem.request = request;
            this.fItem.bIsGetBatchUpdate = this.bIsGetBatchUpdate;
            String strReadOnlly = "";
            String strBackGround = "";
            if (!bIsEdit) {
                strReadOnlly = " readOnly='true' ";
                strBackGround = ";background:#f1f1f1";
            }
            if (!bIsBatch) {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + (Object)this.fItem.generFormParam(arrTF2[1], String.valueOf(strValue) + " name=\"" + _strCode + "\"  id=\"" + _strCode + "\" style=\"width:" + _strFieldsSize + strBackGround + ";\" " + strRule + " " + strReadOnlly);
            }
            else {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + (Object)this.fItem.generFormParam(arrTF2[1], String.valueOf(strValue) + " name=\"" + _strCode + "\"  id=\"" + _strCode + "\"  style=\"width:" + _strFieldsSize + strBackGround + ";\" " + strReadOnlly);
            }
            if (this.fItem.bIsTreeSelect) {
                this.strTreeSelectId = String.valueOf(this.strTreeSelectId) + "," + _strCode;
            }
        }
        else if (_strType.startsWith("PM$")) {
            String strValue = "";
            String strOldValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strOldValue = this.getEditValue(_strCode);
                strValue = " value=\"" + strOldValue + "\" ";
            }
            final String[] arrTF2 = _strType.split("\\$");
            if (arrTF2.length < 2) {
                throw new Exception("-->[" + strLable + "]\u5b57\u6bb5\u7c7b\u578b\u53c2\u6570\u8bbe\u7f6e\u9519\u8bef\uff01\u672a\u53d1\u73b0\u7ed1\u5b9a\u76f8\u5173\u53c2\u6570\uff01");
            }
            this.fItem.strViewName = _strCode;
            this.fItem.strViewValue = strOldValue;
            this.fItem.request = request;
            if (!bIsBatch) {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + (Object)this.fItem.generFormParam(arrTF2[1], String.valueOf(strValue) + " name=\"" + _strCode + "\"  id=\"" + _strCode + "\"  bismut=true  style=\"width:" + _strFieldsSize + ";\" " + strRule + " ");
            }
            else {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + (Object)this.fItem.generFormParam(arrTF2[1], String.valueOf(strValue) + " name=\"" + _strCode + "\"  id=\"" + _strCode + "\"  style=\"width:" + _strFieldsSize + ";\" ");
            }
        }
        else if (_strType.startsWith("R")) {
            String strValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strValue = " value=\"" + this.getEditValue(_strCode) + "\" ";
            }
            strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<select onchange=\"changeSelData(this)\" " + strValue + strRule + " id=\"" + _strCode + "\" " + " name=\"" + _strCode + "\" style=\"width:" + _strFieldsSize + ";\">";
            strvResult = String.valueOf(strvResult) + "</select>";
        }
        else if (_strType.startsWith("9")) {
            String strValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strValue = this.getEditValue(_strCode).toString();
            }
            strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<textarea id=\"" + _strCode + "\" " + strRule + " name=\"" + _strCode + "\"  style=\"height:" + strTempFieldSize + ";width:100%;\">" + strValue + "</textarea>";
            this.bIsFuText = true;
            this.sbFuText.append("var ue" + _strCode + " = UE.getEditor(\"" + _strCode + "\");");
        }
        else if (_strType.startsWith("MAP")) {
            String strValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strValue = this.getEditValue(_strCode).toString();
            }
            strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<div id=\"divmap" + _strCode + "\" " + strRule + "  style=\"margin:10px 0px;height:" + strTempFieldSize + ";width:100%;\">" + strValue + "</div><input type='hidden' " + strRule + " id='" + _strCode + "' name='" + _strCode + "'>" + "<script type=\"text/javascript\" src=\"http://api.map.baidu.com/api?v=2.0&ak=Hvo0danPmU7OTNqooGSeiPSIWXgaGd9o\"></script>" + "<style>.anchorBL{display:none;}</style>" + "<script>var sys_strMapId='" + _strCode + "';</script>" + "<script type=\"text/javascript\" src=\"js/yltmap.js\"></script>";
        }
        else if (_strType.startsWith("8")) {
            String strValue = "";
            if (this.bIsUpdate && bIsDoUpdate) {
                strValue = this.getEditValue(_strCode).toString();
            }
            if (bIsEdit) {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<textarea " + strRule + " id=\"" + _strCode + "\"" + " name=\"" + _strCode + "\"  style=\"height:" + strTempFieldSize + ";width:100%;\">" + strValue + "</textarea>";
            }
            else {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<textarea " + strRule + " id=\"" + _strCode + "\"" + " name=\"" + _strCode + "\"  style=\"height:" + strTempFieldSize + ";width:100%;background:#f1f1f1;\" readonly='readonly'>" + strValue + "</textarea>";
            }
        }
        else if (_strType.startsWith("PIC")) {
            final String strUploadId = String.valueOf(EString.generId()) + "_" + this.iFilePath;
            if (this.bIsUpdate && bIsDoUpdate) {
                final String strValue2 = this.getEditValue(_strCode).toString();
                String strViewPic = "";
                if (strValue2.indexOf("jpg") != -1 || strValue2.indexOf("gif") != -1 || strValue2.indexOf("png") != -1 || strValue2.indexOf("bmp") != -1 || strValue2.indexOf("jpeg") != -1) {
                    strViewPic = "<a href='upload/" + strValue2 + "' target='_blank'><img width='22' height='22' src='" + Dic.strCurRoot + "/upload/" + strValue2 + "' border='0'></a>";
                }
                else if (!strValue2.equals("")) {
                    strViewPic = "<a href='upload/" + strValue2 + "' target='_blank'><img width='22' height='22' src='images/file/" + this.eFile.getFileIco(strValue2) + "' border='0'></a>";
                }
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<table><tr><td class='tdtoupload'><div class='toupload' id='btn" + strUploadId + "'>\u4e0a\u4f20</div></td><td><div id='view" + strUploadId + "'>" + strViewPic + "</div><input id='" + _strCode + "' value='" + strValue2 + "' type='hidden' " + strRule + " name=\"" + _strCode + "\"  ></td></tr></table>";
            }
            else {
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<table><tr><td class='tdtoupload'><div class='toupload' id='btn" + strUploadId + "'>\u4e0a\u4f20</div></td><td><div id='view" + strUploadId + "'></div><input id='" + _strCode + "' type='hidden' " + strRule + " name=\"" + _strCode + "\"  ></td></tr></table>";
            }
            this.sbSingUpload.append("initSingFile('" + strUploadId + "','" + _strCode + "');");
            this.bIsSingleUpload = true;
            ++this.iFilePath;
            this.vecUploadIds.add(String.valueOf(strUploadId) + "," + _strCode);
        }
        else if (_strType.startsWith("MUP")) {
            this.bIsMutiUpload = true;
            if (this.bIsUpdate && bIsDoUpdate) {
                this.strFilePath = this.getEditValue(_strCode).toString();
                String strViewPic2 = "";
                if (this.strFilePath.equals("")) {
                    this.strFilePath = String.valueOf(EString.generId()) + "_" + this.iFilePath;
                    ++this.iFilePath;
                }
                else {
                    final String[] arrFiles = this.eFile.getFilesName(String.valueOf(Dic.strCurPath) + "uploads" + Dic.strPathSplit + this.strFilePath, "*");
                    for (int iFielCount = arrFiles.length, i = 0; i < iFielCount; ++i) {
                        final String strFil = arrFiles[i];
                        if (strFil.indexOf("jpg") != -1 || strFil.indexOf("gif") != -1 || strFil.indexOf("png") != -1 || strFil.indexOf("bmp") != -1 || strFil.indexOf("jpeg") != -1) {
                            strViewPic2 = String.valueOf(strViewPic2) + "<div class='uploadicoview'><a href='uploads/" + this.strFilePath + "/" + strFil + "' target='_blank'><img width='22' height='22' src='" + Dic.strCurRoot + "/uploads/" + this.strFilePath + "/" + strFil + "' border='0'></a>" + "<span onclick=\"delMuiltsUpFile(this,'" + this.strFilePath + "','" + strFil + "');\" style='font-size:16px;cursor:pointer;'>&nbsp;X</span>" + "<div class='mupfilename' pt='" + this.strFilePath + "'>" + arrFiles[i] + "</div></div>";
                        }
                        else {
                            strViewPic2 = String.valueOf(strViewPic2) + "<div class='uploadicoview'><a href='uploads/" + this.strFilePath + "/" + arrFiles[i] + "' target='_blank'><img width='22' height='22' src='images/file/" + this.eFile.getFileIco(arrFiles[i]) + "' border='0'></a>" + "<span onclick=\"delMuiltsUpFile(this,'" + this.strFilePath + "','" + strFil + "');\" style='font-size:16px;cursor:pointer;'>&nbsp;X</span>" + "<br>" + "<div class='mupfilename' pt='" + this.strFilePath + "'>" + arrFiles[i] + "</div></div>";
                        }
                    }
                }
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<table width='100%'><tr><td><div class='toupload' id='btn" + this.strFilePath + "'>\u4e0a\u4f20</div></td><td><div id='view" + this.strFilePath + "'>" + strViewPic2 + "</div></td></tr></table>" + "<input value='" + this.strFilePath + "' type='hidden' " + strRule + " name=\"" + _strCode + "\"  >";
            }
            else {
                this.strFilePath = String.valueOf(EString.generId()) + "_" + this.iFilePath;
                ++this.iFilePath;
                strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<table width='100%'><tr><td><div class='toupload' id='btn" + this.strFilePath + "'>\u4e0a\u4f20</div></td><td><div id='view" + this.strFilePath + "'></div></td></tr></table>" + "<input type='hidden' " + strRule + " name=\"" + _strCode + "\"  value=\"" + this.strFilePath + "\">";
            }
            this.sbMupUpload.append("initMutFile('" + this.strFilePath + "');");
        }
        else {
            String strValue = "";
            if (this.bIsUpdate && bIsDoUpdate && !_strType.startsWith("4$PY_") && !_strType.startsWith("4$TNGLOB_") && !_strType.startsWith("4$GET_")) {
                final String strEditValue = this.getEditValue(_strCode);
                if (!strEditValue.equals("")) {
                    strValue = " value=\"" + strEditValue + "\" ";
                }
            }
            final String strSelInput = this.generSeleEvent(_strSelPage, _strSelSize, _strType, bIsBatch, _strReturn, _strCode, strSelfCode);
            if (this.iCols == 1 || bIsViewTd) {
                if (bIsEdit) {
                    strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<input " + strSelInput + strValue + strType + " " + strRule + " id=\"" + _strCode + "\"" + " name=\"" + _strCode + "\"  style=\"width:" + _strFieldsSize + ";\">";
                }
                else {
                    strvResult = String.valueOf(strvResult) + strTrStyle + strLableTd + strDataTd + "<input " + strValue + strType + " " + strRule + " id=\"" + _strCode + "\"" + " name=\"" + _strCode + "\"  style=\"width:" + _strFieldsSize + ";background:#f1f1f1;\" readonly='readonly'>";
                }
            }
            else if (bIsEdit) {
                strvResult = String.valueOf(strvResult) + strTrStyle + "<input " + strSelInput + strValue + strType + " " + strRule + " id=\"" + _strCode + "\"" + " name=\"" + _strCode + "\"  style=\"width:" + _strFieldsSize + ";\">";
            }
            else {
                strvResult = String.valueOf(strvResult) + strTrStyle + "<input " + strValue + strType + " " + strRule + " id=\"" + _strCode + "\"" + " name=\"" + _strCode + "\"  style=\"width:" + _strFieldsSize + ";background:#f1f1f1;\" readonly='readonly'>";
            }
        }
        if (this.iCols == 1 || bIsViewTd) {
            strvResult = String.valueOf(strvResult) + strTdEnd + strTrEnd;
        }
        else {
            strvResult = String.valueOf(strvResult) + strTrEnd;
        }
        return strvResult;
    }
    
    private String getUrlParams(final HttpServletRequest _request) {
        String strParams = "";
        final Enumeration paramNames = _request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            final String paraName = paramNames.nextElement().toString();
            if (!paraName.equals("SPAGECODE") && !paraName.equals("gs_upl_kc")) {
                String paraValue = _request.getParameter(paraName);
                try {
                    paraValue = EString.encoderStr(paraValue);
                    strParams = String.valueOf(strParams) + "&" + paraName + "=" + URLEncoder.encode(paraValue, "GBK");
                }
                catch (Exception e) {
                    Debug.println("\u53c2\u6570\u7f16\u7801\u9519\u8bef\uff01" + e);
                }
            }
        }
        return strParams;
    }
    
    private String getEditValue(final String _strField) throws Exception {
        String vResult = "";
        final String[] arrField = _strField.split("\\$");
        final Object objRecord = this.hashTableCon.get(arrField[0]);
        if (objRecord == null) {
            return vResult;
        }
        final Record record = (Record)objRecord;
        final FieldEx fieldEx = record.getFieldByName(arrField[1]);
        if (fieldEx == null) {
            return vResult;
        }
        final Object objValue = fieldEx.value;
        if (objValue == null) {
            vResult = "";
        }
        else {
            vResult = objValue.toString().replaceAll("\"", "&quot;");
        }
        return vResult;
    }
    
    private String generSeleEvent(final String _strSelPage, String _strPageSize, final String _strType, final boolean bIsBatch, String _strReturn, final String _strCode, final String strSelfCode) {
        String strvResult = "";
        if (!_strSelPage.equals("0")) {
            boolean bIsMutile = false;
            if (_strReturn.startsWith("$")) {
                _strReturn = _strReturn.substring(1);
                bIsMutile = true;
            }
            if (_strPageSize.equals("0,0")) {
                _strPageSize = "570,350";
            }
            String strUrl = String.valueOf(_strSelPage) + this.strUrlParams;
            if (strUrl.startsWith("_URL")) {
                strUrl = strUrl.substring(4);
            }
            if (!bIsBatch) {
                strvResult = String.valueOf(strvResult) + EString.generSelDigStyle("\u9009\u62e9\u5bf9\u8bdd\u6846", strUrl, _strPageSize, _strReturn, bIsMutile);
            }
            else {
                String strSelDlg = EString.generBatchSelDig("\u9009\u62e9\u5bf9\u8bdd\u6846", strUrl, _strPageSize, _strReturn, bIsMutile);
                strSelDlg = strSelDlg.replaceAll("'", "\\\\'");
                strSelDlg = strSelDlg.replaceAll("\\+_icount\\+", "'\\+_icount\\+'");
                strvResult = String.valueOf(strvResult) + strSelDlg;
            }
        }
        else if (_strType.startsWith("7")) {
            strvResult = String.valueOf(strvResult) + "<img border='0' src=\"" + Dic.strCurRoot + "/images/eve/selectdata.gif\"  onclick=\"miniWin('\u9009\u62e9','','/js/yysj.jsp?strinput=" + _strType.substring(2) + "',220,240,'','');\" align=\"absmiddle\">";
        }
        return strvResult;
    }
    
    public String getFilterData(final String aStrData, final HttpServletRequest aRequest) {
        String vResult = aStrData;
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(vResult);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            final Object objValue = SqlStaticCon.hashStaticConType.get(aStrParam);
            String strReValue;
            if (objValue != null) {
                strReValue = SqlStaticCon.getSqlCon(aStrParam, aRequest.getSession(), aRequest);
            }
            else {
                strReValue = aRequest.getParameter(aStrParam);
            }
            if (strReValue != null) {
                vResult = vResult.replaceAll("<<" + aStrParam.replaceAll("\\$", "\\\\\\$") + ">>", EString.encoderStr(strReValue));
            }
            else {
                final Object objSessionValue = aRequest.getSession().getAttribute(aStrParam);
                if (objSessionValue != null) {
                    vResult = vResult.replaceAll("<<" + aStrParam.replaceAll("\\$", "\\\\\\$") + ">>", objSessionValue.toString());
                }
                else {
                    this.strErrMsg = String.valueOf(this.strErrMsg) + "\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam + "],\uff01";
                    final Enumeration enu = aRequest.getParameterNames();
                    while (enu.hasMoreElements()) {
                        final String strName = enu.nextElement().toString();
                        this.strErrMsg = String.valueOf(this.strErrMsg) + " " + strName + "=" + aRequest.getParameter(strName);
                    }
                    Debug.println(this.strErrMsg);
                }
            }
        }
        return vResult;
    }
    
    public String getFilterDataByStatic(final String aStrData, final HttpServletRequest aRequest, final Hashtable _hashParam, final HashMap<String, String> _hashMapParam) throws Exception {
        String vResult = aStrData;
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(vResult);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            String strReValue = _hashMapParam.get(aStrParam);
            if (strReValue == null) {
                final Object objValue = _hashParam.get(aStrParam);
                if (objValue != null) {
                    strReValue = objValue.toString();
                }
                else {
                    strReValue = aRequest.getParameter(aStrParam);
                }
            }
            if (strReValue != null) {
                vResult = vResult.replaceAll("<<" + aStrParam.replaceAll("\\$", "\\\\\\$") + ">>", EString.encoderStr(strReValue));
            }
            else {
                final Object objSessionValue = aRequest.getSession().getAttribute(aStrParam);
                if (objSessionValue != null) {
                    vResult = vResult.replaceAll("<<" + aStrParam.replaceAll("\\$", "\\\\\\$") + ">>", objSessionValue.toString());
                }
                else {
                    this.strErrMsg = String.valueOf(this.strErrMsg) + "\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam + "],\uff01";
                    final Enumeration enu = aRequest.getParameterNames();
                    while (enu.hasMoreElements()) {
                        final String strName = enu.nextElement().toString();
                        this.strErrMsg = String.valueOf(this.strErrMsg) + " " + strName + "=" + aRequest.getParameter(strName);
                    }
                    Debug.println(this.strErrMsg);
                }
            }
        }
        return vResult;
    }
    
    private String getFilterNoErr(final String aStrData, final HttpServletRequest aRequest) throws Exception {
        String vResult = aStrData;
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(vResult);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            final Object objValue = this.hashParams.get(aStrParam);
            String strReValue;
            if (objValue != null) {
                strReValue = objValue.toString();
            }
            else {
                strReValue = aRequest.getParameter(aStrParam);
            }
            if (strReValue != null) {
                vResult = vResult.replaceAll("<<" + aStrParam + ">>", strReValue);
            }
            else {
                final Object objSessionValue = aRequest.getSession().getAttribute(aStrParam);
                if (objSessionValue != null) {
                    vResult = vResult.replaceAll("<<" + aStrParam + ">>", objSessionValue.toString());
                }
                else {
                    vResult = vResult.replaceAll("<<" + aStrParam + ">>", "1");
                    this.strErrMsg = String.valueOf(this.strErrMsg) + "\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam + "],\u7cfb\u7edf\u5c06\u4ee5[1]\u66ff\u4ee3\uff01";
                }
            }
        }
        return vResult;
    }
    
    private String getFilterNoTip(final String aStrData, final HttpServletRequest aRequest) throws Exception {
        String vResult = aStrData;
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(vResult);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            final Object objValue = this.hashParams.get(aStrParam);
            String strReValue;
            if (objValue != null) {
                strReValue = objValue.toString();
            }
            else {
                strReValue = aRequest.getParameter(aStrParam);
            }
            if (strReValue != null) {
                vResult = vResult.replaceAll("<<" + aStrParam + ">>", strReValue);
            }
            else {
                final Object objSessionValue = aRequest.getSession().getAttribute(aStrParam);
                if (objSessionValue == null) {
                    continue;
                }
                vResult = vResult.replaceAll("<<" + aStrParam + ">>", objSessionValue.toString());
            }
        }
        return vResult;
    }
    
    public StringBuffer generQuery(final Hashtable hashHQRC, final HttpServletRequest aRequest) throws Exception {
        this.strErrMsg = "";
        String strSqlField = this.getFilterNoErr(hashHQRC.get("SQLFIELD").toString(), aRequest);
        if (strSqlField.equals("")) {
            strSqlField = "*";
        }
        String strCondition = hashHQRC.get("SQERYCON").toString();
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(strCondition);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            final Object objValue = SqlStaticCon.hashStaticConType.get(aStrParam);
            String strReValue;
            if (objValue != null) {
                strReValue = SqlStaticCon.getSqlCon(aStrParam, aRequest.getSession(), aRequest);
            }
            else {
                strReValue = aRequest.getParameter(aStrParam);
            }
            if (strReValue != null) {
                strCondition = strCondition.replaceAll("<<" + aStrParam + ">>", EString.encoderStr(strReValue));
            }
            else {
                final Object objSessionValue = aRequest.getSession().getAttribute(aStrParam);
                if (objSessionValue != null) {
                    strCondition = strCondition.replaceAll("<<" + aStrParam + ">>", objSessionValue.toString());
                }
                else {
                    this.strErrMsg = "\u672a\u627e\u5230\u53c2\u6570   [" + aStrParam + "],\uff01";
                }
            }
        }
        final String strQueryTable = this.getFilterNoErr(hashHQRC.get("SQUERYTABLE").toString(), aRequest);
        final Query query = new Query(strSqlField, strQueryTable, strCondition);
        final String[] arrcode = this.getFilterNoTip(hashHQRC.get("SFIELDCODE").toString(), aRequest).split(",");
        final String[] arrname = this.getFilterNoTip(hashHQRC.get("SFIELDNAME").toString(), aRequest).split(",");
        final String strIsDic = hashHQRC.get("STRANS").toString();
        final WebQuery webQuery = new WebQuery();
        if (!strIsDic.equals("")) {
            webQuery.arrStrIsDic = strIsDic.split(",");
        }
        final String strDataDb = hashHQRC.get("SDATADB").toString();
        if (!strDataDb.equals("")) {
            final String strDbName = aRequest.getParameter("sys_data_db_nm");
            if (strDbName == null) {
                throw new Exception("\u6b64\u67e5\u8be2\u4e3a\u52a8\u6001\u6570\u636e\u5e93\uff0c\u8fde\u63a5\u4e2d\u672a\u5305\u542b\u6570\u636e\u5e93\u4fe1\u606f!");
            }
            webQuery.strDBFunName = strDataDb;
        }
        final String strFiterField = aRequest.getParameter("NO_FILTER_FIELD");
        if (strFiterField != null) {
            final int iFiterFieldIndex = Integer.parseInt(aRequest.getParameter("NO_FILTER_FIELD_INDEX"));
            final FilterFieldQuery ffQ = new FilterFieldQuery();
            String strFiterCon = EString.encoderStr(aRequest.getParameter("sys_frame_view_condition"));
            if (!strFiterCon.equals("")) {
                final String strCurDoFiterCondition = String.valueOf(strFiterField) + " in(";
                final int iIndex = strFiterCon.indexOf(strCurDoFiterCondition);
                if (iIndex != -1) {
                    strFiterCon = strFiterCon.substring(0, iIndex).trim();
                    final int iEndIndex = strFiterCon.lastIndexOf(" and");
                    if (iEndIndex != -1) {
                        strFiterCon = strFiterCon.substring(0, iEndIndex).trim();
                    }
                    if (!strFiterCon.equals("")) {
                        query.addHaving(" " + strFiterCon);
                    }
                }
                else {
                    query.addHaving(strFiterCon);
                }
            }
            return ffQ.doFilterFieldQuery(webQuery.arrStrIsDic[iFiterFieldIndex], new Query("distinct " + strFiterField, query, ""), aRequest);
        }
        final String strTrStyle = hashHQRC.get("STRSTYLE").toString();
        webQuery.sys_strTrStyle = strTrStyle;
        webQuery.request = aRequest;
        if (!hashHQRC.get("SSQLCON").toString().equals("")) {
            webQuery.arrFieldType = hashHQRC.get("SSQLCON").toString().split(",");
        }
        final String strQueryField = hashHQRC.get("SQUERYFIELD").toString();
        webQuery.strQueryField = strQueryField;
        webQuery.strDefault = this.strDefault;
        Object objTempValue = hashHQRC.get("SDELCON");
        webQuery.strDelParam = objTempValue.toString();
        objTempValue = hashHQRC.get("SEDITPAGE");
        webQuery.strEditPage = objTempValue.toString();
        webQuery.strUseMod = this.strUseMod;
        if (!this.strDic.equals("")) {
            webQuery.bIsDic = false;
        }
        final String strWidth = aRequest.getParameter("w");
        final String strHeight = aRequest.getParameter("h");
        if (strWidth != null) {
            webQuery.iEditWidth = Integer.parseInt(strWidth);
        }
        if (strHeight != null) {
            webQuery.iEditHeight = Integer.parseInt(strHeight);
        }
        final String strHref = hashHQRC.get("SHREFIELD").toString();
        if (!strHref.equals("")) {
            final String[] arrStrHref = strHref.split("\\$");
            webQuery.hrefname = arrStrHref[0];
            webQuery.href = arrStrHref[1];
        }
        String strHashSplit = hashHQRC.get("SISSPLIT").toString();
        if (strHashSplit.equals("")) {
            strHashSplit = "0";
        }
        if (strHashSplit.equals("0")) {
            this.strIsSplit = "false";
        }
        else {
            this.strIsSplit = "true";
        }
        webQuery.strIsStructs = hashHQRC.get("SISLAYOUT").toString();
        webQuery.strLineCount = this.strLineCount;
        webQuery.strIsSplit = strHashSplit;
        webQuery.strElementTitle = this.strElementTitle;
        webQuery.bIsTrColor = this.isTrColor;
        final String strIsMode = hashHQRC.get("SMOD").toString();
        if (!strIsMode.equals("")) {
            webQuery.iStyle = strIsMode;
            webQuery.strUseMod = "true";
        }
        else {
            webQuery.iStyle = this.strStyle;
        }
        webQuery.strWidth = this.width;
        final Object objFiledSize = hashHQRC.get("SFIELDSIZE");
        if (objFiledSize != null && !objFiledSize.toString().equals("")) {
            webQuery.arrStrFileSize = objFiledSize.toString().split(",");
            double dWidth = 0.0;
            for (int i = webQuery.arrStrFileSize.length - 1; i >= 0; --i) {
                dWidth += Double.parseDouble(webQuery.arrStrFileSize[i]);
            }
            webQuery.strWidth = new StringBuilder(String.valueOf(dWidth)).toString();
        }
        webQuery.strSize = hashHQRC.get("SSIZE").toString();
        webQuery.strAddPage = this.getFilterData(hashHQRC.get("SGLFIELD").toString(), aRequest);
        Object objRights = null;
        final Object objCurRoleRights = aRequest.getSession().getAttribute("SYS_CUR_ROLE_FIELDRIGHTS");
        if (objCurRoleRights != null) {
            objRights = ((Hashtable)objCurRoleRights).get(hashHQRC.get("SPAGECODE").toString());
        }
        if (objRights != null) {
            webQuery.hashFieldsRights = (Hashtable)objRights;
        }
        webQuery.strCurRoleCode = aRequest.getSession().getAttribute("SYS_STRROLECODE").toString();
        webQuery.hrefname = hashHQRC.get("STREE").toString();
        webQuery.bIsToExcel = this.bIsExcel;
        webQuery.response = this.response;
        webQuery.strDataSetId = hashHQRC.get("SCONID").toString();
        final String strColRowSplit = hashHQRC.get("SISCOLCHANGEROW").toString();

        if (strColRowSplit.startsWith("cmd1:")) {
            final String[] arrColToCols = strColRowSplit.substring(5).split(";");
            final int iSplitColsCount = arrColToCols.length;
            final HashMap<String, String[]> hashColToCols = new HashMap<String, String[]>();
            final HashMap<String, String> hashColToColsDic = new HashMap<String, String>();
            for (int j = 0; j < iSplitColsCount; ++j) {
                final String[] arrSplitColsMsg = arrColToCols[j].split(":");
                hashColToCols.put(arrSplitColsMsg[0], arrSplitColsMsg[1].split(","));
                final int iDicCount = arrSplitColsMsg.length;
                if (iDicCount > 2) {
                    for (int k = 2; k < iDicCount; ++k) {
                        hashColToColsDic.put(String.valueOf(arrSplitColsMsg[0]) + "__" + (k - 2), arrSplitColsMsg[k]);
                    }
                }
            }
            webQuery.hashColToCols = hashColToCols;
            webQuery.hashColToColsDic = hashColToColsDic;
        }
        else {
            webQuery.strISCOLTOROW = strColRowSplit;
        }
        webQuery.strPageCode = hashHQRC.get("SPAGECODE").toString();
        final String strInvoke = hashHQRC.get("SINVOKE").toString();
        if (!strInvoke.equals("")) {
            if (strInvoke.startsWith("$")) {
                final ComponentInvoke cInvoke = new ComponentInvoke(strInvoke.substring(1));
                webQuery.sbOp = cInvoke.viewFun(aRequest);
            }
            else {
                webQuery.bIsInvoke = true;
                webQuery.comInvoke = new ComponentInvoke(strInvoke);
                webQuery.comInvoke.request = aRequest;
            }
        }
        webQuery.sysStrRowClick = this.sysStrRowClick;
        if (this.strErrMsg.equals("")) {
            return webQuery.getAllCustMsg(query, arrcode, arrname);
        }
        return webQuery.getAllCustMsg(query, arrcode, arrname).append("<div style='position:absolute;background:yellow;top:300;left:0;color:red;'>" + this.strErrMsg + "</div>");
    }
    
    public StringBuffer generFrame(final HttpServletResponse aResponse, final Hashtable hashHQRC, final String aStrPage, final HttpServletRequest aRequest) throws Exception {
        String strParams = "";
        final Enumeration paramNames = aRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            final String paraName = paramNames.nextElement().toString();
            if (!paraName.equals("SPAGECODE")) {
                String paraValue = aRequest.getParameter(paraName);
                try {
                    paraValue = EString.encoderStr(paraValue);
                }
                catch (Exception e) {
                    Debug.println("\u53c2\u6570\u7f16\u7801\u9519\u8bef\uff01" + e);
                }
                strParams = String.valueOf(strParams) + "&" + paraName + "=" + URLEncoder.encode(paraValue, "GBK");
            }
        }
        final StringBuffer vResult = this.generFrame(aResponse, hashHQRC.get("SGLFIELD").toString(), "sys_pt=1&sys_pg=" + aStrPage + strParams, "sys_pt=2&sys_pg=" + aStrPage + strParams, hashHQRC.get("SDELCON").toString(), hashHQRC.get("SHREFIELD").toString());
        return vResult;
    }
    
    public StringBuffer generFrame(final HttpServletRequest aRequest, final HttpServletResponse aResponse) {
        final StringBuffer vResult = this.generFrame(aResponse, aRequest.getParameter("itype"), "fir=" + aRequest.getParameter("fir"), "fir=" + aRequest.getParameter("sec"), aRequest.getParameter("firwidth"), aRequest.getParameter("secwidth"));
        return vResult;
    }
    
    private StringBuffer generFrame(final HttpServletResponse aResponse, final String aStrType, final String aStrFirstParam, final String strSectParam, String aStrFirstWidth, String aStrSectWidth) {
        final StringBuffer vResult = new StringBuffer();
        if (aStrFirstWidth.equals("")) {
            aStrFirstWidth = "*";
        }
        if (aStrType.equals("1")) {
            try {
                aResponse.sendRedirect("ViewFrame?" + aStrFirstParam);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (aStrType.equals("2")) {
            if (aStrSectWidth.equals("")) {
                aStrSectWidth = "*";
            }
            vResult.append("<html>");
            vResult.append("<HEAD><TITLE></TITLE>");
            vResult.append("    <FRAMESET id='main' border=0  frameBorder=no name='main' frameSpacing=0 rows=*  cols=");
            vResult.append(aStrFirstWidth);
            vResult.append(",7,");
            vResult.append(aStrSectWidth);
            vResult.append(">");
            vResult.append("        <FRAME id=lxleft title=lxleft name=lxleft src=\"ViewFrame?");
            vResult.append(aStrFirstParam);
            vResult.append("\"  scrolling=auto  frameborder =\"no\">");
            vResult.append("        <FRAME name=lineFrame src=\"line.html\" noResize scrolling=no>");
            vResult.append("        <FRAME id=lxmain title=lxmain  frameborder =\"no\" name=lxmain src=\"ViewFrame?");
            vResult.append(strSectParam);
            vResult.append("\">");
            vResult.append("    </FRAMESET>");
            vResult.append("</HTML>");
        }
        else if (aStrType.equals("3")) {
            if (aStrSectWidth.equals("")) {
                aStrSectWidth = "*";
            }
            vResult.append("<html>");
            vResult.append("<HEAD><TITLE></TITLE>");
            vResult.append("    <FRAMESET border=0 name=main frameSpacing=0 cols=* frameBorder=no rows=");
            vResult.append(aStrFirstWidth);
            vResult.append(",7,");
            vResult.append(aStrSectWidth);
            vResult.append(">");
            vResult.append("        <FRAME id=lxleft title=lxleft name=lxleft src=\"ViewFrame?");
            vResult.append(aStrFirstParam);
            vResult.append("\" noResize scrolling=no>");
            vResult.append("        <FRAME name=lineFrame src=\"line.html\" noResize scrolling=no>");
            vResult.append("        <FRAME id=lxmain title=lxmain name=lxmain src=\"ViewFrame?");
            vResult.append(strSectParam);
            vResult.append("\">");
            vResult.append("    </FRAMESET>");
            vResult.append("</HTML>");
        }
        return vResult;
    }
    
    public void setStrIsSplit(final String strIsSplit) {
        this.strIsSplit = strIsSplit;
    }
    
    public String getStrIsSplit() {
        return this.strIsSplit;
    }
    
    public void setStrElementTitle(final String strElementTitle) {
        this.strElementTitle = strElementTitle;
    }
    
    public String getStrElementTitle() {
        return this.strElementTitle;
    }
    
    public void setIsTrColor(final String isTrColor) {
        this.isTrColor = isTrColor;
    }
    
    public String getIsTrColor() {
        return this.isTrColor;
    }
    
    public void setStrStyle(final String strStyle) {
        this.strStyle = strStyle;
    }
    
    public String getStrStyle() {
        return this.strStyle;
    }
    
    public void setWidth(final String width) {
        this.width = width;
    }
    
    public String getWidth() {
        return this.width;
    }
    
    public void setStrUseMod(final String strUseMod) {
        this.strUseMod = strUseMod;
    }
    
    public String getStrUseMod() {
        return this.strUseMod;
    }
    
    public void setStrParams(final String strParams) {
        this.strParams = strParams;
    }
    
    public String getStrParams() {
        return this.strParams;
    }
    
    public void setStrLineCount(final String strParams) {
        this.strLineCount = strParams;
    }
    
    public String getStrLineCount() {
        return this.strLineCount;
    }
    
    public void setStrPut(final String strPut) {
        this.strPut = strPut;
    }
    
    public String getStrPut() {
        return this.strPut;
    }
    
    public void setStrDic(final String strDic) {
        this.strDic = strDic;
    }
    
    public String getStrDic() {
        return this.strDic;
    }
}
