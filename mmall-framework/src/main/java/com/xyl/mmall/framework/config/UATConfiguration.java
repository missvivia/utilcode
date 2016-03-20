package com.xyl.mmall.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Yang,Nan
 * 
 */
@Configuration
@Profile("uat")
@PropertySources(@PropertySource({"classpath:config/uat/url.properties", "classpath:config/uat/oms.properties", "classpath:config/uat/item.properties" , "classpath:config/uat/return.properties" , "classpath:config/uat/ncs.properties" , "classpath:config/uat/onlineactivity.properties"}))
@ImportResource({"classpath:config/uat/datasource.xml","classpath:dubbo-registry.xml","classpath:config/uat/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class UATConfiguration {

} 
