package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sis.util.TJSisData;
import com.timing.util.MisLogger;
import com.yulongtao.web.event.Event;

public class SisDataTiming extends Event {
	private static MisLogger logger = new MisLogger(SisDataTiming.class);
	static String _getSisDate = "";
	MantraUtil tool = null;
	SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
	int index = 0;

	@Override
	public boolean isRun() {
		logger.debug("[:019]->SisDataTiming->isRun:");
		boolean isRun = false; // 判断是否运行
		if (index == 60) {
			tool = new MantraUtil(); // 工具类
			String strNowDate = sdf_ymd.format(new Date()); // 当前时间
			if("".equals(_getSisDate)) {  //初始化
				_getSisDate=strNowDate;
			}else if(strNowDate.equals(_getSisDate)){  //相等
				logger.debug("[:019]->SisDataTiming->isRun:"+strNowDate);
			}else {	//不相等
				int dataRow = tool.getDateCont("t_sis_date_day", "DATETIME='" + _getSisDate + "'"); // 判断数据库是否有当天得数据
				if (dataRow == 0) {
					isRun = true;
				}
			}
			index = 0;
		} else {
			index++;
		}

		return isRun;
	}

	@Override
	public void run() {
		TJSisData sisTool = new TJSisData();
		boolean retBool = sisTool.obtainData(_getSisDate); // 处理当天日期中得内容
		if (retBool = true) {
			_getSisDate = sdf_ymd.format(new Date());
		}
	}
}
