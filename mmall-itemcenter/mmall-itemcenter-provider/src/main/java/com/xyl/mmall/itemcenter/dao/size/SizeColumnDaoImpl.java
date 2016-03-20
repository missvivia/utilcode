package com.xyl.mmall.itemcenter.dao.size;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.intf.DaoInitialInterface;
import com.xyl.mmall.itemcenter.meta.SizeColumn;

@Repository
public class SizeColumnDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SizeColumn> implements SizeColumnDao,
		DaoInitialInterface {
	public static Map<Long, SizeColumn> OPT_MAP = new HashMap<Long, SizeColumn>();

	@Override
	public void loadData() {
		List<SizeColumn> list = _getCategoryList();
		for (SizeColumn c : list) {
			long cid = c.getId();
			OPT_MAP.put(cid, c);
		}
	}

	@Override
	public SizeColumn getSizeColumn(long columnId) {
		return getObjectById(columnId);
	}

	@Override
	public SizeColumn addNewSizeColumn(SizeColumn sizeColumn) {
		return super.addObject(sizeColumn);
	}

	@Override
	public List<SizeColumn> getSizeColumnList(List<Long> columnIdList) {
		if (columnIdList != null && columnIdList.size() > 0) {
			StringBuffer sql = new StringBuffer("SELECT * FROM Mmall_ItemCenter_SizeColumn WHERE Id IN (");
			for (int i = 0; i < columnIdList.size(); i++) {
				long colId = columnIdList.get(i);
				if (i == columnIdList.size() - 1)
					sql.append(" " + colId);
				else
					sql.append(" " + colId + ",");
			}
			sql.append(" )");
			return this.queryObjects(sql.toString());
		}
		return null;
	}

	@Override
	public Map<Long, SizeColumn> getSizeColumnMap(List<Long> columnIdList) {
		Map<Long, SizeColumn> retMap = new HashMap<Long, SizeColumn>();
		if (columnIdList != null && columnIdList.size() > 0) {
			for (int i = 0; i < columnIdList.size(); i++) {
				long colId = columnIdList.get(i);
				retMap.put(colId, OPT_MAP.get(colId));
			}
		}
		return retMap;
	}

	private List<SizeColumn> _getCategoryList() {
		return queryObjects(genSelectSql());
	}

}
