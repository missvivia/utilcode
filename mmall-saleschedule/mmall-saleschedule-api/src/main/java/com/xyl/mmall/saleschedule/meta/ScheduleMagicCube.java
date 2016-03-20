package com.xyl.mmall.saleschedule.meta;

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
@AnnonOfClass(desc = "档期魔方数据备份表", tableName = "Mmall_SaleSchedule_ScheduleMagicCube", policy = "supplierId")
public class ScheduleMagicCube implements Serializable {

	private static final long serialVersionUID = 2430238238016774657L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "档期id", notNull = false)
	private long scheduleId;

	@AnnonOfField(desc = "站点id", notNull = false)
	private long curSupplierAreaId;

	@AnnonOfField(desc = "供应商id")
	private long supplierId;

	@AnnonOfField(desc = "统计日期")
	private long logDay;

	@AnnonOfField(desc = "销售额", notNull = false, type="numeric(15,2)")
	private BigDecimal sale;

	@AnnonOfField(desc = "销售量", notNull = false)
	private long saleCnt;

	@AnnonOfField(desc = "购买人数", notNull = false)
	private long buyerCnt;

	@AnnonOfField(desc = "售卖比", notNull = false)
	private BigDecimal saleRate;

	@AnnonOfField(desc = "供货值", notNull = false, type="numeric(15,2)")
	private BigDecimal supplyMoney;

	@AnnonOfField(desc = "SKU数", notNull = false)
	private int skuCnt;

	@AnnonOfField(desc = "UV", notNull = false)
	private long uv;

	@AnnonOfField(desc = "PV", notNull = false)
	private long pv;

	@AnnonOfField(desc = "预留字段1", notNull = false)
	private String rsv1;

	@AnnonOfField(desc = "预留字段2", notNull = false)
	private String rsv2;

	@AnnonOfField(desc = "预留字段3", notNull = false)
	private String rsv3;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getCurSupplierAreaId() {
		return curSupplierAreaId;
	}

	public void setCurSupplierAreaId(long curSupplierAreaId) {
		this.curSupplierAreaId = curSupplierAreaId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getLogDay() {
		return logDay;
	}

	public void setLogDay(long logDay) {
		this.logDay = logDay;
	}

	public BigDecimal getSale() {
		return sale;
	}

	public void setSale(BigDecimal sale) {
		this.sale = sale;
	}

	public long getSaleCnt() {
		return saleCnt;
	}

	public void setSaleCnt(long saleCnt) {
		this.saleCnt = saleCnt;
	}

	public long getBuyerCnt() {
		return buyerCnt;
	}

	public void setBuyerCnt(long buyerCnt) {
		this.buyerCnt = buyerCnt;
	}

	public BigDecimal getSaleRate() {
		return saleRate;
	}

	public void setSaleRate(BigDecimal saleRate) {
		this.saleRate = saleRate;
	}

	public BigDecimal getSupplyMoney() {
		return supplyMoney;
	}

	public void setSupplyMoney(BigDecimal supplyMoney) {
		this.supplyMoney = supplyMoney;
	}

	public int getSkuCnt() {
		return skuCnt;
	}

	public void setSkuCnt(int skuCnt) {
		this.skuCnt = skuCnt;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public String getRsv1() {
		return rsv1;
	}

	public void setRsv1(String rsv1) {
		this.rsv1 = rsv1;
	}

	public String getRsv2() {
		return rsv2;
	}

	public void setRsv2(String rsv2) {
		this.rsv2 = rsv2;
	}

	public String getRsv3() {
		return rsv3;
	}

	public void setRsv3(String rsv3) {
		this.rsv3 = rsv3;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}

}
