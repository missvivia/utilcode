package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.enums.BrandShopStatus;

/**
 * 品牌门店表meta
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "品牌门店表", tableName = "Mmall_SaleSchedule_BrandShop")
public class BrandShop implements Serializable {
	
	private static final long serialVersionUID = 20140911L;
	
	@AnnonOfField(desc = "品牌门店主键id", primary = true, autoAllocateId = true)
	private long brandShopId;
	
	@AnnonOfField(desc = "品牌标识 是Mmall_SaleSchedule_SupplierBrand的主键", policy = true)
	private long supplierBrandId;
	
	@AnnonOfField(desc = "门店名称", type = "VARCHAR(255)")
	private String shopName;
	
	@AnnonOfField(desc = "门店省份")
	private long province;
	
	@AnnonOfField(desc = "门店城市", unsigned = false)
	private long city;
	
	@AnnonOfField(desc = "门店区镇", unsigned = false)
	private long district;
	
	@AnnonOfField(desc = "门店街道")
	private long street;
	
	@AnnonOfField(desc = "门店省份名字", type = "VARCHAR(20)")
	private String provinceName;
	
	@AnnonOfField(desc = "门店城市名字", type = "VARCHAR(20)")
	private String cityName;
	
	@AnnonOfField(desc = "门店区镇名字", type = "VARCHAR(20)")
	private String districtName;
	
	@AnnonOfField(desc = "门店街道名字", type = "VARCHAR(20)")
	private String streetName;
	
	@AnnonOfField(desc = "门店详细地址", type = "VARCHAR(255)")
	private String shopAddr;
	
	@AnnonOfField(desc = "门店区号比如0571", type = "VARCHAR(10)")
	private String shopZone;
	
	@AnnonOfField(desc = "门店电话", type = "VARCHAR(30)")
	private String shopTel;
	
	@AnnonOfField(desc = "门店联系人", type = "VARCHAR(100)")
	private String shopContact;
	
	@AnnonOfField(desc = "状态(0:无效, 1:新建, 2:启用)")
	private BrandShopStatus status;
	
	@AnnonOfField(desc = "品牌门店经度")
	private BigDecimal longitude;
	
	@AnnonOfField(desc = "品牌门店纬度")
	private BigDecimal latitude;
	
	@AnnonOfField(desc = "状态更新时间")
	private long statusUpdateDate;
	
	@AnnonOfField(desc = "品牌门店更新时间")
	private long shopUpdateDate;
	
	public BrandShopDTO changeDataIntoBrandShopDTO() {
		BrandShopDTO dto = new BrandShopDTO();
		dto.setBrandShopId(brandShopId);
		dto.setSupplierBrandId(supplierBrandId);
		dto.setShopName(shopName);
		JSONObject provinceObject = new JSONObject();
		provinceObject.put("id", province);
		provinceObject.put("name", provinceName);
		dto.setProvince(provinceObject);
		JSONObject cityObject = new JSONObject();
		cityObject.put("id", city);
		cityObject.put("name", cityName);
		dto.setCity(cityObject);
		JSONObject districtObject = new JSONObject();
		districtObject.put("id", district);
		districtObject.put("name", districtName);
		dto.setDistrict(districtObject);
		JSONObject streetObject = new JSONObject();
		streetObject.put("id", street);
		streetObject.put("name", streetName);
		dto.setStreet(streetObject);
		dto.setShopAddr(shopAddr);
		dto.setShopZone(shopZone);
		dto.setShopContact(shopContact);
		dto.setShopTel(shopTel);
		dto.setStatus(status);
		dto.setLatitude(latitude);
		dto.setLongitude(longitude);
		dto.setStatusUpdateDate(statusUpdateDate);
		dto.setShopUpdateDate(shopUpdateDate);
		return dto;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	public long getBrandShopId() {
		return brandShopId;
	}

	public void setBrandShopId(long brandShopId) {
		this.brandShopId = brandShopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddr() {
		return shopAddr;
	}

	public void setShopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}

	public String getShopTel() {
		return shopTel;
	}

	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}

	public String getShopContact() {
		return shopContact;
	}

	public void setShopContact(String shopContact) {
		this.shopContact = shopContact;
	}

	public BrandShopStatus getStatus() {
		return status;
	}

	public void setStatus(BrandShopStatus status) {
		this.status = status;
	}

	public long getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(long statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public long getShopUpdateDate() {
		return shopUpdateDate;
	}

	public void setShopUpdateDate(long shopUpdateDate) {
		this.shopUpdateDate = shopUpdateDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public long getSupplierBrandId() {
		return supplierBrandId;
	}

	public void setSupplierBrandId(long supplierBrandId) {
		this.supplierBrandId = supplierBrandId;
	}

	public long getCity() {
		return city;
	}

	public void setCity(long city) {
		this.city = city;
	}

	public long getDistrict() {
		return district;
	}

	public void setDistrict(long district) {
		this.district = district;
	}

	public String getShopZone() {
		return shopZone;
	}

	public void setShopZone(String shopZone) {
		this.shopZone = shopZone;
	}

	public long getProvince() {
		return province;
	}

	public void setProvince(long province) {
		this.province = province;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public long getStreet() {
		return street;
	}

	public void setStreet(long street) {
		this.street = street;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

}
