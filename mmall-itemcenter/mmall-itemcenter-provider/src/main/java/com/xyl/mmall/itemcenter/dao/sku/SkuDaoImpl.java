package com.xyl.mmall.itemcenter.dao.sku;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.Sku;

@Repository
public class SkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Sku> implements SkuDao {

	@Override
	public Sku addNewSku(Sku sku) {
		return super.addObject(sku);
	}

	@Override
	public List<Sku> getSkuList(long supplierId, long productId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "supplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		sql.append(" ORDER BY SizeIndex ASC");
		return queryObjects(sql.toString());
	}

	@Override
	public Sku getSkuByBarCode(long supplierId, String barCode) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "supplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "BarCode", barCode);
		return queryObject(sql.toString());
	}

	@Override
	public boolean deleteSkuList(long supplierId, long productId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "supplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public List<Sku> getExistSkuIds(long supplierId, List<String> barCodes) {
		List<Sku> retList = new ArrayList<Sku>();
		if (barCodes == null || barCodes.size() == 0)
			return retList;
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		sql.append(" AND BarCode IN (");
		for (int i = 0; i < barCodes.size(); i++) {
			String barCode = barCodes.get(i);
			if (i == barCodes.size() - 1) {
				sql.append("'" + barCode + "'");
			} else {
				sql.append("'" + barCode + "'").append(", ");
			}
		}
		sql.append(" ) ");
		return queryObjects(sql.toString());
	}

	@Override
	public Sku getSku(long supplierId, long pid, String barCode) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "supplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "ProductId", pid);
		SQLUtils.appendExtParamObject(sql, "BarCode", barCode);
		return queryObject(sql.toString());
	}

	@Override
	public int getMaxIndexOfSku(long pid) {
		String sql = "select max(SizeIndex) as max from Mmall_ItemCenter_Sku where ProductId = ?";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString(), pid);
		ResultSet rs = dbr.getResultSet();
		Integer max = null;
		try {
			while (rs.next()) {
				max = rs.getInt("max");
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		if (max == null)
			max = 0;
		return max;
	}
}
