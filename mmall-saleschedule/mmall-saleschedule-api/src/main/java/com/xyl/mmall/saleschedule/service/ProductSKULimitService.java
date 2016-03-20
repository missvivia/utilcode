/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitRecordDTO;

/**
 * ProductSKULimitService.java created by yydx811 at 2015年11月17日 下午1:46:39
 * 商品限购service接口
 *
 * @author yydx811
 */
public interface ProductSKULimitService {

	/**
	 * 添加商品限购配置
	 * @param skuLimitConfigDTO
	 * @return
	 */
	public boolean addProductSKULimitConfig(ProductSKULimitConfigDTO skuLimitConfigDTO);

	/**
	 * 按skuId获取商品限购配置
	 * @param productSKUId
	 * @return
	 */
	public ProductSKULimitConfigDTO getProductSKULimitConfigBySkuId(long productSKUId);
	
	/**
	 * 更新商品限购配置
	 * @param skuLimitConfigDTO
	 * @return
	 */
	public boolean updateProductSKULimitConfig(ProductSKULimitConfigDTO skuLimitConfigDTO);
	
	/**
	 * 删除商品限购配置
	 * @param productSKUId
	 * @return
	 */
	public boolean deleteProductSKULimitConfig(long productSKUId);
	
	/**
	 * 添加限购购买记录 
	 * @param userId
	 * @param skuId
	 * @param deltaCount
	 * @return int 1成功，0其他失败，-1不存在限购配置，-2不在限购时间内，-3超过限购量
	 */
	public int changeProductSKULimitRecord(long userId, long skuId, int deltaCount);
	
	/**
	 * 获取用户购买记录，从db获取
	 * @param userId
	 * @param skuId
	 * @return
	 */
	public ProductSKULimitRecordDTO getProductSKULimitRecordNoCache(long userId, long skuId);
	
	/**
	 * 更新限购购买记录CMS
	 * @param userId
	 * @param skuId
	 * @param canBuyNum
	 * @return int 1成功，0其他失败，-1不存在限购配置，-2不在限购时间内，-3超过限购量
	 */
	public int updateProductSKULimitRecord(long userId, long skuId, int canBuyNum);
	
	
    /**
     * 在限购条件下商品是否允许购买   
     * @param userId
     * @param skuCountMap
     * @return map key:skuId value:result key=0，value=0时表示所有skuId可以购买
     */
	public Map<Long, Integer> getSkuIsAllowedBuyLimitResultMap(long userId,Map<Long, Integer> skuCountMap);
	
	
    /**
     * 获取商品限购信息，包括剩余购买数量
     * @param userId
     * @param skuIds
     * @return 
     */
	public Map<Long, ProductSKULimitConfigDTO> getProductSKULimitConfigDTOMap(long userId,List<Long> skuIds);
	
	
    /**
     * 批量添加限购购买记录
     * @param userId
     * @param skuCountMap
     * @return int 1成功，0其他失败，-1不存在限购配置，-2不在限购时间内，-3超过限购量
     * @throws ServiceException
     */
	public int batchChangeProductSKULimitRecords(long userId, Map<Long, Integer> skuCountMap) throws ServiceException;
	
	/**
	 * 从cache获取购买数量
	 * @param skuId
	 * @param userId
	 * @return
	 */
	public int getTotalBuyNumFromCache(long skuId, long userId);
	
	/**
	 * 同步限购购买数
	 * @param skuId
	 * @param userId
	 * @return int 大于等于0成功返回购买数，-1限购配置不存在，-2不在限购时间内，-3同步失败
	 */
	public int syncCache(long skuId, long userId);

	/**
	 * 获取购买数量
	 * @param skuId
	 * @param userId
	 * @return
	 */
	public int getTotalBuyNum(long skuId, long userId);
	
	/**
	 * 清空商品限购购买缓存
	 * @param skuId
	 * @return
	 */
	public boolean clearLimitCache(long skuId);
	
	/**
	 * 删除限购记录
	 * @param skuId
	 * @return
	 */
	public int deleteSKULimitRecordBySkuId(long skuId);
}
