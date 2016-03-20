package com.xyl.mmall.cms.facade;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.vo.BusinessConditionAO;
import com.xyl.mmall.cms.vo.PagerContainer;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;

/**
 * 商家信息服务Facade
 * 
 * @author hzchaizhf 
 * @create 2014年9月16日
 * 
 */
public interface BusinessFacade {
	
	/**
	 * 根据类型获取所在站点商家信息表含分页
	 * 
	 * @param areaId
	 * @return
	 */
	public PagerContainer getBusinessListByAreaIdAndAccount(BusinessConditionAO conditionAO,DDBParam ddbParam);
	
	/**
	 * 根据搜索条件站点,帐号,公司名称，商家ID获取所在站点商家信息表
	 * 
	 * @param businessConditionDTO
	 * @param ddbParam
	 * @return
	 */
	public PagerContainer getBusinessListByBusinessCondition(BusinessConditionDTO businessConditionDTO);
	
	
	/**
	 * 根据用户名称模糊搜索
	 * @param businessConditionDTO
	 * @return
	 */
	public PagerContainer getUserListByUserName(UserProfileConditionDTO userProfileConditionDTO);
	
	
	/**
	 * 添加商家信息
	 * 
	 * @param businessDTO
	 * @return
	 */
	public BusinessDTO addBusiness(BusinessDTO businessDTO) throws Exception;

	/**
	 * 删除商家信息
	 * 
	 * @param id
	 * @return
	 */
	public int deleteBusiness(long id,long operator);
	
	/**
	 * 批量删除商家信息
	 * @param ids
	 * @return
	 */
	public boolean batchDeleteBusiness(List<Long> ids) ;
	
	/**
	 * 更新收商家信息
	 * 
	 * @param vusiness
	 * @return
	 * @throws Exception 
	 */
	public int updateBusiness(BusinessDTO businessDTO) throws Exception;


	/**
	 * 根据id获取商家信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public BusinessDTO getBusinessById(long id);
	
	/**
	 * 根据registrationNumber获取所在站点商家信息表
	 * 
	 * @param registrationNumber
	 * @return
	 */
	public boolean existsRegistrationNumber(String registrationNumber);
	
	/**
	 * 获取所有站点信息表
	 * 
	 * @param 
	 * @return
	 */
	public List<AreaDTO> getAreaList();
	
	/**
	 * 根据id获取商家区域信息
	 * 
	 * @param id
	 * @return
	 */
	public AreaDTO getAreaById(long id);
	
	/**
	 * 判断商家信息是否存在
	 * 
	 * @param businessAccount
	 * @return
	 */
	public boolean existsBusinessAccount(String businessAccount);
	
	/**
	 * 根据id列表获取站点列表信息
	 * @param idList
	 * @return
	 */
	public List<AreaDTO> getAreadByIdList(List<Long> idList);
	
	/**
	 * 根据站点Ids列表获取站点列表区域信息
	 * @param siteIds
	 * @return
	 */
	public List<SiteAreaDTO> getSiteAreaBySiteIdList(List<Long> siteIds);
	
	
	/**
	 * 根据id列表获取区站点列表信息
	 * @param idList
	 * @return
	 */
	public List<SendDistrictDTO> getDistrictByIdList(List<Long> idList);
	
	
	
	/**
	 * 根据站id列表获取站点列表信息 该接口过期，目前一个站点一个品牌可能存在两个商家，代理商和品牌商 改用getBusinessDTOByAreaIdAndBrandId
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	@Deprecated
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId);
	
	
	/**
	 * 冻结某个账户
	 * @param userId
	 * @param id
	 */
	public boolean lockBusinessAccount(long userId, long id);
	
	/**
	 * 解冻某个账户
	 * @param userId
	 * @param id
	 */
	public void unlockBusinessAccount(long userId,long id);
	
	
	/**
	 * 模糊查询给张辉用 改用getBusinessDTOByName
	 * @param businessAccount
	 * @return
	 */
	public List<Business> getBusinessByAccount(String businessAccount);
	
	/**
	 * 判断站点下是否存在激活用户
	 * @param brandId
	 * @return
	 */
	public boolean existsActiveBusinessByBrand(Long brandId);
	
	/**
	 * 根据帐号查询商家信息
	 * @param businessAccount
	 * @return
	 */
	public BusinessDTO getBusinessDTOByName(String businessAccount);


	/**
	 * 根据站点，品牌以及商家类型(代理商还是品牌商)查询,结果最多一个
	 * @param areaId
	 * @param brandId
	 * @param type
	 * @return
	 */
	public BusinessDTO getBusinessByAreaIdAndBrandId(Long areaId, Long brandId,int type);


	/**
	 * 根据站点，品牌以及商家类型(代理商还是品牌商)查询,结果最多一个
	 * @param areaId
	 * @param brandId
	 * @param type
	 * @return
	 */
	public BusinessDTO getBusinessByAreaBitMapAndBrandId(Long areaBitMap, Long brandId,int type);


	
	
	/**
	 * 是否存在品牌以及商家类型(代理商还是品牌商)查询,结果最多一个
	 * @param areaId
	 * @param brandId
	 * @param type
	 * @return
	 */
	boolean existsAreaIdAndBrandId(Long areaId, long brandId, int type);

	/**
	 * 获取某个站点下商家数据
	 * @param areaId
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOListByAreaId(long areaId);

	/**
	 * 获取站点列表下的商家信息
	 * @param areaIdList
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOListByAreaIdList(List<Long> areaIdList);


	/**
	 * 根据站点和品牌获取商家列表信息
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOByAreaIdAndBrandId(Long areaId, Long brandId);
	
	/**
	 * 
	 * @param brandId
	 * @return
	 */
	public List<Business> existBusinessOfBrandId(long brandId);
	
	/**
	 * 
	 * @param areaIdList
	 * @param areaId
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOListByAreaIdListAndAreaId(List<Long> areaIdList,long areaId);
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOByIdList(List<Long> ids);
	
	/**
	 * 根据brandId获取商家列表
	 * @param brandId
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOByBrandId(long brandId);
	
	/**
	 * 根据areaId获取店铺数量
	 * @param areaId
	 * @return
	 */
	public int getBusinessCountByAreaId(long areaId);
	
	/**
	 * 更新起批金额
	 * @param businessid
	 * @param userid
	 * @param batchCash
	 * @param isOpen
	 * @return
	 */
	public boolean updateBatchCash(long businessid,long userid, BigDecimal batchCash,boolean isOpen);
	
	
	/**
	 * 根据商家Id和用户名分页获取指定用户列表
	 * @param businessId
	 * @param userName
	 * @param ddbparam
	 * @return
	 */
	public PagerContainer getPageBusiUserRelationBybusinessId(long businessId,String userName,DDBParam ddbparam);
	
	/**
	 * 获取商家简略信息
	 * @param businessId
	 * @param isActive
	 * @return
	 */
	public BusinessDTO getBreifBusinessById(long businessId, int isActive);
}
