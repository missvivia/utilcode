package com.xyl.mmall.mainsite.vo.order;

/**
 * 订单确认页-订单支付方式<br>
 * 0:网易宝<br>
 * 1:货到付款
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormPayMethod1VO {

	/**
	 * id
	 */
	private int value;

	/**
	 * 中文描述
	 */
	private String desc;

	/**
	 * 是否选中
	 */
	private boolean isSelected = false;

	/**
	 * 当前支付方式是否有效
	 */
	private boolean isValid = true;

	/**
	 * 支付方式无效时的文案
	 */
	private String invalidMess = "";

	public boolean isValid() {
		return isValid;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getInvalidMess() {
		return invalidMess;
	}

	public void setInvalidMess(String invalidMess) {
		this.invalidMess = invalidMess;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
