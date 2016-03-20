package com.xyl.mmall.task.enums;

public class PushMessageType {
	/**
	 * 立即发送push 预订的 类型 1 = "订单发货"; 2 = "退货成功"; 3 = "退货拒绝"; 4= "活动红包"; 5=
	 * "新的优惠券"; 6 = 货到付款订单被审核不通过; 7 = 订单倒数5分钟 8 = 购物车倒数 5分钟
	 * 
	 * @return
	 */
	//订单发贷
	public final static int send_order = 1;
	
	//退贷成功
	public final static int retruns_success = 2;
	
	//退贷拒绝
	public final static int retruns_fail = 3;
	
	//活动红包
	public final static int give_gift = 4;
	
	//新的优惠卷
	public final static int give_coupon = 5;
	
	//货到付款订单被审核不通过
	public final static int cod_fail = 6;
	
	//订单倒数5分钟
	public final static int order_timeout = 7;
	
	//购物车倒数 5分钟
	public final static int cart_timeout = 8;
	
	//订单取消,即用户付款后，用户取消订单
	public final static int order_cancel_after_pay = 9;
	
	//审核拒绝
	public final static int cod_reject = 10;
	
	//订包裹取消
	public final static int package_cancel = 11;
	
	//订包裹取消
	public final static int tell_user_po = 12;

	//订单红包分享
	public final static int share_gift = 13;
}
