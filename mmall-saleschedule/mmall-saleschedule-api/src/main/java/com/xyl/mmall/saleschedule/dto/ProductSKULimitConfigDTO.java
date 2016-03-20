/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitConfig;

/**
 * ProductSKULimitConfigDTO.java created by yydx811 at 2015年11月17日 下午2:28:59
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class ProductSKULimitConfigDTO extends ProductSKULimitConfig {

	/** 序列化id. */
	private static final long serialVersionUID = -5907675798527195258L;
	
	/** 允许购买的数量. */
	private int allowedNum;

	public ProductSKULimitConfigDTO() {
	}
	
	public ProductSKULimitConfigDTO(ProductSKULimitConfig obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public int getAllowedNum() {
		return allowedNum;
	}

	public void setAllowedNum(int allowedNum) {
		this.allowedNum = allowedNum;
	}
	
	
}
