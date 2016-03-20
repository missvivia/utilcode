package com.xyl.mmall.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/filedir.properties" })
public class FileDirConfiguration {

	/**
	 * 买家首页数据excel目录
	 */
	@Value("${file.indexData.dir}")
	private String indexDataFileDir;

	/**
	 * web 预览静态html目录
	 */
	@Value("${file.webPreHtml.dir}")
	private String webPreHtmlDir;
	
	/**
	 * web首页 静态html目录
	 */
	@Value("${file.webIndexHtml.dir}")
	private String webIndexHtmlDir;

	/**
	 * web 预览静态html目录
	 */
	@Value("${file.wapPreHtml.dir}")
	private String wapPreHtmlDir;
	
	/**
	 * wap首页 静态html目录
	 */
	@Value("${file.wapIndexHtml.dir}")
	private String wapIndexHtmlDir;
	
	/**
	 * app首页 静态html目录
	 */
	@Value("${file.appIndexHtml.dir}")
	private String appIndexHtmlDir;

	/**
	 * web 静态类目html目录
	 */
	@Value("${file.webCategroyHtml.dir}")
	private String webCategroyHtmlDir;
	
	/**
	 * wap 静态类目html目录
	 */
	@Value("${file.wapCategroyHtml.dir}")
	private String wapCategroyHtmlDir;
	
	/**
	 * app 静态类目html目录
	 */
	@Value("${file.appCategroyHtml.dir}")
	private String appCategroyHtmlDir;
	
	
	/**
	 * web 预览页面url
	 */
	@Value("${file.webPreHtml.url}")
	private String webPreHtmlUrl;
	
	/**
	 * wap 预览页面url
	 */
	@Value("${file.wapPreHtml.url}")
	private String wapPreHtmlUrl;
	
	/**
	 * app 预览页面url
	 */
	@Value("${file.appPreHtml.url}")
	private String appPreHtmlUrl;
	
	
	

	public String getWebPreHtmlUrl() {
		return webPreHtmlUrl;
	}

	public void setWebPreHtmlUrl(String webPreHtmlUrl) {
		this.webPreHtmlUrl = webPreHtmlUrl;
	}

	public String getWapPreHtmlUrl() {
		return wapPreHtmlUrl;
	}

	public void setWapPreHtmlUrl(String wapPreHtmlUrl) {
		this.wapPreHtmlUrl = wapPreHtmlUrl;
	}

	public String getAppPreHtmlUrl() {
		return appPreHtmlUrl;
	}

	public void setAppPreHtmlUrl(String appPreHtmlUrl) {
		this.appPreHtmlUrl = appPreHtmlUrl;
	}

	public String getIndexDataFileDir() {
		return indexDataFileDir;
	}

	public void setIndexDataFileDir(String indexDataFileDir) {
		this.indexDataFileDir = indexDataFileDir;
	}

	public String getWebPreHtmlDir() {
		return webPreHtmlDir;
	}

	public void setWebPreHtmlDir(String webPreHtmlDir) {
		this.webPreHtmlDir = webPreHtmlDir;
	}

	public String getWebCategroyHtmlDir() {
		return webCategroyHtmlDir;
	}

	public void setWebCategroyHtmlDir(String webCategroyHtmlDir) {
		this.webCategroyHtmlDir = webCategroyHtmlDir;
	}

	public String getWebIndexHtmlDir() {
		return webIndexHtmlDir;
	}

	public void setWebIndexHtmlDir(String webIndexHtmlDir) {
		this.webIndexHtmlDir = webIndexHtmlDir;
	}

	public String getWapPreHtmlDir() {
		return wapPreHtmlDir;
	}

	public void setWapPreHtmlDir(String wapPreHtmlDir) {
		this.wapPreHtmlDir = wapPreHtmlDir;
	}

	public String getWapIndexHtmlDir() {
		return wapIndexHtmlDir;
	}

	public void setWapIndexHtmlDir(String wapIndexHtmlDir) {
		this.wapIndexHtmlDir = wapIndexHtmlDir;
	}

	public String getAppIndexHtmlDir() {
		return appIndexHtmlDir;
	}

	public void setAppIndexHtmlDir(String appIndexHtmlDir) {
		this.appIndexHtmlDir = appIndexHtmlDir;
	}

	public String getWapCategroyHtmlDir() {
		return wapCategroyHtmlDir;
	}

	public void setWapCategroyHtmlDir(String wapCategroyHtmlDir) {
		this.wapCategroyHtmlDir = wapCategroyHtmlDir;
	}

	public String getAppCategroyHtmlDir() {
		return appCategroyHtmlDir;
	}

	public void setAppCategroyHtmlDir(String appCategroyHtmlDir) {
		this.appCategroyHtmlDir = appCategroyHtmlDir;
	}

	
}
