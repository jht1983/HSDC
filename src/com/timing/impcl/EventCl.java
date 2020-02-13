package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.timing.util.MisLogger;
import com.timing.util.TimingTaskTool;
import com.yulongtao.web.event.Event;

public class EventCl extends Event {
	private static MisLogger logger = new MisLogger(EventCl.class);
	public static Vector<HashMap<String, String>> vecGJStatus = new Vector<HashMap<String, String>>();
	public Vector<HashMap<String, String>> runGJStatus = new Vector<HashMap<String, String>>();

	public static int sys_index = 0;
	TimingTaskTool timTool = new TimingTaskTool();

	// --SIS Data--start
	static boolean isRun = false;
	private int timer = 0;
	// --SIS Data--end

	//tasks run once every day
	FuelDataTiming fuelDataTiming = new FuelDataTiming();
	LaborSchedulingTiming laborSchedulingTiming = new LaborSchedulingTiming();
	MessageRecordTiming messageRecordTiming = new MessageRecordTiming();
	private boolean isNotRun = true;
	private SimpleDateFormat sdfHour = new SimpleDateFormat("HH");

	private int startLoadDataPeriod = 6*10; //10秒X6X10
	
	public boolean isRun() {
		timer++;
		if (timer >= startLoadDataPeriod) {
			timer = 0;
			
			if (vecGJStatus.size() != 0) {
				vecGJStatus.clear();
			}
		}
		
		//check T_QXJL every 10*60 seconds
		if (timer % 60 == 0) {
			timTool.checkQXJL();
			//timTool.checkYHDJPG();
		}
		
		//tasks run once every day
		String hour = sdfHour.format(new Date());
		if ("01".equals(hour)) { //01
			if (isNotRun) {
				isNotRun = false;
				
				try {
					fuelDataTiming.fetchMineralData();
				} catch (Exception e) {
					MantraLog.fileCreateAndWrite(e);
				}
				
				try {
					fuelDataTiming.fetchFuelData();
				} catch (Exception e) {
					MantraLog.fileCreateAndWrite(e);
				}
				
				try {
					laborSchedulingTiming.initLaborSchedulings();
				} catch (Exception e) {
					MantraLog.fileCreateAndWrite(e);
				}
				
				try {
					messageRecordTiming.cleanDutyMessages();
				} catch (Exception e) {
					MantraLog.fileCreateAndWrite(e);
				}
			}
		}
		else if (!isNotRun) {
			isNotRun = true;
		}

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
					logger.debug("initEventClParameter");
					timTool.initEventClParameter();
				}
			}
			
			if (runGJStatus.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.debug("[:019]->EventCl->isRun:ERR");
			MantraLog.fileCreateAndWrite(e);
		}
		return false;
	}

	public synchronized void run() {
		HashMap<String, String> hmp = null;
		logger.debug("[:019]->EventCl->isRun:" + runGJStatus.size());
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
}
