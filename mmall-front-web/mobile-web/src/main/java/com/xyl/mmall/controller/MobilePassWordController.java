/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.xyl.mmall.common.facade.AccountFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.UserProfileFacade;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;

/**
 * PassWordController.java created by yydx811 at 2015年9月6日 下午2:31:17
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Controller
@RequestMapping("/m/password")
public class MobilePassWordController {

	private static Logger logger = Logger.getLogger(MobilePassWordController.class);

	@Autowired
	private UserProfileFacade userProfileFacade;

	@Autowired
	private AccountFacade accountFacade;

	@Autowired
	private SMSFacade smsFacade;


	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;
	
	@Resource
	private NkvConfiguration nkvConfiguration;

	/**
	 * 修改密码
	 * 
	 * @param oldPass
	 * @param newPass
	 * @param confirmPass
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO modify(@RequestParam(value = "oldPass", required = true) String oldPass,
			@RequestParam(value = "newPass", required = true) String newPass,
			@RequestParam(value = "confirmPass", required = true) String confirmPass) {
		final MainSiteUserVO user = userProfileFacade.getUserProfile(SecurityContextUtils.getUserId());
		BaseJsonVO ret = new BaseJsonVO();
		
		if (user.getUserProfile() == null || StringUtils.isBlank(user.getAccount())) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户不存在");
			return ret;
		}
		AccountDTO account = accountFacade.findAccountByUserName(user.getAccount());
		if (account == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户不存在");
			return ret;
		}
		long now = System.currentTimeMillis();
		if (!accountFacade.matchesPassword(oldPass, account)) {
			logger.info("################  matches cos : " + (System.currentTimeMillis() - now));
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "当前登录密码错。");
			return ret;
		}
		if (!StringUtils.equals(newPass, confirmPass)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "两次输入的密码不一致");
			return ret;
		}
		if (!RegexUtils.isValidPassword(confirmPass)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "密码为6-20为数字或字母组合");
			return ret;
		}
		account.setPassword(confirmPass);
		if (accountFacade.updateAccount(account)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "修改成功，请牢记新的登录密码");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR,"修改密码失败");
		}
		return ret;
	}

	/**
	 * 验证当前登录密码
	 * 
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO verifyPassword(@RequestParam(value = "password", required = true) String password) {
		BaseJsonVO ret = new BaseJsonVO();
		final MainSiteUserVO user = userProfileFacade.getUserProfile(SecurityContextUtils.getUserId());
		if (user.getUserProfile() == null || StringUtils.isBlank(user.getAccount())) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户不存在");
			return ret;
		}
		final AccountDTO account = accountFacade.findAccountByUserName(user.getAccount());
		if (account == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户不存在");
			return ret;
		}
		if (accountFacade.matchesPassword(password, account)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "密码正确");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "当前登录密码错。");
		}
		return ret;
	}



	/**
	 * 验证用户名
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/reset/verifyUserName", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO verifyUserName(@RequestParam(value = "userName", required = true) String userName) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		final AccountDTO account = accountFacade.findAccountByUserName(userName);
		if (account == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户名不存在");
			return ret;
		}
		final UserProfileDTO userProfileDTO = userProfileFacade.getUserProfile(userName);
		if (userProfileDTO == null || userProfileDTO.getUserId() < 0l) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户名不存在");
			return ret;
		}
		if (userProfileDTO.getIsValid() != 1) {
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "该用户名已冻结");
			return ret;
		}
		String mobile = userProfileDTO.getMobile();
		String result = checkUserMobile(mobile);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, result);
			return ret;
		}
		mobile = mobile.substring(0, 3) + "****" + mobile.substring(7);
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setMessage("校验成功");
		ret.setResult(mobile);
		return ret;
	}

	/**
	 * 发送验证码
	 * 
	 * @param session
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/reset/sendCode", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO sendCode(HttpServletRequest request,HttpServletResponse resonse,
			@RequestParam(value = "userName", required = true) String userName) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		
		final AccountDTO account = accountFacade.findAccountByUserName(userName);
		if (account == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户名不存在");
			return ret;
		}
		final UserProfileDTO userProfileDTO = userProfileFacade.getUserProfile(userName);
		if (userProfileDTO == null || userProfileDTO.getUserId() < 0l) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户名不存在");
			return ret;
		}
		String mobile = userProfileDTO.getMobile();
		String result = checkUserMobile(mobile);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, result);
			return ret;
		}
		String key = MmallConstant.PASS_CODE_INTERVAL+"_"+userName;
		String interval = getValueByKeyFromCache(key);

		// Long interval = (Long)
		// session.getAttribute(MmallConstant.PASS_CODE_INTERVAL);
		long now = System.currentTimeMillis();
		
		if (interval != null && (now - NumberUtils.toLong(interval)) < MmallConstant.PASS_CODE_INTERVAL_TIME) {
			long tmp = (MmallConstant.PASS_CODE_INTERVAL_TIME - (now - NumberUtils.toLong(interval))) / 1000l;
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, tmp + "秒后，重新获取验证码");
		} else {
//			session.setAttribute(MmallConstant.PASS_CODE_INTERVAL, now);
			setKVToCache(key, String.valueOf(now));
//			setValueofCookies(resonse,MmallConstant.PASS_CODE_INTERVAL,String.valueOf(now));
			smsFacade.sendCode(mobile, MmallConstant.SMS_PASS_CODE_TYPE);
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "短信验证码已发送到您的手机");
		}
		return ret;
	}

//	private void setValueofCookies(HttpServletResponse resonse, String passCodeInterval, String now) {
//		// TODO Auto-generated method stub
//		Cookie cookie = CookieUtils.createCookie(passCodeInterval, now);
//		resonse.addCookie(cookie);
//	}	
//
//	private Long getValueOfCookies(HttpServletRequest request, String passCodeInterval) {
//		// TODO Auto-generated method stub
//		Cookie[] cookies = request.getCookies();
//		if (cookies == null || cookies.length == 0) {
//			return null;
//		}
//		for (int i = 0; i < cookies.length; i++) {
//			String name = cookies[i].getName();
//			if (name != null && name.toLowerCase().trim().equals(passCodeInterval.toLowerCase().trim())) {
//				String value = cookies[i].getValue();
//				if(NumberUtils.isNumber(value)){
//					return Long.valueOf(value);
//				}
//			}
//		}
//		return null;
//	}

	/**
	 * 第三步校验验证码
	 * 
	 * @param model
	 * @param userName
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/reset/third", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO checkCode(HttpServletRequest request,HttpServletResponse resonse,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "code", required = true) String code) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		final AccountDTO account = accountFacade.findAccountByUserName(userName);
		if (account == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户名不存在");
			return ret;
		}
		final UserProfileDTO userProfileDTO = userProfileFacade.getUserProfile(userName);
		if (userProfileDTO == null || userProfileDTO.getUserId() < 0l) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户名不存在");
			return ret;
		}
		String mobile = userProfileDTO.getMobile();
		String result = checkUserMobile(mobile);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, result);
			return ret;
		}
		if (!RegexUtils.isValidPassword(password)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "密码为6-20为数字或字母组合");
			return ret;
		}
		
		if (!smsFacade.checkCode(mobile, code, MmallConstant.SMS_PASS_CODE_TYPE)) {
//			session.setAttribute(MmallConstant.PASS_CODE_SUCCESS_EXPIRE, 0l);
//			setValueofCookies(resonse, MmallConstant.PASS_CODE_SUCCESS_EXPIRE, "0");
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "校验码无效，请重新获取");
		} else {
//			session.setAttribute(MmallConstant.PASS_CODE_SUCCESS_EXPIRE, System.currentTimeMillis());
//			setValueofCookies(resonse, MmallConstant.PASS_CODE_SUCCESS_EXPIRE, System.currentTimeMillis()+"");
//			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "输入一个新密码");
			return resetPassword(account,password);
		}
		return ret;
	}

	/**
	 * 重置密码
	 * 
	 * @param model
	 * @param session
	 * @param userName
	 * @param password
	 * @return
	 */
//	@RequestMapping(value = "/reset/final", method = RequestMethod.POST)
	private  BaseJsonVO resetPassword(AccountDTO account,String password) {
		BaseJsonVO ret = new BaseJsonVO();
		// 修改密码
		account.setPassword(password);
		if (accountFacade.updateAccount(account)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "密码修改成功！请妥善保管");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "修改密码失败");
		}
		return ret;
	}

	private String checkUserMobile(String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return "该用户未绑定手机";
		}
		if (!PhoneNumberUtil.isMobilePhone(mobile)) {
			return "该用户绑定手机号有误";
		}
		return null;
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
