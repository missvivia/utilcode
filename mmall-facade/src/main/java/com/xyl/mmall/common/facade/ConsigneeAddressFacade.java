package com.xyl.mmall.common.facade;

import java.util.List;

import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * 收货地址Facade
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午2:44:07
 *
 */
public interface ConsigneeAddressFacade {

	public ConsigneeAddressDTO addAddress(long userId, ConsigneeAddressDTO address);
	
	public List<ConsigneeAddressDTO> listAddress(long userId);
	
	public ConsigneeAddressDTO getOneAddress(long userId);
	
	public ConsigneeAddressDTO updateAddress(long userId, ConsigneeAddressDTO address);

	public boolean deleteAddress(long id, long userId);
	
	public boolean setDefault(long id, long userId);

	public ConsigneeAddressDTO getDefaultConsigneeAddress(long userId);
	
	public ConsigneeAddressDTO getAddressById(long id, long userId);
}
