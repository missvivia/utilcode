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
@Profile("production")
@PropertySource({"classpath:config/production/url.properties", "classpath:config/production/oms.properties" , "classpath:config/production/item.properties" , "classpath:config/production/return.properties" , "classpath:config/production/ncs.properties", "classpath:config/production/onlineactivity.properties"})
@ImportResource({"classpath:config/production/datasource.xml","classpath:dubbo-registry.xml","classpath:config/production/dubbo-config.xml"})
@Import({NkvConfiguration.class, CommonConfiguration.class})
public class ProductionConfiguration {

}
