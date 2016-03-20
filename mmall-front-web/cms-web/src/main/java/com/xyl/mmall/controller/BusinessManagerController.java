package com.xyl.mmall.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.facade.OmsFacade;
import com.xyl.mmall.cms.dto.BusiUserRelationDTO;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CmsBrandFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.facade.util.DistrictCodeUtil;
import com.xyl.mmall.cms.meta.SendDistrict;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.BusinessConditionAO;
import com.xyl.mmall.cms.vo.OperationResult;
import com.xyl.mmall.cms.vo.PagerContainer;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.annotation.CheckFormToken;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.ExcelUtil;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.member.dto.UserProfileConditionDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;

/**
 * 商家管理
 * 
 * @author hzchaizhf
 * 
 */
@Controller
// @RequestMapping("/business")
public class BusinessManagerController extends BaseController {

	private static Logger LOGGER = LoggerFactory.getLogger(BusinessManagerController.class);

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private OmsFacade omsFacade;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private CmsBrandFacade cmsBrandFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private SiteCMSFacade siteCMSFacade;

	@Autowired
	private LocationService locationService;

	@Autowired
	private UserProfileService userProfileService;

	@RequestMapping(value = { "/business/account" })
	@RequiresPermissions(value = { "business:account" })
	public String index(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/business/account";
	}

	/**
	 * 创建特许经营账号成功页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/business/creaseAccountSuccess" })
	@RequiresPermissions(value = { "business:account" })
	public String createBusinessSuccess(@RequestParam(value = "businessId") long businessId, Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("businessId", businessId);
		return "pages/business/accountSuccess";
	}

	/**
	 * 异步获取商家信息列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/business/account/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> ajaxGetList(BusinessConditionDTO businessConditionDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		long userId = SecurityContextUtils.getUserId();
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if (retArg == null) {
			map.put("code", ResponseCode.RES_ERROR);
			map.put("message", "没有权限获取商家列表!");
			return map;
		}
		// 用户不是平台级时
		if (!RetArgUtil.get(retArg, Boolean.class)) {
			businessConditionDTO.setAgentIds(RetArgUtil.get(retArg, ArrayList.class));
		}
		PagerContainer container = null;
		try {
			container = businessFacade.getBusinessListByBusinessCondition(businessConditionDTO);
			map.put("code", ResponseCode.RES_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", ResponseCode.RES_ERROR);
		}
		map.put("result", container);
		return map;
	}

	/**
	 * 创建商家
	 * 
	 * @param model
	 * @return
	 */
	@CheckFormToken(isCheckRepeat = false)
	@RequestMapping(value = "/business/create", method = RequestMethod.GET)
	@RequiresPermissions(value = { "business:account" })
	public String create(Model model) {
		// DDBParam param = new DDBParam("brandId", true, 0, 0);
		// RetArg retArg = cmsBrandFacade.getAllBrandItemList(param);
		// @SuppressWarnings("unchecked")
		// List<BrandItemDTO> brandDTOList = RetArgUtil.get(retArg,
		// ArrayList.class);
		// for (BrandItemDTO brandItemDTO : brandDTOList) {
		// if (StringUtils.isBlank(brandItemDTO.getBrandNameZh())) {
		// brandItemDTO.setBrandNameZh(brandItemDTO.getBrandNameEn());
		// }
		// }
		// model.addAttribute("brandList", brandDTOList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/business/create";
	}

	/**
	 * ajax创建商家
	 * 
	 * @return
	 * @throws Exception
	 */
	@CheckFormToken
	@RequestMapping("/business/create/account")
	@RequiresPermissions(value = { "business:account" })
	public @ResponseBody BaseJsonVO createBusiness(@RequestBody BusinessDTO businessDTO, HttpServletRequest request)
			throws Exception {
		BaseJsonVO jsonVo = new BaseJsonVO();
		Set<String> areaSet = new HashSet<String>();
		String areaCode = "";
		// 验证配送区域
		for (SendDistrict sendDistrict : businessDTO.getSendDistrictDTOs()) {
			if (sendDistrict.getDistrictId() > 0) {
				areaCode = String.valueOf(sendDistrict.getDistrictId());
			} else if (sendDistrict.getCityId() > 0) {
				areaCode = sendDistrict.getCityId() + "00";
			} else {
				areaCode = sendDistrict.getProvinceId() > 0 ? sendDistrict.getProvinceId() + "0000" : "000000";
			}
			if (!areaSet.add(areaCode)) {
				jsonVo.setCode(ResponseCode.RES_ERROR);
				jsonVo.setMessage("配送区域重复！");
				return jsonVo;
			}
		}
		if (DistrictCodeUtil.isAreaRepeat(areaSet) == null) {
			jsonVo.setCode(ResponseCode.RES_ERROR);
			jsonVo.setMessage("配送区域重复！");
			return jsonVo;
		}

		long userId = SecurityContextUtils.getUserId();
		if (businessDTO.getId() > 0) {
			LOGGER.info("modify business: remote host:" + IPUtils.getOriginalClientIp(request) + "by cms user:"
					+ userId);
			businessDTO.setUpdateBy(userId);
			int result = businessFacade.updateBusiness(businessDTO);
			switch (result) {
			case -1:
				jsonVo.setCode(ResponseCode.RES_ERROR);
				jsonVo.setMessage("更新商家信息失败！");
				break;
			case -2:
				jsonVo.setCode(ResponseCode.RES_ERROR);
				jsonVo.setMessage("帐号" + businessDTO.getBusinessAccount() + "不存在！");
				break;
			case -3:
				jsonVo.setCode(ResponseCode.RES_ERROR);
				jsonVo.setMessage("更新帐号密码失败！");
				break;

			default:
				jsonVo.setCode(ResponseCode.RES_SUCCESS);
				break;
			}
			return jsonVo;
		}
		if (!StringUtils.endsWith(businessDTO.getBusinessAccount(), MmallConstant.BUSINESS_ACCOUNT_SUFFIX)) {
			businessDTO.setBusinessAccount(businessDTO.getBusinessAccount() + MmallConstant.BUSINESS_ACCOUNT_SUFFIX);
		}
		if (businessFacade.existsBusinessAccount(businessDTO.getBusinessAccount())) {
			jsonVo.setCode(ResponseCode.RES_EEXIST);
			jsonVo.setMessage("帐号" + businessDTO.getBusinessAccount() + "已注册！");
		} else {
			businessDTO.setIsActive(0);
			businessDTO.setCreatorId(userId);
			if (businessFacade.addBusiness(businessDTO) != null) {
				jsonVo.setCode(ResponseCode.RES_SUCCESS);
				jsonVo.setResult(businessDTO.getId());// 特许经营创建成功跳转使用
			} else {
				jsonVo.setCode(ResponseCode.RES_ERROR);
				jsonVo.setMessage("创建商家失败！");
			}
		}

		return jsonVo;
	}

	/**
	 * 商家详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/business/detail/{id}/", "/business/detail/{id}" })
	@RequiresPermissions(value = { "business:account" })
	public String detail(@PathVariable long id, Model model) {
		appendStaticMethod(model);
		BusinessDTO bu;
		try {
			bu = businessFacade.getBusinessById(id);
			if (bu != null) {
				model.addAttribute("code", ResponseCode.RES_SUCCESS);
				long userId = SecurityContextUtils.getUserId();
				RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
				if (retArg == null
						|| (!RetArgUtil.get(retArg, Boolean.class) && !RetArgUtil.get(retArg, ArrayList.class)
								.contains(userId))) {
					model.addAttribute("username", SecurityContextUtils.getUserName());// username
																						// 403页面显示有用到
					return "error/403";// 没权限
				}
			} else {
				model.addAttribute("code", ResponseCode.RES_ERROR);
			}
			model.addAttribute("business", bu);
			model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
			return "pages/business/detail";
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	/**
	 * 编辑详情页
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@CheckFormToken(isCheckRepeat = false)
	@RequestMapping({ "/business/edit/{id}/", "/business/edit/{id}" })
	@RequiresPermissions(value = { "business:account" })
	public String edit(@PathVariable long id, Model model) {
		appendStaticMethod(model);
		BusinessDTO bu;
		try {
			long userId = SecurityContextUtils.getUserId();
			RetArg retArgArea = siteCMSFacade.getAgentAreaInfoByUserId(userId);
			Set<Long> areaSet = RetArgUtil.get(retArgArea, HashSet.class);
			boolean isRoot = false;
			// root
			if (RetArgUtil.get(retArgArea, Boolean.class)) {
				isRoot = true;
			}
			bu = businessFacade.getBusinessById(id);
			if (bu != null) {
				model.addAttribute("code", ResponseCode.RES_SUCCESS);
				if (retArgArea == null || (!isRoot && !RetArgUtil.get(retArgArea, ArrayList.class).contains(userId))) {
					model.addAttribute("username", SecurityContextUtils.getUserName());// username
																						// 403页面显示有用到
					return "error/403";// 没权限
				}
			} else {
				model.addAttribute("code", ResponseCode.RES_ERROR);
			}

			Set<Long> codeSet = new HashSet<Long>();
			// 针对直辖市codeList
			Set<Long> dirctcodeSet = new HashSet<Long>();
			for (SendDistrictDTO sendDistrictDTO : bu.getSendDistrictDTOs()) {
				boolean isEdit = false;
				if (isRoot || sendDistrictDTO.getProvinceId() == 0) {
					sendDistrictDTO.setEdit(true);
					isEdit = true;
				}
				// 区域是全市时判断用户有没保函全市区域
				if (!isEdit && sendDistrictDTO.getCityId() == 0) {
					List<LocationCode> ret = locationService.getCityListByProvinceCode(sendDistrictDTO.getProvinceId());
					for (LocationCode locationCode : ret) {
						if (!areaSet.contains(locationCode.getCode())) {
							sendDistrictDTO.setEdit(false);
							break;
						}
						sendDistrictDTO.setEdit(true);
						continue;
					}
				}
				if (!isEdit && RetArgUtil.get(retArgArea, HashSet.class).contains(sendDistrictDTO.getCityId())) {
					sendDistrictDTO.setEdit(true);
				}

				if (sendDistrictDTO.getProvinceId() != 0) {
					codeSet.add(sendDistrictDTO.getProvinceId());
				}
				if (sendDistrictDTO.getCityId() > 0) {
					codeSet.add(sendDistrictDTO.getCityId());
				} else if (sendDistrictDTO.getCityId() < 0) {
					dirctcodeSet.add(sendDistrictDTO.getCityId());
				}
				if (sendDistrictDTO.getDistrictId() != 0) {
					codeSet.add(sendDistrictDTO.getDistrictId());
				}
			}
			List<LocationCode> locationCodes = locationService.getLocationCodeListByCodeList(new ArrayList<Long>(
					codeSet));
			for (Long dirctcode : dirctcodeSet) {
				locationCodes.add(locationService.getLocationCode(dirctcode));
			}
			Map<Long, LocationCode> locationCodeMap = Maps.uniqueIndex(locationCodes,
					new Function<LocationCode, Long>() {
						@Override
						public Long apply(LocationCode arg0) {
							return arg0.getCode();
						}
					});
			for (SendDistrictDTO sendDistrictDTO : bu.getSendDistrictDTOs()) {
				String provinceName = locationCodeMap.get(sendDistrictDTO.getProvinceId()) == null ? ""
						: locationCodeMap.get(sendDistrictDTO.getProvinceId()).getLocationName();
				sendDistrictDTO.setProvinceName(sendDistrictDTO.getProvinceId() == 0 ? "全国" : provinceName);
				String cityName = locationCodeMap.get(sendDistrictDTO.getCityId()) == null ? "" : locationCodeMap.get(
						sendDistrictDTO.getCityId()).getLocationName();
				sendDistrictDTO.setCityName(sendDistrictDTO.getCityId() == 0 ? "全部城市" : cityName);
				String distName = locationCodeMap.get(sendDistrictDTO.getDistrictId()) == null ? "" : locationCodeMap
						.get(sendDistrictDTO.getDistrictId()).getLocationName();
				sendDistrictDTO.setDistName(sendDistrictDTO.getDistrictId() == 0 ? "全部区/县" : distName);
			}
			model.addAttribute("business", bu);
			model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
			return "pages/business/create";
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 判断商家帐号是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/business/account/check", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checkAccount(BusinessConditionAO businessConditionAO) {
		BaseJsonVO result = new BaseJsonVO();
		Map<String, Object> map = new HashMap<>();
		try {
			if (StringUtils.isNotBlank(businessConditionAO.getAccount())) {
				if (!businessFacade.existsBusinessAccount(businessConditionAO.getAccount())) {
					map.put("code", ResponseCode.RES_SUCCESS);
					map.put("result", genRandomNum(16));
				} else {
					map.put("code", 1);
					result.setResult(false);
				}
			}
		} catch (Exception e) {
			map.put("code", ResponseCode.RES_ERROR);

		}

		return map;
	}

	/**
	 * 冻结帐号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/business/lock/{id}/", "/business/lock/{id}" })
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> lock(@PathVariable long id, Model model) {
		Map<String, Object> map = new HashMap<>();
		long userId = SecurityContextUtils.getUserId();
		OperationResult result = new OperationResult();
		try {
			if (businessFacade.lockBusinessAccount(userId, id)) {
				map.put("code", ResponseCode.RES_SUCCESS);
				result.setMsg("冻结成功");
				map.put("result", result);
			} else {
				map.put("code", ResponseCode.RES_ERROR);
				result.setMsg("冻结失败！");
				map.put("result", result);
			}
		} catch (Exception e) {
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("冻结失败");
			map.put("result", result);
		}
		return map;
	}

	/**
	 * 解冻帐号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/business/unlock/{id}/", "/business/unlock/{id}" })
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> unlock(@PathVariable long id, Model model) {
		Map<String, Object> map = new HashMap<>();
		long userId = SecurityContextUtils.getUserId();
		OperationResult result = new OperationResult();
		try {
			businessFacade.unlockBusinessAccount(userId, id);
			map.put("code", ResponseCode.RES_SUCCESS);
			result.setMsg("解冻成功");
			map.put("result", result);
		} catch (Exception e) {
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("解冻失败");
			map.put("result", result);
		}
		return map;
	}

	/**
	 * 删除帐号
	 * 
	 * @param model
	 * @return
	 */
	// @RequestMapping({ "/business/delete/{id}/", "/business/delete/{id}" })
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> delete(@PathVariable long id, Model model) {
		Map<String, Object> map = new HashMap<>();
		long userId = SecurityContextUtils.getUserId();
		OperationResult result = new OperationResult();
		int relt = 0;
		try {
			relt = businessFacade.deleteBusiness(id, userId);
		} catch (Exception e) {
			result.setMsg("删除商家失败!");
			map.put("code", ResponseCode.RES_ERROR);
			return map;
		}
		switch (relt) {
		case -1:
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("删除商家后台管理用户信息失败!");
			break;
		case -2:
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("删除商家后台时商品下架失败!");
			break;
		case -3:
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("因其他原因删除商家失败!");
			break;
		case -4:
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("删除商家失败!");
			break;
		default:
			result.setMsg("删除成功");
			map.put("code", ResponseCode.RES_SUCCESS);
			break;
		}
		map.put("result", result);
		return map;
	}

	/**
	 * 删除商家指定用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/business/deleteBusiUserRelation/{id}/", "/business/deleteBusiUserRelation/{id}" })
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> deleteBusiUserRelation(@PathVariable long id, Model model) {
		Map<String, Object> map = new HashMap<>();
		OperationResult result = new OperationResult();
		try {
			if (businessService.deleteBusiUserRelation(id)) {
				map.put("code", ResponseCode.RES_SUCCESS);
				result.setMsg("删除成功");
				map.put("result", result);
			} else {
				map.put("code", ResponseCode.RES_ERROR);
				result.setMsg("删除失败");
				map.put("result", result);
			}
		} catch (Exception e) {
			map.put("code", ResponseCode.RES_ERROR);
			result.setMsg("删除失败");
			map.put("result", result);
		}
		return map;
	}

	/**
	 * 添加商家指定用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/business/addBusiUserRelation", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> addBusiUserRelation(@RequestBody BusiUserRelationDTO busiUserRelationDTO) {
		Map<String, Object> map = new HashMap<>();
		OperationResult result = new OperationResult();
		map.put("code", ResponseCode.RES_ERROR);
		map.put("result", result);
		try {
			busiUserRelationDTO.setCreateBy(SecurityContextUtils.getUserId());
			busiUserRelationDTO.setUpdateBy(SecurityContextUtils.getUserId());
			if (StringUtils.isEmpty(busiUserRelationDTO.getUserName()) || busiUserRelationDTO.getBusinessId() <= 0) {
				result.setMsg("添加失败,参数有误");
				return map;
			}
			if (!StringUtils.endsWith(busiUserRelationDTO.getUserName(), MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
				busiUserRelationDTO.setUserName(busiUserRelationDTO.getUserName()
						+ MmallConstant.MAINSITE_ACCOUNT_SUFFIX);
			}
			UserProfileDTO userProfileDTO = userProfileService.findUserProfileByUserName(busiUserRelationDTO
					.getUserName());
			if (StringUtils.isEmpty(userProfileDTO.getUserName())) {
				result.setMsg("该用户不存在");
				return map;
			}
			// 验证是否已经存在
			if (businessService.isUserBusinessAllowed(busiUserRelationDTO.getBusinessId(), userProfileDTO.getUserId())) {
				result.setMsg("该用户已经为特许经营买家");
				return map;
			}
			busiUserRelationDTO.setUserId(userProfileDTO.getUserId());
			if (businessService.bindBusiUserRelation(busiUserRelationDTO)) {
				map.put("code", ResponseCode.RES_SUCCESS);
				result.setMsg("添加成功");
			} else {
				result.setMsg("添加失败");
			}
		} catch (Exception e) {
			result.setMsg("添加失败");
		}
		return map;
	}

	@RequestMapping("/business/batchdelete")
	@RequiresPermissions(value = { "business:account" })
	public @ResponseBody BaseJsonVO batchDeleteBusiness(@RequestBody String ids) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		long userId = SecurityContextUtils.getUserId();
		baseJsonVO.setCode(ResponseCode.RES_ERROR);
		if (StringUtils.isEmpty(ids)) {
			baseJsonVO.setMessage("传入参数有误！");
			return baseJsonVO;
		}
		String[] idStrArray = StringUtils.split(ids, ",");
		int result = 0;
		for (String idStr : idStrArray) {
			result = businessFacade.deleteBusiness(Long.parseLong(idStr), userId);
			if (result != 1) {
				switch (result) {
				case -1:
					baseJsonVO.setMessage("删除商家后台管理用户信息失败!");
					return baseJsonVO;
				case -2:
					baseJsonVO.setMessage("删除商家后台时商品下架失败!");
					return baseJsonVO;
				case -3:
					baseJsonVO.setMessage("因其他原因删除商家失败!");
					break;
				case -4:
					baseJsonVO.setMessage("删除商家失败!");
					return baseJsonVO;
				default:
					break;
				}
			}
		}
		if (result == 1) {
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
			baseJsonVO.setMessage("删除商家成功!");
		}
		return baseJsonVO;
	}

	/**
	 * 密码重置
	 * 
	 * @return
	 */
	@RequestMapping(value = "/business/account/reset", method = RequestMethod.GET)
	@RequiresPermissions(value = { "business:account" })
	@ResponseBody
	public Map<String, Object> resetPasswd(int id) {
		BaseJsonVO result = new BaseJsonVO();
		Map<String, Object> map = new HashMap<>();
		try {
			if (!businessFacade.existsBusinessAccount(String.valueOf(id))) {
				map.put("code", ResponseCode.RES_SUCCESS);
				map.put("result", genRandomNum(16));
			} else {
				map.put("code", 1);
				result.setResult(false);
			}
		} catch (Exception e) {
			map.put("code", ResponseCode.RES_ERROR);

		}
		return map;
	}

	/**
	 * 根据用户名称模糊搜索指定用户
	 * 
	 * @param userProfileConditionDTO
	 * @return
	 */
	@RequestMapping(value = "/business/user/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> ajaxSearchUserList(UserProfileConditionDTO userProfileConditionDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		PagerContainer container = null;
		try {
			container = businessFacade.getUserListByUserName(userProfileConditionDTO);
			map.put("code", ResponseCode.RES_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", ResponseCode.RES_ERROR);
		}
		map.put("result", container);
		return map;
	}

	/**
	 * 商家获取指定用户列表,包含用户名模糊搜索
	 * 
	 * @param ddbParam
	 * @param businessId
	 * @return
	 */
	@RequestMapping(value = "/business/busiuser/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "business:account" })
	public Map<String, Object> ajaxGetUserList(@RequestParam(value = "businessId") long businessId,
			@RequestParam(value = "userName") String userName, DDBParam ddbParam) {
		Map<String, Object> map = new HashMap<String, Object>();
		PagerContainer container = null;
		try {
			container = businessFacade.getPageBusiUserRelationBybusinessId(businessId, userName, ddbParam);
			map.put("code", ResponseCode.RES_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", ResponseCode.RES_ERROR);
		}
		map.put("result", container);
		return map;
	}

	/**
	 * 导入商家指定用户
	 * 
	 * @param file
	 * @param response
	 */
	@RequestMapping(value = "/business/user/import")
	@RequiresPermissions(value = { "business:account" })
	public void importUser(@RequestParam("file") MultipartFile file, @RequestParam("businessId") long businessId,
			HttpServletResponse response) {
		// 返回结果写入文件
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=result.txt");
		OutputStream out = null;
		BufferedOutputStream buffer = null;
		long uid = SecurityContextUtils.getUserId();
		try {
			out = response.getOutputStream();
			buffer = new BufferedOutputStream(out);
			BusinessDTO businessDTO = businessService.getBreifBusinessById(businessId, 0);
			if (businessDTO == null) {
				buffer.write("该商家不存在或者处于冻结状态！\n".getBytes("utf-8"));
				return;
			}
			// 初始化workboot
			Workbook workbook = ExcelUtil.initWorkbook(file);
			if (workbook == null) {
				buffer.write("无法读取文件，请确保文件是xlsx或xls格式的！\n".getBytes("utf-8"));
			} else {
				// 工作簿
				int sheetNum = workbook.getNumberOfSheets();
				// 只导入一个sheet
				Sheet sheet = workbook.getSheetAt(0);
				int rowNum = sheet.getLastRowNum();
				if (sheetNum > 1 || rowNum > 500) {
					buffer.write("一次最多500行且不支持多个sheet同时导入！\n".getBytes("utf-8"));
					buffer.write("导入失败！".getBytes("utf-8"));
					return;
				}
				List<String> userAccountList = new ArrayList<String>();
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < rowNum + 1; i++) {
					Row row = sheet.getRow(i);
					String buyAccount = ExcelUtil.getValue(row.getCell(0));
					if (i == 1) {
						String sellerAccount = ExcelUtil.getValue(row.getCell(1));
						if (StringUtils.isEmpty(sellerAccount)
								|| !sellerAccount.equals(businessDTO.getBusinessAccount())) {
							sb.append("Excel的大b账号跟当前选择的商家不一致！\n");
							buffer.write(sb.toString().getBytes("utf-8"));
							buffer.write("导入失败！".getBytes("utf-8"));
							return;
						}
					}
					if (StringUtils.isEmpty(buyAccount) || !buyAccount.endsWith(MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
						sb.append("第").append(i + 1).append("行，添加失败！").append("小b账号格式不对！\n");
						continue;
					}
					userAccountList.add(buyAccount);
				}
				Map<String, Long> userNameMap = userProfileService.findUserIdsByUserNames(userAccountList);
				Map<String, Long> validUserNameMap = new HashMap<String, Long>();
				for (String userName : userAccountList) {
					if (userNameMap.get(userName) == null) {
						sb.append(userName + "账号不存在！\n");
						continue;
					}
					if (businessService.isUserBusinessAllowed(businessId, userNameMap.get(userName))) {
						sb.append(userName + "用户已经为特许经营买家！\n");
						continue;
					}
					validUserNameMap.put(userName, userNameMap.get(userName));
				}
				// 导入商品
				List<BusiUserRelationDTO> busiUserRelationDTOs = new ArrayList<BusiUserRelationDTO>();
				for (Map.Entry<String, Long> entry : validUserNameMap.entrySet()) {
					BusiUserRelationDTO busiUserRelationDTO = new BusiUserRelationDTO();
					busiUserRelationDTO.setBusinessId(businessId);
					busiUserRelationDTO.setUserId(entry.getValue());
					busiUserRelationDTO.setUserName(entry.getKey());
					busiUserRelationDTO.setCreateBy(uid);
					busiUserRelationDTO.setUpdateBy(uid);
					busiUserRelationDTOs.add(busiUserRelationDTO);
				}
				boolean result = businessService.batchImportBusiUserRelation(busiUserRelationDTOs);
				if (result) {
					buffer.write(sb.toString().getBytes("utf-8"));
					buffer.write("导入结束！".getBytes("utf-8"));
				} else {
					buffer.write(sb.toString().getBytes("utf-8"));
					buffer.write("导入失败！".getBytes("utf-8"));
				}
			}

		} catch (Exception e) {
			LOGGER.error(uid + " cms user operate businessid " + businessId + " import user error :" + e.getMessage());
			try {
				if (null != buffer) {
					buffer.write("导入失败！".getBytes("utf-8"));
				}
			} catch (Exception e1) {
				LOGGER.error(uid + " cms user operate businessid " + businessId + " import user error :"
						+ e1.getMessage());
			}
		} finally {
			if (null != buffer) {
				try {
					buffer.flush();
					buffer.close();
				} catch (IOException e) {
					LOGGER.error(uid + " cms user operate businessid " + businessId + " import user error :"
							+ e.getMessage());
				}
			}
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					LOGGER.error(uid + " cms user operate businessid " + businessId + " import user error :"
							+ e.getMessage());
				}
			}
		}

	}

	/**
	 * 导出商家指定的买家
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/business/export/{supplierId}" })
	@RequiresPermissions(value = { "business:account" })
	public void exportBuyer(HttpServletResponse response, HttpServletRequest request, @PathVariable long supplierId,
			Model model) {
		BusinessDTO businessDTO = businessService.getBusinessById(supplierId, 0);
		if (businessDTO == null) {
			return;
		}
		List<Long> buyerIdList = new ArrayList<Long>();
		List<BusiUserRelationDTO> busiUserRelations = businessDTO.getBusiUserRelations();
		if (CollectionUtil.isEmptyOfList(busiUserRelations)) {
			return;
		}
		for (BusiUserRelationDTO busiUserRelationDTO : busiUserRelations) {
			buyerIdList.add(busiUserRelationDTO.getUserId());
		}
		List<UserProfileDTO> userProfileDTOs = userProfileService.findUserProfileByIdList(buyerIdList);
		File f = new File("buyer.xlsx");
		LinkedHashMap<String, String> buyerExcel = new LinkedHashMap<String, String>();
		buyerExcel.put("userName", "买家账号");
		FrameworkExcelUtil.writeExcel("买家账号", buyerExcel, UserProfileDTO.class, f.getName(), userProfileDTOs, request,
				response);
	}

	/**
	 * 生成随即密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}

	/**
	 * 从InputStream获取json字符串
	 * 
	 * @param input
	 * @return
	 */
	private String getJson(InputStream input) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}