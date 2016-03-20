package com.xyl.mmall.oms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.ExpressFeeDao;
import com.xyl.mmall.oms.dto.ExpressFeeSearchParam;
import com.xyl.mmall.oms.meta.ExpressFee;

@Repository("expressFeeDao")
public class ExpressFeeDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ExpressFee> implements ExpressFeeDao{
	
	private String tableName=super.getTableName();
	
	private String SELECT_SQL="select * from "+tableName+" where 1=1";
	
	private static final Logger logger=LoggerFactory.getLogger(ExpressFeeDaoImpl.class);

	@Override
	@Cacheable(value="expressFeeCache")
	public List<ExpressFee> searchExpressFeeByParam(ExpressFeeSearchParam param) {
		logger.info("=====get from database");
		StringBuilder buffer=new StringBuilder(64);
		buffer.append(SELECT_SQL);
		List<Object> paramList=new ArrayList<Object>();
		if(!StringUtils.isEmpty(param.getExpressCompanyCode())){
			buffer.append(" and expressCompanyCode=?");
			paramList.add(param.getExpressCompanyCode());
		}
		
		if(!StringUtils.isEmpty(param.getExpressCompanyName())){
			buffer.append(" and expressCompanyName=?");
			paramList.add(param.getExpressCompanyName());
		}
		
		if(param.getSiteId()!=null){
			buffer.append(" and siteId=?");
			paramList.add(param.getSiteId());
		}
		
		if(!StringUtils.isEmpty(param.getSiteName())){
			buffer.append(" and siteName=?");
			paramList.add(param.getSiteName());
		}
		
		if(param.getTargetProvinceId()!=null){
			buffer.append(" and targetProvinceId=?");
			paramList.add(param.getTargetProvinceId());
		}
		
		if(!StringUtils.isEmpty(param.getTargetProvinceName())){
			buffer.append(" and targetProvinceName=?");
			paramList.add(param.getTargetProvinceName());
		}
		
		if(param.getCodService()!=null){
			buffer.append(" and codService=?");
			paramList.add(param.getCodService());
		}
		return super.queryObjects(buffer.toString(), paramList);
	}

}
