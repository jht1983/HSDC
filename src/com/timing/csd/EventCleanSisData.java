/**
 * 
 */
package com.timing.csd;

import com.timing.impcl.MantraLog;
import com.yulongtao.web.event.Event;

/**
 * Clean the SIS table data every day.
 * 
 * @author tianshisheng
 *
 */
public class EventCleanSisData extends Event {

	@Override
	public boolean isRun() {

		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCleanSisData.run()");
		return true;
	}

	@Override
	public void run() {
		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCleanSisData.run()");
		//clean the sis table data
	}

}
