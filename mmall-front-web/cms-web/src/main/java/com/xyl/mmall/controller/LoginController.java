/**
 * 
 */
package com.xyl.mmall.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xyl.mmall.cms.facade.CmsAuthcFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.security.utils.CookieUtils;

/**
 * URS登录相关。
 * 
 * @author lihui
 *
 */
@Controller
public class LoginController {
	
    private final Logger    log        = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private CmsAuthcFacade authcFacade;

	/**
	 * 跳转至登录页面。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "pages/login";
	}

	/**
	 * 用户登出。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	Subject subject = SecurityUtils.getSubject();
		try {
			subject.logout();
		} catch (SessionException ise) {
			log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		}
        Cookie xylUN = CookieUtils.createCookie(MmallConstant.XYL_CMS_USERNAME, null);
        xylUN.setMaxAge(0);
        response.addCookie(xylUN);
        
        Cookie xylSess = CookieUtils.createCookie(MmallConstant.XYL_CMS_SESS, null);
        xylSess.setMaxAge(0);
        response.addCookie(xylSess);
        
        Cookie exp = CookieUtils.createCookie(MmallConstant.XYL_CMS_EXPIRES, null);
        exp.setMaxAge(0);
        response.addCookie(exp);
        return new ResponseEntity<String>("", HttpStatus.OK);
        // WebUtils.issueRedirect(request, response, "/login");
    }

}
