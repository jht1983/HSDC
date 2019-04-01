package com.yulongtao.web.component;

import javax.servlet.http.*;
import java.util.*;
import com.yulongtao.db.*;
import com.yulongtao.util.*;
import com.yulongtao.pub.*;
import com.yulongtao.sys.*;
import com.yulongtao.*;

public class PageModel
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    public static Hashtable<String, String[]> hashPageStyle;
    public static Hashtable<String, ArrayList> hashPageStyleData;
    public static HashMap<String, HashMap> hashDataType;
    public static Hashtable<String, HashMap> hashPageItemStyle;
    public static Hashtable<String, HashMap> hashPageItemParam;
    public static Hashtable<String, HashMap> hashPageItemCaseStyle;
    public boolean bIsCon;
    private static final int I_Modle_YLT_IMP = 0;
    private static final int I_Modle_YLT_FOR = 1;
    private static final int I_Modle_YLT_FOR_CASE = 2;
    private static final int I_Modle_YLT_FOR_STL = 9;
    private static final int I_Modle_YLT_TREEDATA = 3;
    private static final int I_Modle_YLT_RORM = 4;
    private static final int I_Modle_YLT_V = 5;
    private static final int I_Modle_YLT_VIEW = 6;
    private static final int I_Modle_YLT_FUN = 7;
    private static final int I_Modle_YLT_RDT = 8;
    private static final int I_Modle_YLT_CMP = 10;
    private static final int I_Modle_YLT_GV = 11;
    public Hashtable hashSysParam;
    public boolean bIsJs;
    public String strPageName;
    
    public PageModel(final HttpServletRequest _request, final HttpServletResponse _response) {
        this.bIsCon = false;
        this.bIsJs = false;
        this.strPageName = "";
        this.request = _request;
        this.response = _response;
    }
    
    private static String getAttr(final String _strTag, final String _strTagStart, final String _strTagEnd) {
        final int iUrlStartIndex = _strTag.indexOf(_strTagStart);
        if (iUrlStartIndex == -1) {
            return "";
        }
        final String strAttr = _strTag.substring(iUrlStartIndex + _strTagStart.length());
        final int iUrlEndIndex = strAttr.indexOf(_strTagEnd);
        final String strAttrValue = strAttr.substring(0, iUrlEndIndex);
        return strAttrValue;
    }
    
    public static void initModel() {
        PageModel.hashPageStyle = new Hashtable<String, String[]>();
        PageModel.hashPageStyleData = new Hashtable<String, ArrayList>();
        PageModel.hashDataType = new HashMap<String, HashMap>();
        PageModel.hashPageItemStyle = new Hashtable<String, HashMap>();
        PageModel.hashPageItemCaseStyle = new Hashtable<String, HashMap>();
        PageModel.hashPageItemParam = new Hashtable<String, HashMap>();
        initModel("page");
        initModel("component");
    }
    
    public static Record getRecord(final String _strSql) {
        Record redcord = null;
        final DBFactory dbf = new DBFactory();
        TableEx tableEx = null;
        try {
            tableEx = dbf.query(_strSql);
            redcord = tableEx.getRecord(0);
        }
        catch (Exception ex) {
            return redcord;
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return redcord;
    }
    
    private static void initModel(final String _strFile) {
        final String strCurrPath = String.valueOf(Dic.strCurPath) + "WEB-INF" + Dic.strPathSplit + "style" + Dic.strPathSplit + _strFile;
        try {
            final EFile eFile = new EFile();
            for (final String strFileName : eFile.getFilesName(strCurrPath, "html")) {
                initModleFile(strCurrPath, eFile, strFileName);
            }
        }
        catch (Exception ex) {}
    }
    
    private static boolean initModleFile(final String _strCurrPath, final EFile _eFile, final String _strFileName) {
        boolean bIsExitsPage = true;
        final ArrayList<HashMap> listTreeStyle = new ArrayList<HashMap>();
        final String strStyleFile = _eFile.readFile(String.valueOf(_strCurrPath) + Dic.strPathSplit + _strFileName, "utf-8").toString();
        if (strStyleFile.equals("")) {
            bIsExitsPage = false;
        }
        final String strStyleFileName = _strFileName.split("\\.")[0];
        final String[] arrStrStyle = strStyleFile.split("<ylt.");
        final int iLength = arrStrStyle.length;
        final ArrayList arrListStyle = new ArrayList();
        final HashMap mapDataType = new HashMap();
        final HashMap<Integer, HashMap> mapDataPram = new HashMap<Integer, HashMap>();
        final HashMap<Integer, String[]> mapItemStype = new HashMap<Integer, String[]>();
        final HashMap<Integer, String[]> hashPageItemCaseConfig = new HashMap<Integer, String[]>();
        for (int j = 0; j < iLength; ++j) {
            String strStyleItem = arrStrStyle[j];
            if (strStyleItem.startsWith("imp ")) {
                final int iTagEndIndex = strStyleItem.indexOf("/>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 2);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " url=\"", "\"");
                arrListStyle.add(strDataId);
                mapDataType.put(j, 0);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("gv ")) {
                final int iTagEndIndex = strStyleItem.indexOf("/>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 2);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " msg=\"", "\"");
                arrListStyle.add(strDataId);
                mapDataType.put(j, 11);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("for ")) {
                final int iTagEndIndex = strStyleItem.indexOf("</ylt.for>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 10);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " dataId=\"", "\"");
                arrListStyle.add(strDataId);
                final String strItemContent = strStyleItem.substring(strStyleItem.indexOf(">") + 1);
                final String[] arrStrItemContent = strItemContent.split("\\$value");
                final String strCase = getAttr(strStyleItem, " case=\"", "\"");
                if (!strCase.equals("")) {
                    final String strValue = getAttr(strStyleItem, " cValue=\"", "\"");
                    final String strTo = getAttr(strStyleItem, " to=\"", "\"");
                    hashPageItemCaseConfig.put(j, new String[] { strCase, strValue, strTo });
                    mapDataType.put(j, 2);
                }
                else {
                    final String strStyleList = getAttr(strStyleItem, " stl=\"", "\"");
                    if (!strStyleList.equals("")) {
                        hashPageItemCaseConfig.put(j, strStyleList.split(","));
                        mapDataType.put(j, 9);
                    }
                    else {
                        mapDataType.put(j, 1);
                    }
                }
                mapItemStype.put(j, arrStrItemContent);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("treedata ")) {
                final int iTagEndIndex = strStyleItem.indexOf("/>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 2);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " dataId=\"", "\"");
                arrListStyle.add(strDataId);
                final Record recordTree = getRecord("select S_PARENT,S_NAME,S_NODE,SSQL from t_tree,t_sys_dataset where S_ID='" + strDataId + "' and S_SQL=SCONID");
                hashPageItemCaseConfig.put(j, new String[] { recordTree.getFieldByName("S_NODE").value.toString(), recordTree.getFieldByName("S_NAME").value.toString(), recordTree.getFieldByName("S_PARENT").value.toString(), recordTree.getFieldByName("SSQL").value.toString() });
                mapDataType.put(j, 3);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("form ")) {
                final int iTagEndIndex = strStyleItem.indexOf("</ylt.form>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 11);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " page=\"", "\"");
                arrListStyle.add(strDataId);
                final String strItemContent = strStyleItem.substring(strStyleItem.indexOf(">") + 1);
                final String[] arrStrItemContent = strItemContent.split("\\$value");
                mapItemStype.put(j, arrStrItemContent);
                mapDataType.put(j, 4);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("v ")) {
                final int iTagEndIndex = strStyleItem.indexOf("</ylt.v>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 8);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " message=\"", "\"");
                arrListStyle.add(strDataId);
                final String strItemContent = strStyleItem.substring(strStyleItem.indexOf(">") + 1);
                final String[] arrStrItemContent = strItemContent.split("\\$value");
                mapItemStype.put(j, arrStrItemContent);
                mapDataType.put(j, 5);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("view ")) {
                final int iTagEndIndex = strStyleItem.indexOf("</ylt.view>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 11);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " dataId=\"", "\"");
                arrListStyle.add(strDataId);
                final String strItemContent = strStyleItem.substring(strStyleItem.indexOf(">") + 1);
                final String[] arrStrItemContent = strItemContent.split("\\$value");
                mapDataType.put(j, 6);
                mapItemStype.put(j, arrStrItemContent);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("fun ")) {
                final int iTagEndIndex = strStyleItem.indexOf("/>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 2);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " fun=\"", "\"");
                arrListStyle.add(strDataId);
                mapDataType.put(j, 7);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("rdt ")) {
                final int iTagEndIndex = strStyleItem.indexOf("/>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 2);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " con=\"", "\"");
                arrListStyle.add(strDataId);
                mapDataType.put(j, 8);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else if (strStyleItem.startsWith("cmp ")) {
                final int iTagEndIndex = strStyleItem.indexOf("/>");
                arrStrStyle[j] = strStyleItem.substring(iTagEndIndex + 2);
                strStyleItem = strStyleItem.substring(0, iTagEndIndex);
                final String strDataId = getAttr(strStyleItem, " dataId=\"", "\"");
                arrListStyle.add(strDataId);
                mapDataType.put(j, 10);
                setItemParam(j, mapDataPram, strStyleItem);
            }
            else {
                arrListStyle.add("");
                mapDataType.put(j, -1);
            }
        }
        PageModel.hashPageStyle.put(strStyleFileName, arrStrStyle);
        PageModel.hashPageStyleData.put(strStyleFileName, arrListStyle);
        PageModel.hashDataType.put(strStyleFileName, mapDataType);
        PageModel.hashPageItemStyle.put(strStyleFileName, mapItemStype);
        PageModel.hashPageItemCaseStyle.put(strStyleFileName, hashPageItemCaseConfig);
        PageModel.hashPageItemParam.put(strStyleFileName, mapDataPram);
        return bIsExitsPage;
    }
    
    private static void setItemParam(final int _iIndex, final HashMap<Integer, HashMap> _mapDataPram, final String _strStyleItem) {
        final String strParam = getAttr(_strStyleItem, " param=\"", "\"");
        final HashMap<String, String> hashPValue = new HashMap<String, String>();
        if (!strParam.equals("")) {
            final String[] arrParams = strParam.split("&");
            for (int iParamCount = arrParams.length, k = 0; k < iParamCount; ++k) {
                final String[] arrPramNameValue = arrParams[k].split("=");
                hashPValue.put(arrPramNameValue[0], arrPramNameValue[1]);
            }
        }
        _mapDataPram.put(_iIndex, hashPValue);
    }
    
    public boolean initModleFile(String _strFile) {
        final String strCurPath = String.valueOf(Dic.strCurPath) + "WEB-INF" + Dic.strPathSplit + "style" + Dic.strPathSplit;
        _strFile = String.valueOf(_strFile) + ".html";
        final EFile eFile = new EFile();
        if (eFile.isExitsFile(String.valueOf(strCurPath) + "page" + Dic.strPathSplit + _strFile)) {
            return initModleFile(String.valueOf(strCurPath) + "page", eFile, _strFile);
        }
        return initModleFile(String.valueOf(strCurPath) + "component", eFile, _strFile);
    }
    
    private void loginV(final DBFactory _dbf) throws Exception {
        _dbf.sqlExe("INSERT INTO T_SYS_PAGEINFO (S_PAGECODE,I_VCOUNT) VALUES ('" + this.strPageName + "',1)  ON DUPLICATE KEY UPDATE I_VCOUNT=I_VCOUNT+1", true);
    }
    
    public StringBuffer getStyle(final String _strStyleName) {
        final DBFactory dbf = new DBFactory();
        final TreeModel tm = new TreeModel(this.request);
        tm.hashSysParams = this.hashSysParam;
        final FlatDataModel fdm = new FlatDataModel(this.request, this.response);
        fdm.bIsCon = this.bIsCon;
        fdm.dbf = dbf;
        fdm.hashSysParams = (Hashtable<String, String>)this.hashSysParam;
        final StringBuffer vResult = new StringBuffer();
        final String[] arrStyleModel = PageModel.hashPageStyle.get(_strStyleName);
        final ArrayList arrarListStyleModel = PageModel.hashPageStyleData.get(_strStyleName);
        final HashMap<Integer, Integer> mapDataType = PageModel.hashDataType.get(_strStyleName);
        final HashMap<Integer, String[]> hashArrItempStyle = PageModel.hashPageItemStyle.get(_strStyleName);
        final HashMap<Integer, String[]> hashCaseConfig = PageModel.hashPageItemCaseStyle.get(_strStyleName);
        final HashMap<Integer, HashMap> hashParam = PageModel.hashPageItemParam.get(_strStyleName);
        final int iLength = arrStyleModel.length;
        try {
            for (int i = 0; i < iLength; ++i) {
                final Integer iDataType = mapDataType.get(i);
                if (iDataType != -1) {
                    final String strDataMsg = arrarListStyleModel.get(i).toString();
                    fdm.hashParams = hashParam.get(i);
                    switch (iDataType) {
                        case 0: {
                            vResult.append(this.getStyle(strDataMsg));
                            break;
                        }
                        case 1: {
                            vResult.append(fdm.getStyle(strDataMsg, hashArrItempStyle.get(i)));
                            break;
                        }
                        case 3: {
                            vResult.append(fdm.getTreeData(hashCaseConfig.get(i)));
                            break;
                        }
                        case 2: {
                            vResult.append(fdm.getStyle(strDataMsg, hashArrItempStyle.get(i), hashCaseConfig.get(i)));
                            break;
                        }
                        case 9: {
                            vResult.append(fdm.getStyleSTL(strDataMsg, hashArrItempStyle.get(i), hashCaseConfig.get(i)));
                            break;
                        }
                        case 4: {
                            vResult.append(fdm.getForm(strDataMsg, hashArrItempStyle.get(i)));
                            break;
                        }
                        case 5: {
                            final Object objView = fdm.getParameter(strDataMsg);
                            if (objView == null) {
                                vResult.append(hashArrItempStyle.get(i)[0]);
                                break;
                            }
                            vResult.append(hashArrItempStyle.get(i)[1]).append(objView).append(hashArrItempStyle.get(i)[2]);
                            break;
                        }
                        case 11: {
                            if (strDataMsg.equals("url")) {
                                vResult.append(this.hashSysParam.get("page") + Pub.getUrlParams(this.request));
                                break;
                            }
                            vResult.append(fdm.getParameter(strDataMsg));
                            break;
                        }
                        case 6: {
                            vResult.append(fdm.getView(strDataMsg, hashArrItempStyle.get(i)));
                            break;
                        }
                        case 7: {
                            vResult.append(fdm.doFun(strDataMsg));
                            break;
                        }
                        case 10: {
                            vResult.append(fdm.genCmp(strDataMsg));
                            break;
                        }
                        case 8: {
                            if (this.request.getParameter("sys_bed") == null) {
                                fdm.doRdt(strDataMsg);
                                break;
                            }
                            break;
                        }
                    }
                }
                vResult.append(arrStyleModel[i]);
            }
            if (!this.bIsJs) {
                vResult.append("<script>").append(fdm.sbScript).append("</script>");
            }
        }
        catch (Exception e) {
            System.out.println("\u751f\u6210\u9875\u9762\u9519\u8bef\uff01" + e);
            e.printStackTrace();
            return vResult;
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult;
    }
    
    public StringBuffer getParamJsData(final String _strParamId) {
        StringBuffer vResult = new StringBuffer();
        final DBFactory dbf = new DBFactory();
        final Record recordTree = getRecord("select S_PARENT,S_NAME,S_NODE,SSQL from t_tree,t_sys_dataset where S_ID='" + _strParamId + "' and S_SQL=SCONID");
        final FlatDataModel fdm = new FlatDataModel(this.request, this.response);
        fdm.bIsCon = this.bIsCon;
        fdm.dbf = dbf;
        fdm.hashSysParams = (Hashtable<String, String>)this.hashSysParam;
        try {
            vResult = fdm.getTreeData(recordTree.getFieldByName("SSQL").value.toString(), recordTree.getFieldByName("S_NODE").value.toString(), recordTree.getFieldByName("S_NAME").value.toString(), recordTree.getFieldByName("S_PARENT").value.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return vResult;
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult;
    }
    
    public StringBuffer getGraphJsData(final String _strGraphId, final String _strGraphDivId) {
        final String strObjChartId = "sys_objChart_" + _strGraphDivId;
        String strVarFlag;
        StringBuffer vResult;
        if (this.request.getParameter("sys_is_flushdata") == null) {
            strVarFlag = "var ";
            vResult = new StringBuffer(String.valueOf(strVarFlag) + strObjChartId + " = echarts.init(document.getElementById('chart_" + _strGraphDivId + "'));");
        }
        else {
            strVarFlag = "";
            vResult = new StringBuffer();
        }
        final DBFactory dbf = new DBFactory();
        Record record = getRecord("select * from t_sys_graph_msg where S_ID='" + _strGraphId + "'");
        final String strPageCode = record.getFieldByName("S_PAGE_CODE").value.toString();
        final String strX = record.getFieldByName("S_X").value.toString();
        final String strGraphType = record.getFieldByName("S_ITYPE").value.toString();
        final String strLegends = record.getFieldByName("S_LEGEND").value.toString();
        final Hashtable hashPageMsg = (Hashtable) QRSC.HASHQRSC.get(strPageCode);
        final Hashtable hashFieldMsg = (Hashtable) Pub.getPageFields(hashPageMsg);
        final HashMap<String, String[]> hashColToCols = new HashMap<String, String[]>();
        final String strColRowSplit = hashPageMsg.get("SISCOLCHANGEROW").toString();
        if (strColRowSplit.startsWith("cmd1:")) {
            final String[] arrColToCols = strColRowSplit.substring(5).split(";");
            for (int iSplitColsCount = arrColToCols.length, i = 0; i < iSplitColsCount; ++i) {
                final String[] arrSplitColsMsg = arrColToCols[i].split(":");
                hashColToCols.put(arrSplitColsMsg[0], arrSplitColsMsg[1].split(","));
                final int length = arrSplitColsMsg.length;
            }
        }
        final FlatDataModel fdm = new FlatDataModel(this.request, this.response);
        fdm.bIsCon = this.bIsCon;
        fdm.dbf = dbf;
        fdm.hashSysParams = (Hashtable<String, String>)this.hashSysParam;
        try {
            final String strSql = fdm.gennerSql(String.valueOf(strX) + "," + strLegends, hashPageMsg).replace("desc", "");
            final TableEx tableEx = dbf.query(strSql);
            final int iRecordCount = tableEx.getRecordCount();
            final String[] arrLegends = strLegends.split(",");
            final int iLegCount = arrLegends.length;
            final HashMap<String, String> hashData = new HashMap<String, String>();
            String strXData = "";
            String strSplit = "";
            for (int j = 0; j < iRecordCount; ++j) {
                record = tableEx.getRecord(j);
                for (final String strLegCode : arrLegends) {
                    final String[] arrSplitColsName = hashColToCols.get(strLegCode);
                    if (arrSplitColsName != null) {
                        final int iSplitCount = arrSplitColsName.length;
                        final String strData = record.getFieldByName(strLegCode).value.toString();
                        final int iDataLength = strData.length();
                        for (int l = 0; l < iSplitCount; ++l) {
                            final String strNewLegCode = String.valueOf(strLegCode) + "_" + l + "_";
                            String strLegData = hashData.get(strNewLegCode);
                            char c = '0';
                            int iValue = 5;
                            if (l < iDataLength) {
                                c = strData.charAt(l);
                            }
                            if (c != '0') {
                                iValue = 10;
                            }
                            if (strLegData == null) {
                                strLegData = new StringBuilder(String.valueOf(iValue)).toString();
                            }
                            else {
                                strLegData = String.valueOf(strLegData) + "," + iValue;
                            }
                            hashData.put(strNewLegCode, strLegData);
                        }
                    }
                    else {
                        String strLegData2 = hashData.get(strLegCode);
                        final String strData = record.getFieldByName(strLegCode).value.toString();
                        if (strLegData2 == null) {
                            strLegData2 = strData;
                        }
                        else {
                            strLegData2 = String.valueOf(strLegData2) + "," + strData;
                        }
                        hashData.put(strLegCode, strLegData2);
                    }
                }
                strXData = String.valueOf(strXData) + strSplit + "'" + record.getFieldByName(strX).value.toString() + "'";
                strSplit = ",";
            }
            vResult.append(String.valueOf(strVarFlag) + " arrXData_" + _strGraphDivId + "=[" + strXData + "];");
            final StringBuffer sbSeries = new StringBuffer();
            String strDataSplit = "";
            String strViewLegends = "";
            for (final String strLegendId : arrLegends) {
                final Object objLegEndNm = hashFieldMsg.get(strLegendId);
                final String[] arrSplitColsName2 = hashColToCols.get(strLegendId);
                if (arrSplitColsName2 != null) {
                    for (int iSplitCount2 = arrSplitColsName2.length, k2 = 0; k2 < iSplitCount2; ++k2) {
                        final String strNewId = String.valueOf(strLegendId) + "_" + k2 + "_";
                        final String strSerDataId = "arrData_" + strNewId + "_" + _strGraphDivId;
                        vResult.append(String.valueOf(strVarFlag) + strSerDataId + "=[" + hashData.get(strNewId) + "];\n");
                        sbSeries.append(strDataSplit).append("{ name: '" + arrSplitColsName2[k2] + "', type: '" + strGraphType + "', symbol: 'none', smooth: true, barWidth: 22, itemStyle: { normal: {} },data:" + strSerDataId + "}");
                        strViewLegends = String.valueOf(strViewLegends) + strDataSplit + "'" + arrSplitColsName2[k2] + "'";
                        strDataSplit = ",";
                    }
                }
                else {
                    final String strSerDataId2 = "arrData_" + strLegendId + "_" + _strGraphDivId;
                    vResult.append(String.valueOf(strVarFlag) + strSerDataId2 + "=[" + hashData.get(strLegendId) + "];\n");
                    sbSeries.append(strDataSplit).append("{ name: '" + objLegEndNm + "', type: '" + strGraphType + "', symbol: 'none', smooth: true, barWidth: 22, itemStyle: { normal: {} },data:" + strSerDataId2 + "}");
                    strViewLegends = String.valueOf(strViewLegends) + strDataSplit + "'" + objLegEndNm + "'";
                }
                strDataSplit = ",";
            }
            vResult.append(String.valueOf(strVarFlag) + " arrLegend_" + _strGraphDivId + "=[" + strViewLegends + "];\n");
            vResult.append(String.valueOf(strVarFlag) + " option").append(_strGraphDivId).append("={title: { text: '', x: 'center', subtext: '' },").append("tooltip: { trigger: 'axis' },").append("legend: { data:arrLegend_" + _strGraphDivId + ",left:'30px',top:'0px'},").append("grid: [{ top: '40px', left:'40px', right:'40px', }],").append("dataZoom: [ { id: 'dataZoomX', type: 'inside', xAxisIndex: [0], start: 85, end: 100,filterMode: 'filter' } ],").append("xAxis: { data:arrXData_" + _strGraphDivId + "},").append("yAxis: {},series:[").append(sbSeries).append("]};" + strObjChartId + ".setOption(option" + _strGraphDivId + ");");
            final String strFlushTime = this.request.getParameter("flushtm");
            if (strFlushTime != null) {
                vResult.append("setInterval(function () {eval(ylt.Tools.getTx('','" + _strGraphId + ".v?sys_pagetype=graph&sys_graphid=" + _strGraphDivId + "&sys_is_flushdata=true'));}," + strFlushTime + ");");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return vResult;
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult;
    }
    
    public StringBuffer getPageJsData(final String _strPageId) {
        final StringBuffer vResult = new StringBuffer();
        final DBFactory dbf = new DBFactory();
        final FlatDataModel fdm = new FlatDataModel(this.request, this.response);
        fdm.bIsCon = this.bIsCon;
        fdm.dbf = dbf;
        fdm.hashSysParams = (Hashtable<String, String>)this.hashSysParam;
        String strCon = this.request.getParameter("sys_con");
        String strUrlCon = "";
        if (strCon == null) {
            strCon = "";
        }
        else {
            strUrlCon = "&sys_con=" + strCon;
            if (strCon.equals("1")) {
                strCon = " limit 0,1";
            }
            else {
                final String strField = this.request.getParameter("sys_fd");
                final String strFieldValue = this.request.getParameter("sys_fv");
                if (strCon.equals("2")) {
                    strCon = " where " + strField + ">'" + strFieldValue + "'";
                }
                else {
                    strCon = " where " + strField + "<'" + strFieldValue + "'";
                }
            }
        }
        
        try {
            if (this.request.getParameter("sys_is_flushdata") == null) {
                vResult.append("var ");
            }
            vResult.append("arr").append(_strPageId).append("=");
            vResult.append(fdm.getPageJsData(_strPageId, strCon)).append(";");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            dbf.close();
        }
        
        final String strInvokeFun = this.request.getParameter("sys_fun");
        String strFunUrl = "";
        if (strInvokeFun != null) {
            vResult.append(strInvokeFun).append("();");
            strFunUrl = "&sys_fun=" + strInvokeFun;
        }
        final String strFlushTime = this.request.getParameter("flushtm");
        String strGetTx = "ylt.Tools.";
        if (this.request.getParameter("sys_pt") != null) {
            strGetTx = "";
        }
        if (strFlushTime != null) {
            vResult.append("setInterval(function () {eval(" + strGetTx + "getTx('','" + _strPageId + ".v?sys_pagetype=page" + strUrlCon + "&sys_is_flushdata=true" + strFunUrl + "'));}," + strFlushTime + ");");
        }
        return vResult;
    }
    
    public static void main(final String[] args) {
        KCSTATIC.iBJ = 101;
        KCSTATIC.iGQ = 1;
        QRSC.init();
        System.out.println(new PageModel(null, null).getGraphJsData("154477481563910008", "1"));
    }
}
