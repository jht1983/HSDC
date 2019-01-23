package com.timing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.timing.impcl.MantraLog;
import com.timing.impcl.MantraUtil;
import com.timing.impcl.ProcessParameterVO;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.util.EString;

/**
 * @author bobo
 *
 */
public class TimingTaskTool {
	public static SimpleDateFormat strSdfYmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat strSdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat strSdfYmdx = new SimpleDateFormat("yyyy-M-d");
	public static SimpleDateFormat strSdfd = new SimpleDateFormat("dd");
	public static SimpleDateFormat strSdfm = new SimpleDateFormat("MM");

	public boolean runTimeCheck(String _nextDate, String _aheadDay) {
		Calendar nowDate = new GregorianCalendar();
		Calendar RunATL = dateStrToCalObj(_nextDate, "yyyy-MM-dd HH:mm:ss");
		RunATL.add(Calendar.DAY_OF_WEEK, Integer.parseInt(_aheadDay) * -1);
		if (nowDate.after(RunATL)) {
			return true;
		} else {
			return false;
		}
	}

	public String[] nextTimeCompute(String _nextDate, String _type, String _cycles) {
		String[] timeL = { "", "" }; // 返回时间计算
		Calendar nextDate = dateStrToCalObj(_nextDate, "yyyy-MM-dd HH:mm:ss"); // next Time Calendar
		String[] cycleArr = null;
		int cyclesL = 0;
		timeL[0] = _nextDate; // old Time
		switch (_type) {
		case "moon":
			cyclesL = Integer.parseInt(_cycles);
			nextDate.add(Calendar.MONTH, cyclesL);
			timeL[1] = strSdfYmdHms.format(nextDate.getTime());
			break;
		case "mn": // 2018-07-08 13:32:34 年
			cyclesL = Integer.parseInt(_cycles);
			nextDate.add(Calendar.YEAR, cyclesL);
			timeL[1] = strSdfYmdHms.format(nextDate.getTime());
			break;
		case "mt":
			cyclesL = Integer.parseInt(_cycles);
			nextDate.set(Calendar.SECOND, 0);
			nextDate.set(Calendar.MINUTE, 0);
			nextDate.set(Calendar.HOUR_OF_DAY, 0);
			nextDate.add(Calendar.DAY_OF_MONTH, cyclesL);
			timeL[1] = strSdfYmdHms.format(nextDate.getTime());
			break;
		case "mxs":
			cyclesL = Integer.parseInt(_cycles);
			// cyclesL = cyclesL * 24;
			nextDate.set(Calendar.SECOND, 0);
			nextDate.set(Calendar.MINUTE, 0);
			nextDate.add(Calendar.HOUR, cyclesL);
			timeL[1] = strSdfYmdHms.format(nextDate.getTime());
			break;
		}

		return timeL;
	}

	/**
	 * @param _startDate
	 *            开始时间
	 * @param _type
	 *            类别
	 * @param _cycles
	 *            见长
	 */
	public Calendar defaultCalculation(String _startDate, String _type, String _cycles) { // 开始初始化
		Calendar startDate = dateStrToCalObj(_startDate, "yyyy-MM-dd"); // 开始运行时间
		int cyclesL = 0;

		switch (_type) {
		case "moon":
			cyclesL = Integer.parseInt(_cycles);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.HOUR_OF_DAY, 0);
			startDate.add(Calendar.MONTH, cyclesL);
			break;
		case "mn": // 2018-07-08 13:32:34 年
			cyclesL = Integer.parseInt(_cycles);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.HOUR_OF_DAY, 0);
			startDate.add(Calendar.YEAR, cyclesL);
			break;
		case "mt":
			cyclesL = Integer.parseInt(_cycles);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.HOUR_OF_DAY, 0);
            startDate.add(Calendar.DAY_OF_MONTH, cyclesL);
			break;
		case "mxs":
			cyclesL = Integer.parseInt(_cycles);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.add(Calendar.HOUR, cyclesL);
			break;
		}
		return startDate;
	}

	public static Calendar dateStrToCalObj(String _Str, String _dateFormat) {
		if ("".equalsIgnoreCase(_Str)) {
			return null;
		}
		Calendar calendar = new GregorianCalendar();
		try {
			SimpleDateFormat Simple = new SimpleDateFormat(_dateFormat);
			Date nDate = Simple.parse(_Str);
			calendar.setTime(nDate);
		} catch (ParseException e) {
			MantraLog.fileCreateAndWrite(e);
		}
		return calendar;
	}

	public void setEventClParameter(HttpServletRequest _request) {
//		ProcessParameterVO proVo = new ProcessParameterVO(_request); // 解析标准数据
//		HashMap<String, String> setEventPar = new HashMap<String, String>();
//		TableEx tableEx = null;
//		Record record = null;
//		DBFactory dbf = new DBFactory();
//		int recordIndex = 0;
//		String sql = "";
//
//		if ("1500428508300".equals(proVo.getSpageCode())) {
//			sql = "select T_WXDQGZJH.S_SJPL_ZQDW SYS_TYPE,T_WXDQGZJH.S_SJPL_KSRQ SYS_START,T_WXDQGZJH.S_SJPL_TJQ SYS_AHEADDay,T_WXDQGZJH.S_SJPL_XYZXMBRQ SYS_NEXTDATE,T_WXDQGZJH.S_SJPL_SYZXMBRQ SYS_LASTDATE,T_WXDQGZJH.S_SJPL_ZQ cycles,T_WXDQGZJH.S_QYZT S_QYZT from T_WXDQGZJH where S_ZJ='"
//					+ proVo.getInpPkey() + "'";
//		} else if ("1531376571473".equals(proVo.getSpageCode())) { //运行定期
//			// editToSucces
//			// 2018-07-08 18:32:56
//			sql = "select T_YXDQGZJH_R.S_ZQDW SYS_TYPE,T_YXDQGZJH_R.S_KSRQ SYS_START,\"0\" SYS_AHEADDay,T_YXDQGZJH_R.S_XYZXRQ SYS_NEXTDATE,T_YXDQGZJH_R.S_SYZXRQ SYS_LASTDATE,T_YXDQGZJH_R.S_ZQ cycles,T_YXDQGZJH_R.S_QYZT S_QYZT from T_YXDQGZJH_R where S_ID='"
//					+ proVo.getInpPkey() + "';";
//		}
//		try {
//			tableEx = dbf.query(sql);
//			recordIndex = tableEx.getRecordCount();
//			for (int i = 0; i < recordIndex; i++) {
//				record = tableEx.getRecord(i);
//				String SYS_TYPE = getStrByRecord(record, "SYS_TYPE");
//				String SYS_START = getStrByRecord(record, "SYS_START");
//				String SYS_AHEADDay = getStrByRecord(record, "SYS_AHEADDay");
//				String SYS_NEXTDATE = getStrByRecord(record, "SYS_NEXTDATE");
//				String SYS_LASTDATE = getStrByRecord(record, "SYS_LASTDATE");
//				// 2018-07-04 14:03:51 add enabled;
//				String S_QYZT = getStrByRecord(record, "S_QYZT");
//
//				String cycles = getStrByRecord(record, "cycles");
//				SYS_NEXTDATE = strSdfYmdHms.format(defaultCalculation(SYS_START, SYS_TYPE, cycles).getTime());
//
//				setEventPar.put("SYS_ORG", proVo.getBranck());
//				setEventPar.put("SYS_PK", proVo.getInpPkey());
//				setEventPar.put("SYS_PAGECODE", proVo.getSpageCode());
//				setEventPar.put("SYS_TYPE", SYS_TYPE);
//				setEventPar.put("SYS_START", SYS_START);
//				setEventPar.put("SYS_AHEADDay", SYS_AHEADDay);
//				setEventPar.put("SYS_NEXTDATE", SYS_NEXTDATE);
//				setEventPar.put("SYS_LASTDATE", SYS_LASTDATE);
//				setEventPar.put("S_QYZT", S_QYZT);
//				setEventPar.put("cycles", cycles);
//
//				com.timing.impcl.EventCl.setVecGJStatus(setEventPar);
//				if ("1500428508300".equals(proVo.getSpageCode())) {
//					dbf.sqlExe("update T_WXDQGZJH set T_WXDQGZJH.S_SJPL_XYZXMBRQ='" + SYS_NEXTDATE + "' where S_ZJ='"
//							+ proVo.getInpPkey() + "' ", false);
//				
//				} else if ("1531376571473".equals(proVo.getSpageCode())) {
//					// 2018-07-08 18:32:56
//					dbf.sqlExe("update T_YXDQGZJH_R set T_YXDQGZJH_R.S_XYZXRQ='" + SYS_NEXTDATE + "' where S_ID='"
//							+ proVo.getInpPkey() + "' ", false);
//				}
//				
//
//			}
//		} catch (Exception e) {
//			MantraLog.fileCreateAndWrite(e);
//		} finally {
//			tableEx.close();
//			dbf.close();
//		}
	}

	public void initEventClParameter() {
		TableEx tableEx = null;

		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;

		// editToSucces
		String[] pagecodeArr = { "1500428508300","1531376571473" };
		String[] sqlArr = {
				"select T_WXDQGZJH.S_ZZ SYS_ORG, T_WXDQGZJH.S_ZJ SYS_PK,T_WXDQGZJH.S_SJPL_ZQDW SYS_TYPE,T_WXDQGZJH.S_SJPL_KSRQ SYS_START,T_WXDQGZJH.S_SJPL_TJQ SYS_AHEADDay,T_WXDQGZJH.S_SJPL_XYZXMBRQ SYS_NEXTDATE,T_WXDQGZJH.S_SJPL_SYZXMBRQ SYS_LASTDATE,T_WXDQGZJH.S_SJPL_ZQ cycles,T_WXDQGZJH.S_QYZT S_QYZT from T_WXDQGZJH left join t_sys_flow_run ON t_wxdqgzjh.S_RUN_ID = t_sys_flow_run.S_RUN_ID where t_sys_flow_run.I_ISOVER=1 " 
				,"select T_YXDQGZJH_R.S_ZZ SYS_ORG, T_YXDQGZJH_R.S_ID SYS_PK,T_YXDQGZJH_R.S_ZQDW SYS_TYPE,T_YXDQGZJH_R.S_KSRQ SYS_START,\"0\" SYS_AHEADDay,T_YXDQGZJH_R.S_XYZXRQ SYS_NEXTDATE,T_YXDQGZJH_R.S_SYZXRQ SYS_LASTDATE,T_YXDQGZJH_R.S_ZQ cycles,T_YXDQGZJH_R.S_QYZT S_QYZT from T_YXDQGZJH_R left join t_sys_flow_run ON T_YXDQGZJH_R.S_RUN_ID = t_sys_flow_run.S_RUN_ID where t_sys_flow_run.I_ISOVER=1"
		};
		try {
			for (int i = 0; i < pagecodeArr.length; i++) {
				tableEx = dbf.query(sqlArr[i]); // 运行Sql
				recordIndex = tableEx.getRecordCount();
			
				for (int j = 0; j < recordIndex; j++) {
					HashMap<String, String> setEventPar = new HashMap<String, String>();
					record = tableEx.getRecord(j);
					String SYS_TYPE = getStrByRecord(record, "SYS_TYPE");
					String SYS_START = getStrByRecord(record, "SYS_START");
					String SYS_AHEADDay = getStrByRecord(record, "SYS_AHEADDay");
					String SYS_NEXTDATE = getStrByRecord(record, "SYS_NEXTDATE");
					String SYS_LASTDATE = getStrByRecord(record, "SYS_LASTDATE");
					String cycles = getStrByRecord(record, "cycles");
					//SYS_NEXTDATE = "".equals(SYS_NEXTDATE)defaultCalculation(String _startDate, String _type, String _cycles)?:SYS_NEXTDATE;
					
					if("".equals(SYS_NEXTDATE) ) {
					 
					    MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,""+getStrByRecord(record, "SYS_PK"));
					     
					    if("".equals(SYS_START)){
    					    continue;
    					}
						SYS_NEXTDATE = strSdfYmdHms.format(defaultCalculation(SYS_START, SYS_TYPE, cycles).getTime());
					}
					
					// 2018-07-04 14:03:51 add enabled;
					String S_QYZT = getStrByRecord(record, "S_QYZT");

					setEventPar.put("SYS_PAGECODE", pagecodeArr[i]);
					setEventPar.put("SYS_ORG", getStrByRecord(record, "SYS_ORG"));
					setEventPar.put("SYS_PK", getStrByRecord(record, "SYS_PK"));
					setEventPar.put("SYS_TYPE", SYS_TYPE);
					setEventPar.put("SYS_START", SYS_START);
					setEventPar.put("SYS_AHEADDay", SYS_AHEADDay);
					setEventPar.put("SYS_NEXTDATE", SYS_NEXTDATE);
					setEventPar.put("SYS_LASTDATE", SYS_LASTDATE);
					setEventPar.put("S_QYZT", S_QYZT);
					setEventPar.put("cycles", cycles);
					com.timing.impcl.EventCl.setVecGJStatus(setEventPar);
				}
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}
	}

	public void deleteEventClParameter(String spageCode, String id) {
		Vector<HashMap<String, String>> vecGJStatus = com.timing.impcl.EventCl.getVecGJStatus();
		HashMap<String, String> traMap = null;
		for (int i = 0, j = vecGJStatus.size(); i < j; i++) { // 运行条数
			traMap = vecGJStatus.get(i);
			if (spageCode.equals(traMap.get("SYS_PAGECODE")) && id.equals(traMap.get("SYS_PK"))) {
				vecGJStatus.remove(i);
				break;
			}
		}
	}

	public void editEventClParameter() {

	}

	public void stopEventClParameter(String _sPageCode, String _id) {
		// Change the variable to disabled
		// S_QYZT
		DBFactory dbf = new DBFactory();
		Vector<HashMap<String, String>> vecGJStatus = com.timing.impcl.EventCl.getVecGJStatus();
		HashMap<String, String> traMap = null;
		for (int i = 0, j = vecGJStatus.size(); i < j; i++) { // 运行条数
			traMap = vecGJStatus.get(i);
			if (_sPageCode.equals(traMap.get("SYS_PAGECODE")) && _id.equals(traMap.get("SYS_PK"))) {
				// 01_YQY is on, 02_TY is off
				traMap.put("S_QYZT", "02_TY");
				break;
			}
		}
		try {
			
			if ("1500428508300".equals(_sPageCode)) {
				dbf.sqlExe("update T_WXDQGZJH set T_WXDQGZJH.S_QYZT='02_TY' where S_ZJ='" + _id + "' ", false);

			} else if ("1531376571473".equals(_sPageCode)) {
				// 2018-07-08 18:32:56
				//editToSucc
				dbf.sqlExe("update T_YXDQGZJH_R set T_YXDQGZJH_R.S_QYZT='02_TY' where S_ID='" + _id + "' ", false);
			}
			
			
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
		}

	}

	// case "startEventCl":
	// case "stopEventCl":
	public void startEventClParameter(String _sPageCode, String _id) {
		// Check the data for changes
		DBFactory dbf = new DBFactory();
		Vector<HashMap<String, String>> vecGJStatus = com.timing.impcl.EventCl.getVecGJStatus();
		HashMap<String, String> traMap = null;
		String startDate, type, cycles, SYS_NEXTDATE;
		for (int i = 0, j = vecGJStatus.size(); i < j; i++) { // 运行条数
			traMap = vecGJStatus.get(i);
			if (_sPageCode.equals(traMap.get("SYS_PAGECODE")) && _id.equals(traMap.get("SYS_PK"))) {
				// 01_YQY is on, 02_TY is off
				traMap.put("S_QYZT", "01_YQY");
				startDate = traMap.get("SYS_START");
				type = traMap.get("SYS_TYPE");
				cycles = traMap.get("cycles");

				SYS_NEXTDATE = strSdfYmdHms.format(defaultCalculation(startDate, type, cycles).getTime());

				traMap.put("SYS_NEXTDATE", SYS_NEXTDATE);

				break;
			}
		}
		try {
			if ("1500428508300".equals(_sPageCode)) {
				dbf.sqlExe("update T_WXDQGZJH set T_WXDQGZJH.S_QYZT='01_YQY' where S_ZJ='" + _id + "' ", false);

			} else if ("1531376571473".equals(_sPageCode)) {
				// 2018-07-08 18:32:56
				//editToSucc
				dbf.sqlExe("update T_YXDQGZJH_R set T_YXDQGZJH_R.S_QYZT='01_YQY' where S_ID='" + _id + "' ", false);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
		}
	}
	
	public String findPeopleByDuty(String _id,String _date,String _S_BCBM){
		_S_BCBM = "'" + _S_BCBM.replace(",", "','") + "'";
		String sql = "select S_PEOPLE_CODE S_PEOPLE_CODE from t_lbscwh LEFT JOIN T_LBSCWHZB ON t_lbscwh.S_ID=T_LBSCWHZB.S_PID LEFT JOIN T_BZ ON T_BZ.S_ID=T_LBSCWHZB.s_bzbm LEFT JOIN T_BZRYB ON T_BZRYB.S_USER_CODE=T_BZ.s_bm and T_BZRYB.S_LOG_ATTR=t_lbscwh.S_LOG_ATTR AND T_BZRYB.S_JZ_ATTR=t_lbscwh.S_JZ_ATTR where t_lbscwh.s_id='"+_id+"' AND T_LBSCWHZB.S_RQ = '"+_date+"' and S_BCBM IN ("+_S_BCBM+")";
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		Record record = null;
		int recordIndex = 0;
		StringBuffer retStr = new StringBuffer();
		try {
			// S_USER_CODE,S_PEOPLE_CODE
			tableEx = dbf.query(sql);
			recordIndex = tableEx.getRecordCount();
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				retStr.append(getStrByRecord(record, "S_PEOPLE_CODE"));
				retStr.append(",");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (tableEx != null)
				tableEx.close();
		}
		
		
		return retStr.toString();
	}
	
	
	public String findPeopleByTeam(String _org, String _teamCode) {
		_teamCode = "'" + _teamCode.replace(",", "','") + "'";
		TableEx tableEx = null;
		Record record = null;
		int recordIndex = 0;
		StringBuffer retStr = new StringBuffer();
		try {
			// S_USER_CODE,S_PEOPLE_CODE
			tableEx = new TableEx("T_BZRYB.S_PEOPLE_CODE S_PEOPLE_CODE", "T_BZRYB",
					"S_USER_CODE IN (" + _teamCode + ");");
			recordIndex = tableEx.getRecordCount();
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				retStr.append(getStrByRecord(record, "S_PEOPLE_CODE"));
				retStr.append(",");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (tableEx != null)
				tableEx.close();
		}
		return retStr.toString();
	}

	public String getStrByRecord(Record _rec, String _value) {
		String _retvalue = "";
		if (_rec.getFieldByName(_value).value != null) {
			_retvalue = _rec.getFieldByName(_value).value.toString();
		}
		return _retvalue;
	}

	public boolean runningStartEvent(HashMap<String, String> hmp) {
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		MantraUtil tool = new MantraUtil();
		try {

			String sys_pageCode = hmp.get("SYS_PAGECODE");
			String sys_pk = hmp.get("SYS_PK");
			String sys_org = hmp.get("SYS_ORG");
			String sql = "";
			String people = "";
			if ("1500428508300".equals(sys_pageCode)) { // 维修定期工作
				String plansToField = "T_WXDQGZJH.S_JZ,T_WXDQGZJH.S_SBMC,T_WXDQGZJH.S_SBBM,T_WXDQGZJH.S_GZNR,T_WXDQGZJH.S_GZYQ,'"+hmp.get("SYS_LASTDATE")+"',T_WXDQGZJH.S_ZXXX_ZY,T_WXDQGZJH.S_ZXXX_DQGZFZR,T_WXDQGZJH.S_ZZ,<<SYS_GENER_ID>>,<<FLOW_ID>>,<<UUID>>";
				String performField = "T_WXDQGZZX.S_JZ,T_WXDQGZZX.S_SBMC,T_WXDQGZZX.S_SBBM,T_WXDQGZZX.S_GZNR,T_WXDQGZZX.S_GZYQ,T_WXDQGZZX.S_JHZXSJ,T_WXDQGZZX.S_ZY,T_WXDQGZZX.S_DQGZFZR,T_WXDQGZZX.S_ZZ,T_WXDQGZZX.S_ZJ,T_WXDQGZZX.SYS_FLOW_VER,T_WXDQGZZX.S_RUN_ID";
				sql = "insert into T_WXDQGZZX (" + performField + ") select " + plansToField
						+ " from T_WXDQGZJH where S_ZJ='" + sys_pk + "'";

				tableEx = new TableEx("S_ZXXX_WWZXDW_BM S_BZ,T_WXDQGZJH.S_GZNR S_GZNR", "T_WXDQGZJH", "S_ZJ='" + sys_pk + "'");

				people = findPeopleByTeam(sys_org, getStrByRecord(tableEx.getRecord(0), "S_BZ"));
				// 查找运行班组的所有人员
				sql = tool.sqlDisCom(sql, "pageCode=1500428518499&bmid=" + sys_org); // 处理SQL
				
				dbf.sqlExe(sql.toString(), false);
				tool.recordRel(sys_org, sys_pageCode, sys_pk, "T_WXDQGZJH", "1500428518499", tool.getOrdGreId(),
						"T_WXDQGZZX");
				
				insertMsg(tool.getUUID(), people, getStrByRecord(tableEx.getRecord(0), "S_GZNR"), "1500428518499", tool.getOrdGreId(), sys_org,
						strSdfYmdHms.format(new Date()));
				
				sql = "update T_WXDQGZJH set S_SJPL_SYZXMBRQ='" + hmp.get("SYS_LASTDATE") + "' ,S_SJPL_XYZXMBRQ='"
						+ hmp.get("SYS_NEXTDATE") + "' where S_ZJ='" + sys_pk + "'";
	
				
			}else if("1531376571473".equals(sys_pageCode)){//运行

				String plansToField = "T_YXDQGZJH_R.S_FZZY,T_YXDQGZJH_R.T_BC,T_YXDQGZJH_R.T_BCBM,T_YXDQGZJH_R.s_zz,<<SYS_GENER_ID>>,<<FLOW_ID>>,<<UUID>>,T_YXDQGZJH_R.S_DJH,T_YXDQGZJH_R.S_DQGZZXR,T_YXDQGZJH_R.S_DQGZZXR_BM,T_YXDQGZJH_R.S_LBZ,T_YXDQGZJH_R.S_LBZ_BM,T_YXDQGZJH_R.S_SBBM,T_YXDQGZJH_R.S_SBMC,T_YXDQGZJH_R.T_GZNR,'"+hmp.get("SYS_LASTDATE")+"'" ;
				String performField = "T_YXDQGZZX_R.S_ZY,T_YXDQGZZX_R.S_BC,T_YXDQGZZX_R.S_BCBM,T_YXDQGZZX_R.s_zz,T_YXDQGZZX_R.S_ID,T_YXDQGZZX_R.SYS_FLOW_VER,T_YXDQGZZX_R.S_RUN_ID,T_YXDQGZZX_R.S_LYDJBM,T_YXDQGZZX_R.S_BG_DQGZFZR,T_YXDQGZZX_R.S_BG_DQGZZXR_BM,T_YXDQGZZX_R.S_LBZ,T_YXDQGZZX_R.S_LBZ_BM,T_YXDQGZZX_R.S_SBBM,T_YXDQGZZX_R.S_SBMC,T_YXDQGZZX_R.T_GZNR,T_YXDQGZZX_R.S_JHZXSJ";


				sql = "insert into T_YXDQGZZX_R (" + performField + ") select " + plansToField
						+ " from T_YXDQGZJH_R where S_ID='" + sys_pk + "'";
				
				tableEx = new TableEx("T_YXDQGZJH_R.S_LBZ_BM LB,T_YXDQGZJH_R.T_BCBM BC,T_YXDQGZJH_R.T_GZNR T_GZNR", "T_YXDQGZJH_R", "S_ID='" + sys_pk + "'");
	
				people = findPeopleByDuty(getStrByRecord(tableEx.getRecord(0), "LB"),strSdfYmdx.format(new Date()),getStrByRecord(tableEx.getRecord(0), "BC"));
				
				sql = tool.sqlDisCom(sql, "pageCode=1531366190826&bmid=" + sys_org); // 处理SQL
				
				dbf.sqlExe(sql.toString(), false);
				//关系图
//				tool.recordRel(sys_org, sys_pageCode, sys_pk, "T_WXDQGZJH", "1500428518499", tool.getOrdGreId(),
//						"T_WXDQGZZX");
				
				insertMsg(tool.getUUID(), people, getStrByRecord(tableEx.getRecord(0), "T_GZNR"), "1531366190826", tool.getOrdGreId(), sys_org,
						strSdfYmdHms.format(new Date()));

				sql = "update T_YXDQGZJH_R set S_SYZXRQ='" + hmp.get("SYS_LASTDATE") + "' ,S_XYZXRQ='"
						+ hmp.get("SYS_NEXTDATE") + "' where S_ID='" + sys_pk + "'";
				
			}
		

		

			dbf.sqlExe(sql.toString(), false);

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null) {
				dbf.close();
			}

		}
		return true;
	}

	public void insertMsg(String runId, String getUserCode, String msgCong, String pagecode, String S_ID, String org,
			String date) {
		String sql = " INSERT INTO T_MSG_RECORDS ( S_ID, S_YXID, S_JSR, S_XXLX, S_XXNR, S_PAGECODE, S_SID, S_BMID, S_CFLB, S_ZT, S_FSSJ ) "
				+ "VALUES" + " ( '"+EString.generId()+"', " + "'" + runId + "', " + "'" + getUserCode + "', " + "'system',"
				+ " '" + msgCong + "'," + " '" + pagecode + "', " + "'" + S_ID + "'," + " '" + org + "', " + "'6S', "
				+ "'0', " + "'" + date + "' );";
		DBFactory dbf = new DBFactory();
		try {
			dbf.sqlExe(sql, false);
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
		}
	}

}
