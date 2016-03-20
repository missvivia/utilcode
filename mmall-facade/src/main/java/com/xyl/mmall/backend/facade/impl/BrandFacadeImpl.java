package com.xyl.mmall.backend.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.BrandLogoImg;
import com.xyl.mmall.saleschedule.service.BrandService;

@Facade
public class BrandFacadeImpl implements BrandFacade {
	@Resource
	private BrandService brandService;
	
	@Resource
	private BusinessFacade BusinessFacade;
	
	@Resource
	private LocationService locationService;
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseJsonVO getSupplierBrandlist(long supplierId, int limit, int offset) {
		DDBParam param = new DDBParam("statusUpdateDate", false, limit, offset);
		RetArg retArg = brandService.getSupplierBrandListBySupplierId(param, supplierId);
		List<SupplierBrandDTO> dtos = RetArgUtil.get(retArg, ArrayList.class);
		param = RetArgUtil.get(retArg, DDBParam.class);
		BaseJsonListResultVO listVO = new BaseJsonListResultVO(dtos);
		listVO.setTotal(param.getTotalCount());
		BaseJsonVO jsonVO = new BaseJsonVO(ErrorCode.SUCCESS, listVO);
		jsonVO.setResult(listVO);
		return jsonVO;
	}

	@Override
	public RetArg getAllBrandList(int limit, int offset) {
		DDBParam param = new DDBParam("brandId", true, limit, offset);
		return brandService.getAllBrandList(param);
	}

	@Override
	public SupplierBrandDTO copySupplierBrand(long supplierBrandId) {
		SupplierBrandDTO dto = brandService.copyFromSupplierBrand(supplierBrandId);
		return dto;
	}

	@Override
	public BrandDTO getBrandByBrandId(long id) {
		return brandService.getBrandByBrandId(id);
	}

	@Override
	public SupplierBrandDTO onlineSupplierBrand(long id) {
		return brandService.onlineSupplierBrand(id);
	}

	@Override
	public SupplierBrandDTO offlineSupplierBrand(long id) {
		return brandService.offlineSupplierBrand(id);
	}

	@Override
	public Map<Long, String> getAllBrandIdName() {
		DDBParam param = new DDBParam("brandId", true, 0, 0);
		RetArg retArg = brandService.getAllBrandList(param);
		@SuppressWarnings("unchecked")
		List<BrandDTO> dtoList = RetArgUtil.get(retArg, ArrayList.class);
		Map<Long, String> out = new HashMap<Long, String>();
		for (BrandDTO dto : dtoList) {
			out.put(dto.getBrand().getBrandId(), dto.getBrand().getBrandNameAuto());
		}
		return out;
	}

	@Override
	public SupplierBrandDTO submitAuditSupplierBrand(long id) {
		return brandService.auditSupplierBrand(id);
	}

	@Override
	public boolean delSupplierBrand(long id) {
		return brandService.deleteSupplierBrand(id);
	}

	@Override
	public SupplierBrandFullDTO addNewSupplierBrand(SupplierBrandFullDTO dto, long brandId, 
			long supplierId, String userName, long areaFmt) {
		return brandService.addNewSupplierBrand(dto, brandId, supplierId, 
				userName, areaFmt);
	}

	@Override
	public SupplierBrandDTO submitSupplierBrand(SupplierBrandDTO dto) {
		return brandService.submitSupplierBrand(dto);
	}
	
	@Override
	public SupplierBrandFullDTO getSupplierBrandFull(long supplierBrandId) {
		return brandService.getSupplierBrandFullDetails(supplierBrandId, true, false);
	}
	
	@Override
	public SupplierBrandFullDTO getSupplierBrandFullOnlyShowOnlineShops(long supplierBrandId) {
		return brandService.getSupplierBrandFullDetails(supplierBrandId, true, true);
	}

	@Override
	public BrandShopDTO addNewBrandShop(BrandShopDTO brandShopDTO) {
		return brandService.addNewBrandShop(brandShopDTO);
	}

	@Override
	public BrandShopDTO activeBrandShop(long id) {
		return brandService.activeBrandShop(id);
	}

	@Override
	public BrandShopDTO stopBrandShop(long id) {
		return brandService.stopBrandShop(id);
	}

	@Override
	public boolean delBrandShop(long id) {
		return brandService.delBrandShop(id);
	}

	@Override
	public SupplierBrandDTO saveSupplierBrand(SupplierBrandDTO dto) {
		return brandService.saveSupplierBrand(dto);
	}

	@Override
	public List<BrandLogoImg> getBrandLogoImgs(long brandId) {
		return brandService.getBrandLogoImgs(brandId);
	}
	@Override
	public boolean deleteBrandVisualImg(long id, long index) {
		return brandService.deleteBrandVisualImg(id, index);
	}

	@Override
	public BrandDTO getBrandBySupplierId(long id) {
		// 使用brandService直接得到的brand数据不一定是正确的
		//return brandService.getBrandBySupplierId(id);
		Business business = BusinessFacade.getBusinessById(id);
		return brandService.getBrandByBrandId(business.getActingBrandId());
	}

	@Override
	public List<LocationCode> getProvices() {
		return locationService.getAllProvince();
	}

	@Override
	public List<LocationCode> getCities(long code) {
		return locationService.getCityListByProvinceCode(code);
	}

	@Override
	public List<LocationCode> getDistricts(long code) {
		return locationService.getDistrictListByCityCode(code);
	}

	@Override
	public String getLocationName(long code) {
		return locationService.getLocationNameByCode(code, true);
	}

	@Override
	public BrandShopDTO editBrandShop(BrandShopDTO brandShop) {
		return brandService.editBrandShop(brandShop);
	}

	@Override
	public String getBrandUrlByBrandId(long id) {
		return brandService.getBrandLogoImgs(id).get(0).getBrandImgUrl();
	}

	@Override
	public boolean hasActiveForBrandLike(long userId, long areaId, long startTime, long endTime) {
		return brandService.hasActiveForBrandLike(userId, areaId, startTime, endTime);
	}

	@Override
	public List<Brand> getBrandByName(String brandName) {
		return brandService.getBrandByName(brandName);
	}

}
