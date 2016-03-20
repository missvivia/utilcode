package com.xyl.mmall.oms.warehouse.adapter.ems;

/**
 * 当ems快件轨迹为签收时还会细分多种情况，在此定义和ems的数据对应起来
 * 
 * @author hzzengchengyuan
 *
 */
public enum EmsDelivery {
	W("10", "W", "W", "本人收"),

	M("11", "M", "M", "他人收"),

	N("12", "N", "N", "单位收发章"),

	V("13", "V", "V", "未出口退回妥投"),

	U("14", "U", "U", "退回妥投");

	/**
	 * 可认为用户真正签收的状态字段
	 */
	public static final EmsDelivery[] JUDGE_REAL_SIGNED_ARRAY = new EmsDelivery[] { EmsDelivery.W,
			EmsDelivery.M, EmsDelivery.N };

	/**
	 * 签收明细-编码（客户视角）
	 */
	private String customerCode;

	/**
	 * 签收明细-编码（内部视角）
	 */
	private String internalCode;

	/**
	 * 签收明细-编码
	 */
	private String code;

	/**
	 * 签收明细显示名称
	 */
	private String lable;

	private EmsDelivery(String customerCode, String internalCode, String code, String lable) {
		this.customerCode = customerCode;
		this.internalCode = internalCode;
		this.code = code;
		this.lable = lable;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public String getInternalCode() {
		return internalCode;
	}

	public String getCode() {
		return code;
	}

	public String getLable() {
		return lable;
	}

	/**
	 * 根据签收的名称来获取该签收具体明细
	 * 
	 * @param lable
	 * @return
	 */
	public static EmsDelivery matchByLable(String lable) {
		for (EmsDelivery details : values()) {
			if (details.getLable().equals(lable)) {
				return details;
			}
		}
		return null;
	}

	/**
	 * 判断是否是用户签收妥投，其他情况则为返仓妥投
	 * @param details
	 * @return
	 */
	public static boolean judgeRealSignedByLable(EmsDelivery details) {
		if (details == null) {
			throw new NullPointerException();
		}
		for (EmsDelivery d : JUDGE_REAL_SIGNED_ARRAY) {
			if (d.equals(details)) {
				return true;
			}
		}
		return false;
	}
}
