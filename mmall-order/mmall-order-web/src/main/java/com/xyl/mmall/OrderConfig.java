package com.xyl.mmall;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Yang,Nan
 * 
 */
@Configuration
@ComponentScan
@ImportResource({ "classpath:config/applicationProvider.xml", "classpath:config/ehcache-bean.xml" })
public class OrderConfig {

}
