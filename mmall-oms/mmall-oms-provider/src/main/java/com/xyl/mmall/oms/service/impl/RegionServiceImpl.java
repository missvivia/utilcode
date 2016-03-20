/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.ip.enums.LocationLevel;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.oms.dto.RegioLevel;
import com.xyl.mmall.oms.dto.Region;
import com.xyl.mmall.oms.service.RegionService;

/**
 * @author hzzengchengyuan
 *
 */
@Service("regionService")
public class RegionServiceImpl implements RegionService {

	@Autowired
	private LocationService locationService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.RegionService#getThreeRegionByCode(java.lang.String)
	 */
	@Override
	@Cacheable(value = "omsRegionCache")
	public Region getThreeRegionByCode(String code) {
		Region three = getSection(code);
		Region two = getCity(three.getParentCode());
		three.setParent(two);
		Region one = getProvince(two.getParentCode());
		two.setParent(one);
		return three;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.RegionService#getRegion(java.lang.String)
	 */
	@Override
	@Cacheable(value = "omsRegionCache")
	public Region getRegion(String code) {
		LocationCode l = locationService.getLocationCode(Long.parseLong(code));
		if (l == null) {
			throw new ServiceException("未找到行政区:" + code);
		}
		return new Region(String.valueOf(l.getCode()), l.getLocationName(), convertRegioLevel(l.getLevel()),
				String.valueOf(l.getParentCode()));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.RegionService#getProvince(java.lang.String)
	 */
	@Override
	@Cacheable(value = "omsRegionCache")
	public Region getProvince(String code) {
		Region r = getRegion(code);
		if (r.getLevel() != RegioLevel.ONE) {
			throw new ServiceException("该行政区[" + r + "]不是:" + RegioLevel.ONE.getName());
		}
		return r;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.RegionService#getCity(java.lang.String)
	 */
	@Override
	@Cacheable(value = "omsRegionCache")
	public Region getCity(String code) {
		Region r = getRegion(code);
		if (r.getLevel() != RegioLevel.TWO) {
			throw new ServiceException("该行政区[" + r + "]不是:" + RegioLevel.TWO.getName());
		}
		return r;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.RegionService#getSection(java.lang.String)
	 */
	@Override
	@Cacheable(value = "omsRegionCache")
	public Region getSection(String code) {
		Region r = getRegion(code);
		if (r.getLevel() != RegioLevel.THREE) {
			throw new ServiceException("该行政区[" + r + "]不是:" + RegioLevel.THREE.getName());
		}
		return r;
	}

	private static RegioLevel convertRegioLevel(LocationLevel level) {
		switch (level) {
		case LEVEL_PROVICE:
			return RegioLevel.ONE;
		case LEVEL_CITY:
			return RegioLevel.TWO;
		case LEVEL_DISTRICT:
			return RegioLevel.THREE;
		case LEVEL_STREET:
			return RegioLevel.FOUR;
		default:
			throw new IllegalArgumentException("额...不会到达这里的吧.");
		}
	}

}
