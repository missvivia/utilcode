package com.xyl.mmall.order.dao.tcc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.TCCUtil;
import com.xyl.mmall.order.meta.tcc.OrderTCCLock;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class OrderTCCLockDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderTCCLock> implements OrderTCCLockDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.framework.interfaces.TCCDaoInterface#getListByTranId(long)
	 */
	public List<OrderTCCLock> getListByTranId(long tranId) {
		return TCCUtil.getListByTranId(tranId, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.framework.interfaces.TCCDaoInterface#deleteByTranId(long)
	 */
	public boolean deleteByTranId(long tranId) {
		return TCCUtil.deleteByTranId(tranId, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.tcc.OrderTCCLockDao#getObjectByOrderId(long)
	 */
	public OrderTCCLock getObjectByOrderId(long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObject(sql);
	}
}
