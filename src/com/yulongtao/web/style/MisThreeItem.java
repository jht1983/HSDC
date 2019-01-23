/**
 * 
 */
package com.yulongtao.web.style;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Query;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Node;
import com.yulongtao.pub.TreeData;
import com.yulongtao.web.entrance.Face;

/**
 * @author tianshisheng
 *
 */
public class MisThreeItem extends ThreeItem {
    private boolean bIsViewChild;
    private String strMenuClass;
    private String strMoreClass;
    private TreeData treeData;
    
	public MisThreeItem(HttpServletRequest _request, HttpServletResponse _response) {
		super(_request, _response);
	}
    
    @Override
    public StringBuffer doHome() {
        final String strEvent = " onmouseover=\"ylt.Home.toolsOver(this);\" onmouseout=\"ylt.Home.toolsOut(this);\"";    	
    	String logocontainer = "<div id=\"logocontainer\" class=\"logocontainer w220\"><img src=\"images/menu/top_logo.png\"></div>";
    	String toptoolscontainer = 
    			"<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr class=\"toptoolstr\"><td>" + logocontainer + "</td><td><div class=\"toptoolscontainer h50\">" + 
    	        "<span class='span_ico_right' tip-data=\"\u9000\u51fa\" onclick=\"ylt.Home.bttnClick(this,7);\"" + 
    			strEvent + 
    			"><img src=\"new/images/exit-icon.png\" style=\"width: 20px;height:20px;vertical-align: middle;\"></span>" +
    			"<span class='span_ico_right' tip-data=\"\u4fee\u6539\u5bc6\u7801\" onclick=\"ylt.Home.bttnClick(this,9);\"" + 
    			strEvent + 
    			"><span style='color:#fff;font-size:13px;font-family:\u5fae\u8f6f\u96c5\u9ed1;vertical-align: middle;'>" 
    			+ this.strUserName + 
    			",\u60a8\u597d\uff01 </span></span>" +
    			"<span class='span_ico_right' tip-data=\"\u5934\u50cf\" onclick=\"ylt.Home.bttnClick(this,8);\"><img id='headimg' src=\"upload/" + 
    			this.strUserHead + "\" style=\"vertical-align: middle;\"></i></span>" +
    			"<span class='span_ico_right_time' style=\"color:#fff;font-size:14px;font-family:Î¢ÈíÑÅºÚ;vertical-align: middle;\"><div id=\"Show_Time\" style=\"font-size:13px;\"></div></span>" +
    			"<span class='span_ico_right_time' style=\"color:#fff;font-size:14px;font-family:Î¢ÈíÑÅºÚ;vertical-align: middle;\">µ±Ç°Ê±¼ä:</span></div></td></tr></table>";
    	
        final String strType = this.request.getParameter(this.strTypeName);
        final StringBuffer vResult = new StringBuffer();
        this.generMainHead(vResult);
        vResult.append("<body> <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" height=\"100%\"><tr height=\"10px\"><td colspan=\"2\">" + toptoolscontainer + "</td></tr><tr><td id=\"logomenucontainer\" class=\"logomenucontainer w220\" valign=\"top\" align=\"left\">").append("<div id=\"leftcontainer\" class=\"leftcontainer w220\"><div id=\"leftmenucontainer\" class=\"leftmenucontainer w220\">");
        this.generMenu(vResult);
        vResult.append("</div></div></td><td valign=\"top\">");
        //vResult.append("<span id=\"tools_expand_bttn\" class='span_ico_left' tip-data=\"\u6536\u7f29\u83dc\u5355\u680f\"  onclick=\"ylt.Home.bttnClick(this,1);\"" + strEvent + "><i class=\"iconfont icon-menufold\"></i></span>").append("<span class='span_ico_left' tip-data=\"\u56de\u9996\u9875\" onclick=\"ylt.Home.bttnClick(this,2);\"" + strEvent + "><i class=\"iconfont icon-shouye\"></i></span>").append("<span class='span_ico_left' tip-data=\"\u5237\u65b0\" onclick=\"ylt.Home.bttnClick(this,3);\"" + strEvent + "><i class=\"iconfont icon-refresh\"></i></span>").append("<span class='span_ico_left'><input type=\"text\" placeholder=\"\u641c\u7d22...\" autocomplete=\"off\" ></span>").append("<span class='span_ico_right' tip-data=\"\u5934\u50cf\" onclick=\"ylt.Home.bttnClick(this,8);\"><img id='headimg' src=\"upload/" + this.strUserHead + "\"></i></span>").append("<span class='span_ico_right' tip-data=\"\u9000\u51fa\" onclick=\"ylt.Home.bttnClick(this,7);\"" + strEvent + "><i class=\"iconfont icon-exit\"></i></span>").append("<span class='span_ico_right' tip-data=\"\u5168\u5c4f\" onclick=\"ylt.Home.bttnClick(this,6);\"" + strEvent + "><i class=\"iconfont icon-fullscreen\"></i></span>").append("<span class='span_ico_right' tip-data=\"\u98ce\u683c\" onclick=\"ylt.Home.bttnClick(this,5);\"" + strEvent + "><i class=\"iconfont icon-zhuti_tiaosepan_o\"></i></span>").append("<span class='span_ico_right' tip-data=\"\u6d88\u606f\" onclick=\"ylt.Home.bttnClick(this,4);\"" + strEvent + "><i class=\"iconfont icon-xiaoxi\"></i></span>").append("<span class='span_ico_right' tip-data=\"\u4fee\u6539\u5bc6\u7801\" onclick=\"ylt.Home.bttnClick(this,9);\"" + strEvent + "><span style='color:#999999;font-size:13px;font-family:\u5fae\u8f6f\u96c5\u9ed1;'>" + this.strUserName + ",\u60a8\u597d\uff01 </span></span>");
//        vResult.append("<span class='span_ico_right' tip-data=\"\u9000\u51fa\" onclick=\"ylt.Home.bttnClick(this,7);\"" + strEvent + "><img src=\"new/images/exit-icon.png\" style=\"width: 20px;height:20px;vertical-align: middle;\"></span>").append("<span class='span_ico_right' tip-data=\"\u4fee\u6539\u5bc6\u7801\" onclick=\"ylt.Home.bttnClick(this,9);\"" + strEvent + "><span style='color:#fff;font-size:13px;font-family:\u5fae\u8f6f\u96c5\u9ed1;vertical-align: middle;'>" + this.strUserName + ",\u60a8\u597d\uff01 </span></span>").append("<span class='span_ico_right' tip-data=\"\u5934\u50cf\" onclick=\"ylt.Home.bttnClick(this,8);\"><img id='headimg' src=\"upload/" + this.strUserHead + "\" style=\"vertical-align: middle;\"></i></span>").append("<span class='span_ico_right' style=\"color:#fff;font-size:13px;font-family:Î¢ÈíÑÅºÚ;vertical-align: middle;\"><div id=\"Show_Time\" style=\"font-size:13px;\"></div></span>").append("<span class='span_ico_right' style=\"color:#fff;font-size:13px;font-family:Î¢ÈíÑÅºÚ;vertical-align: middle;\">µ±Ç°Ê±¼ä:</span>");
        vResult.append("<iframe id=\"framehome\" name=\"framehome\" src=\"home.v?bmid=001017\"  frameborder=\"no\" border=\"0\"  width=\"100%\" height=\"100%\" scrolling=\"no\"></iframe> </td> </tr> </table><div id=\"sys_div_rect_tip\" class=\"recttip\" style=\"display:none;\"></div><div id=\"sys_div_msg_tip\" class=\"yl-tipbox\" style=\"display:none;\"></div><div id=\"sys_div_msg_right\" class=\"sys_div_msg_right\" style=\"display:none;\"></div></body> </html>");
        return vResult;
    }
    
    protected void generMainHead(final StringBuffer _sbHead) {
        _sbHead.append("<!DOCTYPE html><html style='width:100%;height:100%;'><title>");
        _sbHead.append(Face.strTitle);
        _sbHead.append("</title>");
        _sbHead.append("<script language=javascript src='js/ylwin.js'></script>");
        _sbHead.append("<script language=javascript src='js/ylttools.js'></script>");
        _sbHead.append("<script language=javascript src='res/js/ylthome.js?v=1.001'></script>");
        _sbHead.append("<link href='css/win.css' rel='stylesheet' type='text/css'>");
        _sbHead.append("<link href='res/css/treeitem.css' rel='stylesheet' type='text/css'>");
        _sbHead.append("<link href='res/css/iconfont.css' rel='stylesheet' type='text/css'>");
    }
    
    private void generMenuNode(final StringBuffer _sbMenu, final String _strParId, final String _strNodeId, final Node _node, final int _iLineHeight, final int _iLevel) {
        final ArrayList arrList = this.treeData.getListChildIds(_strNodeId);
        final int iListSize = arrList.size();
        String strMenuPadding = "";
        if (_iLevel > -1) {
            strMenuPadding = "padding-left:" + (42 + _iLevel * 14) + "px;";
        }
        if (iListSize > 0) {
            if (_iLineHeight == 50) {
                _sbMenu.append("<li onmouseover=\"ylt.Home.mainMenuOver(this);\" onmouseout=\"ylt.Home.mainMenuOut(this);\">");
               // _sbMenu.append("<div id=\"a" + _strNodeId + "\" onclick=\"ylt.Home.nodeMenuClick('par" + _strParId + "','" + _strNodeId + "');\" class=\"" + this.strMenuClass + "\" style=\"line-height:" + _iLineHeight + "px;" + strMenuPadding + "\">");
                _sbMenu.append("<div id=\"a" + _strNodeId + "\" onclick=\"ylt.Home.nodeMenuClick('par" + _strParId + "','" + _strNodeId + "');\" class=\"menunode\" style=\"line-height:" + _iLineHeight + "px;" + strMenuPadding + "\">");
                Object objPic = ((Hashtable)_node.objNodeValue).get("SPIC");
                if (objPic == null || objPic.toString().equals("") || !objPic.toString().startsWith("icon")) {
                    objPic = "icon-xitongguanli";
                }
                _sbMenu.append("<i class=\"iconfont ").append(objPic).append("\" style=\"padding:0px 12px;\"></i>");
            }
            else {
                _sbMenu.append("<li>");
                //_sbMenu.append("<div id=\"a" + _strNodeId + "\" onclick=\"ylt.Home.nodeMenuClick('par" + _strParId + "','" + _strNodeId + "');\" class=\"" + this.strMenuClass + "\" style=\"line-height:" + _iLineHeight + "px;" + strMenuPadding + "\">");
                _sbMenu.append("<div id=\"a" + _strNodeId + "\" onclick=\"ylt.Home.nodeMenuClick('par" + _strParId + "','" + _strNodeId + "');\" class=\"menunode\" style=\"line-height:" + _iLineHeight + "px;" + strMenuPadding + "\">");
                _sbMenu.append("<i></i>");
            }
           // _sbMenu.append("<span>").append(_node.strNodeName).append("</span>").append("<span  class=\"iconfont " + this.strMoreClass + "\" style=\"float:right;margin-right:10px;\"></span>");
            _sbMenu.append("<span>").append(_node.strNodeName).append("</span>").append("<span  class=\"iconfont icon-xiala\" style=\"float:right;margin-right:10px;\"></span>");
            String strDlStyle = "";
            if (_iLineHeight == 50) {
                strDlStyle = "padding-top:10px;padding-bottom:10px;";
            }
            if (this.bIsViewChild) {
                _sbMenu.append("</div><dl id=\"dl" + _strNodeId + "\" style=\"display:none;" + strDlStyle + "\">");
                _sbMenu.append("<script>ylt.Home.setExpandNode('par" + _strParId + "','" + _strNodeId + "');</script>");
                this.bIsViewChild = false;
                this.strMenuClass = "menunode";
                this.strMoreClass = "icon-xiala";
            }
            else {
                _sbMenu.append("</div><dl id=\"dl" + _strNodeId + "\" style=\"display:none;" + strDlStyle + "\">");
            }
            for (int i = 0; i < iListSize; ++i) {
                final Node nodeChild = this.treeData.getChid(arrList.get(i).toString());
                this.generMenuNode(_sbMenu, _strNodeId, nodeChild.strNodeId, nodeChild, 38, _iLevel + 1);
            }
            _sbMenu.append("</dl></li>");
        }
        else {
            _sbMenu.append("<dd><div onclick=\"ylt.Home.leafNodeMenuClick(this,'").append(((Hashtable)_node.objNodeValue).get("SURL")).append("');\" class=\"menunodeleaf\" style=\"line-height:" + _iLineHeight + "px;" + strMenuPadding + "\">").append(_node.strNodeName).append("</div></dd>");
        }
    }
    
    @Override
    protected void generMenu(final StringBuffer _sbMenu) {
        TableEx tableEx = null;
        this.dbf = new DBFactory();
        Query query = new Query("*", "T_SYS_MOD", "1=1 order by ISQL");
        if (!this.strUserCode.equals("888")) {
            if (!this.strGroupRole.equals("")) {
                final String strInRole = "SROLECODE in ('" + this.strGroupRole.replaceAll(",", "','") + "')";
                query = new Query("*", "T_SYS_MOD,t_roleright", "SMODCODE=SRIGHTCODE and (SROLECODE='" + this.strRoleCode + "' or " + strInRole + ") GROUP by SMODCODE order by ISQL");
            }
            else {
                query = new Query("*", "T_SYS_MOD,t_roleright", "SMODCODE=SRIGHTCODE and SROLECODE='" + this.strRoleCode + "' order by ISQL");
            }
        }
        this.treeData = new TreeData();
        try {
            tableEx = this.dbf.query(query);
            final int iRecordCount = tableEx.getRecordCount();
            final String strSplit = "";
            final String strSpanClass = "topmenutab";
            final String strInitModCode = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strModName = record.getFieldByName("SMODNAME").value.toString();
                final String strModCode = record.getFieldByName("SMODCODE").value.toString();
                final String strParCode = strModCode.substring(0, strModCode.length() - 3);
                this.treeData.addListChild(strModCode, strModName, record.clone(), strParCode);
            }
            final ArrayList arrList = this.treeData.getListChildIds("");
            for (int iListSize = arrList.size(), j = 0; j < iListSize; ++j) {
                final Node nodeChild = this.treeData.getChid(arrList.get(j).toString());
                this.generMenuNode(_sbMenu, "", nodeChild.strNodeId, nodeChild, 50, -1);
            }
        }
        catch (Exception ex) {
            return;
        }
        finally {
            this.dbf.close();
        }
        this.dbf.close();
    }
}
