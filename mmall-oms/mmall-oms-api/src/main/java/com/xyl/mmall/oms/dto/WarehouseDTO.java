/**
 * 
 */
package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.meta.WarehouseForm;

/**
 * @author hzzengdan
 *
 */
public class WarehouseDTO extends WarehouseForm {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -631320962254939949L;
	
	/**
	 * 构造函数
	 */
	public WarehouseDTO(WarehouseForm obj){
		ReflectUtil.convertObj(this, obj, false);
	}

}
