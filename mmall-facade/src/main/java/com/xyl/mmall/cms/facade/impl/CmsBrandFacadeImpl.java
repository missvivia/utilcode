package com.xyl.mmall.cms.facade.impl;

import java.util.List;

import javax.annotation.Resource;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.CmsBrandFacade;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.service.BrandService;

@Facade
public class CmsBrandFacadeImpl implements CmsBrandFacade {

	@Resource
	private BrandService brandService;
	
	@Resource
	private BusinessService businessService;
	
	@Resource
	private AgentService agentService;
	
	@Override
	public RetArg/*List<BrandDTO>*/ getAllBrandList(DDBParam param) {
		return brandService.getAllBrandList(param);
	}

	@Override
	public BaseJsonVO editBrand(BrandItemDTO dto, String userName) {
		BaseJsonVO vo = new BaseJsonVO();
		BrandItemDTO itemDTO = brandService.submitBrandItem(dto, userName);
		if (itemDTO == null) {
			vo.setCode(400);
			vo.setMessage("品牌已经存在或者品牌相关的图片没有上传成功");
		} else {
			vo.setCode(200);
			vo.setMessage("ok");
			vo.setResult(itemDTO);
		}
		return vo;
	}

	@Override
	public RetArg/*List<BrandDTO>*/ getSearchedBrandList(String name, DDBParam param) {
		return brandService.searchBrand(name, param);
	}

	@Override
	public RetArg/*List<BrandItemDTO>*/ getAllBrandItemList(DDBParam param) {
		return brandService.getAllBrandItemList(param, 0);
	}

	@Override
	public RetArg/*List<BrandItemDTO>*/ getSearchedBrandItemList(String name, DDBParam param) {
		return brandService.searchBrandItem(name, param);
	}
	
	@Override
	public RetArg getBrandItemListByIndex(String index, DDBParam param) {
		return brandService.getAllBrandForCMS(param, index);
	}
	
	@Override
	public RetArg /*List<SupplierBrandDTO>*/ searchAuditBrand(List<Long> ids, String key,
			int status, DDBParam param) {
		return brandService.searchAuditBrand(ids, key, status, param);
	}

	@Override
	public List<Long> passAuditBrand(List<Long> ids) {
		return brandService.passAuditBrand(ids);
	}

	@Override
	public List<Long> rejectAuditBrand(List<Long> ids, String reason) {
		return brandService.rejectAuditBrand(ids, reason);
	}

	@Override
	public boolean delBrand(long brandId) {
		return brandService.delBrand(brandId);
	}

	@Override
	public List<AreaDTO> getAreaList(long userId, String permission) {
		return businessService.getAreadByIdList(agentService
				.findAgentSiteIdsByPermission(userId, permission));
	}

	@Override
	public SupplierBrandFullDTO getSupplierBrandFullOnlyShowOnlineShops(long supplierBrandId) {
		return brandService.getSupplierBrandFullDetails(supplierBrandId, true, true);
	}

}
