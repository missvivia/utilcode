/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ModelParameter;

/**
 * ModelParameterDao.java created by yydx811 at 2015年5月5日 下午1:33:19
 * 商品模型属性dao
 *
 * @author yydx811
 */
public interface ModelParameterDao extends AbstractDao<ModelParameter> {

	/**
	 * 按模型id获取属性列表
	 * @param modelId
	 * @param isShow 筛选项查询 0不加查询条件 1是 2不是 
	 * @return List<ModelParameter>
	 */
	public List<ModelParameter> getParameterList(long modelId, int isShow);
	
	/**
	 * 添加模型属性
	 * @param modelParameter
	 * @return long
	 */
	public long addModelParameter(ModelParameter modelParameter);

	/**
	 * 获取商品模型属性
	 * @param id
	 * @param modelId
	 * @return
	 */
	public ModelParameter getModelParameter(long id, long modelId);

	/**
	 * 更新模型属性
	 * @param parameter
	 * @return
	 */
	public int updateModelParameter(ModelParameter parameter);

	/**
	 * 删除属性
	 * @param id
	 * @param modelId
	 * @return
	 */
	public int deleteModelParameter(long id, long modelId);
}
