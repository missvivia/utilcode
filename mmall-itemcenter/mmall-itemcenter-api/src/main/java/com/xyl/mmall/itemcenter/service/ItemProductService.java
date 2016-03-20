/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.dto.SkuRecommendationDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSearchMainSiteParam;

/**
 * ItemProductService.java created by yydx811 at 2015年5月14日 下午7:56:37 商品service
 *
 * @author lhp,yydx811
 */
public interface ItemProductService {

	/**
	 * 按搜索条件分页取商品list
	 * 
	 * @param param
	 * @return
	 */
	public BasePageParamVO<ProductSKUDTO> searchProductSKU(ProductSKUSearchParam param);

	/**
	 * 根据商品Ids取商品信息
	 * 
	 * @param prodIds
	 * @return
	 */
	public List<ProductSKUDTO> getProductSKUDTOByProdIds(List<Long> prodIds);

	/**
	 * 根据商品的id获取商品的businessid
	 * 
	 * @param ids
	 * @return Map，其中key是商品的businessId，value是该businessId下的商品的ids（以逗号隔开）
	 */
	public Map<Long, StringBuilder> getProductBusinessIdsByIds(List<String> ids);

	/**
	 * 删除商品
	 * 
	 * @param productId
	 */
	public boolean deleteProduct(long businessId, long productId) throws ItemCenterException;

	/**
	 * 批量删除商品
	 * 
	 * @param productId
	 */
	public boolean batchDeleteProducts(Long businessId, List<Long> prodIds) throws ItemCenterException;

	/**
	 * 添加商品多库存
	 * 
	 * @param productSKUDTOList
	 * @return
	 */
	public List<ProductSKUDTO> addProductSKUs(List<ProductSKUDTO> productSKUDTOList) throws ItemCenterException;

	/**
	 * 添加商品
	 * 
	 * @param productSKUDTO
	 * @return
	 */
	public ProductSKUDTO addProductSKU(ProductSKUDTO productSKUDTO) throws ItemCenterException;

	/**
	 * 获取商品
	 * 
	 * @param skuDTO
	 * @param isGetAll
	 * @return ProductSKUDTO
	 */
	public ProductSKUDTO getProductSKUDTO(ProductSKUDTO skuDTO, boolean isGetAll);

	/**
	 * 根据商家Id和产品Id更新商品状态
	 * 
	 * @param prodId
	 * @param statusType
	 * @param modifyUserId
	 * @return
	 */
	public boolean updateProductSKUStatus(long prodId, ProductStatusType statusType, long modifyUserId);

	/**
	 * 根据商家Id更新商品状态
	 * 
	 * @param businessId
	 * @param statusType
	 * @param modifyUserId
	 */
	public void updateProductSKUStatusByBusinessId(long businessId, ProductStatusType statusType, long modifyUserId)
			throws ItemCenterException;

	/**
	 * 根据商家Id和产品Id批量更新商品状态
	 * 
	 * @param prodIds
	 * @param statusType
	 * @param modifyUserId
	 * @return
	 */
	public boolean batchUpdateProductSKUStatus(List<Long> prodIds, ProductStatusType statusType, long modifyUserId);

	/**
	 * 根据商家Id返回商品数 +
	 * 
	 * @param productStatusType
	 * @return
	 */
	public Map<ProductStatusType, Integer> countProductSKUByBusinessId(long businessId);

	/**
	 * 删除商品图片
	 * 
	 * @param productSKUId
	 * @param id
	 * @return -1 只剩一张图片不能删除
	 */
	public int deleteProdPic(long productSKUId, long id);

	/**
	 * 更新商品
	 * 
	 * @param productSKUDTO
	 * @return
	 */
	public int updateProductSKU(ProductSKUDTO productSKUDTO) throws ItemCenterException;

	/**
	 * 更新商品属性
	 * 
	 * @param addList
	 * @param delList
	 * @return
	 */
	public int updateProdParam(List<ProdParamDTO> addList, List<ProdParamDTO> delList);

	/**
	 * 获取在使用的商品属性选项数量
	 * 
	 * @param paramId
	 * @param paramOptionId
	 * @return
	 */
	public int countProdParamOptionInUse(long paramId, long paramOptionId);

	/**
	 * 获取在使用的商品规格选项数量
	 * 
	 * @param speciId
	 * @param speciOptionId
	 * @return
	 */
	public int countProdSpeciOptionInUse(long speciId, long speciOptionId);

	/**
	 * 按spuid获取商品数量
	 * 
	 * @param spuId
	 * @return
	 */
	public int countProductSKUBySPUId(long spuId);

	/**
	 * 
	 * @param basePageParamVO
	 * @param searchParam
	 * @return
	 */
	public BasePageParamVO<ProductSKUDTO> getProductSKUList(BasePageParamVO<ProductSKUDTO> basePageParamVO,
			ProductSearchMainSiteParam searchParam);

	/**
	 * 按购买数获取区间价格
	 * 
	 * @param skuId
	 * @param buyNum
	 * @return
	 */
	public ProductPriceDTO getProductPriceByBuyNum(long skuId, int buyNum);

	/**
	 * 根据商家Id取得商品类目Ids 场景一：取得店铺相关类目
	 * 
	 * @param businessId
	 * @return
	 */
	public List<Long> getCategoryNormalIdsByBusinessId(long businessId);

	/**
	 * 商品是否上架
	 * 
	 * @param productId
	 * @return
	 */
	public boolean isProductStatusOnline(long productId);

	/**
	 * 取得商品状态
	 * 
	 * @param productIds
	 * @return
	 */
	public Map<Long, Boolean> getProductStatusIsOnline(List<Long> productIds);

	/**
	 * 更新商品销量
	 * 
	 * @param skuId
	 * @param increment
	 * @return
	 */
	public int updateProductSKUSaleNum(long skuId, int increment);

	/**
	 * 批量更新商品销量
	 * 
	 * @param skuCartNumMap
	 * @return
	 */
	public boolean updateProductsSaleNum(Map<Long, Integer> skuCartNumMap);

	/**
	 * 按商品分类id获取品牌id列表
	 * 
	 * @param categoryNormalIds
	 * @param searchValue
	 * @param businessIds
	 * @return
	 */
	public List<Long> getBrandIdsByCategoryIds(List<Long> categoryNormalIds, String searchValue, Set<Long> businessIds);

	/**
	 * 根据搜索条件返回总数
	 * 
	 * @param param
	 * @return
	 */
	public int countProductSKUDTOBySearchParam(ProductSKUSearchParam param);

	/**
	 * 获取简略信息
	 * 
	 * @param skuId
	 * @return
	 */
	public ProductSKUDTO getProductSKUBreifInfo(long skuId);

	/**
	 * 根据spuids取商品信息
	 * 
	 * @param prodIds
	 * @return
	 */
	public List<ProductSKUDTO> getProductSKUDTOBySpuIds(List<Long> spuIds);

	/**
	 * 根据商家Id取得首页商品推荐
	 * 
	 * @param businessId
	 * @return
	 */
	public List<SkuRecommendationDTO> getSKuRecommendationListByBusinessId(long businessId);

	/**
	 * 新增或者编辑首页推荐
	 * 
	 * @param skuRecommendationDTOs
	 * @return
	 */
	public boolean addOrUpdateSkuRecommendationDTOs(List<SkuRecommendationDTO> skuRecommendationDTOs)
			throws ItemCenterException;

	/**
	 * 获取需要同步的商品数量
	 * @param spu
	 * @return
	 */
	public int countSyncSKUBySPU(ItemSPU spu);

	/**
	 * 获取要同步skuIds
	 * @param spu
	 * @return
	 */
	public List<Long> getSyncSKUIdBySPU(ItemSPU spu);

	/**
	 * 更新同步商品信息
	 * @param ids
	 * @param spu
	 * @return
	 */
	public int syncSKUByIds(String ids, ItemSPU spu);
}
