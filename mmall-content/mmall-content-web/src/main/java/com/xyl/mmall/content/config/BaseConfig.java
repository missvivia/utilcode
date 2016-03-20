/**
 * 
 */
package com.xyl.mmall.content.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lihui
 *
 */
@Configuration
@ImportResource({"classpath:config/contentProvider.xml", "classpath:config/ehcache-bean.xml"} )
public class BaseConfig {

}
