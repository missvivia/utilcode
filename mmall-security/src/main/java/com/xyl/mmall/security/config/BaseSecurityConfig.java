/**
 * 
 */
package com.xyl.mmall.security.config;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

/**
 * @author lihui
 *
 */
@ImportResource(value = "classpath:config/security-config.xml")
public abstract class BaseSecurityConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseSecurityConfig.class);

	/**
	 * 
	 */
	private static final String PROPERTY_WEB_UNAUTHORIZED_URL = "web.unauthorizedUrl";

	/**
	 * 
	 */
	private static final String BEAN_NAME_SECURITY_MANAGER = "securityManager";

	/**
	 * 
	 */
	private static final String PROPERTY_WEB_LOGIN_URL = "web.loginUrl";

	/**
	 * 
	 */
	protected static final String PROPERTY_TEST_USER_NAME = "urs.testUserName";

	/**
	 * 
	 */
	protected static final String PROPERTY_TEST_MODE = "urs.testMode";

	/**
	 * property of cookie domain
	 */
	protected static final String PROPERTY_WEB_COOKIE_DOMAIN = "web.cookie.domain";

	/**
	 * Bean name of accountValidator
	 */
	protected static final String BEAN_NAME_ACCOUNT_VALIDATOR = "accountValidator";

	private static final String PROPERTY_FILTER_CHAIN_DEFINITIONS = "filter.chain.definitions";

	@Autowired
	protected Environment env;

	@Autowired
	protected ApplicationContext context;

	@Autowired
	private FactoryBean<Ini.Section> appChainDefinitionSection;

	@Bean
	FilterRegistrationBean shiroSecurityFilterRegistration() throws Exception {
		try {
			FilterRegistrationBean registrationBean = new FilterRegistrationBean();
			registrationBean.setFilter((Filter) shiroSecurityFilter().getObject());
			registrationBean.setOrder(0);
			return registrationBean;
		} catch (Exception e) {
			LOGGER.error("Failed to register shiro security filter!");
			throw e;
		}
	}

	private ShiroFilterFactoryBean shiroSecurityFilter() {
		ShiroFilterFactoryBean shiroSecurityFilter = new ShiroFilterFactoryBean();
		shiroSecurityFilter.setLoginUrl(env.getProperty(PROPERTY_WEB_LOGIN_URL));
		shiroSecurityFilter.setUnauthorizedUrl(env.getProperty(PROPERTY_WEB_UNAUTHORIZED_URL));
		shiroSecurityFilter.setSecurityManager(context.getBean(BEAN_NAME_SECURITY_MANAGER,
				DefaultWebSecurityManager.class));
		shiroSecurityFilter.setFilters(buildFilters());
		try {
			shiroSecurityFilter.setFilterChainDefinitionMap(appChainDefinitionSection.getObject());
		} catch (Throwable e) {
			LOGGER.error("Failed to load application chain definition!");
		}
		if (shiroSecurityFilter.getFilterChainDefinitionMap().size() == 0) {
			shiroSecurityFilter.setFilterChainDefinitions(env.getProperty(PROPERTY_FILTER_CHAIN_DEFINITIONS));
		}
		return shiroSecurityFilter;
	}

	protected abstract Map<String, Filter> buildFilters();
}
