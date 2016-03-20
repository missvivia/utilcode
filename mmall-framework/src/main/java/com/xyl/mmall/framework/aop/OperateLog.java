package com.xyl.mmall.framework.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志
 * @author author:lhp
 *
 * @version date:2015年6月18日下午6:54:19
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

	String clientType() default "web";
}
