package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.meta.ProductSKU;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSearchMainSiteParam;

public interface ProductSKUDao extends AbstractDao<ProductSKU>{
	
	/**
	 * 分页搜索商品
	 * @param param
	 * @return
	 */
	public List<ProductSKUDTO> searchProductSKU(ProductSKUSearchParam param);
	
	/**
	 * 根据商品Ids取商品信息
	 * @param prodIds
	 * @return
	 */
	public List<ProductSKU> getProductSKUList(List<Long> prodIds);
	
	/**
	 * 根据Id list取商品列表
	 * @param ids
	 * @return
	 */
	public List<ProductSKU> getProductSKUListByIds(List<Long>ids);
	
	/**
	 * 根据id获取sku的businessId
	 * @param ids
	 * @return businessId的list
	 */
	public List<ProductSKU> getProductBusinessIdsByIds(List<String> ids);
	
	/**
	 * 返回商品总数
	 * @param param
	 * @return
	 */
	public int countProductSKUDTOBySearchParam(ProductSKUSearchParam param);
	
	
	/**
	 * 根据商家Id返回商品数    +
	 * @param productStatusType
	 * @return
	 */
	public Map<ProductStatusType, Integer> countProductSKUByBusinessId(long businessId);

	/**
	 * 添加商品
	 * @param productSKU
	 * @return
	 */
	public long addProductSKU(ProductSKU productSKU);

	/**
	 * 获取商品
	 * @param sku
	 * @return ProductSKU
	 */
	public ProductSKU getProductSKU(ProductSKU sku);
	
	/**
	 * 批量删除商品
	 * @param proIds
	 * @param businessId
	 * @return
	 */
	public boolean batchDeleteProductSKU(List<Long>proIds,long businessId);
	
	/**
	 * 根据商家Id和商品Id删除商品
	 * @param businessId
	 * @param proId
	 * @return
	 */
	public boolean deleteProductBybusiIdAndProId(long businessId,long prodId);
	
	/**
	 * 根据产品Id更新商品状态
	 * @param prodId
	 * @param statusType
	 * @param modifyUserId
	 * @return
	 */
	public boolean updateProductSKUStatus(long prodId,ProductStatusType statusType,long modifyUserId);
	
	
	/**
	 * 根据商家Id更新商品状态
	 * @param businessId
	 * @param statusType
	 * @param modifyUserId
	 * @return
	 */
	public boolean updateProductSKUStatusByBusinessId(long businessId,ProductStatusType statusType,long modifyUserId);
	
	
	/**
	 * 根据商家Id和产品Id批量更新商品状态
	 * @param prodIds
	 * @param statusType
	 * @param modifyUserId
	 * @return
	 */
	public boolean batchUpdateProductSKUStatus(List<Long> prodIds,ProductStatusType statusType,long modifyUserId);
	
	/**
	 * 更新商品
	 * @param sku
	 * @return
	 */
	public int updateProductSKU(ProductSKU sku);

	/**
	 * 按spuid获取商品数量
	 * @param spuId
	 * @return
	 */
	public int countProductSKUBySPUId(long spuId);

	/**
	 * 按spuid和business分页
	 * @param basePageParamVO
	 * @param searchParam
	 * @param skuIds
	 * @return
	 */
	public BasePageParamVO<ProductSKUDTO> getProductSKUList(BasePageParamVO<ProductSKUDTO> basePageParamVO, 
			ProductSearchMainSiteParam searchParam, List<Long> skuIds);
	
	/**
	 * 根据商家Id取商品类目Id
	 * @param businessId
	 * @return
	 */
	public List<Long> getCategoryNormalIdsByBusinessId(long businessId);
	
	/**
	 * 商品是否上架
	 * @param productId
	 * @return
	 */
	public boolean isProductStatusOnline(long productId);
	
	/**
	 * 取得商品状态
	 * @param productIds
	 * @return
	 */
	public Map<Long, Boolean> getProductStatusIsOnline(List<Long> productIds);
	
	/**
	 * 根据商家Id取得所有商品Id
	 * @param businessId
	 * @return
	 */
	public List<Long> getProductIdsByBusinessId(long businessId);
	
	/**
	 * 更新商品销量
	 * @param skuId
	 * @param increment
	 * @return
	 */
	public int updateProductSKUSaleNum(long skuId, int increment);

	/**
	 * 按商品分类id获取品牌id列表
	 * @param categoryNormalIds
	 * @param searchValue
	 * @param businessIds
	 * @return
	 */
	public List<Long> getBrandIdsByCategoryIds(List<Long> categoryNormalIds, String searchValue, Set<Long> businessIds);
	
	/**
	 * 根据spuIds 获取商品列表
	 * @param spuIds
	 * @return
	 */
	public List<ProductSKU> getProductSKUListBySpuIds(List<Long> spuIds);
	
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
