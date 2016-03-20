package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormSkuDao;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;

@Repository("OmsReturnOrderFormSkuDao")
public class OmsReturnOrderFormSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsReturnOrderFormSku> implements
		OmsReturnOrderFormSkuDao {

	private String sqlQueryByReturnId = "select * from Mmall_Oms_OmsReturnOrderFormSku where returnId=? and userid=?";

	private String sqlUpdateConfirmCount = "update Mmall_Oms_OmsReturnOrderFormSku set confirmCount=? where orderid=? and orderSkuId=?";
	
	@Override
	public List<OmsReturnOrderFormSku> getListByReturnId(long orderId, long userId) {
		return this.queryObjects(sqlQueryByReturnId, orderId, userId);
	}

	@Override
	public boolean updateConfirmCount(long orderId, long orderSkuId, long confirmCount) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateConfirmCount, confirmCount, orderId, orderSkuId) > 0;
	}

}
