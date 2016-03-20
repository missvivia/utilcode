/**
 * 
 */
package com.xyl.mmall.security.service;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;

import com.netease.print.security.authc.AccountValidatorImpl;
import com.netease.print.security.util.URSAuthcUtils;
import com.netease.print.security.util.URSOAuthUtils;
import com.netease.urs.UserValidator;

/**
 * @author lihui
 *
 */
public class AccountValidatorOauthImpl extends AccountValidatorImpl
{
    
    private static final String SUFFIX_POPO = ".popo";
    
    private UserValidator       userValidator;
    
    private boolean             perfTest;
    
    /**
     * (non-Javadoc)
     * 
     * @see com.netease.urs.AccountValidator#getLoginUsername(java.lang.Object[])
     */
    @Override
    public String getLoginUsername(Object [] credentials)
    {
        if (credentials instanceof Cookie [])
        { // 令牌为cookie形式
            Cookie [] cookies = (Cookie []) credentials;
            if (cookies != null && cookies.length > 0 && cookies[0] != null)
            {
                Cookie [] newCookies = new Cookie [cookies.length];
                for (int i = 0; i < cookies.length; i++)
                {
                    // 如果cookie中为ntes_osess,转换成ntes_sess以获取当前的用户名
                    if (URSOAuthUtils.URS_OAUTH_KEY.equals(cookies[i].getName()))
                    {
                        newCookies[i] = new Cookie(URSAuthcUtils.URS_KEY, cookies[i].getValue());
                    }
                    else
                    {
                        newCookies[i] = cookies[i];
                    }
                }
                String userName = isTestMode() ? getTestUserName() : userValidator
                        .getLoginUsernameByEntry("urs", newCookies);
                // 由于userValidator的特殊逻辑，将判断是否返回的userName被添加了.popo的后缀。如果有，去掉后缀。
                if (StringUtils.endsWith(userName, SUFFIX_POPO))
                {
                    userName = StringUtils.removeEnd(userName, SUFFIX_POPO);
                }
                return userName;
            }
        }
        return null;
    }
    
    @Override
    public boolean isValidCredential(String username, Object [] credentials)
    {
        return true;
        // if (perfTest)
        // {
        // Cookie [] cookieCredentials = (Cookie []) credentials;
        // String credentialsUserName = new
        // String(DigestUtils.hex2Byte(cookieCredentials[0]
        // .getValue()));
        // if (StringUtils.isNotBlank(credentialsUserName)
        // && StringUtils.contains(credentialsUserName, "@163.com")
        // && credentialsUserName.contains(username))
        // {
        // return true;
        // }
        // }
        // if (credentials instanceof Cookie [])
        // { // 令牌为cookie形式
        // Cookie [] cookies = (Cookie []) credentials;
        // if (cookies != null && cookies.length > 0 && cookies[0] != null)
        // {
        // Cookie [] newCookies = new Cookie [cookies.length];
        // for (int i = 0; i < cookies.length; i++)
        // {
        // // 如果cookie中为ntes_osess,转换成ntes_sess以获取当前的用户名
        // if (URSOAuthUtils.URS_OAUTH_KEY.equals(cookies[i].getName()))
        // {
        // newCookies[i] = new Cookie(URSAuthcUtils.URS_KEY, cookies[i].getValue());
        // }
        // else
        // {
        // newCookies[i] = cookies[i];
        // }
        // }
        // // 分别校验带.popo和不带.popo后缀的账号名
        // return super.isValidCredential(username, newCookies)
        // || super.isValidCredential(username + SUFFIX_POPO, newCookies);
        // }
        // }
        // return false;
    }
    
    /**
     * @param userValidator
     *            the userValidator to set
     */
    public void setUserValidator(UserValidator userValidator)
    {
        super.setUserValidator(userValidator);
        this.userValidator = userValidator;
    }
    
    /**
     * @return the perfTest
     */
    public boolean isPerfTest()
    {
        return perfTest;
    }
    
    /**
     * @param perfTest
     *            the perfTest to set
     */
    public void setPerfTest(boolean perfTest)
    {
        this.perfTest = perfTest;
    }
}
