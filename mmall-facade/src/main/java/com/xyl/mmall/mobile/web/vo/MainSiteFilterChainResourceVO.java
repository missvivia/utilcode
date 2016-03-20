/**
 * 
 */
package com.xyl.mmall.mobile.web.vo;

import com.xyl.mmall.member.dto.FilterChainResourceDTO;

/**
 * @author lihui
 *
 */
public class MainSiteFilterChainResourceVO {

	private FilterChainResourceDTO filterChainResource;

	public MainSiteFilterChainResourceVO(FilterChainResourceDTO filterChainResource) {
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
