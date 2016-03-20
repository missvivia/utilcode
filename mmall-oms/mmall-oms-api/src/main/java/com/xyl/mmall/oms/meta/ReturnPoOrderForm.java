package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.PoReturnOrderState;

/**
 * 退回给商家的订单，在档期结束或某个时间点，由OmsReturnOrderForm按照poid分拣生成
 * 
 */
@AnnonOfClass(desc = "退供单表", tableName = "Mmall_Oms_PoReturnOrder")
public class ReturnPoOrderForm implements Serializable {
	
	private static final long serialVersionUID = 20140920L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long poReturnOrderId;

	@AnnonOfField(desc = "商家Id", policy = true)
	private long supplierId;

	@AnnonOfField(desc = "档期Id", policy = true)
	private long poOrderId;
	
	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "退供单当前状态.0:创建,1:已确认,2:已发货,3:已收货")
	private PoReturnOrderState state = PoReturnOrderState.NULL;

	@AnnonOfField(desc = "件数")
	private int count;

	@AnnonOfField(desc = "收货地址")
	private String receiverAddress = "";

	@AnnonOfField(desc = "装箱数")
	private int shipBoxQTY;

	@AnnonOfField(desc = "快递公司")
	private String expressCompany = "";

	@AnnonOfField(desc = "承运商联系电话")
	private String expressPhone = "";

	@AnnonOfField(desc = "体积")
	private long volume;

	@AnnonOfField(desc = "重量")
	private long weight;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "发货时间")
	private long shipTime;

	@AnnonOfField(desc = "实际出仓总数量")
	private int realCount;

	@AnnonOfField(desc = "更新时间")
	private long updateTime;

	public long getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(long poOrderId) {
		this.poOrderId = poOrderId;
	}

	/**
	 * @return the id
	 */
	public long getPoReturnOrderId() {
		return poReturnOrderId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setPoReturnOrderId(long id) {
		this.poReturnOrderId = id;
	}

	/**
	 * @return the supplierId
	 */
	public long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the warehouseId
	 */
	public long getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId
	 *            the warehouseId to set
	 */
	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the state
	 */
	public PoReturnOrderState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(PoReturnOrderState state) {
		this.state = state;
	}

	/**
	 * @return the shipBoxQTY
	 */
	public int getShipBoxQTY() {
		return shipBoxQTY;
	}

	/**
	 * @param shipBoxQTY
	 *            the shipBoxQTY to set
	 */
	public void setShipBoxQTY(int shipBoxQTY) {
		this.shipBoxQTY = shipBoxQTY;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the expressCompany
	 */
	public String getExpressCompany() {
		return expressCompany;
	}

	/**
	 * @param expressCompany
	 *            the expressCompany to set
	 */
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	/**
	 * @return the expressPhone
	 */
	public String getExpressPhone() {
		return expressPhone;
	}

	/**
	 * @param expressPhone
	 *            the expressPhone to set
	 */
	public void setExpressPhone(String expressPhone) {
		this.expressPhone = expressPhone;
	}

	/**
	 * @return the receiverAddress
	 */
	public String getReceiverAddress() {
		return receiverAddress;
	}

	/**
	 * @param receiverAddress
	 *            the receiverAddress to set
	 */
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	/**
	 * @return the volume
	 */
	public long getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(long volume) {
		this.volume = volume;
	}

	/**
	 * @return the weight
	 */
	public long getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(long weight) {
		this.weight = weight;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the shipTime
	 */
	public long getShipTime() {
		return shipTime;
	}

	/**
	 * @param shipTime
	 *            the shipTime to set
	 */
	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public int getRealCount() {
		return realCount;
	}

	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	/**
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
