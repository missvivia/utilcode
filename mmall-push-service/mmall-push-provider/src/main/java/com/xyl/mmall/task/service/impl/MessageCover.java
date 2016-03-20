package com.xyl.mmall.task.service.impl;

import com.xyl.mmall.framework.mobile.MobileURL;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.task.bean.PushServiceConfig;

public class MessageCover extends PushMessageType{
	public static String type_1 = "订单已发货 摘要：您的订单<*>已发货，可在已发货订单中查看物流信息。";

	public static String type_2 = "您的订单<*>退货已经办理完成，请到退货订单中查看详情。";

	public static String type_3 = "您的订单<*>退货被拒，请到退货订单中查看详情。";

	public static String type_4 = "送您1个mmall红包，直低现金使用。";

	public static String type_5 = "送您1张mmall优惠券，全场特卖可叠加使用。";

	public static String type_6 = "您的订单<*>配送地址暂不支持货到付款，请联系客服修改。";

	public static String type_7 = "您有一笔未付款订单保留时间还有5分钟，请尽快付款。";
	
	public static String type_8 = "您的购物袋商品保留时间还有5分钟，请尽快结算。";
	
	public static String type_9 = "您的订单<*>已发货";
	
	public static String type_11 = "您的订单<*>中部分商品由于缺货被取消。";
	
	public static String type_12 = "您关注的品牌特卖就要开始了，准备抢购吧";
	
	public static String type_13 = "您的订单签收完成，获得一个新的红包分享";
	
	public static String genAppUrl(int type ,String keyId){
		String url = "";
		switch(type){
			case send_order:
			case retruns_success:
			case retruns_fail:
			case cod_fail:
			case order_cancel_after_pay:
			case cod_reject:
			case package_cancel:
			case share_gift:
			case order_timeout:
				url =  MobileURL.genMobilePageLink(MobileURL.TYPE_ORDER, keyId);
				break;
			case give_coupon:
				url =  MobileURL.genMobilePageLink(MobileURL.TYPE_COUPON, keyId);
				break;
			case give_gift:
				url =  MobileURL.genMobilePageLink(MobileURL.TYPE_GIFT, keyId);
				break;
			case cart_timeout:
				url = MobileURL.genMobilePageLink(MobileURL.TYPE_CART, keyId);
				break;
			case tell_user_po:
				url = MobileURL.genMobilePageLink(MobileURL.TYPE_PO, keyId);
				break;
		}
		return url;	
	}
	
	public static String genTitle(int type){
		switch(type){
			case send_order:
				return PushServiceConfig.type_1;
			case retruns_success:
				return PushServiceConfig.type_2;
			case retruns_fail:
				return PushServiceConfig.type_3;
			case give_gift:
				return PushServiceConfig.type_4;
			case give_coupon:
				return PushServiceConfig.type_5;
			case cod_fail:
			case cod_reject:
				return PushServiceConfig.type_6;
			case order_timeout:
				return PushServiceConfig.type_7;
			case cart_timeout:
				return PushServiceConfig.type_8;
			case order_cancel_after_pay:
				return PushServiceConfig.type_9;
			case package_cancel:
				return PushServiceConfig.type_11;
			case tell_user_po:
				return PushServiceConfig.type_12;
			case share_gift:
				return PushServiceConfig.type_13;
		}
		return "";
		
	}
	
	public static String genMessage(int type,long orderId){
		String temp = "";
		if(orderId > 0)
			temp = String.valueOf(orderId);
		switch(type){
			case send_order:
				return MessageCover.type_1.replace("<*>", temp);
			case retruns_success:
				return MessageCover.type_2.replace("<*>", temp);
			case retruns_fail:
				return MessageCover.type_3.replace("<*>", temp);
			case give_gift:
				return MessageCover.type_4;
			case give_coupon:
				return MessageCover.type_5;
			case cod_fail:
			case cod_reject:
				return MessageCover.type_6.replace("<*>", temp);
			case order_timeout:
				return MessageCover.type_7;
			case cart_timeout:
				return MessageCover.type_8;
			case order_cancel_after_pay:
				return MessageCover.type_9.replace("<*>", temp);
			case package_cancel:
				return MessageCover.type_11.replace("<*>", temp);
			case tell_user_po:
				return MessageCover.type_12;
			case share_gift:
				return MessageCover.type_13;
				
		}
		return "";
		
	}
	
}
