package com.xyl.mmall.photomgr.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.photomgr.dao.AlbumUserDao;
import com.xyl.mmall.photomgr.meta.AlbumUser;

/**
 * 相册用户操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class AlbumUserDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<AlbumUser> implements AlbumUserDao {

	private static final Logger logger = LoggerFactory.getLogger(AlbumUserDaoImpl.class);

	/**
	 * 增加或更新一个用户。 用户不存在则增加。存在则更新。
	 * 
	 * @param user
	 *            待添加或更新用户。
	 * @return 返回带id的用户。
	 */
	public AlbumUser saveAlbumUser(AlbumUser user) {
		logger.debug("saveAlbumUser: " + user);

		try {
			AlbumUser dbUser = getAlbumUserByUserId(user.getUserId());
			if (dbUser == null || dbUser.getId() == 0L) {
				return addObject(user);
			} else {
				user.setId(dbUser.getId());
				return user;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return user;
		}
	}

	/**
	 * 删除一个用户。
	 * 
	 * @param user
	 *            待删除用户
	 * @return 删除成功返回true。否则返回false。
	 */
	public boolean delAlbumUser(AlbumUser user) {
		logger.debug("delAlbumUser: " + user);
		try {
			return deleteById(user.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 根据userId查询用户信息。
	 * 
	 * @param userId
	 * @return 返回DB中的用户信息
	 */
	public AlbumUser getAlbumUserByUserId(long userId) {
		logger.debug("getAlbumUserByUserId: " + userId);
		try {
			return getObjectByUserId(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 根据主键id查询用户信息。
	 * 
	 * @param id
	 *            主键
	 * @return 返回DB中的用户信息
	 */
	public AlbumUser getAlbumUserById(long id) {
		logger.debug("getAlbumUserById: " + id);
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 查询所有用户列表。
	 * 
	 * @return 返回DB中所有存在的用户列表。
	 */
	public List<AlbumUser> getAllUserList() {
		try {
			return super.getAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<AlbumUser>();
		}
	}

	/**
	 * 删除所有用户
	 */
	public void delAllUser() {
		try {
			super.deleteAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
