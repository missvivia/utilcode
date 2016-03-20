/**
 * ==================================================================
 * Copyright (c) XINYUNLIAN Co.ltd Hangzhou, 2015-2016
 * 
 * 杭州新云联技术有限公司拥有该文件的使用、复制、修改和分发的许可权
 * 如果你想得到更多信息，请访问 <http://www.xinyunlian.com>
 *
 * XINYUNLIAN Co.ltd Hangzhou owns permission to use, copy, modify and
 * distribute this documentation.
 * For more information, please see <http://www.xinyunlian.com>
 * ==================================================================
 */

package com.xyl.mmall.bi.core.meta;

/**
 * ApiConsumerLog.java created by skh at 2015年6月4日 下午4:45:22
 * 
 *
 * @author skh
 * @version 1.0
 */

public class ApiConsumerLog extends BasicLog
{
    private static final long serialVersionUID = -8181153415104673878L;
    
    private String            parameters;
    
    public ApiConsumerLog()
    {
        super();
    }
    
    public String getParameters()
    {
        return this.parameters;
    }
    
    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }
}
