/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.ProductSKULimitRecordDao;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitRecord;

/**
 * ProductSKULimitRecordDaoImpl.java created by yydx811 at 2015年11月17日 下午1:05:07
 * 商品限购记录dao接口实现
 *
 * @author yydx811
 */
@Repository
public class ProductSKULimitRecordDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductSKULimitRecord> 
		implements ProductSKULimitRecordDao {

	@Override
	public boolean addProductSKULimitRecord(ProductSKULimitRecord skuLimitRecord) {
		long id = allocateRecordId();
		if (id < 1l) {
			return false;
		}
		skuLimitRecord.setId(id);
		return this.addObject(skuLimitRecord) != null;
	}

	@Override
	public ProductSKULimitRecord getProductSKULimitRecord(long userId, long skuId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "ProductSKUId", skuId);
		return this.queryObject(sql.toString());
	}

	@Override
	public boolean updateProductSKULimitRecord(ProductSKULimitRecord skuLimitRecord) {
		return this.updateObjectByKey(skuLimitRecord);
	}

	@Override
	public int deleteSKULimitRecordBySkuId(long skuId) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "ProductSKUId", skuId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}
}
