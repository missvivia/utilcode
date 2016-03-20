package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

/**
 * 档期取消商品添加的商品参数接收类
 * 
 * @author hzhuangluqian
 *
 */
public class ProductDltPoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5054280520940881658L;

	/** 商品id */
	private long productId;

	/** 取消添加的skuId列表 */
	private List<Long> skuList;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public List<Long> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<Long> skuList) {
		this.skuList = skuList;
	}
}
