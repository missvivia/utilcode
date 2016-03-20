package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 品牌状态
 * @author chengximing
 *
 */
public enum BrandStatus implements AbstractEnumInterface<BrandStatus> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 新建状态
	 */
	BRAND_NEW(0, "品牌新建状态"),
	/**
	 * 核审中
	 */
	BRAND_AUDITING(1, "审核中"),
	
	BRAND_AUDITPASSED_UNUSED(2, "审核通过-暂未使用"),
	
	BRAND_AUDITPASSED_USING(3, "审核通过-使用中"),
	
	BRAND_AUDITREFUSED(4, "审核拒绝"),
	
	BRAND_TRASHED(5, "已作废"),
	;
	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private BrandStatus(int v, String d) {
		value = v;
		desc = d;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#getIntValue()
	 */
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	@Override
    public BrandStatus genEnumByIntValue(int intValue) {
		for (BrandStatus item : BrandStatus.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
    }

}
