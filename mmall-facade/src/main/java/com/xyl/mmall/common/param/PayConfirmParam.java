package com.xyl.mmall.common.param;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年8月21日上午9:50:41
 */
public class PayConfirmParam {
	
	/**
	 * 版本
	 */
	private String version;
	
	/**
	 * 请求系列号
	 */
	private String serial_id;
	
	/**
	 * 分账订单号
	 */
	private String order_no;
	
	/**
	 * 商户Id即买家
	 */
	private String partner_id;
	
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
	
	/**
	 * 请求url
	 */
	private String requestUrl;

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSerial_id() {
		return serial_id;
	}

	public void setSerial_id(String serial_id) {
		this.serial_id = serial_id;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	
	
	

}
