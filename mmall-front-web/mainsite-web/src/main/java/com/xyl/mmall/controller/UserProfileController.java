/**
 * 
 */
package com.xyl.mmall.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.ScheduleLikeFacade;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.config.ExPropertyConfiguration;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.HttpUtil;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.facade.UserCouponFacade;
import com.xyl.mmall.mainsite.facade.UserProfileFacade;
import com.xyl.mmall.mainsite.vo.CouponVO;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;
import com.xyl.mmall.mainsite.vo.order.OrderFormListVO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.obj.ExCustomeServiceStatus;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.service.ScheduleService;
import com.xyl.mmall.util.AreaUtils;

/**
 * 用户信息相关。
 * 
 * @author lihui
 * 
 */
@Controller
@RequestMapping(value = "/profile")
public class UserProfileController {

	private static final String COOKIE_USER_NICK_NAME = "userNickName";

	private static final String MAIN_SITE_DOMAIN = "023.baiwandian.cn";

	@Autowired
	private UserProfileFacade userProfileFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private UserCouponFacade userCouponFacade;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private ScheduleLikeFacade scheduleLikeFacade;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Autowired
	private ReturnPackageFacade returnPackageFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;
	
	@Autowired
	private MainsiteItemFacade itemFacade;

	@Autowired
	private ExPropertyConfiguration exPropertyConfiguration;
	
	@Autowired
	private SMSFacade smsFacade;

	@RequestMapping("index")
	public String index(Model model) {
		MainSiteUserVO user = userProfileFacade.getUserProfile(SecurityContextUtils.getUserId());
		model.addAttribute("profile", user);
		return "pages/profile/index";
	}

	/**
	 * 跳转至个人基本资料页面。
	 * 
	 * @return
	 */
	@BILog(action = "page", type = "personalDataPage")
	@RequestMapping(value = "/basicinfo", method = RequestMethod.GET)
	public String getBasicInfo(Model model) {
		MainSiteUserVO user = userProfileFacade.getUserProfile(SecurityContextUtils.getUserId());
		model.addAttribute("profile", user);
		if (null != user && StringUtils.isBlank(user.getPhone())) {
			ConsigneeAddressDTO defaultAddress = consigneeAddressFacade.getDefaultConsigneeAddress(SecurityContextUtils
					.getUserId());
			if (null != defaultAddress && StringUtils.isNotBlank(defaultAddress.getConsigneeMobile())) {
				model.addAttribute("defaultAddressPhone", defaultAddress.getConsigneeMobile());
			}
		}
		return "pages/profile/myinfo";
	}
	
	/**
	 * 跳转至个人订单页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String orderInfoPage(Model model) {
		return "pages/profile/order";
	}

	/**
	 * 更新用户的基本信息
	 * 
	 * @param userVO
	 *            用户信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
	public @ResponseBody
	BaseJsonVO saveUserProfile(@RequestBody MainSiteUserVO userVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseJsonVO result = userProfileFacade.saveUserProfile(SecurityContextUtils.getUserId(), userVO);
		if (ErrorCode.SUCCESS.getIntValue() == result.getCode()) {
			MainSiteUserVO newUserVO = (MainSiteUserVO) result.getResult();
			// 更新cookie中的昵称
			setCookieWithDomain((HttpServletResponse) response, COOKIE_USER_NICK_NAME,
					URLEncoder.encode(newUserVO.getNickname(), "UTF-8"), -1, MAIN_SITE_DOMAIN);
		}
		return result;
	}

	/**
	 * 设置cookie信息。
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry
	 * @param domain
	 */
	private void setCookieWithDomain(HttpServletResponse response, String name, String value, int expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setDomain(domain);
		cookie.setPath("/");
		response.addCookie(cookie);
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
		userProfile.setIsValid(-1);
		userProfile.setHasNoobCoupon(-1);
		if (userProfileFacade.bindMobile(userProfile)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "绑定成功！");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "绑定失败！");
		}
		return ret;
	}

	/**
	 * 我的闪购。
	 * 
	 * @param phoneNum
	 *            手机号码
	 * @return
	 */
	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/summary", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO summary(HttpServletRequest request, HttpServletResponse response) {
		BaseJsonVO vo = new BaseJsonVO();
		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		int limit = 10;
		int offset = 0;

		// 获取订单
		OrderSearchParam orderSearchParam = new OrderSearchParam();
		orderSearchParam.setOrderColumn("parentId");
		orderSearchParam.setAsc(false);
		orderSearchParam.setUserId(userId);
		orderSearchParam.setVisible(true);
		orderSearchParam.setLimit(limit);
		orderSearchParam.setOffset(offset);

		RetArg retArg = orderFacade.queryNewOrderList(orderSearchParam);
		List<OrderFormListVO> order2VOList = RetArgUtil.get(retArg, ArrayList.class);
//		List<OrderForm2VO> order2VOList = MainsiteVOConvertUtil.convertToOrderForm2VOList(orderDTOList,
//				returnPackageFacade);
		// 获取优惠券
		List<CouponVO> couponVOs = userCouponFacade.getUserCouponList(userId, -1, limit, offset);

		// 获取关注的档期
//		DDBParam paramFav = new DDBParam("CreateDate", false, 12, 0);
//		RetArg arg = mainBrandFacade.getBrandUserFavListByUserId(paramFav, userId, areaId);
//		List<BrandItemDTO> brandItemList = RetArgUtil.get(arg, ArrayList.class);
		//获取关注的商品
		ProductUserFavParam productUserFavParam = new ProductUserFavParam();
		productUserFavParam.setUserId(userId);
		productUserFavParam.setLimit(limit);
		productUserFavParam.setOffset(offset);
		BaseJsonListResultVO resultVO = itemFacade.getProductDTOListByProductUserFavParam(productUserFavParam);
	

		Map<String, Object> result = new HashMap<>();
		result.put("orderList", CollectionUtils.isEmpty(order2VOList) ? new ArrayList<>() : order2VOList);
		result.put("couponList", CollectionUtils.isEmpty(couponVOs) ? new ArrayList<>() : couponVOs);
		vo.setCode(200);

		if (CollectionUtils.isEmpty(resultVO.getList())) {
			result.put("focusList", new ArrayList<>());
			vo.setResult(result);
			return vo;
		}

		result.put("focusList", resultVO.getList());
		vo.setResult(result);
		return vo;
	}

	@RequestMapping(value = "/cartandlike", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getUserCartAndLike(Model modle) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BaseJsonVO result = new BaseJsonVO(resultMap);

		long userId = SecurityContextUtils.getUserId();
		if (userId <= 0) {
			result.setCode(401);
			return result;
		}

		int areaId = AreaUtils.getProvinceCode();
		boolean hasActiveFlag = false;

		Date curDate = new Date();
		Date dateStart = DateUtils.dateFormat(curDate, DateUtils.DATE_FORMAT);
		Date dateEnd = DateUtils.addDay(dateStart, 1);

		// 查找是否有关注的品牌对应的活动在今天开始
		hasActiveFlag = brandFacade.hasActiveForBrandLike(userId, areaId, dateStart.getTime(), dateEnd.getTime());

		// 购物车相关查询,toDo
		long cartLeftTime = cartFacade.getCartLeftTime(userId, areaId);
		long cartLeftCount = cartFacade.getCartValidCount(userId, areaId);
		resultMap.put("cartLeftTime", cartLeftTime);
		resultMap.put("cartCount", cartLeftCount);

//		ExCustomeServiceStatus exCustomeServiceStatus = this.getExCustomeServiceStatus();
//		if (exCustomeServiceStatus != null && exCustomeServiceStatus.getOnline() != null
//				&& Boolean.TRUE.toString().equalsIgnoreCase(exCustomeServiceStatus.getOnline())) {
//			resultMap.put("exCustomerServiceStatus", true);
//		} else {
//			resultMap.put("exCustomerServiceStatus", false);
//		}
		resultMap.put("exCustomerServiceStatus", false);

		resultMap.put("hasActive", hasActiveFlag);
		result.setCode(ErrorCode.SUCCESS);
		return result;
	}

	private ExCustomeServiceStatus getExCustomeServiceStatus() {
		String url = exPropertyConfiguration.getCustomeServiceStatusUrl();
		String res = HttpUtil.getContent(url);
		if (StringUtils.isEmpty(res)) {
			return null;
		}
		ExCustomeServiceStatus response = JSON.parseObject(res, ExCustomeServiceStatus.class);
		return response;
	}

	@RequestMapping(value = "/focus")
	public String focus(Model model) {
		return "pages/profile/focus";
	}

	/**
	 * 获取当前用户的个人信息。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getUserInfo() {
		BaseJsonVO response = new BaseJsonVO();
		response.setCode(ResponseCode.RES_SUCCESS);
		response.setResult(userProfileFacade.getUserProfile(SecurityContextUtils.getUserId()));
		return response;
	}
}
