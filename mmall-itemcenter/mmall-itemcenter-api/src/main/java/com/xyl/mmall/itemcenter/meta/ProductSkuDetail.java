package com.xyl.mmall.itemcenter.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.itemcenter.dto.ProductSKUDetailDTO;

@AnnonOfClass(tableName = "Mmall_ItemCenter_ProductDetail", desc = "商品详情表", dbCreateTimeName = "CreateTime")
public class ProductSkuDetail extends BaseVersion {

	private static final long serialVersionUID = 4046225987726963116L;

	/** 详情id */
	@AnnonOfField(desc = "详情id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;
	
	/** 商品id */
	@AnnonOfField(desc = "商品id", policy = true)
	private long productSKUId;

	/**用户富文本框编辑的HTML*/
	@AnnonOfField(desc = "用户富文本框编辑的HTML", type = "VARCHAR(3000)")
	private String customEditHTML;
	
	/** 商品参数详情 json*/
	@AnnonOfField(desc = "商品参数详情 json", type = "VARCHAR(512)")
	private String prodParam;

	public ProductSkuDetail() {
	}

	public ProductSkuDetail(ProductSKUDetailDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductSKUId() {
		return productSKUId;
	}

	public void setProductSKUId(long productSKUId) {
		this.productSKUId = productSKUId;
	}

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
	}

	public String getProdParam() {
		return prodParam;
	}

	public void setProdParam(String prodParam) {
		this.prodParam = prodParam;
	}
}
