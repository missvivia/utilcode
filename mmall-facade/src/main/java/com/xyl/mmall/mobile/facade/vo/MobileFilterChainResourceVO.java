/**
 * 
 */
package com.xyl.mmall.mobile.facade.vo;

import com.xyl.mmall.member.dto.FilterChainResourceDTO;

/**
 * @author lihui
 *
 */
public class MobileFilterChainResourceVO {

	private FilterChainResourceDTO filterChainResource;

	public MobileFilterChainResourceVO(FilterChainResourceDTO filterChainResource) {
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
