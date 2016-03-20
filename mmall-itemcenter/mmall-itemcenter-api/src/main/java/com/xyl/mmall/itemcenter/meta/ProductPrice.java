package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;

/**
 * 商品价格区间表
 * @author {yydx811}
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProductPrice", desc = "商品价格区间表")
public class ProductPrice implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 2947103679548634386L;

	@AnnonOfField(desc = "价格id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "产品id", policy = true)
	private long productId;
	
	@AnnonOfField(desc = "最小数目")
	private int minNumber;
	
	@AnnonOfField(desc = "最大数目")
	private int maxNumber;
	
	@AnnonOfField(desc = "价格")
	private BigDecimal price;

	public ProductPrice() {
	}

	public ProductPrice(ProductPriceDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(int minNumber) {
		this.minNumber = minNumber;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
