package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货申请：标记OrderSku是否可退货的判定结果
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午4:09:49
 *
 */
@Deprecated
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeprecatedOrderSkuReturnJudgement implements AbstractEnumInterface<DeprecatedOrderSkuReturnJudgement> {
	
	PASSED(true, 50, ""), 
	
	FAILED_PARENT_ORDER(false, 60, "所属订单无法退货"), 
	
	FAILED_PACKAGE_REJECTED(false, 70, "已拒收"), 
	
	FAILED_NOT_SUPPORT(false, 80, "不支持退货"), 
	
	APPLY_RETURN(false, 90, "进入退货申请"), 
	
	NULL(false, 0, "空记录");

	private boolean canReturn;
	
	private int intValue;
	
	private String desc;
	
	private DeprecatedOrderSkuReturnJudgement(boolean canReturn, int intValue, String desc) {
		this.canReturn = canReturn;
		this.intValue = intValue;
		this.desc = desc;
	}

	public boolean isCanReturn() {
		return canReturn;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIntValue() {
		return this.intValue;
	}

	public void setCanReturn(boolean canReturn) {
		this.canReturn = canReturn;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public DeprecatedOrderSkuReturnJudgement genEnumByIntValue(int intValue) {
		for(DeprecatedOrderSkuReturnJudgement osrj : DeprecatedOrderSkuReturnJudgement.values()) {
			if(intValue == osrj.getIntValue()) {
				return osrj;
			}
		}
		return null;
	}

}
