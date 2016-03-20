/**
 * 
 */
package com.xyl.mmall.backend.facade.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.xyl.mmall.backend.facade.OmsFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.oms.service.OmsOutsideService;

/**
 * @author hzzengdan
 *
 */
@Facade
public class OmsFacadeImpl implements OmsFacade {

	@Resource
	private OmsOutsideService omsService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.OmsFacade#httpWarehouseCaller(java.lang.String,
	 *      java.util.Map, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Object httpWarehouseCaller(String warehouseCode, Map<String, Object> params) {
		return omsService.httpWarehouseCaller(warehouseCode, params);
	}

}
