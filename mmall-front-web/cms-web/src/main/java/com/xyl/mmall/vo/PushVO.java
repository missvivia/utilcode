/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.vo;

import java.io.Serializable;
import java.util.List;

/**
 * RedPacketVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public class PushVO  implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private Long pushTime;
	private String link;
	private List<String> areaId;
	private List<String> os;
	private String content;
	private String title;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getPushTime() {
		return pushTime;
	}
	public void setPushTime(Long pushTime) {
		this.pushTime = pushTime;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public List<String> getAreaId() {
		return areaId;
	}
	public void setAreaId(List<String> areaId) {
		this.areaId = areaId;
	}
	public List<String> getOs() {
		return os;
	}
	public void setOs(List<String> os) {
		this.os = os;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
