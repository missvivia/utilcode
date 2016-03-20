/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.PointRule;

/**
 * PointRuleDao.java created by yydx811 at 2015年12月23日 下午1:00:35
 * 积分规则dao接口
 *
 * @author yydx811
 */
public interface PointRuleDao extends AbstractDao<PointRule> {

	/**
	 * 获取积分规则列表 MinPoint升序
	 * @param pointRule
	 * @return
	 */
	public List<PointRule> getRuleList(PointRule pointRule);
}
