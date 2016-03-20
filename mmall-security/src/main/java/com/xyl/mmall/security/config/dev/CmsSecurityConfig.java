/**
 * 
 */
package com.xyl.mmall.security.config.dev;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.xyl.mmall.security.config.BaseSecurityConfig;
import com.xyl.mmall.security.filter.AuthenticatingExceptionFilter;
import com.xyl.mmall.security.filter.MmallCMSAuthenticatingTestFilter;

/**
 * @author lihui
 *
 */
@Configuration
@Profile("dev")
@PropertySource(value = "classpath:/config/dev/cmsSecurity.properties")
@ConditionalOnClass(name = "com.xyl.mmall.service.CmsAuthUserService")
public class CmsSecurityConfig extends BaseSecurityConfig
{
    
    /**
     * Filter name of URS authentication
     */
    private static final String FILTER_NAME_AUTHC_URS = "authcURS";

    private static final String ASYNC_REQUEST_PATH = "asyncRequestPath";
    
    /**
     * (non-Javadoc)
     * 
     * @see com.xyl.mmall.security.config.BaseSecurityConfig#buildFilters()
     */
    @Override
    protected Map<String, Filter> buildFilters()
    {
        Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
        filterMap.put(FILTER_NAME_AUTHC_URS, authWebFilter());
        return filterMap;
    }
    
    Filter authWebFilter()
    {
        // NEAuthenticatingInternalTestingFilter filter = new
        // NEAuthenticatingInternalTestingFilter();
        // filter.setAccountValidator(context.getBean(BEAN_NAME_ACCOUNT_VALIDATOR,
        // AccountValidatorImpl.class));
        // filter.setDomain(env.getProperty(PROPERTY_WEB_COOKIE_DOMAIN));
        // filter.setTestUserName(env.getProperty(PROPERTY_TEST_USER_NAME));
        // filter.setTestMode(env.getProperty(PROPERTY_TEST_MODE));
        // filter.setCookieTimeOut(43200L);
        // return filter;
        MmallCMSAuthenticatingTestFilter filter = new MmallCMSAuthenticatingTestFilter();
        filter.setAsyncRequestPath(env.getProperty(ASYNC_REQUEST_PATH));
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
    FilterRegistrationBean authenticatingExceptionFilter()
    {
        FilterRegistrationBean authenticatingExceptionFilter = new FilterRegistrationBean();
        AuthenticatingExceptionFilter filter = new AuthenticatingExceptionFilter();
        authenticatingExceptionFilter.setFilter(filter);
        authenticatingExceptionFilter.setOrder(-1);
        return authenticatingExceptionFilter;
    }
}
