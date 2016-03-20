package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.CODWhitelistAddress;
import com.xyl.mmall.order.param.CODWBlistAddressParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月16日 下午2:15:40
 *
 */
public class CODWhitelistAddressDTO extends CODWhitelistAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7300692050168816329L;
	
	public CODWhitelistAddressDTO() {
	}
	
	public CODWhitelistAddressDTO(CODWhitelistAddress obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public void fillWithAddressParam(long auditUserId, CODWBlistAddressParam param) {
		if(null == param) {
			return;
		}
		setAuditUserId(auditUserId);
		setUserId(param.getUserId());
		setCtime(System.currentTimeMillis());
		setAddress(param.getAddress());
		setFullAddress(param.mergeAddress());
		setConsigneeMobile(param.getConsigneeMobile());
	}

	public boolean hitWhite(OrderExpInfoDTO expDTO) {
		if(null == expDTO || getUserId() != expDTO.getUserId()) {
			return false;
		}
		if(!getConsigneeMobile().equals(expDTO.getConsigneeMobile()) && !getConsigneeMobile().equals(expDTO.getConsigneeTel())) {
			return false;
		}
		if(!getAddress().equals(expDTO.getAddress())) {
			return false;
		}
		if(!getFullAddress().equals(expDTO.fullAddress())) {
			return false;
		}
		return true;
	}
}
