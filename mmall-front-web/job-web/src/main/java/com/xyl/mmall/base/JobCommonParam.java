package com.xyl.mmall.base;

import java.io.Serializable;

/**
 * 常用的参数
 * 比如任务id，签名，等参数
 * @author hzzhaozhenzuo
 *
 */
public class JobCommonParam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//任务id
	private long id;

	private String signature;
	
	private long timestamp;
	
	private String nonce;
	
	private String uuid;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
