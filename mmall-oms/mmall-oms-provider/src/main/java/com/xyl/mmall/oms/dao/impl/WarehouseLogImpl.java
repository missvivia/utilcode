/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.WarehouseLogDao;
import com.xyl.mmall.oms.meta.WarehouseLog;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("WarehouseLogsDao")
public class WarehouseLogImpl extends PolicyObjectDaoSqlBaseOfAutowired<WarehouseLog> implements WarehouseLogDao {

}
