/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 * @date 2014-09-19
 */
public class ShipSkuExcelVo implements Serializable{
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 9043065487291983162L;
	/**商品编号*/
	private String skuId;
	/**尺码*/
	private String size;
	/**颜色*/
	private String color;
	/**商品名称*/
	private String productName;
	/**发货数量*/
	private int shipNum;
	/**拣货单号*/
	private String pickOrderId;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getShipNum() {
		return shipNum;
	}

	public void setShipNum(int shipNum) {
		this.shipNum = shipNum;
	}

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}
	
	
}
