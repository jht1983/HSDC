/**
 * 
 */
package com.yulongtao.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Debug;
import com.bfkc.hzp.viewrcs;
import com.yulongtao.db.Query;
import com.yulongtao.db.TableEx;
import com.yulongtao.pub.Pub;
import com.yulongtao.util.EString;
import com.yulongtao.web.WebQuery;
import com.yulongtao.web.YLTree;

/**
 * @author tianshisheng
 *
 */
public class MisDicManager extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    
    public void init() throws ServletException {
    }
    
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=GBK");
        final PrintWriter out = response.getWriter();
        final String strParams = request.getParameter("SYSPARAMS");
        if (strParams != null) {
            this.generParams(request, out, strParams);
            return;
        }
        final String strAdd = request.getParameter("STROPTYPE");
        final String strGSUPL = request.getParameter("gs_upl_kc");
        if (strAdd != null && strAdd.length() < 2) {
            Pub.importHead(out, request, "");
        }
        if (strAdd == null) {
            this.dicFrame(request, out);
        }
        else if (strAdd.equals("1")) {
            this.add(request, out);
        }
        else if (strAdd.equals("2")) {
            this.viewDic(request, out);
        }
        else if (strAdd.equals("3")) {
            this.addDic(request, out);
        }
        else if (strAdd.equals("4")) {
            this.viewTree(request, out);
        }
        else if (strAdd.equals("6")) {
            this.view(request, out);
        }
        else {
            out.print(Dic.getDicOption(strAdd, request.getParameter("selv")));
        }
    }
    
    private void generParams(final HttpServletRequest request, final PrintWriter out, final String strParams) {
        final viewrcs viewParams = new viewrcs();
        final String strValue = request.getParameter("value");
        if (strValue != null) {
            out.print(viewParams.viewtype(strParams, "name='" + request.getParameter("name") + "' style='width:120;'", strValue));
        }
        else {
            out.print(viewParams.viewtype(strParams, "name='" + request.getParameter("name") + "' style='width:120;'"));
        }
    }
    
    private void addDic(final HttpServletRequest request, final PrintWriter out) {
        final String SDICTYPEID = request.getParameter("SDICTYPEID");
        final String strWinId = request.getParameter("gs_upl_kc");
        out.println("<form id=\"add\" method=\"post\">");
        out.println("<TABLE class=\"table1\" WIDTH=\"95%\" align=\"center\" cellSpacing=\"0\" cellPadding=\"0\" border=\"0\" >");
        out.println("<TR class='tr1'><td class='td1'>\u5b57\u5178\u4ee3\u7801\uff1a</td><td class='td1'><input type='text' name='T_SYSDIC$SDICID' size=\"30\" rule=\"bxtx\" ruleTip=\"\u5b57\u5178\u4ee3\u7801\u5fc5\u987b\u586b\u5199\uff01\">*</td></tr>");
        out.println("<TR class='tr1'><td class='td1'>\u5b57\u5178\u540d\u79f0\uff1a</td><td class='td1'><input type='text' name='T_SYSDIC$SDICNAME' size=\"30\" rule=\"bxtx\" ruleTip=\"\u7c7b\u578b\u540d\u79f0\u5fc5\u987b\u586b\u5199\uff01\">*</td></tr>");
        out.println("<TR class='tr1'><td class='td1'>\u5b57\u5178\u989c\u8272\uff1a</td><td class='td1'><select name=\"T_SYSDIC$SDICCOLOR\" style='width:200px;'>");
        final Enumeration enuDicColor = Dic.hashRealColor.keys();
        out.println("<option value=\"\" style='background-color:;'>&nbsp;\u65e0&nbsp;</option>");
        while (enuDicColor.hasMoreElements()) {
            final String strColorCode = enuDicColor.nextElement().toString();
            final String[] arrStrColor = Dic.hashRealColor.get(strColorCode);
            out.println("<option value=\"" + strColorCode + "\" style='background-color:" + arrStrColor[0] + ";font-weight:bold;text-align:center;'>&nbsp;" + arrStrColor[1] + "&nbsp;</option>");
        }
        out.println("</select></td></tr>");
        out.println("</table>");
        out.println("<div align=\"center\"><button type=\"submit\">\u786e \u5b9a</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type=\"button\"  onclick=\"history.go(-1)\">\u53d6 \u6d88</button></div>");
        out.println("<input type=\"hidden\" name=\"T_SYSDIC$SDICTYPEID\" value=\"" + SDICTYPEID + "\">");
        out.println("<input type=\"hidden\" name=\"NO_UPL_KC\" value=\"" + strWinId + "\">");
        out.println("</form>");
        out.println("<script language=\"javascript\">gs_upl_kc='" + strWinId + "';add.T_SYSDIC$SDICNAME.focus();init(add);</script>");
        request.getSession().setAttribute("SYS_FORWARDTYPE_PAGEACTION", (Object)"PARPAGEMODEL");
    }
    
    private void viewDic(final HttpServletRequest request, final PrintWriter out) {
        try {
            Dic.init();
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'><body onload='loadpageEvent();' style='width:100%;height:100%;overflow:hidden;'>");
            String strTypeId = request.getParameter("SDICTYPEID");
            String strTypeName = "";
            if (strTypeId == null) {
                strTypeId = request.getParameter("SDICID");
                strTypeName = EString.encoderStr(request.getParameter("SDICNAME"));
            }
            else {
                strTypeName = EString.encoderStr(request.getParameter("SDICTYPENAME"));
            }
            final Query query = new Query("*", "T_SYSDIC", "SDICTYPEID='" + strTypeId + "' order by SDICID");
            final String[] arrcode = { "SDICID", "SDICNAME", "SDICID", "SDICOPNAME" };
            final String[] arrname = { "<font color='#e9bd6d'>" + strTypeName + "</font>\u4ee3\u7801", "<font color='#e9bd6d'>" + strTypeName + "</font>\u540d\u79f0", "\u989c\u8272", "\u64cd\u4f5c\u540d\u79f0" };
            final WebQuery webQuery = new WebQuery();
            webQuery.arrStrIsDic = new String[] { "0", "0", strTypeId, "0" };
            webQuery.strDelParam = "T_SYSDIC$SDICID=<<SDICID>>";
            webQuery.request = request;
            final String[] arrTdSize = { "120", "120", "120", "1" };
            webQuery.arrStrFileSize = arrTdSize;
            webQuery.strSize = "400,220";
            webQuery.strAddPage = "/DicManager?STROPTYPE=3&SDICTYPEID=" + strTypeId + "$\u589e\u52a0\u5b57\u5178";
            webQuery.bIsDic = true;
            out.println(webQuery.getAllCustMsg(query, arrcode, arrname));
        }
        catch (Exception e) {
            Debug.println("\u6253\u5f00\u5b57\u5178\u9519\u8bef\uff01" + e);
        }
        out.println("</body></html>");
    }
    
    private void add(final HttpServletRequest request, final PrintWriter out) {
        String strMod_Code = request.getParameter("SMODCODE");
        final String strWinId = request.getParameter("gs_upl_kc");
        if (strMod_Code == null) {
            strMod_Code = "";
        }
        final String strNewModCode = EString.generId();
        out.println("<form id=\"add\" method=\"post\">");
        out.println("<TABLE class=\"border\" WIDTH=\"100%\" align=\"center\" cellSpacing=\"0\" cellPadding=\"0\" border=\"0\">");
        out.println("<TR class='tr1'><td class='td1'>\u7c7b\u578b\u540d\u79f0\uff1a</td><td class='td1'><input type='text' name='T_SYSDICTYPE$SDICTYPENAME' size=\"30\" rule=\"bxtx\" ruleTip=\"\u7c7b\u578b\u540d\u79f0\u5fc5\u987b\u586b\u5199\uff01\">*</td></tr></table>");
        out.println("<div align=\"center\"><br><button type=\"submit\">\u786e \u5b9a</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type=\"button\"  onclick=\"closeWin();\">\u53d6 \u6d88</button></div>");
        out.println("<input type=\"hidden\" name=\"T_SYSDICTYPE$SDICTYPEID\" value=\"" + strNewModCode + "\">");
        out.println("</form>");
        out.println("<script language=\"javascript\">gs_upl_kc='" + strWinId + "';add.T_SYSDICTYPE$SDICTYPENAME.focus();init(add);</script>");
        request.getSession().setAttribute("SYS_FORWARDTYPE_PAGEACTION", (Object)"PARPAGEMODEL");
    }
    
    private void view(final HttpServletRequest request, final PrintWriter out) {
        try {
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'><body onload='loadpageEvent();' style='width:100%;height:100%;overflow:hidden;'>");
            final Query query = new Query("*", "T_SYSDICTYPE", "1=1 order by SDICTYPEID");
            final String[] arrcode = { "SDICTYPEID", "SDICTYPENAME" };
            final String[] arrname = { "\u7c7b\u578b\u4ee3\u7801", "\u7c7b\u578b\u540d\u79f0" };
            final WebQuery webQuery = new WebQuery();
            webQuery.href = "DicManager?STROPTYPE=2&<<SDICTYPEID>>&<<SDICTYPENAME>>";
            webQuery.strDelParam = "T_SYSDICTYPE$SDICTYPEID=<<SDICTYPEID>>&T_SYSDIC$SDICTYPEID=<<SDICTYPEID>>";
            final String[] arrTdSize = { "120", "1" };
            webQuery.arrStrFileSize = arrTdSize;
            webQuery.request = request;
            webQuery.strSize = "400,150";
            webQuery.strAddPage = "/DicManager?STROPTYPE=1$\u589e\u52a0\u7c7b\u578b";
            out.println(webQuery.getAllCustMsg(query, arrcode, arrname));
        }
        catch (Exception e) {
            Debug.println("\u6253\u5f00\u5b57\u5178\u7c7b\u578b\u9519\u8bef\uff01" + e);
        }
        out.println("</body></html>");
    }
    
    private void viewTree(final HttpServletRequest request, final PrintWriter out) {
        out.println("<body scroll='auto'>");
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("*", "T_SYSDICTYPE", "1=1 order by SDICTYPENAME");
            out.println("<div id='sys_dic_tree'></div>");
            final YLTree ylTree = new YLTree(tableEx, "SDICTYPEID", "", "SDICTYPENAME");
            ylTree.request = request;
            ylTree.setRootName("sys_dic_tree", "\u5b57\u5178\u7ba1\u7406");
            ylTree.setOnClick("reInvok(node);");
            out.print("<script>function reInvok(_objNode){if(_objNode.attributes.nodeCode=='')parent.lxmain .location='DicManager?STROPTYPE=6'; else parent.lxmain .location='DicManager?STROPTYPE=2&SDICTYPEID='+_objNode.attributes.SDICTYPEID+'&SDICTYPENAME='+_objNode.attributes.SDICTYPENAME;}");
            out.print(ylTree);
            out.print("</script>");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
        out.println("</body>");
    }
    
    private void viewTree1(final HttpServletRequest request, final PrintWriter out) {
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("*,'' pid", "T_SYSDICTYPE", "");
            final YLTree ylTree = new YLTree(tableEx, "SDICTYPEID", "pid", "SDICTYPENAME");
            ylTree.setRootName("divdictree", "\u5b57\u5178\u7ba1\u7406");
            ylTree.setOnClick("treeRedirect(node,'');");
            out.println("<body><div id='divdictree'></div>");
            out.println("<script>");
            out.println(ylTree);
            out.println("</script></body>");
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
    
    private void dicFrame(final HttpServletRequest request, final PrintWriter out) {
        out.println("<html>");
        out.println("<HEAD><TITLE>\u5b57\u5178\u7ba1\u7406</TITLE>");
        out.println("    <FRAMESET border=0 name=main frameSpacing=0 rows=* frameBorder=no cols=204,8,*>");
        out.println("        <FRAME id=lxleft title=lxleft name=lxleft src=\"DicManager?STROPTYPE=4\" noResize scrolling=auto>");
        out.println("        <FRAME name=lineFrame src=\"line.html\" noResize scrolling=no>");
        out.println("        <FRAME id=lxmain title=lxmain name=lxmain src=\"DicManager?STROPTYPE=6\">");
        out.println("    </FRAMESET>");
        out.println("</HTML>");
    }
    
    public void destroy() {
    }
}
