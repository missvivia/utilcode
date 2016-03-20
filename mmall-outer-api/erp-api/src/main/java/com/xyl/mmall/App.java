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

package com.xyl.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * App.java created by skh at 2015年6月4日 上午11:25:34
 * 
 *
 * @author skh
 * @version 1.0
 */
@EnableAutoConfiguration
@ComponentScan
public class App
{
    
    /**
     * @param args
     */
    public static void main(String [] args)
    {
        SpringApplication.run(App.class, args);
    }
    
}
