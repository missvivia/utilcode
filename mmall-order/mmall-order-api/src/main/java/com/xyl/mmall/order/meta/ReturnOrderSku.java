package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState;

/**
 * 退回的商品
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:10:09
 * 
 */
@AnnonOfClass(desc = "退回的商品", tableName = "Mmall_Order_ReturnOrderSku_New", dbCreateTimeName = "CreateTime")
public class ReturnOrderSku implements Serializable {

	private static final long serialVersionUID = -166308351606622131L;

	@AnnonOfField(desc = "退货包裹Id(PK)", primary = true, primaryIndex = 1)
	private long retPkgId;

	@AnnonOfField(desc = "OrderSKuId(PK)", primary = true, primaryIndex = 2)
	private long orderSkuId;
	
	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "商品退货状态")
	private ReturnOrderSkuConfirmState retOrdSkuState = ReturnOrderSkuConfirmState.NOT_CONFIRMED;

	@AnnonOfField(desc = "与orderSkuId绑定的poId")
	private long poId;
	
	@AnnonOfField(desc = "与orderSkuId绑定的skuId")
	private long skuId;

	@AnnonOfField(desc = "申请时间")
	private long ctime;
	
	@AnnonOfField(desc = "申请退货数量")
	private int applyedReturnCount;

	@AnnonOfField(desc = "退货理由")
	private String reason = "";
	
	@AnnonOfField(desc = "仓库实际收到的退货数量")
	private int confirmCount;

	@AnnonOfField(desc = "仓库退货备注信息")
	private String confirmInfo = "";

	public long getRetPkgId() {
		return retPkgId;
	}

	public void setRetPkgId(long retPkgId) {
		this.retPkgId = retPkgId;
	}

	public long getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(long orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public ReturnOrderSkuConfirmState getRetOrdSkuState() {
		return retOrdSkuState;
	}

	public void setRetOrdSkuState(ReturnOrderSkuConfirmState retOrdSkuState) {
		this.retOrdSkuState = retOrdSkuState;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public void setApplyedReturnCount(int applyedReturnCount) {
		this.applyedReturnCount = applyedReturnCount;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public int getApplyedReturnCount() {
		return applyedReturnCount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getConfirmCount() {
		return confirmCount;
	}

	public void setConfirmCount(int confirmCount) {
		this.confirmCount = confirmCount;
	}

	public String getConfirmInfo() {
		return confirmInfo;
	}

	public void setConfirmInfo(String confirmInfo) {
		this.confirmInfo = confirmInfo;
	}

}
