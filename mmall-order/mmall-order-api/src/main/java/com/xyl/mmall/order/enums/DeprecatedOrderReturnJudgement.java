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
public enum DeprecatedOrderReturnJudgement implements AbstractEnumInterface<DeprecatedOrderReturnJudgement> {
	
	/**
	 * 可以退换货
	 */
	PASSED(true, 1, ""), 	
	/**
	 * 已经退过
	 */
	FAILED_ALREADY_RETURNED(false, 2, "已经退过"),	
	/**
	 * 包裹未全部处理
	 */
	FAILED_NOT_CONSIGNED(false, 3, "包裹未全部处理"), 
	/**
	 * PO档期结束
	 */
	FAILED_PO_OVER(false, 4, "PO档期结束"), 	
	/**
	 * 订单签收超过7天
	 */
	FAILED_OUT_7DAYS_WITHOUT_KF(false, 5, "订单签收超过7天"), 	
	/**
	 * 重启退货超过3天
	 */
	FAILED_OUT_3DAYS_WITH_KF(false, 6, "重启退货超过3天"), 
	/**
	 * 已拒收
	 */
	FAILED_PACKAGE_REJECTED(false, 7, "已拒收"), 
	/**
	 * 不支持退货
	 */
	FAILED_NOT_SUPPORT(false, 8, "不支持退货"), 
	/**
	 * 进入退货申请
	 */
	APPLY_RETURN(false, 9, "进入退货申请"), 
	/**
	 * 空记录
	 */
	NULL(false, 0, "空记录");

	private boolean canReturn;
	
	private int intValue;
	
	private String desc;
	
	private DeprecatedOrderReturnJudgement(boolean canReturn, int intValue, String desc) {
		this.canReturn = canReturn;
		this.intValue = intValue;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isCanReturn() {
		return canReturn;
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
	public int getIntValue() {
		return this.intValue;
	}

	@Override
	public DeprecatedOrderReturnJudgement genEnumByIntValue(int intValue) {
		for(DeprecatedOrderReturnJudgement osrj : DeprecatedOrderReturnJudgement.values()) {
			if(intValue == osrj.getIntValue()) {
				return osrj;
			}
		}
		return null;
	}

}
