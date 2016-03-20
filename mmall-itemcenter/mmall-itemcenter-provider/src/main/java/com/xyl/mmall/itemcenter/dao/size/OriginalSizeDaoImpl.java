package com.xyl.mmall.itemcenter.dao.size;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.UKeyPolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.OriginalSize;
import com.xyl.mmall.itemcenter.meta.TemplateSize;

@Repository
public class OriginalSizeDaoImpl extends UKeyPolicyObjectDaoSqlBaseOfAutowired<OriginalSize> implements OriginalSizeDao {

	@Override
	public List<OriginalSize> getOriginalSizeList(long id) {
		StringBuffer sqlBuffer = new StringBuffer(genSelectSql());
		sqlBuffer.append(" AND Id = ? ORDER BY ColIndex");
		return queryObjects(sqlBuffer.toString(), id);
	}
	
	@Override
	public List<Size> getSizeList(long templatekey) {
		 List<OriginalSize> list = getOriginalSizeList(templatekey);
		 List<Size> retList = new ArrayList<Size>();
		 for(OriginalSize cSize : list){
			 Size size = cSize;
			 retList.add(size);
		 }
		return retList;
	}

}
