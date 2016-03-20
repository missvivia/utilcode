package com.xyl.mmall.oms.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.SendOutCountryFormDao;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;

@Repository("SendOutCountryFormDao")
public class SendOutCountryFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsSendOutCountryForm> implements SendOutCountryFormDao {
	
	@Override
	public List<OmsSendOutCountryForm> getData(long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" and date="+date);
		return this.queryObjects(sql);
	}

	@Override
	public List<OmsSendOutCountryForm> getDataByDay(long startDay, long endDay,DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" and date>="+startDay);
		sql.append(" and date<="+endDay);
		SqlGenUtil.appendDDBParam(sql, ddbParam, "");
		return this.queryObjects(sql);
	}

	@Override
	public OmsSendOutCountryForm getDataByExpressAndDay(String expressName, long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "ExpressName", expressName);
		SqlGenUtil.appendExtParamObject(sql, "date", date);
		return this.queryObject(sql);
	}
	
	
	
}


