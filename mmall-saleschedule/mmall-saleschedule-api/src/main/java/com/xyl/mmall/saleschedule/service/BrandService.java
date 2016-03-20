package com.xyl.mmall.saleschedule.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.enums.BrandStatus;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.BrandLogoImg;

/**
 * 品牌管理服务类
 * 
 * @author chengximing
 *
 */
public interface BrandService {

	/**
	 * 同步数据库用的接口
	 */
	public void syncData();

	/**
	 * 根据id获得当前supplier对应的品牌
	 * 
	 * @param id
	 * @return
	 */
	public SupplierBrandDTO getSupplierBrandById(long id);

	/**
	 * 添加一个新的SupplierBrand
	 * 
	 * @param fullDto
	 * @param brandId
	 * @param supplierId
	 * @param userName
	 * @param areaFmt
	 * @return
	 */
	public SupplierBrandFullDTO addNewSupplierBrand(SupplierBrandFullDTO fullDto, long brandId, long supplierId,
			String userName, long areaFmt);

	/**
	 * 提交审核
	 * 
	 * @param fullDto
	 * @return
	 */
	public SupplierBrandDTO submitSupplierBrand(SupplierBrandDTO dto);

	/**
	 * 复制一个SupplierBrand
	 * 
	 * @param copyId
	 * @return
	 */
	public SupplierBrandDTO copyFromSupplierBrand(long copyId);

	/**
	 * 上线对应的SupplierBrand
	 * 
	 * @param onlineId
	 * @return
	 */
	public SupplierBrandDTO onlineSupplierBrand(long onlineId);

	/**
	 * 下线对应的SupplierBrand
	 * 
	 * @param onlineId
	 * @return
	 */
	public SupplierBrandDTO offlineSupplierBrand(long offlineId);

	/**
	 * 提交审核对应的SupplierBrand
	 * 
	 * @param onlineId
	 * @return
	 */
	public SupplierBrandDTO auditSupplierBrand(long auditId);

	/**
	 * 删除对应的SupplierBrand
	 * 
	 * @param dto
	 * @return
	 */
	public boolean deleteSupplierBrand(long delId);

	/**
	 * 保存编辑之后的SupplierBrand
	 * 
	 * @param dto
	 * @return
	 */
	public SupplierBrandDTO saveSupplierBrand(SupplierBrandDTO dto);

	/**
	 * 根据supplierId得到对应的SupplierBrand的列表
	 * 
	 * @param param
	 * @param supplierId
	 * @return
	 */
	public RetArg/* List<SupplierBrandDTO> */getSupplierBrandListBySupplierId(DDBParam param, long supplierId);

	/**
	 * 改变SupplierBrand的状态
	 * 
	 * @param id
	 * @param newStatus
	 * @return
	 */
	public boolean setBrandNewStatus(long id, BrandStatus newStatus);

	/**
	 * 返回SupplierBrand的详细信息。showShops控制是否返回门店信息
	 * onlyShowUnderUsingBrandShop控制是否只显示已开启的门店还是全部的门店
	 * 
	 * @param supplierBrandId
	 * @param showShops
	 * @param onlyShowUnderUsingBrandShop
	 * @return
	 */
	public SupplierBrandFullDTO getSupplierBrandFullDetails(long supplierBrandId, boolean showShops,
			boolean onlyShowUnderUsingBrandShop);

	/**
	 * 通过供应商id获取对应的品牌门店列表
	 * 
	 * @param supplierId
	 * @return
	 */
	public List<BrandShopDTO> getBrandShopListBySupplierId(long supplierId);

	public SupplierBrandFullDTO getSupplierBrandFullDTOBySupplierId(long supplierId);

	/**
	 * 通过supplierId返回详细信息
	 * 
	 * @param supplierId
	 * @param userId
	 * @param showShops
	 * @param bMobile
	 * @return
	 */
	public SupplierBrandFullDTO getSupplierBrandFullDTOBySupplierId(long supplierId, long userId, boolean showShops,
			boolean bMobile);

	/**
	 * 得到系统CMS中所有的品牌
	 * 
	 * @return
	 */
	public RetArg getAllBrandList(DDBParam param);

	public BrandDTO getBrandByBrandId(long id);

	/**
	 * 根据supplier id列表返回对应的品牌列表
	 * 
	 * @param supplierIdList
	 * @return
	 */
	public List<BrandDTO> getBrandListBySupplierIdList(List<Long> supplierIdList);

	// /**
	// * 根据supplier id返回对应的品牌数据，如果找不到就为null
	// * @param id
	// * @return
	// */
	// public BrandDTO getBrandBySupplierId(long id);
	/**
	 * 通过供应商id返回品牌店列表 （只返回在使用的门店)
	 * 
	 * @param supplierId
	 * @return
	 */
	public List<BrandShopDTO> getBrandShops(long supplierId);

	/**
	 * 添加品牌门店
	 * 
	 * @param brandShopDTO
	 * @return
	 */
	public BrandShopDTO addNewBrandShop(BrandShopDTO brandShopDTO);

	/**
	 * 编辑品牌门店
	 * 
	 * @param brandShop
	 * @return
	 */
	public BrandShopDTO editBrandShop(BrandShopDTO brandShop);

	/**
	 * 启用对应的门店
	 * 
	 * @param id
	 * @return
	 */
	public BrandShopDTO activeBrandShop(long id);

	/**
	 * 停用对应的门店
	 * 
	 * @param id
	 * @return
	 */
	public BrandShopDTO stopBrandShop(long id);

	/**
	 * 删除对应的门店
	 * 
	 * @param id
	 * @return
	 */
	public boolean delBrandShop(long id);

	/**
	 * 提交一个brandItem
	 * 
	 * @param dto
	 * @return
	 */
	public BrandItemDTO submitBrandItem(BrandItemDTO dto, String userName);

	/**
	 * 通过brandId返回BrandItem的对应数据，用于移动端
	 * 
	 * @param brandId
	 * @param userId
	 * @return
	 */
	public BrandItemDTO getBrandItemDTOByBrandId(long brandId, long userId);

	/**
	 * 返回CMS系统的品牌列表
	 * 
	 * @return
	 */
	public RetArg getAllBrandItemList(DDBParam param, long time);

	/**
	 * 返回推荐的品牌列表
	 * 
	 * @param areaId
	 * @param userId
	 * @param recCount
	 * @param isApp
	 * @return
	 */
	public List<BrandItemDTO> getRecommendBrandItemList(long areaId, long userId, int recCount, boolean isApp);

	/**
	 * 搜索对应的brand
	 * 
	 * @return
	 */
	public RetArg searchBrand(String name, DDBParam param);

	public RetArg searchBrandItem(String name, DDBParam param);

	/**
	 * 删除对应的brand
	 * 
	 * @param brandId
	 */
	public boolean delBrand(long brandId);

	public List<BrandLogoImg> getBrandLogoImgs(long brandId);

	/**
	 * 删除对应品牌的形象图
	 * 
	 * @param id
	 * @param index
	 * @return
	 */
	public boolean deleteBrandVisualImg(long id, long index);

	/**
	 * 根据相关信息查询品牌核审结果
	 * 
	 * @param businessIds
	 * @param key
	 * @param status
	 * @param limit
	 * @param offset
	 * @return
	 */
	public RetArg searchAuditBrand(List<Long> businessIds, String key, int status, DDBParam param);

	/**
	 * 通过品牌审核
	 * 
	 * @param ids
	 * @return
	 */
	public List<Long> passAuditBrand(List<Long> ids);

	/**
	 * 审核拒绝品牌
	 * 
	 * @param ids
	 * @return
	 */
	public List<Long> rejectAuditBrand(List<Long> ids, String reason);

	/**
	 * 判断品牌收藏状态
	 * 
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean getBrandCollectionState(long userId, long brandId);

	/**
	 * 添加品牌收藏
	 * 
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean addBrandCollection(long userId, long brandId);

	/**
	 * 移除品牌收藏
	 * 
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean removeBrandCollection(long userId, long brandId);

	/**
	 * 通过userId获取用户关注的品牌列表
	 * 
	 * @param param
	 * @param time
	 *            如果传<= 0的数值 说明此参数无效，控制返回的数据是time之前的数据
	 * @param userId
	 * @param areaId
	 * @param showDetail
	 *            是否显示活动的详细信息
	 * @param bApp
	 * @return
	 */
	public RetArg getUserFavBrandList(DDBParam param, long time, long userId, long areaId, boolean showDetail,
			boolean bApp);

	/**
	 * 返回入住品牌详细列表
	 * 
	 * @param param
	 * @param time
	 * @param userId
	 * @param begin
	 * @param bApp
	 * @param areaId
	 * @return
	 */
	public RetArg getAllBrandItemListWithDetails(DDBParam param, long time, long userId, String begin, boolean bApp,
			long areaId);

	public boolean hasActiveForBrandLike(long userId, long areaId, long startTime, long endTime);

	/**
	 * 返回每个supplierId对应的待审核的品牌数量
	 * 
	 * @param supplierIdList
	 * @return
	 */
	public Map<Long, Integer> getAuditBrandCountsBySupplierList(List<Long> supplierIdList);

	/**
	 * 返回每个areaId对应的待审核的品牌数量
	 * 
	 * @param areaIdList
	 * @return
	 */
	public Map<Long, Integer> getAuditBrandCountsByAreaList(List<Long> areaIdList);

	/**
	 * 判断用户是否收藏了对应的品牌
	 * 
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean isBrandInFavList(long userId, long brandId);

	/**
	 * 通过brandId的列表得到用户关注的状态
	 * 
	 * @param brandIdList
	 * @param userId
	 * @return
	 */
	public Map<Long, Boolean> getBrandFavStateByIds(List<Long> brandIdList, long userId);

	/**
	 * 返回所有的brand的app移动端的特殊需求 返回brandIdAfter之后的limit个数据
	 * 
	 * @param param
	 * @param brandIdAfter
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public List<BrandItemDTO> getAllBrandForApp(DDBParam param, long brandIdAfter, long userId, long areaId);

	public List<BrandItemDTO> getAllBrandForApp2(long userId, long areaId);

	/**
	 * cms系统中根据字母索引返回系统中品牌数据
	 * 
	 * @param param
	 * @param index
	 * @return
	 */
	public RetArg getAllBrandForCMS(DDBParam param, String index);

	/**
	 * 重置新的供应商品牌的位图区域，供应商区域发生变化，这里需要对应的调整
	 * 
	 * @param supplierId
	 * @param newBitmap
	 * @return
	 */
	public boolean resetSupplierBrandBitmap(long supplierId, long newBitmap);

	public List<Brand> getBrandByName(String name);

	/**
	 * 冻结或者解冻供应商对应的品牌
	 * 
	 * @param supplierId
	 * @param bFreeze
	 * @return
	 */
	public boolean freezeSupplierBrand(long supplierId, boolean bFreeze);
	
	/**
	 * 返回用户品牌对应收藏的用户的相关信息，用于相关的提醒
	 * RetArg List<UserFavListDTO>
	 * 		  int total
	 * @param brandIdList
	 * @param timeAfter
	 * @param limit
	 * @param offset
	 * @return
	*/
	public RetArg getUserFavListByBrandIdList(List<Long> brandIdList, long timeAfter, 
			int limit, int offset);

	BrandDTO getBrandByBrandIdAsync(long id);

	public List<JSONObject> getBrandListInOrderByCategory(long areaId, List<Long> brandValue);
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<JSONObject> getBrandListInOrderByIds(List<Long> ids);
	
	/**
	 * 按sku销量排序
	 * @param brandIds
	 * @return
	 */
	public List<JSONObject> getBrandListOrderBySKUSaleNum(List<Long> brandIds);
}
