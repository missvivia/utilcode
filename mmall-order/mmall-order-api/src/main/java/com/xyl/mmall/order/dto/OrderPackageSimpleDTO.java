package com.xyl.mmall.order.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderPackage;

/**
 * 订单包裹(Simple版)
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class OrderPackageSimpleDTO extends OrderPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderPackageSimpleDTO(OrderPackage obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderPackageSimpleDTO() {
	}
	
	/**
	 * key: OrderSku Id
	 * value: OrderSkuDTO
	 */
	private Map<Long, OrderSkuDTO> orderSkuMap = new HashMap<Long, OrderSkuDTO>();
	
	private OrderExpInfoDTO orderExpInfo;

	public Map<Long, OrderSkuDTO> getOrderSkuMap() {
		return orderSkuMap;
	}

	public void setOrderSkuMap(Map<Long, OrderSkuDTO> orderSkuMap) {
		this.orderSkuMap = orderSkuMap;
	}

	public OrderExpInfoDTO getOrderExpInfo() {
		return orderExpInfo;
	}

	public void setOrderExpInfo(OrderExpInfoDTO orderExpInfo) {
		this.orderExpInfo = orderExpInfo;
	}
	
	/**
	 * 获取PO列表
	 * 
	 * @return
	 */
	public List<Long> extractPOIdList() {
		List<Long> ret = new ArrayList<Long>();
		if(null == orderSkuMap) {
			return ret;
		}
		Set<Long> poIdSet = new HashSet<Long>();
		for(Entry<Long, OrderSkuDTO> entry : orderSkuMap.entrySet()) {
			OrderSkuDTO ordSku = null;
			if(null == entry || null == (ordSku = entry.getValue())) {
				continue;
			}
			poIdSet.add(ordSku.getPoId());
		}
		for (Long poId : poIdSet) {
			ret.add(poId);
		}
		return ret;
	}
	
	/**
	 * 商品结算总金额 = 用户实付Cash金额 + 用户实付红包金额
	 * @return
	 */
	public BigDecimal totalPayedPrice() {
		return computePayedPrice(0);
	}
	
	/**
	 * 用户实付Cash金额
	 * @return
	 */
	public BigDecimal cashPayedPrice() {
		return computePayedPrice(1);
	}
	
	/**
	 * 用户实付红包金额
	 * @return
	 */
	public BigDecimal hbPayedPrice() {
		return computePayedPrice(2);
	}
	
	private BigDecimal computePayedPrice(int button) {
		BigDecimal acc = BigDecimal.ZERO;
		if(CollectionUtil.isEmptyOfMap(orderSkuMap)) {
			return acc;
		}
		for(Entry<Long, OrderSkuDTO> entry : orderSkuMap.entrySet()) {
			OrderSkuDTO ordSku = null;
			if(null == entry || null == (ordSku = entry.getValue())) {
				continue;
			}
			int count = ordSku.getTotalCount();
			BigDecimal choice = BigDecimal.ZERO;
			switch(button) {
			case 0:
				choice = ordSku.getRprice();
				break;
			case 1:
				// to be continued: 结算金额中Cash部分
				choice = ordSku.getRprice();
				break;
			case 2:
				// to be continued: 结算金额中红包部分
				choice = BigDecimal.ZERO;
				break;
			default:
				choice = ordSku.getRprice();
			}
			if(null != choice) {
				acc.add(choice.multiply(new BigDecimal(count)));
			}
		}
		return acc;
	}
}
