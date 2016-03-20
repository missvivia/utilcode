package com.xyl.mmall.itemcenter.dao.size;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.UKeyPolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.TemplateSize;

@Repository
public class TemplateSizeDaoImpl extends UKeyPolicyObjectDaoSqlBaseOfAutowired<TemplateSize> implements TemplateSizeDao {

	@Override
	public TemplateSize addNewTemplateSize(TemplateSize tSize) {
		return super.addObject(tSize);
	}

	@Override
	public boolean deleteTemplateSizeByTemplId(long sizeTemplateId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("DELETE FROM ");
		sql.append(getTableName());
		sql.append(" WHERE 1=1");
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", sizeTemplateId);
		return super.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}


	@Override
	public List<TemplateSize> getTemplateSizeList(long sizeTemplateId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SizeTemplateId", sizeTemplateId);
		return queryObjects(sql.toString());
	}
	
	@Override
	public List<Size> getSizeList(long templatekey) {
		 List<TemplateSize> list = getTemplateSizeList(templatekey);
		 List<Size> retList = new ArrayList<Size>();
		 for(TemplateSize cSize : list){
			 Size size = cSize;
			 retList.add(size);
		 }
		return retList;
	}
}
