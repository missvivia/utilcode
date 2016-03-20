package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;

/**
 * 供应商档期首付款销售明细.
 * 
 * @author wangfeng
 *
 */
public class FinanceFirstPayOrderVO implements Serializable {

	private static final long serialVersionUID = 7121629041945825158L;

	private long poId;

	private long supplierId;

	/** 公司名称. */
	private String supplierName;

	/** 品牌名称. */
	private String brandName;

	private String startDate;

	private String endDate;

	private long orderId;

	private long skuId;

	/** 条形码. */
	protected String barCode = "";

	/** 货号. */
	protected String goodsNo = "";

	/** 伟大的策划要求excel里的销售价(零售价)=数据库表里的原始价格/闪购价(单价). */
	private BigDecimal oriRPrice;

	/** 总数量. */
	private int totalCount;

	/** 销售总额=最终零售价格*总数量 */
	private BigDecimal totalRPrice;

	public FinanceFirstPayOrderVO() {
		super();
	}

	public FinanceFirstPayOrderVO(OrderSkuDTO orderSkuDTO, FinanceFirstPayConfirmVO financeFirstPayConfirmVO,
			POSkuDTO poSkuDTO) {
		super();
		// 订单商品相关信息.
		this.orderId = orderSkuDTO.getOrderId();
		this.skuId = orderSkuDTO.getSkuId();
		SkuSPDTO skuSPDTO = orderSkuDTO.getSkuSPDTO();
		if (skuSPDTO != null)
			this.brandName = skuSPDTO.getBrandName();
		this.oriRPrice = orderSkuDTO.getOriRPrice();
		this.totalCount = orderSkuDTO.getTotalCount();
		this.totalRPrice = BigDecimal.valueOf(totalCount).multiply(oriRPrice);
		// 档期相关信息.
		this.poId = financeFirstPayConfirmVO.getPoId();
		this.supplierId = financeFirstPayConfirmVO.getSupplierId();
		this.supplierName = financeFirstPayConfirmVO.getSupplierName();
		this.startDate = financeFirstPayConfirmVO.getStartDate();
		this.endDate = financeFirstPayConfirmVO.getEndDate();
		// 条形码+货号.
		if (poSkuDTO != null) {
			this.barCode = poSkuDTO.getBarCode();
			this.goodsNo = poSkuDTO.getGoodsNo();
		}
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public BigDecimal getTotalRPrice() {
		return totalRPrice;
	}

	public void setTotalRPrice(BigDecimal totalRPrice) {
		this.totalRPrice = totalRPrice;
	}

}
