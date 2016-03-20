package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.meta.OmsOrderPackage;

/**
 * @author zb
 *
 */
public interface OmsOrderPackageDao extends AbstractDao<OmsOrderPackage> {

	public List<OmsOrderPackage> getListByOmsOrderFormId(long omsOrderFormId, long userId);

	public OmsOrderPackage getByMailNOAndExpressCompany(String mailNO, String expressCompany);

	public boolean updatePackageState(long packageId, OmsOrderPackageState newOmsOrderPackageState,
			OmsOrderPackageState oldOmsOrderPackageState, long packageStateUpdateTime);

	public boolean updatePackageState(String mailNO, String expressCompany,
			OmsOrderPackageState newOmsOrderPackageState, OmsOrderPackageState oldOmsOrderPackageState);

	public List<OmsOrderPackage> getUnFeedListByUpdateTime(long updateTime, int limit);

	public boolean setPackageStateFeedBackToApp(long packageId);
}
