/*
 * 2014-9-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.ActivitySchedule;
import com.xyl.mmall.promotion.activity.Label;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.vo.ActivityVO;

/**
 * PromotionController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-19
 * @since 1.0
 */
@Controller
@RequestMapping("/promotion")
public class PromotionController extends BaseController {

	private static Logger logger = Logger.getLogger(PromotionController.class);

	@Autowired
	private PromotionFacade promotionFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private LocationService locationService;

	/**
	 * 活动列表页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "activity" })
	@RequiresPermissions(value = { "promotion:activity" })
	public String list(Model model) {
		List<AreaDTO> areaList = businessFacade.getAreadByIdList(agentService.findAgentSiteIdsByPermission(
				SecurityContextUtils.getUserId(), "promotion:activity"));
		model.addAttribute("areaList", JsonUtils.toJson(areaList));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 0);
		return "pages/promotion/activity";
	}

	/**
	 * 活动列表页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "activityaudit" })
	@RequiresPermissions(value = { "promotion:activityaudit" })
	public String activityAudit(Model model) {
		List<AreaDTO> areaList = businessFacade.getAreadByIdList(agentService.findAgentSiteIdsByPermission(
				SecurityContextUtils.getUserId(), "promotion:activityaudit"));
		model.addAttribute("areaList", JsonUtils.toJson(areaList));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 1);
		return "pages/promotion/activity";
	}

	/**
	 * 获取站点列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getProvinceList")
	public Map<String, Object> areaList() {
		Map<String, Object> map = new HashMap<>();
		List<AreaDTO> areaList = businessFacade.getAreadByIdList(agentService.findAgentSiteIdsByPermission(
				SecurityContextUtils.getUserId(), "promotion:activityEdit"));
		map.put("result", areaList);
		map.put("code", 200);
		return map;
	}

	/**
	 * 获取列表数据
	 * 
	 * @param state
	 * @param province
	 * @param offset
	 * @param limit
	 * @param model
	 * @return
	 */
	@RequestMapping("listData")
	public void listData(@RequestParam(value = "state", defaultValue = "-1") int state,
			@RequestParam(value = "province", defaultValue = "-1") long province,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10") int limit, 
			@RequestParam(value = "apply", defaultValue = "1") int apply, Model model) {
		long userId = SecurityContextUtils.getUserId();
		List<PromotionDTO> promotions = null;
		int count = 0;
		//获取权限的站点
		List<AreaDTO> selectAreaList = new ArrayList<>();
		PromotionQueryBeanDTO promotionQueryBeanDTO = new PromotionQueryBeanDTO();
		if (province > 0) {
			AreaDTO dto = businessFacade.getAreaById(province);
			selectAreaList.add(dto);
			long selectedPermission = promotionFacade.getAreaPermission(selectAreaList);
			promotionQueryBeanDTO.setAreaPermission(selectedPermission);
		}
		
		if (apply == 1) {
			//申请只获取当前用户申请的
			promotions = promotionFacade.getPromotionList(userId, state, province, limit, offset);
			count = promotionFacade.getPromotionCount(userId, state, province);
		} else {
			Subject subject = SecurityUtils.getSubject();
			if (!subject.isPermitted("promotion:activityaudit")) {
				return;
			}
			
			List<AreaDTO> areaList = businessFacade.getAreadByIdList(agentService.findAgentSiteIdsByPermission(
					SecurityContextUtils.getUserId(), "promotion:activityEdit"));
			
			long fullPermission = promotionFacade.getAreaPermission(areaList);
			
			promotionQueryBeanDTO.setFullPermission(fullPermission);
			promotionQueryBeanDTO.setLimit(limit);
			promotionQueryBeanDTO.setOffset(offset);
			promotionQueryBeanDTO.setAuditState(state);
				
			promotions = promotionFacade.getCommitPromotions(promotionQueryBeanDTO);
			count = promotionFacade.getCommitPromotionCount(promotionQueryBeanDTO);
		}
		
		List<ActivityVO> activityVOs = new ArrayList<>();

		if (CollectionUtils.isEmpty(promotions)) {
			model.addAttribute("list", activityVOs);
			model.addAttribute("total", 1);
			model.addAttribute("code", 200);
			return;
		}

		for (PromotionDTO promotionDTO : promotions) {
			if (promotionDTO == null) {
				continue;
			}

			ActivityVO vo = new ActivityVO(promotionDTO);
			vo.setApplyUserName(promotionDTO.getApplyUserName());
			vo.setAuditUserName(promotionDTO.getAuditUserName());

			if (StringUtils.isNotBlank(vo.getSelectedProvince())) {
				if (!ActivationConstants.OVERALL.equals(vo.getSelectedProvince())) {
					for (String id : vo.getSelectedProvince().split(",")) {
						if (StringUtils.isBlank(id)) {
							continue;
						}
						AreaDTO areaDTO = businessFacade.getAreaById(Long.valueOf(id));
						vo.getProvinceList().add(areaDTO);
					}
				}
			}

			if (StringUtils.isNotBlank(vo.getLabels())) {
				vo.setLabelList(JsonUtils.parseArray(vo.getLabels(), Label.class));
			}

			activityVOs.add(vo);
		}

		model.addAttribute("list", activityVOs);
		model.addAttribute("total", count);
		model.addAttribute("code", 200);
	}
	
	/**
	 * 查看活动的具体详情
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("activityEdit")
	@RequiresPermissions(value = { "promotion:activityEdit" })
	public String view(@RequestParam(value = "id", defaultValue = "-1") long id,
			@RequestParam(value = "editable", defaultValue = "1") int editable, Model model) {
		appendStaticMethod(model);
		PromotionDTO promotion = promotionFacade.getPromotionById(id);
		ActivityVO activityVO = null;

		if (promotion != null) {
			activityVO = new ActivityVO(promotion);
		} else {
			activityVO = new ActivityVO(null);
		}

		List<AreaDTO> areaList = businessFacade.getAreadByIdList(agentService.findAgentSiteIdsByPermission(
				SecurityContextUtils.getUserId(), "promotion:activityEdit"));

		activityVO.setProvinceList(areaList);
		String provinces = activityVO.getSelectedProvince();
		if (StringUtils.isNotBlank(provinces)) {
			if (!ActivationConstants.OVERALL.equals(provinces)) {
				List<Long> ids = new ArrayList<>();
				for (String province : provinces.split(",")) {
					if (StringUtils.isBlank(province)) {
						continue;
					}
					ids.add(Long.valueOf(province));
				}
				activityVO.setProvinceIds(ids);
			}
		}

		if (StringUtils.isNotBlank(activityVO.getLabels())) {
			activityVO.setLabelList(JsonUtils.parseArray(activityVO.getLabels(), Label.class));
		}

		if (StringUtils.isNotBlank(activityVO.getItems())) {
			activityVO.setItemList(JsonUtils.parseArray(activityVO.getItems(), Activation.class));
		}

		extractScheduleFromPromotion(activityVO);

		model.addAttribute("activityVO", activityVO);
		model.addAttribute("editable", editable);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/promotion/activityEdit";
	}

	private void extractScheduleFromPromotion(ActivityVO activityVO) {
		if (activityVO == null) {
			return;
		}

		String contents = activityVO.getItems();
		if (StringUtils.isBlank(contents)) {
			return;
		}

		List<Activation> activations = JsonUtils.parseArray(contents, Activation.class);
		if (CollectionUtils.isEmpty(activations)) {
			return;
		}

		for (Activation activation : activations) {
			List<ActivitySchedule> schedules = activation.getSchedules();
			if (CollectionUtils.isEmpty(schedules)) {
				continue;
			}

			for (ActivitySchedule schedule : schedules) {
				ScheduleVO vo = scheduleFacade.getScheduleById(schedule.getId());
				if (vo != null) {
					if (vo.getPo() == null) {
						continue;
					}

					if (vo.getPo().getScheduleDTO() == null) {
						continue;
					}

					if (vo.getPo().getScheduleDTO().getSchedule() == null) {
						continue;
					}

					schedule.setTitle(vo.getPo().getScheduleDTO().getSchedule().getTitle());
					schedule.setStartTime(vo.getPo().getScheduleDTO().getSchedule().getStartTime());
					
					long provinceId = vo.getPo().getScheduleDTO().getSchedule().getSaleAreaId();
					AreaDTO areaDTO = businessFacade.getAreaById(provinceId);
					if (areaDTO == null) {
						continue;
					}
					schedule.setAreaName(areaDTO.getAreaName());
				}
			}
		}
	}

	/**
	 * 修改促销信息（新增或者修改）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(@RequestBody ActivityVO activityVO) throws Exception {
		Map<String, Object> map = new HashMap<>();

		if (activityVO == null) {
			map.put("code", 404);
			map.put("message", "对象为空");
			return map;
		}

		if (StringUtils.isBlank(activityVO.getName())) {
			map.put("code", 404);
			map.put("message", "名称不能为空");
			return map;
		}

		if (StringUtils.isBlank(activityVO.getDescription())) {
			map.put("code", 404);
			map.put("message", "描述不能为空");
			return map;
		}

		if (!ActivationConstants.OVERALL.equals(activityVO.getAllProvince())
				&& CollectionUtils.isEmpty(activityVO.getProvinceIds())) {
			map.put("code", 404);
			map.put("message", "站点不能为空");
			return map;
		}

		if (!ActivationConstants.OVERALL.equals(activityVO.getAllPo())
				&& CollectionUtils.isEmpty(activityVO.getItemList())) {
			map.put("code", 404);
			map.put("message", "活动效果不能为空");
			return map;
		}

		PromotionDTO promotionDTO = promotionFacade.getPromotionById(activityVO.getId());
		boolean isAdd = false;
		if (promotionDTO == null) {
			promotionDTO = new PromotionDTO();
			isAdd = true;
		}
		promotionDTO.setName(activityVO.getName());
		promotionDTO.setDescription(activityVO.getDescription());
		promotionDTO.setStartTime(activityVO.getStartTime());
		promotionDTO.setEndTime(activityVO.getEndTime());
		promotionDTO.setFavorType(activityVO.getFavorType());
		promotionDTO.setPlatformType(activityVO.getPlatformType());
		
		if (!CollectionUtils.isEmpty(activityVO.getProvinceIds())) {
			String provinces = JsonUtils.toJson(activityVO.getProvinceIds());
			promotionDTO.setSelectedProvince(StringUtils.substring(provinces, 1, provinces.length() - 1));
		} else if (ActivationConstants.OVERALL.equals(activityVO.getAllProvince())) {
			promotionDTO.setSelectedProvince(ActivationConstants.OVERALL);
		}

		if (!CollectionUtils.isEmpty(activityVO.getLabelList())) {
			promotionDTO.setLabels(JsonUtils.toJson(activityVO.getLabelList()));
		} else if (ActivationConstants.OVERALL.equals(activityVO.getAllPo())) {
			promotionDTO.setDeclarePO(ActivationConstants.OVERALL);
		}

		if (!CollectionUtils.isEmpty(activityVO.getItemList())) {
			promotionDTO.setItems(JsonUtils.toJson(activityVO.getItemList()));
			String declarePO = getDeclarePOFromContents(activityVO.getItemList());
			promotionDTO.setDeclarePO(declarePO);
		}

		if (promotionDTO.getStartTime() >= promotionDTO.getEndTime()) {
			logger.error("start time bigger than end time.");
			map.put("code", 404);
			map.put("message", "开始时间不能大于结束时间");
		}
		List<AreaDTO> areaDTOs = businessFacade.getAreadByIdList(activityVO.getProvinceIds());
		//获取区域权限
		long areaPermission = promotionFacade.getAreaPermission(areaDTOs);
		promotionDTO.setAreaPermission(areaPermission);
		
		long userId = SecurityContextUtils.getUserId();
		promotionDTO.setApplyUserId(userId);

		boolean status = false;
		if (isAdd) {
			status = promotionFacade.addPromotion(promotionDTO) != null;
		} else {
			status = promotionFacade.updatePromotion(promotionDTO);
		}

		if (status) {
			map.put("code", 200);
			map.put("message", "保存成功");
		}

		return map;
	}

	/**
	 * 查看活动的具体详情
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("checkpo")
	public Map<String, Object> checkPo(@RequestParam(value = "poId", defaultValue = "") String poIds,
			@RequestParam(value = "id", defaultValue = "") long id,
			@RequestParam(value = "provinceId", defaultValue = "") String provinceIds,
			@RequestParam(value = "start", defaultValue = "") long start,
			@RequestParam(value = "end", defaultValue = "") long end) {
		Map<String, Object> params = new HashMap<>();

		long poId = 0;
		PromotionDTO promotion = null;
		// 不是全部档期
		if (!ActivationConstants.OVERALL.equals(poIds)) {
			if (StringUtils.isBlank(poIds)) {
				params.put("code", 404);
				params.put("message", "请选择po");
				return params;
			}

			String[] idArray = poIds.split(",");
			for (String idValue : idArray) {
				if (StringUtils.isBlank(idValue)) {
					continue;
				}
				poId = Long.valueOf(idValue);
				promotion = promotionFacade.getPromotionByPO(poId, id, start, end, false);
				if (promotion != null) {
					params.put("code", 404);
					params.put("message", "此档期已参与\"" + promotion.getDescription() + "\"活动");
					return params;
				}
			}

			params.put("code", 200);
			return params;
		}

		// 全部档期
		if (StringUtils.isBlank(provinceIds)) {
			params.put("code", 404);
			params.put("message", "请选择站点信息");
			return params;
		}

		if (start <= 0) {
			params.put("code", 404);
			params.put("message", "请选择开始时间");
			return params;
		}

		if (end <= 0) {
			params.put("code", 404);
			params.put("message", "请选择结束时间");
			return params;
		}

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.startDate = start;
		paramDTO.endDate = end;

		// 全部站点
		if (ActivationConstants.OVERALL.equals(provinceIds)) {
			// 全部站点的情况
			if (!batchCheckPo(id, params, paramDTO, start, end)) {
				return params;
			}
		} else {
			// 指定站点的情况
			String[] idArray = provinceIds.split(",");
			for (String provinceId : idArray) {
				if (!StringUtils.isNumeric(provinceId)) {
					continue;
				}
				paramDTO.curSupplierAreaId = Long.valueOf(provinceId);
				if (!batchCheckPo(id, params, paramDTO, start, end)) {
					return params;
				}
			}
		}

		params.put("code", 200);
		return params;
	}

	private boolean batchCheckPo(long id, Map<String, Object> params, ScheduleCommonParamDTO paramDTO, long start,
			long end) {
		PromotionDTO promotion = null;
		ScheduleListVO vo = scheduleFacade.getScheduleListCommon(paramDTO);
		List<Schedule> schedules = getScheduleListFromVo(vo);
		if (CollectionUtils.isEmpty(schedules)) {
			params.put("code", 404);
			params.put("message", "没有任何档期数据");
			return false;
		}

		for (Schedule schedule : schedules) {
			promotion = promotionFacade.getPromotionByPO(schedule.getId(), id, start, end, false);
			if (promotion != null) {
				params.put("code", 404);
				params.put("message", "此档期已参与\"" + promotion.getDescription() + "\"活动");
				return false;
			}
		}
		return true;
	}

	private List<Schedule> getScheduleListFromVo(ScheduleListVO vo) {
		POListDTO dto = vo.getPoList();
		if (dto == null) {
			return null;
		}
		List<PODTO> list = dto.getPoList();
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		List<Schedule> schedules = new ArrayList<>(list.size());
		for (PODTO podto : list) {
			if (podto.getScheduleDTO() == null) {
				continue;
			}

			if (podto.getScheduleDTO() == null) {
				continue;
			}

			schedules.add(podto.getScheduleDTO().getSchedule());
		}
		return schedules;
	}

	/**
	 * 从活动中抽取出参与的po
	 * 
	 * @param contents
	 * @return
	 */
	private String getDeclarePOFromContents(List<Activation> activations) {
		if (CollectionUtils.isEmpty(activations)) {
			return null;
		}

		Set<String> poSet = new HashSet<>();

		for (Activation activation : activations) {
			List<ActivitySchedule> poList = activation.getSchedules();

			if (CollectionUtils.isEmpty(poList)) {
				continue;
			}

			for (ActivitySchedule po : poList) {
				poSet.add(String.valueOf(po.getId()));
			}
		}

		String returnValue = null;
		for (String po : poSet) {
			if (StringUtils.isBlank(returnValue)) {
				returnValue = po;
			} else {
				returnValue = returnValue.concat(",").concat(po);
			}
		}

		return returnValue;
	}

	/**
	 * 删除
	 */
	@RequestMapping("delete")
	@RequiresPermissions(value = { "promotion:activity" })
	public Map<String, Object> delete(@RequestParam(value = "id", defaultValue = "0") long id) {

		Map<String, Object> map = new HashMap<>();

		PromotionDTO promotionDTO = promotionFacade.getPromotionById(id);

		if (promotionDTO == null) {
			map.put("code", 404);
			map.put("message", "此活动不存在");
			return map;
		}

		promotionDTO.setAuditState(StateConstants.CANCEL);

		boolean status = promotionFacade.updatePromotion(promotionDTO);
		if (status) {
			map.put("code", 200);
			map.put("message", "活动删除成功");
		} else {
			map.put("code", 404);
			map.put("message", "活动删除失败");
		}

		return map;
	}

	/**
	 * 提交审核
	 */
	@RequestMapping("auditcommit")
	@RequiresPermissions(value = { "promotion:activity" })
	public Map<String, Object> auditCommit(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		PromotionDTO promotionDTO = promotionFacade.getPromotionById(id);

		if (promotionDTO == null) {
			map.put("code", 404);
			map.put("message", "此活动不存在");
			return map;
		}

		int state = promotionDTO.getAuditState();

		if (StateConstants.COMMIT == state) {
			map.put("code", 404);
			map.put("message", "此活动已提交审核");
			return map;
		}

		if (StateConstants.PASS == state) {
			map.put("code", 404);
			map.put("message", "此活动已通过审核");
			return map;
		}

		if (StateConstants.CANCEL == state) {
			map.put("code", 404);
			map.put("message", "此活动已被删除");
			return map;
		}

		promotionDTO.setAuditState(StateConstants.COMMIT);

		boolean status = promotionFacade.updatePromotion(promotionDTO);
		if (status) {
			map.put("code", 200);
			map.put("message", "提交审核成功");
		} else {
			map.put("code", 404);
			map.put("message", "提交审核失败");
		}

		return map;
	}

	/**
	 * 审核
	 */
	@RequestMapping("audit")
	@RequiresPermissions(value = { "promotion:activityaudit" })
	public Map<String, Object> audit(@RequestParam(value = "id", defaultValue = "0") long id,
			@RequestParam(value = "auditValue", defaultValue = "0") int auditValue) {
		Map<String, Object> map = new HashMap<>();

		// 审核值只能是通过和拒绝
		if (auditValue != StateConstants.PASS && auditValue != StateConstants.REFUSED) {
			map.put("code", 404);
			map.put("message", "审核状态不符合要求");
			return map;
		}

		PromotionDTO promotionDTO = promotionFacade.getPromotionById(id);

		if (promotionDTO == null) {
			map.put("code", 404);
			map.put("message", "此活动不存在");
			return map;
		}

		// 只有审核中的才能审核
		if (promotionDTO.getAuditState() != StateConstants.COMMIT) {
			map.put("code", 404);
			map.put("message", "活动不在审核中，不能进行此操作");
			return map;
		}

		long userId = SecurityContextUtils.getUserId();
		promotionDTO.setAuditState(auditValue);
		promotionDTO.setAuditUserId(userId);
		promotionDTO.setAuditTime(System.currentTimeMillis());

		boolean status = promotionFacade.updatePromotion(promotionDTO);

		if (status) {
			map.put("code", 200);
			map.put("message", "审核成功");
		} else {
			map.put("code", 404);
			map.put("message", "审核失败");
		}

		return map;
	}

	/**
	 * 作废
	 */
	@RequestMapping("discard")
	@RequiresPermissions(value = { "promotion:activityaudit" })
	public Map<String, Object> discard(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		PromotionDTO promotionDTO = promotionFacade.getPromotionById(id);

		if (promotionDTO == null) {
			map.put("code", 404);
			map.put("message", "此活动不存在");
			return map;
		}

		// 只有审核通过的才能作废
		if (promotionDTO.getAuditState() != StateConstants.PASS) {
			map.put("code", 404);
			map.put("message", "活动未审核通过，不能进行此操作");
			return map;
		}

		long userId = SecurityContextUtils.getUserId();
		promotionDTO.setAuditState(StateConstants.COMMIT);
		promotionDTO.setAuditUserId(userId);
		promotionDTO.setAuditTime(System.currentTimeMillis());

		boolean status = promotionFacade.updatePromotion(promotionDTO);

		if (status) {
			map.put("code", 200);
			map.put("message", "撤销活动审核成功");
		} else {
			map.put("code", 404);
			map.put("message", "撤销活动审核失败");
		}

		return map;
	}
}
