/**
 * 
 */
package com.timing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

/**
 * @author tianshisheng
 *
 */
public final class FuelDataTimingDao {
	/**
	 * 
	 * @return
	 */
	public static String[] findMineNos() {
		final String sql = "select MineNo from T_FCM_Mineral";
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		Record record = null;
		int recordIndex = 0;
		String[] result = null;
		try {
			tableEx = dbf.query(sql);
			recordIndex = tableEx.getRecordCount();
			result = new String[recordIndex];

			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				result[i] = getStrByRecord(record, "MineNo");
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if (tableEx != null)
				tableEx.close();
		}

		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<Map<String, Object>> fatchMinerals() {
		List<Map<String, Object>> result = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement preStart = null;
		ResultSet rs = null;
		try {
			con = getSqlServerCon();
		    if(con==null){
		        return result;
		    }
		    
		    String sql = "select * from v_Taiji_Mineral";
		    preStart = con.prepareStatement(sql);
		    rs = preStart.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("MineNo", rs.getString("MineNo"));
				map.put("MineFullName", rs.getString("MineFullName"));
				map.put("MineShortName", rs.getString("MineShortName"));
				result.add(map);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}

			try {
				if (preStart != null)
					preStart.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @param minerals
	 */
	public static void updateMinerals(List<Map<String, Object>> minerals) {
		if (minerals == null || minerals.size() < 1) {
			return;
		}

		DBFactory dbf = new DBFactory();

		try {
			dbf.sqlExe("delete from T_FCM_Mineral", false);

			for (Iterator iterator = minerals.iterator(); iterator.hasNext();) {
				try {
					Map<String, Object> map = (Map<String, Object>) iterator.next();
					dbf.sqlExe("insert into T_FCM_Mineral (MineNo,MineFullName,MineShortName) values('" + map.get("MineNo")
							+ "','" + map.get("MineFullName") + "','" + map.get("MineShortName") + "')", false);
				} catch (Exception e) {
					// do nothing
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if (dbf != null) {
				dbf.close();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<Map<String, Object>> fatchLabdataByDate(Date dateFrom, Date dateTo) {
		List<Map<String, Object>> result = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement preStart = null;
		ResultSet rs = null;
		try {
			SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
			
			con = getSqlServerCon();
		    if(con==null){
		        return result;
		    }
		    
		    String sql = "select * from V_Taiji_QUALITYRESULT where testdate>=? and testdate<? and Datamark is not null";
		    preStart = con.prepareStatement(sql);
		    preStart.setString(1,  sdf_ymd.format(dateFrom));
		    preStart.setString(2,  sdf_ymd.format(dateTo));
		    rs = preStart.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("Datamark", rs.getString("Datamark"));
				map.put("MineralNo", rs.getString("MineralNo"));
				map.put("TESTNO", rs.getString("TESTNO"));
				map.put("testdate", rs.getString("testdate"));
				map.put("datajsonvalue", rs.getString("datajsonvalue"));
				result.add(map);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}

			try {
				if (preStart != null)
					preStart.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				MantraLog.fileCreateAndWrite(e);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param minerals
	 */
	public static void addLabdata(List<Map<String, Object>> data) {
		if (data == null || data.size() < 1) {
			return;
		}

		DBFactory dbf = new DBFactory();
		final ObjectMapper mapper = new ObjectMapper();
		String tableName = "";
		String colNames = "";

		try {
			for (Iterator iterator = data.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				String datamark = String.valueOf(map.get("Datamark")).trim();
				if ("入厂".equals(datamark)) {
					tableName = "T_rcmmz";
					colNames = " (s_id,fxrq,kb1,qsfk1,kgjsfk1,kgjhfaark1,kgjhfaadk1,kgjhfadk1,kgjhffvark1,kgjhffvadk1,kgjhffvdk1," 
							+ "kgjhffvdafk1,kgjqlstark1,kgjqlstadk1,kgjqlstdk1,gdtk1,dtfrlk1,kgjgwrz1,sdjdwrzkmj1,sdjdwrzkcal1) ";
				}
				else if ("入炉".equals(datamark)) {
					tableName = "T_rlmb";
					colNames = " (s_id,fxrq,kb1,qsfk1,kgjsfk1,kgjhfaark1,kgjhfaadk1,kgjhfadk1,kgjhffvark1,kgjhffvadk1,kgjhffvdk1," 
							+ "kgjhffvdafk1,kgjqlstark1,kgjqlstadk1,kgjqlstdk1,gdtk1,dtfrlk1,kgjgwrz1,sdjdwrzkmj1,sdjdwrzkcal1) ";
				}
				else {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "TimingTaskDao.addLabdata -> wrong datamark: " + map);
				}
				
				dbf.sqlExe("delete from " + tableName + " where s_id='" + map.get("TESTNO") + "'", false);

				String datajsonvalue = (String) map.get("datajsonvalue");
				JsonNode node = mapper.readTree(datajsonvalue);
				String Qb_ad = node.get("Qb_ad") == null ? "0" : node.get("Qb_ad").asText(); //弹筒发热量(发热)
				String Qgr_d = node.get("Qgr_d") == null ? "0" : node.get("Qgr_d").asText(); //干基高位发热量(发热)
				String Qnet_var = node.get("Qnet_var") == null ? "0" : node.get("Qnet_var").asText(); //收到基低位发热量(发)
				String Qnet_v_arcal = node.get("Qnet_v_arcal") == null ? "0" : node.get("Qnet_v_arcal").asText(); //收到基低位发热量(大卡)
				String Mt = node.get("Mt") == null ? "0" : node.get("Mt").asText(); //全水(发热量、水)
				String Mad = node.get("Mad") == null ? "0" : node.get("Mad").asText(); //分析水(发、水、碳氢氮、工、硫)
				String Mf = node.get("Mf") == null ? "0" : node.get("Mf").asText(); //外水(发热量)
				String Sad = node.get("Sad") == null ? "0" : node.get("Sad").asText(); //全硫（硫）
				String Sar = node.get("Sar") == null ? "0" : node.get("Sar").asText(); //收到基全硫
				String Sd = node.get("Sd") == null ? "0" : node.get("Sd").asText(); //干基硫
				String Ht_ad = node.get("Ht_ad") == null ? "0" : node.get("Ht_ad").asText(); //氢（过程）（碳氢氮）
				String Har = node.get("Har") == null ? "0" : node.get("Har").asText(); //收到基氢
				String Hd = node.get("Hd") == null ? "0" : node.get("Hd").asText(); //干基氢
				String Vt_ad = node.get("Vt_ad") == null ? "0" : node.get("Vt_ad").asText(); //挥发分（过程）（工分）
				String Vad = node.get("Vad") == null ? "0" : node.get("Vad").asText(); //空干基挥发分（工、发）
				String Var = node.get("Var") == null ? "0" : node.get("Var").asText(); //收到基挥发分
				String Vd = node.get("Vd") == null ? "0" : node.get("Vd").asText(); //干基挥发分
				String Vdaf = node.get("Vdaf") == null ? "0" : node.get("Vdaf").asText(); //干燥无灰基挥发分
				String Aad = node.get("Aad") == null ? "0" : node.get("Aad").asText(); //空干基灰分（工、发）
				String Aar = node.get("Aar") == null ? "0" : node.get("Aar").asText(); //收到基灰分
				String Ad = node.get("Ad") == null ? "0" : node.get("Ad").asText(); //干基灰分
				String FCad = node.get("FCad") == null ? "0" : node.get("FCad").asText(); //干燥无灰基碳（工分）
				String Fcar = node.get("Fcar") == null ? "0" : node.get("Fcar").asText(); //收到基碳
				String Fcd = node.get("Fcd") == null ? "0" : node.get("Fcd").asText(); //干基碳
				String Hdaf = node.get("Hdaf") == null ? "0" : node.get("Hdaf").asText(); //干燥无灰基氢
				String Nad = node.get("Nad") == null ? "0" : node.get("Nad").asText(); //空干基氮（碳氢氮）
				String Nd = node.get("Nd") == null ? "0" : node.get("Nd").asText(); //干基氮
				String CD = node.get("CD") == null ? "0" : node.get("CD").asText(); //干基碳
				String DTWD = node.get("DTWD") == null ? "0" : node.get("DTWD").asText(); //变形温度(灰熔融)
				String STWD = node.get("STWD") == null ? "0" : node.get("STWD").asText(); //软化温度(灰熔融)
				String HTWD = node.get("HTWD") == null ? "0" : node.get("HTWD").asText(); //半球温度(灰熔融)
				String FTWD = node.get("FTWD") == null ? "0" : node.get("FTWD").asText(); //流动温度(灰熔融)
				String Had = node.get("Had") == null ? "0" : node.get("Had").asText(); //空干基氢（发、工、碳氢氮）
				
				String testdate = "";
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					testdate = simpleDateFormat.format(simpleDateFormat.parse((String) map.get("testdate")));
				} catch (Exception e) {
					//do nothing
				}
				
				dbf.sqlExe("insert into " + tableName + colNames + " values('" + 
				    map.get("TESTNO") + "','" + 
				    testdate + "','" + 
					map.get("MineralNo") + "'," + 
					Mt + "," +  //qsfk1
					Mad + "," +  //kgjsfk1
					Aar + "," +  //kgjhfaark1
					Aad + "," +  //kgjhfaadk1
					Ad + "," +  //kgjhfadk1
					Var + "," +  //kgjhffvark1
					Vad + "," +  //kgjhffvadk1
					Vd + "," +  //kgjhffvdk1
					Vdaf + "," +  //kgjhffvdafk1
					Sar + "," +  //kgjqlstark1
					Sad + "," +  //kgjqlstadk1
					Sd + "," +  //kgjqlstdk1
					FCad + "," +  //gdtk1
					Qb_ad + "," +  //dtfrlk1
					Qgr_d + "," +  //kgjgwrz1
					Qnet_var + "," +  //sdjdwrzkmj1
					Qnet_v_arcal +  //sdjdwrzkcal1
					")", false);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if (dbf != null) {
				dbf.close();
			}
		}
	}

	/**
	 * 
	 * @param _rec
	 * @param _value
	 * @return
	 */
	private static String getStrByRecord(Record _rec, String _value) {
		String _retvalue = "";
		if (_rec.getFieldByName(_value).value != null) {
			_retvalue = _rec.getFieldByName(_value).value.toString();
		}
		return _retvalue;
	}
	
	/**
	 * 
	 * @return
	 */
	private static Connection getSqlServerCon() {
		Connection con = null;
		String driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String url = "jdbc:microsoft:sqlserver://172.168.3.4:1433;DatabaseName=CDM";
		String user = "user";
		String password = "123";

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}
		return con;
	}
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String testdate = simpleDateFormat.format(simpleDateFormat.parse("2019-07-28 10:51:57.000"));
		
		System.out.println(testdate);
	}
}
