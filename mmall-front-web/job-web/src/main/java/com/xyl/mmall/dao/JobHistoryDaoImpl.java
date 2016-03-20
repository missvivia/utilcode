package com.xyl.mmall.dao;

import org.springframework.stereotype.Component;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.meta.JobHistory;

@Component
public class JobHistoryDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<JobHistory> implements JobHistoryDao {

	private static final String QUERY_BY_JOB_UNIQUEID = "select * from Mmall_Job_History t where t.jobUniqueId=?";

	private static final String UPDATE_STATUS_BY_JOBUNIQUEID = "update Mmall_Job_History t set t.processStatus=?,t.costTime=?,t.errorDesc=? where t.jobUniqueId=?";

	@Override
	public JobHistory queryByJobUniqueId(String jobUniqueId) {
		return super.queryObject(QUERY_BY_JOB_UNIQUEID, jobUniqueId);
	}

	@Override
	public boolean updateStatusByJobUniqueId(String jobUniqueId, int processStatusUpdate, long costTime,
			String errorDesc) {
		int num = super.getSqlSupport().excuteUpdate(UPDATE_STATUS_BY_JOBUNIQUEID, processStatusUpdate, costTime,
				errorDesc, jobUniqueId);
		return num > 0 ? true : false;
	}
}
