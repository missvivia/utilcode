package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.order.dao.ConsigneeAddressDao;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.meta.ConsigneeAddress;
import com.xyl.mmall.order.service.ConsigneeAddressService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月11日 下午4:57:07
 *
 */
@Service("consigneeAddressService")
public class ConsigneeAddressServiceImpl implements ConsigneeAddressService {

	@Autowired
	private ConsigneeAddressDao consigneeAddressDao;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#getConsigneeAddressListByUserId(long)
	 */
	@Override
	public List<ConsigneeAddressDTO> getConsigneeAddressListByUserId(long userId) {
		List<ConsigneeAddress> caList = consigneeAddressDao.getConsigneeAddressListByUserId(userId);
		List<ConsigneeAddressDTO> ret = new ArrayList<ConsigneeAddressDTO>(caList.size());
		ConsigneeAddressDTO defaultAddress = null;
		for (ConsigneeAddress ca : caList) {
			if (ca.getIsDefault()) {
				defaultAddress = new ConsigneeAddressDTO(ca);
			} else {
				ret.add(new ConsigneeAddressDTO(ca));
			}
		}
		if (defaultAddress != null) {
			ret.add(0, defaultAddress);
		}
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#getConsigneeAddressByIdAndUserId(long,
	 *      long)
	 */
	@Override
	public ConsigneeAddressDTO getConsigneeAddressByIdAndUserId(long id, long userId) {
		ConsigneeAddress ca = consigneeAddressDao.getConsigneeAddressByIdAndUserId(id, userId);
		return (null == ca) ? null : new ConsigneeAddressDTO(ca);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#addConsigneeAddress(long,
	 *      com.xyl.mmall.order.dto.ConsigneeAddressDTO)
	 */
	@Override
	@Transaction
	public ConsigneeAddressDTO addConsigneeAddress(long userId, ConsigneeAddressDTO consigneeAddressDto) {
		consigneeAddressDto.setUserId(userId);
		// 如果当前更新的地址是默认的，就先把原来的默认地址设置为非默认
		if (consigneeAddressDto.isDefault()) {
			ConsigneeAddress defAddress = getDefaultConsigneeAddress(userId);
			if (defAddress != null) {
				if (!consigneeAddressDao.updateAllDefault(userId, false)) {
					throw new ServiceException("更新默认收货地址失败");
				}
			}
		}
		consigneeAddressDto.setId(consigneeAddressDao.allocateRecordId());
		consigneeAddressDto.setCtime(System.currentTimeMillis());
		ConsigneeAddress ca = consigneeAddressDao.addObject(consigneeAddressDto);
		return (null == ca) ? null : new ConsigneeAddressDTO(ca);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#deleteConsigneeAddressByIdAndUserId(long,
	 *      long)
	 */
	@Override
	@Transaction
	public boolean deleteConsigneeAddressByIdAndUserId(long id, long userId) {
		ConsigneeAddressDTO existingAddress = getConsigneeAddressByIdAndUserId(id, userId);
		if (null == existingAddress) {
			throw new ServiceException("用户id冲突");
		}
		return consigneeAddressDao.deleteConsigneeAddress(id, userId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#updateConsigneeAddress(long,
	 *      com.xyl.mmall.order.dto.ConsigneeAddressDTO)
	 */
	@Override
	@Transaction
	public boolean updateConsigneeAddress(long userId, ConsigneeAddressDTO consigneeAddress) {
		boolean bRet = false;

		// 如果id小于等于0，说明是新加的地址
		if (consigneeAddress.getId() <= 0) {
			consigneeAddress.setUserId(userId);
			ConsigneeAddress address = addConsigneeAddress(userId, consigneeAddress);
			bRet = address != null;

		} else {
			ConsigneeAddressDTO existingAddress = getConsigneeAddressByIdAndUserId(consigneeAddress.getId(), userId);
			if (null == existingAddress) {
				throw new ServiceException("用户id冲突");
			}

			// 如果当前更新的地址是默认的，就先把原来的默认地址设置为非默认
			if (consigneeAddress.isDefault()) {
				ConsigneeAddress defAddress = getDefaultConsigneeAddress(userId);
				if (defAddress != null) {
					if (!consigneeAddressDao.updateAllDefault(userId, false)) {
						throw new ServiceException("更新默认收货地址失败");
					}
				}
			}

			consigneeAddress.setUserId(userId);
			bRet = consigneeAddressDao.updateConsigneeAddress(consigneeAddress);

			if (!bRet) {
				throw new ServiceException("更新收货地址失败");
			}
		}

		return bRet;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#setDefaultConsigneeAddress(long,
	 *      long)
	 */
	@Override
	@Transaction
	public boolean setDefaultConsigneeAddress(long id, long userId) {
		ConsigneeAddressDTO existingAddress = getConsigneeAddressByIdAndUserId(id, userId);
		if (null == existingAddress) {
			throw new ServiceException("用户id冲突");
		}
		ConsigneeAddress defAddress = getDefaultConsigneeAddress(userId);
		if (defAddress != null) {
			// 先将当前默认收货地址设置为false，再将新的收货地址设为默认
			if (!consigneeAddressDao.updateAllDefault(userId, false)) {
				throw new ServiceException("更新收货地址失败");
			}
		}
		if (!consigneeAddressDao.updateDefault(id, userId, true)) {
			throw new ServiceException("更新收货地址失败");
		}

		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#getDefaultConsigneeAddress(long)
	 */
	@Override
	public ConsigneeAddressDTO getDefaultConsigneeAddress(long userId) {
		ConsigneeAddress ca = consigneeAddressDao.getDefaultByUserId(userId);
		return (null == ca) ? null : new ConsigneeAddressDTO(ca);
	}

	@Override
	public List<ConsigneeAddressDTO> queryInfoByUserId(long userId, DDBParam ddbParam) {
		return convertMetaToDTO(consigneeAddressDao.queryInfoListByUserId(userId, ddbParam));
	}

	@Override
	public RetArg queryInfoByUserId2(long userId, DDBParam ddbParam) {
		RetArg retArg = new RetArg();

		RetArgUtil.put(retArg, ddbParam);
		return retArg;
	}

	private List<ConsigneeAddressDTO> convertMetaToDTO(List<ConsigneeAddress> list) {
		if (null == list) {
			return null;
		}
		List<ConsigneeAddressDTO> dtoList = new ArrayList<ConsigneeAddressDTO>(list.size());
		for (ConsigneeAddress oei : list) {
			dtoList.add(new ConsigneeAddressDTO(oei));
		}
		return dtoList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#queryUserIdByConsigneeMobile(java.lang.String,
	 *      int, int)
	 */
	@Override
	public List<ConsigneeAddressDTO> queryUserIdByConsigneeMobile(String consigneeMobile, int limit, int offset) {
		DDBParam ddbParam = DDBParam.genParamX(limit);
		ddbParam.setOffset(offset);
		return convertMetaToDTO(consigneeAddressDao.queryUserIdByConsigneeMobile(consigneeMobile, ddbParam));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ConsigneeAddressService#countUserIdByConsigneeMobile(java.lang.String)
	 */
	@Override
	public int countUserIdByConsigneeMobile(String consigneeMobile) {
		return consigneeAddressDao.countUserIdByConsigneeMobile(consigneeMobile);
	}
}
