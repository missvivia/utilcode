package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.OriginalSize;

/**
 * 默认尺码字段dao
 * 
 * @author hzhuangluqian
 *
 */
public interface OriginalSizeDao extends AbstractDao<OriginalSize> {
	/**
	 * 根据默认尺码id获取默认尺码字段列表
	 * 
	 * @param id
	 * @return
	 */
	public List<OriginalSize> getOriginalSizeList(long id);

	/**
	 * 获取尺码字段列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @return
	 */
	public List<Size> getSizeList(long templatekey);
}
