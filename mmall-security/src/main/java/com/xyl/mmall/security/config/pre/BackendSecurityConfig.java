/**
 * 
 */
package com.xyl.mmall.security.config.pre;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.netease.print.security.authc.AccountValidatorImpl;
import com.xyl.mmall.security.config.BaseSecurityConfig;
import com.xyl.mmall.security.filter.AuthenticatingExceptionFilter;
import com.xyl.mmall.security.filter.NEAuthenticatingNoReloginFilter;

/**
 * @author lihui
 *
 */
@Configuration
@Profile("pre")
@PropertySource(value = "classpath:/config/pre/backendSecurity.properties")
@ConditionalOnClass(name = "com.xyl.mmall.service.BackendAuthUserService")
public class BackendSecurityConfig extends BaseSecurityConfig {

	/**
	 * Filter name of URS authentication
	 */
	private static final String FILTER_NAME_AUTHC_URS = "authcURS";

	/**
	 * Add additional filters for customized authentication.
	 * 
	 * @see com.xyl.mmall.security.config.BaseSecurityConfig#buildFilters()
	 */
	protected Map<String, Filter> buildFilters() {
		Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
		filterMap.put(FILTER_NAME_AUTHC_URS, authWebFilter());
		return filterMap;
	}

	/**
	 * Define customized web authentication filter. Do not declare it with
	 * {@link Bean} annotation to avoid auto-load as {@link Filter} by spring
	 * boot.
	 * 
	 * @return
	 */
	Filter authWebFilter() {
		NEAuthenticatingNoReloginFilter filter = new NEAuthenticatingNoReloginFilter();
		filter.setAccountValidator(context.getBean(BEAN_NAME_ACCOUNT_VALIDATOR, AccountValidatorImpl.class));
		filter.setDomain(env.getProperty(PROPERTY_WEB_COOKIE_DOMAIN));
		filter.setCookieTimeOut(43200L);
		return filter;
	}

	/**
	 * 用户认证授权异常过滤器配置。
	 * 
	 * @return FilterRegistrationBean
	 */
	@Bean
	FilterRegistrationBean authenticatingExceptionFilter() {
		FilterRegistrationBean authenticatingExceptionFilter = new FilterRegistrationBean();
		AuthenticatingExceptionFilter filter = new AuthenticatingExceptionFilter();
		authenticatingExceptionFilter.setFilter(filter);
		authenticatingExceptionFilter.setOrder(-1);
		return authenticatingExceptionFilter;
	}

}
