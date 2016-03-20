/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.meta.PresentProduct;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author hzlihui2014
 *
 */
@Repository
public class PresentProductDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<PresentProduct> implements
		PresentProductDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.PresentProductDao#getPresentProductListByAreaId(long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<PresentProduct> getPresentProductListByAreaId(long saleAreaId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "saleAreaId", saleAreaId);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

}
