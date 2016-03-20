package com.xyl.mmall.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * job的url注解类
 * @author hzzhaozhenzuo
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface JobPath {
	String value();
}
