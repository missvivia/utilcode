package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoSku;

/**
 * 添加到档期的skuDTO，档期清单审核
 * 
 * @author hzhuangluqian
 *
 */
public class POSkuDTO extends BaseSkuDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 926103523620587708L;

	private long poId;

	/** 提交审核的时间 */
	private long submitTime;

	/** 商品状态 */
	private StatusType status;

	/** 审核理由 */
	private String rejectReason;

	/** 提交的sku数量 */
	private int skuNum;

	/** 品牌商提供的sku数量 */
	@AnnonOfField(desc = "品牌商提供的sku数量")
	private int supplierSkuNum;

	private int cartStock;

	private int orderStock;

	/**
	 * 产品链接
	 */
	private String productLinkUrl;

	/** 状态 */
	private String statusName;
	
	public POSkuDTO(){
		
	}

	public POSkuDTO(PoSku obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
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

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProductLinkUrl() {
		return productLinkUrl;
	}

	public void setProductLinkUrl(String productLinkUrl) {
		this.productLinkUrl = productLinkUrl;
	}

	public int getCartStock() {
		return cartStock;
	}

	public void setCartStock(int cartStock) {
		this.cartStock = cartStock;
	}

	public int getOrderStock() {
		return orderStock;
	}

	public void setOrderStock(int orderStock) {
		this.orderStock = orderStock;
	}

	public int getSupplierSkuNum() {
		return supplierSkuNum;
	}

	public void setSupplierSkuNum(int supplierSkuNum) {
		this.supplierSkuNum = supplierSkuNum;
	}

	@Override
	public int hashCode() {
		int elm1 = Long.valueOf(poId).hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = barCode.hashCode();
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
		POSkuDTO other = (POSkuDTO) obj;
		if (poId != other.getPoId())
			return false;
		if (!barCode.equals(other.getBarCode()))
			return false;
		return true;
	}
}
