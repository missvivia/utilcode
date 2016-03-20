/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 *
 */
public class ComeCargoShipVo implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 6933894220287133321L;
	
	private String shipOrderId;
	
	private String poOrderId;
	
	private String shipTime;
	
	private String expectedArrivalTime;
	
	private String actualArrivalTime;
	
	private String courierCompanies;
	
	private String expressNO;
	
	private int shippedBoxNum;
	
	private int deliveryBoxNum;
	
	private int deliveryTotal;
	
	private int skuSpeciesNum;

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public String getExpectedArrivalTime() {
		return expectedArrivalTime;
	}

	public void setExpectedArrivalTime(String expectedArrivalTime) {
		this.expectedArrivalTime = expectedArrivalTime;
	}

	public String getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(String actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

	public String getCourierCompanies() {
		return courierCompanies;
	}

	public void setCourierCompanies(String courierCompanies) {
		this.courierCompanies = courierCompanies;
	}

	public String getExpressNO() {
		return expressNO;
	}

	public void setExpressNO(String expressNO) {
		this.expressNO = expressNO;
	}

	public int getShippedBoxNum() {
		return shippedBoxNum;
	}

	public void setShippedBoxNum(int shippedBoxNum) {
		this.shippedBoxNum = shippedBoxNum;
	}

	public int getDeliveryBoxNum() {
		return deliveryBoxNum;
	}

	public void setDeliveryBoxNum(int deliveryBoxNum) {
		this.deliveryBoxNum = deliveryBoxNum;
	}

	public int getDeliveryTotal() {
		return deliveryTotal;
	}

	public void setDeliveryTotal(int deliveryTotal) {
		this.deliveryTotal = deliveryTotal;
	}

	public int getSkuSpeciesNum() {
		return skuSpeciesNum;
	}

	public void setSkuSpeciesNum(int skuSpeciesNum) {
		this.skuSpeciesNum = skuSpeciesNum;
	}

}
