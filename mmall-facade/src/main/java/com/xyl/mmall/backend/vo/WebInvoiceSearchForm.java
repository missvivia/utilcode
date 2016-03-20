package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.oms.enums.JITFlagType;

/**
 * 拣货单查询表单对象
 * 
 * @author zzj(hzzhangzhoujie@corp.netease.com)
 * 
 */
public class WebInvoiceSearchForm implements Serializable {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1631386179457164033L;

	/**
	 * po单编号
	 */
	private String poOrderId;

	/**
	 * 发货单号
	 */
	private String shipOrderId;

	/**
	 * 送货时间（起始）
	 */
	private String shipStartTime;

	/**
	 * 送货时间（结束）
	 */
	private String shipEndTime;

	/**
	 * 出仓状态（1：等待出仓，2:已出仓）
	 */
	private int shipStatus;

	/**
	 * 预计到货时间（起始）
	 */
	private String expectedArrivalTimeOfStart;

	/**
	 * 预计到货时间（结束）
	 */
	private String expectedArrivalTimeOfEnd;

	/**
	 * 实际到货时间（起始）
	 */
	private String actualArrivalTimeOfStart;

	/**
	 * 实际到货时间（结束）
	 */
	private String actualArrivalTimeOfEnd;
	
	/**
	 * JIT模式
	 */
	private JITFlagType jitFlagType;
	
	private int limit;
	
	private int offset;

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public int getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
	}

	public String getShipStartTime() {
		return shipStartTime;
	}

	public void setShipStartTime(String shipStartTime) {
		this.shipStartTime = shipStartTime;
	}

	public String getShipEndTime() {
		return shipEndTime;
	}

	public void setShipEndTime(String shipEndTime) {
		this.shipEndTime = shipEndTime;
	}

	public String getExpectedArrivalTimeOfStart() {
		return expectedArrivalTimeOfStart;
	}

	public void setExpectedArrivalTimeOfStart(String expectedArrivalTimeOfStart) {
		this.expectedArrivalTimeOfStart = expectedArrivalTimeOfStart;
	}

	public String getExpectedArrivalTimeOfEnd() {
		return expectedArrivalTimeOfEnd;
	}

	public void setExpectedArrivalTimeOfEnd(String expectedArrivalTimeOfEnd) {
		this.expectedArrivalTimeOfEnd = expectedArrivalTimeOfEnd;
	}

	public String getActualArrivalTimeOfStart() {
		return actualArrivalTimeOfStart;
	}

	public void setActualArrivalTimeOfStart(String actualArrivalTimeOfStart) {
		this.actualArrivalTimeOfStart = actualArrivalTimeOfStart;
	}

	public String getActualArrivalTimeOfEnd() {
		return actualArrivalTimeOfEnd;
	}

	public void setActualArrivalTimeOfEnd(String actualArrivalTimeOfEnd) {
		this.actualArrivalTimeOfEnd = actualArrivalTimeOfEnd;
	}

	public JITFlagType getJitFlagType() {
		return jitFlagType;
	}

	public void setJitFlagType(JITFlagType jitFlagType) {
		this.jitFlagType = jitFlagType;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	

}
