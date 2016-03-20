/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.AgentArea;

/**
 * AgentAreaDaoImpl.java created by yydx811 at 2015年7月23日 上午10:10:04
 * 管理员区域dao接口实现
 *
 * @author yydx811
 */
@Repository
public class AgentAreaDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<AgentArea> implements AgentAreaDao {
	
	private static Logger log = LoggerFactory.getLogger(AgentAreaDaoImpl.class);

	private String getAgentIdListSql = "select distinct AgentId from "+ getTableName() + " where 1=1 ";
	
	@Override
	public List<AgentArea> getAgentAreaList(long agentId, long siteId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "AgentId", agentId);
		if (siteId > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "SiteId", siteId);
		}
		return this.queryObjects(sql.toString());
	}

	@Override
	public int deleteAgentArea(long agentId) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "AgentId", agentId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public List<Long> getAgentIdListByAreaIds(List<Long> areaIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(getAgentIdListSql);
		SqlGenUtil.appendExtParamColl(sql, "AreaId", areaIds);
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr == null ? null : dbr.getResultSet();
		List<Long> agentIdList = new ArrayList<Long>();
		try {
			if (rs != null) {
				while (rs.next()) {
					agentIdList.add(rs.getLong("AgentId"));
				}
				rs.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (null != dbr) {
				dbr.close();
			}
		}
		return agentIdList;
	}
}
