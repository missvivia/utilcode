package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.enums.WMSExpressCompany;

/**
 * 运费相关基类，定义一些基础属性项
 * 
 * @author hzzengchengyuan
 *
 */
public class FreightBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "结算快递公司")
	private String settleExpressCompany;
	
	@AnnonOfField(desc = "快递公司")
	private String expressCompany;

	@AnnonOfField(desc = "快递单号")
	private String expressNo;
	
	@AnnonOfField(desc = "仓库ID")
	private long warehouseId;
	
	@AnnonOfField(desc = "仓库名称")
	private String warehouseName;
	
	@AnnonOfField(desc = "仓库类别")
	private String warehouseType;
	
	@AnnonOfField(desc = "包裹状态")
	private OmsOrderPackageState packageState;
	
	@AnnonOfField(desc = "状态更新时间")
	private long stateUpdateTime;
	
	@AnnonOfField(desc = "订单发货时间")
	private long shipTime;

	@AnnonOfField(desc = "oms订单号")
	private long omsOrderFormId;
	
	@AnnonOfField(desc = "用户订单号")
	private long userOrderFormId;

	@AnnonOfField(desc = "订单金额,使用优惠卷之前的价格")
	private BigDecimal orderAmount = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "用户实付订单金额")
	private BigDecimal userPayAmount = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "货到付款金额")
	private BigDecimal codAmount = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-用户支付价")
	private BigDecimal expUserPrice = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "订单配送地址")
	private String deliverAddress;

	@AnnonOfField(desc = "配送类型")
	private String deliverType;

	@AnnonOfField(desc = "分拣包装费")
	private BigDecimal pickPackageCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "耗材费")
	private BigDecimal consumablesCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "拦截费")
	private BigDecimal interceptOrderCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "包裹重量，单位g")
	private BigDecimal weight = BigDecimal.ZERO;

	@AnnonOfField(desc = "首重费用")
	private BigDecimal startCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "续重费用")
	private BigDecimal continueCost = BigDecimal.ZERO;

	@AnnonOfField(desc = "是否是货到付款")
	private boolean isCode;

	@AnnonOfField(desc = "货到付款服务费率。如： 0.01 = 1.0%; 0.001 = 1.0‰")
	private BigDecimal codRate = BigDecimal.ZERO;

	@AnnonOfField(desc = "是否保价")
	private boolean isInsurance;

	@AnnonOfField(desc = "保价率")
	private BigDecimal insuranceRate = BigDecimal.ZERO;

	@AnnonOfField(desc = "保价手续费")
	private BigDecimal insuranceCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "是否是一口价")
	private boolean isOnePrice;

	@AnnonOfField(desc = "一口价")
	private BigDecimal onePrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "丢件赔偿额")
	private BigDecimal lostCompensate = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public FreightBase() {
		setCreateTime(System.currentTimeMillis());
	}
	
	public long getId() {
		return id;
	}
	
	public String getSettleExpressCompany() {
		return settleExpressCompany;
	}

	public void setSettleExpressCompany(String settleExpressCompany) {
		this.settleExpressCompany = settleExpressCompany;
	}
	
	public String getExpressCompany() {
		return expressCompany;
	}
	
	public WMSExpressCompany getExpressCompanyEnum() {
		return WMSExpressCompany.genEnumNameIgnoreCase(getExpressCompany());
	}

	public String getExpressNo() {
		return expressNo;
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
	
	public String getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}

	public OmsOrderPackageState getPackageState() {
		return packageState;
	}

	public void setPackageState(OmsOrderPackageState packageState) {
		this.packageState = packageState;
		setStateUpdateTime(System.currentTimeMillis());
	}

	public long getStateUpdateTime() {
		return stateUpdateTime;
	}

	public void setStateUpdateTime(long stateUpdateTime) {
		this.stateUpdateTime = stateUpdateTime;
	}

	public long getShipTime() {
		return shipTime;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public long getUserOrderFormId() {
		return userOrderFormId;
	}

	public void setUserOrderFormId(long userOrderFormId) {
		this.userOrderFormId = userOrderFormId;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public BigDecimal getUserPayAmount() {
		return userPayAmount;
	}

	public void setUserPayAmount(BigDecimal userPayAmount) {
		this.userPayAmount = userPayAmount;
	}

	public String getDeliverAddress() {
		return deliverAddress;
	}

	public String getDeliverType() {
		return deliverType;
	}

	public BigDecimal getPickPackageCost() {
		return pickPackageCost;
	}

	public BigDecimal getConsumablesCost() {
		return consumablesCost;
	}

	public BigDecimal getInterceptOrderCost() {
		return interceptOrderCost;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public BigDecimal getStartCost() {
		return startCost;
	}

	public BigDecimal getContinueCost() {
		return continueCost;
	}

	public boolean isCode() {
		return isCode;
	}

	public BigDecimal getCodAmount() {
		return codAmount;
	}

	public BigDecimal getExpUserPrice() {
		return expUserPrice;
	}

	public void setExpUserPrice(BigDecimal expUserPrice) {
		this.expUserPrice = expUserPrice;
	}

	public BigDecimal getCodRate() {
		return codRate;
	}

	public boolean isInsurance() {
		return isInsurance;
	}

	public BigDecimal getInsuranceRate() {
		return insuranceRate;
	}

	public BigDecimal getInsuranceCharge() {
		return insuranceCharge;
	}

	public boolean isOnePrice() {
		return isOnePrice;
	}

	public BigDecimal getOnePrice() {
		return onePrice;
	}

	public BigDecimal getLostCompensate() {
		return lostCompensate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	public void setOrderAmount(double value) {
		setOrderAmount(new BigDecimal(value));
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}

	public void setPickPackageCost(BigDecimal pickPackageCost) {
		this.pickPackageCost = pickPackageCost;
	}
	
	public void setPickPackageCost(double value) {
		setPickPackageCost(new BigDecimal(value));
	}

	public void setConsumablesCost(BigDecimal consumablesCost) {
		this.consumablesCost = consumablesCost;
	}
	
	public void setConsumablesCost(double value) {
		setConsumablesCost(new BigDecimal(value));
	}

	public void setInterceptOrderCost(BigDecimal interceptOrderCost) {
		this.interceptOrderCost = interceptOrderCost;
	}
	
	public void setInterceptOrderCost(double value) {
		setInterceptOrderCost(new BigDecimal(value));
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public void setWeight(double value) {
		setWeight(new BigDecimal(value));
	}
	
	public void setStartCost(BigDecimal startCost) {
		this.startCost = startCost;
	}
	
	public void setStartCost(double value) {
		setStartCost(new BigDecimal(value));
	}

	public void setContinueCost(BigDecimal continueCost) {
		this.continueCost = continueCost;
	}
	
	public void setContinueCost(double value) {
		setContinueCost(new BigDecimal(value));
	}

	public void setCode(boolean isCode) {
		this.isCode = isCode;
	}

	public void setCodAmount(BigDecimal codAmount) {
		this.codAmount = codAmount;
	}
	
	public void setCodAmount(double value) {
		setCodAmount(new BigDecimal(value));
	}

	public void setCodRate(BigDecimal codRate) {
		this.codRate = codRate;
	}
	
	public void setCodRate(float value) {
		setCodRate(new BigDecimal(value));
	}

	public void setInsurance(boolean isInsurance) {
		this.isInsurance = isInsurance;
	}

	public void setInsuranceRate(BigDecimal insuranceRate) {
		this.insuranceRate = insuranceRate;
	}
	
	public void setInsuranceRate(float value) {
		setInsuranceRate(new BigDecimal(value));
	}

	public void setInsuranceCharge(BigDecimal insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}

	public void setOnePrice(boolean isOnePrice) {
		this.isOnePrice = isOnePrice;
	}
	
	public void setOnePrice(double value) {
		setOnePrice(new BigDecimal(value));
	}

	public void setOnePrice(BigDecimal onePrice) {
		this.onePrice = onePrice;
	}

	public void setLostCompensate(BigDecimal lostCompensate) {
		this.lostCompensate = lostCompensate;
	}
	
	public void setLostCompensate(double value) {
		setLostCompensate(new BigDecimal(value));
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
