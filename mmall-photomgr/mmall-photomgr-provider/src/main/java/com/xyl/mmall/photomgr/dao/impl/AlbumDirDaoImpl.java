package com.xyl.mmall.photomgr.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.photomgr.dao.AlbumDirDao;
import com.xyl.mmall.photomgr.enums.DBField.DirField;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.service.Util;

/**
 * 相册目录操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class AlbumDirDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<AlbumDir> implements AlbumDirDao {
	private static final Logger logger = LoggerFactory.getLogger(AlbumDirDaoImpl.class);

	/**
	 * 新增或更新目录。
	 * 
	 * @param dir
	 *            待添加或更新的目录对象
	 * @return 返回待id的目录对象
	 */
	public AlbumDir saveAlbumDir(AlbumDir dir) {
		logger.debug("saveAlbumDir: " + dir);
		try {
			saveDefaultDir(dir.getUserId());
			AlbumDir dbDir = checkDirExistInDB(dir);
			if (dbDir == null || dbDir.getId() == 0L) { // not exist
				return addObject(dir);
			} else {
				dir.setId(dbDir.getId());
				if (dir.getDirName() != null && !"".equals(dir.getDirName().trim())) {
					updateAlbumDirName(dir);
				}
				return dir;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return dir;
		}
	}

	private String tableName = this.getTableName();

	private String sqlUpdate = "UPDATE " + tableName + " SET " + DirField.dirName + "=? WHERE " + DirField.id
			+ "=? AND " + DirField.userId + "=?";

	private void updateAlbumDirName(AlbumDir dir) {
		Object[] args = new Object[] { dir.getDirName(), dir.getId(), dir.getUserId()};
		getSqlSupport().excuteUpdate(sqlUpdate, args);
	}

	/**
	 * 为某个用户创建一个默认目录
	 * 
	 * @param userId
	 */
	private void saveDefaultDir(long userId) {
		AlbumDir dir = new AlbumDir();
		dir.setUserId(userId);
		dir.setDirName(Util.DEFAULT_CATEGORY_NAME);
		AlbumDir defDir = this.getAlbumDirByName(dir);
		if (defDir == null || defDir.getId() == 0) {
			defDir = new AlbumDir();
			// defDir.setId(Util.DEFAULT_DIR_ID);
			defDir.setDirCreateDate(System.currentTimeMillis());
			defDir.setDirName(Util.DEFAULT_CATEGORY_NAME);
			defDir.setUserId(userId);
			addObject(defDir);
		}
	}

	private AlbumDir checkDirExistInDB(AlbumDir dir) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND " + DirField.userId + "=" + dir.getUserId());
		sql.append(" AND( " + DirField.id + "=" + dir.getId() + " OR " + DirField.dirName + "='" + dir.getDirName()
				+ "') ");

		logger.debug("checkDirExistInDB: " + sql);
		return queryObject(sql);
	}

	/**
	 * 删除目录。
	 * 
	 * @param dir
	 *            待删除目录对象。
	 * @return 删除成功返回true。否则返回false。
	 */
	public boolean delAlbumDir(AlbumDir dir) {
		logger.debug("delAlbumDir: " + dir);
		try {
			return deleteById(dir.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 根据主键id相册目录。
	 * 
	 * @param id
	 *            主键
	 * @return 返回DB中的目录对象
	 */
	public AlbumDir getAlbumDirById(long id) {
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public AlbumDir getDirById(long dirId, long userId) {
		logger.debug("getDirById(): dirId=" + dirId + ", userId=" + userId);
		try {
			StringBuilder sql = new StringBuilder(256);
			sql.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sql, DirField.id, dirId);
			SqlGenUtil.appendExtParamObject(sql, DirField.userId, userId);
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 根据userId和目录名称查询目录信息
	 * 
	 * @param dir
	 *            包含userId和目录name的对象
	 * @return 返回DB中的目录信息
	 */
	public AlbumDir getAlbumDirByName(AlbumDir dir) {
		try {
			StringBuilder sql = new StringBuilder(256);
			sql.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sql, DirField.userId, dir.getUserId());
			SqlGenUtil.appendExtParamObject(sql, DirField.dirName, dir.getDirName());
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public boolean updateDir(AlbumDir dir) {
		try {
			updateAlbumDirName(dir);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 查询所有用户所有目录列表。
	 * 
	 * @return 返回DB中所有用户所用目录列表。
	 */
	public List<AlbumDir> getAllDirList() {
		try {
			return getAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<AlbumDir>();
		}
	}

	/**
	 * 查询某用户所有目录列表。
	 * 
	 * @param userId
	 * @return 返回该用户所有目录列表
	 */
	public List<AlbumDir> getAlbumDirListByUserId(long userId) {
		try {
			StringBuilder sql = new StringBuilder(256);
			sql.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sql, DirField.userId, userId);
			List<AlbumDir> dirList = queryObjects(sql);
			if (dirList == null || dirList.size() == 0) {
				saveDefaultDir(userId);
				dirList = queryObjects(sql);
			} 
			return dirList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<AlbumDir>();
		}
	}

	/**
	 * 清表。
	 * 
	 * @return 清表成功返回true。否则返回false
	 */
	public boolean delAllDir() {
		try {
			deleteAll();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

//	/**
//	 * 查询用户下面的所有目录列表
//	 * 
//	 * @param userId
//	 * @return 返回指定用户下面的所有目录列表
//	 */
//	public List<AlbumDir> getDirListByUserId(long userId) {
//		try {
//			StringBuilder sql = new StringBuilder(256);
//			sql.append(genSelectSql());
//			SqlGenUtil.appendExtParamObject(sql, DirField.userId, userId);
//			return queryObjects(sql);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return new ArrayList<AlbumDir>();
//		}
//	}
}
