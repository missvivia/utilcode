package com.xyl.mmall.bi.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务端日志标记.<br/>
 * 所有值定义参考bi给的文档.
 * 
 * @author wangfeng
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BILog {

	/**
	 * app/web端.<br/>
	 * 默认web.
	 * 
	 * @return app/web
	 */
	String clientType() default "web";

	/**
	 * 用户操作类型.<br/>
	 * 默认page.
	 * 
	 * @return page（页面访问）/ click（点击操作）
	 */
	String action() default "page";

	/**
	 * 具体页面/操作.
	 * 
	 * @return
	 */
	String type();

}
