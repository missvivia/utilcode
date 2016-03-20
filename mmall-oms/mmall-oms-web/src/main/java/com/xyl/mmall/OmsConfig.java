/**
 * 
 */
package com.xyl.mmall;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author hzzengdan
 *
 */
@Configuration
@ImportResource({"classpath:config/applicationProvider.xml","classpath:config/${spring.profiles.active}/ehcache-bean.xml"})
@ComponentScan
public class OmsConfig {

}
