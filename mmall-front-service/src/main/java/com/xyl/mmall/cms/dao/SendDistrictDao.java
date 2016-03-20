package com.xyl.mmall.cms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.cms.meta.SendDistrict;

public interface SendDistrictDao extends AbstractDao<SendDistrict>{
	
	/**
	 * 根据商家Id获取配送区域列表
	 * @param businessId
	 * @return
	 */
	public List<SendDistrict> getDistrictsByBusinessId(long businessId);
	
	/**
	 * 根据商家Ids获取配送区域列表
	 * @param businessId
	 * @return
	 */
	public List<SendDistrict> getDistrictListByBusinessIds(List<Long> businessIds);
	
	
	/**
	 * 根据区IDs获取配送区域列表
	 * @param districtIds
	 * @return
	 */
	public List<SendDistrict> getDistrictListByDistrictIds(List<Long> districtIds);
	
	/**
	 * 根據商家ID刪除配送区域
	 * @param businessId
	 * @return
	 */
	public boolean deleteByBusinessId(long businessId);
	
	
	/**
	 * 根據商家IDs批量刪除配送区域
	 * @param businessIds
	 * @return
	 */
	public boolean batchDeleteByBusinessId(List<Long> businessIds);

	/**
	 * 根据区域id获取商家列表
	 * @param areaId
	 * @param areaFlag 1省，2市，3区
	 * @return
	 */
	public List<Long> getBusinessIdByDistrictId(long areaId, int areaFlag);
}
