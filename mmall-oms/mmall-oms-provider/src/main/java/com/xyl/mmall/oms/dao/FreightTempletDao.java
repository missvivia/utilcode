/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.oms.meta.FreightTemplet;

/**
 * @author hzzengchengyuan
 *
 */
public interface FreightTempletDao extends AbstractDao<FreightTemplet> {
	/**
	 * 获取所有运费模板
	 * 
	 * @return
	 */
	List<FreightTemplet> getAll();

	/**
	 * @param expressCompany
	 * @param origin
	 * @param target
	 * @return
	 */
	FreightTemplet getByOriginTargetAndExpressCompany(String expressCompany, long origin, long target);
	
	/**
	 * @param expressCompany
	 * @param origin
	 * @return
	 */
	FreightTemplet getByOriginAnyTargetAndExpressCompany(String expressCompany, long origin);
	
	/**
	 * 判断三级行政区（县区）是否为偏远地区
	 * @param expressCompany
	 * @param districtId 三级行政区id
	 * @param cityId 三级行政区的上级行政区（二级行政区）Id
	 * @return
	 */
	boolean isEducationDistrict(ExpressCompany expressCompany, long districtId, long cityId);
}
