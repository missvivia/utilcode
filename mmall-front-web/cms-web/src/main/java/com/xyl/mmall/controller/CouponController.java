/*
 * 2014-9-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.promotion.CouponFacade;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.AuthorityFacade;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.vo.CouponConfigVO;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.CouponConfigDTO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.enums.CodeType;
import com.xyl.mmall.promotion.enums.CouponConfigType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.vo.CouponVO;

/**
 * CouponController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-19
 * @since 1.0
 */
@Controller
@RequestMapping("")
public class CouponController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(PromotionController.class);

	@Autowired
	private CouponFacade couponFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private LocationFacade locationFacade;
	
	@Autowired
	private BusinessFacade businessFacade;
	
	@Autowired
	private AuthorityFacade authorityFacade;

	@Autowired
	private SiteCMSFacade siteCMSFacade;

	@Resource
	private AgentService agentService;
	
	@Value("${file.tmp}")
	private String tmpFileDir;

	/**
	 * 获取活动列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/promotion/coupon")
	@RequiresPermissions(value = { "promotion:coupon" })
	public String list(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 0);
		return "pages/promotion/coupon";
	}
	
	/**
	 * 获取活动列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/promotion/couponaudit")
	@RequiresPermissions(value = { "promotion:couponaudit" })
	public String couponAudit(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 1);
		return "pages/promotion/coupon";
	}

	@RequestMapping("/coupon/listData")
	public void listData(@RequestParam(value = "state", defaultValue = "-1") int state,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "qvalue", defaultValue = "") String qvalue,
			@RequestParam(value = "apply", defaultValue = "1") int apply, Model model) {
		List<CouponDTO> coupons = null;
		int total = 0;
		long userId = SecurityContextUtils.getUserId();
		coupons = couponFacade.getCouponList(userId, state, qvalue, limit, offset);
		total = couponFacade.getCouponCount(userId, state, qvalue);
//		if (apply == 1) {
//		} else {
//			Subject subject = SecurityUtils.getSubject();
//			if (!subject.isPermitted("promotion:couponaudit")) {
//				return;
//			}
//			coupons = couponFacade.getCouponList(-1, state, qvalue, limit, offset);
//			total = couponFacade.getCouponCount(-1, state, qvalue);
//		}
		List<CouponVO> list = new ArrayList<CouponVO>();

		if (CollectionUtils.isEmpty(coupons)) {
			model.addAttribute("list", list);
			model.addAttribute("total", total);
			model.addAttribute("code", ResponseCode.RES_SUCCESS);
			return;
		}

		for (CouponDTO couponDTO : coupons) {
			CouponVO vo = new CouponVO(couponDTO);
			vo.setApplyUserName(couponDTO.getApplyUserName());
			vo.setAuditUserName(couponDTO.getAuditUserName());
			String[] areaIds = vo.getAreaIds().split(",");
			List<AreaDTO> areaList = new ArrayList<AreaDTO>(areaIds.length);
			for (String area : areaIds) {
				long areaId = Long.parseLong(area);
				AreaDTO areaDTO = new AreaDTO();
				areaDTO.setId(areaId);
				areaDTO.setAreaName(locationFacade.getLocationNameByCode(areaId, false));
				areaList.add(areaDTO);
			}
			vo.setAreaList(areaList);
			list.add(vo);
		}

		model.addAttribute("list", list);
		model.addAttribute("total", total);
		model.addAttribute("code", ResponseCode.RES_SUCCESS);
	}

	/**
	 * 查看活动的具体详情
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/promotion/couponEdit")
	@RequiresPermissions(value = { "promotion:couponEdit" })
	public String view(@RequestParam(value = "id", defaultValue = "-1") long id,
			@RequestParam(value = "editable", defaultValue = "1") int editable, Model model) {
		appendStaticMethod(model);
		CouponVO couponVO = null;
		long uid = SecurityContextUtils.getUserId();
		if (id > 0l) {
			CouponDTO couponDTO = couponFacade.getCouponById(id, false);
			couponVO = checkPromission(couponDTO);
			if (couponVO == null) {
				model.addAttribute("username", SecurityContextUtils.getUserName());
				return "error/403";
			}
			if (StringUtils.isNotBlank(couponVO.getItems())) {
				couponVO.setItemList(JsonUtils.parseArray(couponVO.getItems(), Activation.class));
			}
			if (StringUtils.isNotBlank(couponVO.getAreaIds())) {
				String[] areaIds = couponVO.getAreaIds().split(",");
				List<AreaDTO> areaList = new ArrayList<AreaDTO>(areaIds.length);
				for (String area : areaIds) {
					long areaId = Long.parseLong(area);
					AreaDTO areaDTO = new AreaDTO();
					areaDTO.setId(areaId);
					areaDTO.setAreaName(locationFacade.getLocationNameByCode(areaId, false));
					areaList.add(areaDTO);
				}
				couponVO.setAreaList(areaList);
			}
		}
		
		model.addAttribute("couponVO", couponVO == null ? new CouponVO(null) : couponVO);
		model.addAttribute("editable", editable);
		if (editable == 1) {
			model.addAttribute("areaList", authorityFacade.getAreaList(uid, 0, uid));
		}
		
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(uid));
		return "pages/promotion/couponEdit";
	}

	/**
	 * 修改促销信息（新增或者修改）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/coupon/save")
	public Map<String, Object> save(@RequestBody CouponVO couponVO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (couponVO == null) {
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "对象为空");
			return map;
		}

		if (StringUtils.isBlank(couponVO.getName())) {
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "名称不能为空");
			return map;
		}

		if (StringUtils.isBlank(couponVO.getDescription())) {
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "描述不能为空");
			return map;
		}
		
		if (CollectionUtils.isEmpty(couponVO.getItemList())) {
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "优惠券效果不能为空");
			return map;
		}
		
		CouponDTO couponDTO = couponFacade.getCouponById(couponVO.getId(), false);
		boolean isAdd = false;
		if (couponDTO == null) {
			couponDTO = new CouponDTO();
			isAdd = true;
		}
		// 如果优惠券编码不为空
		if (StringUtils.isNotBlank(couponVO.getCouponCode())) {
			if (couponVO.getCouponCode().length() > 20) {
				map.put("code", ResponseCode.RES_ERROR);
				map.put("message", "优惠券券号不能大于20位！");
				return map;
			}
			if (!RegexUtils.isLetterOrNumber(couponVO.getCouponCode())) {
				map.put("code", ResponseCode.RES_ERROR);
				map.put("message", "优惠券券号只能为数字或字母组合！");
				return map;
			}
			// 根据优惠券编码获取优惠券
			CouponDTO cdto = couponFacade.getCouponByCode(couponVO.getCouponCode(), false);
			// 如果不为空，并且是添加或者更新编码优惠券id不同
			if (cdto != null && (isAdd || cdto.getId() != couponDTO.getId())) {
				map.put("code", ResponseCode.RES_ERROR);
				map.put("message", "优惠券券号已经存在");
				return map;
			}
		} else {
			map.put("code", ResponseCode.RES_ERROR);
			map.put("message", "优惠券券号不能为空！");
			return map;
		}
		
		if (couponVO.getCodeType() == CodeType.PUBLIC) {
			couponDTO.setCouponCode(couponVO.getCouponCode());
		}
		
		if(StringUtils.isBlank(couponVO.getAreaIds())){
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "区域不能为空！");
			return map;
		}
		//验证区域重复和包含关系
		String[] distirctArray = StringUtils.split(couponVO.getAreaIds(), ',');
		// 获取区域权限
		StringBuilder areaIds = new StringBuilder(255);
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(SecurityContextUtils.getUserId());
		if (retArg == null) {
			map.put("code", ResponseCode.RES_FORBIDDEN);
			map.put("message", "没有区域权限！");
			return map;
		} else {
			// 是否为root
			if (RetArgUtil.get(retArg, Boolean.class)) {
				for (String areaId : distirctArray) {
					areaIds.append(areaId).append(",");
				}
			} else {
				Set<Long> areaSet = RetArgUtil.get(retArg, HashSet.class);
				for (String areaId : distirctArray) {
					try {
						if (areaSet.contains(Long.parseLong(areaId))) {
							areaIds.append(areaId).append(",");
							continue;
						}
					} catch (NumberFormatException e) {
						logger.error("Wrong areaId! AreaId : " + areaId);
					}
					map.put("code", ResponseCode.RES_ERROR);
					map.put("message", "区域错误！");
					return map;
				}
			}
			areaIds.deleteCharAt(areaIds.length() - 1);
		}
		if (areaIds.length() > 255) {
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "区域过多，最多支持40个不重复的区域！");
			return map;
		}
		couponDTO.setAreaIds(areaIds.toString());
		couponDTO.setBinderType(couponVO.getBinderType());
		couponDTO.setCodeType(couponVO.getCodeType());
		couponDTO.setRandomCount(couponVO.getRandomCount());
		couponDTO.setTimes(couponVO.getTimes());
		couponDTO.setTimesType(couponVO.getTimesType());
		couponDTO.setName(couponVO.getName());
		couponDTO.setDescription(couponVO.getDescription());
		couponDTO.setStartTime(couponVO.getStartTime());
		couponDTO.setEndTime(couponVO.getEndTime());
		String users = couponVO.getUsers();
		if (StringUtils.isNotBlank(users)) {
			users = users.trim();
			if (users.length() > 60000) {
				map.put("code", ResponseCode.RES_EPARAM);
				map.put("message", "绑定用户过多，最多支持输入60000个字符！");
				return map;
			}
			if (users.split(",").length > 20) {
				map.put("code", ResponseCode.RES_EPARAM);
				map.put("message", "绑定用户过多，最多支持20个用户！");
				return map;
			}
			couponDTO.setUsers(users);
		}
		couponDTO.setFavorType(couponVO.getFavorType());
		if (couponDTO.getStartTime() >= couponDTO.getEndTime()) {
			logger.error("start time bigger than end time.");
			map.put("code", ResponseCode.RES_EPARAM);
			map.put("message", "开始时间不能大于结束时间");
			return map;
		}

		if (!CollectionUtils.isEmpty(couponVO.getItemList())) {
			couponDTO.setItems(JsonUtils.toJson(couponVO.getItemList()));
		}

		long userId = SecurityContextUtils.getUserId();

		couponDTO.setApplyUserId(userId);

		boolean status = false;
		if (isAdd) {
			status = couponFacade.addCoupon(couponDTO) != null;
		} else {
			status = couponFacade.updateCoupon(couponDTO);
		}

		if (status) {
			map.put("code", ResponseCode.RES_SUCCESS);
			map.put("message", "保存成功");
		}

		return map;
	}

	/**
	 * 删除
	 */
	@RequestMapping("/coupon/delete")
	@RequiresPermissions(value = { "promotion:coupon" })
	public Map<String, Object> delete(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		CouponDTO couponDTO = couponFacade.getCouponById(id, false);

		if (couponDTO == null) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券不存在");
			return map;
		}

		if (checkPromission(couponDTO) == null) {
			map.put("code", ResponseCode.RES_FORBIDDEN);
			map.put("message", "没有权限删除此优惠券！");
			return map;
		}
		
		couponDTO.setAuditState(StateConstants.CANCEL);

		boolean status = couponFacade.updateCoupon(couponDTO);
		if (status) {
			map.put("code", ResponseCode.RES_SUCCESS);
			map.put("message", "优惠券删除成功");
		} else {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "优惠券删除失败");
		}

		return map;
	}

	/**
	 * 提交审核
	 */
	@RequestMapping("/coupon/auditcommit")
	@RequiresPermissions(value = { "promotion:coupon" })
	public Map<String, Object> auditCommit(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		CouponDTO couponDTO = couponFacade.getCouponById(id, false);

		if (couponDTO == null) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券不存在");
			return map;
		}

		if (checkPromission(couponDTO) == null) {
			map.put("code", ResponseCode.RES_FORBIDDEN);
			map.put("message", "没有权限提交此优惠券！");
			return map;
		}
		
		int state = couponDTO.getAuditState();

		if (StateConstants.COMMIT == state) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券已提交审核");
			return map;
		}

		if (StateConstants.PASS == state) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券已通过审核");
			return map;
		}

		if (StateConstants.CANCEL == state) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券已被删除");
			return map;
		}

		couponDTO.setAuditState(StateConstants.COMMIT);

		boolean status = couponFacade.updateCoupon(couponDTO);
		if (status) {
			map.put("code", ResponseCode.RES_SUCCESS);
			map.put("message", "优惠券提交审核成功");
		} else {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "优惠券提交审核失败");
		}

		return map;
	}

	/**
	 * 审核
	 */
	@RequestMapping("/coupon/audit")
	@RequiresPermissions(value = { "promotion:couponaudit" })
	public Map<String, Object> audit(@RequestParam(value = "id", defaultValue = "0") long id,
			@RequestParam(value = "auditValue", defaultValue = "0") int auditValue) {
		Map<String, Object> map = new HashMap<>();

		if (auditValue != StateConstants.PASS && auditValue != StateConstants.REFUSED) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "审核状态不符合要求");
			return map;
		}

		CouponDTO couponDTO = couponFacade.getCouponById(id, false);

		if (couponDTO == null) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券不存在");
			return map;
		}

		if (checkPromission(couponDTO) == null) {
			map.put("code", ResponseCode.RES_FORBIDDEN);
			map.put("message", "没有权限审核此优惠券！");
			return map;
		}
		
		// 只有审核中的才能审核
		if (couponDTO.getAuditState() != StateConstants.COMMIT) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "优惠券不在审核中，不能进行此操作");
			return map;
		}

		long userId = SecurityContextUtils.getUserId();

		couponDTO.setAuditState(auditValue);
		couponDTO.setAuditUserId(userId);
		couponDTO.setAuditTime(System.currentTimeMillis());

		boolean status = couponFacade.updateCoupon(couponDTO);
		if (status) {
			map.put("code", ResponseCode.RES_SUCCESS);
			map.put("message", "操作成功！");
		} else {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "操作失败！");
		}

		return map;
	}
	
	/**
	 * 撤销
	 */
	@RequestMapping("/coupon/discard")
	@RequiresPermissions(value = { "promotion:couponaudit" })
	public Map<String, Object> discard(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		CouponDTO couponDTO = couponFacade.getCouponById(id, false);

		if (couponDTO == null) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "此优惠券不存在");
			return map;
		}

		if (checkPromission(couponDTO) == null) {
			map.put("code", ResponseCode.RES_FORBIDDEN);
			map.put("message", "没有权限撤销此优惠券！");
			return map;
		}
		
		// 只有审核通过的才能作废
		if (couponDTO.getAuditState() != StateConstants.PASS) {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "优惠券未审核通过，不能进行此操作");
			return map;
		}

		long userId = SecurityContextUtils.getUserId();

		couponDTO.setAuditState(StateConstants.CANCEL);
		couponDTO.setAuditUserId(userId);
		couponDTO.setAuditTime(System.currentTimeMillis());

		boolean status = couponFacade.discardCoupon(couponDTO);
		if (status) {
			map.put("code", ResponseCode.RES_SUCCESS);
			map.put("message", "撤销优惠券审核通过成功");
		} else {
			map.put("code", ResponseCode.RES_ENOTEXIST);
			map.put("message", "撤销优惠券审核通过失败");
		}

		return map;
	}
	
	/**
	 * 优惠券下载
	 */
	@RequestMapping("/coupon/exportCode")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		long id = ServletRequestUtils.getLongParameter(request, "id", 0);
		
		CouponDTO couponDTO = couponFacade.getCouponById(id, false);
		if (couponDTO == null) {
			logger.info("Not found! Id : " + id + ".");
			return;
		}

		if (checkPromission(couponDTO) == null) {
			logger.info("No permission to export! Id : " + id + ".");
			return;
		}
		
		List<Coupon> coupons = couponFacade.getRamdomCoupons(couponDTO.getCouponCode());

		if (CollectionUtils.isEmpty(coupons)) {
			return;
		}

		File f = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			f = writeToFile(coupons);
			if (f == null) {
				return;
			}
			
			is = new FileInputStream(f);
			os = response.getOutputStream();
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.addHeader("Content-Disposition", "attachment;filename=Coupon_" + f.getName());
			
			IOUtils.copy(is, os);
			response.flushBuffer();
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				FileUtils.forceDelete(f);
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@RequestMapping(value = "/promotion/config", method = RequestMethod.GET)
	@RequiresPermissions("promotion:couponconfig")
	public String couponConfig(Model model) {
		model.addAttribute("pages", 
				leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/promotion/config";
	}
	
	@RequestMapping(value = "/promotion/couponConfig/getConfigByType", method = RequestMethod.GET)
	@RequiresPermissions("promotion:couponconfig")
	public @ResponseBody BaseJsonVO getCouponConfigByType(
			@RequestParam(value = "siteId", required = true) long siteId,
			@RequestParam(value = "couponConfigType", required = true) int type) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!checkSitePromission(siteId)) {
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "管理员身份与站点不匹配！");
			return ret;
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(couponFacade.getCouponConfigByType(siteId, type));
		return ret;
	}
	
	@RequestMapping(value = "/promotion/couponConfig/preview", method = RequestMethod.GET)
	@RequiresPermissions("promotion:couponconfig")
	public @ResponseBody BaseJsonVO previewCouponConfig(
			@RequestParam(value = "couponCodes", required = true) String couponCodes) {
		BaseJsonVO ret = new BaseJsonVO();
		// 校验优惠券合法性
		String result = checkCouponCodes(couponCodes);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, result);
			return ret;
		}
		List<CouponDTO> couponList = new ArrayList<CouponDTO>();
		Map<String, String> resultMap = couponFacade.getUseFulCouponList(couponCodes, couponList);
		if (!CollectionUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(couponList)) {
			ret.setCode(ResponseCode.RES_EPARAM);
			ret.setResult(resultMap);
		} else {
			for (CouponDTO couponDTO : couponList) {
				if (checkPromission(couponDTO) == null) {
					resultMap.put(couponDTO.getCouponCode(), "优惠券 " + couponDTO.getCouponCode() + " 区域不符合");
				}
			}
			if (!CollectionUtils.isEmpty(resultMap)) {
				ret.setCode(ResponseCode.RES_EPARAM);
				ret.setResult(resultMap);
				return ret;
			}
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(couponList);
		}
		return ret;
	}

	@RequestMapping(value = "/promotion/couponConfig/saveConfig", method = RequestMethod.POST)
	@RequiresPermissions("promotion:couponconfig")
	public @ResponseBody BaseJsonVO saveCouponConfig(@RequestBody CouponConfigVO couponConfigVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (couponConfigVO.getSiteId() < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "站点不能为空！");
			return ret;
		}
		if (!checkSitePromission(couponConfigVO.getSiteId())) {
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "管理员身份与站点不匹配！");
			return ret;
		}
		if (couponConfigVO.getCouponConfigType() < 1) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "优惠券配置类型不能为空！");
			return ret;
		}
		CouponConfigDTO couponConfigDTO = null;
		
		// 如果是关闭配置，不校验其他参数
		if (couponConfigVO.getIsValid() != 1) {
			couponConfigVO = couponFacade.getCouponConfigByType(
					couponConfigVO.getSiteId(), couponConfigVO.getCouponConfigType());
			if (couponConfigVO.getCouponConfigId() < 1l) {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "更新优惠券配置成功！");
				return ret;
			}
			couponConfigDTO = couponConfigVO.convertToDTO();
			couponConfigDTO.setValidFlag(0);
		} else {
			// 校验优惠券类型
			if (CouponConfigType.NULL.genEnumByIntValue(couponConfigVO.getCouponConfigType()).getIntValue() 
					!= couponConfigVO.getCouponConfigType()) {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "优惠券配置类型错误！");
				return ret;
			}
			// 校验优惠券合法性
			String result = checkCouponCodes(couponConfigVO.getCouponCodes());
			if (result != null) {
				ret.setCodeAndMessage(ResponseCode.RES_ERROR, result);
				return ret;
			}
			List<CouponDTO> couponList = new ArrayList<CouponDTO>();
			Map<String, String> resultMap = 
					couponFacade.getUseFulCouponList(couponConfigVO.getCouponCodes(), couponList);
			if (!CollectionUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(couponList)) {
				ret.setCode(ResponseCode.RES_EPARAM);
				ret.setResult(resultMap);
				return ret;
			}
			for (CouponDTO couponDTO : couponList) {
				if (checkPromission(couponDTO) == null) {
					resultMap.put(couponDTO.getCouponCode(), "优惠券 " + couponDTO.getCouponCode() + " 区域不符合");
				}
			}
			if (!CollectionUtils.isEmpty(resultMap)) {
				ret.setCode(ResponseCode.RES_EPARAM);
				ret.setResult(resultMap);
				return ret;
			}
			couponConfigDTO = couponConfigVO.convertToDTO();
		}
		
		int res = 0;
		if (couponConfigDTO.getId() > 0l) {
			res = couponFacade.updateCouponConfig(couponConfigDTO);
		} else {
			res = couponFacade.addCouponConfig(couponConfigDTO);
		}
		if (res > 0) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "更新优惠券配置成功！");
			logger.info("Save coupon config successful! SiteId : {}, type : {}, AgentId : {}",
					couponConfigDTO.getSiteId(), couponConfigDTO.getType(), SecurityContextUtils.getUserId());
			return ret;
		} else if (res == -1) {
			ret.setCodeAndMessage(ResponseCode.RES_EEXIST, "优惠券配置类型已存在请刷新页面！");
			return ret;
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "更新优惠券配置失败！");
			return ret;
		}
	}
	
	private File writeToFile(List<Coupon> coupons) throws FileNotFoundException, IOException {
		File f = new File(tmpFileDir);

		if (!f.exists()) {
			f.mkdirs();
		}

		File file = new File(tmpFileDir + File.separator + System.currentTimeMillis() + ".txt");

		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
			for (Coupon coupon : coupons) {
				osw.append(coupon.getCouponCode()).append("\r\n");
			}

			osw.flush();
			return file;
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private CouponVO checkPromission(CouponDTO couponDTO) {
		if (couponDTO == null || StringUtils.isBlank(couponDTO.getAreaIds())) {
			return null;
		} else {
			// 获取区域权限
			RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(SecurityContextUtils.getUserId());
			if (retArg == null) {
				return null;
			} else {
				// 是否为root
				if (RetArgUtil.get(retArg, Boolean.class)) {
					return new CouponVO(couponDTO);
				} else {
					// 拆分区域
					String[] areaIds = couponDTO.getAreaIds().split(",");
					List<Long> areaList = new ArrayList<Long>(areaIds.length);
					for (String area : areaIds) {
						areaList.add(Long.parseLong(area));
					}
					// 包含任意区域
					if (CollectionUtils.containsAny(RetArgUtil.get(retArg, HashSet.class), areaList)) {
						return new CouponVO(couponDTO);
					} else {
						return null;
					}
				}
			}
		}
	}
	
	private boolean checkSitePromission(long siteId) {
		long userId = SecurityContextUtils.getUserId();
		AgentDTO agent = agentService.findAgentById(userId);
		if (agent.getAgentType() == AgentType.ROOT) {
			return true;
		} else {
			List<Long> siteIdList = agentService.findAgentSiteIdsByPermission(userId, "location:site");
			if (CollectionUtil.isEmptyOfList(siteIdList) || !siteIdList.contains(siteId)) {
				return false;
			}
			return true;
		}
	}
	
	private String checkCouponCodes(String couponCodes) {
		if (StringUtils.isBlank(couponCodes)) {
			return "优惠券号不能为空！";
		}
		String[] codeArray = couponCodes.split(",");
		if (codeArray.length <= 0) {
			return "优惠券号不能为空！";
		}
		if (codeArray.length > 10) {
			return "最多可以填10个优惠券！";
		}
		for (String code : codeArray) {
			if (StringUtils.isBlank(code)) {
				return "优惠券号不能为空！";
			}
		}
		return null;
	}
}
