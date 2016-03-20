/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.dto.PageableQueryDTO;

/**
 * 商家PO退货查询相关的参数集合
 * 
 * @author hzzengchengyuan
 *
 */
public class PoReturnQueryParamVO extends PageableQueryDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 所属站点
	 */
	private long province;
	
	/**
	 * 所在仓库
	 */
	private long warehouseId;
	
	/**
	 * 供应商账号
	 */
	private String supplierAccount;

	/**
	 * po档期编号
	 */
	private String poOrderId;

	/**
	 * po开始时间
	 */
	private String timestar;

	/**
	 * po结束时间
	 */
	private String timeend;

	/**
	 * 品牌ID
	 */
	private long brandId;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 退货单状态
	 */
	private String state;

	/**
	 * 退货需求类型：一退、二退、三退
	 */
	private String type;

	/**
	 * 退供单号
	 */
	private long returnPoOrderId;

	private List<Long> provinces;
	
	/**
	 * 用户权限
	 */
	private String permission;
	
	/**
	 * @return the province
	 */
	public long getProvince() {
		return province;
	}

	public Long[] getProvinces() {
		if (this.provinces == null) {
			this.provinces = new ArrayList<Long>();
		} 
		if (this.province >0 && !this.provinces.contains(this.province)) {
			this.provinces.add(this.province);
		}
		return this.provinces.toArray(new Long[]{});
	}
	
	public boolean hasProvinceParam () {
		return this.getProvinces() != null && this.getProvinces().length > 0;
	}
	
	public boolean isPermissionProvince(long provinceId) {
		if (hasProvinceParam ()) {
			for(long p : this.getProvinces()) {
				if(p == provinceId) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(long province) {
		this.province = province;
	}

	public void setProvinces(long[] provinces) {
		if (provinces !=null ){
			for (long temp : provinces) {
				addProvince(temp);
			}
		}
	}
	
	public void addProvince (long province) {
		if(province >0 ) {
			if (this.provinces == null) {
				this.provinces = new ArrayList<Long>();
			}
			this.provinces.add(province);
		}
	}

	/**
	 * @return the supplierAccount
	 */
	public String getSupplierAccount() {
		return supplierAccount;
	}

	/**
	 * @param supplierAccount
	 *            the supplierAccount to set
	 */
	public void setSupplierAccount(String supplierAccount) {
		this.supplierAccount = supplierAccount;
	}

	/**
	 * @return the poOrderId
	 */
	public String getPoOrderId() {
		return poOrderId;
	}

	/**
	 * @param poOrderId
	 *            the poOrderId to set
	 */
	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	/**
	 * @return the timestar
	 */
	public String getTimestar() {
		return timestar;
	}

	/**
	 * @param timestar
	 *            the timestar to set
	 */
	public void setTimestar(String timestar) {
		this.timestar = timestar;
	}

	/**
	 * @return the timeend
	 */
	public String getTimeend() {
		return timeend;
	}

	/**
	 * @param timeend
	 *            the timeend to set
	 */
	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	/**
	 * @return the brandId
	 */
	public long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId
	 *            the brandId to set
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
	 * @param brandName
	 *            the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the returnPoOrderId
	 */
	public long getReturnPoOrderId() {
		return returnPoOrderId;
	}

	/**
	 * @param returnPoOrderId
	 *            the returnPoOrderId to set
	 */
	public void setReturnPoOrderId(long returnPoOrderId) {
		this.returnPoOrderId = returnPoOrderId;
	}

}
