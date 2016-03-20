package com.xyl.mmall.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.dbsupport.DBResource;
import com.netease.dbsupport.ISqlDAOSupport;
import com.netease.print.daojar.dao.PolicyObjectDaoSqlBaseOfPrint;
import com.xyl.mmall.framework.annotation.Transaction;

/**
 * @author dingmingliang
 * 
 * @param <T>
 */
public class PolicyObjectDaoSqlBaseOfAutowired<T> extends PolicyObjectDaoSqlBaseOfPrint<T> {

	private static Logger logger = Logger.getLogger(PolicyObjectDaoSqlBaseOfAutowired.class);
	
	@Autowired
	private ISqlDAOSupport sqlSupport;
	
	/**
	 * @param obj
	 */
	protected PolicyObjectDaoSqlBaseOfAutowired() {
		// 设置ObjectConfig(如果已经设置过,则不再重复设置)
		super.initObjectConfig();
	}

	@PostConstruct
	public void initSqlSupport() {
		this.setSqlSupport(sqlSupport);
	}	
	
	/**
	 * 获取id
	 * @param type 1订单，2红包/优惠券，3其他
	 * @return
	 */
	@Transaction
	public long getGenerateId(int type) {
		long id = 0l;
		StringBuilder sql = new StringBuilder("SELECT Id FROM Mmall_Generate_Id WHERE Type = ? FOR UPDATE");
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString(), type);
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if(null == rs) {
			return id;
		}
		try {
			while (rs.next()) {
				id = rs.getLong(1);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			dbResource.close();
		}
		++id;
		sql = new StringBuilder("UPDATE Mmall_Generate_Id SET Id = ? WHERE Type = ?");
		if (this.getSqlSupport().excuteUpdate(sql.toString(), id, type) > 0) {
			return id;
		}
		return 0l;
	}
	
}