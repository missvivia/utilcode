/**
 * 
 */
package com.xyl.mmall.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;

/**
 * @author hzlihui2014
 *
 */
@Aspect
@Component
public class PaySuccessActivityAspect {

	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	@Pointcut("execution(@com.xyl.mmall.content.annotation.PaySuccessActivity * *(..))")
	public void mainsitePaySuccessActivityMethod() {
	}

	@Around("mainsitePaySuccessActivityMethod()")
	public Object doPaySuccessActivity(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();
		long currentTime = System.currentTimeMillis();
		if (currentTime > OnlineActivityConstants.ACTIVITY_END_TIME
				|| currentTime < OnlineActivityConstants.ACTIVITY_START_TIME) {
			Object[] args = pjp.getArgs();
			for (Object arg : args) {
				if (arg instanceof Model) {
					Model model = (Model) arg;
					model.addAttribute("ay0126End", true);
				}
			}
			return result;
		}
		if (result instanceof ModelAndView) {
			ModelAndView modelAndView = (ModelAndView) result;
			Map<String, Object> model = modelAndView.getModel();
			if (model.containsKey("order")) {
				OrderForm2VO order = (OrderForm2VO) modelAndView.getModel().get("order");
				if (order.getPayState() == PayState.ONLINE_PAYED) {
					// 显示给用户获取一次抽奖机会
					if (order.getTotalCash().compareTo(OnlineActivityConstants.SUIT_CASE_GIFT_ORDER_AMOUNT) >= 0
							&& onlineActivityFacade.isHasSuitcaseGift(SecurityContextUtils.getUserId(),
									order.getOrderId())) {
						// 查询用户是否在此订单付款是获得旅行箱
						model.put("giftType", 2);
					} else {
						model.put("giftType", 1);
					}
				}
			}
		}
		return result;
	}
}
