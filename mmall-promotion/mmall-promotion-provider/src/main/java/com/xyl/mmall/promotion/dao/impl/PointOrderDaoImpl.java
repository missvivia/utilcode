/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.PointOrderDao;
import com.xyl.mmall.promotion.meta.PointOrder;

/**
 * PointOrderDaoImpl.java created by yydx811 at 2015年12月23日 下午1:20:53
 * 订单积分dao接口实现
 *
 * @author yydx811
 */
@Repository("pointOrderDao")
public class PointOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PointOrder> implements PointOrderDao {

}
