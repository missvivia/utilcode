/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.EncryptUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.member.dto.MobileInfoDTO;
import com.xyl.mmall.mobile.facade.MobileAuthcFacade;
import com.xyl.mmall.mobile.facade.vo.MobileUserVO;
import com.xyl.mmall.security.service.MobileAuthcService;
import com.xyl.mmall.security.token.MobileAccessToken;
import com.xyl.mmall.security.utils.DigestUtils;
import com.xyl.mmall.security.utils.MmallLoginUtils;
import com.xyl.mmall.util.AreaUtils;

/**
 * 手机登录相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping("/m")
public class MobileLoginController {

	private static Logger logger = Logger.getLogger(MobileLoginController.class);

	@Autowired
	private MobileAuthcFacade mobileAuthcFacade;

	@Autowired
	private MobileAuthcService mobileAuthcService;

	@Value("${mobile.site.url}")
	private String mobilesiteUrl;

	protected static final String APP_SIGN_KEY = "8b31b0cc107cbdd80ddad0843c20601d";

	/**
	 * 初始化手机登录需要的ID和密钥。
	 * 
	 * @param pdtVersion
	 *            应用版本号
	 * @param mac
	 *            mac地址
	 * @param deviceType
	 *            设备类型
	 * @param systemName
	 *            系统名称
	 * @param systemVersion
	 *            系统版本
	 * @param resolution
	 *            分辨路
	 * @param uniqueID
	 *            唯一标识符
	 * @return
	 */
	// @RequestMapping(value = "/initApp", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO initApp(@RequestParam("pdtVersion") String pdtVersion,
			@RequestParam("mac") String mac, @RequestParam("deviceType") String deviceType,
			@RequestParam("systemName") String systemName, @RequestParam("systemVersion") String systemVersion,
			@RequestParam("resolution") String resolution, @RequestParam("uniqueID") String uniqueID) {
		// 根据客户端参数获取初始化的Id和key
		Map<String, String> resultMap = mobileAuthcService.getInitAppIdKey(pdtVersion, mac, deviceType, systemName,
				systemVersion, resolution, uniqueID);
		BaseJsonVO response = new BaseJsonVO();
		if (resultMap.containsKey("code")) {
			// 返回initApp失败的错误信息。
			response.setCode(MobileErrorCode.SERVICE_ERROR.getIntValue());
			response.setMessage(resultMap.get("message"));
		} else {
			response.setResult(resultMap);
			// 获取成功时，保存初始化的Id和key
			mobileAuthcFacade.saveInitIdAndKey(resultMap.get("id"), resultMap.get("key"));
		}
		return response;
	}

	/**
	 * 手机用户登录入口。
	 * 
	 * @param id
	 *            手机初始化时获取的分配ID
	 * @param params
	 *            登录需要的参数，以初始化时分配的key加密封装。
	 * @param request
	 * @param response
	 * @return
	 */
	// @RequestMapping(value = "/login", method = RequestMethod.GET)
	// public @ResponseBody BaseJsonVO login(@RequestParam("id") String id,
	// @RequestParam("params") String params,
	// HttpServletRequest request, HttpServletResponse response) {
	// BaseJsonVO responseVO = new BaseJsonVO();
	// // 根据Id获取对应的key等信息
	// MobileInfoDTO mobileInfoDTO =
	// mobileAuthcFacade.findMobileInfoByInitId(id);
	// if (mobileInfoDTO == null) {
	// responseVO.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
	// responseVO.setMessage("无效的初始化ID！");
	// return responseVO;
	// }
	// // URS登录
	// Map<String, String> resultMap = mobileAuthcService.loginForMob(id,
	// params, mobileInfoDTO.getInitKey(),
	// IPUtils.getIpAddr(request));
	// if (resultMap.containsKey("code")) {
	// // 返回urs登录失败的错误信息。
	// responseVO.setCode(Integer.parseInt(resultMap.get("code")));
	// responseVO.setMessage(resultMap.get("message"));
	// } else {
	// // 登录成功时，解密返回的结果获取urs token
	// Map<String, String> ursTokenMap =
	// mobileAuthcService.decryptURSToken(resultMap.get("result"),
	// mobileInfoDTO.getInitKey());
	// // 使用token和Id访问urs进行校验，获取用户名
	// MobileAccessToken ursToken = mobileAuthcService.validateURSToken(id,
	// ursTokenMap.get("token"),
	// request.getRemoteAddr());
	// if (0 != ursToken.getCode()) {
	// responseVO.setCode(ursToken.getCode());
	// responseVO.setMessage(ursToken.getMessage());
	// return responseVO;
	// }
	// // 获取用户的昵称
	// String nickName =
	// ursAuthUserInfoService.getNicknameFromURS(ursToken.getUserName());
	// // 保存或更新用户的信息，并生成手机应用的访问token
	// Map<String, Object> userResultMap =
	// mobileAuthcFacade.upsertURSUser(ursToken.getUserName(), nickName, id,
	// ursTokenMap.get("token"), genMobileToken(ursToken.getUserName(),
	// mobileInfoDTO),
	// request.getRemoteAddr());
	// // 加密返回的token信息
	// userResultMap.put(
	// "result",
	// mobileAuthcService.encryptMobileResult((String)
	// userResultMap.get("result"),
	// mobileInfoDTO.getInitKey()));
	// responseVO.setResult(userResultMap);
	// }
	// return responseVO;
	// }

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO login(@RequestParam("username") String userName,
			@RequestParam("serialno") String serialNo, @RequestParam("timetag") String timetag,
			@RequestParam("secret") String secret, HttpServletRequest request, HttpServletResponse response) {
		BaseJsonVO ret = new BaseJsonVO();
		String password = null;
		// 第一次解密
		password = DigestUtils.decryptAES(DigestUtils.hex2Byte(secret),
				DigestUtils.hex2Byte(EncryptUtils.getMD5(timetag)));

		if (StringUtils.isNotBlank(password)) {
			// 第二次解密
			password = DigestUtils.decryptAES(DigestUtils.hex2Byte(password), DigestUtils.hex2Byte(serialNo));

			if (StringUtils.isNotBlank(password)) {
				// 第三次解密，获取密码
				password = DigestUtils.decryptAES(DigestUtils.hex2Byte(password), DigestUtils.hex2Byte(APP_SIGN_KEY));
			}

			if (StringUtils.isNotBlank(password)) {
				// 登录
				Map<String, String> map = mobileAuthcService.loginFromMobile(userName, password);
				if (StringUtils.isBlank(map.get(MmallConstant.XYL_MAINSITE_FAILED))) {
					response.addCookie(createCookie(MmallConstant.XYL_MAINSITE_USERNAME,
							map.get(MmallConstant.XYL_MAINSITE_USERNAME)));
					response.addCookie(
							createCookie(MmallConstant.XYL_MAINSITE_SESS, map.get(MmallConstant.XYL_MAINSITE_SESS)));
					response.addCookie(createCookie(MmallConstant.XYL_MAINSITE_EXPIRES,
							map.get(MmallConstant.XYL_MAINSITE_EXPIRES)));
					response.addCookie(createCookie(MmallConstant.XYL_MAINSITE_FAILED, null));
					Cookie area = createCookie(AreaUtils.COOKIE_NAME_AREA, null);
					area.setMaxAge(0);
					response.addCookie(area);
					
					ret.setResult(map);
					ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "登录成功！");
				} else {
					MmallLoginUtils.clearMainsiteLoginCookie(response);
					try {
						response.addCookie(createCookie(MmallConstant.XYL_MAINSITE_FAILED,
								URLEncoder.encode(map.get(MmallConstant.XYL_MAINSITE_FAILED), "UTF-8")));
					} catch (UnsupportedEncodingException e) {
						response.addCookie(createCookie(MmallConstant.XYL_MAINSITE_FAILED,
								"login failed:unsupported encoding!!!"));
					}
					ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, map.get(MmallConstant.XYL_MAINSITE_FAILED));
				}
				return ret;
			}
		}
		logger.error("Password decrypt error! UserName : " + userName);
		ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "登录失败！账号或密码错误！");
		return ret;
	}

	public static Cookie createCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(24*60*60);
		return cookie;
	}

	/**
	 * 手机用户第三方登录入口。
	 * 
	 * @param id
	 *            手机初始化时获取的分配ID
	 * @param target
	 *            登录方式代码
	 * @return
	 */
	// @RequestMapping(value = "/ext/login", method = RequestMethod.GET)
	public ModelAndView extLogin(@RequestParam("id") String id, @RequestParam("target") String target) {
		// 根据请求的应用编号，跳转到URS OAuth登录页面
		String url = "http://reg.163.com/outerLogin/oauth2/connect.do" + "?target=" + target + "&id=" + id
				+ "&display=mobile" + "&product=ht&url=" + mobilesiteUrl + "/m/ext/login/callback?id=" + id;
		return new ModelAndView(new RedirectView(url));
	}

	/**
	 * 手机用户第三方登录成功后回调的接口。
	 * 
	 * @param id
	 *            手机初始化时获取的分配ID
	 * @param result
	 *            登录成功后返回的结果，需以初始化时分配的key解密。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	// @RequestMapping(value = "/ext/login/callback", method =
	// RequestMethod.GET)
	public String extLoginCallback(@RequestParam("id") String id, @RequestParam("result") String result,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前ID的key等手机信息
		MobileInfoDTO mobileInfoDTO = mobileAuthcFacade.findMobileInfoByInitId(id);
		if (null == mobileInfoDTO) {
			return "redirect:/m/ext/login/failed?code=" + MobileErrorCode.LOGIN_FAIL.getIntValue() + "&message="
					+ URLEncoder.encode("无效的初始化Id", "UTF-8");
		}
		// 解密URS Oauth返回的result，获取URS token和userName的Map
		Map<String, String> ursResultMap = mobileAuthcService.decryptURSToken(result, mobileInfoDTO.getInitKey());
		// 使用urs访问token和Id获取第三方accessToken和UID
		// 使用accessToken和UID访问第三方接口获取昵称
		MobileAccessToken oAuthToken = mobileAuthcService.validateOAuthToken(id, ursResultMap.get("token"),
				ursResultMap.get("username"), mobileInfoDTO.getInitKey());
		if (0 != oAuthToken.getCode()) {
			return "redirect:/m/ext/login/failed?code=" + oAuthToken.getCode() + "&message=" + oAuthToken.getMessage();
		}
		// 保存或更新用户的信息，并生成手机应用的访问token
		Map<String, Object> userResultMap = mobileAuthcFacade.upsertOauthUser(ursResultMap.get("username"),
				oAuthToken.getNickName(), oAuthToken.getProfileImage(), id, ursResultMap.get("token"),
				genMobileToken(ursResultMap.get("username"), mobileInfoDTO), IPUtils.getIpAddr(request));
		String encryptResult = mobileAuthcService.encryptMobileResult((String) userResultMap.get("result"),
				mobileInfoDTO.getInitKey());
		String params = new StringBuilder(256).append("result=").append(encryptResult).toString();
		return "redirect:/m/ext/login/success?" + params;
	}

	/**
	 * 根据目前的token决定是否生成新的token
	 * 
	 * @param userName
	 * @param ursResultMap
	 * @return
	 */
	private String genMobileToken(String userName, MobileInfoDTO mobileInfoDTO) {
		return System.currentTimeMillis() > mobileInfoDTO.getExpiredTime()
				&& StringUtils.isNotBlank(mobileInfoDTO.getMobileToken()) ? mobileInfoDTO.getMobileToken()
						: mobileAuthcService.genMobileToken(userName, mobileInfoDTO.getInitKey());
	}

	/**
	 * 手机用户第三方登录成功后跳转的空白页面返回结果。不返回任何内容，仅作为手机端验证是否登录成功用。
	 * 
	 * @return
	 */
	// @RequestMapping(value = "/ext/login/success", method = RequestMethod.GET)
	public @ResponseBody String extLoginSuccess() {
		return null;
	}

	/**
	 * 手机用户第三方登录失败后跳转的空白页面返回结果。不返回任何内容，仅作为手机端验证是否登录成功用。
	 * 
	 * @return
	 */
	// @RequestMapping(value = "/ext/login/failed", method = RequestMethod.GET)
	public @ResponseBody String extLoginFailed() {
		return null;
	}

	/**
	 * 手机用户第三方登录方式的免登入口。
	 * 
	 * @param id
	 *            手机初始化时获取的分配ID
	 * @param params
	 *            无缝重登时需要的参数，以初始化时分配的key加密封装。
	 * @param request
	 * @param response
	 * @return
	 */
	// @RequestMapping(value = "/ext/relogin", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO relogin(@RequestParam("id") String id, @RequestParam("params") String params,
			HttpServletRequest request, HttpServletResponse response) {
		BaseJsonVO responseVO = new BaseJsonVO();
		// 根据Id获取对应的key等信息
		MobileInfoDTO mobileInfoDTO = mobileAuthcFacade.findMobileInfoByInitId(id);
		if (mobileInfoDTO == null) {
			responseVO.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
			responseVO.setMessage("无效的初始化ID！");
			return responseVO;
		}
		// 解密重新登录的params，获取URS Oauth验证Token
		Map<String, String> ursTokenMap = mobileAuthcService.decryptURSToken(params, mobileInfoDTO.getInitKey());
		// 使用urs访问token和Id获取第三方accessToken和UID
		// 使用accessToken和UID访问第三方接口获取昵称
		MobileAccessToken oAuthToken = mobileAuthcService.validateOAuthToken(id, ursTokenMap.get("token"),
				ursTokenMap.get("username"), mobileInfoDTO.getInitKey());
		if (0 != oAuthToken.getCode()) {
			responseVO.setCode(oAuthToken.getCode());
			responseVO.setMessage(oAuthToken.getMessage());
			return responseVO;
		}
		// 获取之前一次登录的user信息
		MobileUserVO reloginUser = mobileAuthcFacade.findUserByUserId(mobileInfoDTO.getUserId());
		// 保存或更新用户的信息，并生成手机应用的访问token
		Map<String, Object> userResultMap = mobileAuthcFacade.upsertOauthUser(reloginUser.getUserName(),
				oAuthToken.getNickName(), oAuthToken.getProfileImage(), id, ursTokenMap.get("token"),
				genMobileToken(reloginUser.getUserName(), mobileInfoDTO), IPUtils.getIpAddr(request));
		// 加密返回的token信息
		userResultMap.put("result", mobileAuthcService.encryptMobileResult((String) userResultMap.get("result"),
				mobileInfoDTO.getInitKey()));
		responseVO.setResult(userResultMap);
		return responseVO;
	}

	/**
	 * 手机用户第三方登录成功后置换Token的接口。
	 * 
	 * @param id
	 *            手机初始化时获取的分配ID
	 * @param result
	 *            登录成功后返回的结果，需以初始化时分配的key解密。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	// @RequestMapping(value = "/ext/login/exchange", method =
	// RequestMethod.GET)
	public @ResponseBody BaseJsonVO extLoginExchange(@RequestParam("id") String id,
			@RequestParam("params") String params, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		BaseJsonVO responseVO = new BaseJsonVO();
		// 获取当前ID的key等手机信息
		MobileInfoDTO mobileInfoDTO = mobileAuthcFacade.findMobileInfoByInitId(id);
		if (null == mobileInfoDTO) {
			responseVO.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
			responseVO.setMessage("无效的初始化ID！");
			return responseVO;
		}
		// 通过第三方的accessToken来置换URS的访问Token
		Map<String, String> ursResultMap = mobileAuthcService.exchangeURSToken(id, params, mobileInfoDTO.getInitKey());
		if (StringUtils.isBlank(ursResultMap.get("token"))) {
			responseVO.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
			responseVO.setMessage(ursResultMap.get("message"));
			return responseVO;
		}
		// 使用accessToken和UID访问第三方接口获取昵称
		MobileAccessToken oAuthToken = mobileAuthcService.getUserInfoFromExt(id, params, mobileInfoDTO.getInitKey(),
				ursResultMap.get("username"));
		if (0 != oAuthToken.getCode()) {
			responseVO.setCode(oAuthToken.getCode());
			responseVO.setMessage(oAuthToken.getMessage());
			return responseVO;
		}
		// 保存或更新用户的信息，并生成手机应用的访问token
		Map<String, Object> userResultMap = mobileAuthcFacade.upsertOauthUser(ursResultMap.get("username"),
				oAuthToken.getNickName(), oAuthToken.getProfileImage(), id, ursResultMap.get("token"),
				genMobileToken(ursResultMap.get("username"), mobileInfoDTO), IPUtils.getIpAddr(request));
		// 加密返回的token信息
		userResultMap.put("result", mobileAuthcService.encryptMobileResult((String) userResultMap.get("result"),
				mobileInfoDTO.getInitKey()));
		responseVO.setResult(userResultMap);
		return responseVO;
	}

	/**
	 * 用户登出。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public @ResponseBody BaseJsonVO logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaseJsonVO ret = new BaseJsonVO();
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.logout();
		} catch (SessionException ise) {
			logger.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "logout failed: " + ise.getMessage());
			return ret;
		}
		// 1.用户退出,清理相关cookie
		Cookie xylUN = createCookie(MmallConstant.XYL_MAINSITE_USERNAME, null);
		xylUN.setMaxAge(0);
		response.addCookie(xylUN);

		Cookie xylSess = createCookie(MmallConstant.XYL_MAINSITE_SESS, null);
		xylSess.setMaxAge(0);
		response.addCookie(xylSess);

		Cookie exp = createCookie(MmallConstant.XYL_MAINSITE_EXPIRES, null);
		exp.setMaxAge(0);
		response.addCookie(exp);

		Cookie xylErr = createCookie(MmallConstant.XYL_MAINSITE_FAILED, null);
		xylErr.setMaxAge(0);
		response.addCookie(xylErr);
		
		Cookie area = createCookie(AreaUtils.COOKIE_NAME_AREA, null);
		area.setMaxAge(0);
		response.addCookie(area);
		
		ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "用户退出成功！");
		return ret;

	}
}