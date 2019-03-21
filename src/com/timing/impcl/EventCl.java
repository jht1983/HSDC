package com.timing.impcl;

import java.util.HashMap;
import java.util.Vector;

import com.timing.util.TimingTaskTool;
import com.yulongtao.web.event.Event;

public class EventCl extends Event {
	//1#粉尘
//	public static final String TAG_1_FC = "FKGY:DUP36:10HTA20CQ104_C";//折算前
	public static final String TAG_1_FC = "FKGY:DUP36:10HTA20CQ105";
	//1#SO2
//	public static final String TAG_1_SO2 = "FKGY:DUP36:10HTA20CQ101_C";//折算前
	public static final String TAG_1_SO2 = "FKGY:DUP36:10HTA20CQ101";
	//1#NOX
//	public static final String TAG_1_NOX = "FKGY:DUP36:10HTA20CQ102_C";//折算前
	public static final String TAG_1_NOX = "FKGY:DUP36:10HTA20CQ102";
	//2#粉尘
//	public static final String TAG_2_FC = "FKGY:DUP38:20HTA20CQ104_C";//折算前
	public static final String TAG_2_FC = "FKGY:DUP38:20HTA20CQ105";
	//2#SO2
//	public static final String TAG_2_SO2 = "FKGY:DUP38:20HTA20CQ101_C";//折算前
	public static final String TAG_2_SO2 = "FKGY:DUP38:20HTA20CQ101";
	//2#NOX
//	public static final String TAG_2_NOX = "FKGY:DUP38:20HTA20CQ102_C";//折算前
	public static final String TAG_2_NOX = "FKGY:DUP38:20HTA20CQ102";
	
	//1#实发功率
	public static final String TAG_1_GL = "DCS1:DUP9:10CRC01AO03";
	//2#实发功率
	public static final String TAG_2_GL = "DCS2:DUP9:20CRC01AO03";

	public static Vector<HashMap<String, String>> vecGJStatus = new Vector<HashMap<String, String>>();
	public Vector<HashMap<String, String>> runGJStatus = new Vector<HashMap<String, String>>();

	public static int sys_index = 0;
	// private static final long timeInterval = 10 * 60 * 1000L;
	// super.lFLAGTIME = 60000L;
	private int index = 0;

	TimingTaskTool timTool = new TimingTaskTool();

	// --SIS Data--start
	static String _getSisDate = "";

	static boolean isRun = false;
	public static boolean isStartSis = false; //set to true after deploy into PRO
	
	private int timer = 0;
	// --SIS Data--end

	private int startCleanHour = 23;
	private int startCleanMunite = 55;
	private int startLoadDataPeriod = 6*10; //10秒X6X10
	
	public boolean isRun() {
		timer++;
		if (timer >= startLoadDataPeriod) {
			timer = 0;
			
			if (vecGJStatus.size() != 0) {
				vecGJStatus.clear();
			}
		}
		
		//increment every 10 seconds
		
		//no need for HSDC
// 		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCl->isRun:timer: " + timer);
//		try {
//			if (isStartSis) {
//				DBFactory dbf = null;
//		        MantraUtil mtu = new MantraUtil();
//				
//				//load data 10 minutes
//				if (timer >= startLoadDataPeriod) {
//					timer = 0;
//
//					dbf = new DBFactory();
//					try {
//						dbf.sqlExe("DELETE FROM T_SIS_SYNCH WHERE UNIX_TIMESTAMP(S_TIME) < UNIX_TIMESTAMP(NOW()) - 3600 * 24", false);
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCl->isRun: The T_SIS_SYNCH has clean.");
//					} catch (Exception e) {
//						MantraLog.fileCreateAndWrite(e);
//					} finally {
//						if (dbf != null) {
//							dbf.close();
//						}
//					}
//					
//					httpCon httpConnection = new httpCon();
//					String result = httpConnection.sendGet(
//							"http://172.16.100.11/View/GetSnapshot.aspx", 
//							"first=1&queryField=" + TAG_1_FC +
//							"," + TAG_1_SO2 +
//							"," + TAG_1_NOX +
//							"," + TAG_2_FC +
//							"," + TAG_2_SO2 +
//							"," + TAG_2_NOX +
//							"," + TAG_1_GL +
//							"," + TAG_2_GL);
////					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCl->isRun:SIS Data: " + result);
//					//tag1,实时值 1,时间 1,描述 1,单位 1,|tag2,实时值 2,时间 2,描述 2,单位 2, 
//					if (StringUtils.isNotEmpty(result)) {
//						String[] data = result.split("\\|");
//						
//						String tag = null;
//						String value = null;
//						String time = null;
//						String desc = null;
//						String unit = null;
//						
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//						String currentDate = sdf.format(new Date());
//						
//						dbf = new DBFactory();
//						try {
//							for (int i = 0; i < data.length; i++) {
//								String[] record = data[i].split(",");
//								if (record.length < 5) {
//									continue;
//								}
//								
//								tag = record[0].trim();
//								value = record[1].trim();
//								time = currentDate;//record[2].trim()
//								desc = record[3].trim();
//								unit = record[4].trim();
//								
//								dbf.sqlExe(
//										"INSERT INTO T_SIS_SYNCH (`S_ID`, `S_TAG`, `S_VALUE`, `S_TIME`, `S_DESC`, `S_UNIT`) VALUES ('" + 
//										mtu.getShortUuid() + "','" + 
//								        tag + "','" + 
//								        value + "','" + 
//								        time + "','" + 
//								        desc + "','" + 
//								        unit + "')", false);
//							}
//						} catch (Exception e) {
//							MantraLog.fileCreateAndWrite(e);
//						} finally {
//							if (dbf != null) {
//								dbf.close();
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCl->isRun:ERR");
//			MantraLog.fileCreateAndWrite(e);
//		}
		// -----------获取SIS数据

		// --------定时任务
		try {
			synchronized(this) {
				if (vecGJStatus.size() != 0) { // 非空运行
					HashMap<String, String> runMap = null;

					for (int i = 0, j = vecGJStatus.size(); i < j; i++) { // 运行条数

						runMap = vecGJStatus.get(i);
						String SYS_LASTDATE = runMap.get("SYS_LASTDATE"); // 上一运行时间
						String SYS_NEXTDATE = runMap.get("SYS_NEXTDATE"); // 下一运行时间
						String SYS_TYPE = runMap.get("SYS_TYPE"); // 类别
						String SYS_START = runMap.get("SYS_START"); // 开始时间
						String SYS_AHEADDay = runMap.get("SYS_AHEADDay"); // 提前天数
						String cycles = runMap.get("cycles"); // 提前天数
						String S_QYZT = runMap.get("S_QYZT"); // 启用状态
	                    
						// 01_YQY is on, 02_TY is off
						if ("01_YQY".equals(S_QYZT) && timTool.runTimeCheck(SYS_NEXTDATE, SYS_AHEADDay)) { // 如果当天时间大于运行时间
							String[] cdit = timTool.nextTimeCompute(SYS_NEXTDATE, SYS_TYPE, cycles);

							runMap.put("SYS_LASTDATE", cdit[0]);
							runMap.put("SYS_NEXTDATE", cdit[1]);

							runGJStatus.add(runMap);
						}
					}
				} else {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "initEventClParameter");
					timTool.initEventClParameter();
				}
			}
			
			if (runGJStatus.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[:019]->EventCl->isRun:ERR");
			MantraLog.fileCreateAndWrite(e);
		}
		return false;
	}

	public synchronized void run() {
		HashMap<String, String> hmp = null;
		MantraUtil tool = new MantraUtil();
		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[:019]->EventCl->isRun:" + runGJStatus.size());
		for (int z = 0; z < runGJStatus.size(); z++) {
			hmp = runGJStatus.get(z);
			timTool.runningStartEvent(hmp);
		}

		if (runGJStatus.size() != 0) {
			runGJStatus.clear();
		}

		if (vecGJStatus.size() != 0) {
			vecGJStatus.clear();
		}
	}

	public static Vector<HashMap<String, String>> getVecGJStatus() {
		return vecGJStatus;
	}

	public static void setVecGJStatus(HashMap<String, String> vecGJStatus) {
		EventCl.vecGJStatus.add(vecGJStatus);
	}

	// SPAGECODE='+arr[1]+'&sys_bed=true&S_ID='+arr[2]+'&bmid='+arr[3]

}
