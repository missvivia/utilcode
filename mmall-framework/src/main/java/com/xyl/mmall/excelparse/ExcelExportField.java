package com.xyl.mmall.excelparse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hzhuangluqian
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExportField {
	/**
	 * excel字段描述
	 * 
	 * @return
	 */
	public String desc() default "";

	/**
	 * 辅助属性
	 * 
	 * @return
	 */
	public int paramId() default 0;

	/**
	 * excel的cellIndex
	 * 
	 * @return
	 */
	public int cellIndex() default 0;
}
