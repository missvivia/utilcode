/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.member.dao.FilterChainResourceDao;
import com.xyl.mmall.member.dto.FilterChainResourceDTO;
import com.xyl.mmall.member.meta.FilterChainResource;
import com.xyl.mmall.member.service.FilterChainResourceService;

/**
 * @author lihui
 *
 */
public class FilterChainResourceServiceImpl implements FilterChainResourceService {

	@Autowired
	private FilterChainResourceDao filterChainResourceDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.FilterChainResourceService#findResourceByCategory(int)
	 */
	@Override
	public List<FilterChainResourceDTO> findResourceByCategory(int category) {
		List<FilterChainResource> resources = filterChainResourceDao.findByCategory(category);
		if (CollectionUtils.isEmpty(resources)) {
			return null;
		}
		List<FilterChainResourceDTO> resourcesDTOList = new ArrayList<>();
		for (FilterChainResource resource : resources) {
			resourcesDTOList.add(new FilterChainResourceDTO(resource));
		}
		return resourcesDTOList;
	}

}
