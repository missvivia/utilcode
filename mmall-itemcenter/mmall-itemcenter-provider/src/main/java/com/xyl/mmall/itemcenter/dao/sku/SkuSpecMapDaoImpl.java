package com.xyl.mmall.itemcenter.dao.sku;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.UKeyPolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;

@Repository
public class SkuSpecMapDaoImpl extends UKeyPolicyObjectDaoSqlBaseOfAutowired<SkuSpecMap> implements SkuSpecMapDao {

	private final String getSkuSpecMapBySkuIdAndPoIdSql = "select * from " + super.getTableName()
			+ " where skuId=? and poId=?";

	private final String getSkuSpecMapByPoIdProductSkuId = "select * from " + super.getTableName() + " where poId=? "
			+ " and productId=? and skuId=?";

	@Override
	public SkuSpecMap addNewSkuSpecMap(SkuSpecMap map) {
		return super.addObject(map);
	}

	@Override
	public SkuSpecMap getSkuSpecMap(long poId, long skuId, long specId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "SkuId", skuId);
		SQLUtils.appendExtParamObject(sql, "SkuSpecId", specId);
		return queryObject(sql.toString());
	}

	@Override
	public boolean deleteBySkuId(long skuId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "SkuId", skuId);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	// TODO
	@Override
	public boolean updatePo(long productId, long skuId, long poId, String sizeVal) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "SkuId", skuId);
		SkuSpecMap map = queryObject(sql.toString());
		deleteBySkuId(skuId);
		map.setPoId(poId);
		map.setValue(sizeVal);
		super.addObject(map);
		return true;
	}

	@Override
	public SkuSpecMap getSkuSpecMapBySkuIdAndPoId(long skuId, long poId) {
		return super.queryObject(getSkuSpecMapBySkuIdAndPoIdSql, skuId, poId);
	}

	@Cacheable(value = "searchProdByPO")
	@Override
	public SkuSpecMap getSkuSpecMapByPoIdProductIdSkuId(long poId, long productId, long skuId) {
		return super.queryObject(getSkuSpecMapByPoIdProductSkuId, poId, productId, skuId);
	}
}
