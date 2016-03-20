package com.xyl.mmall.timer.facade;

import com.netease.print.common.meta.RetArg;

/**
 * 订单相关的定时器
 * 
 * @author dingmingliang
 * 
 */
public interface OrderTimerFacade {

	/**
	 * 推送订单给OMS系统
	 * 
	 * @return
	 */
	public void pushOrderToOms();

	/**
	 * 取消未支付的订单
	 * 
	 * @return
	 */
	public void cancelUnpayOrderByTimout();

	/**
	 * 设置订单状态为交易完成
	 */
	public void setOrderToFinishDelive();

	/**
	 * 设置取消订单的任务完成
	 */
	public void setOrderCancelToDone();

	/**
	 * 处理CancelOmsOrderTask记录<br>
	 * 1.判断并取消oms订单<BR>
	 * 2.取消订单
	 */
	public void dealCancelOmsOrderTask();

	/**
	 * 取消Oms里的订单
	 */
	public void cancelOmsOrder();

	/**
	 * 取消已经标记为待取消的订单交易
	 */
	public void cancelTrade();

	/**
	 * 处理包裹退款任务
	 */
	public void orderPackageRefundTask();

	/**
	 * 回收所有包裹取消的订单的优惠券
	 * 
	 * @return RetArg.Boolean RetArg.String
	 */
	public RetArg recycleCouponForOrderOfAllPackageCancelled();
	
	/**
	 * 同步商品销售
	 * @param dealNum
	 */
	public void syncProductSaleNum(int dealNum);
}
