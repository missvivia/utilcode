/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade;

import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.backend.vo.ProductSKULimitRecordVO;
import com.xyl.mmall.cms.vo.ItemSKUBriefVO;

/**
 * ItemSKUFacade.java created by yydx811 at 2015年11月19日 下午11:54:26
 * cms商品facade接口
 *
 * @author yydx811
 */
public interface ItemSKUFacade {

	/**
	 * 根据skuid获取商品简略信息
	 * @param skuId
	 * @return
	 */
	public ItemSKUBriefVO getItemBriefInfo(long skuId);
	
	/**
	 * 获取限购记录
	 * @param limitConfigVO
	 * @param skuId
	 * @param userId
	 * @return
	 */
	public ProductSKULimitRecordVO getItemLimitConfigFromCacheAndDB(
			ProductSKULimitConfigVO limitConfigVO, long skuId, long userId);
	
	/**
	 * 同步限购购买数
	 * @param skuId
	 * @param userId
	 * @return int 大于等于0成功返回购买数，-1限购配置不存在，-2不在限购时间内，-3同步失败
	 */
	public int syncCache(long skuId, long userId);
	
	/**
	 * 更新限购记录
	 * @param skuId
	 * @param userId
	 * @param canBuyNum
	 * @return int 1成功，0其他失败，-1不存在限购配置，-2不在限购时间内，-3超过限购量
	 */
	public int updateLimitRecord(long skuId, long userId, int canBuyNum);
}
