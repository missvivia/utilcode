package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;

/**
 * @author zb
 *
 */
public interface OmsOrderPackageSkuDao extends AbstractDao<OmsOrderPackageSku> {

	public List<OmsOrderPackageSku> getListByPackageId(long packageId, long userId);
}
