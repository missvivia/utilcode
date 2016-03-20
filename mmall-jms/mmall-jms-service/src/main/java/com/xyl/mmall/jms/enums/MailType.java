package com.xyl.mmall.jms.enums;

/**
 * 邮件类型：
 * <p>
 * 1.NORMAL(事务性邮件):a.优惠券过期提醒 ,b:订单支付相关直邮
 * <p>
 * 2.SUBSCRIBE(订阅邮件):a.品牌订阅直邮,b.登录提醒直邮
 * 
 * @author hzzhaozhenzuo
 *
 */
public enum MailType {
	NORMAL, SUBSCRIBE;
}
