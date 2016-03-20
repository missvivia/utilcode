package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;


/**
 * 商品参数详情类
 * 
 * @author hzhuangluqian
 *
 */
public class ProdParamParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2216695649715244350L;

	/** 商品参数id */
	private long id;

	/** 商品参数值 */
	private String value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		int result = 1;
		int elm3 = Long.valueOf(id).hashCode();
		result = 31 * result + elm3;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdParamParam other = (ProdParamParam) obj;
		if (id != other.getId())
			return false;
		return true;
	}
}
