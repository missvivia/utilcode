package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.service.ScheduleBannerService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Service
public class ScheduleBannerServiceImpl extends ScheduleBaseService implements ScheduleBannerService {

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public PODTO saveScheduleBanner(PODTO poDto) {
		logger.debug("saveScheduleBanner: " + poDto);
		boolean result = bannerDao.saveScheduleBanner(poDto.getBannerDTO().getBanner());
		if (result) {
			String msg = "Failure to save banner: " + poDto;
			logger.error(msg);
		}
		return poDto;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean updateScheduleBanner(PODTO poDto) {
		logger.debug("updateScheduleBanner: " + poDto);
		ScheduleBanner banner = poDto.getBannerDTO().getBanner();
		logger.debug("\tSave banner: " + banner);
		boolean result = bannerDao.updateScheduleBanner(banner);
		if (!result) {
			logger.error("Save banner failiure: " + banner + " !!");
			return false;
		}
		return true;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean deleteScheduleBannerById(long id) {
		logger.debug("deleteScheduleBannerById: " + id);
		return bannerDao.deleteScheduleBannerById(id);
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditScheduleBannerReject(PODTO poDTO) {
		logger.debug("auditScheduleBannerReject: " + poDTO);
		ScheduleBanner banner = poDTO.getBannerDTO().getBanner();
		banner.setStatus(CheckState.REJECTED);
		boolean result = bannerDao.auditScheduleBanner(banner);
		if (result) {
			scheduleDao.updateBannerAuditStatus(banner.getScheduleId(), banner.getId(), 0);
			scheduleDao.updateStatus(banner.getScheduleId(), ScheduleState.PASSED, null, null);
		}
		
		return result;
	}

	@Transaction
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditScheduleBannerPass(PODTO poDTO) {
		logger.debug("auditScheduleBannerPass: " + poDTO);
		ScheduleBanner banner = poDTO.getBannerDTO().getBanner();
		banner.setStatus(CheckState.PASSED);
		boolean result = bannerDao.auditScheduleBanner(banner);
//		boolean result = bannerDao.auditScheduleBannerPass(banner.getId(), banner.getAuditUserId(), banner.getAuditUserName());
		
		if (result) {
			scheduleDao.updateBannerAuditStatus(banner.getScheduleId(), banner.getId(), 1);
		}
		
		return result;
	}

	@Override
	public PODTO getScheduleBannerById(long id) {
		logger.debug("getScheduleBannerById: " + id);
		ScheduleBanner banner = bannerDao.getScheduleBannerById(id);
		return _genPODTO(banner);
	}

	@Override
	public PODTO getScheduleBannerByScheduleId(long scheduleId) {
		ScheduleBanner banner = bannerDao.getScheduleBannerByScheduleId(scheduleId);
		return _genPODTO(banner);
	}

	private PODTO _genPODTO(ScheduleBanner banner) {
		ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
		bannerDTO.setBanner(banner);

		PODTO poDto = new PODTO();
		poDto.setBannerDTO(bannerDTO);

		return poDto;
	}

//	@Override
//	public POListDTO getScheduleBannerListByStatus(CheckState status) {
//		return _getScheduleBannerListByStatus(status, 0, 0);
//	}
//
//	@Override
//	public POListDTO getScheduleBannerListByStatus(CheckState status, int curPage, int pageSize) {
//		return _getScheduleBannerListByStatus(status, curPage, pageSize);
//	}

//	private POListDTO _getScheduleBannerListByStatus(CheckState status, int curPage, int pageSize) {
//		logger.debug("_getScheduleBannerListByStatus: " + status + "; curPage=" + curPage + "; pageSize=" + pageSize);
//		curPage = adjustCurPage(curPage);
//		if (curPage != 0 && pageSize != 0) {
//			return bannerDao.getScheduleBannerListByStatus(status, curPage, pageSize);
//		} else {
//			return bannerDao.getScheduleBannerListByStatus(status);
//		}
//	}
//
//	@Override
//	public POListDTO getScheduleBannerListByStartDate(long startDate) {
//		return _getScheduleBannerListByStartDate(startDate, 0, 0);
//	}
//
//	@Override
//	public POListDTO getScheduleBannerListByStartDate(long startDate, int curPage, int pageSize) {
//		return _getScheduleBannerListByStartDate(startDate, curPage, pageSize);
//	}

//	private POListDTO _getScheduleBannerListByStartDate(long startDate, int curPage, int pageSize) {
//		logger.debug("getScheduleBannerListByStartDate: " + startDate);
//		if (curPage != 0 && pageSize != 0) {
//			return bannerDao.getScheduleBannerListByStartDate(startDate, curPage, pageSize);
//		} else {
//			return bannerDao.getScheduleBannerListByStartDate(startDate);
//		}
//	}
//
//	@Override
//	public POListDTO getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate) {
//		return _getScheduleBannerListByStartDateAndEndDate(startDate, endDate, 0, 0);
//	}
//
//	@Override
//	public POListDTO getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate, int curPage, int pageSize) {
//		return _getScheduleBannerListByStartDateAndEndDate(startDate, endDate, curPage, pageSize);
//	}

//	private POListDTO _getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate, int curPage,
//			int pageSize) {
//		curPage = adjustCurPage(curPage);
//		logger.debug("getScheduleBannerListByStartDate: " + startDate);
//
//		if (curPage != 0 && pageSize != 0) {
//			return bannerDao.getScheduleBannerListByStartDate(startDate, curPage, pageSize);
//		} else {
//			return bannerDao.getScheduleBannerListByStartDate(startDate);
//		}
//	}

	@Override
	public POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO, String supplierName, String brandName) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		
		POListDTO poList = bannerDao.getScheduleBannerList(paramDTO, supplierName, brandName);
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		return poList;
	}
	
	@Override
	public POListDTO getScheduleBannerListWithSupplierIdList(ScheduleCommonParamDTO paramDTO, String brandName) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);
		POListDTO poList = bannerDao.getScheduleBannerListWithSupplierIdList(paramDTO, brandName);
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		return poList;
	}
	
	@Override
	public List<ScheduleBannerDTO> getScheduleBannerList(List<Long> poIdList) {
		List<ScheduleBanner> bannerList = bannerDao.getScheduleBannerList(poIdList);
		
		List<ScheduleBannerDTO> result = new ArrayList<ScheduleBannerDTO>();
		for (ScheduleBanner banner : bannerList) {
			ScheduleBannerDTO dto = new ScheduleBannerDTO();
			dto.setBanner(banner);
			result.add(dto);
		}
		
		return result;
	}
}
