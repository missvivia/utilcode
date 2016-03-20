package com.xyl.mmall.order.param;

import java.math.BigDecimal;

import com.netease.print.daojar.meta.base.DDBParam;

public class InvoiceSearchParam extends DDBParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8650201290076454612L;
	
	/**
	 * 发票票号
	 */
	private long invoiceNo;
	
	/**
	 * 发票状态
	 */
	private int state;
	
	/**
	 * 收货人
	 */
	private String consignee;
	
	/**
	 * 联系电话
	 */
	private String phone;
	
	/**
	 * 台头
	 */
	private String title;
	
	/**
	 * 发票金额
	 */
	private BigDecimal money;
	
	/**
	 * 开发日期开始
	 */
	private long stime;
	
	/**
	 * 开发日期结束
	 */
	private long etime;

	public long getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public long getEtime() {
		return etime;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}
	
	

}
