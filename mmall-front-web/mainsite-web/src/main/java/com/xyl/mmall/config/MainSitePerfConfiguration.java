package com.xyl.mmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("performance")
@ImportResource({"classpath:config/performance/ehcache-bean.xml"})
public class MainSitePerfConfiguration {

}
