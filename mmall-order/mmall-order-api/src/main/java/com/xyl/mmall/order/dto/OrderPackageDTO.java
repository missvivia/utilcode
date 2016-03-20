package com.xyl.mmall.order.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderPackage;

/**
 * 订单包裹
 * 
 * @author dingmingliang
 * 
 */
public class OrderPackageDTO extends OrderPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderPackageDTO(OrderPackage obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderPackageDTO() {
	}

	/**
	 * 订单快递地址信息
	 */
	private OrderExpInfoDTO orderExpInfoDTO;

	/**
	 * 包裹退款总计(不包含订单取消和退货导致的退款)
	 */
	private OrderPackageRefundDTO orderPackageRefundDTO;

	/**
	 * 包裹下包含的订单明细
	 */
	private List<? extends OrderCartItemDTO> orderCartItemDTOList;

	public OrderPackageRefundDTO getOrderPackageRefundDTO() {
		return orderPackageRefundDTO;
	}

	public void setOrderPackageRefundDTO(OrderPackageRefundDTO orderPackageRefundDTO) {
		this.orderPackageRefundDTO = orderPackageRefundDTO;
	}

	public OrderExpInfoDTO getOrderExpInfoDTO() {
		return orderExpInfoDTO;
	}

	public void setOrderExpInfoDTO(OrderExpInfoDTO orderExpInfoDTO) {
		this.orderExpInfoDTO = orderExpInfoDTO;
	}

	public List<? extends OrderCartItemDTO> getOrderCartItemDTOList() {
		return orderCartItemDTOList;
	}

	public void setOrderCartItemDTOList(List<? extends OrderCartItemDTO> orderCartItemDTOList) {
		this.orderCartItemDTOList = orderCartItemDTOList;
	}

	/**
	 * 
	 * @return key: OrderSku Id value: OrderSkuDTO
	 */
	public Map<Long, OrderSkuDTO> mapOrderSkuDTO() {
		Map<Long, OrderSkuDTO> ret = new HashMap<Long, OrderSkuDTO>();
		if (null == orderCartItemDTOList) {
			return ret;
		}
		for (OrderCartItemDTO item : orderCartItemDTOList) {
			if (null == item) {
				continue;
			}
			List<? extends OrderSkuDTO> ordSkuList = item.getOrderSkuDTOList();
			for (OrderSkuDTO ordSku : ordSkuList) {
				if (null == ordSku) {
					continue;
				}
				ret.put(ordSku.getId(), ordSku);
			}
		}
		return ret;
	}
}
