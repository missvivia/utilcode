/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProdParam;

/**
 * ProdParamDao.java created by yydx811 at 2015年5月15日 下午2:35:02
 * 商品属性dao
 *
 * @author yydx811
 */
public interface ProdParamDao extends AbstractDao<ProdParam> {
	/**
	 * 根据商品Id删除商品属性
	 * @param proId
	 * @return
	 */
	public boolean deleteParamByProductId(long proId);
	
	/**
	 * 批量添加商品属性
	 * @param paramList
	 * @return
	 */
	public boolean addBulkProdParams(List<ProdParam> paramList);

	/**
	 * 按skuid获取商品属性列表
	 * @param skuId
	 * @return
	 */
	public List<ProdParam> getParamListBySKUId(long skuId);
	
	/**
	 * 批量删除商品属性
	 * @param proIds
	 * @return
	 */
	public boolean batchDeleteProdParam(List<Long>proIds);
	
	/**
	 * 获取商品属性
	 * @param prodParam
	 * @return
	 */
	public ProdParam getParamByIndex(ProdParam prodParam);
	
	/**
	 * 删除商品属性
	 * @param prodParam
	 * @return
	 */
	public int deleteParamByIndex(ProdParam prodParam);

	/**
	 * 获取在使用的商品属性选项数量
	 * @param paramId
	 * @param paramOptionId
	 * @return
	 */
	public int countProdParamOptionInUse(long paramId, long paramOptionId);
	
	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Long> getProductSKUIds(Map<Long, Set<Long>> paramMap);
}
