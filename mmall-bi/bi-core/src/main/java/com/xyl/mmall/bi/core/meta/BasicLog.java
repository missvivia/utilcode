package com.xyl.mmall.bi.core.meta;

import java.io.Serializable;

import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.enums.OpAction;

/**
 * 日志通用信息.
 * 
 * @author wangfeng
 * 
 */
public class BasicLog implements Serializable {

	private static final long serialVersionUID = -8869523151360788620L;

	/** 动作发生时间. **/
	private long time = -1;

	/** 用户操作类型. **/
	private OpAction action;

	/** 具体页面/操作. **/
	private BIType type;

	/** 账号id（没有时为空）. **/
	private String accountId = "-1";

	/** app/web端. **/
	private ClientType clientType;

	/** 操作系统 **/
	private String deviceOs = "";

	/** 设备类型 **/
	private String deviceType = "";

	/** ip地址. */
	private String ip = "";

	/** 省份id. */
	private String provinceCode = "-1";

	/** action=page用户请求来源url;action=click请求url **/
	private String referer = "";

	public BasicLog() {
		super();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public OpAction getAction() {
		return action;
	}

	public void setAction(OpAction action) {
		this.action = action;
	}

	public BIType getType() {
		return type;
	}

	public void setType(BIType type) {
		this.type = type;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public String getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	@Override
	public String toString() {
		return "BasicLog [time=" + time + ", action=" + action + ", type=" + type + ", accountId=" + accountId
				+ ", clientType=" + clientType + ", deviceOs=" + deviceOs + ", deviceType=" + deviceType + ", ip=" + ip
				+ ", provinceCode=" + provinceCode + ", referer=" + referer + "]";
	}

}
