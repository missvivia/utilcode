/**
 * 
 */
package com.xyl.mmall.cms.vo;

import com.xyl.mmall.member.dto.FilterChainResourceDTO;

/**
 * @author lihui
 *
 */
public class CmsFilterChainResourceVO {

	private FilterChainResourceDTO filterChainResource;

	public CmsFilterChainResourceVO(FilterChainResourceDTO filterChainResource) {
		this.filterChainResource = filterChainResource;
	}

	/**
	 * @return the filterChainResource
	 */
	public FilterChainResourceDTO getFilterChainResource() {
		return filterChainResource;
	}

	/**
	 * @param filterChainResource
	 *            the filterChainResource to set
	 */
	public void setFilterChainResource(FilterChainResourceDTO filterChainResource) {
		this.filterChainResource = filterChainResource;
	}
}
