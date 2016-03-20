/**
 * 
 */
package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.FilterChainResource;

/**
 * @author lihui
 *
 */
public class FilterChainResourceDTO extends FilterChainResource {

	private static final long serialVersionUID = 1L;

	public FilterChainResourceDTO() {
	}

	public FilterChainResourceDTO(FilterChainResource obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
