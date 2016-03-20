package com.xyl.mmall.itemcenter.dao.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.ProductDetail;

@Repository
public class ProductDetailDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductDetail> implements ProductDetailDao {
	@Override
	public ProductDetail getProductDetail(long supplierId, long pid) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "ProductId", pid);
		return queryObject(sql.toString());
	}

	@Override
	public ProductDetail saveObject(ProductDetail obj) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", obj.getSupplierId());
		SQLUtils.appendExtParamObject(sql, "ProductId", obj.getProductId());
		ProductDetail detail = queryObject(sql.toString());
		if (detail == null)
			return this.addObject(obj);
		else {
			obj.setId(detail.getId());
			return super.saveObject(obj);
		}
	}

	@Override
	public boolean deleteDetailByPid(long pid) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("DELETE FROM " + getTableName());
		sql.append(" WHERE ProductId = ").append(pid);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean updateCustomHtml(long supplierId, long pid, String html) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET CustomEditHTML = '").append(html);
		sql.append("' WHERE SupplierId = ").append(supplierId);
		sql.append(" AND ProductId = ").append(pid);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

}
