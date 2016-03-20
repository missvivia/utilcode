package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.enums.PlatformType;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * 收货地址
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月8日 下午1:43:48
 * 
 */

@Controller
@RequestMapping("/profile/address")
public class ConsigneeAddressController {

	@Autowired
	private ConsigneeAddressFacade caFacade;
	
	@Autowired
	private LocationFacade locationFacade;

	@BILog(action = "page", type = "addressPage")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String address(Model model) {
		return "pages/profile/address";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO add(@RequestBody ConsigneeAddressDTO address) {
		BaseJsonVO ret = new BaseJsonVO();
		String result = setAreaCode(address);
		if (result != null) {
			ret.setCode(ResponseCode.RES_EPARAM);
			ret.setMessage(result);
			return ret;
		}
		List<ConsigneeAddressDTO> consigneeAddressDTOs = caFacade.listAddress(SecurityContextUtils.getUserId());
		if (consigneeAddressDTOs != null && consigneeAddressDTOs.size() > 10) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("最多添加10个地址!");
			return ret;
		}
		result = validConsigneeAddress(address);
		if (result != null) {
			ret.setCode(ResponseCode.RES_EPARAM);
			ret.setMessage(result);
			return ret;
		}
		address.setAddFrom(PlatformType.MAINSITE.getIntValue());
		ConsigneeAddressDTO retAddress = caFacade.addAddress(SecurityContextUtils.getUserId(), address);
		if (null != retAddress) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(retAddress);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("添加地址失败");
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

	private String setAreaCode(ConsigneeAddressDTO addressDTO) {
		// 地址code设置
	    long sectionCode = addressDTO.getSectionId();
	    LocationCode locationCode = locationFacade.getLocationCodeByCode(sectionCode);
		if (locationCode == null || locationCode.getCode() != sectionCode) {
			return "地址错误！";
		}
		addressDTO.setCityId(locationCode.getParentCode());
		locationCode = locationFacade.getLocationCodeByCode(addressDTO.getCityId());
		if (locationCode == null || locationCode.getCode() != addressDTO.getCityId()) {
			return "地址错误！";
		}
		addressDTO.setProvinceId(locationCode.getParentCode());
		return null;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO list(Model model) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(caFacade.listAddress(SecurityContextUtils.getUserId()));
		return ret;
	}

	/**
	 * 返回所在区的收货地址
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listBySectionId", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO listAddressBySectionId(Model model) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		List<ConsigneeAddressDTO> consigneeAddressDTOs = caFacade.listAddress(SecurityContextUtils.getUserId());
		List<ConsigneeAddressDTO> visibleAddressDTOs = new ArrayList<ConsigneeAddressDTO>();
		if (consigneeAddressDTOs != null) {
			for (ConsigneeAddressDTO consigneeAddressDTO : consigneeAddressDTOs) {
				if (AreaUtils.getAreaCode() == consigneeAddressDTO.getSectionId()) {
					visibleAddressDTOs.add(consigneeAddressDTO);
				}

			}
		}
		ret.setResult(visibleAddressDTOs);
		return ret;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO update(@RequestBody ConsigneeAddressDTO address) {
		BaseJsonVO ret = new BaseJsonVO();
		address.setAddFrom(PlatformType.MAINSITE.getIntValue());
		ConsigneeAddressDTO retAddress = caFacade.updateAddress(SecurityContextUtils.getUserId(), address);
		if (null != retAddress) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(retAddress);
		} else {
			ret.setCode(201);
			ret.setMessage("更新地址失败");
		}
		return ret;
	}

	@RequestMapping(value = "/editPage", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO editPage(int addressId) {
		BaseJsonVO ret = new BaseJsonVO();
		ConsigneeAddressDTO retAddress = caFacade.getAddressById(addressId, SecurityContextUtils.getUserId());
		if (null != retAddress) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(retAddress);
		} else {
			ret.setCode(201);
			ret.setMessage("获取地址失败");
		}
		return ret;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO delete(@RequestParam(value = "id") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		if (caFacade.deleteAddress(id, SecurityContextUtils.getUserId())) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(true);
		} else {
			ret.setCode(201);
			ret.setResult(false);
		}
		return ret;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/setdefault", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO setDefault(@RequestParam(value = "id") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		if (caFacade.setDefault(id, SecurityContextUtils.getUserId())) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setResult(true);
		} else {
			ret.setCode(201);
			ret.setResult(false);
		}
		return ret;
	}
}
