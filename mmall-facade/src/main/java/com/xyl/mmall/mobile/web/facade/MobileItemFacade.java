package com.xyl.mmall.mobile.web.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.mobile.ios.facade.pageView.prdctlist.MobileSku;
import com.xyl.mmall.mobile.ios.facade.param.ProductSearchVO;
import com.xyl.mmall.mobile.web.vo.DetailPromotionVO;
import com.xyl.mmall.mobile.web.vo.PoProductListSearchVO;


public interface MobileItemFacade {

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
	 * 添加档期商品收藏
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean addPoProductIntoFavList(long userId, long poId);
	/**
	 * 删除档期商品收藏
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean removePoProductFromFavList(long userId, long poId);
	
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
	public BaseJsonVO getProductDTOListByProductUserFavParam(ProductUserFavParam productUserFavParam);
	
	/**
	 * 商家中产品列表搜索
	 * @param searchParam
	 * @return
	 */
	public BaseJsonVO searchProductSKU(ProductSKUSearchParam searchParam);
	
	public List<MobileSku> searchProductSKUBySearchParam(ProductSearchVO productSearchVO);
}
