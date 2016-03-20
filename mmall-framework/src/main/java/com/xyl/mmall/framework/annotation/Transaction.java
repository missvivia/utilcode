package com.xyl.mmall.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 标记一个方法，表明需要事务<br>
 * 注意：这里没有使用spring的Transaction注释，因为下面的原因：<br>
 * 1，我们的事务逻辑有单独的流程，用spring的Transaction仍然需要AOP去处理特殊逻辑<br>
 * 2.spring的Transaction开放了太多的自由权给程序员，不便于逻辑的约束
 * 
 * @author yangnan
 * 
 */
@Target(ElementType.METHOD)
public @interface Transaction {

}
