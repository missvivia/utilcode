package com.xyl.mmall.oms.warehouse.adapter.ems;

/**
 * 当ems快件轨迹为未签收时还会细分多种情况，在此定义和ems的数据对应起来
 * @author hzzengchengyuan
 *
 */
public enum EmsUndelivery {
	UNAC_100("100", "正在投递中"),

	UNAC_101("101", "收件人地址有误"),

	UNAC_102("102", "查无此人"),
	
	UNAC_103("103", "收件人不在指定的地址"),
	
	UNAC_104("104", "拒收退回"),
	
	UNAC_105("105", "收件人要求延迟投递"),
	
	UNAC_106("106", "误投"),
	
	UNAC_107("107", "邮件错发"),
	
	UNAC_108("108", "收到时破损，无法投出"),
	
	UNAC_109("109", "禁寄物品"),
	
	UNAC_110("110", "限寄物品"),
	
	UNAC_111("111", "待收费后"),

	UNAC_112("112", "无人认领"),

	UNAC_113("113", "无法找到收件人"),
	
	UNAC_114("114", "因不可抗力原因，邮件未投出"),
	
	UNAC_115("115", "收件人要求自取"),
	
	UNAC_116("116", "法定假日，无法投递"),
	
	UNAC_117("117", "邮件丢失"),
	
	UNAC_118("118", "人已他往"),
	
	UNAC_119("119", "收件人有信箱"),
	
	UNAC_120("120", "安排投递"),
	
	UNAC_121("121", "正在投递"),
	
	UNAC_122("122", "查无此单位"),

	UNAC_123("123", "地址不祥，无电话，电话不对"),

	UNAC_124("124", "地址不详"),
	
	UNAC_125("125", "撤回"),
	
	UNAC_126("126", "迁移新址不明"),
	
	UNAC_127("127", "逾期未领"),
	
	UNAC_128("128", "投递到包裹站"),
	
	UNAC_129("129", "逾期投递员收回"),
	
	UNAC_130("130", "其它");

	/**
	 * 签收明细-编码
	 */
	private String code;

	/**
	 * 签收明细显示名称
	 */
	private String lable;

	private EmsUndelivery(String code, String lable) {
		this.code = code;
		this.lable = lable;
	}

	public String getCode() {
		return code;
	}

	public String getLable() {
		return lable;
	}

	/**
	 * 根据未签收的描述名称来获取该未签收具体明细
	 * @param lable
	 * @return
	 */
	public static EmsUndelivery matchByLable(String lable) {
		for (EmsUndelivery details : values()) {
			if (details.getLable().equals(lable)) {
				return details;
			}
		}
		return null;
	}

}
