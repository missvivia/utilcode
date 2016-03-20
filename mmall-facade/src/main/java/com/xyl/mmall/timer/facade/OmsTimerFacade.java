package com.xyl.mmall.timer.facade;

/**
 * @author zb<br>
 *         一些定时器的入口
 */
public interface OmsTimerFacade {

	/**
	 * 将销售订单推送给仓库
	 */
	public void pushOmsOrderFormToWarehose();

	/**
	 * 将发货单推送给仓库
	 */
	public void pushOmsShipOrderToWarehose();

	/**
	 * 将销售定的状态反馈给app
	 */
	public void pushOmsOrderFormStateToApp();

	/**
	 * 将退货单的状态反馈给app
	 */
	public void pushOmsReturnOrderFormStateToApp();

	/**
	 * 将包裹状态的更新反馈给app
	 */
	public void pushOmsOrderPackageToApp();

	/**
	 * 生成拣货单
	 */
	public void generatePickOrder();

	/**
	 * 取消超时未配送的订单
	 */
	public void cancelTimeOutOrderForm();
	
	/**
	 * 取消超时的入库单
	 */
	public void cancelTimeOutShipOrder();
	
	/**
	 * 计算运费
	 */
	public void calcuateFreight();
}
