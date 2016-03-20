/**
 * 
 */
package com.xyl.mmall.oms.warehouse.util;

import java.util.List;

/**
 * @author hzzengchengyuan
 * 
 */
public interface BeanMapConfiguration {

	/**
	 * 返回Object->Object属性映射列表
	 * 
	 * @return
	 */
	List<BeanMapDescribe> getBeanMapDescribe();

	/**
	 * 添加一个Object->Object属性映射關係
	 * 
	 * @param desc
	 */
	void addBeanMapDescribe(BeanMapDescribe desc);
}
