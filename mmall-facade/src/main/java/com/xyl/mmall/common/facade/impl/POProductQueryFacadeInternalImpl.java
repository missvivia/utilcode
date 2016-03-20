package com.xyl.mmall.common.facade.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.common.facade.POProductQueryFacade;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.service.POProductService;

/**
 * @author dingmingliang
 * 
 */
@Service("poProductQueryFacadeInternalImpl")
public class POProductQueryFacadeInternalImpl implements POProductQueryFacade {

	@Autowired
	private POProductService poProductService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.POProductQueryFacade#getPOSkuDTO(long)
	 */
	public POSkuDTO getPOSkuDTO(long skuId) {
		List<POSkuDTO> dtoList = poProductService.getSkuDTOListBySkuId(Arrays.asList(new Long[] { skuId }));
		return CollectionUtil.getFirstObjectOfCollection(dtoList);
	}

}
