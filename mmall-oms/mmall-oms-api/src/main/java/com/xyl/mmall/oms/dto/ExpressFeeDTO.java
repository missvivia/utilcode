package com.xyl.mmall.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 供外部使用的面向用户的快递费用对象
 * @author hzzhaozhenzuo
 *
 */
public class ExpressFeeDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//快递公司code
	private String expressCompanyCode;
	
	//快递公司名称
	private String expressCompanyName;
	
	//快递费
	private BigDecimal price;
	
	//是否cod服务
	private boolean codService;
	
	public String getExpressCompanyCode() {
		return expressCompanyCode;
	}

	public void setExpressCompanyCode(String expressCompanyCode) {
		this.expressCompanyCode = expressCompanyCode;
	}

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isCodService() {
		return codService;
	}

	public void setCodService(boolean codService) {
		this.codService = codService;
	}
	
}
