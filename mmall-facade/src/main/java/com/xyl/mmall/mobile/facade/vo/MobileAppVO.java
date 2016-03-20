package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileAppVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -487700508495212338L;
	//icon image 连接
	private String iconImage;
	//名称
	private String appName;
	//下载URL
	private String downloadURL;
	//包名字
	private String packageName;
	//该App定义的URLschema，用于打开APP
	private String urlSchema;
	
	private String appDesc;
	public String getIconImage() {
		return iconImage;
	}
	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getUrlSchema() {
		return urlSchema;
	}
	public void setUrlSchema(String urlSchema) {
		this.urlSchema = urlSchema;
	}
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	
	
}
