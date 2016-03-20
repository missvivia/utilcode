package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.framework.vo.BaseJsonListResultVO;

public class POAddSkuVO extends BaseJsonListResultVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1268596334832592469L;

	private int skuTotal;

	private int scheduleType;

	public int getSkuTotal() {
		return skuTotal;
	}

	public void setSkuTotal(int skuTotal) {
		this.skuTotal = skuTotal;
	}

	public int getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}

}
