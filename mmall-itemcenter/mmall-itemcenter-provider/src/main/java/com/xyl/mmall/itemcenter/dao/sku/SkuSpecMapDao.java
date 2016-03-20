package com.xyl.mmall.itemcenter.dao.sku;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;

public interface SkuSpecMapDao extends AbstractDao<SkuSpecMap> {
	/**
	 * 添加sku规格映射
	 * 
	 * @param map
	 * @return
	 */
	public SkuSpecMap addNewSkuSpecMap(SkuSpecMap map);

	/**
	 * 获取sku规格映射
	 * 
	 * @param skuId
	 *            skuId
	 * @param specId
	 *            规格id
	 * @return
	 */
	public SkuSpecMap getSkuSpecMap(long poId, long skuId, long specId);

	/**
	 * 删除某个sku下的所有规格标签
	 * 
	 * @param skuId
	 * @return
	 */
	public boolean deleteBySkuId(long skuId);

	public boolean updatePo(long productId, long skuId, long poId, String sizeVal);

	public SkuSpecMap getSkuSpecMapBySkuIdAndPoId(long skuId, long poId);

	public SkuSpecMap getSkuSpecMapByPoIdProductIdSkuId(long poId, long productId, long skuId);
}
