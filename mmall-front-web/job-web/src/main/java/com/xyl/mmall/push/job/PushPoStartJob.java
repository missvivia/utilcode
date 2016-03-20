package com.xyl.mmall.push.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.saleschedule.dto.UserFavListDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.util.DateUtil;

@JobPath("/push/poMessage")
@Service
public class PushPoStartJob extends BaseJob {

	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BusinessFacade businessFacade;

	private static final Logger logger = LoggerFactory.getLogger(PushPoStartJob.class);

	private Date GetDate(Date data,int h){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, h);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	@Override
	public boolean execute(JobParam param) {
		Date curDate = new Date();
		// 开始时间与结束时间
		Date startTime =GetDate(curDate, 0);
		Date endTime = GetDate(curDate, 24);
		getScheduleByArea(startTime, endTime);
		return true;
	}

	private void getScheduleByArea(Date startDate, Date endDate) {
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.isEmpty()) {
			logger.warn("no area found whne syncInventory");
			return;
		}
		for (AreaDTO areaDTO : areaList) {
			List<Schedule> tmpList = scheduleFacade.getScheduleListByTime(areaDTO.getId(), startDate.getTime(),
					endDate.getTime());
			if (tmpList == null || tmpList.size() == 0) {
				logger.info(areaDTO.getAreaName() + "has  null schedule");
				continue;
			}

			logger.info("time:" + startDate + "~" + endDate +" area:"+areaDTO.getAreaName()+ " has schedule num:" + tmpList.size());
			InerDate id = genIds(tmpList);
			List<UserFavListDTO> userDto = mobilePushManageFacade.getUserFavListByBrandIdList(id.brandIds,9999, 0);
			logger.info("user num is " + userDto.size());
			if (!Converter.isBeforeTen()) {
				logger.info("timeover!!");
				break;
			}
			for (UserFavListDTO dto : userDto) {
				sendPush(dto, id.brandMap, areaDTO.getId());
			}

		}
	}

	class InerDate {
		List<Long> brandIds;

		Map<Long, List<Schedule>> brandMap;
	}

	private InerDate genIds(List<Schedule> tmpList) {
		InerDate id = new InerDate();
		List<Long> brandIds = new ArrayList<Long>();
		Map<Long, List<Schedule>> ids = new HashMap<Long, List<Schedule>>();
		for (Schedule s : tmpList) {
			List<Schedule> a = ids.get(s.getBrandId());
			if (a == null) {
				brandIds.add(s.getBrandId());
				a = new ArrayList<Schedule>();
			}
			a.add(s);
			ids.put(s.getBrandId(), a);
		}
		id.brandIds = brandIds;
		id.brandMap = ids;
		return id;
	}

	private boolean sendPush(UserFavListDTO userlist, Map<Long, List<Schedule>> brandmap, long areaCode) {
		if (userlist.getFavIdList() == null)
			return false;
		int i = 0;
		boolean success = true;
		for (long favBrandId : userlist.getFavIdList()) {
			List<Schedule> schedles = brandmap.get(favBrandId);
			for (Schedule schedle : schedles) {
				if (i > 2)
					break;
				String name = StringUtils.isBlank(schedle.getBrandName()) ? schedle.getBrandNameEn() : schedle
						.getBrandName();
				String _name = schedle.getTitle();
				String message = "您关注的品牌'" + name + "'特卖'" + _name + "'就要开始了，准备抢购吧";
				mobilePushManageFacade.push(userlist.getUserId(), PushMessageType.tell_user_po, null, message,
						schedle.getId(), areaCode);
				i++;
			}
		}
		return success;
	}

}
