/**
 * 
 */
package com.xyl.mmall.backend.vo;

import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;

/**
 * PO退货商品详情VO
 * 
 * @author hzzengchengyuan
 *
 */
public class PoReturnSkuVO extends ReturnPoOrderFormSku {
	public static final String DEFAULT_NULL_LABLE = "--";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 所属站点
	 */
	private long province;
	
	private String provinceName;
	
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	
	/**
	 * 商品条形码
	 */
	private String barCode;
	
	/**
	 * po开始时间
	 */
	private long timestar;

	/**
	 * po结束时间
	 */
	private long timeend;

	/**
	 * 供应商账号
	 */
	private String supplierAccount;

	/**
	 * 品牌id
	 */
	private long brandId;
	
	/**
	 * 品牌
	 */
	private String brandName;

	/**
	 * 供应商公司名称
	 */
	private String companyName;
	
	private String typeDesc = ReturnType.NULL.getDesc();
	
	/**
	 * 箱号
	 */
	private String boxNo;

	/**
	 * 为和前端的字段名skuCount对应起来
	 * @return
	 */
	public int getSkuCount() {
		return getCount();
	}
	
	/**
	 * @return the province
	 */
	public long getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(long province) {
		this.province = province;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * @return the supplierAccount
	 */
	public String getSupplierAccount() {
		return supplierAccount;
	}

	/**
	 * @param supplierAccount the supplierAccount to set
	 */
	public void setSupplierAccount(String supplierAccount) {
		this.supplierAccount = supplierAccount;
	}

	/**
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * @param barCode
	 *            the barCode to set
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	/**
	 * @return the timestar
	 */
	public long getTimestar() {
		return timestar;
	}

	/**
	 * @param timestar the timestar to set
	 */
	public void setTimestar(long timestar) {
		this.timestar = timestar;
	}

	/**
	 * @return the timeend
	 */
	public long getTimeend() {
		return timeend;
	}

	/**
	 * @param timeend the timeend to set
	 */
	public void setTimeend(long timeend) {
		this.timeend = timeend;
	}

	/**
	 * @return the brandId
	 */
	public long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName == null ? DEFAULT_NULL_LABLE : companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public void setType(ReturnType type) {
		super.setType(type);
		this.typeDesc = type == null ? ReturnType.NULL.getDesc() : type.getDesc();
	}
	
	public String getTypeDesc(){
		return this.typeDesc;
	}

	/**
	 * @return the boxNo
	 */
	public String getBoxNo() {
		return boxNo == null ? DEFAULT_NULL_LABLE : boxNo;
	}

	/**
	 * @param boxNo the boxNo to set
	 */
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

}
