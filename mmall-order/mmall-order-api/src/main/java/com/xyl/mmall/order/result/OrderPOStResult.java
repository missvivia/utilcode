package com.xyl.mmall.order.result;

/**
 * poId和订单的统计结果
 * 
 * @author dingmingliang
 * 
 */
public class OrderPOStResult {

	/**
	 * poId
	 */
	private long poId;

	/**
	 * 有效订单数
	 */
	private int validOrdCount;

	/**
	 * 取消订单数
	 */
	private int cancelOrdCount;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public int getValidOrdCount() {
		return validOrdCount;
	}

	public void setValidOrdCount(int validOrdCount) {
		this.validOrdCount = validOrdCount;
	}

	public int getCancelOrdCount() {
		return cancelOrdCount;
	}

	public void setCancelOrdCount(int cancelOrdCount) {
		this.cancelOrdCount = cancelOrdCount;
	}
}
