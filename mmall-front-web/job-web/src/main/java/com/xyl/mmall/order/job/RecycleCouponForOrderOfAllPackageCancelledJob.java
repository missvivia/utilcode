package com.xyl.mmall.order.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.OrderTimerFacade;

/**
 * 定时回收所有包裹取消的订单的优惠券
 * 
 * @author dingmingliang
 * 
 */
@Service
@JobPath("/order/timer/recycleCoupon")
public class RecycleCouponForOrderOfAllPackageCancelledJob extends BaseJob {
	
	private static final Logger logger = Logger.getLogger(RecycleCouponForOrderOfAllPackageCancelledJob.class);

	@Autowired
	private OrderTimerFacade orderTimerFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	public boolean execute(JobParam param) {
		RetArg retArg = orderTimerFacade.recycleCouponForOrderOfAllPackageCancelled();
		Boolean result = RetArgUtil.get(retArg, Boolean.class);
		String log = RetArgUtil.get(retArg, String.class);
		if(null != result && Boolean.TRUE == result) {
			logger.info(log);
			return true;
		}
		logger.warn(log);
		return false;
	}

}
