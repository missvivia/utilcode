package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.Product;

/**
 * 商家平台商品管理中的商品查询结果类
 * 
 * @author hzhuangluqian
 *
 */
public class ProductSearchResultDTO extends Product  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 993629062202074413L;

	/** 类目名称 */
	private String categoryName;

	/** 品牌地址 */
	private String brandName;

	public ProductSearchResultDTO(Product obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
}
