/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.baishi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 * 
 */
@XmlRootElement(name = "SyncSalesOrderInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSSalesOrderDTO.class)
public class BSSyncSalesOrderInfo implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	public static final String ACTIONTYPE_ADD = "ADD";

	public static final String ACTIONTYPE_CANCEL = "CANCEL";

	/**
	 * 卖家编码，保证唯一。是否可以为空：N
	 */
	@MapField("ownerCode")
	private String customerCode;

	/**
	 * 仓库代码。是否可以为空：N
	 */
	@MapField
	private String warehouseCode;

	/**
	 * ERP交易生成物流订单号，卖家系统保证唯一. 是否可以为空：N
	 */
	@MapField("oderId")
	private String orderCode;

	/**
	 * 操作类型：ADD-新增, CANCEL-取消. 是否可以为空：N
	 */
	private String actionType;

	/**
	 * 卖家销售订单编号.是否可以为空：Y
	 */
	private String extTradeId;

	/**
	 * 订单类型：NORMAL-普通订单/交易订单 , WDO-出库单/非交易订单.是否可以为空：N
	 */
	private String orderType = "NORMAL";

	/**
	 * 订单来源（如：360BUY, PAIPAI, DANGDANG, TAOBAO,OTHER）.是否可以为空：Y
	 */
	private String orderSource;

	/**
	 * 下单时间（yyyy-MM-dd HH:mm:ss）.是否可以为空：Y
	 */
	@MapField("orderTime")
	private String orderTime;

	/**
	 * 付款时间（yyyy-MM-dd HH:mm:ss）.是否可以为空：Y
	 */
	private String paymentTime;

	/**
	 * 商品总金额，可用于买保险.是否可以为空：Y
	 */
	@MapField("totalPrice")
	private double totalAmount;

	/**
	 * 运费.是否可以为空：Y
	 */
	@MapField("shipAmount")
	private double shippingAmount;

	/**
	 * 折扣费.是否可以为空：Y
	 */
	private double discountAmount;

	/**
	 * 实际支付费.是否可以为空：Y
	 */
	private double actualAmount;

	/**
	 * 是否保价.是否可以为空：Y
	 */
	private boolean isValueDeclared;

	/**
	 * 保价金额.是否可以为空：Y
	 */
	private double declaringValueAmount;

	/**
	 * 是否代收货款.是否可以为空：Y
	 */
	@MapField("isCod")
	private boolean isPaymentCollected;

	/**
	 * 代收货款金额.是否可以为空：Y
	 */
	@MapField("codAmount")
	private double collectingPaymentAmount;

	/**
	 * <pre>
	 * 快递商编码：
	 * HTKY：汇通
	 * STO：圆通
	 * YUNDA：韵达
	 * SF：顺丰
	 * EMS：EMS
	 * SF-COD：顺丰COD
	 * YTO-COD：圆通COD
	 * </pre>
	 * 
	 * 是否可以为空：Y
	 */
	private String logisticsProviderCode;

	/**
	 * 运单号. 是否可以为空：Y
	 */
	@MapField("logisticCode")
	private String shippingOrderNo;

	/**
	 * 运输公司名称.是否可以为空：Y
	 */
	private String tmsCompany;

	/**
	 * 运输公司联系人.是否可以为空：Y
	 */
	private String tmsLinkman;

	/**
	 * 运输公司电话.是否可以为空：Y
	 */
	private String tmsPhone;

	/**
	 * 运输公司联系人身份证号.是否可以为空：Y
	 */
	private String tmsLinkmanNo;

	/**
	 * 运输公司运单号.是否可以为空：Y
	 */
	@MapField("logisticNo")
	private String tmsShippingNo;

	/**
	 * 卖家备注. 是否可以为空：Y
	 */
	@MapField
	private String note;

	/**
	 * 无库存标记. 是否可以为空：Y
	 */
	private String noStackTag;

	/**
	 * 外部订单类型,例如：NORMAL,COD,GROUP,FX. 是否可以为空：Y
	 */
	private String extOrderType;

	/**
	 * 发票标记 . 是否可以为空：Y
	 */
	private boolean invoiceFlag;

	/**
	 * 发票抬头. 是否可以为空：Y
	 */
	private String invoiceTitle;

	/**
	 * 发票内容. 是否可以为空：Y
	 */
	private String invoiceNote;

	/**
	 * 发票金额. 是否可以为空：Y
	 */
	private String invoiceAmount;

	/**
	 * 下单人. 是否可以为空：Y
	 */
	private String buyerName;

	/**
	 * 下单人电话. 是否可以为空：Y
	 */
	private String buyerPhone;

	/**
	 * 取货点说明. 是否可以为空：Y
	 */
	private String fetchGoodsLocation;

	/**
	 * 店铺名称. 是否可以为空：Y
	 */
	private String sellerName;

	/**
	 * 发货优先级:SHP_LVL1：普通,SHP_LVL2：急,SHP_LVL3：加急
	 */
	private String priorityCode;

	/**
	 * 收货人信息. 是否可以为空：N
	 */
	@XmlElement(name = "recipient")
	@MapClass
	private BSReceiver receiver;

	/**
	 * SKU上去详情
	 */
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	@MapField("skuDetails")
	private List<BSSkuDetail> items;

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode
	 *            the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * @param warehouseCode
	 *            the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode
	 *            the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the extTradeId
	 */
	public String getExtTradeId() {
		return extTradeId;
	}

	/**
	 * @param extTradeId
	 *            the extTradeId to set
	 */
	public void setExtTradeId(String extTradeId) {
		this.extTradeId = extTradeId;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType
	 *            the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderSource
	 */
	public String getOrderSource() {
		return orderSource;
	}

	/**
	 * @param orderSource
	 *            the orderSource to set
	 */
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	/**
	 * @return the orderTime
	 */
	public String getOrderTime() {
		return orderTime;
	}

	/**
	 * @param orderTime
	 *            the orderTime to set
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	/**
	 * @return the paymentTime
	 */
	public String getPaymentTime() {
		return paymentTime;
	}

	/**
	 * @param paymentTime
	 *            the paymentTime to set
	 */
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the shippingAmount
	 */
	public double getShippingAmount() {
		return shippingAmount;
	}

	/**
	 * @param shippingAmount
	 *            the shippingAmount to set
	 */
	public void setShippingAmount(double shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	/**
	 * @return the discountAmount
	 */
	public double getDiscountAmount() {
		return discountAmount;
	}

	/**
	 * @param discountAmount
	 *            the discountAmount to set
	 */
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * @return the actualAmount
	 */
	public double getActualAmount() {
		return actualAmount;
	}

	/**
	 * @param actualAmount
	 *            the actualAmount to set
	 */
	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}

	/**
	 * @return the isValueDeclared
	 */
	public boolean isValueDeclared() {
		return isValueDeclared;
	}

	/**
	 * @param isValueDeclared
	 *            the isValueDeclared to set
	 */
	public void setValueDeclared(boolean isValueDeclared) {
		this.isValueDeclared = isValueDeclared;
	}

	/**
	 * @return the declaringValueAmount
	 */
	public double getDeclaringValueAmount() {
		return declaringValueAmount;
	}

	/**
	 * @param declaringValueAmount
	 *            the declaringValueAmount to set
	 */
	public void setDeclaringValueAmount(double declaringValueAmount) {
		this.declaringValueAmount = declaringValueAmount;
	}

	/**
	 * @return the isPaymentCollected
	 */
	public boolean isPaymentCollected() {
		return isPaymentCollected;
	}

	/**
	 * @param isPaymentCollected
	 *            the isPaymentCollected to set
	 */
	public void setPaymentCollected(boolean isPaymentCollected) {
		this.isPaymentCollected = isPaymentCollected;
	}

	/**
	 * @return the collectingPaymentAmount
	 */
	public double getCollectingPaymentAmount() {
		return collectingPaymentAmount;
	}

	/**
	 * @param collectingPaymentAmount
	 *            the collectingPaymentAmount to set
	 */
	public void setCollectingPaymentAmount(double collectingPaymentAmount) {
		this.collectingPaymentAmount = collectingPaymentAmount;
	}

	/**
	 * @return the logisticsProviderCode
	 */
	public String getLogisticsProviderCode() {
		return logisticsProviderCode;
	}

	/**
	 * @param logisticsProviderCode
	 *            the logisticsProviderCode to set
	 */
	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}

	/**
	 * @return the shippingOrderNo
	 */
	public String getShippingOrderNo() {
		return shippingOrderNo;
	}

	/**
	 * @param shippingOrderNo
	 *            the shippingOrderNo to set
	 */
	public void setShippingOrderNo(String shippingOrderNo) {
		this.shippingOrderNo = shippingOrderNo;
	}

	/**
	 * @return the tmsCompany
	 */
	public String getTmsCompany() {
		return tmsCompany;
	}

	/**
	 * @param tmsCompany
	 *            the tmsCompany to set
	 */
	public void setTmsCompany(String tmsCompany) {
		this.tmsCompany = tmsCompany;
	}

	/**
	 * @return the tmsLinkman
	 */
	public String getTmsLinkman() {
		return tmsLinkman;
	}

	/**
	 * @param tmsLinkman
	 *            the tmsLinkman to set
	 */
	public void setTmsLinkman(String tmsLinkman) {
		this.tmsLinkman = tmsLinkman;
	}

	/**
	 * @return the tmsPhone
	 */
	public String getTmsPhone() {
		return tmsPhone;
	}

	/**
	 * @param tmsPhone
	 *            the tmsPhone to set
	 */
	public void setTmsPhone(String tmsPhone) {
		this.tmsPhone = tmsPhone;
	}

	/**
	 * @return the tmsLinkmanNo
	 */
	public String getTmsLinkmanNo() {
		return tmsLinkmanNo;
	}

	/**
	 * @param tmsLinkmanNo
	 *            the tmsLinkmanNo to set
	 */
	public void setTmsLinkmanNo(String tmsLinkmanNo) {
		this.tmsLinkmanNo = tmsLinkmanNo;
	}

	/**
	 * @return the tmsShippingNo
	 */
	public String getTmsShippingNo() {
		return tmsShippingNo;
	}

	/**
	 * @param tmsShippingNo
	 *            the tmsShippingNo to set
	 */
	public void setTmsShippingNo(String tmsShippingNo) {
		this.tmsShippingNo = tmsShippingNo;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the noStackTag
	 */
	public String getNoStackTag() {
		return noStackTag;
	}

	/**
	 * @param noStackTag
	 *            the noStackTag to set
	 */
	public void setNoStackTag(String noStackTag) {
		this.noStackTag = noStackTag;
	}

	/**
	 * @return the extOrderType
	 */
	public String getExtOrderType() {
		return extOrderType;
	}

	/**
	 * @param extOrderType
	 *            the extOrderType to set
	 */
	public void setExtOrderType(String extOrderType) {
		this.extOrderType = extOrderType;
	}

	/**
	 * @return the invoiceFlag
	 */
	public boolean isInvoiceFlag() {
		return invoiceFlag;
	}

	/**
	 * @param invoiceFlag
	 *            the invoiceFlag to set
	 */
	public void setInvoiceFlag(boolean invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	/**
	 * @return the invoiceTitle
	 */
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	/**
	 * @param invoiceTitle
	 *            the invoiceTitle to set
	 */
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	/**
	 * @return the invoiceNote
	 */
	public String getInvoiceNote() {
		return invoiceNote;
	}

	/**
	 * @param invoiceNote
	 *            the invoiceNote to set
	 */
	public void setInvoiceNote(String invoiceNote) {
		this.invoiceNote = invoiceNote;
	}

	/**
	 * @return the invoiceAmount
	 */
	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	/**
	 * @param invoiceAmount
	 *            the invoiceAmount to set
	 */
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	/**
	 * @return the buyerName
	 */
	public String getBuyerName() {
		return buyerName;
	}

	/**
	 * @param buyerName
	 *            the buyerName to set
	 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	/**
	 * @return the buyerPhone
	 */
	public String getBuyerPhone() {
		return buyerPhone;
	}

	/**
	 * @param buyerPhone
	 *            the buyerPhone to set
	 */
	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	/**
	 * @return the fetchGoodsLocation
	 */
	public String getFetchGoodsLocation() {
		return fetchGoodsLocation;
	}

	/**
	 * @param fetchGoodsLocation
	 *            the fetchGoodsLocation to set
	 */
	public void setFetchGoodsLocation(String fetchGoodsLocation) {
		this.fetchGoodsLocation = fetchGoodsLocation;
	}

	/**
	 * @return the sellerName
	 */
	public String getSellerName() {
		return sellerName;
	}

	/**
	 * @param sellerName
	 *            the sellerName to set
	 */
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	/**
	 * @return the priorityCode
	 */
	public String getPriorityCode() {
		return priorityCode;
	}

	/**
	 * @param priorityCode
	 *            the priorityCode to set
	 */
	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	/**
	 * @return the receiver
	 */
	public BSReceiver getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver
	 *            the receiver to set
	 */
	public void setReceiver(BSReceiver receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the items
	 */
	public List<BSSkuDetail> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<BSSkuDetail> items) {
		this.items = items;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the actiontypeCancel
	 */
	public static String getActiontypeCancel() {
		return ACTIONTYPE_CANCEL;
	}

	public void addItem(BSSkuDetail item) {
		if (this.items == null) {
			this.items = new ArrayList<BSSkuDetail>();
		}
		this.items.add(item);
	}
}
