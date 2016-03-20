package com.xyl.mmall.order.dao;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.ReturnCODBankCardInfo;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月30日 下午4:08:11
 *
 */
@Repository(value = "returnCODBankCardInfoDao")
public class ReturnCODBankCardInfoDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ReturnCODBankCardInfo>
	implements ReturnCODBankCardInfoDao {

}
