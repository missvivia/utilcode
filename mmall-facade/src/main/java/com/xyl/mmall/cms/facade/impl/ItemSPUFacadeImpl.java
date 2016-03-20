/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.service.BrandService;

/**
 * ItemSPUFacadeImpl.java created by yydx811 at 2015年5月6日 下午7:51:03
 * 单品facade实现
 *
 * @author yydx811
 */
@Facade
public class ItemSPUFacadeImpl implements ItemSPUFacade {

	@Resource
	private ItemSPUService itemSPUService;
	
	@Resource
	private CategoryService categoryService;
	
	@Resource
	private BrandService brandService;

	@Override
	public List<ItemSPUVO> getItemSPUList(BasePageParamVO<ItemSPUVO> pageParamVO, 
			ItemSPUVO spuVO, String searchValue) {
		ItemSPUDTO spuDTO = spuVO.convert();
		List<ItemSPUDTO> spuList = null;
		if (pageParamVO != null && pageParamVO.getIsPage() == 1) {
			pageParamVO.setTotal(itemSPUService.getItemSPUCount(spuDTO, searchValue));
			spuList = itemSPUService.getItemSPUList(pageParamVO, spuDTO, searchValue);
		} else {
			spuList = itemSPUService.getItemSPUList(null, spuDTO, searchValue);
		}
		return convertToDTO(spuList);
	}

	/**
	 * 将List<ItemSPUDTO>转换为List<ItemSPUVO>
	 * @param spuList
	 * @return List<ItemSPUVO>
	 */
	public List<ItemSPUVO> convertToDTO(List<ItemSPUDTO> spuList) {
		if (CollectionUtils.isEmpty(spuList)) {
			return new ArrayList<ItemSPUVO>(0);
		} else {
			ArrayList<ItemSPUVO> retList = new ArrayList<ItemSPUVO>(spuList.size());
			for (ItemSPUDTO itemSPUDTO : spuList) {
				ItemSPUVO itemSPUVO = new ItemSPUVO(itemSPUDTO);
				CategoryNormalDTO c = categoryService.getCategoryNormalById(itemSPUVO.getCategoryNormalId(), false);
				itemSPUVO.setCategoryNormalName(c == null ? "" : c.getName());
				BrandDTO b = brandService.getBrandByBrandId(itemSPUVO.getBrandId());
				itemSPUVO.setBrandName(b == null || b.getBrand() == null ? "" : b.getBrand().getBrandNameAuto());
				retList.add(itemSPUVO);
			}
			return retList;
		}
	}

	@Override
	public int addItemSPU(ItemSPUDTO spuDTO) {
		return itemSPUService.addItemSPU(spuDTO);
	}

	@Override
	public ItemSPUVO getItemSPU(ItemSPUDTO spuDTO) {
		spuDTO = itemSPUService.getItemSPU(spuDTO);
		if (spuDTO == null) {
			return null;
		}
		ItemSPUVO ret = new ItemSPUVO(spuDTO);
		CategoryNormalDTO c = categoryService.getCategoryNormalById(ret.getCategoryNormalId(), false);
		ret.setCategoryNormalName(c == null ? "" : c.getName());
		BrandDTO b = brandService.getBrandByBrandId(ret.getBrandId());
		ret.setBrandName(b == null || b.getBrand() == null ? "" : b.getBrand().getBrandNameAuto());
		return ret;
	}

	@Override
	public ItemSPUVO getBriefItemSPU(ItemSPUDTO spuDTO) {
		spuDTO = itemSPUService.getItemSPU(spuDTO);
		if (spuDTO == null) {
			return null;
		}
		return new ItemSPUVO(spuDTO);
	}

	@Override
	public int updateItemSPU(ItemSPUDTO spuDTO) {
		return itemSPUService.updateItemSPU(spuDTO);
	}

	@Override
	public void deleteBulkItemSPU(Set<Long> idSet) {
		itemSPUService.deleteBulkItemSPU(idSet);
	}

	@Override
	public List<JSONObject> getBrandIds(long categoryId) {
		List<Long> brandIds = itemSPUService.getBrandIds(categoryId);
		return getBrandListByIds(brandIds);
	}

	@Override
	public boolean addBulkItemSPU(List<ItemSPUDTO> spuDTOList) {
		return itemSPUService.addBulkItemSPU(spuDTOList);
	}

	@Override
	public List<JSONObject> getBrandIds(List<Long> categoryIds) {
		List<Long> brandIds = itemSPUService.getBrandIds(categoryIds);
		return getBrandListByIds(brandIds);
	}
	
	/**
	 * 根据id列表获取品牌
	 * @param brandIds
	 * @return
	 */
	private List<JSONObject> getBrandListByIds(List<Long> brandIds) {
		if (CollectionUtils.isEmpty(brandIds)) {
			return null;
		}
		List<JSONObject> brandList = brandService.getBrandListInOrderByIds(brandIds);
		return brandList;
	}
}
