package com.xyl.mmall.mainsite.vo.order;

import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
/**
 * 
 * @author author:lhp
 *
 * @version date:2015年5月27日下午6:38:28
 */
public class InvoiceInOrdVO {
	
	/**
	 *  发票抬头
	 */
	private String title;
	
	/**
	 * 发票号
	 */
	private String invoiceNo;
	
	/**
	 * 开发票时间
	 */
	private String createDate;
	
	/**
	 * 发票Id
	 */
	private long id;
	
	/**
	 *   发票状态
	 */
	private InvoiceInOrdState state;
	

	public InvoiceInOrdVO(InvoiceDTO dto) {
		this.title = dto.getTitle();
		this.state = dto.getState();
		this.createDate = DateUtil.dateToString(dto.getCreateTime(), DateUtil.LONG_PATTERN);
		this.invoiceNo = dto.getInvoiceNo();
		this.id = dto.getId();
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getInvoiceNo() {
		return invoiceNo;
	}


	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public InvoiceInOrdState getState() {
		return state;
	}


	public void setState(InvoiceInOrdState state) {
		this.state = state;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	

	
	
	
	

}
