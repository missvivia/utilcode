package com.xyl.mmall.mainsite.util;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.cms.enums.PromotionContentTab;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.mainsite.vo.ChannelDataVo;

@Component
public class ChannelCacheHelper {

	@Autowired
	private PromotionContentFacade promotionContentFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private IPServiceFacade iPServiceFacade;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	private static final int ADDRESS_TYPE = 2;

	private static final int GENTLEMEN_TYPE = 3;

	private static final int CASE_TYPE = 4;

	private static final int KIDSWEAR_TYPE = 5;

	private static final int HOUSE_TYPE = 6;

	private static final int PO_OF_PROMOTION_CONTENT_TYPE = 0;

	private static final String SCHEDULEID_PARAM = "?scheduleId=";

	private static final Logger logger = LoggerFactory.getLogger(ChannelCacheHelper.class);

	@Cacheable(value = "mainSiteCache")
	public ChannelDataVo getChannelDataForOld(Integer channelType, Long areaId) {
		logger.info("===load from database for getChannelDataForOld," + channelType + ",areaId:" + areaId);
		Date curDate = new Date();
		return this.getChannelDataInner(channelType, areaId, curDate);
	}
	
	@Cacheable(value = "mainSiteFreshCache")
	public ChannelDataVo getChannelDataForFresh(Integer channelType, Long areaId) {
		logger.info("===load from database for getChannelDataForFresh," + channelType + ",areaId:" + areaId);
		Date curDate = new Date();
		return this.getChannelDataInner(channelType, areaId, curDate);
	}

	public ChannelDataVo getChannelDataInner(int channelType, long areaId, Date curDate) {
		ChannelDataVo channelDataVo = new ChannelDataVo();

		// 目前千人千面没有在用
		long userId = -1;

		/**
		 * 1.轮翻推广
		 */
		List<PromotionContent> promotionContentList = promotionContentFacade
				.getPromotionContentByAreaAndTimeAndPosition(curDate.getTime(), areaId,
						this.getPromotionContentTabByParam(channelType));

		// 处理推广
		this.processPromoment(promotionContentList);

		channelDataVo.setPromotionContentList(promotionContentList);

		/**
		 * 2.最新特卖
		 */
		JSONArray jsonArray = this.getOnlineScheduleByParam(channelType, areaId, userId, curDate.getTime());
		channelDataVo.setLatestSchedule(jsonArray);

		String title = "女装";
		if (channelType == ADDRESS_TYPE) {
			title = "女装";
		} else if (channelType == GENTLEMEN_TYPE) {
			title = "男装";
		} else if (channelType == CASE_TYPE) {
			title = "鞋包";
		} else if (channelType == KIDSWEAR_TYPE) {
			title = "童装";
		} else if (channelType == HOUSE_TYPE) {
			title = "家纺";
		}

		channelDataVo.setTitle(title);
		return channelDataVo;
	}

	private void processPromoment(List<PromotionContent> promotionContentList) {
		if (promotionContentList == null || promotionContentList.isEmpty()) {
			return;
		}
		for (PromotionContent promotionContent : promotionContentList) {
			if (promotionContent.getPromotionType() == PO_OF_PROMOTION_CONTENT_TYPE) {
				String poUrl = CodeInfoUtil.DOMAIN_URL + CodeInfoUtil.PO_URL + SCHEDULEID_PARAM
						+ promotionContent.getBusinessId();
				promotionContent.setActivityUrl(poUrl);
			}
		}
	}

	private int getPromotionContentTabByParam(int channelType) {
		int promotionContentTab = 2;
		if (channelType == ADDRESS_TYPE) {
			promotionContentTab = PromotionContentTab.WOMAN.getIntValue();
		} else if (channelType == GENTLEMEN_TYPE) {
			promotionContentTab = PromotionContentTab.MAN.getIntValue();
		} else if (channelType == KIDSWEAR_TYPE) {
			promotionContentTab = PromotionContentTab.CHILD.getIntValue();
		}
		return promotionContentTab;
	}

	/**
	 * 最新特卖
	 * 
	 * @param channelType
	 * @param areaId
	 * @param userId
	 * @param curTime
	 * @return
	 */
	private JSONArray getOnlineScheduleByParam(int channelType, long areaId, long userId, long curTime) {
		int scheduleTabType = 2;
		if (channelType == ADDRESS_TYPE) {
			scheduleTabType = 2;
		} else if (channelType == GENTLEMEN_TYPE) {
			scheduleTabType = 3;
		} else if (channelType == CASE_TYPE) {
			scheduleTabType = 4;
		} else if (channelType == KIDSWEAR_TYPE) {
			scheduleTabType = 5;
		} else if (channelType == HOUSE_TYPE) {
			scheduleTabType = 6;
		}

//		UserLoginBean userLoginBean = new UserLoginBean();

//		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(userLoginBean, scheduleTabType, areaId, 0,
//				0, 0);
		ScheduleListVO scheduleListVO=scheduleFacade.getScheduleListForChl(scheduleTabType, areaId, curTime);
		JSONObject jsonObj = ScheduleUtil.geneJsonObjForValidList(scheduleListVO, null, null);
		return this.getJSONArrayByJSONObject(jsonObj);
	}

	private JSONArray getJSONArrayByJSONObject(JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("result");
		return jsonArray;
	}

}