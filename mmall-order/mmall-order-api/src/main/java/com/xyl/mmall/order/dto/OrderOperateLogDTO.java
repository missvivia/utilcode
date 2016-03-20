/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderOperateLog;

/**
 * OrderOperateLogDTO.java created by yydx811 at 2015年6月10日 下午3:46:41
 * cms订单操作日志
 *
 * @author yydx811
 */
public class OrderOperateLogDTO extends OrderOperateLog {

	/** 序列化id. */
	private static final long serialVersionUID = 223572997107487860L;

	public OrderOperateLogDTO() {
	}
	
	public OrderOperateLogDTO(OrderOperateLog obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
