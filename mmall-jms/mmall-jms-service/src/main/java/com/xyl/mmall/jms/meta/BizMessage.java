package com.xyl.mmall.jms.meta;

import java.io.Serializable;
import java.util.Map;

public class BizMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	// 在没有明确传入content时，需要listener解析业务类型，并得到相应的发送内容
	protected long userId;

	protected Object bizUniqueKey;

	// 业务类型id
	protected int bizTypeId;
	
	//是否需要根据业务类型来获取内容
	protected boolean obtainContentByBiz;
	
	protected Map<String,Object> otherParamMap;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Object getBizUniqueKey() {
		return bizUniqueKey;
	}

	public void setBizUniqueKey(Object bizUniqueKey) {
		this.bizUniqueKey = bizUniqueKey;
	}

	public int getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(int bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public boolean isObtainContentByBiz() {
		return obtainContentByBiz;
	}

	public void setObtainContentByBiz(boolean obtainContentByBiz) {
		this.obtainContentByBiz = obtainContentByBiz;
	}

	public Map<String, Object> getOtherParamMap() {
		return otherParamMap;
	}

	public void setOtherParamMap(Map<String, Object> otherParamMap) {
		this.otherParamMap = otherParamMap;
	}
	
	public String toString() {
		return "userId:" + userId + ",bizeTypeId" + bizTypeId + ",bizUniqueKey:"
				+ bizUniqueKey;
	}

}
