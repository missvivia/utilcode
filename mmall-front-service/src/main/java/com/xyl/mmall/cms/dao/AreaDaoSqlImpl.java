package com.xyl.mmall.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.meta.Area;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * 
 * @author hzchaizhf
 * @create 2014年9月15日
 *
 */
@Repository
public class AreaDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Area> 
	implements AreaDao {

	private String tableName = this.getTableName();
	
	private String sql_get_areaList_by_idList = "SELECT * FROM " + tableName + " where id in ";
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.dao.AreaDao#getAreaList()
	 */
	@Override
	public List<Area> getAreaList() {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.dao.AreaDao#getAreaById(long)
	 */
	@Override
	public Area getAreaById(long id) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "id", id);
		return queryObject(sql);
	}

	@Override
	public List<Area> getAreadByIdList(List<Long> idList) {
		if(idList==null || idList.size()==0)
			return getAreaList();
		else{
			StringBuilder sb = new StringBuilder();
			for(Long id:idList){
				sb.append(id).append(",");
			}
			String sql = sql_get_areaList_by_idList + "(" + sb.toString().substring(0, sb.toString().length()-1) + ")";
			return queryObjects(sql);
		}
	}
}
