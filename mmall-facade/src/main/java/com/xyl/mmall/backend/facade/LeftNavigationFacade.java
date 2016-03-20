/**
 * 
 */
package com.xyl.mmall.backend.facade;

import java.util.List;

import com.xyl.mmall.backend.vo.LeftNavItemVO;

/**
 * @author lihui
 *
 */
public interface LeftNavigationFacade {

	/**
	 * 根据商家后台用户ID获取左边导航的数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 左边导航目录列表
	 */
	List<LeftNavItemVO> getLeftNavigationMenu(long userId);

}
