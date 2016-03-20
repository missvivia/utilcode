package com.xyl.mmall.saleschedule.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;


public interface ScheduleDaoYouhua extends AbstractDao<Schedule> {


	Schedule getScheduleByIdWithLimitedFields(long id);
		

	ScheduleDTO getScheduleByScheduleIdForCMSWithNoCache(long scheduleId);
	

	POListDTO getScheduleListForChl(String sqlPart, long saleSiteCode, long curDate);
	

	POListDTO getScheduleListForFuture(String sqlPart, long saleSiteCode, long startTime, long endTime);

	

	boolean updatePrdzlStatus(long id, int status);
	

	boolean updatePrdqdStatus(long id, int status);
}
