package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.CancelOmsOrderTask;
import com.xyl.mmall.order.meta.OrderCancelInfo;

/**
 * 取消Oms订单的任务
 * 
 * @author dingmingliang
 *
 */
public class CancelOmsOrderTaskDTO extends CancelOmsOrderTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20150206L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CancelOmsOrderTaskDTO(CancelOmsOrderTask obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CancelOmsOrderTaskDTO(OrderCancelInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public CancelOmsOrderTaskDTO() {
	}

}
