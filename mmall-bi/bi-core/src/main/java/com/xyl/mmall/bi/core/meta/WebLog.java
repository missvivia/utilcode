package com.xyl.mmall.bi.core.meta;

/**
 * web日志.
 * 
 * @author wangfeng
 * 
 */
public class WebLog extends BasicLog {

	private static final long serialVersionUID = -6220514346214308555L;

	private String browser;

	private String cookie;

	public WebLog() {
		super();
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
