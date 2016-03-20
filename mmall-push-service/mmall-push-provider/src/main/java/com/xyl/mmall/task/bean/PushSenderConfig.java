package com.xyl.mmall.task.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netease.push.meta.AttachType;

/**
 * push 发送的配置文件
 * @author jiangww
 *
 */
public class PushSenderConfig {
	private long taskId;
	private String title;
	private String content;
	private String summary;
	private String alert;
	private String platform;
	private AttachType attachType = AttachType.ACCOUNT;
	private Map<String,Object> filter = new HashMap<String,Object>();
	private long ttl = 86400;
	private boolean offline = true;
	
	//@TODD 这边需要确认部分成功算成功失败
	private boolean sendSuccess = true;
	private List<String> errorArea = new ArrayList<String>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	public boolean isOffline() {
		return offline;
	}
	public void setOffline(boolean offline) {
		this.offline = offline;
	}
	public Map<String,Object> getFilter() {
		return filter;
	}
	public void setFilter(Map<String,Object> filter) {
		this.filter = filter;
	}
	public void addFilter(String key,Object value) {
		this.filter.put(key, value);
	}
	public AttachType getAttachType() {
		return attachType;
	}
	public void setAttachType(AttachType attachType) {
		this.attachType = attachType;
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public boolean isSendSuccess() {
		return sendSuccess;
	}
	public void setSendSuccess(boolean sendSuccess) {
		this.sendSuccess = sendSuccess;
	}
	public List<String> getErrorArea() {
		return errorArea;
	}
	public void setErrorArea(List<String> errorArea) {
		this.errorArea = errorArea;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
}
