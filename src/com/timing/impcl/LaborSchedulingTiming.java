/**
 * 
 */
package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.timing.util.LaborSchedulingTimingDao;
import com.timing.util.TimingTaskTool;
import com.yulongtao.db.DBFactory;

/**
 * @author tianshisheng
 *
 */
public class LaborSchedulingTiming {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 */
	public void initLaborSchedulings() {
		String[] laborIds = LaborSchedulingTimingDao.findLBWHIds();

		for (int i = 0; i < laborIds.length; i++) {
			initLaborScheduling(laborIds[i]);
		}
	}

	/**
	 * 
	 * @param laborId
	 * @throws Exception
	 */
	public void initLaborScheduling(String laborId) {
		DBFactory dbf = null;
		try {
			dbf = new DBFactory();

			// '8&100&2018-11-11&154467085862012623`154503085759215477`true-154467088231212624`152748784842810172`true&153758459996411064'
			Map<String, String> labor = LaborSchedulingTimingDao.findLBWHInfos(laborId);
			String S_LBZQTS = labor.get("T_LBSCWH__S_LBZQTS");
			int S_LBZQTS_int = Integer.parseInt(S_LBZQTS);
			String S_ZQS = labor.get("T_LBSCWH__S_ZQS");
			int S_ZQS_int = Integer.parseInt(S_ZQS);
			if (S_ZQS_int < 500) {
				S_ZQS_int = 500;
			}
			String S_KSRQ = labor.get("T_LBSCWH__S_KSRQ");
			String S_LBCS = labor.get("T_LBDY__S_LBPXTSXQ");
			String pid = labor.get("T_LBSCWH__S_ID");

			int index = 0;
			int dataIndex = 0;
			String[] S_LBCS_arr = S_LBCS.split("-");
			int S_LBCS_arrLength = S_LBCS_arr.length;
			double oneDayTime = S_LBCS_arrLength / S_LBZQTS_int;
			Date ksrq = sdf.parse(S_KSRQ);

			Date now = new Date();
			now = sdf.parse(sdf.format(now));
			Date startDate = TimingTaskTool.getDateBefore(now, S_LBZQTS_int);
			Date endDate = TimingTaskTool.getDateAfter(now, S_LBZQTS_int);

			dbf.exeSqls("delete from T_LBSCWHZB where S_PID='" + laborId + "'", false);
			for (int z = 0; z < S_ZQS_int; z++) {
				for (int i = 0; i < S_LBCS_arrLength; i++) {
					String[] aloneArr = S_LBCS_arr[i].split("`");
					Date por = TimingTaskTool.getDateAfter(ksrq, (int) (dataIndex / oneDayTime));
					String porStr = sdf.format(por);

					if ("true".equals(aloneArr[2])) {
						if (por.after(startDate) && por.before(endDate)) {
							dbf.exeSqls("insert into T_LBSCWHZB (S_ID,S_PID,S_RQ,S_BCBM,S_BZBM,S_SYS_ORDERNUMBER) values ('"
									+ pid + index + "','" + pid + "','" + porStr + "','" + aloneArr[0] + "','" + aloneArr[1]
									+ "','" + index + "');", false);
						}
						index++;
					}

					dataIndex++;
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
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Date now = new Date();
		now = sdf.parse(sdf.format(now));
		Date startDate = TimingTaskTool.getDateBefore(now, 8 - 1);
		Date endDate = TimingTaskTool.getDateAfter(now, 8 - 1);
		System.out.println(sdf.format(startDate));
		System.out.println(sdf.format(endDate));

		Date testDate = sdf.parse("2019-07-29");
		System.out.println(testDate.after(startDate) && testDate.before(endDate));
	}
}
