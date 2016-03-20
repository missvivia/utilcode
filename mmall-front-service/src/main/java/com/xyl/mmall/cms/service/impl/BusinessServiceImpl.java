package com.xyl.mmall.cms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dao.AreaDao;
import com.xyl.mmall.cms.dao.BusiUserRelationDao;
import com.xyl.mmall.cms.dao.BusinessAreaDao;
import com.xyl.mmall.cms.dao.BusinessDao;
import com.xyl.mmall.cms.dao.SendDistrictDao;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusiUserRelationDTO;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.enums.ActiveState;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.meta.Area;
import com.xyl.mmall.cms.meta.BusiUserRelation;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.meta.BusinessArea;
import com.xyl.mmall.cms.meta.SendDistrict;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * 
 * @author hzchaizhf
 * @create 2014年9月16日
 *
 */
@Service
public class BusinessServiceImpl implements BusinessService {

	private static Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

	@Autowired
	private BusinessDao businessDao;

	@Autowired
	private AreaDao areaDao;

	@Autowired
	private BusinessAreaDao businessAreaDao;

	@Autowired
	private SendDistrictDao sendDistrictDao;

	@Autowired
	private BusiUserRelationDao busiUserRelationDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessListByTypeId(long)
	 */
	@Override
	public List<BusinessDTO> getBusinessListByTypeId(int type, long typeId, DDBParam ddbParam) {
		List<Business> buList = businessDao.getBusinessListByTypeId(type, typeId, ddbParam);
		List<BusinessDTO> ret = new ArrayList<BusinessDTO>(buList.size());
		List<Long> areaIds = new ArrayList<Long>();
		List<String> areaNames = new ArrayList<String>();
		for (Business bu : buList) {
			List<BusinessArea> list = businessAreaDao.getAreaBySupplierId(bu.getId());
			if (list != null && list.size() > 0) {
				for (BusinessArea ba : list) {
					areaIds.add(ba.getAreaId());
					areaNames.add(ba.getAreaName());
				}
			}
			BusinessDTO bd = new BusinessDTO(bu);
			bd.setAreaIds(areaIds);
			bd.setAreaNames(areaNames);
			ret.add(bd);
		}
		return ret;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#addBusiness(com.xyl.mmall.cms.dto.BusinessDTO)
	 */
	@Override
	@Transaction
	public BusinessDTO addBusiness(BusinessDTO businessDTO) {
		long id = businessDao.allocateRecordId();
		if (id < 1l) {
			throw new DBSupportRuntimeException("Get generateId failed!");
		}
		businessDTO.setId(id);
		businessDTO.setCreateTime(new Date().getTime());
		businessDTO.setUpdateTime(new Date().getTime());
		Business bu = businessDao.addObject(businessDTO);
		if (bu == null) {
			return null;
		}
		for (SendDistrict sendDistrict : businessDTO.getSendDistrictDTOs()) {
			sendDistrict.setCreateBy(businessDTO.getCreatorId());
			sendDistrict.setUpdateBy(businessDTO.getCreatorId());
			sendDistrict.setBusinessId(id);
		}
		sendDistrictDao.addObjects(businessDTO.getSendDistrictDTOs());
		businessDTO.setId(id);
		return businessDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#deleteBusiness(long)
	 */
	@Override
	@Transaction
	@Caching(evict = { @CacheEvict(value = "businessCache", key = "#id"),
			@CacheEvict(value = "businessBreifCache", key = "#id") })
	public boolean deleteBusiness(long id) {
		boolean succ = false;
		busiUserRelationDao.deleteByBusinessId(id);// 指定用户表不一定有值
		sendDistrictDao.deleteByBusinessId(id);
		businessDao.deleteBusiness(id);
		succ = true;
		return succ;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @throws Exception
	 * @see com.xyl.mmall.cms.service.BusinessService#updateBusiness(com.xyl.mmall.cms.meta.Business)
	 */
	@Override
	@Transaction
	@Caching(evict = { @CacheEvict(value = "businessCache", key = "#businessDTO.id"),
			@CacheEvict(value = "businessBreifCache", key = "#businessDTO.id") })
	public boolean updateBusiness(BusinessDTO businessDTO) {
		if (CollectionUtil.isNotEmptyOfList(businessDTO.getAreaIds())) {
			for (Long id : businessDTO.getAreaIds()) {
				sendDistrictDao.deleteById(id);
			}
		}
		for (SendDistrict sendDistrict : businessDTO.getSendDistrictDTOs()) {
			sendDistrict.setBusinessId(businessDTO.getId());
			if (sendDistrict.getId() > 0) {
				sendDistrict.setUpdateBy(businessDTO.getUpdateBy());
				sendDistrict.setUpdateTime(new Date());
				sendDistrictDao.updateObjectByKey(sendDistrict);
			} else {
				sendDistrictDao.addObject(sendDistrict);
			}
		}
		return businessDao.updateBusiness(businessDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @throws Exception
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessById(long)
	 */
	@Override
	@Cacheable(value = "businessCache", key = "#id")
	public BusinessDTO getBusinessById(long id, int isActive) {
		Business bu = businessDao.getBusinessById(id, isActive);
		if (bu != null) {
			BusinessDTO result = new BusinessDTO(bu);
			List<SendDistrict> sendDistricts = sendDistrictDao.getDistrictsByBusinessId(id);
			result.setSendDistrictDTOs(convertSendDistrictToDTO(sendDistricts));
			List<BusiUserRelation> busiUserRelations = busiUserRelationDao.getBusiUserRelationsByBusinessId(id);
			result.setBusiUserRelations(convertBusiUserRelationToDTO(busiUserRelations));
			return result;
		} else {
			return null;
		}
	}

	private List<BusiUserRelationDTO> convertBusiUserRelationToDTO(List<BusiUserRelation> busiUserRelations) {
		if (busiUserRelations == null) {
			return null;
		}
		List<BusiUserRelationDTO> busiUserRelationDTOs = new ArrayList<BusiUserRelationDTO>();
		for (BusiUserRelation busiUserRelation : busiUserRelations) {
			busiUserRelationDTOs.add(new BusiUserRelationDTO(busiUserRelation));
		}
		return busiUserRelationDTOs;
	}

	private List<SendDistrictDTO> convertSendDistrictToDTO(List<SendDistrict> sendDistricts) {
		if (sendDistricts == null) {
			return null;
		}
		List<SendDistrictDTO> sendDistrictDTOs = new ArrayList<SendDistrictDTO>();
		for (SendDistrict sendDistrict : sendDistricts) {
			sendDistrictDTOs.add(new SendDistrictDTO(sendDistrict));
		}
		return sendDistrictDTOs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getTotal(int, long)
	 */
	@Override
	public int getTotal(long areaId, String businessAccount) {
		// int total = businessDao.getTotal(areaId,businessAccount);
		// if(areaId>0){
		// try {
		// areaId = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		// } catch (Exception e) {
		// logger.error(e.getMessage());
		// return 0;
		// }
		// }
		int total = businessDao.getTotal(areaId, businessAccount);
		return total;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#existsRegistrationNumber(java.lang.String)
	 */
	@Override
	public boolean existsRegistrationNumber(String registrationNumber) {
		List<Business> buList = businessDao.getBusinessListByRegistrationNumber(registrationNumber);
		if (buList != null && buList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getAreaList()
	 */
	@Override
	@Deprecated
	public List<AreaDTO> getAreaList() {
		List<Area> areaList = areaDao.getAreaList();
		List<AreaDTO> ret = new ArrayList<AreaDTO>(areaList.size());
		for (Area a : areaList) {
			ret.add(new AreaDTO(a));
		}
		return ret;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getAreaById(long)
	 */
	@Override
	@Deprecated
	public AreaDTO getAreaById(long id) {
		Area area = areaDao.getAreaById(id);
		return (null == area) ? null : new AreaDTO(area);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#existsBusinessAccount(java.lang.String)
	 */
	@Override
	public boolean existsBusinessAccount(String businessAccount) {
		List<Business> buList = businessDao.getBusinessListByBusinessAccount(businessAccount);
		if (buList != null && buList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessListByAreaId(java.lang.String)
	 */
	@Override
	@Deprecated
	public List<Business> getBusinessListByAreaId(long areaId) {
		List<Business> list = null;
		try {
			list = businessDao.getBusinessListByAreaId(areaId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	@Deprecated
	public List<BusinessDTO> getBusinessDTOListByAreaId(long areaId) {
		List<BusinessDTO> list = null;
		try {
			List<Business> buList = businessDao.getBusinessListByAreaId(areaId);
			if (buList != null && buList.size() > 0) {
				list = new ArrayList<BusinessDTO>();
				for (Business business : buList) {
					BusinessDTO businessDTO = new BusinessDTO(business);
					List<String> areaNames = new ArrayList<String>();
					for (long area_id : businessDTO.getAreaIds()) {
						Area area = areaDao.getAreaById(area_id);
						areaNames.add(area.getAreaName());
					}
					businessDTO.setAreaNames(areaNames);
					list.add(businessDTO);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	@Deprecated
	public List<Business> getBusinessListByAreaIdList(List<Long> areaIdList) {
		List<Business> baList = businessDao.getBusinessListByAreaIdList(areaIdList, 0L);
		return baList;
	}

	@Override
	@Deprecated
	public List<BusinessDTO> getBusinessDTOListByAreaIdList(List<Long> areaIdList, long areaCode) {
		List<Business> buList = businessDao.getBusinessListByAreaIdList(areaIdList, areaCode);
		if (buList != null && buList.size() > 0) {
			List<BusinessDTO> list = new ArrayList<BusinessDTO>();
			for (Business bu : buList) {
				BusinessDTO businessDTO = new BusinessDTO(bu);
				try {
					List<Long> areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(businessDTO.getSiteId());
					List<String> areaNames = new ArrayList<String>();
					businessDTO.setAreaIds(areaIds);
					for (Long areaId : areaIds) {
						Area area = areaDao.getAreaById(areaId);
						areaNames.add(area.getAreaName());
					}
					businessDTO.setAreaNames(areaNames);
				} catch (Exception e) {
					logger.error(e.getMessage());
					return new ArrayList<>();
				}
				list.add(businessDTO);
			}
			return list;
		}
		return new ArrayList<>();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessAccount(java.lang.String)
	 */
	@Override
	public Business getBusinessAccount(String businessAccount) {
		return businessDao.getBusinessAccount(businessAccount);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByCompanyName(java.lang.String)
	 */
	@Override
	public Business getBusinessByCompanyName(String companyName) {
		return businessDao.getBusinessByCompanyName(companyName);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getAreadByIdList(java.util.List)
	 */
	@Override
	public List<AreaDTO> getAreadByIdList(List<Long> idList) {
		List<Area> areaList = areaDao.getAreadByIdList(idList);
		List<AreaDTO> ret = new ArrayList<AreaDTO>(areaList.size());
		for (Area a : areaList) {
			ret.add(new AreaDTO(a));
		}
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByAreaIdAndBrandId(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId) {
		Business bu;
		try {
			bu = businessDao.getBusinessByAreaIdAndBrandId(areaId, brandId);
			return bu;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<BusinessDTO> getBusinessDTOByAreaIdAndBrandId(Long areaId, Long brandId) {
		try {
			Long areaBitMap = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			List<Business> baList = businessDao.getBusinessByAreaIdOrBrandIdOrAccount(areaBitMap, brandId);
			if (baList != null && baList.size() > 0) {
				List<BusinessDTO> result = new ArrayList<BusinessDTO>();
				for (Business bu : baList) {
					BusinessDTO businessDTO = new BusinessDTO(bu);
					List<Long> areaIds;
					areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(businessDTO.getSiteId());
					List<String> areaNames = new ArrayList<String>();
					businessDTO.setAreaIds(areaIds);
					for (Long id : areaIds) {
						Area area = areaDao.getAreaById(id);
						areaNames.add(area.getAreaName());
					}
					businessDTO.setAreaNames(areaNames);
				}
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByBrandIdAndType(java.lang.Long,
	 *      int)
	 */
	@Cacheable(value = "businessCache")
	@Override
	public BusinessDTO getBusinessByBrandIdAndType(Long brandId, int type) {
		Business bu = businessDao.getBusinessByBrandIdAndType(brandId, type);
		if (bu != null) {
			BusinessDTO businessDTO = new BusinessDTO(bu);
			return businessDTO;
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByAreaIdAndBrandId(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	public BusinessDTO getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type) {
		try {
			Long areaBitMap = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			Business bu = businessDao.getBusinessByAreaIdAndBrandId(areaBitMap, brandId, type);
			if (bu != null) {
				BusinessDTO businessDTO = new BusinessDTO(bu);
				return businessDTO;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByAreaIdAndBrandId(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	public BusinessDTO getBusinessByAreaBitMapAndBrandId(Long areaBitMap, Long brandId, int type) {
		Business bu = businessDao.getBusinessByAreaIdAndBrandId(areaBitMap, brandId, type);
		if (bu != null) {
			BusinessDTO businessDTO = new BusinessDTO(bu);
			return businessDTO;
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessListByProvinceOrAccount(long,
	 *      java.lang.String, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<BusinessDTO> getBusinessListByProvinceOrAccount(long areaId, String businessAccount, DDBParam ddbParam) {
		try {
			// if(areaId>0){
			// areaId = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			// }
			List<Business> buList = businessDao.getBusinessListByProvinceOrAccount(areaId, businessAccount, ddbParam);
			List<BusinessDTO> result = new ArrayList<BusinessDTO>();
			if (buList != null) {
				for (Business ba : buList) {
					BusinessDTO buDTO = new BusinessDTO(ba);
					List<Long> areaIds;
					areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(buDTO.getSiteId());
					List<String> areaNames = new ArrayList<String>();
					buDTO.setAreaIds(areaIds);
					for (Long id : areaIds) {
						Area area = areaDao.getAreaById(id);
						if (area == null) {
							logger.error("area id not exist:" + id);
							continue;
						}
						areaNames.add(area.getAreaName());
					}
					buDTO.setAreaNames(areaNames);
					result.add(buDTO);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean existsAreaIdAndBrandId(Long areaId, long brandId) throws Exception {
		Business business;
		business = businessDao.getBusinessByAreaIdAndBrandId(areaId, brandId);
		return business != null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#existsAreaIdAndBrandId(java.lang.Long,
	 *      long, int)
	 */
	@Override
	public boolean existsAreaIdAndBrandId(Long areaId, long brandId, int type) {
		Business bu = businessDao.getBusinessByAreaIdAndBrandId(areaId, brandId, type);
		return bu != null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#updateActiveByUserId(long,
	 *      com.xyl.mmall.cms.enums.ActiveState)
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "businessCache", key = "#businessid"),
			@CacheEvict(value = "businessBreifCache", key = "#businessid") })
	public boolean updateActiveByUserId(long businessid, long userid, ActiveState state) {
		return businessDao.updateActiveByUserId(businessid, userid, state);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByAreaIdOrBrandIdOrAccount(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	public List<Business> getBusinessByAreaIdOrBrandIdOrAccount(Long areaId, Long brandId) {
		try {
			if (areaId > 0) {
				areaId = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			}
			return businessDao.getBusinessByAreaIdOrBrandIdOrAccount(areaId, brandId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByAccount(java.lang.String)
	 */
	@Override
	public List<Business> getBusinessByAccount(String businessAccount) {
		return businessDao.getBusinessByAccount(businessAccount);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessByRegNum(java.lang.String)
	 */
	@Override
	public Business getBusinessByRegNum(String registrationNumber) {
		return businessDao.getBusinessByRegNum(registrationNumber);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessListByIdList(java.util.List)
	 */
	@Override
	@Deprecated
	public List<Business> getBusinessListByIdList(List<Long> idList) {
		return businessDao.getBusinessListByIdList(idList);
	}

	@Override
	public List<Business> getActiveBusinessByBrand(Long brandId) {
		return businessDao.getActiveBusinessByBrand(brandId);
	}

	/**
	 * 根据商家营业执照获取商家信息
	 * 
	 * @param registrationNumber
	 * @return
	 */
	@Override
	@Deprecated
	public BusinessDTO getBusinessByAccountName(String accountName) {
		Business business = businessDao.getBusinessAccount(accountName);
		if (business != null) {
			BusinessDTO businessDTO = new BusinessDTO(business);
			List<Long> areaIds;
			try {
				areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(businessDTO.getSiteId());
			} catch (Exception e) {
				logger.debug(e.getMessage());
				return null;
			}
			List<String> areaNames = new ArrayList<String>();
			businessDTO.setAreaIds(areaIds);
			for (Long areaId : areaIds) {
				Area area = areaDao.getAreaById(areaId);
				areaNames.add(area.getAreaName());
			}
			businessDTO.setAreaNames(areaNames);
			return businessDTO;
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessAreaByBrandId(java.lang.Long)
	 */
	@Override
	public List<Business> getBusinessAreaByBrandId(Long brandId) {
		return businessDao.getBusinessByBrandId(brandId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.service.BusinessService#getBusinessDTOListByIdList(java.util.List)
	 */
	@Override
	public List<BusinessDTO> getBusinessDTOListByIdList(List<Long> ids) {
		try {
			List<Business> list = businessDao.getBusinessListByIdList(ids);
			if (list != null && list.size() > 0) {
				List<BusinessDTO> result = new ArrayList<BusinessDTO>();
				for (Business bu : list) {
					BusinessDTO businessDTO = new BusinessDTO(bu);
					// List<Long> areaIds =
					// ProvinceCodeMapUtil.getCodeListByProvinceFmt(businessDTO.getSiteId());
					// List<String> areaNames = new ArrayList<String>();
					// businessDTO.setAreaIds(areaIds);
					// for (Long areaId : areaIds) {
					// Area area = areaDao.getAreaById(areaId);
					// areaNames.add(area.getAreaName());
					// }
					// businessDTO.setAreaNames(areaNames);
					result.add(businessDTO);
				}
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<BusinessDTO> getActiveBusinessDTOByBrand(Long brandId) {
		try {
			List<Business> list = businessDao.getActiveBusinessByBrand(brandId);
			if (list != null && list.size() > 0) {
				List<BusinessDTO> result = new ArrayList<BusinessDTO>();
				for (Business bu : list) {
					BusinessDTO businessDTO = new BusinessDTO(bu);
					List<Long> areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(businessDTO.getSiteId());
					List<String> areaNames = new ArrayList<String>();
					businessDTO.setAreaIds(areaIds);
					for (Long areaId : areaIds) {
						Area area = areaDao.getAreaById(areaId);
						areaNames.add(area.getAreaName());
					}
					businessDTO.setAreaNames(areaNames);
					result.add(businessDTO);
				}
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public BasePageParamVO<BusinessDTO> getBusinessListByBusinessCondition(BusinessConditionDTO businessConditionDTO) {
		List<Business> list = businessDao.getBusinessListByBusinessCondition(businessConditionDTO);
		BasePageParamVO<BusinessDTO> buBasePageParamVO = new BasePageParamVO<BusinessDTO>();
		buBasePageParamVO.setTotal(businessConditionDTO.getTotalCount());
		if (CollectionUtil.isEmptyOfList(list)) {
			return buBasePageParamVO;
		}
		Function<Business, Long> function = new Function<Business, Long>() {
			@Override
			public Long apply(Business paramF) {
				return paramF.getId();
			}
		};
		List<SendDistrict> sendDistricts = sendDistrictDao
				.getDistrictListByBusinessIds(Lists.transform(list, function));
		Map<Long, List<SendDistrictDTO>> districtMap = new HashMap<Long, List<SendDistrictDTO>>();
		List<SendDistrictDTO> districtNameList = null;
		for (SendDistrict sendDistrict : sendDistricts) {
			Long businessId = sendDistrict.getBusinessId();
			districtNameList = districtMap.get(businessId);
			if (districtNameList == null) {
				districtNameList = new ArrayList<SendDistrictDTO>();
				districtMap.put(businessId, districtNameList);
			}
			districtNameList.add(new SendDistrictDTO(sendDistrict));
		}
		List<BusinessDTO> result = new ArrayList<BusinessDTO>();
		if (CollectionUtil.isNotEmptyOfList(list)) {
			for (Business bu : list) {
				BusinessDTO businessDTO = new BusinessDTO(bu);
				businessDTO.setSendDistrictDTOs(districtMap.get(businessDTO.getId()));
				result.add(businessDTO);
			}
		}
		buBasePageParamVO.setList(result);
		buBasePageParamVO.setTotal(businessConditionDTO.getTotalCount());
		return buBasePageParamVO;
	}

	@Override
	@Transaction
	@CacheEvict(value = { "businessCache", "businessBreifCache" }, allEntries = true)
	public boolean batchDeleteBusiness(List<Long> ids) {
		if (CollectionUtil.isEmptyOfList(ids)) {
			return false;
		}
		busiUserRelationDao.batchDeleteByBusinessIds(ids);
		return sendDistrictDao.batchDeleteByBusinessId(ids) & businessDao.batchDeleteBusiness(ids);
	}

	@Override
	public List<SendDistrictDTO> getDistrictByIdList(List<Long> idList) {
		List<SendDistrict> sendDistricts = sendDistrictDao.getDistrictListByDistrictIds(idList);
		List<SendDistrictDTO> dtoList = new ArrayList<SendDistrictDTO>(idList.size());
		for (SendDistrict sendDistrict : sendDistricts) {
			dtoList.add(new SendDistrictDTO(sendDistrict));
		}
		return dtoList;
	}

	@Override
	public boolean deleteBusiUserRelation(long id) {
		return busiUserRelationDao.deleteById(id);
	}

	@Override
	@Transaction
	public boolean batchImportBusiUserRelation(List<BusiUserRelationDTO> busiUserRelationDTOs) {
		if (CollectionUtil.isEmptyOfList(busiUserRelationDTOs)) {
			return false;
		}
		List<BusiUserRelation> busiUserRelations = new ArrayList<BusiUserRelation>();
		for (BusiUserRelationDTO busiUserRelationDTO : busiUserRelationDTOs) {
			busiUserRelations.add(busiUserRelationDTO);
		}
		return busiUserRelationDao.addObjects(busiUserRelations);
	}

	@Override
	public String getBusinessNameById(long id, int isActive) {
		return getBreifBusinessById(id, isActive).getStoreName();
	}

	@Override
	public List<SendDistrictDTO> getSendDistrictDTOList(long businessId) {
		List<SendDistrict> sendDistrictList = sendDistrictDao.getDistrictsByBusinessId(businessId);
		if (!CollectionUtils.isEmpty(sendDistrictList)) {
			List<SendDistrictDTO> retList = new ArrayList<SendDistrictDTO>(sendDistrictList.size());
			for (SendDistrict s : sendDistrictList) {
				retList.add(new SendDistrictDTO(s));
			}
			return retList;
		}
		return null;
	}

	@Override
	public boolean isUserBusinessAllowed(long businessId, long uid) {
		BusiUserRelation relation = new BusiUserRelation();
		relation.setUserId(uid);
		relation.setBusinessId(businessId);
		relation = busiUserRelationDao.getBusiUserRelation(relation);
		if (relation == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<Long> getBusinessIdByDistrictId(long areaId, int areaFlag) {
		return sendDistrictDao.getBusinessIdByDistrictId(areaId, areaFlag);
	}

	@Override
	public boolean updateBatchCash(long businessid, long userid, BigDecimal batchCash, boolean isOpen) {
		return businessDao.updateBatchCash(businessid, userid, batchCash, isOpen);
	}

	@Override
	public BusinessDTO getBusinessByIdNoCache(long id, int isActive) {
		Business bu = businessDao.getBusinessById(id, isActive);
		if (bu != null) {
			BusinessDTO result = new BusinessDTO(bu);
			List<SendDistrict> sendDistricts = sendDistrictDao.getDistrictsByBusinessId(id);
			result.setSendDistrictDTOs(convertSendDistrictToDTO(sendDistricts));
			List<BusiUserRelation> busiUserRelations = busiUserRelationDao.getBusiUserRelationsByBusinessId(id);
			result.setBusiUserRelations(convertBusiUserRelationToDTO(busiUserRelations));
			return result;
		} else {
			return null;
		}
	}

	@Override
	@Cacheable(value = "businessBreifCache", key = "#id")
	public BusinessDTO getBreifBusinessById(long id, int isActive) {
		Business bu = businessDao.getBusinessById(id, isActive);
		return new BusinessDTO(bu);
	}

	@Override
	public int isPermitOrder(List<Long> businessIds, Long userId, long areaId) {
		if (CollectionUtil.isEmptyOfList(businessIds)) {
			return -1;
		}
		Map<Long, Long> busiUserMap = new HashMap<Long, Long>();
		List<Business> businessList = businessDao.getBusinessListByIdList(businessIds);
		// 商家指定用户
		List<BusiUserRelation> busiUserRelations = busiUserRelationDao.getBusiUserRelationsByUserId(userId);
		if (CollectionUtil.isNotEmptyOfList(busiUserRelations)) {
			for (BusiUserRelation busiUserRelation : busiUserRelations) {
				busiUserMap.put(busiUserRelation.getBusinessId(), busiUserRelation.getUserId());
			}
		}
		// 商家配送区域
		List<SendDistrict> sendDistricts = null;
		for (Business business : businessList) {
			if (business.getType() == SupplierType.SPECIALMANAGE.getIntValue()
					&& busiUserMap.get(business.getId()) == null) {
				return -1;
			}
			sendDistricts = sendDistrictDao.getDistrictsByBusinessId(business.getId());
			if (CollectionUtil.isEmptyOfList(sendDistricts)) {
				return -2;
			}
			boolean isAreaPermit = false;
			for (SendDistrict sendDistrict : sendDistricts) {
				if (sendDistrict.getProvinceId() == 0) {
					isAreaPermit = true;
					continue;
				} else if (sendDistrict.getCityId() == 0) {
					if (AreaCodeUtil.getProvinceCode(areaId) == sendDistrict.getProvinceId() * 10000) {
						isAreaPermit = true;
						continue;
					}
				} else if (sendDistrict.getDistrictId() == 0) {
					if (AreaCodeUtil.getCityCode(areaId) == sendDistrict.getCityId() * 100) {
						isAreaPermit = true;
						continue;
					}
				} else if (sendDistrict.getDistrictId() == areaId) {
					isAreaPermit = true;
					continue;
				}
			}
			// 不在配送区域范围内
			if (!isAreaPermit) {
				return -2;
			}
		}
		return 1;
	}

	@Override
	public BasePageParamVO<BusiUserRelationDTO> getPageBusiUserRelationBybusinessId(long businessId, String userName,
			DDBParam ddbParam) {
		List<BusiUserRelation> busiUserRelations = busiUserRelationDao.getPageBusiUserRelationBybusinessId(businessId,
				userName, ddbParam);
		BasePageParamVO<BusiUserRelationDTO> result = new BasePageParamVO<BusiUserRelationDTO>(
				ddbParam.getTotalCount(), ddbParam.getLimit());
		result.setList(convertBusiUserRelationToDTO(busiUserRelations));
		result.setHasNextPage(ddbParam.isHasNext());
		return result;
	}

	@Override
	public boolean bindBusiUserRelation(BusiUserRelationDTO relationDTO) {
		return busiUserRelationDao.addObject(relationDTO) != null;
	}

	@Override
	public List<BusiUserRelationDTO> getBusiUserRelationsByUserId(long userId) {
		List<BusiUserRelation> busiUserRelations = busiUserRelationDao.getBusiUserRelationsByUserId(userId);
		return convertBusiUserRelationToDTO(busiUserRelations);
	}

}
