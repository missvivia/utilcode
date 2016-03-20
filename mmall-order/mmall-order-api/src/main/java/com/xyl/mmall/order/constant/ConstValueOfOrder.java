package com.xyl.mmall.order.constant;

import java.math.BigDecimal;

import com.netease.print.common.constant.CalendarConst;

/**
 * 订单服务的常量
 * 
 * @author dingmingliang
 * 
 */
public final class ConstValueOfOrder {

	private ConstValueOfOrder() {
	}

	/**
	 * 全场包邮的价格
	 */
	public static final BigDecimal FREE_EXP_RPRICE = new BigDecimal("288");

	/**
	 * COD无效的信息: 产品对应的快递方式不支持
	 */
	public static final String COD_INVALIDMESS_SKU = "您选择的地址无法提供货到付款服务";
	
	/**
	 * COD无效的信息: COD黑名单
	 */
	public static final String COD_INVALIDMESS_INBLACK = "地址黑名单";
	
	/**
	 * COD无效的信息: 金额不对
	 */
	public static final String COD_INVALIDMESS_AMOUNT = "订单金额小于11元或大于2000元，不支持货到付款服务";
	
	/**
	 * 下单后,允许支付的时限: 120分钟
	 */
	public static final long MAX_PAY_TIME = 120 * CalendarConst.MINUTE_TIME;

	/**
	 * 发货后,发票定时器开始处理发票的时间间隔(发货时间): 20天
	 */
	public static final long SEP_INVOICE_TIME = 20 * CalendarConst.DAY_TIME;
	
	/**
	 * 退货申请自动取消时间: 下单后30天
	 */
	public static final long SEP_RP_APPLY_DAY = 30;

	/**
	 * 下单后,转为等待调用库存系统的发货命令(可取消): 2小时
	 */
	@Deprecated
	public static final long MAX_SEND_ERP_TIME = 2 * CalendarConst.HOUR_TIME;
}
