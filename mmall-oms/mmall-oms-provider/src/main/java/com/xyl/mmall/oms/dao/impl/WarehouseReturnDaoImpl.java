/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.PoOrderFormDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.meta.PoOrderForm;
import com.xyl.mmall.oms.meta.WarehouseReturn;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("WarehouseReturnDao")
public class WarehouseReturnDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<WarehouseReturn> implements
		WarehouseReturnDao {


	private String countPoIdFromReturnSku = "SELECT count(DISTINCT(poOrderId)) FROM Mmall_Oms_WarehouseReturn";

	@Autowired
	private PoOrderFormDao poOrderDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.WarehouseReturnDao#getWarehouseReturnByIds(java.util.List)
	 */
	public List<WarehouseReturn> getListByIds(List<Long> ids) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (CollectionUtil.isNotEmptyOfCollection(ids))
			SqlGenUtil.appendExtParamArray(sql, "id", ids.toArray(new Long[0]));
		return queryObjects(sql.toString());

	}

	private long count(String sql) {
		return this.getSqlSupport().queryCount(sql.toString());
	}

	private String genSelectSql(PoRetrunSkuQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		genWhereSql(sql, params);

		if (params.isPageable()) {
			sql.append(" ORDER BY createTime ");
			sql.append(" limit ").append(params.getOffset()).append(",").append(params.getLimit()).append(" ");
		}
		return sql.toString();
	}

	private String genCountSql(PoRetrunSkuQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		genWhereSql(sql, params);
		return sql.toString();
	}

	private void genWhereSql(StringBuilder sql, PoRetrunSkuQueryParamDTO params) {
		long[] poStartTimeRang = params.getPoStartTimeRang();
		// ddb不支持 AND poOrderId in (select poOrderId from Mmall_Oms_PoOrderForm
		// where 1=1 AND startTime >= ? AND startTime < ?)
		if (poStartTimeRang != null && poStartTimeRang.length == 2 && poStartTimeRang[0] > 0 && poStartTimeRang[1] > 0) {
			poStartTimeRang[0] = (poStartTimeRang[0] == 0) ? OmsConstants.LONG_BEFORE_LONG : poStartTimeRang[0];
			poStartTimeRang[1] = (poStartTimeRang[1] == 0) ? OmsConstants.LONG_AFTER_LONG : poStartTimeRang[1];
			List<PoOrderForm> poOrderForms = poOrderDao.getPoOrderList(null, 0, 0, poStartTimeRang[0],
					poStartTimeRang[1], 0, 0);
			if (poOrderForms != null && poOrderForms.size() > 0) {
				List<Long> tempPoOrderIds = new ArrayList<Long>();
				for (PoOrderForm poOrderForm : poOrderForms) {
					// 额... 要取交际，还需要判断po开始时间里的po列表是否和传进来的参数有冲突，如果有则取交集
					tempPoOrderIds.add(Long.parseLong(poOrderForm.getPoOrderId()));
				}
				if (params.getPoOrderIds() != null && params.getPoOrderIds().size() > 0) {
					tempPoOrderIds.retainAll(params.getPoOrderIds());
				}
				params.setPoOrderIds(tempPoOrderIds.toArray(new Long[] {}));
			} else {
				// 额... 没有符合条件的po，查询条件肯定为null，赋值一个绝对不成立的条件值
				params.setPoOrderIds(null);
				params.addPoOrderId(-1);
			}
		}
		Long[] poIds = params.getPoOrderIdArray();
		Long[] supplierIds = params.getSupplierIdArray();
		ReturnType[] types = params.getTypeArray();

		if (poIds != null && poIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "poOrderId", poIds);
		}
		if (supplierIds != null && supplierIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "supplierId", supplierIds);
		}
		if (types != null && types.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "type", types);
		}
		Long[] warehouseIds = params.getWarehouseIdArray();
		if (warehouseIds != null && warehouseIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "warehouseId", warehouseIds);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.WarehouseReturnDao#querReturn(com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO)
	 */
	@Override
	public PageableList<WarehouseReturn> querReturn(PoRetrunSkuQueryParamDTO params) {
		List<WarehouseReturn> data = queryObjects(genSelectSql(params));
		PageableList<WarehouseReturn> list = null;
		if (params.isPageable()) {
			long total = count(genCountSql(params));
			list = new PageableList<WarehouseReturn>(data, params.getLimit(), params.getOffset(), total);
		} else {
			list = new PageableList<WarehouseReturn>(data);
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.WarehouseReturnDao#countByParams(com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO)
	 */
	@Override
	public long countByParams(PoRetrunSkuQueryParamDTO params) {
		return count(genCountSql(params));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.WarehouseReturnDao#queryPoIdFromReturnSku(long,
	 *      long)
	 */
	@Override
	public PageableList<Long> queryPoIdFromReturnSku(long limit, long offset) {
		ResultSet rs = null;
		try {
			StringBuilder sb = new StringBuilder("SELECT DISTINCT(poOrderId) FROM Mmall_Oms_WarehouseReturn");
			this.appendLimitSql(sb, (int)limit, (int)offset);
			long total = count(countPoIdFromReturnSku);
			DBResource resource = this.getSqlSupport().excuteQuery(sb.toString());
			List<Long> poIds = new ArrayList<Long>();
			rs = resource.getResultSet();
			if (resource.getResultSet() != null) {
				while (rs.next()) {
					poIds.add(Long.parseLong(rs.getString(1)));
				}
			}
			return new PageableList<Long>(poIds, limit, offset, total);
		} catch (SQLException e) {
			return new PageableList<Long>();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
