package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.ProductSkuDetail;

@Repository
public class ProductSKUDetailDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductSkuDetail> implements
		ProductSKUDetailDao {

	@Override
	public boolean deleteDetailByProductId(long proId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", proId);
		return this.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public int addProductDetail(ProductSkuDetail skuDetail) {
		return addObject(skuDetail) == null ? -1 : 1;
	}

	@Override
	public ProductSkuDetail getProductDetail(ProductSkuDetail skuDetail) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		if (skuDetail.getId() > 0l) {
			SQLUtils.appendExtParamObject(sql, "Id", skuDetail.getId());
		}
		if (skuDetail.getProductSKUId() > 0l) {
			SQLUtils.appendExtParamObject(sql, "ProductSKUId", skuDetail.getProductSKUId());
		}
		return queryObject(sql.toString());
	}

	@Override
	public boolean batchDeleteProductSKUDetail(List<Long> proIds) {
		if (CollectionUtils.isEmpty(proIds)) {
			return false;
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql()).append("ProductSKUId IN (");
		for (Long id : proIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.length() - 1).append(")");
		return this.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public int updateProductDetail(ProductSkuDetail skuDetail) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET UpdateBy = ").append(skuDetail.getUpdateBy());
		if (StringUtils.isNotBlank(skuDetail.getCustomEditHTML())) {
			sql.append(", CustomEditHTML = '").append(skuDetail.getCustomEditHTML()).append("'");
		}
		if (StringUtils.isNotBlank(skuDetail.getProdParam())) {
			sql.append(", ProdParam = '").append(skuDetail.getProdParam()).append("'");
		}
		sql.append(" WHERE ");
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", skuDetail.getProductSKUId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}
}
