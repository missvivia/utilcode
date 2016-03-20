package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoProduct;

/**
 * 已经添加到po中的商品dto，增加缩略图地址和类目名。
 * 
 * @author hzhuangluqian
 *
 */
public class POProductDTO extends ProductDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6270438743959608982L;

	private long productId;

	private long poId;

	/** 商品状态 */
	private StatusType status;

	/** 审核理由 */
	private String rejectReason;

	/** 审核描述 */
	private String rejectDescp;

	/** 单个手动排序 */
	private int singleIndex;

	/** 按类目排序 */
	private int categoryIndex;

	/** 库存 */
	private int stock;

	/** 库存 */
	private int cartStock;

	/** 销量 */
	private int saleTotal;

	/** 折扣 */
	private BigDecimal discount;

	/** 商品的修改时间 */
	private long submitTime;

	/** 商品的添加时间 */
	private long addTime;

	/** 商品的修改时间 */
	private long uTime;

	private int sameAsShop;

	public POProductDTO(PoProduct obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getSaleTotal() {
		return saleTotal;
	}

	public void setSaleTotal(int saleTotal) {
		this.saleTotal = saleTotal;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectDescp() {
		return rejectDescp;
	}

	public void setRejectDescp(String rejectDescp) {
		this.rejectDescp = rejectDescp;
	}

	public int getSingleIndex() {
		return singleIndex;
	}

	public void setSingleIndex(int singleIndex) {
		this.singleIndex = singleIndex;
	}

	public int getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public long getuTime() {
		return uTime;
	}

	public void setuTime(long uTime) {
		this.uTime = uTime;
	}

	public long getUTime() {
		return uTime;
	}

	public void setUTime(long uTime) {
		this.uTime = uTime;
	}

	public int getCartStock() {
		return cartStock;
	}

	public void setCartStock(int cartStock) {
		this.cartStock = cartStock;
	}

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	@Override
	public int hashCode() {
		int elm1 = goodsNo.hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = colorNum.hashCode();
		result = 31 * result + elm2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		POProductDTO other = (POProductDTO) obj;
		if (!goodsNo.equals(other.getGoodsNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		return true;
	}
}
