/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProductSkuDetail;

/**
 * ProductSKUDetailDTO.java created by yydx811 at 2015年5月17日 下午11:58:09
 * 商品详情dto
 *
 * @author yydx811
 */
public class ProductSKUDetailDTO extends ProductSkuDetail {

	/** 序列化id. */
	private static final long serialVersionUID = 3460381165656753209L;

	public ProductSKUDetailDTO() {
	}

	public ProductSKUDetailDTO(ProductSkuDetail obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
