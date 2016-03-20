package com.xyl.mmall.itemcenter.dao.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoProductDetail;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.ProductDetail;

@Repository
public class PoProductDetailDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PoProductDetail> implements
		PoProductDetailDao {
	@Override
	public PoProductDetail getPoProductDetail(long poId, long pid) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "ProductId", pid);
		return queryObject(sql.toString());
	}

	@Override
	public boolean deleteObject(long poId, long pid) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "ProductId", pid);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	// TODO 数据迁移脚本
	@Override
	public Map<String, Integer> getSameAsShop() {
		Map<String, Integer> retMap = new HashMap<String, Integer>();
		String sql = "select ProductId,PoId, SameAsShop From Mmall_ItemCenter_PoProductDetail";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				long pid = rs.getInt("ProductId");
				long poId = rs.getInt("PoId");
				int same = rs.getInt("SameAsShop");
				retMap.put(pid + "_" + poId, same);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return retMap;
	}
}
