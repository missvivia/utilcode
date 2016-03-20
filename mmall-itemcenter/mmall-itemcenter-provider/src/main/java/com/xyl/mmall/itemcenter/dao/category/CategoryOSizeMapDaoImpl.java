package com.xyl.mmall.itemcenter.dao.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.intf.DaoInitialInterface;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CategoryOSizeMap;

@Repository
public class CategoryOSizeMapDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CategoryOSizeMap> implements
		CategoryOSizeMapDao, DaoInitialInterface {
	public static Map<Long, Long> OPT_MAP = new HashMap<Long, Long>();

	@Override
	public void loadData() {
		List<CategoryOSizeMap> list = _getAllData();
		for (CategoryOSizeMap map : list) {
			OPT_MAP.put(map.getCategoryId(), map.getOriginalSizeId());
		}
	}

	@Override
	public long getCategoryOSizeMap(long categoryId) {
		return OPT_MAP.get(categoryId);
	}

	private List<CategoryOSizeMap> _getAllData() {
		return queryObjects(genSelectSql());
	}
}
