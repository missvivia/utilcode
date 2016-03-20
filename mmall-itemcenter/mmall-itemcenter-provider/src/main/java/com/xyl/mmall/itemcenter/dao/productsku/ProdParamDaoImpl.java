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
import com.xyl.mmall.itemcenter.meta.ProdParam;

/**
 * ProdParamDaoImpl.java created by yydx811 at 2015年5月15日 下午2:36:16
 * 商品属性dao实现
 *
 * @author yydx811
 */
@Repository
public class ProdParamDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProdParam> implements ProdParamDao {

	private static Logger logger = Logger.getLogger(ProdParamDaoImpl.class);
	
	@Override
	public boolean deleteParamByProductId(long proId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", proId);
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public boolean addBulkProdParams(List<ProdParam> paramList) {
		return addObjects(paramList);
	}

	@Override
	public List<ProdParam> getParamListBySKUId(long skuId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", skuId);
		return queryObjects(sql.toString());
	}

	@Override
	public boolean batchDeleteProdParam(List<Long> proIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql()).append(" 1=1 ");
		SqlGenUtil.appendExtParamColl(sql, "ProductSKUId", proIds );
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public ProdParam getParamByIndex(ProdParam prodParam) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", prodParam.getProductSKUId());
		SQLUtils.appendExtParamObject(sql, "ModelParamId", prodParam.getModelParamId());
		SQLUtils.appendExtParamObject(sql, "ModelParamOptionId", prodParam.getModelParamOptionId());
		return queryObject(sql.toString());
	}

	@Override
	public int deleteParamByIndex(ProdParam prodParam) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", prodParam.getProductSKUId());
		SQLUtils.appendExtParamObject(sql, "ModelParamId", prodParam.getModelParamId());
		SQLUtils.appendExtParamObject(sql, "ModelParamOptionId", prodParam.getModelParamOptionId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int countProdParamOptionInUse(long paramId, long paramOptionId) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SQLUtils.appendExtParamObject(sql, "ModelParamId", paramId);
		if (paramOptionId > 0) {
			SQLUtils.appendExtParamObject(sql, "ModelParamOptionId", paramOptionId);
		}
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public List<Long> getProductSKUIds(Map<Long, Set<Long>> paramMap) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ProductSKUId FROM ");
		sql.append(this.getTableName()).append(" WHERE 1 = 0");
		StringBuilder param = new StringBuilder();
		for (Map.Entry<Long, Set<Long>> entry : paramMap.entrySet()) {
			if (CollectionUtils.isEmpty(entry.getValue())) {
				continue;
			}
			param.append(" OR (ModelParamId = ").append(entry.getKey());
			SqlGenUtil.appendExtParamColl(param, "ModelParamOptionId", entry.getValue());
			param.append(")");
		}
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
