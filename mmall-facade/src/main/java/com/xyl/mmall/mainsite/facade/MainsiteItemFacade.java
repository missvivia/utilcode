package com.xyl.mmall.mainsite.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.mainsite.vo.DetailPromotionVO;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;

public interface MainsiteItemFacade {

	public DetailPromotionVO getDetailPagePromotionInfo(long poId);

	public BaseJsonListResultVO getProductList(PoProductListSearchVO param);

	public int isFollowBrand(long loginId, long brandId);

	public int getCartSkuNum(long usrId, int areaId, long skuId);

	public boolean isIPAllowed(int areaCode, long poId);

	public BaseJsonVO detailSizeCheckForYouhua(long poId, long pid);

	public BaseJsonListResultVO getPoProductDTOList(PoProductListSearchVO param);

	/**
	 * 按类别获取商品
	 * @param param
	 * @return
	 */
	public BaseJsonListResultVO getProductDTOListByCategory(PoProductListSearchVO param);
	
	/**
	 * 按搜索条件获取商品
	 * 场景:我的货架补货页面
	 * 
	 * @param param
	 * @return
	 */
	public BaseJsonListResultVO getProductDTOListBySearchParam(PoProductListSearchVO param);

	/**
	 * 获取多维度价格列表
	 * @param productId
	 * @return
	 */
	public List<ProductPriceDTO> getProductPriceDTOByProductId(long productId);
	
	/**
	 * 添加商品收藏
	 * @param userId
	 * @param productId
	 * @return
	 */
	public int addPoProductIntoFavList(long userId, long productId);
	/**
	 * 删除商品收藏
	 * @param userId
	 * @param productId
	 * @return
	 */
	public boolean removePoProductFromFavList(long userId, long productId);
	
	/**
	 * 根据userId和poIds取用户商品收藏关系,key:poId value:'Y'
	 * @param userId
	 * @param poIds
	 * @return
	 */
	public Map<String, String>getPoProductFavListByUserIdOrPoIds(long userId, List<Long> poIds);
	
	
	/**
	 * 收藏商品list
	 * @param productUserFavParam
	 * @return
	 */
	public BaseJsonListResultVO getProductDTOListByProductUserFavParam(ProductUserFavParam productUserFavParam);
	
	
	
	
}
