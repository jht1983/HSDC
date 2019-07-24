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
public final class TimingTaskDao {
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
					colNames = " (s_id,MineFullName,MineShortName) ";
				}
				else if ("入炉".equals(datamark)) {
					tableName = "T_rlmb";
					colNames = " (s_id,MineFullName,MineShortName) ";
				}
				else {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "TimingTaskDao.addLabdata -> wrong datamark: " + map);
				}
				
				dbf.sqlExe("delete from " + tableName + " where s_id='" + map.get("TESTNO") + "'", false);

				String datajsonvalue = (String) map.get("datajsonvalue");
				JsonNode node = mapper.readTree(datajsonvalue);
				String Qb_ad = node.get("Qb_ad").asText();
				String Qgr_d = node.get("Qgr_d").asText();
				String Qnet_var = node.get("Qnet_var").asText();
				String Qnet_v_arcal = node.get("Qnet_v_arcal").asText();
				String Mt = node.get("Mt").asText();
				String Mad = node.get("Mad").asText();
				String Mf = node.get("Mf").asText();
				String Sad = node.get("Sad").asText();
				String Sar = node.get("Sar").asText();
				String Sd = node.get("Sd").asText();
				String Ht_ad = node.get("Ht_ad").asText();
				String Har = node.get("Har").asText();
				String Hd = node.get("Hd").asText();
				String Vt_ad = node.get("Vt_ad").asText();
				String Vad = node.get("Vad").asText();
				String Var = node.get("Var").asText();
				String Vd = node.get("Vd").asText();
				String Vdaf = node.get("Vdaf").asText();
				String Aad = node.get("Aad").asText();
				String Aar = node.get("Aar").asText();
				String Ad = node.get("Ad").asText();
				String FCad = node.get("FCad").asText();
				String Fcar = node.get("Fcar").asText();
				String Fcd = node.get("Fcd").asText();
				String Hdaf = node.get("Hdaf").asText();
				String Nad = node.get("Nad").asText();
				String Nd = node.get("Nd").asText();
				String CD = node.get("CD").asText();
				String DTWD = node.get("DTWD").asText();
				String STWD = node.get("STWD").asText();
				String HTWD = node.get("HTWD").asText();
				String FTWD = node.get("FTWD").asText();
				String Had = node.get("Had").asText();
				
				//TODO:xxxxxxxxxxxxxxxxxxxxxx
				dbf.sqlExe("insert into " + tableName + colNames + " values('" + 
				    map.get("TESTNO") + "','" + 
					map.get("MineFullName") + "','" + 
				    map.get("MineShortName") + "')", false);
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
	public static String getStrByRecord(Record _rec, String _value) {
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
}
