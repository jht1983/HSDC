/**
 * 
 */
package com.yulongtao.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Debug;
import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.PubFun;
import com.yulongtao.pub.SqlCommand;
import com.yulongtao.sys.QRSC;
import com.yulongtao.sys.SumToSub;
import com.yulongtao.sys.WebCommand;
import com.yulongtao.util.EString;
import com.yulongtao.util.MisSerialUtil;

/**
 * @author tianshisheng
 *
 */
public class MisRequestEx {
    private HttpServletRequest request;
    private Hashtable requestHash;
    private String sqlStr;
    private Hashtable hashCodition;
    private DBFactory dbff;
    private String strNoCon;
    private String strChars;
    private boolean bIsDynDB;
    private String strDBName;
    private String strTables;
    public HttpSession session;
    
    public void setDb(final String _strName) {
        this.dbff = new DBFactory(_strName, 1);
        this.bIsDynDB = true;
        this.strDBName = _strName;
    }
    
    public static void getRequestStr(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Writer out = response.getWriter();
        final Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            final String paraName = paramNames.nextElement().toString();
            out.write("String " + paraName + "=requestEx.getParameter(\"" + paraName + "\");<br>");
            out.flush();
        }
    }
    
    public static void getFormStr(final String aTableName, final HttpServletResponse response) throws Exception {
        final Writer out = response.getWriter();
        final TableEx tableMsg = new TableEx(aTableName);
        final int colCount = tableMsg.getColCount();
        final Hashtable hashMsg = tableMsg.getTableMsg();
        final String strIsInt = "";
        out.write("<script LANGUAGE='jscript' SRC='/rep/INCLUDE/jFunction.js'></script>");
        out.write("<link href='/rep/INCLUDE/page.css' rel='stylesheet' type='text/css'>");
        out.write("<SCRIPT LANGUAGE=\"JavaScript\">");
        out.write("function checkvalue(){");
        out.write("if(info.SMODNAME.value=='')");
        out.write("{");
        out.write("alert(\"\u8bf7\u8f93\u5165\u6a21\u5757\u540d\u79f0\");");
        out.write("document.info.SMODNAME.focus();");
        out.write("return false; ");
        out.write("}\t");
        out.write("}");
        out.write("</SCRIPT>");
        for (int i = 1; i <= colCount; ++i) {
            final FieldEx paraName = (FieldEx) hashMsg.get(new StringBuilder().append(i).toString());
            if (paraName.isInt() == 1) {
                out.write("<input type='text' name='" + paraName.fieldName + "' onFocus='this.select()' onBlur='numberCheck(this)' class='textfiled'>\n");
            }
            else if (paraName.isInt() == 2) {
                out.write("<input type='text' name='" + paraName.fieldName + "' onFocus='this.select()' class='textfiled' readonly ><IMG style='CURSOR: hand' onclick=BrowseDate(" + paraName.fieldName + ") height=16 src='/images/datetime.gif' width  =16 align=absMiddle  false ;return>\n");
            }
            else {
                out.write("<input type='text' name='" + paraName.fieldName + "' onFocus='this.select()' class='textfiled'>\n");
            }
        }
        out.write("<input name='' type='submit' value='ok'  class='buttonfiled'>");
    }
    
    public void deleteSolveTable(final String _strTable) {
        this.strTables = this.strTables.replaceAll(String.valueOf(_strTable) + ":", "");
        if (this.strTables.endsWith(":" + _strTable)) {
            this.strTables = this.strTables.replaceAll(":" + _strTable, "");
        }
    }
    
    private String getStringByArray(final String[] _arrStr) {
        final StringBuffer vResult = new StringBuffer();
        final int iLength = _arrStr.length;
        String strSplit = "";
        for (int i = 0; i < iLength; ++i) {
            vResult.append(strSplit);
            vResult.append(_arrStr[i]);
            strSplit = ",";
        }
        return vResult.toString();
    }
    
    public MisRequestEx(final HttpServletRequest aRequest) throws Exception {
        this.request = null;
        this.requestHash = new Hashtable();
        this.sqlStr = "";
        this.hashCodition = new Hashtable();
        this.dbff = new DBFactory();
        this.strNoCon = "";
        this.strChars = "GBK";
        this.bIsDynDB = false;
        this.strDBName = "";
        this.strTables = "";
        this.request = aRequest;
        this.dbff.session = this.request.getSession();
        this.strNoCon = this.request.getParameter("NO_CON");
        final String strCharSet = this.request.getParameter("NO_charset");
        if (strCharSet != null) {
            this.strChars = strCharSet;
        }
        final Enumeration paramNames = this.request.getParameterNames();
        this.strTables = "";
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement().toString();
            if (paraName.startsWith("NO_")) {
                continue;
            }
            if (paraName.equals("SysRedirect")) {
                continue;
            }
            String paraValue = this.request.getParameter(paraName);
            if (paraName.startsWith("CK_")) {
                paraValue = this.getStringByArray(this.request.getParameterValues(paraName));
                paraName = paraName.substring(3);
            }
            if (paraValue == null) {
                continue;
            }
            final String[] arrTablField = paraName.split("III");
            for (int i = 0; i < arrTablField.length; ++i) {
                final String[] arrTables = arrTablField[i].split("\\$");
                if (this.strTables.indexOf(String.valueOf(arrTables[0]) + ":") == -1) {
                    this.strTables = String.valueOf(this.strTables) + arrTables[0] + ":";
                }
            }
            System.out.println("----------------------------------" + paraValue);
            this.requestHash.put(paraName, EString.encoderStr(paraValue, this.strChars));
        }
        if (!this.strTables.equals("")) {
            this.strTables = this.strTables.substring(0, this.strTables.length() - 1);
        }
    }
    
    public MisRequestEx(final HttpServletRequest aRequest, final String aStrChars) throws Exception {
        this.request = null;
        this.requestHash = new Hashtable();
        this.sqlStr = "";
        this.hashCodition = new Hashtable();
        this.dbff = new DBFactory();
        this.strNoCon = "";
        this.strChars = "GBK";
        this.bIsDynDB = false;
        this.strDBName = "";
        this.strTables = "";
        this.strChars = aStrChars;
        this.request = aRequest;
        this.strNoCon = this.request.getParameter("NO_CON");
        final Enumeration paramNames = this.request.getParameterNames();
        this.strTables = "";
        while (paramNames.hasMoreElements()) {
            final String paraName = paramNames.nextElement().toString();
            if (paraName.startsWith("NO_")) {
                continue;
            }
            if (paraName.equals("SysRedirect")) {
                continue;
            }
            final String paraValue = this.request.getParameter(paraName);
            if (paraValue == null) {
                continue;
            }
            final String[] arrTablField = paraName.split("III");
            for (int i = 0; i < arrTablField.length; ++i) {
                final String[] arrTables = arrTablField[i].split("\\$");
                if (this.strTables.indexOf(arrTables[0]) == -1) {
                    this.strTables = String.valueOf(this.strTables) + ":" + arrTables[0];
                }
            }
            this.requestHash.put(paraName, EString.encoderStr(paraValue, this.strChars));
        }
        if (!this.strTables.equals("")) {
            this.strTables = this.strTables.substring(1);
        }
    }
    
    public String deleteParam(final String aParamName) throws Exception {
        String vResultStr = "";
        try {
            vResultStr = this.requestHash.get(aParamName).toString();
        }
        catch (Exception e) {
            Debug.println("\u8b66\u544a\uff01\u6ca1\u6709" + aParamName + "\u8fd9\u4e2a\u53c2\u6570\uff01");
        }
        this.requestHash.remove(aParamName);
        return vResultStr;
    }
    
    public String getParameter(final String aParamName) throws Exception {
        String strValue = this.request.getParameter(aParamName);
        if (strValue == null) {
            strValue = "";
        }
        strValue = EString.encoderStr(strValue, this.strChars);
        return strValue;
    }
    
    public void clearParam() {
        this.requestHash = new Hashtable();
    }
    
    public boolean solveSimpleTable() throws Exception {
        final boolean vResult = false;
        final HashMap hashFieldValue = new HashMap();
        final String[] arrStrTabls = this.strTables.split(":");
        final int iTableCount = arrStrTabls.length;
        final TableEx[] tables = new TableEx[iTableCount];
        final String strBachTable = this.request.getParameter("NO_ISBACH");
        final HashMap hashIsBach = new HashMap();
        if (strBachTable != null) {
            final String[] arrTempBachTables = strBachTable.split(",");
            for (int iBachTableCount = arrTempBachTables.length, i = 0; i < iBachTableCount; ++i) {
                hashIsBach.put(arrTempBachTables[i], "");
            }
        }
        for (int j = 0; j < iTableCount; ++j) {
            if (this.bIsDynDB) {
                tables[j] = new TableEx(arrStrTabls[j], this.strDBName, 1);
            }
            else {
                tables[j] = new TableEx(arrStrTabls[j]);
            }
            final Record record = new Record();
            final Enumeration keys = this.requestHash.keys();
            while (keys.hasMoreElements()) {
                final String keyName = keys.nextElement().toString();
                final String[] arrTablField = keyName.split("III");
                for (int k = 0; k < arrTablField.length; ++k) {
                    final String[] arrTables = arrTablField[k].split("\\$");
                    if (arrTables[0].equals(arrStrTabls[j]) && tables[j].haveFieldByName(arrTables[1])) {
                        Object objValue = this.requestHash.get(keyName);
                        if (objValue != null && objValue.toString().startsWith("GET_")) {
                            final String strGetFieldName = objValue.toString().substring(4);
                            objValue = this.requestHash.get(strGetFieldName);
                            if (objValue != null && objValue.toString().startsWith("4$INCREMOD_")) {
                                final Object objTempFieldValu = hashFieldValue.get(strGetFieldName);
                                if (objTempFieldValu != null) {
                                    objValue = objTempFieldValu;
                                }
                                else {
                                    final String[] arrTF = objValue.toString().split("\\$");
                                    final int iTFCount = arrTF.length;
                                    if (iTFCount == 3) {
                                        objValue = MisSerialUtil.getSerialNum(arrTF[2], this.request);
                                    }
                                    else if (iTFCount > 5) {
                                        objValue = EString.generId(arrTF[2].trim(), arrTF[3].trim(), arrTF[4].trim(), Integer.parseInt(arrTF[5].trim()));
                                    }
                                    else {
                                        objValue = EString.generId(arrTF[2].trim(), arrTF[3].trim(), arrTF[4].trim());
                                    }
                                    hashFieldValue.put(strGetFieldName, objValue);
                                }
                            }
                        }
                        else if (objValue != null && objValue.toString().startsWith("TNGLOB_")) {
                            final String strFieldTempValue = objValue.toString().substring(7);
                            final int iIndexField = strFieldTempValue.indexOf("$");
                            String strFieldParam = strFieldTempValue.substring(0, iIndexField);
                            objValue = this.requestHash.get(String.valueOf(arrTables[0]) + "$" + strFieldParam);
                            strFieldParam = strFieldTempValue.substring(iIndexField + 1);
                            if (!strFieldParam.equals("")) {
                                objValue = String.valueOf(strFieldParam) + "/" + objValue;
                            }
                        }
                        else if (objValue != null && objValue.toString().startsWith("PY_")) {
                            objValue = EString.getPy(this.requestHash.get(objValue.toString().substring(3)).toString());
                        }
                        else if (objValue != null && objValue.toString().startsWith("NPY_")) {
                            objValue = EString.getPy(this.requestHash.get(objValue.toString().substring(4)).toString());
                            objValue = this.generFieldValue(keyName, objValue.toString());
                        }
                        else if (objValue != null && objValue.toString().startsWith("4$INCREMOD_")) {
                            final Object objTempFieldValu2 = hashFieldValue.get(keyName);
                            if (objTempFieldValu2 != null) {
                                objValue = objTempFieldValu2;
                            }
                            else {
                                final String[] arrTF2 = objValue.toString().split("\\$");
                                final int iTFCount2 = arrTF2.length;
                                if (iTFCount2 == 3) {
                                    objValue = MisSerialUtil.getSerialNum(arrTF2[2], this.request);
                                    System.out.println("seria" + objValue);
                                }
                                else if (arrTF2.length > 5) {
                                    objValue = EString.generId(arrTF2[2].trim(), arrTF2[3].trim(), arrTF2[4].trim(), Integer.parseInt(arrTF2[5].trim()));
                                }
                                else {
                                    objValue = EString.generId(arrTF2[2].trim(), arrTF2[3].trim(), arrTF2[4].trim());
                                }
                                hashFieldValue.put(keyName, objValue);
                            }
                        }
                        record.addField(new FieldEx(arrTables[1], objValue));
                    }
                }
            }
            final String strStreamField = this.request.getParameter("NO_SYS_STREAM_FD");
            if (strStreamField != null && !strStreamField.equals("")) {
                final String[] arrTableField = strStreamField.split("\\$");
                if (arrStrTabls[j].equals(arrTableField[0])) {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)this.request.getInputStream()));
                    String strJson = null;
                    final StringBuilder result = new StringBuilder();
                    try {
                        while ((strJson = reader.readLine()) != null) {
                            result.append(strJson);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        reader.close();
                    }
                    record.addField(new FieldEx(arrTableField[1], new String(result.toString().getBytes(), "utf-8")));
                }
            }
            tables[j].addRecord(record);
            if (j == iTableCount - 1) {
                this.doSqlFun(record, hashFieldValue, this.request.getParameter("NO_sys_docommand"));
                this.dbff.solveTable(tables[j], true);
                this.doSqlFun(record, hashFieldValue, this.request.getParameter("NO_sys_finish_docommand"));
            }
            else {
                this.dbff.solveTable(tables[j], false);
            }
        }
        return vResult;
    }
    
    public boolean solveTable() throws Exception {
        final boolean vResult = false;
        final String strIsEmtyIgnore = this.request.getParameter("NO_sys_Emty_Ignore");
        final HashMap hahsEmtyIgnore = new HashMap();
        if (strIsEmtyIgnore != null) {
            final String[] arrEmtyIngore = strIsEmtyIgnore.split(";");
            for (int iLength = arrEmtyIngore.length, i = 0; i < iLength; ++i) {
                final String[] arrTableField = arrEmtyIngore[i].split(":");
                hahsEmtyIgnore.put(arrTableField[0], arrTableField[1]);
            }
        }
        final HashMap hashFieldValue = new HashMap();
        final String[] arrStrTabls = this.strTables.split(":");
        final int iTableCount = arrStrTabls.length;
        final TableEx[] tables = new TableEx[iTableCount];
        final String strBachTable = this.request.getParameter("NO_ISBACH");
        final HashMap hashIsBach = new HashMap();
        if (strBachTable != null) {
            final String[] arrTempBachTables = strBachTable.split(",");
            for (int iBachTableCount = arrTempBachTables.length, j = 0; j < iBachTableCount; ++j) {
                hashIsBach.put(arrTempBachTables[j], "");
            }
        }
        boolean bIsBatch = false;
        Vector vecBachKey = new Vector();
        for (int j = 0; j < iTableCount; ++j) {
            if (this.bIsDynDB) {
                tables[j] = new TableEx(arrStrTabls[j], this.strDBName, 1);
            }
            else {
                tables[j] = new TableEx(arrStrTabls[j]);
            }
            Record record = new Record();
            final Enumeration keys = this.requestHash.keys();
            if (hashIsBach.get(arrStrTabls[j]) != null) {
                bIsBatch = true;
                vecBachKey = new Vector();
            }
            else {
                bIsBatch = false;
            }
            while (keys.hasMoreElements()) {
                final String keyName = keys.nextElement().toString();
                final String[] arrTablField = keyName.split("III");
                for (int k = 0; k < arrTablField.length; ++k) {
                    final String[] arrTables = arrTablField[k].split("\\$");
                    if (arrTables[0].equals(arrStrTabls[j]) && tables[j].haveFieldByName(arrTables[1])) {
                        if (bIsBatch) {
                            vecBachKey.add(keyName);
                        }
                        else {
                            Object objValue = this.requestHash.get(keyName);
                            if (objValue != null && objValue.toString().startsWith("GET_")) {
                                final String strGetFieldName = objValue.toString().substring(4);
                                objValue = this.requestHash.get(strGetFieldName);
                                if (objValue != null && objValue.toString().startsWith("4$INCREMOD_")) {
                                    final Object objTempFieldValu = hashFieldValue.get(strGetFieldName);
                                    if (objTempFieldValu != null) {
                                        objValue = objTempFieldValu;
                                    }
                                    else {
                                        final String[] arrTF = objValue.toString().split("\\$");
                                        final int iTFCount = arrTF.length;
                                        if (iTFCount == 3) {
                                            objValue = MisSerialUtil.getSerialNum(arrTF[2], this.request);
                                        }
                                        else if (iTFCount > 5) {
                                            objValue = EString.generId(arrTF[2].trim(), arrTF[3].trim(), arrTF[4].trim(), Integer.parseInt(arrTF[5].trim()));
                                        }
                                        else {
                                            objValue = EString.generId(arrTF[2].trim(), arrTF[3].trim(), arrTF[4].trim());
                                        }
                                        hashFieldValue.put(strGetFieldName, objValue);
                                    }
                                }
                            }
                            else if (objValue != null && objValue.toString().startsWith("TNGLOB_")) {
                                final String strFieldTempValue = objValue.toString().substring(7);
                                final int iIndexField = strFieldTempValue.indexOf("$");
                                String strFieldParam = strFieldTempValue.substring(0, iIndexField);
                                objValue = this.requestHash.get(String.valueOf(arrTables[0]) + "$" + strFieldParam);
                                strFieldParam = strFieldTempValue.substring(iIndexField + 1);
                                if (!strFieldParam.equals("")) {
                                    objValue = String.valueOf(strFieldParam) + "/" + objValue;
                                }
                            }
                            else if (objValue != null && objValue.toString().startsWith("PY_")) {
                                objValue = EString.getPy(this.requestHash.get(objValue.toString().substring(3)).toString());
                            }
                            else if (objValue != null && objValue.toString().startsWith("NPY_")) {
                                objValue = EString.getPy(this.requestHash.get(objValue.toString().substring(4)).toString());
                                objValue = this.generFieldValue(keyName, objValue.toString());
                            }
                            else if (objValue != null && objValue.toString().startsWith("4$INCREMOD_")) {
                                final Object objTempFieldValu2 = hashFieldValue.get(keyName);
                                if (objTempFieldValu2 != null) {
                                    objValue = objTempFieldValu2;
                                }
                                else {
                                    final String[] arrTF2 = objValue.toString().split("\\$");
                                    final int iTFCount2 = arrTF2.length;
                                    if (iTFCount2 == 3) {
                                        objValue = MisSerialUtil.getSerialNum(arrTF2[2], this.request);
                                        System.out.println("seria" + objValue);
                                    }
                                    else if (arrTF2.length > 5) {
                                        objValue = EString.generId(arrTF2[2].trim(), arrTF2[3].trim(), arrTF2[4].trim(), Integer.parseInt(arrTF2[5].trim()));
                                    }
                                    else {
                                        objValue = EString.generId(arrTF2[2].trim(), arrTF2[3].trim(), arrTF2[4].trim());
                                    }
                                    hashFieldValue.put(keyName, objValue);
                                }
                            }
                            record.addField(new FieldEx(arrTables[1], objValue));
                        }
                    }
                }
            }
            if (bIsBatch) {
                final int iParmCount = vecBachKey.size();
                final int iParmValueCount = this.request.getParameterValues(vecBachKey.get(0).toString()).length;
                final String[][] arrBachKeyValues = new String[iParmCount][iParmValueCount];
                for (int l = 0; l < iParmCount; ++l) {
                    final String strBachKey = vecBachKey.get(l).toString();
                    final String[] arrBachKeyValue = this.request.getParameterValues(strBachKey);
                    arrBachKeyValues[l] = arrBachKeyValue;
                }
                for (int o = 0; o < iParmValueCount; ++o) {
                    record = new Record();
                    for (int m = 0; m < iParmCount; ++m) {
                        final String strBachKey2 = vecBachKey.get(m).toString();
                        String strValue = EString.encoderStr(arrBachKeyValues[m][o], this.strChars);
                        if (strValue.startsWith("GET_")) {
                            final String strGetFieldName2 = strValue.toString().substring(4);
                            final Object objGetValue = this.requestHash.get(strGetFieldName2);
                            if (objGetValue == null) {
                                throw new Exception("\u83b7\u53d6\u540c\u6b65\u6570\u636e\u9519\u8bef\uff01[" + strBachKey2 + "=" + strGetFieldName2 + "]");
                            }
                            strValue = objGetValue.toString();
                            if (strValue.startsWith("4$INCREMOD_")) {
                                strValue = hashFieldValue.get(strGetFieldName2).toString();
                            }
                        }
                        else if (strValue.startsWith("4$INCREMOD_")) {
                            final Object objTempFieldValu3 = hashFieldValue.get(strBachKey2);
                            if (objTempFieldValu3 != null) {
                                strValue = objTempFieldValu3.toString();
                            }
                            else {
                                final String[] arrTF3 = strValue.split("\\$");
                                final int iTFCount3 = arrTF3.length;
                                if (iTFCount3 == 3) {
                                    strValue = MisSerialUtil.getSerialNum(arrTF3[2], this.request);
                                }
                                else if (arrTF3.length > 5) {
                                    strValue = EString.generId(arrTF3[2].trim(), arrTF3[3].trim(), arrTF3[4].trim(), Integer.parseInt(arrTF3[5].trim()));
                                }
                                else {
                                    strValue = EString.generId(arrTF3[2].trim(), arrTF3[3].trim(), arrTF3[4].trim());
                                }
                                hashFieldValue.put(strBachKey2, strValue);
                            }
                        }
                        record.addField(new FieldEx(strBachKey2.split("\\$")[1], strValue));
                    }
                    final Object objEmptyIgnore = hahsEmtyIgnore.get(tables[j].strTableName);
                    boolean bIsAdd = true;
                    if (objEmptyIgnore != null) {
                        final String[] arrIgnore = objEmptyIgnore.toString().split(",");
                        for (int iIgnoreLength = arrIgnore.length, k2 = 0; k2 < iIgnoreLength; ++k2) {
                            if (record.bFieldIsEmty(arrIgnore[k2])) {
                                bIsAdd = false;
                                break;
                            }
                        }
                    }
                    if (bIsAdd) {
                        tables[j].addRecord(record);
                    }
                }
            }
            else {
                tables[j].addRecord(record);
            }
            if (j == iTableCount - 1) {
                this.doSqlFun(record, hashFieldValue, this.request.getParameter("NO_sys_docommand"));
                this.dbff.solveTable(tables[j], true);
                this.doSqlFun(record, hashFieldValue, this.request.getParameter("NO_sys_finish_docommand"));
            }
            else {
                this.dbff.solveTable(tables[j], false);
            }
        }
        return vResult;
    }
    
    private void doSqlFun(final Record _record, final HashMap hashFieldValue, final String _strComMand) throws Exception {
        if (_strComMand != null && !_strComMand.equals("")) {
            if (_strComMand.startsWith("fun_")) {
                new PubFun().doFun(this.request, _strComMand, _record, hashFieldValue);
            }
            else {
                SqlCommand.initSqlMand();
                final String strSql = SqlCommand.getSql(_strComMand);
                this.doSql(strSql, hashFieldValue);
            }
        }
    }
    
    private String generFieldValue(final String _strKeyName, final String _strValue) {
        String vResult = _strValue;
        final String[] arrTableField = _strKeyName.split("\\$");
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("max(substring(" + arrTableField[1] + "," + (_strValue.length() + 1) + ")) MV", arrTableField[0], "N_" + arrTableField[1] + "='" + _strValue + "'");
            final int iRecordCount = tableEx.getRecordCount();
            if (iRecordCount > 0) {
                final Object objValue = tableEx.getRecord(0).getFieldByName("MV").value;
                if (objValue != null) {
                    final String strMValue = objValue.toString();
                    if (strMValue.equals("")) {
                        vResult = String.valueOf(_strValue) + "1";
                    }
                    else {
                        vResult = String.valueOf(_strValue) + (Integer.parseInt(strMValue) + 1);
                    }
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
        
        return vResult;
    }
    
    private boolean doSql(final String _strSql, final HashMap hashFieldValue) throws Exception {
        final boolean vResult = true;
        final WebCommand wCommand = new WebCommand();
        wCommand.hashFieldValue = hashFieldValue;
        wCommand.bIsGbk = true;
        final String[] arrStrSql = wCommand.getFilterSql(_strSql, this.request).split(";");
        final int iSqlLength = arrStrSql.length;
        final int iSqlRealLength = iSqlLength - 1;
        for (int i = 0; i < iSqlLength; ++i) {
            this.dbff.sqlExe(arrStrSql[i], false);
        }
        return vResult;
    }
    
    public boolean delRecord(final String aStrCon, final boolean aIsExecute) throws Exception {
        final boolean vResult = false;
        final String[] arrStrIndex = aStrCon.split(",");
        final int iTableCount = arrStrIndex.length;
        String strTablesEx = "";
        for (int i = 0; i < iTableCount; ++i) {
            final String[] arrFields = arrStrIndex[i].split(":");
            if (strTablesEx.indexOf(arrFields[0]) == -1) {
                strTablesEx = String.valueOf(strTablesEx) + arrFields[0] + ",";
            }
        }
        final String[] arrStrTables = strTablesEx.split(",");
        for (int iTab = arrStrTables.length, j = 0; j < iTab; ++j) {
            final TableEx tableEx = new TableEx(arrStrTables[j]);
            final Record record = new Record();
            for (int k = 0; k < iTableCount; ++k) {
                final String[] arrFields2 = arrStrIndex[k].split(":");
                if (arrStrTables[j].equals(arrFields2[0])) {
                    record.addField(new FieldEx(arrFields2[1], this.requestHash.get(arrFields2[2]), true));
                }
            }
            tableEx.addRecord(record);
            this.dbff.deleteRecord(tableEx, aIsExecute);
        }
        return vResult;
    }
    
    public boolean delRecord() throws Exception {
        final boolean vResult = false;
        final String[] arrStrTabls = this.strTables.split(":");
        final int iTableCount = arrStrTabls.length;
        final TableEx[] tables = new TableEx[iTableCount];
        for (int i = 0; i < iTableCount; ++i) {
            tables[i] = new TableEx(arrStrTabls[i]);
            final Record record = new Record();
            final Enumeration keys = this.requestHash.keys();
            while (keys.hasMoreElements()) {
                final String keyName = keys.nextElement().toString();
                final String[] arrTablField = keyName.split("III");
                for (int j = 0; j < arrTablField.length; ++j) {
                    final String[] arrTables = arrTablField[j].split("\\$");
                    if (arrTables[0].equals(arrStrTabls[i])) {
                        final String strFieldCode = arrTables[1].trim();
                        if (tables[i].haveFieldByName(strFieldCode)) {
                            final String strDelConValue = this.requestHash.get(keyName).toString().trim();
                            if (strDelConValue.startsWith("SYS_LIKE:")) {
                                record.addField(new FieldEx(strFieldCode, strDelConValue.substring(9), true, 2));
                            }
                            else {
                                record.addField(new FieldEx(strFieldCode, strDelConValue, true));
                            }
                        }
                    }
                }
            }
            tables[i].addRecord(record);
            if (i == iTableCount - 1) {
                this.dbff.deleteRecord(tables[i], true);
            }
            else {
                this.dbff.deleteRecord(tables[i], false);
            }
        }
        return vResult;
    }
    
    public boolean updateTable() throws Exception {
        final String strIsEmtyIgnore = this.request.getParameter("NO_sys_Emty_Ignore");
        final HashMap hahsEmtyIgnore = new HashMap();
        if (strIsEmtyIgnore != null) {
            final String[] arrEmtyIngore = strIsEmtyIgnore.split(";");
            for (int iLength = arrEmtyIngore.length, i = 0; i < iLength; ++i) {
                final String[] arrTableField = arrEmtyIngore[i].split(":");
                hahsEmtyIgnore.put(arrTableField[0], arrTableField[1]);
            }
        }
        final String strBachTable = this.request.getParameter("NO_ISBACH");
        final HashMap hashIsBach = new HashMap();
        if (strBachTable != null) {
            final String[] arrTempBachTables = strBachTable.split(",");
            for (int iBachTableCount = arrTempBachTables.length, j = 0; j < iBachTableCount; ++j) {
                hashIsBach.put(arrTempBachTables[j], "");
                this.dbff.sqlExe("delete from " + arrTempBachTables[j] + " where " + this.request.getParameter("NO_sys_ud_con_" + arrTempBachTables[j]), false);
            }
        }
        boolean bIsBatch = false;
        final boolean vResult = false;
        final String[] arrStrTabls = this.strTables.split(":");
        final int iTableCount = arrStrTabls.length;
        final TableEx[] tables = new TableEx[iTableCount];
        Vector vecBachKey = new Vector();
        for (int k = 0; k < iTableCount; ++k) {
            tables[k] = new TableEx(arrStrTabls[k]);
            if (hashIsBach.get(arrStrTabls[k]) != null) {
                bIsBatch = true;
                vecBachKey = new Vector();
            }
            else {
                bIsBatch = false;
            }
            Record record = new Record();
            final Enumeration keys = this.requestHash.keys();
            while (keys.hasMoreElements()) {
                final String keyName = keys.nextElement().toString();
                final String[] arrTablField = keyName.split("III");
                for (int l = 0; l < arrTablField.length; ++l) {
                    final String[] arrTables = arrTablField[l].split("\\$");
                    if (arrTables[0].equals(arrStrTabls[k]) && tables[k].haveFieldByName(arrTables[1])) {
                        if (bIsBatch) {
                            vecBachKey.add(keyName);
                        }
                        else {
                            Object objValue = this.requestHash.get(keyName);
                            if (objValue != null && objValue.toString().startsWith("PY_")) {
                                objValue = EString.getPy(this.requestHash.get(objValue.toString().substring(3)).toString());
                            }
                            else if (objValue != null && objValue.toString().startsWith("TNGLOB_")) {
                                final String strFieldTempValue = objValue.toString().substring(7);
                                final int iIndexField = strFieldTempValue.indexOf("$");
                                String strFieldParam = strFieldTempValue.substring(0, iIndexField);
                                objValue = this.requestHash.get(String.valueOf(arrTables[0]) + "$" + strFieldParam);
                                strFieldParam = strFieldTempValue.substring(iIndexField + 1);
                                if (!strFieldParam.equals("")) {
                                    objValue = String.valueOf(strFieldParam) + "/" + objValue;
                                }
                            }
                            if (this.strNoCon.indexOf(arrTablField[l]) != -1) {
                                record.addField(new FieldEx(arrTables[1], objValue, true));
                            }
                            else {
                                record.addField(new FieldEx(arrTables[1], objValue));
                            }
                        }
                    }
                }
            }
            if (bIsBatch) {
                final int iParmCount = vecBachKey.size();
                final int iParmValueCount = this.request.getParameterValues(vecBachKey.get(0).toString()).length;
                final String[][] arrBachKeyValues = new String[iParmCount][iParmValueCount];
                for (int m = 0; m < iParmCount; ++m) {
                    final String strBachKey = vecBachKey.get(m).toString();
                    final String[] arrBachKeyValue = this.request.getParameterValues(strBachKey);
                    arrBachKeyValues[m] = arrBachKeyValue;
                }
                for (int o = 0; o < iParmValueCount; ++o) {
                    record = new Record();
                    for (int l2 = 0; l2 < iParmCount; ++l2) {
                        final String strBachKey2 = vecBachKey.get(l2).toString();
                        String strValue = EString.encoderStr(arrBachKeyValues[l2][o], this.strChars);
                        if (strValue.startsWith("GET_")) {
                            final String strGetFieldName = strValue.toString().substring(4);
                            final Object objGetValue = this.requestHash.get(strGetFieldName);
                            if (objGetValue == null) {
                                throw new Exception("\u83b7\u53d6\u540c\u6b65\u6570\u636e\u9519\u8bef\uff01[" + strBachKey2 + "=" + strGetFieldName + "]");
                            }
                            strValue = objGetValue.toString();
                        }
                        record.addField(new FieldEx(strBachKey2.split("\\$")[1], strValue));
                    }
                    final Object objEmptyIgnore = hahsEmtyIgnore.get(tables[k].strTableName);
                    boolean bIsAdd = true;
                    if (objEmptyIgnore != null) {
                        final String[] arrIgnore = objEmptyIgnore.toString().split(",");
                        for (int iIgnoreLength = arrIgnore.length, k2 = 0; k2 < iIgnoreLength; ++k2) {
                            if (record.bFieldIsEmty(arrIgnore[k2])) {
                                bIsAdd = false;
                                break;
                            }
                        }
                    }
                    if (bIsAdd) {
                        tables[k].addRecord(record);
                    }
                }
                if (k == iTableCount - 1) {
                    this.dbff.solveTable(tables[k], true);
                }
                else {
                    this.dbff.solveTable(tables[k], false);
                }
            }
            else {
                tables[k].addRecord(record);
                if (k == iTableCount - 1) {
                    this.dbff.updateTable(tables[k], true);
                }
                else {
                    this.dbff.updateTable(tables[k], false);
                }
            }
        }

        String strComMand = this.request.getParameter("NO_sys_docommand");
        if (strComMand == null) {
            strComMand = this.request.getParameter("NO_sys_finish_docommand");
        }
        this.doSqlFun(null, new HashMap(), strComMand);
        return vResult;
    }
    
    public boolean updateSolveTable(final String aStrCon) throws Exception {
        final boolean vResult = false;
        final String[] arrStrTabls = this.strTables.split(":");
        final int iTableCount = arrStrTabls.length;
        final TableEx[] tables = new TableEx[iTableCount];
        for (int i = 0; i < iTableCount; ++i) {
            tables[i] = new TableEx(arrStrTabls[i]);
            final Record record = new Record();
            final Enumeration keys = this.requestHash.keys();
            boolean bIsUpdate = false;
            while (keys.hasMoreElements()) {
                final String keyName = keys.nextElement().toString();
                final String[] arrTablField = keyName.split("III");
                for (int j = 0; j < arrTablField.length; ++j) {
                    final String[] arrTables = arrTablField[j].split("\\$");
                    if (arrTables[0].equals(arrStrTabls[i])) {
                        tables[i].haveFieldByName(arrTables[1]);
                        Object objValue = this.requestHash.get(keyName);
                        if (objValue != null && objValue.toString().startsWith("GET_")) {
                            objValue = this.requestHash.get(objValue.toString().substring(4));
                        }
                        else if (objValue != null && objValue.toString().startsWith("TNGLOB_")) {
                            final String strFieldTempValue = objValue.toString().substring(7);
                            final int iIndexField = strFieldTempValue.indexOf("$");
                            String strFieldParam = strFieldTempValue.substring(0, iIndexField);
                            objValue = this.requestHash.get(String.valueOf(arrTables[0]) + "$" + strFieldParam);
                            strFieldParam = strFieldTempValue.substring(iIndexField + 1);
                            if (!strFieldParam.equals("")) {
                                objValue = String.valueOf(strFieldParam) + "/" + objValue;
                            }
                        }
                        else if (objValue != null && objValue.toString().startsWith("PY_")) {
                            objValue = EString.getPy(this.requestHash.get(objValue.toString().substring(3)).toString());
                        }
                        if (aStrCon.indexOf(arrTablField[j]) != -1) {
                            record.addField(new FieldEx(arrTables[1].trim(), objValue, true));
                            bIsUpdate = true;
                        }
                        else {
                            record.addField(new FieldEx(arrTables[1].trim(), objValue));
                        }
                    }
                }
            }
            tables[i].addRecord(record);
            if (i == iTableCount - 1) {
                if (bIsUpdate) {
                    this.dbff.updateTable(tables[i], true);
                }
                else {
                    this.dbff.solveTable(tables[i], true);
                }
            }
            else if (bIsUpdate) {
                this.dbff.updateTable(tables[i], false);
            }
            else {
                this.dbff.solveTable(tables[i], false);
            }
        }
        return vResult;
    }
    
    public boolean updateTable(final String aTableName, final boolean aIsExecute) throws Exception {
        boolean vResult = false;
        final Enumeration keys = this.requestHash.keys();
        final TableEx table = new TableEx(aTableName);
        final Record record = new Record();
        while (keys.hasMoreElements()) {
            final String keyName = keys.nextElement().toString();
            if (this.hashCodition.get(keyName) == null) {
                record.addField(new FieldEx(keyName, this.requestHash.get(keyName)));
            }
            else {
                record.addField(new FieldEx(keyName, this.requestHash.get(keyName), true));
            }
        }
        table.addRecord(record);
        vResult = this.dbff.updateTable(table, aIsExecute);
        return vResult;
    }
    
    public boolean updateTable(final String aTableName, final String aStrCon) throws Exception {
        boolean vResult = false;
        final Enumeration keys = this.requestHash.keys();
        final TableEx table = new TableEx(aTableName);
        final Record record = new Record();
        while (keys.hasMoreElements()) {
            final String keyName = keys.nextElement().toString();
            record.addField(new FieldEx(keyName, this.requestHash.get(keyName)));
        }
        table.addRecord(record);
        vResult = this.dbff.updateTable(table, aStrCon);
        return vResult;
    }
    
    public boolean updateTable(final String aTableName, final String aStrCon, final String aStrUpField, final String aStrUp, final String aUpdateField1, final String aStrUpdate1) throws Exception {
        boolean vResult = false;
        vResult = this.dbff.sqlExe("UPDATE T_YYD SET " + aUpdateField1 + "=" + aUpdateField1 + "+" + this.requestHash.get(aStrUpdate1) + "," + aStrUpField + "=" + this.requestHash.get(aStrUp) + " where " + aStrCon, true);
        return vResult;
    }
    
    public boolean deleteTable(final String aTableName, final boolean aIsExecute) throws Exception {
        boolean vResult = false;
        final Enumeration keys = this.requestHash.keys();
        final TableEx table = new TableEx(aTableName);
        final Record record = new Record();
        while (keys.hasMoreElements()) {
            final String keyName = keys.nextElement().toString();
            record.addField(new FieldEx(keyName, this.requestHash.get(keyName), true));
        }
        table.addRecord(record);
        vResult = this.dbff.deleteRecord(table, aIsExecute);
        return vResult;
    }
    
    public void setCondition(final String aParamName) {
        this.hashCodition.put(aParamName, "true");
    }
    
    public void clearCondition() {
        this.hashCodition = new Hashtable();
    }
    
    public boolean solveTableEx(final String aTableName) {
        final boolean vResult = false;
        return vResult;
    }
    
    public void setParameter(final String aParaName, final String aParamValue) {
        this.requestHash.put(aParaName, aParamValue);
    }
    
    public boolean deleteByPrim(final String aTableName) {
        final boolean vResult = false;
        return vResult;
    }
    
    public boolean deleteByCondition(final String aTableName, final boolean aIsExecute) throws Exception {
        boolean vResult = false;
        final Enumeration keys = this.requestHash.keys();
        final TableEx table = new TableEx(aTableName);
        final Record record = new Record();
        while (keys.hasMoreElements()) {
            final String keyName = keys.nextElement().toString();
            if (this.hashCodition.get(keyName) != null) {
                record.addField(new FieldEx(keyName, this.requestHash.get(keyName), true));
            }
        }
        table.addRecord(record);
        vResult = this.dbff.deleteRecord(table, aIsExecute);
        return vResult;
    }
    
    public boolean soleveBatch(final String aStrPage, final String _strCon) throws Exception {
        boolean vResult = false;
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(aStrPage);
        final String[] arrcodeSingle = hashHQRC.get("SFIELDCODE").toString().split(",");
        int iSingleCount = arrcodeSingle.length;
        Record record = new Record();
        final TableEx tableMain = new TableEx(arrcodeSingle[0].split("\\$")[0]);
        for (final String strTableField : arrcodeSingle) {
            record.addField(new FieldEx(strTableField.split("\\$")[1], this.requestHash.get(strTableField)));
        }
        tableMain.addRecord(record);
        final String[] arrStrChildForm = hashHQRC.get("SFORM").toString().split("\\$");
        final Hashtable hashMutHQRC = (Hashtable) QRSC.HASHQRSC.get(arrStrChildForm[1]);
        final String[] arrcodeMutl = hashMutHQRC.get("SFIELDCODE").toString().split(",");
        iSingleCount = arrcodeMutl.length;
        final TableEx tableChild = new TableEx(arrcodeMutl[0].split("\\$")[0]);
        final HashMap<String, String[]> hashMut = new HashMap<String, String[]>();
        final String[] arrStrFiled = new String[iSingleCount];
        for (int j = 0; j < iSingleCount; ++j) {
            final String strTableField = arrcodeMutl[j];
            final String strFieldName = strTableField.split("\\$")[1];
            hashMut.put(strFieldName, this.request.getParameterValues(strTableField));
            arrStrFiled[j] = strFieldName;
        }
        final int iRecordCount = hashMut.get(arrStrFiled[0]).length;
        final int[] arrICount = new int[iRecordCount];
        final String[] arrStrWhere = new String[iRecordCount];
        boolean bIsInsert = false;
        if (arrStrChildForm.length == 6) {
            if (!arrStrChildForm[3].equals("")) {
                final String[] arrParKeyFiled = arrStrChildForm[4].split(",");
                for (int iParCount = arrParKeyFiled.length, k = 0; k < iParCount; ++k) {
                    arrStrChildForm[5] = arrStrChildForm[5].replaceAll("<<par_" + arrParKeyFiled[k] + ">>", this.requestHash.get(String.valueOf(arrStrChildForm[3]) + "$" + arrParKeyFiled[k]).toString());
                }
            }
            bIsInsert = true;
        }
        for (int l = 0; l < iRecordCount; ++l) {
            record = new Record();
            String strWhere = arrStrChildForm[5];
            for (int m = 0; m < iSingleCount; ++m) {
                final String strField = arrStrFiled[m];
                final String strFieldValue = EString.encoderStr(hashMut.get(strField)[l]);
                record.addField(new FieldEx(arrStrFiled[m], strFieldValue));
                if (strWhere.indexOf("<<" + strField + ">>") != -1) {
                    strWhere = strWhere.replaceAll("<<" + strField + ">>", strFieldValue);
                }
                if (!bIsInsert && strField.equals(arrStrChildForm[6])) {
                    arrICount[l] = Integer.parseInt(strFieldValue);
                }
            }
            strWhere = strWhere.replaceAll("sys_Mkey", String.valueOf(EString.generId()) + "_" + l);
            strWhere = strWhere.replaceAll("sys_Date", String.valueOf(EString.getCurDate_HH()) + "_" + l);
            arrStrWhere[l] = strWhere;
            tableChild.addRecord(record);
        }
        final SumToSub sts = new SumToSub();
        String strSql = "";
        if (bIsInsert) {
            for (int k = 0; k < iRecordCount; ++k) {
                strSql = String.valueOf(strSql) + ";" + arrStrWhere[k];
            }
        }
        else {
            for (int k = 0; k < iRecordCount; ++k) {
                strSql = String.valueOf(strSql) + sts.doSubSql(this.dbff, arrICount[k], arrStrChildForm[3], arrStrChildForm[4], arrStrWhere[k]);
            }
        }
        System.out.println(":::::::::::::::" + strSql);
        if (!strSql.equals("")) {
            this.dbff.exeSqls(strSql.substring(1), false);
        }
        this.dbff.solveTable(tableChild, false);
        this.dbff.solveTable(tableMain, true);
        vResult = true;
        return vResult;
    }
    
    public boolean solveTable(final boolean bIsExecute) throws Exception {
        final boolean vResult = false;
        final String[] arrStrTabls = this.strTables.split(":");
        final int iTableCount = arrStrTabls.length;
        final TableEx[] tables = new TableEx[iTableCount];
        for (int i = 0; i < iTableCount; ++i) {
            tables[i] = new TableEx(arrStrTabls[i]);
            final Record record = new Record();
            final Enumeration keys = this.requestHash.keys();
            while (keys.hasMoreElements()) {
                final String keyName = keys.nextElement().toString();
                this.session.setAttribute(keyName, this.requestHash.get(keyName));
                final String[] arrTablField = keyName.split("III");
                for (int j = 0; j < arrTablField.length; ++j) {
                    final String[] arrTables = arrTablField[j].split("\\$");
                    if (arrTables[0].equals(arrStrTabls[i]) && tables[i].haveFieldByName(arrTables[1])) {
                        record.addField(new FieldEx(arrTables[1], this.requestHash.get(keyName)));
                    }
                }
            }
            tables[i].addRecord(record);
            if (i == iTableCount - 1) {
                this.dbff.solveTable(tables[i], bIsExecute);
            }
            else {
                this.dbff.solveTable(tables[i], false);
            }
        }
        return vResult;
    }
    
    public void addCol(final String aStrTable, final String aStrCol, final int aLength, final boolean aIsExecute) throws Exception {
        this.dbff.sqlExe("alter table " + aStrTable + " add column " + aStrCol + " varchar(" + aLength + ")", aIsExecute);
    }
    
    public void delCol(final String aStrTable, final String aStrCol, final boolean aIsExecute) throws Exception {
        this.dbff.sqlExe("alter table " + aStrTable + " drop column " + aStrCol, aIsExecute);
    }
    
    public void close() {
        this.dbff.close();
    }
    
    public void soleveAarry(final String aStrTable, final String[] aArrInputField, final String[] aArrField, final boolean bIsExcute) throws Exception {
        final TableEx tableEx = new TableEx(aStrTable);
        final Record record = new Record();
        for (int iFieldCount = aArrField.length, i = 0; i < iFieldCount; ++i) {
            record.addField(new FieldEx(aArrField[i], this.requestHash.get(aArrInputField[i])));
        }
        tableEx.addRecord(record);
        this.dbff.solveTable(tableEx, bIsExcute);
    }
    
    public void soleveAarry(final String aStrTable, final String[] aArrInputField, final String[] aArrField, final int iMaxRecord, final String aStrLeftPrimy, final String[] aArrLeftInput, final String[] aArrLeftField, final boolean bIsExcute) throws Exception {
        final TableEx tableEx = new TableEx(aStrTable);
        int iFieldCount = 0;
        for (int j = 0; j < iMaxRecord; ++j) {
            final Record record = new Record();
            final Object objPrimy = this.requestHash.get(String.valueOf(aStrLeftPrimy) + j);
            if (objPrimy == null) {
                break;
            }
            if (objPrimy.toString().equals("")) {
                break;
            }
            iFieldCount = aArrField.length;
            for (int i = 0; i < iFieldCount; ++i) {
                record.addField(new FieldEx(aArrField[i], this.requestHash.get(aArrInputField[i])));
            }
            iFieldCount = aArrLeftField.length;
            for (int i = 0; i < iFieldCount; ++i) {
                record.addField(new FieldEx(aArrLeftField[i], this.requestHash.get(String.valueOf(aArrLeftInput[i]) + j)));
            }
            tableEx.addRecord(record);
        }
        this.dbff.solveTable(tableEx, bIsExcute);
    }
    
    public void soleveRequestAarry(final String aStrTable, final String[] aArrInputField, final String[] aArrField, final int iMaxRecord, final String aStrLeftPrimy, final String[] aArrLeftInput, final String[] aArrLeftField, final boolean bIsExcute) throws Exception {
        final TableEx tableEx = new TableEx(aStrTable);
        int iFieldCount = 0;
        for (int j = 0; j < iMaxRecord; ++j) {
            final Record record = new Record();
            final Object objPrimy = this.request.getParameter(String.valueOf(aStrLeftPrimy) + j);
            if (objPrimy != null) {
                if (!objPrimy.toString().equals("")) {
                    iFieldCount = aArrField.length;
                    for (int i = 0; i < iFieldCount; ++i) {
                        record.addField(new FieldEx(aArrField[i], this.request.getParameter(aArrInputField[i])));
                    }
                    iFieldCount = aArrLeftField.length;
                    for (int i = 0; i < iFieldCount; ++i) {
                        record.addField(new FieldEx(aArrLeftField[i], this.request.getParameter(String.valueOf(aArrLeftInput[i]) + j)));
                    }
                    tableEx.addRecord(record);
                }
            }
        }
        this.dbff.solveTable(tableEx, bIsExcute);
    }
    
    public void soleveAarry(final String aStrTable, final int iMaxRecord, final String aStrLeftPrimy, final String[] aArrLeftInput, final String[] aArrLeftField, final String aStrAddField, final boolean bIsExcute) throws Exception {
        final TableEx tableExSol = new TableEx(aStrTable);
        final TableEx tableExUp = new TableEx(aStrTable);
        int iFieldCount = 0;
        final TableEx tabKc = new TableEx("*", aStrTable, "");
        final int iRecordCount = tabKc.getRecordCount();
        final Hashtable hash = new Hashtable();
        iFieldCount = aArrLeftField.length;
        for (int i = 0; i < iRecordCount; ++i) {
            String strHashName = "";
            final Record recordKc = tabKc.getRecord(i);
            for (int j = 0; j < iFieldCount; ++j) {
                if (!aArrLeftField[j].equals(aStrAddField)) {
                    strHashName = String.valueOf(strHashName) + recordKc.getFieldByName(aArrLeftField[j]).value;
                }
            }
            hash.put(strHashName, recordKc.getFieldByName(aStrAddField).value);
        }
        for (int k = 0; k < iMaxRecord; ++k) {
            final Record record = new Record();
            final Object objPrimy = this.requestHash.get(String.valueOf(aStrLeftPrimy) + k);
            if (objPrimy == null) {
                break;
            }
            if (objPrimy.toString().equals("")) {
                break;
            }
            iFieldCount = aArrLeftField.length;
            String strTempName = "";
            for (int l = 0; l < iFieldCount; ++l) {
                if (!aArrLeftField[l].equals(aStrAddField)) {
                    strTempName = String.valueOf(strTempName) + this.requestHash.get(String.valueOf(aArrLeftInput[l]) + k);
                }
            }
            final Object objSl = hash.get(strTempName);
            if (objSl != null) {
                for (int m = 0; m < iFieldCount; ++m) {
                    if (!aArrLeftField[m].equals(aStrAddField)) {
                        record.addField(new FieldEx(aArrLeftField[m], this.requestHash.get(String.valueOf(aArrLeftInput[m]) + k), true));
                    }
                    else {
                        final int iCurSl = Integer.parseInt(objSl.toString());
                        final int iInputSl = iCurSl + Integer.parseInt(this.requestHash.get(String.valueOf(aArrLeftInput[m]) + k).toString());
                        record.addField(new FieldEx(aArrLeftField[m], new StringBuilder(String.valueOf(iInputSl)).toString()));
                    }
                }
                tableExUp.addRecord(record);
            }
            else {
                for (int m = 0; m < iFieldCount; ++m) {
                    record.addField(new FieldEx(aArrLeftField[m], this.requestHash.get(String.valueOf(aArrLeftInput[m]) + k)));
                }
                tableExSol.addRecord(record);
            }
        }
        this.dbff.updateTable(tableExUp, false);
        this.dbff.solveTable(tableExSol, bIsExcute);
    }
    
    public void soleveAarry(final String aStrBjTable, final TableEx aTableJq, final String[] aArrBj, final String[] aArrJq, final String strBj, final String aStrJq, final boolean bIsExcute) throws Exception {
        final TableEx tableExSol = new TableEx(aStrBjTable);
        final TableEx tableExUp = new TableEx(aStrBjTable);
        final TableEx tabKc = new TableEx("*", aStrBjTable, "");
        final int iRecordCount = tabKc.getRecordCount();
        final Hashtable hash = new Hashtable();
        final int iFieldCount = aArrBj.length;
        for (int i = 0; i < iRecordCount; ++i) {
            String strHashName = "";
            final Record recordKc = tabKc.getRecord(i);
            for (int j = 0; j < iFieldCount; ++j) {
                strHashName = String.valueOf(strHashName) + recordKc.getFieldByName(aArrBj[j]).value;
            }
            hash.put(strHashName, recordKc.getFieldByName(strBj).value);
        }
        for (int iJqRecordCount = aTableJq.getRecordCount(), k = 0; k < iJqRecordCount; ++k) {
            final Record recordJq = aTableJq.getRecord(k);
            String strTempName = "";
            for (int l = 0; l < iFieldCount; ++l) {
                Object objRecordJq = recordJq.getFieldByName(aArrJq[l]).value;
                if (objRecordJq == null) {
                    objRecordJq = "";
                }
                strTempName = String.valueOf(strTempName) + objRecordJq;
            }
            final Object objValue = hash.get(strTempName);
            if (objValue == null) {
                final Record record = new Record();
                for (int m = 0; m < iFieldCount; ++m) {
                    Object objRecordJq2 = recordJq.getFieldByName(aArrJq[m]).value;
                    if (objRecordJq2 == null) {
                        objRecordJq2 = "";
                    }
                    record.addField(new FieldEx(aArrBj[m], objRecordJq2));
                }
                final int iCurKc = 0 - Integer.parseInt(recordJq.getFieldByName(aStrJq).value.toString());
                record.addField(new FieldEx(strBj, new StringBuilder(String.valueOf(iCurKc)).toString()));
                tableExSol.addRecord(record);
            }
            else {
                final Record record = new Record();
                for (int m = 0; m < iFieldCount; ++m) {
                    Object objRecordJq2 = recordJq.getFieldByName(aArrJq[m]).value;
                    if (objRecordJq2 == null) {
                        objRecordJq2 = "";
                    }
                    record.addField(new FieldEx(aArrBj[m], objRecordJq2, true));
                }
                final int iCurKc = Integer.parseInt(objValue.toString()) - Integer.parseInt(recordJq.getFieldByName(aStrJq).value.toString());
                record.addField(new FieldEx(strBj, new StringBuilder(String.valueOf(iCurKc)).toString()));
                tableExUp.addRecord(record);
            }
        }
        this.dbff.updateTable(tableExUp, false);
        this.dbff.solveTable(tableExSol, bIsExcute);
    }
    
    public void update(final TableEx aTableEx, final boolean aIsExcute) throws Exception {
        this.dbff.updateTable(aTableEx, aIsExcute);
    }
}
