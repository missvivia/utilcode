/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.ProdPic;

/**
 * ProdPicDaoImpl.java created by yydx811 at 2015年5月15日 下午2:38:51
 * 商品图片dao实现
 *
 * @author yydx811
 */
@Repository
public class ProdPicDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProdPic> implements ProdPicDao {

	@Override
	public boolean deleteProdPicByProductId(long proId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", proId);
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public boolean addBulkProdPics(List<ProdPic> picList) {
		return addObjects(picList);
	}

	@Override
	public List<ProdPic> getPicListBySKUId(long skuId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", skuId);
		this.appendOrderSql(sql, "Id", true);
		return queryObjects(sql.toString());
	}

	@Override
	public boolean batchDeleteProdPic(List<Long> proIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql()).append(" 1=1 ");
		SqlGenUtil.appendExtParamColl(sql, "ProductSKUId", proIds );
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public List<ProdPic> getPicListBySKUIds(List<Long> skuIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "ProductSKUId", skuIds );
		sql.append(" order by productSKUId,CreateTime" );
		return queryObjects(sql);
	}

	@Override
	public int deleteProdPic(long productSKUId, long id) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		SQLUtils.appendExtParamObject(sql, "ProductSKUId", productSKUId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

}
