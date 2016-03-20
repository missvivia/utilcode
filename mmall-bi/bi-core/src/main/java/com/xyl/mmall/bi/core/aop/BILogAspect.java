package com.xyl.mmall.bi.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.meta.ApiConsumerLog;
import com.xyl.mmall.bi.core.meta.AppLog;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.core.meta.CMSLog;
import com.xyl.mmall.bi.core.meta.OrderLog;
import com.xyl.mmall.bi.core.meta.WebLog;
import com.xyl.mmall.bi.core.service.NQSBILogMessageProducer;
import com.xyl.mmall.bi.core.util.BasicLogUtils;
import com.xyl.mmall.bi.core.util.OtherKeyUtils;

/**
 * bi log Aspect.
 * 
 * @author wangfeng
 * 
 */
@Aspect
@Component
public class BILogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(BILogAspect.class);

	@Autowired
	private NQSBILogMessageProducer nqsBILogMessageProducer;

	/**
	 * 使用@BILog的方法都会触发
	 */
	@After("within(com.xyl.mmall..*) && @annotation(biLog)")
	public void excute(JoinPoint joinPoint, BILog biLog) {
		Object target = joinPoint.getTarget();
		String serviceName = target.getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("++++ " + serviceName + "#" + methodName + " BILog Begin +++++");

		try {
			// 1.获取clientType
			String clientTypeValue = biLog.clientType();
			ClientType clientType = ClientType.NULL.genEnumByValue(clientTypeValue);
			// 2.获取参数
			Object[] args = joinPoint.getArgs();
			// 3.判断是否是order类型
			if (clientType == ClientType.ORDER) {
				String otherKey = OtherKeyUtils.getOtherKeyOfOrder(biLog, args);
				if (otherKey == null)
					return;
				// 异步发送的日志信息
				nqsBILogMessageProducer.sendBILog(new OrderLog(biLog), otherKey);
			} else if (clientType == ClientType.CMS) {
				String otherKey = OtherKeyUtils.getOtherKeyOfCMS(biLog, args);
				if (otherKey == null)
					return;
				// 异步发送的日志信息
				nqsBILogMessageProducer.sendBILog(new CMSLog(biLog), otherKey);
			} else {
				// 创建日志通用信息
				BasicLog basicLog = createBasicLog(biLog);
				String otherKey = OtherKeyUtils.getOtherKey(clientType, biLog, args);
				if (otherKey == null)
					LOGGER.warn("otherKey is null! " + basicLog.toString());
				// 异步发送的日志信息
				nqsBILogMessageProducer.sendBILog(basicLog, otherKey);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("++++ " + serviceName + "#" + methodName + " BILog End +++++");
	}

    /**
     * 获取日志通用信息.
     * 
     * @param serviceName
     * @param methodName
     * @param clientTypeValue
     * @return
     */
    private BasicLog createBasicLog(BILog biLog)
    {
        // 根据app端还是web端创建日志通用信息.
        String clientTypeValue = biLog.clientType();
        ClientType clientType = ClientType.NULL.genEnumByValue(clientTypeValue);
        if (ClientType.APP == clientType)
        {
            // app端
            AppLog logEntity = new AppLog();
            BasicLogUtils.setAppLogInfo(logEntity, biLog);
            return logEntity;
        }
        else if (ClientType.ORDER == clientType)
        {
            // 销售数据
            return new OrderLog(biLog);
        }
        else if (ClientType.WEB == clientType)
        {
            // web端
            WebLog logEntity = new WebLog();
            BasicLogUtils.setWebLogInfo(logEntity, biLog);
            return logEntity;
        }
        else
        {
            // api_consumer
            ApiConsumerLog logEntity = new ApiConsumerLog();
            BasicLogUtils.setApiConsumerLogInfo(logEntity, biLog);
            return logEntity;
        }
    }
}
