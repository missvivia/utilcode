/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.common.facade.AccountFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.constant.MmallConstant;
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
@RequestMapping("/password")
public class PassWordController {
	
	private static Logger logger = Logger.getLogger(PassWordController.class);

	@Autowired
	private UserProfileFacade userProfileFacade;
	
	@Autowired
	private AccountFacade accountFacade;
	
	@Autowired
	private SMSFacade smsFacade;
	
	/**
	 * 修改密码页面请求
	 * @return
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String password(Model model) {
		model.addAttribute("success", 0);
		model.addAttribute("message", "");
		return "pages/password/password";
	}
	
	/**
	 * 修改密码
	 * @param oldPass
	 * @param newPass
	 * @param confirmPass
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@RequestParam(value = "oldPass", required = true) String oldPass,
			@RequestParam(value = "newPass", required = true) String newPass,
			@RequestParam(value = "confirmPass", required = true) String confirmPass,
			Model model) {
		final MainSiteUserVO user = userProfileFacade.getUserProfile(SecurityContextUtils.getUserId());
		if (user.getUserProfile() == null || StringUtils.isBlank(user.getAccount())) {
			model.addAttribute("success", 2);
			model.addAttribute("message", "用户不存在");
			return "pages/password/password";
		}
		AccountDTO account = accountFacade.findAccountByUserName(user.getAccount());
		if (account == null) {
			model.addAttribute("success", 2);
			model.addAttribute("message", "用户不存在");
			return "pages/password/password";
		}
		long now = System.currentTimeMillis();
		if (!accountFacade.matchesPassword(oldPass, account)) {
			logger.info("################  matches cos : " + (System.currentTimeMillis() - now));
			model.addAttribute("success", 2);
			model.addAttribute("message", "当前登录密码错误。");
			return "pages/password/password";
		}
		if (!StringUtils.equals(newPass, confirmPass)) {
			model.addAttribute("success", 2);
			model.addAttribute("message", "两次输入的密码不一致");
			return "pages/password/password";
		}
		if (!RegexUtils.isValidPassword(confirmPass)) {
			model.addAttribute("success", 2);
			model.addAttribute("message", "密码为6-20为数字或字母组合");
			return "pages/password/password";
		}
		account.setPassword(confirmPass);
		if (accountFacade.updateAccount(account)) {
			model.addAttribute("success", 1);
			model.addAttribute("message", "修改成功，请牢记新的登录密码");
		} else {
			model.addAttribute("success", 2);
			model.addAttribute("message", "修改密码失败");
		}
		return "pages/password/password";
	}
	
	/**
	 * 验证当前登录密码
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO verifyPassword(
			@RequestParam(value = "oldPass", required = true) String password) {
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
	 * 重置密码页面请求第一步
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reset/first", method = RequestMethod.GET)
	public String resetFirst(Model model) {
		model.addAttribute("code", 0);
		model.addAttribute("message", "");
		return "pages/password/reset";
	}
	
	/**
	 * 验证用户名
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/reset/verifyUserName", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO verifyUserName(
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
		ret.setResult(mobile);
		return ret;
	}
	
	/**
	 * 发送验证码
	 * @param session
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/reset/sendCode", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO sendCode(HttpSession session,
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
		// 校验重发间隔
		Long interval = (Long) session.getAttribute(MmallConstant.PASS_CODE_INTERVAL);
		long now = System.currentTimeMillis();
		if (interval != null && (now - interval.longValue()) < MmallConstant.PASS_CODE_INTERVAL_TIME) {
			long tmp = (MmallConstant.PASS_CODE_INTERVAL_TIME - (now - interval.longValue())) / 1000l;
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, tmp + "秒后，重新获取验证码");
		} else {
			session.setAttribute(MmallConstant.PASS_CODE_INTERVAL, now);
			smsFacade.sendCode(mobile, MmallConstant.SMS_PASS_CODE_TYPE);
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "短信验证码已发送到您的手机");
		}
		return ret;
	}
	
	/**
	 * 第三步校验验证码
	 * @param model
	 * @param userName
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/reset/third", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO checkCode(HttpSession session,
			@RequestParam(value = "userName", required = true) String userName,
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
		if (!smsFacade.checkCode(mobile, code, MmallConstant.SMS_PASS_CODE_TYPE)) {
			session.setAttribute(MmallConstant.PASS_CODE_SUCCESS_EXPIRE, 0l);
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "验证码错误");
		} else {
			session.setAttribute(MmallConstant.PASS_CODE_SUCCESS_EXPIRE, System.currentTimeMillis());
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "输入一个新密码");
		}
		return ret;
	}
	
	/**
	 * 重置密码
	 * @param model
	 * @param session
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/reset/final", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO resetPassword(HttpSession session,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!RegexUtils.isValidPassword(password)) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "密码为6-20为数字或字母组合");
			return ret;
		}
		// 时效
		Long expire = (Long) session.getAttribute(MmallConstant.PASS_CODE_SUCCESS_EXPIRE);
		long now = System.currentTimeMillis();
		if (expire == null || (now - expire.longValue()) > MmallConstant.PASS_CODE_SUCCESS_EXPIRE_TIME) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "重置已超时！");
			return ret;
		}
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		AccountDTO account = accountFacade.findAccountByUserName(userName);
		if (account == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "该用户名不存在");
			return ret;
		}
		final UserProfileDTO userProfileDTO = userProfileFacade.getUserProfile(userName);
		if (userProfileDTO == null || userProfileDTO.getUserId() < 0l) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "该用户名不存在");
			return ret;
		}
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
}
