/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.UserPoint;

/**
 * UserPointDTO.java created by yydx811 at 2015年12月23日 下午3:30:34
 * 用户积分dto
 *
 * @author yydx811
 */
public class UserPointDTO extends UserPoint {

	/** 序列化id. */
	private static final long serialVersionUID = 8722484170734984053L;

	public UserPointDTO() {
	}
	
	public UserPointDTO(UserPoint obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
