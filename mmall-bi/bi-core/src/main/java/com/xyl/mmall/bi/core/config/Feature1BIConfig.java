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
@Profile("feature1")
@PropertySources(@PropertySource({ "classpath:config/feature1/mmallBi.properties" }))
@ImportResource({ "classpath:mmall-bi-msg.xml" })
public class Feature1BIConfig {

}
