package com.xyl.mmall.mainsite.vo.order;

import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.backend.vo.CouponVO;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.dto.OrderFormExtInfoDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 我的订单页-订单对象VO
 * 
 * @author dingmingliang
 * 
 */
public class OrderForm2VO extends OrderFormBasicVO {

	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 订单parentId
	 */
	private long parentId;

	/**
	 * 订购时间
	 */
	private String orderDate;

	/**
	 * 第一个发货时间
	 */
	private String expDate;

	/**
	 * 支付成功时间
	 */
	private String payDate;

	/**
	 * 全部包裹确认收货时间
	 */
	private String confirmDate;

	/**
	 * 订单更新时间
	 */
	private String updateDate;

	/**
	 * 交易关闭倒计时
	 */
	private long payCloseCD;

	/**
	 * 支付成功时间
	 */
	private long payTime;

	/**
	 * 支付流水号 支付平台生成的
	 */
	private String payOrderSn;

	@AnnonOfField(desc = "OMS接单时间")
	private long omsTime;

	@AnnonOfField(desc = "全部包裹确认收货时间")
	private long confirmTime;

	/**
	 * 订单取消时间
	 */
	private String cancelTimeStr;

	/**
	 * 买家请求的发票抬头
	 */
	private String invoiceTitle;

	@AnnonOfField(desc = "取消来源(0:用户取消,1:客服取消,2:超时系统取消)")
	private OrderCancelSource cancelSource;

	@AnnonOfField(desc = "订单退货状态", notNull = false)
	private DeprecatedReturnState returnState;

	@AnnonOfField(desc = "订单支付方式(0:网易宝,1:货到付款,2:红包+网易宝)")
	private OrderFormPayMethod orderFormPayMethod;

	/**
	 * 快递信息
	 */
	private OrderExpInfoVO expInfo;

	/**
	 * 包裹列表
	 */
	@Deprecated
	private List<OrderPackageVO> packageList;

	/**
	 * 订单明细列表
	 */
	private List<OrderCartItemVO> cartList;

	/**
	 * 物流
	 */
	private List<OrderLogisticsVO> orderLogisticsVOs;

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
	 * 取消原因
	 */
	private String cancelReason;

	/**
	 * 
	 */
	private OrderFormExtInfoDTO extDTO;

	/**
	 * 发票
	 */
	private List<InvoiceInOrdVO> invoiceInOrdVOs;

	/**
	 * 买家ID
	 */
	private long userId;

	/**
	 * 买家姓名即买家账号
	 */
	private String userName;

	/**
	 * 买家手机
	 */
	private String phoneNo;

	/**
	 * 买家昵称
	 */
	private String nickName;

	@AnnonOfField(desc = "支付状态(20:未付款,30:已付款,40:已经退款)")
	private PayState payState;

	@AnnonOfField(desc = "订单状态")
	private OrderFormState orderFormState;

	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	/**
	 * 优惠券
	 */
	private CouponVO couponVO;

	/**
	 * 代客下单账号
	 */
	private String proxyAccount;

	public OrderFormExtInfoDTO getExtDTO() {
		return extDTO;
	}

	public void setExtDTO(OrderFormExtInfoDTO extDTO) {
		this.extDTO = extDTO;
	}

	/**
	 * 是否可以申请退换货
	 */
	@Deprecated
	private boolean canApplyReturn;

	@Deprecated
	public boolean isCanApplyReturn() {
		return canApplyReturn;
	}

	@Deprecated
	public void setCanApplyReturn(boolean canApplyReturn) {
		this.canApplyReturn = canApplyReturn;
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public boolean isCanCOD() {
		return canCOD;
	}

	public void setCanCOD(boolean canCOD) {
		this.canCOD = canCOD;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public OrderCancelSource getCancelSource() {
		return cancelSource;
	}

	public void setCancelSource(OrderCancelSource cancelSource) {
		this.cancelSource = cancelSource;
	}

	public long getPayCloseCD() {
		return payCloseCD;
	}

	public void setPayCloseCD(long payCloseCD) {
		this.payCloseCD = payCloseCD;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public long getOmsTime() {
		return omsTime;
	}

	public void setOmsTime(long omsTime) {
		this.omsTime = omsTime;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
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

	public DeprecatedReturnState getReturnState() {
		return returnState;
	}

	public void setReturnState(DeprecatedReturnState returnState) {
		this.returnState = returnState;
	}

	public OrderFormPayMethod getOrderFormPayMethod() {
		return orderFormPayMethod;
	}

	public void setOrderFormPayMethod(OrderFormPayMethod orderFormPayMethod) {
		this.orderFormPayMethod = orderFormPayMethod;
	}

	public OrderExpInfoVO getExpInfo() {
		return expInfo;
	}

	public void setExpInfo(OrderExpInfoVO expInfo) {
		this.expInfo = expInfo;
	}

	public List<OrderPackageVO> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<OrderPackageVO> packageList) {
		this.packageList = packageList;
	}

	public List<OrderCartItemVO> getCartList() {
		return cartList;
	}

	public void setCartList(List<OrderCartItemVO> cartList) {
		this.cartList = cartList;
	}

	public List<OrderLogisticsVO> getOrderLogisticsVOs() {
		return orderLogisticsVOs;
	}

	public void setOrderLogisticsVOs(List<OrderLogisticsVO> orderLogisticsVOs) {
		this.orderLogisticsVOs = orderLogisticsVOs;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<InvoiceInOrdVO> getInvoiceInOrdVOs() {
		return invoiceInOrdVOs;
	}

	public void setInvoiceInOrdVOs(List<InvoiceInOrdVO> invoiceInOrdVOs) {
		this.invoiceInOrdVOs = invoiceInOrdVOs;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCancelTimeStr() {
		return cancelTimeStr;
	}

	public void setCancelTimeStr(String cancelTimeStr) {
		this.cancelTimeStr = cancelTimeStr;
	}

	public CouponVO getCouponVO() {
		return couponVO;
	}

	public void setCouponVO(CouponVO couponVO) {
		this.couponVO = couponVO;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPayOrderSn() {
		return payOrderSn;
	}

	public void setPayOrderSn(String payOrderSn) {
		this.payOrderSn = payOrderSn;
	}

	public String getProxyAccount() {
		return proxyAccount;
	}

	public void setProxyAccount(String proxyAccount) {
		this.proxyAccount = proxyAccount;
	}

}
