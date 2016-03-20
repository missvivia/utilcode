package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.DeprecatedReturnCouponHbRecycleState;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.enums.RefundType;

/**
 * 退货记录
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:10:21
 * 
 */
@Deprecated
@AnnonOfClass(desc = "退货记录", tableName = "Mmall_Order_ReturnForm", dbCreateTimeName = "CreateTime")
public class DeprecatedReturnForm implements Serializable {

	private static final long serialVersionUID = -6927719922584400168L;

	@AnnonOfField(desc = "退货Id(PK)", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "要退的订单的Id")
	private long orderId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "申请时间")
	private long ctime;

	@AnnonOfField(desc = "退货-快递号")
	private String mailNO;

	@AnnonOfField(desc = "退货-快递公司")
	private ExpressCompany expressCompany;

	@AnnonOfField(desc = "仓库确认收货时间")
	private long confirmTime;
	
	@AnnonOfField(desc = "标记优惠券是否已发(退款成功，发10元的优惠券补贴)")
	private boolean useCoupon = false;
	
// 退货订单的原始信息明细(主站显示给用户看的数据)	
	@AnnonOfField(desc = "商品总额")
	private BigDecimal goodsTotalRPrice;
	
	@AnnonOfField(desc = "扣除活动优惠")
	private BigDecimal hdYPrice;

	@AnnonOfField(desc = "扣除优惠券抵用")
	private BigDecimal couponYPrice;
	
	@AnnonOfField(desc = "扣除运费")
	private BigDecimal expPrice;
//---------------

// 实际要退款明细的组成(运维系统里用到的实际金额)
	@AnnonOfField(desc = "实付金额")
	private BigDecimal payedCashPrice;
	
//	// 退款金额 hbPrice + returnCashPrice
//	@AnnonOfField(desc = "退款金额 - 红包抵用金额 (需求未知)")
//	private BigDecimal hbPrice;
//	
//	@AnnonOfField(desc = "退款金额 - 在线支付金额 (需求未知)")
//	private BigDecimal returnCashPrice;
	
	@AnnonOfField(desc = "优惠券（回寄运费补帖）")
	private BigDecimal expSubsidyPrice;
//---------------
	
	@AnnonOfField(desc = "退货地址Id")
	private String returnExpInfoId;

	@AnnonOfField(desc = "退货状态")
	private DeprecatedReturnState returnState;

	@AnnonOfField(desc = "退款方式：原路返回；网易宝")
	private RefundType refundType;
	
	@AnnonOfField(desc = "系统/客服退款(拒绝退款)时间")
	private long returnTime;
	
	@AnnonOfField(desc = "备注信息")
	private String extInfo;
	
	@AnnonOfField(desc = "客服Id")
	private long kfId;
	
	@AnnonOfField(desc = "客服名字")
	private String kfName = "";
	
	@AnnonOfField(desc = "是否向JIT推送服务成功")
	private boolean jitSucc = false;
	
	@AnnonOfField(desc = "优惠券+红包回收状态")
	private DeprecatedReturnCouponHbRecycleState couponHbRecycleState;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
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

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public BigDecimal getHdYPrice() {
		return hdYPrice;
	}

	public void setHdYPrice(BigDecimal hdYPrice) {
		this.hdYPrice = hdYPrice;
	}

	public BigDecimal getCouponYPrice() {
		return couponYPrice;
	}

	public void setCouponYPrice(BigDecimal couponYPrice) {
		this.couponYPrice = couponYPrice;
	}

//	public BigDecimal getHbPrice() {
//		return hbPrice;
//	}
//
//	public void setHbPrice(BigDecimal hbPrice) {
//		this.hbPrice = hbPrice;
//	}

	public BigDecimal getGoodsTotalRPrice() {
		return goodsTotalRPrice;
	}

	public void setGoodsTotalRPrice(BigDecimal goodsTotalRPrice) {
		this.goodsTotalRPrice = goodsTotalRPrice;
	}

	public BigDecimal getExpPrice() {
		return expPrice;
	}

	public void setExpPrice(BigDecimal expPrice) {
		this.expPrice = expPrice;
	}

	public BigDecimal getPayedCashPrice() {
		return payedCashPrice;
	}

	public void setPayedCashPrice(BigDecimal payedCashPrice) {
		this.payedCashPrice = payedCashPrice;
	}

//	public BigDecimal getReturnCashPrice() {
//		return returnCashPrice;
//	}
//
//	public void setReturnCashPrice(BigDecimal returnCashPrice) {
//		this.returnCashPrice = returnCashPrice;
//	}

	public BigDecimal getExpSubsidyPrice() {
		return expSubsidyPrice;
	}

	public void setExpSubsidyPrice(BigDecimal expSubsidyPrice) {
		this.expSubsidyPrice = expSubsidyPrice;
	}

	public String getReturnExpInfoId() {
		return returnExpInfoId;
	}

	public void setReturnExpInfoId(String returnExpInfoId) {
		this.returnExpInfoId = returnExpInfoId;
	}

	public DeprecatedReturnState getReturnState() {
		return returnState;
	}

	public void setReturnState(DeprecatedReturnState returnState) {
		this.returnState = returnState;
	}

	public RefundType getRefundType() {
		return refundType;
	}

	public void setRefundType(RefundType refundType) {
		this.refundType = refundType;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public boolean isUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(boolean useCoupon) {
		this.useCoupon = useCoupon;
	}

	public long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
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

	public boolean isJitSucc() {
		return jitSucc;
	}

	public void setJitSucc(boolean jitSucc) {
		this.jitSucc = jitSucc;
	}

	public DeprecatedReturnCouponHbRecycleState getCouponHbRecycleState() {
		return couponHbRecycleState;
	}

	public void setCouponHbRecycleState(DeprecatedReturnCouponHbRecycleState couponHbRecycleState) {
		this.couponHbRecycleState = couponHbRecycleState;
	}

}