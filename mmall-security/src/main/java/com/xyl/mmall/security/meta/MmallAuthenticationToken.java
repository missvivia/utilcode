/**
 * ==================================================================
 * Copyright (c) JTI Co.ltd Hangzhou, 2012-2016
 * 
 * 杭州杰唐信息技术有限公司拥有该文件的使用、复制、修改和分发的许可权
 * 如果你想得到更多信息，请访问 <http://www.jtang.com.cn>
 *
 * JTang Co.ltd Hangzhou owns permission to use, copy, modify and
 * distribute this documentation.
 * For more information, please see <http://www.jtang.com.cn>
 * ==================================================================
 */

package com.xyl.mmall.security.meta;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * MmallAuthenticationToken.java created by skh at 2015年4月14日 下午7:05:36
 * 
 *
 * @author skh
 * @version 1.0
 */

public class MmallAuthenticationToken implements HostAuthenticationToken,
        RememberMeAuthenticationToken
{
    private static final long serialVersionUID = -5098620732962017660L;
    
    private String            username;
    private Object            credentials;
    private boolean           rememberMe       = false;
    private String            host;
    private MmallUserType     userType;
    
    public MmallAuthenticationToken(String username, Object credentials, MmallUserType userType)
    {
        this.username = username;
        this.credentials = credentials;
        this.userType = userType;
    }
    
    public String getUsername()
    {
        return this.username;
    }
    
    public MmallUserType getUserType()
    {
        return this.userType;
    }
    
    public void clear()
    {
        this.username = null;
        this.host = null;
        this.rememberMe = false;
        this.credentials = null;
    }
    
    public boolean equals(Object obj)
    {
        if ((obj instanceof MmallAuthenticationToken))
        {
            MmallAuthenticationToken test = (MmallAuthenticationToken) obj;
            return (super.equals(test)) && (this.host.equals(test.getHost()));
        }
        return false;
    }
    
    public int hashCode()
    {
        int code = super.hashCode();
        if (getHost() != null)
        {
            code ^= getHost().hashCode();
        }
        return code;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.shiro.authc.AuthenticationToken#getPrincipal()
     */
    @Override
    public Object getPrincipal()
    {
        return username;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.shiro.authc.AuthenticationToken#getCredentials()
     */
    @Override
    public Object getCredentials()
    {
        return this.credentials;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.shiro.authc.RememberMeAuthenticationToken#isRememberMe()
     */
    @Override
    public boolean isRememberMe()
    {
        return this.rememberMe;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.shiro.authc.HostAuthenticationToken#getHost()
     */
    @Override
    public String getHost()
    {
        return this.host;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" - ");
        sb.append(this.username);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null)
        {
            sb.append(" (").append(this.host).append(")");
        }
        return sb.toString();
    }
}
