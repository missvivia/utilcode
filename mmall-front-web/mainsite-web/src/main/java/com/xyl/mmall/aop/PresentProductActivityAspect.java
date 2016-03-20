/**
 * 
 */
package com.xyl.mmall.aop;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;
import com.xyl.mmall.mainsite.vo.PresentProductVO;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author hzlihui2014
 *
 */
@Aspect
@Component
public class PresentProductActivityAspect {

	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	@Pointcut("execution(@com.xyl.mmall.content.annotation.PresentProductActivity * *(..))")
	public void mainsitePresentProductActivityMethod() {
	}

	@Around("mainsitePresentProductActivityMethod()")
	public Object doPresentProductActivity(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();
		long currentTime = System.currentTimeMillis();
		if (currentTime > OnlineActivityConstants.ACTIVITY_V2_END_TIME
				|| currentTime < OnlineActivityConstants.ACTIVITY_START_TIME) {
			return result;
		}
		Object[] args = pjp.getArgs();
		for (Object arg : args) {
			if (arg instanceof Model) {
				Model model = (Model) arg;
				// 为页面返回多个展示商品信息对象。
				Map<Integer, List<PresentProductVO>> resultMap = onlineActivityFacade.getPresentProductList(AreaUtils
						.getAreaCode());
				if (CollectionUtils.isNotEmpty(resultMap.keySet())) {
					for (Integer key : resultMap.keySet()) {
						model.addAttribute("tab" + key, resultMap.get(key));
					}
				}
				break;
			}
		}
		return result;
	}
}
