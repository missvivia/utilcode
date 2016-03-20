package com.xyl.mmall.oms.freight;

import com.xyl.mmall.oms.dto.Region;
import com.xyl.mmall.oms.enums.WMSExpressCompany;

/**
 * @author hzzengchengyuan
 *
 */
public class FreightCalcMeta {
	private WMSExpressCompany expressCompany;
	
	/**
	 * 始发地，为一级行政区
	 */
	private Region origin;

	/**
	 * 目的地，为三级行政区
	 */
	private Region dest;

	/**
	 * 重量，单位g
	 */
	private double weight;
	
	/**
	 * 订单金额，单位元
	 */
	private double orderAmount;
	
	private double codAmount;

	private boolean isCod;

	private boolean isReverse;

	private boolean isInsurance;
	
	public FreightCalcMeta() {}

	public FreightCalcMeta(WMSExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}
	
	public WMSExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(WMSExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	public Region getOrigin() {
		return origin;
	}

	public void setOrigin(Region origin) {
		if (origin == null) {
			throw new NullPointerException();
		}
		this.origin = origin;
	}

	/**
	 * 返回三级行政区目的地
	 * @return
	 */
	public Region getDest() {
		return dest;
	}

	public void setDest(Region dest) {
		if (dest == null) {
			throw new NullPointerException();
		}
		this.dest = dest;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getCodAmount() {
		return codAmount;
	}

	public void setCodAmount(double codAmount) {
		this.codAmount = codAmount;
	}

	public boolean isCod() {
		return isCod;
	}

	public void setCod(boolean isCod) {
		this.isCod = isCod;
	}

	public boolean isReverse() {
		return isReverse;
	}

	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isInsurance() {
		return isInsurance;
	}

	public void setInsurance(boolean isInsurance) {
		this.isInsurance = isInsurance;
	}

}
