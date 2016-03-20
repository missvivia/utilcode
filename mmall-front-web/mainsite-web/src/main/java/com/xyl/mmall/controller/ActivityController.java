package com.xyl.mmall.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.user.SimpleUserType;
import com.netease.print.security.util.LoginUtils;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.service.ActivationRecordService;
import com.xyl.mmall.util.EncryptUtils;

/**
 * 
 * @author hzlihui2014
 *
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController {
	
	@Autowired
	private ApplicationContext ctx;
	
	@Resource
	private OnlineActivityFacade onlineActivityFacade;
	
	@Autowired
	private ActivationRecordService activationRecordService;
	
	@Value("${mailMater.coupon.list}")
	private String couponCodes;
	
//	@RequestMapping(value = "/20150113", method = RequestMethod.GET)
//	public String getIndex(Model model) {
//		return "pages/activity/20150113";
//	}
	
	@RequestMapping(value = "/20150916", method = RequestMethod.GET)
	public String getIndex(Model model) {
		return "pages/activity/20150916";
	}
	
	/**
	 * 邮箱大师合作
	 * @param request
	 * @param model
	 */
//	@RequestMapping("mailMaster/coupon")
	public String mailMasterCoupon(HttpServletRequest request, HttpServletResponse response) {
		String email = StringUtils.trim(ServletRequestUtils.getStringParameter(request, "email", ""));
		String couponCode = StringUtils.trim(ServletRequestUtils.getStringParameter(request, "couponCode", ""));
		String sign = StringUtils.trim(ServletRequestUtils.getStringParameter(request, "sign", ""));
		boolean free = ServletRequestUtils.getBooleanParameter(request, "free", false);
		
		String validSign = EncryptUtils.encrypt(couponCode + "paopao_163_com", "md5");
		if (free && !StringUtils.equalsIgnoreCase(sign, validSign)) {
			return "pages/404";
		}
		
		Map<String, Object> model = new HashMap<>();
		try {
			if (StringUtils.isBlank(email)) {
				model.put("state", -1);
				model.put("msg", "Email不能为空");
				return "pages/404";
			}
			
			if (free && StringUtils.isBlank(couponCode)) {
				model.put("state", -2);
				model.put("msg", "优惠券不能为空");
				return "pages/404";
			}
			
			LoginUtils.executeLogin(email, SimpleUserType.URS, request, response);
			long userId = SecurityContextUtils.getUserId();
			
			Map<String, Object> map = activationRecordService.dispatchCouponCode(userId, free, couponCode, couponCodes, new PromotionLock(userId));
			int state = (int) map.get("state");
			
			if (state == 1) {
				String url = "http://m.023.baiwandian.cn/activity/cooperate/coupon?from=mailMaster&time=" + System.currentTimeMillis();
				return "redirect:" + url;
			}
			
		} catch (Exception e) {
		}
		return "pages/404";
	}
	
//	@RequestMapping(value = "/20150213", method = RequestMethod.GET)
	public String get20150213Index(Model model) {
		return "pages/activity/20150213";
	}
	
//	@RequestMapping(value = "/20150213/lottery")
	@ResponseBody
	public Map<String, Object> get20150213Lottery() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("optFlag", 1);
		result.put("giftType", 5);
		return result;
	}
}
