package com.xyl.mmall.itemcenter.dao.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.intf.DaoInitialInterface;
import com.xyl.mmall.itemcenter.meta.Category;

@Repository
public class CategoryDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Category> implements CategoryDao,
		DaoInitialInterface {
	public static Map<Long, Category> OPT_MAP = new HashMap<Long, Category>();

	@Override
	public void loadData() {
		List<Category> list = _getCategoryList();
		for (Category c : list) {
			long cid = c.getId();
			OPT_MAP.put(cid, c);
		}
	}

	@Override
	public List<Category> getCategoryListBySuperId(long superId) {
		List<Category> retList = new ArrayList<Category>();
		for (long cid : OPT_MAP.keySet()) {
			Category category = OPT_MAP.get(cid);
			if (category.getSuperCategoryId() == superId)
				retList.add(category);
		}
		return retList;
	}

	@Override
	public Category getCategoryById(long id) {
		Category category = OPT_MAP.get(id);
		return category;
	}

	@Override
	public List<Category> getLowestCategoryById(List<Category> retList, long id) {
		List<Category> list = getCategoryListBySuperId(id);
		if (list != null && list.size() > 0) {
			for (Category c : list) {
				long tmpId = c.getId();
				getLowestCategoryById(retList, tmpId);
			}
		} else {
			Category c = getCategoryById(id);
			if (c != null)
				retList.add(c);
		}
		return retList;
	}

	public void getCategoryListByLowestId(List<Category> retList, long lowerId) {
		Category c = getCategoryById(lowerId);
		retList.add(c);
		long superId = c.getSuperCategoryId();
		if (superId > 0) {
			getCategoryListByLowestId(retList, superId);
		}
	}

	@Override
	public Category getFirstCategoryByLowestId(long lowerId) {
		List<Category> retList = new ArrayList<Category>();
		getCategoryListByLowestId(retList, lowerId);
		return retList.get(retList.size() - 1);
	}

	@Override
	public List<Category> getCategoryList() {
		List<Category> retList = new ArrayList<Category>();
		for (long cid : OPT_MAP.keySet()) {
			Category category = OPT_MAP.get(cid);
			retList.add(category);
		}
		return retList;
	}

	private List<Category> _getCategoryList() {
		return queryObjects(genSelectSql());
	}

	@Override
	public List<Category> getCategoryListByLevel(int level) {
		List<Category> retList = new ArrayList<Category>();
		for (long cid : OPT_MAP.keySet()) {
			Category category = OPT_MAP.get(cid);
			if (category.getLevel() == level)
				retList.add(category);
		}
		return retList;
	}

	@Override
	public List<Category> getCategoryList(List<Long> ids) {
		List<Category> retList = new ArrayList<Category>();
		if (ids != null && ids.size() > 0) {
			for (long id : ids) {
				retList.add(OPT_MAP.get(id));
			}
		}
		return retList;
	}
}
