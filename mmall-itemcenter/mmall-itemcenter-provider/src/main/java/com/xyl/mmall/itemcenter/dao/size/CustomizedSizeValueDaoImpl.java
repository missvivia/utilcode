package com.xyl.mmall.itemcenter.dao.size;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.CustomizedSizeValue;

@Repository
public class CustomizedSizeValueDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CustomizedSizeValue> implements
		CustomizedSizeValueDao {

	@Override
	public List<CustomizedSizeValue> getCustomizedSizeValueList(long productId, long sizeId, int isInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", sizeId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", isInPo);
		return queryObjects(sql.toString());
	}

	private List<CustomizedSizeValue> getCustomizedSizeValueList(long productId, int isInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", isInPo);
		return queryObjects(sql.toString());
	}

	@Override
	public boolean deleteCustomizedSizeValue(long productId, int isInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", isInPo);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean deleteCustomizedSizeValue(long productId, long sizeId, int isInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", sizeId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", isInPo);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public List<SizeValue> getSizeValueList(long templatekey, long sizeId, int isInPo) {
		List<CustomizedSizeValue> list = getCustomizedSizeValueList(templatekey, sizeId, isInPo);
		List<SizeValue> retList = new ArrayList<SizeValue>();
		for (CustomizedSizeValue cSizeValue : list) {
			SizeValue sizeValue = cSizeValue;
			retList.add(sizeValue);
		}
		return retList;
	}

	@Override
	public List<SizeValue> getSizeValueList(long templatekey, int isInPo) {
		List<CustomizedSizeValue> list = getCustomizedSizeValueList(templatekey, isInPo);
		List<SizeValue> retList = new ArrayList<SizeValue>();
		for (CustomizedSizeValue cSizeValue : list) {
			SizeValue sizeValue = cSizeValue;
			retList.add(sizeValue);
		}
		return retList;
	}

	@Override
	public CustomizedSizeValue getCustomizedSizeValue(long productId, long columnId, long skuId, int isInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "productId", productId);
		SQLUtils.appendExtParamObject(sql, "ColumnId", columnId);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", skuId);
		SQLUtils.appendExtParamObject(sql, "isInPo", isInPo);
		return queryObject(sql.toString());
	}

	@Override
	public SizeValue getSizeValue(long templatekey, long columnId, long sizeId, int isInPo) {
		CustomizedSizeValue oSizeValue = getCustomizedSizeValue(templatekey, columnId, sizeId, isInPo);
		SizeValue sizeValue = oSizeValue;
		return sizeValue;
	}

	// TODO 数据迁移脚本
	@Override
	public Map<Long, String> getSize(long productId, int isInPo) {
		Map<Long, String> retMap = new HashMap<Long, String>();
		String sql = "select RecordIndex Value from Mmall_ItemCenter_CustomizedSizeValue where ProductId = " + productId
				+ " AND ColumnId = 2 AND IsInPo = " + isInPo;
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				retMap.put(rs.getLong("RecordIndex"), rs.getString("Value"));
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return retMap;
	}
}
