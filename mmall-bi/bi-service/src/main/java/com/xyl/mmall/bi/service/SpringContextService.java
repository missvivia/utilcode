package com.xyl.mmall.bi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 以静态变量保存Spring ApplicationContext.
 * 
 * @author wangfeng
 * 
 */
@Component
public class SpringContextService implements ApplicationContextAware, DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(SpringContextService.class);

	private static final Object LOCK = new Object();

	private static ApplicationContext applicationContext;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		synchronized (LOCK) {
			SpringContextService.applicationContext = applicationContext;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextService.clear();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		logger.debug("清除SpringContextService中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

}
