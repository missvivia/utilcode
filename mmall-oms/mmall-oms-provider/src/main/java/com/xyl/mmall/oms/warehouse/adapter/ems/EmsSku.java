package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 *
 */
@XmlAccessorType (XmlAccessType.FIELD)
@MapClass(WMSSkuDetailDTO.class)
public class EmsSku implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = -1631654466487973606L;

	@XmlElement
	@MapField(WMSSkuDetailDTO.field_skuId)
	private String sku_code = "";
	
	@XmlTransient
	@MapField(WMSSkuDetailDTO.field_artNo)
	private String barcode = "";

	@XmlElement
	@MapField(WMSSkuDetailDTO.field_normalCount)
	private int count;

	@XmlElement
	@MapField(WMSSkuDetailDTO.field_defectiveCount)
	private int junk_count;

	@XmlElement
	@MapField(WMSSkuDetailDTO.field_price)
	private double sale_price;

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getJunk_count() {
		return junk_count;
	}

	public void setJunk_count(int junk_count) {
		this.junk_count = junk_count;
	}

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
