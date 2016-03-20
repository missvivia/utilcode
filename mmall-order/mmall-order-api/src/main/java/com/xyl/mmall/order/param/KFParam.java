package com.xyl.mmall.order.param;

import java.io.Serializable;

/**
 * 客服退款操作信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午5:00:05
 *
 */
public class KFParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9163134737584600226L;

	public KFParam() {
	}
	
	/**
	 * 客服Id
	 */
	private long kfId;
	
	/**
	 * 客服Name
	 */
	private String kfName;

	public long getKfId() {
		return kfId;
	}

	public void setKfId(long kfId) {
		this.kfId = kfId;
	}

	public String getKfName() {
		return kfName;
	}

	public void setKfName(String kfName) {
		this.kfName = kfName;
	}
	
}
