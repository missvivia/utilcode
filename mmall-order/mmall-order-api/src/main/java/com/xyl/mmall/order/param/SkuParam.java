package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * 组单使用的Sku信息(包含购买数量)
 * 
 * @author dingmingliang
 * 
 */
public class SkuParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * SkuId
	 */
	private long skuId;
	
	/**
	 * 销售价
	 */
	private BigDecimal salePrice;

	/**
	 * 活动优惠的差额(单位)
	 */
	private BigDecimal hdSPrice;

	/**
	 * 优惠券优惠的差额(单位)
	 */
	private BigDecimal couponSPrice;

	/**
	 * 红包优惠的差额(单位)
	 */
	private BigDecimal redSPrice = BigDecimal.ZERO;

	/**
	 * 购买个数
	 */
	private int count;

	/**
	 * 选择的促销的Id
	 */
	private Long promotionId;

	/**
	 * 选择的促销效果的Idx
	 */
	private int promotionIdx;

	// ---------------------------------

	/**
	 * 专柜价
	 */
	private BigDecimal marketPrice;

	/**
	 * 原始价格
	 */
	private BigDecimal oriRPrice;

	/**
	 * ProductId
	 */
	private long productId;

	/**
	 * 供应商Id
	 */
	private long supplierId;

	/**
	 * poId
	 */
	@Deprecated
	private long poId;
	
    /** 店铺订单起批金额 . */
    private BigDecimal        batchCash;

	/**
	 * 生成SkuParam(只包含skuId+count+salePrice+hdSPrice+couponSPrice字段)
	 * 
	 * @param skuId
	 * @param count
	 * @param salePrice
	 * @param hdSPrice
	 * @param couponSPrice
	 * @param redSPrice
	 * @return
	 */
	public static SkuParam genSkuParam(long skuId, int count, BigDecimal salePrice, BigDecimal hdSPrice,
			BigDecimal couponSPrice, BigDecimal redSPrice) {
		SkuParam obj = new SkuParam();
		obj.setSkuId(skuId);
		obj.setProductId(skuId);
		obj.setCount(count);
		obj.setSalePrice(salePrice);
		obj.setHdSPrice(hdSPrice);
		obj.setCouponSPrice(couponSPrice);
		obj.setRedSPrice(redSPrice);

		return obj;
	}

	public BigDecimal getRedSPrice() {
		return redSPrice;
	}

	public void setRedSPrice(BigDecimal redSPrice) {
		this.redSPrice = redSPrice;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public int getPromotionIdx() {
		return promotionIdx;
	}

	public void setPromotionIdx(int promotionIdx) {
		this.promotionIdx = promotionIdx;
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

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
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

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	
    public BigDecimal getBatchCash()
    {
        return batchCash;
    }
    
    public void setBatchCash(BigDecimal batchCash)
    {
        this.batchCash = batchCash;
    }
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}

	
    private	class SKUSpeci{
		
		/** 规格选项名. */
		private String speciOptionName;
		
		/** 规格名称. */
		private String specificationName;
		
		/** 规格显示类型. */
		private int type;

		public String getSpeciOptionName() {
			return speciOptionName;
		}

		public void setSpeciOptionName(String speciOptionName) {
			this.speciOptionName = speciOptionName;
		}

		public String getSpecificationName() {
			return specificationName;
		}

		public void setSpecificationName(String specificationName) {
			this.specificationName = specificationName;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
		
		
	}
	
	
}