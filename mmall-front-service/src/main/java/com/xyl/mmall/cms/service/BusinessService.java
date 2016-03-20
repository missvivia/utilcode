package com.xyl.mmall.cms.service;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusiUserRelationDTO;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.enums.ActiveState;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * 商家信息服务service
 * 
 * @author hzchaizhf
 * @create 2014年9月16日
 * 
 */
public interface BusinessService {

	/**
	 * 根据类型获取所在站点商家信息表
	 * 
	 * @param areaId
	 * @return
	 */
	public List<BusinessDTO> getBusinessListByTypeId(int type, long typeId, DDBParam ddbParam);

	/**
	 * 根据站点和帐号获取所在站点商家信息表
	 * 
	 * @param areaId
	 * @param businessAccount
	 * @param ddbParam
	 * @return
	 */
	public List<BusinessDTO> getBusinessListByProvinceOrAccount(long areaId, String businessAccount, DDBParam ddbParam);

	/**
	 * 根据搜索条件站点,帐号,公司名称，商家ID获取所在站点商家信息表
	 * 
	 * @param areaId
	 * @param businessAccount
	 * @param ddbParam
	 * @return
	 */
	public BasePageParamVO<BusinessDTO> getBusinessListByBusinessCondition(BusinessConditionDTO businessConditionDTO);

	/**
	 * 添加商家信息
	 * 
	 * @param businessDTO
	 * @return
	 */
	public BusinessDTO addBusiness(BusinessDTO businessDTO);

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
	 * 更新收商家信息
	 * 
	 * @param vusiness
	 * @return
	 * @throws Exception
	 */
	public boolean updateBusiness(BusinessDTO businessDTO);

	/**
	 * 根据id获取商家信息
	 * 
	 * @param id
	 * @param isActive
	 *            -1不加查选条件，0激活，1未激活
	 * @return
	 * @throws Exception
	 */
	public BusinessDTO getBusinessById(long id, int isActive);

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
	 * 根据地区号获取商家列表
	 * 
	 * @param areaId
	 * @return
	 */
	public List<Business> getBusinessListByAreaId(long areaId);

	/**
	 * 根据地区号列表获取商家列表
	 * 
	 * @param areaIdList
	 * @return
	 */
	public List<Business> getBusinessListByAreaIdList(List<Long> areaIdList);

	/**
	 * 根据商家帐号获取商家信息
	 * 
	 * @param businessAccount
	 * @return
	 */
	public Business getBusinessAccount(String businessAccount);

	/**
	 * 根据商家名称获取商家信息
	 * 
	 * @param companyName
	 * @return
	 */
	@Deprecated
	public Business getBusinessByCompanyName(String companyName);

	/**
	 * 根据id列表获取站点列表信息
	 * 
	 * @param idList
	 * @return
	 */
	public List<AreaDTO> getAreadByIdList(List<Long> idList);

	/**
	 * 根据站点id和品牌id获取商家信息
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId);

	/**
	 * 根据品牌id和商家类型获取商家信息.
	 * 
	 * @param brandId
	 * @param type
	 *            商家类型.
	 * @return
	 */
	public BusinessDTO getBusinessByBrandIdAndType(Long brandId, int type);

	/**
	 * 根据站点id和品牌id获取商家信息
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public BusinessDTO getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type);

	/**
	 * 同一个站点下某个品牌是否存在
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	public boolean existsAreaIdAndBrandId(Long areaId, long brandId, int type);

	/**
	 * 更新帐号激活状态
	 * 
	 * @param businessid
	 * @param userid
	 * @param state
	 * @return
	 */
	public boolean updateActiveByUserId(long businessid, long userid, ActiveState state);

	/**
	 * 
	 * @param areaId
	 * @param brandId
	 * @param businessAccount
	 * @return
	 */
	public List<Business> getBusinessByAreaIdOrBrandIdOrAccount(Long areaId, Long brandId);

	/**
	 * 
	 * @param businessAccount
	 * @return
	 */
	public List<Business> getBusinessByAccount(String businessAccount);

	/**
	 * 根据商家营业执照获取商家信息
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
	 * 获取某个站点下的激活商家
	 * 
	 * @param brandId
	 * @return
	 */
	public List<Business> getActiveBusinessByBrand(Long brandId);

	/**
	 * 获取某个站点下的激活商家
	 * 
	 * @param brandId
	 * @return
	 */
	public List<BusinessDTO> getActiveBusinessDTOByBrand(Long brandId);

	boolean existsAreaIdAndBrandId(Long areaId, long brandId) throws Exception;

	public BusinessDTO getBusinessByAccountName(String accountName);

	/**
	 * 获取某个站点下商家数据
	 * 
	 * @param areaId
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOListByAreaId(long areaId);

	/**
	 * 获取站点列表下的用户id
	 * 
	 * @param areaIdList
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOListByAreaIdList(List<Long> areaIdList, long areaCode);

	/**
	 * 根据站点和品牌获取商家列表
	 * 
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
	public List<Business> getBusinessAreaByBrandId(Long brandId);

	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<BusinessDTO> getBusinessDTOListByIdList(List<Long> ids);

	/**
	 * 
	 * @param areaBitMap
	 * @param brandId
	 * @param type
	 * @return
	 */
	public BusinessDTO getBusinessByAreaBitMapAndBrandId(Long areaBitMap, Long brandId, int type);

	/**
	 * 根据id列表获取区站点列表信息
	 * 
	 * @param idList
	 * @return
	 */
	public List<SendDistrictDTO> getDistrictByIdList(List<Long> idList);

	/**
	 * 删除商家用户关系
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteBusiUserRelation(long id);

	/**
	 * 
	 * @param id
	 * @param isActive
	 *            -1不加查选条件，0激活，1未激活
	 * @return
	 */
	public String getBusinessNameById(long id, int isActive);

	/**
	 * 根据商家id获取配送区域
	 * 
	 * @param businessId
	 * @return
	 */
	public List<SendDistrictDTO> getSendDistrictDTOList(long businessId);

	/**
	 * 店铺是否允许用户
	 * 
	 * @param businessId
	 * @param uid
	 * @return
	 */
	public boolean isUserBusinessAllowed(long businessId, long uid);

	/**
	 * 根据区域id获取商家列表
	 * 
	 * @param areaId
	 * @param areaFlag
	 *            1省，2市，3区
	 * @return
	 */
	public List<Long> getBusinessIdByDistrictId(long areaId, int areaFlag);

	/**
	 * 更新起批金额
	 * 
	 * @param businessid
	 * @param userid
	 * @param batchCash
	 * @param isOpen
	 * @return
	 */
	public boolean updateBatchCash(long businessid, long userid, BigDecimal batchCash, boolean isOpen);

	/**
	 * 无缓存取商家信息
	 * 
	 * @param id
	 * @param isActive
	 * @return
	 */
	public BusinessDTO getBusinessByIdNoCache(long id, int isActive);

	/**
	 * 取简单的商家信息
	 * 
	 * @param id
	 * @param isActive
	 * @return
	 */
	public BusinessDTO getBreifBusinessById(long id, int isActive);

	/**
	 * 是否允许下单：特许经营商家只允许指定用户,当前地区在商家配送区域范围内
	 * 
	 * @param businessIds
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public int isPermitOrder(List<Long> businessIds, Long userId, long areaId);

	/**
	 * 批量导入商家指定用户
	 * 
	 * @param busiUserRelationDTOs
	 * @return
	 */
	public boolean batchImportBusiUserRelation(List<BusiUserRelationDTO> busiUserRelationDTOs);

	/**
	 * 分页取得特许经营商家的指定用户账号
	 * 
	 * @param businessId
	 * @param userName
	 * @param ddbParam
	 * @return
	 */
	public BasePageParamVO<BusiUserRelationDTO> getPageBusiUserRelationBybusinessId(long businessId, String userName,
			DDBParam ddbParam);

	/**
	 * 单独绑定
	 * 
	 * @param relationDTO
	 * @return
	 */
	public boolean bindBusiUserRelation(BusiUserRelationDTO relationDTO);

	/**
	 * 根据userId获取特供关系
	 * 
	 * @param userId
	 * @return
	 */
	public List<BusiUserRelationDTO> getBusiUserRelationsByUserId(long userId);
}
