package com.xyl.mmall.cms.facade.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.cms.facade.ScheduleBannerFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.common.facade.ScheduleBaseFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Facade
public class ScheduleBannerFacadeImpl extends ScheduleBaseFacade implements ScheduleBannerFacade {

	@Override
	public ScheduleVO saveScheduleBanner(PODTO poDto) {
		PODTO dbDto = bannerService.saveScheduleBanner(poDto);
		ScheduleVO vo = new ScheduleVO();
		vo.setPo(dbDto);
		return vo;
	}

	@Override
	public boolean updateScheduleBanner(PODTO poDto) {
		return bannerService.updateScheduleBanner(poDto);
	}

	@Override
	public boolean deleteScheduleBannerById(long id) {
		return bannerService.deleteScheduleBannerById(id);
	}

	@Override
	public boolean auditScheduleBannerReject(PODTO poDTO) {
		return bannerService.auditScheduleBannerReject(poDTO);
	}

	@Override
	public boolean auditScheduleBannerPass(PODTO poDTO) {
		return bannerService.auditScheduleBannerPass(poDTO);
	}

	@Override
	public ScheduleVO getScheduleBannerById(long id) {
		PODTO dbDto = bannerService.getScheduleBannerById(id);
		ScheduleVO vo = new ScheduleVO();
		vo.setPo(dbDto);

		return vo;
	}

	@Override
	public ScheduleVO getScheduleBannerByScheduleId(long scheduleId) {
		PODTO dbDto = bannerService.getScheduleBannerByScheduleId(scheduleId);
		ScheduleVO vo = new ScheduleVO();
		vo.setPo(dbDto);

		return vo;
	}

	@Override
	public ScheduleListVO getScheduleBannerList(ScheduleCommonParamDTO paramDTO, String supplierName, String brandName) {
		setSiteFlag(paramDTO);
		if (paramDTO.startDate != 0) {
			paramDTO.startDate = ScheduleUtil.getSpecificBeginTime(paramDTO.startDate).getTimeInMillis();
		}
		if (paramDTO.endDate != 0) {
			paramDTO.endDate = ScheduleUtil.getSpecificEndTime(paramDTO.endDate).getTimeInMillis();			
		}
		POListDTO poList = bannerService.getScheduleBannerList(paramDTO, supplierName, brandName);
		
		for (PODTO poDTO : poList.getPoList()) {
			PODTO tmp = scheduleService.getScheduleByIdForCMSWithNoCache(poDTO.getBannerDTO().getBanner().getScheduleId());
			if (tmp.getScheduleDTO() == null || tmp.getScheduleDTO().getSchedule() == null) {
				logger.warn("Wrong or dirty data for banner with id '" + poDTO.getBannerDTO().getBanner().getId() + "'!!");
				continue;
			}
			poDTO.setScheduleDTO(tmp.getScheduleDTO());
		}
		
		return _genScheduleListVO(poList);
	}

	private ScheduleListVO _genScheduleListVO(POListDTO poList) {
//		ScheduleListVO vo = new ScheduleListVO();
//		vo.setPoList(poList);

		POListDTO filteredPOList = ScheduleUtil.filterPOList(poList);
		ScheduleListVO vo = new ScheduleListVO();
		vo.setPoList(filteredPOList);
		
		return vo;
	}
	
	@Resource
	private ScheduleService scheduleService;
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleFacadeImpl.class);

	
	@Override
	public ScheduleListVO getScheduleBannerListWithSupplierIdList(ScheduleCommonParamDTO paramDTO, String brandName) {
		setSiteFlag(paramDTO);
		if (paramDTO.startDate != 0) {
			paramDTO.startDate = ScheduleUtil.getSpecificBeginTime(paramDTO.startDate).getTimeInMillis();
		}
		if (paramDTO.endDate != 0) {
			paramDTO.endDate = ScheduleUtil.getSpecificEndTime(paramDTO.endDate).getTimeInMillis();			
		}
		POListDTO poList = bannerService.getScheduleBannerListWithSupplierIdList(paramDTO, brandName);
		
		for (PODTO poDTO : poList.getPoList()) {
			PODTO tmp = scheduleService.getScheduleByIdForCMSWithNoCache(poDTO.getBannerDTO().getBanner().getScheduleId());
			if (tmp.getScheduleDTO() == null || tmp.getScheduleDTO().getSchedule() == null) {
				logger.warn("Wrong or dirty data for banner with id '" + poDTO.getBannerDTO().getBanner().getId() + "'!!");
				continue;
			}
			poDTO.setScheduleDTO(tmp.getScheduleDTO());
		}
		
		return _genScheduleListVO(poList);
	}
}
