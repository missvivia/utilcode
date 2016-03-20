package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.RejectPackageState;
import com.xyl.mmall.oms.meta.RejectPackageSku;

/**
 * @author hzzengchengyuan
 *
 */
public interface RejectPackageSkuDao extends AbstractDao<RejectPackageSku> {

	/**
	 * 更新拒收件中所有商品状态
	 * 
	 * @param rejectPackageId
	 * @param newState
	 * @param oldState
	 * @return
	 */
	boolean updateStateByPackageId(long rejectPackageId, RejectPackageState newState, RejectPackageState oldState);

	/**
	 * 获取拒件单下所有的商品
	 * 
	 * @param rejectPackageId
	 * @return
	 */
	List<RejectPackageSku> getByPackageId(long rejectPackageId);
}
