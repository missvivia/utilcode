package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.ReturnForm;

/**
 * 退货记录(订单维度)
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午12:24:29
 *
 */
public class ReturnFormDTO extends ReturnForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -591978774101455868L;

	/**
	 * 构造函数
	 */
	public ReturnFormDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public ReturnFormDTO(ReturnForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
//	/**
//	 * 退货关联的Order
//	 */
//	private OrderFormBriefDTO orderFormBriefDTO;
//	
//	/**
//	 * 退货关联的ReturnPackage
//	 * key: orderPackageId, value: _ReturnPackageDTO
//	 */
//	private Map<Long, _ReturnPackageDTO> retPkgMap = new HashMap<Long, _ReturnPackageDTO>();
//
//	public OrderFormBriefDTO getOrderFormBriefDTO() {
//		return orderFormBriefDTO;
//	}
//
//	public void setOrderFormBriefDTO(OrderFormBriefDTO orderFormBriefDTO) {
//		this.orderFormBriefDTO = orderFormBriefDTO;
//	}
//
//	public Map<Long, _ReturnPackageDTO> getRetPkgMap() {
//		return retPkgMap;
//	}
//
//	public void setRetPkgMap(Map<Long, _ReturnPackageDTO> retPkgMap) {
//		this.retPkgMap = retPkgMap;
//	}
	
}
