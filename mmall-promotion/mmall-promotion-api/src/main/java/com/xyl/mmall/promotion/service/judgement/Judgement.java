/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.judgement;

/**
 * Judgement.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public interface Judgement {
	/**
	 * 原对象和目标对象相等判断
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean equal(Object base, Object desc);
	
	/**
	 * 原对象和目标对象不相等判断
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean nonEqual(Object base, Object desc);
	
	/**
	 * 原对象大于目标对象判断
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean greaterThan(Object base, Object desc);
	
	/**
	 * 原对象大于等于目标对象判断
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean greaterEqualThan(Object base, Object desc);
	
	/**
	 * 原对象小于目标对象判断
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean lessThan(Object base, Object desc);
	
	/**
	 * 原对象小于等于目标对象判断
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean lessEqualThan(Object base, Object desc);
	
	/**
	 * 原对象在低值对象和高值对象之间
	 * @param base 原对象
	 * @param low 低值对象
	 * @param heigh 高职对象
	 * @return
	 */
	boolean between(Object base, Object low, Object heigh);
	
	/**
	 * 原对象包含目标对象
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean contains(Object base, Object desc);
	
	/**
	 * 目标对象包含原对象
	 * @param base 原对象
	 * @param desc 目标对象
	 * @return
	 */
	boolean indexOf(Object base, String desc);
}
