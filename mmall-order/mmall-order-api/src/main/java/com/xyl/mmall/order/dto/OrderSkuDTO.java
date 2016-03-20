package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.meta.OrderSku;

/**
 * OrderCartItem上显示用的基本单位
 * 
 * @author dingmingliang
 * 
 */
public class OrderSkuDTO extends OrderSku {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * Sku的快照信息
	 */
	private SkuSPDTO skuSPDTO;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderSkuDTO(OrderSku obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderSkuDTO() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}

	public void setSkuSnapshot(String skuSnapshot) {
		super.setSkuSnapshot(skuSnapshot);
		setSkuSPDTO(JsonUtils.fromJson(getSkuSnapshot(), SkuSPDTO.class));
	}

	public SkuSPDTO getSkuSPDTO() {
		return skuSPDTO;
	}

	public void setSkuSPDTO(SkuSPDTO skuSPDTO) {
		this.skuSPDTO = skuSPDTO;
	}
}
