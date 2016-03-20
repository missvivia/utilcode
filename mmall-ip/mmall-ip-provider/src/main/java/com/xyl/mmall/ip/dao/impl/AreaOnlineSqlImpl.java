package com.xyl.mmall.ip.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.ip.dao.AreaOnlineDao;
import com.xyl.mmall.ip.meta.AreaOnline;

@Repository
public class AreaOnlineSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<AreaOnline> implements AreaOnlineDao {
	
	private static final Logger logger=LoggerFactory.getLogger(AreaOnlineSqlImpl.class);

	@Override
	public AreaOnline getAreaOnlineById(long areaId) {
		logger.info("=====get from database,areaId:"+areaId);
		return getObjectById(areaId);
	}

	@Override
	public List<AreaOnline> getAreaOnlineByStatus(int status) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		if (status >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "Status", status);
		}
		return queryObjects(sql.toString());
	}

}
