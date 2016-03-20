/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.FreightTempletDao;
import com.xyl.mmall.oms.dto.Region;
import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.meta.FreightTemplet;
import com.xyl.mmall.oms.service.FreightTempletService;

/**
 * @author hzzengchengyuan
 *
 */
@Service("freightTempletService")
public class FreightTempletServiceImpl implements FreightTempletService {

	@Autowired
	private FreightTempletDao freightTempletDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.FreightTempletService#getFreightTemplet(com.xyl.mmall.oms.enums.WMSExpressCompany,
	 *      com.xyl.mmall.oms.dto.Region,
	 *      com.xyl.mmall.oms.dto.Region)
	 */
	@Override
	@Cacheable(value="omsFreightTempletCache")
	public FreightTemplet getFreightTemplet(WMSExpressCompany expressCompany, Region origin, Region dest) {
		FreightTemplet template = freightTempletDao.getByOriginTargetAndExpressCompany(expressCompany.name(),
				Long.parseLong(origin.getCode()), Long.parseLong(dest.getCode()));
		if (template == null) {
			template = freightTempletDao.getByOriginAnyTargetAndExpressCompany(expressCompany.name(),
					Long.parseLong(origin.getCode()));
		}
		if (template == null) {
			throw new ServiceException("没有找到运费模板，参数：".concat(expressCompany.getDesc()).concat(",")
					.concat(origin.toString()).concat("->").concat(dest.toString()));
		}
		return template;
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.service.FreightTempletService#isEducationDistrict(com.xyl.mmall.framework.enums.ExpressCompany, long, long)
	 */
	@Override
	@Cacheable(value="omsEducationCache")
	public boolean isEducationDistrict(ExpressCompany expressCompany, long districtId, long cityId) {
		return freightTempletDao.isEducationDistrict(expressCompany, districtId, cityId);
	}

}
