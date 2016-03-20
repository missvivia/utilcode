package com.xyl.mmall.photomgr.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.photomgr.meta.AlbumUser;

/**
 * 相册用户操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
public interface AlbumUserDao extends AbstractDao<AlbumUser> {
	/**
	 * 增加或更新一个用户。 用户不存在则增加。存在则更新。
	 * 
	 * @param user
	 *            待添加或更新用户。
	 * @return 返回带id的用户。
	 */
	AlbumUser saveAlbumUser(AlbumUser user);

	/**
	 * 删除一个用户。
	 * 
	 * @param user
	 *            待删除用户
	 * @return 删除成功返回true。否则返回false。
	 */
	boolean delAlbumUser(AlbumUser user);

	/**
	 * 根据userId查询用户信息。
	 * 
	 * @param userId
	 * @return 返回DB中的用户信息
	 */
	AlbumUser getAlbumUserByUserId(long userId);

	/**
	 * 根据主键id查询用户信息。
	 * 
	 * @param id
	 *            主键
	 * @return 返回DB中的用户信息
	 */
	AlbumUser getAlbumUserById(long id);

	/**
	 * 查询所有用户列表。
	 * 
	 * @return 返回DB中所有存在的用户列表。
	 */
	List<AlbumUser> getAllUserList();
	
	/**
	 * 删除所有用户
	 */
	void delAllUser();
}
