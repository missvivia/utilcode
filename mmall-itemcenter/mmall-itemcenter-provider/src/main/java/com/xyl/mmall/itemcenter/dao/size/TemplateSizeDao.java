package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.TemplateSize;

public interface TemplateSizeDao extends AbstractDao<TemplateSize> {
	/**
	 * 增加尺码模板的尺码字段
	 * 
	 * @param tSize
	 * @return
	 */
	public TemplateSize addNewTemplateSize(TemplateSize tSize);


	/**
	 * 获取指定尺码模板id下的所有尺码字段
	 * 
	 * @param sizeTemplateId
	 * @return
	 */
	public List<TemplateSize> getTemplateSizeList(long sizeTemplateId);

	/**
	 * 删除指定尺码模板id下的所有尺码字段
	 * 
	 * @param sizeTemplateId
	 * @return
	 */
	public boolean deleteTemplateSizeByTemplId(long sizeTemplateId);

	/**
	 * 获取尺码字段列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @return
	 */
	public List<Size> getSizeList(long templatekey);
}
