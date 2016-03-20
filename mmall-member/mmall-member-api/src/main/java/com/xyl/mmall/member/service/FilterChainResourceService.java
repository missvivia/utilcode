/**
 * 
 */
package com.xyl.mmall.member.service;

import java.util.List;

import com.xyl.mmall.member.dto.FilterChainResourceDTO;

/**
 * 安全控制相关的Filter Chain的服务。
 * 
 * @author lihui
 *
 */
public interface FilterChainResourceService {

	/**
	 * 根据类别获取不同项目的Filter Chain的定义。
	 * 
	 * @param category
	 *            项目的类别
	 * @return Filter Chain的定义列表
	 */
	List<FilterChainResourceDTO> findResourceByCategory(int category);
}
