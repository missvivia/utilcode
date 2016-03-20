package com.xyl.mmall.mobile.web.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.mobile.web.facade.MobileBusinessFacade;

/**
 * 
 * 
 * @author wangfeng
 *
 */
@Facade("mobileBusinessFacade")
public class MobileBusinessFacadeImpl implements MobileBusinessFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileBusinessFacadeImpl.class);

	@Autowired
	private BusinessService businessService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainSiteBusinessFacade#getBusinessByAreaIdAndBrandId(java.lang.Long,
	 *      java.lang.Long, int)
	 */
	@Override
	public BusinessDTO getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type) {
		BusinessDTO businessDTO = businessService.getBusinessByBrandIdAndType(brandId, type);
		if (businessDTO == null)
			return null;

		try {
			long areaBitMap = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			boolean isPermitted = (areaBitMap & businessDTO.getSiteId()) == areaBitMap;
			if (!isPermitted)
				return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return businessDTO;
	}

}
