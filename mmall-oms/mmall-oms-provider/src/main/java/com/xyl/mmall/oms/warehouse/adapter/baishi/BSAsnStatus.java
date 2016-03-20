/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.baishi;


/**
 * 百世补货单状态(入库单)
 * 
 * @author hzzengchengyuan
 * 
 */
public enum BSAsnStatus {
	NEW(0, "未开始处理"),

	WMS_ACCEPT(1, "仓库接单"),

	INPROCESS(2, "处理中"),

	FULFILLED(3, "收货完成"),

	@Deprecated
	EXCEPTION(4, "异常"),

	CANCELED(5, "取消"),

	CLOSED(6, "关闭"),

	WSM_REJECT(7, "拒单"),

	CANCELEDFAIL(8, "取消失败");
	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	BSAsnStatus(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public static BSAsnStatus genEnumNameIgnoreCase(String name) {
		for (BSAsnStatus type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
