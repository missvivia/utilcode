/**
 * 
 */
package com.xyl.mmall.security.config.feature1;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.xyl.mmall.security.config.BaseSecurityConfig;
import com.xyl.mmall.security.filter.MobileAuthenticatingFilter;
import com.xyl.mmall.security.filter.MobileOptionalAuthcFilter;
import com.xyl.mmall.security.service.MobileAuthcService;
import com.xyl.mmall.security.service.MobileAuthcServiceImpl;
import com.xyl.mmall.security.service.MobileTokenService;
import com.xyl.mmall.security.service.URSAuthUserInfoService;
import com.xyl.mmall.security.service.URSAuthUserInfoServiceImpl;
import com.xyl.mmall.security.thirdparty.TencentApp;
import com.xyl.mmall.security.thirdparty.ThirdPartyApp;
import com.xyl.mmall.security.thirdparty.WeiboApp;
import com.xyl.mmall.security.thirdparty.WeixinApp;
import com.xyl.mmall.security.thirdparty.YixinApp;

/**
 * @author lihui
 *
 */
@Configuration
@Profile("feature1")
@PropertySource(value = "classpath:/config/feature1/mobileSecurity.properties")
@ConditionalOnClass(name = "com.xyl.mmall.service.MobileAuthUserService")
public class MobileSecurityConfig extends BaseSecurityConfig {
	
	/**
	 * 
	 */
	private static final String EXCHANGE_URS_TOKEN_URL = "exchangeURSTokenUrl";
	
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
	private static final String APP_WEIXIN_USER_INFO_URL = "app.weixin.userInfoUrl";
	
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
	private static final String LOGOUT_FOR_MOB_URL = "logoutForMobUrl";

	/**
	 * 
	 */
	private static final String LOGIN_FOR_MOB_URL = "loginForMobUrl";

	/**
	 * 
	 */
	private static final String INIT_APP_URL = "initAppUrl";

	/**
	 * 
	 */
	private static final String CHECK_URS_TOKEN_URL = "checkURSTokenUrl";

	/**
	 * 
	 */
	private static final String CHECK_OAUTH_TOKEN_URL = "checkOAuthTokenUrl";

	/**
	 * 
	 */
	private static final String BEAN_NAME_MOBILE_TOKEN_SERVICE = "mobileTokenService";

	/**
	 * @return
	 */
	@Override
	protected Map<String, Filter> buildFilters() {
		Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
		filterMap.put("authcMobileOptional", authMobileOptionalFilter());
		filterMap.put("authcMobile", authMobileFilter());
		return filterMap;
	}

	Filter authMobileFilter() {
		MobileAuthenticatingFilter filter = new MobileAuthenticatingFilter();
		filter.setMobileTokenService(context.getBean(BEAN_NAME_MOBILE_TOKEN_SERVICE, MobileTokenService.class));
		return filter;
	}

	Filter authMobileOptionalFilter() {
		MobileOptionalAuthcFilter filter = new MobileOptionalAuthcFilter();
		filter.setMobileTokenService(context.getBean(BEAN_NAME_MOBILE_TOKEN_SERVICE, MobileTokenService.class));
		return filter;
	}

	@Bean(name = "mobileAuthcService")
	MobileAuthcService mobileAuthcService() {
		MobileAuthcServiceImpl mobileAuthcServiceImpl = new MobileAuthcServiceImpl();
		mobileAuthcServiceImpl.setCheckOAuthTokenUrl(env.getProperty(CHECK_OAUTH_TOKEN_URL));
		mobileAuthcServiceImpl.setCheckURSTokenUrl(env.getProperty(CHECK_URS_TOKEN_URL));
		mobileAuthcServiceImpl.setInitAppUrl(env.getProperty(INIT_APP_URL));
		mobileAuthcServiceImpl.setLoginForMobUrl(env.getProperty(LOGIN_FOR_MOB_URL));
		mobileAuthcServiceImpl.setLogoutForMobUrl(env.getProperty(LOGOUT_FOR_MOB_URL));
		mobileAuthcServiceImpl.setContext(this.context);
		mobileAuthcServiceImpl.setProduct(env.getProperty(PRODUCT_CODE));
		mobileAuthcServiceImpl.setExchangeURSTokenUrl(env.getProperty(EXCHANGE_URS_TOKEN_URL));
		return mobileAuthcServiceImpl;
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

	@Bean(name = "wxApp")
	ThirdPartyApp weixinApp() {
		WeixinApp weixinApp = new WeixinApp();
		weixinApp.setUserInfoUrl(env.getProperty(APP_WEIXIN_USER_INFO_URL));
		return weixinApp;
	}
}
