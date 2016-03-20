package com.xyl.mmall.timer.facade;

import com.netease.print.common.meta.RetArg;

/**
 * 退货相关的定时器
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月29日 上午9:10:24
 *
 */
public interface ReturnTimerFacade {

//	public static final String DISTRIBUTED_RETURN_COUPON_CODE = "THBT1220";
	
	/**
	 * 将退货申请状态设为“已取消”
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg setReturnStateToCanceled();
	
	/**
	 * 向JIT推送退货信息
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg pushReturnPackageToJIT();
	
	/**
	 * 回收优惠券
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg recycleCoupon();
	
	/**
	 * 退款成功，发10元的红包补贴
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg distributeReturnExpHb();
	
	/**
	 * 退款成功，回收红包
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg recycleHb();
	
	/**
	 * 正常件待退款
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg returnCash();

	/**
	 * 优惠券是否已经返回
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	boolean couponRecycled(long userId, long orderId);
}
