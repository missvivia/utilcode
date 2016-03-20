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
@Profile("pre")
@PropertySource({"classpath:config/pre/url.properties", "classpath:config/pre/oms.properties" , "classpath:config/pre/item.properties" , "classpath:config/pre/return.properties" , "classpath:config/pre/ncs.properties", "classpath:config/pre/onlineactivity.properties"})
@ImportResource({"classpath:config/pre/datasource.xml","classpath:dubbo-registry.xml","classpath:config/pre/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class PreConfiguration {

}
