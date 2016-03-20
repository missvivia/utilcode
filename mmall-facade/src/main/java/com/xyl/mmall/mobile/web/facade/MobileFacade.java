package com.xyl.mmall.mobile.web.facade;

import java.util.List;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.mobile.web.vo.OrderReplenishStoreVO;
import com.xyl.mmall.order.dto.OrderReplenishDTO;

public interface MobileFacade {

	/**
	 * 
	 * @param pageParamVO
	 * @return
	 */
	public List<OrderReplenishStoreVO> getReplenishList(BasePageParamVO<OrderReplenishStoreVO> pageParamVO, 
			OrderReplenishDTO replenishDTO);
}
