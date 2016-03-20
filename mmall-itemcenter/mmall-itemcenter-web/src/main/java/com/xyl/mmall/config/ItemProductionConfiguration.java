package com.xyl.mmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
@ImportResource({"classpath:config/production/ehcache-bean.xml"})
public class ItemProductionConfiguration {

}
