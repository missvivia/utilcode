/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProdSpeci;

/**
 * ProdSpeciDao.java created by yydx811 at 2015年5月15日 下午2:32:14
 * 商品规格dao
 *
 * @author yydx811
 */
public interface ProdSpeciDao extends AbstractDao<ProdSpeci> {
	
	/**
	 * 根据商品Id删除商品规格
	 * @param proId
	 * @return
	 */
	public boolean deleteSpeciByProductId(long proId);

	/**
	 * 批量添加规格列表
	 * @param speciList
	 * @return
	 */
	public boolean addBulkProdSpeciList(List<ProdSpeci> speciList);
	
	/**
	 * 按skuid获取商品规格列表
	 * @param skuId
	 * @return
	 */
	public List<ProdSpeci> getSpeciListBySKUId(long skuId);
	
	/**
	 * 按skuids获取商品规格列表
	 * @param skuIds
	 * @return
	 */
	public List<ProdSpeci> getSpeciListBySKUIds(List<Long> skuIds);
	
	/**
	 * 批量删除商品规格
	 * @param proIds
	 * @return
	 */
	public boolean batchDeleteProdSpeci(List<Long>proIds);

	/**
	 * 获取在使用的商品规格选项数量
	 * @param speciId
	 * @param speciOptionId
	 * @return
	 */
	public int countProdSpeciOptionInUse(long speciId, long speciOptionId);
	
	/**
	 * 
	 * @param speciMap
	 * @param skuIds
	 * @return
	 */
	public List<Long> getProductSKUIds(Map<Long, Set<Long>> speciMap, List<Long> skuIds);
}
