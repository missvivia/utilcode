package com.xyl.mmall.framework.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.netease.dbsupport.ISqlDAOSupport;
import com.netease.print.daojar.dao.UKeyPolicyObjectDaoSqlBaseOfPrint;

/**
 * @author dingmingliang
 * 
 * @param <T>
 */
public class UKeyPolicyObjectDaoSqlBaseOfAutowired<T> extends UKeyPolicyObjectDaoSqlBaseOfPrint<T> {

	@Autowired
	private ISqlDAOSupport sqlSupport;

	/**
	 * @param obj
	 */
	protected UKeyPolicyObjectDaoSqlBaseOfAutowired() {
		// 设置ObjectConfig(如果已经设置过,则不再重复设置)
		super.initObjectConfig();
	}
	
	@PostConstruct
	public void initSqlSupport() {
		this.setSqlSupport(sqlSupport);
	}
}
