package com.timing.impcl;

import java.io.IOException;
import java.util.Scanner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
	/*
	 * 安全�?查及隐患排查治理
	 * 
	 * 
	 */
public class SafeCheck {
	MantraUtil mu = new MantraUtil();
	
	/*
	 * 安全�?查方�?
	 * 流程结束之后部门安全�?查及附表对应安全�?查方案新�?
	 * 
	 */
		public boolean checkToolBusinessDeal(String _planPk) {
		    
		    
		    if ("".equals(_planPk)) {
			        return false;
		        }
		    TableEx tableZhu = null;
		    String T_AQJCFA__S_ZZ="";
		    DBFactory dbf = new DBFactory();
		    long timeStamp = System.currentTimeMillis();//流水�?
		    
		    String zhu_sql="select T_AQJCFA.S_DJRQ T_AQJCFA__S_DJRQ,T_AQJCFA.S_DJH T_AQJCFA__S_DJH,T_AQJCFA.S_ZZ T_AQJCFA__S_ZZ,T_AQJCFA.S_ID T_AQJCFA__S_ID ,group_concat(T_LXJCFB.S_ID) zb_id,T_LXJCFB.S_ZRBMBM T_LXJCFB__S_ZRBMBM,T_LXJCFB.S_BMFZRBM T_LXJCFB__S_BMFZRBM from T_AQJCFA left join T_LXJCFB on T_AQJCFA.S_ID = T_LXJCFB.FID where T_AQJCFA.S_ID ='"+_planPk+"' group by T_LXJCFB.S_ZRBMBM";
		    try{
		        
		        tableZhu = dbf.query(zhu_sql);
                int iCount=tableZhu.getRecordCount();//查询主表外连接子表的条数
                
                
               for(int i=0;i<iCount;i++){
	            //主表新增
	            Record zhu_rd = tableZhu.getRecord(i);//每行的数据集�?
                String son_Fid=timeStamp+i+"";
                
                T_AQJCFA__S_ZZ = getBillDataToString(zhu_rd, "T_AQJCFA__S_ZZ");//主表组织
             	dbf.sqlExe("insert into T_BMAQJC (S_ID,S_AQJCFA_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_JCFABM,S_FAMC,S_CJRQ,S_JCFZBM,S_JCZTYQ,T_BZ,S_ZDR,S_ZDSJ,S_ZHXGR,S_ZHXGSJ,S_BMFZRBM)"
                           + " select '"+son_Fid+"' ,'"+_planPk+"',S_ZZ,'"+mu.getFlowVer("1516247158225",T_AQJCFA__S_ZZ)+"','"+mu.getShortUuid()+"',S_JCFABM,S_FAMC,S_CJRQ,'"+getBillDataToString(zhu_rd, "T_LXJCFB__S_ZRBMBM")+"',T_CJZTYQ,T_BZ,S_ZDR,S_ZDSJ,S_ZHXGR,S_ZHXGSJ,'"+getBillDataToString(zhu_rd, "T_LXJCFB__S_BMFZRBM")+"' from T_AQJCFA where T_AQJCFA.S_ID='" +_planPk+"'",false);
             
             	    dbf.sqlExe("insert into T_BMAQJCZB (S_ID,S_FID,S_CJXM,S_BZYQ,S_ZCZRBM,S_ZCZRBMBM) select S_ID, '"+son_Fid+"', S_JCXM,S_BZYQ,S_ZCZRBM,S_ZRBMBM from T_LXJCFB where T_LXJCFB.S_ID in("+getBillDataToString(zhu_rd, "zb_id")+")",false);
              
                  
                   mu.recordRel(T_AQJCFA__S_ZZ, "1516247158225",getBillDataToString(zhu_rd, "T_AQJCFA__S_ID"), "T_AQJCFA", "1516166904515", son_Fid,
						"T_BMAQJC");//������ϵ
                   
               }
		            
		    }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (tableZhu != null)
    				tableZhu.close();
    			if (dbf != null)
    				dbf.close();
    		}
		return false;
		}
        	public String getBillDataToString(Record record, String str) {
        		return record.getFieldByName(str).value.toString();
        	}	
	

			public boolean branchSafeCheck(String _planPk) {
		    
		    
		    if ("".equals(_planPk)) {
			        return false;
		        }
		    TableEx tableZi = null;
		    String T_BMAQJC__S_ZZ ="";
		    DBFactory dbf = new DBFactory();
		    long timeStamp = System.currentTimeMillis();//流水�?
		    
		    String zi_sql="select T_BMAQJC.S_ID T_BMAQJC__S_ID,   T_BMAQJC.S_ZZ T_BMAQJC__S_ZZ,T_BMAQJCZB.S_ID T_BMAQJCZB__S_ID,T_BMAQJCZB.S_CJXM T_BMAQJCZB__S_CJXM,T_BMAQJCZB.S_BZYQ T_BMAQJCZB__S_BZYQ,T_BMAQJCZB.S_ZCZRBM T_BMAQJCZB__S_ZCZRBM,T_BMAQJCZB.S_ZCZZR T_BMAQJCZB__S_ZCZZR,T_BMAQJCZB.S_ZCZZRBM T_BMAQJCZB__S_ZCZZRBM"+
		                  " FROM T_BMAQJCZB left join T_BMAQJC  on T_BMAQJCZB.S_FID=T_BMAQJC.S_ID where T_BMAQJC.S_ID ='"+_planPk+"'";
		    
		    //查询出部门安全检查子表的S_ID,�?查项目S_CJXM，标准要求S_BZYQ，自查责任部门S_ZCZRBM，自查责任人S_ZCZZR，自查责任人编码S_ZCZZRBM
		    try{
		        
		        tableZi = dbf.query(zi_sql);
                int iCount=tableZi.getRecordCount();//查询得到的条�?
                
                
                
               for(int i=0;i<iCount;i++){
                   
                   String son_Fid=timeStamp+i+"";
	            //主表新增
	            Record zi_rd = tableZi.getRecord(i);//每行的数据集�?
	            //1、上�?-安全�?查方�?-表体更新T_LXJCFB   字段：自查责任人S_ZCZZR, S_ZCZZRBM 
                T_BMAQJC__S_ZZ = getBillDataToString(zi_rd, "T_BMAQJC__S_ZZ");
                dbf.sqlExe("update T_LXJCFB set S_ZCZZR='"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ZCZZR")+"',S_ZCZZRBM='"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ZCZZRBM")+"' where S_ID='"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ID")+"'",false);
                //2、下�?-责任人安全检查方�?-表体新增T_ZRRAQJCZB 字段：S_FID,S_CJXM,S_BZYQ,S_ZCZRBM,S_ZCZRR,S_ZCZRRBM
                dbf.sqlExe("insert into T_ZRRAQJCZB (S_ID ,S_FID,S_CJXM,S_BZYQ,S_ZCZRBM,S_ZCZRR,S_ZCZRRBM) select S_ID,'"+son_Fid+"',S_CJXM,S_BZYQ,S_ZCZRBM,S_ZCZZR,S_ZCZZRBM from T_BMAQJCZB where T_BMAQJCZB.S_ID='"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ID")+"'",false);
                //3、下�?-责任人安全检查方�?-表头新增T_ZRRAQJC 字段：S_ZZ,S_DJH,S_DJRQ,S_FAMC,S_CJRQ,S_ZCZRR,S_ZCZRRBM,T_CJZTYQ,S_BZ
                
                dbf.sqlExe("insert into T_ZRRAQJC (S_ID ,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_FAMC,S_CJRQ,S_ZCZRR,S_ZCZRRBM,T_CJZTYQ,S_BZ) select '"+son_Fid+"',S_ZZ,'"+mu.getFlowVer("1516166904515",T_BMAQJC__S_ZZ)+"','"+mu.getShortUuid()+"',S_FAMC,S_CJRQ,'"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ZCZZR") +"','"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ZCZZRBM") +"',S_JCZTYQ,T_BZ from T_BMAQJC left join  T_BMAQJCZB on  T_BMAQJC.S_ID =T_BMAQJCZB.S_FID where T_BMAQJCZB.S_ID='"+getBillDataToString(zi_rd, "T_BMAQJCZB__S_ID")+"'",false);
               
    
                   
                   
                   mu.recordRel(T_BMAQJC__S_ZZ, "1516166904515",getBillDataToString(zi_rd, "T_BMAQJC__S_ID"), "T_BMAQJC", "15175538437610", son_Fid,
						"T_ZRRAQJC");//������ϵ
                   
               }
                
                
              
		            
		    }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
		return false;
		}
		
		public boolean affirmSafeCheck(String _planPk) {
		    //责任人安全检�?
		    if ("".equals(_planPk)) {
			        return false;
		        }
		    TableEx tableZi = null;
		    String T_ZRRAQJC__S_ZZ = "";
		    DBFactory dbf = new DBFactory();
		    long timeStamp = System.currentTimeMillis();//流水�?
		    String zi_sql = "select T_ZRRAQJCZB.T_ZCQKJWT T_ZRRAQJCZB__T_ZCQKJWT,T_ZRRAQJCZB.S_WTLX T_ZRRAQJCZB__S_WTLX,T_ZRRAQJCZB.S_SFCZWT T_ZRRAQJCZB__S_SFCZWT,T_ZRRAQJCZB.S_ZCSJ T_ZRRAQJCZB__S_ZCSJ,T_ZRRAQJCZB.S_ZCZRR T_ZRRAQJCZB__S_ZCZRR,T_ZRRAQJCZB.S_ZCZRBM T_ZRRAQJCZB__S_ZCZRBM,T_ZRRAQJCZB.S_BZYQ T_ZRRAQJCZB__S_BZYQ,T_ZRRAQJCZB.S_CJXM T_ZRRAQJCZB__S_CJXM,T_ZRRAQJCZB.S_FID T_ZRRAQJCZB__S_FID,T_ZRRAQJCZB.S_ID T_ZRRAQJCZB__S_ID	,T_ZRRAQJC.S_ZZ T_ZRRAQJC__S_ZZ from T_ZRRAQJCZB left join T_ZRRAQJC on T_ZRRAQJCZB.S_FID=T_ZRRAQJC.S_ID where T_ZRRAQJC.S_ID='"+_planPk+"' and T_ZRRAQJCZB.S_SFCZWT='true'";
		    
		    
		    try{
		         tableZi = dbf.query(zi_sql);
                int iCount=tableZi.getRecordCount();//查询得到的条�?
                
                
                
               for(int i=0;i<iCount;i++){
	            //主表新增
	            
	            Record zi_rd = tableZi.getRecord(i);//每行的数据集�?
	            T_ZRRAQJC__S_ZZ = getBillDataToString(zi_rd, "T_ZRRAQJC__S_ZZ");
	            
	            dbf.sqlExe("update T_LXJCFB set S_ZCSJ='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_ZCSJ")+"',S_SFCZWT='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_SFCZWT")+"',S_WTLX='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_WTLX")+"',T_ZCQKJWT='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__T_ZCQKJWT")+"' where S_ID='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_ID")+"'",false);
	            dbf.sqlExe("update T_BMAQJCZB  set S_ZCSJ='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_ZCSJ")+"',S_SFCZWT='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_SFCZWT")+"',S_WTLX='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_WTLX")+"',T_ZCQKJWT='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__T_ZCQKJWT")+"' where S_ID='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_ID")+"'",false);
	            dbf.sqlExe("insert into T_AQJCWTQRZ  (S_ID ,S_FID,S_JCXM,S_BZYQ,S_ZCZRRBM,S_ZCZRR,S_ZCSJ,S_WTLX,T_ZCQKJWT) select S_ID,'"+timeStamp+0+"',S_CJXM,S_BZYQ,S_ZCZRBM,S_ZCZRR,S_ZCSJ,S_WTLX,T_ZCQKJWT from T_ZRRAQJCZB where T_ZRRAQJCZB.S_ID='"+getBillDataToString(zi_rd, "T_ZRRAQJCZB__S_ID")+"'",false);
               }
               
               if(iCount!=0){
                   dbf.sqlExe("insert into T_AQJCWTQR (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_AQJCFABM,S_FAMC,S_CJRQ,S_CJFZBM,S_CJZFZR,T_JCZTYQ) select '"+timeStamp+0+"',S_ZZ,'"+mu.getFlowVer("15175538437610",T_ZRRAQJC__S_ZZ)+"','"+mu.getShortUuid()+"',S_DJH,S_FAMC,S_DJRQ,S_CJFZBM,S_CJZFZR,T_CJZTYQ from T_AQJCFA where S_ID =(select T_LXJCFB.FID from T_LXJCFB where T_LXJCFB.S_ID ='"+getBillDataToString(tableZi.getRecord(0), "T_ZRRAQJCZB__S_ID")+"') ",false);
		   
		        mu.recordRel(T_ZRRAQJC__S_ZZ, "15175538437610",_planPk, "T_ZRRAQJC", "1516587886146", timeStamp+""+0,
						"T_AQJCWTQR");//������ϵ
               }
		         
						
						
						
		    }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
		    
		    
		    	return false;
		}
		
		public boolean modifySafeCheck(String _planPk) {
		    //安全�?查问题确�?
		    if ("".equals(_planPk)) {
			        return false;
		        }
		    TableEx tableZi = null;
		    String T_AQJCWTQR__S_ZZ = "";
		    String T_AQJCWTQRZ__S_WTLX="";
		    DBFactory dbf = new DBFactory();
		    long timeStamp = System.currentTimeMillis();//流水�?
		    String zi_sql = "select T_AQJCWTQR.S_ZZ T_AQJCWTQR__S_ZZ, T_AQJCWTQRZ.S_WTLX T_AQJCWTQRZ__S_WTLX,T_AQJCWTQRZ.T_ZCQKJWT T_AQJCWTQRZ__T_ZCQKJWT,T_AQJCWTQRZ.S_ID T_AQJCWTQRZ__S_ID from T_AQJCWTQRZ left join T_AQJCWTQR on T_AQJCWTQRZ.S_FID=T_AQJCWTQR.S_ID where T_AQJCWTQR.S_ID='"+_planPk+"'";
		    
		      try{
		         tableZi = dbf.query(zi_sql);
                int iCount=tableZi.getRecordCount();//查询得到的条�?
                
                
                
               for(int i=0;i<iCount;i++){
	            //主表新增
	            
	            Record zi_rd = tableZi.getRecord(i);//每行的数据集�?
	            T_AQJCWTQR__S_ZZ = getBillDataToString(zi_rd, "T_AQJCWTQR__S_ZZ");
	            T_AQJCWTQRZ__S_WTLX=getBillDataToString(zi_rd,"T_AQJCWTQRZ__S_WTLX");
	            dbf.sqlExe("update T_LXJCFB set S_WTLX='"+getBillDataToString(zi_rd, "T_AQJCWTQRZ__S_WTLX")+"' ,T_ZCQKJWT='"+getBillDataToString(zi_rd, "T_AQJCWTQRZ__T_ZCQKJWT")+"' where T_LXJCFB.S_ID='"+getBillDataToString(zi_rd, "T_AQJCWTQRZ__S_ID")+"'",false);
	            
	            if(T_AQJCWTQRZ__S_WTLX.equals("WT")){//判断问题类型为问�?---�?查问题整改表新增
	                dbf.sqlExe("insert into T_CJWTZG (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_CJFABM,S_FAMC,S_CJZRBM,S_CJZFZR,T_CJZTYQ,T_JCXM,S_BZYQ,S_ZCZRBM,S_ZCZRR,S_ZCSJ,S_WTLX,T_ZCQKJWT,S_WTQRQK) select T_AQJCWTQRZ.S_ID,T_AQJCWTQR.S_ZZ, '"+mu.getFlowVer("1516587886146",T_AQJCWTQR__S_ZZ)+"','"+mu.getShortUuid()+"',T_AQJCWTQR.S_AQJCFABM,T_AQJCWTQR.S_FAMC,T_AQJCWTQR.S_CJFZBM,T_AQJCWTQR.S_CJZFZR,T_AQJCWTQR.T_JCZTYQ,T_AQJCWTQRZ.S_JCXM,T_AQJCWTQRZ.S_BZYQ,T_AQJCWTQRZ.S_ZCZRRBM,T_AQJCWTQRZ.S_ZCZRR,T_AQJCWTQRZ.S_ZCSJ,T_AQJCWTQRZ.S_WTLX,T_AQJCWTQRZ.T_ZCQKJWT,T_AQJCWTQRZ.S_WTQRQK from T_AQJCWTQRZ left join T_AQJCWTQR on T_AQJCWTQRZ.S_FID=T_AQJCWTQR.S_ID where T_AQJCWTQR.S_ID='"+_planPk+"'",false);
	            
	             mu.recordRel(T_AQJCWTQR__S_ZZ, "1516587886146",_planPk, "T_AQJCWTQR", "1516602563575", getBillDataToString(zi_rd, "T_AQJCWTQRZ__S_ID"),
						"T_CJWTZG");//������ϵ
	                
	            }else if(T_AQJCWTQRZ__S_WTLX.equals("YH")){//判断问题类型为隐�?--隐患等级评估新增
	                dbf.sqlExe("insert into T_YHDJPG (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_JCFABM,S_JCRQ,T_ZCQKJWT,S_WTQRQK,S_BZYQ,S_LYJCFA,S_ZCSJ,S_ZCZRR,S_JCXM) select T_AQJCWTQRZ.S_ID,T_AQJCWTQR.S_ZZ, '"+mu.getFlowVer("1516587886146",T_AQJCWTQR__S_ZZ)+"','"+mu.getShortUuid()+"', T_AQJCWTQR.S_AQJCFABM,T_AQJCWTQR.S_CJRQ,T_AQJCWTQRZ.T_ZCQKJWT,T_AQJCWTQRZ.S_WTQRQK,T_AQJCWTQRZ.S_BZYQ,T_AQJCWTQR.S_FAMC,T_AQJCWTQRZ.S_ZCSJ,T_AQJCWTQRZ.S_ZCZRR,T_AQJCWTQRZ.S_JCXM  from T_AQJCWTQRZ left join T_AQJCWTQR on T_AQJCWTQRZ.S_FID=T_AQJCWTQR.S_ID where T_AQJCWTQR.S_ID='"+_planPk+"'",false);
	            
	            mu.recordRel(T_AQJCWTQR__S_ZZ, "1516587886146",_planPk, "T_AQJCWTQR", "1516606174518", getBillDataToString(zi_rd, "T_AQJCWTQRZ__S_ID"),
						"T_YHDJPG");//������ϵ
	                
	            }
	           
	            
               }
	            
               }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
		    
		       	return false;
		}
		
	    public boolean alterSafeCheck(String _planPk) {
			    //安全�?查问题整�?
			     if ("".equals(_planPk)) {
			        return false;
		        }
	
		    DBFactory dbf = new DBFactory();
		     try{
		         
		         dbf.sqlExe("update T_LXJCFB set S_WTZGD=(select T_CJWTZG.S_DJH from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') , S_ZGZZR=(select T_CJWTZG.S_ZGZZR from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') , S_ZGYSR=(select T_CJWTZG.S_ZGYSR from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') where T_LXJCFB.S_ID= '"+_planPk+"'",false);
		         
		         dbf.sqlExe("update T_BMAQJCZB set S_WTZGD=(select T_CJWTZG.S_DJH from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') where T_BMAQJCZB.S_ID= '"+_planPk+"'" ,false);
		         
		         dbf.sqlExe("update T_ZRRAQJCZB set S_WTZGD=(select T_CJWTZG.S_DJH from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') where T_ZRRAQJCZB.S_ID= '"+_planPk+"'",false);
		         
		         dbf.sqlExe("update T_AQJCWTQRZ set S_WTZGD=(select T_CJWTZG.S_DJH from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"'), S_ZGZZR=(select T_CJWTZG.S_ZGZZR from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') , S_ZGYSR=(select T_CJWTZG.S_ZGYSR from T_CJWTZG where T_CJWTZG.S_ID='"+_planPk+"') where T_AQJCWTQRZ.S_ID= '"+_planPk+"'",false);
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (dbf != null)
    				dbf.close();
    		}
			return false;
		}
		
    public boolean ratHazard(String _planPk) {
	    //隐患等级评估
	     if ("".equals(_planPk)) {
	        return false;
	    }
	    
	    DBFactory dbf = new DBFactory();
	     TableEx tableZi = null;
	     long timeStamp = System.currentTimeMillis();
	     String T_YHDJPG__S_ZZ="";
	     String sql = "select T_YHDJPG.S_ZZ T_YHDJPG__S_ZZ from T_YHDJPG where S_ID = '"+_planPk+"'";
	     try{
	    	 tableZi = dbf.query(sql);
	    	 T_YHDJPG__S_ZZ = getBillDataToString(tableZi.getRecord(0), "T_YHDJPG__S_ZZ");
	         dbf.sqlExe("insert into T_YHZL (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_YHMC,T_YHXZ,T_YHCSDYY,S_YHDJ,S_YHPGSJ,S_YHLB,T_YHWHCD,S_QXBH,S_QXZT,S_JZ,S_QXLB,S_SBMC,S_SBBM,S_SSZY,S_XQDW,S_FXR,S_FXRBM,S_FXSJ,S_FXRSSBM_NAME,S_FXRSSBM,S_FXRSSBZ,S_GZPPZ,S_GZXXSM,S_GZFZR,S_GZFZRBM,S_QXYY,S_YHFXR,S_YHFXRBM,S_YHFXRSSBM_NAME,S_YHFXRSSBM,S_GSZNBM,S_PGFZR,S_PGFZRBM,S_PGSJ,S_PGLD,S_PGLDBM,S_PGLDSJ,S_PG_PGDJ,S_PG_PGFZR,S_PG_PGFZRBM,S_PG_PGSJ,S_PG_PGLD,S_PG_PGLDBM,S_PG_PGLDSJ,S_KNDZHG) select '" + 
	    	            timeStamp + "',S_ZZ,'" + mu.getFlowVer("1516606174518",T_YHDJPG__S_ZZ) + "','" + mu.getShortUuid() + 
	    	            "' ,S_YHMC,T_YHXZ,T_YHCSDYY,S_YHDJ,S_DJRQ,S_YHLB,S_YHWHCD,S_QXBH,S_QXZT,S_JZ,S_QXLB,S_SBMC,S_SBBM,S_SSZY,S_XQDW,S_FXR,S_FXRBM,S_FXSJ,S_FXRSSBM_NAME,S_FXRSSBM,S_FXRSSBZ,S_GZPPZ,S_GZXXSM,S_GZFZR,S_GZFZRBM,S_QXYY,S_YHFXR,S_YHFXRBM,S_YHFXRSSBM_NAME,S_YHFXRSSBM,S_GSZNBM,S_PGFZR,S_PGFZRBM,S_PGSJ,S_PGLD,S_PGLDBM,S_PGLDSJ,S_PG_PGDJ,S_PG_PGFZR,S_PG_PGFZRBM,S_PG_PGSJ,S_PG_PGLD,S_PG_PGLDBM,S_PG_PGLDSJ,S_KNDZHG from T_YHDJPG where S_ID = '" + _planPk + "'", false);
	         
	         mu.recordRel(T_YHDJPG__S_ZZ, "1516606174518", _planPk, "T_YHDJPG", "1516613463357", timeStamp+"","T_YHZL");
	     }catch (Exception e) {
	    	MantraLog.fileCreateAndWrite(e);
		} finally {
		    if (tableZi != null)
				tableZi.close();
			if (dbf != null)
				dbf.close();
		}
		return false;
    }
			
		 public boolean govern(String _planPk) {
			    //隐患治理
			     if ("".equals(_planPk)) {
			        return false;
		        }
	         TableEx tableZi = null;
		     long timeStamp = System.currentTimeMillis();//流水�?
		     String T_YHZL__S_ZZ="";
		     String sql = "select T_YHZL.S_ZZ T_YHZL__S_ZZ from T_YHZL where S_ID = '"+_planPk+"'";
		    DBFactory dbf = new DBFactory();
		     try{
		         tableZi = dbf.query(sql);
		          T_YHZL__S_ZZ = getBillDataToString(tableZi.getRecord(0), "T_YHZL__S_ZZ");
		         
		         dbf.sqlExe("update T_LXJCFB set S_YHZLD =(select S_DJH from T_YHZL where T_YHZL.S_ID='"+_planPk+"') ,S_YHDJ=(select S_YHDJ from T_YHZL where T_YHZL.S_ID='"+_planPk+"'),S_YHZGR=(select S_ZLZRR from T_YHZL where T_YHZL.S_ID='"+_planPk+"'),S_YHZLYSR=(select S_YSR from T_YHZL where T_YHZL.S_ID='"+_planPk+"') where T_LXJCFB.S_ID= '"+_planPk+"'",false);
		         dbf.sqlExe("update T_AQJCWTQRZ  set S_YHZLD =(select S_DJH from T_YHZL where T_YHZL.S_ID='"+_planPk+"') ,S_YHDJ=(select S_YHDJ from T_YHZL where T_YHZL.S_ID='"+_planPk+"'),S_YHZLYSR=(select S_ZLZRR from T_YHZL where T_YHZL.S_ID='"+_planPk+"'),S_YHYSR=(select S_YSR from T_YHZL where T_YHZL.S_ID='"+_planPk+"') where T_AQJCWTQRZ.S_ID= '"+_planPk+"'",false);
		         dbf.sqlExe("update T_BMAQJCZB  set S_YHZLD =(select S_DJH from T_YHZL where T_YHZL.S_ID='"+_planPk+"') ,S_YHDJ=(select S_YHDJ from T_YHZL where T_YHZL.S_ID='"+_planPk+"') where T_BMAQJCZB.S_ID= '"+_planPk+"'",false);
		         dbf.sqlExe("update T_ZRRAQJCZB set S_YHZLD =(select S_DJH from T_YHZL where T_YHZL.S_ID='"+_planPk+"') ,S_YHDJ=(select S_YHDJ from T_YHZL where T_YHZL.S_ID='"+_planPk+"') where T_ZRRAQJCZB.S_ID= '"+_planPk+"'",false);
		         dbf.sqlExe("insert into  T_YHBG (T_YHBG.S_ID,T_YHBG.S_ZZ,T_YHBG.SYS_FLOW_VER,T_YHBG.S_RUN_ID,T_YHBG.S_YHDJ,T_YHBG.S_YHLB,T_YHBG.S_YHMC,T_YHBG.S_YHPGSJ,T_YHBG.T_THCSDYY,T_YHBG.T_YHWHCD,T_YHBG.T_YHXZ,T_YHBG.T_YHZGJH,T_YHBG.T_YJYAJS,T_YHBG.T_FKCS,T_YHBG.T_ZGCS,T_YHBG.S_YHSSDW) select '"+timeStamp+"',T_YHZL.S_ZZ,'"+mu.getFlowVer("1517366491610",T_YHZL__S_ZZ)+"','"+mu.getShortUuid()+"' ,T_YHZL.S_YHDJ,T_YHZL.S_YHLB,T_YHZL.S_YHMC,T_YHZL.S_YHPGSJ,T_YHZL.T_YHCSDYY,T_YHZL.T_YHWHCD,T_YHZL.T_YHXZ,T_YHZL.T_ZGJH,T_YHZL.T_YJYAJS,T_YHZL.T_LSFFCS,T_YHZL.T_ZGCS,T_YHZL.S_ZZ from T_YHZL where T_YHZL.S_ID ='"+_planPk+"'",false);
		     
		          mu.recordRel(T_YHZL__S_ZZ, "1516613463357",_planPk, "T_YHZL", "1517366491610", timeStamp+"","T_YHBG");//������ϵ
		      
		         
		         
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    		     if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
			return false;
		}
		
		 public boolean outProject(String _planPk) {
			    //隐患治理
			     if ("".equals(_planPk)) {
			        return false;
		        }
	          DBFactory dbf = new DBFactory();
	          
	          try{
		         
		         
		         dbf.sqlExe("update T_JXZJHFJ set S_FLAGWW=1 where T_JXZJHFJ.S_ZJ=(select T_WWXM.S_FJID FROM T_WWXM WHERE  T_WWXM.S_ID='"+_planPk+"')",true);
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    			if (dbf != null)
    				dbf.close();
    		}
	         	return false;
		}
		
				
		 public boolean borrowTools(String _planPk) {
			    //工器具管理�?�用
			     if ("".equals(_planPk)) {
			        return false;
		        }
	          DBFactory dbf = new DBFactory();
	          
	          TableEx tableZi = null;
		     long timeStamp = System.currentTimeMillis();//流水�?
		     String T_JYGQJ__S_ZZ="";
		     String sql = "select T_JYGQJ.S_ZZ T_JYGQJ__S_ZZ from T_JYGQJ where S_ID = '"+_planPk+"'";
		     try{
		         
		         
		          tableZi = dbf.query(sql);
		          T_JYGQJ__S_ZZ = getBillDataToString(tableZi.getRecord(0), "T_JYGQJ__S_ZZ");
		         dbf.sqlExe("insert into T_GQJJYGH (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_GL,S_GLYBM,S_GQJBH,S_GQJMC,S_LYSL,S_LYBM,S_LYBMBM,S_LYR,S_LYRBM,S_LYRQ,S_YHRQ,S_LYYT) select S_ID,S_ZZ,'"+mu.getFlowVer("1515723789958",T_JYGQJ__S_ZZ)+"','"+mu.getShortUuid()+"',S_GLY,S_GLYBM,S_GQJBH,S_GQJMC,S_SL,S_LYBM,S_LYBMBM,S_LYR,S_LYRBM,S_LYRQ,S_YHRQ,T_LYYT from T_JYGQJ where S_ID = '"+_planPk+"'",true);
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    		    if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
	         	return false;
		}
		
		
		
		 public boolean proposal(String _planPk) {
			    //�����������̽�������������ϱ�
			     if ("".equals(_planPk)) {
			        return false;
		        }
	          DBFactory dbf = new DBFactory();
	          
	           TableEx tableZi = null;
		     long timeStamp = System.currentTimeMillis();//流水�?
		     String T_HLHJY__S_ID="";
		      String sql = "select T_HLHJY.S_ID T_HLHJY__S_ID from T_HLHJY where S_ID = '"+_planPk+"'";
	          
	          try{
	              
		          tableZi = dbf.query(sql);
		          T_HLHJY__S_ID = getBillDataToString(tableZi.getRecord(0), "T_HLHJY__S_ID");
		           dbf.sqlExe(" insert into T_SBZB (S_ZZ,S_TYPE,S_ID,SYS_FLOW_VER,S_RUN_ID) select S_ZZ,'HLHJY',S_ID,'"+mu.getFlowVer("1522719345443",T_HLHJY__S_ID)+"','"+mu.getShortUuid()+"' from T_HLHJY where S_ID = '"+_planPk+"'",true);
		        //��ͷ����
		         dbf.sqlExe("insert into T_HLHJYSB (S_FID,S_SBDW,S_SPR,S_SPSJ,S_SPYJ,S_JYBM,T_JYNR,T_SSJGFX,S_SSBM,S_SSR,S_SSSJ,S_SSJSSJ,S_JYLX,T_SM,T_BZ ) select S_ID,S_ZZ,S_SPR,S_SPSJ,T_SPYJ,S_JYBM,T_JYNR,T_SSJGFX,S_SSBM,S_SSR,S_KSSJSJ,S_SJSJSJ,S_JYLX,T_SM,T_BZ from T_HLHJY where T_HLHJY.S_ID='"+_planPk+"'",false);
		         //�������
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    		     if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
	         	return false;
		}
		
		public boolean innovate(String _planPk) {
			    //��������ͻ�������ϱ�
			     if ("".equals(_planPk)) {
			        return false;
		        }
	          DBFactory dbf = new DBFactory();
	          
	           TableEx tableZi = null;
		     long timeStamp = System.currentTimeMillis();//流水�?
		     String T_HLHJY__S_ID="";
		      String sql = "select T_HLHJY.S_ID T_HLHJY__S_ID from T_HLHJY where S_ID = '"+_planPk+"'";
	          
	          try{
	              
		          tableZi = dbf.query(sql);
		          T_HLHJY__S_ID = getBillDataToString(tableZi.getRecord(0), "T_HLHJY__S_ID");
		           dbf.sqlExe(" insert into T_SBZB (S_ZZ,S_TYPE,S_ID,SYS_FLOW_VER,S_RUN_ID) select S_ZZ,'JSSB',S_ID,'"+mu.getFlowVer("1522727526758",T_HLHJY__S_ID)+"','"+mu.getShortUuid()+"' from T_HLHJY where S_ID = '"+_planPk+"'",true);
		        //��ͷ����
		         dbf.sqlExe("insert into T_JSCXBS (S_FID,S_SBDW,S_SHBM,S_BTJR,S_GZGW,T_XCGGS,T_DWTJJY,S_TJR,S_XRZWZC,T_TJLY) select S_ID,S_ZZ,S_SSBM,S_BTJR,S_GZGW,T_XCGGS,T_DWTJJY,S_TJR,S_XRZWZC,T_TJLY from T_JSCXSB where S_ID = '"+_planPk+"'",true);
		         //�������
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    		     if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
	         	return false;
		}
		
		
			public boolean excellent(String _planPk) {
			    //���㼼�Ĵ��³ɹ��ϱ�
			     if ("".equals(_planPk)) {
			        return false;
		        }
	          DBFactory dbf = new DBFactory();
	          
	           TableEx tableZi = null;
		     long timeStamp = System.currentTimeMillis();//流水�?
		     String T_HLHJY__S_ID="";
		      String sql = "select T_HLHJY.S_ID T_HLHJY__S_ID from T_HLHJY where S_ID = '"+_planPk+"'";
	          
	          try{
	              
		          tableZi = dbf.query(sql);
		          T_HLHJY__S_ID = getBillDataToString(tableZi.getRecord(0), "T_HLHJY__S_ID");
		           dbf.sqlExe(" insert into T_SBZB (S_ZZ,S_TYPE,S_ID,SYS_FLOW_VER,S_RUN_ID) select S_ZZ,'YXSB',S_ID,'"+mu.getFlowVer("1522732741869",T_HLHJY__S_ID)+"','"+mu.getShortUuid()+"' from T_HLHJY where S_ID = '"+_planPk+"'",true);
		        //��ͷ����
		         dbf.sqlExe("insert into T_YXJGBS (S_FID,S_SBDW,S_SHBM,S_BTJR,S_GZGW,T_XCGGS,T_DWTJJY,S_XRZWZC,T_TJLY,S_TJR) select S_ID,S_ZZ,S_SSBM,S_BTJR,S_GZGW,T_XCGGS,T_DWTJJY,S_XRZWZC,T_TJLY,S_TJR from T_YXJGSB where S_ID= '"+_planPk+"'",true);
		         //�������
		         
		     }catch (Exception e) {
		    	MantraLog.fileCreateAndWrite(e);
    		} finally {
    		     if (tableZi != null)
    				tableZi.close();
    			if (dbf != null)
    				dbf.close();
    		}
	         	return false;
		}
}
