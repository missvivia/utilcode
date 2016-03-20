/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.common.facade.VerificationCodeServiceFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;

/**
 * 首页相关。
 * 
 * @author lihui
 * 
 */
@Controller
@RequestMapping("")
public class IndexController {

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private PromotionContentFacade promotionContentFacade;

	@Autowired
	private VerificationCodeServiceFacade verificationCodeServiceFacade;
	
	@Autowired
	private AgentService agentService;
	

	@RequestMapping(value = { "/", "/index", "/index/home" }, method = RequestMethod.GET)
	public String homeNew(Model model) {
		model.addAttribute("username", SecurityContextUtils.getUserName());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/indexNew";
	}
	
	/**
	 * 跳转到首页。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/index/status" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "index:status" })
	public String home(Model model) {
		model.addAttribute("username", SecurityContextUtils.getUserName());

		Subject subject = SecurityUtils.getSubject();

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.CHECKING);
		paramDTO.poStatusList = poStatusList;
		JSONArray pendingPOList = null;
		if (subject.isPermitted("schedule:audit")) {
			pendingPOList = scheduleFacade.getCheckingPOSelfData(paramDTO);
		}

		paramDTO = new ScheduleCommonParamDTO();
		poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.PASSED);
		paramDTO.poStatusList = poStatusList;
		JSONArray pendingAuditList = null;
		if (subject.isPermitted("audit:productlist") || subject.isPermitted("audit:product")
				|| subject.isPermitted("audit:banner") || subject.isPermitted("audit:decorate")
				|| subject.isPermitted("audit:brand")) {
			pendingAuditList = scheduleFacade.getCheckingPOOthersData(paramDTO);
		}

		paramDTO = new ScheduleCommonParamDTO();
		JSONObject promotionAuditList = null;
		if (subject.isPermitted("promotion:activity")) {
			promotionAuditList = scheduleFacade.getPOPromotionData(paramDTO);
		}

		paramDTO = new ScheduleCommonParamDTO();
		JSONArray orderAuditList = null;
		if (subject.isPermitted("order:topay") || subject.isPermitted("order:return")) {
			orderAuditList = scheduleFacade.getCheckingOrderData(paramDTO);
		}

		// webEmptyList = [{"date":1414510876264, "siteList":["Jiang Su",
		// "Zhe Jiang"]}, {"date":1414510876264, "siteList":["Jiang Su",
		// "Zhe Jiang"]}]
		JSONArray webEmptyList = null;
		JSONArray IOSEmptyList = null;// new JSONArray(); // TODO
		JSONArray androidEmptyList = null;// new JSONArray(); // TODO
		if (subject.isPermitted("content:spread")) {
			webEmptyList = new JSONArray();
			IOSEmptyList = new JSONArray(); 
			androidEmptyList = new JSONArray(); 

			// 获取未来三天po当其空闲的站点名称
//			List<PCAvaliableDTO> webList = null;
//			try {
//				webList = promotionContentFacade.pcAvaliable3Days();
//			} catch (Exception e) {
//				//
//			}
//			if (webList != null) {
//				for (PCAvaliableDTO web : webList) {
//					JSONObject item = new JSONObject();
//					item.put("date", web.getCurrDate());
//					List<Area> areaList = web.getAratList();
//					JSONArray arrItem = new JSONArray();
//					if (areaList != null) {
//						for (Area areaBean : areaList) {
//							arrItem.add(areaBean.getAreaName());
//						}
//					}
//					item.put("siteList", arrItem);
//
//					webEmptyList.add(item);
//				}
//			}
		}

		model.addAttribute("jobNum", 10);
		model.addAttribute("webEmptyList", webEmptyList);
		model.addAttribute("IOSEmptyList", IOSEmptyList);
		model.addAttribute("androidEmpeyList", androidEmptyList);
		model.addAttribute("pendingPOList", pendingPOList);
		model.addAttribute("pendingAuditList", pendingAuditList);
		model.addAttribute("promotionAudit", promotionAuditList);
		model.addAttribute("orderAuditList", orderAuditList);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/index";
	}
	
	/* 首页绑定手机和邮箱
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/index/remind" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "index:remind" })
	public String reminder(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		String username = SecurityContextUtils.getUserName();
		AgentDTO agentDTO = agentService.findAgentByName(username);
		if(agentDTO!=null){
			model.addAttribute("phone",agentDTO.getMobile());
			model.addAttribute("email",agentDTO.getEmail());
		}else{
			model.addAttribute("phone","");
			model.addAttribute("email","");		
		}
		return "pages/remind";
	}
	
	
	/**
	 * 异步发送单个手机验证码
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/index/remind/getVerifCode", method = RequestMethod.POST)
	@RequiresPermissions(value = { "index:remind" })
	public @ResponseBody BaseJsonVO getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String jsonString;
		jsonString = getJson(request.getInputStream());
		JSONObject json = JSON.parseObject(jsonString);
		String phone = json.getString("phone");
		BaseJsonVO result = new BaseJsonVO();
		if (StringUtils.isNotBlank(phone) && phone.length() == 11 && phone.startsWith("1")) {
			VerificationCode verificationCode = verificationCodeServiceFacade.sendVerificationCodeOfSms(phone, 10);
			result.setCode(verificationCode == null ? 201 : ErrorCode.SUCCESS.getIntValue());
			result.setMessage(verificationCode == null ? "验证码发送失败！" : "验证码发送成功！");
			result.setResult(verificationCode);
		} else {
			result.setCode(201);
			result.setMessage("手机号码错误！");
		}
		return result;
	}
	
	
	/**
	 * 保存绑定的手机号码
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value ="/index/remind/savePhonenum",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ajaxSavePhonenum(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String jsonString;
		jsonString = getJson(request.getInputStream());
		JSONObject json = JSON.parseObject(jsonString);
		String credential = json.getString("credential");
		String sign = json.getString("sign");
		String expiredTime = json.getString("expiredTime");
		String yzm = json.getString("yzm");
		String newphone = json.getString("newphone");
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setCredential(credential);
		verificationCode.setSign(sign);
		if(expiredTime!=null && !"".equals(expiredTime))
			verificationCode.setExpiredTime(Long.valueOf(expiredTime));
		String username = SecurityContextUtils.getUserName();
		AgentDTO agentDTO = agentService.findAgentByName(username);
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		if (yzm!=null && StringUtils.isNumeric(yzm)) {
			VCodeResult codeResult = verificationCodeServiceFacade.validateVerificationCode(verificationCode,
					Integer.parseInt(yzm));
			if (VCodeResult.RETCODE_SUCCESS == codeResult.getRetCode()) {
				try{
					agentDTO.setMobile(newphone);
					//更新
					agentService.upsertAgent(agentDTO, agentDTO.getId());
					map.put("code", "200");
					result.put("msg", "保存成功");
					map.put("result", result);
				}catch(Exception e){
					map.put("code", "201");
					result.put("msg","保持失败");
					map.put("result", result);
				}
				return map;
			}
		}
		map.put("code", "201");
		result.put("msg", "验证码错误");
		map.put("result", result);
		return map;

	}
	
	/**
	 * 保存绑定的邮箱
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value ="/index/remind/saveEmail",method = RequestMethod.POST)
	@RequiresPermissions(value = { "index:phone" })
	@ResponseBody
	public Map<String, Object> ajaxSaveEmail(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		String jsonString;
		jsonString = getJson(request.getInputStream());
		JSONObject json = JSON.parseObject(jsonString);
		String email = json.getString("email");
		String username = SecurityContextUtils.getUserName();
		AgentDTO agentDTO = agentService.findAgentByName(username);
		agentDTO.setEmail(email);
		AgentDTO obj = agentService.upsertAgent(agentDTO, agentDTO.getId());
		if(obj!=null){
			map.put("code", "200");
			result.put("msg", email+"保存成功");
			map.put("result", result);
		}else{
			map.put("code", "201");
			result.put("msg", "验证码错误");
			map.put("result", result);
		}
		return map;

	}
	
	
	/**
	 * 从InputStream获取json字符串
	 * @param input
	 * @return
	 */
	private String getJson(InputStream input){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		try {
			while((line=reader.readLine())!=null){
				sb.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
