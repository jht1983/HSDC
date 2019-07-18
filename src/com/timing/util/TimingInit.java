package com.timing.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class TimingInit {

	public void init() {
//		EventCl.vecGJStatus = initData("T_WXDQGZJH");

	}

	public HashMap<String, String> InsertAttr(HttpServletRequest request) {
		HashMap<String, String> vecAddDate = new HashMap<String, String>(); // 返回的HashMap
		Map inputDataMap = request.getParameterMap(); // 获取传进来的所有参数
		Iterator entries = inputDataMap.entrySet().iterator(); // 创建迭代器
		String _strSplitN = ",";// 切割符号-变量
		try {
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next(); // 得到当前的 KEYVALUE entry形式
				String sys_PageKey = (String) entry.getKey(); // 得到KEY
				sys_PageKey.substring(sys_PageKey.indexOf("$") + 1);
				if (sys_PageKey.indexOf("NO_") == 0)
					continue; // 过滤NO_开头的
				if (sys_PageKey.indexOf("$") == 0)
					continue; // 过滤$开头的
				StringBuffer sys_PageValue = new StringBuffer();
				for (String a : (String[]) entry.getValue()) { // 如果数据重复 拼接
					sys_PageValue.append(new String(a.getBytes("iso8859-1"), "GBK"));
					sys_PageValue.append(_strSplitN);
				}
				vecAddDate.put(sys_PageKey, sys_PageValue.substring(0, sys_PageValue.length() - _strSplitN.length())); // 向返回的HashMap
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}
		return vecAddDate;
	}

	public boolean insertMain(HttpServletRequest request) {
//		String pageCode = request.getParameter("SPAGECODE");
//		String id = request.getParameter("S_ID");
//		switch (pageCode) {
//		case "1500428508300":
//			EventCl.vecGJStatus.add(getTabelAllData("T_WXDQGZJH", "T_WXDQGZJH.S_ZJ = '"+id+"'"));
//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "维修定时任务添加");
//			break;
//		default:
//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "定时任务缺省不添加");
//			break;
//		}
		return true;
	}

	public Vector<HashMap<String, String>> initData(String _tableName) {
		Vector<HashMap<String, String>> initVecprtData = new Vector<HashMap<String, String>>();
		HashMap<String, String> initMapData = null;
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		FieldEx fileEx = null;
		try {
			String sql = " select * from " + _tableName;
			tableEx = dbf.query(sql);
			int recordCount = tableEx.getRecordCount();
			int colCount = tableEx.getColCount();
			for (int i = 0; i < recordCount; i++) {
				initMapData = new HashMap<String, String>();
				record = tableEx.getRecord(i);
				for (int j = 0; j < colCount; j++) {
					fileEx = record.getFieldById(j + 1);
					initMapData.put(fileEx.fieldName.toString(), fileEx.value.toString());
				}
				initVecprtData.add(initMapData);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		return initVecprtData;
	}

	public HashMap<String, String> getTabelAllData(String _tableName, String _queryConditions) {
		HashMap<String, String> dataMap = new HashMap<String, String>();
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		FieldEx fileEx = null;
		try {
			String sql = " select * from " + _tableName + " where " + _queryConditions;
			tableEx = dbf.query(sql);
			int recordCount = tableEx.getRecordCount();
			int colCount = tableEx.getColCount();
			for (int i = 0; i < recordCount; i++) {
				record = tableEx.getRecord(i);
				for (int j = 0; j < colCount; j++) {
					fileEx = record.getFieldById(j + 1);
					dataMap.put(fileEx.fieldName.toString(), fileEx.value.toString());
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		return dataMap;
	}
}
