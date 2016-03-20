package com.xyl.mmall.cms.facade;

import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleBannerFacade {
	ScheduleVO saveScheduleBanner(PODTO poDto);

	boolean updateScheduleBanner(PODTO poDto);

	boolean auditScheduleBannerReject(PODTO poDTO);

	boolean auditScheduleBannerPass(PODTO poDTO);

	boolean deleteScheduleBannerById(long id);

	ScheduleVO getScheduleBannerById(long id);

	ScheduleVO getScheduleBannerByScheduleId(long scheduleId);

//	ScheduleListVO getScheduleBannerListByStatus(CheckState status);
//
//	ScheduleListVO getScheduleBannerListByStatus(CheckState status, int curPage, int pageSize);
//
//	ScheduleListVO getScheduleBannerListByStartDate(long startDate);
//
//	ScheduleListVO getScheduleBannerListByStartDate(long startDate, int curPage, int pageSize);
//
//	ScheduleListVO getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate);
//
//	ScheduleListVO getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate, int curPage, int pageSize);

	ScheduleListVO getScheduleBannerList(ScheduleCommonParamDTO paramDTO, String supplierName, String brandName);

	ScheduleListVO getScheduleBannerListWithSupplierIdList(ScheduleCommonParamDTO paramDTO, String brandName);
}
