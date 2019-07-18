/**
 * 
 */
package com.timing.util;

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
	public static String[] findMineNos(){
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
}
