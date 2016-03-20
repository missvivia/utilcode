package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsOrderPackageDao;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.meta.OmsOrderPackage;

/**
 * @author zb
 *
 */
@Repository("OmsOrderPackageDao")
public class OmsOrderPackageDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsOrderPackage> implements
		OmsOrderPackageDao {

	private String sqlSelectByOmsOrderFormId = "select * from Mmall_Oms_OmsOrderPackage where omsOrderFormId=? and userId=?";

	private String sqlSelectByMailNOAndExpressCompany = "select * from Mmall_Oms_OmsOrderPackage where mailno=? and expresscompany=?";

	private String sqlUpdateOmsOrderPackageState = "update Mmall_Oms_OmsOrderPackage set omsOrderPackageState=?,packageStateUpdateTime=? where packageId=? and omsOrderPackageState=?";
	
	private String sqlUpdateOmsOrderPackageState2 = "update Mmall_Oms_OmsOrderPackage set omsOrderPackageState=?,packageStateUpdateTime=? where mailno=? and expresscompany=? and omsOrderPackageState=?";

	private String sqlSelectUnFeedByUpdateTime = "select * from Mmall_Oms_OmsOrderPackage where packageStateUpdateTime<? and packageStateFeedBackToApp=0 and omsOrderPackageState!=0 order by packageStateUpdateTime desc";

	private String sqlSetPackageStateFeedBackToApp = "update Mmall_Oms_OmsOrderPackage set packageStateFeedBackToApp=1 where packageId=?";

	@Override
	public List<OmsOrderPackage> getListByOmsOrderFormId(long omsOrderFormId, long userId) {
		return this.queryObjects(sqlSelectByOmsOrderFormId, omsOrderFormId, userId);
	}

	@Override
	public OmsOrderPackage getByMailNOAndExpressCompany(String mailNO, String expressCompany) {
		return this.queryObject(sqlSelectByMailNOAndExpressCompany, mailNO, expressCompany);
	}

	@Override
	public boolean updatePackageState(long packageId, OmsOrderPackageState newOmsOrderPackageState,
			OmsOrderPackageState oldOmsOrderPackageState, long packageStateUpdateTime) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateOmsOrderPackageState, newOmsOrderPackageState.getIntValue(),
				packageStateUpdateTime, packageId, oldOmsOrderPackageState.getIntValue()) > 0;
	}
	
	@Override
	public boolean updatePackageState(String mailNO, String expressCompany,
			OmsOrderPackageState newOmsOrderPackageState, OmsOrderPackageState oldOmsOrderPackageState) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateOmsOrderPackageState2, newOmsOrderPackageState.getIntValue(),
				System.currentTimeMillis(), mailNO, expressCompany, oldOmsOrderPackageState.getIntValue()) > 0;
	}

	@Override
	public List<OmsOrderPackage> getUnFeedListByUpdateTime(long updateTime, int limit) {
		StringBuilder sb = new StringBuilder(sqlSelectUnFeedByUpdateTime);
		this.appendLimitSql(sb, limit, 0);
		return this.queryObjects(sb.toString(), updateTime);
	}

	@Override
	public boolean setPackageStateFeedBackToApp(long packageId) {
		return this.getSqlSupport().excuteUpdate(sqlSetPackageStateFeedBackToApp, packageId) > 0;
	}

}
