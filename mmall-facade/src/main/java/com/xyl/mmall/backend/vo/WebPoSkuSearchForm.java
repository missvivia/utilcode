package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.cms.vo.PagerConditionAO;

/**
 * @author hzzengdan
 *
 */
public class WebPoSkuSearchForm extends PagerConditionAO implements Serializable{
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 4693413955706715058L;
	/**拣货单编号*/
	private String pickOrderId;
	/**发货单编号*/
	private String shipOrderId;
	/**拣货状态*/
	private int pickStates;
	/**发货状态*/
	private int shipStates;
	/**拣货开始时间*/
	private String pickStartTime;
	/**拣货截止时间*/
	private String pickEndTime;
	/**发货开始时间*/
	private String shipStartTime;
	/**发货截止时间*/
	private String shipEndTime;
	
	private String poOrderId;

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public int getPickStates() {
		return pickStates;
	}

	public void setPickStates(int pickStates) {
		this.pickStates = pickStates;
	}

	public int getShipStates() {
		return shipStates;
	}

	public void setShipStates(int shipStates) {
		this.shipStates = shipStates;
	}

	public String getPickStartTime() {
		return pickStartTime;
	}

	public void setPickStartTime(String pickStartTime) {
		this.pickStartTime = pickStartTime;
	}

	public String getPickEndTime() {
		return pickEndTime;
	}

	public void setPickEndTime(String pickEndTime) {
		this.pickEndTime = pickEndTime;
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

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}
	
}