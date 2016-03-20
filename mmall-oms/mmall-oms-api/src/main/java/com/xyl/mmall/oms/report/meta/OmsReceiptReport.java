package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "各省订单签收状态统计", tableName = "Mmall_Oms_Report_ReceiptForm")
public class OmsReceiptReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "快递公司", notNull = false)
	private String express;

	@AnnonOfField(desc = "仓库编号")
	private long warehouseId;

	@AnnonOfField(desc = "仓库名称", notNull = false)
	private String warehouse;

	@AnnonOfField(desc = "发货量")
	private int total;

	@AnnonOfField(desc = "24H发货量")
	private int total_24;

	@AnnonOfField(desc = "24H妥投率", notNull = false, defa = "0.0")
	private BigDecimal rate_24;

	@AnnonOfField(desc = "48H发货量")
	private int total_48;

	@AnnonOfField(desc = "48H妥投率", notNull = false, defa = "0.0")
	private BigDecimal rate_48;

	@AnnonOfField(desc = "72H发货量")
	private int total_72;

	@AnnonOfField(desc = "72H妥投率", notNull = false, defa = "0.0")
	private BigDecimal rate_72;

	@AnnonOfField(desc = ">72H发货量")
	private int total_after_72;

	@AnnonOfField(desc = ">72H妥投率", notNull = false, defa = "0.0")
	private BigDecimal rate_after_72;

	@AnnonOfField(desc = "未签收")
	private int no_receipt;

	@AnnonOfField(desc = "退单率", notNull = false)
	private BigDecimal returnOrder;

	@AnnonOfField(desc = "日期")
	private long date;

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

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal_24() {
		return total_24;
	}

	public void setTotal_24(int total_24) {
		this.total_24 = total_24;
	}

	public BigDecimal getRate_24() {
		if (rate_24 != null)
			rate_24 = rate_24.setScale(4, RoundingMode.HALF_UP);
		return rate_24;
	}

	public void setRate_24(BigDecimal rate_24) {
		this.rate_24 = rate_24;
	}

	public int getTotal_48() {
		return total_48;
	}

	public void setTotal_48(int total_48) {
		this.total_48 = total_48;
	}

	public BigDecimal getRate_48() {
		if (rate_48 != null)
			rate_48 = rate_48.setScale(4, RoundingMode.HALF_UP);
		return rate_48;
	}

	public void setRate_48(BigDecimal rate_48) {
		this.rate_48 = rate_48;
	}

	public int getTotal_72() {
		return total_72;
	}

	public void setTotal_72(int total_72) {
		this.total_72 = total_72;
	}

	public BigDecimal getRate_72() {
		if (rate_48 != null)
			rate_48 = rate_48.setScale(4, RoundingMode.HALF_UP);
		return rate_72;
	}

	public void setRate_72(BigDecimal rate_72) {
		this.rate_72 = rate_72;
	}

	public int getTotal_after_72() {
		return total_after_72;
	}

	public void setTotal_after_72(int total_after_72) {
		this.total_after_72 = total_after_72;
	}

	public BigDecimal getRate_after_72() {
		if (rate_48 != null)
			rate_48 = rate_48.setScale(4, RoundingMode.HALF_UP);
		return rate_after_72;
	}

	public void setRate_after_72(BigDecimal rate_after_72) {
		this.rate_after_72 = rate_after_72;
	}

	public int getNo_receipt() {
		return no_receipt;
	}

	public void setNo_receipt(int no_receipt) {
		this.no_receipt = no_receipt;
	}

	public BigDecimal getReturnOrder() {
		return returnOrder;
	}

	public void setReturnOrder(BigDecimal returnOrder) {
		this.returnOrder = returnOrder;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
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

}
