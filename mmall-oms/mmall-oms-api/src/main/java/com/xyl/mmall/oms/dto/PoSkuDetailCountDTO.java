/**
 * Po查询详情时，sku的统计数量
 */
package com.xyl.mmall.oms.dto;

import java.io.Serializable;

import com.xyl.mmall.oms.enums.SupplyType;

/**
 * @author hzzengdan
 * @date 2014-09-15
 */
public class PoSkuDetailCountDTO implements Serializable {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = -9175389431961880213L;

	/**
	 * po单编号
	 */
	private String poOrderId;

	/**
	 * 档期开始时间
	 */
	private long commodityStartTime;

	/**
	 * 档期结束时间
	 */
	private long commodityEndTime;

	/**
	 * 销售总量
	 */
	private int totalSales;

	/**
	 * 未拣货数量
	 */
	private int unPickedAmount;

	/**
	 * 未拣货数量
	 */
	private int pickedAmount;

	/**
	 * 已到货数量
	 */
	private int arrivedAmount;

	/**
	 * 代理商自供货量
	 */
	private int selfStock;

	/**
	 * 品牌参与供货量
	 */
	private int backupStock;

	/**
	 * 总供货数量
	 */
	private int totalStock;
	
	private SupplyType supplyType;

	public int getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}

	public int getUnPickedAmount() {
		return unPickedAmount;
	}

	public void setUnPickedAmount(int unPickedAmount) {
		this.unPickedAmount = unPickedAmount;
	}

	public int getArrivedAmount() {
		return arrivedAmount;
	}

	public void setArrivedAmount(int arrivedAmount) {
		this.arrivedAmount = arrivedAmount;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public long getCommodityStartTime() {
		return commodityStartTime;
	}

	public void setCommodityStartTime(long commodityStartTime) {
		this.commodityStartTime = commodityStartTime;
	}

	public long getCommodityEndTime() {
		return commodityEndTime;
	}

	public void setCommodityEndTime(long commodityEndTime) {
		this.commodityEndTime = commodityEndTime;
	}

	public int getPickedAmount() {
		return pickedAmount;
	}

	public void setPickedAmount(int pickedAmount) {
		this.pickedAmount = pickedAmount;
	}

	public int getSelfStock() {
		return selfStock;
	}

	public void setSelfStock(int selfStock) {
		this.selfStock = selfStock;
	}

	public int getBackupStock() {
		return backupStock;
	}

	public void setBackupStock(int backupStock) {
		this.backupStock = backupStock;
	}

	public int getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}

}
