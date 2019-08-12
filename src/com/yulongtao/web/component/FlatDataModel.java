package com.yulongtao.web.component;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.page.method.Fun;
import com.yonyou.mis.util.ApplicationUtils;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Node;
import com.yulongtao.pub.Pub;
import com.yulongtao.pub.TreeData;
import com.yulongtao.sys.Dic;
import com.yulongtao.sys.PreloadJS;
import com.yulongtao.sys.QRSC;
import com.yulongtao.util.EFile;
import com.yulongtao.util.EString;
import com.yulongtao.web.ABSElement;
import com.yulongtao.web.ParamTree;
import com.yulongtao.web.chart.CharView;
import com.yulongtao.web.chart.ChartData;

public class FlatDataModel
{
    public static Hashtable<String, HashMap> hashFlatDataStyle;
    public boolean bIsCon;
    private ParamTree pTree;
    public HttpServletRequest request;
    public HttpServletResponse response;
    public Hashtable<String, String> hashSysParams;
    public HashMap<String, String> hashParams;
    private StringBuffer sbHtml;
    private HashMap hashMapStyleModel;
    StringBuffer sbScript;
    public DBFactory dbf;
    private TreeData treeData;
    CharView cV;
    
    private static HashMap initTreeStyeContent(final String _strStyleFile) {
        final String[] arrStrContent = _strStyleFile.split("\\$value");
        final int iFieldCount = arrStrContent.length;
        final String[] arrStrField = new String[iFieldCount];
        for (int k = 0; k < iFieldCount; ++k) {
            final String strContent = arrStrContent[k].trim();
            if (strContent.startsWith("(")) {
                final int iKhEnd = strContent.indexOf(")");
                arrStrField[k] = strContent.substring(1, iKhEnd).trim();
                arrStrContent[k] = strContent.substring(iKhEnd + 1);
            }
            else {
                arrStrField[k] = "";
            }
        }
        final HashMap hashvResult = new HashMap();
        hashvResult.put("STRCONTENT", arrStrContent);
        hashvResult.put("STRFIELD", arrStrField);
        return hashvResult;
    }
    
    public static void setStyle() {
        final String strCurrPath = String.valueOf(Dic.strCurPath) + "style\\flatstyle";
        FlatDataModel.hashFlatDataStyle = new Hashtable<String, HashMap>();
        try {
            final EFile eFile = new EFile();
            final String[] strFiles = eFile.getFilesName(strCurrPath, "html");
            for (int iFileCount = strFiles.length, i = 0; i < iFileCount; ++i) {
                final ArrayList<HashMap> listTreeStyle = new ArrayList<HashMap>();
                final String strStyleFile = eFile.readFile(String.valueOf(strCurrPath) + "/" + strFiles[i]).toString();
                final String strStyleFileName = strFiles[i].split("\\.")[0];
                final HashMap hashVresult = initTreeStyeContent(strStyleFile);
                FlatDataModel.hashFlatDataStyle.put(strStyleFileName, hashVresult);
            }
        }
        catch (Exception e) {
            System.out.println("\u52a0\u8f7d\u98ce\u683c\u6587\u4ef6\u5931\u8d25\uff01" + e);
        }
    }
    
    public FlatDataModel(final HttpServletRequest _request, final HttpServletResponse _response) {
        this.bIsCon = false;
        this.pTree = new ParamTree();
        this.hashParams = null;
        this.sbHtml = new StringBuffer();
        this.sbScript = new StringBuffer();
        this.dbf = null;
        this.treeData = null;
        this.cV = null;
        this.request = _request;
        this.response = _response;
    }
    
    private void viewFlatData(final String _strSql) {
        final String[] arrStrStart = (String[]) this.hashMapStyleModel.get("STRCONTENT");
        final String[] arrStrField = (String[]) this.hashMapStyleModel.get("STRFIELD");
        final int iFieldCount = arrStrStart.length;
        final DBFactory dbf = new DBFactory();
        TableEx tableEx = null;
        try {
            tableEx = dbf.query(_strSql);
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                for (int j = 0; j < iFieldCount; ++j) {
                    if (!arrStrField[j].equals("")) {
                        this.sbHtml.append(record.getFieldByName(arrStrField[j]).value);
                    }
                    this.sbHtml.append(arrStrStart[j]);
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
            dbf.close();
        }
    }
    
    public String getParameter(String _strParamName) {
        String strReValue = null;
        boolean bIsDic = false;
        String strDicType = "";
        String strDicDefalutValue = "";
        if (_strParamName.startsWith("dic.")) {
            bIsDic = true;
            final String[] arrDicParam = _strParamName.split("\\.");
            strDicType = arrDicParam[1];
            _strParamName = arrDicParam[2];
            if (arrDicParam.length > 3) {
                strDicDefalutValue = arrDicParam[3];
            }
        }
        if (_strParamName.startsWith("_fun_")) {
            final String _strFun = _strParamName.substring(5);
            final String[] arrStrFun = _strFun.split("_");
            final int iParamCount = arrStrFun.length;
            final Fun fun = new Fun();
            fun.request = this.request;
            fun.response = this.response;
            final Class objClass = fun.getClass();
            if (iParamCount > 1) {
                final Class[] classParam = new Class[iParamCount - 1];
                final String[] paramValue = new String[iParamCount - 1];
                for (int i = 1; i < iParamCount; ++i) {
                    classParam[i - 1] = String.class;
                    paramValue[i - 1] = arrStrFun[i];
                }
                try {
                    strReValue = objClass.getMethod(arrStrFun[0], (Class[])classParam).invoke(fun, (Object[])paramValue).toString();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    strReValue = Class.forName("com.page.method.Fun").getMethod(arrStrFun[0], (Class<?>[])new Class[0]).invoke(fun, new Object[0]).toString();
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return strReValue;
        }
        if (Dic.HashNeedSession.get(_strParamName) != null) {
            final Object objSessionValue = this.request.getSession().getAttribute(_strParamName);
            if (objSessionValue != null) {
                strReValue = objSessionValue.toString();
            }
        }
        else {
            if (this.hashParams != null) {
                strReValue = this.hashParams.get(_strParamName);
            }
            else {
                strReValue = null;
            }
            if (strReValue == null) {
                strReValue = this.request.getParameter(_strParamName);
                if (strReValue != null) {
                    strReValue = EString.encoderStr(strReValue, "utf-8");
                }
                else {
                    final Object objSessionValue = this.request.getSession().getAttribute(_strParamName);
                    if (objSessionValue != null) {
                        strReValue = objSessionValue.toString();
                    }
                }
            }
        }
        if (bIsDic) {
            if (strReValue != null && !strReValue.toString().equals("")) {
                strReValue = Dic.hash.get(String.valueOf(strDicType) + "_" + strReValue).toString();
            }
            else {
                strReValue = strDicDefalutValue;
            }
        }
        return strReValue;
    }
    
    private String getFilterSql(final String aStrData) {
        String vResult = aStrData;
        final String regex = "<<([^<^>]*)>>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(vResult);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            final String strReValue = this.getParameter(aStrParam);
            if (strReValue != null) {
                vResult = vResult.replaceAll("<<" + aStrParam + ">>", strReValue);
            }
        }
        return vResult;
    }
    
    public StringBuffer getStyle(final String _strPageId, final String[] _arrItemStyle) throws Exception {
        this.sbHtml = new StringBuffer();
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(_strPageId);
        this.viewFlatData(hashHQRC, _arrItemStyle);
        return this.sbHtml;
    }
    
    public StringBuffer getView(final String _strPageId, final String[] _arrItemStyle) throws Exception {
        this.sbHtml = new StringBuffer();
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(_strPageId);
        this.viewViewData(hashHQRC, _arrItemStyle);
        return this.sbHtml;
    }
    
    private void viewViewData(final Hashtable hashHQRC, final String[] _arrItemStyle) throws Exception {
        final TableEx tableEx = this.gennerTable(hashHQRC);
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrFiledType = hashHQRC.get("SSQLCON").toString().split(",");
        final String[] arrDic = hashHQRC.get("STRANS").toString().split(",");
        final int iFieldCount = arrcode.length;
        final int iRecordCount = tableEx.getRecordCount();
        final int iStyleItemCount = _arrItemStyle.length;
        int iFieldIndex = 0;
        final EFile eFile = new EFile();
        final Record record = tableEx.getRecord(0);
        this.sbHtml.append(_arrItemStyle[0]);
        for (int j = 1; j < iStyleItemCount; ++j) {
            iFieldIndex = j - 1;
            if (iFieldIndex < iFieldCount) {
                Object objValue = record.getFieldByName(arrcode[iFieldIndex]).value;
                if (objValue != null) {
                    if (!arrDic[iFieldIndex].equals("0")) {
                        objValue = Dic.getDics(arrDic[iFieldIndex], objValue.toString());
                    }
                    if (arrFiledType[iFieldIndex].equals("FILE")) {
                        objValue = eFile.getEFiles("uploads" + Dic.strPathSplit + objValue, "*", objValue.toString());
                    }
                }
                this.sbHtml.append(objValue);
            }
            this.sbHtml.append(_arrItemStyle[j]);
        }
    }
    
    public StringBuffer getForm(final String _strPageId, final String[] _arrItemStyle) throws Exception {
        this.sbHtml = new StringBuffer();
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(_strPageId);
        final ABSElement absElement = new ABSElement();
        final String[] arrField = absElement.generInputField_View(hashHQRC, this.request);
        this.viewFormData(hashHQRC, _arrItemStyle, arrField);
        if (absElement.bIsMutiUpload || absElement.bIsSingleUpload) {
            this.sbHtml.append("<script type='text/javascript' src='js/yluploader.js'></script><script>var uploadUrl='").append("uploadFile?';").append(absElement.sbMupUpload).append(absElement.sbSingUpload).append("</script>");
        }
        this.sbHtml.append(absElement.sbUpdateConfig);
        this.sbScript.append("gs_root='" + Dic.strCurRoot + "';init(add);");
        return this.sbHtml;
    }
    
    private void viewFormData(final Hashtable hashHQRC, final String[] _arrItemStyle, final String[] _arrField) throws Exception {
        final int iFieldCount = _arrField.length;
        final int iStyleItemCount = _arrItemStyle.length;
        final int iFieldIndex = 0;
        for (int i = 0; i < iStyleItemCount; ++i) {
            this.sbHtml.append(_arrItemStyle[i]);
            if (i < iFieldCount) {
                this.sbHtml.append(_arrField[i]);
            }
        }
    }
    
    public StringBuffer getStyle(final String _strPageId, final String[] _arrItemStyle, final String[] _arrCaseConfig) throws Exception {
        this.sbHtml = new StringBuffer();
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(_strPageId);
        this.viewFlatData(hashHQRC, _arrItemStyle, _arrCaseConfig);
        return this.sbHtml;
    }
    
    public StringBuffer getStyleSTL(final String _strPageId, final String[] _arrItemStyle, final String[] _arrCaseConfig) throws Exception {
        this.sbHtml = new StringBuffer();
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(_strPageId);
        this.viewFlatDataSTL(hashHQRC, _arrItemStyle, _arrCaseConfig);
        return this.sbHtml;
    }
    
    private void viewFlatDataSTL(final Hashtable hashHQRC, final String[] _arrItemStyle, final String[] _arrCaseConfig) throws Exception {
        final TableEx tableEx = this.gennerTable(hashHQRC);
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final int iFieldCount = arrcode.length;
        final int iRecordCount = tableEx.getRecordCount();
        final int iStyleItemCount = _arrItemStyle.length;
        int iFieldIndex = 0;
        int iStyleIndex = 0;
        final int iStyleCount = _arrCaseConfig.length;
        for (int i = 0; i < iRecordCount; ++i) {
            final Record record = tableEx.getRecord(i);
            this.sbHtml.append(_arrItemStyle[0]);
            if (iStyleIndex >= iStyleCount) {
                iStyleIndex = 0;
            }
            this.sbHtml.append(_arrCaseConfig[iStyleIndex]);
            this.sbHtml.append(_arrItemStyle[1]);
            ++iStyleIndex;
            for (int j = 2; j < iStyleItemCount; ++j) {
                iFieldIndex = j - 2;
                if (iFieldIndex < iFieldCount) {
                    this.sbHtml.append(record.getFieldByName(arrcode[iFieldIndex]).value);
                }
                this.sbHtml.append(_arrItemStyle[j]);
            }
        }
    }
    
    private void viewFlatData(final Hashtable hashHQRC, final String[] _arrItemStyle, final String[] _arrCaseConfig) throws Exception {
        final TableEx tableEx = this.gennerTable(hashHQRC);
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final int iFieldCount = arrcode.length;
        final int iRecordCount = tableEx.getRecordCount();
        final int iStyleItemCount = _arrItemStyle.length;
        int iFieldIndex = 0;
        for (int i = 0; i < iRecordCount; ++i) {
            final Record record = tableEx.getRecord(i);
            this.sbHtml.append(this.fiterItemStyle(record, _arrItemStyle[0], _arrCaseConfig[0], _arrCaseConfig[1], _arrCaseConfig[2]));
            for (int j = 1; j < iStyleItemCount; ++j) {
                iFieldIndex = j - 1;
                if (iFieldIndex < iFieldCount) {
                    this.sbHtml.append(record.getFieldByName(arrcode[iFieldIndex]).value);
                }
                this.sbHtml.append(this.fiterItemStyle(record, _arrItemStyle[j], _arrCaseConfig[0], _arrCaseConfig[1], _arrCaseConfig[2]));
            }
        }
    }
    
    private String fiterItemStyle(final Record _record, String _str, final String _strCase, final String _strCaseValue, final String _strTo) {
        final String strFieldValue = _record.getFieldByName(_strCaseValue).value.toString();
        String strCaseValue = this.hashSysParams.get(_strCase);
        if (strCaseValue == null) {
            strCaseValue = _strCase;
        }
        if (strFieldValue.equals(strCaseValue)) {
            _str = _str.replaceAll("\\$" + _strCase, _strTo);
        }
        return _str;
    }
    
    private TableEx gennerTable(final Hashtable hashHQRC) throws Exception {
        final String strSqlField = hashHQRC.get("SQLFIELD").toString();
        final String strCondition = hashHQRC.get("SQERYCON").toString();
        final String strQueryTable = hashHQRC.get("SQUERYTABLE").toString();
        String strSql = "select " + strSqlField + " from " + strQueryTable;
        if (!strCondition.equals("")) {
            strSql = String.valueOf(strSql) + " where " + strCondition;
        }
        final TableEx tableEx = this.dbf.query(this.getFilterSql(strSql));
        return tableEx;
    }
    
    private String gennerSql(final Hashtable hashHQRC) throws Exception {
        final String strSqlField = hashHQRC.get("SQLFIELD").toString();
        final String strCondition = hashHQRC.get("SQERYCON").toString();
        final String strQueryTable = hashHQRC.get("SQUERYTABLE").toString();
        String strSql = "select " + strSqlField + " from " + strQueryTable;
        if (!strCondition.equals("")) {
            strSql = String.valueOf(strSql) + " where " + strCondition;
        }
        return this.getFilterSql(strSql);
    }
    
    public String gennerSql(final String _strSqlField, final Hashtable hashHQRC) throws Exception {
        final String strCondition = hashHQRC.get("SQERYCON").toString();
        final String strQueryTable = hashHQRC.get("SQUERYTABLE").toString();
        String strSql = "select " + _strSqlField + " from " + strQueryTable;
        if (!strCondition.equals("")) {
            strSql = String.valueOf(strSql) + " where " + strCondition;
        }
        return this.getFilterSql(strSql);
    }
    
    public StringBuffer getPageJsData(final String _strPageId, final String _strCon) throws Exception {
        final StringBuffer vResult = new StringBuffer("[");
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(_strPageId);
        String strSql = this.gennerSql(hashHQRC);
        if (!_strCon.equals("")) {
            strSql = "select * from (" + this.gennerSql(hashHQRC) + ") A " + _strCon;
        }
        TableEx tableEx = null;
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final String strColRowSplit = hashHQRC.get("SISCOLCHANGEROW").toString();
        final HashMap<String, String[]> hashColToCols = new HashMap<String, String[]>();
        final HashMap<String, String> hashColToColsDic = new HashMap<String, String>();
        if (strColRowSplit.startsWith("cmd1:")) {
            final String[] arrColToCols = strColRowSplit.substring(5).split(";");
            for (int iSplitColsCount = arrColToCols.length, i = 0; i < iSplitColsCount; ++i) {
                final String[] arrSplitColsMsg = arrColToCols[i].split(":");
                hashColToCols.put(arrSplitColsMsg[0], arrSplitColsMsg[1].split(","));
                final int iDicCount = arrSplitColsMsg.length;
                if (iDicCount > 2) {
                    for (int j = 2; j < iDicCount; ++j) {
                        hashColToColsDic.put(String.valueOf(arrSplitColsMsg[0]) + "__" + (j - 2), arrSplitColsMsg[j]);
                    }
                }
            }
        }
        final String strDataDb = hashHQRC.get("SDATADB").toString();
        if (!strDataDb.equals("")) {
            this.dbf.close();
            this.dbf = new DBFactory(strDataDb, 1);
        }
        tableEx = this.dbf.query(strSql);
        final int iRecordCount = tableEx.getRecordCount();
        final int iFieldCount = arrcode.length;
        String strSplit = "";
        for (int k = 0; k < iRecordCount; ++k) {
            final Record record = tableEx.getRecord(k);
            vResult.append(strSplit).append("{");
            String strFieldSplit = "";
            for (int l = 0; l < iFieldCount; ++l) {
                final String[] arrSplitCols = hashColToCols.get(arrcode[l]);
                if (arrSplitCols != null) {
                    final int iSplitColCount = arrSplitCols.length;
                    final String strValue = record.getFieldByName(arrcode[l]).value.toString();
                    final int iValueLength = strValue.length();
                    for (int m = 0; m < iSplitColCount; ++m) {
                        String strNewValue = "";
                        if (m < iValueLength) {
                            final char c = strValue.charAt(m);
                            strNewValue = hashColToColsDic.get(String.valueOf(arrcode[l]) + "__" + c);
                            if (strNewValue == null) {
                                strNewValue = new StringBuilder(String.valueOf(c)).toString();
                            }
                        }
                        vResult.append(strFieldSplit).append("'").append(String.valueOf(arrcode[l]) + "_" + m).append("':'").append(strNewValue).append("'");
                        strFieldSplit = ",";
                    }
                }
                else {
                    vResult.append(strFieldSplit).append("'").append(arrcode[l]).append("':'").append(record.getFieldByName(arrcode[l]).value).append("'");
                }
                strFieldSplit = ",";
            }
            vResult.append("}");
            strSplit = ",";
        }
        vResult.append("]");
        return vResult;
    }
    
    private void viewFlatData(final Hashtable hashHQRC, final String[] _arrItemStyle) throws Exception {
        String strSql = this.gennerSql(hashHQRC);
        String strHaving = this.request.getParameter("SYS_HAV_FIT");
        if (strHaving != null) {
            if (this.request.getParameter("sys_charset") == null) {
                strHaving = EString.encoderStr(strHaving);
            }
            else {
                strHaving = EString.encoderStr(strHaving, "utf-8");
            }
            final String[] arrHaVing = strHaving.split(":", -1);
            if (hashHQRC.get("SPAGECODE").toString().equals(arrHaVing[0])) {
                strSql = "select * from (" + strSql + ") sys_hav where " + arrHaVing[1];
                final int iLogic = Integer.parseInt(arrHaVing[2]);
                switch (iLogic) {
                    case 0: {
                        strSql = String.valueOf(strSql) + " like '%" + arrHaVing[3] + "%'";
                        break;
                    }
                    case 1: {
                        strSql = String.valueOf(strSql) + " like '" + arrHaVing[3] + "%'";
                        break;
                    }
                }
            }
        }
        System.out.println("===============" + strSql);
        TableEx tableEx = null;
        final String[] arrcode = hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrDic = hashHQRC.get("STRANS").toString().split(",");
        int pageSize = 0;
        boolean bIsSplit = false;
        final String strSplitMsg = hashHQRC.get("SISSPLIT").toString();
        if (!strSplitMsg.equals("")) {
            final String[] arrIsSplit = strSplitMsg.split(":");
            if (arrIsSplit[0].equals("0")) {
                bIsSplit = false;
            }
            else {
                bIsSplit = true;
                pageSize = Integer.parseInt(arrIsSplit[1]);
            }
        }
        String strSplit = "";
        final String strDataDb = hashHQRC.get("SDATADB").toString();
        boolean bIsDynDB = false;
        final DBFactory dbfTemp = this.dbf;
        if (!strDataDb.equals("")) {
            this.dbf = new DBFactory(strDataDb, 1);
            bIsDynDB = true;
        }
        if (bIsSplit) {
            if (this.request != null) {
                final String strUrl = this.request.getRequestURL().toString();
                String strCount = this.request.getParameter("RECORDCOUNT");
                int iTotalPage = 0;
                if (strCount == null) {
                    tableEx = this.dbf.query(strSql);
                    strCount = new StringBuilder(String.valueOf(tableEx.getRecordCount())).toString();
                }
                iTotalPage = Integer.parseInt(strCount) / pageSize;
                if (Integer.parseInt(strCount) % pageSize > 0) {
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
                final int iStartCount = (iCurrentPage - 1) * pageSize;
                strSql = String.valueOf(strSql) + " limit " + iStartCount + "," + pageSize;
                tableEx = this.dbf.query(strSql);
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
                    }
                }
                String strSy = "<label class='fytext' onclick=\"document.postForm.OPESTR.value='SHOUYE';postForm.submit();\"><img onmouseover='sys_spit_mouseover(this,1)' onmouseout='sys_spit_mouseout(this,1)' src='images/split/sy.png'></label>";
                String Wy = "&nbsp;&nbsp;<label class='fytext' onclick=\"document.postForm.OPESTR.value='WEIYE';postForm.submit();\"><img onmouseover='sys_spit_mouseover(this,2)' onmouseout='sys_spit_mouseout(this,2)' src='images/split/wy.png'></label>";
                String Syy = "&nbsp;&nbsp;<label class='fytext' onclick=\"document.postForm.OPESTR.value='SHANGYE';postForm.submit();\"><img onmouseover='sys_spit_mouseover(this,3)' onmouseout='sys_spit_mouseout(this,3)' src='images/split/syy.png'></label>";
                String Xyy = "&nbsp;&nbsp;<label class='fytext' onclick=\"document.postForm.OPESTR.value='XIAYE';postForm.submit();\"><img onmouseover='sys_spit_mouseover(this,4)' onmouseout='sys_spit_mouseout(this,4)' src='images/split/xyy.png'></label>";
                if (iCurrentPage == 1) {
                    strSy = "<label class='fytext1'><img src='images/split/sy1.png'></label>";
                    Syy = "&nbsp;&nbsp;<label class='fytext1'><img src='images/split/syy1.png'></label>";
                }
                if (iCurrentPage == iTotalPage) {
                    Wy = "&nbsp;&nbsp;<label class='fytext1'><img src='images/split/wy1.png'></label>";
                    Xyy = "&nbsp;&nbsp;<label class='fytext1'><img src='images/split/xyy1.png'></label>";
                }
                strSplit = " <div><table id='fytable' class=\"fy\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" align=\"center\" ><form name=\"postForm\" style=\"margin:0px;\" method=post action=\"" + strUrl + "?CURRENTPAGE=" + iCurrentPage + "\">" + strFormParam + "<tr>" + "<td align='left'>\u663e\u793a" + iStartCount + "\uff0d" + (iStartCount + pageSize) + "\u6761,\u5171" + strCount + "\u6761</td><td align='right'><label id='sys_table_label_other_msg'></label>" + strSy + Syy + "&nbsp;&nbsp;\u7b2c<input type='text' name='GOPAGE' value='" + iCurrentPage + "' size='6' class='gotext' onblur=\"document.postForm.OPESTR.value='GO';postForm.submit();\">\u5171" + iTotalPage + "\u9875" + Xyy + Wy + "<input name=\"RECORDCOUNT\" type=\"hidden\" value=\"" + strCount + "\">" + "<input name=\"OPESTR\" type=\"hidden\" value=\"\"></td></tr></form>" + "</table></div>";
                strSplit = String.valueOf(strSplit) + "<script>var i_sys_Dtata_Count=" + strCount + ";</script>";
            }
        }
        else {
            tableEx = this.dbf.query(strSql);
        }
        final String[] arrFiledType = hashHQRC.get("SSQLCON").toString().split(",");
        final EFile eFile = new EFile();
        final int iFieldCount = arrcode.length;
        final int iRecordCount = tableEx.getRecordCount();
        final int iStyleItemCount = _arrItemStyle.length;
        int iFieldIndex = 0;
        for (int i = 0; i < iRecordCount; ++i) {
            final Record record = tableEx.getRecord(i);
            this.sbHtml.append(_arrItemStyle[0]);
            for (int j = 1; j < iStyleItemCount; ++j) {
                iFieldIndex = j - 1;
                if (iFieldIndex < iFieldCount) {
                    Object objValue = record.getFieldByName(arrcode[iFieldIndex]).value;
                    if (objValue != null) {
                        if (!arrDic[iFieldIndex].equals("0")) {
                            if (arrDic[iFieldIndex].startsWith("color.")) {
                                final String strDicCode = arrDic[iFieldIndex].substring(6);
                                objValue = Dic.hashColor.get(String.valueOf(strDicCode) + "_" + objValue);
                            }
                            else {
                                objValue = Dic.getDics(arrDic[iFieldIndex], objValue.toString());
                            }
                        }
                        if (arrFiledType[iFieldIndex].equals("FILE")) {
                            objValue = eFile.getEFiles("uploads" + Dic.strPathSplit + objValue, "*", objValue.toString());
                        }
                        if (arrFiledType[iFieldIndex].equals("MFILE")) {
                            objValue = eFile.getMEFiles("uploads" + Dic.strPathSplit + objValue, "*", objValue.toString());
                        }
                    }
                    if (objValue == null) {
                        objValue = "";
                    }
//                    this.sbHtml.append(objValue);
                    this.sbHtml.append(ApplicationUtils.escapeHTML(String.valueOf(objValue)));
                }
                this.sbHtml.append(_arrItemStyle[j]);
            }
        }
        if (bIsSplit) {
            this.sbHtml.append("<div id='sys_split_msg' style='display:none;'>" + strSplit + "</div>");
        }
        if (bIsDynDB) {
            this.dbf.close();
            this.dbf = dbfTemp;
        }
    }
    
    public StringBuffer getTreeData(final String[] _arrTreeConfig) throws Exception {
        this.treeData = new TreeData();
        this.solveData(_arrTreeConfig[3], _arrTreeConfig[0], _arrTreeConfig[1], _arrTreeConfig[2]);
        return this.treeDataToJs();
    }
    
    public StringBuffer getTreeData(final String _strSql, final String strIdField, final String _strNMField, final String _strPidField) throws Exception {
        this.treeData = new TreeData();
        this.solveData(_strSql, strIdField, _strNMField, _strPidField);
        final StringBuffer vResult = new StringBuffer("var objTreeData=");
        this.generNodes(vResult, "", "", null, false);
        return vResult;
    }
    
    private StringBuffer treeDataToJs() {
        final StringBuffer vResult = new StringBuffer("<script>var objTreeData=");
        this.generNodes(vResult, "", "", null, false);
        return vResult.append("</script>");
    }
    
    private void generNode(final StringBuffer _sbNodes, final String _strName, final String _strValue) {
        _sbNodes.append("'").append(_strName).append("':'").append(_strValue).append("',");
    }
    
    private void gnerNodes(final StringBuffer _sbNodes, final Object _objValue) {
        final Hashtable hashRecord = (Hashtable)_objValue;
        final Enumeration enuFiledNames = hashRecord.keys();
        while (enuFiledNames.hasMoreElements()) {
            final String strFiledCode = enuFiledNames.nextElement().toString();
            Object objFiledValue = hashRecord.get(strFiledCode);
            if (objFiledValue == null) {
                objFiledValue = "";
            }
            this.generNode(_sbNodes, strFiledCode, objFiledValue.toString());
        }
    }
    
    private void generNodes(final StringBuffer _sbNodes, final String _strParentId, final String _strName, final Object _objValue, final boolean _bIsOver) {
        _sbNodes.append("{");
        this.generNode(_sbNodes, "text", _strName);
        this.generNode(_sbNodes, "nodeCode", _strParentId);
        if (_objValue != null) {
            this.gnerNodes(_sbNodes, _objValue);
        }
        _sbNodes.append("children:[");
        final ArrayList arrList = this.treeData.getListChildIds(_strParentId);
        final int iListSize = arrList.size();
        final int iTrueSize = iListSize - 1;
        for (int i = 0; i < iListSize; ++i) {
            final Node node = this.treeData.getChid(arrList.get(i).toString());
            this.generNodes(_sbNodes, node.strNodeId, node.strNodeName, node.objNodeValue, i < iTrueSize);
        }
        if (_bIsOver) {
            _sbNodes.append("]},");
        }
        else {
            _sbNodes.append("]}");
        }
    }
    
    private void solveData(final String _strSql, final String _strCode, final String _strName, final String _strPCode) throws Exception {
        final TableEx tableEx = this.dbf.query(this.getFilterSql(_strSql));
        for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
            final Record record = tableEx.getRecord(i);
            final String strId = record.getFieldByName(_strCode).value.toString();
            final String strPId = record.getFieldByName(_strPCode).value.toString();
            final String strName = record.getFieldByName(_strName).value.toString();
            this.treeData.addListChild(strId, strName, record.clone(), strPId);
        }
    }
    
    public StringBuffer getOrderMsg() throws Exception {
        final StringBuffer vResult = new StringBuffer();
        final String _strSql = "select S_NE_ID,S_CJBG,S_DDWC,F_ZJ from t_check_need where S_NE_ID='<<od>>'";
        final TableEx tableEx = this.dbf.query(this.getFilterSql(_strSql));
        final Record record = tableEx.getRecord(0);
        final String strNeeId = record.getFieldByName("S_NE_ID").value.toString();
        vResult.append("<tr><td colspan='5' align='right'>\u603b\u4ef7:&nbsp;</td><td>" + record.getFieldByName("F_ZJ").value + "</td>");
        vResult.append("</table>   <table style=\"width:900px;height: 160px\"  cellpadding=\"0\" cellspacing=\"0\"> <tr> <td class=\"tdtitle\" colspan=\"4\" align=\"center\">\u68c0\u6d4b\u670d\u52a1\u62a5\u544a</td> </tr>");
        final EFile eFile = new EFile();
        final String[] arrFile = eFile.getFilesName(String.valueOf(Dic.strCurPath) + "uploads" + Dic.strPathSplit + "bg" + strNeeId + "_", "*");
        final int iFileCount = arrFile.length;
        String strBg = record.getFieldByName("S_CJBG").value.toString();
        strBg = strBg.replaceAll("<br>", " ");
        System.out.println("iFileCount" + iFileCount);
        for (int i = 0; i < iFileCount; ++i) {
            vResult.append("<tr> <td class=\"tdtitle\" rowspan=\"2\">\u62a5\u544a\u540d\u79f0</td> <td class=\"linetd\" rowspan=\"2\">").append(arrFile[i].split("\\.")[0]).append("</td> <td class=\"tdtitle\">\u62a5\u544a\u7f16\u53f7:</td> <td class=\"linetd\" width='200' valign='bottom' align='center'>").append(strNeeId).append("_").append(i + 1).append("</td> </tr> <tr> <td class=\"tdtitle\">\u62a5\u544a\u65f6\u95f4:</td> <td class=\"linetd\" valign='bottom' align='center'>").append(strBg).append("</td> </tr>");
        }
        vResult.append("<tr> <td class=\"tdtitle\" colspan=\"4\">\u5907\u6ce8</td> </tr> <tr> <td class=\"linetd\"  height=\"80\" colspan=\"4\"></td> </tr> <tr> <td valign=\"bottom\"class=\"tdtitle\">\u51ed\u8bc1\u53d1\u653e\u5355\u4f4d\uff1a</td> <td valign=\"bottom\"class=\"linetd\">\u5168\u6c47\u6d4b</td> <td valign=\"bottom\"class=\"tdtitle\">\u65e5\u671f\uff1a</td><td valign=\"bottom\"class=\"linetd\" align='center'>");
        vResult.append(record.getFieldByName("S_DDWC").value.toString().replace("<br>", " ")).append("</td></tr>");
        return vResult;
    }
    
    public void doRdt(final String _strRdt) throws Exception {
        final String[] arrRdt = _strRdt.split(":");
        final String strConParam = arrRdt[0];
        final String strConValue = this.getParameter(strConParam);
        if (strConValue != null) {
            for (int iConCount = arrRdt.length, i = 1; i < iConCount; ++i) {
                final String[] arrCon = arrRdt[i].split("=");
                System.out.println(String.valueOf(strConValue) + "=" + arrCon[0] + "to" + arrCon[1]);
                if (strConValue.equals(arrCon[0])) {
                    this.response.sendRedirect(String.valueOf(arrCon[1]) + Pub.getUrlParams(this.request));
                    break;
                }
            }
            return;
        }
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(strConParam);
        final TableEx tableEx = this.gennerTable(hashHQRC);
        if (tableEx.getRecordCount() > 0) {
            final Record record = tableEx.getRecord(0);
            final String strCon = record.getFieldByName(hashHQRC.get("SFIELDCODE").toString().split(",")[0]).value.toString();
            for (int iConCount2 = arrRdt.length, j = 1; j < iConCount2; ++j) {
                final String[] arrCon2 = arrRdt[j].split("=");
                System.out.println(String.valueOf(strCon) + "=" + arrCon2[0] + "to" + arrCon2[1]);
                if (strCon.equals(arrCon2[0])) {
                    this.response.sendRedirect(arrCon2[1]);
                    break;
                }
            }
        }
    }
    
    public StringBuffer genCmp(final String _strFun) throws Exception {
        final StringBuffer vResult = new StringBuffer();
        final String[] arrItem = _strFun.split(",");
        final String strTag = arrItem[0];
        final String strDataId = arrItem[1];
        final String strValue = this.getFilterSql(arrItem[2]);
        final String strValueDo = " " + arrItem[3] + " ";
        final ArrayList aL = Dic.hashDicCode.get(strDataId);
        final ArrayList aLName = Dic.hashDicName.get(strDataId);
        final int iDicCount = aL.size();
        String strSelect = "";
        vResult.append("<").append(strTag).append(" value=''>").append("---\u8bf7\u9009\u62e9---</").append(strTag).append(">");
        for (int d = 0; d < iDicCount; ++d) {
            final String strCode = aL.get(d).toString();
            if (strValue.equals(strCode)) {
                strSelect = strValueDo;
            }
            else {
                strSelect = "";
            }
            vResult.append("<").append(strTag).append(strSelect).append(" value='").append(strCode).append("'>").append(aLName.get(d)).append("</").append(strTag).append(">");
        }
        return vResult;
    }
    
    public StringBuffer doFun(final String _strFun) throws Exception {
        if (_strFun.equals("getUOrder")) {
            return this.getUOrder();
        }
        if (_strFun.equals("getOrderMsg")) {
            return this.getOrderMsg();
        }
        if (_strFun.equals("getWXUOrder")) {
            return this.getWXUOrder();
        }
        if (_strFun.startsWith("getFiles")) {
            return this.getFiles(_strFun);
        }
        if (_strFun.startsWith("listFolders")) {
            return this.listFolders(_strFun);
        }
        if (_strFun.startsWith("getChart")) {
            return this.getChart(_strFun);
        }
        if (_strFun.startsWith("setSession")) {
            return this.setSession(this.getFilterSql(_strFun));
        }
        if (_strFun.startsWith("getAllPass")) {
            return this.getAllPass();
        }
        if (_strFun.startsWith("docConvert")) {
            final String[] arrStrFun = this.getFilterSql(_strFun).split("_");
            if (arrStrFun[3].equals("true")) {
                return this.convertDoc(arrStrFun[1], arrStrFun[2], true);
            }
            return this.convertDoc(arrStrFun[1], arrStrFun[2], false);
        }
        else {
            if (_strFun.equals("")) {
                final String strVerN = String.valueOf(EString.generId()) + Math.random();
                PreloadJS.hashVer.put(strVerN, "");
                return new StringBuffer(strVerN);
            }
            final String[] arrStrFun = _strFun.split("_");
            final int iParamCount = arrStrFun.length;
            final Fun fun = new Fun();
            fun.request = this.request;
            fun.response = this.response;
            final Class objClass = fun.getClass();
            if (iParamCount > 1) {
                final Class[] classParam = new Class[iParamCount - 1];
                final String[] paramValue = new String[iParamCount - 1];
                for (int i = 1; i < iParamCount; ++i) {
                    classParam[i - 1] = String.class;
                    paramValue[i - 1] = arrStrFun[i];
                }
                return (StringBuffer)objClass.getMethod(arrStrFun[0], (Class[])classParam).invoke(fun, (Object[])paramValue);
            }
            return (StringBuffer)Class.forName("com.page.method.Fun").getMethod(arrStrFun[0], (Class<?>[])new Class[0]).invoke(fun, new Object[0]);
        }
    }
    
    public StringBuffer getAllPass() {
        final StringBuffer vResult = new StringBuffer();
        final int iPass = Integer.parseInt(this.request.getSession().getAttribute("SYS_CUR_LOGIN_MEMBER_CURPASS").toString());
        for (int i = 1; i <= 300; ++i) {
            if (i <= iPass) {
                vResult.append("<div style=\"float:left;width:130px;height:20px;margin-left:20px;margin-bottom:20px;overflow:hidden;\"> <a href=\"setpass.v?p=").append(i).append("\" style=\"color:#fff;\">\u7b2c ").append(i).append(" \u5173\u6311\u6218</a> </div>");
            }
            else {
                vResult.append("<div style=\"float:left;width:130px;height:20px;margin-left:20px;margin-bottom:20px;overflow:hidden;\"><font style=\"color:#e1e1e1;\">\u7b2c ").append(i).append(" \u5173\u6311\u6218</font> </div>");
            }
        }
        return vResult;
    }
    
    public StringBuffer setSession(final String _strFun) {
        final StringBuffer vResult = new StringBuffer();
        final String[] arrSessionMsg = _strFun.substring(11).split(";");
        final int iSesionCount = arrSessionMsg.length;
        final HttpSession session = this.request.getSession();
        for (int i = 0; i < iSesionCount; ++i) {
            final String[] arrSession = arrSessionMsg[i].split("=");
            session.setAttribute(arrSession[0], (Object)arrSession[1]);
        }
        return vResult;
    }
    
    public StringBuffer convertDoc(final String _strSrcFile, final String _strToFile, final boolean _bIsView) {
        final StringBuffer vResult = new StringBuffer();
        final String strUpcaseFile = _strToFile.toUpperCase();
        final String strSource = String.valueOf(Dic.strCurPath) + _strSrcFile;
        final String strToFile = String.valueOf(Dic.strCurPath) + _strToFile;
        int iPageCount = -1;
        try {
            if (strUpcaseFile.endsWith(".HTML")) {
                iPageCount = EFile.docConvert(strSource, strToFile, 2);
                if (_bIsView) {
                    this.response.sendRedirect(URLEncoder.encode(_strToFile, "utf-8"));
                }
                else {
                    vResult.append("true:" + iPageCount);
                }
            }
            else if (strUpcaseFile.endsWith(".PDF")) {
                iPageCount = EFile.docConvert(strSource, strToFile, 1);
                if (_bIsView) {
                    this.response.sendRedirect(URLEncoder.encode(_strToFile, "utf-8"));
                }
                else {
                    vResult.append("true");
                }
            }
            else if (strUpcaseFile.endsWith(".JPG")) {
                iPageCount = EFile.docConvert(strSource, strToFile, 3);
                if (_bIsView) {
                    final String strPicName = _strToFile.split("\\.")[0];
                    vResult.append("<html><body style='background:#fafafa;'><table align='center'>");
                    for (int i = 0; i < iPageCount; ++i) {
                        vResult.append("<tr><td><img style='border:1px solid #c6c6c6;margin-top:10px;' src='" + strPicName + "_" + i + ".jpg'></td></tr>");
                    }
                    vResult.append("</table></body></html>");
                }
                else {
                    vResult.append("true:" + iPageCount);
                }
            }
        }
        catch (Exception ex) {}
        return vResult;
    }
    
    private StringBuffer getChart(final String _strFun) {
        if (this.cV == null) {
            this.cV = new CharView();
        }
        final ChartData chartData = new ChartData(_strFun.split("_")[1], this.request);
        this.cV.setData(chartData);
        return this.cV.getChart();
    }
    
    private StringBuffer listFolders(final String _strFun) {
        final String[] arrStrFun = _strFun.split("_");
        final int iParamCount = arrStrFun.length;
        final EFile eFile = new EFile();
        final HashMap hashDic = new HashMap();
        if (iParamCount > 2) {
            final ParamTree pTree = new ParamTree();
            pTree.request = this.request;
            final TableEx tableEx = pTree.getTableEx(arrStrFun[2]);
            try {
                for (int iDicCount = tableEx.getRecordCount(), i = 0; i < iDicCount; ++i) {
                    final Record record = tableEx.getRecord(i);
                    hashDic.put(record.getFieldByName("CODE").value, record.getFieldByName("NAME").value);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                tableEx.close();
            }
        }
        final String strFolder = this.getParameter(arrStrFun[1]);
        final String strPath = "uploads" + Dic.strPathSplit + strFolder;
        return eFile.listFolders(strPath, strFolder, hashDic);
    }
    
    private StringBuffer getFiles(final String _strFun) {
        final String[] arrStrFun = _strFun.split("_");
        final int iParamCount = arrStrFun.length;
        final EFile eFile = new EFile();
        String strPath = "uploads";
        String strFolder = "";
        for (int i = 1; i < iParamCount; ++i) {
            final String strFd = this.getParameter(arrStrFun[i]);
            strPath = String.valueOf(strPath) + Dic.strPathSplit + strFd;
            strFolder = String.valueOf(strFolder) + "/" + strFd;
        }
        return eFile.getEFiles(strPath, "*", strFolder);
    }
    
    public StringBuffer getUOrder() throws Exception {
        final StringBuffer vResult = new StringBuffer();
        final HttpSession session = this.request.getSession();
        String _strSql = "select S_STATUS, S_NE_ID,S_JCZT,FROM_UNIXTIME(S_NE_ID/1000,'%Y\u5e74%m\u6708%d\u65e5 %H:%i:%S') ddsj,S_XMXX,I_YPSL,F_JG,F_ZJ from t_check_need,t_jcxm where S_NE_ID=S_NEED_ID and S_USER='<<SYS_STRCURUSER>>' and S_STATUS<>'WCLDD' order by S_NE_ID desc";
        boolean bIsYh = true;
        if (session.getAttribute("SYS_IS_SYS_U").toString().equals("1")) {
            _strSql = "select S_STATUS, S_NE_ID,S_JCZT,FROM_UNIXTIME(S_NE_ID/1000,'%Y\u5e74%m\u6708%d\u65e5 %H:%i:%S') ddsj,S_XMXX,I_YPSL,F_JG,F_ZJ from t_check_need,t_jcxm where S_NE_ID=S_NEED_ID and S_HZSYS='<<SYS_STRCURUSER>>' and S_STATUS<>'WCLDD' order by S_NE_ID desc";
            bIsYh = false;
        }
        final TableEx tableEx = this.dbf.query(this.getFilterSql(_strSql));
        final int iCount = tableEx.getRecordCount();
        String strParentOrd = "";
        StringBuffer sbRight = new StringBuffer();
        int iRightRowSpan = 0;
        for (int i = 0; i < iCount; ++i) {
            final Record record = tableEx.getRecord(i);
            final String strNeId = record.getFieldByName("S_NE_ID").value.toString();
            final String strStatus = record.getFieldByName("S_STATUS").value.toString();
            if (!strParentOrd.equals(strNeId)) {
                if (iRightRowSpan != 0) {
                    vResult.append("<td rowspan='").append(iRightRowSpan).append("' ").append(sbRight);
                }
                if (i > 0) {
                    vResult.append("<tr style=''><td colspan='4'><hr style='height:1px;border:none;border-top:1px dashed gray;' /></td></tr>");
                }
                vResult.append("<tr style='background:#e7f5fe;'><td colspan='4' class='orderstyle' style='height:55px;line-height:25px;'>");
                vResult.append("&nbsp;&nbsp;\u8ba2\u5355\u53f7 :&nbsp;<a class='orderstyle' href='ddxq.view?od=").append(strNeId).append("' target='_blank'>").append(strNeId).append("</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\u8ba2\u5355\u65f6\u95f4 :&nbsp; ").append(record.getFieldByName("ddsj").value);
                vResult.append("<br>&nbsp;&nbsp;\u68c0\u6d4b\u4e3b\u9898 :&nbsp;").append(record.getFieldByName("S_JCZT").value);
                vResult.append("</td></tr>");
                vResult.append("<tr>");
                vResult.append("<td class='orderxm'>&nbsp;&nbsp;");
                vResult.append(Dic.hash.get("T_CHECK_TYPE_" + record.getFieldByName("S_XMXX").value));
                vResult.append("</td> <td class='orderypjg'>X");
                vResult.append(record.getFieldByName("I_YPSL").value);
                vResult.append("</td> <td class='orderypjg'>");
                vResult.append(record.getFieldByName("F_JG").value);
                vResult.append("</td>");
                strParentOrd = strNeId;
                sbRight = new StringBuffer("class='orderbttnpanel' align='right' valign='middle'><table width='100%'><tr><td width='33%' align='center'>");
                sbRight.append(record.getFieldByName("F_ZJ").value);
                sbRight.append("</td><td width='33%' align='center'>");
                if (strStatus.equals("DDSC")) {
                    sbRight.append("\u672a\u4ed8\u6b3e");
                }
                else {
                    sbRight.append(Dic.hash.get("1480583063430_" + strStatus));
                }
                sbRight.append("</td><td width='33%' align='center'>");
                if (bIsYh && strStatus.equals("DDSC")) {
                    sbRight.append("<a href=\"ddfk.view?od=" + strNeId + "\" target=\"_blank\"><button class='titlebg' style=\"cursor:pointer;\">\u4ed8\u6b3e</button></a><br><br>");
                }
                if (strStatus.equals("DDWC") && !bIsYh) {
                    sbRight.append("<a href=\"ddpz.view?od=" + strNeId + "\" target=\"_blank\"><button class='titlebg' style=\"cursor:pointer;\">\u6253\u5370\u8ba2\u5355\u51ed\u8bc1</button></a><br><br>");
                }
                sbRight.append("<button class='titlebg2' style=\"cursor:pointer;\" onclick=\"nTalk.im_openInPageChat();\">\u6c9f\u901a\u5ba2\u670d</button>");
                sbRight.append("</td></tr></table> </td></tr>");
                iRightRowSpan = 1;
            }
            else {
                if (iRightRowSpan % 2 != 0) {
                    sbRight.append("<tr style='background:#f6f7f9'>");
                }
                else {
                    sbRight.append("<tr>");
                }
                sbRight.append("<td  class='orderxm'>&nbsp;&nbsp;");
                sbRight.append(Dic.hash.get("T_CHECK_TYPE_" + record.getFieldByName("S_XMXX").value));
                sbRight.append("</td> <td class='orderypjg'>X");
                sbRight.append(record.getFieldByName("I_YPSL").value);
                sbRight.append("</td> <td class='orderypjg'>");
                sbRight.append(record.getFieldByName("F_JG").value);
                sbRight.append("</td></tr>");
                ++iRightRowSpan;
            }
        }
        if (iRightRowSpan != 0) {
            vResult.append("<td rowspan='").append(iRightRowSpan).append("' ").append(sbRight);
        }
        return vResult;
    }
    
    public StringBuffer getWXUOrder() throws Exception {
        final StringBuffer vResult = new StringBuffer();
        final HttpSession session = this.request.getSession();
        String _strSql = "select S_STATUS,S_JCZT, S_NE_ID,FROM_UNIXTIME(S_NE_ID/1000,'%Y\u5e74%m\u6708%d\u65e5 %H:%i:%S') ddsj,S_XMXX,I_YPSL,F_JG,F_ZJ from t_check_need,t_jcxm where S_NE_ID=S_NEED_ID and S_USER='<<SYS_STRCURUSER>>' and S_STATUS<>'WCLDD' order by S_NE_ID desc";
        boolean bIsYh = true;
        if (session.getAttribute("SYS_IS_SYS_U").toString().equals("1")) {
            _strSql = "select S_STATUS,S_JCZT, S_NE_ID,FROM_UNIXTIME(S_NE_ID/1000,'%Y\u5e74%m\u6708%d\u65e5 %H:%i:%S') ddsj,S_XMXX,I_YPSL,F_JG,F_ZJ from t_check_need,t_jcxm where S_NE_ID=S_NEED_ID and S_HZSYS='<<SYS_STRCURUSER>>' and S_STATUS<>'WCLDD' order by S_NE_ID desc";
            bIsYh = false;
        }
        final TableEx tableEx = this.dbf.query(this.getFilterSql(_strSql));
        final int iCount = tableEx.getRecordCount();
        String strParentOrd = "";
        StringBuffer sbRight = new StringBuffer();
        final int iRightRowSpan = 0;
        for (int i = 0; i < iCount; ++i) {
            final Record record = tableEx.getRecord(i);
            final String strNeId = record.getFieldByName("S_NE_ID").value.toString();
            final String strStatus = record.getFieldByName("S_STATUS").value.toString();
            if (!strParentOrd.equals(strNeId)) {
                if (i != 0) {
                    vResult.append(sbRight);
                }
                vResult.append("<tr><td colspan='3' height='30px'><br>");
                vResult.append("<table width='100%'><tr><td align='left'><b>\u8ba2\u5355\u53f7 :&nbsp;<a href='xqxqwx.w?od=" + strNeId + "'>").append(strNeId).append("</a></b></td><td align='right'><b>").append(record.getFieldByName("ddsj").value);
                vResult.append("</b></td></tr></table></td></tr>");
                sbRight = new StringBuffer("<tr><td colspan='3'  height='70px'><br><table width='100%'><tr><td width='50%' align='left'>\u603b\u4ef7:");
                sbRight.append(record.getFieldByName("F_ZJ").value);
                sbRight.append("</td><td width='50%' align='right'>\u72b6\u6001:");
                if (strStatus.equals("DDSC")) {
                    sbRight.append("\u672a\u4ed8\u6b3e");
                }
                else {
                    sbRight.append(Dic.hash.get("1480583063430_" + strStatus));
                }
                sbRight.append("</td></tr><tr><td colspan='2' align='center'>");
                if (bIsYh && strStatus.equals("DDSC")) {
                    sbRight.append("<button onclick=\"window.location='p/pay.jsp?o=" + strNeId + "&f=" + record.getFieldByName("F_ZJ").value + "'\" class='bttn' style='width:40%;background:#3a82d4;'>\u4ed8\u6b3e</button>");
                }
                if (strStatus.equals("DDWC")) {
                    sbRight.append("<br>\u62a5\u544a\u72b6\u6001:\u62a5\u544a\u5df2\u751f\u6210\uff0c\u8bf7\u767b\u5f55\u7f51\u7ad9\u4e0b\u8f7d");
                }
                sbRight.append("</td></tr></table></td></tr><tr><td colspan='3' style='border-bottom:1px solid #f6f7f9;height:10px;'></td></tr>");
                strParentOrd = strNeId;
            }
            vResult.append("<tr>");
            vResult.append("<td height='30px'>&nbsp;&nbsp;");
            vResult.append(Dic.hash.get("T_CHECK_TYPE_" + record.getFieldByName("S_XMXX").value));
            vResult.append("</td> <td height='30px'>X");
            vResult.append(record.getFieldByName("I_YPSL").value);
            vResult.append("</td> <td height='30px'>");
            vResult.append(record.getFieldByName("F_JG").value);
            vResult.append("</td></tr>");
        }
        if (!sbRight.toString().equals("")) {
            vResult.append(sbRight);
        }
        vResult.append("</table>");
        return vResult;
    }
    
    public static void main(final String[] args) {
    }
}
