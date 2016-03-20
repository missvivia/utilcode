package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一级类
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileLastVersionVO implements Serializable{

	/**
	 * 
	 */

	private static final long serialVersionUID = -8197206063838883110L;
	//最新版本号
	private String	latestVersion;
	//说明
	private String	desc;
	//下载URL
	private String	 downloadURL;
	

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	public String getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	
	
}
