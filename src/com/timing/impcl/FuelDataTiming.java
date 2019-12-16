/**
 * 
 */
package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timing.util.AES256Util;
import com.timing.util.FuelDataTimingDao;
import com.timing.util.MisLogger;
import com.timing.util.TimingTaskTool;
import com.url.urlUtill.httpCon;

/**
 * @author tianshisheng
 *
 */
public class FuelDataTiming {
	private static MisLogger logger = new MisLogger(FuelDataTiming.class);
	private static final String SERVER_ADDRESS = "http://172.168.3.3:8078";
	
	private static final String PARAM_USERCODE = "UserCode";
	private static final String PARAM_PASSWORD = "Password";
	private static final String PARAM_VALIDTIMELENGTH = "ValidTimeLength";

	private static final String PARAM_STARTTIME = "StartTime";
	private static final String PARAM_ENDTIME = "EndTime";
	private static final String PARAM_MINENO = "MineNo";

	private static final String PARAM_TIMESTAMP = "timestamp";
	private static final String PARAM_SIGNATURE = "signature";
	
	private static final String VALUE_USERCODE = "taiji";
	private static final String VALUE_PASSWORD = "test1234";
	
	private static final String DATE_TIME = " 00:00:00";
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	final ObjectMapper mapper = new ObjectMapper();
	
	public void fetchMineralData() {
		try {
			List<Map<String, Object>> result = FuelDataTimingDao.fatchMinerals();
			FuelDataTimingDao.updateMinerals(result);
		} catch (Exception e) {
			logger.debug("FuelDataTiming.fetchMineralData -> Fail fetch Mineral data.");
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
	}
	
	public void fetchMineralDataByWS() {
		try {
			httpCon httpConnection = new httpCon();
			Map<String, String> parameterMap = new HashMap<>();
			String result = httpConnection.doPost(SERVER_ADDRESS + "/api/MineralData", parameterMap);
		} catch (Exception e) {
			logger.debug("FuelDataTiming.fetchMineralDataByWS -> Fail fetch Mineral data.");
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void fetchFuelData() {
		try {
			Date dateTo = new Date();
			Date dateFrom = TimingTaskTool.getDateBefore(dateTo, 100); //7
			List<Map<String, Object>> result = FuelDataTimingDao.fatchLabdataByDate(dateFrom, dateTo);
			
			if (result != null && result.size() > 0) {
				FuelDataTimingDao.addLabdata(result);
			}
		} catch (Exception e) {
			logger.debug("FuelDataTiming.fetchFuelData -> Fail fetch fuel data.");
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void fetchFuelDataByWS() {
		try {
			httpCon httpConnection = new httpCon();
			Map<String, String> parameterMap = new HashMap<>();
			parameterMap.put(PARAM_USERCODE, VALUE_USERCODE);
			parameterMap.put(PARAM_PASSWORD, VALUE_PASSWORD);
			parameterMap.put(PARAM_VALIDTIMELENGTH, "600");
			String result = httpConnection.doPost(SERVER_ADDRESS + "/api/token", parameterMap);

			JsonNode node = mapper.readTree(result);
			String token = node.get("Data").get("Token").asText();
//			System.out.println("Token:" + token);

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(PARAM_USERCODE, VALUE_USERCODE);
			long milli = System.currentTimeMillis() + 8 * 3600 * 1000;
			long ticks = (milli * 10000) + 621355968000000000L;
			headerMap.put(PARAM_TIMESTAMP, String.valueOf(ticks));

			parameterMap.clear();
			Date endDate = new Date();
			Date startDate = TimingTaskTool.getDateBefore(endDate, 70); //7
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			
			parameterMap.put(PARAM_STARTTIME, sdf.format(startDate) + DATE_TIME); // yyyy-MM-dd HH:mm:ss
			parameterMap.put(PARAM_ENDTIME, sdf.format(endDate) + DATE_TIME); // yyyy-MM-dd HH:mm:ss
			
			//获取矿别，循环获取检测结果
			String[] mineNos = FuelDataTimingDao.findMineNos();
			if (mineNos != null) {
				for (int i = 0; i < mineNos.length; i++) {
					try {
						parameterMap.put(PARAM_MINENO, mineNos[i].trim());

						String parameterJson = mapper.writeValueAsString(parameterMap);
						String signatureStr = headerMap.get(PARAM_USERCODE) + headerMap.get(PARAM_TIMESTAMP) + parameterJson;
//						System.out.println("signatureStr:" + signatureStr);
						byte[] dataBytes = signatureStr.getBytes("utf-8");
						byte[] encode = AES256Util.cryptData(token, dataBytes, true);
						headerMap.put(PARAM_SIGNATURE, AES256Util.parseByte2HexStr(encode));

						result = httpConnection.doPost(SERVER_ADDRESS + "/api/labdata", headerMap, parameterMap);
//						System.out.println("test data:" + result);

						node = mapper.readTree(result);
						JsonNode data = node.get("Data");
						if (data.isArray()) {
							for (JsonNode objNode : data) {
								logger.debug(">>>>>>>>>>>>" + objNode.get("TestNo").asText());
						    }
						}
					} catch (Exception e) {
						MantraLog.fileCreateAndWrite(e);
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			logger.debug("FuelDataTiming.fetchFuelDataByWS -> Fail fetch fuel data.");
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FuelDataTiming fuelDataTiming = new FuelDataTiming();
//		fuelDataTiming.fetchMineralData();
		fuelDataTiming.fetchFuelData();
	}
}
