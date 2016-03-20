/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.spu.ItemSPUDao;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.service.ItemSPUService;

/**
 * ItemSPUServiceImpl.java created by yydx811 at 2015年5月6日 下午7:54:55
 * 单品service实现
 *
 * @author yydx811
 */
@Service
public class ItemSPUServiceImpl implements ItemSPUService {

	@Autowired
	private ItemSPUDao itemSPUDao;
	
	@Override
	public List<ItemSPUDTO> getItemSPUList(BasePageParamVO<?> pageParamVO, ItemSPUDTO spuDTO, String searchValue) {
		List<ItemSPU> spuList = null;
		if (pageParamVO == null) {
			spuList = itemSPUDao.getItemSPUList(spuDTO, searchValue);
		} else {
			spuList = itemSPUDao.getItemSPUList(pageParamVO, spuDTO, searchValue);
		}
		return convertToDTO(spuList);
	}

	/**
	 * 将List<ItemSPU>转换为List<ItemSPUDTO>
	 * @param spuList
	 * @return List<ItemSPUDTO>
	 */
	public List<ItemSPUDTO> convertToDTO(List<ItemSPU> spuList) {
		if (CollectionUtils.isEmpty(spuList)) {
			return new ArrayList<ItemSPUDTO>(0);
		} else {
			ArrayList<ItemSPUDTO> retList = new ArrayList<ItemSPUDTO>(spuList.size());
			for (ItemSPU itemSPU : spuList) {
				retList.add(new ItemSPUDTO(itemSPU));
			}
			return retList;
		}
	}

	@Override
	public int addItemSPU(ItemSPUDTO spuDTO) {
		ItemSPU spu = new ItemSPU(spuDTO);
		return itemSPUDao.addItemSPU(spu);
	}

	@Override
	public ItemSPUDTO getItemSPU(ItemSPUDTO spuDTO) {
		ItemSPU itemSPU = new ItemSPU(spuDTO);
		itemSPU = itemSPUDao.getItemSPU(itemSPU);
		if (itemSPU == null) {
			return null;
		} else {
			spuDTO = new ItemSPUDTO(itemSPU);
			return spuDTO;
		}
	}

	@Override
	public int updateItemSPU(ItemSPUDTO spuDTO) {
		ItemSPU itemSPU = new ItemSPU(spuDTO);
		return itemSPUDao.updateItemSPU(itemSPU);
	}

	@Override
	public void deleteBulkItemSPU(Set<Long> idSet) {
		itemSPUDao.deleteBulkItemSPU(idSet);
	}

	@Override
	public int getItemSPUCount(ItemSPUDTO spuDTO, String searchValue) {
		return itemSPUDao.getItemSPUCount(spuDTO, searchValue);
	}

	@Override
	public List<Long> getBrandIds(long categoryId) {
		return itemSPUDao.getBrandIds(categoryId);
	}

	@Override
	public List<Long> getSPUIds(List<Long> categoryIds, Set<Long> brandIds, String searchValue) {
		return itemSPUDao.getSPUIds(categoryIds, brandIds, searchValue);
	}

	@Override
	public boolean addBulkItemSPU(List<ItemSPUDTO> spuDTOList) {
		return itemSPUDao.addObjects(spuDTOList);
	}

	@Override
	public List<Long> getBrandIds(List<Long> categoryIds) {
		return itemSPUDao.getBrandIds(categoryIds);
	}
	public List<ItemSPUDTO> getItemSPUList(ItemSPUDTO spuDTO){
		List<ItemSPU> itemSPUs = itemSPUDao.getItemSPUListBySearchParam(spuDTO);
		
		if(!CollectionUtils.isEmpty(itemSPUs)){
			List<ItemSPUDTO> itemSPUDTOs = new ArrayList<>();
			ItemSPUDTO itemSPUDTO = null;
			for(ItemSPU itemSPU:itemSPUs){
				itemSPUDTO = new ItemSPUDTO(itemSPU);
				itemSPUDTOs.add(itemSPUDTO);
			}
			return itemSPUDTOs;
		}
		
		return null;
	}
}
