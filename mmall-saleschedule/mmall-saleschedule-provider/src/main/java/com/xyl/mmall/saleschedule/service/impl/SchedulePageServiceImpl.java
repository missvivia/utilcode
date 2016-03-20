package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.saleschedule.service.SchedulePageService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Service
public class SchedulePageServiceImpl extends ScheduleBaseService implements SchedulePageService {

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public PODTO saveSchedulePage(PODTO poDto) {
		logger.debug("saveSchedulePage: " + poDto);
		boolean result = pageDao.saveSchedulePage(poDto.getPageDTO().getPage());
		if (result) {
			return poDto;
		} else {
			PODTO pDto = new PODTO();
			pDto.setScheduleDTO(null);
			return pDto;
		}
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean updateSchedulePage(PODTO poDto) {
		logger.debug("updateSchedulePage: " + poDto);
		return pageDao.updateSchedulePage(poDto.getPageDTO().getPage());
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean deleteSchedulePageById(long id) {
		logger.debug("deleteSchedulePageById: " + id);
		return pageDao.deleteSchedulePageById(id);
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditSchedulePageReject(long id, long poId, String desc) {
		logger.debug("auditSchedulePageReject: " + id + "; desc=" + desc + "; poId=" + poId);
		boolean result = pageDao.updateStatus(id, CheckState.REJECTED, desc, poId);
		
		if (result) {
			scheduleDao.updatePageAuditStatus(poId, id, 0);
			scheduleDao.updateStatus(poId, ScheduleState.PASSED, null, null);
		}
		
		return result;
	}
	
	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditSchedulePagePass(long id, long poId) {
		logger.debug("auditSchedulePageReject: " + id + "; poId=" + poId);
		boolean result = pageDao.updateStatus(id, CheckState.PASSED, null, poId);
		
		if (result) {
			scheduleDao.updatePageAuditStatus(poId, id, 1);
		}
		
		return result;
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean auditSchedulePageSubmit(long id, Long poId) {
		logger.debug("auditSchedulePageSubmit: " + id);
		return pageDao.updateStatus(id, CheckState.CHECKING, null, poId);
	}

	@Cacheable(value = "poCache")
	@Override
	public PODTO getSchedulePageById(long id) {
//		logger.debug("getSchedulePageById: " + id);
//		SchedulePage page = pageDao.getSchedulePageById(id, null);
//		if (page == null) {
//			return null;
//		}
//
//		PODTO poDto = new PODTO();
//		SchedulePageDTO pageDto = new SchedulePageDTO();
//		pageDto.setPage(page);
//		poDto.setPageDTO(pageDto);
//
//		logger.debug("Successfully get po : " + poDto);
//		return poDto;
		return pageDao.getPOByPageIdOrScheduleId(id, 0);
	}

	@Cacheable(value = "poCache")
	@Override
	public PODTO getSchedulePageByScheduleId(long id) {
//		logger.debug("getSchedulePageByScheduleId: " + id);
//		SchedulePage page = pageDao.getSchedulePageByScheduleId(id, null);
//		if (page == null) {
//			logger.error("Cannot find Schedule Page by PO id '" + id + "'!!!");
//			return null;
//		}
//
//		PODTO poDto = new PODTO();
//		SchedulePageDTO pageDto = new SchedulePageDTO();
//		pageDto.setPage(page);
//		poDto.setPageDTO(pageDto);
//
//		logger.debug("Successfully get po : " + poDto);
//		return poDto;
		
		return pageDao.getPOByPageIdOrScheduleId(0, id);
	}

	@Override
	public POListDTO getSchedulePageList(ScheduleCommonParamDTO paramDTO, long pageId, String brandName) {
		paramDTO.curPage = adjustCurPage(paramDTO.curPage);

		POListDTO poList = pageDao.getSchedulePageList(paramDTO, pageId, brandName);
		//POListPager(poList, paramDTO.curPage, paramDTO.pageSize);
		
		logger.debug("Successfully get po list: " + poList);
		return poList;
	}
	
	@Override
	public List<SchedulePageDTO> getSchedulePageList(List<Long> poIdList) {
		List<SchedulePage> pageList = pageDao.getSchedulePageList(poIdList);
		
		List<SchedulePageDTO> result = new ArrayList<SchedulePageDTO>();
		for (SchedulePage page : pageList) {
			SchedulePageDTO dto = new SchedulePageDTO();
			dto.setPage(page);
			result.add(dto);
		}
		
		return result;
	}

	@Override
	public List<ScheduleStatusDTO> getSchedulePageStatusList() {
		List<ScheduleStatusDTO> statusList = new ArrayList<ScheduleStatusDTO>();
		for (CheckState item : CheckState.values()) {
			if (item != CheckState.NULL) {
				ScheduleStatusDTO status = new ScheduleStatusDTO();
				status.setId(item.getIntValue());
				status.setName(item.getDesc());
				statusList.add(status);
			}
		}
		return statusList;
	}

	@Override
	@CacheEvict(value = { "poCache" }, allEntries = true)
	public boolean updatePOPagePrdListOrderType(long poId, int type) {
		return pageDao.updatePOPagePrdListOrderType(poId, type);
	}

	@Override
	public int getPOPagePrdListOrderType(long poId) {
		SchedulePage page = pageDao.getSchedulePageByScheduleId(poId, 0L);
		if (page == null) {
			return 0;
		} else {
			return page.getPrdListOrderType();
		}
	}
}
