package com.xyl.mmall.presync;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.mainsite.util.SignUtilOfCommon;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 品购页cache缓存预热
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Service
@JobPath("/presync/schedulepage")
public class SchedulePagePreSyncJob extends BasePreSyncJob {

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BusinessFacade businessFacade;

	private static final ResourceBundle preSyncResourceBundle = ResourceTextUtil
			.getResourceBundleByName("content.presync");

	private static final Logger logger = LoggerFactory.getLogger(SchedulePagePreSyncJob.class);

	// schedule中首页的tab值
	public static final int INDEX_TAB_OF_SCHEDULE = 1;

	private static final int ADDRESS_TYPE = 2;

	private static final int GENTLEMEN_TYPE = 3;

	private static final int KIDSWEAR_TYPE = 5;
	
	private static final String secKey="2ef633a2b8f541f088b71991b11d6aa1";
	
	protected void fillBizDesc() {
		super.setBizDesc("品购页cache缓存预热");
	}

	@Override
	public boolean execute(JobParam param) {
		logger.info("===begin presync schedule page");
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.size() <= 0) {
			logger.error("no area found when presync schedule page");
			return false;
		}
		
		Date date=new Date();
		
		String timestamp=String.valueOf(date.getTime());
		String sign=SignUtilOfCommon.genSig(secKey, timestamp);

		// 首页
		boolean indexFlag = this.processByType(INDEX_TAB_OF_SCHEDULE, areaList,sign,timestamp);

		// 男装
		boolean memFlag = this.processByType(GENTLEMEN_TYPE, areaList,sign,timestamp);

		// 女装
		boolean dressFlag = this.processByType(ADDRESS_TYPE, areaList,sign,timestamp);

		// 童装
		boolean kidFlag = this.processByType(KIDSWEAR_TYPE, areaList,sign,timestamp);

		logger.info("===end presync schedule page");
		return indexFlag && memFlag && dressFlag && kidFlag;
	}

	private boolean processByType(int scheduleType, List<AreaDTO> areaList,String sign,String timestamp) {
		boolean successFlag = true;
		for (AreaDTO areaDTO : areaList) {
			if (!this.processOneAreaForSpecType(areaDTO.getId(), scheduleType,sign,timestamp)) {
				logger.error("===error presync schedule page for area:" + areaDTO.getId());
				successFlag = false;
			}
		}
		return successFlag;
	}

	private boolean processOneAreaForSpecType(long areaId, int scheduleType,String sign,String timestamp) {
		logger.info("===begin presync schedule page for areaId:" + areaId + ",scheduleType:" + scheduleType);
		boolean flag = true;
		UserLoginBean bean = new UserLoginBean();
		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(bean, scheduleType, areaId, 0, 0, 0);
		if (scheduleListVO == null || scheduleListVO.getPoList() == null
				|| scheduleListVO.getPoList().getPoList() == null || scheduleListVO.getPoList().getPoList().size() <= 0) {
			logger.info(" no schedule found for presync schedule page,areaId:" + areaId + ",scheduleType:"
					+ scheduleType);
			return flag;
		}

		List<PODTO> poList = scheduleListVO.getPoList().getPoList();
		logger.info("===found nums:" + poList.size() + " for presync schedule page,areaId:" + areaId + ",scheduleType:"
				+ scheduleType);

		for (PODTO podto : poList) {
			if (podto.getScheduleDTO() == null || podto.getScheduleDTO().getSchedule() == null) {
				logger.error("===error no schedule found from podto when presync schedule page " + ",areaId:" + areaId
						+ ",scheduleType:" + scheduleType);
				continue;
			}
			String scheduleId = String.valueOf(podto.getScheduleDTO().getSchedule().getId());
			
			String url=ResourceTextUtil.getTextFromResourceByKey(preSyncResourceBundle, "schedule.url", scheduleId,sign,timestamp);

			if (!super.invokeUrl(url, 15)) {
				flag = false;
				logger.error("===error invoke url:" + url + ",areaId:" + areaId + ",scheduleType:" + scheduleType);
			}
		}
		logger.info("===finish presync schedule page for areaId:" + areaId + ",scheduleType:" + scheduleType);
		return flag;
	}

}
