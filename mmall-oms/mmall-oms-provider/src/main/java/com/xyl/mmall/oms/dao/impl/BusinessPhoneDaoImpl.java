package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.BusinessPhoneDao;
import com.xyl.mmall.oms.meta.BusinessPhoneForm;

@Repository("BusinessPhoneDao")
public class BusinessPhoneDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<BusinessPhoneForm> implements BusinessPhoneDao {

	private String tableName = this.getTableName();
	
	private String sql_update_business_phone = "UPDATE " + tableName + " SET PHONE=? WHERE BUSINESSACCOUNT=? AND ORDERID=? ";
	
	@Override
	public List<BusinessPhoneForm> getBusinessPhoneByAccountId(String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		return queryObjects(sql);
	}

	@Override
	public BusinessPhoneForm addBusinessPhone(BusinessPhoneForm businessForm) {
		return this.addObject(businessForm);
	}

	@Override
	public boolean modifyBusinessPhone(BusinessPhoneForm businessForm) {
		return this.getSqlSupport().excuteUpdate(sql_update_business_phone,businessForm.getPhone(),businessForm.getBusinessAccount(),businessForm.getOrderId())>0;
	}

	@Override
	public BusinessPhoneForm getBusinessByAccountAndPhone(
			String businessAccount, String phone) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		SqlGenUtil.appendExtParamObject(sql, "phone", phone);
		return queryObject(sql);
	}

}
