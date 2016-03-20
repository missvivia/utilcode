package com.xyl.mmall.common.out.facade;

import java.util.List;

import com.xyl.mmall.common.param.PayOrderParam;
import com.xyl.mmall.common.param.PayRecieveGoodsParam;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;

/**
 * 调用支付平台接口
 * 
 * @author author:lhp
 *
 * @version date:2015年8月6日下午3:55:03
 */
public interface PayFacade {

	/**
	 * 组装订单支付参数
	 * 
	 * @param orderBDTOList
	 * @return
	 */
	public PayOrderParam buildPayOrderParam(List<OrderFormBriefDTO> orderBDTOList);

	/**
	 * 验证签名
	 * 
	 * @param sgnMsgSrc
	 * @param sgnMsg
	 * @return
	 */
	public boolean validSignMsg(String sgnMsgSrc, String sgnMsg);

	/**
	 * 确认收货 在线支付改成即时交易了，确认收货接口不再调用
	 * 
	 * @param payRecieveGoodsParam
	 * @return
	 */
	@Deprecated
	public int confirmRecieveGoods(PayRecieveGoodsParam payRecieveGoodsParam);

}
