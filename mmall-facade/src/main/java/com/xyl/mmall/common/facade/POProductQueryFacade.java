package com.xyl.mmall.common.facade;

import com.xyl.mmall.itemcenter.dto.POSkuDTO;

/**
 * POProduct的查询Facade
 * 
 * @author dingmingliang
 * 
 */
public interface POProductQueryFacade {

	/**
	 * 根据skuId,获取POSkuDTO信息
	 * 
	 * @param skuId
	 * @return
	 */
	public POSkuDTO getPOSkuDTO(long skuId);
}
