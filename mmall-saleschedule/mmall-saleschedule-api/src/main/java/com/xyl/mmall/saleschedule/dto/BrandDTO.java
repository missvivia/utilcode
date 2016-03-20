package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.meta.Brand;

public class BrandDTO implements Serializable {

	private static final long serialVersionUID = 5003708059554024177L;
	
	private Brand brand;
	
	private String logo;
	
	private String visualImgWeb;
	
	private String visualImgApp;
	
	private long supplierId;

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getVisualImgWeb() {
		return visualImgWeb;
	}

	public void setVisualImgWeb(String visualImgWeb) {
		this.visualImgWeb = visualImgWeb;
	}

	public String getVisualImgApp() {
		return visualImgApp;
	}

	public void setVisualImgApp(String visualImgApp) {
		this.visualImgApp = visualImgApp;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	
}
