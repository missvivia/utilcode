/**
 * 
 */
package com.xyl.mmall.security.config.performance;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.netease.print.security.authc.URSOAuthService;
import com.netease.print.security.authc.URSOAuthServiceImpl;
import com.xyl.mmall.security.config.BaseSecurityConfig;
import com.xyl.mmall.security.filter.NEAuthenticatingMainSiteFilter;
import com.xyl.mmall.security.service.AccountValidatorOauthImpl;
import com.xyl.mmall.security.service.MainSiteAuthcService;
import com.xyl.mmall.security.service.MainSiteAuthcServiceImpl;
import com.xyl.mmall.security.service.URSAuthUserInfoService;
import com.xyl.mmall.security.service.URSAuthUserInfoServiceImpl;
import com.xyl.mmall.security.thirdparty.TencentApp;
import com.xyl.mmall.security.thirdparty.ThirdPartyApp;
import com.xyl.mmall.security.thirdparty.WeiboApp;
import com.xyl.mmall.security.thirdparty.YixinApp;

/**
 * @author lihui
 *
 */
@Configuration
@Profile("performance")
@PropertySource(value = "classpath:/config/performance/wapSecurity.properties")
@ConditionalOnClass(name = "com.xyl.mmall.service.WapAuthUserService")
public class WapSecurityConfig extends BaseSecurityConfig {

	/**
	 * 
	 */
	private static final String GET_URS_OTHER_INFO_URL = "getURSOtherInfoUrl";

	/**
	 * 
	 */
	private static final String PRODUCT_CODE = "productCode";
	
	/**
	 * 
	 */
	private static final String APP_SINA_USER_INFO_URL = "app.sina.userInfoUrl";

	/**
	 * 
	 */
	private static final String APP_YIXIN_USER_INFO_URL = "app.yixin.userInfoUrl";

	/**
	 * 
	 */
	private static final String APP_TENCENT_USER_INFO_URL = "app.tencent.userInfoUrl";

	/**
	 * 
	 */
	private static final String APP_TENCENT_CLIENT_ID = "app.tencent.clientId";

	/**
	 * 
	 */
	private static final String OAUTH_ACCESS_TOKEN_URL = "oauthAccessTokenUrl";

	/**
	 * 
	 */
	private static final String MOBILE_PATH_PATTERN = "mobile.pathPattern";

	/**
	 * 
	 */
	private static final String MOBILE_LOGIN_PATH = "mobile.loginPath";

	/**
	 * 
	 */
	private static final String BEAN_NAME_ACCOUNT_VALIDATOR_OAUTH = "accountValidatorOauth";

	/**
	 * 
	 */
	private static final String FILTER_AUTHC_URS = "authcURS";

	/**
	 * Add additional filters for customized authentication.
	 * 
	 * @see com.xyl.mmall.security.config.BaseSecurityConfig#buildFilters()
	 */
	protected Map<String, Filter> buildFilters() {
		Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
		filterMap.put(FILTER_AUTHC_URS, authWebFilter());
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
		NEAuthenticatingMainSiteFilter filter = new NEAuthenticatingMainSiteFilter();
		filter.setAccountValidator(context.getBean(BEAN_NAME_ACCOUNT_VALIDATOR_OAUTH, AccountValidatorOauthImpl.class));
		filter.setDomain(env.getProperty(PROPERTY_WEB_COOKIE_DOMAIN));
		filter.setMobileLoginPath(env.getProperty(MOBILE_LOGIN_PATH));
		filter.setMobilePathPattern(env.getProperty(MOBILE_PATH_PATTERN));
		filter.setCookieTimeOut(43200L);
		return filter;
	}

	@Bean(name = "ursOAuthService")
	URSOAuthService ursOAuthService() {
		URSOAuthServiceImpl ursOAuthService = new URSOAuthServiceImpl();
		ursOAuthService.setAccountValidator(context.getBean(BEAN_NAME_ACCOUNT_VALIDATOR_OAUTH,
				AccountValidatorOauthImpl.class));
		return ursOAuthService;
	}

	@Bean(name = "wapAuthcService")
	MainSiteAuthcService wapAuthcService() {
		MainSiteAuthcServiceImpl wapAuthcService = new MainSiteAuthcServiceImpl();
		wapAuthcService.setRetrieveOauthAccessTokenUrl(env.getProperty(OAUTH_ACCESS_TOKEN_URL));
		wapAuthcService.setContext(this.context);
		return wapAuthcService;
	}

	@Bean(name = "ursAuthUserInfoService")
	URSAuthUserInfoService ursAuthUserInfoService() {
		URSAuthUserInfoServiceImpl ursAuthUserInfoService = new URSAuthUserInfoServiceImpl();
		ursAuthUserInfoService.setGetURSOtherInfoUrl(env.getProperty(GET_URS_OTHER_INFO_URL));
		ursAuthUserInfoService.setProduct(env.getProperty(PRODUCT_CODE));
		return ursAuthUserInfoService;
	}
	
	@Bean(name = "tencentApp")
	ThirdPartyApp tencentApp() {
		TencentApp tencentApp = new TencentApp();
		tencentApp.setClientId(env.getProperty(APP_TENCENT_CLIENT_ID));
		tencentApp.setUserInfoUrl(env.getProperty(APP_TENCENT_USER_INFO_URL));
		return tencentApp;
	}

	@Bean(name = "yixinApp")
	ThirdPartyApp yixinApp() {
		YixinApp yixinApp = new YixinApp();
		yixinApp.setUserInfoUrl(env.getProperty(APP_YIXIN_USER_INFO_URL));
		return yixinApp;
	}

	@Bean(name = "sinaApp")
	ThirdPartyApp weiboApp() {
		WeiboApp weiboApp = new WeiboApp();
		weiboApp.setUserInfoUrl(env.getProperty(APP_SINA_USER_INFO_URL));
		return weiboApp;
	}

}
