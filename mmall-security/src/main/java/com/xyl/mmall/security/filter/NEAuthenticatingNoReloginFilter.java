/**
 * 
 */
package com.xyl.mmall.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.util.WebUtils;

import com.netease.print.security.authc.NEAuthenticationToken;
import com.netease.print.security.filter.NEAuthenticatingFilter;
import com.netease.print.security.user.SimpleUserType;
import com.netease.print.security.util.LoginUtils;
import com.xyl.mmall.security.exception.UnauthenticatedAccountException;
import com.xyl.mmall.security.utils.CookieUtils;

/**
 * @author lihui
 *
 */
public class NEAuthenticatingNoReloginFilter extends NEAuthenticatingFilter
{
    
    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.netease.print.security.filter.NEAuthenticatingFilter#executeLogin(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    protected boolean executeLogin(ServletRequest request, ServletResponse response)
            throws Exception
    {
        // 1.获取用户访问凭证
        // NEAuthenticationToken token = (NEAuthenticationToken) createToken(request,
        // response);
        NEAuthenticationToken token = createMmallToken(request, response);
        if (token == null)
            return false;
        
        if (!StringUtils.hasText(token.getUserName()))
            return false;
        
        // 2.提交凭证
        try
        {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return true;
        }
        catch (AuthenticationException ae)
        {
            cleanupAuthenticationInfo(request, response);
            // 当捕获的异常类型为未授权账户或已冻结时，需跳转到对应的错误页面，因此继续向上抛出。
            if (ae instanceof UnauthenticatedAccountException
                    || ae instanceof LockedAccountException)
            {
                throw ae;
            }
            return LoginUtils.onLoginFailure(token, ae, request, response);
        }
    }
    
    private NEAuthenticationToken createMmallToken(ServletRequest request, ServletResponse response)
    {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        Cookie [] credentials = CookieUtils.getCredentials(httpServletRequest);
        String username = CookieUtils.getCookieByName(httpServletRequest, "username").getValue();
        NEAuthenticationToken token = new NEAuthenticationToken(username, credentials,
                SimpleUserType.URS);
        return token;
    }
    
}
