/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.meta.UserProfile;

/**
 * @author lihui
 *
 */
public interface UserProfileDao extends AbstractDao<UserProfile> {

	/**
	 * 根据用户名字段获取用户信息。
	 * 
	 * @param userName
	 *            用户名字段
	 * @return 用户信息
	 */
	UserProfile findByUserName(String userName);
	
	
	/**
	 * 根据用户名字段List获取用户信息List。
	 * 
	 * @param userNameList
	 *            用户名字段list
	 * @return 用户信息
	 */
	public List<UserProfile> findByUserNames(List<String> userNameList);

	/**
	 * 获取指定查询条件和分页的用户列表。
	 * 
	 * @param searchParams
	 *            查询条件
	 * @param param
	 *            分页条件
	 * @return 用户列表
	 */
	List<UserProfile> findByParams(Map<String, String> searchParams, DDBParam param);

	/**
	 * 获取指定查询条件的用户列表数量。
	 * 
	 * @param searchParams
	 *            查询条件
	 * @return 用户列表数量
	 */
	int countByParams(Map<String, String> searchParams);

	/**
	 * 获取指定用户ID列表的用户信息
	 * 
	 * @param userIdList
	 *            用户ID列表
	 * @return 用户列表
	 */
	List<UserProfile> findByIdList(List<Long> userIdList);
	
	List<UserProfile> getUserListByUserCondition(UserProfileConditionDTO userProfileConditionDTO);
	
	
	/**
	 * 删除用户
	 * @param userName
	 * @return
	 */
	public boolean deleteUserProfileByUserName(String userName);
	
	/**
	 * 搜索用户信息
	 * @param basePageParamVO
	 * @param searchValue
	 * @return
	 */
	public BasePageParamVO<UserProfileDTO> queryUserList(BasePageParamVO<UserProfileDTO> basePageParamVO, String searchValue);
	
	/**
	 * 更新用户基本信息
	 * @param userProfile
	 * @return
	 */
	public UserProfileDTO updateUserBaseInfo(UserProfileDTO userProfile);
}
