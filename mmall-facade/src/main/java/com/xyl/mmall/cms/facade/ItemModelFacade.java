/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xyl.mmall.cms.vo.ItemModelVO;
import com.xyl.mmall.cms.vo.ModelParameterVO;
import com.xyl.mmall.cms.vo.ModelSpecificationVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;

/**
 * ItemModelFacade.java created by yydx811 at 2015年5月5日 上午9:24:46
 * 商品模型facade
 *
 * @author yydx811
 */
public interface ItemModelFacade {

	/**
	 * 获取商品模型列表
	 * @param pageParamVO
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return List<ItemModelVO>
	 */
	public List<ItemModelVO> getItemModelList(BasePageParamVO<ItemModelVO> pageParamVO, String searchValue, 
			String startTime, String endTime);
	
	/**
	 * 按分类id获取列表
	 * @param categoryId
	 * @param isContainList
	 * @return ItemModelVO
	 */
	public ItemModelVO getItemModel(long categoryId, boolean isContainList);
	
	/**
	 * 添加商品模型
	 * @param itemModelDTO
	 * @return long
	 */
	public long addItemModel(ItemModelDTO itemModelDTO);
	
	/**
	 * 按id获取商品模型
	 * @param id
	 * @param isContainList
	 * @return ItemModelVO
	 */
	public ItemModelVO getItemModelById(long id, boolean isContainList);
	
	/**
	 * 添加商品模型属性
	 * @param parameterDTO
	 * @return long
	 */
	public long addModelParameter(ModelParameterDTO parameterDTO);

	/**
	 * 添加商品模型规格
	 * @param specificationDTO
	 * @return long
	 */
	public long addModelSpecification(ModelSpecificationDTO specificationDTO);
	
	/**
	 * 添加规格选项
	 * @param speciOptionDTO
	 * @return long
	 */
	public long addSpeciOption(ModelSpeciOptionDTO speciOptionDTO);
	
	/**
	 * 添加规格选项
	 * @param paramOptionDTO
	 * @return long
	 */
	public long addParamOption(ModelParamOptionDTO paramOptionDTO);
	
	/**
	 * 更新商品模型
	 * @param modelDTO
	 * @return int
	 */
	public int updateItemModel(ItemModelDTO modelDTO);
	
	/**
	 * 获取商品模型属性
	 * @param id
	 * @param modelId
	 * @return
	 */
	public ModelParameterVO getModelParameter(long id, long modelId);
	
	/**
	 * 获取商品模型规格
	 * @param id
	 * @param modelId
	 * @return
	 */
	public ModelSpecificationVO getModelSpecification(long id, long modelId);
	
	/**
	 * 更新模型属性
	 * @param parameterDTO
	 * @return
	 */
	public int updateModelParameter(ModelParameterDTO parameterDTO);
	
	/**
	 * 更新模型规格
	 * @param specificationDTO
	 * @return
	 */
	public int updateModelSpecification(ModelSpecificationDTO specificationDTO);
	
	/**
	 * 更新属性选项
	 * @param optionDTO
	 * @return int
	 */
	public int updateModelParamOption(ModelParamOptionDTO optionDTO);
	
	/**
	 * 更新规格选项
	 * @param optionDTO
	 * @return int
	 */
	public int updateModelSpeciOption(ModelSpeciOptionDTO optionDTO);
	
	/**
	 * 删除属性选项
	 * @param id
	 * @param parameterId
	 * @return
	 */
	public int deleteModelParamOption(long id, long parameterId);
	
	/**
	 * 删除规格选项
	 * @param id
	 * @param specificationId
	 * @return
	 */
	public int deleteModelSpeciOption(long id, long specificationId);
	
	/**
	 * 删除属性
	 * @param id
	 * @param modelId
	 * @return
	 */
	public int deleteModelParameter(long id, long modelId);
	
	/**
	 * 删除规格
	 * @param id
	 * @param modelId
	 * @return
	 */
	public int deleteModelSpecification(long id, long modelId);
	
	/**
	 * 删除商品模型
	 * @param id
	 * @return
	 */
	public int deleteItemModel(long id);
	
	/**
	 * 根据分类id获取规格键值对map
	 * @param categoryId
	 * @return
	 */
	public Map<Long, Set<Long>> getModelSpecificationKeyValues(long categoryId);

	/**
	 * 根据分类id获取属性键值对map
	 * @param categoryId
	 * @return
	 */
	public Map<Long, Set<Long>> getModelParamKeyValues(long categoryId);

	/**
	 * 按分类id获取列表
	 * @param categoryId
	 * @param isShow 筛选项查询 0不加查询条件 1是 2不是 
	 * @return ItemModelVO
	 */
	public ItemModelVO getItemModel(long categoryId, int isShow);
}
