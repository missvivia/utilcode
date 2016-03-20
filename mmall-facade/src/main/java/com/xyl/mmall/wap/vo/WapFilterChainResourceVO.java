/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.wap.vo;

import com.xyl.mmall.member.dto.FilterChainResourceDTO;

/**
 * WapFilterChainResourceVO.java created by yydx811 at 2015年10月22日 上午11:31:39
 * wap登录拦截器
 *
 * @author yydx811
 */
public class WapFilterChainResourceVO {

	private FilterChainResourceDTO filterChainResourceDTO;
	
	public WapFilterChainResourceVO(FilterChainResourceDTO filterChainResourceDTO) {
		this.filterChainResourceDTO = filterChainResourceDTO;
	}

	public FilterChainResourceDTO getFilterChainResourceDTO() {
		return filterChainResourceDTO;
	}

	public void setFilterChainResourceDTO(FilterChainResourceDTO filterChainResourceDTO) {
		this.filterChainResourceDTO = filterChainResourceDTO;
	}
}
