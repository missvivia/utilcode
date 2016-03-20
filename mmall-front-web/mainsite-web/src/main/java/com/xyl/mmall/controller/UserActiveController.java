package com.xyl.mmall.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.mainsite.facade.ActiveTellFacade;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ActiveTellDTO;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;
import com.xyl.mmall.util.AreaUtils;

/**
 * 用户活动通知controller
 * 
 * @author hzzhaozhenzuo
 *
 */
@Controller
@RequestMapping("/user/active")
public class UserActiveController {

	@Autowired
	private ActiveTellFacade activeTellFacade;
	
	@Autowired
	private BrandFacade brandFacade;
	
	@Autowired
	private ScheduleFacade scheduleFacade;

	/**
	 * 用户活动通知
	 * 
	 * @param activeType
	 * @param activeId
	 *            对于品牌通知传品牌id
	 * @param type
	 * @param value
	 * @param timeStr
	 *            用户选择活动通知的哪一天，如【2014-10-19】
	 * @return
	 */
	@RequestMapping(value = "/tellme", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject tellme(@RequestParam("activeType") int activeType, @RequestParam("activeId") long activeId,
			@RequestParam("type") int type, @RequestParam("value") String value, String timeStr) {
		// long userId=SecurityContextUtils.getUserId();
		int areaId = AreaUtils.getProvinceCode();
		JSONObject jsonObject = new JSONObject();
		Date curDate = new Date();

		long beginTimeOfPo = 0;
		if (activeType == CodeInfoUtil.PO_TYPE_OF_ACTIVE_TELL) {
			// 档期通知
			Date dateUserSelected = DateUtils.parseStringToDate(DateUtils.DATE_FORMAT, timeStr);
			beginTimeOfPo = this.getBeginDateByParam(dateUserSelected).getTime();
		}

		// check whether has recorded
		boolean recordedFlag = this.hasRecord(activeType, activeId, type, value, areaId, beginTimeOfPo);
		if (recordedFlag) {
			jsonObject.put("code", 200);
			return jsonObject;
		}

		// create activeTell
		ActiveTellDTO res = this.createActiveTell(areaId, curDate, activeType, activeId, type, value, beginTimeOfPo);
		jsonObject.put("code", res.getActiveTell() != null ? 200 : 400);
		return jsonObject;
	}

	private boolean hasRecord(int activeType, long activeId, int type, String value, long areaId, long beginTimeOfPo) {
		ActiveTellCommonParamDTO param = new ActiveTellCommonParamDTO();
		// 区域
		param.setAreaId(areaId);
		// 通知业务类型
		param.setTellActiveType(activeType);
		// 手机或邮件
		param.setTellTargetType(type);
		// 手机号或邮箱
		param.setTellTargetValue(value);
		
		param.setTellActiveId(activeId);

		if (activeType == CodeInfoUtil.PO_TYPE_OF_ACTIVE_TELL) {
			// 活动通知时需要将用户选择的活动开始时间作为参数进行查询
			param.setActiveBeginTime(beginTimeOfPo);
		}

		List<ActiveTell> activeTellList = activeTellFacade.getActiveTellByParam(param);
		return activeTellList != null && activeTellList.size() > 0 ? true : false;
	}

	private Date getBeginDateByParam(Date curDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		calendar.set(Calendar.HOUR_OF_DAY, CodeInfoUtil.PO_BEGIN_HOUR);
		calendar.set(Calendar.MINUTE, CodeInfoUtil.PO_BEGIN_MINUTE);
		calendar.set(Calendar.SECOND, CodeInfoUtil.PO_BEGIN_SENCOND);
		calendar.set(Calendar.MILLISECOND, CodeInfoUtil.PO_BEGIN_MiSENCOND);
		return calendar.getTime();
	}

	private ActiveTellDTO createActiveTell(int areaId, Date curDate, int activeType, long activeId, int type,
			String value, long beginTimeOfPo) {
		ActiveTell activeTell = new ActiveTell();
		activeTell.setAreaId(areaId);
		activeTell.setCreateTime(curDate.getTime());
		activeTell.setTellActiveType(activeType);
		activeTell.setTellTargetType(type);
		activeTell.setTellTargetValue(value);
		activeTell.setTellActiveId(activeId);

		if (activeType == CodeInfoUtil.PO_TYPE_OF_ACTIVE_TELL) {
			// po档期活动通知，才有通知时间
			activeTell.setActiveBeginTime(beginTimeOfPo);
			ScheduleVO scheduleVO=scheduleFacade.getScheduleById(activeId);
			if(scheduleVO!=null && scheduleVO.getPo()!=null && scheduleVO.getPo().getScheduleDTO()!=null && scheduleVO.getPo().getScheduleDTO().getSchedule()!=null){
				activeTell.setTellActiveTitle(scheduleVO.getPo().getScheduleDTO().getSchedule().getBrandName());
			}
		}

		if (activeType == CodeInfoUtil.BRAND_TYPE_OF_ACTIVE_TELL) {
			//fill brand name
			BrandDTO brandDTO=brandFacade.getBrandByBrandId(activeId);
			if(brandDTO!=null && brandDTO.getBrand()!=null){
				// activeTell.setTellActiveTitle(brandDTO.getBrand().getBrandNameZh());
				// 这里采用自动的品牌 会处理中英文名字为空的情况
				activeTell.setTellActiveTitle(brandDTO.getBrand().getBrandNameAuto());
			}
		}

		// create
		ActiveTellDTO activeTellDTO = new ActiveTellDTO();
		activeTellDTO.setActiveTell(activeTell);
		ActiveTellDTO res = activeTellFacade.saveActiveTell(activeTellDTO);
		return res;
	}
}
