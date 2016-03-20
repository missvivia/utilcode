/**
 * 
 */
package com.xyl.mmall.mobile.web.facade;

import java.util.HashMap;
import java.util.List;

import com.xyl.mmall.mobile.web.vo.OrderTraceVO;



/**
 * 用户订单相关facade
 * 
 * @author lihui
 *
 */
public interface MobileOrderTraceFacade {

	/**
	 * 根据快递公司和快递单号查询快递物流轨迹信息数组，游标越小轨迹越早。没有轨迹信息空的列表。
	 * 
	 * @param expressCompany
	 * @param expressNO
	 * @return
	 */
	public List<OrderTraceVO> getTrace(String expressCompany, String expressNO);
	
	public List<HashMap<String,Object>> getTraceByOrderId(long userId,long orderId);
	
}
