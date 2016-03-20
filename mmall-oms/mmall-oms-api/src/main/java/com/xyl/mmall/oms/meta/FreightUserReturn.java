package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;

/**
 * 用户退货所产生的应付物流商费用
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "Oms 退货物流费用", tableName = "Mmall_Oms_FreightUserReturn")
public class FreightUserReturn implements Serializable{

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "结算快递公司")
	private String settleExpressCompany;

	@AnnonOfField(desc = "快递公司")
	private String expressCompany;

	@AnnonOfField(desc = "快递单号，原包裹运单号")
	private String expressNo;

	@AnnonOfField(desc = "仓库ID")
	private long warehouseId;

	@AnnonOfField(desc = "仓库名称")
	private String warehouseName;

	@AnnonOfField(desc = "oms订单号")
	private long omsOrderFormId;

	@AnnonOfField(desc = "用户订单号")
	private long userOrderFormId;
	
	@AnnonOfField(desc = "原始用户退货单号")
	private long userReturnOrderFormId;

	@AnnonOfField(desc = "用户实付订单金额")
	private BigDecimal payAmount = BigDecimal.ZERO;

	@AnnonOfField(desc = "用户退货时快递公司")
	private String returnExpressCompany;

	@AnnonOfField(desc = "用户退货时快递单号")
	private String returnExpressNo;

	@AnnonOfField(desc = "退回地址")
	private String returnAddress;

	@AnnonOfField(desc = "退货服务费")
	private BigDecimal returnServiceCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "仓库收入时间")
	private long wmsReceivedTime;
	
	@AnnonOfField(desc = "退货单状态")
	private OmsReturnOrderFormState state;
	
	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public FreightUserReturn (){
		setCreateTime(System.currentTimeMillis());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
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

	public long getUserOrderFormId() {
		return userOrderFormId;
	}

	public void setUserOrderFormId(long userOrderFormId) {
		this.userOrderFormId = userOrderFormId;
	}

	public long getUserReturnOrderFormId() {
		return userReturnOrderFormId;
	}

	public void setUserReturnOrderFormId(long userReturnOrderFormId) {
		this.userReturnOrderFormId = userReturnOrderFormId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getReturnExpressCompany() {
		return returnExpressCompany;
	}

	public void setReturnExpressCompany(String returnExpressCompany) {
		this.returnExpressCompany = returnExpressCompany;
	}

	public String getReturnExpressNo() {
		return returnExpressNo;
	}

	public void setReturnExpressNo(String returnExpressNo) {
		this.returnExpressNo = returnExpressNo;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public BigDecimal getReturnServiceCharge() {
		return returnServiceCharge;
	}

	public void setReturnServiceCharge(BigDecimal returnServiceCharge) {
		this.returnServiceCharge = returnServiceCharge;
	}

	public long getWmsReceivedTime() {
		return wmsReceivedTime;
	}

	public void setWmsReceivedTime(long wmsReceivedTime) {
		this.wmsReceivedTime = wmsReceivedTime;
	}

	public OmsReturnOrderFormState getState() {
		return state;
	}

	public void setState(OmsReturnOrderFormState state) {
		this.state = state;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
