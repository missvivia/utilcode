package com.xyl.mmall.cms.facade.param;

import java.io.Serializable;

/**
 * 到付审核查询、搜索的接口参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月11日 下午3:50:31
 *
 */
public class FrontCODAuditOperationParam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 121989770610600281L;

	private long logId;
	
	private long orderId;
	
	private long userId;
	
	private String extInfo = "";

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	
}
