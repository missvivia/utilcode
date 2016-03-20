package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xyl.mmall.timer.facade.OmsTimerFacade;
import com.xyl.mmall.timer.facade.OrderTimerFacade;
import com.xyl.mmall.timer.facade.ReturnTimerFacade;

/**
 * @author zb<br>
 *
 */
@Controller
@RequestMapping("/oms")
public class OmsJobController {
	@Autowired
	private OmsTimerFacade omsTimerFacade;

	@Autowired
	private OrderTimerFacade orderTimerFacade;

	@Autowired
	private ReturnTimerFacade returnTimerFacade;

	@RequestMapping("cancelOmsOrder")
	public void cancelOmsOrder() {
		orderTimerFacade.cancelOmsOrder();
	}

	@RequestMapping("cancelTrade")
	public void cancelTrade() {
		orderTimerFacade.cancelTrade();
	}

	@RequestMapping("pushReturnPackageToJIT")
	public void pushReturnPackageToJIT() {
		returnTimerFacade.pushReturnPackageToJIT();
	}

	@RequestMapping("pushOrderToOms")
	public void pushOrderToOms() {
		orderTimerFacade.pushOrderToOms();
	}

	/**
	 * 将销售订单推送给仓库
	 */
	@RequestMapping("pushOmsOrderFormToWarehose")
	public void pushOmsOrderFormToWarehose() {
		omsTimerFacade.pushOmsOrderFormToWarehose();
	}

	/**
	 * 将发货单推送给仓库
	 */
	@RequestMapping("pushOmsShipOrderToWarehose")
	public void pushOmsShipOrderToWarehose() {
		omsTimerFacade.pushOmsShipOrderToWarehose();
	}

	/**
	 * 将销售定的状态反馈给app
	 */
	@RequestMapping("pushOmsOrderFormStateToApp")
	public void pushOmsOrderFormStateToApp() {
		omsTimerFacade.pushOmsOrderFormStateToApp();
	}

	/**
	 * 将退货单的状态反馈给app
	 */
	@RequestMapping("pushOmsReturnOrderFormStateToApp")
	public void pushOmsReturnOrderFormStateToApp() {
		omsTimerFacade.pushOmsReturnOrderFormStateToApp();
	}

	/**
	 * 
	 */
	@RequestMapping("pushOmsOrderPackageToApp")
	public void pushOmsOrderPackageToApp() {
		omsTimerFacade.pushOmsOrderPackageToApp();
	}

	@RequestMapping("generatePickOrder")
	public void generatePickOrder() {
		omsTimerFacade.generatePickOrder();
	}

	@RequestMapping("cancelTimeOutOrderForm")
	public void cancelTimeOutOrderForm() {
		omsTimerFacade.cancelTimeOutOrderForm();
	}

	@RequestMapping("cancelTimeOutShipOrder")
	public void cancelTimeOutShipOrder() {
		omsTimerFacade.cancelTimeOutShipOrder();
	}

}
