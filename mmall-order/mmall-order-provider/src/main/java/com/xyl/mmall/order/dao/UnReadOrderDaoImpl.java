package com.xyl.mmall.order.dao;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.UnReadOrderList;

/**
 * @author hzjiangww
 * 
 */
@Repository
public class UnReadOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<UnReadOrderList> implements UnReadOrderDao {
	private String sqlSelect = "SELECT * FROM " + this.getTableName() + " WHERE userId = ? and type = ? ";
	private String sqlUpdate = "UPDATE " + this.getTableName() + " set lastReadTime = ? WHERE id = ? ";
	@Override
	public long getLastReadTIme(long userId, int type) {
		UnReadOrderList unRead = queryObject(sqlSelect, userId,type);
		if(unRead == null)
			return 0;
		return unRead.getLastReadTime() ;
	}

	@Override
	public boolean updateTime(long userId, int type, long time) {
		UnReadOrderList unRead = queryObject(sqlSelect, userId,type);
		if(unRead == null){
			unRead = new UnReadOrderList();
			unRead.setUserId(userId);
			unRead.setLastReadTime(time);
			unRead.setType(type);
			return this.addObject(unRead) != null;
		}else{
			return this.getSqlSupport().updateRecord(sqlUpdate, time,unRead.getId());
		}
		
	}


	

}
