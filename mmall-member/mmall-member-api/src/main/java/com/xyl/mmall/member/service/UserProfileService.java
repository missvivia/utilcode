/**
 * 
 */
package com.xyl.mmall.member.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.member.dto.UserLoginInfoDTO;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;

/**
 * 主站用户信息相关服务。
 * 
 * @author lihui
 *
 */
public interface UserProfileService {

	/**
	 * 根据用户ID获取用户信息。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户信息
	 */
	UserProfileDTO findUserProfileById(long userId);
	
	/**
	 * 根据用户ID列表获取用户信息列表。
	 * 
	 * @param userIdList
	 *            用户ID列表
	 * @return 用户信息
	 */
	List<UserProfileDTO> findUserProfileByIdList(List<Long> userIdList);

	/**
	 * 根据用户登录名获取用户信息。
	 * 
	 * @param userName
	 *            用户登录名
	 * @return 用户信息
	 */
	UserProfileDTO findUserProfileByUserName(String userName);

	/**
	 * 根据用户登录名获取用户信息；如果是初次登录将创建用户，并返回用户信息。
	 * 
	 * @param userName
	 *            用户登录名
	 * @param userIp
	 *            用户IP
	 * @return 用户信息
	 */
	UserProfileDTO upsertUserProfile(String userName, String userIp);

	/**
	 * 根据用户登录名获取用户信息；如果是初次登录将创建用户，并返回用户信息。
	 * 
	 * @param userName
	 *            用户登录名
	 * @param nickName
	 *            用户昵称
	 * @param userIp
	 *            用户IP
	 * @return 用户信息
	 */
	UserProfileDTO upsertUserProfileWithNickName(String userName, String nickName, String userIp);

	/**
	 * 根据用户登录名获取用户信息；如果是初次登录将创建用户，并返回用户信息。
	 * 
	 * @param userName
	 *            用户登录名
	 * @param nickName
	 *            用户昵称
	 * @param profileImage
	 *            用户头像
	 * @param userIp
	 *            用户IP
	 * @return 用户信息
	 */
	UserProfileDTO upsertUserProfileWithNickNameAndImage(String userName, String nickName, String profileImage,
			String userIp);

	/**
	 * 更新指定用户的手机号码。
	 * 
	 * @param userId
	 *            用户ID
	 * @param mobile
	 *            手机号码
	 * @param email
	 *            邮件地址
	 * @return 更新后的用户信息
	 */
	UserProfileDTO upsertUserMobileAndEmail(long userId, String mobile, String email);

	/**
	 * 更新指定的用户基本信息。
	 * 
	 * @param userProfile
	 *            用户信息
	 * @return 更新后的用户信息
	 */
	UserProfileDTO updateUserProfile(UserProfileDTO userProfile);

	/**
	 * 更新指定用户的昵称。
	 * 
	 * @param userName
	 *            用户名
	 * @param nickName
	 *            用户昵称
	 * @return 更新后的用户信息
	 */
	UserProfileDTO updateNickName(String userName, String nickName);

	/**
	 * 根据查询条件搜索用户列表。
	 * 
	 * @param searchParams
	 *            查询条件
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 用户列表
	 */
	List<UserProfileDTO> searchUserByParams(Map<Integer, String> searchParams, int limit, int offset);
	
	/**
	 * 根据查询条件搜索用户列表的数量。
	 * 
	 * @param searchParams
	 *            查询条件
	 * @return 用户列表数量
	 */
	int countUserByParams(Map<Integer, String> searchParams);

	/**
	 * @param userId
	 * @return
	 */
	UserLoginInfoDTO findLastLoginInfoById(long userId);
	
	/**
	 * 根据用户名搜
	 * @param userProfileConditionDTO
	 * @return
	 */
	public BasePageParamVO<UserProfileDTO> getUserListByUserCondition(UserProfileConditionDTO userProfileConditionDTO);
	
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
	 * @param profileDTO
	 * @return
	 */
	public UserProfileDTO updateUserBaseInfo(UserProfileDTO profileDTO);
	
	/**
	 * 添加用户
	 * @param userProfileDTO
	 * @return
	 */
	public UserProfileDTO addUser(UserProfileDTO userProfileDTO);
	
	
	/**
	 * 根据用户名list查询userId
	 * @param userNames
	 * @return
	 */
	public Map<String, Long> findUserIdsByUserNames(List<String>userNames);
}
