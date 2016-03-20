/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemModelFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemModelVO;
import com.xyl.mmall.cms.vo.ModelParamOptionVO;
import com.xyl.mmall.cms.vo.ModelParameterVO;
import com.xyl.mmall.cms.vo.ModelSpeciOptionVO;
import com.xyl.mmall.cms.vo.ModelSpecificationVO;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.itemcenter.enums.ModelParamOptionType;
import com.xyl.mmall.itemcenter.enums.ModelSpeciOptionType;

/**
 * ItemModelController.java created by yydx811 at 2015年4月29日 下午5:53:39
 * 商品管理controller
 *
 * @author yydx811
 */
@Controller
//@RequestMapping(value = "/item")
public class ItemModelController {

	private static Logger logger = Logger.getLogger(ItemModelController.class);
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private ItemModelFacade itemModelFacade;
	
	@Autowired
	private CategoryFacade categoryFacade;
	
	@Autowired
	private ProductFacade productFacade;
	
	@RequestMapping(value = "/item/model")
	@RequiresPermissions(value = { "item:model" })
	public String itemModel(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/item/model";
	}
	
	@RequestMapping(value = "/item/createmodel", method = RequestMethod.GET)
	public String create(@RequestParam(required = false, value = "modelId") Long id,Model model) {
		model.addAttribute("modelId", id);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/item/createmodel";
	}
	/**
	 * 获取模型列表
	 * @param pageParamVO
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/item/model/list")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO getItemModelList(BasePageParamVO<ItemModelVO> pageParamVO, 
			String searchValue, String startTime, String endTime) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isNotBlank(startTime)) {
			if (!RegexUtils.isAllNumber(startTime)) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
			}
		}
		if (StringUtils.isNotBlank(endTime)) {
			if (!RegexUtils.isAllNumber(endTime)) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
			}
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		pageParamVO.setList(itemModelFacade.getItemModelList(pageParamVO, searchValue, startTime, endTime));
		ret.setResult(pageParamVO);
		return ret;
	}

	/**
	 * 根据分类id获取模型
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/item/model/checkModel")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO getModelParameterAndSpecificationList(
			@RequestParam(required = true, value = "categoryId") long categoryId) {
		BaseJsonVO ret = new BaseJsonVO();
		if (itemModelFacade.getItemModel(categoryId, false) == null) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("分类下不存在模型，可以使用。");
		} else {
			ret.setCode(ResponseCode.RES_EEXIST);
			ret.setMessage("该分类下已存在一个模型，无法继续新建。");
		}
		return ret;
	}
/*
	public @ResponseBody BaseJsonVO getModelParameter(
			@RequestParam(required = true, value = "parameterId") long parameterId) {
		BaseJsonVO ret = new BaseJsonVO();
		return ret;
	}
	
	public @ResponseBody BaseJsonVO getModelSpecification(
			@RequestParam(required = true, value = "specificationId") long specificationId) {
		BaseJsonVO ret = new BaseJsonVO();
		return ret;
	}
*/
	/**
	 * 创建模型
	 * @param itemModelVO
	 * @return
	 */
	@RequestMapping(value = "/item/model/create", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO addItemModel(@RequestBody ItemModelVO itemModelVO) {
		BaseJsonVO ret = new BaseJsonVO();
		// 模型参数校验
		if (itemModelVO.getCategoryNormalId() < 1l || StringUtils.isBlank(itemModelVO.getModelName())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		CategoryNormalVO c = categoryFacade.getCategoryNormalById(itemModelVO.getCategoryNormalId(), false);
		if (c.getCategoryDepth() != CategoryNormalLevel.LEVEL_THIRD.getIntValue()) {
			ret.setCode(ResponseCode.RES_EPARAM);
			ret.setMessage("【" + c.getCategoryName() + "】不是第三级分类，不能添加商品模型！");
			return ret;
		}
		ItemModelVO tmp = itemModelFacade.getItemModel(itemModelVO.getCategoryNormalId(), false);
		if (tmp != null && tmp.getModelId() > 0l) {
			ret.setCode(ResponseCode.RES_EEXIST);
			ret.setMessage("【" + tmp.getCategoryNormalName() + "】已存在商品模型【" + tmp.getModelName() + "】！");
			return ret;
		}
		ItemModelDTO itemModelDTO = itemModelVO.convertToDTO();
		long uid = SecurityContextUtils.getUserId();
		itemModelDTO.setAgentId(uid);
		// 属性参数校验
		if (CollectionUtils.isEmpty(itemModelVO.getParameterList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加扩展属性！");
		}
		ArrayList<ModelParameterDTO> parameterDTOList = new ArrayList<ModelParameterDTO>(itemModelVO.getParameterList().size());
		for (ModelParameterVO parameterVO : itemModelVO.getParameterList()) {
			if (CollectionUtils.isEmpty(parameterVO.getOptionList())) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加属性选项！");
			}
			if (StringUtils.isBlank(parameterVO.getParameterName())) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性名不能为空！");
			}
			if (ModelParamOptionType.genEnumByIntValue(parameterVO.getSingle()) == null) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择属性操作样式！");
			}
			if (parameterVO.getShow() != 1) {
				parameterVO.setShow(2);
			}
			ModelParameterDTO parameterDTO = parameterVO.convertToDTO();
			parameterDTO.setAgentId(uid);
			// 属性选项校验
			ArrayList<ModelParamOptionDTO> optionDTOList = new ArrayList<ModelParamOptionDTO>(parameterVO.getOptionList().size());
			for (ModelParamOptionVO optionVO : parameterVO.getOptionList()) {
				if (StringUtils.isBlank(optionVO.getParamOption())) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性选项名不能为空！");
				}
				ModelParamOptionDTO optionDTO = optionVO.convertToDTO();
				optionDTO.setAgentId(uid);
				optionDTOList.add(optionDTO);
			}
			parameterDTO.setModelParamOptionList(optionDTOList);
			parameterDTOList.add(parameterDTO);
		}
		itemModelDTO.setParameterList(parameterDTOList);
		// 规格参数校验
		if (CollectionUtils.isEmpty(itemModelVO.getSpecificationList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加规格参数！");
		}
		ArrayList<ModelSpecificationDTO> specificationDTOList = 
				new ArrayList<ModelSpecificationDTO>(itemModelVO.getSpecificationList().size());
		for (ModelSpecificationVO specificationVO : itemModelVO.getSpecificationList()) {
			if (CollectionUtils.isEmpty(specificationVO.getSpeciOptionList())) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加规格选项！");
			}
			if (StringUtils.isBlank(specificationVO.getSpecificationName())) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格名不能为空！");
			}
			if (ModelSpeciOptionType.genEnumByIntValue(specificationVO.getSpecificationType()) == null) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择规格显示样式！");
			}
			if (specificationVO.getShow() != 1) {
				specificationVO.setShow(2);
			}
			ModelSpecificationDTO specificationDTO = specificationVO.convertToDTO();
			specificationDTO.setAgentId(uid);
			// 规格选项校验
			ArrayList<ModelSpeciOptionDTO> optionDTOList = new ArrayList<ModelSpeciOptionDTO>(specificationVO.getSpeciOptionList().size());
			for (ModelSpeciOptionVO optionVO : specificationVO.getSpeciOptionList()) {
				if (StringUtils.isBlank(optionVO.getSpeciOption())) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格选项名不能为空！");
				}
				if (ModelSpeciOptionType.genEnumByIntValue(optionVO.getSpeciOptionType()) == null) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择规格显示样式！");
				}
				ModelSpeciOptionDTO optionDTO = optionVO.convertToDTO();
				optionDTO.setAgentId(uid);
				optionDTOList.add(optionDTO);
			}
			specificationDTO.setSpeciOptionList(optionDTOList);
			specificationDTOList.add(specificationDTO);
		}
		itemModelDTO.setSpecificationList(specificationDTOList);
		
		long res = itemModelFacade.addItemModel(itemModelDTO);
		if (res > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("创建成功！");
			ret.setResult(res);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("创建失败！");
		}
		return ret;
	}

	/**
	 * 添加属性
	 * @param parameterVO
	 * @return
	 */
	@RequestMapping(value = "/item/model/paramAdd", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO addModelParameter(@RequestBody ModelParameterVO parameterVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (CollectionUtils.isEmpty(parameterVO.getOptionList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请输入选项！");
		}
		if (StringUtils.isBlank(parameterVO.getParameterName())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性名不能为空！");
		}
		if (parameterVO.getItemModelId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择商品模型！");
		}
		if (ModelParamOptionType.genEnumByIntValue(parameterVO.getSingle()) == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请属性选择操作样式！");
		}
		if (parameterVO.getShow() != 1) {
			parameterVO.setShow(2);
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(parameterVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		ModelParameterDTO parameterDTO = parameterVO.convertToDTO();
		long uid = SecurityContextUtils.getUserId();
		parameterDTO.setAgentId(uid);
		ArrayList<ModelParamOptionDTO> optionDTOList = new ArrayList<ModelParamOptionDTO>(parameterVO.getOptionList().size());
		for (ModelParamOptionVO optionVO : parameterVO.getOptionList()) {
			if (StringUtils.isBlank(optionVO.getParamOption())) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "选项名不能为空！");
			}
			ModelParamOptionDTO optionDTO = optionVO.convertToDTO();
			optionDTO.setAgentId(uid);
			optionDTOList.add(optionDTO);
		}
		parameterDTO.setModelParamOptionList(optionDTOList);

		long res = itemModelFacade.addModelParameter(parameterDTO);
		if (res > 0) {
			ret.setCode(ErrorCode.SUCCESS);
			ret.setMessage("创建成功！");
			ret.setResult(res);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("创建失败！");
		}
		
		return ret;
	}

	/**
	 * 添加属性选项
	 * @param optionVO
	 * @return
	 */
	@RequestMapping(value = "/item/model/paramOptionAdd", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO addParamOption(@RequestBody ModelParamOptionVO optionVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isBlank(optionVO.getParamOption())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性选项名不能为空！");
		}
		if (optionVO.getModelParameterId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择属性！");
		}
		ModelParameterVO parameterVO = itemModelFacade.getModelParameter(optionVO.getModelParameterId(), optionVO.getItemModelId());
		if (parameterVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性不存在！");
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(optionVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		
		ModelParamOptionDTO optionDTO = optionVO.convertToDTO();
		optionDTO.setAgentId(SecurityContextUtils.getUserId());

		long res = itemModelFacade.addParamOption(optionDTO);
		if (res > 0) {
			ret.setCode(ErrorCode.SUCCESS);
			ret.setMessage("创建成功！");
			ret.setResult(res);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("创建失败！");
		}
		return ret;
	}

	/**
	 * 添加模型规格
	 * @param specificationVO
	 * @return
	 */
	@RequestMapping(value = "/item/model/speciAdd", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO addModelSpecification(@RequestBody ModelSpecificationVO specificationVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (CollectionUtils.isEmpty(specificationVO.getSpeciOptionList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加规格选项！");
		}
		if (StringUtils.isBlank(specificationVO.getSpecificationName())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格名不能为空！");
		}
		if (specificationVO.getItemModelId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择商品模型！");
		}
		if (ModelSpeciOptionType.genEnumByIntValue(specificationVO.getSpecificationType()) == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择规格显示样式！");
		}
		if (specificationVO.getShow() != 1) {
			specificationVO.setShow(2);
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(specificationVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		ModelSpecificationDTO specificationDTO = specificationVO.convertToDTO();
		long uid = SecurityContextUtils.getUserId();
		specificationDTO.setAgentId(uid);
		// 规格选项校验
		ArrayList<ModelSpeciOptionDTO> optionDTOList = new ArrayList<ModelSpeciOptionDTO>(specificationVO.getSpeciOptionList().size());
		for (ModelSpeciOptionVO optionVO : specificationVO.getSpeciOptionList()) {
			if (StringUtils.isBlank(optionVO.getSpeciOption())) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格选项名不能为空！");
			}
			if (ModelSpeciOptionType.genEnumByIntValue(optionVO.getSpeciOptionType()) == null) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择规格显示样式！");
			}
			ModelSpeciOptionDTO optionDTO = optionVO.convertToDTO();
			optionDTO.setAgentId(uid);
			optionDTOList.add(optionDTO);
		}
		specificationDTO.setSpeciOptionList(optionDTOList);

		long res = itemModelFacade.addModelSpecification(specificationDTO);
		if (res > 0) {
			ret.setCode(ErrorCode.SUCCESS);
			ret.setMessage("创建成功！");
			ret.setResult(res);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("创建失败！");
		}
		return ret;
	}

	/**
	 * 添加规格选项
	 * @param optionVO
	 * @return
	 */
	@RequestMapping(value = "/item/model/speciOptionAdd", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO addSpeciOption(@RequestBody ModelSpeciOptionVO optionVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isBlank(optionVO.getSpeciOption())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格选项名不能为空！");
		}
		if (optionVO.getSpecificationId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择规格！");
		}
		if (ModelSpeciOptionType.genEnumByIntValue(optionVO.getSpeciOptionType()) == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择规格显示样式！");
		}
		ModelSpecificationVO specificationVO = itemModelFacade.getModelSpecification(optionVO.getSpecificationId(), 
				optionVO.getItemModelId());
		if (specificationVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格不存在！");
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(optionVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		
		ModelSpeciOptionDTO optionDTO = optionVO.convertToDTO();
		optionDTO.setAgentId(SecurityContextUtils.getUserId());

		long res = itemModelFacade.addSpeciOption(optionDTO);
		if (res > 0) {
			ret.setCode(ErrorCode.SUCCESS);
			ret.setMessage("创建成功！");
			ret.setResult(res);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("创建失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/item/model/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO getItemModel(@RequestParam(required = true, value = "modelId") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		ItemModelVO modelVO = itemModelFacade.getItemModelById(id, true);
		if (modelVO == null) {
			ret.setCode(404);
			ret.setMessage("商品模型不存在！");
		} else {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(modelVO);
		}
		return ret;
	}

	@RequestMapping(value = "/item/model/update", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO updateItemModel(@RequestBody ItemModelVO modelVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (modelVO.getModelId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		ItemModelVO old = itemModelFacade.getItemModelById(modelVO.getModelId(), true);
		// 判断属性列表
		if (CollectionUtils.isEmpty(old.getParameterList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加属性列表！");
		}
		// 判断规格列表
		if (CollectionUtils.isEmpty(old.getSpecificationList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请添加规格列表！");
		}
		// 判断分类 
		if (modelVO.getCategoryNormalId() > 0l) {
			if (modelVO.getCategoryNormalId() != old.getCategoryNormalId()) {
				ItemModelVO tmp = itemModelFacade.getItemModel(modelVO.getCategoryNormalId(), false);
				if (tmp != null) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "【" + tmp.getCategoryNormalName() + "】分类下已存在商品模型！");
				}
			}
		}
		ItemModelDTO modelDTO = modelVO.convertToDTO();
		modelDTO.setAgentId(SecurityContextUtils.getUserId());
		int res = itemModelFacade.updateItemModel(modelDTO);
		if (res > 0){
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新失败！");
		}
	}

	@RequestMapping(value = "/item/model/updateParam", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO updateParam(@RequestBody ModelParameterVO parameterVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (parameterVO.getParameterId() < 1l || parameterVO.getItemModelId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误!");
		}
		if (parameterVO.getSingle() > 0 && ModelParamOptionType.genEnumByIntValue(parameterVO.getSingle()) == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误!");
		}
		ModelParameterVO old = itemModelFacade.getModelParameter(parameterVO.getParameterId(), parameterVO.getItemModelId());
		if (old == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "模型属性不存在！");
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(parameterVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		if (parameterVO.getShow() > 0 && parameterVO.getShow() != 1) {
			parameterVO.setShow(2);
		}

		ModelParameterDTO parameterDTO = parameterVO.convertToDTO();
		parameterDTO.setAgentId(SecurityContextUtils.getUserId());
		int res = itemModelFacade.updateModelParameter(parameterDTO);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新失败！");
		}
	}

	@RequestMapping(value = "/item/model/updateSpeci", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO updateSpeci(@RequestBody ModelSpecificationVO specificationVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (specificationVO.getSpecificationId() < 1l || specificationVO.getItemModelId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误!");
		}
		if (specificationVO.getSpecificationType() > 0 
				&& ModelSpeciOptionType.genEnumByIntValue(specificationVO.getSpecificationType()) == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误!");
		}
		ModelSpecificationVO old = itemModelFacade.getModelSpecification(
				specificationVO.getSpecificationId(), specificationVO.getItemModelId());
		if (old == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "模型规格不存在！");
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(specificationVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		if (specificationVO.getShow() > 0 && specificationVO.getShow() != 1) {
			specificationVO.setShow(2);
		}
		ModelSpecificationDTO specificationDTO = specificationVO.convertToDTO();
		specificationDTO.setAgentId(SecurityContextUtils.getUserId());
		int res = itemModelFacade.updateModelSpecification(specificationDTO);

		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新失败！");
		}
	}

	@RequestMapping(value = "/item/model/updateParamOption", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO updateParamOption(@RequestBody ModelParamOptionVO optionVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (optionVO.getParamOptionId() < 1l || optionVO.getModelParameterId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误!");
		}
		ModelParameterVO parameterVO = itemModelFacade.getModelParameter(optionVO.getModelParameterId(), optionVO.getItemModelId());
		if (parameterVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "模型属性不存在！");
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(optionVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		ModelParamOptionDTO optionDTO = optionVO.convertToDTO();
		optionDTO.setAgentId(SecurityContextUtils.getUserId());
		int res = itemModelFacade.updateModelParamOption(optionDTO);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新失败！");
		}
	}

	@RequestMapping(value = "/item/model/updateSpeciOption", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO updateSpeciOption(@RequestBody ModelSpeciOptionVO optionVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (optionVO.getSpeciOptionId() < 1l || optionVO.getSpecificationId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误!");
		}
		ModelSpecificationVO specificationVO = itemModelFacade.getModelSpecification(optionVO.getSpecificationId(),
				optionVO.getItemModelId());
		if (specificationVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "模型规格不存在！");
		}
		ItemModelVO modelVO = itemModelFacade.getItemModelById(optionVO.getItemModelId(), false);
		if (modelVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品模型不存在！");
		}
		ModelSpeciOptionDTO optionDTO = optionVO.convertToDTO();
		optionDTO.setAgentId(SecurityContextUtils.getUserId());
		int res = itemModelFacade.updateModelSpeciOption(optionDTO);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新失败！");
		}
	}
	
	@RequestMapping(value = "/item/model/del")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO deleteItemModel(@RequestParam(required = true, value = "modelId") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		// service里检测是否有在使用的商品
		int res = itemModelFacade.deleteItemModel(id);
		if (res > 0) {
			logger.info("Delete item model success! ModelId : " + id + " AgentId : " + SecurityContextUtils.getUserId());
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		} else if (res == -2){
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "该模型中属性有正在使用的商品！");
		} else if (res == -3){
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "该模型中规格有正在使用的商品！");
		}  else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "删除失败！");
		}
	}
	
	@RequestMapping(value = "/item/model/delParam")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO deleteModelParm(@RequestParam(required = true, value = "itemModelId") long modelId, 
			@RequestParam(required = true, value = "parameterId") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		// 检测是否有在使用的商品
		if (productFacade.countProdParamInUse(id) > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "该属性有正在使用的商品！");
		}
		if (itemModelFacade.deleteModelParameter(id, modelId) > 0) {
			logger.info("Delete item model param success! ModelId : " + modelId 
					+ " ParamId : " + id + " AgentId : " + SecurityContextUtils.getUserId());
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "删除失败！");
		}
	}
	
	@RequestMapping(value = "/item/model/delSpeci")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO deleteModelSpeci(@RequestParam(required = true, value = "itemModelId") long modelId, 
			@RequestParam(required = true, value = "specificationId") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		// 检测是否有在使用的商品
		if (productFacade.countProdSpeciInUse(id) > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "该规格有正在使用的商品！");
		}
		if (itemModelFacade.deleteModelSpecification(id, modelId) > 0) {
			logger.info("Delete item model speci success! ModelId : " + modelId 
					+ " SpeciId : " + id + " AgentId : " + SecurityContextUtils.getUserId());
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "删除失败！");
		}
	}
	
	@RequestMapping(value = "/item/model/delParamOption")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO deleteModelParamOption(@RequestParam(required = true, value = "paramOptionId") long id, 
			@RequestParam(required = true, value = "parameterId") long parameterId) {
		BaseJsonVO ret = new BaseJsonVO();
		// 检测是否有在使用的商品
		if (productFacade.countProdParamOptionInUse(parameterId, id) > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "该属性选项有正在使用的商品！");
		}
		if (itemModelFacade.deleteModelParamOption(id, parameterId) > 0) {
			logger.info("Delete item model param success! ParamId : " + parameterId 
					+ " ParamOptionId : " + id + " AgentId : " + SecurityContextUtils.getUserId());
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "删除失败！");
		}
	}
	
	@RequestMapping(value = "/item/model/delSpeciOption")
	@RequiresPermissions(value = { "item:model" })
	public @ResponseBody BaseJsonVO deleteModelSpeciOption(@RequestParam(required = true, value = "speciOptionId") long id, 
			@RequestParam(required = true, value = "specificationId") long specificationId) {
		BaseJsonVO ret = new BaseJsonVO();
		// 检测是否有在使用的商品
		if (productFacade.countProdSpeciOptionInUse(specificationId, id) > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "该规格选项有正在使用的商品！");
		}
		if (itemModelFacade.deleteModelSpeciOption(id, specificationId) > 0) {
			logger.info("Delete item model speci success! SpeciId : " + specificationId 
					+ " SpeciOptionId : " + id + " AgentId : " + SecurityContextUtils.getUserId());
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "删除失败！");
		}
	}
	
	
	private BaseJsonVO setCodeAndMessage(BaseJsonVO ret, int code, String message) {
		ret.setCode(code);
		ret.setMessage(message);
		return ret;
	}
}
