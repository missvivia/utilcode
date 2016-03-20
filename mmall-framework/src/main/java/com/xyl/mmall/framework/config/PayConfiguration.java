package com.xyl.mmall.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 调用支付平台接口配置
 * @author author:lhp
 *
 * @version date:2015年8月6日下午4:26:29
 */
@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/pay.properties" })
public class PayConfiguration {
	
	@Value("${pay.version}")
	private String version;
	
	@Value("${pay.type}")
	private String type;
	
	@Value("${pay.return_url}")
	private String returnUrl;

	@Value("${pay.notice_url}")
	private String noticeUrl;
	
	@Value("${pay.pay_url}")
	private String payUrl;
	
	/**
	 * 1 UTF-8
	 */
	@Value("${pay.charset}")
	private String charset;
	
	/**
	 * 1 RSA 2 MD5
	 */
	@Value("${pay.sign_type}")
	private String signType;
	
	@Value("${pay.confirm_url}")
	private String confirmUrl;
	
	@Value("${pay.partner_id}")
	private String partnerId;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}


	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getConfirmUrl() {
		return confirmUrl;
	}

	public void setConfirmUrl(String confirmUrl) {
		this.confirmUrl = confirmUrl;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	
	

}
