/**
 * 
 */
package com.xyl.mmall.security.filter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;

import com.netease.print.security.authc.AccountValidatorImpl;
import com.netease.print.security.authc.NEAuthenticationToken;
import com.netease.print.security.user.SimpleUserType;
import com.netease.urs.UserValidatorImpl;
import com.xyl.mmall.security.utils.CookieUtils;

/**
 * @author lihui
 *
 */
public class NEAuthenticatingInternalTestingFilter extends NEAuthenticatingNoReloginFilter
{
    
    private String testUserName;
    
    private String testMode;
    
    private String mobileLoginPath;
    
    private String mobilePathPattern;
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception
    {
        try
        {
            return super.onAccessDenied(request, response);
        }
        catch (Throwable e)
        {
            if (e instanceof NoClassDefFoundError || e instanceof UnsatisfiedLinkError)
            {
                Field field = ReflectionUtils.findField(this.getClass(), "accountValidator");
                ReflectionUtils.makeAccessible(field);
                AccountValidatorImpl accountValidator = (AccountValidatorImpl) ReflectionUtils
                        .getField(field, this);
                Field userField = ReflectionUtils.findField(AccountValidatorImpl.class,
                        "userValidator");
                ReflectionUtils.makeAccessible(userField);
                UserValidatorImpl userValidator = (UserValidatorImpl) ReflectionUtils.getField(
                        userField, accountValidator);
                userValidator.setTestMode(true);
                boolean loggedIn = executeLogin(request, response);
                if (!loggedIn)
                {
                    redirectToLogin(request, response);
                }
                return loggedIn;
            }
            throw e;
        }
    }
    
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
    {
        final HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        // Cookie [] credentials = URSAuthcUtils.getCredentials(httpServletRequest);
        Cookie [] credentials = CookieUtils.getCredentials(httpServletRequest);
        String userName = httpServletRequest.getParameter("username");
        if (StringUtils.isEmpty(userName))
        {
            String pInfo = getNonSpecCookieValue(httpServletRequest, "P_INFO");
            // Cookie[] pInfo = NECookieUtils.getCredentials(httpServletRequest,
            // "P_INFO");
            // if (pInfo != null && pInfo.length > 0 && pInfo[0] != null) {
            if (StringUtils.isNotBlank(pInfo))
            {
                try
                {
                    String [] pinfoStr = StringUtils.split(URLDecoder.decode(pInfo, "UTF-8"), "|");
                    if (pinfoStr != null && pinfoStr.length > 0)
                    {
                        userName = pinfoStr[0];
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isEmpty(userName) && Boolean.parseBoolean(testMode))
        {
            userName = testUserName;
        }
        // if (!StringUtils.isEmpty(userName))
        // {
        // userName = FullUserNameUtils.getFullUserName(userName);
        // }
        // else
        // {
        // return null;
        // }
        SimpleUserType simpleUserType = SimpleUserType.getSimpleUserTypeOfUrsOAuth(userName);
        NEAuthenticationToken token = new NEAuthenticationToken(userName, credentials,
                credentials[0] == null ? simpleUserType : SimpleUserType.URS);
        try
        {
            // String pInfo = getNonSpecCookieValue(httpServletRequest, "P_INFO");
            // String sInfo = getNonSpecCookieValue(httpServletRequest, "S_INFO");
            String pInfo = "pInfo";
            String sInfo = "sInfo";
            // Cookie[] pInfo = NECookieUtils.getCredentials(httpServletRequest,
            // "P_INFO");
            // Cookie[] sInfo = NECookieUtils.getCredentials(httpServletRequest,
            // "S_INFO");
            long loginTime = System.currentTimeMillis() / 1000L;
            // if (sInfo == null || sInfo.length == 0 || sInfo[0] == null) {
            if (StringUtils.isNotBlank(sInfo))
            {
                // setCookieWithDomain((HttpServletResponse) response, "S_INFO",
                // URLEncoder.encode(loginTime + "|0|##|" + token.getUserName(),
                // "UTF-8"), -1,
                // ".163.com");
                setCookieWithDomain((HttpServletResponse) response, "S_INFO",
                        URLEncoder.encode(loginTime + "|0|##|" + token.getUserName(), "UTF-8"), -1,
                        "www.example.com");
            }
            // if (pInfo == null || pInfo.length == 0 || pInfo[0] == null) {
            if (StringUtils.isNotBlank(pInfo))
            {
                String p_info_value = URLEncoder.encode(token.getUserName() + "|" + loginTime
                        + "|0|lede|00&99|null#0|null|lede|" + token.getUserName(), "UTF-8");
                setCookieWithDomain((HttpServletResponse) response, "P_INFO", p_info_value, -1,
                        "www.example.com");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return token;
    }
    
    /**
     * 获得请求中指定的Cookie值.<br>
     * 取出带特殊符号(如"@")的Cookie值
     * 
     * @param request
     * @param cookieName
     *            要获取的Cookie名称
     * @return
     */
    private static String getNonSpecCookieValue(HttpServletRequest request, String cookieName)
    {
        if (cookieName == null)
            return "";
        
        String cookies = request.getHeader("Cookie");
        if (cookies != null)
        {
            cookieName = cookieName + "=";
            int fromIndex = cookies.indexOf(cookieName);
            if (fromIndex >= 0)
            {
                int endIndex = cookies.indexOf(";", fromIndex);
                if (endIndex >= 0)
                {
                    return cookies.substring(fromIndex + cookieName.length(), endIndex);
                }
                else
                {
                    return cookies.substring(fromIndex + cookieName.length());
                }
            }
        }
        return "";
    }
    
    private void setCookieWithDomain(HttpServletResponse response, String name, String value,
            int expiry, String domain)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    /**
     * @return the testUserName
     */
    public String getTestUserName()
    {
        return testUserName;
    }
    
    /**
     * @param testUserName
     *            the testUserName to set
     */
    public void setTestUserName(String testUserName)
    {
        this.testUserName = testUserName;
    }
    
    /**
     * 
     * (non-Javadoc)
     * 
     * @see org.apache.shiro.web.filter.AccessControlFilter#getLoginUrl()
     */
    public String getLoginUrl()
    {
        // 重写获取login url的方法。根据请求的path决定
        // 是否返回正常的login页面或者mobile的login页面。
        // 由于在redirectToLogin中已经保存request，可以直接获取被保存的reqeust
        try
        {
            SavedRequest savedRequest = WebUtils.getSavedRequest(null);
            // 判断是否为mobile页面的请求
            return isMobilePageRequst(savedRequest) && StringUtils.isNotBlank(mobileLoginPath) ? mobileLoginPath
                    : super.getLoginUrl();
        }
        catch (Exception e)
        {
            if (e instanceof UnavailableSecurityManagerException)
            {
                return super.getLoginUrl();
            }
            throw e;
        }
    }
    
    /**
     * 判断是否为mobile页面的请求
     * 
     * @param savedRequest
     * @return
     */
    private boolean isMobilePageRequst(SavedRequest savedRequest)
    {
        if (null == savedRequest)
        {
            return false;
        }
        String requestURI = savedRequest.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        return StringUtils.isNotBlank(requestURI) && StringUtils.isNotBlank(mobilePathPattern)
                && pathMatcher.match(mobilePathPattern, requestURI);
    }
    
    /**
     * @return the mobilePathPattern
     */
    public String getMobilePathPattern()
    {
        return mobilePathPattern;
    }
    
    /**
     * @param mobilePathPattern
     *            the mobilePathPattern to set
     */
    public void setMobilePathPattern(String mobilePathPattern)
    {
        this.mobilePathPattern = mobilePathPattern;
    }
    
    /**
     * @return the mobileLoginPath
     */
    public String getMobileLoginPath()
    {
        return mobileLoginPath;
    }
    
    /**
     * @param mobileLoginPath
     *            the mobileLoginPath to set
     */
    public void setMobileLoginPath(String mobileLoginPath)
    {
        this.mobileLoginPath = mobileLoginPath;
    }
    
    /**
     * @return the testMode
     */
    public String getTestMode()
    {
        return testMode;
    }
    
    /**
     * @param testMode
     *            the testMode to set
     */
    public void setTestMode(String testMode)
    {
        this.testMode = testMode;
    }
}
