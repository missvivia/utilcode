/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 * 
 */
public class PoStatisticListVo implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 530115072600393545L;

	/** 商品编号 */
	private String skuId;
	
	private String barcode;
	
	private String productName;



	/** 商品数量 */
	private int skuNum;

	/** 拣货单编号 */
	private String pickOrderId;

	/** 发货单编号 */
	private String shipOrderId;

	/** 拣货时间 */
	private Long pickTime;

	/** 发货时间 */
	private Long shipTime;

	/** 拣货状态 */
	private int pickStates;

	/** 发货状态 */
	private int shipStates;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public Long getPickTime() {
		return pickTime;
	}

	public void setPickTime(Long pickTime) {
		this.pickTime = pickTime;
	}

	public Long getShipTime() {
		return shipTime;
	}

	public void setShipTime(Long shipTime) {
		this.shipTime = shipTime;
	}

	public int getPickStates() {
		return pickStates;
	}

	public void setPickStates(int pickStates) {
		this.pickStates = pickStates;
	}

	public int getShipStates() {
		return shipStates;
	}

	public void setShipStates(int shipStates) {
		this.shipStates = shipStates;
	}
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
