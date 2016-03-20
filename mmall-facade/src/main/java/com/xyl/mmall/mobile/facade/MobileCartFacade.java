/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import java.util.List;

import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * @author hzjiangww
 *
 */
public interface MobileCartFacade {
	/**
	 * 在详情页增加detail 
	 * @param skuId
	 * @param count
	 * type 1: 重新购买
	 * @return
	 */
	public BaseJsonVO addToCart(long userId,Long skuId,int areaCode,Integer count,int type);
	

	/**
	 * 在购物车页面修改数量
	 * @return
	 */
	public BaseJsonVO updateInCartPage(long userId, int areaCode, Long skuId, Integer add, Integer del);
	
	/**
	 * 获得初略的购物车信息
	 * @param skuId
	 * @return
	 */
	public BaseJsonVO getCartInfo(long userId,int areaCode);
	/**
	 * 获得购物车详细信息
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public BaseJsonVO getCartDetail(long userId,int areaCode);

	/**
	 * 批量删除
	 * @param userId
	 * @param areaId
	 * @param skuId
	 * @return
	 */
	public BaseJsonVO deleteInCartPage(long userId, int areaId, List<Long> skuId);

}