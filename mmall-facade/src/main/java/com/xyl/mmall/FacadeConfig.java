package com.xyl.mmall;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:dubbo-cart-consumer.xml",
		"classpath:dubbo-member-consumer.xml", "classpath:dubbo-oms-consumer.xml",
		"classpath:dubbo-saleschedule-consumer.xml", "classpath:dubbo-itemcenter.xml",
		"classpath:dubbo-promotion-consumer.xml", "classpath:dubbo-order-consumer.xml",
		"classpath:dubbo-photomgr-consumer.xml", "classpath:dubbo-push-consumer.xml", "classpath:dubbo-ip-consumer.xml", 
		"classpath:dubbo-content-consumer.xml", "classpath:dubbo-tcc.xml"})
@ComponentScan
public class FacadeConfig {

}
