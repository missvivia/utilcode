package com.xyl.mmall.bi.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Configuration
@Profile("test")
@PropertySources(@PropertySource({ "classpath:config/test/mmallBi.properties" }))
@ImportResource({ "classpath:mmall-bi-msg.xml" })
public class TestBIConfig {

}
