package com.xyl.mmall.mobile.ios.facade;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.order.param.OrderSearchParam;

public interface IosOrderFacade {

	public RetArg queryNewOrderList(OrderSearchParam orderSearchParam);
	
	public RetArg queryOrderFormByOrderIdAndUserId(long userId, long orderId, Boolean isVisible);

	public RetArg queryOrderFormByParentOrderIdAndUserId(long userId, long orderId, Boolean isVisible);
	
	public int queryNewOrderCount(OrderSearchParam orderSearchParam);
}
