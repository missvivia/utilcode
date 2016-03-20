package com.xyl.mmall.itemcenter.dao.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.intf.DaoInitialInterface;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CategoryOSizeMap;
import com.xyl.mmall.itemcenter.meta.ProductParameter;

@Repository
public class ProductParamDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductParameter> implements
		ProductParamDao, DaoInitialInterface {
	public static Map<Long, ProductParameter> OPT_MAP = new HashMap<Long, ProductParameter>();

	@Override
	public void loadData() {
		List<ProductParameter> list = _getAllData();
		for (ProductParameter paramter : list) {
			OPT_MAP.put(paramter.getId(), paramter);
		}
	}

	@Override
	public ProductParameter getObjectById(long id) {
		return OPT_MAP.get(id);
	}

	@Override
	public List<ProductParameter> getParamList(List<Long> ids) {
		List<ProductParameter> retList = new ArrayList<ProductParameter>();
		for (long id : ids) {
			ProductParameter parameter = OPT_MAP.get(id);
			retList.add(parameter);
		}
		return retList;
	}

	private List<ProductParameter> _getAllData() {
		return queryObjects(genSelectSql());
	}
}
