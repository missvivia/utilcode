/**
 * 
 */
package com.xyl.mmall.controller;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netease.print.security.authc.LogoutService;
import com.netease.print.security.util.NEOpenIdUtils;
import com.xyl.mmall.cms.facade.CmsAuthcFacade;
import com.xyl.mmall.security.utils.SimpleLoginUtils;

/**
 * OpenID登录相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping("/openid")
public class OpenIdLoginController {

	@Autowired
	private CmsAuthcFacade authcFacade;

	@Autowired
	private LogoutService logoutService;

	/**
	 * 运维用户使用OpenID登录方式登录。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		NEOpenIdUtils.authentication(request, response, "/openid/callback");
		return "";
	}

	/**
	 * OpenID登录成功后的跳转。将检查登录后的返回结果，并跳转至首页。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (NEOpenIdUtils.check_authentication(request, response)) {
			String userName = request.getParameter("openid.sreg.email");
			try {
				SimpleLoginUtils.executeLogin(userName, request, response);
				logoutService.removeNeteaseAuthCookie(response);
				long loginTime = System.currentTimeMillis() / 1000L;
				setCookieWithDomain((HttpServletResponse) response, "S_INFO",
						URLEncoder.encode(loginTime + "|0|##|" + userName, "UTF-8"), -1, ".163.com");
				String p_info_value = URLEncoder.encode(userName + "|" + loginTime + "|0|lede|00&99|null#0|null|lede|"
						+ userName, "UTF-8");
				setCookieWithDomain((HttpServletResponse) response, "P_INFO", p_info_value, -1, ".163.com");
				return "redirect:/index?username=" + userName;
			} catch (Exception e) {
				request.setAttribute("authentication.username", userName);
				throw e;
			}
		}
		throw new UnauthorizedException("OpenId 校验失败！");
	}

	private void setCookieWithDomain(HttpServletResponse response, String name, String value, int expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setDomain(domain);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
