/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.service;

import java.sql.SQLException;
import java.util.List;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;

/**
 * ItemModelService.java created by yydx811 at 2015年5月5日 上午9:50:09
 * 商品模型service
 *
 * @author yydx811
 */
public interface ItemModelService {

	/**
	 * 获取商品模型列表
	 * @param pageParamVO
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return List<ItemModelDTO>
	 */
	public List<ItemModelDTO> getItemModelList(BasePageParamVO<?> pageParamVO, String searchValue, 
			String startTime, String endTime);
	
	/**
	 * 按分类id获取属性和规格列表
	 * @param categoryId
	 * @param isContainList
	 * @return ItemModelDTO
	 */
	public ItemModelDTO getItemModel(long categoryId, boolean isContainList);
	
	/**
	 * 添加商品模型
	 * @param itemModelDTO
	 * @return long
	 */
	public long addItemModel(ItemModelDTO itemModelDTO) throws SQLException;
	
	/**
	 * 按id获取商品模型
	 * @param id
	 * @param isContainList
	 * @return ItemModelDTO
	 */
	public ItemModelDTO getItemModelById(long id, boolean isContainList);
	
	/**
	 * 添加商品模型属性
	 * @param parameterDTO
	 * @return long
	 */
	public long addModelParameter(ModelParameterDTO parameterDTO) throws SQLException;

	/**
	 * 添加商品模型规格
	 * @param specificationDTO
	 * @return long
	 */
	public long addModelSpecification(ModelSpecificationDTO specificationDTO) throws SQLException;
	
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
	 * 按条件获取数量
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return int
	 */
	public int getItemModelCount(String searchValue, String startTime, String endTime);

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
	public ModelParameterDTO getModelParameter(long id, long modelId);
	
	/**
	 * 根据模型属性id获取选项列表
	 * @param parameterId
	 * @return
	 */
	public List<ModelParamOptionDTO> getModelParamOptionList(long parameterId);

	/**
	 * 获取商品模型规格
	 * @param id
	 * @param modelId
	 * @return
	 */
	public ModelSpecificationDTO getModelSpecification(long id, long modelId);

	/**
	 * 获取商品模型规格
	 * @param id
	 * @return
	 */
	public ModelSpecificationDTO getModelSpecification(long id);

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
	 * 获取选项
	 * @param id
	 * @param specificationId
	 * @return
	 */
	public ModelSpeciOptionDTO getSpeciOption(long id, long specificationId);
	
	/**
	 * 按模型id获取属性列表
	 * @param modelId
	 * @param isShow 筛选项查询 0不加查询条件 1是 2不是 
	 * @return
	 */
	public List<ModelParameterDTO> getModelParamList(long modelId, int isShow);

	/**
	 * 按分类id获取属性和规格列表
	 * @param categoryId
	 * @param isShow 筛选项查询 0不加查询条件 1是 2不是 
	 * @return ItemModelDTO
	 */
	public ItemModelDTO getItemModel(long categoryId, int isShow);
}
