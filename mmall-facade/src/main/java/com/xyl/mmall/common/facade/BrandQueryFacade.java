package com.xyl.mmall.common.facade;

import java.util.concurrent.Future;

import com.xyl.mmall.saleschedule.dto.BrandDTO;

/**
 * @author dingmingliang
 *
 */
public interface BrandQueryFacade {

	/**
	 * 根据brandId,获得BrandDTO
	 * 
	 * @param brandId
	 * @return
	 */
	public BrandDTO getBrandByBrandId(long brandId);
	
	public Future<BrandDTO> getBrandByBrandIdAsync(long brandId);
}
