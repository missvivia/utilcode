/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitRecord;

/**
 * ProductSKULimitRecordDao.java created by yydx811 at 2015年11月17日 下午1:04:25
 * 商品限购记录dao接口
 *
 * @author yydx811
 */
public interface ProductSKULimitRecordDao extends AbstractDao<ProductSKULimitRecord> {

	/**
	 * 新增商品限购记录
	 * @param skuLimitRecord
	 * @return
	 */
	public boolean addProductSKULimitRecord(ProductSKULimitRecord skuLimitRecord);
	
	/**
	 * 按用户id和商品id获取限购购买记录
	 * @param userId
	 * @param skuId
	 * @return
	 */
	public ProductSKULimitRecord getProductSKULimitRecord(long userId, long skuId);
	
	/**
	 * 更新商品限购购买记录
	 * @param skuLimitRecord
	 * @return
	 */
	public boolean updateProductSKULimitRecord(ProductSKULimitRecord skuLimitRecord);
	
	/**
	 * 删除记录
	 * @param skuId
	 * @return
	 */
	public int deleteSKULimitRecordBySkuId(long skuId);
}
