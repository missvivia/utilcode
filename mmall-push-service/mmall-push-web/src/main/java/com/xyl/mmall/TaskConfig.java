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
@ImportResource({"classpath:config/*.xml","classpath:mmall-jms-base.xml"})
public class TaskConfig {

}
