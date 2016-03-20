package com.xyl.mmall.order.param;

import java.io.Serializable;

/**
 * 客服退款操作信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午5:00:05
 *
 */
public class ReturnOperationParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9163134737584600226L;

	public ReturnOperationParam() {
	}
	
	/**
	 * 退货记录Id
	 */
	private long retId;
	
	/**
	 * 用户Id
	 */
	private long userId;
	
	/**
	 * 退款说明
	 */
	private String extInfo = "";
	
	public long getRetId() {
		return retId;
	}

	public void setRetId(long retId) {
		this.retId = retId;
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
