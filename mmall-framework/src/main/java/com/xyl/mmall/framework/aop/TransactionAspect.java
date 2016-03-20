package com.xyl.mmall.framework.aop;
import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.dbsupport.transaction.IDBTransactionManager;
import com.xyl.mmall.framework.exception.IPrintErrorLog;
import com.xyl.mmall.framework.exception.IPrintInfoLog;
import com.xyl.mmall.framework.exception.IReThrowException;

/**
 * 
 * 处理事务异常的切面，针对不同interface的异常，有不同的处理方式
 * 
 * @author yangnan
 * 
 */
@Component
@Aspect
@Order(value = 0)
public class TransactionAspect {

	private static final Logger logger = LoggerFactory.getLogger(TransactionAspect.class);

	/**
	 * 如果事务失败返回类型是boolean型，将返回false
	 */
	private static final boolean FAIL_BOOLEAN = false;

	/**
	 * 如果事务失败返回类型是long型，将返回-1
	 */
	private static final long FAIL_LONG = -1;

	/**
	 * 如果事务失败返回类型是Object，将返回-1
	 */
	private static final Object FAIL_OBJ = null;

	@Resource
	private IDBTransactionManager transactionManager;

	

	@Pointcut("execution(@com.xyl.mmall.framework.annotation.Transaction * *(..))")
	public void transactionalMethod() {
	}

	@Pointcut("transactionalMethod()")
	public void serviceOperationPoint() {
	}

	@Around("serviceOperationPoint()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		// start stopwatch
		if (this.transactionManager.getAutoCommit())
			// 未启动事务
			return processTransaction(pjp);
		else
			// 已经启动了事务，在事务内部，所以不需要启动新的事务，直接执行既可。
			return processNormal(pjp);
	}

	/**
	 * 不启动事务 直接执行
	 * 
	 * @param invocation
	 *            调用的方法
	 * @return
	 * @throws Throwable
	 */
	private Object processNormal(ProceedingJoinPoint invocation) throws Throwable {
		return invocation.proceed();
	}

	/**
	 * 启动事务执行 并在结束时关闭事务
	 * 
	 * @param invocation
	 *            调用的方法
	 * @return
	 * @throws Throwable
	 */
	private Object processTransaction(ProceedingJoinPoint invocation) throws Throwable {
		try {
			MethodSignature methodSig = (MethodSignature) invocation.getSignature();
			this.transactionManager.setAutoCommit(false);
			if (logger.isDebugEnabled())
				logger.debug(methodSig.getMethod().getName() + "事务增强开始");

			Object result = invocation.proceed();

			if (logger.isDebugEnabled())
				logger.debug(methodSig.getMethod().getName() + "事务增强结束");

			return result;
		} catch (Exception sre) {
			// 记录异常日志־
			if (sre instanceof IPrintInfoLog)
				logger.info(sre.getMessage());
			else if (sre instanceof IPrintErrorLog)
				logger.error(sre.getMessage());
			else {
				logger.error(sre.getMessage(), sre);
			}
			try {
				this.transactionManager.rollback();
			} catch (DBSupportRuntimeException se) {
				logger.error(se.getMessage());
			}

			if (sre instanceof IReThrowException)
				throw sre;
			return getFailValue(invocation, "事务回滚");
		} finally {
			this.transactionManager.setAutoCommit(true);
			this.transactionManager.clear();
		}
	}

	/**
	 * @param invocation
	 * @param msg
	 * @return
	 */
	private Object getFailValue(ProceedingJoinPoint invocation, String msg) {
		MethodSignature methodSig = (MethodSignature) invocation.getSignature();
		logger.debug(methodSig.getMethod().getName() + "事务回滚");
		if (logger.isDebugEnabled())
			logger.error(methodSig.getMethod().getName() + msg);
		Class<?> returnType = methodSig.getMethod().getReturnType();
		if (returnType.equals(long.class) || returnType.equals(Long.class))
			return FAIL_LONG;
		else if (returnType.equals(boolean.class) || returnType.equals(Boolean.class))
			return FAIL_BOOLEAN;
		else if (returnType.equals(int.class) || returnType.equals(Integer.class))
			return -1;
		else
			return FAIL_OBJ;
	}

	public IDBTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(IDBTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
