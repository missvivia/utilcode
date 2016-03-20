package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.ReturnOrderFormDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;

/**
 * @author zb
 *
 */
public interface OmsReturnOrderFormService {

	/**
	 * 用户退货订单状态更新
	 * 
	 * @param wmsReturnOrderUpdateDTO
	 * @return
	 */
	public boolean wmsReturnOrderUpdate(WMSReturnOrderUpdateDTO wmsReturnOrderUpdateDTO);

	/**
	 * 保存一个退货订单
	 * 
	 * @param returnOrderFormDTO
	 * @return
	 */
	public boolean saveReturnOrderForm(ReturnOrderFormDTO returnOrderFormDTO);

	public List<OmsReturnOrderForm> getListByConfirmTimeAndState(long confirmTime, OmsReturnOrderFormState returnState,
			int limit);

	public boolean updateOmsReturnOrderFormState(long orderId, OmsReturnOrderFormState state, String extInfo);

	public List<OmsReturnOrderFormSku> getOmsReturnOrderFormSkuListByReturnId(long returnId, long userId);

}
