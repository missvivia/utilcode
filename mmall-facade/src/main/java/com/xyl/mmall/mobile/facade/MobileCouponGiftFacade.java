/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;

/**
 * @author hzjiangww
 *
 */
public interface MobileCouponGiftFacade {
	//获得优惠券列表，分页
	BaseJsonVO getUserCouponList(long userId,MobilePageCommonAO ao,Integer status);
	
	//获得红包列表，分页
	BaseJsonVO getUserGiftList(long userId,MobilePageCommonAO ao,Integer status,long appversion);
	/**
	 * 绑定优惠券
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public BaseJsonVO bindCoupon(long userId, int areaId,String couponCode);
}