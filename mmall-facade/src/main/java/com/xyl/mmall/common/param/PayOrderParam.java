package com.xyl.mmall.common.param;


/**
 * 买家下单后提交支付
 * @author author:lhp
 *
 * @version date:2015年8月18日下午3:14:32
 */
public class PayOrderParam {
	
	private String version;//版本
	private String serial_id;//请求系列号
	private String start_time;//订单提交时间
	private String expire_time;//订单失效时间,2个小时
	private String customer_ip;//客户下单域名以及 IP 可空
	private String order_details;//订单明细
	private String total_amount;//订单总金额，实付
	private String type;//担保交易类
	private String buyer_id;//新云联账号 
	
	private String paymethod;//支付方式  可空
	private String org_code;//目标基金机构代码 可空
	private String currency_code;//交易币种 可空
	private String direct_flag;//是否直连 可空
	private String borrowing_marked;//资金来源借贷标示 可空
	private String coupon_flag;//红包标示 可空
	private String least_pay;//订单最少支付 可空
	private String coupon;//红包消费列表 可空
			
			
	private String return_url;//商户回调地址
	private String notice_url;//商户通知地址
	private String partner_id;//商户Id
	private String cashier_type;//收银台产品,01 微信 02 PC
	private String split_rule_code;//分账规则代码
	private String split_rule;//分账规则
	private String bonus;//红包金额，商城抵扣金额
	private String settle_amount;//订单结算金额
	private String token;//登录标示 可空
	private String remark;//扩展字段
	private String charset;//编码方式
	private String sign_type;//签名类型
	private String sign_msg;//签名值

	
	
	
	private String requestUrl;//请求url
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
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	public String getOrder_details() {
		return order_details;
	}
	public void setOrder_details(String order_details) {
		this.order_details = order_details;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getNotice_url() {
		return notice_url;
	}
	public void setNotice_url(String notice_url) {
		this.notice_url = notice_url;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}
	public String getCashier_type() {
		return cashier_type;
	}
	public void setCashier_type(String cashier_type) {
		this.cashier_type = cashier_type;
	}
	public String getSplit_rule_code() {
		return split_rule_code;
	}
	public void setSplit_rule_code(String split_rule_code) {
		this.split_rule_code = split_rule_code;
	}
	public String getSplit_rule() {
		return split_rule;
	}
	public void setSplit_rule(String split_rule) {
		this.split_rule = split_rule;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getSettle_amount() {
		return settle_amount;
	}
	public void setSettle_amount(String settle_amount) {
		this.settle_amount = settle_amount;
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
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getCustomer_ip() {
		return customer_ip;
	}
	public void setCustomer_ip(String customer_ip) {
		this.customer_ip = customer_ip;
	}
	public String getSign_msg() {
		return sign_msg;
	}
	public void setSign_msg(String sign_msg) {
		this.sign_msg = sign_msg;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getPaymethod() {
		return paymethod;
	}
	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getDirect_flag() {
		return direct_flag;
	}
	public void setDirect_flag(String direct_flag) {
		this.direct_flag = direct_flag;
	}
	public String getBorrowing_marked() {
		return borrowing_marked;
	}
	public void setBorrowing_marked(String borrowing_marked) {
		this.borrowing_marked = borrowing_marked;
	}
	public String getCoupon_flag() {
		return coupon_flag;
	}
	public void setCoupon_flag(String coupon_flag) {
		this.coupon_flag = coupon_flag;
	}
	public String getLeast_pay() {
		return least_pay;
	}
	public void setLeast_pay(String least_pay) {
		this.least_pay = least_pay;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
