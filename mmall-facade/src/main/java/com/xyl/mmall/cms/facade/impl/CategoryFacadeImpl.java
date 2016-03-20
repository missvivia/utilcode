package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.util.DistrictCodeUtil;
import com.xyl.mmall.cms.meta.SendDistrict;
import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.common.enums.CategoryErrorMsgEnum;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.dto.SearchCategoryContentDTO;
import com.xyl.mmall.content.service.CategoryContentService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.service.AgentService;

/**
 * CategoryFacadeImpl.java created by yydx811 at 2015年4月27日 上午10:45:38
 * 分类facade实现
 *
 * @author yydx811
 */
@Facade
public class CategoryFacadeImpl implements CategoryFacade {

	@Resource
	private CategoryService categoryService;

	@Resource
	private AgentService agentService;
	
	@Resource
	private CategoryContentService categoryContentService;
	
	@Override
	public List<CategoryNormalVO> getCategoryNormalList(BasePageParamVO<CategoryNormalVO> basePageParamVO) {
		List<CategoryNormalDTO> categoryNormalDTOList = null;
		// 是否分页
		if (basePageParamVO.getIsPage() == 1) {
			basePageParamVO.setTotal(categoryService.getCategoryNormalCount(null));
			categoryNormalDTOList = categoryService.getCategoryNormalList(basePageParamVO);
		} else {
			categoryNormalDTOList = categoryService.getCategoryNormalList(null);
		}
		return convertCategoryNormalVO(categoryNormalDTOList);
	}

	@Override
	public List<CategoryNormalVO> getFirstCategoryNormalList(BasePageParamVO<CategoryNormalVO> basePageParamVO) {
		List<CategoryNormalDTO> categoryNormalDTOList = null;
		// 是否分页
		if (basePageParamVO.getIsPage() == 1) {
			CategoryNormalDTO categoryNormalDTO = new CategoryNormalDTO();
			categoryNormalDTO.setLevel(CategoryNormalLevel.LEVEL_FIRST.getIntValue());
			basePageParamVO.setTotal(categoryService.getCategoryNormalCount(categoryNormalDTO));
			categoryNormalDTOList = categoryService.getFirstCategoryNormalList(basePageParamVO);
		} else {
			categoryNormalDTOList = categoryService.getFirstCategoryNormalList(null);
		}
		return convertCategoryNormalVO(categoryNormalDTOList);
	}

	@Override
	public List<CategoryNormalVO> getSubCategoryNormalList(long superCategoryId) {
		List<CategoryNormalDTO> categoryNormalDTOList = categoryService.getSubCategoryNormalList(superCategoryId);
		return convertCategoryNormalVO(categoryNormalDTOList);
	}

	@Override
	public CategoryNormalVO getCategoryNormalById(long id, boolean isContainSub) {
		CategoryNormalDTO categoryNormalDTO = categoryService.getCategoryNormalById(id, isContainSub);
		if (categoryNormalDTO == null || categoryNormalDTO.getId() < 1l) {
			return null;
		}
		CategoryNormalVO ret = new CategoryNormalVO(categoryNormalDTO);
		// 获取用户名
		AgentDTO agent = agentService.findAgentById(categoryNormalDTO.getAgentId());
		ret.setOperator(agent == null ? "" : agent.getRealName());
		return ret;
	}

	/**
	 * 将List<CategoryNormalDTO>转换为List<CategoryNormalVO>
	 * @param categoryNormalDTOList
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalVO> convertCategoryNormalVO(List<CategoryNormalDTO> categoryNormalDTOList) {
		if (CollectionUtils.isEmpty(categoryNormalDTOList)) {
			return new ArrayList<CategoryNormalVO>(0);
		} else {
			ArrayList<CategoryNormalVO> retlist = new ArrayList<CategoryNormalVO>(categoryNormalDTOList.size());
			for (CategoryNormalDTO obj : categoryNormalDTOList) {
				retlist.add(new CategoryNormalVO(obj));
			}
			return retlist;
		}
	}

	@Override
	public int createCategoryNormal(CategoryNormalDTO categoryNormalDTO) {
//		CategoryNormalDTO cnDTO = new CategoryNormalDTO();
//		cnDTO.setName(categoryNormalDTO.getName());
//		cnDTO = categoryService.getCategoryNormal(cnDTO);
//		if (cnDTO != null && cnDTO.getId() < 1l) {
//			return -1;
//		}
		if (categoryNormalDTO.getSuperCategoryId() > 0l) {
			CategoryNormalDTO cnDTO = categoryService.getCategoryNormalById(categoryNormalDTO.getSuperCategoryId(), false);
			if (cnDTO == null || cnDTO.getId() < 1l) {
				return -1;
			} 
			if (cnDTO.getLevel() == CategoryNormalLevel.LEVEL_FIRST.getIntValue()) {
				categoryNormalDTO.setLevel(CategoryNormalLevel.LEVEL_SECOND.getIntValue());
			} else if (cnDTO.getLevel() == CategoryNormalLevel.LEVEL_SECOND.getIntValue()) {
				categoryNormalDTO.setLevel(CategoryNormalLevel.LEVEL_THIRD.getIntValue());
			} else {
				return -2;
			}
		} else {
			categoryNormalDTO.setLevel(CategoryNormalLevel.LEVEL_FIRST.getIntValue());
		}
		return categoryService.createCategoryNormal(categoryNormalDTO);
	}

	@Override
	public int updateCategoryNormal(CategoryNormalDTO categoryNormalDTO) {
		return categoryService.updateCategoryNormal(categoryNormalDTO);
	}

	@Override
	public int getMaxShowIndex(CategoryNormalDTO categoryNormalDTO) {
		return categoryService.getMaxShowIndex(categoryNormalDTO);
	}

	@Override
	@Transaction
	public int updateCategoryNormalSort(CategoryNormalDTO categoryNormalDTO, int isUp) {
		return categoryService.updateCategoryNormalSort(categoryNormalDTO, isUp);
	}

	@Override
	public int deleteCategoryNormal(long id) {
		return categoryService.deleteCategoryNormal(id);
	}
	
	@Override
	public int saveCategoryContent(CategoryContentDTO dto) {
		//验证内容分类信息
		int checkResult = validCategoryContent(dto);
		if(checkResult!=CategoryErrorMsgEnum.SUCCESS.getIntValue()){
			return checkResult; 
		};
		categoryContentService.saveCategoryContent(dto);
		return CategoryErrorMsgEnum.SUCCESS.getIntValue();
	}


	@Override
	public List<CategoryContentVO> getSubCategoryContentList(
			long superCategoryId) {
		List<CategoryContentDTO> categoryContentDTOs = categoryContentService.getSubCategoryContentList(superCategoryId);
		return convertContentDTOToVO(categoryContentDTOs);
	}

	@Override
	public int updateCategoryContent(CategoryContentDTO dto) {
		//验证内容分类信息
		int checkResult = validCategoryContent(dto);
		if(checkResult!=CategoryErrorMsgEnum.SUCCESS.getIntValue()){
			return checkResult; 
		};
		categoryContentService.updateCategoryContent(dto);
		return CategoryErrorMsgEnum.SUCCESS.getIntValue();
	}

	@Override
	public boolean deleteCategoryContent(long categoryId) {
		return categoryContentService.deleteCategoryContent(categoryId);
	}
	
	private List<CategoryContentVO> convertContentDTOToVO(List<CategoryContentDTO> categoryContentDTOs) {
		List<CategoryContentVO> categoryContentVOs = new ArrayList<CategoryContentVO>();
		for(CategoryContentDTO categoryContentDTO: categoryContentDTOs) {
			categoryContentVOs.add(new CategoryContentVO(categoryContentDTO));
		}
		return categoryContentVOs;
	}

	//不使用分页了，搜索直接从缓存里取判断过滤
	@Override
	public BasePageParamVO<CategoryContentDTO> searchCategoryContentList(
			SearchCategoryContentDTO searchDto) {
		BasePageParamVO<CategoryContentDTO> basePageParamVO = new BasePageParamVO<CategoryContentDTO>();
		List<CategoryContentDTO> categoryContentDTOs = categoryContentService.getCategoryContentListByRootId(searchDto.getRootId());
		List<CategoryContentDTO> filterCategoryContentDTOs = new ArrayList<CategoryContentDTO>();
		for(CategoryContentDTO categoryContentDTO:categoryContentDTOs){
			if(categoryContentDTO.getName().indexOf(searchDto.getName())>=0){
				filterCategoryContentDTOs.add(categoryContentDTO);
				continue;
			}
			if(CollectionUtil.isNotEmptyOfList(categoryContentDTO.getSubCategoryContentDTOs())){
				List<CategoryContentDTO>secondFilterDtos = new ArrayList<CategoryContentDTO>();
				for(CategoryContentDTO secondCategoryContentDTO:categoryContentDTO.getSubCategoryContentDTOs()){
					if(secondCategoryContentDTO.getName().indexOf(searchDto.getName())>=0){
						secondFilterDtos.add(secondCategoryContentDTO);
						continue;
					}
					
					if(CollectionUtil.isNotEmptyOfList(secondCategoryContentDTO.getSubCategoryContentDTOs())){
						List<CategoryContentDTO>thirdFilterDtos = new ArrayList<CategoryContentDTO>();
						for(CategoryContentDTO thirdCategoryContentDTO:secondCategoryContentDTO.getSubCategoryContentDTOs()){
							if(thirdCategoryContentDTO.getName().indexOf(searchDto.getName())>=0){
								thirdFilterDtos.add(thirdCategoryContentDTO);
							}
						}
						if(CollectionUtil.isNotEmptyOfList(thirdFilterDtos)){
							secondCategoryContentDTO.setSubCategoryContentDTOs(thirdFilterDtos);
							secondFilterDtos.add(secondCategoryContentDTO);
						}
					}
				}
				if(CollectionUtil.isNotEmptyOfList(secondFilterDtos)){
					categoryContentDTO.setSubCategoryContentDTOs(secondFilterDtos);
					filterCategoryContentDTOs.add(categoryContentDTO);
				}
			}
			
		}
		basePageParamVO.setList(filterCategoryContentDTOs);
		return basePageParamVO;
	}
	
	@Override
	public List<CategoryContentDTO> getCategoryContentListByAreaId(long areaId) {
		long categoryRootId = 0l;
		categoryRootId = this.categoryRootIdByareaId(areaId);
		if(categoryRootId==0){
			return null;
		}
		return  categoryContentService.getCategoryContentListByRootId(categoryRootId);
	}
	
	public List<CategoryContentDTO> getCategoryContentListByAreaId(long areaId,Boolean hasAllData,Long categoryId) throws Exception{
		if(hasAllData ==null || hasAllData.booleanValue()){
			return this.getCategoryContentListByAreaId(areaId);
		}
		long categoryRootId = 0l;
		categoryRootId = this.categoryRootIdByareaId(areaId);
		if(categoryRootId==0){
			return null;
		}
		
		return categoryContentService.getCategoryContentListByRootId(categoryRootId, hasAllData, categoryId);
		
	}
	
	private long categoryRootIdByareaId(long areaId){
		List<CategoryContentDTO> categoryContentDTOs = categoryContentService.getCategoryContentListByLevelAndRootId(0, -1);
		long categoryRootId = 0l;
		for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
			if(StringUtils.isNotEmpty(categoryContentDTO.getDistrictIds())&&AreaCodeUtil.isContainArea(String.valueOf(areaId), categoryContentDTO.getDistrictIds())){
				categoryRootId = categoryContentDTO.getId();
				break;
			}
		}
		return categoryRootId;
	}
	
	//新增或者编辑内容分类时，验证
	private int validCategoryContent(CategoryContentDTO dto) {
		if (StringUtils.isEmpty(dto.getName())) {
			return CategoryErrorMsgEnum.CATEGORY_NAME_NULL_ERROR.getIntValue();
		}
		if (dto.getSuperCategoryId() > 0l) {
			CategoryContentDTO cnDTO = categoryContentService.getCategoryContentById(dto.getSuperCategoryId());
			if (cnDTO == null || cnDTO.getId() < 1l) {
				return CategoryErrorMsgEnum.SUB_CATEGORY_NOT_EXIST_ERROR.getIntValue();
			} else {
				if (cnDTO.getLevel() >= 3) {
					return CategoryErrorMsgEnum.CATEGORY_LEVEL_BEYOND_ERROR.getIntValue();
				} else {
					dto.setLevel(cnDTO.getLevel() + 1);
				}
			}
		} else {
			// 编辑时验证
			if (dto.getId() > 0 && dto.getLevel() != 0) {
				List<CategoryContentDTO> categoryContentDTOs = categoryContentService.getSubCategoryContentList(dto
						.getId());
				if (CollectionUtil.isNotEmptyOfList(categoryContentDTOs)) {
					CategoryContentDTO cnDTO = categoryContentService.getCategoryContentById(dto.getId());
					// 除了显示顺序能修改
					if (!cnDTO.getName().equals(dto.getName()) || cnDTO.getRootId() != dto.getRootId()
							|| cnDTO.getLevel() != dto.getLevel()
							|| cnDTO.getSuperCategoryId() != dto.getSuperCategoryId()) {
						return CategoryErrorMsgEnum.CATEGORY_MODIFIED_ERROR.getIntValue();
					}
				}
				if (StringUtils.isNotBlank(dto.getCategoryNormalIds())
						&& dto.getLevel() != CategoryNormalLevel.LEVEL_THIRD.getIntValue()) {
					return CategoryErrorMsgEnum.CONTENT_NORMAL_CATEGORY_RELATED_ERROR.getIntValue();
				}
			}
		}
		return CategoryErrorMsgEnum.SUCCESS.getIntValue();
	}

	@Override
	public CategoryContentVO getCategoryContentById(long id) {
		CategoryContentDTO categoryContentDTO = categoryContentService.getCategoryContentById(id);
		if (categoryContentDTO == null) {
			return null;
		}
		if (categoryContentDTO.getLevel() != 0) {
			long rootId = categoryContentDTO.getRootId();
			Map<Long, CategoryContentDTO> categoryMap = categoryContentService.getCategoryContentDTOMapByRootId(rootId);
			categoryContentDTO = categoryMap.get(id);
			if (categoryContentDTO == null) {
				return null;
			}
		}
		CategoryContentVO categoryContentVO = new CategoryContentVO(categoryContentDTO);
		// 构建关联商品分类
		if (StringUtils.isNotEmpty(categoryContentDTO.getCategoryNormalIds())) {
			List<Long> idList = new ArrayList<Long>();
			String[] idsArray = StringUtils.split(categoryContentDTO.getCategoryNormalIds(), ",");
			for (int i = 0; i < idsArray.length; i++) {
				idList.add(Long.parseLong(idsArray[i]));
			}
			categoryContentVO
					.setCategoryNormalVOs(convertCategoryNormalVO(categoryService.getCategoryListByIds(idList)));
		}
		// 构建区域
		if (StringUtils.isNotEmpty(categoryContentDTO.getDistrictIds())) {
			List<SendDistrictDTO> sendDistricts = new ArrayList<SendDistrictDTO>();
			String[] idsArray = StringUtils.split(categoryContentDTO.getDistrictIds(), ",");
			String[] namesArray = StringUtils.split(categoryContentDTO.getDistrictNames(), ",");
			for (int i = 0; i < idsArray.length; ++i) {
				SendDistrict sendDistrict = new SendDistrict();
				sendDistrict.setProvinceId(DistrictCodeUtil.getProvinceCode(idsArray[i]));
				sendDistrict.setCityId(idsArray[i].substring(2, 4).equals("00") ? 0 : DistrictCodeUtil
						.getCityCode(idsArray[i]));
				sendDistrict.setDistrictId(idsArray[i].substring(4, 6).equals("00") ? 0 : Long.parseLong(idsArray[i]));
				sendDistrict.setDistrictName(namesArray[i]);
				sendDistricts.add(new SendDistrictDTO(sendDistrict));
			}
			categoryContentVO.setSendDistrictDTOs(sendDistricts);
		}
		return categoryContentVO;
	}
	
	@Override
	public String getCategoryFullName(long categoryId) {
		return categoryService.getFullCategoryNormalName(categoryId);
	}

	@Override
	public String isContainNormalCategory(long categoryId) {
		List<CategoryContentDTO> contentDTOList = categoryContentService.queryThirdCategoryContentListBindCategoryNormal();
		StringBuffer ret = new StringBuffer();
		String id = String.valueOf(categoryId);
		for (CategoryContentDTO contentDTO : contentDTOList) {
			String[] categoryNormalIds = contentDTO.getCategoryNormalIds().split(",");
			Set<String> idSet = new HashSet<String>();
			idSet.addAll(Arrays.asList(categoryNormalIds));
			if (idSet.contains(id)) {
				ret.append(contentDTO.getName()).append("，");
			}
		}
		if (ret.length() > 0) {
			ret.deleteCharAt(ret.length() - 1);
			return ret.toString();
		}
		return null;
	}

	@Override
	public List<CategoryContentDTO> getCategoryContentListByLevelAndRootId(int level,long rootId){
		return categoryContentService.getCategoryContentListByLevelAndRootId(level, rootId);
	}
	
	@Override
	public List<CategoryContentDTO> getCategoryContentListByRootId(long rootId) {
		return categoryContentService.getCategoryContentListByRootId(rootId);
	}

	@Override
	public boolean deleteCategoryContentTree(long rootId) {
		return categoryContentService.deleteCategoryContent(rootId);
	}

	@Override
	public List<CategoryNormal> getCategoryNormalByName(String categoryNormalName, int level, long superId) {
		return categoryService.getCategoryNormalByName(categoryNormalName, level, superId);
	}

	@Override
	public CategoryContentVO getBriefCategoryContentById(long categoryContentId) {
		CategoryContentDTO categoryContentDTO = 
				categoryContentService.getCategoryContentById(categoryContentId);
		if (categoryContentDTO == null) {
			return null;
		}
		return new CategoryContentVO(categoryContentDTO);
	}
}
