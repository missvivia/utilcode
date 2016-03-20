/**
 * 
 */
package com.xyl.mmall.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lihui
 *
 */
@Configuration
@ImportResource({ "classpath:config/memberProvider.xml", "classpath:config/ehcache-bean.xml" })
public class BaseConfig {

}
