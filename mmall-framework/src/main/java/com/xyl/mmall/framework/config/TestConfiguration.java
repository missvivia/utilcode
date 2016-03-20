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
@Profile("test")
@PropertySource({ "classpath:config/test/url.properties", "classpath:config/test/oms.properties" , "classpath:config/test/item.properties" , "classpath:config/test/return.properties" , "classpath:config/test/ncs.properties", "classpath:config/test/onlineactivity.properties"})
@ImportResource({"classpath:config/test/datasource.xml","classpath:dubbo-registry.xml","classpath:config/test/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class TestConfiguration {

}
