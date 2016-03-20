/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.backend.vo.ProductSKUBackendVO;
import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;

/**
 * ProductFacade.java created by yydx811 at 2015年5月14日 下午7:40:56 商品facade
 *
 * @author yydx811
 */
public interface ProductFacade {

	/**
	 * 分页搜索商品
	 * 
	 * @param searchParam
	 * @return
	 */
	public BasePageParamVO<ProductSKUBackendVO> searchProductSKU(ProductSKUSearchParam searchParam);

	/**
	 * 根据商品Ids获取商品信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<ProductSKUBackendVO> getProductSKUVO(List<Long> prodIds);

	/**
	 * 批量删除商品
	 * 
	 * @param productIds
	 * @return
	 */
	public int deleteProducts(long businessId, List<Long> productIds);

	/**
	 * 删除单个商品
	 * 
	 * @param businessId
	 * @param productId
	 * @return
	 */
	public int deleteProduct(long businessId, Long productId);

	/**
	 * 添加商品多库存
	 * 
	 * @param productSKUDTOList
	 * @return
	 */
	public int addProductSKUs(List<ProductSKUDTO> productSKUDTOList);

	/**
	 * 获取商品
	 * 
	 * @param skuDTO
	 * @param isGetAll
	 * @return ProductSKUBackendVO
	 */
	public ProductSKUBackendVO getProductSKUVO(ProductSKUDTO skuDTO, boolean isGetAll);

	/**
	 * 根据商家Id和产品Id更新商品状态
	 * 
	 * @param prodId
	 * @param action
	 * @param modifyUserId
	 * @return
	 */
	public boolean updateProductSKUStatus(long prodId, String action, long modifyUserId);

	/**
	 * 根据商家Id和产品Id批量更新商品状态
	 * 
	 * @param prodIds
	 * @param action
	 * @param modifyUserId
	 * @return
	 */
	public boolean batchUpdateProductSKUStatus(List<Long> prodIds, String action, long modifyUserId);

	/**
	 * 跟新库存
	 * 
	 * @param prodId
	 * @param count
	 * @param businessId
	 * @return
	 */
	public int updateProductSKUStock(long prodId, int count, long businessId);

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
	 * @param limitConfigDTO
	 * @return
	 */
	public int updateProductSKU(ProductSKUDTO productSKUDTO, ProductSKULimitConfigDTO limitConfigDTO);

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
	 * 获取在使用的商品属性数量
	 * 
	 * @param paramId
	 * @return
	 */
	public int countProdParamInUse(long paramId);

	/**
	 * 获取在使用的商品规格数量
	 * 
	 * @param speciId
	 * @return
	 */
	public int countProdSpeciInUse(long speciId);

	/**
	 * 按spuid获取商品数量
	 * 
	 * @param spuId
	 * @return
	 */
	public int countProductSKUBySPUId(long spuId);

	/**
	 * 根据搜索条件返回总数
	 * 
	 * @param param
	 * @return
	 */
	public int countProductSKUDTOBySearchParam(ProductSKUSearchParam param);

	/**
	 * 获取限购配置
	 * 
	 * @param skuId
	 * @return
	 */
	public ProductSKULimitConfigVO getProductSKULimitConfig(long skuId);

	public boolean recoverOrderSkuLimit(long userId, Map<Long, OrderSkuDTO> orderSkuMap);

	/**
	 * 添加商品
	 * 
	 * @param productSKUDTO
	 * @return
	 */
	public long addProductSKU(ProductSKUDTO productSKUDTO);

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
