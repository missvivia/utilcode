package com.xyl.mmall.cms.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.meta.BusiUserRelation;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

@Repository
public class BusiUserRelationDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<BusiUserRelation> implements
		BusiUserRelationDao {

	private String tableName = this.getTableName();

	private String sqlDeleteByBusinesssID = "DELETE FROM " + tableName + " WHERE BusinessId=?";

	@Override
	public List<BusiUserRelation> getBusiUserRelationsByBusinessId(long businessId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "BusinessId", businessId);
		return queryObjects(sql);
	}
	
	
	@Override
	public List<BusiUserRelation> getBusiUserRelationsByUserId(long userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		return queryObjects(sql);
	}
	

	@Override
	public boolean deleteByBusinessId(long businessId) {
		return this.getSqlSupport().excuteUpdate(sqlDeleteByBusinesssID, businessId) > 0;
	}

	@Override
	public BusiUserRelation getBusiUserRelation(BusiUserRelation relation) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		if (relation.getBusinessId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "BusinessId", relation.getBusinessId());
		}
		if (relation.getUserId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "UserId", relation.getUserId());
		}
		if (relation.getId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "Id", relation.getId());
		}
		return queryObject(sql.toString());
	}

	@Override
	public boolean batchDeleteByBusinessIds(List<Long> businessIds) {
		if (CollectionUtil.isEmptyOfList(businessIds)) {
			return false;
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append("DELETE FROM Mmall_CMS_BusiUserRelation WHERE BusinessId in ");
		sql.append("(");
		for (Long id : businessIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");

		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
	
	@Override
	public List<BusiUserRelation> getPageBusiUserRelationBybusinessId(long businessId,String userName,
			DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
        sql.append(genSelectSql());
        SqlGenUtil.appendExtParamObject(sql, "BusinessId", businessId);
        if(StringUtils.isNotEmpty(userName)){
       	  sql.append( " AND UserName LIKE '%").append(userName.replaceAll("'", "\\\\'")).append( "%'");
       }
        return getListByDDBParam(sql .toString(), ddbParam);
	}
}
