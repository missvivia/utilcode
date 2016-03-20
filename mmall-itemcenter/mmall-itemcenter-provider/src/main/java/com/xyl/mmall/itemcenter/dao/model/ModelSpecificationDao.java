/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ModelSpecification;

/**
 * ModelSpecificationDao.java created by yydx811 at 2015年5月5日 下午2:07:25
 * 商品模型规格dao
 *
 * @author yydx811
 */
public interface ModelSpecificationDao extends AbstractDao<ModelSpecification> {

	/**
	 * 按商品模型id获取规格列表
	 * @param modelId
	 * @param isShow 筛选项查询 0不加查询条件 1是 2不是 
	 * @return List<ModelSpecification>
	 */
	public List<ModelSpecification> getSpecificationList(long modelId, int isShow);
	
	/**
	 * 添加商品模型规格
	 * @param specification
	 * @return long
	 */
	public long addModelSpecification(ModelSpecification specification);

	/**
	 * 获取商品模型规格
	 * @param id
	 * @param modelId
	 * @return
	 */
	public ModelSpecification getModelSpecification(long id, long modelId);

	/**
	 * 获取商品模型规格
	 * @param id
	 * @return
	 */
	public ModelSpecification getModelSpecification(long id);

	/**
	 * 更新模型规格
	 * @param specification
	 * @return
	 */
	public int updateModelSpecification(ModelSpecification specification);
	
	/**
	 * 删除规格
	 * @param id
	 * @param modelId
	 * @return
	 */
	public int deleteModelSpecification(long id, long modelId);
}
