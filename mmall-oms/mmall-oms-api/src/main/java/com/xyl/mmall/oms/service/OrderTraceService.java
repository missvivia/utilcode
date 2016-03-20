/**
 * 
 */
package com.xyl.mmall.oms.service;

import com.xyl.mmall.oms.meta.OrderTrace;

/**
 * @author hzzengchengyuan
 *
 */
public interface OrderTraceService {

	/**
	 * 根据快递公司和快递单号查询快递物流轨迹信息数组，游标越小轨迹越早。没有轨迹信息返回null
	 * @param expressCompany
	 * @param expressNO
	 * @return
	 */
	public OrderTrace[] getTrace(String expressCompany, String expressNO);
}
