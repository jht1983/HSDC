package com.yulongtao.web.component;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.Debug;
import com.yulongtao.pub.Pub;
import com.yulongtao.pub.StringRes;
import com.yulongtao.sys.QRSC;
import com.yulongtao.web.ABSElement;

public class TabPage extends WebElement
{
    private String strPageNamePreFix;
    private String strPageFrame;
    private String strPagePreFix;
    private String strPageClass;
    private Vector vecPageName;
    private Vector vecPageSrc;
    private Vector vecPageType;
    private StringBuffer sbPage;
    private String strBttn;
    private ABSElement absElement;
    private String strPid;
    private int iCurIndex;
    private boolean bIsFieldForm;
    
    public TabPage() {
        this.strPageNamePreFix = "pgnm_";
        this.strPageFrame = "pgfm_";
        this.strPagePreFix = "pg_";
        this.strPageClass = "class_";
        this.vecPageName = new Vector();
        this.vecPageSrc = new Vector();
        this.vecPageType = new Vector();
        this.sbPage = new StringBuffer();
        this.strBttn = "";
        this.absElement = null;
        this.strPid = "";
        this.iCurIndex = 0;
        this.bIsFieldForm = false;
        this.strNamePreFix = "tabpage_";
    }
    
    public TabPage(final boolean _bIsFieldForm) {
        this.strPageNamePreFix = "pgnm_";
        this.strPageFrame = "pgfm_";
        this.strPagePreFix = "pg_";
        this.strPageClass = "class_";
        this.vecPageName = new Vector();
        this.vecPageSrc = new Vector();
        this.vecPageType = new Vector();
        this.sbPage = new StringBuffer();
        this.strBttn = "";
        this.absElement = null;
        this.strPid = "";
        this.iCurIndex = 0;
        this.bIsFieldForm = false;
        this.strNamePreFix = "tabpage_";
        this.bIsFieldForm = true;
    }
    
    public TabPage(final Hashtable hashHQRC, final HttpServletRequest request) {
        this();
        final String strFieldCode = hashHQRC.get("SFIELDCODE").toString();
        final String strFieldName = hashHQRC.get("SFIELDNAME").toString();
        final String strCachePage = hashHQRC.get("SQUERYFIELD").toString();
        final String strBttns = hashHQRC.get("SHREFIELD").toString();
        final String[] arrStrFieldCode = strFieldCode.split("\\|");
        final String[] arrStrFieldName = strFieldName.split("\\|");
        final String[] arrStrBttns = strBttns.split("\\|");
        Object objPid = request.getParameter("v2");
        final Object objIndex = request.getParameter("iindex");
        if (objIndex != null) {
            this.iCurIndex = Integer.parseInt(objIndex.toString());
        }
        if (objPid != null) {
            this.strPid = objPid.toString();
        }
        else {
            objPid = request.getSession().getAttribute("pid");
            if (objPid != null) {
                this.strPid = objPid.toString();
            }
        }
        final Hashtable hashParHQRC = (Hashtable) QRSC.HASHQRSC.get(strCachePage);
        this.sbComponent.append("\r<form id=\"add\" method=\"post\" style=\"width:100%;height:100%;\">\r ");
        this.absElement = new ABSElement();
        final int iPageCount = arrStrFieldCode.length;
        final String strUrlParams = Pub.getParamsNoSPageCode(request);
        for (int i = 0; i < iPageCount; ++i) {
            String strContent = arrStrFieldCode[i];
            if (strContent.startsWith("URL:")) {
                strContent = String.valueOf(strContent.substring(4)) + strUrlParams;
                this.addPage(arrStrFieldName[i], strContent);
            }
            else {
                try {
                    this.addPage(arrStrFieldName[i], this.getFormContent(strContent, hashParHQRC, request));
                }
                catch (Exception e) {
                    Debug.println("\u751f\u6210\u7f13\u5b58\u8868\u5355\u9519\u8bef\uff01" + e);
                }
            }
        }
        for (final String strBttnMsg : arrStrBttns) {
            if (!strBttnMsg.trim().equals("")) {
                final String[] arrStrBttnMsg = strBttnMsg.split(",");
                this.setBttn("<button " + arrStrBttnMsg[1] + " " + arrStrBttnMsg[3] + "><img src='" + arrStrBttnMsg[2] + "'>" + arrStrBttnMsg[0] + "</button>&nbsp;&nbsp;&nbsp;&nbsp;");
            }
        }
        if (!strCachePage.equals("")) {
            this.setBttn("<button type='submit'>\u786e \u5b9a </button>&nbsp;&nbsp;&nbsp;&nbsp;<button>\u53d6 \u6d88 </button></form><script language='javascript'>init(add);</script>");
        }
    }
    
    private StringBuffer getFormContent(final String strContent, final Hashtable hashHQRC, final HttpServletRequest request) throws Exception {
        final StringBuffer vResult = new StringBuffer();
        final String[] arrcode = this.absElement.getFilterData(hashHQRC.get("SFIELDCODE").toString(), request).split(",");
        final String[] arrname = hashHQRC.get("SFIELDNAME").toString().replaceAll(" ", "&nbsp;").split(",");
        final String[] arrSelPage = hashHQRC.get("SQUERYFIELD").toString().split(",");
        final String[] arrSelPageSize = hashHQRC.get("SSIZE").toString().split(":");
        final String[] arrReturn = hashHQRC.get("SGLFIELD").toString().split(",");
        final String[] arrType = this.absElement.getFilterData(hashHQRC.get("SDELCON").toString(), request).split(",");
        final String[] arrFieldsSize = hashHQRC.get("SFIELDSIZE").toString().split(",");
        final String[] arrStrRules = hashHQRC.get("SHREFIELD").toString().split(",");
        final Object objTableOpType = hashHQRC.get("SQLFIELD");
        final String strCon = hashHQRC.get("SEDITPAGE").toString();
        if (!strCon.equals("") && !strCon.equals("*")) {
            this.absElement.initCon(strCon, request);
        }
        else {
            this.absElement.bIsUpdate = false;
        }
        final Object objIsBatch = hashHQRC.get("SFREE1");
        final int iCount = arrcode.length;
        String strSingCode = "";
        String strSingName = "";
        String strSingType = "";
        String strSingSize = "";
        String strSingPage = "";
        String strSingPageSize = "";
        String strSingReturn = "";
        String strSingRule = "";
        for (int i = 0; i < iCount; ++i) {
            if (strContent.indexOf(arrcode[i]) != -1) {
                strSingCode = String.valueOf(strSingCode) + "," + arrcode[i];
                strSingName = String.valueOf(strSingName) + "," + arrname[i];
                strSingType = String.valueOf(strSingType) + "," + arrType[i];
                strSingSize = String.valueOf(strSingSize) + "," + arrFieldsSize[i];
                strSingPage = String.valueOf(strSingPage) + "," + arrSelPage[i];
                strSingPageSize = String.valueOf(strSingPageSize) + "," + arrSelPageSize[i];
                strSingReturn = String.valueOf(strSingReturn) + "," + arrReturn[i];
                strSingRule = String.valueOf(strSingRule) + "," + arrStrRules[i];
            }
        }
        if (!strSingCode.equals("")) {
            final String[] arrSingCode = strSingCode.substring(1).split(",");
            final String strStyle = "1";
            final String strIsMode = hashHQRC.get("SMOD").toString();
            final String strWidth = "100%";
            if (!strIsMode.equals("")) {
                vResult.append(this.absElement.generInputModField(arrSingCode.length, arrSingCode, strSingName.substring(1).split(","), strSingType.substring(1).split(","), strSingSize.substring(1).split(","), request, strSingPage.substring(1).split(","), strSingPageSize.substring(1).split(","), strSingReturn.substring(1).split(","), strIsMode, strSingRule.substring(1).split(",")));
            }
            else {
                vResult.append("<table width=\"" + strWidth + "\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"inputtb" + strStyle + "\">");
                vResult.append(this.absElement.generInputField(arrSingCode.length, arrSingCode, strSingName.substring(1).split(","), strSingType.substring(1).split(","), strSingSize.substring(1).split(","), request, strSingPage.substring(1).split(","), strSingPageSize.substring(1).split(","), strSingReturn.substring(1).split(","), strSingRule.substring(1).split(",")));
            }
        }
        return vResult.append("</table>");
    }
    
    @Override
    protected void generStyle() {
        final int iIndex = super.getIdIndex();
        final String strId = String.valueOf(this.strNamePreFix) + iIndex;
        this.sbComponent.append("<table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' id='").append(strId).append("' icurtarget='" + this.iCurIndex + "'>");
        this.sbComponent.append("<tr style='background-color: #e6e9ed;'><td height='36px' valign='bottom'><table border='0' cellpadding='0' cellspacing='0' height='30px'><tr>");
        final StringBuffer sbSize = this.generContent(strId);
        this.sbComponent.append("</table><div class='tab-border'></div><div style='width:100%;height:2px;'></div></td></tr><tr><td valign='top' id='" + strId + "_content'>");
        this.sbComponent.append(this.sbPage);
        this.sbComponent.append("</td></tr>");
        if (!this.strBttn.equals("")) {
            this.sbComponent.append("<tr><td height='30px' align='center'>");
            this.sbComponent.append(this.strBttn);
            this.sbComponent.append("</td></tr>");
        }
        this.sbComponent.append("</table>");
        if (!this.bIsFieldForm) {
            this.sbComponent.append("</form>");
        }
        this.sbComponent.append(sbSize);
    }
    
    public void setBttn(final String _strBttn) {
        this.strBttn = String.valueOf(this.strBttn) + _strBttn;
    }
    
    private StringBuffer generContent(final String _strComId) {
        StringBuffer vResult = new StringBuffer("<script>function initTabPageSize(){sys_init_tabs_size(" + _strComId + ");");
        if (this.bIsFieldForm) {
            vResult = new StringBuffer("<script>function initTabPageSize(){");
        }
        for (int iSize = this.vecPageName.size(), i = 0; i < iSize; ++i) {
            final String strPageNameId = String.valueOf(_strComId) + this.strPageNamePreFix + i;
            final String strPageId = String.valueOf(_strComId) + this.strPagePreFix + i;
            final String strPageFrameId = String.valueOf(_strComId) + this.strPageFrame + i;
            final String strPageClassId = String.valueOf(_strComId) + this.strPageClass + i;
            String strClass = "";
            String strDisplay = "none";
            if (i == this.iCurIndex) {
                strClass = "_current";
                strDisplay = "";
            }
            this.sbComponent.append("<td id='").append(strPageNameId).append("' onclick='doPageClick(this,").append(String.valueOf(_strComId) + "," + i).append(");' style='padding:0 6px;' height='28px'><table border='0' cellpadding='0' cellspacing='0' id='" + strPageClassId + "_rt' class='sys_tabs_right" + strClass + "'><tr><td id='" + strPageClassId + "_lt' class='sys_tabs_left" + strClass + "'>").append(this.vecPageName.get(i)).append("</td></tr></table></td>");
            this.sbPage.append("<div id='").append(strPageId).append("' style='background-color: #fff;width:100%;height:100%;display:" + strDisplay + ";vertical-align:top;'>");
            if (this.vecPageType.get(i).toString().equals("0")) {
                this.sbPage.append(String.valueOf(StringRes.STRFRAMESTART) + " src='" + this.vecPageSrc.get(i) + "&v2=" + this.strPid + "' name='" + strPageFrameId + "'  id='" + strPageFrameId + "'></iframe>");
            }
            else {
                this.sbPage.append(this.vecPageSrc.get(i));
            }
            this.sbPage.append("</div>");
            vResult.append("sys_extends_parent_size(").append(strPageId).append(");");
        }
        vResult.append("}addInitEvent(initTabPageSize);");
        if (!this.bIsFieldForm) {
            this.sbComponent.append("<td class='tdquerybgsing'></td><td></td>");
        }
        vResult.append("sys_iStrCurPageIndex=").append(this.iCurIndex).append(";");
        vResult.append("function changeProjiect(_obj){window.location='View?SPAGECODE=1401789371249&v2='+_obj.value+'&iindex='+sys_iStrCurPageIndex;}");
        return vResult.append("</script>");
    }
    
    public void addPage(final String _strName, final String _strSrc) {
        this.vecPageName.add(_strName);
        this.vecPageSrc.add(_strSrc);
        this.vecPageType.add(0);
    }
    
    public void addPage(final String _strName, final StringBuffer _sbSrc) {
        this.vecPageName.add(_strName);
        this.vecPageSrc.add(_sbSrc);
        this.vecPageType.add(1);
    }
}
