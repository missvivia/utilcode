package com.xyl.mmall.itemcenter.dao.product;

import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.PoProductDetail;

public interface PoProductDetailDao extends AbstractDao<PoProductDetail> {

	public PoProductDetail getPoProductDetail(long poId, long pid);

	public boolean deleteObject(long poId, long pid);

	public Map<String, Integer> getSameAsShop();

}
