package com.xyl.mmall.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;

import com.xyl.mmall.security.utils.DigestUtils;

/**
 * 
 * @author hzlihui2014
 *
 */
public class NEAuthenticatingMainSiteTestingFilter extends NEAuthenticatingInternalTestingFilter
{
    
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
    {
        final HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        // String credentials =
        // URSAuthcUtils.getCredentialsFromCookie(httpServletRequest);
        // String userName = new String(DigestUtils.hex2Byte(credentials));
        // if (StringUtils.isNotBlank(userName) && StringUtils.contains(userName,
        // "@163.com"))
        // {
        // NEAuthenticationToken token = new NEAuthenticationToken(userName,
        // credentials,
        // SimpleUserType.URS);
        // return token;
        // }
        return super.createToken(httpServletRequest, response);
    }
    
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response)
    {
        String nes = getTokenValue(request);
        if (StringUtils.isBlank(nes))
            return false;
        String userName = new String(DigestUtils.hex2Byte(nes));
        if (StringUtils.isNotBlank(userName) && StringUtils.contains(userName, "@163.com"))
        {
            return true;
        }
        return super.isLoginAttempt(request, response);
    }
    
}
