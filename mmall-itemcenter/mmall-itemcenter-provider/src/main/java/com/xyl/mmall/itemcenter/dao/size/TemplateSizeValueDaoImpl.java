package com.xyl.mmall.itemcenter.dao.size;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.UKeyPolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CustomizedSizeValue;
import com.xyl.mmall.itemcenter.meta.OriginalSizeValue;
import com.xyl.mmall.itemcenter.meta.TemplateSizeValue;

@Repository
public class TemplateSizeValueDaoImpl extends UKeyPolicyObjectDaoSqlBaseOfAutowired<TemplateSizeValue> implements
		TemplateSizeValueDao {

	@Override
	public TemplateSizeValue addNewTemplateSizeValue(TemplateSizeValue tmplSV) {
		return super.addObject(tmplSV);
	}

	@Override
	public boolean deleteTemplateSizeValue(long sizeTemplateId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", sizeTemplateId);
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}
	
	@Override
	public TemplateSizeValue getTemplateSizeValue(long sizeTemplateId, long columnId, long recordIndex) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", sizeTemplateId);
		SQLUtils.appendExtParamObject(sql, "ColumnId", columnId);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", recordIndex);
		return queryObject(sql.toString());
	}

	@Override
	public SizeValue getSizeValue(long templatekey, long columnId, long sizeId) {
		TemplateSizeValue oSizeValue = getTemplateSizeValue(templatekey, columnId, sizeId);
		SizeValue sizeValue = oSizeValue;
		return sizeValue;
	}
	
	@Override
	public List<TemplateSizeValue> getTemplateSizeValueList(long sizeTemplateId, long sizeId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", sizeTemplateId);
		SQLUtils.appendExtParamObject(sql, "RecordIndex", sizeId);
		return queryObjects(sql.toString());
	}

	private List<TemplateSizeValue> getTemplateSizeValueIndexes(long sizeTempateId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", sizeTempateId);
		sql.append(" GROUP BY RecordIndex ORDER BY RecordIndex ASC");
		return queryObjects(sql.toString());
	}

	@Override
	public List<Long> getSizeIdList(long id) {
		List<TemplateSizeValue> list = getTemplateSizeValueIndexes(id);
		List<Long> retList = new ArrayList<Long>();
		for (TemplateSizeValue tmpSizeValue : list) {
			SizeValue sizeValue = tmpSizeValue;
			retList.add(sizeValue.getRecordIndex());
		}
		return retList;
	}
	
	@Override
	public List<SizeValue> getSizeValueList(long templatekey,long sizeId){
		List<TemplateSizeValue> list = getTemplateSizeValueList(templatekey,sizeId);
		List<SizeValue> retList = new ArrayList<SizeValue>();
		for (TemplateSizeValue tmpSizeValue : list) {
			SizeValue sizeValue = tmpSizeValue;
			retList.add(sizeValue);
		}
		return retList;
	}
	
	@Override
	public List<SizeValue> getSizeValueList(long templatekey){
		List<TemplateSizeValue> list = getTemplateSizeValueList(templatekey);
		List<SizeValue> retList = new ArrayList<SizeValue>();
		for (TemplateSizeValue tmpSizeValue : list) {
			SizeValue sizeValue = tmpSizeValue;
			retList.add(sizeValue);
		}
		return retList;
	}
	
	@Override
	public List<TemplateSizeValue> getTemplateSizeValueList(long templateId){
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", templateId);
		return queryObjects(sql.toString());
	}
}
