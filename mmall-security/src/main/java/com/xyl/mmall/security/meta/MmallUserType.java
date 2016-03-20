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

/**
 * MmallUserType.java created by skh at 2015年4月14日 下午7:08:07
 * 
 *
 * @author skh
 * @version 1.0
 */

public enum MmallUserType
{
    NULL(-1, "NULL"),
    
    NORMAL(1, "NORMAL");
    
    private final int    value;
    private final String description;
    
    private MmallUserType(int value, String description)
    {
        this.value = value;
        this.description = description;
    }
    
    public MmallUserType genEnumByIntValue(int intValue)
    {
        for (MmallUserType item : values())
        {
            if (item.value == intValue)
            {
                return item;
            }
        }
        return NULL;
    }
    
    public MmallUserType genEnumByDescription(String desc)
    {
        for (MmallUserType item : values())
        {
            if (item.description.equalsIgnoreCase(desc))
            {
                return item;
            }
        }
        return NULL;
    }
    
    public static MmallUserType genMmallUserType(int v)
    {
        return NULL.genEnumByIntValue(v);
    }
    
    public static MmallUserType genMmallUserType(String desc)
    {
        return NULL.genEnumByDescription(desc);
    }
    
    public int getValue()
    {
        return this.value;
    }
    
    public String getDescription()
    {
        return this.description;
    }
}
