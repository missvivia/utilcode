package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;


@AnnonOfClass(desc = "oms签收情况", tableName = "Mmall_Oms_Report_ReceiptDaily")
public class OmsReceiptDaily implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "仓库编号")
	private long warehouseId;
	
	@AnnonOfField(desc = "妥投数")
	private int total;
	
	@AnnonOfField(desc = "时间范围")
	private int type;
	
	@AnnonOfField(desc = "未签收,即未妥投")
	private int no_receipt;
	
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
	
	public long getWarehouseId() {
		return warehouseId;
	}
	
	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getNo_receipt() {
		return no_receipt;
	}
	
	public void setNo_receipt(int no_receipt) {
		this.no_receipt = no_receipt;
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
