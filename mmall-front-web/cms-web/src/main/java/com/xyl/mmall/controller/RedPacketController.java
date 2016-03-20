/*
 * 2014-9-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xyl.mmall.backend.facade.promotion.RedPacketFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.DistributeRule;
import com.xyl.mmall.vo.RedPacketVO;

/**
 * RedPacketController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-19
 * @since 1.0
 */

@Controller
public class RedPacketController extends BaseController {
	private static Logger logger = Logger.getLogger(PromotionController.class);

	@Autowired
	private RedPacketFacade redPacketFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	/**
	 * 获取红包列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/promotion/packet")
	@RequiresPermissions(value = { "promotion:packet" })
	public String list(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 0);
		return "pages/promotion/packet";
	}

	/**
	 * 获取红包列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/promotion/packetaudit")
	@RequiresPermissions(value = { "promotion:packetaudit" })
	public String packetAudit(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 1);
		return "pages/promotion/packet";
	}

	@RequestMapping("/packet/listData")
	public void listData(@RequestParam(value = "state", defaultValue = "-1") int state,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "apply", defaultValue = "1") int apply,
			@RequestParam(value = "qvalue", defaultValue = "") String qvalue, Model model) {
		long userId = SecurityContextUtils.getUserId();
		List<RedPacketDTO> redPacketDTOs = null;
		int total = 0;
		qvalue = StringUtils.trim(qvalue);
		if (apply == 1) {
			redPacketDTOs = redPacketFacade.getRedPacketList(userId, state, qvalue, limit, offset);
			total = redPacketFacade.getRedPacketCount(userId, state, qvalue);
		} else {
			Subject subject = SecurityUtils.getSubject();
			if (!subject.isPermitted("promotion:packetaudit")) {
				return;
			}
			redPacketDTOs = redPacketFacade.getRedPacketList(-1, state, qvalue, limit, offset);
			total = redPacketFacade.getRedPacketCount(-1, state, qvalue);
		}

		List<RedPacketVO> list = new ArrayList<>();

		if (CollectionUtils.isEmpty(redPacketDTOs)) {
			model.addAttribute("list", list);
			model.addAttribute("total", total);
			model.addAttribute("code", 200);
			return;
		}

		for (RedPacketDTO redPacketDTO : redPacketDTOs) {
			RedPacketVO vo = new RedPacketVO(redPacketDTO);
			vo.setApplyUserName(redPacketDTO.getApplyUserName());
			vo.setAuditUserName(redPacketDTO.getAuditUserName());
			list.add(vo);
		}

		model.addAttribute("list", list);
		model.addAttribute("total", total);
		model.addAttribute("code", 200);
	}

	/**
	 * 查看红包的具体详情
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/promotion/packetEdit")
	@RequiresPermissions(value = { "promotion:packetEdit", "promotion:packet", "promotion:packetaudit" })
	public String view(@RequestParam(value = "id", defaultValue = "-1") long id,
			@RequestParam(value = "editable", defaultValue = "1") int editable, Model model) {
		appendStaticMethod(model);
		RedPacketDTO redPacketDTO = redPacketFacade.getRedPacketById(id);
		RedPacketVO redPacketVO = null;
		if (redPacketDTO != null) {
			redPacketVO = new RedPacketVO(redPacketDTO);
		}

		model.addAttribute("redPacketVO", redPacketVO != null ? redPacketVO.cloneObject() : null);
		model.addAttribute("editable", editable);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/promotion/packetEdit";
	}

	/**
	 * 修改促销信息（新增或者修改）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/packet/save")
	@RequiresPermissions(value = { "promotion:packetEdit" })
	public Map<String, Object> save(@RequestBody RedPacketVO redPacketVO) throws Exception {
		Map<String, Object> map = new HashMap<>();

		if (redPacketVO == null) {
			map.put("code", 404);
			map.put("message", "对象为空");
			return map;
		}

		if (StringUtils.isBlank(redPacketVO.getName())) {
			map.put("code", 404);
			map.put("message", "名称不能为空");
			return map;
		}

		if (StringUtils.isBlank(redPacketVO.getDescription())) {
			map.put("code", 404);
			map.put("message", "描述不能为空");
			return map;
		}

		// 是否分享
		if (redPacketVO.isShare()) {
			// 判断是否能除尽
			if (redPacketVO.getDistributeRule() == DistributeRule.EQUALLY) {
				double c = redPacketVO.getCash().divide(new BigDecimal(redPacketVO.getCopies()), MathContext.DECIMAL32)
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double total = c * redPacketVO.getCopies();
				if (total != redPacketVO.getCash().doubleValue()) {
					map.put("code", 404);
					map.put("message", "红包金额不可除尽，请重设红包金额或裂变份数");
					return map;
				}
			}
		}

		RedPacketDTO redPacketDTO = redPacketFacade.getRedPacketById(redPacketVO.getId());

		boolean isAdd = false;
		if (redPacketDTO == null) {
			redPacketDTO = new RedPacketDTO();
			isAdd = true;
		}

		redPacketDTO.setName(redPacketVO.getName());
		redPacketDTO.setDescription(redPacketVO.getDescription());
		redPacketDTO.setStartTime(redPacketVO.getStartTime());
		redPacketDTO.setEndTime(redPacketVO.getEndTime());
		redPacketDTO.setAuditState(redPacketVO.getAuditState());
		if (redPacketVO.getBinderType() == null) {
			redPacketDTO.setBinderType(BinderType.NULL);
		}
		redPacketDTO.setBinderType(redPacketVO.getBinderType());
		redPacketDTO.setCash(redPacketVO.getCash());
		if (redPacketVO.isShare()) {
			redPacketDTO.setDistributeRule(redPacketVO.getDistributeRule());
			//重新设置数量
			redPacketDTO.setCopies(redPacketVO.getCopies());
			redPacketDTO.setCount(redPacketVO.getCount());
			redPacketDTO.setValidDay(redPacketVO.getValidDay());
		} else {
			redPacketDTO.setDistributeRule(DistributeRule.NULL);
		}
		if (StringUtils.isBlank(redPacketVO.getUsers())) {
			redPacketDTO.setUsers("");
		} else {
			redPacketDTO.setUsers(redPacketVO.getUsers());
		}
		redPacketDTO.setUsed(redPacketVO.isUsed());
		redPacketDTO.setShare(redPacketVO.isShare());
		redPacketDTO.setProduce(redPacketVO.isProduce());
		redPacketDTO.setPlatform(redPacketVO.getPlatform());

		if (redPacketVO.getStartTime() >= redPacketVO.getEndTime()) {
			logger.error("start time bigger than end time.");
			map.put("code", 404);
			map.put("message", "开始时间不能大于结束时间");
		}

		long userId = SecurityContextUtils.getUserId();

		redPacketDTO.setApplyUserId(userId);

		boolean status = false;
		if (isAdd) {
			status = redPacketFacade.addRedPacket(redPacketDTO) != null;
		} else {
			status = redPacketFacade.updateRedPacket(redPacketDTO);
		}

		if (status) {
			map.put("code", 200);
			map.put("message", "保存成功");
		}

		return map;
	}

	/**
	 * 删除
	 */
	@RequestMapping("/packet/delete")
	@RequiresPermissions(value = { "promotion:packet" })
	public Map<String, Object> delete(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		RedPacketDTO redPacketDTO = redPacketFacade.getRedPacketById(id);

		if (redPacketDTO == null) {
			map.put("code", 404);
			map.put("message", "此红包不存在");
			return map;
		}

		redPacketDTO.setAuditState(StateConstants.CANCEL);

		boolean status = redPacketFacade.updateRedPacket(redPacketDTO);
		if (status) {
			map.put("code", 200);
			map.put("message", "红包删除成功");
		} else {
			map.put("code", 404);
			map.put("message", "红包删除失败");
		}

		return map;
	}

	/**
	 * 提交审核
	 */
	@RequestMapping("/packet/auditcommit")
	@RequiresPermissions(value = { "promotion:packet" })
	public Map<String, Object> auditCommit(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		RedPacketDTO redPacketDTO = redPacketFacade.getRedPacketById(id);

		if (redPacketDTO == null) {
			map.put("code", 404);
			map.put("message", "此红包不存在");
			return map;
		}

		int state = redPacketDTO.getAuditState();

		if (StateConstants.COMMIT == state) {
			map.put("code", 404);
			map.put("message", "此红包已提交审核");
			return map;
		}

		if (StateConstants.PASS == state) {
			map.put("code", 404);
			map.put("message", "此红包已通过审核");
			return map;
		}

		if (StateConstants.CANCEL == state) {
			map.put("code", 404);
			map.put("message", "此红包已被删除");
			return map;
		}

		redPacketDTO.setAuditState(StateConstants.COMMIT);

		boolean status = redPacketFacade.updateRedPacket(redPacketDTO);
		if (status) {
			map.put("code", 200);
			map.put("message", "红包提交审核成功");
		} else {
			map.put("code", 404);
			map.put("message", "红包提交审核失败");
		}

		return map;
	}

	/**
	 * 审核
	 */
	@RequestMapping("/packet/audit")
	@RequiresPermissions(value = { "promotion:packetaudit" })
	public Map<String, Object> audit(@RequestParam(value = "id", defaultValue = "0") long id,
			@RequestParam(value = "auditValue", defaultValue = "0") int auditValue) {
		Map<String, Object> map = new HashMap<>();

		if (auditValue != StateConstants.PASS && auditValue != StateConstants.REFUSED) {
			map.put("code", 404);
			map.put("message", "审核状态不符合要求");
			return map;
		}

		RedPacketDTO redPacketDTO = redPacketFacade.getRedPacketById(id);

		if (redPacketDTO == null) {
			map.put("code", 404);
			map.put("message", "此红包不存在");
			return map;
		}

		// 只有审核中的才能审核
		if (redPacketDTO.getAuditState() != StateConstants.COMMIT) {
			map.put("code", 404);
			map.put("message", "红包不在审核中，不能进行此操作");
			return map;
		}

		long userId = SecurityContextUtils.getUserId();

		redPacketDTO.setAuditState(auditValue);
		redPacketDTO.setAuditUserId(userId);
		redPacketDTO.setAuditTime(System.currentTimeMillis());

		boolean status = redPacketFacade.updateRedPacket(redPacketDTO);
		if (status) {
			map.put("code", 200);
			map.put("message", "红包审核成功");
		} else {
			map.put("code", 404);
			map.put("message", "活动审核失败");
		}

		return map;
	}

	/**
	 * 撤销
	 */
	@RequestMapping("/packet/discard")
	@RequiresPermissions(value = { "promotion:packetaudit" })
	public Map<String, Object> discard(@RequestParam(value = "id", defaultValue = "0") long id) {
		Map<String, Object> map = new HashMap<>();

		RedPacketDTO redPacketDTO = redPacketFacade.getRedPacketById(id);

		if (redPacketDTO == null) {
			map.put("code", 404);
			map.put("message", "此红包不存在");
			return map;
		}

		// 只有审核中的才能审核
		if (redPacketDTO.getAuditState() != StateConstants.PASS) {
			map.put("code", 404);
			map.put("message", "红包未审核通过，不能进行此操作");
			return map;
		}

		long userId = SecurityContextUtils.getUserId();

		redPacketDTO.setAuditState(StateConstants.COMMIT);
		redPacketDTO.setAuditUserId(userId);
		redPacketDTO.setAuditTime(System.currentTimeMillis());

		boolean status = redPacketFacade.discardRedPacket(redPacketDTO);
		if (status) {
			map.put("code", 200);
			map.put("message", "撤销红包审核成功");
		} else {
			map.put("code", 404);
			map.put("message", "撤销活动审核失败");
		}

		return map;
	}
}
