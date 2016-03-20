package com.xyl.mmall.common.param;

/**
 * 确认收货返回结果
 * @author author:lhp
 *
 * @version date:2015年8月20日上午9:46:33
 */
public class PayConfirmResult {

	/**
	 * 分账订单号
	 */
	private String order_no;
	/**
	 * 处理结果码
	 */
	private String result_code;
	/**
	 * 状态码
	 */
	private String state_code;
	/**
	 * 系统完成时间
	 */
	private String complete_time;
	
	/**
	 * 编码方式
	 */
	private String charset;
	
	/**
	 * 签名方式
	 */
	private String sign_type;
	
	/**
	 * 签名串
	 */
	private String sign_msg;

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
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

	public String getComplete_time() {
		return complete_time;
	}

	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
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
