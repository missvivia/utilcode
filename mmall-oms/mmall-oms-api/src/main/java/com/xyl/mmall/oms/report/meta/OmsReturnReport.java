package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "返货订单报表", tableName = "Mmall_Oms_Report_ReturnReport")
public class OmsReturnReport implements Serializable {

	private static final long serialVersionUID = -1931628174231776998L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "发货日期")
	private long date;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "快递公司名称")
	private String expressCompany;

	@AnnonOfField(desc = "仓库名称")
	private String warehouseName;

	@AnnonOfField(desc = "返货单量")
	private int returnProduct;

	@AnnonOfField(desc = "返回单量")
	private int returnCount;

	@AnnonOfField(desc = "返回占比", notNull = false)
	private BigDecimal returnRate;

	@AnnonOfField(desc = "未返回单量")
	private int noreturnCount;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long modifyTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getReturnProduct() {
		return returnProduct;
	}

	public void setReturnProduct(int returnProduct) {
		this.returnProduct = returnProduct;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	public BigDecimal getReturnRate() {
		if (returnRate != null)
			returnRate = returnRate.setScale(4, RoundingMode.HALF_UP);
		return returnRate;
	}

	public void setReturnRate(BigDecimal returnRate) {
		this.returnRate = returnRate;
	}

	public int getNoreturnCount() {
		return noreturnCount;
	}

	public void setNoreturnCount(int noreturnCount) {
		this.noreturnCount = noreturnCount;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}
}
