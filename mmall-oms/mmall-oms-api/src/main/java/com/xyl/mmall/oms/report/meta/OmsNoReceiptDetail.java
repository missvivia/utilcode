package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;

@AnnonOfClass(desc = "未收货明细", tableName = "Mmall_Oms_Report_NoReceiptDetail")
public class OmsNoReceiptDetail implements Serializable{

	private static final long serialVersionUID = 1357891510224726219L;

	@AnnonOfField(desc = "主键",primary = true, autoAllocateId=true)
	private long id;
	
	@AnnonOfField(desc = "仓库id")
	private long warehouseId;
	
	@AnnonOfField(desc = "运单号")
	private String mailNo;
	
	@AnnonOfField(desc = "拒收时间")
	private long packageStateUpdateTime;
	
	@AnnonOfField(desc = "联系人电话")
	private String consigneeTel;
	
	@AnnonOfField(desc = "地址")
	private String address;
	
	@AnnonOfField(desc = "未签收情况,0发货,20拒收,30丢失")
	private int omsOrderPackageState;
	
	@AnnonOfField(desc = "包裹id")
	private long packageId;
	
	@AnnonOfField(desc = "收货人")
	private String consigneeName;
	
	@AnnonOfField(desc = "日期")
	private long day;
	
	@AnnonOfField(desc = "订单号")
	private long OmsOrderFormId;
	
	@AnnonOfField(desc = "创建日期")
	private long createTime;
	
	@AnnonOfField(desc = "更新日期")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public long getPackageStateUpdateTime() {
		return packageStateUpdateTime;
	}

	public void setPackageStateUpdateTime(long packageStateUpdateTime) {
		this.packageStateUpdateTime = packageStateUpdateTime;
	}

	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public long getDay() {
		return day;
	}

	public void setDay(long day) {
		this.day = day;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getOmsOrderFormId() {
		return OmsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		OmsOrderFormId = omsOrderFormId;
	}

	public int getOmsOrderPackageState() {
		return omsOrderPackageState;
	}

	public void setOmsOrderPackageState(int omsOrderPackageState) {
		this.omsOrderPackageState = omsOrderPackageState;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}
	
	
	
	
}
