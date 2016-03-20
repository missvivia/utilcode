package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.netease.push.util.JSONUtils;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.CmsBrandFacade;
import com.xyl.mmall.cms.facade.FeedbackFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.excelparse.XLSExport;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.UserFeedback;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.mobile.facade.converter.MobileOS;
import com.xyl.mmall.mobile.facade.vo.MobileKeyPairVO;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.vo.UserFeedbackVO;

/**
 * APP内容管理Controller
 * 
 * @author xiangwenbin
 *
 */
@Controller
@RequestMapping("/app")
public class AppContentController extends BaseController {

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Resource
	private CmsBrandFacade cmsBrandFacade;

	@Autowired
	private FeedbackFacade feedbackFacade;

	@Autowired
	private MobilePushManageFacade pushManageFacade;

	/**
	 * 消息管理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pmessage", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:pmessage" })
	public String pushMessage(Model model) {
		appendStaticMethod(model);
		long userId = SecurityContextUtils.getUserId();
		List<AreaDTO> areaList = cmsBrandFacade.getAreaList(userId, "app:feedback");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		List<MobileKeyPairVO> os= MobileOS.getSupportOs();
		model.addAttribute("provinceList", list);
		model.addAttribute("os",os );
		model.addAttribute("os_json",JSONUtils.getJson(os));
		model.addAttribute("provinceList_json",JSONUtils.getJson(list));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/app/pmessage";
	}

	/**
	 * 意见反馈
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:feedback" })
	public String feedback(Model model) {
		appendStaticMethod(model);
		long userId = SecurityContextUtils.getUserId();
		List<AreaDTO> areaList = cmsBrandFacade.getAreaList(userId, "app:feedback");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute("provinceList", list);
		model.addAttribute("systems", feedbackFacade.getAllSystems());
		model.addAttribute("versions", feedbackFacade.getAllVersions());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/app/feedback";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/feedback/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:feedback" })
	public @ResponseBody BaseJsonVO getFeedbackList(@RequestParam long startTime, @RequestParam long endTime,
			@RequestParam long areaId, @RequestParam String system, @RequestParam String version,
			@RequestParam String key, @RequestParam int limit, @RequestParam int offset) {
		DDBParam param = new DDBParam("submitTime", false, limit, offset);
		RetArg retArg = feedbackFacade.getFeedBackList(startTime, endTime, areaId, system, version, key, param);
		List<UserFeedback> list = RetArgUtil.get(retArg, ArrayList.class);
		param = RetArgUtil.get(retArg, DDBParam.class);
		BaseJsonVO vo = new BaseJsonVO();
		vo.setCode(200);
		BaseJsonListResultVO resultVO = new BaseJsonListResultVO(list);
		resultVO.setTotal(param.getTotalCount());
		vo.setResult(resultVO);
		return vo;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/feedback/export", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:feedback" })
	public void exportFeedbackExcel(@RequestParam long startTime, @RequestParam long endTime,
			@RequestParam long areaId, @RequestParam String system, @RequestParam String version,
			@RequestParam String key, HttpServletResponse response) {
		DDBParam param = new DDBParam("submitTime", false, 0, 0);
		RetArg retArg = feedbackFacade.getFeedBackList(startTime, endTime, areaId, system, version, key, param);
		List<UserFeedback> list = RetArgUtil.get(retArg, ArrayList.class);
		List<UserFeedbackVO> listVO = new ArrayList<>(list.size());
		for (UserFeedback feedback : list) {
			UserFeedbackVO vo = new UserFeedbackVO();
			vo.genDataFromMeta(feedback);
			listVO.add(vo);
		}
		String fileName = "feedback_startTime_" + startTime + "_endTime_" + endTime;
		XLSExport export = new XLSExport(fileName);
		export.createEXCEL(listVO, UserFeedbackVO.class);
		try {
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			export.exportXLS(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /**
	// * 意见反馈
	// * @param model
	// * @return
	// */
	// @RequestMapping(value = "/push", method = RequestMethod.GET)
	// @RequiresPermissions(value = {"app:push" })
	// public String push(Model model) {
	// appendStaticMethod(model);
	// long userId = SecurityContextUtils.getUserId();
	// List<AreaDTO> areaList = cmsBrandFacade.getAreaList(userId,
	// "app:feedback");
	// List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
	// model.addAttribute("provinceList", list);
	// model.addAttribute("os", MobileOS.getSupportOs());
	// model.addAttribute("pages",
	// leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
	// return "pages/app/push";
	// }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pmessage/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:pmessage" })
	public @ResponseBody BaseJsonVO getPushList(@RequestParam(value = "startTime", required = false) Long startTime,
			@RequestParam(value = "endTime", required = false) Long endTime,
			@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "os", required = false) String os,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset) {
		if (limit == null)
			limit = 10;
		if (offset == null)
			offset = 0;
		DDBParam param = new DDBParam("pushTime", false, limit, offset);
		if (startTime == null)
			startTime = System.currentTimeMillis() - 7*24*60*60*1000;
		if (endTime == null)
			endTime = 0l;
		PushManagementDTO ao = new PushManagementDTO();
		ao.setAreaCode(areaId);
		if (StringUtils.isNotBlank(search))
			ao.setContent(search);
		ao.setStartTime(startTime);
		ao.setPushSuccess(-1);
		ao.setEndTime(endTime);
		if(!os.contains(","))
			ao.setPlatformType(os);
		RetArg retArg = pushManageFacade.getPushConfigList(ao, param);
		List<PushManagementDTO> list = RetArgUtil.get(retArg, ArrayList.class);
		param = RetArgUtil.get(retArg, DDBParam.class);
		BaseJsonVO vo = new BaseJsonVO();
		vo.setCode(200);
		BaseJsonListResultVO resultVO = new BaseJsonListResultVO(list);
		resultVO.setTotal(param.getTotalCount());
		vo.setResult(resultVO);
		return vo;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pmessage/add", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:pmessage" })
	public @ResponseBody BaseJsonVO addPush(@RequestParam(value = "pushTime", required = true) Long pushTime,
			@RequestParam(value = "link", required = false) String link,
			@RequestParam(value = "areaId", required = true) List<String> areaIds,
			@RequestParam(value = "os", required = true) List<String> os,
			@RequestParam(value = "content", required = true) String content,
			@RequestParam(value = "title", required = true) String title) {
		BaseJsonVO vo = new BaseJsonVO();
		vo.setCode(200);
		for (String areaId : areaIds) {
			PushManagement ao = new PushManagement();
			ao.setAreaCode(areaId);
			ao.setContent(content);
			ao.setCreateTime(System.currentTimeMillis());
			ao.setUpdateTime(System.currentTimeMillis());
			ao.setPushTime(pushTime);
			ao.setLink(link);
			ao.setOperator(SecurityContextUtils.getUserName());
			String osl = "";
			if (os != null) {
				for (String a : os) {
					osl = osl + "," + a;
				}
			}
			ao.setPlatformType(osl);
			ao.setTitle(title);
			PushManagementDTO dto = pushManageFacade.addPushConfig(ao);
			if (dto == null || dto.getId() == 0)
				vo.setCode(500);
		}
		
		return vo;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pmessage/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:pmessage" })
	public @ResponseBody BaseJsonVO deletePush(@RequestParam long id) {
		Boolean success = pushManageFacade.deletePushConfigById(id);
		BaseJsonVO vo = new BaseJsonVO();
		if (success)
			vo.setCode(200);
		else
			vo.setCode(500);
		return vo;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pmessage/update", method = RequestMethod.GET)
	@RequiresPermissions(value = { "app:pmessage" })
	public @ResponseBody BaseJsonVO updatePush(@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "pushTime", required = true) Long pushTime,
			@RequestParam(value = "link", required = false) String link,
			@RequestParam(value = "areaId", required = true) String areaId,
			@RequestParam(value = "os", required = true) List<String> os,
			@RequestParam(value = "content", required = true) String content,
			@RequestParam(value = "title", required = true) String title) {
		PushManagementDTO ao = new PushManagementDTO();
		ao.setAreaCode(areaId);
		ao.setContent(content);
		ao.setCreateTime(System.currentTimeMillis());
		ao.setUpdateTime(System.currentTimeMillis());
		ao.setPushTime(pushTime);
		ao.setLink(link);
		ao.setOperator(SecurityContextUtils.getUserName());
		String osl = "";
		if (os != null) {
			for (String a : os) {
				osl = osl + "," + a;
			}
		}
		ao.setPlatformType(osl);
		ao.setTitle(title);
		boolean success = pushManageFacade.updatePushManagement(id, ao);
		BaseJsonVO vo = new BaseJsonVO();
		if (success)
			vo.setCode(200);
		else
			vo.setCode(500);
		return vo;
	}

}
