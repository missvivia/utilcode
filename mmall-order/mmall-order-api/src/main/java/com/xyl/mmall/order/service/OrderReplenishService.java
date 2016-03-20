/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.service;

import java.util.List;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.order.dto.OrderReplenishDTO;
import com.xyl.mmall.order.dto.OrderReplenishStoreDTO;

/**
 * OrderReplenishService.java created by yydx811 at 2015年6月6日 下午4:00:15
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public interface OrderReplenishService {

	/**
	 * 
	 * @param basePageParamVO
	 * @param replenishDTO
	 * @return
	 */
	public BasePageParamVO<OrderReplenishStoreDTO> getReplenishList(BasePageParamVO<OrderReplenishStoreDTO> basePageParamVO, 
			OrderReplenishDTO replenishDTO);
	
	/**
	 * 获取列表
	 * @param replenishDTO
	 * @return
	 */
	public List<OrderReplenishDTO> getReplenishList(OrderReplenishDTO replenishDTO);
}
