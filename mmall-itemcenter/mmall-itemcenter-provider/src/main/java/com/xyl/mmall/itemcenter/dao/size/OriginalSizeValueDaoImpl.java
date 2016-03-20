package com.xyl.mmall.itemcenter.dao.size;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.UKeyPolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.OriginalSizeValue;
import com.xyl.mmall.itemcenter.meta.TemplateSizeValue;

@Repository
public class OriginalSizeValueDaoImpl extends UKeyPolicyObjectDaoSqlBaseOfAutowired<OriginalSizeValue> implements
		OriginalSizeValueDao {

	private List<OriginalSizeValue> getOrigialSizeValueIndexes(long id) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		sql.append(" GROUP BY RecordIndex ORDER BY RecordIndex ASC");
		return queryObjects(sql.toString());
	}

	@Override
	public List<Long> getSizeIdList(long id) {
		List<OriginalSizeValue> list = getOrigialSizeValueIndexes(id);
		List<Long> retList = new ArrayList<Long>();
		for (OriginalSizeValue oSizeValue : list) {
			SizeValue sizeValue = oSizeValue;
			retList.add(sizeValue.getRecordIndex());
		}
		return retList;
	}

	@Override
	public OriginalSizeValue getOriginalSizeValue(long id, long columnId, long sizeId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		SQLUtils.appendExtParamObject(sql, "ColumnId", columnId);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", sizeId);
		return queryObject(sql.toString());
	}

	@Override
	public SizeValue getSizeValue(long templatekey, long columnId, long sizeId) {
		OriginalSizeValue oSizeValue = getOriginalSizeValue(templatekey, columnId, sizeId);
		SizeValue sizeValue = oSizeValue;
		return sizeValue;
	}

	public List<OriginalSizeValue> getOriginalSizeValueList(long id, long sizeId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", sizeId);
		return queryObjects(sql.toString());
	}

	@Override
	public List<SizeValue> getSizeValueList(long templatekey, long sizeId) {
		List<OriginalSizeValue> list = getOriginalSizeValueList(templatekey, sizeId);
		List<SizeValue> retList = new ArrayList<SizeValue>();
		for (OriginalSizeValue tmpSizeValue : list) {
			SizeValue sizeValue = tmpSizeValue;
			retList.add(sizeValue);
		}
		return retList;
	}
}
