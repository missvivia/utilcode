/**
 * 
 */
package com.xyl.mmall.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class BrandController extends BaseController {

	@Resource
	MainBrandFacade mainBrandFacade;

	@Resource
	BusinessFacade businessFacade;
	
	@Resource
	private Producer captchaProducer;

	@BILog(action = "page", type = "brandDetailPage", clientType="wap")
	@RequestMapping(value = "/mainbrand/story", method = RequestMethod.GET)
	public String getBrandStory(Model model, @RequestParam long id,
			HttpServletRequest request, HttpServletResponse response) {
		appendStaticMethod(model);
		long userId = SecurityContextUtils.getUserId();
		// 接受的id是 brandId
		long areaId = AreaUtils.getAreaCode();
		JSONObject jsonObject = new JSONObject();
		if (areaId > 0) {
			// 主站品牌mini页面由于品牌的逻辑的变化，这里要对应的修改
			// 优先选择显示一个站点下面的代理商，其次是品牌商
			// type = 1 是代理商 type = 2 是品牌商
			SupplierBrandFullDTO brandDto = null;
			BusinessDTO targetDTO = businessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 1);
			if (targetDTO != null) {
				long supplierId = targetDTO.getId();
				brandDto = mainBrandFacade.getBrandFullDTOBySupplierId(supplierId, userId, true);
			}
			if (targetDTO == null || brandDto == null) {
				targetDTO = businessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 2);
				if (targetDTO != null) {
					long supplierId = targetDTO.getId();
					brandDto = mainBrandFacade.getBrandFullDTOBySupplierId(supplierId, userId, true);
				}
			}
			// Business business = businessFacade.getBusinessByAreaIdAndBrandId(areaId, id);
			if (targetDTO != null && targetDTO.getIsActive() == 0) {
				if (brandDto != null) {
					jsonObject.put("brandInfo", brandDto);
					POListDTO curPoListDTO = mainBrandFacade.getPOList(id, areaId);
//					UserLoginBean userLoginBean = QrqmUtils.getCurIPLastLoginTime(request, response);
//					curPoListDTO = POBaseUtil.filterForEveryOne(userLoginBean, curPoListDTO);
					jsonObject.put("polist", changePOListIntoJSONData(curPoListDTO, userId));
					jsonObject.put("nextpolist", changePOListIntoJSONData(mainBrandFacade.getPOListFuture(id, areaId, 4), userId));
					jsonObject.put("businessInfo", getBusinessLawInfo(targetDTO));
					model.addAttribute("data", jsonObject);
					return "pages/brand/story";
				} 
			}
		}
		return "pages/404";
	}
	
	// 获取供应商法人信息
	private JSONObject getBusinessLawInfo(BusinessDTO dto) {
		JSONObject jsonObject = new JSONObject();
		if (dto != null) {
			String address = dto.getContactProvince() + dto.getContactCity() +
					dto.getContactCountry() + dto.getContactAddress();
			jsonObject.put("companyName", dto.getCompanyName());
			jsonObject.put("legalPerson", dto.getLegalPerson());
			jsonObject.put("contactAddress", address);
			jsonObject.put("registrationNumber", dto.getRegistrationNumber());
			jsonObject.put("registrationNumberStart", dto.getRegistrationNumberStart());
			jsonObject.put("registrationNumberEnd", dto.getRegistrationNumberEnd());
			jsonObject.put("brandAuthImg", dto.getBrandAuthImg());
			jsonObject.put("registrationImg", dto.getRegistrationImg());
		}
		return jsonObject;
	}
	
	private JSONArray changePOListIntoJSONData(POListDTO dto, long userId) {
		// 这个polist的数据其实只可能最多1个，同一个时间最多只能有一个档期在线
		// 2014/10/30 现在polist又有可能有多个了
		JSONArray out = new JSONArray();
		// 重排序
		ScheduleUtil.sortPOListForMainsite(dto);
		List<PODTO> list = dto.getPoList();
		for (PODTO podDto : list) {
			JSONObject obj = new JSONObject();
			JSONObject bannerObject = new JSONObject();
			bannerObject.put("scheduleId", podDto.getScheduleDTO().getSchedule().getId());
			bannerObject.put("homeBannerImgUrl", podDto.getBannerDTO().getBanner().getHomeBannerImgUrl());
			obj.put("banner", bannerObject);
			obj.put("brandId", podDto.getScheduleDTO().getSchedule().getBrandId());
			obj.put("brandLogo", podDto.getScheduleDTO().getSchedule().getBrandLogo());
			obj.put("title", podDto.getScheduleDTO().getSchedule().getTitle());
			//JSONObject prdDetail = new JSONObject();
			obj.put("minDiscount", podDto.getScheduleDTO().getSchedule().getMinDiscount());
			//obj.put("prdDetail", prdDetail);
			obj.put("startTime", podDto.getScheduleDTO().getSchedule().getStartTime());
			obj.put("endTime", podDto.getScheduleDTO().getSchedule().getEndTime());
			obj.put("promotionDesc", podDto.getPromotionDesc());
			out.add(obj);
		}
		return out;
	}
	
	@RequestMapping(value ="/brandList", method = RequestMethod.GET)
	public String brandListPage(HttpServletRequest request, HttpServletResponse response, Model model){
		return "pages/brand/list";
	}
	
	@RequestMapping(value = "/brand/3g/list", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getEnteredBrandForWap() {
		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		List<BrandItemDTO> list = mainBrandFacade.getAllBrandForApp3G(userId, areaId);
		BaseJsonVO out = new BaseJsonVO();
		BaseJsonListResultVO resultVO = new BaseJsonListResultVO(list);
		resultVO.setTotal(list.size());
		out.setCode(200);
		out.setResult(resultVO);
		return out;
	}
	
	@BILog(action = "page", type = "brandListPage", clientType="wap")
	@RequestMapping(value ="/brand/settle", method = RequestMethod.GET)
	public String brandSettle(Model model){
		return "pages/brand/brand.settle";
	}

	@BILog(action = "click", type = "verifycode", clientType="wap")
	@RequestMapping(value = "/brand/genverifycode", method = RequestMethod.GET)
	public void genVerifyCode(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();  
		//String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		
		String capText = captchaProducer.createText();
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		session.setAttribute(CodeInfoUtil.KAPTCHA_SESSION_DATE, System.currentTimeMillis());
		
		BufferedImage bi = captchaProducer.createImage(capText);  
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}
	
}
