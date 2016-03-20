package com.xyl.mmall.order.result;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;

/**
 * 统计订单里,取消的OrderSku
 * 
 * @author dingmingliang
 * 
 */
public class StCancelOrderSkuResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20141201L;

	private long orderId;

	private long userId;

	private long orderSkuId;

	private long skuId;

	private long poId;

	private long packageId;

	private long cancelTime;

	private int count;

	/**
	 * 退款方式
	 */
	private String refundType;

	/**
	 * 退款原因
	 */
	private String refundReason;

	/**
	 * 运费退款金额
	 */
	private BigDecimal refundExp = BigDecimal.ZERO;

	private OrderSkuDTO orderSkuDTO;

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public BigDecimal getRefundExp() {
		return refundExp;
	}

	public void setRefundExp(BigDecimal refundExp) {
		this.refundExp = refundExp;
	}

	public OrderSkuDTO getOrderSkuDTO() {
		return orderSkuDTO;
	}

	public void setOrderSkuDTO(OrderSkuDTO orderSkuDTO) {
		this.orderSkuDTO = orderSkuDTO;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	/**
	 * 根据OrderSkuDTO生成StCancelOrderSkuResult
	 * 
	 * @param orderSkuDTO
	 * @return
	 */
	public static StCancelOrderSkuResult genInstance(OrderSkuDTO orderSkuDTO) {
		StCancelOrderSkuResult obj = new StCancelOrderSkuResult();
		obj.setOrderId(orderSkuDTO.getOrderId());
		obj.setUserId(orderSkuDTO.getUserId());
		obj.setOrderSkuId(orderSkuDTO.getId());
		obj.setSkuId(orderSkuDTO.getSkuId());
		obj.setPoId(orderSkuDTO.getPoId());
		obj.setPackageId(orderSkuDTO.getPackageId());
		obj.setCount(orderSkuDTO.getTotalCount());
		obj.setOrderSkuDTO(orderSkuDTO);
		return obj;
	}

	/**
	 * 根据ReturnOrderSkuDTO生成StCancelOrderSkuResult
	 * 
	 * @param orderSkuDTO
	 * @param returnOrderSkuDTO
	 * @return
	 */
	public static StCancelOrderSkuResult genInstance(OrderSkuDTO orderSkuDTO, ReturnOrderSkuDTO returnOrderSkuDTO) {
		StCancelOrderSkuResult obj = new StCancelOrderSkuResult();
		obj.setOrderId(returnOrderSkuDTO.getOrdSkuDTO().getOrderId());
		obj.setUserId(returnOrderSkuDTO.getUserId());
		obj.setOrderSkuId(returnOrderSkuDTO.getOrdSkuDTO().getId());
		obj.setSkuId(returnOrderSkuDTO.getSkuId());
		obj.setPoId(returnOrderSkuDTO.getPoId());
		obj.setPackageId(returnOrderSkuDTO.getRetPkgId());
		obj.setCount(returnOrderSkuDTO.getConfirmCount());
		obj.setOrderSkuDTO(orderSkuDTO);
		return obj;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderSkuId() {
		return orderSkuId;
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

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}	
}
