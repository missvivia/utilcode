/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemModelFacade;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemModelVO;
import com.xyl.mmall.cms.vo.ModelParamOptionVO;
import com.xyl.mmall.cms.vo.ModelParameterVO;
import com.xyl.mmall.cms.vo.ModelSpeciOptionVO;
import com.xyl.mmall.cms.vo.ModelSpecificationVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;
import com.xyl.mmall.itemcenter.service.ItemModelService;

/**
 * ItemModelFacadeImpl.java created by yydx811 at 2015年5月5日 上午9:31:30
 * 商品模型facade实现
 *
 * @author yydx811
 */
@Facade
public class ItemModelFacadeImpl implements ItemModelFacade {

	private static Logger logger = Logger.getLogger(ItemModelFacadeImpl.class);
	
	@Resource
	private ItemModelService itemModelService;
	
	@Autowired
	private CategoryFacade categoryFacade;
	
	@Override
	public List<ItemModelVO> getItemModelList(BasePageParamVO<ItemModelVO> pageParamVO, String searchValue,
			String startTime, String endTime) {
		List<ItemModelDTO> modelList = null;
		// 是否分页
		if (pageParamVO.getIsPage() == 1) {
			pageParamVO.setTotal(itemModelService.getItemModelCount(searchValue, startTime, endTime));
			modelList = itemModelService.getItemModelList(pageParamVO, searchValue, startTime, endTime);
		} else {
			modelList = itemModelService.getItemModelList(null, searchValue, startTime, endTime);
		}
		return convertItemModelVO(modelList);
	}

	/**
	 * 将List<ItemModelDTO>转换为List<ItemModelVO>
	 * @param modelList
	 * @return List<ItemModelVO>
	 */
	public List<ItemModelVO> convertItemModelVO(List<ItemModelDTO> modelList) {
		if (CollectionUtils.isEmpty(modelList)) {
			return new ArrayList<ItemModelVO>(0);
		} else {
			ArrayList<ItemModelVO> retList = new ArrayList<ItemModelVO>(modelList.size());
			for (ItemModelDTO itemModelDTO : modelList) {
				retList.add(new ItemModelVO(itemModelDTO));
			}
			return retList;
		}
	}

	@Override
	public ItemModelVO getItemModel(long categoryId, boolean isContainList) {
		ItemModelDTO itemModel = itemModelService.getItemModel(categoryId, isContainList);
		if (itemModel == null) {
			return null;
		}
		ItemModelVO modelVO = new ItemModelVO(itemModel);
		CategoryNormalVO c = categoryFacade.getCategoryNormalById(categoryId, false);
		if (c == null) {
			logger.error("Get categoryNormal by id error! id : " + categoryId);
		} else {
			modelVO.setCategoryNormalName(c.getCategoryName());
		}
		return modelVO;
	}

	@Override
	public long addItemModel(ItemModelDTO itemModelDTO) {
		try {
			return itemModelService.addItemModel(itemModelDTO);
		} catch (SQLException e) {
			logger.error(e);
			return -1l;
		}
	}

	@Override
	public ItemModelVO getItemModelById(long id, boolean isContainList) {
		ItemModelDTO itemModel = itemModelService.getItemModelById(id, isContainList);
		if (itemModel == null) {
			return null;
		}
		ItemModelVO modelVO = new ItemModelVO(itemModel);
		CategoryNormalVO c = categoryFacade.getCategoryNormalById(modelVO.getCategoryNormalId(), false);
		if (c == null) {
			logger.error("Get categoryNormal by id error! id : " + modelVO.getCategoryNormalId());
		} else {
			modelVO.setCategoryNormalName(c.getCategoryName());
		}
		return modelVO;
	}

	@Override
	public long addModelParameter(ModelParameterDTO parameterDTO) {
		try {
			return itemModelService.addModelParameter(parameterDTO);
		} catch (SQLException e) {
			logger.error(e);
			return -1l;
		}
	}

	@Override
	public long addModelSpecification(ModelSpecificationDTO specificationDTO) {
		try {
			return itemModelService.addModelSpecification(specificationDTO);
		} catch (SQLException e) {
			logger.error(e);
			return -1l;
		}
	}

	@Override
	public long addSpeciOption(ModelSpeciOptionDTO speciOptionDTO) {
		return itemModelService.addSpeciOption(speciOptionDTO);
	}

	@Override
	public long addParamOption(ModelParamOptionDTO paramOptionDTO) {
		return itemModelService.addParamOption(paramOptionDTO);
	}

	@Override
	public int updateItemModel(ItemModelDTO modelDTO) {
		return itemModelService.updateItemModel(modelDTO);
	}

	@Override
	public ModelParameterVO getModelParameter(long id, long modelId) {
		ModelParameterDTO parameterDTO = itemModelService.getModelParameter(id, modelId);
		if (parameterDTO == null) {
			return null;
		} else {
			return new ModelParameterVO(parameterDTO);
		}
	}

	@Override
	public ModelSpecificationVO getModelSpecification(long id, long modelId) {
		ModelSpecificationDTO specificationDTO = itemModelService.getModelSpecification(id, modelId);
		if (specificationDTO == null) {
			return null;
		} else {
			return new ModelSpecificationVO(specificationDTO);
		}
	}

	@Override
	public int updateModelParameter(ModelParameterDTO parameterDTO) {
		return itemModelService.updateModelParameter(parameterDTO);
	}

	@Override
	public int updateModelSpecification(ModelSpecificationDTO specificationDTO) {
		return itemModelService.updateModelSpecification(specificationDTO);
	}

	@Override
	public int updateModelParamOption(ModelParamOptionDTO optionDTO) {
		return itemModelService.updateModelParamOption(optionDTO);
	}

	@Override
	public int updateModelSpeciOption(ModelSpeciOptionDTO optionDTO) {
		return itemModelService.updateModelSpeciOption(optionDTO);
	}

	@Override
	public int deleteModelParamOption(long id, long parameterId) {
		return itemModelService.deleteModelParamOption(id, parameterId);
	}

	@Override
	public int deleteModelSpeciOption(long id, long specificationId) {
		return itemModelService.deleteModelSpeciOption(id, specificationId);
	}

	@Override
	public int deleteModelParameter(long id, long modelId) {
		return itemModelService.deleteModelParameter(id, modelId);
	}

	@Override
	public int deleteModelSpecification(long id, long modelId) {
		return itemModelService.deleteModelSpecification(id, modelId);
	}

	@Override
	public int deleteItemModel(long id) {
		return itemModelService.deleteItemModel(id);
	}

	@Override
	public Map<Long, Set<Long>> getModelSpecificationKeyValues(long categoryId) {
		ItemModelVO modelVO = getItemModel(categoryId, true);
		if (modelVO == null) {
			return null;
		} else {
			if (CollectionUtils.isEmpty(modelVO.getSpecificationList())) {
				return null;
			} else {
				Map<Long, Set<Long>> modelSpeciMap = new HashMap<Long, Set<Long>>(modelVO.getSpecificationList().size());
				for (ModelSpecificationVO specificationVO : modelVO.getSpecificationList()) {
					if (CollectionUtils.isEmpty(specificationVO.getSpeciOptionList())) {
						modelSpeciMap.put(specificationVO.getSpecificationId(), new HashSet<Long>(0));
					} else {
						Set<Long> idSet = new HashSet<Long>(specificationVO.getSpeciOptionList().size());
						for (ModelSpeciOptionVO optionVO : specificationVO.getSpeciOptionList()) {
							idSet.add(optionVO.getSpeciOptionId());
						}
						modelSpeciMap.put(specificationVO.getSpecificationId(), idSet);
					}
				}
				return modelSpeciMap;
			}
		}
	}

	@Override
	public Map<Long, Set<Long>> getModelParamKeyValues(long categoryId) {
		ItemModelVO modelVO = getItemModel(categoryId, true);
		if (modelVO == null) {
			return null;
		} else {
			if (CollectionUtils.isEmpty(modelVO.getParameterList())) {
				return null;
			} else {
				Map<Long, Set<Long>> modelParamMap = new HashMap<Long, Set<Long>>(modelVO.getParameterList().size());
				for (ModelParameterVO parameterVO : modelVO.getParameterList()) {
					if (CollectionUtils.isEmpty(parameterVO.getOptionList())) {
						modelParamMap.put(parameterVO.getParameterId(), new HashSet<Long>(0));
					} else {
						Set<Long> idSet = new HashSet<Long>(parameterVO.getOptionList().size());
						for (ModelParamOptionVO optionVO : parameterVO.getOptionList()) {
							idSet.add(optionVO.getParamOptionId());
						}
						modelParamMap.put(parameterVO.getParameterId(), idSet);
					}
				}
				return modelParamMap;
			}
		}
	}

	@Override
	public ItemModelVO getItemModel(long categoryId, int isShow) {
		ItemModelDTO itemModel = itemModelService.getItemModel(categoryId, isShow);
		if (itemModel == null) {
			return null;
		}
		ItemModelVO modelVO = new ItemModelVO(itemModel);
		return modelVO;
	}
}
