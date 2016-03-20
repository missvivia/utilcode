package com.xyl.mmall.mainsite.vo.order;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 订单包裹的VO对象
 * 
 * @author dingmingliang
 * 
 */
public class OrderPackageVO {

	@AnnonOfField(desc = "包裹顺序id")
	private long packageIndex;

	@AnnonOfField(desc = "包裹id", primary = true, autoAllocateId = true)
	private long packageId;

	@AnnonOfField(desc = "快递号", type = "VARCHAR(32)", notNull = false)
	private String mailNO;

	@AnnonOfField(desc = "快递公司-用户选择的")
	private ExpressCompany expressCompany;

	@AnnonOfField(desc = "快递公司-ERP回传", notNull = false)
	private ExpressCompany expressCompany2;

	@AnnonOfField(desc = "快递公司-ERP回传", notNull = false)
	private String expressCompanyReturn;

	@AnnonOfField(desc = "包裹的状态")
	private OrderPackageState orderPackageState;

	@AnnonOfField(desc = "OMS接单时间")
	private long omsTime;

	@AnnonOfField(desc = "快递开始时间")
	private long expSTime;

	@AnnonOfField(desc = "快递签收时间")
	private long confirmTime;

	/**
	 * 订单明细列表
	 */
	private List<OrderCartItemVO> cartList;

	/**
	 * 是否可以申请退换货
	 */
	private boolean canApplyReturn;

	/**
	 * 包裹退货状态
	 */
	private ReturnPackageState returnPackageState;

	/**
	 * 包裹取消时间
	 */
	private long cancelTime;

	/**
	 * 退款现金金额
	 */
	private BigDecimal refundRealCash = BigDecimal.ZERO;

	public BigDecimal getRefundRealCash() {
		return refundRealCash;
	}

	public void setRefundRealCash(BigDecimal refundRealCash) {
		this.refundRealCash = refundRealCash;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public ReturnPackageState getReturnPackageState() {
		return returnPackageState;
	}

	public void setReturnPackageState(ReturnPackageState returnPackageState) {
		this.returnPackageState = returnPackageState;
	}

	public boolean isCanApplyReturn() {
		return canApplyReturn;
	}

	public void setCanApplyReturn(boolean canApplyReturn) {
		this.canApplyReturn = canApplyReturn;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public List<OrderCartItemVO> getCartList() {
		return cartList;
	}

	public void setCartList(List<OrderCartItemVO> cartList) {
		this.cartList = cartList;
	}

	public long getPackageIndex() {
		return packageIndex;
	}

	public void setPackageIndex(long packageIndex) {
		this.packageIndex = packageIndex;
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

	public ExpressCompany getExpressCompany2() {
		return expressCompany2;
	}

	public void setExpressCompany2(ExpressCompany expressCompany2) {
		this.expressCompany2 = expressCompany2;
	}

	public OrderPackageState getOrderPackageState() {
		return orderPackageState;
	}

	public void setOrderPackageState(OrderPackageState orderPackageState) {
		this.orderPackageState = orderPackageState;
	}

	public long getOmsTime() {
		return omsTime;
	}

	public void setOmsTime(long omsTime) {
		this.omsTime = omsTime;
	}

	public long getExpSTime() {
		return expSTime;
	}

	public void setExpSTime(long expSTime) {
		this.expSTime = expSTime;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getExpressCompanyReturn() {
		return expressCompanyReturn;
	}

	public void setExpressCompanyReturn(String expressCompanyReturn) {
		this.expressCompanyReturn = expressCompanyReturn;
	}

}
