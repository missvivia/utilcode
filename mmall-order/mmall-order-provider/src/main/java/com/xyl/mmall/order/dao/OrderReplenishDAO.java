/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.order.dto.OrderReplenishStoreDTO;
import com.xyl.mmall.order.meta.OrderReplenish;

/**
 * OrderReplenishDAO.java created by yydx811 at 2015年6月6日 下午6:30:38
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public interface OrderReplenishDAO extends AbstractDao<OrderReplenish> {

	/**
	 * 
	 * @param basePageParamVO
	 * @param replenish
	 * @return
	 */
	public BasePageParamVO<OrderReplenishStoreDTO> getReplenishGroupByBusinessId(BasePageParamVO<OrderReplenishStoreDTO> basePageParamVO,
			OrderReplenish replenish);
	
	/**
	 * 
	 * @param replenish
	 * @return
	 */
	public List<OrderReplenish> getReplenishList(OrderReplenish replenish);
}
