package com.xyl.mmall.order.dao.tcc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.TCCUtil;
import com.xyl.mmall.order.meta.tcc.OrderExpInfoTCC;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class OrderExpInfoTCCDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderExpInfoTCC> implements
		OrderExpInfoTCCDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.framework.interfaces.TCCDaoInterface#getListByTranId(long)
	 */
	public List<OrderExpInfoTCC> getListByTranId(long tranId) {
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

}
