package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.IncrField;
import com.xyl.mmall.order.meta.SkuOrderStock;

/**
 * @author dingmingliang
 * 
 */
public interface SkuOrderStockDao extends AbstractDao<SkuOrderStock> {

	/**
	 * 更新Sku的已售数量(自动设置并更新upTime字段)
	 * 
	 * @param obj
	 * @param ifColl
	 * @return
	 */
	public boolean updateSaleCount(SkuOrderStock obj, Collection<IncrField<Integer>> ifColl);

	/**
	 * 读取skuId对应的库存信息
	 * 
	 * @param skuIdColl
	 * @return
	 */
	public List<SkuOrderStock> getListBySkuIds(Collection<Long> skuIdColl);

	/**
	 * 删除记录(根据主键)
	 * 
	 * @param skuIdColl
	 * @return
	 */
	public boolean deleteObjectByKeys(Collection<? extends SkuOrderStock> objColl);
}
