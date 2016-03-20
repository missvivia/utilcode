/**
 * 
 */
package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;

/**
 * @author hzzengdan
 * 
 */
public class ShipSkuDTO extends ShipSkuItemForm {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 2614255033328862530L;

	
	public ShipSkuDTO() {}
	
	/**
	 * 构造函数
	 */
	public ShipSkuDTO(ShipSkuItemForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public ShipSkuDTO(PickSkuItemForm pickSku) {
		ReflectUtil.convertObj(this, pickSku, false);
	}

	private PickMoldType pickMoldType;
	
	private PickStateType pickStates;

	private String pickOrderId;

	private Long pickTime;
	
	public PickMoldType getPickMoldType() {
		return pickMoldType;
	}

	public void setPickMoldType(PickMoldType pickMoldType) {
		this.pickMoldType = pickMoldType;
	}

	public PickStateType getPickStates() {
		return pickStates;
	}

	public void setPickStates(PickStateType pickStates) {
		this.pickStates = pickStates;
	}

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}

	public Long getPickTime() {
		return pickTime;
	}

	public void setPickTime(Long pickTime) {
		this.pickTime = pickTime;
	}
	
	
}
