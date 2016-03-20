/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.order.dto.OrderReplenishStoreDTO;
import com.xyl.mmall.order.meta.OrderReplenish;

/**
 * OrderReplenishDAOImpl.java created by yydx811 at 2015年6月6日 下午6:34:52
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Repository
public class OrderReplenishDAOImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderReplenish> implements
		OrderReplenishDAO {

	@Override
	public BasePageParamVO<OrderReplenishStoreDTO> getReplenishGroupByBusinessId(
			BasePageParamVO<OrderReplenishStoreDTO> basePageParamVO, OrderReplenish replenish) {
		StringBuilder sql = new StringBuilder("SELECT BusinessId FROM ");
		sql.append(this.getTableName()).append(" WHERE ").append("UserId = ").append(replenish.getUserId());
		sql.append(" GROUP BY BusinessId");
		this.appendOrderSql(sql, "UpdateTime", false);
		if (basePageParamVO.getIsPage() == 1) {
			int count = getReplenishCountGroupByBusinessId(replenish);
			if (count == 0) {
				return null;
			}
			basePageParamVO.setTotal(count);
			this.appendLimitSql(sql, basePageParamVO.getPageSize(), basePageParamVO.getStartRownum() - 1);
		}
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if(null == rs) {
			return null;
		}
		try {
			List<OrderReplenishStoreDTO> retList = new ArrayList<OrderReplenishStoreDTO>();
			while (rs.next()) {
				OrderReplenishStoreDTO replenishStoreDTO = new OrderReplenishStoreDTO();
				replenishStoreDTO.setBusinessId(rs.getLong(1));
				retList.add(replenishStoreDTO);
			}
			rs.close();
			basePageParamVO.setList(retList);
			return basePageParamVO;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbResource.close();
		}
		return null;
	}

	@Override
	public List<OrderReplenish> getReplenishList(OrderReplenish replenish) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", replenish.getUserId());
		if (replenish.getBusinessId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "BusinessId", replenish.getBusinessId());
		}
		return queryObjects(sql.toString());
	}
	
	public int getReplenishCountGroupByBusinessId(OrderReplenish replenish) {
		StringBuilder sql = new StringBuilder("SELECT COUNT(BusinessId) FROM ");
		sql.append(this.getTableName()).append(" WHERE ").append("UserId = ").append(replenish.getUserId());
		sql.append(" GROUP BY BusinessId");
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if(null == rs) {
			return 0;
		}
		try {
			int i = 0;
			while (rs.next()) {
				++i;
			}
			rs.close();
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbResource.close();
		}
		return 0;
	}
}
