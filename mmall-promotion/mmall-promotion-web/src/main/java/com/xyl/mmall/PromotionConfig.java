/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * PromotionConfig.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */

@Configuration
@ImportResource({"classpath:config/applicationProvider.xml", "classpath:config/ehcache-bean.xml"})
@ComponentScan
public class PromotionConfig {

}
