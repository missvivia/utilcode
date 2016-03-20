package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.enums.CartItemType;

/**
 * 订单明细
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderCartItem", desc = "订单明细")
public class OrderCartItem implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "销售价")
	private BigDecimal retailPrice;

	@AnnonOfField(desc = "原价")
	private BigDecimal originalPrice;

	@AnnonOfField(desc = "购买数量")
	private int count;

	@AnnonOfField(desc = "购物车类型(1:普通)")
	private CartItemType cartType;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "订单Id")
	private long orderId;

	@AnnonOfField(desc = "包裹Id")
	private long packageId;

	@AnnonOfField(desc = "是否使用了赠品规则")
	private boolean useGift = false;

	@AnnonOfField(desc = "选择的促销Id", notNull = false)
	private Long promotionId;

	@AnnonOfField(desc = "选择的促销效果的Idx", unsigned = false)
	private int promotionIdx;

	public int getPromotionIdx() {
		return promotionIdx;
	}

	public void setPromotionIdx(int promotionIdx) {
		this.promotionIdx = promotionIdx;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public boolean isUseGift() {
		return useGift;
	}

	public void setUseGift(boolean useGift) {
		this.useGift = useGift;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public CartItemType getCartType() {
		return cartType;
	}

	public void setCartType(CartItemType cartType) {
		this.cartType = cartType;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}