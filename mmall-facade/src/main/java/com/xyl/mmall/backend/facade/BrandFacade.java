package com.xyl.mmall.backend.facade;

import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.BrandLogoImg;

public interface BrandFacade {
	
	public String getBrandUrlByBrandId(long id);
	
	public BaseJsonVO getSupplierBrandlist(long supplierId, int limit, int offset);
	
	public SupplierBrandDTO copySupplierBrand(long supplierBrandId);
	
	public SupplierBrandDTO onlineSupplierBrand(long id);
	
	public SupplierBrandDTO offlineSupplierBrand(long id);
	
	public SupplierBrandDTO submitAuditSupplierBrand(long id);
	
	public boolean delSupplierBrand(long id);
	
	public boolean deleteBrandVisualImg(long id, long index);
	
	public SupplierBrandFullDTO addNewSupplierBrand(SupplierBrandFullDTO dto, long brandId, 
			long supplierId, String userName, long areaFmt);
	
	public SupplierBrandDTO submitSupplierBrand(SupplierBrandDTO dto);
	
	public SupplierBrandDTO saveSupplierBrand(SupplierBrandDTO dto);
	
	public SupplierBrandFullDTO getSupplierBrandFull(long supplierBrandId);
	
	public SupplierBrandFullDTO getSupplierBrandFullOnlyShowOnlineShops(long supplierBrandId);
	
	public BrandDTO getBrandByBrandId(long id);
	
	public BrandShopDTO addNewBrandShop(BrandShopDTO brandShop);
	
	public BrandShopDTO editBrandShop(BrandShopDTO brandShop);
	
	public BrandShopDTO activeBrandShop(long id);
	
	public BrandShopDTO stopBrandShop(long id);
	
	public boolean delBrandShop(long id);
	
	public Map<Long, String> getAllBrandIdName();
	
	public List<BrandLogoImg> getBrandLogoImgs(long brandId);
	
	public RetArg getAllBrandList(int limit, int offset);
	
	public BrandDTO getBrandBySupplierId(long id);
	/**
	 * 获得省份列表
	 * @return
	 */
	public List<LocationCode> getProvices();
	/**
	 * 通过省份code查询包含的市的列表
	 * @param code
	 * @return
	 */
	public List<LocationCode> getCities(long code);
	/**
	 * 根据市的code返回区和县的列表
	 * @param code
	 * @return
	 */
	public List<LocationCode> getDistricts(long code);
	/**
	 * 返回code对应的全部的地址
	 * @param code
	 * @return
	 */
	public String getLocationName(long code);
	
	/***
	 * 用户在某个区域，是否在指定时间段内有活动
	 * @param userId
	 * @param areaId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public boolean hasActiveForBrandLike(long userId, long areaId, long startTime, long endTime);

	/**
	 * 品牌名完全匹配
	 * @param brandName
	 * @return
	 */
	public List<Brand> getBrandByName(String brandName);
}
