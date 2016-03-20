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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.xyl.mmall.handler.AllRequestsInterceptor;

/**
 * WebConfig.java created by skh at 2015年6月23日 下午3:56:04
 * 
 *
 * @author skh
 * @version 1.0
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Value("${isTest}")
    private boolean isTest;
    
    public void addInterceptors(InterceptorRegistry registry)
    {
        // registry.addInterceptor(new LocaleInterceptor());
        // registry.addInterceptor(new
        // ThemeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        // registry.addInterceptor(new
        // SecurityInterceptor()).addPathPatterns("/secure/*");
        if (!isTest)
        {
            registry.addInterceptor(new AllRequestsInterceptor()).addPathPatterns("/**");
        }
    }
}
