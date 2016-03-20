/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mainsite.vo;

import java.io.Serializable;
import java.util.List;

/**
 * OrderReplenishStoreVO.java created by yydx811 at 2015年6月6日 下午6:00:25
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class OrderReplenishStoreVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 3379734125803561324L;

	/** 店铺id. */
	private long supplierId;

	/** 店铺名. */
	private String storeName;

	/** 补货列表. */
	private List<OrderReplenishVO> replenishVOList;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public List<OrderReplenishVO> getReplenishVOList() {
		return replenishVOList;
	}

	public void setReplenishVOList(List<OrderReplenishVO> replenishVOList) {
		this.replenishVOList = replenishVOList;
	}
}
