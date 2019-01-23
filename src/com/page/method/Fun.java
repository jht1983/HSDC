    package com.page.method;
    
    import java.util.Hashtable;
    import com.bfkc.process.ProcessRunOperation;
    import com.timing.impcl.EventCl;
    import com.timing.impcl.MantraLog;
    import com.timing.impcl.MeasuresTool;
    import com.timing.impcl.MantraUtil;
    import com.timing.impcl.SafeCheck;
    import com.timing.impcl.StandProTool;
    import com.timing.impcl.ProcessUtillMantra;
    import com.timing.impcl.RelationVO;
    import com.sip.business.BusinessUtil;//DEMO
    import com.yulongtao.pub.Pub;
    import java.io.PrintWriter;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import com.yulongtao.db.DBFactory;
    import com.yulongtao.db.Record;
    import com.yulongtao.util.EString;
    import com.yulongtao.db.TableEx;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Iterator;
    import com.bfkc.process.DataSynchronous;
    import com.yulongtao.util.EString;
    import com.timing.impcl.CheckTool;
    import com.timing.util.TimingInit;
    import java.io.File;
    import java.io.FileInputStream;
    import com.sis.util.TJSisData;
    import com.sun.org.apache.bcel.internal.generic.NEW;
    
    import java.io.OutputStream;
    import com.poi.temp.REPwordPoi;
    import java.io.ByteArrayOutputStream;
    import java.text.SimpleDateFormat;
    import javax.servlet.http.HttpSession;
    import java.util.Date;
    import com.extop.sip.sso.SsoUtil;
    import com.yulongtao.sys.Dic;
    import com.url.urlUtill.httpCon;
    public class Fun
    {
        
        
      public HttpServletRequest request;
      public HttpServletResponse response;
      public HashMap hashFieldValue;
        
        public StringBuffer ssoRegistered(){
            StringBuffer strBuf = new StringBuffer(); 
            String userCode = request.getParameter("userCode");
            String keyInfor = request.getParameter("keyInfor");
            
          
            
            
            
            if(userCode!=null){
                SsoUtil ssoUtil = new SsoUtil();
                strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("0");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append(ssoUtil.DataEncryption(keyInfor,userCode));
                strBuf.append("\"");
                strBuf.append("}");
            }else{
                strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("-4");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append("Transmission information is incomplete : userCode");
                strBuf.append("\"");
                strBuf.append("}");
            }
            return strBuf;
        }
        
      public StringBuffer ssoCheckUser() {
    		StringBuffer strBuf = new StringBuffer();
    		SsoUtil ssoUtil = new SsoUtil();
    		String userCode = request.getParameter("userCode");
    		String keyInfor = request.getParameter("keyInfor");
    		if (userCode == null) {
    		    
    		    strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("-4");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append("Transmission information is incomplete : userCode");
                strBuf.append("\"");
                strBuf.append("}");
    		    
    			
    			return strBuf;
    		}
    		if (keyInfor == null) {
    		    
    		     strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("-4");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append("Transmission information is incomplete : keyInfor");
                strBuf.append("\"");
                strBuf.append("}");
                
    			return strBuf;
    		}
    
    		int errCod = ssoUtil.CheckUserRegisteredInfor(keyInfor, userCode);
    		if (errCod == 0) {
    			TableEx tableEx = null;
    			int vResult = 0;
    			try {
    				tableEx = new TableEx("*", "T_RGXX", "SYGZW_NEW='" + userCode + "'");
    				int iRecordCount = tableEx.getRecordCount();
    			
    				 if (iRecordCount <= 0){
    					  strBuf.append("There is no account in the system");
    					  return strBuf;
    			      }
    			      Record record = tableEx.getRecord(0);
    				 HttpSession htpSess = request.getSession();
    // 				 htpSess.setAttribute("SYS_STRCURUSER",record.getFieldByName("SYGZW").value.toString());
    // 				htpSess.setAttribute("SYS_STRCURUSERNAME", record.getFieldByName("SYGMC").value.toString());
    // 				 String userBranchId=record.getFieldByName("SBRANCHID").value.toString();
    // 				 htpSess.setAttribute("SYS_STRBRANCHID",userBranchId);
    				 
    // 				 htpSess.setAttribute("SYS_BRANCHID_SPLIT",userBranchId);
    // 				 htpSess.setAttribute("SYS_STRROLECODE", record.getFieldByName("SROLECODE").value.toString());
    			
    // 		setGroupRole(record.getFieldByName("SROLECODE").value.toString());
    		
    // 				 strBuf.append( htpSess.getAttribute("SYS_STRCURUSER").toString());
    // 				 strBuf.append(htpSess.getAttribute("SYS_STRCURUSERNAME").toString());
    // 				 strBuf.append(htpSess.getAttribute("SYS_STRROLECODE").toString());
    				
    				 
    // 				  Object objBranchName = Dic.hash.get("t_sys_branch_"+userBranchId);
    // 	              if (objBranchName == null) {
    // 	                objBranchName = "";
    // 	              }
    // 	              htpSess.setAttribute("SYS_STRBRANCHNAME",objBranchName);
    	              
    	              
    	              //````````````````````
    	              int iUserCount = -1;
    	                htpSess.setMaxInactiveInterval(-1);
    	              htpSess.setAttribute("SYS_STRCURUSER",record.getFieldByName("SYGZW").value.toString());
    	              htpSess.setAttribute("SYS_STRCURUSERNAME", record.getFieldByName("SYGMC").value.toString());
    	              
    	              String strUserBranchId = record.getFieldByName("SBRANCHID").value.toString();
    	              htpSess.setAttribute("SYS_BRANCHID_SPLIT", strUserBranchId);
    	              
    	              htpSess.setAttribute("SYS_STRBRANCHID", strUserBranchId.split(",")[0]);
    	              
    	              Object objBranchName = Dic.hash.get("t_sys_branch_" + strUserBranchId);
    	              if (objBranchName == null) {
    	                objBranchName = "";
    	              }
    	              htpSess.setAttribute("SYS_STRBRANCHNAME", objBranchName.toString());
    	              
    	              String strRoleCode = record.getFieldByName("SROLECODE").value.toString();
    	              
    	              htpSess.setAttribute("SYS_STRROLECODE", strRoleCode);
    	              htpSess.setAttribute("SYS_STRZDZK", record.getFieldByName("FDISCOUNT").value.toString());
    	              htpSess.setAttribute("SYS_USER_COUNT", Integer.valueOf(iUserCount));
    	              htpSess.setAttribute("SYS_STRCURUSER_IP", this.request.getRemoteAddr());
    	              
    	              setGroupRole(strRoleCode);
    	              
    
    	           //   setSys(this.strUser);
    	              
    	              String strDefPId = record.getFieldByName("S_DEF_MOD").value.toString();
    	              if (strDefPId.equals(""))
    	              {
    	               // setDefaultProjiect();
    	              }
    	              else
    	              {
    	                String[] arrStrDefPid = strDefPId.split(":");
    	                htpSess.setAttribute("ppid", arrStrDefPid[0]);
    	                if (arrStrDefPid.length > 1) {
    	                  htpSess.setAttribute("pid", arrStrDefPid[1]);
    	                } else {
    	                  htpSess.setAttribute("pid", "");
    	                }
    	              }
    	              htpSess.setAttribute("SYS_USER_LOGIN_DATE", EString.getCurDate());
    	              
    	              htpSess.setAttribute("SYS_CURDATE", new SimpleDateFormat("hh:mm").format(new Date()));
    	              Dic.hash.put(userCode, record.getFieldByName("SYGMC").value.toString());
    	              initFiledRights(record.getFieldByName("SROLECODE").value.toString(), record.getFieldByName("SBRANCHID").value.toString());
    	              
    	              
    	              htpSess.setAttribute("SYS_STRCURUSERHEAD","");
    	           htpSess.setAttribute("ppid","FCM");
    	              htpSess.setAttribute("SYS_USER_COUNT","99999999");
    	              htpSess.setAttribute("SYS_IS_SYS_U","-1");
    	              
    	              
    			} catch (Exception e) {
    			    
    		    	MantraLog.fileCreateAndWrite(e);
    			} finally {
    				if (tableEx != null) {
    					tableEx.close();
    				}
    			}
    			
    			
    			strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("0");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append(keyInfor);
                strBuf.append("\"");
                strBuf.append("}");
    			
    			
    		}else if(errCod == -1){
    		     strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("-1");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append("The registration information has been destroyed");
                strBuf.append("\"");
                strBuf.append("}");
    		    
    		   
    		}else if(errCod == -2){
    		    
    		     strBuf.append("{");
                strBuf.append("\"smail\":\"");
                strBuf.append("-2");
                strBuf.append("\",");
                strBuf.append("\"keyInfor\":\"");
                strBuf.append(" User and registration information do not match");
                strBuf.append("\"");
                strBuf.append("}");
               
    		}
           
    		return strBuf;
    	}
        
         private void initFiledRights(String _strRoleCode, String _strBranchCode)
    	  {
    	      
    	    TableEx tableEx = null;
    	    int iRecordCount = 0;
    	     HttpSession htpSess = request.getSession();
    	    Hashtable hashRoleFields = new Hashtable();
    	    try
    	    {
    	      String strGroupRole = "";
    	      Object objGroupRole = htpSess.getAttribute("SYS_STR_GROUPROLECODE");
    	      if (objGroupRole != null) {
    	        strGroupRole = objGroupRole.toString();
    	      }
    	      String strInRole = "";
    	      if (!strGroupRole.equals("")) {
    	        strInRole = " or srolecode in ('" + strGroupRole.replaceAll(",", "','") + "')";
    	      }
    	      tableEx = new TableEx("SPAGECODE,SFIELDCODE", "t_sys_rightdetail", "srolecode='" + _strRoleCode + "'" + strInRole);
    	      iRecordCount = tableEx.getRecordCount();
    	      for (int i = 0; i < iRecordCount; i++)
    	      {
    	        Record record = tableEx.getRecord(i);
    	        String strPageCode = record.getFieldByName("SPAGECODE").value.toString();
    	        Object objHashRights = hashRoleFields.get(strPageCode);
    	        Hashtable hashFields = new Hashtable();
    	        if (objHashRights != null) {
    	          hashFields = (Hashtable)objHashRights;
    	        }
    	        hashFields.put(record.getFieldByName("SFIELDCODE").value.toString(), "OK");
    	        hashRoleFields.put(strPageCode, hashFields);
    	      }
    	    }
    	    catch (Exception localException) {}finally
    	    {
    	      if (tableEx != null) {
    	        tableEx.close();
    	      }
    	    }
    	    try
    	    {
    	      tableEx = new TableEx("S_PAGECODE,S_FIELDCODE", "t_sys_branchfield", "S_ROLECODE='" + _strRoleCode + "' and S_BM='" + _strBranchCode + "'");
    	      iRecordCount = tableEx.getRecordCount();
    	      for (int i = 0; i < iRecordCount; i++)
    	      {
    	        Record record = tableEx.getRecord(i);
    	        String strPageCode = record.getFieldByName("S_PAGECODE").value.toString();
    	        Object objHashRights = hashRoleFields.get(strPageCode);
    	        Hashtable hashFields = new Hashtable();
    	        if (objHashRights != null) {
    	          hashFields = (Hashtable)objHashRights;
    	        }
    	        hashFields.put(record.getFieldByName("S_FIELDCODE").value.toString(), "OK");
    	        hashRoleFields.put(strPageCode, hashFields);
    	      }
    	      htpSess.setAttribute("SYS_CUR_ROLE_FIELDRIGHTS", hashRoleFields);
    	    }
    	    catch (Exception localException1) {}finally
    	    {
    	      if (tableEx != null) {
    	        tableEx.close();
    	      }
    	    }
    	  }
    	
    	
    	
        
        
         private void setGroupRole(String strRoleCode){
        TableEx tableEx = null;
        try
        {
          tableEx = new TableEx("S_SONROLE", "t_sys_role", "SROLECODE='" + strRoleCode + "'");
          if (tableEx.getRecordCount() > 0) {
            request.getSession().setAttribute("SYS_STR_GROUPROLECODE", tableEx.getRecord(0).getFieldByName("S_SONROLE").value);
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
          tableEx.close();
        }
      }
        
        
        
        
        
        public StringBuffer getSisDate(){
            StringBuffer sr = new StringBuffer();
             TJSisData sisTool = new TJSisData();
             sisTool.init();
            return sr;
        }
        
        public StringBuffer getTableCollByName(){
    
            StringBuffer sr = new StringBuffer();
            String _tableOne=request.getParameter("tableOne");
            String _tableTwo=request.getParameter("tableTwo");
             MantraUtil mtu = new MantraUtil();
            if(_tableOne == null && _tableOne.trim().length() > 0){
                
            }else{
                sr.append("<td>");
               sr.append(mtu.getTableCol(_tableOne));
                sr.append("</td>");
            }
            if(_tableTwo == null && _tableTwo.trim().length() > 0){
                
            }else{
                sr.append("<td>");
                sr.append(mtu.getTableCol(_tableTwo));
                sr.append("</td>");
            }
            
           
            return sr;
        }
        
        public StringBuffer porcessEvent(String _type){
            StringBuffer sr = new StringBuffer();
            if("delAss".equals(_type)){
                MantraUtil mtu = new MantraUtil();
                String _S_ID=request.getParameter("S_ID");
                String _S_RUN_ID=request.getParameter("S_RUN_ID");
                String _S_FLOW_ID=request.getParameter("S_FLOW_ID");
                String _S_AUTO_VER=request.getParameter("S_AUTO_VER");
                mtu.delAss(_S_ID,_S_RUN_ID, _S_FLOW_ID, _S_AUTO_VER);
                sr.append("true");
            }
            
            return sr;
        }
         public StringBuffer getModPowerMeter(){
    
            StringBuffer sr = new StringBuffer();
           // String ModCode = "005007017001";
            String ModCode = request.getParameter("S_MODCODE");
            String sys_bed = request.getParameter("sys_bed");
            com.power.util.PermissionQuery perQuer = new com.power.util.PermissionQuery();
            if("true".equals(sys_bed)){
                perQuer.UpdateModPowerMeter(request);
            }
           
            
            return perQuer.ModPowerMeter(ModCode,request);
        }
        public StringBuffer logOut(){
            String[] loginAttr = { "SYS_STRCURUSER", "SYS_STRCURUSERNAME", "SYS_BRANCHID_SPLIT", "SYS_STRBRANCHID",
    					"SYS_STRBRANCHNAME", "SYS_STRROLECODE", "SYS_STRZDZK", "SYS_USER_COUNT", "SYS_STRCURUSER_IP",
    					"SYS_USER_LOGIN_DATE", "SYS_CURDATE" };
    		for (int i = 0, j = loginAttr.length; i < j; i++) {
    				request.removeAttribute(loginAttr[i]);
    		}
    		return new StringBuffer();
        }
        public StringBuffer pageFileData() {
    
            StringBuffer sr = new StringBuffer();
            String pageCode=request.getParameter("pageCode");
    
            if(pageCode == null && pageCode.trim().length() > 0){
                
            }else{
                
                try{
                    String [] pageCodeSplit = pageCode.split(",");
                    BusinessUtil bu = new BusinessUtil();
    
                    sr.append(bu.getJsonToString(pageCodeSplit[0],pageCodeSplit[1]));
                }catch(Exception e){
                    MantraLog.fileCreateAndWrite(e);
                }
            }
            
           
            return sr;
        }
         public StringBuffer keyTradingCon() {
             StringBuffer sb = new StringBuffer();
             sb.append("1234567890");
             MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"1234567890");
             return sb;
         }
         
         
         public StringBuffer NcSystemlogin() {
            TableEx tableEx = null;
            Record record = null;
            StringBuffer sb = new StringBuffer();
            String user ="";
            String retSsoKey="";
          
            try {
                tableEx = new TableEx("NCCode","t_rgxx","SYGZW='"+(String) request.getSession().getAttribute("SYS_STRCURUSER")+"'");
                int iRecordC=tableEx.getRecordCount();
                if(iRecordC>0){
                    record=tableEx.getRecord(0);
                    user = record.getFieldByName("NCCode").value.toString();
                }
                //String reqHttp = "http://61.134.61.241:9080/service/ssoRegServlet?ssoKey="+System.currentTimeMillis()+"&userCode="+user;
               // String reqHttp = "http://61.134.61.241:9080/service/ssoRegServlet?ssoKey=111111&userCode="+user;
                // retSsoKey=htt.sendGet(reqHttp, "");
                
                
            } catch(Exception e){
                sb.append(e.toString());
                MantraLog.fileCreateAndWrite(e);
            } finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            
            sb.append("var userCode='"+user+"';");
            
            sb.append("var ssoKey='"+System.currentTimeMillis()+"';");
            
            return sb;
         }
         
         
         public StringBuffer runLogInit(){
            String S_RZLX=request.getParameter("T_LBCSHJL$S_RZLX");
            String S_LBID=request.getParameter("T_LBCSHJL$S_LBID");
            String S_JZH=request.getParameter("T_LBCSHJL$S_JZH");
            String S_YY=request.getParameter("T_LBCSHJL$S_YY");
            String bmid=request.getParameter("NO_BMID_TOU");
            String sql="";
            String Id=EString.generId();
            switch (S_RZLX) {
        		case "ZZRZ":
        		    sql="insert into T_ZZYXRZ (S_ID,S_BCLB,S_ZZ,S_LBID,S_LB_TZNR,CREATETIME) values ('"+Id+"','YJB','"+bmid+"','"+S_LBID+"','"+S_YY+"',NOW());";
    			break;
                case "JZZRZ":
    			    sql="insert into T_JZZYXRZ (S_ID,S_BCLB,S_ZZ,S_LBID,S_JZ,S_LB_TZNR,CREATETIME) values ('"+Id+"','YJB','"+bmid+"','"+S_LBID+"','"+S_JZH+"','"+S_YY+"',NOW());";
    			break;
    			case "TLRZ":
    			    sql="insert into T_TLYXRZ (S_ID,S_BCLB,S_ZZ,S_LBID,S_LB_TZNR,CREATETIME) values ('"+Id+"','YJB','"+bmid+"','"+S_LBID+"','"+S_YY+"',NOW());";
    			break;
    			case "RLZBRZ":
    			    sql="insert into T_RLZBJL (S_ID,S_BCLB,S_ZZ,S_LBID,S_LB_TZNR,CREATETIME) values ('"+Id+"','YJB','"+bmid+"','"+S_LBID+"','"+S_YY+"',NOW());";
    			break;
    			case "HXBZJL":
    			    sql="insert into S_HXBZRZ (S_ID,S_BCLB,S_ZZ,S_LBID,S_LB_TZNR,CREATETIME) values ('"+Id+"','YJB','"+bmid+"','"+S_LBID+"','"+S_YY+"',NOW());";
    			break;
    			case "HSYXRZ":
    			    sql="insert into T_HSRZJL (S_ID,S_BCLB,S_ZZ,S_LBID,S_LB_TZNR,CREATETIME) values ('"+Id+"','YJB','"+bmid+"','"+S_LBID+"','"+S_YY+"',NOW());";
    			break;
        		default:
    			break;
    		}
    		DBFactory dbf = new DBFactory();
    		try{
    		    dbf.sqlExe(sql,false);
        		    dbf.sqlExe("update T_LBSCWHZB set S_SFYX ='CSH' where S_ID='"+S_LBID+"'",false);
    		}catch(Exception e){
    		    MantraLog.fileCreateAndWrite(e);
    		}finally{
    		    if (dbf!=null){
    		        dbf.close();
    		    }
    		}
            return null;
         }
    
      public StringBuffer getUserInfo(){
            StringBuffer sr = new StringBuffer();
            String strVresult="";
            TableEx tableEx=null;
            try{
                tableEx=new TableEx("*","t_sys_branch","S_CODE='001'");
                int iRecordC=tableEx.getRecordCount();
                Record record;
                for(int i=0;i<iRecordC;i++){
                    record=tableEx.getRecord(i);
                    sr.append(record.getFieldByName("S_NAME").value);
                }
            }catch(Exception e){
                sr.append(e.toString());
            } finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            return sr;
        }
        
        /*
        
        
       娴佺▼鎸傝捣
    
    	 * 鎸傝捣娴佺▼
    	 * @param _strFlowId 娴佺▼鍙?
    	 * @param _strFlowRunId 鑺傜偣鍙?
    	 * @param _strVersion 鐗堟湰鍙?
    	 * @param _strType 1:鎸傝捣 2:鍚敤
    	 * @return
    	 
    	public boolean processFlowHand(String _strFlowId,String _strFlowRunId,String _strVersion,String _strType) 
        
        
    
        public StringBuffer HandFlow(){
            StringBuffer returnSb = new StringBuffer();
            ProcessRunOperation pro = new ProcessRunOperation();
            String flowId=request.getParameter("flowId");
            String flowRunCode=request.getParameter("flowRunCode");
            String flowType=request.getParameter("flowType");
            String flowVersion=request.getParameter("flowVersion");
            if(flowId == null && flowId.trim().length() > 0){
                returnSb.append("娴佺▼ID涓虹┖");
                return returnSb;
            }
            if(flowRunCode == null && flowRunCode.trim().length() > 0){
               returnSb.append("娴佺▼杩愯ID涓虹┖");
                return returnSb; 
            }
            if(flowType == null && flowType.trim().length() > 0){
                returnSb.append("绫诲瀷涓虹┖");
                return returnSb;
            }
    
            try{
                boolean a= pro.processFlowHand(flowId,flowRunCode,flowVersion,flowType);
                returnSb.append(a);
            }catch(Exception e){
                returnSb.append("鍑虹幇閿欒");
                returnSb.append(e.toString());
            }
            return returnSb;
        }
       */ 
        
        public StringBuffer sevenGetString(){
            StringBuffer a = new StringBuffer();
            com.poi.java.PoiUtill poiu = new com.poi.java.PoiUtill();
    	    poiu.createExcel();
    
            String getVale = request.getParameter("value");
            if("TimingTask".equals(getVale)){
                a.append("<tr><td>");
                a.append(EventCl.vecGJStatus);
                a.append("</td></tr>");
            }else if("Err".equals(getVale)){
                a.append("<tr><td>");
                a.append(MantraLog.getFileErr());
                a.append("</td></tr>");
            }else if("LogPro".equals(getVale)){
                a.append("<tr><td>");
                a.append(MantraLog.getFileStrByFileName(MantraLog.LOG_PROGRESS));
                a.append("</td></tr>");  
            }else if("delete".equals(getVale)){
                MantraLog.deleteLog(MantraLog.LOG_PROGRESS);
                MantraLog.deleteLog();
                a.append("<tr><td>OK</td></tr>"); 
            }else if("getSession".equals(getVale)){
                a.append("<tr><td>");
                String[] loginAttr = { "SYS_STRCURUSER", "SYS_STRCURUSERNAME", "SYS_BRANCHID_SPLIT", "SYS_STRBRANCHID",
    					"SYS_STRBRANCHNAME", "SYS_STRROLECODE", "SYS_STRZDZK", "SYS_USER_COUNT", "SYS_STRCURUSER_IP",
    					"SYS_USER_LOGIN_DATE", "SYS_CURDATE" };
    			HttpSession htpSess = request.getSession();
    			for (int i = 0, j = loginAttr.length; i < j; i++) {
    				a.append(loginAttr[i]);
    				a.append(":");
    				a.append(htpSess.getAttribute(loginAttr[i]));
    				a.append(";</br>");
    			}
    			a.append("</td></tr>"); 
            }else if("startSisData".equals(getVale)){
                EventCl.isStartSis=true;
                a.append("<tr><td>");
                a.append(EventCl.isStartSis+"");
                
    			a.append("</td></tr>"); 
            }else if("AllSession".equals(getVale)){
                EventCl.isStartSis=true;
                a.append("<tr><td>");
                java.util.Enumeration   e   =   request.getSession().getAttributeNames(); 	
                while( e.hasMoreElements())   {     
                    String sessionName=(String)e.nextElement();    
                    a.append("\nsession item name="+sessionName); 
                    a.append("\nsession item value="+request.getSession().getAttribute(sessionName));
                    
                }
    
    
                
    			a.append("</td></tr>"); 
            }else if("endSisData".equals(getVale)){
                EventCl.isStartSis=false;
                a.append("<tr><td>");
                a.append(EventCl.isStartSis+"");
                
    			a.append("</td></tr>"); 
            }
            
            
           
            return a;
        }
        
        public StringBuffer getFlowUid(){
            StringBuffer sr = new StringBuffer();
    
            String _formId=request.getParameter("formId");
            String _bmid=request.getParameter("bmid");
            if(_formId == null && _formId.trim().length() > 0){
                sr.append("琛ㄥ崟ID涓虹┖");
                return sr;
            }
            if(_bmid == null && _bmid.trim().length() > 0){
                sr.append("缁勭粐涓虹┖");
                return sr;
            }
            
            MantraUtil mtu = new MantraUtil();
            sr.append("var UUID='");
            sr.append(mtu.getShortUuid());
            sr.append("';");
            sr.append("var S_AUDIT_VERSION='");
            sr.append(mtu.getFlowVer(_formId,_bmid));
            sr.append("';");
            return sr;
        }
        
        public StringBuffer htmlToMethod(){
            StringBuffer retStrBuf = new StringBuffer();
            String disType = request.getParameter("disType");
            boolean bol = false;
            if (disType == null && disType.trim().length() > 0) {// 琛ㄥ崟鍒ょ┖
    			retStrBuf.append("var error = 'Dissolve the error'");
    			return retStrBuf;
    		}
    
            switch (disType) {
        		case "XJJHTOXJJL":
        		    CheckTool ctl = new CheckTool();
        		    bol = ctl.inspectionPlanGeneratesRecords(request);
        		    retStrBuf.append("var error = '"+bol+"'");
    		    break;
    		    case "REALTIME":
        		
                    TJSisData sisTool = new TJSisData();
                    Map<String, String> sisData = sisTool.getSISData();
    
                     retStrBuf.append("var eleSISData='"+sisData.get(TJSisData.MD_FDL)+"';");
                     retStrBuf.append("var eleSISDataYfdl='"+sisData.get(TJSisData.MD_YFDL)+"';");
                     retStrBuf.append("var eleSISDataNfdl='"+sisData.get(TJSisData.MD_NFDL)+"';");
                     
    		    break;
    		    case "PPIDID":
        		
    		    break;
    		    case "EventCl":
            		new com.timing.util.TimingTaskTool().deleteEventClParameter(request.getParameter("SPAGECODE"),request.getParameter("S_ID"));
    		    break;
    		    case "startEventCl":
            		new com.timing.util.TimingTaskTool().startEventClParameter(request.getParameter("SPAGECODE"),request.getParameter("S_ID"));
    		    break;
    		    case "stopEventCl":
            		new com.timing.util.TimingTaskTool().stopEventClParameter(request.getParameter("SPAGECODE"),request.getParameter("S_ID"));
    		    break;
    		    case "poirEO":
    		        
    		        String S_V_ID=request.getParameter("S_ID");
    		        MantraUtil tool =new MantraUtil();
    		        REPwordPoi rep = new REPwordPoi();
    		        String sql = "select T_BZGZRZ.SYS_FLOW_VER T_BZGZRZ__SYS_FLOW_VER,T_BZGZRZ.S_BJ T_BZGZRZ__S_BJ,(SELECT t_sys_branch.S_NAME FROM t_sys_branch WHERE	t_sys_branch.S_CODE = T_BZGZRZ.S_BM) T_BZGZRZ__S_BM,(SELECT t_bz.S_MC FROM	t_bz	WHERE	t_bz.S_BM = T_BZGZRZ.S_BZ	 and s_zz='001001') T_BZGZRZ__S_BZ,T_BZGZRZ.S_BZRS T_BZGZRZ__S_BZRS,T_BZGZRZ.S_CD T_BZGZRZ__S_CD,T_BZGZRZ.S_CQL T_BZGZRZ__S_CQL,T_BZGZRZ.S_CQRS T_BZGZRZ__S_CQRS,T_BZGZRZ.S_DJH T_BZGZRZ__S_DJH,T_BZGZRZ.S_GCRY T_BZGZRZ__S_GCRY,T_BZGZRZ.S_HX T_BZGZRZ__S_HX,T_BZGZRZ.S_ID T_BZGZRZ__S_ID,T_BZGZRZ.S_JCKHYJ T_BZGZRZ__S_JCKHYJ,T_BZGZRZ.S_KG T_BZGZRZ__S_KG,T_BZGZRZ.S_QTJS T_BZGZRZ__S_QTJS,T_BZGZRZ.S_RUN_ID T_BZGZRZ__S_RUN_ID,T_BZGZRZ.S_SJ T_BZGZRZ__S_SJ,T_BZGZRZ.S_TQ T_BZGZRZ__S_TQ,T_BZGZRZ.S_TXRQ T_BZGZRZ__S_TXRQ,T_BZGZRZ.S_XQ T_BZGZRZ__S_XQ,T_BZGZRZ.S_YJZBRY T_BZGZRZ__S_YJZBRY,T_BZGZRZ.S_ZDR T_BZGZRZ__S_ZDR,T_BZGZRZ.S_ZDSJ T_BZGZRZ__S_ZDSJ,T_BZGZRZ.S_ZT T_BZGZRZ__S_ZT,T_BZGZRZ.S_ZZ T_BZGZRZ__S_ZZ,T_BZGZRZ.T_AQQK T_BZGZRZ__T_AQQK,T_BZGZRZ.T_BHHYJL T_BZGZRZ__T_BHHYJL,T_BZGZRZ.T_BQHYJL T_BZGZRZ__T_BQHYJL	 from T_BZGZRZ where S_ID='"+S_V_ID+"'";
            		String value = "T_BZGZRZ__S_BM,T_BZGZRZ__S_BZ,T_BZGZRZ__S_TXRQ,T_BZGZRZ__S_TXRQ,T_BZGZRZ__S_XQ,T_BZGZRZ__S_TQ,T_BZGZRZ__S_BZRS,T_BZGZRZ__S_CQRS,T_BZGZRZ__S_CQL,T_BZGZRZ__S_BJ,T_BZGZRZ__S_SJ,T_BZGZRZ__S_CD,T_BZGZRZ__S_ZT,T_BZGZRZ__S_KG,T_BZGZRZ__S_HX,T_BZGZRZ__S_GCRY,T_BZGZRZ__S_YJZBRY,T_BZGZRZ__S_GCRY,T_BZGZRZ__S_YJZBRY,T_BZGZRZ__T_BQHYJL,T_BZGZRZ__T_BHHYJL,T_BZGZRZ__T_AQQK,T_BZGZRZ__S_QTJS,T_BZGZRZ__S_JCKHYJ";
            		String[] varArr = value.split(",");
            		TableEx tableEx = null;
            		Record record = null;
            		DBFactory dbf =new DBFactory();
            		Map<String, String> hm = new HashMap<String,String>();
            		String dataStr = "";
            		String bzmc="";
            		String rq="";
            		try {
            			tableEx = dbf.query(sql);
            			for(int i = 0 ; i < tableEx.getRecordCount() ; i++) {
            				record = tableEx.getRecord(i);
            				bzmc = tool.getStrByRecord(record, "T_BZGZRZ__S_BZ");
            				rq= tool.getStrByRecord(record, "T_BZGZRZ__S_TXRQ");
            				for(int j = 0 ; j < varArr.length ; j++) {
            				    
            				    dataStr = tool.getStrByRecord(record, varArr[j]);
            			
            					hm.put("${"+(j+1)+"}", dataStr);
            				}
            				
            			}
                        
                        List<HashMap> tableDate = new ArrayList<HashMap>();
                        
                        HashMap<String,String> hmt = null;
                        sql = "select S_NR nr,S_WCQK wcqk,S_FZR fzr from t_bzgzrz_son where S_FID='"+S_V_ID+"' order by S_ID+0";
                        tableEx = dbf.query(sql);
                        for(int i = 0 ; i < tableEx.getRecordCount() ; i++) {
                            hmt = new HashMap();
            				record = tableEx.getRecord(i);
            				hmt.put("${1}", tool.getStrByRecord(record, "nr"));
            				hmt.put("${2}", tool.getStrByRecord(record, "wcqk"));
            				hmt.put("${3}", tool.getStrByRecord(record, "fzr"));
            				
            				tableDate.add(hmt);
            			}
    
            			response.reset();
            			response.setContentType("application/x-msdownload");
            // 		long timeStamp = System.currentTimeMillis();  
            //             SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            //             String sd = sdf.format(new Date(timeStamp)); 
                         
            			//榆神热电XX班工作日志+日期
            			String fileName = "榆神热电"+bzmc+"班工作日志"+rq+".docx";
            			response.addHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes(),"ISO8859-1"));
            			
            			//测试环境
            			// ByteArrayOutputStream ostream = rep.readwriteWord("../webapps/sip/upload/153432489673410002_0.docx", hm,tableDate);
                        //生产
                          ByteArrayOutputStream ostream = rep.readwriteWord("webapps/ROOT/wordtemp/153051724776710002_0.docx", hm,tableDate);
      
            		    retStrBuf.append("\u6b63\u5728\u4e0b\u8f7d");
            			
            			OutputStream out = response.getOutputStream();
            			
            			out.write(ostream.toByteArray());
    
    			        out.close();
            			
            		}catch (Exception e) {
            		 MantraLog.fileCreateAndWrite(e);
            		} finally {
                        if (tableEx != null) {
                            tableEx.close();
                        }
                    }
    		        
            		
    		    break;
            }
            
            
            return retStrBuf;
        }
        
    	public StringBuffer getFormFields() {
    		StringBuffer sb = new StringBuffer();
    		String formCod = request.getParameter("formId");
    		String fromType = request.getParameter("fromType");
    		if (formCod == null && formCod.trim().length() > 0) {// 琛ㄥ崟鍒ょ┖
    			sb.append("娴佺▼ID涓虹┖");
    			return sb;
    		}
    		if ("2".equals(fromType)) {// 鍗曚竴琛ㄥ崟
    			String[][] PageField = new Pub().getPageField(formCod);
    			for (int i = 0, j = PageField[0].length; i < j; i++) {
    				sb.append(PageField[0][j]).append(":").append(PageField[1][j]);
    			}
    		} else {// 澶嶅悎琛ㄥ崟
    
    		}
    		return sb;
    	}
        
        public StringBuffer getLCJHBZCod(){
            StringBuffer sr = new StringBuffer();
            sr.append("var UUID='");
            MantraUtil mtu = new MantraUtil();
            sr.append(mtu.getShortUuid());
            sr.append("';");
             sr.append("var S_AUDIT_VERSION='");
            //select S_AUDIT_VERSION from T_SYS_FLOW_MAIN where S_FORMS ="1510196651437" order by S_AUDIT_VERSION desc limit 1
            String bmid = request.getParameter("bmid");
            
            String strVresult="";
            TableEx tableEx=null;
            try{
                tableEx=new TableEx("S_AUDIT_VERSION","T_SYS_FLOW_MAIN"," S_FORMS =\"1510196651437\" and S_ORG_ID=\"" + bmid + "\" order by S_AUDIT_VERSION desc limit 1 ");
                int iRecordC=tableEx.getRecordCount();
                Record record;
                for(int i=0;i<iRecordC;i++){
                    record=tableEx.getRecord(i);
                    sr.append(record.getFieldByName("S_AUDIT_VERSION").value);
                }
            }catch(Exception e){
                MantraLog.fileCreateAndWrite(e);
            } finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            sr.append("';");
            return sr;
        }
        
        public void MeasuresToolEntr(HttpServletRequest request){
            // MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"G:");
    
    		MeasuresTool mt = new MeasuresTool();
    		SafeCheck sc = new SafeCheck();
    		String formCode = request.getParameter("SPAGECODE");
    		String S_ID=request.getParameter("S_ID");
    		
            switch (formCode) {
    		case "1510196651437":
    		    
    			mt.MeasuresPlanBusinessDeal(S_ID);
    			
    			break;
    		case "1513048527561":
    	
    			mt.PrinMeasuresPlanBusinessDeal(S_ID);
    			
    			break;
    		case "1505896107531":
      
            	CheckTool ct = new CheckTool();
    			ct.checkToolBusinessDeal(S_ID);
    			
    			break;
    		case "1516247158225"://Eric淇敼 2018骞?2鏈?5鏃? 15:27:28
      
            
    			sc.checkToolBusinessDeal(S_ID);
    			
    			break;
    		case "1516166904515"://Eric淇敼 2018骞?2鏈?5鏃? 15:27:39
                
              
        		sc.branchSafeCheck(S_ID);
    		
    	    	break;
    	    case "15175538437610"://Eric淇敼 2018骞?2鏈?23鏃? 10:09:59
                
              
        		sc.affirmSafeCheck(S_ID);
    		
    	    	break;	
    	    case "1516587886146"://Eric淇敼 2018骞?2鏈?23鏃? 17:11:33
                
              
        		sc.modifySafeCheck(S_ID);
    		
    	    	break;
        	case "1516602563575"://Eric淇敼 2018骞?2鏈?24鏃? 15:13:12
                
              
        		sc.alterSafeCheck(S_ID);
    		
    	    	break;
    	    case "1516606174518"://Eric淇敼 2018骞?2鏈?24鏃? 16:19:31
                
              
        		sc.ratHazard(S_ID);
    		
    	    	break;
    	    case "1516613463357"://Eric淇敼 2018骞?2鏈?24鏃? 16:48:46
                
              
        		sc.govern(S_ID);
    		
    	    	break;
    	    
    
    	    case "1506310525794"://Eric淇敼 2018骞?2鏈?28鏃? 11:43:54
                
              
        		sc.outProject(S_ID);
    		
    	    	break;
    	    	
    	      case "1515723789958"://Eric淇敼 2018骞?3鏈?5鏃? 13:17:55
                
              
        		sc.borrowTools(S_ID);
    		
    	    	break;
    	    case "1522727526758"://Eric 2018年4月4日 13:52:53//技术
                
              
        		sc.innovate(S_ID);
    		
    	    	break;
    	    	
    	    case "1522732741869"://Eric 2018-4-4 13:54:22 优秀
                
              
        		sc.excellent(S_ID);
    		
    	    	break;
    	   case "1522719345443"://Eric 2018-4-4 13:54:37 合理化建议
                
              
        		sc.proposal(S_ID);
    		
    	    	break;
    		case "1517475877461"://Manter 2018-02-22 16:27:33
                new com.timing.impcl.SafetyInquireTool().SafetyInquireSum(request);
    		    break;
    		case "151772091835710956"://Manter 2018-02-22 16:27:33
                new com.timing.impcl.SafetyInquireTool().SafetyInquireBranch(request);
    		    break;    
    		    
    		 case "55555555555555555555"://Manter 2018-02-24 16:33:12
                new com.timing.impcl.SafetyInquireTool().SafetyInquireToRect(request);
    		    break;      
    		default:
    			break;
    		}
        }
        /**
         * 2018/1/2 16:56
         * 
         * Mantra
         * 
         * 
         **/
        public void timingTaskData() throws Exception{
            HashMap<String, String> vecAddDate = new HashMap<String, String>();
            String _strSplitN=",";
            Map hm = request.getParameterMap();
            Iterator entries = hm.entrySet().iterator();
            while (entries.hasNext()) {  
                Map.Entry entry = (Map.Entry) entries.next();
                String sys_PageKey = (String) entry.getKey();  
                if(sys_PageKey.indexOf("NO_")==0){
                    continue;
                }
                if(sys_PageKey.indexOf("$")==0){
                    continue;
                }
                sys_PageKey = sys_PageKey.substring(sys_PageKey.indexOf("$")+1,sys_PageKey.length());
                // String anb=new String("asa".getBytes("iso8859-1"),"UTF-8");
                StringBuffer sys_PageValue = new StringBuffer();
                for(String a : (String [])entry.getValue()){
                    sys_PageValue.append(new String(a.getBytes("iso8859-1"),"GBK"));
                    sys_PageValue.append(_strSplitN);
                }
                vecAddDate.put(sys_PageKey, sys_PageValue.substring(0,sys_PageValue.length()-_strSplitN.length()));
            }
    		EventCl.vecGJStatus.add(vecAddDate);
            EventCl.sys_index++;
        }
        
    public StringBuffer backFlow(){
        StringBuffer sb = new StringBuffer();
        ProcessRunOperation pro = new ProcessRunOperation();
        String flowId=request.getParameter("flowId");
        String flowRunCode=request.getParameter("flowRunCode");
        String flowRunUserCode=request.getParameter("flowRunUserCode");
        String flowVersion=request.getParameter("flowVersion");
        
     
        if(flowId == null && flowId.trim().length() > 0){
            sb.append("流程ID为空");
            return sb;
        }
        if(flowRunCode == null && flowRunCode.trim().length() > 0){
           sb.append("流程运行ID为空");
            return sb; 
        }
        if(flowRunUserCode == null && flowRunUserCode.trim().length() > 0){
            sb.append("运行用户为空");
            return sb;
        }
        //监控撤回
        //EString.getCurDate()
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---------------------[backFlow_start]---------------------");
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->FLOW_ID="+flowId);
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->FLOW_RUN_CODE="+flowRunCode);
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->FLOW_RUN_USER_CODE="+flowRunUserCode);
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->FLOW_VERSION="+flowVersion);
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->date="+EString.getCurDate());
        try{
            boolean  results = pro.backFlowRun(flowId,flowRunCode,flowVersion,flowRunUserCode);
            MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->pro.backFlowRun:"+results);
            if(results==true){
                ProcessUtillMantra pUm = new ProcessUtillMantra();
                results = pUm.delRunLog(flowId,flowRunCode,flowVersion,flowRunUserCode);
                MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"[backFlow]->pUm.delRunLog:"+results);
            }
            sb.append( "var SYS_FLOW_RUNOVER='"+results+"';" );
        }catch(Exception e){
            sb.append("出现错误");
            sb.append(e.toString());
        }
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,sb.toString());
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---------------------[backFlow_start]---------------------");
        return sb;
    }
      public StringBuffer invoke(String paramString1, String paramString2)
      {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("<br>").append(paramString1).append("<br>").append(paramString2);
        localStringBuffer.append(this.request.getParameter("a"));
        String[][] arrayOfString = new Pub().getPageField("1499224454197");
        int i = arrayOfString[0].length;
        for (int j = 0; j < i; j++)
          localStringBuffer.append(arrayOfString[0][j]).append(":").append(arrayOfString[1][j]);
        return localStringBuffer;
      }
      
        public StringBuffer tableLatestData()
          {
            TableEx tableEx = null;
            Record record = null;
            StringBuffer localStringBuffer = new StringBuffer();
            String tableNameToSelect= this.request.getParameter("tableNameToSelect");
            String tableCol= this.request.getParameter("tableCol");
            String tableCond= this.request.getParameter("tableCond");
            if ((tableNameToSelect != null) && (tableNameToSelect.trim().length() > 0)) {
                
            }else{
                localStringBuffer.append("/**********琛ㄥ悕涓嶈兘涓虹┖********/");
                return localStringBuffer;
            }
            if ((tableCol != null) && (tableCol.trim().length() > 0)) {
                
            }else{
                localStringBuffer.append("/**********鍒椾笉鑳戒负*鎴栬?呯┖********/");
                return localStringBuffer;
            }
            if ((tableCond != null) && (tableCond.trim().length() > 0)) {
                
            }else{
                tableCond="";
            }
    
            String [] tableColArr=tableCol.split(",");
            try {
                tableEx=new TableEx(tableCol, tableNameToSelect, tableCond+" order by S_ID desc limit 1");
                for(int i =0,j=tableEx.getRecordCount();i<j;i++){
                    record=tableEx.getRecord(i);
                    for(int z=0,m=tableColArr.length;z<m;z++){
                        localStringBuffer.append(getBillDataToString(record,tableColArr[z]));
                        localStringBuffer.append("`");
                    }
                }
            }catch (Exception e) {
                localStringBuffer.append(e.toString());
            } finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            return localStringBuffer;
          }
    
      public StringBuffer invoke1() {
        return new StringBuffer("invoke1");
      }
    
      public StringBuffer TheGateway() {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("<table id='flowgate' width='100%' class='table1'><col width='30%'/><col width='10%'/><col /><tr><th class='td1'>鑺傜偣鍚嶇О </th><th class='td1'>榛樿 </th><th class='td1'>鏉′欢璁剧疆 | 鏄惁鎵嬪姩閫夋嫨鑺傜偣<input id='SFSDXZJD' type='checkbox' style='vertical-align: middle;' onclick='morenxuanze(this)' /> </th></tr");
        localStringBuffer.append("</table>");
        localStringBuffer.append("<table class='table1' cellpadding='0' cellspacing='0' width='100%' style='display:black;position:fixed; bottom:0;'><tbody><tr><td colspan='2' align='right' class='bttformoparea'><table><tbody><tr><td><a id='submitToObj' class='button green'><span><div class='bttn_panel' style='background-image:url(images/eve/qd.png);'>纭畾</div></span></a></td><td width='50px'> </td></tr></tbody></table></td></tr></tbody></table>");
        localStringBuffer.append("<input type=\"hidden\" name=\"S_CONDITION\" id=\"S_CONDITION\" value=\"\" />");
        localStringBuffer.append("<input type=\"hidden\" name=\"S_AUDIT_DEF\" id=\"S_AUDIT_DEF\" value=\"\" />");
        localStringBuffer.append("<input type=\"hidden\" name=\"S_AUDIT_SEL\" id=\"S_AUDIT_SEL\" value=\"\" />");
        return localStringBuffer;
      }
        public StringBuffer ChildField() {
            TableEx tableEx = null;
            Record record = null;
            String strFormid = this.request.getParameter("formid");
            StringBuffer localStringBuffer = new StringBuffer();
            String fileId="";
            localStringBuffer.append("<script>");
            localStringBuffer.append("var flowId=[");
            localStringBuffer.append("[\"\",\"\"],");
            try {
                tableEx=new TableEx("SPAGETYPE,SFIELDCODE", "t_sys_pagemsg", "SPAGECODE='"+strFormid+"'");
                int iRecordCount = tableEx.getRecordCount();
                for (int i = 0; i < iRecordCount; i++) {
                    record = tableEx.getRecord(i);
                    String SPAGETYPE=record.getFieldByName("SPAGETYPE").value.toString();
                    String SFIELDCODE = record.getFieldByName("SFIELDCODE").value.toString();
                    if("9".equals(SPAGETYPE)){
                        fileId = SFIELDCODE;
                    }else{
                        fileId = strFormid;
                    }
                    String[][] arrayOfString = new Pub().getPageField(fileId);
                    int fieldLength=arrayOfString[0].length;
                    for (int z = 0; z < fieldLength; z++) {
                      localStringBuffer.append("[\"");
                      localStringBuffer.append(arrayOfString[0][z].replace("$", "\\."));
                      localStringBuffer.append("\",\"");
                      localStringBuffer.append(arrayOfString[1][z]);
                      localStringBuffer.append("\"],");
                    }
                }
            }catch (Exception e) {
                localStringBuffer.append(e.toString());
            } finally {
                localStringBuffer.append("];");
                localStringBuffer.append("</script>");
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            return localStringBuffer;
        }
       public StringBuffer TheGateway2() {
        StringBuffer localStringBuffer = new StringBuffer();
        String str = this.request.getParameter("formid");
        TableEx tableEx = null;
        Record record = null;
       
        localStringBuffer.append("<table id='ConditionsTable' class='table1' >");
        localStringBuffer.append("<col width='10%' />");
        localStringBuffer.append("<col width='20%' />");
        localStringBuffer.append("<col width='20%' />");
        localStringBuffer.append("<col width='15%' />");
        localStringBuffer.append("<col width='25%' />");
        localStringBuffer.append("<col width='5%' />");
        localStringBuffer.append("<tr>");
        localStringBuffer.append("<td class='td1'></td>");
        localStringBuffer.append("<td class='td1'>鍒?</td>");
        localStringBuffer.append("<td class='td1'>鏉′欢</td>");
        localStringBuffer.append("<td class='td1'>鏉′欢</td>");
        localStringBuffer.append("<td class='td1'>鎷兼帴</td>");
        localStringBuffer.append("<td class='td1'></td>");
        localStringBuffer.append("</tr>");
        localStringBuffer.append("</table>");
    
        localStringBuffer.append("<table class='table1' cellpadding='0' cellspacing='0' width='100%' style='display:black'><tbody><tr><td colspan='2' align='right' class='bttformoparea'><table><tbody><tr><td class='td1'><a onclick='save();' class='button green'><span><div class='bttn_panel' style='background-image:url(images/eve/qd.png);'>纭畾</div></span></a></td><td width='50px'> </td></tr></tbody></table></td></tr></tbody></table>");
    
    try {
        tableEx=new TableEx("SPAGETYPE,SFIELDCODE", "t_sys_pagemsg", "SPAGECODE='"+str+"'");
        int iRecordCount = tableEx.getRecordCount();
        for (int ii = 0; ii < iRecordCount; ii++) {
            record = tableEx.getRecord(ii);
            String SPAGETYPE=record.getFieldByName("SPAGETYPE").value.toString();
            String SFIELDCODE = record.getFieldByName("SFIELDCODE").value.toString();
            String fileId="";
            if("9".equals(SPAGETYPE)){
                fileId = SFIELDCODE;
            }else{
                fileId = str;
            }
            String[][] arrayOfString = new Pub().getPageField(fileId);
            int i = arrayOfString[0].length;
            localStringBuffer.append("<script>");
            localStringBuffer.append("var flowId=[");
            localStringBuffer.append("[\"\",\"\"],");
            for (int j = 0; j < i; j++) {
              localStringBuffer.append("[\"");
              localStringBuffer.append(arrayOfString[0][j].replace("$", "\\."));
              localStringBuffer.append("\",\"");
              localStringBuffer.append(arrayOfString[1][j]);
              localStringBuffer.append("\"],");
            }
            localStringBuffer.append("];");
            localStringBuffer.append("</script>");
        }
        // int i = arrayOfString[0].length;
        // localStringBuffer.append("<script>");
        // localStringBuffer.append("var flowId=[");
        // localStringBuffer.append("[\"\",\"\"],");
        // for (int j = 0; j < i; j++) {
        //   localStringBuffer.append("[\"");
        //   localStringBuffer.append(arrayOfString[0][j].replace("$", "\\."));
        //   localStringBuffer.append("\",\"");
        //   localStringBuffer.append(arrayOfString[1][j]);
        //   localStringBuffer.append("\"],");
        // }
        // localStringBuffer.append("];");
        // localStringBuffer.append("</script>");
    } catch (Exception e) {
        localStringBuffer.append(e.toString());
    } finally {
        if (tableEx != null) {
            tableEx.close();
        }
    }
        return localStringBuffer;
      }
        public StringBuffer Lbrz(){
            TableEx tableEx = null;
            Record record = null;
            StringBuffer localStringBuffer = new StringBuffer();
            String tableNameToSelect= this.request.getParameter("tableNameToSelect");
            String tableCol= this.request.getParameter("tableCol");
            String bmid=this.request.getParameter("bmid");
            try {
                tableEx=new TableEx(tableCol, tableNameToSelect, " S_ZZ='"+bmid+"' S_BCLB='JBCG' order by S_ID desc limit 1");
                for(int i =0,j=tableEx.getRecordCount();i<j;i++){
                    record=tableEx.getRecord(i);
                    localStringBuffer.append(getBillDataToString(record,tableCol));
                }
            }catch (Exception e) {
                localStringBuffer.append(e.toString());
            } finally {
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            return localStringBuffer;
        }
    
        public StringBuffer tempSelectfs(String _strSPAGECODE, String _strSplit) {
            StringBuffer returnStringBuffer = new StringBuffer();
            String[][] arrayOfString = new Pub().getPageField(_strSPAGECODE);
            String str2 = "";
            int iArray = arrayOfString[0].length;
            returnStringBuffer.append(_strSPAGECODE);
            returnStringBuffer.append("\\$");
            returnStringBuffer.append(new Pub().getPageName(_strSPAGECODE));
            returnStringBuffer.append("\\$");
            returnStringBuffer.append("true");
            returnStringBuffer.append("\\$");
            for (int z = 0; z < iArray; z++) {//arrayOfString[1][j]涓枃 //arrayOfString[0][j]浠ｇ⒓  琛?$鍒?
                if("$".equals(arrayOfString[0][z].substring(0,1))){
                    continue;
                }
                returnStringBuffer.append(str2);
                returnStringBuffer.append(arrayOfString[1][z]);
                returnStringBuffer.append(",");
                returnStringBuffer.append(arrayOfString[0][z].replaceAll("\\$", "\\."));
                returnStringBuffer.append(",true,false,false,");
                str2 = _strSplit;
            }
            return returnStringBuffer;
        }
      public StringBuffer TheSelectTable() {
            TableEx tableEx = null;
            Record record = null;
            StringBuffer localStringBuffer = new StringBuffer();
            StringBuffer returnValue = new StringBuffer();
            String[][] arrayOfString = null;
            localStringBuffer.append("<script>");
            localStringBuffer.append("var TABLECONTROL=\"");
            try {
                String str1 = this.request.getParameter("formid");
    
                tableEx=new TableEx("SPAGECODE,SPAGETYPE,SFIELDCODE,SFIELDNAME", "t_sys_pagemsg", "SPAGECODE='"+str1+"'");
                if ((str1 != null) && (str1.trim().length() > 0)) {
                    int iRecordCount = tableEx.getRecordCount();
         
                    for (int i = 0; i < iRecordCount; i++) {
                        record = tableEx.getRecord(i);
                        String SPAGETYPE=record.getFieldByName("SPAGETYPE").value.toString();
                        String SFIELDCODE = record.getFieldByName("SFIELDCODE").value.toString();
                        if("9".equals(SPAGETYPE)){
                            returnValue.append("<table id='"+SFIELDCODE+"#tableCol1' class='tableCol' cellpadding='0' cellspacing='0' width='50%' style=\"float: left;table-layout:fixed;display:none;\"><col width='' /><col width='9%' /><col width='9%' /><col width='9%' /><col width='25%' /><tr class='tr1'><td class='inputth1'>瀛楁鍚嶇О </td><td class='inputth1' style='display:none;'>瀛楁浠ｇ爜 </td><td class='inputth1'>鏌ョ湅 </td><td class='inputth1'>缂栬緫 </td><td class='inputth1'>蹇呭～ </td><td class='inputth1'>瑙﹀彂浜嬪姟</td></tr>");
                            returnValue.append("</table>");
                            returnValue.append("<table id='"+SFIELDCODE+"#tableCol2' class='tableCol' cellpadding='0' cellspacing='0' width='50%' style=\"float: left;table-layout:fixed;;display:none;\"><col width='' /><col width='9%' /><col width='9%' /><col width='9%' /><col width='25%' /><tr class='tr1'><td class='inputth1'>瀛楁鍚嶇О </td><td class='inputth1' style='display:none;'>瀛楁浠ｇ爜 </td><td class='inputth1'>鏌ョ湅 </td><td class='inputth1'>缂栬緫 </td><td class='inputth1'>蹇呭～ </td><td class='inputth1'>瑙﹀彂浜嬪姟</td></tr>");
                            returnValue.append("</table>");
                            localStringBuffer.append(tempSelectfs(SFIELDCODE,"|"));
                            String sonIdArr=record.getFieldByName("SFIELDNAME").value.toString();
                            String [] sonArr = sonIdArr.split(",");
                            int sonArrLength=sonArr.length;
                            for(int j = 0 ; j < sonArrLength ; j++){
                                if(sonArr[j].indexOf("URL")==0){
                                    continue;
                                }
                                localStringBuffer.append("`");
                                returnValue.append("<table id='"+sonArr[j]+"#tableCol1' class='tableCol' cellpadding='0' cellspacing='0' width='50%' style=\"float: left;table-layout:fixed;display:none;\"><col width='' /><col width='9%' /><col width='9%' /><col width='9%' /><col width='25%' /><tr class='tr1'><td class='inputth1'>瀛楁鍚嶇О </td><td class='inputth1' style='display:none;'>瀛楁浠ｇ爜 </td><td class='inputth1'>鏌ョ湅 </td><td class='inputth1'>缂栬緫 </td><td class='inputth1'>蹇呭～ </td><td class='inputth1'>瑙﹀彂浜嬪姟</td></tr>");
                                returnValue.append("</table>");
                                returnValue.append("<table id='"+sonArr[j]+"#tableCol2' class='tableCol' cellpadding='0' cellspacing='0' width='50%' style=\"float: left;table-layout:fixed;;display:none;\"><col width='' /><col width='9%' /><col width='9%' /><col width='9%' /><col width='25%' /><tr class='tr1'><td class='inputth1'>瀛楁鍚嶇О </td><td class='inputth1' style='display:none;'>瀛楁浠ｇ爜 </td><td class='inputth1'>鏌ョ湅 </td><td class='inputth1'>缂栬緫 </td><td class='inputth1'>蹇呭～ </td><td class='inputth1'>瑙﹀彂浜嬪姟</td></tr>");
                                returnValue.append("</table>");
                                localStringBuffer.append(tempSelectfs(sonArr[j],"|"));
                            }
                        }else{
                            returnValue.append("<table id='"+str1+"#tableCol1' class='tableCol' cellpadding='0' cellspacing='0' width='50%' style=\"float: left;table-layout:fixed;display:none;\"><col width='' /><col width='9%' /><col width='9%' /><col width='9%' /><col width='25%' /><tr class='tr1'><td class='inputth1'>瀛楁鍚嶇О </td><td class='inputth1' style='display:none;'>瀛楁浠ｇ爜 </td><td class='inputth1'>鏌ョ湅 </td><td class='inputth1'>缂栬緫 </td><td class='inputth1'>蹇呭～ </td><td class='inputth1'>瑙﹀彂浜嬪姟</td></tr>");
                            returnValue.append("</table>");
                            returnValue.append("<table id='"+str1+"#tableCol2' class='tableCol' cellpadding='0' cellspacing='0' width='50%' style=\"float: left;table-layout:fixed;;display:none;\"><col width='' /><col width='9%' /><col width='9%' /><col width='9%' /><col width='25%' /><tr class='tr1'><td class='inputth1'>瀛楁鍚嶇О </td><td class='inputth1' style='display:none;'>瀛楁浠ｇ爜 </td><td class='inputth1'>鏌ョ湅 </td><td class='inputth1'>缂栬緫 </td><td class='inputth1'>蹇呭～ </td><td class='inputth1'>瑙﹀彂浜嬪姟</td></tr>");
                            returnValue.append("</table>");
                            localStringBuffer.append(tempSelectfs(str1,"|"));
                        }
                    }
                }
            } catch (Exception e) {
      
            } finally {
                localStringBuffer.append("\";");
                localStringBuffer.append("</script>");
                if (tableEx != null) {
                    tableEx.close();
                }
            }
            return returnValue.append(localStringBuffer);
        }
    
      public void doFlow(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) {
        StringBuffer localStringBuffer = new StringBuffer();
        String str1 = paramHttpServletRequest.getParameter("NO_UPL_KC");
        String strDoRun = paramHttpServletRequest.getParameter("T_DQYZGZP$S_SYS_FLOWJS_DORUN");
         StringBuffer srReturn = new StringBuffer();
         TableEx ex =null;
        try {
          boolean bool = true;
    
          ProcessRunOperation localProcessRunOperation = new ProcessRunOperation();
    
          String str2 = paramHttpServletRequest.getParameter("NO_OPTYPE");
          if ("1".equals(str2)){
              ex = localProcessRunOperation.queryFlowRun(paramHttpServletRequest.getParameter("NO_sys_flow_id"),paramHttpServletRequest.getParameter("NO_sys_flow_Ver"),paramHttpServletRequest.getParameter("NO_sys_S_RUN_ID"));
              if(ex.getRecordCount()>0){
                    localProcessRunOperation.processSave(paramHttpServletRequest);
              }else{
                  bool = localProcessRunOperation.processStart(paramHttpServletRequest, localStringBuffer,"","").booleanValue();
              }
          }else {
                bool = localProcessRunOperation.processStart(paramHttpServletRequest, localStringBuffer,"","").booleanValue();
          }
      
         
          if(strDoRun!=null&&(strDoRun.trim().length() > 0)){
              String[] strDoRunArray = strDoRun.split("\\$");
              srReturn.append("<script type='text/javascript' src='js/check.js'></script>");
              srReturn.append("<script type='text/javascript' src='res/js/flowrunjs.js'></script>");
              srReturn.append("<script>");
              srReturn.append(strDoRunArray[0]).append("(");
              int iLeng=strDoRunArray.length;
              for(int i=1;i<iLeng;i++){
                  String strTemp = strDoRunArray[i];
                   strTemp = localProcessRunOperation.replaceRequestVal(paramHttpServletRequest,strTemp);/**session*/
                   if(strTemp.equals(strDoRunArray[i])){
                       strTemp = localProcessRunOperation.getRequestParam(paramHttpServletRequest,strTemp);/**request*/
                   }
                   srReturn.append(strTemp).append(i<iLeng-1?",":"");
              }
              srReturn.append(");");
              srReturn.append("</script>");
          }else{
               srReturn.append("<script>");
               if(str1==null||"".equals(str1)){
                
                    srReturn.append("var src = parent.document.getElementById(\"sys_bd\");src.src=src.src; ");
                   
               }else{
                srReturn.append("try{parent.document.getElementById('"+str1+"').objOpen.location.reload();}catch(err){parent.location.reload();}");
                srReturn.append("parent.closeWinById('"+str1+"');");
               }
                srReturn.append("</script>");
                       
            
          }
            paramHttpServletResponse.getWriter().print(srReturn.toString());
            //paramHttpServletResponse.getWriter().print(bool + "----" + localStringBuffer.toString());
          /**paramHttpServletResponse.getWriter().print(bool + "----" + localStringBuffer.toString());*/
        } catch (Exception e) {
            if(ex!=null){ex.close();}
            e.printStackTrace();
        }finally{
             if(ex!=null){ex.close();}
        }
        
     
      }
    
      public StringBuffer doFlowRun()
      {
        StringBuffer localStringBuffer = new StringBuffer();
        boolean bool = false;
        try
        {
          ProcessRunOperation localProcessRunOperation = new ProcessRunOperation();
          bool = localProcessRunOperation.processRun(this.request, localStringBuffer).booleanValue();
          localStringBuffer.append("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
        localStringBuffer = new StringBuffer();
        return localStringBuffer.append("true");
      // return localStringBuffer;
      }
    
      public StringBuffer doFlowChoieNode(String paramString)
      {
        StringBuffer localStringBuffer = new StringBuffer();
        try {
          String str = "";
          ProcessRunOperation localProcessRunOperation=new ProcessRunOperation();
          HashMap hm=new HashMap(localProcessRunOperation.processAudAll(this.request));
          if ("back".equals(paramString)) {
    
            if(hm.get("processAuditSelectNode")!=null){
                str=hm.get("processAuditSelectNode").toString();
            }
            
          }
          else if ("run".equals(paramString)) {
            if(hm.get("processNodeAudit")!=null){
                str=hm.get("processNodeAudit").toString();
            }
      
          } else if ("audio".equals(paramString)) {
            if(hm.get("processAudCustomNodeIds")!=null){
                str=hm.get("processAudCustomNodeIds").toString();
            }
          }else if ("strFlowPj".equals(paramString)) {
            if(hm.get("strFlowPj")!=null){
                str=hm.get("strFlowPj").toString();
            }
           
          }else if ("auditFlag".equals(paramString)) {
            if(hm.get("auditFlag")!=null){
                str=hm.get("auditFlag").toString();
            }
           
          }else if ("nowNodeName".equals(paramString)) {
            if(hm.get("nowNodeName")!=null){
                str=hm.get("nowNodeName").toString();
            }
           
          }
          localStringBuffer.append(str);
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
        return localStringBuffer;
      }
    
    
      public StringBuffer delFlowRunData() {
        StringBuffer localStringBuffer = new StringBuffer();
        try
        {
          ProcessRunOperation localProcessRunOperation = new ProcessRunOperation();
          localProcessRunOperation.shanchutable(this.request.getParameter("flowid").toString());
        }
        catch (Exception localException) {
          localException.printStackTrace();
        }
        return localStringBuffer;
      }
      public String getBillDataToString(Record record, String str) {
            return record.getFieldByName(str).value.toString();
        }
      public StringBuffer dataBase() {
          StringBuffer sb = new StringBuffer();
          TableEx tableEx =null;
          Record record=null;
          try{
              tableEx = new TableEx("STABLENAME,STABLECODE","T_SYS_TABLE","");
              for(int i =0,j=tableEx.getRecordCount();i<j;i++){
                  record=tableEx.getRecord(i);
                  sb.append(getBillDataToString(record,"STABLENAME"));
                   sb.append(",");
                    sb.append(getBillDataToString(record,"STABLECODE"));
                     sb.append("\\|");
              }
              
          }catch(Exception e){
              sb.append(e.toString());
          }finally{
              if(tableEx!=null){
                  tableEx.close();
              }
          }
     
          return sb;
      }
      //------------------------------------------------------
      public StringBuffer createDataBase() {
            StringBuffer sql = new StringBuffer();
            String tableCode = request.getParameter("tableCode");
            String tableName = request.getParameter("tableName");
            tableName=com.yulongtao.util.EString.encoderStr(tableName,"utf-8");
            String[] tableCodes = tableCode.split(",");
            String[] tableNames = tableName.split(",");
            int tableLength = tableCodes.length;
            long timeL = System.currentTimeMillis();
            StringBuffer SFIELDS = new StringBuffer(); //鏄剧ず鐨勫瓧娈?   ,鍏堝叏涓鸿嫳鏂?  寮?澶存湁,
            StringBuffer SFIELDSQLS = new StringBuffer();// 鏌ヨ鐨勬墍鏈?   寮?澶存湁,
            StringBuffer SFIELDNAMES = new StringBuffer();//涓枃鍚嶇О  寮?澶存湁,
            String STABLES = "";//琛ㄤ唬鐮?
            String SCONNAME = "";//SCONNAME_MEANING = SCONNAME  涓鸿〃鍚?
            String SCONID = Long.toString(timeL); //娴佹按鍙?
            StringBuffer SSQL = new StringBuffer();//sql 璇彞   鎷兼帴 select    from   
            String SCONNAME_MEANING = "";
            TableEx tableEx = null;
            Record record = null;
           DBFactory dbf = new DBFactory();
            try {
                for (int i = 0; i < tableLength; i++) {
                    tableEx = new TableEx("SITEMCODE,SITEMNAME", "T_SYS_ITEM", "STYPECODE='" + tableCodes[i] + "'");
                    int iRecordCount = tableEx.getRecordCount();
                    STABLES = tableCodes[i];
                    SCONNAME =tableNames[i];
                    SCONNAME_MEANING = SCONNAME;
                    SCONID += "_" + i;
                    for (int j = 0; j < iRecordCount; j++) {
                        record = tableEx.getRecord(j);
                        String SITEMCODE = getBillDataToString(record, "SITEMCODE");//鑻辨枃浠ｇ爜
                        String SITEMNAME = getBillDataToString(record, "SITEMNAME");//涓枃鍚嶇О
                        SFIELDS.append(",");
                        SFIELDS.append(SITEMNAME);
                        SFIELDSQLS.append(",");
                        SFIELDSQLS.append(SITEMCODE);
                        SFIELDSQLS.append(" '");
                        SFIELDSQLS.append(SITEMNAME);
                        SFIELDSQLS.append("'");
                        SFIELDNAMES.append(",");
                        SFIELDNAMES.append(SITEMNAME);
                    }
                    
                    SSQL.append("select ");
                    SSQL.append(SFIELDSQLS.deleteCharAt(0));
                    SSQL.append(" from ");
                    SSQL.append(STABLES);
                    
                    sql.append("insert into t_sys_dataset (SCONNAME,SCONID,SSQL,SCONNAME_MEANING,SFIELDS,SFIELDSQLS,SFIELDNAMES,STABLES,SNOTRANS,SELEMENTTYPE) values (");
                    sql.append("\"");
                    sql.append(SCONNAME);
                    sql.append("\",");
                    sql.append("\"");
                    sql.append(SCONID);
                    sql.append("\",");
                    sql.append("\"");
                    sql.append(SSQL);
                    sql.append("\",");
                    sql.append("\"");
                    sql.append(SCONNAME_MEANING);
                    sql.append("\",");
                    sql.append("\"");
                    sql.append(SFIELDS);
                    sql.append("\",");
                    sql.append("\",");
                    sql.append(SFIELDSQLS);
                    sql.append("\",");
                    
                    //sql.append("\",");
                    //sql.append(SFIELDSQLS);
                    //sql.append("\",");
                    
                    sql.append("\"");
                    sql.append(SFIELDNAMES);
                    sql.append("\",");
                    sql.append("\",");
                    sql.append(STABLES);
                    sql.append("\",");
                    sql.append("\"");
                    sql.append("888");
                    sql.append("\",");
                    sql.append("\"");
                    sql.append("002");
                    sql.append("\");");
                    
                }
            dbf.sqlExe(sql.toString(), true);
            } catch (Exception e) {
                sql.append(e.toString());
            } finally {
                if(tableEx!=null){
                    tableEx.close();
                }
                if(dbf!=null){
                    dbf.close();
                }
            }
            return sql;
        }
        
          public StringBuffer dataTb(){
            StringBuffer sr = new StringBuffer();
        	String strId = request.getParameter("S_ID");
        	try{
        	    String str = new DataSynchronous().getTabConfigInfo(strId,sr);
        	    sr.append(str);
        	     String str1 = request.getParameter("NO_UPL_KC");
        	}catch(Exception e){
        	    sr.append(e);
        	}
    		return sr;
        }
        public String getFlowStartNodeId(HttpServletRequest request, String _flowId, String _flowVersion) {
     
    		DBFactory dbFactory = new DBFactory();
    		TableEx tableEx = null;
    		Record record;
    		String S_AUDIT_ROLE = "";
    		String S_AUDIT_SQRY = "";
    		String S_AUDIT_SQRYATTR = "";
    		String I_NODE_ID = "";
    		String userRole = request.getSession().getAttribute("SYS_STRROLECODE").toString();// 用户角色
    		String userOrg = request.getSession().getAttribute("SYS_STRBRANCHID").toString(); // 用户组织
    		
    		String sql = "select I_NODE_ID,S_AUDIT_ROLE,S_AUDIT_SQRY,S_AUDIT_SQRYATTR from t_sys_flow_node where S_FLOW_ID='"
    				+ _flowId + "' and S_AUDIT_VERSION='" + _flowVersion + "' AND I_TYPE=3;";
    		try {
    			tableEx = dbFactory.query(sql);
    			int recordIndex = tableEx.getRecordCount();
    			for (int i = 0; i < recordIndex; i++) { // 多个开始节点进行判断
    				record = tableEx.getRecord(i);
    				S_AUDIT_ROLE = record.getFieldByName("S_AUDIT_ROLE").value.toString(); // 角色
    				S_AUDIT_SQRY = record.getFieldByName("S_AUDIT_SQRY").value.toString(); // 三权力条件
    				S_AUDIT_SQRYATTR = record.getFieldByName("S_AUDIT_SQRYATTR").value.toString();// 三权力条件
    				I_NODE_ID = record.getFieldByName("I_NODE_ID").value.toString();
    				if ("".equals(S_AUDIT_ROLE)&&!"".equals(S_AUDIT_SQRY)) { // 角色优先,其次为按钮
    					S_AUDIT_SQRYATTR = S_AUDIT_SQRYATTR.split(":")[1];
    					if(SepOfCertification(userRole,userOrg, S_AUDIT_SQRY, S_AUDIT_SQRYATTR)) {
    						return I_NODE_ID;
    					}
    				} else {
    					String[] roleArr = S_AUDIT_ROLE.split(",");
    					for (int j = 0; j < roleArr.length; j++) {
    						if (roleArr[j].equals(userRole)) {
    							return I_NODE_ID;
    						}
    					}
    				}
    
    			}
    			return tableEx.getRecord(0).getFieldByName("I_NODE_ID").value.toString();
    		} catch (Exception e) {
    			MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (tableEx != null) {
    				tableEx.close();
    			}
    			if(dbFactory!=null) {
    				dbFactory.close();
    			}
    		}
    
    		return I_NODE_ID;
    	}
    
    	public boolean SepOfCertification(String _strUserCode,String _strUserOrg, String _strSqCode, String _SqCond) {
    		DBFactory dbFactory = new DBFactory();
    		TableEx tableEx = null;
    		String sql = "select S_ATTR from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where T_JSYHZ.S_ID = '"+_strSqCode+"' and  S_ATTR = '"+_SqCond+"' and   '"+_strUserOrg+"' like concat(S_BMID,'%') and S_JSDM='"+_strUserCode+"';";
    		try {
    			tableEx = dbFactory.query(sql);
    			int recordIndex = tableEx.getRecordCount();
    			if(recordIndex>0) {
    				return true;
    			}
    		}catch (Exception e) {
    			MantraLog.fileCreateAndWrite(e);
    		}finally {
    			if(dbFactory!=null) {
    				dbFactory.close();
    			}
    			if(tableEx!=null) {
    				tableEx.close();
    			}
    		}
    
    		return false;
    	}
    }
