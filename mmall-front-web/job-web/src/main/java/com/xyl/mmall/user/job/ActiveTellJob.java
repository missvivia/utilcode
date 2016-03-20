package com.xyl.mmall.user.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.mainsite.facade.ActiveTellFacade;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.util.DateUtil;
import com.xyl.mmall.util.ResourceTextUtil;

@JobPath("/active/tell")
@Service
public class ActiveTellJob extends BaseJob {

	@Autowired
	private ActiveTellFacade activeTellFacade;

	private static final Logger logger = LoggerFactory
			.getLogger(ActiveTellJob.class);

	@Autowired
	private MessagePushFacade messagePushFacade;

	private static final ResourceBundle mailResourceBundle = ResourceTextUtil
			.getResourceBundleByName("content.mail");

	private static final ResourceBundle smsResourceBundle = ResourceTextUtil
			.getResourceBundleByName("content.sms");

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BusinessFacade businessFacade;
	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;
	
	@Override
	public boolean execute(JobParam param) {
		Date curDate = new Date();
		Date poTime = this.getBeginDateByParam(curDate);

		boolean f1 = this.processPo(curDate, poTime);
		boolean f2 = this.processBrand(curDate, poTime);
		return f1 && f2;
	}

	// 品牌活动通知处理
	private boolean processBrand(Date curDate, Date poTime) {
		// 品牌通知获取
		ActiveTellCommonParamDTO so = new ActiveTellCommonParamDTO();
		so.setTellActiveType(0);
		so.setActiveBeginTime(poTime.getTime());
		List<ActiveTell> activeTellList = activeTellFacade
				.getActiveTellByParam(so);
		if (activeTellList == null) {
			logger.info("no record to send for brand active tell");
			return true;
		}

		// 开始时间与结束时间
		Date startTime = DateUtil.getDateYMD(curDate, 0);
		Date endTime = DateUtil.getDateYMD(curDate, 1);

		// 获取今天要上线的全部档期
		List<Schedule> scheduleList = this
				.getScheduleByArea(startTime, endTime);
		if (scheduleList == null || scheduleList.isEmpty()) {
			return true;
		}

		Map<Long, List<Schedule>> poOnlineMapByBrandId = this
				.groupScheduleByBrandId(scheduleList);

		List<ActiveTell> tellSendList = new ArrayList<ActiveTell>();

		// 遍历通知列表，开始发送
		for (ActiveTell tell : activeTellList) {
			if (poOnlineMapByBrandId.get(tell.getTellActiveId()) != null) {
				tellSendList.add(tell);
			}
		}
		this.send(tellSendList, poTime);
		return true;
	}

	private List<Schedule> getScheduleByArea(Date startDate, Date endDate) {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.isEmpty()) {
			logger.warn("no area found whne syncInventory");
			return scheduleList;
		}
		for (AreaDTO areaDTO : areaList) {
			List<Schedule> tmpList = scheduleFacade.getScheduleListByTime(
					areaDTO.getId(), startDate.getTime(), endDate.getTime());
			if (tmpList != null && tmpList.size() > 0) {
				scheduleList.addAll(tmpList);
			}
		}
		return scheduleList;
	}

	private Map<Long, List<Schedule>> groupScheduleByBrandId(
			List<Schedule> scheduleList) {
		Map<Long, List<Schedule>> map = new HashMap<Long, List<Schedule>>();
		for (Schedule schedule : scheduleList) {
			List<Schedule> tempList = map.get(schedule.getBrandId());
			if (tempList == null) {
				tempList = new ArrayList<Schedule>();
			}
			tempList.add(schedule);
			map.put(schedule.getBrandId(), tempList);
		}
		return map;
	}

	// po活动通知处理
	private boolean processPo(Date curDate, Date poTime) {
		// 获取要发送短信的记录
		ActiveTellCommonParamDTO so = new ActiveTellCommonParamDTO();
		so.setTellActiveType(1);
		so.setActiveBeginTime(poTime.getTime());
		List<ActiveTell> activeTellList = activeTellFacade
				.getActiveTellByParam(so);
		if (activeTellList == null) {
			logger.info("no record to send for po active tell");
			return true;
		}
		this.send(activeTellList, poTime);
		return true;
	}

	private void send(List<ActiveTell> tellList, Date poTime) {
		List<ActiveTell> removeList = new ArrayList<ActiveTell>();
		String showTimeStr = DateUtil.parseDateToString(
				DateUtil.DATE_TIME_FORMAT, poTime);
		for (ActiveTell tell : tellList) {
			logger.info("try to send:" + tell.getTellTargetValue());
			if (tell.getTellTargetType() == 0) {
				// 手机发送
				this.sendSms(tell, showTimeStr);
			} else if (tell.getTellTargetType() == 1) {
				// 邮件
				this.sendEmail(tell, showTimeStr);
			}
			removeList.add(tell);
		}
		if (!this.removeRecord(removeList)) {
			logger.error("cannot remove the activerecord,time:" + poTime);
		}
	}

	private boolean sendSms(ActiveTell activeTell, String showTimeStr) {
		String smsContent;
		if (activeTell.getTellActiveType() == CodeInfoUtil.BRAND_TYPE_OF_ACTIVE_TELL) {
			smsContent = ResourceTextUtil.getTextFromResourceByKey(
					smsResourceBundle, "active.tell.brand.content",
					activeTell.getTellActiveTitle(), showTimeStr);
		} else {
			smsContent = ResourceTextUtil.getTextFromResourceByKey(
					smsResourceBundle, "active.tell.po.content",activeTell.getTellActiveTitle(), showTimeStr);
		}
		return messagePushFacade.sendSms(activeTell.getTellTargetValue(),
				smsContent);
	}
	
	private boolean sendPush(ActiveTell activeTell) {
		long userId = Long.parseLong(activeTell.getTellTargetValue());
		String message = "您关注的"+activeTell.getTellActiveTitle()+"特卖就要开始了，准备抢购吧";
		return false;
		//return mobilePushManageFacade.push(userId, PushMessageType.tell_user_po, "特卖即将开始", message, activeTell.getAreaId());
	}

	private boolean sendEmail(ActiveTell activeTell, String showTimeStr) {
		String title;
		String content;
		if (activeTell.getTellActiveType() == CodeInfoUtil.BRAND_TYPE_OF_ACTIVE_TELL) {
			title = ResourceTextUtil.getTextFromResourceByKey(
					mailResourceBundle, "active.tell.brand.title");
			content = ResourceTextUtil.getTextFromResourceByKey(
					mailResourceBundle, "active.tell.brand.content",
					activeTell.getTellActiveTitle(), showTimeStr);
		} else {
			title = ResourceTextUtil.getTextFromResourceByKey(
					mailResourceBundle, "active.tell.po.title");
			content = ResourceTextUtil.getTextFromResourceByKey(
					mailResourceBundle, "active.tell.po.content", showTimeStr);
		}
		return messagePushFacade.sendMail(MailType.SUBSCRIBE,
				activeTell.getTellTargetValue(), title, content);
	}

	private boolean removeRecord(List<ActiveTell> removeList) {
		if (removeList == null || removeList.isEmpty()) {
			return true;
		}
		return activeTellFacade.removeActiveTell(removeList);
	}

	// 获取品牌活动上期时间
	private Date getBeginDateByParam(Date curDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		calendar.set(Calendar.HOUR_OF_DAY, CodeInfoUtil.PO_BEGIN_HOUR);
		calendar.set(Calendar.MINUTE, CodeInfoUtil.PO_BEGIN_MINUTE);
		calendar.set(Calendar.SECOND, CodeInfoUtil.PO_BEGIN_SENCOND);
		calendar.set(Calendar.MILLISECOND, CodeInfoUtil.PO_BEGIN_MiSENCOND);
		return calendar.getTime();
	}

}
