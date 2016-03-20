package com.xyl.mmall.cms.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.EncryptUtil;
import com.xyl.mmall.cms.facade.util.POBaseUtil;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.facade.util.SyncInventoryHelper;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.cms.vo.POSortVO;
import com.xyl.mmall.cms.vo.POStatusGetVO;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.common.facade.ScheduleBaseFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.ScheduleAuditData;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.meta.Promotion;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleMagicCubeDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.ScheduleMagicCube;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

@Facade
public class ScheduleFacadeImpl extends ScheduleBaseFacade implements ScheduleFacade {
	
	@Autowired
	private SyncInventoryHelper syncInventoryHelper;
	
	@Resource
	private SiteCMSService siteCMSService;
	
	@Override
	public List<AreaDTO> getAllowedAreaList(String permission) {
		long curUserId = SecurityContextUtils.getUserId();
		List<Long> allowedAreaIdList = agentService.findAgentSiteIdsByPermission(curUserId, permission);
		if (allowedAreaIdList == null) {
			allowedAreaIdList = new ArrayList<Long>();
		}
//		List<SiteCMSDTO> siteList = siteCMSService.getSiteCMSList(allowedAreaIdList);
		// TODO 按站点取区域
		
		return businessService.getAreadByIdList(allowedAreaIdList);
	}

	@Override
	public List<IdNameBean> getStoreAreaList() {
		List<IdNameBean> warehouseList = new ArrayList<IdNameBean>();
		WarehouseDTO[] warehouseArr = warehouseService.getAllWarehouse();
		if (warehouseArr != null) {
			for (WarehouseDTO warehouse : warehouseArr) {
				IdNameBean item = new IdNameBean();
				item.setId(warehouse.getWarehouseId() + POBaseUtil.NULL_STR);
				item.setName(warehouse.getWarehouseName());
				warehouseList.add(item);
			}
		}

		return warehouseList;
	}

	@Override
	public List<IdNameBean> getAllProvince() {
		List<LocationCode> siteList = locationService.getAllProvince();
		if (siteList == null) {
			return new ArrayList<IdNameBean>();
		}

		List<IdNameBean> result = new ArrayList<IdNameBean>();
		for (LocationCode locationCode : siteList) {
			IdNameBean site = new IdNameBean();
			site.setId(locationCode.getCode() + POBaseUtil.NULL_STR);
			site.setName(locationCode.getLocationName());
			result.add(site);
		}

		return result;
	}

	@Override
	public boolean canAccessPO(ScheduleDTO po, String accessPermission) {
		List<AreaDTO> permittedAreaList = getAllowedAreaList(accessPermission);
		if (permittedAreaList == null || permittedAreaList.size() == 0) {
			return false;
		}

		List<Long> permittedAreaIdList = new ArrayList<Long>();
		for (AreaDTO area : permittedAreaList) {
			permittedAreaIdList.add(area.getId());
		}

		List<Long> poAreaIdList = new ArrayList<Long>();
		for (ScheduleSiteRela siteRela : po.getSiteRelaList()) {
			poAreaIdList.add(siteRela.getSaleSiteId());
		}

		for (Long poAreaId : poAreaIdList) {
			boolean found = false;
			for (Long permittedAreaId : permittedAreaIdList) {
				if (poAreaId.equals(permittedAreaId)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ScheduleVO saveSchedule(PODTO poDTO) {
		PODTO dbDto = scheduleService.saveSchedule(poDTO);
		ScheduleVO vo = new ScheduleVO();
		vo.setPo(dbDto);
		return vo;
	}

	@Override
	public boolean updateSchedule(PODTO poDTO) {
		return scheduleService.updateSchedule(poDTO);
	}

	@Override
	public boolean deleteScheduleById(long id) {
		return scheduleService.deleteScheduleById(id);
	}

	@Override
	public boolean adjustScheduleDate(long id, long newStartTime, long newEndTime, String desc, String poFollowerName,
			long poFollowerId) {
		return scheduleService.adjustScheduleDate(id, newStartTime, newEndTime, desc, poFollowerName, poFollowerId);
	}

	@Override
	public boolean auditScheduleReject(long id, String desc) {
		return scheduleService.auditScheduleReject(id, desc);
	}

	@Override
	public boolean auditSchedulePass(long id) {
		return scheduleService.auditSchedulePass(id);
	}

	@Override
	public boolean auditScheduleSubmit(long id) {
		return scheduleService.auditScheduleSubmit(id);
	}

	@Override
	public boolean isProductInPO(long poId) {
		return poProductService.isProductInPO(poId);
	}

	@Override
	public void updatePrdListSubmitStatus(long poId) {
		int prodNum = poProductService.getProductNum(poId);
		int prodApproval = poProductService.getProductNumOfStatus(poId, StatusType.APPROVAL);
		int skuNum = poProductService.getSkuNum(poId);
		int skuApproval = poProductService.getSkuNumOfStatus(poId, StatusType.APPROVAL);

		if (prodNum == prodApproval) {
			scheduleService.updatePrdzlStatus(poId, StatusType.APPROVAL.getIntValue());
		} else {
			scheduleService.updatePrdzlStatus(poId, StatusType.PENDING.getIntValue());
		}

		if (skuNum == skuApproval) {
			scheduleService.updatePrdqdStatus(poId, StatusType.APPROVAL.getIntValue());
		} else {
			scheduleService.updatePrdqdStatus(poId, StatusType.PENDING.getIntValue());
		}

		if ((prodNum == prodApproval) && (skuNum == skuApproval)) {
			scheduleService.updatePOPrdStatus(poId, StatusType.APPROVAL.getIntValue());
		} else {
			poProductService.submitProduct(poId);
			scheduleService.updatePOPrdStatus(poId, StatusType.PENDING.getIntValue());
		}

	}

	@Override
	public boolean auditPOProductListPass(long poId) {
		return scheduleService.auditPOProductListPass(poId);
	}

	@Override
	public boolean auditPOOnline(long poId) {
		try {
			poProductService.productOnline(poId);
		} catch (Exception e) {
			logger.error("Error occured when call API: poProductService.productOnline(" + poId + ")!!!", e);
			return false;
		}
		
		List<POSkuDTO> skuList = null;
		try {
			skuList = poProductService.getSkuDTOListByPo(poId);
		} catch (Exception e) {
			logger.error("Error occured when call API: poProductService.getSkuDTOListByPo(" + poId + ")!!!", e);
			// auditSchedulePass(poId);
			return false;
		}

		if (skuList == null || skuList.size() == 0) {
			logger.warn("Cannot get any sku by call API: poProductService.getSkuDTOListByPo() for PO '" + poId + "'!!!");
			// auditSchedulePass(poId);
			return false;
		}

		logger.info("Got sku list for PO '" + poId + "': " + skuList);

		List<SkuOrderStockDTO> skuOrderStockList = new ArrayList<SkuOrderStockDTO>();
		if (skuList != null) {
			long now = System.currentTimeMillis();
			for (POSkuDTO sku : skuList) {
				SkuOrderStockDTO item = new SkuOrderStockDTO();
				item.setCtime(sku.getSubmitTime() == 0 ? now : sku.getSubmitTime());
				item.setUpTime(sku.getSubmitTime() == 0 ? now : sku.getSubmitTime());
				item.setStockCount(sku.getSkuNum() + sku.getSupplierSkuNum());
				item.setSkuId(sku.getId());
				skuOrderStockList.add(item);
				logger.info("      skuId: " + sku.getId());
			}
		}

		boolean flag = false;
		try {
			flag = skuOrderStockService.saveSkuOrderStockDTOColl(skuOrderStockList);
			if (!flag) {
				logger.error("Failured to call API: skuOrderStockService.saveSkuOrderStockDTOColl()!!!");
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occured when call API: skuOrderStockService.saveSkuOrderStockDTOColl()!!!", e);
			// auditSchedulePass(poId);
			return false;
		}

		try {
			List<Long> poIdList = new ArrayList<Long>();
			poIdList.add(poId);
			flag = syncInventoryHelper.inventoryPo(poIdList);
			if (!flag) {
				logger.error("Failured to call API: SyncInventoryHelper.inventoryPo()!!!");
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occured when call API: SyncInventoryHelper.inventoryPo()!!!", e);
			return false;
		}

		try {
			List<Long> poIdList = new ArrayList<Long>();
			poIdList.add(poId);
			flag = syncInventoryHelper.inventoryPo(poIdList);
			if (!flag) {
				logger.error("Failured to call API: SyncInventoryHelper.inventoryPo()!!!");
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occured when call API: SyncInventoryHelper.inventoryPo()!!!", e);
			return false;
		}
		
		if (!flag) {
			logger.error("Failed update SKU stock for PO '" + poId + "'!!!");
			// try to roll back PO status
			// auditSchedulePass(poId);
			return false;
		} else {
			logger.info("suc update SKU stock for PO '" + poId + "'!!!");
			flag = scheduleService.auditScheduleForBackend(poId, ScheduleState.BACKEND_PASSED, null);

			if (!flag) {
				logger.error("Online failed for PO '" + poId + "'!!!");
				return false;
			}
			logger.info("suc update PO status for online!!!");
		}

		return flag;
	}

	@Override
	public boolean auditPOOffline(long poId) {
		logger.debug("auditPOOffline: " + poId);
		return scheduleService.auditSchedulePass(poId);
	}

	@Override
	public ScheduleVO getScheduleById(long id) {
		PODTO dbDto = scheduleService.getScheduleById(id);
		ScheduleVO vo = new ScheduleVO();
		vo.setPo(dbDto);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleList(ScheduleCommonParamDTO paramDTO) {
		setSiteFlag(paramDTO);
		if (paramDTO.startDate != 0) {
			paramDTO.startDate = ScheduleUtil.getSpecificBeginTime(paramDTO.startDate).getTimeInMillis();
		}
		if (paramDTO.endDate != 0) {
			paramDTO.endDate = ScheduleUtil.getSpecificEndTime(paramDTO.endDate).getTimeInMillis();
		}
		POListDTO poList = scheduleService.getScheduleList(paramDTO);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(poList);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleListCommon(ScheduleCommonParamDTO paramDTO) {
		POListDTO poList = scheduleService.getScheduleListCommon(paramDTO);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(poList);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleList(ScheduleCommonParamDTO paramDTO, List<ScheduleState> statusList,
			boolean isCheck) {
		setSiteFlag(paramDTO);
		POListDTO poList = scheduleService.getScheduleList(paramDTO, statusList, isCheck);

		if (poList.getPoList().size() > 0) {
			long now = System.currentTimeMillis();
			for (PODTO poDTO : poList.getPoList()) {
				ScheduleUtil.setPOPrdStatus(poDTO);
				poDTO.setPoStatus(ScheduleUtil.getPOMainStatus(poDTO, now));
			}
		}
		
		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);

		return vo;
	}
	
	@Override
	public boolean batchUpdatePOOrder(POSortVO vo) {
		return scheduleService.batchUpdatePOOrder(vo.getPoOrderList());
	}

	@Override
	public ScheduleListVO getScheduleBannerList(ScheduleCommonParamDTO paramDTO) {
		POListDTO poList = scheduleService.getScheduleBannerList(paramDTO);

		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleListForCMS(ScheduleCommonParamDTO paramDTO, int type, Object val) {
		setSiteFlag(paramDTO);
		POListDTO poList = scheduleService.getScheduleListForCMS(paramDTO, type, val);
		// ScheduleListVO vo = new ScheduleListVO();
		// vo.setPoList(poList);

		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);

		return vo;
	}

	@Override
	public Map<Long, String> getSupplierIdAccountMap(POListDTO poList, int flag) {
		Map<Long, String> supplierIdAccountMap = new ConcurrentHashMap<Long, String>();
		for (PODTO po : poList.getPoList()) {
			long supplierId = 0;
			long poId = 0;
			if (flag == 1) {
				supplierId = po.getScheduleDTO().getSchedule().getSupplierId();
				poId = po.getScheduleDTO().getSchedule().getId();
			} else if (flag == 2) {
				supplierId = po.getPageDTO().getPage().getSupplierId();
				poId = po.getPageDTO().getPage().getScheduleId();
			} else if (flag == 3) {
				supplierId = po.getBannerDTO().getBanner().getSupplierId();
				poId = po.getBannerDTO().getBanner().getScheduleId();
			}
			BusinessDTO dto = businessFacade.getBusinessById(supplierId);
			// String account = dto.getBusinessAccount();
			String account = "";
			if (dto == null) {
				logger.warn("Wrong or dirty data: [supplierId=" + supplierId + ", POId=" + poId + "]");
			} else {
				account = dto.getBusinessAccount();
			}
			supplierIdAccountMap.put(supplierId, account);
		}

		return supplierIdAccountMap;
	}

	@Override
	public ScheduleListVO getScheduleListForOMS(ScheduleCommonParamDTO paramDTO, long startTimeBegin,
			long startTimeEnd, long endTimeBegin, long endTimeEnd, long createTimeBegin, long createTimeEnd) {
		if (startTimeBegin != 0) {
			startTimeBegin = ScheduleUtil.getSpecificBeginTime(startTimeBegin).getTimeInMillis();
		}
		if (startTimeEnd != 0) {
			startTimeEnd = ScheduleUtil.getSpecificEndTime(startTimeEnd).getTimeInMillis();
		}
		if (endTimeBegin != 0) {
			endTimeBegin = ScheduleUtil.getSpecificBeginTime(endTimeBegin).getTimeInMillis();
		}
		if (endTimeEnd != 0) {
			endTimeEnd = ScheduleUtil.getSpecificEndTime(endTimeEnd).getTimeInMillis();
		}
		if (createTimeBegin != 0) {
			createTimeBegin = ScheduleUtil.getSpecificBeginTime(createTimeBegin).getTimeInMillis();
		}
		if (createTimeEnd != 0) {
			createTimeEnd = ScheduleUtil.getSpecificEndTime(createTimeEnd).getTimeInMillis();
		}
		POListDTO poList = scheduleService.getScheduleListForOMS(paramDTO, startTimeBegin, startTimeEnd, endTimeBegin,
				endTimeEnd, createTimeBegin, createTimeEnd);

		// filter PO
		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);

		return vo;
	}

	@Override
	public List<ScheduleStatusDTO> getScheduleStateList() {
		return scheduleService.getScheduleStateList();
	}

	@Override
	public List<ScheduleStatusDTO> getScheduleStateForBackend() {
		return scheduleService.getScheduleStateList();
	}

	@Override
	public boolean auditScheduleForBackend(long id, ScheduleState status, String desc) {
		return scheduleService.auditScheduleForBackend(id, status, desc);
	}

	@Override
	public List<ScheduleChannelDTO> getScheduleChannelList() {
		return scheduleService.getScheduleChannelList();
	}

	@Override
	public JSONArray getProdCategoryList() {
		List<Category> categoryList = categoryService.getCategoryList();
		JSONArray firstCategory = new JSONArray();

		for (Category category : categoryList) {
			if (category.getSuperCategoryId() == 0) {
				JSONObject firstItem = _genCategoryJson(category);
				firstCategory.add(firstItem);

				for (Category c2 : categoryList) {
					if (c2.getSuperCategoryId() == category.getId()) {
						JSONObject secondItem = _genCategoryJson(c2);
						firstItem.getJSONArray("sub").add(secondItem);

						for (Category c3 : categoryList) {
							if (c3.getSuperCategoryId() == c2.getId()) {
								JSONObject thirdItem = _genCategoryJson(c3);
								secondItem.getJSONArray("sub").add(thirdItem);
							}
						}
					}
				}
			}
		}

		return firstCategory;
	}

	private JSONObject _genCategoryJson(Category category) {
		JSONObject json = new JSONObject();
		json.put("id", category.getId());
		json.put("name", category.getName());
		json.put("sub", new JSONArray());

		return json;
	}

	@Override
	public JSONArray getProdDetailAuditStatusList() {
		JSONArray arr = new JSONArray();
		for (StatusType item : StatusType.values()) {
			if (item != StatusType.NULL) {
				JSONObject json = new JSONObject();
				json.put("id", item.getIntValue());
				json.put("name", item.getDesc());
				arr.add(json);
			}
		}

		return arr;
	}

	// ***************************************
	// Schedule pages management
	// ***************************************
	@Override
	public ScheduleListVO getScheduleListForPOPages(ScheduleCommonParamDTO paramDTO, int type, Object key) {
		POListDTO poList = scheduleService.getScheduleListForPOPages(paramDTO, type, key);

		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);

		return vo;
	}

	@Override
	public boolean auditSchedulePageSubmit(long id, Long poId) {
		return pageService.auditSchedulePageSubmit(id, poId);
	}

	@Override
	public boolean auditSchedulePagePass(long pageId, long poId) {
		return pageService.auditSchedulePagePass(pageId, poId);
	}

	@Override
	public boolean auditSchedulePageReject(long pageId, long poId, String desc) {
		return pageService.auditSchedulePageReject(pageId, poId, desc);
	}

	@Override
	public List<ScheduleStatusDTO> getSchedulePageStatusList() {
		return pageService.getSchedulePageStatusList();
	}

	@Override
	public List<IdNameBean> getPOAuditStatusListForApprover() {
		return _getCommonStatusList();
	}
	
	private List<IdNameBean> _getCommonStatusList() {
		List<IdNameBean> statusList = new ArrayList<IdNameBean>();
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("1");
			bean.setName("待审核");
			statusList.add(bean);
		}
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("2");
			bean.setName("审核通过");
			statusList.add(bean);
		}
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("3");
			bean.setName("审核未通过");
			statusList.add(bean);
		}
		
		return statusList;
	}

	public List<IdNameBean> getPOPrdAuditStatusListForApprover() {
		List<IdNameBean> statusList = _getCommonStatusList();
		IdNameBean bean = new IdNameBean();
		bean.setId("4");
		bean.setName("失效");
		statusList.add(bean);

		return statusList;
	}


	
	@Override
	public List<IdNameBean> getPOAduitStatusListForApplier() {
		List<IdNameBean> statusList = new ArrayList<IdNameBean>();
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("1");
			bean.setName("待提交");
			statusList.add(bean);
		}
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("2");
			bean.setName("审核中");
			statusList.add(bean);
		}
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("3");
			bean.setName("审核通过");
			statusList.add(bean);
		}
		{
			IdNameBean bean = new IdNameBean();
			bean.setId("4");
			bean.setName("审核未通过");
			statusList.add(bean);
		}
		return statusList;
	}

	@Override
	public ScheduleListVO getSchedulePageList(ScheduleCommonParamDTO paramDTO, long pageId, String brandName) {
		setSiteFlag(paramDTO);
		if (paramDTO.startDate != 0) {
			paramDTO.startDate = ScheduleUtil.getSpecificBeginTime(paramDTO.startDate).getTimeInMillis();
		}
		if (paramDTO.endDate != 0) {
			paramDTO.endDate = ScheduleUtil.getSpecificEndTime(paramDTO.endDate).getTimeInMillis();
		}
		POListDTO poList = pageService.getSchedulePageList(paramDTO, pageId, brandName);

		for (PODTO poDTO : poList.getPoList()) {
			PODTO tmp = scheduleService.getScheduleByIdForCMSWithNoCache(poDTO.getPageDTO().getPage().getScheduleId());
			if (tmp.getScheduleDTO() == null || tmp.getScheduleDTO().getSchedule() == null) {
				logger.warn("Wrong or dirty data for page with id '" + poDTO.getPageDTO().getPage().getId() + "'!!");
				continue;
			}
			poDTO.setScheduleDTO(tmp.getScheduleDTO());
		}

		// ScheduleListVO vo = new ScheduleListVO();
		// vo.setPoList(poList);

		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleListForChl(UserLoginBean userLoginBean, long chlId, long saleSiteCode,
			long startId, int curPage, int pageSize) {
		ScheduleListVO vo = new ScheduleListVO();
		if (saleSiteCode <= 0) {
			POListDTO poList= new POListDTO();
			vo.setPoList(poList);
			return vo;
		}

		long saleSiteFlag = getSaleSiteFlag(saleSiteCode);
		POListDTO poList = scheduleService.getScheduleListForChl(chlId, saleSiteFlag, startId, curPage, pageSize);

		// poList = POBaseUtil.filterForEveryOne(userLoginBean, poList);
		// QrqmUtils.writeDyingPOListToCookie(userLoginBean);

		vo.setPoList(poList);
		ScheduleUtil.sortPOListForMainsite(poList);
		fillPOPromotion(poList);
		return vo;
	}

	@Override
	public ScheduleListVO getScheduleListForFuture(long userId, long chlId, long saleSiteCode, int daysAfter,
			int retSize) {
		long saleSiteFlag = getSaleSiteFlag(saleSiteCode);
		POListDTO poList = scheduleService.getScheduleListForFuture(chlId, saleSiteFlag, daysAfter, retSize);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(poList);

		return vo;
	}

	@Override
	public List<BrandDTO> getBrandListBySupplierIdList(List<Long> supplierIdList) {
		return brandService.getBrandListBySupplierIdList(supplierIdList);
	}

	@Override
	public boolean updateSchedulePage(PODTO poDto) {
		return pageService.updateSchedulePage(poDto);
	}

	@Override
	public PODTO getSchedulePageById(long pageId) {
		return pageService.getSchedulePageById(pageId);
	}

	@Override
	public PODTO getSchedulePageByScheduleId(long poId) {
		return pageService.getSchedulePageByScheduleId(poId);
	}

	@Override
	public JSONArray getPrdListByPrdIdList(List<Long> prdIdList, PODTO poDTO) {

		if (prdIdList == null || prdIdList.size() == 0) {
			return new JSONArray();
		}

		List<POProductDTO> prdList = poProductService.getProductDTOList(poDTO.getPageDTO().getPage().getScheduleId(),
				prdIdList);
		if (prdList == null) {
			return new JSONArray();
		}

		JSONArray result = new JSONArray();
		for (POProductDTO prd : prdList) {
			JSONObject item = new JSONObject();
			item.put("productName", prd.getProductName());
			item.put("productId", prd.getId() + "");
			item.put("listShowPicList", prd.getListShowPicList());
			item.put("salePrice", prd.getSalePrice());
			item.put("marketPrice", prd.getMarketPrice());

			result.add(item);
		}

		return result;
	}

	@Override
	public JSONObject getPrdListByPrdIdListForMainSite(List<Long> prdIdList, PODTO poDTO) {

		if (prdIdList == null || prdIdList.size() == 0) {
			return new JSONObject();
		}

		List<POProductDTO> prdList = poProductService.getProductDTOList(poDTO.getScheduleDTO().getSchedule().getId(),
				prdIdList);
		if (prdList == null) {
			return new JSONObject();
		}

		long brandId = poDTO.getPageDTO().getPage().getBrandId();
		String brandName = poDTO.getPageDTO().getPage().getBrandName();
		String brandNameEn = poDTO.getPageDTO().getPage().getBrandNameEn();

		JSONObject result = new JSONObject();
		for (POProductDTO prd : prdList) {
			JSONObject item = new JSONObject();
			item.put("productName", prd.getProductName());
			item.put("id", prd.getId());
			item.put("brandId", brandId);
			item.put("brandName", brandNameEn + " " + brandName);
			item.put("listShowPicList", prd.getListShowPicList());
			item.put("salePrice", prd.getSalePrice());
			item.put("marketPrice", prd.getMarketPrice());
			List<POSkuDTO> skuList = (List<POSkuDTO>) prd.getSKUList();

			List<Long> skuIdList = new ArrayList<Long>();
			for (POSkuDTO sku : skuList) {
				skuIdList.add(sku.getId());
			}
			Map<Long, Integer> cartMap = null;
			try {
				cartMap = cartService.getInventoryCount(skuIdList);
			} catch (Exception e) {
				cartMap = new HashMap<Long, Integer>();
				for (Long skuId : skuIdList) {
					cartMap.put(skuId, null);
				}
			}
			for (Long skuId : skuIdList) {
				if (!cartMap.containsKey(skuId)) {
					cartMap.put(skuId, null);
				}
			}

			Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
			List<SkuOrderStockDTO> orderList = skuOrderStockService.getSkuOrderStockDTOListBySkuIds(skuIdList);
			if (orderList == null) {
				for (Long skuId : skuIdList) {
					orderMap.put(skuId, null);
				}
			} else {
				for (SkuOrderStockDTO order : orderList) {
					orderMap.put(order.getSkuId(), order.getStockCount());
				}
				for (Long skuId : skuIdList) {
					if (!orderMap.containsKey(skuId)) {
						orderMap.put(skuId, null);
					}
				}
			}

			JSONArray skuArr = new JSONArray();
			for (POSkuDTO sku : skuList) {
				JSONObject skuItem = new JSONObject();
				skuItem.put("id", sku.getId());
				skuItem.put("size", sku.getSize());
				skuItem.put("num", sku.getSkuNum());
				int state = 0;
				long skuId = sku.getId();
				Integer v1 = cartMap.get(skuId);
				Integer v2 = orderMap.get(skuId);

				if (v1 == null || v2 == null) {
					state = 0;
				} else if (v1 == 0 && v2 == 0) {
					state = 3;
				} else if (v1 == 0 && v2 > 0) {
					state = 2;
				} else {
					state = 1;
				}

				skuItem.put("state", state);
				skuArr.add(skuItem);
			}
			item.put("skuList", skuArr);
			result.put(prd.getId() + "", item);
		}

		return result;
	}

	private void _fillAllowedAreaIdList(ScheduleCommonParamDTO paramDTO, String permission) {
		if (paramDTO.curSupplierAreaId == 0) {
			List<AreaDTO> areaList = getAllowedAreaList(permission);

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
		}
	}

	private int _getCntChecking(POStatusGetVO vo, int flag) {
		int total = 0;

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.curSupplierAreaId = vo.getCurSupplierAreaId();
		paramDTO.startDate = vo.getStartDate();
		paramDTO.endDate = vo.getEndDate();
		if (paramDTO.startDate != 0) {
			paramDTO.startDate = ScheduleUtil.getSpecificBeginTime(paramDTO.startDate).getTimeInMillis();
		}
		if (paramDTO.endDate != 0) {
			paramDTO.endDate = ScheduleUtil.getSpecificEndTime(paramDTO.endDate).getTimeInMillis();
		}
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.PASSED);
		paramDTO.poStatusList = poStatusList;

		_fillAllowedAreaIdList(paramDTO, "schedule:place");
		setSiteFlag(paramDTO);

		POListDTO poList = null;
		if (vo.getFlag() == 0) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 0);
		} else if (vo.getFlag() == 1) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 1);
		} else if (vo.getFlag() == 2) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 2);
		} else if (vo.getFlag() == 3) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 3);
		} else if (vo.getFlag() == 4) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 4);
		} else if (vo.getFlag() == 5) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 5);
		}

		if (poList == null || poList.getPoList().size() == 0) {
			return 0;
		}

		List<Long> poIdList2 = new ArrayList<Long>();
		for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
			poIdList2.add(poList.getPoList().get(i).getScheduleDTO().getSchedule().getId());
		}

		if (flag == 3) {
			for (PODTO poDTO : poList.getPoList()) {
				if (poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPage() == 0) {
					++total;
				}
			}
		} else if (flag == 4) {
			for (PODTO poDTO : poList.getPoList()) {
				if (poDTO.getScheduleDTO().getScheduleVice().getFlagAuditBanner() == 0) {
					++total;
				}
			}
		} else if (flag == 5) {
			long now = System.currentTimeMillis();
			for (PODTO poDTO : poList.getPoList()) {
				Schedule schedule = poDTO.getScheduleDTO().getSchedule();
				ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
				//if (schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
				if (vice.getFlagAuditPrdqd() == StatusType.APPROVAL.getIntValue()
						&& vice.getFlagAuditPrdzl() == StatusType.APPROVAL.getIntValue()
						&& vice.getFlagAuditPage() == 1
						&& vice.getFlagAuditBanner() == 1
						&& schedule.getStatus() == ScheduleState.PASSED
						&& schedule.getStartTime() > now) {
					// ready for online
					++total;
				}
			}
		} else {
			List<Long> poIdList = new ArrayList<Long>();
			for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
				poIdList.add(poList.getPoList().get(i).getScheduleDTO().getSchedule().getId());
			}

			// get audit status for each po
			List<ScheduleAuditData> auditData = new ArrayList<ScheduleAuditData>();
			for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
				ScheduleAuditData data = poProductService.getItemScheduleAuditData(poList.getPoList().get(i)
						.getScheduleDTO().getSchedule().getId());
				auditData.add(data);
			}

			for (int i = 0, j = auditData.size(); i < j; i++) {
				ScheduleAuditData data = auditData.get(i);
				if (flag == 1) {
					if (!data.isSkuPass()) {
						++total;
					}
				} else if (flag == 2) {
					if (!data.isProductPass()) {
						++total;
					}
				}
			}
		}

		return total;
	}

	private POListDTO _getOriginalPoListDTO(ScheduleCommonParamDTO paramDTO, POStatusGetVO vo) {
		POListDTO poList = null;
		if (vo.getFlag() == 0) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 0);
		} else if (vo.getFlag() == 1) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 1);
		} else if (vo.getFlag() == 2) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 2);
		} else if (vo.getFlag() == 3) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 3);
		} else if (vo.getFlag() == 4) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 4);
		} else if (vo.getFlag() == 5) {
			poList = scheduleService.getScheduleListByStartEndTimeWithType(paramDTO, 5);
		}

		if (poList.getPoList().size() == 0) {
			return poList;
		}

		_setPOSkuStatus(poList);

		return poList;
	}

	private void _setPOSkuStatus(POListDTO poList) {
		List<Long> poIdList = new ArrayList<Long>();
		for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
			poIdList.add(poList.getPoList().get(i).getScheduleDTO().getSchedule().getId());
		}

		// get audit status for each po
		List<ScheduleAuditData> auditData = new ArrayList<ScheduleAuditData>();
		for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
			ScheduleAuditData data = poProductService.getItemScheduleAuditData(poList.getPoList().get(i)
					.getScheduleDTO().getSchedule().getId());
			auditData.add(data);
		}
		for (int i = 0, j = auditData.size(); i < j; i++) {
			ScheduleAuditData data = auditData.get(i);
			PODTO poDTO = poList.getPoList().get(i);

			poDTO.setSkuPass(data.isSkuPass());
			poDTO.setProductPass(data.isProductPass());
			poDTO.setPassItemNum(data.getPassItemNum());
			poDTO.setPassSkuNum(data.getPassSkuNum());
			poDTO.setPassProductNum(data.getPassProductNum());
		}
	}
	
	private void _filterPOForReadOnline(ScheduleCommonParamDTO paramDTO, List<PODTO> filteredPOList,
			POStatusGetVO vo, int filterFlag, int i) {
		//paramDTO.curPage = (paramDTO.curPage + 1) * paramDTO.pageSize;
		paramDTO.curPage = i * paramDTO.pageSize;
		POListDTO tmpPOList = _getOriginalPoListDTO(paramDTO, vo);

		long now = System.currentTimeMillis();
		for (PODTO poDTO : tmpPOList.getPoList()) {
			Schedule schedule = poDTO.getScheduleDTO().getSchedule();
			ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
			if (schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
					&& vice.getFlagAuditPage() == 1
					&& vice.getFlagAuditBanner() == 1
					&& schedule.getStatus() == ScheduleState.PASSED
					&& schedule.getStartTime() > now) {
				// ready for online
				filteredPOList.add(poDTO);
			}
		}
	}

	private void _filterPOForPrdListOrPrdInfo(ScheduleCommonParamDTO paramDTO, List<PODTO> filteredPOList,
			POStatusGetVO vo, int filterFlag, int i) {
		// paramDTO.curPage = (paramDTO.curPage + 1) * paramDTO.pageSize;
		paramDTO.curPage = i * paramDTO.pageSize;
		POListDTO tmpPOList = _getOriginalPoListDTO(paramDTO, vo);

		for (PODTO poDTO : tmpPOList.getPoList()) {
			if (filterFlag == 1) {
				if (!poDTO.isSkuPass()) {
					filteredPOList.add(poDTO);
				}
			} else if (filterFlag == 2) {
				if (!poDTO.isProductPass()) {
					filteredPOList.add(poDTO);
				}
			}
		}
	}

	@Override
	public JSONObject getScheduleListByStartEndTime(POStatusGetVO vo) {
		JSONObject json = new JSONObject();

		int cntUncheckedPrdList = _getCntChecking(vo, 1);
		int cntUncheckedPrdInfo = _getCntChecking(vo, 2);
		int cntUncheckedPage = _getCntChecking(vo, 3);
		int cntUncheckedBanner = _getCntChecking(vo, 4);
		int cntReadyOnline = _getCntChecking(vo, 5);

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.curSupplierAreaId = vo.getCurSupplierAreaId();
		paramDTO.startDate = vo.getStartDate();
		paramDTO.endDate = vo.getEndDate();
		paramDTO.curPage = vo.getOffset();
		paramDTO.pageSize = vo.getLimit();
		if (paramDTO.startDate != 0) {
			paramDTO.startDate = ScheduleUtil.getSpecificBeginTime(paramDTO.startDate).getTimeInMillis();
		}
		if (paramDTO.endDate != 0) {
			paramDTO.endDate = ScheduleUtil.getSpecificEndTime(paramDTO.endDate).getTimeInMillis();
		}
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		// if (vo.getFlag() == 0) { // not filter
		poStatusList.add(ScheduleState.PASSED);
		poStatusList.add(ScheduleState.BACKEND_PASSED);
		poStatusList.add(ScheduleState.OFFLINE);
		// } else {
		// poStatusList.add(ScheduleState.PASSED);
		// }

		paramDTO.poStatusList = poStatusList;
		_fillAllowedAreaIdList(paramDTO, "schedule:place");
		setSiteFlag(paramDTO);

		// get all valid PO
		POListDTO poList = _getOriginalPoListDTO(paramDTO, vo);
		if (poList.getPoList().size() == 0) {
			return ScheduleUtil.generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR,
					"Cannot find any PO!!!");
		}

		// filter PO list
		int filterFlag = vo.getFlag();
		List<PODTO> filteredPOList = new ArrayList<PODTO>();
		if (filterFlag == 0) { // not filter
			for (PODTO poDTO : poList.getPoList()) {
				filteredPOList.add(poDTO);
			}
		} else if (filterFlag == 1) { // filter by prd list audit status
			int oldCurPage = paramDTO.curPage;
			int i = 0;
			while (filteredPOList.size() < cntUncheckedPrdList) {
				_filterPOForPrdListOrPrdInfo(paramDTO, filteredPOList, vo, 1, i);
				i++;
			}
			POListDTO tmpPOList = new POListDTO();
			tmpPOList.setPoList(filteredPOList);
			ScheduleUtil.POListPager(tmpPOList, oldCurPage, paramDTO.pageSize);
			filteredPOList = tmpPOList.getPoList();
		} else if (filterFlag == 2) { // filter by prd info audit status
			int oldCurPage = paramDTO.curPage;
			int i = 0;
			while (filteredPOList.size() < cntUncheckedPrdInfo) {
				_filterPOForPrdListOrPrdInfo(paramDTO, filteredPOList, vo, 2, i);
				i++;
			}
			POListDTO tmpPOList = new POListDTO();
			tmpPOList.setPoList(filteredPOList);
			ScheduleUtil.POListPager(tmpPOList, oldCurPage, paramDTO.pageSize);
			filteredPOList = tmpPOList.getPoList();
		} else if (filterFlag == 3) { // filter by page audit status
			for (PODTO poDTO : poList.getPoList()) {
				if (poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPage() == 0) {
					filteredPOList.add(poDTO);
				}
			}
		} else if (filterFlag == 4) { // filter by banner audit status
			for (PODTO poDTO : poList.getPoList()) {
				if (poDTO.getScheduleDTO().getScheduleVice().getFlagAuditBanner() == 0) {
					filteredPOList.add(poDTO);
				}
			}
		} else if (filterFlag == 5) {
			int oldCurPage = paramDTO.curPage;
			int i = 0;
			while (filteredPOList.size() < cntReadyOnline && filteredPOList.size() < paramDTO.pageSize) {
				_filterPOForReadOnline(paramDTO, filteredPOList, vo, 5, i);
				i++;
			}
			POListDTO tmpPOList = new POListDTO();
			tmpPOList.setPoList(filteredPOList);
			ScheduleUtil.POListPager(tmpPOList, oldCurPage, paramDTO.pageSize);
			filteredPOList = tmpPOList.getPoList();
		}

		Collections.sort(filteredPOList, new Comparator<PODTO>() {
			@Override
			public int compare(PODTO po1, PODTO po2) {
				long diff = po1.getScheduleDTO().getSchedule().getStartTime()
						- po2.getScheduleDTO().getSchedule().getStartTime();
				if (diff > 0L) {
					return 1;
				} else if (diff == 0L) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		// set PO status
		ScheduleUtil.setPOMainStatus(filteredPOList);

		// generate PO JSON for front web end
		int total = poList.getTotal(); // for attention
		JSONArray list = new JSONArray();
		JSONObject result = new JSONObject();
		json.put("code", ScheduleUtil.CODE_OK);
		json.put("result", result);
		result.put("cntUncheckedPrdList", cntUncheckedPrdList);
		result.put("cntUncheckedPrdInfo", cntUncheckedPrdInfo);
		result.put("cntUncheckedPage", cntUncheckedPage);
		result.put("cntUncheckedBanner", cntUncheckedBanner);
		result.put("cntReadyOnline", cntReadyOnline);
		if (filterFlag == 0) {
			result.put("total", total);
		} else if (filterFlag == 1) {
			result.put("total", cntUncheckedPrdList);
		} else if (filterFlag == 2) {
			result.put("total", cntUncheckedPrdInfo);
		} else if (filterFlag == 3) {
			result.put("total", cntUncheckedPage);
		} else if (filterFlag == 4) {
			result.put("total", cntUncheckedBanner);
		} else if (filterFlag == 5) {
			result.put("total", cntReadyOnline);
		}

		List<AreaDTO> areaList = getAllowedAreaList("schedule:place");
		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		for (AreaDTO area : areaList) {
			areaMap.put(area.getId(), area.getAreaName());
		}

		result.put("list", list);
		Map<Long, List<JSONObject>> map = new ConcurrentHashMap<Long, List<JSONObject>>();
		for (PODTO poDTO : filteredPOList) {
			JSONObject item = new JSONObject();
			Schedule schedule = poDTO.getScheduleDTO().getSchedule();
			ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
			item.put("id", schedule.getId());
			BusinessDTO dto = businessFacade.getBusinessById(schedule.getSupplierId());
			if (dto == null) {
				logger.warn("PO '" + schedule.getId() + "'s supplier '" + schedule.getSupplierId()
						+ "' doesnot exist!!!");
				item.put("supplierName", "");
			} else {
				// item.put("supplierName", schedule.getSupplierName());
				item.put("supplierName", dto.getBusinessAccount());
			}
			item.put("title", schedule.getTitle());

			List<ScheduleSiteRela> saleSiteListInPO = poDTO.getScheduleDTO().getSiteRelaList();
			List<IdNameBean> saleSiteList = new ArrayList<IdNameBean>();
			for (ScheduleSiteRela saleSite : saleSiteListInPO) {
				IdNameBean bean = new IdNameBean();
				bean.setId(saleSite.getSaleSiteId() + POBaseUtil.NULL_STR);
				bean.setName(areaMap.get(saleSite.getSaleSiteId()));
				saleSiteList.add(bean);
			}
			item.put("saleSiteList", saleSiteList);

			item.put("startTime", schedule.getStartTime());
			item.put("endTime", schedule.getEndTime());
			item.put("createDate", schedule.getCreateTimeForLogic());
			item.put("createPerson", vice.getCreateUser());
			item.put("prdListStatus", poDTO.isSkuPass());
			item.put("prdInfoStatus", poDTO.isProductPass());
			item.put("pageStatus", vice.getFlagAuditPage() == 1 ? true : false);
			item.put("bannerStatus", vice.getFlagAuditBanner() == 1 ? true : false);
			item.put("itemPassRate", poDTO.getPassItemNum() + " / " + vice.getProductTotalCnt());
			item.put("productPassRate", poDTO.getPassProductNum() + " / " + vice.getUnitCnt());
			item.put("skuPassRate", poDTO.getPassSkuNum() + " / " + vice.getSkuCnt());
			item.put("poStatus", poDTO.getPoStatus());
			item.put("poFollowerUserName", vice.getPoFollowerUserName());
			item.put("brandName", ScheduleUtil.getCombinedBrandName(schedule.getBrandNameEn(), schedule.getBrandName()));

			long poStartTime = schedule.getStartTime();
			Calendar cc = Calendar.getInstance();
			cc.setTimeInMillis(poStartTime);
			cc.set(Calendar.HOUR_OF_DAY, 0);
			cc.set(Calendar.MINUTE, 0);
			cc.set(Calendar.SECOND, 0);
			cc.set(Calendar.MILLISECOND, 0);
			poStartTime = cc.getTimeInMillis();
			if (!map.containsKey(poStartTime)) {
				map.put(poStartTime, new ArrayList<JSONObject>());
			}
			map.get(poStartTime).add(item);
		}

		// sort PO by startTime
		Set<Long> keySet = map.keySet();
		List<Long> keyList = new ArrayList<Long>();
		for (Long key : keySet) {
			keyList.add(key);
		}
		Collections.sort(keyList);

		// convert map to JSON
		for (Long key : keyList) {
			JSONObject group = new JSONObject();
			group.put("date", key);
			group.put("poList", JSON.toJSON(map.get(key)));

			list.add(group);
		}

		return json;
	}

	@Override
	public List<Schedule> getScheduleListByTime(long siteId, long startTime, long endTime) {
		long saleSiteFlag = getSaleSiteFlag(siteId);
		return scheduleService.getScheduleListByTime(saleSiteFlag, startTime, endTime);
	}

	@Override
	public JSONObject getStatisticData(ScheduleCommonParamDTO paramDTO, String url) {
		JSONObject summary = new JSONObject();

		// String url = "http://sj.baiwandian.cn/getLatestPoInfo";
		String paramSupplierId = "?supplierId=" + paramDTO.supplierId;
		long timestamp = System.currentTimeMillis();
		String paramTimestamp = "&timestamp=" + timestamp;
		String key = EncryptUtil.encrypt(paramDTO.supplierId + "_" + timestamp);
		String paramKey = "&key=" + key;
		url = url + paramSupplierId + paramTimestamp + paramKey;
		logger.debug("URL: " + url);

		String response = "";
		boolean flagReq = false;
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2000);
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Request PO magic cube data failure!!!");
				flagReq = false;
			} else {
				response = getMethod.getResponseBodyAsString();
				logger.debug("Success get PO magic cube data: " + response);
				flagReq = true;
			}
		} catch (Exception e) {
			logger.error("Request PO magic cube data failure, cause: " + e.getMessage());
			flagReq = false;
		} finally {
			getMethod.releaseConnection();
		}

		ScheduleMagicCubeDTO mcDTO = null;
		if (flagReq) {
			try {
				JSONObject json = JSON.parseObject(response);
				JSONObject data = json.getJSONObject("relatedObject");
				boolean success = json.getBooleanValue("success");
				if (success && data != null) {
					ScheduleMagicCube mc = new ScheduleMagicCube();
					mc.setCurSupplierAreaId(paramDTO.curSupplierAreaId);
					mc.setScheduleId(paramDTO.poId);
					mc.setSupplierId(paramDTO.supplierId);
					mc.setLogDay(System.currentTimeMillis());
					mc.setPv(data.getLongValue("po_total_pv"));
					mc.setUv(data.getLongValue("po_total_uv"));
					mc.setSale(data.getBigDecimal("po_total_sales"));
					mc.setSaleCnt(data.getLongValue("po_total_sales_num"));
					String ratio = data.getString("po_sales_stock_ratio");
					if (ratio.indexOf("%") != -1) {
						ratio = ratio.substring(0, ratio.indexOf("%"));
					}
					double dRatio = Double.parseDouble(ratio);
					mc.setSaleRate(new BigDecimal(dRatio));
					mc.setBuyerCnt(data.getLongValue("po_buyer_num"));
					mc.setSkuCnt(data.getIntValue("total_sku"));
					mc.setSupplyMoney(data.getBigDecimal("stock_value"));

					mcDTO = new ScheduleMagicCubeDTO();
					mcDTO.setMagicCube(mc);

					// save to DB
					scheduleMagicCubeService.deleteScheduleMagicCubeBySupplierId(paramDTO.supplierId);
					scheduleMagicCubeService.saveScheduleMagicCube(mcDTO);
				} else {
					logger.error("Failure to get magic cube data: " + response);
					mcDTO = scheduleMagicCubeService.getScheduleMagicCubeBySupplierId(paramDTO.supplierId);
				}
			} catch (Exception e) {
				logger.error("Failure parse response got from magic cube!!: " + response, e);
				mcDTO = scheduleMagicCubeService.getScheduleMagicCubeBySupplierId(paramDTO.supplierId);
			}
		} else {
			// get data from local DB
			mcDTO = scheduleMagicCubeService.getScheduleMagicCubeBySupplierId(paramDTO.supplierId);
		}

		if (mcDTO == null || mcDTO.getMagicCube() == null) {
			ScheduleMagicCube mc = new ScheduleMagicCube();
			mcDTO = new ScheduleMagicCubeDTO();
			mcDTO.setMagicCube(mc);
			// return summary;
		}

		summary.put("sale", mcDTO.getMagicCube().getSale());
		summary.put("saleCount", mcDTO.getMagicCube().getSaleCnt());
		summary.put("buyCount", mcDTO.getMagicCube().getBuyerCnt());
		summary.put("saleRate", mcDTO.getMagicCube().getSaleRate());
		summary.put("supply", mcDTO.getMagicCube().getSupplyMoney());
		summary.put("skuCount", mcDTO.getMagicCube().getSkuCnt());
		summary.put("UVCount", mcDTO.getMagicCube().getUv());
		summary.put("PVCount", mcDTO.getMagicCube().getPv());

		// test
		{
			// summary.put("sale", 123384);
			// summary.put("saleCount", 1243);
			// summary.put("buyCount", 1000);
			// summary.put("saleRate", 70);
			// summary.put("supply", 120000);
			// summary.put("skuCount", 800);
			// summary.put("UVCount", 1200043);
			// summary.put("PVCount", 2400043);
		}
		return summary;
	}

	private void _fillOnlinePOData(JSONObject data, Schedule schedule) {
		data.put("id", schedule.getId() + POBaseUtil.NULL_STR);
		int pick = pickSkuFacade.unPickCountOfPoOrderId(schedule.getId() + "");
		int waiting = pickSkuFacade.unShipCountOfPoOrderId(schedule.getId() + "");
		PoRetrunSkuQueryParamDTO params = new PoRetrunSkuQueryParamDTO();
		params.addPoOrderId(schedule.getId());
		params.addState(PoReturnOrderState.NEW);
		long returnOrderCnt = poReturnService.countPoReturnOrderSkuCount(params);
		// int receipt =
		// invoiceService.getInvoiceInOrdSupplierCountOfInit(schedule.getSupplierId());
		data.put("pick", pick);
		data.put("waiting", waiting);
		data.put("return", returnOrderCnt);
		// data.put("receipt", receipt);
	}

	@Override
	public int getInvoiceInOrdSupplierCountOfInit(long supplierId) {
		return invoiceService.getInvoiceInOrdSupplierCountOfInit(supplierId);
	}

	@Override
	public JSONArray getOnlinePOData(ScheduleCommonParamDTO paramDTO) {
		JSONArray onlineScheduleList = new JSONArray();

		POListDTO poList = scheduleService.getScheduleListByStartEndTime(paramDTO);
		if (poList.getPoList().size() == 0) {
			return onlineScheduleList;
		}

		for (PODTO po : poList.getPoList()) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			ScheduleVice vice = po.getScheduleDTO().getScheduleVice();
			ScheduleUtil.setPOShowTime(schedule);
			long now = System.currentTimeMillis();

			if (schedule.getStatus() == ScheduleState.BACKEND_PASSED) {
				if (schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
						&& vice.getFlagAuditBanner() == 1 && vice.getFlagAuditPage() == 1) {
					JSONObject data = new JSONObject();
					if (schedule.getStartTime() <= now && schedule.getEndTime() > now) { // online,
																							// in
																							// sale
						data.put("status", 5);
						data.put("title", schedule.getTitle());
						_fillOnlinePOData(data, schedule);
						onlineScheduleList.add(data);
					}

					if (schedule.getEndTime() < now) { // PO offline
						data.put("status", 4);
						data.put("title", schedule.getTitle());
						_fillOnlinePOData(data, schedule);
						onlineScheduleList.add(data);
					}
				}
			}

			if (schedule.getStatus() == ScheduleState.OFFLINE) { // PO offline
				JSONObject data = new JSONObject();
				data.put("status", 4);
				data.put("title", schedule.getTitle());
				_fillOnlinePOData(data, schedule);
				onlineScheduleList.add(data);
			}
		}

		return onlineScheduleList;
	}

	@Override
	public JSONArray getOfflinePOData(ScheduleCommonParamDTO paramDTO) {
		JSONArray unlineScheduleList = new JSONArray();

		POListDTO poList = scheduleService.getScheduleListByStartEndTime(paramDTO);
		if (poList.getPoList().size() == 0) {
			return unlineScheduleList;
		}

		_setPOSkuStatus(poList);
		ScheduleUtil.setPOMainStatus(poList.getPoList());

		for (PODTO po : poList.getPoList()) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			ScheduleVice vice = po.getScheduleDTO().getScheduleVice();
			ScheduleUtil.setPOShowTime(schedule);
			long now = System.currentTimeMillis();
			if (schedule.getStatus() == ScheduleState.BACKEND_PASSED) {
				if (schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
						&& vice.getFlagAuditBanner() == 1 && vice.getFlagAuditPage() == 1) {
					if (schedule.getStartTime() <= now && schedule.getEndTime() > now) { // online,
																							// in
																							// sale
						continue;
					}

					if (schedule.getEndTime() < now) { // PO offline
						continue;
					}
				}
			}

			if (schedule.getStatus() == ScheduleState.OFFLINE) { // offline
				continue;
			}
			ScheduleBanner banner = null;
			try {
				banner = bannerService.getScheduleBannerByScheduleId(schedule.getId()).getBannerDTO().getBanner();
			} catch (Exception e) {
				logger.error("===== Wrong or dirty banner data: poId=" + schedule.getId());
			}
			SchedulePage page = null;
			try {
				page = pageService.getSchedulePageByScheduleId(schedule.getId()).getPageDTO().getPage();
			} catch (Exception e) {
				logger.error("====== Wrong or dirty page data: poId=" + schedule.getId());
			}
			JSONObject item = new JSONObject();
			item.put("id", schedule.getId() + POBaseUtil.NULL_STR);
			item.put("title", schedule.getTitle());
			item.put("status", po.getPoStatus());
			item.put("startTime", schedule.getStartTime());
			item.put("prdStatus", schedule.getFlagAuditPrdList());
			if (page == null) {
				item.put("pageStatus", CheckState.DRAFT.getIntValue());
			} else {
				item.put("pageStatus", page.getStatus().getIntValue());
			}

			if (banner == null) {
				item.put("bannerStatus", CheckState.DRAFT.getIntValue());
			} else {
				item.put("bannerStatus", banner.getStatus().getIntValue());
			}

			unlineScheduleList.add(item);
		}

		return unlineScheduleList;
	}

	@Override
	public JSONArray getCheckingPOSelfData(ScheduleCommonParamDTO paramDTO) {
		JSONArray pendingPOList = new JSONArray();

		List<AreaDTO> areaList = getAllowedAreaList("schedule:audit");
		Map<Long, String> areaMap = ScheduleUtil.convertAreaList2Map(areaList);
		if (areaMap.size() == 0) { // no permission for any site
			return pendingPOList;
		}

		POListDTO poList = scheduleService.getScheduleListByStartEndTime(paramDTO);
		if (poList.getPoList().size() == 0) {
			return pendingPOList;
		}

		Map<String, Integer> cntMap = new ConcurrentHashMap<String, Integer>();
		for (PODTO po : poList.getPoList()) {
			List<ScheduleSiteRela> saleSiteListInPO = po.getScheduleDTO().getSiteRelaList();
			for (ScheduleSiteRela saleSiteInPO : saleSiteListInPO) {
				String areaName = areaMap.get(saleSiteInPO.getSaleSiteId());
				if (areaName == null) {
					logger.info("Wrong area id for PO '" + po.getScheduleDTO().getSchedule().getId() + "'!!!");
					continue;
				}
				if (!cntMap.containsKey(areaName)) {
					cntMap.put(areaName, 0);
				}

				int val = cntMap.get(areaName);
				++val;
				cntMap.put(areaName, val);
			}
		}

		for (Iterator<Long> iter = areaMap.keySet().iterator(); iter.hasNext();) {
			String areaName = areaMap.get(iter.next());
			JSONObject item = new JSONObject();
			item.put("siteName", areaName);
			if (cntMap.get(areaName) == null) {
				item.put("count", 0);
			} else {
				item.put("count", cntMap.get(areaName));
			}

			pendingPOList.add(item);
		}

		return pendingPOList;
	}

	private void _incMapCnt(Map<String, Integer> map, String key) {
		if (!map.containsKey(key)) {
			map.put(key, 0);
		}

		int val = map.get(key);
		++val;
		map.put(key, val);
	}

	@Override
	public JSONArray getCheckingPOOthersData(ScheduleCommonParamDTO paramDTO) {
		JSONArray pendingAuditList = new JSONArray();

		List<AreaDTO> areaList1 = getAllowedAreaList("audit:productlist");
		List<AreaDTO> areaList2 = getAllowedAreaList("audit:product");
		List<AreaDTO> areaList3 = getAllowedAreaList("audit:banner");
		List<AreaDTO> areaList4 = getAllowedAreaList("audit:decorate");
		List<AreaDTO> areaList5 = getAllowedAreaList("audit:brand");

		List<AreaDTO> areaList = areaList1;
		if (areaList1.size() == 0 && areaList2.size() == 0 && areaList3.size() == 0 && areaList4.size() == 0
				&& areaList5.size() == 0) { // no permission for any site
			return pendingAuditList;
		} else {
			int max = areaList1.size();
			if (areaList2.size() > max) {
				areaList = areaList2;
			}
			if (areaList3.size() > max) {
				areaList = areaList3;
			}
			if (areaList4.size() > max) {
				areaList = areaList4;
			}
			if (areaList5.size() > max) {
				areaList = areaList5;
			}
		}
		Map<Long, String> areaMap = ScheduleUtil.convertAreaList2Map(areaList);
		if (areaMap.size() == 0) {
			return pendingAuditList;
		}

		POListDTO poList = scheduleService.getScheduleListByStartEndTime(paramDTO);
		if (poList.getPoList().size() == 0) {
			return pendingAuditList;
		}

		Map<String, Integer> prdListCntMap = new ConcurrentHashMap<String, Integer>();
		Map<String, Integer> prdInfoCntMap = new ConcurrentHashMap<String, Integer>();
		Map<String, Integer> pageCntMap = new ConcurrentHashMap<String, Integer>();
		Map<String, Integer> bannerCntMap = new ConcurrentHashMap<String, Integer>();
		Map<String, Integer> brandCntMap = new ConcurrentHashMap<String, Integer>();

		List<Long> poIdList = new ArrayList<Long>();
		for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
			poIdList.add(poList.getPoList().get(i).getScheduleDTO().getSchedule().getId());
		}

		// get audit status for each po
		List<ScheduleAuditData> auditData = new ArrayList<ScheduleAuditData>();
		for (int i = 0, j = poList.getPoList().size(); i < j; i++) {
			ScheduleAuditData data = poProductService.getItemScheduleAuditData(poList.getPoList().get(i)
					.getScheduleDTO().getSchedule().getId());
			auditData.add(data);
		}
		for (int i = 0, j = auditData.size(); i < j; i++) {
			ScheduleAuditData data = auditData.get(i);
			PODTO poDTO = poList.getPoList().get(i);

			poDTO.setSkuPass(data.isSkuPass());
			poDTO.setProductPass(data.isProductPass());
		}

		for (PODTO po : poList.getPoList()) {
			List<ScheduleSiteRela> saleSiteListInPO = po.getScheduleDTO().getSiteRelaList();
			for (ScheduleSiteRela saleSiteInPO : saleSiteListInPO) {
				String areaName = areaMap.get(saleSiteInPO.getSaleSiteId());
				if (areaName == null) {
					logger.info("Wrong area id for PO '" + po.getScheduleDTO().getSchedule().getId() + "'!!!");
					continue;
				}

				if (!po.isSkuPass()) {
					_incMapCnt(prdListCntMap, areaName);
				}

				if (!po.isProductPass()) {
					_incMapCnt(prdInfoCntMap, areaName);
				}
			}
		}

		// get page audit cnt
		List<SchedulePageDTO> pageList = pageService.getSchedulePageList(poIdList);
		for (SchedulePageDTO page : pageList) {
			List<ScheduleSiteRela> saleSiteListInPO = this.getSaleSiteListInPO(poList, page.getPage().getScheduleId());
			for (ScheduleSiteRela saleSiteInPO : saleSiteListInPO) {
				String areaName = areaMap.get(saleSiteInPO.getSaleSiteId());
				if (areaName == null) {
					logger.info("Wrong area id for PO page '" + page.getPage().getId() + "'!!!");
					continue;
				}
				if (page.getPage().getStatus() == CheckState.CHECKING) {
					_incMapCnt(pageCntMap, areaName);
				}
			}
		}

		// get banner audit cnt
		List<ScheduleBannerDTO> bannerList = bannerService.getScheduleBannerList(poIdList);
		for (ScheduleBannerDTO banner : bannerList) {
			List<ScheduleSiteRela> saleSiteListInPO = this.getSaleSiteListInPO(poList, banner.getBanner().getScheduleId());
			for (ScheduleSiteRela saleSiteInPO : saleSiteListInPO) {
				String areaName = areaMap.get(saleSiteInPO.getSaleSiteId());
				if (areaName == null) {
					logger.info("Wrong area id for PO banner '" + banner.getBanner().getId() + "'!!!");
					continue;
				}
				if (banner.getBanner().getStatus() == CheckState.CHECKING) {
					_incMapCnt(bannerCntMap, areaName);
				}
			}
		}

		// get brand audit cnt
		Map<Long, Integer> brandList = brandService
				.getAuditBrandCountsByAreaList(new ArrayList<Long>(areaMap.keySet()));
		if (brandList != null) {
			for (Iterator<Long> iter = areaMap.keySet().iterator(); iter.hasNext();) {
				long key = iter.next();
				if (brandList.containsKey(key)) {
					brandCntMap.put(areaMap.get(key), brandList.get(key));
				} else {
					brandCntMap.put(areaMap.get(key), 0);
				}
			}
		}

		// summary all cnt
		for (Iterator<Long> iter = areaMap.keySet().iterator(); iter.hasNext();) {
			String areaName = areaMap.get(iter.next());
			JSONObject item = new JSONObject();
			item.put("siteName", areaName);

			if (prdListCntMap.containsKey(areaName)) {
				item.put("productListCount", prdListCntMap.get(areaName));
			} else {
				item.put("productListCount", 0);
			}

			if (prdInfoCntMap.containsKey(areaName)) {
				item.put("meterialCount", prdInfoCntMap.get(areaName));
			} else {
				item.put("meterialCount", 0);
			}

			if (pageCntMap.containsKey(areaName)) {
				item.put("pageCount", pageCntMap.get(areaName));
			} else {
				item.put("pageCount", 0);
			}

			if (brandCntMap.containsKey(areaName)) {
				item.put("brandCount", brandCntMap.get(areaName));
			} else {
				item.put("brandCount", 0);
			}

			if (bannerCntMap.containsKey(areaName)) {
				item.put("spreadCount", bannerCntMap.get(areaName));
			} else {
				item.put("spreadCount", 0);
			}

			pendingAuditList.add(item);
		}

		return pendingAuditList;
	}

	private List<ScheduleSiteRela> getSaleSiteListInPO(POListDTO poList, long poId) {
		for (PODTO po : poList.getPoList()) {
			if (po.getScheduleDTO().getSchedule().getId() == poId) {
				return po.getScheduleDTO().getSiteRelaList();
			}
		}

		return new ArrayList<ScheduleSiteRela>();
	}

	@Override
	public JSONObject getPOPromotionData(ScheduleCommonParamDTO paramDTO) {
		JSONObject result = new JSONObject();

		long uid = SecurityContextUtils.getUserId();
		// coupon
		int couponCount = couponService.getCouponCount(agentService.findAgentSiteIdsByPermission(uid, "location:area"),
				StateConstants.COMMIT, null);

		// red packet
		int giftCount = redPacketService.getRedPacketCount(-1, StateConstants.COMMIT, "");

		// activitys
		JSONArray activitys = new JSONArray();
		List<AreaDTO> areaList = getAllowedAreaList("promotion:activity");
		if (areaList.size() != 0) { // have permissions
			Map<Long, String> areaMap = ScheduleUtil.convertAreaList2Map(areaList);
			Map<Integer, Integer> promotionMap = promotionService.getCommitMap();
			if (promotionMap != null) {
				for (Iterator<Long> iter = areaMap.keySet().iterator(); iter.hasNext();) {
					int siteId = iter.next().intValue();
					JSONObject activity = new JSONObject();
					activity.put("siteName", areaMap.get((long) siteId));
					if (promotionMap.get(siteId) == null) {
						activity.put("activity", 0);
					} else {
						activity.put("activity", promotionMap.get(siteId));
					}

					activitys.add(activity);
				}
			}
		}

		result.put("activitys", activitys);
		result.put("couponCount", couponCount);
		result.put("giftCount", giftCount);

		return result;
	}

	@Override
	public JSONArray getCheckingOrderData(ScheduleCommonParamDTO paramDTO) {

		JSONArray orderAuditList = new JSONArray();

		List<AreaDTO> areaList1 = getAllowedAreaList("order:topay");
		List<AreaDTO> areaList2 = getAllowedAreaList("order:return");

		List<AreaDTO> areaList = areaList1;
		if (areaList1.size() == 0 && areaList2.size() == 0) { // no permission
																// for any site
			return orderAuditList;
		} else {
			int max = areaList1.size();
			if (areaList2.size() > max) {
				areaList = areaList2;
			}
		}
		Map<Long, String> areaMap = ScheduleUtil.convertAreaList2Map(areaList);
		if (areaMap.size() == 0) {
			return orderAuditList;
		}

		Map<Integer, Long> toPayMap = codAuditService.waitingAuditCount();
		if (toPayMap == null) {
			toPayMap = new ConcurrentHashMap<Integer, Long>(0);
		}

		Map<Integer, Long> returnGoodsMap = retPkgQueryService.waitingReturnAuditCount();
		if (returnGoodsMap == null) {
			returnGoodsMap = new ConcurrentHashMap<Integer, Long>(0);
		}

		// TODO nobody currently develop this module
		Map<Integer, Long> returnMoneyMap = retPkgQueryService.waitingReturnCountOfCOD();
		if (null == returnMoneyMap) {
			returnMoneyMap = new ConcurrentHashMap<Integer, Long>(0);
		}
		// if (returnMoneyMap == null) {
		// returnMoneyMap = new ConcurrentHashMap<Integer, Long>(0);
		// }

		for (Iterator<Long> iter = areaMap.keySet().iterator(); iter.hasNext();) {
			int key = iter.next().intValue();
			JSONObject item = new JSONObject();
			item.put("siteName", areaMap.get((long) key));
			if (toPayMap.get(key) == null) {
				item.put("toPayCount", 0);
			} else {
				item.put("toPayCount", toPayMap.get(key));
			}

			if (returnGoodsMap.get(key) == null) {
				item.put("returnGoodsCount", 0);
			} else {
				item.put("returnGoodsCount", returnGoodsMap.get(key));
			}

			if (returnMoneyMap.get(key) == null) {
				item.put("returnMoneyCount", 0);
			} else {
				item.put("returnMoneyCount", returnMoneyMap.get(key));
			}

			orderAuditList.add(item);
		}

		return orderAuditList;
	}

	@Override
	public void scheduleTimerOffline() {
		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.BACKEND_PASSED);
		paramDTO.poStatusList = poStatusList;

		POListDTO poList = scheduleService.getScheduleListByStartEndTime(paramDTO);
		if (poList.getPoList().size() == 0) {
			return;
		}

		for (PODTO po : poList.getPoList()) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			ScheduleVice vice = po.getScheduleDTO().getScheduleVice();
			if (schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
					&& vice.getFlagAuditBanner() == 1 && vice.getFlagAuditPage() == 1) {
				if (schedule.getEndTime() < System.currentTimeMillis()) { // PO
																			// offline
					boolean result = scheduleService.makeScheduleOffline(schedule.getId());
					if (result) {
						logger.debug("PO '" + schedule.getId() + "' offline OK!!!");
					} else {
						logger.error("PO '" + schedule.getId() + "' offline failure!!!");
					}
				}
			}
		}
	}

	@Override
	public void scheduelTimerUnlike() {
		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.OFFLINE);
		paramDTO.poStatusList = poStatusList;

		POListDTO poList = scheduleService.getScheduleListByStartEndTime(paramDTO);
		if (poList.getPoList().size() == 0) {
			return;
		}

		long now = System.currentTimeMillis();
		for (PODTO po : poList.getPoList()) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			if ((now - schedule.getEndTime()) >= ScheduleUtil.UNLIKE_PO_DURATION) {
				boolean result = likeService.unlikeByPoId(schedule.getId());
				if (result) {
					logger.debug("PO '" + schedule.getId() + "' batch being unliked OK!!!");
				} else {
					logger.error("PO '" + schedule.getId() + "' batch being unliked failure!!!");
				}
			}
		}
	}

	@Override
	public List<Schedule> getScheduleByIdList(List<Long> idList) {
		return scheduleService.getScheduleByIdList(idList);
	}

	@Override
	public List<String> getPromotionByPO(long poId) {
		List<String> list = new ArrayList<String>();
		try {
			Promotion promotion = promotionService.getPromotionByPO(poId, 0, true);
			if (promotion != null) {
				list.add(promotion.getDescription());
			}
		} catch (Exception e) {
			// ignore
			logger.error(e.getMessage(), e);
		}

		return list;
	}

	@Override
	public POListDTO getScheduleListByBrandNameOrSupplierAcct(String permission, long siteCode, String supplierAcct,
			String brandName) {
		List<AreaDTO> areaList = getAllowedAreaList(permission);
		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.allowedAreaIdList = new ArrayList<Long>();
		for (AreaDTO area : areaList) {
			paramDTO.allowedAreaIdList.add(area.getId());
		}
		paramDTO.curSupplierAreaId = siteCode;
		setSiteFlag(paramDTO);

		return scheduleService.getScheduleListByBrandNameOrSupplierAcct(paramDTO, supplierAcct, brandName);
	}

	@Override
	public POListDTO getScheduleListByBrandIdList(ScheduleCommonParamDTO paramDTO) {
		return scheduleService.getScheduleListByBrandIdList(paramDTO);
	}
	
	@Override
	public List<POProductDTO> getPoProductByPo(long poId) {
		return poProductService.getProductDTOListByPoCache(poId);
	}

	@Override
	public List<PODTO> getOnlineScheduleList() {
		return scheduleService.getOnlinePOList(0, 0);
	}

	@Override
	public Map<Long, Integer> getInventoryCount(List<Long> skuIds) {
		return cartService.getInventoryCount(skuIds);
	}
	


	@Override
	public ScheduleListVO getScheduleListForChl(long chlId, long saleSiteFlag, long curDate) {
		ScheduleListVO vo = new ScheduleListVO();
		if (saleSiteFlag <= 0) {
			POListDTO poList = new POListDTO();
			vo.setPoList(poList);
			return vo;
		}

		long saleSiteFlagCode = getSaleSiteFlag(saleSiteFlag);
		POListDTO poList = scheduleService.getScheduleListForChl(chlId, saleSiteFlagCode, curDate);

		// poList = POBaseUtil.filterForEveryOne(userLoginBean, poList);
		// QrqmUtils.writeDyingPOListToCookie(userLoginBean);

		vo.setPoList(poList);
		ScheduleUtil.sortPOListForMainsite(poList);
		
		fillPOPromotion(poList);
		return vo;
	}

	private void fillPOPromotion(POListDTO poList) {
		if (poList == null || poList.getPoList() == null || poList.getPoList().size() <= 0) {
			return;
		}
		
		List<Long> poIdList = new ArrayList<Long>();
		for (PODTO poDTO : poList.getPoList()) {
			poIdList.add(poDTO.getScheduleDTO().getSchedule().getId());
		}
		try {
			Map<Long, String> poPromotionMap = promotionService.getPromotionTipMap(poIdList, true);
			if (poPromotionMap != null) {
				for (PODTO poDTO : poList.getPoList()) {
					String promotionDesc = poPromotionMap.get(poDTO.getScheduleDTO().getSchedule().getId());
					poDTO.setPromotionDesc(promotionDesc);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public ScheduleListVO getScheduleListForFuture(long chlId, long saleSiteCode, long startTime, long endTime) {
		long saleSiteFlag = getSaleSiteFlag(saleSiteCode);
		POListDTO poList = scheduleService.getScheduleListForFuture(chlId, saleSiteFlag, startTime, endTime);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(poList);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleListForPrdOrListAudit(ScheduleCommonParamDTO paramDTO, Integer status, boolean isPrdList) {
		if (status != null && status != 0) {
			status += 1;
		}
		setSiteFlag(paramDTO);
		POListDTO poList = scheduleService.getScheduleListForPrdOrListAudit(paramDTO);

		if (poList.getPoList() == null || poList.getPoList().size() == 0) {
			ScheduleListVO vo = new ScheduleListVO();
			vo.setPoList(poList);
			return vo;
		}
		
		// 2. set product flag
		for (PODTO poDTO : poList.getPoList()) {
			ScheduleUtil.setPOPrdStatus(poDTO);
		}

		// 3. 根据状态过滤
		List<PODTO> filteredPOList = new ArrayList<PODTO>();
		if (status == null || status == 0) { 
			for (PODTO poDTO : poList.getPoList()) {
				ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
				if (isPrdList) {
					int qdStatus = vice.getFlagAuditPrdqd();
					if ((qdStatus != 1) && (qdStatus != 0)) {
						poDTO.setPoStatus(qdStatus);
						filteredPOList.add(poDTO);
					}
				} else {
					int zlStatus = vice.getFlagAuditPrdzl();
					if ((zlStatus != 1) && (zlStatus != 0)) {
						poDTO.setPoStatus(zlStatus);
						filteredPOList.add(poDTO);
					}
				}
			}
		} else {
			for (PODTO poDTO : poList.getPoList()) {
				ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
				if (isPrdList) {
					int qdStatus = vice.getFlagAuditPrdqd();
					if (qdStatus == status.intValue()) {
						if ((qdStatus != 1) && (qdStatus != 0)) {
							poDTO.setPoStatus(qdStatus);
							filteredPOList.add(poDTO);
						}
					}
				} else {
					int zlStatus = vice.getFlagAuditPrdzl();
					if (zlStatus == status.intValue()) {
						if ((zlStatus != 1) && (zlStatus != 0)) {
							poDTO.setPoStatus(zlStatus);
							filteredPOList.add(poDTO);
						}
					}
				}
			}
		}

		// 4. 对于失效的PO，调用陆谦服务把该PO下的所有商品都置为失效
		long now = System.currentTimeMillis();
		for (PODTO poDTO : filteredPOList) {
			poDTO.setValid(true);
			int poMainStatus = ScheduleUtil.getPOMainStatus(poDTO, now);
			if (poDTO.getPoStatus() == 2) {
				if (poDTO.getScheduleDTO().getSchedule().getStartTime() < now) {
					poDTO.setValid(false);
				}
			} else if (poDTO.getPoStatus() == 3) {
				if (poDTO.getScheduleDTO().getSchedule().getStartTime() < now
						&& (poMainStatus == 1 || poMainStatus == 2 || poMainStatus == 4 || poMainStatus == 6)) {
					poDTO.setValid(false);
				}
			} else if (poDTO.getPoStatus() == 4) {
				if (poDTO.getScheduleDTO().getSchedule().getStartTime() < now) {
					poDTO.setValid(false);
				}
			}
		}

		// 5. 分页
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(new POListDTO());
		vo.getPoList().setPoList(filteredPOList);
		ScheduleUtil.POListPager(vo.getPoList(), paramDTO.curPage, paramDTO.pageSize);

		return vo;
	}

	@Override
	public boolean invalidPrdsForExpiredPO(boolean isPrdList) {
		try {
			ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
			List<ScheduleState> statusList = new ArrayList<ScheduleState>();
			statusList.add(ScheduleState.PASSED);
			statusList.add(ScheduleState.BACKEND_PASSED);
			POListDTO poList = scheduleService.getScheduleListForPrdOrListAudit(paramDTO);

			if (poList.getPoList() == null || poList.getPoList().size() == 0) {
				return true;
			}

			// 2. set product flag
			for (PODTO poDTO : poList.getPoList()) {
				int prdStatus = poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPrdzl();
				int prdListStatus = poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPrdqd();

				if (prdListStatus != 3) {
					poDTO.setProductPass(false);
				} else {
					poDTO.setProductPass(true);
				}

				if (prdStatus != 3) {
					poDTO.setSkuPass(false);
				} else {
					poDTO.setSkuPass(true);
				}
			}

			// 3. set PO main status
			List<PODTO> filteredPOList = new ArrayList<PODTO>();
			for (PODTO poDTO : poList.getPoList()) {
				if (isPrdList) {
					poDTO.setPoStatus(poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPrdqd());
				} else {
					poDTO.setPoStatus(poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPrdzl());
				}

				filteredPOList.add(poDTO);
			}

			// 4. invalid prds for those expired PO
			long now = System.currentTimeMillis();
			for (PODTO poDTO : filteredPOList) {
				poDTO.setValid(true);
				int poMainStatus = ScheduleUtil.getPOMainStatus(poDTO, now);
				if (poDTO.getPoStatus() == 2) {
					if (poDTO.getScheduleDTO().getSchedule().getStartTime() < now) {
						poDTO.setValid(false);
					}
				} else if (poDTO.getPoStatus() == 3) {
					if (poDTO.getScheduleDTO().getSchedule().getStartTime() < now
							&& (poMainStatus == 1 || poMainStatus == 2 || poMainStatus == 4 || poMainStatus == 6)) {
						poDTO.setValid(false);
					}
				} else if (poDTO.getPoStatus() == 4) {
					if (poDTO.getScheduleDTO().getSchedule().getStartTime() < now) {
						poDTO.setValid(false);
					} 
				}
				if (!poDTO.isValid()) {
					try {
						// TODO
						logger.debug("Success invalid all products for PO '" + poDTO.getScheduleDTO().getSchedule().getId() + "'!!!");
					} catch (Exception e) {
						logger.error("Error occured when invalid all products for PO '"
								+ poDTO.getScheduleDTO().getSchedule().getId() + "'!!!", e);
					}
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public Schedule getScheduleByScheduleId(long id) {
		return scheduleService.getScheduleByScheduleId(id);
	}
}
