package com.xyl.mmall.order.dao;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.CODWhitelistAddress;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:58
 *
 */
@Repository(value = "CODWhitelistAddressDao")
public class CODWhitelistAddressDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CODWhitelistAddress> 
	implements CODWhitelistAddressDao {

}
