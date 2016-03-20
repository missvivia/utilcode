package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSSkuDetailDTO.class)
public class SkuDetail {
	@MapField(WMSSkuDetailDTO.field_skuId)
	private String sku_code = "";

	@MapField(WMSSkuDetailDTO.field_name)
	private String sku_name = "";

	@MapField(WMSSkuDetailDTO.field_artNo)
	private String bar_code = "";

	private int package_num;

	@MapField(WMSSkuDetailDTO.field_normalCount)
	private int count;

	@MapField(WMSSkuDetailDTO.field_count)
	private int plan_count;
	
	@MapField(WMSSkuDetailDTO.field_defectiveCount)
	private int junk_count;

	@MapField(WMSSkuDetailDTO.field_unit)
	private String unit = "件";

	@MapField(value = WMSSkuDetailDTO.field_length, isCM = true)
	private double sku_length;

	@MapField(value = WMSSkuDetailDTO.field_width, isCM = true)
	private double sku_width;

	@MapField(value = WMSSkuDetailDTO.field_height, isCM = true)
	private double sku_height;

	@MapField(value = WMSSkuDetailDTO.field_weight, isKG = true)
	private double sku_weight;

	private String spec1;

	private String spec2;

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	public int getPackage_num() {
		return package_num;
	}

	public void setPackage_num(int package_num) {
		this.package_num = package_num;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPlan_count() {
		return plan_count;
	}

	public void setPlan_count(int plan_count) {
		this.plan_count = plan_count;
	}

	public int getJunk_count() {
		return junk_count;
	}

	public void setJunk_count(int junk_count) {
		this.junk_count = junk_count;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getSku_length() {
		return sku_length;
	}

	public void setSku_length(double sku_length) {
		this.sku_length = sku_length;
	}

	public double getSku_width() {
		return sku_width;
	}

	public void setSku_width(double sku_width) {
		this.sku_width = sku_width;
	}

	public double getSku_height() {
		return sku_height;
	}

	public void setSku_height(double sku_height) {
		this.sku_height = sku_height;
	}

	public double getSku_weight() {
		return sku_weight;
	}

	public void setSku_weight(double sku_weight) {
		this.sku_weight = sku_weight;
	}

	public String getSpec1() {
		return spec1;
	}

	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	public String getSpec2() {
		return spec2;
	}

	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}
}
