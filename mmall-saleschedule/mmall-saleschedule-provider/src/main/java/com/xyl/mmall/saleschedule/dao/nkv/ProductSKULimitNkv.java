/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dao.nkv;

/**
 * ProductSKULimitNkv.java created by yydx811 at 2015年11月16日 下午11:56:06
 * 商品限购nkv接口
 *
 * @author yydx811
 */
public interface ProductSKULimitNkv {

	/**
	 * 变更用户商品限购数
	 * @param skuId
	 * @param userId
	 * @param deltaCount
	 * @param expire
	 * @return
	 */
	public int changeProductSKULimit(long skuId, long userId, int deltaCount, int expire);
	
	/**
	 * 清除某个商品的限购
	 * @param skuId
	 * @return
	 */
	public boolean clearProductSKULimit(long skuId);
	
	/**
	 * 获取用户限购购买记录
	 * @param skuId
	 * @param userId
	 * @return
	 */
	public int getProductSKULimit(long skuId, long userId);
	
	/**
	 * 设置用户商品限购数，要set先判断是否存在，或处理返回码-20004
	 * @param skuId
	 * @param userId
	 * @param total
	 * @param expire
	 * @return
	 */
	public boolean setProductSKULimit(long skuId, long userId, int total, int expire);
	
	/**
	 * 删除
	 * @param skuId
	 * @param userId
	 * @return
	 */
	public boolean delProductSKULimit(long skuId, long userId);
}
