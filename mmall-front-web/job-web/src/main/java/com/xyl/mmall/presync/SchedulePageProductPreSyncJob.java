package com.xyl.mmall.presync;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 品购页商品列表cache缓存预热
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Service
@JobPath("/presync/schedulepage/product")
public class SchedulePageProductPreSyncJob extends BasePreSyncJob {

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BusinessFacade businessFacade;

	private static final ResourceBundle preSyncResourceBundle = ResourceTextUtil
			.getResourceBundleByName("content.presync");

	private static final String SCHEDULE_PAGE_PRODUCT_URL = ResourceTextUtil.getTextFromResourceByKey(
			preSyncResourceBundle, "schedule.product.url");

	private static final Logger logger = LoggerFactory.getLogger(SchedulePageProductPreSyncJob.class);

	// schedule中首页的tab值
	public static final int INDEX_TAB_OF_SCHEDULE = 1;

	private static final int ADDRESS_TYPE = 2;

	private static final int GENTLEMEN_TYPE = 3;

	private static final int KIDSWEAR_TYPE = 5;

	protected void fillBizDesc() {
		super.setBizDesc("品购页商品列表cache缓存预热");
	}

	@Override
	public boolean execute(JobParam param) {
		logger.info("===begin presync schedule page product");
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.size() <= 0) {
			logger.error("no area found when presync schedule page product");
			return false;
		}

		// 首页
		boolean indexFlag = this.processByType(INDEX_TAB_OF_SCHEDULE, areaList);

		// 男装
		boolean memFlag = this.processByType(GENTLEMEN_TYPE, areaList);

		// 女装
		boolean dressFlag = this.processByType(ADDRESS_TYPE, areaList);

		// 童装
		boolean kidFlag = this.processByType(KIDSWEAR_TYPE, areaList);

		logger.info("===end presync schedule page product");
		return indexFlag && memFlag && dressFlag && kidFlag;
	}

	private boolean processByType(int scheduleType, List<AreaDTO> areaList) {
		boolean successFlag = true;
		for (AreaDTO areaDTO : areaList) {
			if (!this.processOneAreaForSpecType(areaDTO.getId(), scheduleType)) {
				logger.error("===error presync schedule page product for area:" + areaDTO.getId());
				successFlag = false;
			}
		}
		return successFlag;
	}

	private boolean processOneAreaForSpecType(long areaId, int scheduleType) {
		logger.info("===begin presync schedule page product for areaId:" + areaId + ",scheduleType:" + scheduleType);
		boolean flag = true;
		UserLoginBean bean = new UserLoginBean();
		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(bean, scheduleType, areaId, 0, 0, 0);
		if (scheduleListVO == null || scheduleListVO.getPoList() == null
				|| scheduleListVO.getPoList().getPoList() == null || scheduleListVO.getPoList().getPoList().size() <= 0) {
			logger.info(" no schedule found for presync schedule page product,areaId:" + areaId + ",scheduleType:"
					+ scheduleType);
			return flag;
		}

		List<PODTO> poList = scheduleListVO.getPoList().getPoList();
		logger.info("===found nums:" + poList.size() + " for presync schedule page product,areaId:" + areaId
				+ ",scheduleType:" + scheduleType);

		for (PODTO podto : poList) {
			if (podto.getScheduleDTO() == null || podto.getScheduleDTO().getSchedule() == null) {
				logger.error("===error no schedule found from podto when presync schedule page product " + ",areaId:"
						+ areaId + ",scheduleType:" + scheduleType);
				continue;
			}
			long scheduleId = podto.getScheduleDTO().getSchedule().getId();

			// 构造商品访问
			String url = SCHEDULE_PAGE_PRODUCT_URL;
			
			//0到64条第一页访问
			String jsonParam1=this.getParamJson(scheduleId, 64, 0);
			boolean flag1=this.invokeUrlWithJsonParam(url, 15, jsonParam1);
			
			//第二页访问
			String jsonParam2=this.getParamJson(scheduleId, 64, 64);
			boolean flag2=this.invokeWithUrlAndParamInner(url, 15, jsonParam2);
			
			return flag1&&flag2;
		}
		logger.info("===finish presync schedule page product for areaId:" + areaId + ",scheduleType:" + scheduleType);
		return flag;
	}
	
	private boolean invokeWithUrlAndParamInner(String url,int invokeNums,String jsonParam){
		if (!super.invokeUrlWithJsonParam(url, invokeNums, jsonParam)) {
			logger.error("===error invoke url:" + url);
			return false;
		}
		return true;
	}
	
	private String getParamJson(long scheduleId,int limit,int offset){
		PoProductListSearchVO poProductListSearchVO = new PoProductListSearchVO();
		poProductListSearchVO.setCategoryId(0);
		poProductListSearchVO.setLimit(limit);
		poProductListSearchVO.setOffset(offset);
		poProductListSearchVO.setScheduleId(scheduleId);
		poProductListSearchVO.setDesc(true);
		poProductListSearchVO.setOrder(0);
		String jsonParam = JSON.toJSONString(poProductListSearchVO);
		return jsonParam;
	}

}
