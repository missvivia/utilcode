package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dao.category.CategoryNormalDao;
import com.xyl.mmall.itemcenter.dao.product.PoProductDao;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;
import com.xyl.mmall.itemcenter.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private PoProductDao poProductDao;

	@Autowired
	private CategoryNormalDao categoryNormalDao;

	public List<Category> getSubCategoryList(long superCategoryId) {
		try {
			List<Category> retList = categoryDao.getCategoryListBySuperId(superCategoryId);
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Category> getCategoryList() {
		try {
			return categoryDao.getCategoryList();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	public List<Category> getCategoryListBylowId(long lowCategoryId) {
		try {
			List<Category> retList = new ArrayList<Category>();
			categoryDao.getCategoryListByLowestId(retList, lowCategoryId);
			Collections.reverse(retList);
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public Category getCategoryById(long cid) {
		try {
			return categoryDao.getCategoryById(cid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Category> getCategoryListByLevel(int level) {
		return categoryDao.getCategoryListByLevel(level);
	}

	@Override
	public Category getFirstCategoryByLowestId(long lowerId) {
		return categoryDao.getFirstCategoryByLowestId(lowerId);
	}

	@Override
	public List<Category> getLowestCategoryById(long id) {
		List<Category> retList = new ArrayList<Category>();
		categoryDao.getLowestCategoryById(retList, id);
		return retList;
	}

	@Override
	public List<CategoryNormalDTO> getCategoryNormalList(BasePageParamVO<?> basePageParamVO) {
		if (basePageParamVO == null) {
			return categoryNormalDao.getCategoryNormalList();
		} else {
			return categoryNormalDao.getCategoryNormalList(basePageParamVO);
		}
	}

	@Override
	public List<CategoryNormalDTO> getFirstCategoryNormalList(BasePageParamVO<?> basePageParamVO) {
		List<CategoryNormal> list = null;
		if (basePageParamVO == null) {
			list = categoryNormalDao.getFirstCategoryNormalList();
		} else {
			list = categoryNormalDao.getFirstCategoryNormalList(basePageParamVO);
		}
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<CategoryNormalDTO>(0);
		} else {
			List<CategoryNormalDTO> retList = new ArrayList<CategoryNormalDTO>(list.size());
			for (CategoryNormal c : list) {
				retList.add(new CategoryNormalDTO(c));
			}
			return retList;
		}
	}

	@Override
	public List<CategoryNormalDTO> getSubCategoryNormalList(long superCategoryId) {
		List<CategoryNormal> list = categoryNormalDao.getSubCategoryNormalList(superCategoryId);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<CategoryNormalDTO>(0);
		} else {
			List<CategoryNormalDTO> retList = new ArrayList<CategoryNormalDTO>(list.size());
			for (CategoryNormal c : list) {
				retList.add(new CategoryNormalDTO(c));
			}
			return retList;
		}
	}

	@Override
	public CategoryNormalDTO getCategoryNormalById(long id, boolean isContainSub) {
		CategoryNormal categoryNormal = categoryNormalDao.getCategoryNormalById(id);
		if (categoryNormal == null || categoryNormal.getId() < 1l) {
			return null;
		}
		CategoryNormalDTO result = new CategoryNormalDTO(categoryNormal);
		if (isContainSub) {
			// 判断是否为第三级分类
			if (result.getLevel() == CategoryNormalLevel.LEVEL_THIRD.getIntValue()) {
				return result;
			}
			// 按id获取子分类
			List<CategoryNormalDTO> subList = getSubCategoryNormalList(id);
			if (CollectionUtils.isEmpty(subList)) {
				return result;
			}
			// 判断是否为第一级分类
			if (result.getLevel() == CategoryNormalLevel.LEVEL_FIRST.getIntValue()) {
				for (CategoryNormalDTO secondDTO : subList) {
					secondDTO.setSameParentList(getSubCategoryNormalList(secondDTO.getId()));
				}
			}
			result.setSameParentList(subList);
		}

		return result;
	}

	@Override
	public int createCategoryNormal(CategoryNormalDTO categoryNormalDTO) {
		CategoryNormal categoryNormal = new CategoryNormal(categoryNormalDTO);
		return categoryNormalDao.createCategoryNormal(categoryNormal);
	}

	@Override
	public CategoryNormalDTO getCategoryNormal(CategoryNormalDTO categoryNormalDTO) {
		CategoryNormal c = new CategoryNormal(categoryNormalDTO);
		c = categoryNormalDao.getCategoryNormal(c);
		categoryNormalDTO = new CategoryNormalDTO(c);
		return categoryNormalDTO;
	}

	@Override
	public int updateCategoryNormal(CategoryNormalDTO categoryNormalDTO) {
		CategoryNormal c = new CategoryNormal(categoryNormalDTO);
		return categoryNormalDao.updateCategoryNormal(c);
	}

	@Override
	public int getMaxShowIndex(CategoryNormalDTO categoryNormalDTO) {
		CategoryNormal c = new CategoryNormal(categoryNormalDTO);
		return categoryNormalDao.getMaxShowIndex(c);
	}

	@Override
	public int updateCategoryNormalSort(CategoryNormalDTO categoryNormalDTO, int isUp) {
		CategoryNormal c = new CategoryNormal(categoryNormalDTO);
		return categoryNormalDao.updateCategoryNormalSort(c, isUp);
	}

	@Override
	@Transaction
	public int deleteCategoryNormal(long id) {
		return categoryNormalDao.deleteCategoryNormal(id);
	}

	@Override
	public List<CategoryNormalDTO> getCategoryListByIds(List<Long> ids) {
		List<CategoryNormal> categoryNormals = categoryNormalDao.getCategoryListByIds(ids);
		List<CategoryNormalDTO> list = new ArrayList<CategoryNormalDTO>();
		if (!CollectionUtils.isEmpty(categoryNormals)) {
			for (CategoryNormal c : categoryNormals) {
				list.add(new CategoryNormalDTO(c));
			}
		}
		return list;
	}

	@Override
	public int getCategoryNormalCount(CategoryNormalDTO categoryNormalDTO) {
		CategoryNormal categoryNormal = new CategoryNormal(categoryNormalDTO);
		return categoryNormalDao.getCategoryNormalCount(categoryNormal);
	}

	@Override
	public String getFullCategoryNormalName(long categoryNormalId) {
		CategoryNormalDTO categoryNormalDTO = getCategoryNormalById(categoryNormalId, false);
		if (categoryNormalDTO == null) {
			return null;
		}
		StringBuffer fullName = new StringBuffer(categoryNormalDTO.getName());
		if (categoryNormalDTO.getLevel() == CategoryNormalLevel.LEVEL_FIRST.getIntValue()) {
			return fullName.toString();
		}
		categoryNormalDTO = getCategoryNormalById(categoryNormalDTO.getSuperCategoryId(), false);
		if (categoryNormalDTO == null) {
			return null;
		}
		fullName.insert(0, "＞").insert(0, categoryNormalDTO.getName());
		if (categoryNormalDTO.getLevel() == CategoryNormalLevel.LEVEL_FIRST.getIntValue()) {
			return fullName.toString();
		}
		categoryNormalDTO = getCategoryNormalById(categoryNormalDTO.getSuperCategoryId(), false);
		if (categoryNormalDTO == null) {
			return null;
		}
		fullName.insert(0, "＞").insert(0, categoryNormalDTO.getName());
		return fullName.toString();
	}

	@Override
	@Cacheable(value = "categoryNormalCache")
	public Map<Long, CategoryNormalDTO> getCategoryNormalMapFromCache() {
		Map<Long, CategoryNormalDTO> categoryMap = new HashMap<Long, CategoryNormalDTO>();
		List<CategoryNormal> categoryNormals = categoryNormalDao.getALLCategoryNormalList();
		if (CollectionUtil.isEmptyOfList(categoryNormals)) {
			return categoryMap;
		}
		List<CategoryNormalDTO> categoryNormalDTOs = new ArrayList<CategoryNormalDTO>();
		for (CategoryNormal categoryNormal : categoryNormals) {
			categoryNormalDTOs.add(new CategoryNormalDTO(categoryNormal));
		}
		// 初始化第二级和第三级类目全称
		// Collections.sort(categoryNormalDTOs);
		CategoryNormalDTO dto = null;
		for (CategoryNormalDTO categoryNormalDTO : categoryNormalDTOs) {
			switch (CategoryNormalLevel.getEnumByValue(categoryNormalDTO.getLevel())) {
			case LEVEL_FIRST:
				categoryMap.put(categoryNormalDTO.getId(), categoryNormalDTO);
				break;
			case LEVEL_SECOND:
				dto = categoryMap.get(categoryNormalDTO.getSuperCategoryId());
				categoryNormalDTO.setFullCategoryName(dto.getName() + "＞" + categoryNormalDTO.getName());
				categoryMap.put(categoryNormalDTO.getId(), categoryNormalDTO);
				break;
			case LEVEL_THIRD:
				dto = categoryMap.get(categoryNormalDTO.getSuperCategoryId());
				categoryNormalDTO.setFullCategoryName(dto.getFullCategoryName() + "＞" + categoryNormalDTO.getName());
				categoryMap.put(categoryNormalDTO.getId(), categoryNormalDTO);
			default:
				break;
			}
		}
		// 初始化第一级和第二级类目的子类目第三级的ids
		Collections.sort(categoryNormalDTOs, Collections.reverseOrder());
		for (CategoryNormalDTO category : categoryNormalDTOs) {
			CategoryNormalDTO parent = categoryMap.get(category.getSuperCategoryId());
			switch (CategoryNormalLevel.getEnumByValue(category.getLevel())) {
			case LEVEL_FIRST:
				break;
			case LEVEL_SECOND:
				if (StringUtils.isNotEmpty(category.getSubThirdIds())) {
					String subSecIds = StringUtils.isNotEmpty(parent.getSubThirdIds()) ? parent.getSubThirdIds() + ","
							+ category.getSubThirdIds() : category.getSubThirdIds() + "";
					parent.setSubThirdIds(subSecIds);
				}
				break;
			case LEVEL_THIRD:
				String subids = StringUtils.isNotEmpty(parent.getSubThirdIds()) ? parent.getSubThirdIds() + ","
						+ category.getId() : category.getId() + "";
				parent.setSubThirdIds(subids);
			default:
				break;

			}
		}
		return categoryMap;
	}

	@Override
	public List<CategoryNormal> getCategoryNormalByName(String categoryNormalName, int level, long superId) {
		return categoryNormalDao.getCategoryNormalByName(categoryNormalName, level, superId);
	}
}
