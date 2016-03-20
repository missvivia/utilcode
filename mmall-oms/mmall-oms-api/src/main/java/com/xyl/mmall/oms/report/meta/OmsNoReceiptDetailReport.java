package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;


@AnnonOfClass(desc = "未收获明细报表", tableName = "Mmall_Oms_Report_NoReceiptDetailReport")
public class OmsNoReceiptDetailReport implements Serializable {

	private static final long serialVersionUID = 2134260538995090342L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "发货日期")
	private long date;

	@AnnonOfField(desc = "快递公司名称")
	private String expressCompany;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "包裹id")
	private long packageId;

	@AnnonOfField(desc = "仓库名称")
	private String warehouseName;

	@AnnonOfField(desc = "订单号")
	private long omsOrderFormId;

	@AnnonOfField(desc = "正向运单号", notNull = false)
	private String mailNO;

	@AnnonOfField(desc = "反向运单号", notNull = false)
	private String mailNOReturn;

	@AnnonOfField(desc = "拒收时间", notNull = false)
	private long rejectTime;

	@AnnonOfField(desc = "拒收原因", notNull = false)
	private String rejectReason;

	@AnnonOfField(desc = "收货人", notNull = false)
	private String consigneeName;

	@AnnonOfField(desc = "联系方式", notNull = false)
	private String consigneeMobile;

	@AnnonOfField(desc = "联系人地址", notNull = false)
	private String consigneeAddress;

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

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public String getMailNOReturn() {
		return mailNOReturn;
	}

	public void setMailNOReturn(String mailNOReturn) {
		this.mailNOReturn = mailNOReturn;
	}

	public long getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(long rejectTime) {
		this.rejectTime = rejectTime;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
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

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

}
