/**
 * 
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 运费模板
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "Oms 运费模板", tableName = "Mmall_Oms_FreightTemplet")
public class FreightTemplet implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "模板id", primary = true, autoAllocateId = true)
	private String id;

	@AnnonOfField(desc = "模板描述")
	private String descInfo;

	@AnnonOfField(desc = "快递类别，一般情况下仓库和快递公司是同一家")
	private String expressCompany;

	@AnnonOfField(desc = "仓库公司类别")
	private String warehouseType;

	@AnnonOfField(desc = "仓库始发地")
	private String origin;
	
	@AnnonOfField(desc = "始发地Id")
	private long originId;

	@AnnonOfField(desc = "目的地")
	private String target;
	
	@AnnonOfField(desc = "目的地Id")
	private long targetId;

	@AnnonOfField(desc = "配送类型，如：市内配送、跨省配送、邻省配送等")
	private String deliverType;

	@AnnonOfField(desc = "首重，单位KG")
	private int startWeight;

	@AnnonOfField(desc = "首重费用")
	private BigDecimal startCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "每次续重的最小重量，单位KG")
	private int continueWeight;

	@AnnonOfField(desc = "续重费用")
	private BigDecimal continueCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "货到付款服务费率。如： 0.01 = 1.0%; 0.001 = 1.0‰")
	private BigDecimal rateCod = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向运费费率，指在未妥投的情况下，包裹返仓的运费费率。")
	private BigDecimal rateReverse = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向服务费，一般为固定的值，按单收取")
	private BigDecimal reverseServiceCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "是否保价")
	private boolean isInsurance;

	@AnnonOfField(desc = "保价率")
	private BigDecimal rateInsurance = BigDecimal.ZERO;

	@AnnonOfField(desc = "分拣包装费")
	private BigDecimal pickPackageCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "耗材费")
	private BigDecimal consumablesCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "拦截费")
	private BigDecimal interceptOrderCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "是否是一口价")
	private boolean isOnePrice;

	@AnnonOfField(desc = "一口价金额")
	private BigDecimal onePrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "丢件赔偿额")
	private BigDecimal lostCompensate = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "最低cod收费")
	private BigDecimal codMinCharge = BigDecimal.ZERO;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescInfo() {
		return descInfo;
	}

	public void setDescInfo(String descInfo) {
		this.descInfo = descInfo;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public long getOriginId() {
		return originId;
	}

	public void setOriginId(long originId) {
		this.originId = originId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}

	public int getStartWeight() {
		return startWeight;
	}

	public void setStartWeight(int startWeight) {
		this.startWeight = startWeight;
	}

	public BigDecimal getStartCost() {
		return startCost;
	}

	public void setStartCost(BigDecimal startCost) {
		this.startCost = startCost;
	}

	public int getContinueWeight() {
		return continueWeight;
	}

	public void setContinueWeight(int continueWeight) {
		this.continueWeight = continueWeight;
	}

	public BigDecimal getContinueCost() {
		return continueCost;
	}

	public void setContinueCost(BigDecimal continueCost) {
		this.continueCost = continueCost;
	}

	public BigDecimal getRateCod() {
		return rateCod;
	}

	public void setRateCod(BigDecimal rateCod) {
		this.rateCod = rateCod;
	}

	public BigDecimal getRateReverse() {
		return rateReverse;
	}

	public void setRateReverse(BigDecimal rateReverse) {
		this.rateReverse = rateReverse;
	}

	public BigDecimal getReverseServiceCharge() {
		return reverseServiceCharge;
	}

	public void setReverseServiceCharge(BigDecimal reverseServiceCharge) {
		this.reverseServiceCharge = reverseServiceCharge;
	}

	public boolean isInsurance() {
		return isInsurance;
	}

	public void setInsurance(boolean isInsurance) {
		this.isInsurance = isInsurance;
	}

	public BigDecimal getRateInsurance() {
		return rateInsurance;
	}

	public void setRateInsurance(BigDecimal rateInsurance) {
		this.rateInsurance = rateInsurance;
	}

	public BigDecimal getPickPackageCost() {
		return pickPackageCost;
	}

	public void setPickPackageCost(BigDecimal pickPackageCost) {
		this.pickPackageCost = pickPackageCost;
	}

	public BigDecimal getConsumablesCost() {
		return consumablesCost;
	}

	public void setConsumablesCost(BigDecimal consumablesCost) {
		this.consumablesCost = consumablesCost;
	}

	public BigDecimal getInterceptOrderCost() {
		return interceptOrderCost;
	}

	public void setInterceptOrderCost(BigDecimal interceptOrderCost) {
		this.interceptOrderCost = interceptOrderCost;
	}

	public boolean isOnePrice() {
		return isOnePrice;
	}

	public void setOnePrice(boolean isOnePrice) {
		this.isOnePrice = isOnePrice;
	}

	public BigDecimal getOnePrice() {
		return onePrice;
	}

	public void setOnePrice(BigDecimal onePrice) {
		this.onePrice = onePrice;
	}

	public BigDecimal getLostCompensate() {
		return lostCompensate;
	}

	public void setLostCompensate(BigDecimal lostCompensate) {
		this.lostCompensate = lostCompensate;
	}

	public BigDecimal getCodMinCharge() {
		return codMinCharge;
	}

	public void setCodMinCharge(BigDecimal codMinCharge) {
		this.codMinCharge = codMinCharge;
	}
	
}
