package com.sis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;

public class TJSisData {
// 	static String[] dayCondCode = { "rb1_fdl", "rb1_jhdl", "rb1_hbfc", "rb1_hbso", "rb1_hbno", "rb2_fdl", "rb2_jhdl",
// 			"rb2_hbfc", "rb2_hbso", "rb2_hbno","DCS1:DUP9:10CRC01AO03","DCS2:DUP9:20CRC01AO03" };
// 	static String[] dayCondName = { "#1 机组日发电量 ", "#1 机组日计划电量 ", "#1 机粉尘浓度日平均", "#1 机 SO2 排放浓度日平均 ",
// 			"#1 机 NOX 排放浓度日平均 ", "#2 机组日发电量", "#2 机组日计划电量 ", "#2 机粉尘浓度日平均 ", "#2 机 SO2 排放浓度日平均 ", "#2 机 NOX 排放浓度日平均 ", "#1 ", "#2 " };

    static String[] dayCondCode = { "rb1_fdl", "rb1_jhdl", "rb1_hbfc", "rb1_hbso", "rb1_hbno", "rb2_fdl", "rb2_jhdl",
			"rb2_hbfc", "rb2_hbso", "rb2_hbno","rb1_yxsj","rb2_yxsj"};
	static String[] dayCondName = { "#1 机组日发电量 ", "#1 机组日计划电量 ", "#1 机粉尘浓度日平均", "#1 机 SO2 排放浓度日平均 ",
			"#1 机 NOX 排放浓度日平均 ", "#2 机组日发电量", "#2 机组日计划电量 ", "#2 机粉尘浓度日平均 ", "#2 机 SO2 排放浓度日平均 ", "#2 机 NOX 排放浓度日平均 ","rb1_yxsj","rb2_yxsj"  };

    //日发电 md_fdl
    public static String MD_FDL = "md_fdl";
    //月发电 md_yfdl
    public static String MD_YFDL = "md_yfdl";
    //年发电 md_nfdl
    public static String MD_NFDL = "md_nfdl";

	public void init() {
		TJSisData dbs = new TJSisData();
		Connection con = dbs.getSqlServerCon();
		PreparedStatement preStart = null;
		ResultSet rs = null;
		DBFactory dbf = new DBFactory();
		StringBuffer doSql = null;
		try {
			String sql = "select ID id,TAGCODE code,DATETIME time,R_VAL value,FLAG flag	from RPT_DAY where TAGCODE = ? ";
			preStart = con.prepareStatement(sql);
			for (int i = 0, j = dayCondCode.length; i < j; i++) {
				preStart.setString(1, dayCondCode[i]);
				rs = preStart.executeQuery();
				while (rs.next()) {
					doSql = new StringBuffer();
					doSql.append("INSERT INTO sip.t_sis_date_day (TAGCODE, DATETIME, R_VAL, FLAG, S_ID) VALUES (");
					doSql.append("'");
					doSql.append(rs.getString("code"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("time"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("value"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("flag"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("id"));
					doSql.append("');");
					dbf.exeSqls(doSql.toString(), true);
				}
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null)
				dbf.close();
			try {
				if (rs != null)
					rs.close();
				if (preStart != null)
					preStart.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}
		}
	}

	public boolean obtainData(String _time) {
		boolean retBool = false;
		TJSisData dbs = new TJSisData();
		Connection con = dbs.getSqlServerCon();
		PreparedStatement preStart = null;
		ResultSet rs = null;
		DBFactory dbf = new DBFactory();
		StringBuffer doSql = null;
		try {
			String sql = "select ID id,TAGCODE code,DATETIME time,R_VAL value,FLAG flag	from RPT_DAY where TAGCODE = ? and DATETIME = ? ";
			preStart = con.prepareStatement(sql);
			for (int i = 0, j = dayCondCode.length; i < j; i++) {
				preStart.setString(1, dayCondCode[i]);
				preStart.setString(2, _time);
				rs = preStart.executeQuery();
				while (rs.next()) {

					doSql = new StringBuffer();
					doSql.append("INSERT INTO sip.t_sis_date_day (TAGCODE, DATETIME, R_VAL, FLAG, S_ID) VALUES (");
					doSql.append("'");
					doSql.append(rs.getString("code"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("time"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("value"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("flag"));
					doSql.append("',");
					doSql.append("'");
					doSql.append(rs.getString("id"));
					doSql.append("');");
					dbf.exeSqls(doSql.toString(), true);
				}
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null)
				dbf.close();
			try {
				if (rs != null)
					rs.close();
				if (preStart != null)
					preStart.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}
		}
		if(doSql!=null) {
			retBool=true;
		}
		return retBool;
	}



	public Connection getSqlServerCon() {
		Connection con = null;
		String driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String url = "jdbc:microsoft:sqlserver://10.2.1.24:1433;DatabaseName=Yulin_SIS";
		String user = "sis";
		String password = "sis";

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			
			}else{
			    
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	public static Date getNextDay(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime();  
        return date;  
    }
	
	public Map<String, String> getSISData() {
		TJSisData dbs = new TJSisData();
		
		Connection con = null;
		PreparedStatement preStart = null;
		ResultSet rs = null;
		Map<String, String> result = new HashMap<String, String>();
		SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
		try {
			con = dbs.getSqlServerCon();
		    if(con==null){
		        return result;
		    }
		    
		    Calendar date = Calendar.getInstance();
	        String year = String.valueOf(date.get(Calendar.YEAR));
		    String sql = "select TAGCODE TAGCODE,R_VAL R_VAL from RPT_DAY" + year + " where DATETIME=? AND TAGCODE in ('" + MD_FDL + "','" + MD_YFDL + "','" + MD_NFDL + "') ";
//			String sql = "select R_VAL R_VAL from RPT_DAY where DATETIME=? AND TAGCODE in ('md1_fdl','md2_fdl') "; //分机组
//			String sql = "select R_VAL R_VAL from RPT_DAY where DATETIME=? AND TAGCODE in ('rb1_fdl','rb2_fdl') ";
		//	String sql = "select R_VAL R_VAL from RPT_DAY where DATETIME=? AND TAGCODE in ('DCS1:DUP9:10CRC01AO03','DCS2:DUP9:20CRC01AO03') ";
			preStart = con.prepareStatement(sql);
		    preStart.setString(1, sdf_ymd.format(getNextDay(new Date())));
		    
			rs = preStart.executeQuery();
			while (rs.next()) {
				result.put(rs.getString("TAGCODE"), String.valueOf(rs.getFloat("R_VAL")));
			}
		} catch (Exception e) {
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,e.getMessage());
			MantraLog.fileCreateAndWrite(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preStart != null)
					preStart.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}
		}
		return result;
	}
}
