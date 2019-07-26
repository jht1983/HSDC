/**
 * 
 */
package com.timing.util;

import java.util.HashMap;
import java.util.Map;

import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

/**
 * @author tianshisheng
 *
 */
public final class LaborSchedulingTimingDao {
	/**
	 * 
	 * @return
	 */
	public static String[] findLBWHIds() {
		final String sql = "select S_ID from T_LBSCWH where S_BMID = '001017'";
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
				result[i] = getStrByRecord(record, "S_ID");
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
	public static Map<String, String> findLBWHInfos(String sid) {
		final String sql = "SELECT '001017' bmid," + 
				"	T_LBSCWH.S_JZ_ATTR T_LBSCWH__S_JZ_ATTR," + 
				"	T_LBSCWH.S_LOG_ATTR T_LBSCWH__S_LOG_ATTR," + 
				"	T_LBSCWH.S_BMID T_LBSCWH__S_BMID," + 
				"	(" + 
				"		SELECT" + 
				"			S_LBZDMC" + 
				"		FROM" + 
				"			T_LBDY" + 
				"		WHERE" + 
				"			S_ID = T_LBSCWH.S_GZZX" + 
				"	) T_LBSCWH__S_GZZX," + 
				"	T_LBSCWH.S_ID T_LBSCWH__S_ID," + 
				"	T_LBSCWH.S_JSRQ T_LBSCWH__S_JSRQ," + 
				"	T_LBSCWH.S_KSRQ T_LBSCWH__S_KSRQ," + 
				"	T_LBSCWH.S_LBZQTS T_LBSCWH__S_LBZQTS," + 
				"	T_LBSCWH.S_ZQS T_LBSCWH__S_ZQS," + 
				"	T_LBSCWH.S_BMID T_LBSCWH__S_BMID," + 
				"	T_LBDY.S_LBPXTSXQ T_LBDY__S_LBPXTSXQ" + 
				" FROM" + 
				"	T_LBSCWH LEFT JOIN T_LBDY ON T_LBDY.S_ID = T_LBSCWH.S_GZZX" + 
				" WHERE" + 
				"	T_LBSCWH.S_BMID = '001017' and T_LBSCWH.S_ID='" + sid + "'";
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		Record record = null;
		Map<String, String> result = new HashMap<String, String>();
		try {
			tableEx = dbf.query(sql);
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				result.put("T_LBSCWH__S_LBZQTS", getStrByRecord(record, "T_LBSCWH__S_LBZQTS"));
				result.put("T_LBSCWH__S_ZQS", getStrByRecord(record, "T_LBSCWH__S_ZQS"));
				result.put("T_LBSCWH__S_KSRQ", getStrByRecord(record, "T_LBSCWH__S_KSRQ"));
				result.put("T_LBDY__S_LBPXTSXQ", getStrByRecord(record, "T_LBDY__S_LBPXTSXQ"));
				result.put("T_LBSCWH__S_ID", getStrByRecord(record, "T_LBSCWH__S_ID"));
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
	 * @param _org
	 * @param _teamCode
	 * @return
	 */
	public static boolean isLBZBExist(String date, String laborId) {
		TableEx tableEx = null;
		int recordIndex = 0;
		boolean result = false;
		try {
			tableEx = new TableEx("S_RQ", "T_LBSCWHZB", "S_RQ ='" + date + "' and S_PID='" + laborId + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex > 0) {
				result = true;
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
}
