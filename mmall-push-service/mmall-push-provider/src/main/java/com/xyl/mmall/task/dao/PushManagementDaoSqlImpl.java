package com.xyl.mmall.task.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.meta.PushManagement;

/**
 * 
 * @author jiangww
 *
 */
@Repository
public class PushManagementDaoSqlImpl extends
		PolicyObjectDaoSqlBaseOfAutowired<PushManagement> implements
		PushManagementDao {

	private String tableName = this.getTableName();

	private String sqlUpdateById = "UPDATE "
			+ tableName
			+ " SET title=?, content=?, platformType=?, link=?, areaCode=?, operator=?, updateTime=?, pushTime=?  WHERE id=? and pushSuccess=0";
	private String sqlUpdateSuccessById = "UPDATE " + tableName
			+ " SET pushSuccess=? , pushFailArea = ? WHERE id=?";
	private String sqlSlectForTask = "SELECT * FROM "
			+ tableName
			+ " WHERE pushSuccess = 0 AND pushTime >= ? AND pushTime < ?";
	
	@Override
	public List<PushManagement> getPushConfigList(
			PushManagementDTO pushManagementAo, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		List<Object> paramT = new ArrayList<Object>();
		sql.append(genSelectSql());
		
		if(pushManagementAo.getStartTime() > 0  ){
			sql.append(" AND pushTime > ?");
			paramT.add(pushManagementAo.getStartTime());
		}
		if(pushManagementAo.getEndTime() > 0  ){
			sql.append(" AND pushTime <= ?");
			paramT.add(pushManagementAo.getEndTime());
		}
		
		if(pushManagementAo.getPushSuccess() != -1 ){
			sql.append(" AND pushSuccess = ?");
			paramT.add(pushManagementAo.getPushSuccess());
		}
		if(StringUtils.isNotBlank(pushManagementAo.getAreaCode())){
			sql.append(" AND AreaCode like ?");
			paramT.add("%"+pushManagementAo.getAreaCode()+"%");
		}
		if(StringUtils.isNotBlank(pushManagementAo.getPlatformType())){
			sql.append(" AND PlatformType like ?");
			paramT.add("%"+pushManagementAo.getPlatformType()+"%");
		}
		if(StringUtils.isNotBlank(pushManagementAo.getContent())){
			sql.append(" AND content like ?");
			paramT.add("%"+pushManagementAo.getContent()+"%");
		}
		return getListByDDBParam(sql.toString(),param,paramT.toArray());
	}

	@Override
	public boolean updatePushManagement(long id,
			PushManagementDTO pushManagementDTO) {
		// TODO Auto-generated method stub
		return this.getSqlSupport().excuteUpdate(sqlUpdateById,
				pushManagementDTO.getTitle(), pushManagementDTO.getContent(),
				pushManagementDTO.getPlatformType(),
				pushManagementDTO.getLink(), pushManagementDTO.getAreaCode(),
				pushManagementDTO.getOperator(),
				pushManagementDTO.getUpdateTime(),
				pushManagementDTO.getPushTime(), id) > 0;
	}

	@Override
	public boolean updatePushSuccess(long id, int success,String error) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateSuccessById,  success,error,id) > 0;
	}

	@Override
	public List<PushManagement> getPushTaskList(long startTime, long endTime) {
		return queryObjects(sqlSlectForTask, startTime,endTime);
	}

}
