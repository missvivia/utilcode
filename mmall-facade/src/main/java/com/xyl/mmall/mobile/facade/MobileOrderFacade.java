/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.param.MobileOrderCommitAO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;

/**
 * @author hzjiangww
 *
 */
public interface MobileOrderFacade {
	/**
	 * 获得未读信息
	 * @param userId
	 * @param t1 待支付
	 * @param t2 代发货
	 * @param t3 已发货
	 * @return
	 */
	public BaseJsonVO unReadOrder(long userId);
	
	public BaseJsonVO cancelOrder(long userId,Long orderId,int type,String reason);
	
	public BaseJsonVO deleteOrder(long userId,Long orderId);
	
	
	public BaseJsonVO getOrderList(long userId,Integer type,MobilePageCommonAO ao);
	
	public BaseJsonVO getOrderDetail(long userId,long orderId,long appversion);
	
	
	
	/**
	 * 购物车提交
	 * @param skuId
	 * @return
	 */
	public BaseJsonVO genOrder(long userId,int areaCode,int os,long appversion,int useGiftMoney);
	
	public BaseJsonVO genOrderChange(long userId,int areaCode,int os, MobileOrderCommitAO ao,long appversion);
	
	public BaseJsonVO postOrder(long userId,int areaCode,int os, MobileOrderCommitAO ao,String token,long appversion);
	
	public BaseJsonVO changePay(long userId,int type,long orderId,String token,long appversion);
	
	public boolean paySuccess(long trd);
}