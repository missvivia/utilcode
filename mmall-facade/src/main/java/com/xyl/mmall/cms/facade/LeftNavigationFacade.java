/**
 * 
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.LeftNavItemVO;

/**
 * 运维平台左边导航条Facade
 * 
 * @author lihui
 *
 */
public interface LeftNavigationFacade {

	/**
	 * 根据运维平台用户ID获取左边导航的数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 左边导航目录列表
	 */
	List<LeftNavItemVO> getLeftNavigationMenu(long userId);

}
