package com.xyl.mmall.order.dao;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.DeprecatedReturnCODBankCardInfo;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月30日 下午4:08:11
 *
 */
@Deprecated
@Repository(value = "deprecatedReturnCODBankCardInfoDao")
public class DeprecatedReturnCODBankCardInfoDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<DeprecatedReturnCODBankCardInfo>
	implements DeprecatedReturnCODBankCardInfoDao {

}
