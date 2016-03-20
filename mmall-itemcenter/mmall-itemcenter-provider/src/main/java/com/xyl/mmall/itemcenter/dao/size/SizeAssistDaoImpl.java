package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.SizeAssist;

@Repository
public class SizeAssistDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SizeAssist> implements SizeAssistDao {

	@Override
	public BaseSearchResult<SizeAssist> getSizeAssistList(long supplierId, int limit, int offset) {
		BaseSearchResult<SizeAssist> result = new BaseSearchResult<SizeAssist>();
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		DDBParam ddbParam = new DDBParam();
		ddbParam.setOrderColumn("Id");
		ddbParam.setAsc(false);
		ddbParam.setLimit(limit);
		ddbParam.setOffset(offset);
		List<SizeAssist> list = getListByDDBParam(sql.toString(), ddbParam);
		result.setList(list);
		result.setHasNext(ddbParam.isHasNext());
		result.setTotal(ddbParam.getTotalCount());
		return result;
	}

}
