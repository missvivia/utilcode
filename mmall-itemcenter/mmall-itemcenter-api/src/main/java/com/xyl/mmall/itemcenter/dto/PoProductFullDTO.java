package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoProductDetail;

public class PoProductFullDTO extends ProductFullDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5032641176380916165L;

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

	/** 缩略图地址 */
	private String showPicPath;

	/** 库存 */
	private int stock;

	/** 销量 */
	private int saleTotal;

	/** 折扣 */
	private BigDecimal discount;

	/** 商品的修改时间 */
	private long submitTime;

	/** 商品的添加时间 */
	@AnnonOfField(desc = "商品的添加时间")
	private long addTime;

	/** 商品的修改时间 */
	@AnnonOfField(desc = "商品的修改时间")
	private long uTime;

	public PoProductFullDTO(PoProduct obj, PoProductDetail obj2) {
		ReflectUtil.convertObj(this, obj2, false);
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public PoProductFullDTO(ProductFullDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
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

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
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

}
