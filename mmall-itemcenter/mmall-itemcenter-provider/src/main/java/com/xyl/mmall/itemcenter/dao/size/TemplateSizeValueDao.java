package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.TemplateSizeValue;

/**
 * 尺码模板的尺码字段值dao
 * 
 * @author hzhuangluqian
 *
 */
public interface TemplateSizeValueDao extends AbstractDao<TemplateSizeValue> {
	/**
	 * 添加尺码模板字段值
	 * 
	 * @param tmplSV
	 * @return
	 */
	public TemplateSizeValue addNewTemplateSizeValue(TemplateSizeValue tmplSV);

	/**
	 * 获取尺码模板中的尺码字段值类
	 * 
	 * @param templateSizeId
	 *            尺码模板id
	 * @param columnId
	 *            尺码字段id
	 * @param recordIndex
	 *            尺码id
	 * @return
	 */
	public TemplateSizeValue getTemplateSizeValue(long templateSizeId, long columnId, long recordIndex);

	/**
	 * 获取指定尺码id的所有字段值里列表
	 * 
	 * @param sizeTemplateId
	 *            尺码模板id
	 * @param sizeId
	 *            尺码id
	 * @return
	 */
	public List<TemplateSizeValue> getTemplateSizeValueList(long sizeTemplateId, long sizeId);

	/**
	 * 获取尺码id列表
	 * 
	 * @param id
	 *            模板id
	 * @return
	 */
	public List<Long> getSizeIdList(long id);

	/**
	 * 获取尺码字段值
	 * 
	 * @param templatekey
	 *            模板id
	 * @param columnId
	 *            字段id
	 * @param sizeId
	 *            尺码id
	 * @return
	 */
	public SizeValue getSizeValue(long templatekey, long columnId, long sizeId);

	/**
	 * 获取尺码字段值列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @param sizeId
	 *            尺码id
	 * @return
	 */
	public List<SizeValue> getSizeValueList(long templatekey, long sizeId);

	/**
	 * 获取尺码模板的尺码字段值列表
	 * 
	 * @param templateId
	 *            尺码模板id
	 * @return
	 */
	public List<TemplateSizeValue> getTemplateSizeValueList(long templateId);

	/**
	 * 删除某个尺码模板下的所有尺码字段值
	 * 
	 * @param sizeTemplateId
	 *            尺码模板id
	 * @return
	 */
	public boolean deleteTemplateSizeValue(long sizeTemplateId);

	public List<SizeValue> getSizeValueList(long templatekey);
}
