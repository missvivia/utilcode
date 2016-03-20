package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.saleschedule.enums.BrandStatus;

/**
 * 供应商的品牌meta
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "供应商的品牌表", tableName = "Mmall_SaleSchedule_SupplierBrand")
public class SupplierBrand implements Serializable {
	
	private static final long serialVersionUID = 20140911L;
	
	@AnnonOfField(desc = "供应商的品牌表主键id", primary = true, autoAllocateId = true)
	private long supplierBrandId;
	
	@AnnonOfField(desc = "Mmall_ItemCenter_Brand的品牌id")
	private long brandId;
	
	@AnnonOfField(desc = "供应商标识", policy = true)
	private long supplierId;
	
	@AnnonOfField(desc = "该品牌对应的供应商是否被冻结")
	private boolean supplierIsFreeze = false;
	
	@AnnonOfField(desc = "品牌介绍1", type = "VARCHAR(300)")
	private String brandDesc1;
	
	@AnnonOfField(desc = "品牌介绍2", type = "VARCHAR(90)")
	private String brandDesc2;
	
	@AnnonOfField(desc = "编辑人", type = "VARCHAR(100)")
	private String editor;
	
	@AnnonOfField(desc = "品牌状态")
	private BrandStatus status;
	
	// 现在一个品牌可以对应多个供应商，这个字段就没有意义了
//	@AnnonOfField(desc = "品牌创建的供应商所属的区域id")
//	private long areaId;
	
	@AnnonOfField(desc = "品牌创建的供应商所属的区域的特殊二进制编码")
	private long areaFormatCode;
	
	@AnnonOfField(desc = "状态更新时间")
	private long statusUpdateDate;
	
	@AnnonOfField(desc = "拒绝的理由，在品牌审核拒绝的时候有效", notNull = false, type = "VARCHAR(255)")
	private String rejectReason;

	public long getBrandId() {
		return brandId;
	}

	public long getSupplierBrandId() {
		return supplierBrandId;
	}

	public void setSupplierBrandId(long supplierBrandId) {
		this.supplierBrandId = supplierBrandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getBrandDesc1() {
		return brandDesc1;
	}

	public void setBrandDesc1(String brandDesc1) {
		this.brandDesc1 = brandDesc1;
	}

	public String getBrandDesc2() {
		return brandDesc2;
	}

	public void setBrandDesc2(String brandDesc2) {
		this.brandDesc2 = brandDesc2;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public BrandStatus getStatus() {
		return status;
	}

	public void setStatus(BrandStatus status) {
		this.status = status;
	}

	public long getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(long statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public long getAreaFormatCode() {
		return areaFormatCode;
	}

	public void setAreaFormatCode(long areaFormatCode) {
		this.areaFormatCode = areaFormatCode;
	}

	public boolean isSupplierIsFreeze() {
		return supplierIsFreeze;
	}

	public void setSupplierIsFreeze(boolean supplierIsFreeze) {
		this.supplierIsFreeze = supplierIsFreeze;
	}

//	public long getAreaId() {
//		return areaId;
//	}
//
//	public void setAreaId(long areaId) {
//		this.areaId = areaId;
//	}
	
}
