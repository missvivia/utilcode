package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

/**
 * PO商品添加的请求参数接收对象
 * 
 * @author hzhuangluqian
 *
 */
public class PoAddProductReqVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6706385681836718567L;

	/** 档期id */
	private long poId;

	private long supplierId;
	/** 添加的商品信息 */
	private List<ProductAddPoVO> data;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public List<ProductAddPoVO> getData() {
		return data;
	}

	public void setData(List<ProductAddPoVO> data) {
		this.data = data;
	}

}
