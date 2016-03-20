/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ModelSpeciOption;

/**
 * ModelSpeciOptionDao.java created by yydx811 at 2015年5月5日 下午2:35:11
 * 商品模型规格选项dao
 *
 * @author yydx811
 */
public interface ModelSpeciOptionDao extends AbstractDao<ModelSpeciOption> {

	/**
	 * 根据规格id获取选项列表
	 * @param specificationId
	 * @param optionType
	 * @return List<ModelSpeciOption>
	 */
	public List<ModelSpeciOption> getSpeciOptionList(long specificationId, int optionType);
	
	/**
	 * 添加模型规格选项列表
	 * @param speciOptionList
	 * @return boolean
	 */
	public boolean addBulkSpeciOptions(List<ModelSpeciOption> speciOptionList);
	
	/**
	 * 添加规格选项
	 * @param speciOption
	 * @return long
	 */
	public long addSpeciOption(ModelSpeciOption speciOption);
	
	/**
	 * 获取最大顺序
	 * @param speciOption
	 * @return int
	 */
	public int getMaxIndex(ModelSpeciOption speciOption);
	
	/**
	 * 更新规格选项
	 * @param option
	 * @return int
	 */
	public int updateModelSpeciOption(ModelSpeciOption option);
	
	/**
	 * 删除规格选项
	 * @param id
	 * @param specificationId
	 * @return
	 */
	public int deleteModelSpeciOption(long id, long specificationId);
	
	/**
	 * 获取规格选项
	 * @param id
	 * @param specificationId
	 * @return ModelSpeciOption
	 */
	public ModelSpeciOption getModelSpeciOption(long id, long specificationId);
	
	/**
	 * 按规格id删除规格选项
	 * @param specificationId
	 * @return
	 */
	public int deleteBulkModelSpeciOption(long specificationId);
}
