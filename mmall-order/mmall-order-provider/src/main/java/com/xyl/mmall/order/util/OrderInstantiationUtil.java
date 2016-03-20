package com.xyl.mmall.order.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.tcc.OrderTCCLockDao;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.meta.tcc.OrderTCCLock;

@Component
public class OrderInstantiationUtil {

	@Autowired
	private OrderFormDao orderFormDao;

	@Autowired
	private OrderTCCLockDao orderTCCLockDao;

	@Autowired
	private OrderPackageDao orderPackageDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 判断普通服务(非TCC服务),是否可以继续执行<br>
	 * (获得订单记录锁+判断TCC记录是否存在)
	 * 
	 * @param packageId
	 * @param userId
	 * @return RetArg.OrderForm<br>
	 *         RetArg.Boolean: 是否可以继续
	 */
	public RetArg isContinueForNormalServiceByPackageId(long packageId, long userId) {
		// 1.读取包裹信息
		OrderPackage orderPackage = orderPackageDao.getObjectByIdAndUserId(packageId, userId);
		long orderId = orderPackage == null ? -1L : orderPackage.getOrderId();
		// 2.调用isContinueForNormalService
		RetArg retArg = isContinueForNormalService(orderId, userId);
		return retArg;
	}

	/**
	 * 判断普通服务(非TCC服务),是否可以继续执行<br>
	 * (获得订单记录锁+判断TCC记录是否存在)
	 * 
	 * @param orderId
	 * @param userId
	 * @return RetArg.OrderForm<br>
	 *         RetArg.Boolean: 是否可以继续
	 */
	public RetArg isContinueForNormalService(long orderId, long userId) {
		RetArg retArg = new RetArg();
		try {

			// 1.读取订单的数据(获得订单Lock)
			OrderForm orderForm = new OrderForm();
			orderForm.setOrderId(orderId);
			orderForm.setUserId(userId);
			orderForm = orderFormDao.getLockByKey(orderForm);
			if (orderForm == null) {
				logger.error("orderForm==null ,OrderId=" + orderId + " ,UserId=" + userId);
				RetArgUtil.put(retArg, false);
				return retArg;
			}
			// 2.判断订单是否存在TCC记录
			OrderTCCLock orderTCCLock = orderTCCLockDao.getObjectByOrderId(orderId);
			if (orderTCCLock != null) {
				logger.error("OrderTCCLock!=null " + orderTCCLock.toString());
				RetArgUtil.put(retArg, false);
				return retArg;
			}

			RetArgUtil.put(retArg, true);
			RetArgUtil.put(retArg, orderForm);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			RetArgUtil.put(retArg, false);
		}
		return retArg;
	}
}
