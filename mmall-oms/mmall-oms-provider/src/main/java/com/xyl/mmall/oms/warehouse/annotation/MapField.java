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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapField {
	String value() default "";

	/**
	 * 标识该字段是否为时间类型，如果为true则该字段不论数据类型为何都作为时间处理。 long -> date (new Date(long)) ;
	 * String -> date(new format("YYYY-MM-dd HH:mm:ss").parse(String))
	 * 
	 * @return
	 */
	boolean isDate() default false;
	
	


	
	/**
	 * 标识该字段是否为KG单位的重量，如果为true则将该字段的值转换为long类型的克
	 * @return
	 */
	boolean isKG() default false;
	
	/**
	 * 标识该字段是否为G单位的重量
	 * @return
	 */
	boolean isG() default false;
	
	/**
	 * 标识该字段是否为米单位的长度，如果为true则将该字段的值转换long类型的厘米
	 * @return
	 */
	boolean isM() default false;
	
	/**
	 * 标识该字段是否为分米单位的长度，如果为true则将该字段的值转换long类型的厘米
	 * @return
	 */
	boolean isDM() default false;
	
	/**
	 * 标识该字段是否为厘米单位的长度，如果为true则将该字段的值转换long类型的厘米
	 * @return
	 */
	boolean isCM() default false;
	
	/**
	 * 标识该字段是否为毫米单位的长度
	 * @return
	 */
	boolean isMM() default false;
	
}
