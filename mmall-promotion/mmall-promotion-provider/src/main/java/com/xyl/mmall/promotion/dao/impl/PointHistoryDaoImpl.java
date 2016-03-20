/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.PointHistoryDao;
import com.xyl.mmall.promotion.meta.PointHistory;

/**
 * PointHistoryDaoImpl.java created by yydx811 at 2015年12月23日 下午1:06:21
 * 积分历史记录dao接口实现
 *
 * @author yydx811
 */
@Repository("pointHistoryDao")
public class PointHistoryDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PointHistory> implements PointHistoryDao {

}
