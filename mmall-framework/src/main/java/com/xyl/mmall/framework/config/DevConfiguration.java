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
@Profile("dev")
@PropertySources(@PropertySource({"classpath:config/dev/url.properties", "classpath:config/dev/oms.properties", "classpath:config/dev/item.properties" , "classpath:config/dev/return.properties" , "classpath:config/dev/ncs.properties" , "classpath:config/dev/onlineactivity.properties"}))
@ImportResource({"classpath:config/dev/datasource.xml","classpath:dubbo-registry.xml","classpath:config/dev/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class DevConfiguration {

} 
