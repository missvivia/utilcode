/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class OnlineActivityController {

	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	/**
	 * 发送口号到用户的手机。
	 * 
	 * @param type
	 * @param phoneNum
	 * @return
	 */
	@RequestMapping(value = "/preheat/msgslogan", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO sendSloganMessage(@RequestParam("type") String type,
			@RequestParam("phoneNum") String phoneNum, HttpServletRequest request) {
		// 发送口号到指定手机号，需要有防攻击机制
		return onlineActivityFacade.sendSloganMessage(type, phoneNum, IPUtils.getIpAddr(request));
	}

	/**
	 * 为用户绑定优惠券礼包
	 * 
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/activity/getcoupon", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO getCouponPack(@RequestParam(value="p", required=false) String key) {
		return onlineActivityFacade.bindCouponPack(SecurityContextUtils.getUserId(), key);
	}

	/**
	 * 用户的活动参与信息，包括各个活动的计数等。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/activity/summary")
	public @ResponseBody BaseJsonVO getActivitySummary() {
		return onlineActivityFacade.getActivitySummary(SecurityContextUtils.getUserId());
	}

	/**
	 * 用户戳破一个mmall。
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/activity/knockpaopao")
	public @ResponseBody BaseJsonVO knockBubble(@RequestParam("id") String id) {
		return onlineActivityFacade.knockBubble(SecurityContextUtils.getUserId(), SecurityContextUtils.getUserName(),
				id);
	}

	/**
	 * 用户做一次抽奖。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/activity/lottery")
	public @ResponseBody BaseJsonVO doLottery() {
		// TODO 用户消耗一次抽奖机会做抽奖。
		long userId = SecurityContextUtils.getUserId();
		String userName = SecurityContextUtils.getUserName();
		
		return onlineActivityFacade.userLottery(userId, userName);
	}

	/**
	 * 校验用户填入的口号。
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/preheat/verifyslogan", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO verifySlogan(@RequestParam("msg") String msg) {
		return onlineActivityFacade.verifySlogan(msg);
	}

	/**
	 * 活动规则页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/activity/rule")
	public String getActivityRulePage() {
		return "pages/activity/index";
	}

	/**
	 * 活动预热页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/preheat")
	public String getPreheatPage() {
		return "pages/activity/preheat";
	}

	/**
	 * 移动版-活动预热页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/preheat/wap")
	public String getPreheatPhonePage() {
		return "pages/activity/p20150113";
	}

	/**
	 * 测试获取手机号码列表。
	 * 
	 * @return
	 */
	// @RequestMapping(value = "/preheat/phoneNum")
	public @ResponseBody BaseJsonVO getPreheatPhoneNum() {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		Map<Integer, List<String>> map = new HashMap<>();
		for (int i = 0; i < 100; i++) {
			Set<byte[]> set = onlineActivityFacade.getOnlineActivitySms(i);
			List<String> list = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(set)) {
				for (byte[] b : set) {
					list.add(new String(b));
				}
			}
			map.put(i, list);
		}
		result.setResult(map);
		return result;
	}

	/**
	 * 返回第三方分享的页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/activity/3dshare")
	public String getThirdSharePage() {
		return "pages/activity/3dshare";
	}
	
	/**
	 * 校验活动是否结束。
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/preheat/activityend", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO isActivityEnd() {
		BaseJsonVO result = new BaseJsonVO();
		long currentTime = System.currentTimeMillis();
		result.setResult(currentTime > OnlineActivityConstants.ACTIVITY_END_TIME
				|| currentTime < OnlineActivityConstants.ACTIVITY_START_TIME);
		result.setCode(ErrorCode.SUCCESS);
		return result;
	}

}
