package com.xyl.mmall.order.service;

import java.util.Collection;
import java.util.List;

import com.xyl.mmall.order.dto.SkuOrderStockDTO;

/**
 * 订单服务的Sku库存数据
 * 
 * @author dingmingliang
 * 
 */
public interface SkuOrderStockService {

	/**
	 * 获得指定SkuId的库存
	 * 
	 * @param skuId
	 * @return
	 */
	public SkuOrderStockDTO getSkuOrderStockDTOBySkuId(long skuId);

	/**
	 * 获得指定SkuId的库存
	 * 
	 * @param skuIdColl
	 * @return
	 */
	public List<SkuOrderStockDTO> getSkuOrderStockDTOListBySkuIds(Collection<Long> skuIdColl);

	/**
	 * 添加Sku的库存信息
	 * 
	 * @param skuOrderStockDTO
	 * @return
	 */
	public boolean addSkuOrderStockDTO(SkuOrderStockDTO skuOrderStockDTO);

	/**
	 * 保存Sku的库存信息
	 * 
	 * @param skuOrderStockDTOColl
	 * @return
	 */
	public boolean saveSkuOrderStockDTOColl(Collection<SkuOrderStockDTO> skuOrderStockDTOColl);
	
	/**
	 * 更新Sku库存信息
	 * @param skuId
	 * @param count
	 * @return
	 */
	public boolean updateSkuOrderStock(long skuId,int count);
	
	
	/**
	 * 删除Sku库存信息
	 * @param skuId
	 * @return
	 */
	public boolean deleteSkuOrderStock(long skuId);
	
	/**
	 * 批量删除商品库存信息
	 * @param skuIds
	 * @return
	 */
	public boolean bacthDeleteSkuOrderStock(List<Long>skuIds);
}
