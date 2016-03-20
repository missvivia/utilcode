package com.xyl.mmall.cart.clean.meta;

import java.io.Serializable;

/**
 * cart区域清除指针对象
 * 每条记录代表一组清除区域集，这组清除区域集由n个小区域组成，并且这组区域集只被一台任务服务器处理
 * @author hzzhaozhenzuo
 *
 */
public class CartCleanAreaPoint implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String distributeKey;
	
	private int point;
	
	private int proccessStatus;

	public String getDistributeKey() {
		return distributeKey;
	}

	public void setDistributeKey(String distributeKey) {
		this.distributeKey = distributeKey;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getProccessStatus() {
		return proccessStatus;
	}

	public void setProccessStatus(int proccessStatus) {
		this.proccessStatus = proccessStatus;
	}
}
