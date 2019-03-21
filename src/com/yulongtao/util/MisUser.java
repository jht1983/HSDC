package com.yulongtao.util;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.sys.Dic;

public class MisUser
{
    public String strUser;
    public String strPassword;
    private Hashtable hashRights;
    private Record recUserMsg;
    private HttpSession session;
    public String strErroMsg;
    private HttpServletRequest request;
    public static int iUserCount;
    private Vector vecRoles;
    
    static {
        User.iUserCount = -1;
    }
    
    public MisUser(final String aStrUser, final String aStrPassword) {
        this.strUser = "";
        this.strPassword = "";
        this.recUserMsg = null;
        this.strErroMsg = "";
        this.request = null;
        this.vecRoles = new Vector();
        this.strUser = aStrUser;
        this.strPassword = aStrPassword;
    }
    
    public MisUser(final HttpServletRequest aRequest) {
        this.strUser = "";
        this.strPassword = "";
        this.recUserMsg = null;
        this.strErroMsg = "";
        this.request = null;
        this.vecRoles = new Vector();
        this.strUser = aRequest.getParameter("user");
        this.strPassword = aRequest.getParameter("password");
        this.session = aRequest.getSession();
        this.request = aRequest;
    }
    
    public String getStaRole(final String _strBMBH, final String _strRoleCode) {
        String vResult = "";
        TableEx tablEx = null;
        try {
            tablEx = new TableEx("S_STANDARDCODE,S_ROLENAME", "t_standardtorole,t_standard_role", "t_standardtorole.S_STANDARDCODE=t_standard_role.S_ROLECODE and S_BMBH='" + _strBMBH + "' and t_standardtorole.S_ROLECODE='" + _strRoleCode + "'");
            for (int iRecordCount = tablEx.getRecordCount(), i = 0; i < iRecordCount; ++i) {
                if (i == 0) {
                    vResult = tablEx.getRecord(i).getFieldByName("S_STANDARDCODE").value.toString();
                }
                final String[] arrRole = { tablEx.getRecord(i).getFieldByName("S_STANDARDCODE").value.toString(), tablEx.getRecord(i).getFieldByName("S_ROLENAME").value.toString() };
                this.vecRoles.add(arrRole);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return vResult;
        }
        finally {
            if (tablEx != null) {
                tablEx.close();
            }
        }
        if (tablEx != null) {
            tablEx.close();
        }
        return vResult;
    }
    
    public void setDefaultProjiect() {
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("S_PROJIECTID,S_PPID", "t_projiect", "S_PPID<>'' order by S_PROJIECTID desc");
            if (tableEx.getRecordCount() > 0) {
                this.session.setAttribute("pid", (Object)tableEx.getRecord(0).getFieldByName("S_PROJIECTID").value.toString());
                this.session.setAttribute("ppid", (Object)tableEx.getRecord(0).getFieldByName("S_PPID").value.toString());
            }
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
    
    public String getDefaultProjiect() {
        TableEx tableEx = null;
        String vResult = "";
        try {
            tableEx = new TableEx("S_PROJIECTID,S_PPID", "t_projiect", "1=1 order by S_PROJIECTID desc");
            if (tableEx.getRecordCount() > 0) {
                vResult = String.valueOf(tableEx.getRecord(0).getFieldByName("S_PPID").value.toString()) + ":" + tableEx.getRecord(0).getFieldByName("S_PROJIECTID").value.toString();
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
    
    public int login() throws Exception {
        String strVer = this.request.getParameter("ver");
        if (strVer == null) {
            strVer = "";
        }
        strVer = strVer.toUpperCase();
        Object objVer = this.session.getAttribute("SYS_VERIFICATION_CODE");
        if (objVer == null) {
            objVer = "";
        }
        if (!strVer.equals(objVer.toString())) {
            return -2;
        }
        final int vResult = this.login("T_RGXX", "SYGZW_NEW", "SYGMM");
        if (vResult == 3) {
            this.session.setMaxInactiveInterval(-1);
            this.session.setAttribute("SYS_STRCURUSER", (Object)this.strUser);
            this.session.setAttribute("SYS_STRCURUSERNAME", (Object)this.getUserMsg("SYGMC"));
            final String strUserBranchId = this.getUserMsg("SBRANCHID");
            this.session.setAttribute("SYS_BRANCHID_SPLIT", (Object)strUserBranchId);
            this.session.setAttribute("SYS_STRBRANCHID", (Object)strUserBranchId.split(",")[0]);
            if (strUserBranchId.equals("")) {
                this.session.setAttribute("SYS_STRENTERPRISE", (Object)"");
            }
            else if (strUserBranchId.length() > 2) {
                this.session.setAttribute("SYS_STRENTERPRISE", (Object)strUserBranchId.substring(0, 3));
            }
            else {
                this.session.setAttribute("SYS_STRENTERPRISE", (Object)strUserBranchId.substring(0, 2));
            }
            Object objBranchName = Dic.hash.get("t_sys_branch_" + strUserBranchId);
            if (objBranchName == null) {
                objBranchName = "";
            }
            this.session.setAttribute("SYS_STRBRANCHNAME", (Object)objBranchName.toString());
            this.session.setAttribute("SYS_STRCURUSERHEAD", (Object)this.getUserMsg("S_HEADPIC"));
            final String strRoleCode = this.getUserMsg("SROLECODE");
            this.session.setAttribute("SYS_STRROLECODE", (Object)strRoleCode);
            this.session.setAttribute("SYS_STRZDZK", (Object)this.getUserMsg("FDISCOUNT"));
            this.session.setAttribute("SYS_USER_COUNT", (Object)User.iUserCount);
            this.session.setAttribute("SYS_STRCURUSER_IP", (Object)this.request.getRemoteAddr());
            this.setGroupRole(strRoleCode);
            this.setSys(this.strUser);
            final String strDefPId = this.getUserMsg("S_DEF_MOD");
            if (strDefPId.equals("")) {
                this.setDefaultProjiect();
            }
            else {
                final String[] arrStrDefPid = strDefPId.split(":");
                this.session.setAttribute("ppid", (Object)arrStrDefPid[0]);
                if (arrStrDefPid.length > 1) {
                    this.session.setAttribute("pid", (Object)arrStrDefPid[1]);
                }
                else {
                    this.session.setAttribute("pid", (Object)"");
                }
            }
            this.session.setAttribute("SYS_USER_LOGIN_DATE", (Object)EString.getCurDate());
            this.session.setAttribute("SYS_CURDATE", (Object)new SimpleDateFormat("hh:mm").format(new Date()));
            Dic.hash.put(this.strUser, this.getUserMsg("SYGMC"));
            this.initFiledRights(this.getUserMsg("SROLECODE"), this.getUserMsg("SBRANCHID"));
        }
        return vResult;
    }
    
    private void setGroupRole(final String strRoleCode) {
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("S_SONROLE", "t_sys_role", "SROLECODE='" + strRoleCode + "'");
            if (tableEx.getRecordCount() > 0) {
                this.session.setAttribute("SYS_STR_GROUPROLECODE", tableEx.getRecord(0).getFieldByName("S_SONROLE").value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            tableEx.close();
        }
        tableEx.close();
    }
    
    private void setSys(final String _strUid) {
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("I_SHZT", "t_sysjg", "S_UID='" + _strUid + "'");
            final int iRecordCount = tableEx.getRecordCount();
            if (iRecordCount > 0) {
                final Record record = tableEx.getRecord(0);
                this.session.setAttribute("SYS_IS_SYS_U", record.getFieldByName("I_SHZT").value);
            }
            else {
                this.session.setAttribute("SYS_IS_SYS_U", (Object)"-1");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            tableEx.close();
        }
        tableEx.close();
    }
    
    private int getUserCount() {
        int vResult = 0;
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("count(*) ct", "T_RGXX", "");
            vResult = Integer.parseInt(tableEx.getRecord(0).getFieldByName("ct").value.toString());
        }
        catch (Exception ex) {
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
    
    private void initFiledRights(final String _strRoleCode, final String _strBranchCode) {
        TableEx tableEx = null;
        int iRecordCount = 0;
        final Hashtable hashRoleFields = new Hashtable();
        Label_0279: {
            try {
                String strGroupRole = "";
                final Object objGroupRole = this.session.getAttribute("SYS_STR_GROUPROLECODE");
                if (objGroupRole != null) {
                    strGroupRole = objGroupRole.toString();
                }
                String strInRole = "";
                if (!strGroupRole.equals("")) {
                    strInRole = " or srolecode in ('" + strGroupRole.replaceAll(",", "','") + "')";
                }
                tableEx = new TableEx("SPAGECODE,SFIELDCODE", "t_sys_rightdetail", "srolecode='" + _strRoleCode + "'" + strInRole);
                iRecordCount = tableEx.getRecordCount();
                for (int i = 0; i < iRecordCount; ++i) {
                    final Record record = tableEx.getRecord(i);
                    final String strPageCode = record.getFieldByName("SPAGECODE").value.toString();
                    final Object objHashRights = hashRoleFields.get(strPageCode);
                    Hashtable hashFields = new Hashtable();
                    if (objHashRights != null) {
                        hashFields = (Hashtable)objHashRights;
                    }
                    hashFields.put(record.getFieldByName("SFIELDCODE").value.toString(), "OK");
                    hashRoleFields.put(strPageCode, hashFields);
                }
            }
            catch (Exception ex) {
                break Label_0279;
            }
            finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            if (tableEx != null) {
                tableEx.close();
            }
            try {
                tableEx = new TableEx("S_PAGECODE,S_FIELDCODE", "t_sys_branchfield", "S_ROLECODE='" + _strRoleCode + "' and S_BM='" + _strBranchCode + "'");
                iRecordCount = tableEx.getRecordCount();
                for (int j = 0; j < iRecordCount; ++j) {
                    final Record record = tableEx.getRecord(j);
                    final String strPageCode2 = record.getFieldByName("S_PAGECODE").value.toString();
                    final Object objHashRights2 = hashRoleFields.get(strPageCode2);
                    Hashtable hashFields2 = new Hashtable();
                    if (objHashRights2 != null) {
                        hashFields2 = (Hashtable)objHashRights2;
                    }
                    hashFields2.put(record.getFieldByName("S_FIELDCODE").value.toString(), "OK");
                    hashRoleFields.put(strPageCode2, hashFields2);
                }
                this.session.setAttribute("SYS_CUR_ROLE_FIELDRIGHTS", (Object)hashRoleFields);
            }
            catch (Exception ex2) {
                return;
            }
            finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
        }
        if (tableEx != null) {
            tableEx.close();
        }
    }
    
    public static String getUserCode(final HttpSession aSession) {
        return aSession.getAttribute("SYS_STRCURUSER").toString();
    }
    
    public static String getUserName(final HttpSession aSession) {
        return aSession.getAttribute("SYS_STRCURUSERNAME").toString();
    }
    
    public static String getUserJg(final HttpSession aSession) {
        return aSession.getAttribute("SYS_STRBRANCHID").toString();
    }
    
    public static String getUserJs(final HttpSession aSession) {
        return aSession.getAttribute("SYS_STRROLECODE").toString();
    }
    
    public String getUserMsg(final String aStrField) throws Exception {
        Object vResult = "";
        vResult = this.recUserMsg.getFieldByName(aStrField).value;
        if (vResult == null) {
            vResult = "";
        }
        return vResult.toString();
    }
    
    public int login(final String aStrTable, final String aStrUF, final String aStrPF) {
        final int vResult = 0;
        TableEx tableEx = null;
        try {
            if (this.getUserCount() > User.iUserCount) {
                return -1;
            }
            tableEx = new TableEx("*", aStrTable, String.valueOf(aStrUF) + "='" + this.strUser + "'");
            final int iRecordCount = tableEx.getRecordCount();
            if (iRecordCount <= 0) {
                this.strErroMsg = "\u5bf9\u4e0d\u8d77,\u4e0d\u5b58\u5728\u6b64\u7528\u6237\uff01";
                return 0;
            }
            final Record record = tableEx.getRecord(0);
            String strUserId = record.getFieldByName(aStrUF).value.toString();
            if (!strUserId.equals(this.strUser)) {
                this.strErroMsg = "\u5bf9\u4e0d\u8d77,\u4e0d\u5b58\u5728\u6b64\u7528\u6237\uff01";
                return 0;
            }
            final String strPsword = record.getFieldByName(aStrPF).value.toString();
            this.recUserMsg = record;
            final String strEnpwd = EncryptString.compute(this.strPassword);
            if (strPsword.equals("1") && this.strPassword.equals("1")) {
                return 2;
            }
            if (strPsword.equals(strEnpwd)) {
                strUserId = (this.strUser = record.getFieldByName("SYGZW").value.toString());
                this.updateUserLoginMsg(strUserId);
                return 3;
            }
            this.strErroMsg = "\u5bf9\u4e0d\u8d77,\u5bc6\u7801\u9519\u8bef\uff01\uff01";
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (tableEx != null) {
                tableEx.close();
            }
        }
        return vResult;
    }
    
    private void updateUserLoginMsg(final String _strUserId) {
        final DBFactory dbf = new DBFactory();
        TableEx tableEx = null;
        try {
            tableEx = new TableEx("t_rgxx");
            final Record record = new Record();
            record.addField(new FieldEx("SYGZW", _strUserId, true));
            record.addField(new FieldEx("S_LOGINTIME", EString.getCurDateHH()));
            record.addField(new FieldEx("S_LOGINIP", this.request.getRemoteAddr()));
            tableEx.addRecord(record);
            dbf.updateTable(tableEx, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            tableEx.close();
            dbf.close();
        }
        tableEx.close();
        dbf.close();
    }
}
