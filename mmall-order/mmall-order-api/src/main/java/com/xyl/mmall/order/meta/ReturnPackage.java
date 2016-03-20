package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.order.enums.JITPushState;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnExpHbState;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 退货包裹记录
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:10:21
 * 
 */
@AnnonOfClass(desc = "退货包裹记录", tableName = "Mmall_Order_ReturnPackage", dbCreateTimeName = "CreateTime")
public class ReturnPackage implements Serializable {

	private static final long serialVersionUID = -6927719922584400168L;

// 1. 退货包裹明细：非金额部分
	@AnnonOfField(desc = "退货包裹Id(PK)", primary = true, autoAllocateId = true)
	private long retPkgId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "要退的订单的Id")
	private long orderId;
	
	@AnnonOfField(desc = "要退的订单包裹的Id")
	private long orderPkgId;

	@AnnonOfField(desc = "申请时间")
	private long ctime;
	
	@AnnonOfField(desc = "退款方式：原路返回；网易宝")
	private RefundType refundType;
	
	@AnnonOfField(desc = "退货地址Id")
	private String returnExpInfoId = "";
	
	@AnnonOfField(desc = "退货-快递号")
	private String mailNO = "";

	@AnnonOfField(desc = "退货-快递公司")
	private ExpressCompany expressCompany = ExpressCompany.NULL;
	
	@AnnonOfField(desc = "货到付款退货场景下的银行卡信息Id")
	private long bankCardInfoId;

// 2. 退货包裹明细：金额部分
	/**
	 * 2.1 申请退款商品总金额（原始结算价） = 申请的红包退款金额 + 申请退款商品金额（平摊退款价）
	 */
	@AnnonOfField(desc = "申请退款商品总金额（原始结算价）")
	private BigDecimal applyedReturnTotalPrice = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "申请的红包退款金额")
	private BigDecimal applyedReturnHbPrice = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "申请退款商品金额（平摊退款价）")
	private BigDecimal applyedReturnCashPrice = BigDecimal.ZERO;
	
	/**
	 * 2.2 实际退款商品金额 = 退给用户的红包退款金额 + 退给用户的商品退款金额（原路/网易宝/到付银行卡）
	 */	
	@AnnonOfField(desc = "实际退款商品金额")
	private BigDecimal payedTotalPriceToUser = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "实际退给用户的红包退款金额")
	private BigDecimal payedHbPriceToUser = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "实际退给用户的商品退款金额（原路/网易宝/到付银行卡）")
	private BigDecimal payedCashPriceToUser = BigDecimal.ZERO;
	
	/**
	 * 2.3 回寄运费补帖：红包
	 */
	@AnnonOfField(desc = "红包（回寄运费补帖）")
	private BigDecimal expSubsidyPrice = BigDecimal.ZERO;
	
// 3. jit相关部分
	@AnnonOfField(desc = "是否向JIT推送服务成功")
	private JITPushState jitPushState = JITPushState.INIT;
	
	@AnnonOfField(desc = "仓库确认收货时间")
	private long confirmTime;
	
// 4. 相关状态
	@AnnonOfField(desc = "退货状态")
	private ReturnPackageState returnState = ReturnPackageState.INIT;
	
	@AnnonOfField(desc = "标记红包是否已发(退款成功，发10元的红包补贴)")
	private ReturnExpHbState retExpCouponState = ReturnExpHbState.INIT;
	
// 5. 退款相关操作
	@AnnonOfField(desc = "系统/客服退款(拒绝退款)时间")
	private long returnOperationTime;
	
	@AnnonOfField(desc = "备注信息")
	private String extInfo = "";
	
	@AnnonOfField(desc = "客服Id")
	private long kfId;
	
	@AnnonOfField(desc = "客服名字")
	private String kfName = "";

// 6. 记录是否有效
	@AnnonOfField(desc = "标记记录是否有效")
	private boolean deprecated = false;

	public long getRetPkgId() {
		return retPkgId;
	}

	public void setRetPkgId(long id) {
		this.retPkgId = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderPkgId() {
		return orderPkgId;
	}

	public void setOrderPkgId(long orderPkgId) {
		this.orderPkgId = orderPkgId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public RefundType getRefundType() {
		return refundType;
	}

	public void setRefundType(RefundType refundType) {
		this.refundType = refundType;
	}

	public String getReturnExpInfoId() {
		return returnExpInfoId;
	}

	public void setReturnExpInfoId(String returnExpInfoId) {
		this.returnExpInfoId = returnExpInfoId;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	public long getBankCardInfoId() {
		return bankCardInfoId;
	}

	public void setBankCardInfoId(long bankCardInfoId) {
		this.bankCardInfoId = bankCardInfoId;
	}

	public BigDecimal getApplyedReturnTotalPrice() {
		return applyedReturnTotalPrice;
	}

	public void setApplyedReturnTotalPrice(BigDecimal goodsTotalRPrice) {
		this.applyedReturnTotalPrice = goodsTotalRPrice;
	}

	public BigDecimal getApplyedReturnHbPrice() {
		return applyedReturnHbPrice;
	}

	public void setApplyedReturnHbPrice(BigDecimal applyedReturnHbPrice) {
		this.applyedReturnHbPrice = applyedReturnHbPrice;
	}

	public BigDecimal getApplyedReturnCashPrice() {
		return applyedReturnCashPrice;
	}

	public void setApplyedReturnCashPrice(BigDecimal applyedReturnCashPrice) {
		this.applyedReturnCashPrice = applyedReturnCashPrice;
	}

	public BigDecimal getPayedTotalPriceToUser() {
		return payedTotalPriceToUser;
	}

	public void setPayedTotalPriceToUser(BigDecimal payedReturnCashPrice) {
		this.payedTotalPriceToUser = payedReturnCashPrice;
	}

	public BigDecimal getPayedHbPriceToUser() {
		return payedHbPriceToUser;
	}

	public void setPayedHbPriceToUser(BigDecimal hbPriceToUser) {
		this.payedHbPriceToUser = hbPriceToUser;
	}

	public BigDecimal getPayedCashPriceToUser() {
		return payedCashPriceToUser;
	}

	public void setPayedCashPriceToUser(BigDecimal cashPriceToUser) {
		this.payedCashPriceToUser = cashPriceToUser;
	}

	public BigDecimal getExpSubsidyPrice() {
		return expSubsidyPrice;
	}

	public void setExpSubsidyPrice(BigDecimal expSubsidyPrice) {
		this.expSubsidyPrice = expSubsidyPrice;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public JITPushState getJitPushState() {
		return jitPushState;
	}

	public void setJitPushState(JITPushState jitPushState) {
		this.jitPushState = jitPushState;
	}

	public ReturnPackageState getReturnState() {
		return returnState;
	}

	public void setReturnState(ReturnPackageState returnState) {
		this.returnState = returnState;
	}

	public ReturnExpHbState getRetExpCouponState() {
		return retExpCouponState;
	}

	public void setRetExpCouponState(ReturnExpHbState retExpCouponState) {
		this.retExpCouponState = retExpCouponState;
	}

//	public HbRecycleState getHbRecycleState() {
//		return hbRecycleState;
//	}
//
//	public void setHbRecycleState(HbRecycleState hbRecycleState) {
//		this.hbRecycleState = hbRecycleState;
//	}

	public long getReturnOperationTime() {
		return returnOperationTime;
	}

	public void setReturnOperationTime(long returnOperationTime) {
		this.returnOperationTime = returnOperationTime;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public long getKfId() {
		return kfId;
	}

	public void setKfId(long kfId) {
		this.kfId = kfId;
	}

	public String getKfName() {
		return kfName;
	}

	public void setKfName(String kfName) {
		this.kfName = kfName;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}
	
}