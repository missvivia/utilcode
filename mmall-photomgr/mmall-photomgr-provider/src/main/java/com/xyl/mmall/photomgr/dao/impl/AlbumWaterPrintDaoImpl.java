package com.xyl.mmall.photomgr.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.photomgr.dao.AlbumWaterPrintDao;
import com.xyl.mmall.photomgr.enums.DBField.WaterField;
import com.xyl.mmall.photomgr.meta.AlbumWaterPrint;

/**
 * 相册用户操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class AlbumWaterPrintDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<AlbumWaterPrint> implements
		AlbumWaterPrintDao {

	private static final Logger logger = LoggerFactory.getLogger(AlbumWaterPrintDaoImpl.class);

	@Override
	public AlbumWaterPrint saveAlbumWaterPrint(AlbumWaterPrint wp) {
		logger.debug("saveAlbumWaterPrint: " + wp);
		try {
			return addObject(wp);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return wp;
		}
	}

	@Override
	public boolean delAlbumWaterPrint(AlbumWaterPrint wp) {
		logger.debug("delAlbumWaterPrint: " + wp);
		try {
			return this.deleteById(wp.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateAlbumWaterPrint(AlbumWaterPrint wp) {
		logger.debug("updateAlbumWaterPrint: " + wp);
		try {
			return update(wp);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public AlbumWaterPrint getAlbumWaterPrintByUserId(long userId) {
		logger.debug("getAlbumWaterPrintByUserId: " + userId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, WaterField.userId, userId);
		try {
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}
