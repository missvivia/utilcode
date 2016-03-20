/**
 * 
 */
package com.xyl.mmall.backend.vo;

import com.xyl.mmall.member.dto.FilterChainResourceDTO;

/**
 * @author lihui
 *
 */
public class BackendFilterChainResourceVO {

	private FilterChainResourceDTO filterChainResource;

	public BackendFilterChainResourceVO(FilterChainResourceDTO filterChainResource) {
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
