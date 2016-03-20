package com.xyl.mmall.cart.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.cart.enums.CartItemType;

@AnnonOfClass(desc = "购物车的Item", tableName = "TB_Cart_CartItem", policy = "userId")
public class CartItem implements Serializable {
	
	private static final long serialVersionUID = 20140911L;
	
	@AnnonOfField(desc = "购物车Item的id", primary = true)
	private long id;
	
	@AnnonOfField(desc = "用户id")
	private long userid;
	
	@AnnonOfField(desc = "放入购物车时候的销售价格")
	private BigDecimal retailPrice;
	
	@AnnonOfField(desc = "商品原价")
	private BigDecimal originalPrice;
	
	@AnnonOfField(desc = "购买数量")
	private int count;
	
	@AnnonOfField(desc = "标示该Item是普通商品还是赠品，0 普通商品 1 赠品")
	private CartItemType cartType;
	
	@AnnonOfField(desc = "创建时间")
	private long createTime;
	
	@AnnonOfField(desc = "是否在购物车中显示")
	private boolean isVisible = true;
	
	@AnnonOfField(desc = "sku id")
	private long skuId;
	
	@AnnonOfField(desc = "branch id 品牌id")
	private long branchId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public CartItemType getCartType() {
		return cartType;
	}

	public void setCartType(CartItemType cartType) {
		this.cartType = cartType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
}
