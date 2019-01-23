package com.bfkc.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class DataSynchronous {
	
	public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public Connection getCon(String aStrDBName, String aStrDBIP,
			String aDBUser, int iDBTypeTemp, String aStrPassword,
			String aStrDBPort) {
		Connection vCon = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			vCon = DriverManager.getConnection(
					(new StringBuilder("jdbc:oracle:thin:@")).append(aStrDBIP)
							.append(":").append(aStrDBPort).append(":")
							.append(aStrDBName).toString(), aDBUser,
					aStrPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vCon;
	}
	public void doSql(String _strSql,String aStrDBName, String aStrDBIP, String aDBUser, int iDBTypeTemp, String aStrPassword, String aStrDBPort){
		Connection vCon = getCon(aStrDBName, aStrDBIP, aDBUser, iDBTypeTemp, aStrPassword, aStrDBPort);
		PreparedStatement pst = null;
		ResultSet rst = null;
		Map<String,String> map = new HashMap<String, String>();
		try {
			pst = vCon.prepareStatement(_strSql);
			rst = pst.executeQuery();
			ResultSetMetaData rsmd = rst.getMetaData();
			map = solveMeta(rsmd,map);
//			int count = rst.getMetaData().getColumnCount();//列数量
//			
//			String strColName = "";//列名称
//			String strColVal = "";//列值
//			int iColType = 0;//列类型
//			while(rst.next()){
//				_strUpdateSql.append("insert into ").append(_strTabName).append("(");
//				 for(int i = 1; i <= count; i++){
//					 iColType = rsmd.getColumnType(i);
//			            if(2004!=iColType){
//			            	strColName = rsmd.getColumnName(i);
//			            	strColVal = rst.getString(strColName);
//			            	if(_strKey.equals(strColName)){
//			            		_dbf.sqlExe(new StringBuffer().append("delete from ").append(_strTabName).append(" where ").append(_strKey).append("='").append(strColVal).append("'").toString(), true);
//			            	}
//							_strCol.append(strColName).append(",");
//							_strVal.append("'").append(("null".equals(strColVal)||strColVal==null)?"":strColVal).append("'").append(",");
//			            }
//				 }
//				 _strUpdateSql.append(_strCol.deleteCharAt(_strCol.length()-1)).append(") ").append(" values (").append(_strVal.deleteCharAt(_strVal.length()-1)).append(")").append(";");
//			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
	        if (rst != null) {
	            try {
	                rst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (vCon != null) {
	            try {
	            	vCon.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
		}
	}
	   private Map<String,String> solveMeta(ResultSetMetaData arsmd,Map<String,String> map)
		        throws Exception
		    {
		        int count = arsmd.getColumnCount();
		        System.out.println("   tableName:"+"   tableType:"+"   iItemLength:");
		        for(int i = 1; i <= count; i++)
		        {
		            String tableName = arsmd.getColumnLabel(i);
		            int iItemLength = arsmd.getColumnDisplaySize(i);
		            int tableType = arsmd.getColumnType(i);
//		            if(2004==tableType){
		            	map.put(i+"", tableName);
		            	System.out.print("   "+tableName);
		            	System.out.print("   "+tableType);
		            	System.out.print("   "+iItemLength);
		            	System.out.println();
//		            }
		        }
		        return map;
		    }
	
	
	public String getTabConfigInfo(String _strId,StringBuffer sr){
		TableEx ex = null;
		TableEx exPd = null;
		String strUserName="";
		String strPwd="";
		String strTabName="";
		String strCon="";
		String strDataSource = "";
		String strIp = "";
		String strPort = "";
		String strCreateTAbSql = "";//建表语句
		int strDataBaseType = 1;
		String strTbDate = "";//同步数据日期
		String strOtherSql="";
		String strFlag = "1";
		String strDyb = "";//对应表
		String strTabIsVal="";
		String strSid = "";//同步表主键
		String strDbNameRemote="";//远程数据库名称
		String strDbNameLocal="";//本地数据库名称
		String strKey = "";//主键
//		DBFactory dbf = null;
		DBFactory dbf = new DBFactory();
		int iColType = 0;//字段类型
		Map<String,String> map = new HashMap<String, String>();
		DBFactory dbfOther = null;
		StringBuffer strUpdateSql = new StringBuffer();
		StringBuffer strVal = new StringBuffer();
		StringBuffer strCol = new StringBuffer();
		Connection vCon = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			/**查询数据库配置*/
			//select to_char(sysdate,'yyyy-MM-dd HH24:mi:ss')  from dual;
			ex = new TableEx("*", "T_DATA_TB",(_strId==null||"".equals(_strId))?"":("S_ID="+_strId));
			int iCount = ex.getRecordCount();
			Record rd = null;
			for(int a=0;a<iCount;a++){
				rd = ex.getRecord(a);
				strUserName=getColString("S_ZH",rd);//账号
				strPwd=getColString("S_PWD",rd);//密码
				strTabName=getColString("S_TABNAME",rd);//表名
				strTabName =strTabName.toUpperCase();
				strIp=getColString("S_IP",rd);//IP
				strPort=getColString("S_PORT",rd);//端口
				strSid=getColString("S_ID",rd);//主键值
				strTbDate = getColString("S_TBRQ", rd);//同步日期
				strDyb=getColString("S_DYB",rd);//对应表
				strDbNameRemote=getColString("S_DATABASE",rd);//远程数据库名称
				strDbNameLocal=getColString("S_DATABASE_LOCAL",rd);//本地数据库名称
//				strCreateTAbSql=getColString("S_CREATETAB",rd);//建表语句
				strDataBaseType=Integer.parseInt(getColString("S_TYPE",rd));//数据库类型
				strCon=getColString("S_TAB_COL",rd);//查询条件
				strDataSource = getColString("S_DATASOURCE", rd);//数据源
				strCreateTAbSql = getColString("S_CREATETAB", rd);//建表语句
				strKey = getColString("S_KEY", rd).toUpperCase();//主键名称
				if(strTabName.indexOf("t_sys_")>-1){
					return "";
				}
				/**判断本地库是否有数据*/
				exPd = dbf.query("select COUNT(*) as COUNT from all_tables where table_name='"+strTabName+"'");
			    strFlag=exPd.getRecord(0).getFieldByName("COUNT").value.toString();
			//	strFlag = "0";
				if("0".equals(strFlag)){//无数据
					/**建表*/
					dbf.sqlExe("drop table "+strTabName,true);
					System.out.println(strCreateTAbSql);
					dbf.sqlExe(strCreateTAbSql, true);
				}
				/**查询数据sql*/
				if(strCon!=null&&!"".equals(strCon)){
					strCon = " where 1=1 and " +strCon.replace("strupdatedate", strTbDate);
				}
				strOtherSql= "select * from "+strTabName +" "+strCon;
				sr.append(strOtherSql);
				//start
				vCon = getCon(strDataSource, strIp, strUserName, strDataBaseType, strPwd, strPort);
					pst = vCon.prepareStatement(strOtherSql);
					rst = pst.executeQuery();
					ResultSetMetaData rsmd = rst.getMetaData();
					map = solveMeta(rsmd,map);
					int count = rst.getMetaData().getColumnCount();//列数量
					
					String strColName = "";//列名称
					String strColVal = "";//列值
					
					while(rst.next()){
						strUpdateSql.append("insert into ").append(strTabName).append("(");
						 for(int i = 1; i <= count; i++){
							 iColType = rsmd.getColumnType(i);
					            if(2004!=iColType){
					            	strColName = rsmd.getColumnName(i);
					            	strColVal = rst.getString(strColName);
					            	iColType = rsmd.getColumnType(i);
					            	if(strKey.equals(strColName)){
					            		dbf.sqlExe(new StringBuffer().append("delete from ").append(strTabName).append(" where ").append(strKey).append("='").append(strColVal).append("'").toString(), true);
					            	}
					            	if("null".equals(strColVal)||strColVal==null||"".equals(strColVal)){
					            		if(iColType==2){
					            			strVal.append(0).append(",");
					            		}else{
					            			strVal.append("'").append("'").append(",");
					            		}
					            	}else{
					            	    if(strColVal.contains("'")){
					            			strColVal = strColVal.replace("'", "''");
					            		}
					            		if(strColVal.contains("\\")){
					            			strColVal = strColVal.replace("\\", "/");
					            		}
					            		strVal.append("'").append(strColVal).append("'").append(",");
					            	}
									strCol.append(strColName).append(",");
					            }
						 }
						 strUpdateSql.append(strCol.deleteCharAt(strCol.length()-1)).append(") ").append(" values (").append(strVal.deleteCharAt(strVal.length()-1)).append(")").append(";");
						 dbf.sqlExe(strUpdateSql.toString(), true);
							strUpdateSql= new StringBuffer();
							strVal = new StringBuffer();
							strCol = new StringBuffer();
					}
				//end
					
//				dbfOther = new DBFactory(strDataSource, strIp, strUserName, strDataBaseType, strPwd, strPort);
				/**更新同步日期*/
				dbf.sqlExe("update t_data_tb set S_TBRQ='"+strSdfYmdHms.format(new Date())+"' where S_ID="+strSid, true);
			}
		} catch (Exception e) {
//			strFlag = "0";
			sr.append(e.toString());
			e.printStackTrace();
		}finally{
			if(ex!=null){ex.close();}
			if(exPd!=null){exPd.close();}
			if(dbfOther!=null){dbfOther.close();}
			if(dbf!=null){dbf.close();}
	        if (rst != null) {
	            try {
	                rst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (vCon != null) {
	            try {
	            	vCon.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
			return sr.toString();
		}
	}
	
	public void updateData(String _sql,DBFactory _dbf,boolean _b) {
		try {
			_dbf.sqlExe(_sql,_b);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public String getColString(String _strCol,Record rd){
		String strReturn = "";
		try{
			strReturn= (rd.getFieldByName(_strCol)==null||"".equals(rd.getFieldByName(_strCol)))?"":rd.getFieldByName(_strCol).value.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strReturn;
	}
	
    public static void main(String args[])
            throws Exception
        {
            DBFactory dbf = null;
            TableEx end = null;
            try
            {
            	String strTabName="BD_PSNDOC";
            	String	strOtherSql= "select *  from "+strTabName  +" where 1=2";
            	new DataSynchronous().doSql(strOtherSql,"orcl","extop.tpddns.cn" , "gnnczs", 1, "gnnczs", "1521");
//            	DBFactory dbfOther = new DBFactory("orcl","extop.tpddns.cn" , "gnnczs", 1, "gnnczs", "1521");
				
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            finally{
            	if(end!=null)end.close();
            }
            Exception exception;
           
        }
}
