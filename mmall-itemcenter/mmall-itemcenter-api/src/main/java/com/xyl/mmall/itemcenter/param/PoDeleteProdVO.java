package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

/**
 * 从档期中取消添加商品的参数接收对象类
 * 
 * @author hzhuangluqian
 *
 */
public class PoDeleteProdVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2136011267395179940L;

	/** poId */
	private long poId;

	/** 从档期中取消添加的商品信息列表 */
	private List<ProductDltPoVO> data;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public List<ProductDltPoVO> getData() {
		return data;
	}

	public void setData(List<ProductDltPoVO> data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
