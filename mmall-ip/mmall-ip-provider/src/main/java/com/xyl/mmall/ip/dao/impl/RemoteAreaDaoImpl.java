package com.xyl.mmall.ip.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.ip.dao.RemoteAreaDao;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.enums.RetState;
import com.xyl.mmall.ip.meta.RemoteArea;

@Repository
public class RemoteAreaDaoImpl extends
	PolicyObjectDaoSqlBaseOfAutowired<RemoteArea> implements RemoteAreaDao {

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.ip.dao.RemoteAreaDao#isDistrictRemote(long, com.xyl.mmall.ip.enums.ExpressType)
	 */
	@Override
	public RetArg isDistrictRemote(long districtCode, ExpressType type) {
		RetArg retArg = new RetArg();
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		SqlGenUtil.appendExtParamObject(sql, "level", 3);
		SqlGenUtil.appendExtParamObject(sql, "code", districtCode);
		SqlGenUtil.appendExtParamObject(sql, "type", type.getIntValue());
		List<RemoteArea> list = queryObjects(sql);
		if (list.size() == 0) {
			RetArgUtil.put(retArg, 0.0);
			RetArgUtil.put(retArg, RetState.STATE_FALSE);
			return retArg;
		} else {
			RetArgUtil.put(retArg, list.get(0).getPrice());
			RetArgUtil.put(retArg, RetState.STATE_TRUE);
			return retArg;
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.ip.dao.RemoteAreaDao#isCityRemote(long, com.xyl.mmall.ip.enums.ExpressType)
	 */
	@Override
	public RetArg isCityRemote(long cityCode, ExpressType type) {
		RetArg retArg = new RetArg();
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		SqlGenUtil.appendExtParamObject(sql, "code", cityCode);
		SqlGenUtil.appendExtParamObject(sql, "type", type.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "level", 2);
		List<RemoteArea> list = queryObjects(sql);
		if (list.size() == 0) {
			StringBuilder sqlPar = new StringBuilder(256);
			sqlPar.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sqlPar, "valid", true);
			SqlGenUtil.appendExtParamObject(sqlPar, "parentCode", cityCode);
			SqlGenUtil.appendExtParamObject(sqlPar, "type", type.getIntValue());
			SqlGenUtil.appendExtParamObject(sqlPar, "level", 3);
			List<RemoteArea> listPar = queryObjects(sqlPar);
			if (listPar.size() == 0) {
				RetArgUtil.put(retArg, 0.0);
				RetArgUtil.put(retArg, RetState.STATE_FALSE);
				return retArg;
			} else {
				RetArgUtil.put(retArg, listPar.get(0).getPrice());
				RetArgUtil.put(retArg, RetState.STATE_NOTFULLY_TRUE);
				return retArg;
			}
		} else {
			RetArgUtil.put(retArg, list.get(0).getPrice());
			RetArgUtil.put(retArg, RetState.STATE_TRUE);
			return retArg;
		}
	}

}
