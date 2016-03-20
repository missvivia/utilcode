package com.xyl.mmall.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.facade.MainSiteBusinessFacade;
import com.xyl.mmall.mainsite.util.ControllerResultHandlerUtil;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * 主站的品牌Controller
 * 
 * @author chengximing
 *
 */
@Controller
public class BrandController extends BaseController {
	@Resource
	private MainBrandFacade mainBrandFacade;

	@Resource
	private BusinessFacade businessFacade;

	@Resource
	private MainSiteBusinessFacade mainSiteBusinessFacade;

	@Resource
	private ScheduleFacade scheduleFacade;
	
	@Resource
	private Producer captchaProducer;
	
	@RequestMapping(value = "/brand/state", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO isFollow(@RequestParam long id) {
		BaseJsonVO vo = new BaseJsonVO();
		vo.setCode(200);
		long userId = SecurityContextUtils.getUserId();
		if (mainBrandFacade.isBrandFavoredByUser(id, userId)) {
			vo.setResult(true);
		} else {
			vo.setResult(false);
		}
		return vo;
	}

	/**
	 * 我的品牌关注查询
	 * 
	 * @param model
	 * @param userId
	 * @return
	 */
	@BILog(action = "page", type = "myFavoritePage")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/brand/favlist", method = RequestMethod.GET)
	public @ResponseBody JSONObject getBrandLikeOfUser(@RequestParam int limit, @RequestParam int offset,
			@RequestParam(required = false) Boolean mobile, HttpServletRequest request, HttpServletResponse response) {
		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		JSONObject jsonObject = new JSONObject();
		DDBParam param = new DDBParam("createDate", false, 0, 0);
		RetArg retArg = null;
		if (mobile == null || (mobile != null && mobile.equals(false))) {
			retArg = mainBrandFacade.getBrandUserFavListByUserId(param, userId, areaId);
		} else {
			retArg = mainBrandFacade.getFavbrandListApp(param, 0, userId, areaId);
		}
		param = RetArgUtil.get(retArg, DDBParam.class);
		List<BrandItemDTO> brandItemList = RetArgUtil.get(retArg, ArrayList.class);
		// Map<Long, List<ScheduleDTO>> poResultMap = new HashMap<Long,
		// List<ScheduleDTO>>();
		// // 重新按照千人千面的逻辑处理掉档期数据
		// if (areaId > 0) {
		// List<Long> ids = new ArrayList<>(brandItemList.size());
		// for (BrandItemDTO dto : brandItemList) {
		// ids.add(dto.getBrandId());
		// }
		// ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		// paramDTO.brandIdList = ids;
		// paramDTO.curSupplierAreaId = areaId;
		// try {
		// paramDTO.saleSiteFlag =
		// ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		// } catch (Exception e) {
		// paramDTO.saleSiteFlag = -1;
		// }
		// POListDTO poList =
		// scheduleFacade.getScheduleListByBrandIdList(paramDTO);
		// UserLoginBean userLoginBean =
		// QrqmUtils.getCurIPLastLoginTime(request, response);
		// poList = POBaseUtil.filterForEveryOne(userLoginBean, poList);
		// for (PODTO po : poList.getPoList()) {
		// Schedule s = po.getScheduleDTO().getSchedule();
		// long brandId = s.getBrandId();
		// if (poResultMap.get(brandId) == null) {
		// poResultMap.put(brandId, new ArrayList<ScheduleDTO>());
		// }
		// ScheduleDTO dto = new ScheduleDTO();
		// dto.setSchedule(s);
		// poResultMap.get(brandId).add(dto);
		// }
		// }
		// for (BrandItemDTO dto : brandItemList) {
		// if (poResultMap.get(dto.getBrandId()) == null) {
		// dto.setScheduleId(0);
		// dto.setPoCount(0);
		// dto.setNextPoTime(0);
		// dto.setNextPoEndTime(0);
		// } else {
		// List<ScheduleDTO> resDtos = poResultMap.get(dto.getBrandId());
		// dto.setScheduleId(resDtos.get(0).getSchedule().getId());
		// RetArg retArgPO =
		// POBaseUtil.getOnlineScheduleSizeAndNextPoTime(resDtos);
		// dto.setPoCount(RetArgUtil.get(retArgPO, Integer.class));
		// dto.setNextPoTime(RetArgUtil.get(retArgPO, Long.class, 0));
		// dto.setNextPoEndTime(RetArgUtil.get(retArgPO, Long.class, 1));
		// }
		// }
		Collections.sort(brandItemList);
		// brandItemList = reOrderBrandItemList(brandItemList);
		// 后台根据cache内存分页
		List<BrandItemDTO> outDtos = new ArrayList<>();
		if (offset < brandItemList.size()) {
			for (int idx = offset; idx < brandItemList.size(); idx++) {
				if (outDtos.size() < limit) {
					outDtos.add(brandItemList.get(idx));
				}
			}
		}
		ControllerResultHandlerUtil.handleResult(jsonObject, true, outDtos, brandItemList.size());
		return jsonObject;
	}

	/**
	 * 获取最新关注的最多五个品牌关注项
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/brand/recentfavlist", method = RequestMethod.GET)
	public @ResponseBody JSONObject getRecentBrandLikeOfUser() {
		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		JSONObject jsonObject = new JSONObject();
		DDBParam param = new DDBParam("createDate", false, 5, 0);
		RetArg retArg = mainBrandFacade.getBrandUserFavListByUserId(param, userId, areaId);
		param = RetArgUtil.get(retArg, DDBParam.class);
		List<BrandItemDTO> brandItemList = RetArgUtil.get(retArg, ArrayList.class);
		ControllerResultHandlerUtil.handleResult(jsonObject, true, brandItemList, brandItemList.size());
		return jsonObject;
	}

	/**
	 * 未登录的状态的一个跳转辅助controller，必须是登录的才能用的接口
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	// @BILog(action = "page", type = "brandDetailPage")
	// @RequestMapping(value = "/mainbrand/story/redirect", method =
	// RequestMethod.GET)
	// public String getBrandStory(Model model, @RequestParam long id) {
	// // 接受的id是 brandId
	// appendStaticMethod(model);
	// long userId = SecurityContextUtils.getUserId();
	// long areaId = AreaUtils.getAreaCode();
	// JSONObject jsonObject = new JSONObject();
	// if (areaId > 0) {
	// if (userId > 0) {
	// mainBrandFacade.addBrandCollection(userId, id);
	// }
	// BusinessDTO targetDTO =
	// businessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 1);
	// if (targetDTO == null) {
	// targetDTO = businessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 2);
	// }
	// if (targetDTO != null && targetDTO.getIsActive() == 0) {
	// long supplierId = targetDTO.getId();
	// SupplierBrandFullDTO brandDto =
	// mainBrandFacade.getBrandFullDTOBySupplierId(supplierId, userId);
	// if (brandDto != null) {
	// jsonObject.put("brandInfo", brandDto);
	// POListDTO curPoListDTO = mainBrandFacade.getPOList(id, areaId);
	// jsonObject.put("polist", changePOListIntoJSONData(curPoListDTO, userId));
	// jsonObject.put("nextpolist",
	// changePOListIntoJSONData(mainBrandFacade.getPOListFuture(id, areaId, 4),
	// userId));
	// jsonObject.put("businessInfo", getBusinessLawInfo(targetDTO));
	// model.addAttribute("data", jsonObject);
	// return "pages/brand/story";
	// }
	// }
	// }
	// return "pages/404";
	// }

	@BILog(action = "page", type = "brandDetailPage")
	@RequestMapping(value = "/mainbrand/story", method = RequestMethod.GET)
	public String getBrandStory(Model model, @RequestParam long id, HttpServletRequest request,
			HttpServletResponse response) {
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
			BusinessDTO targetDTO = mainSiteBusinessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 1);
			if (targetDTO != null) {
				long supplierId = targetDTO.getId();
				brandDto = getSupplierBrandFullDTO(supplierId, userId);
			}
			if (targetDTO == null || brandDto == null) {
				targetDTO = mainSiteBusinessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 2);
				if (targetDTO != null) {
					long supplierId = targetDTO.getId();
					brandDto = getSupplierBrandFullDTO(supplierId, userId);
				}
			}
			// Business business =
			// businessFacade.getBusinessByAreaIdAndBrandId(areaId, id);
			if (targetDTO != null && targetDTO.getIsActive() == 0) {
				if (brandDto != null) {
					jsonObject.put("brandInfo", brandDto);
					POListDTO curPoListDTO = mainBrandFacade.getPOList(id, areaId);
					// UserLoginBean userLoginBean =
					// QrqmUtils.getCurIPLastLoginTime(request, response);
					// curPoListDTO =
					// POBaseUtil.filterForEveryOne(userLoginBean,
					// curPoListDTO);
					jsonObject.put("polist", changePOListIntoJSONData(curPoListDTO, userId));
					jsonObject.put("nextpolist",
							changePOListIntoJSONData(mainBrandFacade.getPOListFuture(id, areaId, 4), userId));
					jsonObject.put("businessInfo", getBusinessLawInfo(targetDTO, false));
					model.addAttribute("data", jsonObject);
					return "pages/brand/story";
				}
			}
		}
		return "pages/404";
	}

	/**
	 * 获取品牌数据.
	 * 
	 * @param supplierId
	 * @param userId
	 * @return
	 */
	private SupplierBrandFullDTO getSupplierBrandFullDTO(long supplierId, long userId) {
		// 1.品牌数据
		SupplierBrandFullDTO supplierBrandFullDTO = mainBrandFacade.getBrandFullDTOBySupplierId(supplierId);
		if (supplierBrandFullDTO != null) {
			SupplierBrandFullDTO dto = ReflectUtil.cloneObj(supplierBrandFullDTO);
			// 2.是否关注
			boolean favByUser = mainBrandFacade.getBrandCollectionState(userId, supplierBrandFullDTO.getBrandId());
			dto.setFavByUser(favByUser);
			return dto;
		}
		return null;
	}

	// 获取供应商法人信息
	// 简化供应商信息 详细信息需要用户输入验证码验证之后才行
	private JSONObject getBusinessLawInfo(BusinessDTO dto, boolean bFull) {
		JSONObject jsonObject = new JSONObject();
		if (dto != null) {
			String address = dto.getContactProvince();
			jsonObject.put("companyName", dto.getCompanyName());
			if (bFull) {
				address += dto.getContactCity() + dto.getContactCountry() + dto.getContactAddress();
				jsonObject.put("legalPerson", dto.getLegalPerson());
				jsonObject.put("registrationNumber", dto.getRegistrationNumber());
				jsonObject.put("registrationNumberStart", dto.getRegistrationNumberStart());
				jsonObject.put("registrationNumberEnd", dto.getRegistrationNumberEnd());
				if (dto.getType() == 2) {
					jsonObject.put("brandAuthImg", dto.getBrandImg());
				} else {
					jsonObject.put("brandAuthImg", dto.getBrandAuthImg());
				}
				jsonObject.put("registrationImg", dto.getRegistrationImg());
				jsonObject.put("registrationNumberAvaliable", dto.getRegistrationNumberAvaliable());
			}
			jsonObject.put("contactAddress", address);

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
			// JSONObject prdDetail = new JSONObject();
			obj.put("minDiscount", podDto.getScheduleDTO().getSchedule().getMinDiscount());
			// obj.put("prdDetail", prdDetail);
			obj.put("startTime", podDto.getScheduleDTO().getSchedule().getStartTime());
			obj.put("endTime", podDto.getScheduleDTO().getSchedule().getEndTime());
			out.add(obj);
		}
		return out;
	}

	// 这个接口不安全 以后会废弃
	@RequestMapping(value = "/mainbrand/backend", method = RequestMethod.GET)
	public String getBackendBrandStory(Model model, @RequestParam long id) {
		appendStaticMethod(model);
		// 接受的id是 supplierBrandId
		model.addAttribute("data", mainBrandFacade.getBrandFullDTO(id));
		return "pages/brand/backstory";
	}

	@RequestMapping(value = "/mainbrand/backend2", method = RequestMethod.POST)
	public String preViewBrandStory(Model model, @RequestParam Map<String, String> data) {
		appendStaticMethod(model);
		String dtoString = data.get("data");
		JSONObject dto = JSONObject.parseObject(dtoString);
		model.addAttribute("data", dto);
		return "pages/brand/backstory";
	}

	/**
	 * 添加我的关注活动 post
	 * 
	 * @param model
	 * @param scheduleId
	 */
	@BILog(action = "click", type = "followBrand")
	@RequestMapping(value = "/brand/follow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addBrandLikeOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();
		JSONObject jsonObject = new JSONObject();
		long brandId = param.getLongValue("brandId");
		if (userId > 0 && mainBrandFacade.addBrandCollection(userId, brandId)) {
			jsonObject.put("code", 200);
			jsonObject.put("message", "ok");
		} else {
			jsonObject.put("code", 400);
			jsonObject.put("message", "follow brandId = " + brandId + " userId = " + userId + " failed!");
		}
		return jsonObject;
	}

	/**
	 * 删除我的关注活动 post
	 * 
	 * @param model
	 * @param brandId
	 */
	@BILog(action = "click", type = "defollowBrand")
	@RequestMapping(value = "/brand/unfollow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject delBrandLikeOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();
		JSONObject jsonObject = new JSONObject();
		long brandId = param.getLongValue("brandId");
		if (mainBrandFacade.removeBrandCollection(userId, brandId)) {
			jsonObject.put("code", 200);
			jsonObject.put("message", "ok");
		} else {
			jsonObject.put("code", 400);
			jsonObject.put("message", "unfollow brandId = " + brandId + " userId = " + " failed!");
		}
		return jsonObject;
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/brand/settlelist", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getEnteredBrand(@RequestParam String type, @RequestParam int limit, @RequestParam int offset) {
		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		DDBParam param = new DDBParam("brandId", false, limit, offset);
		RetArg retArg = mainBrandFacade.getAllBrandList(param, userId, type.toUpperCase(), areaId);
		param = RetArgUtil.get(retArg, DDBParam.class);
		List<BrandItemDTO> list = RetArgUtil.get(retArg, ArrayList.class);
		BaseJsonVO out = new BaseJsonVO();
		BaseJsonListResultVO resultVO = new BaseJsonListResultVO(list);
		resultVO.setTotal(param.getTotalCount());
		out.setCode(200);
		out.setResult(resultVO);
		return out;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/brand/fullfavlist", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getBrandFavListFullDetails(@RequestParam int limit, @RequestParam int offset) {
		long userId = SecurityContextUtils.getUserId();
		long areaId = AreaUtils.getAreaCode();
		if (areaId > 0) {
			DDBParam param = new DDBParam("createDate", false, limit, offset);
			RetArg retArg = mainBrandFacade.getFavbrandList(param, 0, userId, areaId);
			param = RetArgUtil.get(retArg, DDBParam.class);
			List<BrandItemDTO> list = RetArgUtil.get(retArg, ArrayList.class);
			// 重排序
			// list = reOrderBrandItemList(list);
			BaseJsonVO vo = new BaseJsonVO();
			BaseJsonListResultVO result = new BaseJsonListResultVO(list);
			result.setTotal(param.getTotalCount());
			vo.setCode(200);
			vo.setResult(result);
			return vo;
		} else {
			return new BaseJsonVO();
		}
	}

	// private List<BrandItemDTO> reOrderBrandItemList(List<BrandItemDTO> list)
	// {
	// Collections.sort(list, new Comparator<BrandItemDTO>() {
	// @Override
	// public int compare(BrandItemDTO o1, BrandItemDTO o2) {
	// if ((o1.getPoCount() != 0 && o2.getPoCount() == 0) ||
	// (o1.getPoCount() == 0 && o2.getPoCount() != 0)) {
	// return o2.getPoCount() - o1.getPoCount();
	// } else if ((o1.getNextPoTime() != 0L && o2.getNextPoTime() == 0L) ||
	// (o1.getNextPoTime() == 0L && o2.getNextPoTime() != 0L)) {
	// return (o1.getNextPoTime() != 0L ? -1 : 1);
	// } else {
	// return 1;
	// }
	// }
	// });
	// return list;
	// }

	@BILog(action = "page", type = "brandListPage")
	@RequestMapping(value = "/brand/settle", method = RequestMethod.GET)
	public String brandSettle(Model model) {
		return "pages/brand/brand.settle";
	}

	@BILog(action = "click", type = "verifycode")
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
	
	@BILog(action = "click", type = "verifycode")
	@RequestMapping(value = "/brand/verifycode", method = RequestMethod.GET)
	public String brandVerifyCode(Model model) {
		return "pages/brand/verifycode";
	}
	
	@BILog(action = "page", type = "business")
	@RequestMapping(value = "/brand/businessinfo", method = RequestMethod.POST)
	public ModelAndView brandBusinessInfo(Model model, HttpServletRequest request,
			@RequestParam long id, @RequestParam String code,RedirectAttributes redirectAttrs) {
		JSONObject jsonObject = new JSONObject();
		ModelAndView result= new ModelAndView("pages/brand/businessinfo");
		HttpSession session = request.getSession();  
		String codeInSession = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		long codeGenTime = (Long)session.getAttribute(CodeInfoUtil.KAPTCHA_SESSION_DATE);
		codeInSession = codeInSession.trim().toUpperCase();
		code = code.trim().toUpperCase();
		if (code.equals(codeInSession)) {
			long cur = System.currentTimeMillis();
			if (cur - codeGenTime < 5 * 60 * 1000) {
				long userId = SecurityContextUtils.getUserId();
				// 接受的id是 brandId
				long areaId = AreaUtils.getAreaCode();
				if (areaId > 0) {
					SupplierBrandFullDTO brandDto = null;
					BusinessDTO targetDTO = mainSiteBusinessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 1);
					if (targetDTO != null) {
						long supplierId = targetDTO.getId();
						brandDto = getSupplierBrandFullDTO(supplierId, userId);
					}
					if (targetDTO == null || brandDto == null) {
						targetDTO = mainSiteBusinessFacade.getBusinessByAreaIdAndBrandId(areaId, id, 2);
						if (targetDTO != null) {
							long supplierId = targetDTO.getId();
							brandDto = getSupplierBrandFullDTO(supplierId, userId);
						}
					}
					
					if (targetDTO != null && targetDTO.getIsActive() == 0) {
						if (brandDto != null) {
							jsonObject.put("businessInfo", getBusinessLawInfo(targetDTO, true));
							jsonObject.put("info", "ok");
							model.addAttribute("data", jsonObject);
							return result; 
						} else {
							jsonObject.put("info", "找不到对应的商家信息");
						}
					} else {
						jsonObject.put("info", "找不到对应的商家信息");
					}
				} else {
					jsonObject.put("info", "无法判断您的地理位置");
				}
			} else {
				jsonObject.put("info", "验证码超时");
			}
		} else {
			jsonObject.put("info", "验证码输入不正确");
		}
		redirectAttrs.addFlashAttribute("data", jsonObject);
		result.setViewName("redirect:/brand/verifycode?id=" + id);
		return result;
	}
	
	@RequestMapping(value = "/brand/listByCategory", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getBrandByCategory(Long categoryId) {
		BaseJsonVO out = new BaseJsonVO();
		if (categoryId == null || categoryId == 0) {
			out.setCode(400);
			out.setMessage("请选择分类");
			return out;
		}
		long areaId = AreaUtils.getAreaCode();
		List<JSONObject> list = mainBrandFacade.getBrandListInOrderByCategory(categoryId, areaId);
		out.setCode(200);
		out.setResult(list);
		return out;
	}
}
