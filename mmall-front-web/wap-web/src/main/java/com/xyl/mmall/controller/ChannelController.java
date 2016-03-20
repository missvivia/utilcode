package com.xyl.mmall.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.enums.PromotionContentTab;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.util.QrqmUtils;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("")
public class ChannelController {

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

	// 轮翻推广
	private static final String PROMOTION_CONTENT = "promotionContent";

	// 最新特卖
	private static final String SCHEDULE_TODAY = "scheduleToday";

	private static final int ADDRESS_TYPE = 2;

	private static final int GENTLEMEN_TYPE = 3;

	private static final int CASE_TYPE = 4;

	private static final int KIDSWEAR_TYPE = 5;

	private static final int HOUSE_TYPE = 6;

	private static final int PO_OF_PROMOTION_CONTENT_TYPE = 0;

	private static final String SCHEDULEID_PARAM = "?scheduleId=";

	/**
	 * 女装,index:2
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "womenswearPage", clientType="wap")
	@RequestMapping(value = { "/dress" }, method = RequestMethod.GET)
	public String address(Model model, HttpServletRequest request, HttpServletResponse response) {

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(ADDRESS_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		return "pages/index/subpage";
	}

	/**
	 * 男装,index:3
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "menswearPage", clientType="wap")
	@RequestMapping(value = { "/gentlemen" }, method = RequestMethod.GET)
	public String gentlemen(Model model, HttpServletRequest request, HttpServletResponse response) {

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(GENTLEMEN_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		return "pages/index/subpage";
	}

	/**
	 * 包
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "shoePage", clientType="wap")
	@RequestMapping(value = { "/case" }, method = RequestMethod.GET)
	public String showCaseType(Model model, HttpServletRequest request, HttpServletResponse response) {

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(CASE_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		return "pages/index/subpage";
	}

	/**
	 * 童装
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "childswearPage", clientType="wap")
	@RequestMapping(value = { "/kidswear" }, method = RequestMethod.GET)
	public String kidswear(Model model, HttpServletRequest request, HttpServletResponse response) {

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(KIDSWEAR_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		return "pages/index/subpage";
	}

	/**
	 * 家纺
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "textilePage", clientType="wap")
	@RequestMapping(value = { "/house" }, method = RequestMethod.GET)
	public String house(Model model, HttpServletRequest request, HttpServletResponse response) {

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(HOUSE_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		return "pages/index/subpage";
	}

	/**
	 * 根据频道类型填充频道所需的值
	 * 
	 * @param channelType
	 *            1代表女装,2代表男装,3代表童装,4代表箱包
	 * @param userId
	 * @param curTime
	 * @param model
	 */
	private void filleDataByChannelType(int channelType, long areaId, long userId, long curTime, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		/**
		 * 1.轮翻推广
		 */
		List<PromotionContent> promotionContentList = promotionContentFacade
				.getPromotionContentByAreaAndTimeAndPosition(curTime, areaId,
						this.getPromotionContentTabByParam(channelType));

		// 处理推广
		this.processPromoment(promotionContentList);
		model.addAttribute(PROMOTION_CONTENT, promotionContentList);

		/**
		 * 2.最新特卖
		 */
		JSONArray jsonArray = this.getOnlineScheduleByParam(channelType, areaId, userId, curTime, request, response);
		model.addAttribute(SCHEDULE_TODAY, jsonArray);

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
		model.addAttribute("title", title);
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
	 * @param channelType
	 * @param areaId
	 * @param userId
	 * @param curTime
	 * @return
	 */
	private JSONArray getOnlineScheduleByParam(int channelType, long areaId, long userId, long curTime,
			HttpServletRequest request, HttpServletResponse response) {
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

		UserLoginBean userLoginBean = QrqmUtils.getCurIPLastLoginTime(request, response);

		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(userLoginBean, scheduleTabType, areaId, 0,
				0, 0);
		// List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		// List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		JSONObject jsonObj = ScheduleUtil.geneJsonObjForValidList(scheduleListVO, null, null);
		return this.getJSONArrayByJSONObject(jsonObj);
	}

	private JSONArray getJSONArrayByJSONObject(JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("result");
		return jsonArray;
	}
}
