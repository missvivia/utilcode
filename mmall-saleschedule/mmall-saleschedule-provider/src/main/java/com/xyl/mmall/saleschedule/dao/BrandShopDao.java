package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.BrandShop;

/**
 * BrandShop的数据库相关操作封装
 * @author chengximing
 *
 */
public interface BrandShopDao extends AbstractDao<BrandShop> {
	
	public boolean updateBrandShopStatus(BrandShop brandShop);
	
	public List<BrandShop> getBrandShopListByBrandSupplierId(long supplierBrandId, boolean onlyShowUnderUsingBrandShop);
	/**
	 * 通过supplierBrandId的列表返回所有的品牌店(品牌门店都应该是已启用的状态)
	 * @param supplierBrandIds
	 * @return
	 */
	public List<BrandShop> getBrandShopListByBrandSupplierIdList(List<Long> supplierIds);
	/**
	 * 通过supplierBrandId返回所有的品牌店(品牌门店都应该是已启用的状态)
	 * @param supplierId
	 * @return
	 */
	public List<BrandShop> getBrandShopListByByBrandSupplierId(long supplierId);
}
