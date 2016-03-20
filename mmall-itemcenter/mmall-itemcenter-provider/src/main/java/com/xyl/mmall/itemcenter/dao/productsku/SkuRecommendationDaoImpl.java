package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.SkuRecommendation;

/**
 * 
 * SkuRecommendationDaoImpl.java created by lhp at 2015年12月31日 上午11:09:18
 * 这里对类或者接口作简要描述
 *
 * @author lhp
 */
@Repository
public class SkuRecommendationDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SkuRecommendation> implements
		SkuRecommendationDao {

	@Override
	public List<SkuRecommendation> getSKuRecommendationListByBusinessId(long businessId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "BusinessId", businessId);
		this.appendOrderSql(sql, "ShowIndex", true);
		return queryObjects(sql.toString());
	}

}
