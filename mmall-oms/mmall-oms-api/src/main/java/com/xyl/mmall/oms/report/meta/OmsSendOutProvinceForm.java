package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 
 * @author hzliujie 2014年12月11日 上午10:58:42
 */
@AnnonOfClass(desc = "oms发货报表", tableName = "Mmall_Oms_Report_SendOutProvinceForm")
public class OmsSendOutProvinceForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "发货时间")
	private long date;

	@AnnonOfField(desc = "快递公司", type = "varchar(255)")
	private String expressName;

	@AnnonOfField(desc = "仓库Id")
	private long warehouseId;

	@AnnonOfField(desc = "仓库名字", type = "varchar(255)")
	private String warehouse;

	@AnnonOfField(desc = "发货数量")
	private int num;

	@AnnonOfField(desc = "COD占比")
	private BigDecimal codRate;

	@AnnonOfField(desc = "仓库占比")
	private BigDecimal warehouseRate;

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

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public BigDecimal getCodRate() {
		if (codRate != null)
			codRate = codRate.setScale(4, RoundingMode.HALF_UP);
		return codRate;
	}

	public void setCodRate(BigDecimal codRate) {
		this.codRate = codRate;
	}

	public BigDecimal getWarehouseRate() {
		if (warehouseRate != null)
			warehouseRate = warehouseRate.setScale(4, RoundingMode.HALF_UP);
		return warehouseRate;
	}

	public void setWarehouseRate(BigDecimal warehouseRate) {
		this.warehouseRate = warehouseRate;
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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

}
