package com.xyl.mmall.order.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.meta.InvoiceInOrdSupplier;

/**
 * @author dingmingliang
 * 
 */
public class InvoiceInOrdSupplierDTO extends InvoiceInOrdSupplier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 收货地址
	 */
	private OrderExpInfoDTO orderExpInfoDTO;

	/**
	 * 
	 */
	private InvoiceInOrdDTO invoiceInOrdDTO;

	/**
	 * 
	 */
	private List<InvoiceSkuSPDTO> invoiceSkuSPDTOList;

	public InvoiceInOrdDTO getInvoiceInOrdDTO() {
		return invoiceInOrdDTO;
	}

	public void setInvoiceInOrdDTO(InvoiceInOrdDTO invoiceInOrdDTO) {
		this.invoiceInOrdDTO = invoiceInOrdDTO;
	}

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public InvoiceInOrdSupplierDTO(InvoiceInOrdSupplier obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public InvoiceInOrdSupplierDTO() {
	}

	public OrderExpInfoDTO getOrderExpInfoDTO() {
		return orderExpInfoDTO;
	}

	public void setOrderExpInfoDTO(OrderExpInfoDTO orderExpInfoDTO) {
		this.orderExpInfoDTO = orderExpInfoDTO;
	}

	public List<InvoiceSkuSPDTO> getInvoiceSkuSPDTOList() {
		return invoiceSkuSPDTOList;
	}

	public void setInvoiceSkuSPDTOList(List<InvoiceSkuSPDTO> invoiceSkuSPDTOList) {
		this.invoiceSkuSPDTOList = invoiceSkuSPDTOList;
	}

	public void setSkuSnapshots(String skuSnapshots) {
		super.setSkuSnapshots(skuSnapshots);
		setInvoiceSkuSPDTOList(JsonUtils.parseArray(getSkuSnapshots(), InvoiceSkuSPDTO.class));
	}
}
