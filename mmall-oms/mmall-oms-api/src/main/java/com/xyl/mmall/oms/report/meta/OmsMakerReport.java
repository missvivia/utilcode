package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "仓库生产统计报表", tableName = "Mmall_Oms_OmsMakerReport", policy = "warehouseId")
public class OmsMakerReport implements Serializable {

	private static final long serialVersionUID = 5527900335382130307L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "报表生成时间")
	private long makeTime;

	@AnnonOfField(desc = "仓储服务商,如EMS，顺丰", notNull = false, defa = "", type = "VARCHAR(64)")
	private String warehouseOwner;

	@AnnonOfField(desc = "仓储名称", notNull = false, defa = "", type = "VARCHAR(128)")
	private String warehouseName;

	@AnnonOfField(desc = "上期结存", notNull = false, defa = "0")
	private int preBalance;

	@AnnonOfField(desc = "已汇总订单", notNull = false, defa = "0")
	private int gatherCnt;

	@AnnonOfField(desc = "取消订单", notNull = false, defa = "0")
	private int cancelCnt;

	@AnnonOfField(desc = "已发货订单", notNull = false, defa = "0")
	private int sentCnt;

	@AnnonOfField(desc = "缺货订单", notNull = false, defa = "0")
	private int lackCnt;

	@AnnonOfField(desc = "延迟订单", notNull = false, defa = "0")
	private int delayCnt;

	@AnnonOfField(desc = "本期结存", notNull = false, defa = "0")
	private int curBalance;

	@AnnonOfField(desc = "延迟占比", notNull = false, defa = "0.00")
	private BigDecimal delayRate;

	@AnnonOfField(desc = "延迟订单id列表，逗号,分隔", notNull = false, type = "text")
	private String delayOrderIds;

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

	public long getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(long makeTime) {
		this.makeTime = makeTime;
	}

	public String getWarehouseOwner() {
		return warehouseOwner;
	}

	public void setWarehouseOwner(String warehouseOwner) {
		this.warehouseOwner = warehouseOwner;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getPreBalance() {
		return preBalance;
	}

	public void setPreBalance(int preBalance) {
		this.preBalance = preBalance;
	}

	public int getGatherCnt() {
		return gatherCnt;
	}

	public void setGatherCnt(int gatherCnt) {
		this.gatherCnt = gatherCnt;
	}

	public int getCancelCnt() {
		return cancelCnt;
	}

	public void setCancelCnt(int cancelCnt) {
		this.cancelCnt = cancelCnt;
	}

	public int getSentCnt() {
		return sentCnt;
	}

	public void setSentCnt(int sentCnt) {
		this.sentCnt = sentCnt;
	}

	public int getLackCnt() {
		return lackCnt;
	}

	public void setLackCnt(int lackCnt) {
		this.lackCnt = lackCnt;
	}

	public int getDelayCnt() {
		return delayCnt;
	}

	public void setDelayCnt(int delayCnt) {
		this.delayCnt = delayCnt;
	}

	public int getCurBalance() {
		return curBalance;
	}

	public void setCurBalance(int curBalance) {
		this.curBalance = curBalance;
	}

	public BigDecimal getDelayRate() {
		return delayRate;
	}

	public void setDelayRate(BigDecimal delayRate) {
		this.delayRate = delayRate;
	}

	public String getDelayOrderIds() {
		return delayOrderIds;
	}

	public void setDelayOrderIds(String delayOrderIds) {
		this.delayOrderIds = delayOrderIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (makeTime ^ (makeTime >>> 32));
		result = prime * result + (int) (warehouseId ^ (warehouseId >>> 32));
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
		OmsMakerReport other = (OmsMakerReport) obj;
		if (makeTime != other.makeTime)
			return false;
		if (warehouseId != other.warehouseId)
			return false;
		return true;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
