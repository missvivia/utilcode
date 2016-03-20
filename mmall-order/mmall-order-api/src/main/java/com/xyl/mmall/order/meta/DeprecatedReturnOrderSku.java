package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState;

/**
 * 退回的商品
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:10:09
 * 
 */
@Deprecated
@AnnonOfClass(desc = "退回的商品", tableName = "Mmall_Order_ReturnOrderSku", dbCreateTimeName = "CreateTime")
public class DeprecatedReturnOrderSku implements Serializable {

	private static final long serialVersionUID = -166308351606622131L;

	// part-1：用户申请退货时填入
	@AnnonOfField(desc = "订单Id(PK)", primary = true, primaryIndex = 1)
	private long orderId;

	@AnnonOfField(desc = "OrderSKuId(PK)", primary = true, primaryIndex = 2)
	private long orderSkuId;
	
	@AnnonOfField(desc = "与orderSkuId绑定的skuId")
	private long skuId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "退货Id")
	private long returnId;
	
	@AnnonOfField(desc = "申请时间")
	private long ctime;
	
	@AnnonOfField(desc = "退货数量")
	private long returnCount;

	@AnnonOfField(desc = "退款总额")
	private BigDecimal returnPrice;

	@AnnonOfField(desc = "退货理由")
	private String reason;
	
	// part-2：仓库收到退货时填入
	@AnnonOfField(desc = "仓库实际收到的退货数量")
	private long confirmCount;

	@AnnonOfField(desc = "未收货/部分收货/已收货")
	private DeprecatedReturnOrderSkuState retOrdSkuState;

	@AnnonOfField(desc = "仓库退货备注信息")
	private String confirmInfo;
	
	// part-3：客服退款/拒绝时填入
	@AnnonOfField(desc = "处理人Id")
	private long kfId;
	
	@AnnonOfField(desc = "实际退款总额")
	private BigDecimal actualReturnPrice;
	
	@AnnonOfField(desc = "退款/拒绝备注信息")
	private String extInfo;

	public String getReason() {
		return reason;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setOrderSkuId(long orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setReturnId(long returnId) {
		this.returnId = returnId;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getOrderSkuId() {
		return orderSkuId;
	}

	public long getOrderId() {
		return orderId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getUserId() {
		return userId;
	}

	public long getReturnId() {
		return returnId;
	}

	public long getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(long returnCount) {
		this.returnCount = returnCount;
	}

	public BigDecimal getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(BigDecimal totalReturnPrice) {
		this.returnPrice = totalReturnPrice;
	}

	public long getConfirmCount() {
		return confirmCount;
	}

	public void setConfirmCount(long confirmCount) {
		this.confirmCount = confirmCount;
	}

	public DeprecatedReturnOrderSkuState getRetOrdSkuState() {
		return retOrdSkuState;
	}

	public void setRetOrdSkuState(DeprecatedReturnOrderSkuState orderSkuState) {
		this.retOrdSkuState = orderSkuState;
	}

	public String getConfirmInfo() {
		return confirmInfo;
	}

	public void setConfirmInfo(String confirmInfo) {
		this.confirmInfo = confirmInfo;
	}

	public long getKfId() {
		return kfId;
	}

	public void setKfId(long kfId) {
		this.kfId = kfId;
	}

	public BigDecimal getActualReturnPrice() {
		return actualReturnPrice;
	}

	public void setActualReturnPrice(BigDecimal actualReturnPrice) {
		this.actualReturnPrice = actualReturnPrice;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	
	
}
