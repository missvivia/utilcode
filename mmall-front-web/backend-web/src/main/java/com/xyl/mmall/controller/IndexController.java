/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.VerificationCodeServiceFacade;
import com.xyl.mmall.config.ResourceTextUtil;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.oms.dto.BusinessPhoneDTO;
import com.xyl.mmall.oms.meta.BusinessPhoneForm;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;

/**
 * 首页相关
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping("")
public class IndexController extends BaseController {

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private ItemCenterFacade itemCenterFacade;
	
	@Autowired
	private ScheduleFacade scheduleFacade;
	
	@Autowired
	private JITSupplyManagerFacade jitManagerFacade;

	@Autowired
	private VerificationCodeServiceFacade verificationCodeServiceFacade;
	
	@Autowired
	private DealerService dealerService;
	
	@Autowired
	private BusinessService businessService;
	
	private static final ResourceBundle rs = ResourceTextUtil.getResourceBundleByName("config.application");
	

	@RequestMapping(value = { "/", "/index", "/index/home" }, method = RequestMethod.GET)
	public String homeNew(Model model) {
		model.addAttribute("username", SecurityContextUtils.getUserName());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/indexNew";
	}
	
	/**
	 * 跳转到商家后台管理系统首页。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/index/status" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "index:status" })
	public String home(Model model) {
		model.addAttribute("username", SecurityContextUtils.getUserName());
		
		long userId = ScheduleUtil.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(userId);
		Business supplier = businessFacade.getBusinessById(supplierId);
		
		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.supplierId = supplier.getId();
		paramDTO.curSupplierAreaId = supplier.getAreaId();
		List<ScheduleState> poStatusList = new ArrayList<ScheduleState>();
		poStatusList.add(ScheduleState.PASSED);
		poStatusList.add(ScheduleState.BACKEND_PASSED);
		poStatusList.add(ScheduleState.OFFLINE);
		paramDTO.endDate = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(paramDTO.endDate);
		c.add(Calendar.MONTH, -3);
		paramDTO.startDate = c.getTimeInMillis();
		paramDTO.poStatusList = poStatusList;
		paramDTO.createWhereFlag = true;
		
		JSONArray onlineScheduleList = scheduleFacade.getOnlinePOData(paramDTO);
		
		int receipt = scheduleFacade.getInvoiceInOrdSupplierCountOfInit(supplierId);
		
		JSONArray unlineScheduleList = scheduleFacade.getOfflinePOData(paramDTO);

		paramDTO = new ScheduleCommonParamDTO();
		paramDTO.supplierId = supplier.getId();
		paramDTO.curSupplierAreaId = supplier.getAreaId();
		JSONObject summary = scheduleFacade.getStatisticData(paramDTO, 
				ResourceTextUtil.getTextFromResourceByKey(rs, "dealer.data.url"));

		model.addAttribute("onlineScheduleList", onlineScheduleList);
		model.addAttribute("unlineScheduleList", unlineScheduleList);
		model.addAttribute("receipt", receipt);
		model.addAttribute("summary", summary);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/index";
	}
	
	/**
	 * 手机号码绑定页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/index/phones" })
	@RequiresPermissions(value = { "index:phones" })
	public String bindPhones(Model model) {
		appendStaticMethod(model);
		//String businessAccount = SecurityContextUtils.getUserName();
		
		long userId = SecurityContextUtils.getUserId();
		DealerDTO dealer = dealerService.findDealerById(userId);
		List<BusinessPhoneDTO> businessPhoneList = jitManagerFacade.getBusinessPhoneFormByBusinessAccount(dealer.getName());
		Business business = businessService.getBusinessById(dealer.getSupplierId(), -1);
		List<BusinessPhoneDTO> phones = enrich(business,businessPhoneList);
		List<BusinessPhoneLogForm> businessPhoneLogList = jitManagerFacade.getBusinessPhoneLogForm(dealer.getName());
		model.addAttribute("phones", phones);
		model.addAttribute("log", businessPhoneLogList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/phones";
	}
	
	/**
	 * 每个商家可以绑定三个联系人，方便前端显示，为空也返回size=3的list数据
	 * @param businessPhoneList
	 */
	private List<BusinessPhoneDTO> enrich(Business business,List<BusinessPhoneDTO> businessPhoneList) {
		int size = 0;
		List<BusinessPhoneDTO> result = new ArrayList<BusinessPhoneDTO>();
		if(businessPhoneList!=null)
			size=businessPhoneList.size();
		if(businessPhoneList==null || businessPhoneList.size()==0)
			businessPhoneList = new ArrayList<BusinessPhoneDTO>();
		else{
			result.addAll(businessPhoneList);
		}
		for(int i=size;i<3;i++){
			BusinessPhoneDTO businessDTO = new BusinessPhoneDTO();
			businessDTO.setOrderId(i+1);
			businessDTO.setBusinessAccount(business.getBusinessAccount());
			businessDTO.setSupplierName(business.getCompanyName());
			businessPhoneList.add(businessDTO);
		}
		return businessPhoneList;
	}
	
	/**
	 * 异步发送单个手机验证码
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/index/phones/getVerifCode", method = RequestMethod.POST)
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
	@RequestMapping(value ="/index/phones/savePhonenum",method = RequestMethod.POST)
	@RequiresPermissions(value = { "index:phones" })
	@ResponseBody
	public Map<String, Object> ajaxSavePhonenum(HttpServletRequest request, HttpServletResponse response
			) throws IOException  {
		String jsonString;
		jsonString = getJson(request.getInputStream());
		JSONObject json = JSON.parseObject(jsonString);
		String newphone = json.getString("newphone");
		String oldphone = json.getString("oldphone");
		String orderId= json.getString("orderId");
		String code= json.getString("yzm");
		String credential= json.getString("credential");
		String expiredTime = json.getString("expiredTime");
		String sign= json.getString("sign");

		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setCredential(credential);
		verificationCode.setSign(sign);
		if(expiredTime!=null && !"".equals(expiredTime))
			verificationCode.setExpiredTime(Long.valueOf(expiredTime));
		String businessAccount = SecurityContextUtils.getUserName();
		// TODO:从session取帐号
		//businessAccount = "huangqqai@126.com";
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		if (code!=null && StringUtils.isNumeric(code)) {
			VCodeResult codeResult = verificationCodeServiceFacade.validateVerificationCode(verificationCode,
					Integer.parseInt(code));
			if (VCodeResult.RETCODE_SUCCESS == codeResult.getRetCode()) {
				Date curr = new Date();
				BusinessPhoneLogForm bplf = new BusinessPhoneLogForm();
				bplf.setBusinessAccount(businessAccount);
				bplf.setCreateTime(curr.getTime());
				bplf.setNewphone(newphone);
				bplf.setOldphone(oldphone);
				bplf.setOrderId(Integer.valueOf(orderId));
				try{
					BusinessPhoneForm bpf = new BusinessPhoneForm();
		 			bpf.setBusinessAccount(businessAccount);
					if(oldphone==null || "".equals(oldphone.trim())){
						bpf.setCreateTime(curr.getTime());
						bpf.setModifyTime(curr.getTime());
						bpf.setPhone(newphone);
						bpf.setOrderId(Integer.valueOf(orderId));
						BusinessPhoneForm exist = jitManagerFacade.getBusinessPhone(businessAccount, newphone);
						if(exist!=null){
							map.put("code", "201");
							result.put("msg","该手机号已存在");
							map.put("result", result);
							return map;
						}
						jitManagerFacade.addBusinessPhoneForm(bpf);
					}else{
						bpf.setModifyTime(curr.getTime());
						bpf.setPhone(newphone);
						bpf.setOrderId(Integer.valueOf(orderId));		
						BusinessPhoneForm exist = jitManagerFacade.getBusinessPhone(businessAccount, newphone);
						if(exist.getOrderId()!=Integer.valueOf(orderId)){
							map.put("code", "201");
							result.put("msg","该手机号已存在");
							map.put("result", result);
							return map;
						}
						jitManagerFacade.updateBusinessPhoneForm(bpf);
					}
					jitManagerFacade.addBusinessPhoneLogForm(bplf);
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
