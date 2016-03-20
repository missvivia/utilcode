package com.xyl.mmall.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.enums.PromotionContentTab;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.util.ChannelCacheHelper;
import com.xyl.mmall.mainsite.vo.ChannelDataVo;
import com.xyl.mmall.util.AreaUtils;

@Controller
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

	@Autowired
	private ChannelCacheHelper channelCacheHelper;

	private static final int ADDRESS_TYPE = 2;

	private static final int GENTLEMEN_TYPE = 3;

	private static final int CASE_TYPE = 4;

	private static final int KIDSWEAR_TYPE = 5;

	private static final int HOUSE_TYPE = 6;

	private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

	/**
	 * 女装,index:2
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "womenswearPage")
//	@RequestMapping(value = { "/dress" }, method = RequestMethod.GET)
	public String address(Model model, HttpServletRequest request, HttpServletResponse response) {
		long oldTime = System.currentTimeMillis();

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(ADDRESS_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		long cost = System.currentTimeMillis() - oldTime;
		logger.info("===cost for address:" + cost);

		return "pages/index/subpage";
	}

	/**
	 * 男装,index:3
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "menswearPage")
//	@RequestMapping(value = { "/gentlemen" }, method = RequestMethod.GET)
	public String gentlemen(Model model, HttpServletRequest request, HttpServletResponse response) {
		long oldTime = System.currentTimeMillis();

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(GENTLEMEN_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		long cost = System.currentTimeMillis() - oldTime;
		logger.info("===cost for gentlemen:" + cost);

		return "pages/index/subpage";
	}

	/**
	 * 包
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "shoePage")
//	@RequestMapping(value = { "/case" }, method = RequestMethod.GET)
	public String showCaseType(Model model, HttpServletRequest request, HttpServletResponse response) {
		long oldTime = System.currentTimeMillis();

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(CASE_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		long cost = System.currentTimeMillis() - oldTime;
		logger.debug("===cost for showCaseType:" + cost);

		return "pages/index/subpage";
	}

	/**
	 * 童装
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "childswearPage")
//	@RequestMapping(value = { "/kidswear" }, method = RequestMethod.GET)
	public String kidswear(Model model, HttpServletRequest request, HttpServletResponse response) {
		long oldTime = System.currentTimeMillis();

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(KIDSWEAR_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		long cost = System.currentTimeMillis() - oldTime;
		logger.info("===cost for kidswear:" + cost);

		return "pages/index/subpage";
	}

	/**
	 * 家纺
	 * 
	 * @param model
	 * @return
	 */
	@BILog(action = "page", type = "textilePage")
//	@RequestMapping(value = { "/house" }, method = RequestMethod.GET)
	public String house(Model model, HttpServletRequest request, HttpServletResponse response) {

		long oldTime = System.currentTimeMillis();

		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		Date curDate = new Date();

		this.filleDataByChannelType(HOUSE_TYPE, areaId, userId, curDate.getTime(), model, request, response);

		long cost = System.currentTimeMillis() - oldTime;
		logger.debug("===cost for house:" + cost);

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

		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		ChannelDataVo channelDataVo;
		if (currentHour >= CodeInfoUtil.PO_BEGIN_HOUR) {
			/**
			 * 新数据
			 */
			channelDataVo = channelCacheHelper.getChannelDataForFresh(channelType, areaId);
		} else {
			/**
			 * 旧数据
			 */
			channelDataVo = channelCacheHelper.getChannelDataForOld(channelType, areaId);
		}

		/**
		 * 1.轮翻推广
		 */
		// model.addAttribute(PROMOTION_CONTENT,channelDataVo.getPromotionContentList());
		Date curDate = new Date();
		List<PromotionContent> promotionContentList = promotionContentFacade
				.getPromotionContentByAreaAndTimeAndPosition(curDate.getTime(), areaId,
						this.getPromotionContentTabByParam(channelType));
		model.addAttribute(PROMOTION_CONTENT, promotionContentList);

		/**
		 * 2.最新特卖
		 */
		model.addAttribute(SCHEDULE_TODAY, channelDataVo.getLatestSchedule());

		model.addAttribute("pageTitle", channelDataVo.getTitle());
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

}
