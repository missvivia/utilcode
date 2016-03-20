package com.xyl.mmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("feature1")
@ImportResource({"classpath:config/feature1/ehcache-bean.xml"})
public class MainSiteFeature1Configuration {

}
