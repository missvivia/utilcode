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

package com.xyl.mmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.netease.backend.trace.filters.TraceListener;

/**
 * ErpApiConfig.java created by skh at 2015年6月4日 下午1:22:06
 * 
 *
 * @author skh
 * @version 1.0
 */
@Configuration
@ImportResource({ "classpath:config/mvc-config.xml", "classpath:config/dubbo-config.xml",
        "classpath:config/${spring.profiles.active}/ehcache-bean.xml" })
public class ErpApiConfig
{
    /**
     * WEB项目需要的trace listener。
     * 
     * @return TraceListener
     */
    @Bean
    TraceListener traceListener()
    {
        return new TraceListener();
    }
}
