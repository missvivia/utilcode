package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzzengchengyuan
 *
 */
public enum WarehouseLogState implements AbstractEnumInterface<WarehouseLogState> {

	EXCEPTION(11, "异常"), 
	
	/**
	 * 一般日志，无执行结果或状态的日志
	 */
	NORMAL(0,"一般"),
	
	SUCESS(1,"成功"), 
	
	FAILURE(2,"失败");
	
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
    private WarehouseLogState(int v, String d) {
        value = v;
        desc = d;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int getIntValue() {
        return this.value;
    }

    @Override
    public WarehouseLogState genEnumByIntValue(int i) {
        for (WarehouseLogState type : values()) {
            if (type.getValue() == i) {
                return type;
            }
        }
        return null;
    }

    public WarehouseLogState genEnumByDesc(String desc) {
        for (WarehouseLogState type : values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }
}
