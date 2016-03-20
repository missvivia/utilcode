/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.xyl.mmall.promotion.dao.UserPointDao;
import com.xyl.mmall.promotion.dto.UserPointDTO;
import com.xyl.mmall.promotion.service.UserPointService;

/**
 * UserPointServiceImpl.java created by yydx811 at 2015年12月23日 下午3:25:28
 * 用户积分service接口实现
 *
 * @author yydx811
 */
@Service("userPointService")
public class UserPointServiceImpl implements UserPointService {

	@Autowired
	private UserPointDao userPointDao;

	@Override
	@Cacheable(value = "userPointCache", key = "#userId")
	public UserPointDTO getUserPointByUserId(long userId) {
		UserPointDTO pointDTO = new UserPointDTO(userPointDao.getUserPointByUserId(userId));
		return pointDTO;
	}

	@Override
	@CacheEvict(value = "userPointCache", key = "#result.userId")
	public UserPointDTO saveUserPoint(UserPointDTO userPointDTO) {
		try {
			userPointDTO = new UserPointDTO(userPointDao.saveObject(userPointDTO));
		} catch (DBSupportRuntimeException e) {
			userPointDao.updateUserPoint(userPointDTO);
		}
		return userPointDTO;
	}
}
