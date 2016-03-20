package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

/**
 * 尺码和尺码详情
 * 
 * @author hzhuangluqian
 *
 */
public class SizeDetailPaire implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3083167686458131200L;

	/** 尺码，如L码 */
	private String size;

	/** 尺码详情，如（身高 180，腰围 99） */
	private String sizeDetail;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSizeDetail() {
		return sizeDetail;
	}

	public void setSizeDetail(String sizeDetail) {
		this.sizeDetail = sizeDetail;
	}
}
