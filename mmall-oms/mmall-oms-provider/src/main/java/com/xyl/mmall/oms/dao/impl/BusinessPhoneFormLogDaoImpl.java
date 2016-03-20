package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.BusinessPhoneLogDao;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;

@Repository("BusinessPhoneLogDao")
public class BusinessPhoneFormLogDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<BusinessPhoneLogForm> implements BusinessPhoneLogDao {

	@Override
	public List<BusinessPhoneLogForm> getBusinessPhoneLogByAccountId(String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		return queryObjects(sql);
	}

	@Override
	public BusinessPhoneLogForm addBusinessPhoneLog(BusinessPhoneLogForm businessForm) {
		return this.addObject(businessForm);
	}

}
