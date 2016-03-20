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
public @interface ExcelField {

	/**
	 * excel字段描述
	 * 
	 * @return
	 */
	public String desc() default "";

	/**
	 * 默认数据库的类型
	 * 
	 * @return
	 */
	public boolean required() default true;

	/**
	 * excel的cellIndex
	 * 
	 * @return
	 */
	public int cellIndex() default 0;

	/**
	 * excel的title
	 * 
	 * @return
	 */
	public String title() default "";

	/**
	 * 单元格的字符串的最大长度或者数值的最大值
	 * 
	 * @return
	 */
	public String max() default "-1";

}
