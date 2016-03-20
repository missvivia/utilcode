/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.SqlDealUtil;
import com.xyl.mmall.promotion.dao.PointDao;
import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.meta.Point;

/**
 * PointDaoImpl.java created by yydx811 at 2015年12月23日 下午12:29:51
 * 积分dao接口实现
 *
 * @author yydx811
 */
@Repository("pointDao")
public class PointDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Point> implements PointDao {

	@Override
	public List<Point> getBy(PointDTO point, Integer limit, Integer offset) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		appendEqualCondition(sql, point);
		if (StringUtils.isNotBlank(point.getSearchKey())) {
			String like = " '%" + SqlDealUtil.escapeLikeClauseChars(point.getSearchKey()) + "%'";
			sql.append(" AND (Name LIKE ").append(like).append(" OR Description LIKE ").append(like).append(")");
		}
		//审核请求
		if (point.getApply() != 1) {
			sql.append(" AND (AuditState=1 or AuditState=2)");
		}
		sql.append(" ORDER BY CreateTime DESC, Id DESC");
		
		if (limit != null) {
			sql.append(" Limit ").append(limit);
		}
		if (offset != null) {
			sql.append(" OFFSET ").append(offset);
		}

		return queryObjects(sql);	
	}

	private void appendEqualCondition(StringBuilder sql, PointDTO point) {
		if (point == null) return;
		if (point.getId() != null && point.getId() > 0) sql.append(" AND Id=" + point.getId());
		if (point.getApplyUserId() != null && point.getApplyUserId() > 0) sql.append(" AND ApplyUserId=" + point.getApplyUserId());
		if (point.getAuditState() != null && point.getAuditState() > 0) sql.append(" AND AuditState=" + point.getAuditState());
//		if (point.getAuditTime() != null && point.getAuditTime() > 0) sql.append(" AND AuditTime=" + point.getAuditTime());
		if (point.getAuditUserId() != null && point.getAuditUserId() > 0) sql.append(" AND AuditUserId=" + point.getAuditUserId());
		if (point.getSiteId() != null && point.getSiteId() > 0) sql.append(" AND SiteId=" + point.getSiteId());
		if (point.getPointDelta() != null && point.getPointDelta() != 0) sql.append(" AND PointDelta=" + point.getPointDelta());
	}

	@Override
	public boolean updatePartlyById(PointDTO updateObject) {
		if (updateObject == null || updateObject.getId() <= 0) {
			throw new IllegalArgumentException("ID不存在");
		}
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(this.getTableName());
		sql.append(" SET");
		boolean preComma = false;
		if (updateObject.getApplyUserId() != null && updateObject.getApplyUserId() > 0)  {
			sql.append(preComma ? " ," : "");
			sql.append(" ApplyUserId=" + updateObject.getApplyUserId());
			preComma = true;
		}
		if (updateObject.getAuditState() != null && updateObject.getAuditState() > 0) {
			sql.append(preComma ? " ," : "");
			sql.append(" AuditState=" + updateObject.getAuditState());
			preComma = true;			
		}
		if (updateObject.getAuditTime() != null && updateObject.getAuditTime() > 0) {
			sql.append(preComma ? " ," : "");
			sql.append(" AuditTime=" + updateObject.getAuditTime());
			preComma = true;
		}
		if (updateObject.getAuditUserId() != null && updateObject.getAuditUserId() > 0) {
			sql.append(preComma ? " ," : "");
			sql.append(" AuditUserId=" + updateObject.getAuditUserId());
			preComma = true;
		}
		if (updateObject.getDescription() != null) {
			sql.append(preComma ? " ," : "");
			sql.append(" Description='" + SqlDealUtil.escapeSingleQuoteChars(updateObject.getDescription() + "'"));
			preComma = true;
		}
		if (updateObject.getName() != null) {
			sql.append(preComma ? " ," : "");
			sql.append(" Name='" + SqlDealUtil.escapeSingleQuoteChars(updateObject.getName() + "'"));
			preComma = true;
		}
		if (updateObject.getReason() != null) {
			sql.append(preComma ? " ," : "");
			sql.append(" Reason='" + SqlDealUtil.escapeSingleQuoteChars(updateObject.getReason() + "'"));
			preComma = true;
		}
		if (updateObject.getPointDelta() != null && updateObject.getPointDelta() != 0) {
			sql.append(preComma ? " ," : "");
			sql.append(" PointDelta=" + updateObject.getPointDelta());
			preComma = true;
		}
		if (updateObject.getUsers() != null) {
			sql.append(preComma ? " ," : "");
			sql.append(" Users=" + updateObject.getUsers());
			preComma = true;
		}
		
		sql.append(" WHERE Id=");
		sql.append(updateObject.getId());
		if (updateObject.getSiteId() != null && updateObject.getSiteId() > 0) {
			sql.append(" AND SiteId=" + updateObject.getSiteId());
		}
		
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public boolean deleteById(Long[] ids) {
		StringBuilder sql = new StringBuilder();
		sql.append(this.genDeleteSql());
		SqlGenUtil.appendExtParamArray(sql, "Id", ids);
		return this.getSqlSupport().excuteUpdate(sql.toString()) == ids.length;
	}

}
