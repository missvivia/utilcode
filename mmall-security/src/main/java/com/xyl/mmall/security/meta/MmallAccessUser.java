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

import java.io.Serializable;

import com.netease.print.security.authc.NEAccessUser;

/**
 * MmallUser.java created by skh at 2015年4月15日 上午10:46:37
 * 
 *
 * @author skh
 * @version 1.0
 */

public class MmallAccessUser extends NEAccessUser implements Serializable
{
    private static final long serialVersionUID = 9012589422638055265L;
    private long              vistorId;
    private String            vistorName;
    private long              hostId;
    private String            hostName;
    private MmallUserType     userType;
    private Object credential;
    
    public MmallAccessUser()
    {
        
    }
    
    public MmallAccessUser(String vistorName)
    {
        this.vistorName = vistorName;
    }
    
    public MmallAccessUser(long vistorId, String vistorName, long hostId, String hostName)
    {
        this.vistorId = vistorId;
        this.vistorName = vistorName;
        this.hostId = hostId;
        this.hostName = hostName;
    }
    
    public long getVistorId()
    {
        return vistorId;
    }
    
    public void setVistorId(long vistorId)
    {
        this.vistorId = vistorId;
    }
    
    public String getVistorName()
    {
        return vistorName;
    }
    
    public void setVistorName(String vistorName)
    {
        this.vistorName = vistorName;
    }
    
    public long getHostId()
    {
        return hostId;
    }
    
    public void setHostId(long hostId)
    {
        this.hostId = hostId;
    }
    
    public String getHostName()
    {
        return hostName;
    }
    
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    
    public MmallUserType getUserType()
    {
        return userType;
    }
    
    public void setUserType(MmallUserType userType)
    {
        this.userType = userType;
    }
    
    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = prime + (this.vistorName == null ? 0 : this.vistorName.hashCode());
        return result;
    }
    
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        
        if (object == null)
        {
            return false;
        }
        
        if (getClass() != object.getClass())
        {
            return false;
        }
        
        MmallAccessUser other = (MmallAccessUser) object;
        if (this.vistorName == null)
        {
            if (other.vistorName != null)
            {
                return false;
            }
        }
        else if (!this.vistorName.equals(other.vistorName))
        {
            return false;
        }
        return true;
    }
    
    public String toString()
    {
        return this.vistorName;
    }

    public Object getCredential()
    {
        return credential;
    }

    public void setCredential(Object credential)
    {
        this.credential = credential;
    }
}
