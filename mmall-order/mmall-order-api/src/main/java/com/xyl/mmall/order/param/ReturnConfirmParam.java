package com.xyl.mmall.order.param;

import java.io.Serializable;

/**
 * 仓库收到退货的信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午4:58:05
 *
 */
public class ReturnConfirmParam implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7855158135706607222L;

	public ReturnConfirmParam() {
	}
	
	//(desc = "仓库实际收到的退货数量")
	private int confirmCount;

	//(desc = "仓库退货备注信息")
	private String extInfo = "";

	public int getConfirmCount() {
		return confirmCount;
	}

	public void setConfirmCount(int confirmCount) {
		this.confirmCount = confirmCount;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String reason) {
		this.extInfo = reason;
	}
}
