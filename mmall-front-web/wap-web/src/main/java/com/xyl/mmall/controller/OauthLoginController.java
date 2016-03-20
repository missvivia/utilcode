/**
 * 
 */
package com.xyl.mmall.controller;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.netease.print.security.authc.URSOAuthService;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;
import com.xyl.mmall.security.service.MainSiteAuthcService;
import com.xyl.mmall.security.token.MobileAccessToken;

/**
 * 第三方登录相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping("/ext")
public class OauthLoginController {

	private static final String COOKIE_USER_NICK_NAME = "userNickName";

	private static final String MAIN_SITE_DOMAIN = "m.023.baiwandian.cn";

	@Autowired
	private URSOAuthService ursOAuthService;

	@Autowired
	private MainSiteAuthcFacade mainSiteAuthcFacade;

	@Autowired
	private MainSiteAuthcService mainSiteAuthcService;
	
	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	/**
	 * 用户第三方登入入口。
	 * 
	 * @param app
	 *            第三方验证的类别代码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login/{app}")
	public ModelAndView login(@PathVariable String app, HttpServletRequest request, HttpServletResponse response) {
		String redirectURL = ServletRequestUtils.getStringParameter(request, "redirectURL", "http://m.023.baiwandian.cn");
		String url = "http://reg.163.com/outerLogin/oauth2/connect.do?target=" + app
				+ "&url2=http://m.023.baiwandian.cn/login&product=mmall"
				+ "&url=http://m.023.baiwandian.cn/ext/callback?redirectURL=" + redirectURL;
		return new ModelAndView(new RedirectView(url));
	}

	/**
	 * 第三方登录成功后的回调方法。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/callback")
	public ModelAndView callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String redirectURL = ServletRequestUtils.getStringParameter(request, "redirectURL", "http://m.023.baiwandian.cn");
		if (ursOAuthService.login(request, response)) {
			onlineActivityFacade.bindCouponPack(SecurityContextUtils.getUserId());
			MobileAccessToken mobileAccessToken = mainSiteAuthcService.getNickNameFromExt(SecurityContextUtils
					.getUserName());
			if (null != mobileAccessToken && StringUtils.isNotBlank(mobileAccessToken.getNickName())) {
				// 更新用户的昵称信息
				String nickName = mainSiteAuthcFacade.updateNickName(SecurityContextUtils.getUserName(),
						mobileAccessToken.getNickName());
				if (StringUtils.isNotBlank(nickName)) {
					// 更新cookie中的昵称
					setCookieWithDomain((HttpServletResponse) response, COOKIE_USER_NICK_NAME,
							URLEncoder.encode(nickName, "UTF-8"), -1, MAIN_SITE_DOMAIN);
				}
			}
			return new ModelAndView(new RedirectView(redirectURL));
		}
		return new ModelAndView(new RedirectView("/error/403"));
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
}
