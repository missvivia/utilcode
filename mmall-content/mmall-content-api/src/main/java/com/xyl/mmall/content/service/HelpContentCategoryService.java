/**
 * 
 */
package com.xyl.mmall.content.service;

import java.util.List;

import com.xyl.mmall.content.dto.HelpContentCategoryDTO;

/**
 * 帮助中心类目相关服务。
 * 
 * @author lihui
 *
 */
public interface HelpContentCategoryService {

	/**
	 * 获取帮助中心左边导航条的类目数据。
	 * 
	 * @return 类目列表
	 */
	List<HelpContentCategoryDTO> getHelpContentCategoryList();
}
