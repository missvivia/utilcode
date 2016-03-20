/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.wap.facade;

import java.util.List;

import com.xyl.mmall.wap.vo.WapFilterChainResourceVO;

/**
 * WapAuthcFacade.java created by yydx811 at 2015年10月22日 上午11:28:10
 * wap认证相关facade
 *
 * @author yydx811
 */
public interface WapAuthcFacade {

	/**
	 * 获取wap登录拦截地址
	 * @return
	 */
	public List<WapFilterChainResourceVO> getWapFilterChainResource();
}
