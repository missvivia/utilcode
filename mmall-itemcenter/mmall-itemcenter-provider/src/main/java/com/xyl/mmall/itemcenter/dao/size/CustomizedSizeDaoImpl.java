package com.xyl.mmall.itemcenter.dao.size;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.CustomizedSize;

@Repository
public class CustomizedSizeDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CustomizedSize> implements
		CustomizedSizeDao {


	@Override
	public List<CustomizedSize> getCustomizedSizeList(long productId, int IsInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", IsInPo);
		sql.append(" ORDER BY ColIndex ASC");
		return queryObjects(sql.toString());
	}
	
	@Override
	public List<Size> getSizeList(long templatekey, int IsInPo) {
		 List<CustomizedSize> list = getCustomizedSizeList(templatekey, IsInPo);
		 List<Size> retList = new ArrayList<Size>();
		 for(CustomizedSize cSize : list){
			 Size size = cSize;
			 retList.add(size);
		 }
		return retList;
	}
	
	@Override
	public boolean deleteCustomizedSize(long productId, int IsInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", IsInPo);
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}
}
