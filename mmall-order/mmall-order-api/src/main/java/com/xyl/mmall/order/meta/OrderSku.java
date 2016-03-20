package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * OrderCartItem上显示用的基本单位<br>
 * 包含了订单上的赠品
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderSku", desc = "OrderCartItem上显示用的基本单位", dbCreateTimeName = "CreateTime")
public class OrderSku implements Serializable {

	private static final long serialVersionUID = 3L;

	@AnnonOfField(desc = "id", primary = true)
	private long id;

	@AnnonOfField(desc = "订单Id")
	private long orderId;

	@AnnonOfField(desc = "包裹Id")
	private long packageId;

	@AnnonOfField(desc = "订单明细Id")
	private long orderCartItemId;

	@AnnonOfField(desc = "专柜价(单位)")
	private BigDecimal marketPrice;

	@AnnonOfField(desc = "原始价格/闪购价(单价)")
	private BigDecimal oriRPrice;

	@AnnonOfField(desc = "最终零售价格[包含红包优惠](单位)")
	private BigDecimal rprice;

	@AnnonOfField(desc = "活动优惠的差额(单位)")
	private BigDecimal hdSPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "优惠券优惠的差额(单位)")
	private BigDecimal couponSPrice = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "红包优惠的差额(单位)")
	private BigDecimal redSPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "数量单位")
	private int unitCount = 1;

	@AnnonOfField(desc = "总数量")
	private int totalCount;

	@AnnonOfField(desc = "产品的供应商Id")
	private long supplierId;

	@AnnonOfField(desc = "产品Id")
	private long productId;

	@AnnonOfField(desc = "skuId")
	private long skuId;

	@AnnonOfField(desc = "poId")
	private long poId;

	@AnnonOfField(desc = "userId", policy = true)
	private long userId;

	@AnnonOfField(desc = "是否是赠品")
	private boolean isGift = false;

	@AnnonOfField(desc = "是否是直接和订单关联")
	private boolean isOrder = false;

	@AnnonOfField(desc = "sku快照(SkuSPDTO的JSON数据)", type = "text")
	private String skuSnapshot;
	
	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	public BigDecimal getRedSPrice() {
		return redSPrice;
	}

	public void setRedSPrice(BigDecimal redSPrice) {
		this.redSPrice = redSPrice;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderCartItemId() {
		return orderCartItemId;
	}

	public void setOrderCartItemId(long orderCartItemId) {
		this.orderCartItemId = orderCartItemId;
	}

	public boolean isOrder() {
		return isOrder;
	}

	public void setOrder(boolean isOrder) {
		this.isOrder = isOrder;
	}

	public String getSkuSnapshot() {
		return skuSnapshot;
	}

	public void setSkuSnapshot(String skuSnapshot) {
		this.skuSnapshot = skuSnapshot;
	}

	public BigDecimal getHdSPrice() {
		return hdSPrice;
	}

	public void setHdSPrice(BigDecimal hdSPrice) {
		this.hdSPrice = hdSPrice;
	}

	public BigDecimal getCouponSPrice() {
		return couponSPrice;
	}

	public void setCouponSPrice(BigDecimal couponSPrice) {
		this.couponSPrice = couponSPrice;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public boolean getIsGift() {
		return isGift();
	}

	public boolean isGift() {
		return isGift;
	}

	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}

	public void setIsGift(boolean isGift) {
		setGift(isGift);
	}

	public BigDecimal getRprice() {
		return rprice;
	}

	public void setRprice(BigDecimal rprice) {
		this.rprice = rprice;
	}

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
	}

	public int getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
