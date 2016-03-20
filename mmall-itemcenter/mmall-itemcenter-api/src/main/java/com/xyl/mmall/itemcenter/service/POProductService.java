package com.xyl.mmall.itemcenter.service;

import java.util.List;

import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.CategoryGroupDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductFullDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.dto.PoSkuVo;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductSearchResultDTO;
import com.xyl.mmall.itemcenter.dto.ScheduleAuditData;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.PoDeleteProdVO;
import com.xyl.mmall.itemcenter.param.PoProductSo;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.param.ProductAddPoVO;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;

/**
 * 档期商品服务接口（切勿和商品管理中的商品混淆）
 * 
 * @author hzhuangluqian
 *
 */
public interface POProductService {

	/**
	 * 根据筛选条件类对商品样品库进行筛选
	 * 
	 * @param searchDTO
	 *            筛选条件
	 * @return
	 */
	public BaseSearchResult<ProductSearchResultDTO> searchProduct(POProductSearchParam searchDTO);

	/**
	 * 根据筛选条件类对已经添加到档期的商品进行筛选
	 * 
	 * @param searchDTO
	 *            筛选条件
	 * @return
	 */
	public BaseSearchResult<POProductDTO> searchAddPOProduct(POProductSearchParam searchDTO);

	/**
	 * 根据供应商id，档期id，货号，条形码获取已经添加到档期的sku
	 * 
	 * @param poId
	 *            档期id
	 * @param goodsNo
	 *            货号
	 * @param barCode
	 *            条形码
	 * 
	 * @return
	 */
	public PoSku getPoSku(long poId, String goodsNo, String barCode);

	/**
	 * 根据请求参数类把商品样本添加到档期商品中
	 * 
	 * @param req
	 *            请求参数对象
	 */
	public void addProductToPo(long supplierId, long poId, ProductAddPoVO vo);

	/**
	 * 根据请求参数对象档期商品移除
	 * 
	 * @param req
	 *            请求参数对象
	 */
	public void deleteProductFromPo(PoDeleteProdVO req);

	/**
	 * 更新档期商品的审核状态
	 * 
	 * @param status
	 *            审核状态
	 * @param reason
	 *            审核原因
	 * @param descp
	 *            审核描述
	 * @param productIds
	 *            更新的档期商品id列表
	 */
	public void updateProductStaus(StatusType status, String reason, String descp, List<Long> productIds);

	/**
	 * 更新档期sku的审核状态
	 * 
	 * @param status
	 *            审核状态
	 * @param reason
	 *            审核理由
	 * @param skuIds
	 *            更新的skuId列表
	 */
	public void updateSkuStaus(StatusType status, String reason, List<Long> skuIds);

	/**
	 * 更新档期商品
	 * 
	 * @param productParam
	 */
	public void saveProduct(ProductSaveParam productParam);

	/**
	 * 查询某po下是否有添加的商品
	 * 
	 * @param po
	 * @return
	 */
	public boolean isProductInPO(long po);

	/**
	 * 提交某个po下的商品资料和商品清单
	 * 
	 * @param po
	 */
	public void submitProduct(long po);

	/**
	 * 根据skuId列表获取sku信息列表
	 * 
	 * @param skuIds
	 * @return
	 */
	public List<POSkuDTO> getSkuDTOListBySkuId(List<Long> skuIds);

	/**
	 * 根据档期id和货号获取该档期下指定货号的商品列表
	 * 
	 * @param poId
	 *            档期id
	 * @param goodsNo
	 *            货号
	 * @return
	 */
	public List<PoProduct> getProductListByGoodsNo(long poId, String goodsNo);

	/**
	 * 查询指定档期下所有商品根据最低级类目分组的数量情况
	 * 
	 * @param poId
	 *            档期id
	 * @return
	 */
	public List<CategoryGroupDTO> getProductGroupByCategory(long poId);

	/**
	 * 档期中的商品按类目排序的操作接口
	 * 
	 * @param poId
	 *            档期id
	 * @param sortList
	 *            类目id的排序列表
	 */
	public void sortProductByCategory(long poId, List<Long> sortList);

	/**
	 * 根据档期id获取PoSku对象列表
	 * 
	 * @param poId
	 *            档期id
	 * @return
	 */
	public List<PoSku> getSkuListByPo(long poId);

	/**
	 * 根据档期id获取PoSkuDTO对象列表
	 * 
	 * @param poId
	 *            档期id
	 * @return
	 */
	public List<POSkuDTO> getSkuDTOListByPo(long poId);

	/**
	 * 档期中的商品单个排序的操作接口
	 * 
	 * @param poId
	 *            档期id
	 * @param sortList
	 *            档期商品id的排列顺序
	 */
	public void sortProductBySingle(long poId, List<Long> sortList);

	/**
	 * 根据档期id获取档期商品的统计字段对象
	 * 
	 * @param poId
	 *            档期id
	 * @return
	 */
	public ScheduleAuditData getItemScheduleAuditData(long poId);

	/**
	 * 判断传入的档期id列表中的档期的商品和sku是否都通过了审核
	 * 
	 * @param poIds
	 *            档期id列表
	 * @return
	 */
	public List<Boolean> isItemReviewPass(List<Long> poIds);

	/**
	 * 查询指定商家某条形码所添加的档期id列表
	 * 
	 * @param supplierId
	 *            商家id
	 * @param barCode
	 *            条形码
	 * @return
	 */
	public List<Long> getPOByBarCode(long supplierId, String barCode);

	/**
	 * 根据商家id，档期id，货号，色号查询档期商品
	 * 
	 * @param supplierId
	 *            商家id
	 * @param poId
	 *            档期id
	 * @param goodsNo
	 *            货号
	 * @param colorNum
	 *            色号
	 * @return
	 */
	public PoProduct getPoProduct(long supplierId, long poId, String goodsNo, String colorNum);

	/**
	 * 根据档期id和档期商品id获取档期商品对象
	 * 
	 * @param poId
	 *            档期id
	 * @param pid
	 *            档期商品id
	 * @return
	 */
	public PoProduct getPoProduct(long poId, long pid);

	/**
	 * 根据档期商品id获取档期商品
	 * 
	 * @param pid
	 * @return
	 */
	public PoProduct getPoProduct(long pid);

	/**
	 * 批量添加商品到档期的操作接口
	 * 
	 * @param product
	 *            要添加到档期的sku对应的档期商品对象
	 * @param poSku
	 *            要添加到档期的sku
	 * @param rawProductId
	 *            商品库中的对应的相同货号色号的商品id
	 */
	public void BatchAddProductToPo(PoProduct product, PoSku poSku, long rawProductId);

	/**
	 * 根据档期商品id获取档期商品DTO对象
	 * 
	 * @param productId
	 *            档期商品id
	 * @return
	 */
	public POProductDTO getProductDTO(long productId);

	/**
	 * 获取档期id列表中每个档期专柜同款的档期商品个数
	 * 
	 * @param poIds
	 *            档期商品id列表
	 * @param sameAsShop
	 *            是否专柜同款
	 * @return
	 */
	public List<Integer> getProductCount(List<Long> poIds, int sameAsShop);

	/**
	 * 根据档期id获取档期商品对象列表
	 * 
	 * @param poId
	 *            档期id
	 * @return
	 */
	public List<PoProduct> getProductListByPo(long poId);

	/**
	 * 获取某个档期id下所指定的档期商品id列表的档期商品对象
	 * 
	 * @param poId
	 *            档期id
	 * @param pids
	 *            要返回档期商品的id列表
	 * @return
	 */
	public List<POProductDTO> getProductDTOList(long poId, List<Long> pids);

	/**
	 * 根据档期id获取档期商品DTO对象列表
	 * 
	 * @param poId
	 *            档期id
	 * @return
	 */
	public List<POProductDTO> getProductDTOListByPo(long poId);

	/**
	 * 返回相应的档期商品id和图片类型的图片链接
	 * 
	 * @param pid
	 *            档期商品id
	 * @param type
	 *            图片类型
	 * @return
	 */
	public List<String> getProductPic(long pid, PictureType type);

	/**
	 * 根据档期sku的id获取档期skuDTO对象
	 * 
	 * @param skuId
	 *            档期中的sku id
	 * @return
	 */
	public POSkuDTO getPOSkuDTO(long skuId);

	/**
	 * 根据档期商品id获取信息比较全的档期商品DTO对象
	 * 
	 * @param productId
	 *            档期商品id
	 * @return
	 */
	public PoProductFullDTO getProductFullDTO(long productId);

	public PoProduct getPoProductByPoIdAndProduct(long productId, long poId);

	public PoProduct copyProduct(ProductFullDTO rawProduct, long poId);

	public PoProductFullDTO getProductFullDTO(long supplierId, long poId, String goodsNo, String colorNum);

	public Category getCategoryBySkuId(long skuId);

	public SkuSpecMap getSkuSpecMapBySkuIdAndPoId(long skuId, long poId);

	public List<PoSku> getPoSkuListByParam(PoSkuSo so);

	public PoSku getSku(long poId, String barCode);

	public List<PoProduct> getPoProductListByName(String productName);

	public List<PoProduct> getPoProductByGoodsNo(String goodsNo);

	public PoProduct getProductBySkuId(long skuId);

	public SkuSpecMap getSkuSpecMapByPoIdProductIdSkuId(long poId, long productId, long skuId);

	public List<PoSkuVo> getPoSkuVosByParam(PoSkuSo so);

	public Long getPoSkuVosCountByParam(PoSkuSo so);

	public PoProductFullDTO getProductFullDTOForMainSite(long productId);

	public List<CategoryGroupDTO> getProductGroupByCategoryCache(long poId);

	public List<POProductDTO> getProductDTOListByPoCache(long poId);

	public List<PoSku> getPoSkusByIds(List<Long> ids);

	public List<ProdPicMap> getProductPicListCache(List<Long> pids, PictureType type);

	public List<ProdPicMap> getProductPicList(List<Long> pids, PictureType type);

	public List<Long> getSkuIds(long poId, long pid);

	public List<PoProductVo> getPoProductosByParam(PoProductSo so);

	public Long getPoProductVosCountByParam(PoProductSo so);

	public PoSku getPoSkuByBarCode(String barcode);

	public void deleteProduct(long poId, long productId);

	public List<String> getProductPicNoCache(long pid, PictureType type);

	public int getProductNumOfStatus(long poId, StatusType status);

	public int getSkuNumOfStatus(long poId, StatusType status);

	public int getProductNum(long poId);

	public int getSkuNum(long poId);

	public void productOnline(long poId);

	/**
	 * 根据类目id获取档期商品DTO对象列表
	 * 
	 * @param catgoryId
	 *            档期id
	 * @return
	 */
	public List<POProductDTO> getProductDTOListByCatgory(long catgoryId);
	
	public List<POProductDTO> getProductDTOListByCategoryAndCache(long catgoryId);
}
