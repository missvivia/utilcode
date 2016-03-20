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
@Profile("feature1")
@PropertySource({ "classpath:config/feature1/url.properties", "classpath:config/feature1/oms.properties" , "classpath:config/feature1/item.properties" , "classpath:config/feature1/return.properties" , "classpath:config/feature1/ncs.properties" , "classpath:config/feature1/onlineactivity.properties"})
@ImportResource({"classpath:config/feature1/datasource.xml","classpath:dubbo-registry.xml","classpath:config/feature1/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class Feature1Configuration {

}
