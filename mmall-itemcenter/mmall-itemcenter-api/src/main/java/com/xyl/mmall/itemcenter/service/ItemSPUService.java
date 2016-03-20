/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.service;

import java.util.List;
import java.util.Set;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;

/**
 * ItemSPUService.java created by yydx811 at 2015年5月6日 下午7:52:59
 * 单品service
 *
 * @author yydx811
 */
public interface ItemSPUService {

	/**
	 * 获取单品列表
	 * @param pageParamVO
	 * @param spuDTO
	 * @param searchValue
	 * @return List<ItemSPUDTO>
	 */
	public List<ItemSPUDTO> getItemSPUList(BasePageParamVO<?> pageParamVO, ItemSPUDTO spuDTO, String searchValue);
	
	/**
	 * 添加单品
	 * @param spuDTO
	 * @return int
	 */
	public int addItemSPU(ItemSPUDTO spuDTO);
	
	/**
	 * 按id获取单品
	 * @param spuDTO
	 * @return ItemSPUDTO
	 */
	public ItemSPUDTO getItemSPU(ItemSPUDTO spuDTO);

	/**
	 * 更新单品
	 * @param spuDTO
	 * @return int
	 */
	public int updateItemSPU(ItemSPUDTO spuDTO);
	
	/**
	 * 批量删除单品
	 * @param idSet
	 */
	public void deleteBulkItemSPU(Set<Long> idSet);

	/**
	 * 获取数量
	 * @param spu
	 * @param searchValue
	 * @return int
	 */
	public int getItemSPUCount(ItemSPUDTO spuDTO, String searchValue);
	
	/**
	 * 根据商品分类获取品牌id列表
	 * @param categoryId
	 * @return List<Long> 品牌id列表
	 */
	public List<Long> getBrandIds(long categoryId);
	
	/**
	 * 按分类id获取单品id
	 * @param categoryIds
	 * @param brandIds
	 * @param searchValue
	 * @return
	 */
	public List<Long> getSPUIds(List<Long> categoryIds, Set<Long> brandIds, String searchValue);

	/**
	 * 批量添加spu
	 * @param spuDTOList
	 * @return
	 */
	public boolean addBulkItemSPU(List<ItemSPUDTO> spuDTOList);
	
	/**
	 * 根据商品分类id列表获取品牌id列表
	 * @param categoryIds
	 * @return List<Long> 品牌id列表
	 */
	public List<Long> getBrandIds(List<Long> categoryIds);
	
	/**
	 * 根据单品条件获取单品列表
	 * @param spuDTO
	 * @return ItemSPUDTO
	 */
	public List<ItemSPUDTO> getItemSPUList(ItemSPUDTO spuDTO);
}
