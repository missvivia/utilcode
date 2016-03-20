package com.xyl.mmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@ImportResource({"classpath:config/test/ehcache-bean.xml"})
public class IPTestConfiguration {

}
