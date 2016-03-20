package com.xyl.mmall.oms.dto;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class OrderReportDTO implements Serializable {

	private static final long serialVersionUID = 6587044947616968447L;
	
	private long time;
	
	private int totalOrderNumber;
	 
	private int collectiveOrderNumber;
	
	private int cancelOrderNumber;
	
	/**
	 * 例如EMS产生订单，EMS占比
	 */
	private List<JSONObject> expressOrder;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getTotalOrderNumber() {
		return totalOrderNumber;
	}

	public void setTotalOrderNumber(int totalOrderNumber) {
		this.totalOrderNumber = totalOrderNumber;
	}

	public int getCollectiveOrderNumber() {
		return collectiveOrderNumber;
	}

	public void setCollectiveOrderNumber(int collectiveOrderNumber) {
		this.collectiveOrderNumber = collectiveOrderNumber;
	}

	public int getCancelOrderNumber() {
		return cancelOrderNumber;
	}

	public void setCancelOrderNumber(int cancelOrderNumber) {
		this.cancelOrderNumber = cancelOrderNumber;
	}

	public List<JSONObject> getExpressOrder() {
		return expressOrder;
	}

	public void setExpressOrder(List<JSONObject> expressOrder) {
		this.expressOrder = expressOrder;
	}
	
}
