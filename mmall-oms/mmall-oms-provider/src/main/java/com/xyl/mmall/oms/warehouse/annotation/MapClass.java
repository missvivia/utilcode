/**
 * 
 */
package com.xyl.mmall.oms.warehouse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hzzengchengyuan
 * 
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MapClass {
	Class<?> value() default MapClass.class;

	boolean isAllField() default true;
}
