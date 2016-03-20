/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.cms.dto.BusiUserRelationDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.UserInfoFacade;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.enums.PlatformType;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.vo.BindSuppliersVO;
import com.xyl.mmall.vo.UserConsigneeAddressVO;

/**
 * UserController.java created by yydx811 at 2015年7月30日 下午1:47:10
 * 用户相关controller
 *
 * @author yydx811
 */
@RestController
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private static Set<String> judianAccountSet = new HashSet<String>(2) {
		private static final long serialVersionUID = -9014399065840116983L;
		{
			add("xyl-authen-1000000");
			add("yydx811");
		}
	};

	@Autowired
	private UserInfoFacade userFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;
	
	@Autowired
	private LocationFacade locationFacade;

	@Autowired
	private BusinessService businessService;
	
	@RequestMapping(value = "/user/userBasicInfo", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getUserInfo(
			@RequestParam(value = "userId", required = true) long userId,
    		@RequestParam(value = "appid") String appid) {
		BaseJsonVO retJson = new BaseJsonVO();
		retJson.setCode(ResponseCode.RES_SUCCESS);
		retJson.setResult(userFacade.getUserDetailInfo(userId));
		if (logger.isDebugEnabled()) {
			logger.debug("Get userInfo from erp! Appid : " + appid + ", UserId : " + userId);
		}
		return retJson;
	}

	@RequestMapping(value = "/user/addConsigneeAddress", method = RequestMethod.POST)
	public BaseJsonVO addUserConsigneeAddress(UserConsigneeAddressVO addressVO, 
			@RequestParam(value = "appid") String appid) {
		BaseJsonVO retJson = new BaseJsonVO();
		if (!judianAccountSet.contains(appid)) {
			retJson.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "Not Found!");
			return retJson;
		}
		String errorMsg = validConsigneeAddress(addressVO);
		if (StringUtils.isNotEmpty(errorMsg)) {
			retJson.setCodeAndMessage(ResponseCode.RES_ERROR, errorMsg);
			return retJson;
		}
		long uid = addressVO.getUserId();
		List<ConsigneeAddressDTO> consigneeAddressDTOs = consigneeAddressFacade.listAddress(uid);
		if (consigneeAddressDTOs != null && consigneeAddressDTOs.size() > 10) {
			retJson.setCodeAndMessage(ResponseCode.RES_ERROR, "最多添加10个地址！");
			return retJson;
		}
		ConsigneeAddressDTO address = addressVO.convertToDTO();
		address.setAddFrom(PlatformType.PUSH.getIntValue());
		address.setCtime(System.currentTimeMillis());
		ConsigneeAddressDTO retAddress = consigneeAddressFacade.addAddress(uid, address);
		if (null != retAddress) {
			retJson.setCode(ResponseCode.RES_SUCCESS);
			retJson.setResult(retAddress.getId());
			logger.info("Add user consigneeAddress sucessful! appId : {}, addressId : {}.", 
					appid, retAddress.getId());
		} else {
			retJson.setCodeAndMessage(ResponseCode.RES_ERROR, "添加收货地址失败！");
			logger.info("Add user consigneeAddress failed! appId : {}, userId : {}.", appid, uid);
		}
		return retJson;
	}

	@RequestMapping(value = "/user/bindSuppliers", method = RequestMethod.POST)
	public BaseJsonVO userBindSuppliers(BindSuppliersVO bindSuppliersVO,
			@RequestParam(value = "appid") String appid) {
		BaseJsonVO retJson = new BaseJsonVO();
		if (!judianAccountSet.contains(appid)) {
			retJson.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "Not Found!");
			return retJson;
		}
		String userName = bindSuppliersVO.getUserName();
		// 用户校验
		if (StringUtils.isBlank(userName)) {
			retJson.setCodeAndMessage(ResponseCode.RES_EPARAM, "用户名不能为空！");
			return retJson;
		}
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName += MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		long userId = userFacade.getUserIdByUserName(userName);
		if (userId < 1l) {
			retJson.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "用户不存在！");
			return retJson;
		}
		List<String> businessAccountList = bindSuppliersVO.getBusinessAccountList();
		// 绑定商家
		if (CollectionUtils.isEmpty(businessAccountList)) {
			retJson.setCodeAndMessage(ResponseCode.RES_EPARAM, "绑定列表不能为空！");
			return retJson;
		}
		// 绑定校验
		StringBuilder result = new StringBuilder(128);
		List<String> list = new ArrayList<String>(businessAccountList.size());
		for (String account : businessAccountList) {
			if (StringUtils.isNotBlank(account) && !list.contains(account)) {
				list.add(account);
				if (!StringUtils.endsWith(account, MmallConstant.BUSINESS_ACCOUNT_SUFFIX)) {
					account += MmallConstant.BUSINESS_ACCOUNT_SUFFIX;
				}
				Business business = businessService.getBusinessAccount(account);
				if (business == null || business.getId() < 1l || business.getIsActive() != 0) {
					logger.info("Supplier is not invalid! BusinessAccount : {}, appId : {}.", account, appid);
					continue;
				}
				if (business.getType() == SupplierType.SPECIALMANAGE.getIntValue()) {
					// 验证是否已绑定
					if (businessService.isUserBusinessAllowed(business.getId(), userId)) {
						logger.info("User had already bind to supplier! UserId : {}, businessId : {}, appId : {}.", 
								userId, business.getId(), appid);
						continue;
					}
					BusiUserRelationDTO relationDTO = new BusiUserRelationDTO();
					relationDTO.setBusinessId(business.getId());
					relationDTO.setUserId(userId);
					relationDTO.setUserName(userName);
					if (businessService.bindBusiUserRelation(relationDTO)) {
						logger.info("User bind to supplier successful! UserId : {}, businessId : {}, appId : {}.", 
								userId, relationDTO.getBusinessId(), appid);
						result.append(list.get(list.size() - 1)).append(",");
					} else {
						logger.info("User bind to supplier failed! UserId : {}, businessId : {}, appId : {}.", 
								userId, relationDTO.getBusinessId(), appid);
					}
				} else {
					logger.info("Supplier is not a special provision! BusinessId : {}, appId : {}.", 
							business.getId(), appid);
				}
			}
		}
		retJson.setCodeAndMessage(ResponseCode.RES_SUCCESS, "批处理完成！");
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
			retJson.setResult(result.toString());
		}

		return retJson;
	}
	
	private String validConsigneeAddress(UserConsigneeAddressVO address) {
		// 用户是否存在
		String userName = address.getUserName();
		if (StringUtils.isBlank(userName)) {
			return "用户名不能为空！";
		}
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName += MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		long userId = userFacade.getUserIdByUserName(userName);
		if (userId < 1l) {
			return "用户不存在！";
		}
		address.setUserId(userId);
		// 地址code校验
		long provinceCode = address.getProvinceCode(), 
			 cityCode = address.getCityCode(), 
		     sectionCode = address.getSectionCode(),
		     streetCode = address.getStreetCode();
		LocationCode locationCode = locationFacade.getLocationCodeByCode(streetCode);
		if (locationCode == null || locationCode.getParentCode() != sectionCode) {
			return "地址错误！";
		}
		locationCode = locationFacade.getLocationCodeByCode(sectionCode);
		if (locationCode == null || locationCode.getParentCode() != cityCode) {
			return "地址错误！";
		}
		locationCode = locationFacade.getLocationCodeByCode(cityCode);
		if (locationCode == null || locationCode.getParentCode() != provinceCode) {
			return "地址错误！";
		}
		locationCode = locationFacade.getLocationCodeByCode(provinceCode);
		if (locationCode == null || locationCode.getParentCode() != 0) {
			return "地址错误！";
		}
		// 详细地址
		if (StringUtils.isBlank(address.getDetailAddress())) {
			return "详细地址不能为空！";
		}
		if (address.getDetailAddress().length() > 64) {
			return "详细地址不能大于64个字符！";
		}
		// 收货人
		if (StringUtils.isBlank(address.getConsigneeName())) {
			return "收货人不能为空！";
		}
		if (address.getConsigneeName().length() > 25) {
			return "收货人不能大于64个字符！";
		}
		// 收货人手机号
		if (!PhoneNumberUtil.isMobilePhone(address.getMobile())) {
			return "手机号码格式有误！";
		}
		// 选填项
		// 邮编
		String zipCode = address.getZipCode();
		if (StringUtils.isNotBlank(zipCode)) {
			zipCode = zipCode.trim();
			if (zipCode.length() > 8) {
				return "邮编最长8位！";
			}
			address.setZipCode(zipCode);
		}
		// 地址备注
		if (StringUtils.isNotBlank(address.getAddressComment())) {
			if (address.getAddressComment().length() > 100) {
				return "地址备注最长100位！";
			}
		}
		// 默认
		if (address.getIsDefault() != 1 && address.getIsDefault() != 0) {
			address.setIsDefault(0);
		}
		// 收货人座机
		if (StringUtils.isNotBlank(address.getConsigneeTel())) {
			if (!PhoneNumberUtil.isFixedPhone(address.getConsigneeTel())) {
				return "座机格式错误！";
			}
		}
		return null;
	}
}
