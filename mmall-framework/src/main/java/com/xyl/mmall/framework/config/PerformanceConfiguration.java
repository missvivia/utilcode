package com.xyl.mmall.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Yang,Nan
 * 
 */
@Configuration
@Profile("performance")
@PropertySource({ "classpath:config/performance/url.properties", "classpath:config/performance/oms.properties" , "classpath:config/performance/item.properties" , "classpath:config/performance/return.properties" , "classpath:config/performance/ncs.properties" , "classpath:config/performance/onlineactivity.properties"})
@ImportResource({"classpath:config/performance/datasource.xml","classpath:dubbo-registry.xml","classpath:config/performance/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class PerformanceConfiguration {

}
