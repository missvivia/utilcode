package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProductSkuDetail;

public interface ProductSKUDetailDao extends AbstractDao<ProductSkuDetail> {
	
	/**
	 * 根据商品Id删除detail
	 * @param proId
	 * @return
	 */
	public boolean deleteDetailByProductId(long proId);

	/**
	 * 添加商品详情
	 * @param skuDetail
	 * @return
	 */
	public int addProductDetail(ProductSkuDetail skuDetail);
	
	/**
	 * 获取商品详情
	 * @param skuDetail
	 * @return ProductSkuDetail
	 */
	public ProductSkuDetail getProductDetail(ProductSkuDetail skuDetail);
	
	/**
	 * 批量删除商品详情
	 * @param proIds
	 * @return
	 */
	public boolean batchDeleteProductSKUDetail(List<Long>proIds);
	
	/**
	 * 更新商品详情
	 * @param skuDetail
	 * @return
	 */
	public int updateProductDetail(ProductSkuDetail skuDetail);
}
