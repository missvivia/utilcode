/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.meta.SiteArea;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * SiteAreaDaoImpl.java created by yydx811 at 2015年7月16日 下午5:18:23 
 * 站点区域dao接口实现
 * 
 * @author yydx811
 */
@Repository
public class SiteAreaDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SiteArea> implements SiteAreaDao {

	private String sql_get_sitecms_by_idList = "SELECT * FROM " + getTableName() + " where SiteId in ";

	@Override
	public List<SiteArea> getSiteAreaList(long siteId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "SiteId", siteId);
		return this.queryObjects(sql.toString());
	}

	@Override
	public int getSiteAreaCount(long areaId, long siteId) {
		StringBuilder sql = new StringBuilder(genCountSql());
		if (siteId > 0) {
			SqlGenUtil.appendExtParamObject(sql, "SiteId", siteId);
		}
		if (areaId != 0) {
			SqlGenUtil.appendExtParamObject(sql, "AreaId", areaId);
		}
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public int deleteBulkSiteArea(long siteId, List<Long> areaIds) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "SiteId", siteId);
		if (CollectionUtils.isNotEmpty(areaIds)) {
			sql.append(" AND AreaId IN (");
			for (Long areaId : areaIds) {
				sql.append(areaId).append(",");
			}
			sql.deleteCharAt(sql.length() - 1).append(")");
		}
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteBulkSiteArea(List<Long> siteIds) {
		if (CollectionUtils.isEmpty(siteIds)) {
			return 0;
		} else {
			StringBuilder sql = new StringBuilder(genDeleteSql());
			sql.append(" SiteId IN (");
			for (Long areaId : siteIds) {
				sql.append(areaId).append(",");
			}
			sql.deleteCharAt(sql.length() - 1).append(")");
			return this.getSqlSupport().excuteUpdate(sql.toString());
		}
	}

	@Override
	public List<SiteArea> getSiteAreasList(List<Long> siteIds) {
		if (siteIds == null || siteIds.size() == 0) {
			return queryObjects(genSelectSql());
		} else {
			StringBuilder sb = new StringBuilder();
			for (Long id : siteIds) {
				sb.append(id).append(",");
			}
			String sql = sql_get_sitecms_by_idList + "(" + sb.deleteCharAt(sb.length() - 1).toString() + ")";
			return queryObjects(sql);
		}
	}
}