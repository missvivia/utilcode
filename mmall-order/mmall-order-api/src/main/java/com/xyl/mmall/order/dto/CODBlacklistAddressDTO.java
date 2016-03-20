package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.CODBlacklistAddress;
import com.xyl.mmall.order.param.CODWBlistAddressParam;

/**
 * 黑名单信息DTO
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:51:57
 *
 */
public class CODBlacklistAddressDTO extends CODBlacklistAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 766669464131340180L;

	/**
	 * 构造函数
	 */
	public CODBlacklistAddressDTO() {
	}

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CODBlacklistAddressDTO(CODBlacklistAddress obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public void fillWithAddressParam(long auditUserId, CODWBlistAddressParam param) {
		if(null == param) {
			return;
		}
		setAuditUserId(auditUserId);
		setUserId(param.getUserId());
		setCtime(System.currentTimeMillis());
		setProvince(param.getProvince());
		setCity(param.getCity());
		setSection(param.getSection());
		setStreet(param.getStreet());
		setAddress(param.getAddress());
		setConsigneeMobile(param.getConsigneeMobile());
	}
	
	public boolean hitBlack(ConsigneeAddressDTO caDTO) {
		if(null == caDTO || getUserId() != caDTO.getUserId()) {
			return false;
		}
		if(!getConsigneeMobile().equals(caDTO.getConsigneeMobile()) && !getConsigneeMobile().equals(caDTO.getConsigneeTel())) {
			return false;
		}
		if(!getProvince().equals(caDTO.getProvince())) {
			return false;
		}
		if(!getCity().equals(caDTO.getCity())) {
			return false;
		}
		if(!getSection().equals(caDTO.getSection())) {
			return false;
		}
		if(!getStreet().equals(caDTO.getStreet())) {
			return false;
		}
		if(!getAddress().equals(caDTO.getAddress())) {
			return false;
		}
		return true;
	}
}
