/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;

/**
 * ItemSPUFacade.java created by yydx811 at 2015年5月6日 下午7:42:35
 * 单品facade
 *
 * @author yydx811
 */
public interface ItemSPUFacade {

	/**
	 * 获取单品列表
	 * @param pageParamVO
	 * @param spuVO
	 * @param searchValue
	 * @return List<ItemSPUVO>
	 */
	public List<ItemSPUVO> getItemSPUList(BasePageParamVO<ItemSPUVO> pageParamVO, 
			ItemSPUVO spuVO, String searchValue);
	
	/**
	 * 添加单品
	 * @param spuDTO
	 * @return
	 */
	public int addItemSPU(ItemSPUDTO spuDTO);
	
	/**
	 * 按id获取单品
	 * @param spuDTO
	 * @return ItemSPUVO
	 */
	public ItemSPUVO getItemSPU(ItemSPUDTO spuDTO);
	
	/**
	 * 获取单品简略信息
	 * @param spuDTO
	 * @return
	 */
	public ItemSPUVO getBriefItemSPU(ItemSPUDTO spuDTO);
	
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
	 * 获取品牌列表
	 * @param categoryId
	 * @return
	 */
	public List<JSONObject> getBrandIds(long categoryId);
	
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
	public List<JSONObject> getBrandIds(List<Long> categoryIds);
}
