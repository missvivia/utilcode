/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.service;

import com.xyl.mmall.promotion.dto.UserPointDTO;

/**
 * UserPointService.java created by yydx811 at 2015年12月23日 下午3:20:18
 * 用户积分service
 *
 * @author yydx811
 */
public interface UserPointService {

	/**
	 * 按用户id获取积分
	 * @param userId
	 * @return
	 */
	public UserPointDTO getUserPointByUserId(long userId);
	
	/**
	 * 保存用户积分
	 * @param userPointDTO
	 * @return
	 */
	public UserPointDTO saveUserPoint(UserPointDTO userPointDTO);
}
