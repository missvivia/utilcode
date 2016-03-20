package com.xyl.mmall.cart.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.backend.facade.CartCleanCacheFacade;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.promotion.utils.DateUtils;

@Service
@JobPath("/cart/clean")
public class CartAreaCleanJob extends BaseJob {
	
	@Autowired
	private CartCleanCacheFacade cartCleanCacheFacade;
	
	private static final Logger logger=LoggerFactory.getLogger(CartAreaCleanJob.class);

	@Override
	public boolean execute(JobParam param) {
		
		String timeStr=DateUtils.parseLongToString(DateUtils.DATE_TIME_FORMAT, param.getCommonParam().getTimestamp());
		
		logger.info("try to clean cart area,time:"+timeStr);
		
		//toDo
		int areaId = -1;
		boolean distributeProcess = false;

		// 1.get the point
		int[] pointArr = cartCleanCacheFacade.getPositionShouldProcessedByCurrentJob(areaId);
		if (pointArr == null || pointArr.length != 2) {
			logger.error("error init the point record for cart clean");
			return false;
		}

		// 2.update the point
		boolean updateflag = cartCleanCacheFacade.setUpPoint(areaId, pointArr[1], pointArr[0]);
		if (!updateflag) {
			logger.error("cannot update the point at method:beginClean,areaId:" + areaId + ",distributeProcess:"
					+ distributeProcess);
			return false;
		}

		// 3.begin clean the cache
		boolean cleanFlag = cartCleanCacheFacade.cleanOverTimeCartForJob(areaId, pointArr[1]);

		// 4.finally update the status for current point
		boolean updateStatusFlag = cartCleanCacheFacade.pointFlagToSuccessOrFail(cleanFlag, areaId,
				pointArr[1], pointArr[1]);

		if (!updateStatusFlag) {
			logger.error("cannot update the status for current point when finishing clean cache for cart");
			return false;
		}

		return true;
	}

}
