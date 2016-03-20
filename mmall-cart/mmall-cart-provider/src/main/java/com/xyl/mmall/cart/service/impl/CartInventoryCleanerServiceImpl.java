package com.xyl.mmall.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.cart.dao.CartDao;
import com.xyl.mmall.cart.service.CartInventoryCleanerService;

@Service
public class CartInventoryCleanerServiceImpl implements CartInventoryCleanerService {

	@Autowired
	private CartCleanCacheOperInf cartCleanCacheOperInf;

	@Autowired
	private CartDao cartDao;
	
	public boolean cleanOverTimeCartForJob(int areaId, int posProcessedByCurJob) {
		return cartCleanCacheOperInf.cleanOverTimeCartForJob(areaId, posProcessedByCurJob);
	}

	public boolean setUpPoint(int areaId, int posProcessedByCurJob, int oldPoint) {
		return cartCleanCacheOperInf.setUpPoint(areaId, posProcessedByCurJob, oldPoint);
	}

	public boolean pointFlagToSuccessOrFail(boolean cleanSuccessFlag, int areaId, int posProcessedByCurJob, int oldPoint) {
		return cartCleanCacheOperInf.pointFlagToSuccessOrFail(cleanSuccessFlag, areaId, posProcessedByCurJob, oldPoint);
	}

	public int[] getPositionShouldProcessedByCurrentJob(int areaId) {
		return cartCleanCacheOperInf.getPositionShouldProcessedByCurrentJob(areaId);
	}

}
