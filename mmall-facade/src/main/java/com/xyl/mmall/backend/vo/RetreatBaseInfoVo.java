/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 *
 */
public class RetreatBaseInfoVo implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 6011051199187363012L;
	
	private String serialNO;
	
	private String returnStates;
	
	private String returnOrderId;
	
	private String poOrderId;
	
	private int returnNum;
	/**体积*/
	private double volume;
	
	private double weight;
	
	private String brandName;

	public String getSerialNO() {
		return serialNO;
	}

	public void setSerialNO(String serialNO) {
		this.serialNO = serialNO;
	}

	public String getReturnStates() {
		return returnStates;
	}

	public void setReturnStates(String returnStates) {
		this.returnStates = returnStates;
	}

	public String getReturnOrderId() {
		return returnOrderId;
	}

	public void setReturnOrderId(String returnOrderId) {
		this.returnOrderId = returnOrderId;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public int getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	
}
