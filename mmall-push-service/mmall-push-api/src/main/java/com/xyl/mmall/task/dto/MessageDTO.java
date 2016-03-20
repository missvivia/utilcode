package com.xyl.mmall.task.dto;

import java.io.Serializable;

import com.xyl.mmall.task.enums.PlatformType;

/**
 * 普通消息，子类可再扩展需要的字段
 * 包含了具体消息行为
 * @author hzzhaozhenzuo
 *
 */
public class MessageDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//消息内容
	private String content;
	
	//消息过期时间，单位是秒
	private Long ttl;
	
	//平台
	private PlatformType platform;
	
	private String title;
	
	private String alert;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTtl() {
		return ttl;
	}

	public void setTtl(Long ttl) {
		this.ttl = ttl;
	}

	public PlatformType getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformType platform) {
		this.platform = platform;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}
	
}
