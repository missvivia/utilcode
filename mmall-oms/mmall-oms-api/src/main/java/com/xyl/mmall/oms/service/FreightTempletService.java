/**
 * 
 */
package com.xyl.mmall.oms.service;

import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.oms.dto.Region;
import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.meta.FreightTemplet;

/**
 * @author hzzengchengyuan
 *
 */
public interface FreightTempletService {

	/**
	 * 根据物流方和包裹的起始地、目的地获取运费模板
	 * 
	 * @param expressCompany
	 * @param origin
	 * @param dest
	 * @return
	 */
	FreightTemplet getFreightTemplet(WMSExpressCompany expressCompany, Region origin, Region dest);

	/**
	 * 判断三级行政区（县区）是否为偏远地区
	 * 
	 * @param expressCompany
	 * @param districtId
	 *            三级行政区id
	 * @param cityId
	 *            三级行政区的上级行政区（二级行政区）Id
	 * @return
	 */
	boolean isEducationDistrict(ExpressCompany expressCompany, long districtId, long cityId);
}
