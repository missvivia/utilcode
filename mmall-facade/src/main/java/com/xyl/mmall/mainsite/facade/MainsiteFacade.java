package com.xyl.mmall.mainsite.facade;

import java.util.List;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.mainsite.vo.OrderReplenishStoreVO;
import com.xyl.mmall.order.dto.OrderReplenishDTO;

public interface MainsiteFacade {

	/**
	 * 
	 * @param pageParamVO
	 * @return
	 */
	public List<OrderReplenishStoreVO> getReplenishList(BasePageParamVO<OrderReplenishStoreVO> pageParamVO, 
			OrderReplenishDTO replenishDTO);
}
