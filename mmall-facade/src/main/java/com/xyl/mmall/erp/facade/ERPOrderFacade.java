/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.erp.facade;

import java.util.List;

import com.xyl.mmall.erp.vo.OrderDetailInfoErpVO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderOperateParam;

/**
 * ERPOrderFacade.java created by yydx811 at 2015年8月3日 上午10:02:10
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public interface ERPOrderFacade {

	/**
	 * erp修改订单状态
	 * @param param
	 * @return int
	 */
	public int modifyOrderState(OrderOperateParam param);
	
	/**
	 * 
	 * @param businessIds
	 * @param startTime
	 * @param endTime
	 * @param state -1 全部
	 * @return
	 */
	public List<OrderForm> queryOrderFacade(String businessIds, long startTime, long endTime, int state);
	
	/**
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public OrderFormDTO getOrderFormByOrderId(long orderId, long userId);

	/**
	 * 根据订单Id查看订单信息
	 * @param orderId
	 * @return
	 */
	public OrderDetailInfoErpVO queryOrderDetailInfoByOrderId(long orderId);
}
