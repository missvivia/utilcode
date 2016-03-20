/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ModelParamOption;

/**
 * ModelParamOptionDao.java created by yydx811 at 2015年5月5日 下午1:47:07
 * 属性选项dao
 *
 * @author yydx811
 */
public interface ModelParamOptionDao extends AbstractDao<ModelParamOption> {

	/**
	 * 按属性id获取属性选项列表
	 * @param parameterId
	 * @return List<ModelParamOption>
	 */
	public List<ModelParamOption> getParamOptionList(long parameterId);
	
	/**
	 * 添加属性选项列表
	 * @param optionList
	 * @return boolean
	 */
	public boolean addBulkParamOptions(List<ModelParamOption> optionList);
	
	/**
	 * 添加属性选项
	 * @param paramOption
	 * @return long
	 */
	public long addParamOption(ModelParamOption paramOption);
	
	/**
	 * 更新属性选项
	 * @param option
	 * @return int
	 */
	public int updateModelParamOption(ModelParamOption option);
	
	/**
	 * 删除属性选项
	 * @param id
	 * @param parameterId
	 * @return
	 */
	public int deleteModelParamOption(long id, long parameterId);
	
	/**
	 * 按parameterId删除选项列表
	 * @param parameterId
	 * @return
	 */
	public int deleteBulkParamOption(long parameterId);
}
