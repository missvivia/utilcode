package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.meta.WarehouseForm;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseService {
	/**
	 * 数据表结构调整，仓库属性中没有了area属性，原area属性迁移为仓库id，请调用{@link #getWarehouseById(long)}
	 * @param warehouseId
	 * @return
	 */
	@Deprecated
	public WarehouseForm getWarehouseByArea(long warehouseId);
	
	/**
	 * @param warehouseId
	 * @return
	 */
	public WarehouseDTO getWarehouseById(long warehouseId);
	
	/**
	 * 获取所有仓库列表
	 * @return
	 */
	public WarehouseDTO[] getAllWarehouse();
	
	/**
	 * 获取所有仓库列表
	 * @return
	 */
	public WarehouseDTO[] getAllWarehouseByIdList(List<Long> areaLists);
}
