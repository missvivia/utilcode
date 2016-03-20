package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 
 * @author hzliujie 2014年12月11日 上午10:58:42
 */
@AnnonOfClass(desc = "oms发货报表", tableName = "Mmall_Oms_Report_SendOutReport")
public class OmsSendOutReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "快递公司", notNull = false)
	private String expressName;

	@AnnonOfField(desc = "仓库Id")
	private long warehouseId;

	@AnnonOfField(desc = "COD数量")
	private int cod;

	@AnnonOfField(desc = "非COD数量")
	private int noncode;

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

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public int getNoncode() {
		return noncode;
	}

	public void setNoncode(int noncode) {
		this.noncode = noncode;
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
