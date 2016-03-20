/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.xyl.mmall.member.dao.MobileInfoDao;
import com.xyl.mmall.member.dto.MobileInfoDTO;
import com.xyl.mmall.member.meta.MobileInfo;
import com.xyl.mmall.member.service.MobileInfoService;

/**
 * @author lihui
 *
 */
public class MobileInfoServiceImpl implements MobileInfoService {

	@Autowired
	private MobileInfoDao mobileInfoDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.MobileInfoService#upsertMobileInfo(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public MobileInfoDTO upsertMobileInfo(String initId, String initKey) {
		MobileInfo mobileInfo = mobileInfoDao.findByInitId(initId);
		// 1.如数据库表中没有此initId,则创建
		if (mobileInfo == null) {
			mobileInfo = addNewMobileInfo(initId, initKey);
			if (mobileInfo == null)
				return null;
		}
		// 2.创建返回的DTO
		return new MobileInfoDTO(mobileInfo);
	}

	/**
	 * @param initId
	 * @param initKey
	 * @return
	 */
	private MobileInfo addNewMobileInfo(String initId, String initKey) {
		MobileInfo mobileInfo = new MobileInfo();
		// 设置initId
		mobileInfo.setInitId(initId);
		// 设置initKey
		mobileInfo.setInitKey(initKey);
		// 设置当前时间，用以定时清理旧的数据
		mobileInfo.setInitTime(System.currentTimeMillis());
		try {
			mobileInfoDao.addObject(mobileInfo);
		} catch (DBSupportRuntimeException ex) {
			// 兼容用户重复创建的情况
			mobileInfo = mobileInfoDao.findByInitId(initId);
		}
		return mobileInfo;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.MobileInfoService#updateUserInfo(java.lang.String,
	 *      java.lang.String, long, java.lang.String)
	 */
	@Override
	@CacheEvict(value = "mobileInfoOfToken", key = "#token")
	public MobileInfoDTO updateUserInfo(String initId, String token, long userId, String mobileToken) {
		MobileInfo mobileInfo = mobileInfoDao.findByInitId(initId);
		if (mobileInfo != null) {
			// 设置当前登录用户ID
			mobileInfo.setUserId(userId);
			// 设置URS访问的Token
			mobileInfo.setUrsToken(token);
			// 设置手机访问token
			mobileInfo.setMobileToken(mobileToken);
			// 设置手机访问token的过期时间
			mobileInfo.setExpiredTime(System.currentTimeMillis() + 2 * 60 * 60 * 1000);
			mobileInfoDao.updateObjectByKey(mobileInfo);
		}
		return new MobileInfoDTO(mobileInfo);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.MobileInfoService#findMobileInfoByInitId(java.lang.String)
	 */
	@Override
	public MobileInfoDTO findMobileInfoByInitId(String id) {
		MobileInfo mobileInfo = mobileInfoDao.findByInitId(id);
		if (mobileInfo != null) {
			return new MobileInfoDTO(mobileInfo);
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.MobileInfoService#findMobileInfoByInitIdAndToken(java.lang.String)
	 */
	@Override
	@Cacheable(value = "mobileInfoOfToken", key = "#token")
	public MobileInfoDTO findMobileInfoByInitIdAndToken(String token) {
		String[] tokens = StringUtils.split(token, "|");
		MobileInfo mobileInfo = null;
		if (tokens != null) {
			if (tokens.length > 1) {
				mobileInfo = mobileInfoDao.findByInitIdAndMobileToken(tokens[1], tokens[0]);
			} else {
				mobileInfo = mobileInfoDao.findByMobileToken(tokens[0]);
			}
		}
		if (mobileInfo != null) {
			return new MobileInfoDTO(mobileInfo);
		}
		return null;
	}
}
