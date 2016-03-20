/*
 * 2014-10-25
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.promotion.dao.PromotionLockDao;
import com.xyl.mmall.promotion.meta.PromotionLock;

/**
 * ActionSerializationAspect.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-25
 * @since      1.0
 */
@Aspect
@Component
public class ActionSerializationAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActionSerializationAspect.class);

	@Autowired
	private PromotionLockDao promotionLockDao;

	/**
	 * 使用@ActionSerialization的方法都会触发
	 */
	@Pointcut("execution(@com.xyl.mmall.promotion.aop.ActionSerialization * *(..))")
	private void actionSerializationMethod() {
	}

	@Before("actionSerializationMethod()")
	public void before(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("++++ " + methodName + " ActionSerialization Begin +++++");

		Object[] args = joinPoint.getArgs();
		// 1.检查是否符合约定规则
		if (!hasUserLockParam(args))
			throw new ServiceException(methodName + " : argument of  PromotionLock is Undefined. ");

		// 2.加锁
		for (Object object : args) {
			if (object instanceof PromotionLock) {
				 PromotionLock promotionLock = (PromotionLock) object;
				long userId = promotionLock.getUserId();
				if (userId == 0L)
					throw new ServiceException(methodName
							+ " : argument of PromotionLock.userId=0, Please defined it.");
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("userId=" + userId);
				PromotionLock lock = promotionLockDao.getLock(userId);
				if (lock == null) {
					promotionLockDao.addObject(promotionLock);
					promotionLockDao.getLock(userId);
				}
				
				break;
			}
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("++++ " + methodName + " ActionSerialization End +++++");
	}

	/**
	 * 检查参数中是否有PromotionLock对象.
	 * 
	 * @param args
	 * @return
	 */
	private boolean hasUserLockParam(Object[] args) {
		for (Object object : args) {
			if (object instanceof PromotionLock) {
				return true;
			}
		}
		return false;
	}
}
