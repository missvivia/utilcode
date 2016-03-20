/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.member.meta.UserLoginInfo;

/**
 * @author lihui
 *
 */
public interface UserLoginInfoDao extends AbstractDao<UserLoginInfo> {

	/**
	 * 根据用户ID查找登录信息。
	 * 
	 * @param userId
	 *            登录信息
	 * @param param
	 *            查询限制条件
	 * @return 登录信息列表
	 */
	List<UserLoginInfo> findByUserId(long userId, DDBParam param);

}
