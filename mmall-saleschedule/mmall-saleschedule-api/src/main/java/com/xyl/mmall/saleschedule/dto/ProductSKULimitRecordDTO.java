/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitRecord;

/**
 * ProductSKULimitRecordDTO.java created by yydx811 at 2015年11月17日 下午2:32:23
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class ProductSKULimitRecordDTO extends ProductSKULimitRecord {

	/** 序列化id. */
	private static final long serialVersionUID = 6878603445907845287L;

	public ProductSKULimitRecordDTO() {
	}
	
	public ProductSKULimitRecordDTO(ProductSKULimitRecord obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
