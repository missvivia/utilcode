package com.xyl.mmall.jms.meta;

import com.xyl.mmall.jms.enums.SmsType;

public class SmsMessage extends BizMessage {

	private static final long serialVersionUID = 1L;

	private String mobile;

	private String content;

	private int level;

	private String group;
	
	private SmsType smsType;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public SmsType getSmsType() {
		return smsType;
	}

	public void setSmsType(SmsType smsType) {
		this.smsType = smsType;
	}

}
