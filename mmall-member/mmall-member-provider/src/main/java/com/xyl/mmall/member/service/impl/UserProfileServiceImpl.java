/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.member.dao.UserLoginInfoDao;
import com.xyl.mmall.member.dao.UserProfileDao;
import com.xyl.mmall.member.dto.UserLoginInfoDTO;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.UserSearchType;
import com.xyl.mmall.member.meta.UserLoginInfo;
import com.xyl.mmall.member.meta.UserProfile;
import com.xyl.mmall.member.service.UserProfileService;

/**
 * @author lihui
 *
 */
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileDao userProfileDao;

	@Autowired
	private UserLoginInfoDao userLoginInfoDao;

	// @Autowired
	// private UniqueUserDao uniqueUserDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.user.service.UserProfileService#getUserInfoById()
	 */
	@Override
	@Cacheable(value = "userProfileOfId", key = "#userId")
	public UserProfileDTO findUserProfileById(long userId) {
		return new UserProfileDTO(userProfileDao.getObjectById(userId));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.user.service.UserProfileService#findUserProfileByUserName(java.lang.String)
	 */
	@Override
	@Cacheable(value = "userProfileOfUserName", key = "#userName")
	public UserProfileDTO findUserProfileByUserName(String userName) {
		return new UserProfileDTO(userProfileDao.findByUserName(userName));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#upsertUserProfile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@Transaction
	public UserProfileDTO upsertUserProfile(String userName, String userIp) {
		return upsertUserProfile(userName, null, null, userIp);
	}

	/**
	 * 根据用户名、昵称等信息创建/更新用户信息。
	 * 
	 * @param userName
	 *            用户名
	 * @param nickName
	 *            昵称
	 * @param userIp
	 *            用户IP
	 * @return 更新后的用户信息
	 */
	private UserProfileDTO upsertUserProfile(String userName, String nickName, String userImageURL, String userIp) {
		UserProfile userProfile = userProfileDao.findByUserName(userName);
		// 1.如数据库表中没有此用户名下的用户,则创建
		if (userProfile == null) {
			userProfile = addNewUserProfile(userName, nickName, userImageURL);
		} else if (StringUtils.isNotEmpty(nickName) || StringUtils.isNotEmpty(userImageURL)) {
			if (StringUtils.isNotEmpty(nickName) && StringUtils.isBlank(userProfile.getNickName())) {
				userProfile.setNickName(nickName);
			}
			if (StringUtils.isNotEmpty(userImageURL)) {
				userProfile.setUserImageURL(userImageURL);
			}
			userProfileDao.updateObjectByKey(userProfile);
		}
		if (userProfile == null) {
			return null;
		}
		// 2.记录用户操作信息
		long lastLoginTime = saveUserLoginLog(userProfile, userIp);
		// 3.创建返回的DTO
		return new UserProfileDTO(userProfile, lastLoginTime);
	}

	/**
	 * 插入用户登录信息。
	 * 
	 * @param userProfile
	 * @param userIp
	 * @return
	 */
	private long saveUserLoginLog(UserProfile userProfile, String userIp) {
		UserLoginInfo loginInfo = new UserLoginInfo();
		loginInfo.setLoginIp(userIp);
		loginInfo.setLoginTime(System.currentTimeMillis());
		loginInfo.setUserId(userProfile.getUserId());
		userLoginInfoDao.addObject(loginInfo);
		return -1;
	}

	/**
	 * 创建新的用户
	 * 
	 * @param userName
	 *            用户名
	 * @param nickName
	 *            昵称
	 * @param userImageURL
	 *            用户头像
	 * @return
	 */
	private UserProfile addNewUserProfile(String userName, String nickName, String userImageURL) {
		// try {
		// UniqueUser uniqueUser = new UniqueUser();
		// uniqueUser.setUserName(userName);
		// uniqueUser.setCreateTime(System.currentTimeMillis());
		// uniqueUserDao.addObject(uniqueUser);
		// } catch (DBSupportRuntimeException ex) {
		// // 兼容用户重复创建的情况
		// return userProfileDao.findByUserName(userName);
		// }
		long id = userProfileDao.allocateRecordId();
		if (id < 1l) {
			throw new DBSupportRuntimeException("Get generateId failed!");
		}
		UserProfile userProfile = new UserProfile();
		userProfile.setUserName(userName);
		userProfile.setNickName(nickName);
		userProfile.setUserImageURL(userImageURL);
		userProfile.setRegTime(System.currentTimeMillis());
		userProfile.setUserId(id);// TODO
		try {
			userProfileDao.addObject(userProfile);
		} catch (DBSupportRuntimeException ex) {
			// 兼容用户重复创建的情况
			userProfile = userProfileDao.findByUserName(userName);
		}
		return userProfile;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#upsertUserProfileWithNickName(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO upsertUserProfileWithNickName(String userName, String nickName, String userIp) {
		return upsertUserProfile(userName, nickName, null, userIp);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#upsertUserProfileWithNickNameAndImage(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO upsertUserProfileWithNickNameAndImage(String userName, String nickName, String profileImage,
			String userIp) {
		return upsertUserProfile(userName, nickName, profileImage, userIp);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#upsertUserMobileAndEmail(long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO upsertUserMobileAndEmail(long userId, String mobile, String email) {
		UserProfile user = userProfileDao.getObjectById(userId);
		if (user != null) {
			if (StringUtils.isNotBlank(mobile)) {
				user.setMobile(mobile);
			}
			if (StringUtils.isNotBlank(email)) {
				user.setEmail(email);
			}
			userProfileDao.updateObjectByKey(user);
			return new UserProfileDTO(user);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#updateUserProfile(com.xyl.mmall.member.dto.UserProfileDTO)
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO updateUserProfile(UserProfileDTO userProfile) {
		UserProfile user = userProfileDao.getObjectById(userProfile.getUserId());
		if (user != null && userProfile != null) {
			if (userProfile.getBirthDay() != 0) {
				user.setBirthDay(userProfile.getBirthDay());
			}
			if (userProfile.getBirthMonth() != 0) {
				user.setBirthMonth(userProfile.getBirthMonth());
			}
			if (userProfile.getBirthYear() != 0) {
				user.setBirthYear(userProfile.getBirthYear());
			}
			if (userProfile.getEmail() != null && !"".equals(userProfile.getEmail().trim())) {
				user.setEmail(StringUtils.trimToEmpty(userProfile.getEmail()));
			}

			if (StringUtils.isNotEmpty(userProfile.getNickName())) {
				user.setNickName(userProfile.getNickName());
			}
			if (userProfile.getGender() != null) {
				user.setGender(userProfile.getGender());
			}
			userProfileDao.updateObjectByKey(user);
			return new UserProfileDTO(user);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#updateNickName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO updateNickName(String userName, String nickName) {
		UserProfile userProfile = userProfileDao.findByUserName(userName);
		// 1.如数据库表中没有此用户名下的用户,则返回空。正常情况下此场景永远不应该发生。
		if (userProfile == null) {
			return null;
		}
		// 2.当更新的昵称不为空，同时数据库中的昵称为空，或者昵称是账号的前缀的时候，更新昵称。
		if (StringUtils.isNotEmpty(nickName) && (StringUtils.isBlank(userProfile.getNickName())
				|| StringUtils.startsWith(userName, userProfile.getNickName()))) {
			userProfile.setNickName(nickName);
			userProfileDao.updateObjectByKey(userProfile);
		}
		return new UserProfileDTO(userProfile);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#searchUserByParams(java.util.Map,
	 *      int, int)
	 */
	@Override
	public List<UserProfileDTO> searchUserByParams(Map<Integer, String> searchParams, int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		Map<String, String> paramMap = new HashMap<>();
		for (Integer key : searchParams.keySet()) {
			UserSearchType type = UserSearchType.getUserSearchTypeByIntValue(key);
			paramMap.put(type.getDesc(), searchParams.get(key));
		}
		List<UserProfile> userList = userProfileDao.findByParams(paramMap, param);
		List<UserProfileDTO> userDTOList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userList)) {
			for (UserProfile user : userList) {
				userDTOList.add(new UserProfileDTO(user));
			}
		}
		return userDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#countUserByParams(java.util.Map)
	 */
	@Override
	public int countUserByParams(Map<Integer, String> searchParams) {
		Map<String, String> paramMap = new HashMap<>();
		for (Integer key : searchParams.keySet()) {
			UserSearchType type = UserSearchType.getUserSearchTypeByIntValue(key);
			paramMap.put(type.getDesc(), searchParams.get(key));
		}
		return userProfileDao.countByParams(paramMap);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#findLastLoginInfoById(long)
	 */
	@Override
	public UserLoginInfoDTO findLastLoginInfoById(long userId) {
		DDBParam param = DDBParam.genParam1();
		param.setAsc(false);
		param.setOrderColumn("loginTime");
		List<UserLoginInfo> loginInfoList = userLoginInfoDao.findByUserId(userId, param);
		if (!CollectionUtils.isEmpty(loginInfoList)) {
			return new UserLoginInfoDTO(loginInfoList.get(0));
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.UserProfileService#findUserProfileByIdList(java.util.List)
	 */
	@Override
	public List<UserProfileDTO> findUserProfileByIdList(List<Long> userIdList) {
		List<UserProfile> userList = userProfileDao.findByIdList(userIdList);
		List<UserProfileDTO> userDTOList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userList)) {
			for (UserProfile user : userList) {
				userDTOList.add(new UserProfileDTO(user));
			}
		}
		return userDTOList;
	}

	@Override
	public BasePageParamVO<UserProfileDTO> getUserListByUserCondition(UserProfileConditionDTO userProfileConditionDTO) {
		List<UserProfile> userList = userProfileDao.getUserListByUserCondition(userProfileConditionDTO);
		List<UserProfileDTO> userDTOList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userList)) {
			for (UserProfile user : userList) {
				userDTOList.add(new UserProfileDTO(user));
			}
		}
		BasePageParamVO<UserProfileDTO> result = new BasePageParamVO<UserProfileDTO>();
		result.setHasNextPage(userProfileConditionDTO.isHasNext());
		result.setList(userDTOList);
		result.setTotal(userProfileConditionDTO.getTotalCount());
		return result;
	}

	@Override
	@Transaction
	public boolean deleteUserProfileByUserName(String userName) {
		return userProfileDao.deleteUserProfileByUserName(userName);
	}

	@Override
	public BasePageParamVO<UserProfileDTO> queryUserList(BasePageParamVO<UserProfileDTO> basePageParamVO,
			String searchValue) {
		return userProfileDao.queryUserList(basePageParamVO, searchValue);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO updateUserBaseInfo(UserProfileDTO profileDTO) {
		return userProfileDao.updateUserBaseInfo(profileDTO);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "userProfileOfId", key = "#result.userId"),
			@CacheEvict(value = "userProfileOfUserName", key = "#result.userName") })
	public UserProfileDTO addUser(UserProfileDTO userProfileDTO) {
		UserProfile user = userProfileDao.addObject(userProfileDTO);
		if (user != null) {
			userProfileDTO = new UserProfileDTO(user);
		}
		return userProfileDTO;
	}

	@Override
	public Map<String, Long> findUserIdsByUserNames(List<String> userNames) {
		Map<String, Long> userNameMap = new HashMap<String, Long>();
		List<UserProfile> userProfiles = userProfileDao.findByUserNames(userNames);
		if (CollectionUtil.isNotEmptyOfList(userProfiles)) {
			for (UserProfile userProfile : userProfiles) {
				userNameMap.put(userProfile.getUserName(), userProfile.getUserId());
			}
		}
		return userNameMap;
	}
}
