/**
 * 
 */
package com.timing.csd;

import com.timing.impcl.MantraLog;
import com.timing.util.MisLogger;
import com.yulongtao.web.event.Event;

/**
 * Clean the SIS table data every day.
 * 
 * @author tianshisheng
 *
 */
public class EventCleanSisData extends Event {
	private static MisLogger logger = new MisLogger(EventCleanSisData.class);

	@Override
	public boolean isRun() {

		logger.debug("EventCleanSisData.run()");
		return true;
	}

	@Override
	public void run() {
		logger.debug("EventCleanSisData.run()");
		//clean the sis table data
	}

}
