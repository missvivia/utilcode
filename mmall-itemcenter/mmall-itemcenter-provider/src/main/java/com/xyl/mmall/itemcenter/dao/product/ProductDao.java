package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSearchParam;

/**
 * 商品样本库dao
 * 
 * @author hzhuangluqian
 *
 */
public interface ProductDao extends AbstractDao<Product> {
	/**
	 * 添加商品到样本库
	 * 
	 * @param product
	 * @return
	 */
	public Product addProduct(Product product);

	/**
	 * 根据商品id获取商品
	 * 
	 * @param productId
	 * @return
	 */
	public Product getProduct(long productId);

	/**
	 * 根据商品列表页的筛选类查询商品
	 * 
	 * @param param
	 * @return
	 */
	public BaseSearchResult<Product> searchProduct(ProductSearchParam param);

	/**
	 * 根据PO商品添加页的商品筛选条件类查询商品
	 * 
	 * @param param
	 * @return
	 */
	public BaseSearchResult<Product> searchProduct(POProductSearchParam param);

	/**
	 * 返回时用某个尺码模板的商品列表
	 * 
	 * @param sizeTemplateId
	 *            尺码模板id
	 * @return
	 */
	public List<Product> getListBySizeTemplateId(long supplierId, long sizeTemplateId);

	public Long getProductId(long supplierId, String goodNo, String colorNum);

	public Product getProduct(long supplierId, String goodNo, String colorNum);

	public List<Product> getProductListByGoodsNo(long supplierId, String goodsNo);

	public List<Product> getListBySizeAssistId(long supplierId, long helpId);

	public List<Product> searchExportProduct(long supplierId, List<Long> pids);

	public List<Product> searchExportProduct(ProductSearchParam param);

	public boolean updateCustomHtmlState(long supplierId, String goodsNo, String colorNum, int state);

	public boolean updatePicState(long supplierId, String goodsNo, String colorNum, int state);

	public Product getProduct(long supplierId, long productId);

	public boolean updateProductName(long supplierId, long productId, String productName);

	public boolean updateThumb(long productId, String path);

	public boolean updateProductThumb(long supplierId, String goodsNo, String colorNum, String thumb);

	public List<PoProduct> getPoProductListByName(String productName);

	public List<Long> getProductId();

}
