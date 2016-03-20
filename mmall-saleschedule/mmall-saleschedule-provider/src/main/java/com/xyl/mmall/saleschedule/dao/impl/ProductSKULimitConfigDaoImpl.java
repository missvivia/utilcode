/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.ProductSKULimitConfigDao;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitConfig;

/**
 * ProductSKULimitConfigDaoImpl.java created by yydx811 at 2015年11月17日 上午9:53:35
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Repository
public class ProductSKULimitConfigDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductSKULimitConfig> 
		implements ProductSKULimitConfigDao {

	@Override
	public boolean addProductSKULimitConfig(ProductSKULimitConfig limitConfig) {
		long id = this.allocateRecordId();
		if (id < 1l) {
			return false;
		}
		limitConfig.setId(id);
		return this.addObject(limitConfig) != null;
	}

	@Override
	public ProductSKULimitConfig getProductSKULimitConfigBySkuId(long productSKUId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "ProductSKUId", productSKUId);
		return this.queryObject(sql.toString());
	}

	@Override
	public boolean deleteProductSKULimitConfigBySkuId(long productSKUId) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "ProductSKUId", productSKUId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 1;
	}

	@Override
	public boolean updateProductSKULimitConfig(ProductSKULimitConfig skuLimitConfig) {
		return this.updateObjectByKey(skuLimitConfig);
	}

}
