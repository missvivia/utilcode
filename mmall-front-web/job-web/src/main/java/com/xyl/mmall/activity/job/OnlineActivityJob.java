/**
 * 
 */
package com.xyl.mmall.activity.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;

/**
 * @author hzlihui2014
 *
 */
@JobPath("/onlineActivity")
@Service
public class OnlineActivityJob extends BaseJob {

	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	@Override
	public boolean execute(JobParam param) {
		return onlineActivityFacade.sendOnlineActivitySms();
	}

}
