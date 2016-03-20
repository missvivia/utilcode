/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.PoOrderFormDao;
import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.service.PickOrderService;
import com.xyl.mmall.oms.service.PickSkuService;

/**
 * PickSkuServiceImpl.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */

@Service("pickSkuService")
public class PickSkuServiceImpl implements PickSkuService {

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private PoOrderFormDao poOrderDao;

	@Autowired
	private PickOrderService pickOrderService;

	@Override
	public List<PickSkuDTO> getPickSkuDetail(String pickId, long supplierId) {
		List<PickSkuItemForm> psif = pickSkuDao.getPickSkuList(pickId, supplierId);
		List<PickSkuDTO> psd = new ArrayList<PickSkuDTO>();
		for (PickSkuItemForm k : psif) {
			psd.add(new PickSkuDTO(k));
		}
		return psd;
	}

	@Override
	public List<PickSkuItemForm> getPickSkuListByPoOrder(String poOrderId, String pickOrderId, String pickStartTime,
			String pickEndTime) {
		return pickSkuDao.getPickSkuListByPoOrder(poOrderId, pickOrderId, pickStartTime, pickEndTime);
	}

	@Override
	public List<PickSkuItemForm> getPickSkuByPoOrderId(String poOrderId, long supplierId) {
		return pickSkuDao.getPickSkuByPoOrderId(poOrderId, supplierId);
	}

	@Override
	public List<PickSkuItemForm> getUnPickSkuByPoOrderId(String poOrderId) {
		return pickSkuDao.getUnPickSkuByPoOrderId(poOrderId);
	}

	@Override
	public List<PickSkuItemForm> getUnPickListByCreateTime(long createTime, int limit) {
		return pickSkuDao.getUnPickListByCreateTime(createTime, limit);
	}

	@Override
	public List<PickSkuItemForm> getPickSkuByPoOrderIdAndOriSupplierId(String poOrderId, long supplierId) {
		return pickSkuDao.getPickSkuByPoOrderIdAndOriSupplierId(poOrderId, supplierId);
	}

}
