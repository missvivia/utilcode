/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.mobile.facade.MobileAddressFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.param.MobileAddressAO;
import com.xyl.mmall.mobile.facade.vo.MobileAreaOpVO;
import com.xyl.mmall.mobile.facade.vo.MobileConsigneeAddressVO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobileAddressFacadeImpl implements MobileAddressFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ConsigneeAddressFacade consigneeAddressFacade;
	@Resource
	private LocationFacade locationFacade;
	@Override
	public BaseJsonVO getConsigneeAddressList(long userId) {
		try {
			List<ConsigneeAddressDTO> dtos = consigneeAddressFacade.listAddress(userId);
			//ConsigneeAddressDTO def = consigneeAddressFacade.getDefaultConsigneeAddress(userId);
			//long def_id = (def == null ? 0 : def.getId());
			return Converter.listBaseJsonVO(converterDtoToVo(dtos), false);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	@Override
	public BaseJsonVO updateConsigneeAddress(long userId, MobileAddressAO ao) {
		// 3:删除，2：修改，1：新增
		try {
			MobileChecker.checkNull("INPUT DATE", ao);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}

		ConsigneeAddressDTO dto = converterVoToDto(ao);
		boolean success = false;
		try {
			switch (ao.getType()) {
			case 1:
				dto = consigneeAddressFacade.addAddress(userId, dto);
				success = (dto != null);
				break;
			case 2:
				dto = consigneeAddressFacade.updateAddress(userId, dto);
				success = (null != dto);
				break;
			case 3:
				success = consigneeAddressFacade.deleteAddress(ao.getAddressId(), userId);
				break;
			default:
				logger.error("not allowed type for user id:" + userId + " type:" + ao.getType());
				return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "type=" + ao.getType());
			}
			if (!success) {
				throw new ServiceException("unsuccess in service");
			}
			MobileConsigneeAddressVO vo =  coverterDtoTOVo(dto);
			vo.setIsDefault(ao.getIsDefault());
			return Converter.genrBaseJsonVO("address", vo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	
	private MobileConsigneeAddressVO coverterDtoTOVo(ConsigneeAddressDTO dto){
		MobileConsigneeAddressVO vo = new MobileConsigneeAddressVO();
		vo.setAddressId(dto.getId());
		vo.setAddressSuffix(dto.getAddress());
		vo.setCityId((int)dto.getCityId());
		vo.setCityName(dto.getCity());
		vo.setName(dto.getConsigneeName());
		vo.setPhone(dto.getConsigneeMobile());
		vo.setTel(dto.getConsigneeTel());
		vo.setProvinceId(dto.getProvinceId());
		vo.setProvinceName(dto.getProvince());
		vo.setDistrictId(dto.getSectionId());
		vo.setStreetId(dto.getStreetId());
		vo.setStreetName(dto.getStreet());
		vo.setDistrictName(dto.getSection());
		vo.setZipcode(dto.getZipcode());
		return vo;
	}
	
	private List<MobileConsigneeAddressVO> converterDtoToVo(List<ConsigneeAddressDTO> dtos) {
		List<MobileConsigneeAddressVO> vos = new ArrayList<MobileConsigneeAddressVO>();
		vos.add(null);
		if (dtos != null) {
			for (ConsigneeAddressDTO dto : dtos) {
				MobileConsigneeAddressVO vo =  coverterDtoTOVo(dto);
				if (dto.isDefault()){
					vo.setIsDefault(1);
					vos.set(0, vo);
				}else{
				vos.add(vo);
				}
			}
		}
		if(vos.get(0) == null){
			vos.remove(0);
		}
		return vos;
	}

	private ConsigneeAddressDTO converterVoToDto(MobileAddressAO ao) {
		ConsigneeAddressDTO dto = new ConsigneeAddressDTO();
		if (ao.getAddressId() != 0)
			dto.setId(ao.getAddressId());
		dto.setCityId(ao.getCityId());
		dto.setConsigneeMobile(ao.getPhone());
		dto.setConsigneeTel(ao.getTel());
		dto.setConsigneeName(ao.getName());
		dto.setDefault(ao.getIsDefault() == 1);
		dto.setProvinceId(ao.getProvinceId());
		dto.setSectionId(ao.getDistrictId());
		dto.setStreetId(ao.getStreetId());
		dto.setZipcode(ao.getZipcode());
		dto.setAddress(ao.getAddress());
		return dto;
	}

	@Override
	public BaseJsonVO getAreaCodeList(Long timeStamp) {
		try {
			MobileChecker.checkZero("LAST UPDATE TIME", timeStamp);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		
		List<LocationCode> codes = locationFacade.getLocationCodeListAfterTimeStamp(timeStamp);
		List<MobileAreaOpVO> areaOp = new ArrayList<MobileAreaOpVO>();
		
		try {
		if(codes == null || codes.size() == 0 || timeStamp < 1420041600000l) {
			return Converter.listTimeBaseJsonVO(areaOp, false);
		}
		for(LocationCode lc :codes){
			MobileAreaOpVO vo = new MobileAreaOpVO();
			vo.setAreaType(lc.getLevel().getIntValue());
			vo.setParentCode(lc.getParentCode());
			vo.setCode(lc.getCode());
			vo.setName(lc.getLocationName());
			//可用就是增加或修改,不可用就是删除
			if(lc.isValid()){
				vo.setOpType(1);
			}else{
				vo.setOpType(2);
			}
			areaOp.add(vo);		
		}
		return Converter.listTimeBaseJsonVO(areaOp, false);
		
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXCEPTION);
		}
	}
}
