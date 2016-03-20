/**
 * 
 */
package com.xyl.mmall.framework.poi;

import java.util.Map;

/**
 * @author jmy
 *
 */
public interface IExcelDataExtracter {
	/**
	 * 获取Excel中的数据结构配置信息（即元数据）
	 * @return 非null
	 */
	Map<String, MetaData> getMetaData(); 
	
	/**
	 * 获取Excel中配置的所有数据
	 * @return 以引用变量为key，对应数据为vlaue的Map对象
	 */
	Map<String, Object> getAllData();
	
	/**
	 * 获取Excel中指定ID的数据
	 * @param id 数据引用变量
	 * @return 引用变量所对应的数据对象
	 */
	Object getData(String id);
}
