package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.util.List;

import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;

public class SupplierBrandFullDTO implements Serializable {

	private static final long serialVersionUID = -7815434963895073834L;
	
	private SupplierBrandDTO basic;
	/**
	 * 用户是否收藏该品牌，用于主站的相关接口
	 */
	private boolean favByUser;
	
	private long brandId;
	
	private List<BrandShopDTO> shops;
	
	/**
	 * 用在移动端品牌故事的
	 */
	private String brandVisualImgApp;

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public boolean isFavByUser() {
		return favByUser;
	}

	public void setFavByUser(boolean favByUser) {
		this.favByUser = favByUser;
	}

	public SupplierBrandDTO getBasic() {
		return basic;
	}

	public void setBasic(SupplierBrandDTO basic) {
		this.basic = basic;
	}

	public List<BrandShopDTO> getShops() {
		return shops;
	}

	public void setShops(List<BrandShopDTO> shops) {
		this.shops = shops;
	}

	public String getBrandVisualImgApp() {
		return brandVisualImgApp;
	}

	public void setBrandVisualImgApp(String brandVisualImgApp) {
		this.brandVisualImgApp = brandVisualImgApp;
	}

}
