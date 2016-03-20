/**
 * 
 */
package com.xyl.mmall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.mainsite.facade.UserProfileFacade;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.mobile.facade.MobileAppInfoFacade;
import com.xyl.mmall.mobile.facade.param.MobileFeedBackAO;
import com.xyl.mmall.order.service.OrderUnreadService;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class UserProfileController {

	@Autowired
	private UserProfileFacade userProfileFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private UserRedPacketService userRedPacketService;

	@Autowired
	private ConsigneeAddressFacade caFacade;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private MobileAppInfoFacade mobileAppInfoFacade;

	@Autowired
	private OrderUnreadService orderUnreadService;
	
	@Autowired
	private SMSFacade smsFacade;

	/**
	 * WAP用户中心页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/profile/index")
	public String getWapUserCenterPage(HttpServletRequest request, HttpServletResponse response, Model model) {
		long userId = SecurityContextUtils.getUserId();
		MainSiteUserVO user = userId > 0 ? userProfileFacade.getUserProfile(userId) : null;
		model.addAttribute("profile", user);
		model.addAttribute("orderCount", getOrderCount());
		int size = 0;
		if (userId > 0) {
			long couponReadTime = orderUnreadService.getUnReadTime(userId, 10);
			size = userCouponService.getUserCouponCountBycreateTime(userId, couponReadTime);
		}
		model.addAttribute("availableNum", size);
		return "pages/profile/index";
	}

	private List<Integer> getOrderCount() {
		// 1.生成各个Tab的订单计数
		Map<OrderQueryType, Integer> orderCountMap = getOrderCountMap(SecurityContextUtils.getUserId());
		// 2.转换计数
		List<Integer> countList = new ArrayList<>();
		OrderQueryType queryType = null;
		queryType = OrderQueryType.ALL;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		queryType = OrderQueryType.WAITING_PAY;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		queryType = OrderQueryType.WAITING_DELIVE;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		queryType = OrderQueryType.ALREADY_DELIVE;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		return countList;
	}

	private Map<OrderQueryType, Integer> getOrderCountMap(long userId) {
		Map<OrderQueryType, Integer> orderCountMap = new LinkedHashMap<>();
		if (userId > 0) {
			for (OrderQueryType queryType : OrderQueryType.values()) {
				if (queryType == OrderQueryType.SEARCH)
					continue;
				DDBParam param = DDBParam.genParam1();
				RetArg retArg = orderFacade.queryOrderList(userId, queryType, null, param,false);
				param = RetArgUtil.get(retArg, DDBParam.class);
				if (param != null)
					orderCountMap.put(queryType, param.getTotalCount());
			}
		}
		return orderCountMap;
	}

	/**
	 * WAP用户地址页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/profile/address")
	public String getWapUserAddressPage(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "pages/profile/address";
	}

	/**
	 * WAP用户修改地址页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/profile/address/edit", method = RequestMethod.GET)
	public String getWapUserAddressPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) String id, Model model) {
		if (StringUtils.isNotBlank(id)) {
			model.addAttribute(
					"address",
					caFacade.getAddressById(StringUtils.isNumeric(id) ? Long.parseLong(id) : 0L,
							SecurityContextUtils.getUserId()));
		}
		return "pages/profile/address.edit";
	}

	/**
	 * WAP获取省份列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/address/provinceList", method = RequestMethod.GET)
	public @ResponseBody JSONObject getWapUserAddressProvinceList(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject object = new JSONObject();
		object.put("code", ErrorCode.SUCCESS.getIntValue());
		List<LocationCode> provinceList = brandFacade.getProvices();
		List<LocationCode> newProvinceList = new ArrayList<>(provinceList.size());
		for (LocationCode province : provinceList) {
			if (StringUtils.isNotBlank(province.getProvinceHead())) {
				newProvinceList.add(province);
			}
		}
		object.put("provinceList", newProvinceList);
		return object;
	}

	@RequestMapping(value = "/profile/focus")
	public String focus(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "pages/profile/focus";
	}

	@BILog(action = "page", type = "couponPage", clientType="wap")
	@RequestMapping(value={"/coupon","/coupon/*"})
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		long userId = SecurityContextUtils.getUserId();
		orderUnreadService.updateReadTime(userId, 10);
		BigDecimal totalCash = userRedPacketService.getTotalCash(userId, new PromotionLock(userId));
		model.addAttribute("totalCash", totalCash);
		return "pages/coupon/coupon";
	}

	/**
	 * 跳转至个人基本资料页面。
	 * 
	 * @return
	 */
	@BILog(action = "page", type = "personalDataPage", clientType="wap")
	@RequestMapping(value = "/profile/basicinfo", method = RequestMethod.GET)
	public String getBasicInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		MainSiteUserVO user = userProfileFacade.getUserProfile(SecurityContextUtils.getUserId());
		model.addAttribute("profile", user);
		return "pages/profile/myinfo";
	}

	/**
	 * 跳转至反馈页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String getFeedbackPage(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "pages/feedback/feedback";
	}

	/**
	 * 跳转至手机修改页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/profile/mobile", method = RequestMethod.GET)
	public String getMobilePage(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "pages/profile/mobile";
	}

	/**
	 * 用户获取验证码。
	 * 
	 * @param phoneNum 手机号码
	 * @return
	 */
	@RequestMapping(value = "/mobile/getCode", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getVerifyCode(HttpSession session,
			@RequestParam(value = "mobile", required = true) String mobile) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!PhoneNumberUtil.isMobilePhone(mobile)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "手机号有误！");
			return ret;
		}
		// 校验重发间隔
		Long interval = (Long) session.getAttribute(MmallConstant.MOBILE_CODE_INTERVAL);
		long now = System.currentTimeMillis();
		if (interval != null && (now - interval.longValue()) < MmallConstant.MOBILE_CODE_INTERVAL_TIME) {
			long tmp = (MmallConstant.MOBILE_CODE_INTERVAL_TIME - (now - interval.longValue())) / 1000l;
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, tmp + "秒后，重新获取验证码");
			return ret;
		}
		session.setAttribute(MmallConstant.MOBILE_CODE_INTERVAL, now);
		smsFacade.sendCode(mobile, MmallConstant.SMS_MOBILE_CODE_TYPE);
		ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "验证码已发送到你的手机，15分钟内有效。");
		return ret;
	}

	/**
	 * 用户绑定手机号码。
	 * 
	 * @param phoneNum 手机号码
	 * @return
	 */
	@RequestMapping(value = "/mobile/bind", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO bindMobile(
			@RequestParam(value = "mobile", required = true) String mobile, 
			@RequestParam(value = "code", required = true) String code) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!PhoneNumberUtil.isMobilePhone(mobile)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "手机号有误！");
			return ret;
		}
		if (!smsFacade.checkCode(mobile, code, MmallConstant.SMS_MOBILE_CODE_TYPE)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "校验码无效，请重新获取");
			return ret;
		}
		UserProfileDTO userProfile = new UserProfileDTO();
		userProfile.setUserId(SecurityContextUtils.getUserId());
		userProfile.setUserName(SecurityContextUtils.getUserName());
		userProfile.setMobile(mobile);
		userProfile.setIsValid(-1); // 不更新可用状态
		userProfile.setHasNoobCoupon(-1);
		if (userProfileFacade.bindMobile(userProfile)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "绑定成功！");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "绑定失败！");
		}
		return ret;
	}

	/**
	 * 提交用户反馈
	 * 
	 * @param request
	 * @param feedBack
	 * @return
	 */
	@RequestMapping(value = "/feedback/submit", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO feedback(HttpServletRequest request, @RequestBody MobileFeedBackAO feedBack) {
		String userName = SecurityContextUtils.getUserName();
		if (feedBack == null) {
			feedBack = new MobileFeedBackAO();
		}
		feedBack.setOs("WAP");
		feedBack.setVersion("1.0");
		int areaId = AreaUtils.getProvinceCode();
		return mobileAppInfoFacade.feedBack(userName, areaId, feedBack);
	}

}
