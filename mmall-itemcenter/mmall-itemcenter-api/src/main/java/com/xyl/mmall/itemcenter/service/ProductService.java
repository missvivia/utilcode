package com.xyl.mmall.itemcenter.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.ExcelExportProduct;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSearchResultDTO;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;
import com.xyl.mmall.itemcenter.meta.ProductParameter;
import com.xyl.mmall.itemcenter.meta.SizeAssist;
import com.xyl.mmall.itemcenter.meta.Sku;
import com.xyl.mmall.itemcenter.param.BatchUploadPicParam;
import com.xyl.mmall.itemcenter.param.BatchUploadSizeParam;
import com.xyl.mmall.itemcenter.param.ChangeProductNameParam;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.ProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProductUniqueParam;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;

public interface ProductService {

	/**
	 * 添加单个商品
	 * 
	 * @param product
	 * @return TODO
	 */
	public int saveProduct(ProductSaveParam product);

	/**
	 * 根据搜索条件查找商品
	 * 
	 * @param searchDTO
	 * @return
	 */
	public BaseSearchResult<ProductSearchResultDTO> searchProduct(ProductSearchParam searchDTO);

	/**
	 * 保存商品图片，如果该类型的商品图片没有，则新增，否则更新
	 * 
	 * @param pid
	 *            商品id
	 * @param type
	 *            图片类型
	 * @param pathList
	 *            图片地址列表
	 * @return
	 */
	public boolean saveProductPic(long pid, PictureType type, List<String> pathList);

	/**
	 * 获取商品参数列表
	 * 
	 * @param categoryId
	 *            第一级类目id
	 * @return
	 */
	public List<ProductParamDTO> getProductParamList(long categoryId);

	/**
	 * 根据样本库商品id获取样本库商品
	 * 
	 * @param productId
	 *            商品id
	 * @return
	 */
	public ProductDTO getProductDTO(long productId);

	/**
	 * 根据样本库商品id获取sku列表
	 * 
	 * @param productId
	 *            商品id
	 * @return
	 */
	public List<Sku> getSkuList(long supplierId, long productId);

	/**
	 * 删除商品
	 * 
	 * @param productId
	 */
	public void deleteProduct(long supplierId, long productId);

	/**
	 * 根据尺码助手id获取尺码助手对象（有缓存）
	 * 
	 * @param id
	 * @return
	 */
	public SizeAssist getSizeAssist(long id);

	/**
	 * 根据尺码助手id获取尺码助手对象
	 * 
	 * @param id
	 * @return
	 */
	public SizeAssist getSizeAssistNoCache(long id);

	/**
	 * 保存尺码助手
	 * 
	 * @param sizeAssist
	 * @return
	 */
	public SizeAssist saveSizeAssist(SizeAssist sizeAssist);

	/**
	 * 批量查找尺码助手
	 * 
	 * @param supplierId
	 *            商家id
	 * @param limit
	 *            要查找的尺码助手个数
	 * @param offset
	 *            偏移量
	 * @return
	 */
	public BaseSearchResult<SizeAssist> getSizeAssistList(long supplierId, int limit, int offset);

	/**
	 * 删除尺码助手
	 * 
	 * @param supplierId
	 * @param id
	 * @return
	 */
	public int deleteSizeAssistById(long supplierId, long id);

	/**
	 * 根据商家id，货号，色号获取商品库中的商品对象
	 * 
	 * @param supplierId
	 *            商家id
	 * @param goodsNo
	 *            货号
	 * @param colorNum
	 *            色号
	 * @return
	 */
	public Product getProductByUniq(long supplierId, String goodsNo, String colorNum);

	/**
	 * 根据商品库中的商品id获取商品对象
	 * 
	 * @param pid
	 *            商品库中的商品id
	 * @return
	 */
	public Product getProduct(long pid);

	/**
	 * 根据商品库商品id获取商品详情DTO
	 * 
	 * @param pid
	 *            商品库商品id
	 * @return
	 */
	public ProductFullDTO getProductFullDTOById(long pid);

	public List<Long> getProductIdListByUniq(List<ProductUniqueParam> params);

	public String batchSaveSize(long supplierId, List<String> header, BatchUploadSizeParam param);

	/**
	 * 获取某个商家商品库中同一个货号的所有商品列表
	 * 
	 * @param supplierId
	 *            商家id
	 * @param goodsNo
	 *            货号
	 * @return
	 */
	public List<Product> getProductListByGoodsNo(long supplierId, String goodsNo);

	/**
	 * 批量删除商品
	 * 
	 * @param supplierId
	 *            商家id
	 * @param ids
	 *            商品库中的商品id列表
	 */
	public void deleteProducts(long supplierId, List<Long> ids);

	/**
	 * 存储商品自定义编辑区
	 * 
	 * @param supplierId
	 *            商家id
	 * @param goodsNo
	 *            货号
	 * @param colorNum
	 *            色号
	 * @param html
	 *            nos地址
	 */
	public void batchSaveCustomHtml(long supplierId, String goodsNo, String colorNum, String html);

	/**
	 * 根据商品属性id获取属性下拉对象列表
	 * 
	 * @param optId
	 *            商品属性id
	 * @return
	 */
	public List<ProductParamOption> getOptionList(long optId);

	public String batchSaveProductPic(long supplierId, long pid, BatchUploadPicParam param);

	public List<ExcelExportProduct> searchExportProduct(long supplierId, List<Long> pids);

	public List<ExcelExportProduct> searchExportProduct(ProductSearchParam searchParam);

	/**
	 * 根据最低级类目id获取该类目下所有商品属性的下拉菜单选项的Map，key是下拉选项的文字描述。value是下拉选项对象id
	 * 
	 * @param lowCategoryId
	 *            最低级类目id
	 * @return
	 */
	public Map<String, Long> getProductParamOptMap(long lowCategoryId);

	public List<Map<String, Object>> getProductParamExport(List<Long> categoryList);

	/**
	 * 批量修改商品名称服务
	 * 
	 * @param param
	 * @return
	 */
	public boolean batchChangeProductName(ChangeProductNameParam param);

	/**
	 * 保存商品信息
	 * 
	 * @param productParam
	 * @return
	 */
	public int SingleSaveProduct(ProductSaveParam productParam);

	public void saveCustomizedSizeValue(long pid, int recordIndex, List<SizeColumnParam> colList, List<String> sizeValue);

	public List<ProductDTO> searchProduct(POProductSearchParam searchDTO);

	/**
	 * 根据商品库商品id获取商品详情DTO，包括sku列表，图片
	 * 
	 * @param productId
	 * @return
	 */
	public ProductFullDTO getProductFullDTO(long productId);

	/**
	 * 获取商品库中的sku对象
	 * 
	 * @param supplierId
	 *            商家id
	 * @param goodsNo
	 *            货号
	 * @param colorNum
	 *            色号
	 * @param barCode
	 *            条形码
	 * @return
	 */
	public Sku getSku(long supplierId, String goodsNo, String colorNum, String barCode);

	/**
	 * 获取商品库中的sku对象
	 * 
	 * @param supplierId
	 *            商家id
	 * @param barCode
	 *            条码
	 * @return
	 */
	public Sku getSku(long supplierId, String barCode);

	/**
	 * 根据商家id和条形码获取对应的商品的货号、色号、类目
	 * 
	 * @param supplierId
	 *            商家id
	 * @param barcode
	 *            条形码
	 * @return
	 */
	public Map<String, String> getGoodsNoAndColorNum(long supplierId, String barcode);

	public void batchUploadSizeAddSku(Sku sku, List<SizeColumnParam> colList, List<String> sizeValue);

	/**
	 * 根据商品属性id获取商品属性DTO
	 * 
	 * @param id
	 *            商品属性id
	 * @return
	 */
	public ProductParamDTO getProductParamDTO(long id);

	/**
	 * 根据商家id，货号，色号获取商品库中的商品详情DTO（不包含sku和图片）
	 * 
	 * @param supplierId
	 *            商家id
	 * @param goodsNo
	 *            货号
	 * @param colorNum
	 *            色号
	 * @return
	 */
	public ProductFullDTO getProductFullDTOByUniq(long supplierId, String goodsNo, String colorNum);

	public Map<String, Long> getProductParamForBat(long paramId);

	public ProductParameter getProductParam(long paramId);

	/**
	 * 根据商家id，货号获取商品库中的商品详情DTO（不包含sku和图片）
	 * 
	 * @param supplierId
	 *            商家id
	 * @param goodsNo
	 *            货号
	 * @return
	 */
	public List<ProductFullDTO> getProductFullDTOListByGoodsNo(long supplierId, String goodsNo);
	
	/**
	 * 获取多维度价格列表
	 * @param productId
	 * @return
	 */
	public List<ProductPriceDTO> getProductPriceDTOByProductId(long productId);
	
	
	/**
	 * 根据productIds获取多维度价格列表
	 * @param productId
	 * @return
	 */
	public Map<String,List<ProductPriceDTO>> getProductPriceDTOByProductIds(List<Long> productIds);


}
