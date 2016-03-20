package com.xyl.mmall.itemcenter.dao.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.intf.DaoInitialInterface;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;

@Repository
public class ProductParamOptDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductParamOption> implements
		ProductParamOptDao, DaoInitialInterface {
	public static Map<Long, List<ProductParamOption>> OPT_MAP = new HashMap<Long, List<ProductParamOption>>();

	@Override
	public void loadData() {
		List<ProductParamOption> list = getOptionList();
		for (ProductParamOption opt : list) {
			long prodParamId = opt.getProdParamId();
			List<ProductParamOption> optList = OPT_MAP.get(prodParamId);
			if (optList == null) {
				optList = new ArrayList<ProductParamOption>();
				optList.add(opt);
				OPT_MAP.put(prodParamId, optList);
			} else {
				optList.add(opt);
			}
		}
	}

	@Override
	public List<ProductParamOption> getOptionList(long prodParamId) {
		return OPT_MAP.get(prodParamId);
	}

	private List<ProductParamOption> getOptionList() {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		return queryObjects(sql.toString());
	}
}
