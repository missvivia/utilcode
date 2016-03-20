/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.model.ItemModelDao;
import com.xyl.mmall.itemcenter.dao.model.ModelParamOptionDao;
import com.xyl.mmall.itemcenter.dao.model.ModelParameterDao;
import com.xyl.mmall.itemcenter.dao.model.ModelSpeciOptionDao;
import com.xyl.mmall.itemcenter.dao.model.ModelSpecificationDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProdParamDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProdSpeciDao;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;
import com.xyl.mmall.itemcenter.meta.ItemModel;
import com.xyl.mmall.itemcenter.meta.ModelParamOption;
import com.xyl.mmall.itemcenter.meta.ModelParameter;
import com.xyl.mmall.itemcenter.meta.ModelSpeciOption;
import com.xyl.mmall.itemcenter.meta.ModelSpecification;
import com.xyl.mmall.itemcenter.service.ItemModelService;

/**
 * ItemModelServiceImpl.java created by yydx811 at 2015年5月5日 上午9:56:48
 * 商品模型service实现
 *
 * @author yydx811
 */
@Service
public class ItemModelServiceImpl implements ItemModelService {
	
	@Autowired
	private ItemModelDao itemModelDao;
	
	@Autowired
	private ModelParameterDao modelParameterDao;
	
	@Autowired
	private ModelParamOptionDao modelParamOptionDao;
	
	@Autowired
	private ModelSpecificationDao modelSpecificationDao;
	
	@Autowired
	private ModelSpeciOptionDao modelSpeciOptionDao;
	
	@Autowired
	private ProdParamDao prodParamDao;
	
	@Autowired
	private ProdSpeciDao prodSpeciDao;

	@Override
	public List<ItemModelDTO> getItemModelList(BasePageParamVO<?> pageParamVO, String searchValue, String startTime,
			String endTime) {
		List<ItemModel> modelList = null;
		if (pageParamVO == null) {
			modelList = itemModelDao.getItemModelList(searchValue, startTime, endTime);
		} else {
			modelList = itemModelDao.getItemModelList(pageParamVO, searchValue, startTime, endTime);
		}
		return convertItemModelDTO(modelList);
	}

	/**
	 * 将List<ItemModel>转换为List<ItemModelDTO>
	 * @param modelList
	 * @return List<ItemModelDTO>
	 */
	public List<ItemModelDTO> convertItemModelDTO(List<ItemModel> modelList) {
		if (CollectionUtils.isEmpty(modelList)) {
			return new ArrayList<ItemModelDTO>(0);
		} else {
			List<ItemModelDTO> retList = new ArrayList<ItemModelDTO>(modelList.size());
			for (ItemModel itemModel : modelList) {
				retList.add(new ItemModelDTO(itemModel));
			}
			return retList;
		}
	}

	@Override
	public ItemModelDTO getItemModel(long categoryId, boolean isContainList) {
		// 先根据分类id获取模型
		ItemModel model = new ItemModel();
		model.setCategoryNormalId(categoryId);
		model = itemModelDao.getItemModel(model);
		if (model == null || model.getId() < 1l) {
			return null;
		}
		ItemModelDTO retDTO = new ItemModelDTO(model);
		if (!isContainList) {
			return retDTO;
		}
		return getParamAndSpeciList(retDTO, 0);
	}
	
	public ItemModelDTO getParamAndSpeciList(ItemModelDTO retDTO, int isShow) {
		// 根据模型id获取属性列表
		retDTO.setParameterList(getModelParamList(retDTO.getId(), isShow));
		// 根据模型id获取规格列表
		List<ModelSpecification> specificationList = modelSpecificationDao.getSpecificationList(retDTO.getId(), isShow);
		if (!CollectionUtils.isEmpty(specificationList)) {
			// 按规格id获取规格列表
			List<ModelSpecificationDTO> retSpeciList = new ArrayList<ModelSpecificationDTO>(specificationList.size());
			for (ModelSpecification specification : specificationList) {
				ModelSpecificationDTO speciDTO = new ModelSpecificationDTO(specification);
				List<ModelSpeciOption> optionList = modelSpeciOptionDao.getSpeciOptionList(speciDTO.getId(), specification.getType());
				if (!CollectionUtils.isEmpty(optionList)) {
					List<ModelSpeciOptionDTO> retOptionList = new ArrayList<ModelSpeciOptionDTO>(optionList.size());
					for (ModelSpeciOption speciOption : optionList) {
						retOptionList.add(new ModelSpeciOptionDTO(speciOption));
					}
					speciDTO.setSpeciOptionList(retOptionList);
				}
				retSpeciList.add(speciDTO);
			}
			retDTO.setSpecificationList(retSpeciList);
		}
		return retDTO;
	}

	@Override
	@Transaction
	public long addItemModel(ItemModelDTO itemModelDTO) throws SQLException {
		// 添加模型，获取模型id
		ItemModel model = new ItemModel(itemModelDTO);
		long modelId = itemModelDao.addItemModel(model);
		if (modelId < 1l) {
			return -1l;
		}
		// 添加属性
		for (ModelParameterDTO parameterDTO : itemModelDTO.getParameterList()) {
			ModelParameter parameter = new ModelParameter(parameterDTO);
			parameter.setModelId(modelId);
			long parameterId = modelParameterDao.addModelParameter(parameter);
			if (parameterId < 1l) {
				throw new SQLException("Add modelParameter error!");
			}
			ArrayList<ModelParamOption> optionList = new ArrayList<ModelParamOption>(parameterDTO.getModelParamOptionList().size());
			for (ModelParamOptionDTO optionDTO : parameterDTO.getModelParamOptionList()) {
				ModelParamOption option = new ModelParamOption(optionDTO);
				option.setParameterId(parameterId);
				optionList.add(option);
			}
			if (!modelParamOptionDao.addBulkParamOptions(optionList)) {
				throw new SQLException("Add modelParamOption error!");
			}
		}
		// 添加规格
		for (ModelSpecificationDTO specificationDTO : itemModelDTO.getSpecificationList()) {
			ModelSpecification specification = new ModelSpecification(specificationDTO);
			specification.setModelId(modelId);
			long specificationId = modelSpecificationDao.addModelSpecification(specification);
			if (specificationId < 1l) {
				throw new SQLException("Add modelSpecification error!");
			}
			ArrayList<ModelSpeciOption> optionList = new ArrayList<ModelSpeciOption>(specificationDTO.getSpeciOptionList().size());
			int i = 1;
			for (ModelSpeciOptionDTO optionDTO : specificationDTO.getSpeciOptionList()) {
				ModelSpeciOption option = new ModelSpeciOption(optionDTO);
				option.setSpecificationId(specificationId);
				option.setShowIndex(i);
				optionList.add(option);
				i++;
			}
			if (!modelSpeciOptionDao.addBulkSpeciOptions(optionList)) {
				throw new SQLException("Add modelSpeciOption error!");
			}
		}
		return modelId;
	}

	@Override
	public ItemModelDTO getItemModelById(long id, boolean isContainList) {
		// 先根据id获取模型
		ItemModel model = new ItemModel();
		model.setId(id);
		model = itemModelDao.getItemModel(model);
		if (model == null || model.getId() < 1l) {
			return null;
		}
		ItemModelDTO retDTO = new ItemModelDTO(model);
		if (!isContainList) {
			return retDTO;
		}
		return getParamAndSpeciList(retDTO, 0);
	}

	@Override
	@Transaction
	public long addModelParameter(ModelParameterDTO parameterDTO) throws SQLException {
		ModelParameter parameter = new ModelParameter(parameterDTO);
		long parameterId = modelParameterDao.addModelParameter(parameter);
		if (parameterId < 1l) {
			throw new SQLException("Add modelParameter error!");
		}
		ArrayList<ModelParamOption> optionList = new ArrayList<ModelParamOption>(parameterDTO.getModelParamOptionList().size());
		for (ModelParamOptionDTO optionDTO : parameterDTO.getModelParamOptionList()) {
			ModelParamOption option = new ModelParamOption(optionDTO);
			option.setParameterId(parameterId);
			optionList.add(option);
		}
		if (!modelParamOptionDao.addBulkParamOptions(optionList)) {
			throw new SQLException("Add modelParamOption error!");
		}
		return parameterId;
	}

	@Override
	@Transaction
	public long addModelSpecification(ModelSpecificationDTO specificationDTO) throws SQLException {
		ModelSpecification specification = new ModelSpecification(specificationDTO);
		long specificationId = modelSpecificationDao.addModelSpecification(specification);
		if (specificationId < 1l) {
			throw new SQLException("Add modelSpecification error!");
		}
		ArrayList<ModelSpeciOption> optionList = new ArrayList<ModelSpeciOption>(specificationDTO.getSpeciOptionList().size());
		int i = 1;
		for (ModelSpeciOptionDTO optionDTO : specificationDTO.getSpeciOptionList()) {
			ModelSpeciOption option = new ModelSpeciOption(optionDTO);
			option.setSpecificationId(specificationId);
			option.setShowIndex(i);
			optionList.add(option);
			i++;
		}
		if (!modelSpeciOptionDao.addBulkSpeciOptions(optionList)) {
			throw new SQLException("Add modelSpeciOption error!");
		}
		return specificationId;
	}

	@Override
	public long addSpeciOption(ModelSpeciOptionDTO speciOptionDTO) {
		ModelSpeciOption speciOption = new ModelSpeciOption(speciOptionDTO);
		return modelSpeciOptionDao.addSpeciOption(speciOption);
	}

	@Override
	public long addParamOption(ModelParamOptionDTO paramOptionDTO) {
		ModelParamOption paramOption = new ModelParamOption(paramOptionDTO);
		return modelParamOptionDao.addParamOption(paramOption);
	}

	@Override
	public int getItemModelCount(String searchValue, String startTime, String endTime) {
		return itemModelDao.getItemModelCount(searchValue, startTime, endTime);
	}

	@Override
	public int updateItemModel(ItemModelDTO modelDTO) {
		ItemModel model = new ItemModel(modelDTO);
		return itemModelDao.updateItemModel(model);
	}

	@Override
	public ModelParameterDTO getModelParameter(long id, long modelId) {
		ModelParameter parameter = modelParameterDao.getModelParameter(id, modelId);
		if (parameter == null || parameter.getId() < 1l) {
			return null;
		} else {
			return new ModelParameterDTO(parameter);
		}
	}

	@Override
	public ModelSpecificationDTO getModelSpecification(long id, long modelId) {
		ModelSpecification specification = modelSpecificationDao.getModelSpecification(id, modelId);
		if (specification == null || specification.getId() < 1l) {
			return null;
		} else {
			return new ModelSpecificationDTO(specification);
		}
	}

	@Override
	public int updateModelParameter(ModelParameterDTO parameterDTO) {
		ModelParameter parameter = new ModelParameter(parameterDTO);
		return modelParameterDao.updateModelParameter(parameter);
	}

	@Override
	public int updateModelSpecification(ModelSpecificationDTO specificationDTO) {
		ModelSpecification specification = new ModelSpecification(specificationDTO);
		return modelSpecificationDao.updateModelSpecification(specification);
	}

	@Override
	public int updateModelParamOption(ModelParamOptionDTO optionDTO) {
		ModelParamOption option = new ModelParamOption(optionDTO);
		return modelParamOptionDao.updateModelParamOption(option);
	}

	@Override
	public int updateModelSpeciOption(ModelSpeciOptionDTO optionDTO) {
		ModelSpeciOption option = new ModelSpeciOption(optionDTO);
		return modelSpeciOptionDao.updateModelSpeciOption(option);
	}

	@Override
	public int deleteModelParamOption(long id, long parameterId) {
		return modelParamOptionDao.deleteModelParamOption(id, parameterId);
	}

	@Override
	public int deleteModelSpeciOption(long id, long specificationId) {
		return modelSpeciOptionDao.deleteModelSpeciOption(id, specificationId);
	}

	@Override
	@Transaction
	public int deleteModelParameter(long id, long modelId) {
		int res = modelParameterDao.deleteModelParameter(id, modelId);
		if (res < 1) {
			return res;
		}
		modelParamOptionDao.deleteBulkParamOption(id);
		return res;
	}

	@Override
	@Transaction
	public int deleteModelSpecification(long id, long modelId) {
		int res = modelSpecificationDao.deleteModelSpecification(id, modelId);
		if (res < 1) {
			return res;
		}
		modelSpeciOptionDao.deleteBulkModelSpeciOption(id);
		return res;
	}

	@Override
	@Transaction
	public int deleteItemModel(long id) {
		List<ModelParameter> parameterList = modelParameterDao.getParameterList(id, 0);
		if (CollectionUtils.isNotEmpty(parameterList)) {
			for (ModelParameter parameter : parameterList) {
				if (prodParamDao.countProdParamOptionInUse(parameter.getId(), 0) > 0) {
					return -2;
				}
			}
		}
		List<ModelSpecification> specificationList = modelSpecificationDao.getSpecificationList(id, 0);
		if (CollectionUtils.isNotEmpty(specificationList)) {
			for (ModelSpecification specification : specificationList) {
				if (prodSpeciDao.countProdSpeciOptionInUse(specification.getId(), 0) > 0) {
					return -3;
				}
			}
		}
		if (CollectionUtils.isNotEmpty(parameterList)) {
			for (ModelParameter parameter : parameterList) {
				deleteModelParameter(parameter.getId(), id);
			}
		}
		if (CollectionUtils.isNotEmpty(specificationList)) {
			for (ModelSpecification specification : specificationList) {
				deleteModelSpecification(specification.getId(), id);
			}
		}
		return itemModelDao.deleteItemModel(id);
	}

	@Override
	public List<ModelParamOptionDTO> getModelParamOptionList(long parameterId) {
		List<ModelParamOption> optionList = modelParamOptionDao.getParamOptionList(parameterId);
		if (CollectionUtils.isEmpty(optionList)) {
			return null;
		} else {
			List<ModelParamOptionDTO> retOptionList = new ArrayList<ModelParamOptionDTO>(optionList.size());
			for (ModelParamOption option : optionList) {
				retOptionList.add(new ModelParamOptionDTO(option));
			}
			return retOptionList;
		}
	}

	@Override
	public ModelSpeciOptionDTO getSpeciOption(long id, long specificationId) {
		ModelSpeciOption option = modelSpeciOptionDao.getModelSpeciOption(id, specificationId);
		if (option == null || option.getId() < 1l) {
			return null;
		} else {
			return new ModelSpeciOptionDTO(option);
		}
	}

	@Override
	public List<ModelParameterDTO> getModelParamList(long modelId, int isShow) {
		// 按属性id获取属性选项列表
		List<ModelParameter> parameterList = modelParameterDao.getParameterList(modelId, isShow);
		if (CollectionUtils.isEmpty(parameterList)) {
			return null;
		} else {
			List<ModelParameterDTO> retParamList = new ArrayList<ModelParameterDTO>(parameterList.size());
			for (ModelParameter modelParameter : parameterList) {
				ModelParameterDTO paramDTO = new ModelParameterDTO(modelParameter);
				List<ModelParamOption> optionList = modelParamOptionDao.getParamOptionList(paramDTO.getId());
				if (!CollectionUtils.isEmpty(optionList)) {
					List<ModelParamOptionDTO> retOptionList = new ArrayList<ModelParamOptionDTO>(optionList.size());
					for (ModelParamOption option : optionList) {
						retOptionList.add(new ModelParamOptionDTO(option));
					}
					paramDTO.setModelParamOptionList(retOptionList);
				}
				retParamList.add(paramDTO);
			}
			return retParamList;
		}
	}

	@Override
	public ItemModelDTO getItemModel(long categoryId, int isShow) {
		// 先根据分类id获取模型
		ItemModel model = new ItemModel();
		model.setCategoryNormalId(categoryId);
		model = itemModelDao.getItemModel(model);
		if (model == null || model.getId() < 1l) {
			return null;
		}
		ItemModelDTO retDTO = new ItemModelDTO(model);
		return getParamAndSpeciList(retDTO, isShow);
	}

	@Override
	public ModelSpecificationDTO getModelSpecification(long id) {
		ModelSpecification specification = modelSpecificationDao.getModelSpecification(id);
		if (specification == null || specification.getId() < 1l) {
			return null;
		} else {
			return new ModelSpecificationDTO(specification);
		}
	}
}
