package com.xyl.mmall.mobile.web.facade;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;

public interface MobileMainBrandFacade {

	/**
	 * 获取所有入驻品牌
	 * 
	 * @param param
	 * @param time
	 * @return
	 */
	public RetArg getAllBrandItemList(DDBParam param, long time);

	/**
	 * 获取推荐的品牌列表
	 * 
	 * @return
	 */
	public List<BrandItemDTO> getRecommendBrandItemList(long areaId, long userId, int count, boolean isApp);

	/**
	 * 获取单个品牌信息
	 * 
	 * @return
	 */
	public BrandItemDTO getBrandItemDTO(long brandId, long userId);

	/**
	 * 通过supplierBrandId得到后台中的品牌相关数据, 用于backend跳转过来的页面
	 * 
	 * @param supplierBrandId
	 * @return
	 */
	public SupplierBrandFullDTO getBrandFullDTO(long supplierBrandId);

	/**
	 * 通过supplierId得到品牌相关数据,用于mainsite(品牌数据带缓存)
	 * 
	 * @param supplierId
	 * @param userId
	 * @return
	 */
	public SupplierBrandFullDTO getBrandFullDTOBySupplierId(long supplierId);

	/**
	 * 通过supplierId得到后台中的品牌相关数据, 用于mainsite和wap
	 * 
	 * @param supplierId
	 * @param userId
	 * @param bMobile
	 * @return
	 */
	public SupplierBrandFullDTO getBrandFullDTOBySupplierId(long supplierId, long userId, boolean bMobile);

	/**
	 * 通过区域id和品牌id返相应的品牌店的列表
	 * 
	 * @param areaId
	 * @param brandId
	 * @return
	 */
	@Deprecated
	public List<BrandShopDTO> getBrandShops(long areaId, long brandId);

	/**
	 * 通过供应商品牌id获取对应的品牌门店列表
	 * 
	 * @param supplierId
	 * @return
	 */
	public List<BrandShopDTO> getBrandShopListBySupplierId(long supplierId);

	// /**
	// * 通过supplierId得到后台中的品牌相关数据, 不会返回品牌店的信息，用于mainsite
	// * @param supplierId
	// * @return
	// */
	// public SupplierBrandFullDTO
	// getBrandFullDTOByBySupplierIdWithOutbrandShops(long supplierId);
	/**
	 * 返回品牌关注的状态，是否被用户关注
	 * 
	 * @param suerId
	 * @param brandId
	 * @return
	 */
	public boolean getBrandCollectionState(long userId, long brandId);

	/**
	 * 添加品牌收藏
	 * 
	 * @param userId
	 * @param brandiD
	 * @return
	 */
	public boolean addBrandCollection(long userId, long brandId);

	/**
	 * 移除品牌收藏
	 * 
	 * @param userId
	 * @param brandiD
	 * @return
	 */
	public boolean removeBrandCollection(long userId, long brandId);

	/**
	 * 通过userId返回对应的品牌关注列表，这个接口用在主站的侧边栏收藏点击
	 * 
	 * @param param
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public RetArg getBrandUserFavListByUserId(DDBParam param, long userId, long areaId);

	/**
	 * 返回个人用户的收藏的品牌列表，比getBrandUserFavListByUserId结果更加详细
	 * 
	 * @param param
	 * @param time
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public RetArg getFavbrandList(DDBParam param, long time, long userId, long areaId);

	/**
	 * 返回个人用户的收藏的品牌列表，比getBrandUserFavListByUserId结果更加详细，用于移动端
	 * 
	 * @param param
	 * @param time
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public RetArg getFavbrandListApp(DDBParam param, long time, long userId, long areaId);

	/**
	 * 返回主站的入住品牌列表和详细情况
	 * 
	 * @param param
	 * @param userId
	 * @param begin
	 * @param areaId
	 * @return
	 */
	public RetArg getAllBrandList(DDBParam param, long userId, String begin, long areaId);

	/**
	 * 返回主站的入住品牌列表和详细情况，用于移动端
	 * 
	 * @param param
	 * @param userId
	 * @param time
	 * @param areaId
	 * @return
	 */
	public RetArg getAllBrandListApp(DDBParam param, long userId, long time, long areaId);

	/**
	 * 返回主站的入住品牌列表和详细情况，用于移动端(可以使用index作为首字母查询)
	 * 
	 * @param param
	 * @param userId
	 * @param time
	 * @param areaId
	 * @param index
	 * @return
	 */
	public RetArg getBrandListAppByIndex(DDBParam param, long userId, long time, long areaId, String index);

	/**
	 * 返回所在区域的当前时间相关档期列表
	 * 
	 * @param brandId
	 * @param areaId
	 * @return
	 */
	public POListDTO getPOList(long brandId, long areaId);

	/**
	 * 返回所在区域的未来时间相关档期列表
	 * 
	 * @param brandId
	 * @param areaId
	 * @param dayAfter
	 * @return
	 */
	public POListDTO getPOListFuture(long brandId, long areaId, int dayAfter);

	/**
	 * 判断用户是否收藏对应的档期
	 * 
	 * @param poId
	 * @param userId
	 * @return
	 */
	public boolean isPOFavoredByUser(long poId, long userId);

	/**
	 * 判断用户是否收藏对应的品牌
	 * 
	 * @param brandId
	 * @param userId
	 * @return
	 */
	public boolean isBrandFavoredByUser(long brandId, long userId);

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

	/**
	 * 返回3G端所需要的品牌列表
	 * 
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public List<BrandItemDTO> getAllBrandForApp3G(long userId, long areaId);

	public List<JSONObject> getBrandListInOrderByCategory(long categoryId, long areaId);

}
