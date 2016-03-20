package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.OriginalSizeValue;

/**
 * 默认尺码字段值dao
 * 
 * @author hzhuangluqian
 *
 */
public interface OriginalSizeValueDao extends AbstractDao<OriginalSizeValue> {

	/**
	 * 获取默认尺码的尺码字段值
	 * 
	 * @param id
	 *            默认尺码id
	 * @param columnId
	 *            尺码字段id
	 * @param sizeId
	 *            尺码id
	 * @return
	 */
	public OriginalSizeValue getOriginalSizeValue(long id, long columnId, long sizeId);

	/**
	 * 根据默认尺码模板id，获取该尺码模板下的所有尺码id
	 * 
	 * @param id
	 *            默认尺码模板id
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

	public List<SizeValue> getSizeValueList(long templatekey, long sizeId);

}
