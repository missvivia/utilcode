package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;

/**
 * 财务-包裹下商品.
 * 
 * @author wangfeng
 *
 */
public class FinanceOrderSkuVO implements Serializable {

	private static final long serialVersionUID = 9017122947922214887L;

	private long productId;

	/** 商品名称. */
	private String productName;

	/** PO单编号. */
	private long poId;

	/** 商品类目. */
	private String categoryName = "";

	/** 原始价格(单位). */
	private BigDecimal oriRPrice;

	/** 商品销售总价(零售总价). */
	private BigDecimal totalOriRPrice;

	/** 最终零售价格(单位). */
	private BigDecimal rprice;

	private BigDecimal totalRprice;

	/** 活动+优惠券+红包优惠金额(单位). */
	private BigDecimal yhPrice;

	/** 优惠总金额. */
	private BigDecimal yhTotalPrice;

	/** 购买数量. */
	private int totalCount;

	/** po开始时间. */
	private String poStartDate;

	/** po结束时间. */
	private String poEndDate;

	public FinanceOrderSkuVO() {
		super();
	}

	public FinanceOrderSkuVO(OrderSkuDTO orderSkuDTO) {
		super();
		this.productId = orderSkuDTO.getProductId();
		SkuSPDTO skuSPDTO = orderSkuDTO.getSkuSPDTO();
		if (skuSPDTO != null)
			this.productName = skuSPDTO.getProductName();
		this.poId = orderSkuDTO.getPoId();
		this.oriRPrice = orderSkuDTO.getOriRPrice();
		this.totalCount = orderSkuDTO.getTotalCount();
		this.totalOriRPrice = BigDecimal.valueOf(totalCount).multiply(oriRPrice);
		this.rprice = orderSkuDTO.getRprice();
		this.totalRprice = BigDecimal.valueOf(totalCount).multiply(rprice);
		this.yhPrice = orderSkuDTO.getCouponSPrice().add(orderSkuDTO.getHdSPrice()).add(orderSkuDTO.getRedSPrice());
		this.yhTotalPrice = BigDecimal.valueOf(totalCount).multiply(yhPrice);
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
	}

	public BigDecimal getTotalOriRPrice() {
		return totalOriRPrice;
	}

	public void setTotalOriRPrice(BigDecimal totalOriRPrice) {
		this.totalOriRPrice = totalOriRPrice;
	}

	public BigDecimal getRprice() {
		return rprice;
	}

	public void setRprice(BigDecimal rprice) {
		this.rprice = rprice;
	}

	public BigDecimal getTotalRprice() {
		return totalRprice;
	}

	public void setTotalRprice(BigDecimal totalRprice) {
		this.totalRprice = totalRprice;
	}

	public BigDecimal getYhPrice() {
		return yhPrice;
	}

	public void setYhPrice(BigDecimal yhPrice) {
		this.yhPrice = yhPrice;
	}

	public BigDecimal getYhTotalPrice() {
		return yhTotalPrice;
	}

	public void setYhTotalPrice(BigDecimal yhTotalPrice) {
		this.yhTotalPrice = yhTotalPrice;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getPoStartDate() {
		return poStartDate;
	}

	public void setPoStartDate(String poStartDate) {
		this.poStartDate = poStartDate;
	}

	public String getPoEndDate() {
		return poEndDate;
	}

	public void setPoEndDate(String poEndDate) {
		this.poEndDate = poEndDate;
	}

}
