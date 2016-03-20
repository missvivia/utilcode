package com.xyl.mmall.itemcenter.dao.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.meta.PoProductUserFav;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;

@Repository
public class PoProductUserFavDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PoProductUserFav> implements
		PoProductUserFavDao {

	@Override
	public boolean addPoProductIntoFavList(long userId, long poId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "poId", poId);
		List<PoProductUserFav> ret = queryObjects(sql);
		if (ret.size() == 0) {
			PoProductUserFav fav = new PoProductUserFav();
			fav.setPoId(poId);
			fav.setCreateDate(System.currentTimeMillis());
			// fav.setPoFavId(-1);
			fav.setUserId(userId);
			addObject(fav);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removePoProductFromFavList(long userId, long poId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "poId", poId);
		List<PoProductUserFav> ret = queryObjects(sql);
		if (ret.size() == 0) {
			return false;
		} else {
			for (PoProductUserFav fav : ret) {
				deleteObjectByKey(fav);
			}
			return true;
		}
	}

	@Override
	public int getPoProductFavCount(long poId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT COUNT(*) FROM Mmall_ItemCenter_PoProductUserFav ");
		sql.append(" WHERE poId = " + poId);
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				int size = rs.getInt("COUNT(*)");
				return size;
			}
		} catch (SQLException e) {
			return 0;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return 0;
	}

	@Override
	public boolean isPoProductInFavList(long userId, long poId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "poId", poId);
		List<PoProductUserFav> ret = queryObjects(sql);
		if (ret.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<PoProductUserFav> getPoProductFavListByUserIdOrPoIds(Long userId, List<Long> poIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		sql.append(" and poId in ");
		sql.append("(");
		for (Long id : poIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");
		return queryObjects(sql);
	}

	@Override
	public List<PoProductUserFav> getPageProductUserFavDTOByUserId(ProductUserFavParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", param.getUserId());
		return getListByDDBParam(sql.toString(), (DDBParam) param);
	}

	@Override
	public boolean removeProductFromFavListByProId(long productId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "poId", productId);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public boolean batchRemoveProductFromFavListByProIds(List<Long> productIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		sql.append(" poId in ");
		sql.append("(");
		for (Long id : productIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
}
