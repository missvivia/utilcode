/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.spu;

import java.util.List;
import java.util.Set;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.meta.ItemSPU;

/**
 * ItemSPUDao.java created by yydx811 at 2015年5月6日 下午7:57:23
 * 单品dao
 *
 * @author yydx811
 */
public interface ItemSPUDao extends AbstractDao<ItemSPU> {

	/**
	 * 获取单品列表
	 * @param pageParamVO
	 * @param spu
	 * @param searchValue
	 * @return List<ItemSPU>
	 */
	public List<ItemSPU> getItemSPUList(BasePageParamVO<?> pageParamVO, ItemSPUDTO spu, String searchValue);
	
	/**
	 * 获取数量
	 * @param spu
	 * @param searchValue
	 * @return int
	 */
	public int getItemSPUCount(ItemSPUDTO spu, String searchValue);

	/**
	 * 获取单品列表
	 * @param spu
	 * @param searchValue
	 * @return List<ItemSPU>
	 */
	public List<ItemSPU> getItemSPUList(ItemSPUDTO spu, String searchValue);
	
	
	/**
	 * 根据spuIdList获取单品列表
	 * @param spuIdList
	 * @return List<ItemSPU>
	 */
	public List<ItemSPU> getItemSPUList(List<Long>spuIdList);
	
	/**
	 * 添加单品
	 * @param spu
	 * @return int
	 */
	public int addItemSPU(ItemSPU spu);
	
	/**
	 * 按条件获取单品
	 * @param itemSPU
	 * @return ItemSPU
	 */
	public ItemSPU getItemSPU(ItemSPU itemSPU);

	/**
	 * 更新单品
	 * @param itemSPU
	 * @return int
	 */
	public int updateItemSPU(ItemSPU itemSPU);

	/**
	 * 批量删除单品
	 * @param idSet
	 */
	public void deleteBulkItemSPU(Set<Long> idSet);

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
	 * 搜索单品
	 * @param spu
	 * @return
	 */
	public List<ItemSPU> getItemSPUListBySearchParam(ItemSPUDTO spu);

	/**
	 * 根据商品分类列表获取品牌id列表
	 * @param categoryIds
	 * @return List<Long> 品牌id列表
	 */
	public List<Long> getBrandIds(List<Long> categoryIds);
}
