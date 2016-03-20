/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.service;

import java.util.List;

import com.xyl.mmall.promotion.dto.PointRuleDTO;

/**
 * PointRuleService.java created by yydx811 at 2015年12月23日 下午3:54:29
 * 积分规则service
 *
 * @author yydx811
 */
public interface PointRuleService {

	/**
	 * 按id获取积分规则
	 * @param id
	 * @return
	 */
	public PointRuleDTO getPointRuleById(long id);
	
	/**
	 * 获取积分规则列表，按站点id缓存
	 * @param pointRuleDTO
	 * @return
	 */
	public List<PointRuleDTO> getPointRuleList(PointRuleDTO pointRuleDTO);
	
	/**
	 * 获取积分规则列表，不使用缓存
	 * @param pointRuleDTO
	 * @return
	 */
	public List<PointRuleDTO> getPointRuleListNoCache(PointRuleDTO pointRuleDTO);
}
