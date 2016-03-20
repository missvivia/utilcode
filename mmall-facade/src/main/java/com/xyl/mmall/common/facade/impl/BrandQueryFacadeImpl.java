package com.xyl.mmall.common.facade.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcContext;
import com.xyl.mmall.common.facade.BrandQueryFacade;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.service.BrandService;

/**
 * @author dingmingliang
 * 
 */
@Service
public class BrandQueryFacadeImpl implements BrandQueryFacade {

	@Autowired
	BrandService brandService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.BrandQueryFacade#getBrandByBrandId(long)
	 */
	public BrandDTO getBrandByBrandId(long brandId) {
		return brandService.getBrandByBrandId(brandId);
	}
	
	public Future<BrandDTO> getBrandByBrandIdAsync(final long brandId) {
		return RpcContext.getContext().asyncCall(new Callable<BrandDTO>() {
			@Override
			public BrandDTO call() throws Exception {
				// TODO Auto-generated method stub
				return brandService.getBrandByBrandId(brandId);
			}
			
		});
//		return brandService.getBrandByBrandIdAsync(brandId);
	}

}
