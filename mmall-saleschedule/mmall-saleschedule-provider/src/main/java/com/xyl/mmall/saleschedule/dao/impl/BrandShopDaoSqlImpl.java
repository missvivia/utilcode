package com.xyl.mmall.saleschedule.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.BrandShopDao;
import com.xyl.mmall.saleschedule.enums.BrandShopStatus;
import com.xyl.mmall.saleschedule.enums.BrandStatus;
import com.xyl.mmall.saleschedule.meta.BrandShop;

@Repository
public class BrandShopDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<BrandShop>
	implements BrandShopDao {

	@Cacheable(value = "brandCache")
	@Override
	public List<BrandShop> getBrandShopListByBrandSupplierId(long supplierBrandId, boolean onlyShowUnderUsingBrandShop) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		if (onlyShowUnderUsingBrandShop) {
			SqlGenUtil.appendExtParamObject(sql, "status", BrandShopStatus.SHOP_USING.getIntValue());
		}
		return queryObjects(sql);
	}

	@Override
	public boolean updateBrandShopStatus(BrandShop brandShop) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("brandShopId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("status");
		return SqlGenUtil.update(getTableName(), setOfUpdate, setOfWhere, brandShop, getSqlSupport());
	}

	// 这个方法暂时没有用处
	@Override
	public List<BrandShop> getBrandShopListByBrandSupplierIdList(List<Long> supplierIds) {
		StringBuilder supplierIdsString = new StringBuilder(256);
		if (supplierIds.size() > 0) {
			supplierIdsString.append(" (");
			for (Long supplierId : supplierIds) {
				supplierIdsString.append(supplierId).append(",");
			}
			supplierIdsString.deleteCharAt(supplierIdsString.lastIndexOf(","));
			supplierIdsString.append(") ");
		} else {
			List<BrandShop> out = new ArrayList<>();
			return out;
		}
		
		StringBuilder sql = new StringBuilder(512);
		sql.append("SELECT A.* FROM Mmall_SaleSchedule_BrandShop A, Mmall_SaleSchedule_SupplierBrand B ")
			.append("WHERE B.supplierId IN ").append(supplierIdsString)
			.append(" AND B.status = ").append(BrandStatus.BRAND_AUDITPASSED_USING.getIntValue())
			.append(" AND B.supplierBrandId = A.supplierBrandId AND A.status = ")
			.append(BrandShopStatus.SHOP_USING.getIntValue());
		return queryObjects(sql);
	}

	@Override
	public List<BrandShop> getBrandShopListByByBrandSupplierId(long supplierId) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("SELECT A.* FROM Mmall_SaleSchedule_BrandShop A, Mmall_SaleSchedule_SupplierBrand B ")
		.append("WHERE B.supplierId = ").append(supplierId)
		.append(" AND B.status = ").append(BrandStatus.BRAND_AUDITPASSED_USING.getIntValue())
		.append(" AND B.supplierBrandId = A.supplierBrandId AND A.status = ")
		.append(BrandShopStatus.SHOP_USING.getIntValue());
		return queryObjects(sql);
	}

}
