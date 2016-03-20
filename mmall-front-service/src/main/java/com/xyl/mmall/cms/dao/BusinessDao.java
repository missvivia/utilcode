package com.xyl.mmall.cms.dao;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.enums.ActiveState;
import com.xyl.mmall.cms.meta.Business;

/**
 * 
 * @author hzchaizhf
 * @create 2014年9月15日
 *
 */
public interface BusinessDao extends AbstractDao<Business> {

	/**
	 * 删除商家信息
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteBusiness(long id);
	
	/**
	 * 批量删除商家信息
	 * 
	 * @param id
	 * @return
	 */
	public boolean batchDeleteBusiness(List<Long> ids);

	/**
	 * 根据省份，站点获取商家信息列表
	 * 
	 * @param areaId
	 * @param businessAccount
	 * @param ddbParam
	 * @return
	 */
	@Deprecated
	public List<Business> getBusinessListByProvinceOrAccount(long areaId, String businessAccount, DDBParam ddbParam);

	/**
	 * 根据type,typeId获取所在站点商家信息表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Business> getBusinessListByTypeId(int type, long typeId, DDBParam ddbParam);

	/**
	 * 更新收商家信息
	 * 
	 * @param Business
	 * @return
	 */
	public boolean updateBusiness(Business Business);

	/**
	 * 根据id获取商家信息
	 * 
	 * @param id
	 * @param isActive -1不加查选条件，0激活，1未激活
	 * @return
	 */
	public Business getBusinessById(long id, int isActive);

	/**
	 * 根据商家信息总数
	 * 
	 * @param type
	 * @return
	 */
	public int getTotal(long areaId, String businessAccount);

	/**
	 * 根据registrationNumber获取所在站点商家信息表
	 * 
	 * @param registrationNumber
	 * @return
	 */
	public List<Business> getBusinessListByRegistrationNumber(String registrationNumber);

	/**
	 * 根据商家账号获取商家信息表
	 * 
	 * @param registrationNumber
	 * @return
	 */
	public List<Business> getBusinessListByBusinessAccount(String businessAccount);

	/**
	 * 根据地区号获取商家列表
	 * 
	 * @param areaId
	 * @return
	 * @throws Exception
	 */
	public List<Business> getBusinessListByAreaId(long areaId) throws Exception;

	/**
	 * 根据地区号列表获取商家列表
	 * 
	 * @param areaIdList
	 * @return
	 */
	@Deprecated
	public List<Business> getBusinessListByAreaIdList(List<Long> areaIdList, long areaCode);

	/**
	 * 根据商家帐号获取商家信息
	 * 
	 * @param businessAccount
	 * @return
	 */
	public Business getBusinessAccount(String businessAccount);

	/**
	 * 根据公司名称获取公司信息
	 * 
	 * @param companyName
	 * @return
	 */
	public Business getBusinessByCompanyName(String companyName);

	/**
	 * 根据站点id和品牌id获取商家信息
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId) throws Exception;

	/**
	 * 更新帐号激活状态
	 * 
	 * @param businessid
	 * @param userid
	 * @param state
	 * @return
	 */
	public boolean updateActiveByUserId(long businessid,long userid, ActiveState state);

	/**
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public List<Business> getBusinessByAreaIdOrBrandIdOrAccount(Long areaId, Long brandId);

	/**
	 * 根据商家帐号获取商家信息
	 * 
	 * @param businessAccount
	 * @return
	 */
	public List<Business> getBusinessByAccount(String businessAccount);

	/**
	 * 根据营业执照编号获取商家信息
	 * 
	 * @param registrationNumber
	 * @return
	 */
	public Business getBusinessByRegNum(String registrationNumber);

	/**
	 * 根据id列表获取商家信息
	 * 
	 * @param idList
	 * @return
	 */
	public List<Business> getBusinessListByIdList(List<Long> idList);

	/**
	 * 获取某个品牌下激活的商家帐号
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public List<Business> getActiveBusinessByBrand(Long brandId);

	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type);

	/**
	 * 根据品牌id和商家类型获取商家信息.
	 * 
	 * @param brandId
	 * @param type
	 *            商家类型.
	 * @return
	 */
	public Business getBusinessByBrandIdAndType(Long brandId, int type);

	public List<Business> getBusinessByBrandId(Long brandId);
	
	/**
	 * 根据搜索条件站点,帐号,公司名称，商家ID获取所在站点商家信息表
	 * @param businessConditionDTO
	 * @return
	 */
	public List<Business> getBusinessListByBusinessCondition(
			BusinessConditionDTO businessConditionDTO);
	
	/**
	 * 更新起批金额
	 * @param businessid
	 * @param userid
	 * @param batchCash
	 * @param isOpen
	 * @return
	 */
	public boolean updateBatchCash(long businessid,long userid, BigDecimal batchCash,boolean isOpen);
}
