package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.param.SizeTemplateSearchParam;

/**
 * 尺码模板类dao
 * 
 * @author hzhuangluqian
 *
 */
public interface SizeTemplateDao extends AbstractDao<SizeTemplate> {
	/**
	 * 添加新的尺码模板
	 * 
	 * @param sizeTempl
	 * @return
	 */
	public SizeTemplate addNewSizeTemplate(SizeTemplate sizeTempl);

	/**
	 * 
	 * @param searchDTO
	 * @return
	 */
	public BaseSearchResult<SizeTemplate> searchSizeTemplate(SizeTemplateSearchParam searchDTO);


	/**
	 * 获取尺码模板meta对象列表
	 * 
	 * @param categoryId
	 *            最低类目id
	 * @param supplierId
	 *            供应商id
	 * @return
	 */
	public List<SizeTemplate> getSizeTemplate(long categoryId, long supplierId);

	/**
	 * 保存尺码模板
	 * 
	 * @param sizeTemplate
	 */
	public void saveSizeTemplate(SizeTemplate sizeTemplate);

}
