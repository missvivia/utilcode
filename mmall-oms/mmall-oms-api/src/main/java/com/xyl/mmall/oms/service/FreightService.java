package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.FreightCodDTO;
import com.xyl.mmall.oms.dto.FreightDTO;
import com.xyl.mmall.oms.dto.FreightReverseDTO;
import com.xyl.mmall.oms.dto.FreightUserReturnDTO;

/**
 * 运费计算服务
 * 
 * @author hzzengchengyuan
 *
 */
public interface FreightService {

	/**
	 * 当包裹由客服设置为丢件时，被上层模块调用
	 * 
	 * @param expressCompany
	 * @param expressNo
	 * @return
	 */
	boolean onLostPackagee(String expressCompany, String expressNo);
	
	
	/**
	 * 计算被缓存起来的订单运费
	 * @return
	 */
	boolean doCalcuateCache();

	/**
	 * 根据快递公司、仓库站点和发货时间范围查询正向运费
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightDTO> queryFreight(String expressCompany, long warehouseId, long startTime, long endTime);
	
	/**
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightCodDTO> queryFreightCod(String expressCompany, long warehouseId, long startTime, long endTime);
	
	/**
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightReverseDTO> queryFreightReverse(String expressCompany, long warehouseId, long startTime, long endTime);
	
	/**
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightUserReturnDTO> queryFreightUserReturn(String expressCompany, long warehouseId, long startTime, long endTime);
}
