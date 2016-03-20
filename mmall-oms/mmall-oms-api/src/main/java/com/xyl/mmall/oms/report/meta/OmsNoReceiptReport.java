package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "未签收报表", tableName = "Mmall_Oms_Report_NoReceiptReport")
public class OmsNoReceiptReport implements Serializable {

	private static final long serialVersionUID = 1837194590684732116L;

	@AnnonOfField(desc = "未签收报表", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "发货日期")
	private long date;
	
	@AnnonOfField(desc = "快递公司名称")
	private String expressCompany;
	
	@AnnonOfField(desc = "仓库名称")
	private String warehouseName;
	
	@AnnonOfField(desc = "未签收单量合计")
	private int count;
	
	@AnnonOfField(desc = "拒收单量合计")
	private int reject;
	
	@AnnonOfField(desc = "其他")
	private int other;
	
	@AnnonOfField(desc = "仓库id")
	private long warehouseId;
	
	@AnnonOfField(desc = "创建时间")
	private long createTime;
	
	@AnnonOfField(desc = "更新时间")
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getReject() {
		return reject;
	}

	public void setReject(int reject) {
		this.reject = reject;
	}

	public int getOther() {
		return other;
	}

	public void setOther(int other) {
		this.other = other;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	
	
}
