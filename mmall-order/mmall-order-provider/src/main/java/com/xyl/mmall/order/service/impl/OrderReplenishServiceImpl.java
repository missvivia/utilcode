/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.order.dao.OrderReplenishDAO;
import com.xyl.mmall.order.dto.OrderReplenishDTO;
import com.xyl.mmall.order.dto.OrderReplenishStoreDTO;
import com.xyl.mmall.order.meta.OrderReplenish;
import com.xyl.mmall.order.service.OrderReplenishService;

/**
 * OrderReplenishServiceImpl.java created by yydx811 at 2015年6月6日 下午4:02:24
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Service("orderReplenishService")
public class OrderReplenishServiceImpl implements OrderReplenishService {

	@Autowired
	private OrderReplenishDAO orderReplenishDAO;
	
	@Override
	public BasePageParamVO<OrderReplenishStoreDTO> getReplenishList(BasePageParamVO<OrderReplenishStoreDTO> basePageParamVO,
			OrderReplenishDTO replenishDTO) {
		// 获取店铺分页
		OrderReplenish replenish = new OrderReplenish(replenishDTO);
		basePageParamVO = orderReplenishDAO.getReplenishGroupByBusinessId(basePageParamVO, replenish);
		if (basePageParamVO == null || CollectionUtils.isEmpty(basePageParamVO.getList())) {
			return null;
		}
		// 获取补货单列表
		List<OrderReplenishStoreDTO> retList = new ArrayList<OrderReplenishStoreDTO>(basePageParamVO.getList().size());
		for (OrderReplenishStoreDTO replenishStoreDTO : basePageParamVO.getList()) {
			replenishDTO.setBusinessId(replenishStoreDTO.getBusinessId());
			List<OrderReplenishDTO> replenishDTOList = getReplenishList(replenishDTO);
			if (CollectionUtils.isEmpty(replenishDTOList)) {
				continue;
			}
			replenishStoreDTO.setReplenishDTOList(replenishDTOList);
			retList.add(replenishStoreDTO);
		}
		basePageParamVO.setList(retList);
		return basePageParamVO;
	}

	@Override
	public List<OrderReplenishDTO> getReplenishList(OrderReplenishDTO replenishDTO) {
		OrderReplenish replenish = new OrderReplenish(replenishDTO);
		return convertToDTO(orderReplenishDAO.getReplenishList(replenish));
	}

	private List<OrderReplenishDTO> convertToDTO(List<OrderReplenish> replenishList) {
		if (CollectionUtils.isEmpty(replenishList)) {
			return null;
		}
		List<OrderReplenishDTO> retList = new ArrayList<OrderReplenishDTO>(replenishList.size());
		for (OrderReplenish replenish : replenishList) {
			retList.add(new OrderReplenishDTO(replenish));
		}
		return retList;
	}
}
