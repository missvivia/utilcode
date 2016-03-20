package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsOrderPackageSkuDao;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;

@Repository("OmsOrderPackageSkuDao")
public class OmsOrderPackageSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsOrderPackageSku> implements
		OmsOrderPackageSkuDao {
	private String sqlSelectByPackageId = "select * from Mmall_Oms_OmsOrderPackageSku where packageId=? and userId=?";

	@Override
	public List<OmsOrderPackageSku> getListByPackageId(long packageId, long userId) {
		return this.queryObjects(sqlSelectByPackageId, packageId, userId);
	}

}
