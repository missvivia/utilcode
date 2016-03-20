package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.meta.ProductPrice;
import com.xyl.mmall.itemcenter.param.ProductPriceParam;

public interface ProductPriceDao extends AbstractDao<ProductPrice> {

	/**
	 * 获取多维度价格列表
	 * @param productId
	 * @return
	 */
	public List<ProductPriceDTO> getProductPriceDTOByProductId(long productId);
	
	
	/**
	 * 根据productIds获取多维度价格列表
	 * @param productIds
	 * @return
	 */
	public List<ProductPrice> getProductPriceByProductIds(List<Long> productIds);
	
	/**
	 * 批量添加商品价格
	 * @param priceList
	 * @return
	 */
	public boolean addBulkProductPrice(List<ProductPrice> priceList);
	
	/**
	 * 删除商品价格
	 * @param skuId
	 * @return
	 */
	public int deleteProductPriceBySKUId(long skuId);
	
	/**
	 * 批量删除商品价格
	 * @param skuIds
	 * @return
	 */
	public boolean batchDeleteProductPriceBySKUIds(List<Long>skuIds);
	
	/**
	 * 根据价格区间搜索商品skuId
	 * @param productPriceParam
	 * @return
	 */
	public List<Long> getProductIdsByProductPriceParam(ProductPriceParam productPriceParam);
}
