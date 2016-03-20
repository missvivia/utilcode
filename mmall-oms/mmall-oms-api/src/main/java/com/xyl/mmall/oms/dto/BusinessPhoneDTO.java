package com.xyl.mmall.oms.dto;

import com.xyl.mmall.oms.meta.BusinessPhoneForm;

/**
 * 
 * @author hzliujie
 *
 */
public class BusinessPhoneDTO extends BusinessPhoneForm {

	private static final long serialVersionUID = 1L;
	private String verifCode;
	private String supplierName;

	public String getVerifCode() {
		return verifCode;
	}

	public void setVerifCode(String verifCode) {
		this.verifCode = verifCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
}
