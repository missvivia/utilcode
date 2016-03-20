package com.xyl.mmall.itemcenter.dao.category;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.CategoryOSizeMap;

/**
 * 类目和尺码对应dao
 * 
 * @author hzhuangluqian
 *
 */
public interface CategoryOSizeMapDao extends AbstractDao<CategoryOSizeMap> {
	/**
	 * 根据类目id查询该类目对应的默认尺码
	 * 
	 * @param categoryId
	 * @return
	 */
	public long getCategoryOSizeMap(long categoryId);
}
