package com.xyl.mmall.obj;

import java.io.Serializable;


/**
 * 易信调用获取token接口结果对象
 * @author hzzhaozhenzuo
 *
 */
public class ExResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String access_token;
	
	private String openid;
	
	private long expires_in;
	
	private String haveNewLeaveMessage;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getHaveNewLeaveMessage() {
		return haveNewLeaveMessage;
	}

	public void setHaveNewLeaveMessage(String haveNewLeaveMessage) {
		this.haveNewLeaveMessage = haveNewLeaveMessage;
	}
}
