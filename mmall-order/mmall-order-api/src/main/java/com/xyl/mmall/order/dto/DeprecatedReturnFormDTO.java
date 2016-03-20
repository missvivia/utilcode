package com.xyl.mmall.order.dto;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;

/**
 * 退货记录：填写退货信息页面
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午12:24:29
 *
 */
@Deprecated
public class DeprecatedReturnFormDTO extends DeprecatedReturnForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -591978774101455868L;

	/**
	 * 构造函数
	 */
	public DeprecatedReturnFormDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public DeprecatedReturnFormDTO(DeprecatedReturnForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	/**
	 * 退货关联的Order
	 */
	private OrderFormDTO orderFormDTO;
	
	/**
	 * 订单中退回的OrderSku
	 */
	private List<DeprecatedReturnOrderSkuDTO> retOrderSkuList = new ArrayList<DeprecatedReturnOrderSkuDTO>();

	public OrderFormDTO getOrderFormDTO() {
		return orderFormDTO;
	}

	public void setOrderFormDTO(OrderFormDTO orderFormDTO) {
		this.orderFormDTO = orderFormDTO;
	}
	
	public List<DeprecatedReturnOrderSkuDTO> getRetOrderSkuList() {
		return retOrderSkuList;
	}

	public void setRetOrderSkuList(List<DeprecatedReturnOrderSkuDTO> retSkuList) {
		this.retOrderSkuList = retSkuList;
	}
}
