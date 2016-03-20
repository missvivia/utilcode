package com.xyl.mmall.oms.dto;

/**
 * Po单报表Dto
 * 
 * @author hzzengdan
 * @date 2014-09-04
 */
public class PoOrderReportFormDTO {
	/** po单编号 */
	private String poOrderId;

	/** 上线时间 */
	private Long onlineTime;

	/** 下线时间 */
	private Long offlineTime;

	/** 订单数 */
	private int orderNumber;

	/** 有效订单数 */
	private int effectiveOrdersNumber;

	/** 取消订单数 */
	private int cancelOrdersNumber;

	/** 未拣货数量 */
	private int unPickNumber;

	/** 拣货中数量 */
	private int pickingNumber;

	/** 最后拣货时间 */
	private Long lastPickTime;

	/** 累计拣货数量 */
	private int cumulativePickNumber;

	/** 累计已发送的拣货数量 */
	private int cumulativeSendPickNumber;

	/** 发货中的商品数量 */
	private int shippingNumber;

	/** 发货车次 */
	private int shipTrips;

	/** 最后发货时间 */
	private Long lastShipTime;

	/** 累计发货数量 */
	private int cumulativeShipNumber;

	/** 最后到货时间 */
	private Long finalArrivalTime;

	/** 累计到货数量 */
	private int cumulativeArrivalNumber;

	/** 累计OQC数量 */
	private int cumulativeOQCNumber;

	/** 一退数量 */
	private int oneReturnNumber;

	/** 二退数量 */
	private int twoReturnNumber;

	/** 三退数量 */
	private int threeReturnNumber;

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}



	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getEffectiveOrdersNumber() {
		return effectiveOrdersNumber;
	}

	public void setEffectiveOrdersNumber(int effectiveOrdersNumber) {
		this.effectiveOrdersNumber = effectiveOrdersNumber;
	}

	public int getCancelOrdersNumber() {
		return cancelOrdersNumber;
	}

	public void setCancelOrdersNumber(int cancelOrdersNumber) {
		this.cancelOrdersNumber = cancelOrdersNumber;
	}

	public int getUnPickNumber() {
		return unPickNumber;
	}

	public void setUnPickNumber(int unPickNumber) {
		this.unPickNumber = unPickNumber;
	}

	public int getPickingNumber() {
		return pickingNumber;
	}

	public void setPickingNumber(int pickingNumber) {
		this.pickingNumber = pickingNumber;
	}


	public int getCumulativePickNumber() {
		return cumulativePickNumber;
	}

	public void setCumulativePickNumber(int cumulativePickNumber) {
		this.cumulativePickNumber = cumulativePickNumber;
	}

	public int getCumulativeSendPickNumber() {
		return cumulativeSendPickNumber;
	}

	public void setCumulativeSendPickNumber(int cumulativeSendPickNumber) {
		this.cumulativeSendPickNumber = cumulativeSendPickNumber;
	}

	public int getShippingNumber() {
		return shippingNumber;
	}

	public void setShippingNumber(int shippingNumber) {
		this.shippingNumber = shippingNumber;
	}

	public int getShipTrips() {
		return shipTrips;
	}

	public void setShipTrips(int shipTrips) {
		this.shipTrips = shipTrips;
	}

	public int getCumulativeShipNumber() {
		return cumulativeShipNumber;
	}

	public void setCumulativeShipNumber(int cumulativeShipNumber) {
		this.cumulativeShipNumber = cumulativeShipNumber;
	}

	public int getCumulativeArrivalNumber() {
		return cumulativeArrivalNumber;
	}

	public void setCumulativeArrivalNumber(int cumulativeArrivalNumber) {
		this.cumulativeArrivalNumber = cumulativeArrivalNumber;
	}

	public int getCumulativeOQCNumber() {
		return cumulativeOQCNumber;
	}

	public void setCumulativeOQCNumber(int cumulativeOQCNumber) {
		this.cumulativeOQCNumber = cumulativeOQCNumber;
	}

	public int getOneReturnNumber() {
		return oneReturnNumber;
	}

	public void setOneReturnNumber(int oneReturnNumber) {
		this.oneReturnNumber = oneReturnNumber;
	}

	public int getTwoReturnNumber() {
		return twoReturnNumber;
	}

	public void setTwoReturnNumber(int twoReturnNumber) {
		this.twoReturnNumber = twoReturnNumber;
	}

	public int getThreeReturnNumber() {
		return threeReturnNumber;
	}

	public void setThreeReturnNumber(int threeReturnNumber) {
		this.threeReturnNumber = threeReturnNumber;
	}

	public Long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Long getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Long offlineTime) {
		this.offlineTime = offlineTime;
	}

	public Long getLastPickTime() {
		return lastPickTime;
	}

	public void setLastPickTime(Long lastPickTime) {
		this.lastPickTime = lastPickTime;
	}

	public Long getLastShipTime() {
		return lastShipTime;
	}

	public void setLastShipTime(Long lastShipTime) {
		this.lastShipTime = lastShipTime;
	}

	public Long getFinalArrivalTime() {
		return finalArrivalTime;
	}

	public void setFinalArrivalTime(Long finalArrivalTime) {
		this.finalArrivalTime = finalArrivalTime;
	}
	
	
}
