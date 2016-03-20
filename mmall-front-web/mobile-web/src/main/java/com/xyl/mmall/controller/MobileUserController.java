/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.mobile.facade.MobileAuthcFacade;
import com.xyl.mmall.mobile.web.facade.MobileUserProfileFacade;
import com.xyl.mmall.mobile.web.vo.MainSiteUserVO;

/**
 * 手机用户信息相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping("/m")
public class MobileUserController {

	@Autowired
	private MobileAuthcFacade mobileAuthcFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Autowired
	private SMSFacade smsFacade;
	
	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;
	
	@Resource
	private NkvConfiguration nkvConfiguration;
	
	@Resource
	private MobileUserProfileFacade mobileUserProfileFacade;
	
	
	private static Logger logger = Logger.getLogger(MobileUserController.class);
	/**
	 * 获取当前用户的个人信息。
	 * 
	 * @return
	 */
	@RequestMapping("/getUserInfo")
	public @ResponseBody BaseJsonVO getUserInfo() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("user", mobileAuthcFacade.findUserByUserId(SecurityContextUtils.getUserId()));
//		resultMap.put("address", MobileAuthcFacadeImpl.converterDefaultAddressToVo(consigneeAddressFacade
//				.getDefaultConsigneeAddress(SecurityContextUtils.getUserId())));
		resultMap.put("address", consigneeAddressFacade
				.getDefaultConsigneeAddress(SecurityContextUtils.getUserId()));
		BaseJsonVO response = new BaseJsonVO();
		response.setCodeAndMessage(ErrorCode.SUCCESS.getIntValue(), ErrorCode.SUCCESS.getDesc());
		response.setResult(resultMap);
		return response;
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
	BaseJsonVO saveUserProfile(@RequestBody MainSiteUserVO userVO) throws Exception {
		BaseJsonVO result = mobileUserProfileFacade.saveUserProfile(SecurityContextUtils.getUserId(), userVO);
		return result;
	}

	/**
	 * 用户获取验证码。
	 * 
	 * @param phoneNum 手机号码
	 * @return
	 */
	@RequestMapping(value = "/mobile/getCode", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getVerifyCode(@RequestParam(value = "mobile", required = true) String mobile) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!PhoneNumberUtil.isMobilePhone(mobile)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "手机号有误！");
			return ret;
		}
		// 校验重发间隔
//		Long interval = (Long) session.getAttribute(MmallConstant.MOBILE_CODE_INTERVAL);
		String key = MmallConstant.PASS_CODE_INTERVAL+"_"+SecurityContextUtils.getUserId();
		String interval = getValueByKeyFromCache(key);
		
		long now = System.currentTimeMillis();
		if (interval != null && (now - NumberUtils.toLong(interval)) < MmallConstant.MOBILE_CODE_INTERVAL_TIME) {
			long tmp = (MmallConstant.MOBILE_CODE_INTERVAL_TIME - (now - NumberUtils.toLong(interval))) / 1000l;
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, tmp + "秒后，重新获取验证码");
			return ret;
		}
//		session.setAttribute(MmallConstant.MOBILE_CODE_INTERVAL, now);
		setKVToCache(key, String.valueOf(now));
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
		if (mobileUserProfileFacade.bindMobile(userProfile)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "绑定成功！");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "绑定失败！");
		}
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	private void setKVToCache(String key,String value) {
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		map.put(key.getBytes(), value.getBytes());
		try {
			defaultExtendNkvClient.hmset(nkvConfiguration.rdb_common_namespace, key.getBytes(), map,
					new NkvOption(5000, (short) 0, 1 * 5 * 60));
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("################  put value in cache error:", e);
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private String getValueByKeyFromCache(String key){
		try {
			Map<String,String> hashMap = new HashMap<>();
			Result<Map<byte[], byte[]>> formTokenMap = defaultExtendNkvClient.hgetall(nkvConfiguration.rdb_common_namespace,
					key.getBytes(), new NkvOption(5000));
			if(formTokenMap != null && formTokenMap.getResult()!= null){
				Set<Entry<byte[], byte[]>> bytes = formTokenMap.getResult().entrySet();
				for(Entry<byte[], byte[]> entry:bytes){
					hashMap.put(new String(entry.getKey(),"UTF-8"), new String(entry.getValue(),"UTF-8"));
				}
				return hashMap.get(key);
			}
			
		} catch (Exception e) {
			logger.error("################  get value by key from cache error:  "+e.getMessage());
			return "";
		} 
		return "";
	}


}
