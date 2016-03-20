package com.xyl.mmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("pre")
@ImportResource({"classpath:config/pre/ehcache-bean.xml"})
public class ItemPreConfiguration {

}
