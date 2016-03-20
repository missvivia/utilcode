/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.meta.UserProfile;

/**
 * @author lihui
 *
 */
@Repository
public class UserProfileDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserProfile> implements UserProfileDao {

	private static Logger logger = Logger.getLogger(UserProfileDaoSqlImpl.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.user.service.dao.UserProfileDao#findByUserName(java.lang.String)
	 */
	@Override
	public UserProfile findByUserName(String userName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userName", userName);
		return queryObject(sql);
	}

	@Override
	public List<UserProfile> findByUserNames(List<String> userNameList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "userName", userNameList);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.UserProfileDao#findByParams(java.util.Map,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<UserProfile> findByParams(Map<String, String> searchParams, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (!CollectionUtils.isEmpty(searchParams)) {
			for (String key : searchParams.keySet()) {
				SqlGenUtil.appendExtParamObject(sql, key, searchParams.get(key));
			}
		}
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.UserProfileDao#countByParams(java.util.Map)
	 */
	@Override
	public int countByParams(Map<String, String> searchParams) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		if (!CollectionUtils.isEmpty(searchParams)) {
			for (String key : searchParams.keySet()) {
				SqlGenUtil.appendExtParamObject(sql, key, searchParams.get(key));
			}
		}
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.UserProfileDao#findByIdList(java.util.List)
	 */
	@Override
	public List<UserProfile> findByIdList(List<Long> userIdList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "userId", userIdList);
		return queryObjects(sql);
	}

	@Override
	public List<UserProfile> getUserListByUserCondition(UserProfileConditionDTO userProfileConditionDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (StringUtils.isNotEmpty(userProfileConditionDTO.getUserName())) {
			sql.append(" AND UserName LIKE '%").append(userProfileConditionDTO.getUserName().replaceAll("'", "\\\\'"))
					.append("%'");
		}

		return getListByDDBParam(sql.toString(), (DDBParam) userProfileConditionDTO);
	}

	@Override
	public boolean deleteUserProfileByUserName(String userName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "userName", userName);
		return super.getSqlSupport().updateRecord(sql.toString());
	}

	@Override
	public BasePageParamVO<UserProfileDTO> queryUserList(BasePageParamVO<UserProfileDTO> basePageParamVO,
			String searchValue) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		sql = searchSQL(sql, searchValue);
		if (basePageParamVO.getIsPage() == 1) {
			int count = getUserListCount(searchValue);
			if (count == 0) {
				return basePageParamVO;
			}
			basePageParamVO.setTotal(count);
			int offset = basePageParamVO.getStartRownum() - 1;
			int limit = basePageParamVO.getPageSize();
			sql.append(" LIMIT ").append(offset).append(", ").append(limit);
		}
		List<UserProfile> userProfileList = this.queryObjects(sql.toString());
		if (CollectionUtils.isEmpty(userProfileList)) {
			return null;
		}
		basePageParamVO.setList(convertToDTO(userProfileList));
		return basePageParamVO;
	}

	public int getUserListCount(String searchValue) {
		StringBuilder sql = new StringBuilder(genCountSql());
		sql = searchSQL(sql, searchValue);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	private List<UserProfileDTO> convertToDTO(List<UserProfile> userProfileList) {
		List<UserProfileDTO> retList = new ArrayList<UserProfileDTO>(userProfileList.size());
		for (UserProfile userProfile : userProfileList) {
			retList.add(new UserProfileDTO(userProfile));
		}
		return retList;
	}

	private StringBuilder searchSQL(StringBuilder sql, String searchValue) {
		if (StringUtils.isNotBlank(searchValue)) {
			searchValue = searchValue.trim();
			String userName = searchValue.replaceAll("'", "\\\\'");
			sql.append(" AND (UserName LIKE '%").append(userName).append("%' OR Email LIKE '%").append(userName)
					.append("%'");
			char[] chars = searchValue.toCharArray();
			sql.append(" OR NickName Like '%");
			for (int i = 0; i < chars.length; i++) {
				sql.append(chars[i]).append("%");
			}
			sql.append("'");
			if (RegexUtils.isAllNumber(searchValue)) {
				sql.append(" OR UserId = ").append(searchValue).append(" OR Mobile LIKE '%").append(searchValue)
						.append("%'");
			}
			sql.append(")");
		}
		this.appendOrderSql(sql, "RegTime", false);
		return sql;
	}

	@Override
	public UserProfileDTO updateUserBaseInfo(UserProfileDTO userProfile) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET");
		if (userProfile.getEmail() == null) {
			userProfile.setEmail("");
		}
		sql.append(" Email = '").append(userProfile.getEmail()).append("'");
		if (StringUtils.isNotBlank(userProfile.getNickName())) {
			sql.append(", NickName = '").append(userProfile.getNickName()).append("'");
		}
		if (StringUtils.isNotBlank(userProfile.getMobile())) {
			sql.append(", Mobile = '").append(userProfile.getMobile()).append("'");
		}
		if (StringUtils.isNotBlank(userProfile.getUserImageURL())) {
			sql.append(", UserImageURL = '").append(userProfile.getUserImageURL()).append("'");
		}
		if (StringUtils.isNotBlank(userProfile.getLicence())) {
			sql.append(", Licence = '").append(userProfile.getLicence()).append("'");
		}
		if (userProfile.getGender() != null) {
			sql.append(", Gender = ").append(userProfile.getGender().getIntValue());
		}
		if (userProfile.getBirthYear() > 0) {
			sql.append(", BirthYear = ").append(userProfile.getBirthYear());
		}
		if (userProfile.getBirthMonth() > 0) {
			sql.append(", BirthMonth = ").append(userProfile.getBirthMonth());
		}
		if (userProfile.getBirthDay() > 0) {
			sql.append(", BirthDay = ").append(userProfile.getBirthDay());
		}
		if (userProfile.getIsValid() >= 0) {
			sql.append(", IsValid = ").append(userProfile.getIsValid());
		}
		if (userProfile.getHasNoobCoupon() >= 0) {
			sql.append(", HasNoobCoupon = ").append(userProfile.getHasNoobCoupon());
		}
		if (userProfile.getUserType() >= 0) {
			sql.append(", UserType = ").append(userProfile.getUserType());
		}
		sql.append(" WHERE ");
		SqlGenUtil.appendExtParamObject(sql, "UserId", userProfile.getUserId());
		SqlGenUtil.appendExtParamObject(sql, "UserName", userProfile.getUserName());
		if (this.getSqlSupport().excuteUpdate(sql.toString()) > 0) {
			return userProfile;
		} else {
			userProfile = new UserProfileDTO();
			userProfile.setUserName("");
			return userProfile;
		}
	}
}
