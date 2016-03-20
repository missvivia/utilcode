/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.productsku;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.ProdSpeci;

/**
 * ProdSpeciDaoImpl.java created by yydx811 at 2015年5月15日 下午2:33:16
 * 商品规格dao实现
 *
 * @author yydx811
 */
@Repository
public class ProdSpeciDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProdSpeci> implements ProdSpeciDao {

	private static Logger logger = Logger.getLogger(ProdSpeciDaoImpl.class);
	
	@Override
	public boolean deleteSpeciByProductId(long proId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", proId);
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public boolean addBulkProdSpeciList(List<ProdSpeci> speciList) {
		return addObjects(speciList);
	}

	@Override
	public List<ProdSpeci> getSpeciListBySKUId(long skuId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", skuId);
		return queryObjects(sql.toString());
	}

	@Override
	public boolean batchDeleteProdSpeci(List<Long> proIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql()).append(" 1=1 ");
		SqlGenUtil.appendExtParamColl(sql, "ProductSKUId", proIds );
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public List<ProdSpeci> getSpeciListBySKUIds(List<Long> skuIds) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "ProductSKUId", skuIds);
		return queryObjects(sql.toString());
	}

	@Override
	public int countProdSpeciOptionInUse(long speciId, long speciOptionId) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SQLUtils.appendExtParamObject(sql, "ModelSpeciId", speciId);
		SQLUtils.appendExtParamObject(sql, "ModelSpeciOptionId", speciOptionId);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public List<Long> getProductSKUIds(Map<Long, Set<Long>> speciMap, List<Long> skuIds) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ProductSKUId FROM ");
		sql.append(this.getTableName()).append(" WHERE (1 = 1");
		SqlGenUtil.appendExtParamColl(sql, "ProductSKUId", skuIds);
		sql.append(") AND (1 = 0");
		StringBuilder param = new StringBuilder();
		for (Map.Entry<Long, Set<Long>> entry : speciMap.entrySet()) {
			if (CollectionUtils.isEmpty(entry.getValue())) {
				continue;
			}
			param.append(" OR (ModelParamId = ").append(entry.getKey());
			SqlGenUtil.appendExtParamColl(param, "ModelParamOptionId", entry.getValue());
			param.append(")");
		}
		param.append(")");
		sql.append(param);
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if(null == rs) {
			return null;
		}
		try {
			List<Long> idList = new ArrayList<Long>();
			while (rs.next()) {
				long id = rs.getLong(1);
				idList.add(id);
			}
			rs.close();
			return idList;
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			dbResource.close();
		}
		return null;
	}
}
