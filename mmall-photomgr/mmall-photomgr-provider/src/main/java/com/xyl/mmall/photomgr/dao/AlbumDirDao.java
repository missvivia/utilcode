package com.xyl.mmall.photomgr.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.photomgr.meta.AlbumDir;

/**
 * 相册目录操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
public interface AlbumDirDao extends AbstractDao<AlbumDir> {

	/**
	 * 新增或更新目录。
	 * 
	 * @param dir
	 *            待添加或更新的目录对象
	 * @return 返回待id的目录对象
	 */
	AlbumDir saveAlbumDir(AlbumDir dir);

	/**
	 * 删除目录。
	 * 
	 * @param dir
	 *            待删除目录对象。
	 * @return 删除成功返回true。否则返回false。
	 */
	boolean delAlbumDir(AlbumDir dir);

	/**
	 * 根据主键id相册目录。
	 * 
	 * @param id
	 *            主键
	 * @return 返回DB中的目录对象
	 */
	AlbumDir getAlbumDirById(long id);
	
	/**
	 * Get dir by dirId and userId
	 * 
	 * @param dirId
	 * @param userId For balance purpose
	 * @return
	 */
	AlbumDir getDirById(long dirId, long userId);
	
	/**
	 * 根据userId和目录名称查询目录信息
	 * 
	 * @param dir
	 *            包含userId和目录name的对象
	 * @return 返回DB中的目录信息
	 */
	AlbumDir getAlbumDirByName(AlbumDir dir);

	/**
	 * 查询所有用户所有目录列表。
	 * 
	 * @return 返回DB中所有用户所用目录列表。
	 */
	List<AlbumDir> getAllDirList();

	/**
	 * 查询某用户所有目录列表。
	 * 
	 * @param userId
	 * @return 返回该用户所有目录列表
	 */
	List<AlbumDir> getAlbumDirListByUserId(long userId);

	/**
	 * 清表
	 * 
	 * @return 清表成功返回true，否则返回false
	 */
	boolean delAllDir();

//	/**
//	 * 查询用户下面的所有目录列表
//	 * 
//	 * @param userId
//	 * @return 返回指定用户下面的所有目录列表
//	 */
//	List<AlbumDir> getDirListByUserId(long userId);
	
	/**
	 * Update dir name
	 * @param dir
	 * @return
	 */
	boolean updateDir(AlbumDir dir);
}
