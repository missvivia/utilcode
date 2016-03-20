package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.xyl.mmall.oms.dto.warehouse.WMSPackageDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSPackageDTO.class)
public class ShipDetail {
	@MapField(WMSPackageDTO.field_shipNo)
	private String mailno = "";

	@MapField(value = WMSPackageDTO.field_length, isCM = true)
	private double length;

	@MapField(value = WMSPackageDTO.field_width, isCM = true)
	private double width;

	@MapField(value = WMSPackageDTO.field_height, isCM = true)
	private double height;

	@MapField(value = WMSPackageDTO.field_weight , isKG = true)
	private double weight;

	@XmlElementWrapper(name = "sku_details")
	@XmlElement(name = "sku_detail")
	@MapField(WMSPackageDTO.field_skuDetails)
	private List<SkuDetail> list;

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<SkuDetail> getList() {
		return list;
	}

	public void setList(List<SkuDetail> list) {
		this.list = list;
	}

}
