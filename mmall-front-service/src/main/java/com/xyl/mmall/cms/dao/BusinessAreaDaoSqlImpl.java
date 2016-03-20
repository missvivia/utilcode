package com.xyl.mmall.cms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.meta.BusinessArea;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * 
 * @author hzliujie
 * 2014年12月2日 下午3:57:51
 */
@Repository
public class BusinessAreaDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<BusinessArea> implements BusinessAreaDao {
	
	private String tableName = this.getTableName();
	
	private String sqlDelete = "DELETE FROM " + tableName + " WHERE supplierId=?";
	
	@Override
	public List<BusinessArea> getAreaList() {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		return queryObjects(sql);
	}

	@Override
	public List<BusinessArea> getAreaByAreaId(long areaId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "areaId", areaId);
		sql.append(" or type='0' ");
		return queryObjects(sql);
	}

	@Override
	public List<BusinessArea> getAreaBySupplierId(long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		return queryObjects(sql);
	}

	@Override
	public int getTotal(long areaId, String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		if(areaId!=0){
			SqlGenUtil.appendExtParamObject(sql, "areaId", areaId);
		}
		if(businessAccount!=null && !"".equals(businessAccount.trim())){
			if(isNumeric(businessAccount))
				SqlGenUtil.appendExtParamObject(sql, "supplierId", businessAccount);
			else
				SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		}
		return this.getSqlSupport().queryCount(sql.toString());
	}
	
		
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}

	@Override
	public List<BusinessArea> getBusinessListByProvinceOrAccount(long areaId,
			String businessAccount, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if(areaId!=0)
			SqlGenUtil.appendExtParamObject(sql, "areaId", areaId);
		if(businessAccount!=null && !"".equals(businessAccount.trim()))
			if(businessAccount!=null && !"".equals(businessAccount.trim())){
				if(isNumeric(businessAccount.trim()))
					SqlGenUtil.appendExtParamObject(sql, "supplierId", businessAccount.trim());
				else
					SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount.trim());
			}
		SqlGenUtil.appendDDBParam(sql, ddbParam, "");
		return queryObjects(sql);
	}

	@Override
	public boolean deleteBusiness(long supplierId) {
		return this.getSqlSupport().excuteUpdate(sqlDelete, supplierId) > 0;
	}

	@Override
	public BusinessArea getBusinessByAreaIdAndBrandId(Long areaId, Long brandId,
			int type) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "type", type);
		SqlGenUtil.appendExtParamObject(sql, "areaId", areaId);
		String subSql = " AND SiteId&"+areaId + "=" + areaId;
		return queryObject(sql+subSql);
	}

	@Override
	public List<BusinessArea> getBusinessAreaListByAreaIdList(
			List<Long> areaIdList) {
		if (areaIdList == null || areaIdList.size() == 0) {
			return new ArrayList<BusinessArea>();
		}
		StringBuilder idsString = new StringBuilder(256);
		idsString.append("(");
		for (Long areaId : areaIdList) {
			idsString.append(areaId).append(",");
		}
		idsString.deleteCharAt(idsString.lastIndexOf(","));
		idsString.append(") ");
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND areaId IN ").append(idsString);
		return queryObjects(sql);
	}

	@Override
	public List<BusinessArea> getBusinessByAreaIdAndBrandId(Long areaId,
			Long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "areaId", areaId);
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		return queryObjects(sql);
	}

	@Override
	public List<BusinessArea> getBusinessByBrandId(Long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		return queryObjects(sql);
	}

}
