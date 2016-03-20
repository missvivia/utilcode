package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.RejectPackageState;
import com.xyl.mmall.oms.meta.RejectPackage;

/**
 * @author hzzengchengyuan
 *
 */
public interface RejectPackageDao extends AbstractDao<RejectPackage> {

	/**
	 * 更新拒收件状态
	 * 
	 * @param rejectPackageId
	 * @param newState
	 * @param oldState
	 * @return
	 */
	boolean updateState(long rejectPackageId, RejectPackageState newState, RejectPackageState oldState);

	/**
	 * 根据快递公司和快递单号获取拒收件
	 * 
	 * @param expressCompany
	 * @param expressNO
	 * @return
	 */
	RejectPackage getByExpressInfo(String expressCompany, String expressNO);
	
	/**
	 * 根据设为拒件时间范围查询
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<RejectPackage> queryByCreateTime(long startTime, long endTime);
}
