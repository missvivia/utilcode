/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.WarehouseService;

/**
 * WarehouseServiceImpl.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */
@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {

	@Autowired
	private WarehouseDao warehouseDao;

	@Override
	@Cacheable(value="warehouseFormCache")
	public WarehouseForm getWarehouseByArea(long aredId) {
		return warehouseDao.getWarehouseById(aredId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.WarehouseService#getWarehouseById(long)
	 */
	@Override
	@Cacheable(value="warehouseFormCache")
	public WarehouseDTO getWarehouseById(long warehouseId) {
		WarehouseForm form = warehouseDao.getWarehouseById(warehouseId);
		return form == null ? null : new WarehouseDTO(form);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.WarehouseService#getAllWarehouse()
	 */
	@Override
	@Cacheable(value="warehouseFormCache")
	public WarehouseDTO[] getAllWarehouse() {
		List<WarehouseForm> wf = warehouseDao.getList();
		List<WarehouseDTO> wd = new ArrayList<WarehouseDTO>();
		for (WarehouseForm k : wf) {
			wd.add(new WarehouseDTO(k));
		}
		return wd.toArray(new WarehouseDTO[] {});
	}

	@Override
	@Cacheable(value="warehouseFormCache")
	public WarehouseDTO[] getAllWarehouseByIdList(List<Long> areaLists) {
		List<WarehouseForm> wf = warehouseDao.getListByProvinceList(areaLists);
		List<WarehouseDTO> wd = new ArrayList<WarehouseDTO>();
		for (WarehouseForm k : wf) {
			wd.add(new WarehouseDTO(k));
		}
		return wd.toArray(new WarehouseDTO[] {});
	}

}
