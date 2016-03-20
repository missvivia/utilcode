package com.xyl.mmall.mobile.ios.facade.pageView.orderDetail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.backend.vo.CouponVO;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.mobile.ios.facade.pageView.common.OrderFormBasic;
import com.xyl.mmall.mobile.web.vo.order.InvoiceInOrdVO;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;

public class OrderDetailComposite extends OrderFormBasic {
	
	/**
	 * 订单parentId
	 */
	private long parentId;
	
	/**
	 * 订购时间
	 */
	private String orderDate;
	
	
	/**
	 * 支付成功时间
	 */
	private String payDate;
	

	/**
	 * 交易关闭倒计时
	 */
	private long payCloseCD;
	

	/**
	 *  支付成功时间
	 */
	@JsonIgnore
	private long payTime;

	/**
	 * 支付流水号  支付平台生成的
	 */
	private String payOrderSn;

	
	@AnnonOfField(desc = "发货时间")
	@JsonIgnore
	private long omsTime;


	/**
	 * 买家请求的发票抬头
	 */
	@JsonIgnore
	private String invoiceTitle;

	
	/**
	 * 快递信息
	 */
	private OrderExpInfoComposite expInfo;
	

	/**
	 * 订单明细列表
	 */
//	private List<OrderCartItemVO> cartList;
	
	private List<OrderItemCommitInfoVO> orderSkuInfos;
	
	/**
	 * 备注
	 */
	private String comment;
	
	/**
	 * 是否可以货到付款
	 */
	private boolean canCOD;

	/**
	 * 是否可以取消
	 */
	private boolean canCancel;
	
	/**
	 * 订单取消时间
	 */
	private String cancelDate;
	
	
	/**
	 * 取消原因
	 */
	private String cancelReason;
	/**
	 * 发票
	 */
	@JsonIgnore
	private List<InvoiceInOrdVO>invoiceInOrdVOs;
	
	/**
	 * 买家ID
	 */
	private long userId;
	
	
	@AnnonOfField(desc = "支付状态(20:未付款,30:已付款,40:已经退款)")
	private PayState payState;

	@AnnonOfField(desc = "订单状态")
	private OrderFormState orderFormState;
	
	@AnnonOfField(desc = "下单时间")
	@JsonIgnore
	private long orderTime;
	
	/**
	 * 优惠券
	 */
	private CouponVO couponVO;

	private SpSource spSource = SpSource.MMALL;
	

	public SpSource getSpSource() {
		return spSource;
	}

	public void setSpSource(SpSource spSource) {
		this.spSource = spSource;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}


	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}


	public long getPayCloseCD() {
		return payCloseCD;
	}

	public void setPayCloseCD(long payCloseCD) {
		this.payCloseCD = payCloseCD;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public String getPayOrderSn() {
		return payOrderSn;
	}

	public void setPayOrderSn(String payOrderSn) {
		this.payOrderSn = payOrderSn;
	}

	public long getOmsTime() {
		return omsTime;
	}

	public void setOmsTime(long omsTime) {
		this.omsTime = omsTime;
	}


	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public OrderExpInfoComposite getExpInfo() {
		return expInfo;
	}

	public void setExpInfo(OrderExpInfoComposite expInfo) {
		this.expInfo = expInfo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isCanCOD() {
		return canCOD;
	}

	public void setCanCOD(boolean canCOD) {
		this.canCOD = canCOD;
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public List<InvoiceInOrdVO> getInvoiceInOrdVOs() {
		return invoiceInOrdVOs;
	}

	public void setInvoiceInOrdVOs(List<InvoiceInOrdVO> invoiceInOrdVOs) {
		this.invoiceInOrdVOs = invoiceInOrdVOs;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}


	public PayState getPayState() {
		return payState;
	}

	public void setPayState(PayState payState) {
		this.payState = payState;
	}

	public OrderFormState getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(OrderFormState orderFormState) {
		this.orderFormState = orderFormState;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public CouponVO getCouponVO() {
		return couponVO;
	}

	public void setCouponVO(CouponVO couponVO) {
		this.couponVO = couponVO;
	}

	public List<OrderItemCommitInfoVO> getOrderSkuInfos() {
		return orderSkuInfos;
	}

	public void setOrderSkuInfos(List<OrderItemCommitInfoVO> orderSkuInfos) {
		this.orderSkuInfos = orderSkuInfos;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	
	
}
