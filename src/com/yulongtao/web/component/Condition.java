package com.yulongtao.web.component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Pub;
import com.yulongtao.sys.QRSC;
import com.yulongtao.util.EString;

public class Condition
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter out;
    private String strElementId;
    private Hashtable hashHQRC;
    
    public Condition(final HttpServletRequest _request, final HttpServletResponse _response, final String _strElementId) {
        this.request = _request;
        this.response = _response;
        this.strElementId = _strElementId;
        this.hashHQRC = (Hashtable) QRSC.HASHQRSC.get(this.strElementId);
        try {
            this.out = this.response.getWriter();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void viewNormalCondition() {
        this.out.print("<!DOCTYPE html> <html style=\"width:100%;height:100%;\">");
        this.out.print(Pub.STR_IMPORT);
        this.out.print("<style>.spanclose{cursor:pointer;}.spanclose:hover{color:red;}</style>");
        this.out.print("<style> *{font-family:\u5fae\u8f6f\u96c5\u9ed1;} .fiterbttn{padding:0 10px;float:left;margin-right:13px;height:35px;line-height:35px;border-top-left-radius: 5px; border-top-right-radius: 5px;} .fiteractive{background:#3fb6ee;color:#fff;} .fiternoactive{background:#eeeeee;color:#23527c;}.fiterbttn:hover{background:#3fb6ee;color:#fff;cursor:pointer;} .icon { display: inline-block; background: url(images/icon.png) no-repeat; vertical-align: middle; } .icon-file { width: 22px; height: 18px; margin-right: 5px; background-position: 0 -22px; float: left; margin-top: 5px; } .icon-minus { width: 15px; height: 15px; margin: 0 4px; background-position: -30px -22px; float: left; margin-top: 5px; } .icon-add { width: 15px; height: 15px; margin: 0 4px; background-position: -30px 0; float: left; margin-top: 8px; } .divpnode{cursor:pointer;height:30px;line-height:30px;border:1px solid #fff;} .divcnode{cursor:pointer;height:30px;line-height:30px;padding-left:50px;border:1px solid #fff;} .divpnode:hover{background:#e5f0fb;border:1px solid #bbd4ef;} .divcnode:hover{background:#e5f0fb;border:1px solid #bbd4ef;}button{padding:0 10px;margin-right:13px;height:30px;line-height:30px;border-radius: 5px;} .bttnblue{background:#3fb6ee;color:#fff;border:1px solid #269abc} .bttngray{background:#fff;color:#999999;border:1px solid #cccccc} </style>");
        final String strWinId = this.request.getParameter("gs_upl_kc");
        this.out.print("<script>var strWinId='" + strWinId + "';function closeWin(){parent.closeWinById(strWinId);}var sys_ObjSelDicType={};");
        final String[] arrStrFieldCode = this.hashHQRC.get("SFIELDCODE").toString().split(",");
        final String[] arrStrFieldName = this.hashHQRC.get("SFIELDNAME").toString().split(",");
        final String strIsDic = this.hashHQRC.get("STRANS").toString();
        if (!strIsDic.equals("")) {
            final String[] arrStrIsDic = strIsDic.split(",");
            for (int iDicCount = arrStrIsDic.length, i = 0; i < iDicCount; ++i) {
                if (!arrStrIsDic[i].equals("0")) {
                    this.out.print("sys_ObjSelDicType['" + arrStrFieldCode[i] + "']='" + arrStrIsDic[i] + "';");
                }
            }
        }
        final String strParamter = this.getPageParameter(this.request);
        this.out.print("function sys_doConditionSel(_obj){var strDicType=sys_ObjSelDicType[_obj.value];var strId='tjz'+_obj.id.substring(2);if(strDicType!=null){$(strId).className='sysselinput';}else $(strId).className='';}");
        this.out.print("function sys_doConditionSelValue(_obj){var strId='f'+_obj.id.substring(2);var strLgId='l'+_obj.id.substring(2);var strDicType=sys_ObjSelDicType[$(strId).value];if(strDicType!=null){miniWin('\u9009\u62e9\u503c','','Menu?O_SYS_TYPE=viewdic" + strParamter + "&d='+strDicType+'&v='+_obj.id+'&l='+$(strLgId).value,350,550,'','');}}");
        this.out.print("var sys_str_Cur_Con_Id=\"\"; function sys_del_Often_con() { if(sys_str_Cur_Con_Id==\"\"){ alert(\"\u5f53\u524d\u672a\u9009\u62e9\u4efb\u4f55\u65b9\u6848\uff01\"); return; } var vResult = getTx('comid=sys_100&conid=' + sys_str_Cur_Con_Id, 'docommand'); if (vResult == 'true') { var objCon = $('span_' + sys_str_Cur_Con_Id); objCon.parentNode.removeChild(objCon); $(\"inputcycxnm\").value=\"\"; $(\"tbconcontainer\").innerHTML=\"\"; iIdCount=0; } }");
        this.out.print("</script>");
        final String strFieldMsg = String.valueOf(Pub.generSelect("", arrStrFieldCode, this.hashHQRC.get("SFIELDNAME").toString().split(",")).toString()) + " onchange=\"sys_doConditionSel(this);\"";
        this.out.print("<body scroll=\"no\" style=\"width:100%;height:100%;overflow:auto;background:#fff;\"> <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#fff;width:1000px;height:450px;\"> <tr> <td style=\"width:280px;height:53px;border-bottom:1px solid #9fdaf6;\" valign=\"bottom\"> <div id=\"divcurcontitle\" class=\"fiterbttn fiteractive\" onclick=\"viewFieldsCons(this,'divfields','divconditions');\">\u5019\u9009\u6761\u4ef6</div> <div class=\"fiterbttn \" onclick=\"viewFieldsCons(this,'divconditions','divfields');\">\u67e5\u8be2\u65b9\u6848</div> </td> <td style=\"width:26px;\"></td> <td style=\"height:53px;border-bottom:1px solid #9fdaf6;\" valign=\"bottom\"><div class=\"fiterbttn fiteractive\">\u67e5\u8be2\u6761\u4ef6</div><div style='float:right;'><input id=\"inputcycxnm\" class=\"bttngray\" placeholder=\"\u65b9\u6848\u540d\u79f0\" style=\"padding-left:15px;margin-right:10px;\"><button class=\"bttngray\"  style=\"padding-left:15px;\" onclick=\"sys_clear_Often_con();\">\u6e05\u7a7a</button></div></td> </tr> <tr> <td style=\"height:53px;\"> <input type=\"text\" class=\"sysselinput\" style=\"width:100%;\"> </td> <td></td> <td> <label style=\"float:left;font-size:12px;\"> <input type=\"radio\" style=\"vertical-align:middle; margin-top:-2px; margin-bottom:1px;\" name=\"orderType\" id=\"timeraido\" value=\"1\" checked=\"true\">\u666e\u901a </label> <label style=\"float:left;font-size:12px;\"> <input type=\"radio\" style=\"vertical-align:middle; margin-top:-2px; margin-bottom:1px;\" name=\"orderType\" id=\"timeraido\" value=\"1\" checked=\"true\">\u9ad8\u7ea7 </label> </td> </tr>   <tr> <td valign='top'> <div id=\"divfields\" style=\"width:100%;height:400px;overflow:auto;border:1px solid #f2f2f2;display:;\">");
        this.out.print("<div class=\"divpnode\" onclick=\"doCloseTable(this,'A001');\" style=\"display:;\"> <i id=\"node_A001\" class=\"icon icon-minus\"></i> <i class=\"icon icon-file\"></i> " + this.hashHQRC.get("SPAGENAME") + " </div> <div id=\"chl_A001\">");
        for (int iFieldCount = arrStrFieldCode.length, j = 0; j < iFieldCount; ++j) {
            if (!arrStrFieldCode[j].startsWith("$")) {
                this.out.print("<div onclick=\"addQueryField('" + arrStrFieldName[j] + "','" + arrStrFieldCode[j] + "');\" class=\"divcnode\"> <i class=\"icon icon-file\"></i>" + arrStrFieldName[j] + " </div>");
            }
        }
        this.out.print("</div>");
        this.out.print("</div><div id=\"divconditions\" style=\"width:100%;height:400px;overflow:auto;border:1px solid #f2f2f2;display:none;\">");
        Object objCurUser = this.request.getSession().getAttribute("SYS_STRCURUSER");
        if (objCurUser == null) {
            objCurUser = "";
        }
        TableEx tableEx = null;
        String strInitScript = "";
        try {
            tableEx = new TableEx("S_CON_ID,S_CON_NM,S_CONDITION,I_CON_COUNT", "t_sys_user_condition", "S_PAGECODE='" + this.strElementId + "' and S_USER='" + objCurUser + "' and I_CON_COUNT<>-1");
            for (int iConCount = tableEx.getRecordCount(), k = 0; k < iConCount; ++k) {
                final Record record = tableEx.getRecord(k);
                final Object objConId = record.getFieldByName("S_CON_ID").value;
                this.out.println("<div id=\"span_" + objConId + "\" class=\"divpnode\" style=\"padding-left:15px;\" onclick=\"sys_do_NormalQueryCYCX('" + objConId + "'," + record.getFieldByName("I_CON_COUNT").value + ",'" + record.getFieldByName("S_CON_NM").value + "');\">" + record.getFieldByName("S_CON_NM").value + "</div>");
                this.out.println("<textarea id='con_" + objConId + "' style='display:none;'>" + record.getFieldByName("S_CONDITION").value + "</textarea>");
                strInitScript = "sys_do_NormalQueryCYCX('" + objConId + "'," + record.getFieldByName("I_CON_COUNT").value + ",'" + record.getFieldByName("S_CON_NM").value + "');";
            }
        }
        catch (Exception ex) {
            //
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        
        this.out.print("</div></td> <td></td> <td valign=\"top\"><div style=\"width:723px;height:400px;overflow:auto;\"><table id=\"tbconcontainer\" cellpadding=\"0\" cellspacing=\"0\">");
        this.out.print("</table></div></td> </tr>");
        this.out.print("<tr> <td align=\"left\" style=\"height:64px;\"> <button onclick=\"sys_doSaveNormalCondition();\" class=\"bttnblue\">\u4fdd\u5b58\u65b9\u6848</button> <button class=\"bttngray\"  style=\"padding-left:15px;\" onclick=\"sys_del_Often_con();\">\u5220\u9664\u65b9\u6848</button> </td> <td></td> <td align=\"right\"> <button class=\"bttnblue\" onclick=\"sys_doNormalCondition();\">\u786e\u5b9a</button> <button class=\"bttngray\" onclick=\"closeWin();\">\u53d6\u6d88</button> </td> </tr>");
        this.out.print("</table> </body> </html> <script> function doCloseTable(_obj,_strId){ objDivField=document.getElementById(\"chl_\"+_strId); if(objDivField.style.display==\"\"){ objDivField.style.display=\"none\"; document.getElementById(\"node_\"+_strId).className=\"icon icon-add\"; }else{ objDivField.style.display=\"\"; document.getElementById(\"node_\"+_strId).className=\"icon icon-minus\"; } }");
        this.out.print("var iIdCount=0; function addQueryField(_strName,_strCode){ var objTable=document.getElementById(\"tbconcontainer\"); var iRowsCount = objTable.rows.length; var objTr = objTable.insertRow(iRowsCount);  var strDicType = sys_ObjSelDicType[_strCode]; var strClassName=\"\"; var strSelDate=\"\"; var strLogic=\"<option value='like'>\u6a21\u7cca\u7b49\u4e8e</option>\"+ \"\t\t<option value='='>\u7b49\u4e8e</option>\"+ \"\t\t<option value='>'>\u5927\u4e8e</option>\"+ \"\t\t<option value='<'>\u5c0f\u4e8e</option>\"+ \"\t\t<option value='in'>\u5305\u542b</option>\"+ \"\t<option value='are'>\u4ecb\u4e8e</option>\"; var strEvent=\"sys_doConditionSelValue(this);\"; if (strDicType != null){ strClassName= 'sysselinput';strSelDate=''; strLogic=\"<option value='='>\u7b49\u4e8e</option>\"+ \"\t\t<option value='in'>\u5305\u542b</option>\";} if(_strName.indexOf('\u65f6\u95f4')!=-1 || _strName.indexOf('\u65e5\u671f')!=-1){strEvent='WdatePicker();';strClassName='Wdate';strLogic= \"<option value='like'>\u7b49\u4e8e</option>\"+\"<option value='are'>\u4ecb\u4e8e</option>\"+ \"<option value='>'>\u5927\u4e8e</option>\"+ \"<option value='<'>\u5c0f\u4e8e</option>\";} objTr.innerHTML=\"<td onclick='delCurRow(this);' style='width:50px;'><img src=\\\"images/close.png\\\"></td>\"+ \"<td style=\\\"float:left;margin:6px;padding-left:13px;border:1px dotted #dcdcdc;height:30px;line-height:30px;width:200px;overflow:hidden;\\\">\"+ _strName+\"<input id='f\"+iIdCount+\"' type='hidden' value='\"+_strCode+\"'></td>\"+  \"<td style=\\\"float:left;margin:6px;height:30px;width:120px;\\\">\"+ \"\t<select  onchange='sys_do_conChange(this,\"+iIdCount+\")'  id='l\"+iIdCount+\"' style=\\\"width:100%;height:100%\\\">\"+ strLogic+ \"\t</select>\"+ \"</td>\"+ \"<td style=\\\"float:left;margin:6px;height:30px;width:230px;\\\">\"+ \"\t<input onclick='\"+strEvent+\"' id='fv\"+iIdCount+\"' type=\\\"text\\\" class=\\\"\"+strClassName+\"\\\" style=\\\"width:100%;height:100%;\\\"><input id='fvh\"+iIdCount+\"' type='hidden' value=''><input id='tjzdate\"+iIdCount+\"'  type='hidden'>\"+ \"</td><td width='20'>\"+strSelDate+\"</td>\"; iIdCount++; }");
        this.out.print("var objCurViewDiv=$(\"divcurcontitle\"); function viewFieldsCons(_obj,_strViewDivId,_strHidDivId){ if(objCurViewDiv!=null) objCurViewDiv.className=\"fiterbttn\"; _obj.className=\"fiterbttn fiteractive\"; $(_strViewDivId).style.display=\"\"; $(_strHidDivId).style.display=\"none\"; objCurViewDiv=_obj; }");
        this.out.println("function delCurRow(_obj){ var objCurRow=_obj.parentNode; objCurRow.parentNode.removeChild(objCurRow); }");
        this.out.println("function sys_do_conChange(_obj,_iCount){ if(_obj.value==\"are\"){ var objCurValueInput=$(\"fv\"+_iCount); var objDateArea = document.createElement('div'); objDateArea.setAttribute('id','divdate'+_iCount); objDateArea.innerHTML=\"<input id='ds\"+_iCount+\"' type='text' style='width:103px;' class='Wdate' onclick='WdatePicker()'><font style='font-size:12px'> \u81f3 </font><input id='de\"+_iCount+\"' type='text' style='width:103px;' class='Wdate' onclick='WdatePicker()'>\"; objCurValueInput.parentNode.appendChild(objDateArea); objCurValueInput.style.display=\"none\"; }else{ var objCurValueInput=$(\"fv\"+_iCount); var objDateDiv=$('divdate'+_iCount); if(objDateDiv!=null) objCurValueInput.parentNode.removeChild(objDateDiv); objCurValueInput.style.display=\"\"; } }");
        this.out.print("</script>");
        this.out.print("<form id='formsavecon' action='YLWebAction' method='post'>");
        this.out.print("<input type=\"hidden\" name=\"NO_DOSCRIPT\" value=\"window.location='view.do?id=602" + this.strElementId + "&gs_upl_kc=" + strWinId + "';\">");
        this.out.print("<input id='t_sys_user_condition$S_PAGECODE' name='t_sys_user_condition$S_PAGECODE' type='hidden' value='" + this.strElementId + "'>");
        this.out.print("<input id='t_sys_user_condition$S_CON_ID' name='t_sys_user_condition$S_CON_ID' type='hidden' value='" + EString.generId() + "'>");
        this.out.print("<input id='t_sys_user_condition$S_CON_NM' name='t_sys_user_condition$S_CON_NM' type='hidden'>");
        this.out.print("<input id='t_sys_user_condition$S_USER' name='t_sys_user_condition$S_USER' type='hidden' value='" + objCurUser + "'>");
        this.out.print("<input id='t_sys_user_condition$I_CON_COUNT' name='t_sys_user_condition$I_CON_COUNT' type='hidden' value='0'>");
        this.out.print("<textarea id='t_sys_user_condition$S_CONDITION' name='t_sys_user_condition$S_CONDITION' style='display:none;'></textarea>");
        this.out.print("</form>");
        this.out.println("<script>" + strInitScript + "</script>");
    }
    
    public void viewCondition() {
        this.out.print(Pub.STR_IMPORT);
        this.out.print("<style>.spanclose{cursor:pointer;}.spanclose:hover{color:red;}</style>");
        final String strWinId = this.request.getParameter("gs_upl_kc");
        this.out.print("<script>var strWinId='" + strWinId + "';function closeWin(){parent.closeWinById(strWinId);}var sys_ObjSelDicType={};");
        final String[] arrStrFieldCode = this.hashHQRC.get("SFIELDCODE").toString().split(",");
        final String strIsDic = this.hashHQRC.get("STRANS").toString();
        if (!strIsDic.equals("")) {
            final String[] arrStrIsDic = strIsDic.split(",");
            for (int iDicCount = arrStrIsDic.length, i = 0; i < iDicCount; ++i) {
                if (!arrStrIsDic[i].equals("0")) {
                    this.out.print("sys_ObjSelDicType['" + arrStrFieldCode[i] + "']='" + arrStrIsDic[i] + "';");
                }
            }
        }
        final String strParamter = this.getPageParameter(this.request);
        this.out.print("function sys_doConditionSel(_obj){var strDicType=sys_ObjSelDicType[_obj.value];var strId='tjz'+_obj.id.substring(2);if(strDicType!=null){$(strId).className='sysselinput';}else $(strId).className='';}");
        this.out.print("function sys_doConditionSelValue(_obj){var strId='zd'+_obj.id.substring(3);var strDicType=sys_ObjSelDicType[$(strId).value];if(strDicType!=null){miniWin('\u9009\u62e9\u503c','','Menu?O_SYS_TYPE=viewdic" + strParamter + "&d='+strDicType+'&v='+_obj.id,350,550,'','');}}");
        this.out.print("function sys_del_Often_con(_strConId){var vResult=getTx('comid=sys_100&conid='+_strConId,'docommand');if(vResult=='true'){var objCon=$('span_'+_strConId);objCon.parentNode.removeChild(objCon);}}");
        this.out.print("</script>");
        final String strFieldMsg = String.valueOf(Pub.generSelect("", arrStrFieldCode, this.hashHQRC.get("SFIELDNAME").toString().split(",")).toString()) + " onchange=\"sys_doConditionSel(this);\"";
        this.out.print("<form id='add' action='view.do?id=601" + this.strElementId + "' method='post'>");
        this.out.print("<TABLE id='sys_tabop' WIDTH='100%' align='center' cellSpacing='0' cellPadding='0' border='0' class='table1'>");
        this.out.print(Pub.getTableHead(new String[] { "\u62ec\u53f7", "\u5b57\u6bb5", "\u6761\u4ef6", "\u6761\u4ef6\u503c", "\u903b\u8f91\u5173\u7cfb", "\u64cd\u4f5c" }));
        this.out.print(Pub.getTableTr("", new String[] { Pub.getKuoHao("kh1").toString(), String.valueOf(strFieldMsg) + "name='zd1' id='zd1'>", Pub.getTiaoJian("tj1").toString(), "<input type='text' id='tjz1' name='tjz1' onclick=\"sys_doConditionSelValue(this);\" style=\"width:160px;\"><input id='tjzdate1'  type='hidden'>", Pub.getLuoJiFu("ljf1").toString(), "<img src='images/eve/rgb.png' style='cursor:hand;' onclick='sys_del_row(this);'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='images/eve/xzrq.png' onclick=\"WdatePicker({el:'tjzdate1',onpicked:function() {$dp.$('tjz1').value=$dp.cal.getDateStr();}})\">" }));
        this.out.print("</table>");
        this.out.print("<textarea id='sys_fielddata' style='display:none;'>");
        this.out.print(strFieldMsg);
        this.out.print("</textarea>");
        this.out.print("<textarea id='sys_kuohaodata' style='display:none;'>");
        this.out.print("<input type='text' class='ylselect' style='width:60px;' texts=' ,(' codes=' ,(' ");
        this.out.print("</textarea>");
        this.out.print("<textarea id='sys_tiaojiandata' style='display:none;'>");
        this.out.print("<input type='text' class='ylselect' codes='=,LIKE,>,>=,<,<=,IN,NOT IN,<>,NOT LIKE' texts='\u7b49\u4e8e,\u6a21\u7cca\u7b49\u4e8e,\u5927\u4e8e,\u5927\u4e8e\u7b49\u4e8e,\u5c0f\u4e8e,\u5c0f\u4e8e\u7b49\u4e8e,\u5305\u542b,\u4e0d\u5305\u542b,\u4e0d\u7b49\u4e8e,\u975e\u6a21\u7cca\u7b49\u4e8e' ");
        this.out.print("</textarea>");
        this.out.print("<textarea id='sys_luojifudata' style='display:none;'>");
        this.out.print("<input type='text' onchange='sys_AddCondition(this)' class='ylselect' style='width:100px;' codes=',AND,OR,)AND,)OR,)' texts=',\u5e76\u4e14,\u6216\u8005,)\u5e76\u4e14,)\u6216\u8005,)' ");
        this.out.print("</textarea>");
        this.out.print(Pub.STR_FORMSAVEOP);
        this.out.print("</form>");
        Object objCurUser = this.request.getSession().getAttribute("SYS_STRCURUSER");
        if (objCurUser == null) {
            objCurUser = "";
        }
        this.out.print("<form id='formsavecon' action='YLWebAction' method='post'>");
        this.out.print("<input type=\"hidden\" name=\"NO_DOSCRIPT\" value=\"window.location='view.do?id=600" + this.strElementId + "&gs_upl_kc=" + strWinId + "';\">");
        this.out.print("<input id='t_sys_user_condition$S_PAGECODE' name='t_sys_user_condition$S_PAGECODE' type='hidden' value='" + this.strElementId + "'>");
        this.out.print("<input id='t_sys_user_condition$S_CON_ID' name='t_sys_user_condition$S_CON_ID' type='hidden' value='" + EString.generId() + "'>");
        this.out.print("<input id='t_sys_user_condition$S_CON_NM' name='t_sys_user_condition$S_CON_NM' type='hidden'>");
        this.out.print("<input id='t_sys_user_condition$S_USER' name='t_sys_user_condition$S_USER' type='hidden' value='" + objCurUser + "'>");
        this.out.print("<textarea id='t_sys_user_condition$S_CONDITION' name='t_sys_user_condition$S_CONDITION' style='display:none;'></textarea>");
        this.out.print("</form>");
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("S_CON_ID,S_CON_NM,S_CONDITION", "t_sys_user_condition", "S_PAGECODE='" + this.strElementId + "' and S_USER='" + objCurUser + "'");
            for (int iConCount = tableEx.getRecordCount(), j = 0; j < iConCount; ++j) {
                final Record record = tableEx.getRecord(j);
                final Object objConId = record.getFieldByName("S_CON_ID").value;
                this.out.println("<span id='span_" + objConId + "' class='sys_cycx_lable'><span onclick=\"sys_do_QueryCYCX('" + objConId + "');\">");
                this.out.println(record.getFieldByName("S_CON_NM").value);
                this.out.println("<textarea id='con_" + objConId + "' style='display:none;'>" + record.getFieldByName("S_CONDITION").value + "</textarea>");
                this.out.println("</span>&nbsp;&nbsp;<span class='spanclose' onclick=\"sys_del_Often_con('" + objConId + "');\">X</span></span>");
            }
        }
        catch (Exception ex) {
            //
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        
        this.out.print("<script>sys_doConditionSel($('zd1'));</script>");
    }
    
    public String getPageParameter(final HttpServletRequest _request) {
        String strFiterParam = "";
        final Enumeration paramNames = _request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            final String paraName = paramNames.nextElement().toString();
            if (!paraName.equals("id") && !paraName.equals("SPAGECODE")) {
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
    
    public void doCondition() {
        this.out.println(this.request.getSession());
    }
}
