/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.logger.CommonLogger;
import com.xyl.mmall.mobile.facade.MobileAddressFacade;
import com.xyl.mmall.mobile.facade.MobileAppInfoFacade;
import com.xyl.mmall.mobile.facade.MobileCouponGiftFacade;
import com.xyl.mmall.mobile.facade.MobileFavoriteFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileConfig;
import com.xyl.mmall.mobile.facade.converter.MobileOS;
import com.xyl.mmall.mobile.facade.param.MobileAddressAO;
import com.xyl.mmall.mobile.facade.param.MobileFeedBackAO;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.service.MobileHeaderProcess;

/**
 * @author lihui
 *
 */
@RestController
@RequestMapping("/m")
public class MobileUserCenterController {
	@Autowired
	private CommonLogger logger;

	@Autowired
	private MobileFavoriteFacade mobileFavoriteFacade;

	@Autowired
	private MobileAddressFacade mobileAddressFacade;

	@Autowired
	private MobileCouponGiftFacade mobileCouponGiftFacade;

	@Autowired
	private MobileAppInfoFacade mobileAppInfoFacade;

	// 获取收藏列表
	@RequestMapping(value = "/getFavoriteList", method = RequestMethod.GET)
	public BaseJsonVO getFavoriteList(HttpServletRequest request,
			@RequestParam(value = "type", required = false) Integer type, MobilePageCommonAO pager) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		return mobileFavoriteFacade.getFavoriteList(userId, areaCode, type, pager);
	}

	// 添加收藏
	@RequestMapping(value = "/addFavorite", method = RequestMethod.GET)
	public BaseJsonVO addFavorite(HttpServletRequest request,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "id", required = false) Long id) {
		Long userId = MobileHeaderProcess.getUserId();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("branId", id);
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		logger.logger(ao, "click", "followBrand", userId, map);
		return mobileFavoriteFacade.addFavorite(userId, type, id,areaCode);
	}

	// 取消收藏
	@RequestMapping(value = "/cancelFavorite", method = RequestMethod.GET)
	public BaseJsonVO cancelFavorite(HttpServletRequest request,@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "id", required = false) Long id) {
		Long userId = MobileHeaderProcess.getUserId();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("branId", id);
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		logger.logger(ao, "click", "defollowBrand", userId, map);
		return mobileFavoriteFacade.cancelFavorite(userId, type, id);
	}

	@RequestMapping(value = "/getGiftMoneyList", method = RequestMethod.GET)
	public BaseJsonVO getGiftMoneyList(HttpServletRequest request, MobilePageCommonAO pager,@RequestParam(value = "status", required = false) Integer status) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		BaseJsonVO b = mobileCouponGiftFacade.getUserGiftList(userId, pager,status,Converter.protocolVersion(ao.getProtocolVersion()));
		return b;

	}

	@RequestMapping(value = "/getCouponList", method = RequestMethod.GET)
	public BaseJsonVO getCouponList(HttpServletRequest request, MobilePageCommonAO pager,@RequestParam(value = "status", required = false) Integer status) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		logger.logger(ao, "page", "couponPage", userId);
		return mobileCouponGiftFacade.getUserCouponList(userId, pager,status);

	}

	@RequestMapping(value = "/bindCoupon", method = RequestMethod.GET)
	public BaseJsonVO getCouponList(HttpServletRequest request,
			@RequestParam(value = "couponCode", required = false) String couponCode) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaId = MobileHeaderProcess.getAreaCode(ao);
		return mobileCouponGiftFacade.bindCoupon(userId, areaId, couponCode);

	}

	// 获取地址列表
	@RequestMapping(value = "/getAddressList", method = RequestMethod.GET)
	public BaseJsonVO getAddressList(HttpServletRequest request) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		logger.logger(ao, "page", "addressPage", userId);
		return mobileAddressFacade.getConsigneeAddressList(userId);
	}

	// 更新地址
	@RequestMapping(value = "/updateAddress", method = RequestMethod.POST, consumes = { "application/x-www-form-urlencoded;charset=UTF-8" })
	public BaseJsonVO updateAddress(MobileAddressAO ao) {
		Long userId = MobileHeaderProcess.getUserId();
		return mobileAddressFacade.updateConsigneeAddress(userId, ao);
	}

	/**
	 * 
	 * 0---------------------------
	 */
	// 获取推荐
	// TODO
	@RequestMapping(value = "/getRecommendAPP", method = RequestMethod.GET)
	public BaseJsonVO getRecommendAPP(HttpServletRequest request,
			@RequestParam(value = "channel", required = false) String channel, MobilePageCommonAO pager) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaId = MobileHeaderProcess.getAreaCode(ao);
		return mobileAppInfoFacade.getRecommend(channel, areaId, ao);
	}

	// 反馈
	// TODO
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public BaseJsonVO feedback(HttpServletRequest request, MobileFeedBackAO feedBack) {
		String userName = MobileHeaderProcess.getUserName();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		if (feedBack == null)
			feedBack = new MobileFeedBackAO();

		feedBack.setOs(ao.getOs() + ao.getOsVersion());
		feedBack.setVersion(ao.getAppVersion());
		int areaId = MobileHeaderProcess.getAreaCode(ao);
		return mobileAppInfoFacade.feedBack(userName, areaId, feedBack);
	}

	// 检测更新
	// TODO
	@RequestMapping(value = "/upgradeCheck", method = RequestMethod.GET)
	public BaseJsonVO upgradeCheck(HttpServletRequest request) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		MobileOS os = MobileOS.getOs(ao.getOs());
		boolean update = Converter.protocolVersion(ao.getAppVersion()) < Converter.protocolVersion(MobileConfig.version);
		return mobileAppInfoFacade.updateVersion(os,ao.getChannel(),update);
	}
}
