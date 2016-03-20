package com.xyl.mmall.common.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xyl.mmall.common.facade.POProductQueryFacade;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;

/**
 * POProductQueryFacade实现的X小时缓存版本
 * 
 * @author dingmingliang
 * 
 */
@Service("poProductQueryFacadeXHCacheImpl")
public class POProductQueryFacadeXHCacheImpl implements POProductQueryFacade {

	@Resource(name = "poProductQueryFacadeInternalImpl")
	POProductQueryFacadeInternalImpl serviceImpl;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.POProductQueryFacade#getPOSkuDTO(long)
	 */
	public POSkuDTO getPOSkuDTO(long skuId) {
		return serviceImpl.getPOSkuDTO(skuId);
	}
}
