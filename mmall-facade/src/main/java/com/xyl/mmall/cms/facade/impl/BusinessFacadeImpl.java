package com.xyl.mmall.cms.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusiUserRelationDTO;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.enums.ActiveState;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.cms.vo.BusinessConditionAO;
import com.xyl.mmall.cms.vo.PagerContainer;
import com.xyl.mmall.cms.vo.Pagination;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.service.AccountService;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.oms.service.OmsOutsideService;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 
 * @author hzchaizhf
 * @create 2014年9月16日
 * 
 */
@Facade
public class BusinessFacadeImpl implements BusinessFacade {

	private static Logger log = LoggerFactory.getLogger(BusinessFacadeImpl.class);

	@Resource
	private BusinessService businessService;

	@Resource
	private OmsOutsideService omsService;

	@Resource
	private BrandService brandService;

	@Resource
	private DealerService dealerService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private LocationService locationService;

	@Resource
	private UserProfileService userProfileService;

	@Resource
	private AccountService accountService;

	@Resource
	private ItemProductService itemProductService;

	@Resource
	private SiteCMSService siteCMSService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getBusinessListByTypeId(long)
	 */
	@Override
	public PagerContainer getBusinessListByAreaIdAndAccount(BusinessConditionAO conditionAO, DDBParam ddbParam) {
		PagerContainer container = conditionAO.getPagerContainer();
		List<BusinessDTO> buList = Collections.emptyList();
		// 1.获取总数
		int count = businessService.getTotal(conditionAO.getProvince(), conditionAO.getAccount());
		container.setTotalCount(count);
		// 2.获取分页列表
		buList = businessService.getBusinessListByProvinceOrAccount(conditionAO.getProvince(),
				conditionAO.getAccount(), ddbParam);
//		for (BusinessDTO bu : buList) {
//			// 2.1获取品牌名称
//			BrandDTO brand = brandService.getBrandByBrandId(bu.getActingBrandId());
//			String brandName = null;
//			if (brand != null && brand.getBrand() != null) {
//				// brandName = brand.getBrand().getBrandNameZh();
//				brandName = ScheduleUtil.getCombinedBrandName(brand.getBrand().getBrandNameEn(), brand.getBrand()
//						.getBrandNameZh());
//			}
//			bu.setActingBrandName(brandName);
//		}
		// 3.排序
		Collections.sort(buList, new BusinessDTO());
		container.setDataList(buList);
		return container;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#addBusiness(com.xyl.mmall.cms.dto.BusinessDTO)
	 */
	@Override
	@Transaction
	public BusinessDTO addBusiness(BusinessDTO businessDTO) throws Exception {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail(businessDTO.getContactEmail());
		accountDTO.setUsername(businessDTO.getBusinessAccount());
		accountDTO.setPassword(businessDTO.getPassword());
		// 创建密码账号
		accountDTO = accountService.createAccount(accountDTO);
		if (accountDTO == null) {
			return null;
		}
		Business bu = null;
		// 1.添加用户到TB_Business,Mmall_CMS_BusiUserRelation,Mmall_CMS_SendDistrict表
		bu = businessService.addBusiness(businessDTO);
		// 2.添加用户帐号到dealer表
		DealerDTO dealerDTO = dealerService.assignNewDealerOwner(bu.getBusinessAccount(), bu.getId());
		if (dealerDTO == null) {
			throw new Exception("商家添加失败！");
		}
		return (null == dealerDTO) ? null : new BusinessDTO(bu);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#deleteBusiness(long)
	 */
	@Override
	@Transaction
	public int deleteBusiness(long id, long operator) {
		BusinessDTO businessDTO = businessService.getBusinessById(id, -1);
		if (businessDTO == null) {
			return 0;// 商家不存在
		}
		try {
			accountService.deleteAccountByUserName(businessDTO.getBusinessAccount());
			dealerService.deleteDealerInfoByBusinessID(id);
			itemProductService.updateProductSKUStatusByBusinessId(id, ProductStatusType.OFFLINE, operator);
		} catch (ServiceException e) {
			log.error("delete business " + id + " error", e);
			return -1;// 删除商家后台管理用户信息失败
		} catch (ItemCenterException itemCenterException) {
			log.error("delete business " + id + " error", itemCenterException);
			return -2;// 删除商家后台时商品下架失败
		} catch (Exception e) {
			log.error("delete business " + id + " error", e);
			return -3;// 删除商家后台时其他原因
		}
		if (!businessService.deleteBusiness(id)) {
			return -4;// 删除商家失败
		}
		log.info("delete business account " + businessDTO.getBusinessAccount() + "  by " + operator);
		return 1;// 成功
	}

	@Override
	public boolean batchDeleteBusiness(List<Long> ids) {
		return businessService.batchDeleteBusiness(ids);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#updateBusiness(com.xyl.mmall.cms.dto.BusinessDTO)
	 */
	@Override
	public int updateBusiness(BusinessDTO businessDTO) throws Exception {
		boolean result = businessService.updateBusiness(businessDTO);
		if (!result) {
			return -1;// 更新商家信息失败
		}
		if (businessDTO.getPasswordIsChange().equals("N")) {
			return 1;
		}
		AccountDTO accountDTO = accountService.findAccountByUserName(businessDTO.getBusinessAccount());
		if (accountDTO == null) {
			return -2;// 账号找不到
		}
		accountDTO.setPassword(businessDTO.getPassword());
		result = accountService.updateAccount(accountDTO);
		if (!result) {
			return -3;// 更新账号密码失败
		}
		return 1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @throws Exception
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getBusinessById(long)
	 */
	@Override
	public BusinessDTO getBusinessById(long id) {
		BusinessDTO businessDTO = businessService.getBusinessByIdNoCache(id, -1);
//		if (businessDTO == null)
//			return null;
//		else {
//			BrandDTO brand = brandService.getBrandByBrandId(businessDTO.getActingBrandId());
//			String brandName = "";
//			if (brand != null && brand.getBrand() != null) {
//				// brandName = brand.getBrand().getBrandNameZh();
//				brandName = ScheduleUtil.getCombinedBrandName(brand.getBrand().getBrandNameEn(), brand.getBrand()
//						.getBrandNameZh());
//			}
//
//			businessDTO.setActingBrandName(brandName);
//			// locationService.getLocationCodeListByCodeList(codeList);
//		}
		return businessDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#existsRegistrationNumber(java.lang.String)
	 */
	@Override
	public boolean existsRegistrationNumber(String registrationNumber) {
		return businessService.existsRegistrationNumber(registrationNumber);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getAreaList()
	 */
	@Override
	public List<AreaDTO> getAreaList() {
		return businessService.getAreaList();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getAreaById(long)
	 */
	@Override
	public AreaDTO getAreaById(long id) {
		return businessService.getAreaById(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#existsBusinessAccount(java.lang.String)
	 */
	@Override
	public boolean existsBusinessAccount(String businessAccount) {
		boolean result = businessService.existsBusinessAccount(businessAccount);
		if (!result) {
			DealerDTO dealerDTO = dealerService.findDealerByName(businessAccount);
			return dealerDTO != null;
		}
		return result;
	}

	@Override
	public List<BusinessDTO> getBusinessDTOListByAreaId(long areaId) {
		return businessService.getBusinessDTOListByAreaId(areaId);
	}

	@Override
	public List<BusinessDTO> getBusinessDTOListByAreaIdList(List<Long> areaIdList) {
		return businessService.getBusinessDTOListByAreaIdList(areaIdList, 0L);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getAreadByIdList(java.util.List)
	 */
	@Override
	public List<AreaDTO> getAreadByIdList(List<Long> idList) {
		return businessService.getAreadByIdList(idList);
	}

	@Override
	public List<SiteAreaDTO> getSiteAreaBySiteIdList(List<Long> siteIds) {
		return siteCMSService.getSiteAreasList(siteIds);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getBusinessByAreaIdAndBrandId(java.lang.Long,
	 *      java.lang.Long, int)
	 */
	@Override
	public BusinessDTO getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type) {
		return businessService.getBusinessByAreaIdAndBrandId(areaId, brandId, type);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getBusinessByAreaIdAndBrandId(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId) {
		return businessService.getBusinessByAreaIdAndBrandId(areaId, brandId);
	}

	@Override
	public List<BusinessDTO> getBusinessDTOByAreaIdAndBrandId(Long areaId, Long brandId) {
		return businessService.getBusinessDTOByAreaIdAndBrandId(areaId, brandId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#existsAreaIdAndBrandId(java.lang.Long,
	 *      long, int)
	 */
	@Override
	public boolean existsAreaIdAndBrandId(Long areaId, long brandId, int type) {
		return businessService.existsAreaIdAndBrandId(areaId, brandId, type);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#lockBusinessAccount(long,
	 *      long)
	 */
	@Override
	public boolean lockBusinessAccount(long userId, long id) {
		// List<PODTO> list = scheduleService.getOnlinePOList(0, id);
		// if(list != null && list.size()>0)
		// return false;
		Business buss = businessService.getBusinessById(id, -1);
		if (buss != null) {
			businessService.updateActiveByUserId(id, userId, ActiveState.LOCK);
			DealerDTO dealerDTO = dealerService.findDealerByName(buss.getBusinessAccount());
			dealerService.updateDealerAccountStatus(userId, dealerDTO.getId(), AccountStatus.LOCKED);
		//	brandService.freezeSupplierBrand(id, true);
			try {
				itemProductService.updateProductSKUStatusByBusinessId(id, ProductStatusType.OFFLINE, userId);
			} catch (ItemCenterException e) {
				log.error("update product to offline while locking business " + id, e.getMessage());
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#unlockBusinessAccount(long,
	 *      long)
	 */
	@Override
	public void unlockBusinessAccount(long userId, long id) {
		// businessService.updateActiveByUserId(id, ActiveState.UNLOCK);
		Business buss = businessService.getBusinessById(id, -1);
		if (buss != null) {
			businessService.updateActiveByUserId(id, userId, ActiveState.UNLOCK);
			DealerDTO dealerDTO = dealerService.findDealerByName(buss.getBusinessAccount());
			dealerService.updateDealerAccountStatus(userId, dealerDTO.getId(), AccountStatus.NORMAL);
			//brandService.freezeSupplierBrand(id, false);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getBusinessByAccount(java.lang.String)
	 */
	@Override
	public List<Business> getBusinessByAccount(String businessAccount) {
		return businessService.getBusinessByAccount(businessAccount);

	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#existsActiveBusinessByBrand(java.lang.Long)
	 */
	@Override
	public boolean existsActiveBusinessByBrand(Long brandId) {
		List<Business> list = businessService.getActiveBusinessByBrand(brandId);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.BusinessFacade#getBusinessDTOByName(java.lang.String)
	 */
	@Override
	public BusinessDTO getBusinessDTOByName(String businessAccount) {
		return businessService.getBusinessByAccountName(businessAccount);
	}

	@Override
	public List<Business> existBusinessOfBrandId(long brandId) {
		return businessService.getBusinessAreaByBrandId(brandId);
	}

	@Override
	public List<BusinessDTO> getBusinessDTOListByAreaIdListAndAreaId(List<Long> areaIdList, long areaId) {
		return businessService.getBusinessDTOListByAreaIdList(areaIdList, areaId);
	}

	@Override
	public List<BusinessDTO> getBusinessDTOByIdList(List<Long> ids) {
		return businessService.getBusinessDTOListByIdList(ids);
	}

	@Override
	public List<BusinessDTO> getBusinessDTOByBrandId(long brandId) {
		return businessService.getActiveBusinessDTOByBrand(brandId);
	}

	@Override
	public BusinessDTO getBusinessByAreaBitMapAndBrandId(Long areaBitMap, Long brandId, int type) {
		return businessService.getBusinessByAreaBitMapAndBrandId(areaBitMap, brandId, type);
	}

	@Override
	public PagerContainer getBusinessListByBusinessCondition(BusinessConditionDTO businessConditionDTO) {
		PagerContainer pagerContainer = new PagerContainer(new Pagination(businessConditionDTO.getOffset(),
				businessConditionDTO.getLimit()));
		if (StringUtils.isNotBlank(businessConditionDTO.getBusinessId())
				&& !StringUtils.isNumeric(businessConditionDTO.getBusinessId())) {
			return pagerContainer;
		}
		BasePageParamVO<BusinessDTO> result = businessService.getBusinessListByBusinessCondition(businessConditionDTO);
		pagerContainer.getPagination().setHasNextPage(businessConditionDTO.isHasNext());
		pagerContainer.getPagination().setPageSize(result.getPageSize());
		pagerContainer.setDataList(result.getList());
		pagerContainer.setTotalCount(result.getTotal());
		return pagerContainer;
	}

	@Override
	public List<SendDistrictDTO> getDistrictByIdList(List<Long> idList) {
		return businessService.getDistrictByIdList(idList);
	}

	@Override
	public PagerContainer getUserListByUserName(UserProfileConditionDTO userProfileConditionDTO) {
		PagerContainer pagerContainer = new PagerContainer(new Pagination(userProfileConditionDTO.getOffset(),
				userProfileConditionDTO.getLimit()));
		BasePageParamVO<UserProfileDTO> result = userProfileService.getUserListByUserCondition(userProfileConditionDTO);
		pagerContainer.getPagination().setHasNextPage(userProfileConditionDTO.isHasNext());
		pagerContainer.setDataList(result.getList());
		pagerContainer.setTotalCount(result.getTotal());
		return pagerContainer;
	}

	@Override
	public int getBusinessCountByAreaId(long areaId) {
		List<Long> businessIdList = businessService.getBusinessIdByDistrictId(areaId, 2);
		return businessIdList == null ? 0 : businessIdList.size();
	}

	@Override
	public boolean updateBatchCash(long businessid, long userid, BigDecimal batchCash, boolean isOpen) {
		return businessService.updateBatchCash(businessid, userid, batchCash, isOpen);
	}

	@Override
	public PagerContainer getPageBusiUserRelationBybusinessId(long businessId,String userName,
			DDBParam ddbparam) {
		PagerContainer pagerContainer = new PagerContainer(new Pagination(ddbparam.getOffset(),
				ddbparam.getLimit()));
		BasePageParamVO<BusiUserRelationDTO> basePageParamVO = businessService.getPageBusiUserRelationBybusinessId(businessId,userName, ddbparam);
		List<Long> userIdList = new ArrayList<Long>();
		if(CollectionUtil.isEmptyOfList(basePageParamVO.getList())){
			return pagerContainer;
		}
		for (BusiUserRelationDTO busiUserRelationDTO : basePageParamVO.getList()) {
			userIdList.add(busiUserRelationDTO.getUserId());
		}
		pagerContainer.getPagination().setHasNextPage(basePageParamVO.isHasNextPage());
		pagerContainer.setDataList(basePageParamVO.getList());
		pagerContainer.setTotalCount(basePageParamVO.getTotal());
		return pagerContainer;
	}

	@Override
	public BusinessDTO getBreifBusinessById(long businessId, int isActive) {
		return businessService.getBreifBusinessById(businessId, isActive);
	}

}
