package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.SizeColumn;

public interface SizeColumnDao extends AbstractDao<SizeColumn> {
	/**
	 * 获取尺码字段类
	 * 
	 * @param columnId
	 *            字段id
	 * @return
	 */
	public SizeColumn getSizeColumn(long columnId);

	/**
	 * 增加尺码字段
	 * 
	 * @param sizeColumn
	 * @return
	 */
	public SizeColumn addNewSizeColumn(SizeColumn sizeColumn);

	public List<SizeColumn> getSizeColumnList(List<Long> columnIdList);

	public Map<Long, SizeColumn> getSizeColumnMap(List<Long> columnIdList);
}
