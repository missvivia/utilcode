package com.xyl.mmall.content.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.constants.CategoryContentLevel;
import com.xyl.mmall.content.dao.CategoryContentDao;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.meta.CategoryContent;
import com.xyl.mmall.content.service.CategoryContentService;
import com.xyl.mmall.framework.annotation.Transaction;

@Service
public class CategoryContentServiceImpl implements CategoryContentService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryContentServiceImpl.class);

	@Autowired
	private CategoryContentDao categoryContentDao;

	public List<CategoryContentDTO> getSubCategoryContentList(long superCategoryContentId) {
		List<CategoryContent> categoryContentList = categoryContentDao
				.getCategoryContentListBySuperId(superCategoryContentId);
		return convertCategoryToDTO(categoryContentList);
	}

	@Override
	public List<CategoryContentDTO> getPageCategoryContentList(DDBParam ddbParam) {
		List<CategoryContent> categoryContentList = categoryContentDao.getCategoryContentList();
		return convertCategoryToDTO(categoryContentList);
	}

	@Override
	public CategoryContentDTO getCategoryContentById(long cid) {
		CategoryContent categoryContent = categoryContentDao.getObjectById(cid);
		return new CategoryContentDTO(categoryContent);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "categoryContentList", allEntries = true),
			@CacheEvict(value = "categoryContentMap", allEntries = true) })
	public boolean saveCategoryContent(CategoryContentDTO categoryContentDTO) {
		CategoryContent categoryContent = categoryContentDao.addObject((CategoryContent) categoryContentDTO);
		return categoryContent != null ? true : false;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "categoryContentList", allEntries = true),
			@CacheEvict(value = "categoryContentMap", allEntries = true) })
	public boolean updateCategoryContent(CategoryContentDTO categoryContentDTO) {
		return categoryContentDao.updateCategoryContent(categoryContentDTO);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "categoryContentList", allEntries = true),
			@CacheEvict(value = "categoryContentMap", allEntries = true) })
	public boolean deleteCategoryContent(long categoryId) {
		return categoryContentDao.deleteById(categoryId);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "categoryContentList", allEntries = true),
			@CacheEvict(value = "categoryContentMap", allEntries = true) })
	@Transaction
	public boolean deleteCategoryContentTree(long categoryId) {
		return categoryContentDao.deleteById(categoryId)
				|| categoryContentDao.deleteCategoryContentByRootId(categoryId);
	}

	private List<CategoryContentDTO> convertCategoryToDTO(List<CategoryContent> categoryContentList) {
		if (CollectionUtils.isNotEmpty(categoryContentList)) {
			List<CategoryContentDTO> dtoList = new ArrayList<CategoryContentDTO>(categoryContentList.size());
			for (CategoryContent categoryContent : categoryContentList) {
				dtoList.add(new CategoryContentDTO(categoryContent));
			}
			return dtoList;
		}
		return new ArrayList<CategoryContentDTO>(0);
	}

	@Override
	@Cacheable(value = "categoryContentList", key = "#rootId")
	public List<CategoryContentDTO> getCategoryContentListByRootId(long rootId) {
		List<CategoryContentDTO> toallist = convertCategoryToDTO(
				categoryContentDao.getCategoryContentListByRootId(rootId));
		List<CategoryContentDTO> tempList = null;
		Map<Long, List<CategoryContentDTO>> subLevel3Map = new HashMap<Long, List<CategoryContentDTO>>();
		Map<Long, List<CategoryContentDTO>> subLevel2Map = new HashMap<Long, List<CategoryContentDTO>>();
		List<CategoryContentDTO> level1List = new ArrayList<CategoryContentDTO>();
		// 构建内容分类层级关系
		for (CategoryContentDTO categoryContentDTO : toallist) {
			if (categoryContentDTO.getLevel() == CategoryContentLevel.LEVEL_THIRD.getIntValue()) {
				if (subLevel3Map.get(categoryContentDTO.getSuperCategoryId()) == null) {
					tempList = new ArrayList<CategoryContentDTO>();
					subLevel3Map.put(categoryContentDTO.getSuperCategoryId(), tempList);
				} else {
					tempList = (List<CategoryContentDTO>) subLevel3Map.get(categoryContentDTO.getSuperCategoryId());
				}
				tempList.add(categoryContentDTO);
			} else if (categoryContentDTO.getLevel() == CategoryContentLevel.LEVEL_SECOND.getIntValue()) {
				categoryContentDTO.setSubCategoryContentDTOs(subLevel3Map.get(categoryContentDTO.getId()));
				if (subLevel2Map.get(categoryContentDTO.getSuperCategoryId()) == null) {
					tempList = new ArrayList<CategoryContentDTO>();
					subLevel2Map.put(categoryContentDTO.getSuperCategoryId(), tempList);
				} else {
					tempList = (List<CategoryContentDTO>) subLevel2Map.get(categoryContentDTO.getSuperCategoryId());
				}
				tempList.add(categoryContentDTO);
			} else {
				categoryContentDTO.setSubCategoryContentDTOs(subLevel2Map.get(categoryContentDTO.getId()));
				level1List.add(categoryContentDTO);
			}
		}

		return level1List;
	}

	@Override
	@Cacheable(value = "categoryContentMap", key = "#rootId")
	public Map<Long, CategoryContentDTO> getCategoryContentDTOMapByRootId(long rootId) {
		return getNoCacheCategoryContentDTOMapByRootId(rootId);
	}

	public Map<Long, CategoryContentDTO> getNoCacheCategoryContentDTOMapByRootId(long rootId) {
		Map<Long, CategoryContentDTO> categoryMap = new HashMap<Long, CategoryContentDTO>();
		CategoryContent treeContent = categoryContentDao.getObjectById(rootId);
		if (treeContent == null) {
			return categoryMap;
		}
		List<CategoryContentDTO> toallist = getCategoryContentListByRootId(rootId);
		StringBuilder fstSbBuilder = null, secSbBuilder = null;
		// 赋值二三级区域值
		for (CategoryContentDTO categoryContentDTO : toallist) {
			fstSbBuilder = new StringBuilder();
			categoryMap.put(categoryContentDTO.getId(), categoryContentDTO);
			List<CategoryContentDTO> level2List = categoryContentDTO.getSubCategoryContentDTOs();
			if (level2List == null) {
				continue;
			}
			for (CategoryContentDTO secCategoryContentDTO : level2List) {
				secSbBuilder = new StringBuilder();
				categoryMap.put(secCategoryContentDTO.getId(), secCategoryContentDTO);
				secCategoryContentDTO.setDistrictIds(treeContent.getDistrictIds());
				secCategoryContentDTO.setDistrictNames(treeContent.getDistrictNames());
				List<CategoryContentDTO> level3List = secCategoryContentDTO.getSubCategoryContentDTOs();
				if (level3List == null) {
					continue;
				}
				for (CategoryContentDTO thdCategoryContentDTO : level3List) {
					categoryMap.put(thdCategoryContentDTO.getId(), thdCategoryContentDTO);
					thdCategoryContentDTO.setDistrictIds(treeContent.getDistrictIds());
					thdCategoryContentDTO.setDistrictNames(treeContent.getDistrictNames());// 构建二三级区域值
					if (StringUtils.isNotEmpty(thdCategoryContentDTO.getCategoryNormalIds()))
						secSbBuilder.append(thdCategoryContentDTO.getCategoryNormalIds()).append(",");
				}
				if (secSbBuilder.length() >= 1) {
					secCategoryContentDTO
							.setCategoryNormalIds(secSbBuilder.deleteCharAt(secSbBuilder.lastIndexOf(",")).toString());// 构建一二级内容类目包含商品分类值
				}
				if (StringUtils.isNotEmpty(secCategoryContentDTO.getCategoryNormalIds())) {
					fstSbBuilder.append(secCategoryContentDTO.getCategoryNormalIds()).append(",");
				}
			}
			if (fstSbBuilder.length() >= 1) {
				categoryContentDTO
						.setCategoryNormalIds(fstSbBuilder.deleteCharAt(fstSbBuilder.lastIndexOf(",")).toString());
			}

		}
		return categoryMap;
	}

	@Override
	public List<CategoryContentDTO> queryThirdCategoryContentListBindCategoryNormal() {
		List<CategoryContent> contentList = categoryContentDao.queryThirdCategoryContentListBindCategoryNormal();
		return convertCategoryToDTO(contentList);
	}

	@Override
	public List<CategoryContentDTO> getCategoryContentListByLevelAndRootId(int level, long rootId) {
		List<CategoryContent> contentList = categoryContentDao.getCategoryContentListByLevelAndRootId(level, rootId);
		return convertCategoryToDTO(contentList);
	}

	@Override
	@Cacheable(value = "categoryContentMap")
	public List<CategoryContentDTO> getCategoryContentListByRootId(long rootId, Boolean hasAllData, Long categoryId)
			throws Exception {
		// TODO Auto-generated method stub

		if (hasAllData == null || hasAllData.booleanValue()) {
			return this.getCategoryContentListByRootId(rootId);
		}
		List<CategoryContentDTO> toallist = convertCategoryToDTO(
				categoryContentDao.getCategoryContentListByRootId(rootId));
		List<CategoryContentDTO> categoryContentDTOs = new ArrayList<CategoryContentDTO>();
		if (categoryId == null) {
			for (CategoryContentDTO categoryContentDTO : toallist) {
				if (categoryContentDTO.getLevel() == CategoryContentLevel.LEVEL_FIRST.getIntValue()) {
					categoryContentDTOs.add(categoryContentDTO);
				}
			}
			return categoryContentDTOs;
		} else {
			CategoryContentDTO temp = null;
			for (CategoryContentDTO categoryContentDTO : toallist) {
				if (categoryContentDTO.getId() == categoryId.longValue()) {
					temp = categoryContentDTO;
					break;
				}
			}
			if (temp != null) {
				this.compositeCategory(toallist, temp);

				if (temp.getSubCategoryContentDTOs() != null && !temp.getSubCategoryContentDTOs().isEmpty()) {
					categoryContentDTOs.addAll(temp.getSubCategoryContentDTOs());
					// categoryContentDTOs.add(temp);
				}
			}

		}

		return categoryContentDTOs;
	}

	private void compositeCategory(List<CategoryContentDTO> list, CategoryContentDTO categoryContentDTO) {
		List<CategoryContentDTO> children = new ArrayList<CategoryContentDTO>();
		categoryContentDTO.setSubCategoryContentDTOs(children);
		for (CategoryContentDTO temp : list) {
			if (temp.getSuperCategoryId() == categoryContentDTO.getId()) {
				children.add(temp);
				this.compositeCategory(list, temp);
			}
		}
	}

}
