/**
 *退供物流信息 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 *
 */
public class RetreatlogisticsInforVo implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 7678058817353400057L;
	
	private String shipAddress;
	
	private String deliveryAddress;
	
	private String courierCompanies;
	
	private String expressNO;

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
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

}
