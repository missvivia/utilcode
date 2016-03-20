/**
 * 
 */
package com.xyl.mmall.aop;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;

/**
 * @author hzlihui2014
 *
 */
@Aspect
@Component
public class CollectBubbleActivityAspect {

	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	@Pointcut("execution(@com.xyl.mmall.content.annotation.CollectBubbleActivity * *(..))")
	public void mainsiteCollectBubbleActivityMethod() {
	}

	@Around("mainsiteCollectBubbleActivityMethod()")
	public Object doCollectBubbleActivity(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();
		long currentTime = System.currentTimeMillis();
		// 判断是否活动未开始或已结束
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
		// 判断当前是否在整点的活动分钟之内
		Calendar calNow = Calendar.getInstance();
		Calendar calNextStart = Calendar.getInstance();
		calNextStart.set(Calendar.MINUTE, 0);
		calNextStart.set(Calendar.SECOND, 0);
		calNextStart.set(Calendar.MILLISECOND, 0);
		Calendar calNextEnd = Calendar.getInstance();
		calNextEnd.set(Calendar.SECOND, 0);
		calNextEnd.set(Calendar.MILLISECOND, 0);
		// 遍历活动时间段
		for (int activityTIme : OnlineActivityConstants.ORD_BUBBLE_ACTIVITY_TIME) {
			calNextStart.set(Calendar.HOUR_OF_DAY, activityTIme);
			calNextEnd.set(Calendar.MINUTE, OnlineActivityConstants.FIND_BUBBLE_MIN_TIME);
			calNextEnd.set(Calendar.SECOND, OnlineActivityConstants.FIND_BUBBLE_SECOND_TIME);
			calNextEnd.set(Calendar.HOUR_OF_DAY, activityTIme);
			if (calNow.after(calNextStart) && calNow.before(calNextEnd)) {
				Object[] args = pjp.getArgs();
				for (Object arg : args) {
					if (arg instanceof Model) {
						Model model = (Model) arg;
						RequestAttributes ra = RequestContextHolder.getRequestAttributes();
						StringBuilder requestUrl = new StringBuilder(128);
						if (ra != null) {
							HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
							requestUrl.append(request.getRequestURI());
							if (request.getQueryString() != null) {
								requestUrl.append("?").append(request.getQueryString());
							}
						}
						// 为页面返回一个paopao对象。
						model.addAttribute(
								"paopao",
								onlineActivityFacade.genBubble(SecurityContextUtils.getUserId(),
										SecurityContextUtils.getUserName(), requestUrl.toString()));
						break;
					}
				}
				break;
			}
		}
		return result;
	}
}
