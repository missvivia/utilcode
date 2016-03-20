package com.xyl.mmall.common.facade.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.order.annotation.ConsigneeAddressFiller;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.service.ConsigneeAddressService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月8日 下午3:04:25
 *
 */
@Facade("consigneeAddressFacade")
public class ConsigneeAddressFacadeImpl implements ConsigneeAddressFacade {

	@Resource
	private ConsigneeAddressService caService;

	@Override
	@ConsigneeAddressFiller
	public ConsigneeAddressDTO addAddress(long userId, ConsigneeAddressDTO address) {
		if (!isValidAddress(address)) {
			throw new ServiceException("输入的地址信息错误！");
		}
		return caService.addConsigneeAddress(userId, address);
	}

	private boolean isValidAddress(ConsigneeAddressDTO address) {
		return StringUtils.isNotBlank(address.getAddress())
				&& (StringUtils.isNotBlank(address.getConsigneeMobile()) || StringUtils.isNotBlank(address
						.getConsigneeTel())) && StringUtils.isNotBlank(address.getConsigneeName())
				&& address.getProvinceId() != 0 && address.getCityId() != 0;
	}

	@ConsigneeAddressFiller
	public ConsigneeAddressDTO getAddressById(long id, long userId) {
		return caService.getConsigneeAddressByIdAndUserId(id, userId);
	}

	@Override
	@ConsigneeAddressFiller
	public List<ConsigneeAddressDTO> listAddress(long userId) {
		return caService.getConsigneeAddressListByUserId(userId);
	}

	@Override
	@ConsigneeAddressFiller
	public ConsigneeAddressDTO getOneAddress(long userId) {
		return CollectionUtil.getFirstObjectOfCollection(caService.queryInfoByUserId(userId, DDBParam.genParam1()));
	}

	@Override
	@ConsigneeAddressFiller
	public ConsigneeAddressDTO updateAddress(long userId, ConsigneeAddressDTO address) {
		if (!isValidAddress(address)) {
			throw new ServiceException("输入的地址信息错误！");
		}
		if (caService.updateConsigneeAddress(userId, address)) {
			return address;
		}
		return null;
	}

	@Override
	public boolean deleteAddress(long id, long userId) {
		return caService.deleteConsigneeAddressByIdAndUserId(id, userId);
	}

	@Override
	public boolean setDefault(long id, long userId) {
		return caService.setDefaultConsigneeAddress(id, userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ConsigneeAddressFacade#getDefaultConsigneeAddress(long)
	 */
	@Override
	@ConsigneeAddressFiller
	public ConsigneeAddressDTO getDefaultConsigneeAddress(long userId) {
		return caService.getDefaultConsigneeAddress(userId);
	}
}
