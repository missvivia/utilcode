package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;

/**
 * @author zb
 *
 */
public interface OmsReturnOrderFormSkuDao extends AbstractDao<OmsReturnOrderFormSku> {
	public List<OmsReturnOrderFormSku> getListByReturnId(long returnId, long userId);

	public boolean updateConfirmCount(long orderId, long orderSkuId, long confirmCount);

}
