package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzliujie
 * 2014年10月22日 下午3:38:48
 */
public enum WarehouseStockType implements AbstractEnumInterface<WarehouseStockType> {

	ZCRK(0,"正常入库"), BHRK(1,"补货入库"), THRK(2,"退货入库");
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
    private WarehouseStockType(int v, String d) {
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
    public WarehouseStockType genEnumByIntValue(int i) {
        for (WarehouseStockType type : values()) {
            if (type.getValue() == i) {
                return type;
            }
        }
        return null;
    }

    public WarehouseStockType genEnumByDesc(String desc) {
        for (WarehouseStockType type : values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }
}
