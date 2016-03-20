package com.xyl.mmall.photomgr.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.photomgr.dao.AlbumImgDao;
import com.xyl.mmall.photomgr.enums.DBField.DirField;
import com.xyl.mmall.photomgr.enums.DBField.ImgField;
import com.xyl.mmall.photomgr.meta.AlbumImg;

/**
 * 相册图片后台服务。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class AlbumImgDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<AlbumImg> implements AlbumImgDao {
	private static final Logger logger = LoggerFactory.getLogger(AlbumImgDaoImpl.class);

	private String tableName = this.getTableName();

	private String sqlUpdate = "UPDATE " + tableName + " SET " + ImgField.dirId + "=?, " + ImgField.imgName + "=?, "
			+ ImgField.imgUrl + "=?, " + ImgField.nosPath + "=?, " + ImgField.height + "=?, " + ImgField.width + "=?, "
			+ ImgField.imgType + "=? WHERE " + ImgField.id + "=? AND " + ImgField.userId + "=?";

	private String sqlUpdateDir = "UPDATE " + tableName + " SET " + ImgField.dirId + "=?  WHERE " + ImgField.id
			+ "=? AND " + ImgField.userId + "=?";

	private String sqlUpdateDirId = "UPDATE " + tableName + " SET " + ImgField.dirId + "=?  WHERE " + ImgField.dirId
			+ "=? AND " + ImgField.userId + "=?";

	/**
	 * 新增或更新图片信息。
	 * 
	 * @param img
	 *            待添加或更新图片对象。
	 * @return 返回待id的图片对象
	 */
	public AlbumImg saveAlbumImg(AlbumImg img) {
		logger.debug("saveAlbumImg: " + img);
		AlbumImg dbImg = checkImgExistInDB(img);
		if (dbImg == null || dbImg.getId() == 0L) { // not exist
			logger.debug("Image doesn't exist in DB: " + img);
			return addObject(img);
		} else {
			img.setId(dbImg.getId());
			if (img.getDirId() == 0) {
				img.setDirId(dbImg.getDirId());
			}
			if (img.getHeight() == 0) {
				img.setHeight(dbImg.getHeight());
			}
			if (img.getWidth() == 0) {
				img.setWidth(dbImg.getWidth());
			}
			if (img.getImgName() == null) {
				img.setImgName(dbImg.getImgName());
			}
			if (img.getImgType() == null) {
				img.setImgType(dbImg.getImgType());
			}
			if (img.getImgUrl() == null) {
				img.setImgUrl(dbImg.getImgUrl());
			}
			if (img.getNosPath() == null) {
				img.setNosPath(dbImg.getNosPath());
			}
			Object[] args = new Object[] { img.getDirId(), img.getImgName(), img.getImgUrl(), img.getNosPath(),
					img.getHeight(), img.getWidth(), img.getImgType(), img.getId(), img.getUserId() };
			getSqlSupport().excuteUpdate(sqlUpdate, args);
			return img;
		}
	}

	private AlbumImg checkImgExistInDB(AlbumImg img) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND " + ImgField.userId + "=" + img.getUserId());
		sql.append(" AND " + ImgField.dirId + "=" + img.getDirId());
		sql.append(" AND " + ImgField.nosPath + "='" + img.getNosPath() + "'");
		return queryObject(sql);
	}

	@Override
	public boolean updateImgDir(AlbumImg img) {
		Object[] args = new Object[] { img.getDirId(), img.getId(), img.getUserId() };
		int i = getSqlSupport().excuteUpdate(sqlUpdateDir, args);
		return i == 1;
	}

	@Override
	public boolean chgDir(long newDirId, long userId, long oldDirId) {
		Object[] args = new Object[] { newDirId, oldDirId, userId };
		try {
			getSqlSupport().excuteUpdate(sqlUpdateDirId, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 批量新增或更新图片信息。
	 * 
	 * @param imgList
	 *            待添加或更新的图片列表
	 * @return 返回更新后的待id的图片列表
	 */
	public List<AlbumImg> saveAlbumImgs(List<AlbumImg> imgList) {
		logger.debug("saveAlbumImgs: " + imgList);
		for (AlbumImg img : imgList) {
			AlbumImg imgWithId = saveAlbumImg(img);
			img.setId(imgWithId.getId());
		}

		return imgList;
	}

	/**
	 * 删除图片。
	 * 
	 * @param img
	 *            待删除图片对象。
	 * @return 删除成功返回true。否则返回false。
	 */
	public boolean delAlbumImg(AlbumImg img) {
		logger.debug("delAlbumImg: " + img);
		AlbumImg dbImg = this.getAlbumImgById(img.getId());
		if (dbImg == null || dbImg.getId() == 0L) {
			logger.debug("Img doesn't exist in DB: " + img);
			return false;
		}
		return deleteByIdAndUserId(dbImg.getId(), dbImg.getUserId());
	}

	/**
	 * 批量删除图片。
	 * 
	 * @param imgList
	 *            待删除的图片列表。
	 * @return 全部删除成功返回true。任何一个删除失败返回false。
	 */
	public boolean delAlbumImgs(List<AlbumImg> imgList) {
		logger.debug("delAlbumImgs: " + imgList);
		for (AlbumImg img : imgList) {
			try {
				delAlbumImg(img);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * 根据主键id查询图片信息。
	 * 
	 * @param id
	 *            主键
	 * @return 返回DB中的图片信息
	 */
	@Cacheable(value="albumCache")
	public AlbumImg getAlbumImgById(long id) {
		logger.debug("getAlbumImgById: " + id);
		return this.getObjectById(id);
	}

	/**
	 * 根据userId查询图片信息。
	 * 
	 * @param userId
	 * @return 返回DB中的图片信息
	 */
	public AlbumImg getAlbumImgByUserId(long userId) {
		logger.debug("getAlbumImgByUserId: " + userId);
		return this.getObjectByUserId(userId);
	}

//	/**
//	 * 根据图片nos路径查询图片信息。
//	 * 
//	 * @param nosPath
//	 *            图片在nos上的路径
//	 * @return 返回DB中的图片信息
//	 */
//	public AlbumImg getAlbumImgByNosPath(String nosPath) {
//		logger.debug("getAlbumImgByNosPath: " + nosPath);
//		StringBuilder sql = new StringBuilder(256);
//		sql.append(genSelectSql());
//		SqlGenUtil.appendExtParamObject(sql, ImgField.nosPath, nosPath);
//		return queryObject(sql);
//	}
//
//	/**
//	 * 根据图片URL查询图片信息。
//	 * 
//	 * @param imgUrl
//	 *            图片URL
//	 * @return 返回DB中的图片信息
//	 */
//	public AlbumImg getAlbumImgByUrl(String imgUrl) {
//		logger.debug("getAlbumImgByUrl: " + imgUrl);
//		StringBuilder sql = new StringBuilder(256);
//		sql.append(genSelectSql());
//		SqlGenUtil.appendExtParamObject(sql, ImgField.imgUrl, imgUrl);
//		return queryObject(sql);
//	}

	/**
	 * 查询所有用户所有目录下的所有图片。 包括所有用户所有目录下的图片。
	 * 
	 * @return 返回所欲用户所有目录下的所有图片列表。
	 */
	public List<AlbumImg> getAllImgs() {
		return getAll();
	}

	/**
	 * 查询某个用户指定目录下的图片列表。
	 * 
	 * @param dirId
	 * @return
	 */
	public List<AlbumImg> getAlbumImgListByDirId(long userId, long dirId) {
		logger.debug("getAlbumImgListByDirId: " + dirId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, ImgField.userId, userId);
		SqlGenUtil.appendExtParamObject(sql, ImgField.dirId, dirId);
		return queryObjects(sql);
	}
	
	@Override
	public List<AlbumImg> batchQueryImgList(List<Long> idList) {
		logger.debug("batchQueryImgList: " + idList);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (idList != null && idList.size() != 0) {
			StringBuilder inClause = new StringBuilder();
			inClause.append(" IN (");
			for (Long id : idList) {
				inClause.append(id).append(",");
			}
			inClause.append(-1);
			inClause.append(")");

			sql.append(" AND " + ImgField.id + inClause.toString());
		}
		
		return queryObjects(sql);
	}

	/**
	 * 查询符合条件的图片列表。
	 * 
	 * @return
	 */
	public List<AlbumImg> getAlbumImgListByCond(long userId, long dirId, long queryStartTime, long queryEndTime,
			String queryImgName, int curPage, int pageSize) {
		logger.debug("getAlbumImgListByCond: " + userId + "-" + dirId + "-" + queryStartTime + "-" + queryEndTime + "-"
				+ queryImgName);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND " + ImgField.userId + " = " + userId + " and " + ImgField.dirId + "=" + dirId);
		if (queryStartTime != 0) {
			sql.append(" AND " + ImgField.createDate + " >= " + queryStartTime);
		}
		if (queryEndTime != 0) {
			sql.append(" AND " + ImgField.createDate + " <= " + queryEndTime);
		}
		if (queryImgName != null && !"".equals(queryImgName)) {
			sql.append(" AND " + ImgField.imgName + " = '" + queryImgName + "'");
		}
		if (curPage != 0 && pageSize != 0) {
			if (curPage < 0) {
				curPage = 0;
			}
			if (pageSize < 0) {
				pageSize = 0;
			}
			//sql.append(" limit " + getOffset(curPage, pageSize) + "," + pageSize);
			sql.append(" limit " + curPage + "," + pageSize);
		}
		logger.debug("getAlbumImgListByCond: " + sql.toString());
		return queryObjects(sql);
	}

//	private int getOffset(int curPage, int pageSize) {
//		return (curPage - 1) * pageSize;
//	}

	/**
	 * 查询用户所有目录下的所有图片列表。
	 * 
	 * @param userId
	 * @return 用户所有目录下的图片列表。
	 */
	public List<AlbumImg> getAlbumImgListByUserId(long userId) {
		logger.debug("getAlbumImgListByUserId: " + userId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, DirField.userId, userId);
		return queryObjects(sql);
	}

	/**
	 * 删除所有图片
	 * 
	 * @return 要么返回true，要么抛出异常。
	 */
	public boolean deleteAllImg() {
		super.deleteAll();
		return true;
	}
}
