/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dto;

import java.io.Serializable;
import java.util.List;

/**
 * OrderReplenishStoreDTO.java created by yydx811 at 2015年6月6日 下午6:00:25
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class OrderReplenishStoreDTO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 3379734125803561324L;

	/** 店铺id. */
	private long businessId;

	/** 补货列表. */
	private List<OrderReplenishDTO> replenishVOList;

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public List<OrderReplenishDTO> getReplenishDTOList() {
		return replenishVOList;
	}

	public void setReplenishDTOList(List<OrderReplenishDTO> replenishVOList) {
		this.replenishVOList = replenishVOList;
	}
}
