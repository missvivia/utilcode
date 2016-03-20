package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.OmsSkuState;
import com.xyl.mmall.oms.meta.OmsSku;

/**
 * @author zb
 *
 */
public interface OmsSkuDao extends AbstractDao<OmsSku> {
	public List<OmsSku> getOmsSkuListByState(OmsSkuState state, int limit, int offset);

	public boolean updateState(long skuId, OmsSkuState state);
}
