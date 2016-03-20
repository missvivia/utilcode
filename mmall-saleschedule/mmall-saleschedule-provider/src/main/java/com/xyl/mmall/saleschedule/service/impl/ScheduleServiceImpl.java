package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.ScheduleChannel;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Service
public class ScheduleServiceImpl extends ScheduleBaseService implements ScheduleService {

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public PODTO saveSchedule(PODTO poDTO) {
		logger.debug("saveSchedule: " + poDTO);
		
		Schedule schedule = poDTO.getScheduleDTO().getSchedule();
		
		// 0. set sale site bitset flag
		try {
			schedule.setSaleSiteFlag(ProvinceCodeMapUtil.getProvinceFmtByCodeList(
					ScheduleBackendUtil.getPOSaleSiteCodeList(poDTO.getScheduleDTO().getSiteRelaList())));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// 0.1. set channel bitset flag
		genChannelFlag(poDTO.getScheduleDTO());
		
		// 0.2. save to DB
		boolean result = scheduleDao.saveSchedule(schedule);
		if (result) {
			// 1. save schedule vice 
			ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
			if (vice != null) {
				vice.setScheduleId(schedule.getId());
				viceDao.saveScheduleVice(vice);
			}
			
			// 2. save schedule and salesite relation
			List<ScheduleSiteRela> siteRelaList = poDTO.getScheduleDTO().getSiteRelaList();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				siteRela.setScheduleId(schedule.getId());
				
				// 2.1 set show order under special salesite
				List<ScheduleSiteRela> dbSiteRelaList = 
						siteRelaDao.getScheduleSiteRelaList(siteRela.getSaleSiteId(), schedule.getStartTime());
				int maxShowOrder = ScheduleBackendUtil.getMaxShowOrder(dbSiteRelaList);
				siteRela.setShowOrder(maxShowOrder+1);
			}
			
			if (siteRelaList != null && siteRelaList.size() > 0) {
				siteRelaDao.batchSaveScheduleSiteRela(siteRelaList);
			}
			
			// generate an empty schedule page at same time.
			SchedulePage page = getPageFromScheduleObj(schedule);
			pageDao.saveSchedulePage(page);

			SchedulePageDTO pageDto = new SchedulePageDTO();
			pageDto.setPage(page);

			poDTO.setPageDTO(pageDto);

			// generate an empty banner at same time
			ScheduleBanner banner =  getBannerFromScheduleObj(schedule);
			bannerDao.saveScheduleBanner(banner);

			ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
			bannerDTO.setBanner(banner);

			poDTO.setBannerDTO(bannerDTO);

			// update pageId and bannerId in PO
			schedule.setPageId(page.getId());
			schedule.setBannerId(banner.getId());
			scheduleDao.updateSchedulePageIdAndBannerId(schedule);
			if (vice != null) {
				vice.setPageId(page.getId());
				vice.setBannerId(banner.getId());
				viceDao.updateSchedulePageIdAndBannerId(vice);
			}
			
			return poDTO;
		} else {
			PODTO pDto = new PODTO();
			pDto.setScheduleDTO(null);
			return pDto;
		}
	}
	
	private long genChannelFlag(ScheduleDTO scheduleDTO) {
		String adPosition = scheduleDTO.getScheduleVice().getAdPosition();
		adPosition = adPosition.substring(1, adPosition.length() - 1);
		String[] arr = adPosition.split(",");
		
		List<Long> flagList = new ArrayList<Long>();
		List<ScheduleChannel> chlList = channelDao.getScheduleChannelList();
		for (int i = 0; i < arr.length; i++) {
			String chlName = arr[i];
			for (Iterator<ScheduleChannel> iter = chlList.iterator(); iter.hasNext();) {
				ScheduleChannel chl = iter.next();
				if (chl.getName().trim().equals(chlName.trim())) {
					flagList.add(chl.getFlag());
				}
			}
		}
		
		long flag = 0;
		if (flagList.size() > 0) {
			for (int i = 0, j = flagList.size(); i < j; i++) {
				flag = flag^flagList.get(i);
			}
		}
		
		scheduleDTO.getSchedule().setChlFlag(flag);
		
		return flag;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean updateSchedule(PODTO poDTO) {
		logger.debug("updateSchedule: " + poDTO);
		Schedule schedule = poDTO.getScheduleDTO().getSchedule();
		genChannelFlag(poDTO.getScheduleDTO());
		boolean result = scheduleDao.updateSchedule(schedule);
		if (result) {
			{
				// 1. update schedule vice 
				ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
				if (vice != null) {
					viceDao.updateScheduleVice(vice);
				}
				
				// 2. update schedule and salesite relation
				List<ScheduleSiteRela> siteRelaList = poDTO.getScheduleDTO().getSiteRelaList();
				// 2.1 set show order under special salesite
				for (ScheduleSiteRela siteRela : siteRelaList) {
					List<ScheduleSiteRela> dbSiteRelaList = 
							siteRelaDao.getScheduleSiteRelaList(siteRela.getSaleSiteId(), schedule.getStartTime());
					int maxShowOrder = ScheduleBackendUtil.getMaxShowOrder(dbSiteRelaList);
					siteRela.setShowOrder(maxShowOrder+1);
				}
				
				// 2.2 save relation of sale site
				if (siteRelaList != null && siteRelaList.size() > 0) {
					siteRelaDao.deleteScheduleSiteRelaByScheduleId(schedule.getId());
					siteRelaDao.batchSaveScheduleSiteRela(siteRelaList);
				}
			}
			
			// update page
			SchedulePage page = getPageFromScheduleObj(schedule);
			pageDao.updateSchedulePagePOField(page);
			
			// update banner
			ScheduleBanner banner = getBannerFromScheduleObj(schedule);
			bannerDao.updateScheduleBannerPOField(banner);
		} 
		
		return result;
	}
	
	private SchedulePage getPageFromScheduleObj(Schedule schedule) {
		long now = System.currentTimeMillis();
		
		SchedulePage page = new SchedulePage();
		page.setScheduleId(schedule.getId());
		//page.setSaleAreaId(schedule.getSaleAreaId());
		page.setSupplierId(schedule.getSupplierId());
		page.setSupplierName(schedule.getSupplierName());
		page.setBrandName(schedule.getBrandName());
		page.setBrandId(schedule.getBrandId());
		page.setBrandNameEn(schedule.getBrandNameEn());
		page.setCreateDate(now);
		page.setStatus(CheckState.DRAFT);
		page.setStatusUpdateDate(now);
		page.setTitle(schedule.getTitle());
		
		return page;
		
	}
	
	private ScheduleBanner getBannerFromScheduleObj(Schedule schedule) {
		long now = System.currentTimeMillis();
		
		ScheduleBanner banner = new ScheduleBanner();
		banner.setScheduleId(schedule.getId());
		//banner.setSaleAreaId(schedule.getSaleAreaId());
		banner.setSupplierId(schedule.getSupplierId());
		banner.setSupplierName(schedule.getSupplierName());
		banner.setBrandId(schedule.getBrandId());
		banner.setBrandName(schedule.getBrandName());
		banner.setBrandNameEn(schedule.getBrandNameEn());
		banner.setStatus(CheckState.DRAFT);
		banner.setCreateDate(now);
		banner.setUpdateDate(now);
		
		return banner;
	}
	

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean deleteScheduleById(long id) {
		logger.debug("deleteScheduleById: " + id);
		boolean result = scheduleDao.deleteScheduleById(id);
		
		if (result) {
			viceDao.deleteScheduleViceByScheduleId(id);
			siteRelaDao.deleteScheduleSiteRelaByScheduleId(id);
		}
		
		return result;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean adjustScheduleDate(long id, long newStartTime, long newEndTime, String desc, String poFollowerName, long poFollowerId) {
		logger.debug("adjustScheduleDate: id=" + id + "; start=" + newStartTime + "; end=" + newEndTime + "; desc="
				+ desc);

		boolean result = false;
		result = scheduleDao.adjustScheduleDate(id, newStartTime, newEndTime, desc);
		result = viceDao.updatePOFollowerUser(poFollowerName, poFollowerId, id);
		result = siteRelaDao.updateScheduleSiteRelaPOStartTime(newStartTime, id);
		
		return result;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditScheduleReject(long id, String desc) {
		logger.debug("auditScheduleReject: " + id + "; desc=" + desc);
		return scheduleDao.updateStatus(id, ScheduleState.REJECTED, desc, null);
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditSchedulePass(long id) {
		logger.debug("auditScheduleReject: " + id);
		return scheduleDao.updateStatus(id, ScheduleState.PASSED, null, null);
	}
	
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditScheduleSubmit(long id) {
		logger.debug("auditScheduleSubmit: " + id);
		return scheduleDao.updateStatus(id, ScheduleState.CHECKING, null, null);
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean makeScheduleOffline(long id) {
		logger.debug("makeScheduleOffline: " + id);
		return scheduleDao.updateStatus(id, ScheduleState.OFFLINE, null, null);
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean updatePOPrdStatus(long poId, int status) {
		logger.debug("updatePOPrdStatus(" + poId + "," + status + ") called!!!");
		boolean result = scheduleDao.updatePrdListAuditStatus(poId, status);
		return result;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean batchUpdatePOPrdStatus(List<Long> poIdList, int status) {
		logger.debug("batchUpdatePOPrdStatus(" + poIdList + "," + status + ") called!!!");
		if (poIdList == null || poIdList.size() == 0 || status < 0) {
			logger.error("Wrong parameter!!!");
			return false;
		}
		for (Long poId : poIdList) {
			boolean flag = scheduleDao.updatePrdListAuditStatus(poId, status);
			if (!flag) {
				logger.error("Failure update status for po '" + poId + "' with status '" + status + "'!!!");
				return false;
			} else {
				if (status == 4) {
					scheduleDao.updateStatus(poId, ScheduleState.PASSED, null, null);
				}
			}
		}

		return true;
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditPOProductListPass(long poId) {
		logger.debug("auditPOProductListPass: " + poId);
		boolean result = scheduleDao.updatePrdListAuditStatus(poId, 1);
		return result;
	}

	private boolean isPOValid(Schedule s) {
		long now = System.currentTimeMillis();
		long poStartTime = s.getStartTime();
		long poEndTime = s.getEndTime();
		if (poStartTime > now) {
			return false;
		}
		if (poEndTime < now) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public PODTO getScheduleByIdForCMSWithNoCache(long id) {
		logger.debug("getScheduleById: " + id);

		ScheduleDTO scheduleDTO = youhuaDao.getScheduleByScheduleIdForCMSWithNoCache(id);
		PODTO poDTO = new PODTO();
		if (scheduleDTO.getSchedule() != null) {
			boolean isExpire = scheduleDTO.getSchedule().getEndTime() < System.currentTimeMillis();
			poDTO.setExpire(isExpire);
			poDTO.setValid(isPOValid(scheduleDTO.getSchedule()));
		}
		poDTO.setScheduleDTO(scheduleDTO);

		logger.debug("Successfully get po : " + poDTO);
		return poDTO;
	}
	
	@Cacheable(value = "poCache")
	@Override
	public PODTO getScheduleById(long id) {
		logger.debug("getScheduleById: " + id);

		ScheduleDTO scheduleDTO = scheduleDao.getScheduleByScheduleId(id);
		PODTO poDTO = new PODTO();
		if (scheduleDTO.getSchedule() != null) {
			boolean isExpire = scheduleDTO.getSchedule().getEndTime() < System.currentTimeMillis();
			poDTO.setExpire(isExpire);
			poDTO.setValid(isPOValid(scheduleDTO.getSchedule()));
		}
		
		poDTO.setScheduleDTO(scheduleDTO);

		logger.debug("Successfully get po : " + poDTO);
		return poDTO;
	}

	@Cacheable(value = "poCache")
	@Override
	public POListDTO batchGetScheduleListByIdList(List<Long> poIdList) {
		logger.debug("batchGetScheduleListByIdList: " + poIdList);
		//curPage = adjustCurPage(curPage);
		
		POListDTO poList = new POListDTO();
		for (Long poId : poIdList) {
			Schedule schedule = youhuaDao.getScheduleByIdWithLimitedFields(poId);
			if (schedule == null) {
				logger.warn("Cannot find PO for id '" + poId + "'!!!");
				continue;
			}
			ScheduleDTO scheduleDTO = new ScheduleDTO();
			PODTO poDTO = new PODTO();
			scheduleDTO.setSchedule(schedule);
			poDTO.setScheduleDTO(scheduleDTO);
			poList.getPoList().add(poDTO);
		}
		
		//POListDTO poList = scheduleDao.batchGetScheduleListByIdList(poIdList);
		if (poList.getPoList() != null && poList.getPoList().size() != 0) {
			long now = System.currentTimeMillis();
			for (PODTO po : poList.getPoList()) {
				boolean isExpire = po.getScheduleDTO().getSchedule().getEndTime() < now;
				po.setExpire(isExpire);
				po.setValid(isPOValid(po.getScheduleDTO().getSchedule()));
			}
		}
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleListCommon(ScheduleCommonParamDTO paramDTO) {
		setSiteFlagForParamDTO(paramDTO);
		POListDTO poList = scheduleDao.getScheduleListCommon(paramDTO);
		
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		
		return poList;
	}
	
	@Override
	public POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		int offset = paramDTO.curPage;
		int limit = paramDTO.pageSize;
		
		// query all
		paramDTO.curPage = 0;
		paramDTO.pageSize = 0;
		POListDTO poList = scheduleDao.getScheduleList(paramDTO);
		
		// for Order
		if (paramDTO.poStatusList == null || paramDTO.poStatusList.size() == 0) {
			return poList;
		}
		
		if (poList.getPoList().size() == 0) {
			return poList;
		}
		
		// for CMS promotion
		// Then filter in memory
		List<PODTO> filteredPOList = new ArrayList<PODTO>();
		long now = System.currentTimeMillis();
		for (PODTO po : poList.getPoList()) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			if (schedule.getEndTime() < now) {  // PO offline
				continue;
			}
			
			ScheduleVice vice = po.getScheduleDTO().getScheduleVice();
			// invalid
			if (schedule.getStartTime() < now &&
					(schedule.getFlagAuditPrdList() != 3 || vice.getFlagAuditBanner() != 1
					|| vice.getFlagAuditPage() != 1)) {
				continue;
			}
			
			// invalid
			if (schedule.getStartTime() < now && schedule.getStatus() == ScheduleState.PASSED 
					&& (schedule.getFlagAuditPrdList() == 3 
					&& vice.getFlagAuditBanner() == 1 && vice.getFlagAuditPage() == 1)) {
				continue;
			}
			
			filteredPOList.add(po);
		}
		
		// pager in memory
		poList.setTotal(filteredPOList.size());
		poList.setHasNext(hasNext(poList.getTotal(), offset, limit));

		List<PODTO> filteredPOList2 = new ArrayList<PODTO>();
		if (limit != 0) {
			for (int i = offset; i < (offset+limit); i++) {
				if (i < filteredPOList.size()) {
					filteredPOList2.add(filteredPOList.get(i));
				}
			}
			poList.setPoList(filteredPOList2);
		} else {
			poList.setPoList(filteredPOList);
		}
		
		
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}
	
	private boolean hasNext(int total, int curPage, int pageSize) {
		if (curPage != 0 && pageSize != 0) {
			return curPage * pageSize < total;
		}
		return false;
	}

	@Override
	public POListDTO getScheduleList(long brandId, long saleSiteFlag) {
		POListDTO poList = scheduleDao.getScheduleList(brandId);
		
		// filter for sale site
		filterForSaleSite(poList, saleSiteFlag);
		
		// set showOrder for every PO
		batchSetShowOrder(poList.getPoList(), saleSiteFlag);
		
		return poList;
	}

	@Override
	public POListDTO getScheduleListFuture(long brandId, long saleSiteFlag, int dayAfter) {
		POListDTO poList = scheduleDao.getScheduleListFuture(brandId, dayAfter);
		
		// filter for sale site
		filterForSaleSite(poList, saleSiteFlag);
		
		// set showOrder for every PO
		batchSetShowOrder(poList.getPoList(), saleSiteFlag);
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleList(long saleAreaId, long supplierId, String brandName, long startDate,
			long endDate, int curPage, int pageSize) {
		logger.debug("getScheduleList: " + saleAreaId + "; " + supplierId + "; " + brandName);
		curPage = adjustCurPage(curPage);
		long siteFlag = -1;
		try {
			siteFlag = ProvinceCodeMapUtil.getProvinceFmtByCode(saleAreaId);
		} catch (Exception e) {
			
		}
		POListDTO poList = scheduleDao.getScheduleList(siteFlag, supplierId, brandName, startDate, endDate);

		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO, List<ScheduleState> statusList, boolean isCheck) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		POListDTO poList = scheduleDao.getScheduleList(paramDTO, statusList, isCheck);

		List<Long> idList = new ArrayList<Long>();
		for (PODTO po : poList.getPoList()) {
			idList.add(po.getScheduleDTO().getSchedule().getId());
			po.getScheduleDTO().setSiteRelaList(new ArrayList<ScheduleSiteRela>());
		}
		
		List<ScheduleSiteRela> siteRelaList = siteRelaDao.getScheduleSiteRelaListWithNoCache(idList);
		if (siteRelaList != null) {
			Map<String, List<ScheduleSiteRela>> map = new ConcurrentHashMap<>();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				String key = siteRela.getScheduleId() + "";
				if (map.get(key) == null) {
					map.put(key, new ArrayList<ScheduleSiteRela>());
				}
				map.get(key).add(siteRela);
			}
			
			for (PODTO po : poList.getPoList()) {
				String key = po.getScheduleDTO().getSchedule().getId() + "";
				if (map.get(key) == null) {
					logger.warn("PO '" + key + "' don't have any siteRela data!!!!");
					continue;
				}
				
				po.getScheduleDTO().setSiteRelaList(map.get(key));
			}
		}
		
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);

		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleListByBrandNameOrSupplierAcct(ScheduleCommonParamDTO paramDTO, String supplierAcct,
			String brandName) {
		POListDTO poList = scheduleDao.getScheduleListByBrandNameOrSupplierAcct(paramDTO, supplierAcct, brandName);

		logger.debug("Successfully get po list: " + poList);
		return poList;
	}
	
	@Override
	public POListDTO getScheduleListByBrandIdList(ScheduleCommonParamDTO paramDTO) {
		POListDTO poList = scheduleDao.getScheduleListByBrandIdList(paramDTO);
		
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public Map<Long, List<Long>> getWarehouseListByPOIdList(List<Long> poIdList) {
		Map<Long, List<Long>> result = new ConcurrentHashMap<Long, List<Long>>();
		POListDTO poList = scheduleDao.getScheduleListByPOIdList(poIdList); 
		
		if (poList.getPoList() == null || poList.getPoList().size() == 0) {
			return result;
		}
		
		for (PODTO poDTO : poList.getPoList()) {
			List<Long> warehouseList = new ArrayList<Long>();
			if (poDTO.getScheduleDTO().getScheduleVice().getSupplierStoreId() != 0) {
				warehouseList.add(poDTO.getScheduleDTO().getScheduleVice().getSupplierStoreId());
			}
			
			if (poDTO.getScheduleDTO().getScheduleVice().getBrandStoreId() != 0) {
				warehouseList.add(poDTO.getScheduleDTO().getScheduleVice().getBrandStoreId());
			}
			
			if (result.get(poDTO.getScheduleDTO().getSchedule().getId()) == null) {
				result.put(poDTO.getScheduleDTO().getSchedule().getId(), warehouseList);
			}
		}
		
		return result;
	}
	
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean batchUpdatePOOrder(List<Schedule> poList) {
		logger.debug("batchUpdatePOOrder: " + poList);
		if (poList == null || poList.size() == 0) {
			return true;
		}
		
		long saleSiteId = poList.get(0).getSaleAreaId();
		for (Schedule po : poList) {
			siteRelaDao.updateScheduleSiteRelaShowOrder(po.getShowOrder(), po.getId(), saleSiteId);
		}
		
		return true;
	}

	@Override
	public POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		POListDTO poList = scheduleDao.getScheduleBannerList(paramDTO);

		
		List<Long> poIdList = new ArrayList<Long>();
		for (PODTO po : poList.getPoList()) {
			poIdList.add(po.getScheduleDTO().getSchedule().getId());
		}
		POListDTO vicePOList = scheduleDao.getScheduleListByPOIdList(poIdList);
		for (PODTO outerPO : vicePOList.getPoList()) {
			long outerPOId = outerPO.getScheduleDTO().getSchedule().getId();
			for (PODTO innerPO : poList.getPoList()) {
				long innerPOId = innerPO.getScheduleDTO().getSchedule().getId();
				if (outerPOId == innerPOId) {
					innerPO.setScheduleDTO(outerPO.getScheduleDTO());
				}
			}
		}
		
		for (PODTO poDTO : poList.getPoList()) {
			if (poDTO.getBannerDTO().getBanner() == null) {
				Schedule schedule = poDTO.getScheduleDTO().getSchedule();

				ScheduleBanner banner = getBannerFromScheduleObj(schedule);
				bannerDao.saveScheduleBanner(banner);

				poDTO.getBannerDTO().setBanner(banner);

				// update banner id of schedule
				schedule.setBannerId(banner.getId());
				scheduleDao.updateSchedulePageIdAndBannerId(schedule);
			}

			poDTO.getScheduleDTO()
					.getSchedule()
					.setStatus(
							ScheduleState.NULL.genEnumByIntValue(poDTO.getBannerDTO().getBanner().getStatus()
									.getIntValue()));
		}

		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleListForOMS(ScheduleCommonParamDTO paramDTO,
			long startTimeBegin, long startTimeEnd, long endTimeBegin, long endTimeEnd, long createTimeBegin,
			long createTimeEnd) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		POListDTO poList = scheduleDao.getScheduleListForOMS(paramDTO, startTimeBegin, startTimeEnd,
				endTimeBegin, endTimeEnd, createTimeBegin, createTimeEnd);
		
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		
		// use field 'flagAuditPrdList' as PO's status
		if (poList.getPoList() != null && poList.getPoList().size() != 0) {
			for (PODTO poDTO : poList.getPoList()) {
				int flagAuditPrdList = poDTO.getScheduleDTO().getSchedule().getFlagAuditPrdList();
				if (flagAuditPrdList <= 1) {
					poDTO.getScheduleDTO().getSchedule().setStatus(ScheduleState.DRAFT);
				} else {
					poDTO.getScheduleDTO().getSchedule()
							.setStatus(ScheduleState.NULL.genEnumByIntValue(flagAuditPrdList));
				}
			}
		}
		
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleListForCMS(ScheduleCommonParamDTO paramDTO, int type, Object val) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		POListDTO poList = scheduleDao.getScheduleListForCMS(paramDTO, type, val);
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
				
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public POListDTO getScheduleListForPOPages(ScheduleCommonParamDTO paramDTO, int type, Object key) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);

		POListDTO poList = scheduleDao.getScheduleListForPOPages(paramDTO, type, key);
		
		List<Long> poIdList = new ArrayList<Long>();
		for (PODTO po : poList.getPoList()) {
			poIdList.add(po.getScheduleDTO().getSchedule().getId());
		}
		POListDTO vicePOList = scheduleDao.getScheduleListByPOIdList(poIdList);
		for (PODTO outerPO : vicePOList.getPoList()) {
			long outerPOId = outerPO.getScheduleDTO().getSchedule().getId();
			for (PODTO innerPO : poList.getPoList()) {
				long innerPOId = innerPO.getScheduleDTO().getSchedule().getId();
				if (outerPOId == innerPOId) {
					innerPO.setScheduleDTO(outerPO.getScheduleDTO());
				}
			}
		}
				
		for (PODTO poDTO : poList.getPoList()) {
			if (poDTO.getPageDTO().getPage() == null) {
				Schedule schedule = poDTO.getScheduleDTO().getSchedule();

				// generate one empty page record. just for legacy data.
				SchedulePage page = getPageFromScheduleObj(schedule);
				pageDao.saveSchedulePage(page);

				poDTO.getPageDTO().setPage(page);

				// update page id of schedule
				schedule.setPageId(page.getId());
				scheduleDao.updateSchedulePageIdAndBannerId(schedule);
			}

			poDTO.getScheduleDTO()
					.getSchedule()
					.setStatus(
							ScheduleState.NULL
									.genEnumByIntValue(poDTO.getPageDTO().getPage().getStatus().getIntValue()));
		}

		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Cacheable(value = "poCache")
	@Override
	public List<ScheduleChannelDTO> getScheduleChannelList() {
		List<ScheduleChannelDTO> channelDTOList = new ArrayList<ScheduleChannelDTO>();
		List<ScheduleChannel> channelList = channelDao.getScheduleChannelList();

		if (channelList == null) {
			logger.error("Cannot find any channel info!!!");
			return channelDTOList;
		}

		for (ScheduleChannel chl : channelList) {
			ScheduleChannelDTO dto = new ScheduleChannelDTO();
			dto.setId(chl.getId());
			dto.setName(chl.getName());
			dto.setIconUrl(chl.getIconUrl());
			dto.setIconId(chl.getIconId());
			dto.setFlag(chl.getFlag());

			channelDTOList.add(dto);
		}

		return channelDTOList;
	}

	@Override
	public List<ScheduleStatusDTO> getScheduleStateList() {
		List<ScheduleStatusDTO> statusList = new ArrayList<ScheduleStatusDTO>();
		for (ScheduleState item : ScheduleState.values()) {
			int val = item.getIntValue();
			if (val > 0 && val < 100) {
				ScheduleStatusDTO status = new ScheduleStatusDTO();
				status.setId(item.getIntValue());
				status.setName(item.getDesc());
				statusList.add(status);
			}
		}

		return statusList;
	}

	@Override
	public List<ScheduleStatusDTO> getScheduleStateListForBackend() {
		List<ScheduleStatusDTO> statusList = new ArrayList<ScheduleStatusDTO>();
		for (ScheduleState item : ScheduleState.values()) {
			int val = item.getIntValue();
			if (val > 200 && val < 300) {
				ScheduleStatusDTO status = new ScheduleStatusDTO();
				status.setId(item.getIntValue());
				status.setName(item.getDesc());
				statusList.add(status);
			}
		}

		ScheduleStatusDTO status = new ScheduleStatusDTO();
		status.setId(ScheduleState.PASSED.getIntValue());
		status.setName(ScheduleState.DRAFT.getDesc());
		statusList.add(status);

		return statusList;
	}

	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditScheduleForBackend(long id, ScheduleState status, String desc) {
		logger.debug("auditScheduleForBackend: " + id);
		return scheduleDao.updateStatus(id, status, desc, null);
	}

	private long getChlFlag (long chlId) {
		List<ScheduleChannel> channelList = channelDao.getScheduleChannelList();
		for (ScheduleChannel chl : channelList) {
			if (chl.getId() == chlId) {
				return chl.getFlag();
			}
		}
		return -1;
	}
	
	@Cacheable(value = "poCache")
	@Override
	public POListDTO getScheduleListForChl(long chlId, long saleSiteFlag, long startId, int curPage,
			int pageSize) {
		String sqlPart = genSqlPart2(chlId);
		curPage = adjustCurPage(curPage);

		POListDTO poList = scheduleDao.getScheduleListForChl(sqlPart, saleSiteFlag, startId, curPage, pageSize);

		if (poList.getPoList() == null || poList.getPoList().size() == 0) {
			return poList;
		}
		
		// filter for channel
		long chlFlag = getChlFlag(chlId);
		List<PODTO> filteredPOList = new ArrayList<PODTO>();
		for (PODTO poDTO : poList.getPoList()) {
			Schedule s = poDTO.getScheduleDTO().getSchedule();
			if ((s.getChlFlag() & chlFlag) == chlFlag) {
				filteredPOList.add(poDTO);
			}
		}
		
		// set showOrder for every PO
		batchSetShowOrder(filteredPOList, saleSiteFlag);
		
		poList.setPoList(filteredPOList);
		return poList;
	}

	@Cacheable(value = "poCache")
	@Override
	public POListDTO getScheduleListForFuture(long chlId, long saleSiteFlag, int daysAfter,
			int retSize) {
		String sqlPart = genSqlPart2(chlId);

		POListDTO poList = scheduleDao.getScheduleListForFuture(sqlPart, saleSiteFlag, daysAfter, retSize);

		// set showOrder for every PO
		batchSetShowOrder(poList.getPoList(), saleSiteFlag);
		
		return poList;
	}

	private String genSqlPart2(long chlId) {
		StringBuilder sqlPart = new StringBuilder(" ");
//		List<ScheduleChannelDTO> chlList = getScheduleChannelList();
//		for (ScheduleChannelDTO chl : chlList) {
//			if (chl.getId() == chlId) {
//				// found
//				sqlPart.append(" AND chlFlag" + "&" + chl.getFlag() + "=" + chl.getFlag());
//			}
//		}
		
		return sqlPart.toString();
	}


	@Override
	public POListDTO getScheduleListByStartEndTime(ScheduleCommonParamDTO paramDTO) {
		POListDTO poList = scheduleDao.getScheduleListByStartEndTime(paramDTO);
		
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}
	
	@Override
	public POListDTO getScheduleListByStartEndTimeWithType(ScheduleCommonParamDTO paramDTO, int type) {
		setSiteFlagForParamDTO(paramDTO);
		POListDTO poList = scheduleDao.getScheduleListByStartEndTimeWithType(paramDTO, type);
		
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}

	@Override
	public List<Schedule> getScheduleListByTime(long saleSiteFlag, long startTime, long endTime) {
		List<Schedule> poList = scheduleDao.getScheduleListByTime(saleSiteFlag, startTime, endTime);
		if (poList == null || poList.size() == 0) {
			return poList;
		}
		
		if (saleSiteFlag <= 0) {
			return poList;
		}
		
		// filter for sale site
		List<Schedule> filteredPOList = new ArrayList<Schedule>();
		for (Schedule s : poList) {
			if ((s.getSaleSiteFlag() & saleSiteFlag) == saleSiteFlag) {
				filteredPOList.add(s);
			}
		}

		return filteredPOList;
	}

	@Override
	public List<PODTO> getOnlinePOList(long areaId, long supplierId) {
		List<PODTO> result = new ArrayList<PODTO>();

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.curSupplierAreaId = areaId;
		paramDTO.supplierId = supplierId;
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.BACKEND_PASSED);
		paramDTO.poStatusList = poStatusList;

		POListDTO poList = scheduleDao.getScheduleListByStartEndTime(paramDTO);
		if (poList == null) {
			return result;
		}

		for (PODTO po : poList.getPoList()) {
			if (po.getScheduleDTO().getSchedule().getStartTime() <= getTodayLastTime().getTimeInMillis()
					&& po.getScheduleDTO().getSchedule().getEndTime() > getTodayBeginTime().getTimeInMillis()) { // online,
																													// in
																													// sale
				result.add(po);
			}
		}

		return result;
	}

	@Override
	public List<Schedule> getScheduleByIdList(List<Long> idList) {
		List<Schedule> list = new ArrayList<Schedule>();
		if (idList == null || idList.isEmpty()) {
			return list;
		}
		
		for(Long id:idList){
			list.add(scheduleDao.getScheduleById(id));
		}
		return list;
	}
	
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public void clearCache() {
		logger.debug("This method is used to clear PO cache when the time is close to PO's startTime,"
				+ "Called by job project!!!");
	}
	
	@Override
	public List<Schedule> getSameDayPOList(Schedule comparePO) {
		return scheduleDao.getSameDayPOList(comparePO);
	}
	
	@Override
	public boolean updatePrdzlStatus(long id, int status) {
		return youhuaDao.updatePrdzlStatus(id, status);
	}
	
	@Override
	public boolean updatePrdqdStatus(long id, int status) {
		return youhuaDao.updatePrdqdStatus(id, status);
	}
	
	@Cacheable(value = "poCache")
	@Override
	public POListDTO getScheduleListForChl(long chlId, long saleSiteFlag, long curDate) {
		String sqlPart = genSqlPart2(chlId);

		POListDTO poList = youhuaDao.getScheduleListForChl(sqlPart, saleSiteFlag, curDate);

		if (poList.getPoList() == null || poList.getPoList().size() == 0) {
			return poList;
		}
		
		// filter for channel
		long chlFlag = getChlFlag(chlId);
		List<PODTO> filteredPOList = new ArrayList<PODTO>();
		for (PODTO poDTO : poList.getPoList()) {
			Schedule s = poDTO.getScheduleDTO().getSchedule();
			if ((s.getChlFlag() & chlFlag) == chlFlag) {
				filteredPOList.add(poDTO);
			}
		}
		
		// set showOrder for every PO
		batchSetShowOrder(filteredPOList, saleSiteFlag);
		
		poList.setPoList(filteredPOList);
		return poList;
	}
	
	@Cacheable(value = "poCache")
	@Override
	public POListDTO getScheduleListForFuture(long chlId, long saleSiteFlag, long startTime, long endTime) {
		String sqlPart = genSqlPart2(chlId);

		POListDTO poList = youhuaDao.getScheduleListForFuture(sqlPart, saleSiteFlag, startTime, endTime);

		// set showOrder for every PO
		batchSetShowOrder(poList.getPoList(), saleSiteFlag);
		return poList;
	}
	
	@Override
	public POListDTO getScheduleListForPrdOrListAudit(ScheduleCommonParamDTO paramDTO) {
		return scheduleDao.getScheduleListForPrdOrListAudit(paramDTO);
	}
	
	@Transaction
	@Override
	public boolean test() {
		System.out.println("============= SaleSchedule Data migration begin =============");

		List<ScheduleSiteRela> siteRelaList = siteRelaDao.getAllScheduleSiteRelaList();
		Set<Long> poIdSet = new HashSet<Long>();
		for (ScheduleSiteRela siteRela : siteRelaList) {
			poIdSet.add(siteRela.getScheduleId());
		}
		
		for (Long id : poIdSet) {
			ScheduleDTO poDTO = youhuaDao.getScheduleByScheduleIdForCMSWithNoCache(id);
			Schedule po = poDTO.getSchedule();
			if (po == null) {
				System.out.println("PO " + id + " is not exist!!");
				continue;
			} else {
				System.out.println("Deal PO " + id + "...");
			}
			siteRelaDao.updateScheduleSiteRelaShowOrder(po.getShowOrder(), id);
			siteRelaDao.updateScheduleSiteRelaPOStartTime(po.getStartTime(), id);
		}
		/**
		 *  select A.id, B.scheduleId as poId,  A.showOrder as sOrder, B.showOrder as sOrder, B.saleSiteId as site, 
		     FROM_UNIXTIME(A.startTime/1000,'%Y-%m-%d')as sTime , FROM_UNIXTIME(B.poStartTime/1000,'%Y-%m-%d')as poSTime 
		    FROM Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_SiteRela B
		    WHERE A.id = B.scheduleId limit 100;
		 */
		System.out.println("============= SaleSchedule Data migration end =============");
		return true;
	}
	
	@Override
	public Map<String, Object> getProductById(long productId) {
		return scheduleDao.getProductById(productId);
	}
	
	@Transaction
	@Override
	public boolean updatePOSaleSite(long poId, long saleSiteFlag, List<Long> saleSiteList) {
		scheduleDao.updatePOSaleSiteFlag(poId, saleSiteFlag);
		
		List<ScheduleSiteRela> siteRelaList = new ArrayList<ScheduleSiteRela>();
		
		for (Long saleSiteCode : saleSiteList) {
			ScheduleSiteRela siteRela = new ScheduleSiteRela();
			siteRela.setSaleSiteId(saleSiteCode);
			siteRela.setScheduleId(poId);
			siteRelaList.add(siteRela);
		}
		
		siteRelaDao.deleteScheduleSiteRelaByScheduleId(poId);
		siteRelaDao.batchSaveScheduleSiteRela(siteRelaList);
		
		return true;
	}

	@Cacheable(value = "poCache")
	@Override
	public Schedule getScheduleByScheduleId(long id) {
		return scheduleDao.getScheduleById(id);
	}
	

}

