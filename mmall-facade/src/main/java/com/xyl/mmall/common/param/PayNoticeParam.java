package com.xyl.mmall.common.param;

import java.io.Serializable;

public class PayNoticeParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7690160848124490820L;

	/**
	 * 商户订单号
	 */
	private String o_order_no;
	
	/**
	 * 处理结果码
	 */
	private String result_code;
	
	/**
	 * 状态码
	 */
	private String state_code;
	
	/**
	 * 商户订单金额
	 */
	private String order_amount;

	/**
	 * 实际支付金额
	 */
	private String pay_amount;
	
	/**
	 * 收单时间
	 */
	private String acquiring_time;
	
	/**
	 * 处理完成时间
	 */
	private String complete_time;
	
	/**
	 * 支付流水号
	 */
	private String order_no;
	
	/**
	 * 商户Id
	 */
	private String partner_id;
	
	/**
	 * 扩展字段
	 */
	private String remark;
	
	/**
	 * 编码方式
	 */
	private String charset;
	
	/**
	 * 签名类型
	 */
	private String sign_type;
	
	
	/**
	 * 签名字符串
	 */
	private String sign_msg;


	public String getO_order_no() {
		return o_order_no;
	}


	public void setO_order_no(String o_order_no) {
		this.o_order_no = o_order_no;
	}


	public String getResult_code() {
		return result_code;
	}


	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}


	public String getState_code() {
		return state_code;
	}


	public void setState_code(String state_code) {
		this.state_code = state_code;
	}


	public String getOrder_amount() {
		return order_amount;
	}


	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}


	public String getPay_amount() {
		return pay_amount;
	}


	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}


	public String getAcquiring_time() {
		return acquiring_time;
	}


	public void setAcquiring_time(String acquiring_time) {
		this.acquiring_time = acquiring_time;
	}


	public String getComplete_time() {
		return complete_time;
	}


	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
	}


	public String getOrder_no() {
		return order_no;
	}


	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}


	public String getPartner_id() {
		return partner_id;
	}


	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getCharset() {
		return charset;
	}


	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getSign_type() {
		return sign_type;
	}


	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}


	public String getSign_msg() {
		return sign_msg;
	}


	public void setSign_msg(String sign_msg) {
		this.sign_msg = sign_msg;
	}
	

	
}
