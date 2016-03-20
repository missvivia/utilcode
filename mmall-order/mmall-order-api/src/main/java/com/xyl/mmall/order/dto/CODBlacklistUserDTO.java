package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.CODBlacklistUser;

/**
 * 黑名单信息DTO
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:51:57
 *
 */
public class CODBlacklistUserDTO extends CODBlacklistUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8315244889923601695L;

	/**
	 * 构造函数
	 */
	public CODBlacklistUserDTO() {
	}

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CODBlacklistUserDTO(CODBlacklistUser obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
}
