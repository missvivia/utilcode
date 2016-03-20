package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;

@Repository("OmsOrderFormSkuDao")
public class OmsOrderFormSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsOrderFormSku> implements
		OmsOrderFormSkuDao {

	private String sqlQueryCount = "select sum(totalcount) from Mmall_Oms_OmsOrderFormSku,Mmall_Oms_OmsOrderForm where Mmall_Oms_OmsOrderFormSku.userid=Mmall_Oms_OmsOrderForm.userid and Mmall_Oms_OmsOrderFormSku.PoId=? and"
			+ " Mmall_Oms_OmsOrderFormSku.omsOrderFormId=Mmall_Oms_OmsOrderForm.omsOrderFormId and (Mmall_Oms_OmsOrderForm.OmsOrderFormState=19 or Mmall_Oms_OmsOrderForm.OmsOrderFormState=21);";

	private String sqlQueryByOmsOrderFormId = "select * from Mmall_Oms_OmsOrderFormSku where omsOrderFormId=? and userId=?";

	private String sqlQueryCountByPoId = "Select sum(totalcount) from Mmall_Oms_OmsOrderFormSku where poId=?";
	
	private String getByOmsOrderFormIdAndSkuId = "select * from Mmall_Oms_OmsOrderFormSku where omsOrderFormId=? and skuId=?";
	
	@Override
	public long getCancelSkuCountInPoId(long poId) {
		return this.getSqlSupport().queryCount(sqlQueryCount, poId);
	}

	@Override
	public List<OmsOrderFormSku> queryByOmsOrderFormId(long omsOrderFormId, long userId) {
		return this.queryObjects(sqlQueryByOmsOrderFormId, omsOrderFormId, userId);
	}

	@Override
	public OmsOrderFormSku getBySupplierIdAndSkuId(long poId, long skuId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", poId);
		SqlGenUtil.appendExtParamObject(sql, "skuId", skuId);
		return queryObject(sql.toString());
	}

	@Override
	public int getTotalSoldByPoId(long poId) {
		return this.getSqlSupport().queryCount(sqlQueryCountByPoId, poId);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.OmsOrderFormSkuDao#getByOmsOrderFormIdAndSkuId(long, long)
	 */
	@Override
	public OmsOrderFormSku getByOmsOrderFormIdAndSkuId(long omsOrderFormId, long skuId) {
		return queryObject(getByOmsOrderFormIdAndSkuId,omsOrderFormId,skuId);
	}

}
