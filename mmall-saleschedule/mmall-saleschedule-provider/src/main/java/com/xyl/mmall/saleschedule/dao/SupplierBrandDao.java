package com.xyl.mmall.saleschedule.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.meta.SupplierBrand;

/**
 * 供应商品牌Dao
 * @author chengximing
 *
 */
public interface SupplierBrandDao extends AbstractDao<SupplierBrand> {

	/**
	 * 同步数据库用的接口
	 */
	void syncData();
	/**
	 * 根据供应商ID 返回所需要的SupplierBrand列表
	 * @param param
	 * @param supplierId
	 * @return brand列表
	 */
	public RetArg/*List<SupplierBrand>*/ getSupplierBrandListBySupplierId(DDBParam param, long supplierId);
	/**
	 * 根据供应商ID 返回所需要的在线的SupplierBrand，如果没有的话，对应的项为null
	 * @param supplierId
	 * @return
	 */
	public SupplierBrand getOnlineSupplierBrandBySupplierId(long supplierId);
	/**
	 * 根据key id返回待审核或者已经审核的品牌
	 * @param supplierId
	 * @param key
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<SupplierBrand> getAuditSupplierBrandListBySupplierId(long supplierId, 
			int status, DDBParam param);
	/**
	 * 根据brandid获取对应的SupplierBrand
	 * @param brandId
	 * @return
	 */
	public SupplierBrand getSupplierBrandById(long id);
	/**
	 * 更新相应的SupplierBrand状态信息
	 * @param brand
	 * @return 是否成功
	 */
	public boolean updateBrandStatus(SupplierBrand brand);
	
	/**
	 * 跟新SupplierBrand的内容
	 * @param brand
	 * @return
	 */
	public boolean updateBrandContent(SupplierBrand brand);
	/**
	 * 通过供应商id列表查询BrandId的列表
	 * @param supplerIdList
	 * @return
	 */
	public List<BrandDTO> getBrandsBySupplerIdList(List<Long> supplierIdList);
	/**
	 * 通过brandId获取对应在线的SupplierBrand列表
	 * @param supplierId
	 * @return
	 */
	public List<SupplierBrand> getOnlineSupplierBrandList(long brandId);
	/**
	 * 搜索对应的满足条件的SupplierBrandList
	 * @param businessIdList
	 * @param status
	 * @param key
	 * @param param
	 * @return
	 */
	public RetArg searchAuditBrand(List<Long> businessIdList, int status, String key, DDBParam param);
	/**
	 * 返回待审核品牌的数量
	 * @param supplierIdList
	 * @return
	 */
	public Map<Long, Integer> getAuditBrandCountsBySupplierList(List<Long> supplierIdList);
	/**
	 * 返回待审核品牌的数量
	 * @param areaIdList
	 * @return
	 */
	public Map<Long, Integer> getAuditBrandCountsByAreaList(List<Long> areaIdList);
	/**
	 * 返回供应商对应的区域支持的map
	 * @param supplierId
	 * @return
	 */
	// public Map<Long, String> getSupplierAreaListMap(long supplierId);
	/**
	 * 重置新的供应商品牌的位图区域，供应商区域发生变化，这里需要对应的调整
	 * @param supplierId
	 * @param newBitmap
	 * @return
	 */
	public boolean resetSupplierBrandBitmap(long supplierId, long newBitmap);
	/**
	 * 冻结或者解冻供应商对应的品牌
	 * @param supplierId
	 * @param bFreeze
	 * @return
	 */
	public boolean freezeSupplierBrand(long supplierId, boolean bFreeze);
}
