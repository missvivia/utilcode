package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * 更新订单明细的包裹信息Param
 * 
 * @author dingmingliang
 * 
 */
public class OrderServiceUpdatePackageIdParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20141201L;

	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 快递号
	 */
	private String mailNO = "";

	/**
	 * 快递公司-ERP回传
	 */
	private String expressCompany = "";

	/**
	 * 
	 */
	private long skuId;

	/**
	 * 仓库Id
	 */
	private long warehouseId;

	/**
	 * 仓库名称
	 */
	private String warehouseName = "";

	/**
	 * 是否是超时未配送的情况(默认不是)
	 */
	private boolean isOutTime = false;

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public boolean isOutTime() {
		return isOutTime;
	}

	public void setOutTime(boolean isOutTime) {
		this.isOutTime = isOutTime;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}