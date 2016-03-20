package com.xyl.mmall;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Configuration
@ImportResource({"classpath:mmall-bi-service.xml","classpath:mmall-jms-base.xml","classpath:mmall-jms.xml","classpath:mvc-config.xml","classpath:config/dubbo-config.xml"})
@ComponentScan
public class BILogConfig {

}
