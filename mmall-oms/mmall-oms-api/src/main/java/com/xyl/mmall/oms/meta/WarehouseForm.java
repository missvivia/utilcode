/**
 * 
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "仓库表", tableName = "Mmall_Oms_Warehouse")
public class WarehouseForm implements Serializable {

	/** 序列. */
	private static final long serialVersionUID = 3887608090979357584L;

	@AnnonOfField(desc = "仓库主键", primary = true, autoAllocateId = true, policy = true)
	private long warehouseId;

	/** 仓库类型在 {@link WarehouseType}中定义. */
	@AnnonOfField(desc = "仓库类型,顺丰,EMS")
	private String type;
	
	/** 快递公司在 {@link WMSExpressCompany}中定义. */
	@AnnonOfField(desc = "默认快递公司")
	private String expressCompany;
	
	@AnnonOfField(desc = "仓库编码")
	private String warehouseCode;

	@AnnonOfField(desc = "仓库名称")
	private String warehouseName = "";

	@AnnonOfField(desc = "仓库所在省")
	private String province;
	
	@AnnonOfField(desc = "仓库所在城市")
	private String city;
	
	@AnnonOfField(desc = "仓库详细地址")
	private String address = "";

	@AnnonOfField(desc = "联系电话")
	private String phone = "";

	@AnnonOfField(desc = "仓库退货地址")
	private String returnAddress = "";
	
	@AnnonOfField(desc = "省份id")
	private long provinceId;
	
	public long getWarehouseId() {
		return warehouseId;
	}

	public String getType() {
		return type;
	}
	
	public WarehouseType getTypeEnum() {
		return WarehouseType.genEnumNameIgnoreCase(getType());
	}

	public String getExpressCompany() {
		return expressCompany;
	}
	
	public WMSExpressCompany getExpressCompanyEnum() {
		return WMSExpressCompany.genEnumNameIgnoreCase(getType());
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}
	
	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * 获取完整地址，省-市-详细地址
	 * @return
	 */
	public String getFullAddress() {
		String[] addressItem = new String[] { getProvince(), getCity(), getAddress() };
		StringBuilder sb = new StringBuilder();
		boolean preEmpty = true;
		for (String item : addressItem) {
			if (!preEmpty) {
				sb.append("-");
			}
			boolean currentEmpty = isBlank(item);
			if (!currentEmpty) {
				sb.append(item);
			}
			preEmpty = currentEmpty;
		}
		return sb.toString();
	}
	
	private boolean isBlank (String str) {
		return str == null || str.trim().length() == 0;
	}
	
}
