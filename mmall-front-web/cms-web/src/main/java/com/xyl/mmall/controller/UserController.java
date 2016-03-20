/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.facade.AuthorityFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.UserInfoFacade;
import com.xyl.mmall.cms.facade.util.DistrictCodeUtil;
import com.xyl.mmall.cms.vo.UserProfileVO;
import com.xyl.mmall.cms.vo.ValetUserVO;
import com.xyl.mmall.common.facade.AccountFacade;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.enums.PlatformType;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.Gender;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * UserController.java created by yydx811 at 2015年6月23日 下午5:53:45 用户信息controller
 *
 * @author yydx811
 */
@Controller
@RequestMapping("/userInfo")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private UserInfoFacade userFacade;

	@Autowired
	private AuthorityFacade authorityFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Autowired
	private LocationFacade locationFacade;

	@Autowired
	private AccountFacade accountFacade;

	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userList" })
	public String list(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/userInfo/userList";
	}

	@RequestMapping(value = "/queryUser", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userList" })
	public @ResponseBody BaseJsonVO queryUserList(BasePageParamVO<UserProfileVO> basePageParamVO, String searchValue) {
		BaseJsonVO ret = new BaseJsonVO();
		if (basePageParamVO.getIsPage() == 1) {
			basePageParamVO.setList(userFacade.queryUserList(basePageParamVO, searchValue));
		} else {
			basePageParamVO.setList(userFacade.queryUserList(null, searchValue));
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(basePageParamVO);
		return ret;
	}

	@RequestMapping("/userDetail/{userId}")
	@RequiresPermissions({ "userInfo:userDetail" })
	public String userInfo(Model model, @PathVariable String userId,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		if (StringUtils.isNotBlank(userId) && RegexUtils.isAllNumber(userId)) {
			long uid = Long.parseLong(userId);
			// 基本信息
			model.addAttribute("baseInfo", userFacade.getUserDetailInfo(uid));
			// 收货地址
			model.addAttribute("consigneeAddress", userFacade.getUserConsigneeAddressList(uid));
		}
		return "/pages/userInfo/userDetail";
	}

	@RequestMapping(value = "/userOrder", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO getUserOrderList(@RequestParam(value = "userId", required = true) long userId,
			@RequestParam(value = "limit", required = false, defaultValue = "8") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		// 订单信息 需要 order:query 权限
		boolean flag = authorityFacade.isContainPermission(SecurityContextUtils.getUserId(), "order:query");
		if (!flag) {
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "没有查看订单的权限！");
			return ret;
		}
		limit = limit <= 0 ? 8 : limit;
		DDBParam ddbParam = DDBParam.genParamX(limit);
		ddbParam.setOffset(offset);
		ddbParam.setAsc(false);
		ddbParam.setOrderColumn("orderTime");
		ret.setResult(userFacade.getUserOrderList(userId, ddbParam));
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}

	@RequestMapping(value = "/userCoupon", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO getUserCouponList(@RequestParam(value = "userId", required = true) long userId,
			@RequestParam(value = "limit", required = false, defaultValue = "8") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		limit = limit <= 0 ? 8 : limit;
		ret.setResult(userFacade.getUserCoupons(userId, -1, limit, offset));
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}

	@RequestMapping(value = "/userUpdate", method = RequestMethod.POST)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO updateUserBaseInfo(@RequestBody UserProfileVO userProfileVO,
			HttpServletRequest request) {
		BaseJsonVO ret = new BaseJsonVO();
		if (userProfileVO.getUid() < 1l || StringUtils.isBlank(userProfileVO.getAccount())) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "参数错误！");
			return ret;
		}
		UserProfileDTO profileDTO = new UserProfileDTO();
		profileDTO.setUserId(userProfileVO.getUid());
		profileDTO.setUserName(userProfileVO.getAccount());
		profileDTO.setUserType(userProfileVO.getUserType());
		String result = checkUserParam(userProfileVO, profileDTO);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, result);
			return ret;
		}
		profileDTO.setIsValid(-1);
		profileDTO.setHasNoobCoupon(-1);

		AccountDTO accountDTO = accountFacade.findAccountByUserName(userProfileVO.getAccount());
		if (accountDTO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户账号不存在！");
			return ret;
		}
		if (userProfileVO.getIsModifyPass() == 1) {
			if (RegexUtils.isValidPassword(userProfileVO.getPassword())) {
				accountDTO.setPassword(userProfileVO.getPassword());
			} else {
				ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "密码为6-20数字或字母组合");
				return ret;
			}
		} else {
			accountDTO.setPassword(null);
		}
		try {
			logger.info("update user baseinfo. UserId : {}, AgentId : {}, ip : {}.", profileDTO.getUserId(),
					SecurityContextUtils.getUserId(), IPUtils.getIpAddr(request));
			if (userFacade.updateUserBaseInfo(accountDTO, profileDTO) > 0) {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "更新成功！");
				return ret;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		ret.setCodeAndMessage(ResponseCode.RES_ERROR, "更新失败！");
		return ret;
	}

	@RequestMapping(value = "/lockUser", method = RequestMethod.POST)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO lockUser(@RequestBody UserProfileVO userProfileVO, HttpServletRequest request) {
		userProfileVO.setIsActive(0);
		return updateUserStatus(userProfileVO, request);
	}

	@RequestMapping(value = "/unlockUser", method = RequestMethod.POST)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO unlockUser(@RequestBody UserProfileVO userProfileVO, HttpServletRequest request) {
		userProfileVO.setIsActive(1);
		return updateUserStatus(userProfileVO, request);
	}

	private BaseJsonVO updateUserStatus(UserProfileVO userProfileVO, HttpServletRequest request) {
		BaseJsonVO ret = new BaseJsonVO();
		if (userProfileVO.getUid() < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "参数错误！");
			return ret;
		}
		UserProfileDTO profileDTO = userFacade.getUserBaseInfo(userProfileVO.getUid());
		if (profileDTO == null || profileDTO.getUserId() < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户不存在！");
			return ret;
		}
		if (profileDTO.getIsValid() == userProfileVO.getIsActive()) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "操作成功！");
			return ret;
		}
		profileDTO.setIsValid(userProfileVO.getIsActive());
		try {
			logger.info("update user status. UserId : {}, AgentId : {}, ip : {}.", profileDTO.getUserId(),
					SecurityContextUtils.getUserId(), IPUtils.getIpAddr(request));
			if (userFacade.updateUserBaseInfo(new AccountDTO(), profileDTO) > 0) {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "操作成功！");
				return ret;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		ret.setCodeAndMessage(ResponseCode.RES_ERROR, "操作失败！");
		return ret;
	}

	@RequestMapping(value = "/consigneeAddress/add", method = RequestMethod.POST)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO addAddress(@RequestBody ConsigneeAddressDTO address) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = address.getUserId();
		// 设置省Id,从区ID中取
		if (address.getProvinceId() == 0 && address.getSectionId() != 0) {
			address.setProvinceId(DistrictCodeUtil.getProvinceCode(String.valueOf(address.getSectionId())));
		}
		List<ConsigneeAddressDTO> consigneeAddressDTOs = consigneeAddressFacade.listAddress(uid);
		if (consigneeAddressDTOs != null && consigneeAddressDTOs.size() > 10) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("最多添加10个地址！");
			return ret;
		}
		String errorMsg = validConsigneeAddress(address);
		if (StringUtils.isNotEmpty(errorMsg)) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage(errorMsg);
			return ret;
		}
		address.setAddFrom(PlatformType.CMS.getIntValue());
		ConsigneeAddressDTO retAddress = consigneeAddressFacade.addAddress(uid, address);
		if (null != retAddress) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(retAddress);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("添加收货地址失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/consigneeAddress/update", method = RequestMethod.POST)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO updateAddress(@RequestBody ConsigneeAddressDTO address) {
		BaseJsonVO ret = new BaseJsonVO();
		address.setAddFrom(PlatformType.CMS.getIntValue());
		ConsigneeAddressDTO retAddress = consigneeAddressFacade.updateAddress(address.getUserId(), address);
		if (null != retAddress) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(retAddress);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("更新收货地址失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/consigneeAddress/get", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO getAddress(@RequestParam(required = true, value = "addressId") long addressId,
			@RequestParam(required = true, value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		ConsigneeAddressDTO retAddress = consigneeAddressFacade.getAddressById(addressId,
				SecurityContextUtils.getUserId());
		if (null != retAddress) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(retAddress);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("获取收货地址失败！");
		}
		return ret;
	}

	/**
	 * 删除收货地址
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/consigneeAddress/del", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO delAddress(@RequestParam(required = true, value = "id") long id,
			@RequestParam(required = true, value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		if (consigneeAddressFacade.deleteAddress(id, userId)) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult("删除收货地址成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setResult("删除收货地址失败！");
		}
		return ret;
	}

	/**
	 * 设置默认收货地址
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/consigneeAddress/setDefault", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:userDetail" })
	public @ResponseBody BaseJsonVO setDefaultAddress(@RequestParam(value = "id") long id,
			@RequestParam(required = true, value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		if (consigneeAddressFacade.setDefault(id, userId)) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult("设置成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setResult("设置失败！");
		}
		return ret;
	}

	private String validConsigneeAddress(ConsigneeAddressDTO address) {
		if (StringUtils.isEmpty(address.getAddress())) {
			return "详细地址不能为空!";
		}
		if (StringUtils.isEmpty(address.getConsigneeName())) {
			return "收货人不能为空!";
		}
		if (!PhoneNumberUtil.isMobilePhone(address.getConsigneeMobile())) {
			return "手机号码格式有误!";
		}
		if (address.getProvinceId() == 0 || address.getCityId() == 0) {
			return "所在地区不能为空!";
		}
		return null;
	}

	@RequestMapping(value = "/getArea/province", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:userDetail", "location:area" }, logical = Logical.AND)
	public @ResponseBody JSONObject getProvince() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		jsonObject.put("result", locationFacade.getAllProvince());
		return jsonObject;
	}

	@RequestMapping(value = "/getArea/city", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:userDetail", "location:area" }, logical = Logical.AND)
	public @ResponseBody JSONObject getCityList(@RequestParam long code) {
		return locationFacade.getCityList(code);
	}

	@RequestMapping(value = "/getArea/district", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:userDetail", "location:area" }, logical = Logical.AND)
	public @ResponseBody JSONObject getDistrictList(@RequestParam long code) {
		return locationFacade.getDistrictList(code);
	}

	@RequestMapping(value = "/getArea/street", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:userDetail", "location:area" }, logical = Logical.AND)
	public @ResponseBody JSONObject getStreetList(@RequestParam long code) {
		return locationFacade.getStreetList(code);
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@RequiresPermissions({ "userInfo:create" })
	public String createUser() {
		return "/pages/userInfo/create";
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@RequiresPermissions({ "userInfo:userDetail" })
	@BILog(clientType = "cms", type = "cmsUserRegist", action = "click")
	public @ResponseBody BaseJsonVO addUser(@RequestBody UserProfileVO userProfileVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (!authorityFacade.isContainPermission(SecurityContextUtils.getUserId(), "userInfo:create")) {
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "没有添加用户的权限！");
			return ret;
		}
		if (StringUtils.isBlank(userProfileVO.getAccount())) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "参数错误！");
			return ret;
		}
		// 用户名是否存在
		String userName = userProfileVO.getAccount();
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		AccountDTO accountDTO = accountFacade.findAccountByUserName(userName);
		if (accountDTO != null) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "用户名已存在！");
			return ret;
		}
		if (!RegexUtils.isValidPassword(userProfileVO.getPassword())) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "密码为6-20数字或字母组合");
			return ret;
		}
		UserProfileDTO profileDTO = new UserProfileDTO();
		profileDTO.setUserName(userName);
		if (StringUtils.isBlank(userProfileVO.getNick())) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "昵称不能为空！");
			return ret;
		}
		String result = checkUserParam(userProfileVO, profileDTO);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, result);
			return ret;
		}
		profileDTO.setUserType(userProfileVO.getUserType());
		profileDTO.setRegTime(System.currentTimeMillis());
		profileDTO.setIsValid(1);
		profileDTO.setPlatformType(PlatformType.CMS.getIntValue());
		accountDTO = new AccountDTO();
		accountDTO.setEmail(profileDTO.getEmail() == null ? "" : profileDTO.getEmail());
		accountDTO.setUsername(userName);
		accountDTO.setPassword(userProfileVO.getPassword());
		long id = userFacade.addUser(accountDTO, profileDTO);
		if (id < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "添加失败！");
		} else {
			userProfileVO.setUid(id);
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "添加成功！");
			ret.setResult(id);
		}
		return ret;
	}

	private String checkUserParam(UserProfileVO userProfileVO, UserProfileDTO profileDTO) {
		// 性别
		if (userProfileVO.getSex() >= 0) {
			if (Gender.getGenderByIntValue(userProfileVO.getSex()).getIntValue() == -1) {
				return "性别参数错误！";
			}
			profileDTO.setGender(Gender.getGenderByIntValue(userProfileVO.getSex()));
		}
		// 手机
		if (StringUtils.isNotBlank(userProfileVO.getMobileNumber())) {
			if (!PhoneNumberUtil.isMobilePhone(userProfileVO.getMobileNumber())) {
				return "请输入正确的手机号！";
			}
			profileDTO.setMobile(userProfileVO.getMobileNumber());
		}
		// 生日
		if (StringUtils.isNotBlank(userProfileVO.getBirth())) {
			if (!RegexUtils.isAllNumber(userProfileVO.getBirth().replaceAll("-", "").trim())) {
				return "生日参数错误！";
			}
			String[] date = userProfileVO.getBirth().split("-");
			if (date.length != 3) {
				return "生日参数错误！";
			}
			profileDTO.setBirthYear(Integer.parseInt(date[0].trim()));
			profileDTO.setBirthMonth(Integer.parseInt(date[1].trim()));
			profileDTO.setBirthDay(Integer.parseInt(date[2].trim()));
		}
		// email
		if (StringUtils.isNotBlank(userProfileVO.getEmailAddress())) {
			if (userProfileVO.getEmailAddress().trim().length() > 32) {
				return "邮箱长度过长！";
			} else {
				profileDTO.setEmail(userProfileVO.getEmailAddress().trim());
			}
		}
		// 昵称
		if (StringUtils.isNotBlank(userProfileVO.getNick())) {
			if (userProfileVO.getNick().trim().length() > 64) {
				return "昵称长度过长！";
			} else {
				profileDTO.setNickName(userProfileVO.getNick().trim());
			}
		}
		// 许可证号
		if (StringUtils.isNotBlank(userProfileVO.getLicence())) {
			if (userProfileVO.getLicence().trim().length() > 64) {
				return "许可证长度过长！";
			} else {
				profileDTO.setLicence(userProfileVO.getLicence().trim());
			}
		}
		// 头像
		if (StringUtils.isNotBlank(userProfileVO.getUserPhoto())) {
			if (userProfileVO.getUserPhoto().trim().length() > 64) {
				return "头像地址长度过长！";
			} else {
				profileDTO.setUserImageURL(userProfileVO.getUserPhoto().trim());
			}
		}
		return null;
	}

	@RequestMapping(value = "/proxy", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:proxy" })
	public String getValetList(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/userInfo/proxy";
	}

	/**
	 * 搜索小b
	 * 
	 * @param searchValue
	 * @return
	 */
	@RequestMapping(value = "/proxy/searchProxyUser", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:proxy" })
	public @ResponseBody BaseJsonVO searchProxyUserList(BasePageParamVO<UserProfileVO> basePageParamVO,
			String searchValue) {
		List<UserProfileVO> userProfileVOs = null;
		if (basePageParamVO.getIsPage() == 1) {
			userProfileVOs = userFacade.queryUserList(basePageParamVO, searchValue);
		} else {
			userProfileVOs = userFacade.queryUserList(null, searchValue);
		}
		BasePageParamVO<ValetUserVO> pageParamVO = new BasePageParamVO<ValetUserVO>();
		pageParamVO = basePageParamVO.copy(pageParamVO);

		if (CollectionUtil.isNotEmptyOfList(userProfileVOs)) {
			List<ValetUserVO> valetUserVOs = new ArrayList<ValetUserVO>(userProfileVOs.size());
			ValetUserVO valetUserVO = null;
			for (UserProfileVO userProfileVO : userProfileVOs) {
				valetUserVO = new ValetUserVO(userProfileVO);
				ConsigneeAddressDTO address = consigneeAddressFacade.getDefaultConsigneeAddress(userProfileVO.getUid());
				valetUserVO.setAddress(address != null ? address.mergeAddress() : "");
				valetUserVOs.add(valetUserVO);
			}
			pageParamVO.setList(valetUserVOs);
		}
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(pageParamVO);
		return ret;
	}

	@RequestMapping(value = "/proxy/userLogin", method = RequestMethod.GET)
	@RequiresPermissions(value = { "userInfo:proxy" })
	public ModelAndView valetOrder(Model model, @RequestParam(value = "userId", required = true) long userId) {
		// UserProfileDTO userProfileDTO = userFacade.getUserBaseInfo(userId);
		// if (userProfileDTO == null || userProfileDTO.getUserId() < 1l) {
		// model.addAttribute("code", ResponseCode.RES_ENOTEXIST);
		// model.addAttribute("message", "用户不存在！");
		// model.addAttribute("pages",
		// leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		// return new ModelAndView("pages/userInfo/proxy");
		// }
		return new ModelAndView("redirect:http://denglu.baiwandian.cn/proxy/userLogin?userId=" + userId);
	}
}
