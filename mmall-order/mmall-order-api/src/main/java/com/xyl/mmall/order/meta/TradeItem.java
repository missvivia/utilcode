package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.order.enums.TradeItemPayMethod;

/**
 * 交易对象(和网易宝对接用)<br>
 * 和OrderForm是多对一的关系
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "交易对象", tableName = "Mmall_Order_TradeItem")
public class TradeItem implements Serializable {

	/** 序列串分割字符表. */
	public static final String[] SPLITE_STRINGS = new String[] { "/" };

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "交易id", primary = true, autoAllocateId = true)
	private long tradeId;

	@AnnonOfField(desc = "交易关联的订单id")
	private long orderId;
	
	@AnnonOfField(desc = "组合订单的父id")
	private long parentId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "现金支付金额")
	private BigDecimal cash;
	
	@AnnonOfField(desc = "修改后的金额")
	private BigDecimal modifiedCash;

	@AnnonOfField(desc = "支付状态(20:未支付,30:已支付,40:已退款,41:已关闭)")
	private PayState payState;

	@AnnonOfField(desc = "创建时间")
	private long ctime;

	@AnnonOfField(desc = "支付成功时间")
	private long payTime;

	@AnnonOfField(desc = "扩展信息", type = "mediumtext")
	private String extInfo = "";

	@AnnonOfField(desc = "支付方式(0:网易宝,1:货到付款,2:0元支付)")
	private TradeItemPayMethod tradeItemPayMethod;

	@AnnonOfField(desc = "交易流水")
	private String payOrderSn = "";

	@AnnonOfField(desc = "网银流水号")
	private String orderSn = "";

	@AnnonOfField(desc = "交易供应商")
	private SpSource spSource = SpSource.MMALL;

	public TradeItemPayMethod getTradeItemPayMethod() {
		return tradeItemPayMethod;
	}

	public void setTradeItemPayMethod(TradeItemPayMethod tradeItemPayMethod) {
		this.tradeItemPayMethod = tradeItemPayMethod;
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public long getParentId()
	{
	    return parentId;
	}
	
	public void setParentId(long parentId)
	{
	    this.parentId = parentId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	
	public BigDecimal getModifiedCash() {
		return modifiedCash;
	}

	public void setModifiedCash(BigDecimal modifiedCash) {
		this.modifiedCash = modifiedCash;
	}

	public PayState getPayState() {
		return payState;
	}

	public void setPayState(PayState payState) {
		this.payState = payState;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public String getPayOrderSn() {
		return payOrderSn;
	}

	public void setPayOrderSn(String payOrderSn) {
		this.payOrderSn = payOrderSn;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public SpSource getSpSource() {
		return spSource;
	}

	public void setSpSource(SpSource spSource) {
		this.spSource = spSource;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
