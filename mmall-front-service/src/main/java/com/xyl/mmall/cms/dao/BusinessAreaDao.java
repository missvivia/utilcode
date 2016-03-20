package com.xyl.mmall.cms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.meta.BusinessArea;

/**
 * 
 * @author hzliujie
 * 2014年12月2日 下午3:52:02
 */
public interface BusinessAreaDao extends AbstractDao<BusinessArea> {
	

	/**
	 * 获取所有站点信息表
	 * 
	 * @param 
	 * @return
	 */
	public List<BusinessArea> getAreaList();

	/**
	 * 根据id站点商家信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<BusinessArea> getAreaByAreaId(long areaId);
	
	/**
	 * 根据商家获取商家经营站点列表
	 * @param supplierId
	 * @return
	 */
	public List<BusinessArea> getAreaBySupplierId(long supplierId);
	
	/**
	 * 获取站点和帐号下商家总数
	 * @param areaId
	 * @param businessAccount
	 * @return
	 */
	public int getTotal(long areaId, String businessAccount );

	/**
	 * 
	 * @param areaId
	 * @param businessAccount
	 * @param ddbParam
	 * @return
	 */
	public List<BusinessArea> getBusinessListByProvinceOrAccount(long areaId, String businessAccount, DDBParam ddbParam);
	
	/**
	 * 
	 * @param areaId
	 * @return
	 */
	public boolean deleteBusiness(long supplierId);
	
	/**
	 * 某个品牌某个站点下是否存在
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public BusinessArea getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type);
	
	/**
	 * 根据地区号列表获取商家列表
	 * @param areaIdList
	 * @return
	 */
	public List<BusinessArea> getBusinessAreaListByAreaIdList(List<Long> areaIdList);

	/**
	 * 根据品牌和站点查询商家列表
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public List<BusinessArea> getBusinessByAreaIdAndBrandId(Long areaId, Long brandId);
	
	/**
	 * 
	 * @param brandId
	 * @return
	 */
	public List<BusinessArea> getBusinessByBrandId(Long brandId);
	
	
}
