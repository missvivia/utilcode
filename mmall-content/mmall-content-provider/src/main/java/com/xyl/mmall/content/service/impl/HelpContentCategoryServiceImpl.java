/**
 * 
 */
package com.xyl.mmall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.dto.HelpContentCategoryDTO;
import com.xyl.mmall.content.meta.HelpContentCategory;
import com.xyl.mmall.content.service.HelpContentCategoryService;
import com.xyl.mmall.content.dao.HelpContentCategoryDao;

/**
 * @author lihui
 *
 */
public class HelpContentCategoryServiceImpl implements HelpContentCategoryService {

	@Autowired
	private HelpContentCategoryDao helpContentCategoryDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpContentCategoryService#getHelpContentCategoryList()
	 */
	@Override
	@Cacheable(value = "helpContentCategory")
	public List<HelpContentCategoryDTO> getHelpContentCategoryList() {
		DDBParam param = DDBParam.genParam500();
		param.setOrderColumn("parentId, orderBy");
		param.setAsc(true);
		List<HelpContentCategory> categoryList = helpContentCategoryDao.getListByDDBParam(param);
		if (CollectionUtils.isEmpty(categoryList)) {
			return null;
		}
		List<HelpContentCategoryDTO> categoryDTOList = new ArrayList<>();
		for (HelpContentCategory category : categoryList) {
			if (category.getId() == 0L) {
				continue;
			}
			categoryDTOList.add(new HelpContentCategoryDTO(category));
		}
		return categoryDTOList;
	}

}
