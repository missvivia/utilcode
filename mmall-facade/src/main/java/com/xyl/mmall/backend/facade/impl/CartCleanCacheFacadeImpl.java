package com.xyl.mmall.backend.facade.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.facade.CartCleanCacheFacade;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cart.service.CartInventoryCleanerService;
import com.xyl.mmall.framework.annotation.Facade;

@Facade
public class CartCleanCacheFacadeImpl implements CartCleanCacheFacade {
	@Autowired
	private CartInventoryCleanerService cartInventoryCleanerService;

	@Override
	public int[] getPositionShouldProcessedByCurrentJob(int areaId) {
		return cartInventoryCleanerService.getPositionShouldProcessedByCurrentJob(areaId);
	}

	@Override
	public boolean setUpPoint(int areaId, int posProcessedByCurJob, int oldPoint) {
		return cartInventoryCleanerService.setUpPoint(areaId, posProcessedByCurJob, oldPoint);
	}

	@Override
	public boolean cleanOverTimeCartForJob(int areaId, int posProcessedByCurJob) {
		return cartInventoryCleanerService.cleanOverTimeCartForJob(areaId, posProcessedByCurJob);
	}

	@Override
	public boolean pointFlagToSuccessOrFail(boolean cleanSuccessFlag, int areaId, int posProcessedByCurJob, int oldPoint) {
		return cartInventoryCleanerService.pointFlagToSuccessOrFail(cleanSuccessFlag, areaId, posProcessedByCurJob,
				oldPoint);
	}

}
