package com.xyl.mmall.photomgr.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.photomgr.meta.AlbumWaterPrint;

/**
 * 水印
 * 
 * @author hzzhanghui
 * 
 */
public interface AlbumWaterPrintDao extends AbstractDao<AlbumWaterPrint> {
	/**
	 * 新增
	 */
	AlbumWaterPrint saveAlbumWaterPrint(AlbumWaterPrint wp);

	/**
	 * 删除
	 */
	boolean delAlbumWaterPrint(AlbumWaterPrint wp);

	/**
	 * 更新
	 */
	boolean updateAlbumWaterPrint(AlbumWaterPrint wp);

	/**
	 * 查询
	 */
	AlbumWaterPrint getAlbumWaterPrintByUserId(long userId);

}
