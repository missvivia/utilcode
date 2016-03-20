package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.enums.OrderFormState;

@AnnonOfClass(desc = "订单", tableName = "Mmall_Order_OrderForm")
public class OrderForm implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "订单Id", primary = true, autoAllocateId = true)
	private long orderId;

	@AnnonOfField(desc = "订单父Id")
	private long parentId;

	@AnnonOfField(desc = "商店的Id")
	private long businessId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "站点Id")
	private int provinceId;

	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	@AnnonOfField(desc = "支付成功时间")
	private long payTime;

	@AnnonOfField(desc = "发货时间")
	private long omsTime;

	@AnnonOfField(desc = "全部包裹确认收货时间")
	private long confirmTime;

	@AnnonOfField(desc = "购物车商品零售总价-结算价")
	private BigDecimal cartRPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "购物车商品零售总价-原价")
	private BigDecimal cartOriRPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-原价")
	private BigDecimal expOriPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-用户支付价(总额)")
	private BigDecimal expUserPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-系统垫付金额")
	private BigDecimal expSysPayPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-用户支付价(红包抵扣)")
	private BigDecimal expUserPriceOfRed = BigDecimal.ZERO;

	@AnnonOfField(desc = "订单总金额里红包抵扣的金额")
	private BigDecimal redCash = BigDecimal.ZERO;

	@AnnonOfField(desc = "订单总金额里优惠券抵扣的金额")
	private BigDecimal couponDiscount = BigDecimal.ZERO;

	@Deprecated
	@AnnonOfField(desc = "快递方式(用户下单时选择的)")
	private ExpressCompany expressCompany;

	@AnnonOfField(desc = "订单对用户是否可见")
	private boolean isVisible = true;

	@AnnonOfField(desc = "支付状态(20:未付款,30:已付款,40:已经退款)")
	private PayState payState;

	@AnnonOfField(desc = "订单状态")
	private OrderFormState orderFormState;

	@AnnonOfField(desc = "订单来源(0:主站,1:安卓,2:IOS)")
	private OrderFormSource orderFormSource;

	@AnnonOfField(desc = "订单支付方式(0:网易宝,1:货到付款)")
	private OrderFormPayMethod orderFormPayMethod;

	@AnnonOfField(desc = "订单渠道")
	private SpSource spSource = SpSource.MMALL;

	@AnnonOfField(desc = "订单上使用的促销Id", notNull = false)
	private Long promotionId;

	@Deprecated
	@AnnonOfField(desc = "客服重新开启退货申请入口")
	private boolean kfReopenReturn = false;

	@Deprecated
	@AnnonOfField(desc = "客服重新开启退货申请入口的时间(kfReopenReturn=true时有效)")
	private long reopenReturnTime;

	@AnnonOfField(desc = "是否允许选择货到付款(根据产品的配送方式+快递+地址判断)")
	private boolean canCODBySku = false;

	@AnnonOfField(desc = "扩展字段")
	private String extInfo;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	@AnnonOfField(desc = "更新时间")
	private Date updateTime;

	@AnnonOfField(desc = "备注")
	private String comment;

	@AnnonOfField(desc = "操作用户Id")
	private long agentId;

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public BigDecimal getExpUserPriceOfRed() {
		return expUserPriceOfRed;
	}

	public void setExpUserPriceOfRed(BigDecimal expUserPriceOfRed) {
		this.expUserPriceOfRed = expUserPriceOfRed;
	}

	public BigDecimal getRedCash() {
		return redCash;
	}

	public void setRedCash(BigDecimal redCash) {
		this.redCash = redCash;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public boolean isCanCODBySku() {
		return canCODBySku;
	}

	public void setCanCODBySku(boolean canCODBySku) {
		this.canCODBySku = canCODBySku;
	}

	@Deprecated
	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	@Deprecated
	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	public OrderFormPayMethod getOrderFormPayMethod() {
		return orderFormPayMethod;
	}

	public void setOrderFormPayMethod(OrderFormPayMethod orderFormPayMethod) {
		this.orderFormPayMethod = orderFormPayMethod;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public BigDecimal getCartOriRPrice() {
		return cartOriRPrice;
	}

	public void setCartOriRPrice(BigDecimal cartOriRPrice) {
		this.cartOriRPrice = cartOriRPrice;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public long getOmsTime() {
		return omsTime;
	}

	public void setOmsTime(long omsTime) {
		this.omsTime = omsTime;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

	public BigDecimal getExpOriPrice() {
		return expOriPrice;
	}

	public void setExpOriPrice(BigDecimal expOriPrice) {
		this.expOriPrice = expOriPrice;
	}

	public BigDecimal getExpUserPrice() {
		return expUserPrice;
	}

	public void setExpUserPrice(BigDecimal expUserPrice) {
		this.expUserPrice = expUserPrice;
	}

	public BigDecimal getExpSysPayPrice() {
		return expSysPayPrice;
	}

	public void setExpSysPayPrice(BigDecimal expSysPayPrice) {
		this.expSysPayPrice = expSysPayPrice;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public PayState getPayState() {
		return payState;
	}

	public void setPayState(PayState payState) {
		this.payState = payState;
	}

	public OrderFormState getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(OrderFormState orderFormState) {
		this.orderFormState = orderFormState;
	}

	public OrderFormSource getOrderFormSource() {
		return orderFormSource;
	}

	public void setOrderFormSource(OrderFormSource orderFormSource) {
		this.orderFormSource = orderFormSource;
	}

	public SpSource getSpSource() {
		return spSource;
	}

	public void setSpSource(SpSource spSource) {
		this.spSource = spSource;
	}

	public boolean isKfReopenReturn() {
		return kfReopenReturn;
	}

	public void setKfReopenReturn(boolean kfReopenReturn) {
		this.kfReopenReturn = kfReopenReturn;
	}

	public long getReopenReturnTime() {
		return reopenReturnTime;
	}

	public void setReopenReturnTime(long reopenReturnTime) {
		this.reopenReturnTime = reopenReturnTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

}