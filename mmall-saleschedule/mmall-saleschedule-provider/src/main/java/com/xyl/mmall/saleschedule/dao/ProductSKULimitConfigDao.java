/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitConfig;

/**
 * ProductSKULimitConfigDao.java created by yydx811 at 2015年11月17日 上午9:52:19
 * 商品限购dao接口
 *
 * @author yydx811
 */
public interface ProductSKULimitConfigDao extends AbstractDao<ProductSKULimitConfig> {

	/**
	 * 新增限购配置
	 * @param limitConfig
	 * @return
	 */
	public boolean addProductSKULimitConfig(ProductSKULimitConfig limitConfig);
	
	/**
	 * 根据商品id获取限购配置
	 * @param productSKUId
	 * @return
	 */
	public ProductSKULimitConfig getProductSKULimitConfigBySkuId(long productSKUId);
	
	/**
	 * 删除商品限购
	 * @param productSKUId
	 * @return
	 */
	public boolean deleteProductSKULimitConfigBySkuId(long productSKUId);
	
	/**
	 * 更新商品限购配置
	 * @param skuLimitConfig
	 * @return
	 */
	public boolean updateProductSKULimitConfig(ProductSKULimitConfig skuLimitConfig);
}
