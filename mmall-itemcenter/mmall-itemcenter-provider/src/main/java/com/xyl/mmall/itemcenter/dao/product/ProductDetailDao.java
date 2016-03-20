package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProductDetail;

public interface ProductDetailDao extends AbstractDao<ProductDetail> {

	public ProductDetail getProductDetail(long supplierId, long pid);

	public boolean updateCustomHtml(long supplierId, long pid, String html);

	public boolean deleteDetailByPid(long pid);

}
