package com.xyl.mmall.framework.enums;

import java.util.HashMap;
import java.util.Map;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 通用错误返回消息列表
 * 
 * @author jiangww
 *
 */
public enum ErrorCode implements AbstractEnumInterface<ErrorCode> {

	/**
	 * 正常
	 */
	NULL(0, "SUCCESS"),

	/**
	 * 1~99 系统通用错误代码
	 */
	EMPTY(10, "INPUT VALUE IS EMPTY"), ID_NOT_ALLOWED_NULL(11, "ID NOT ALLOWED NULL"),

	NO_MATCH(20, "INPUT PARAM NOT MATCH"), AREA_NOT_MATCH(21, "AREA NOT MATCH"), DATA_NOT_MATCH(22,
			"DATA NOT MATCH"), USER_NOT_MATCH(23, "USER NOT MATCH"),

	CAN_NOT_FIND_OBJECT(30, "CAN NOT FIND OBJECT"),

	LOGIN_FAIL(40, "LOGIN FAIL"), USER_NAME_NOT_EXSIX(41, "USER NAME NOT EXSIX"), PASSWORD_ERROR(42,
			"PASS WORD ERROR"), ACCOUNT_FREEZE(43, "ACCOUNT FREEZE"), LOGIN_TIMESOUT(44, "LOGIN TIMESOUT"),

	NOT_SUPPORT(50, "NOT SUPPORT"), INPUT_TYPE_NOT_SUPPORT(51, "INPUT TYPE NOT SUPPORT"), CALL_URL_NOT_SUPPORT(52,
			"CALL URL NOT SUPPORT"),

	SERVICE_ERROR(60, "SERVICE ERROR"), USER_CHECKED_ERROR(61, "USER CHECKED ERROR"), SERVICE_EXCEPTION(62,
			"SERVICE UNKONW EXCEPTION"), SERVICE_EXEC_FAIL(62, "SERVICE PROCESS FAIL"),

	CART_ERROR(70, "CAET ERROR"), ADD_CART_FAIL(71, "ADD CART FAIL"), CART_TIMEOUT(73,
			"CART TIME OUT"), DELETE_CART_FAIL(72, "DELETE CART ITEM FAIL"), ADDRESS_EMPTY(74,
					"EMPTY ADDRESS"), CART_FULL(75, "CAET FULL"),

	ORDER_ERROR(80, "ORRDR ERROR"), ORDER_TIMEOUT(81, "ORDER TIME OUT"), ORDER_PARAM_EMPTY(82,
			"ORDER PARAM EMPTY"), ORDER_ADDRESS_ERROR(83, "ORDER ADDRESS ERROR"), ORDER_PAY_FAIL(84,
					"ORDER PAY FAIL"), ORDER_UNKNOW_ERROR(85, "ORDER UNKNOW ERROR"),

	SERVICE_BUSY(90, "SERVICE BUSY"), SERVICE_MAINTAIN(91, "SERVICE MAINTAIN"),

	COUPON_CANT_USE(100, "COUNPON CAN NOT USE"), /**
													 * 100以上用户自定义
													 */
	SUCCESS(200, "成功"),

	CART_COUNT_NOT_ENOUGH(401, "NO ENOUGH CART COUNT FOR THIS SKU ID"),

	CART_PO_INVALID(402, "PO IS INVALID"),

	CART_OVERFLOW(403, "OVER 10 ITEM IN THE CART"),

	CART_ITEMOVERFLOW(404, "进货单中最多只能加入50件不同的商品！"),

	CART_INTERNAL_ERROR(502, "CART SERVICE INTERNAL ERROR"), /**
																 * 500~599商品管理异常
																 */
	DELETE_FAILED(501, "DELETE FALIED CAUSE BY FOREIGN KEY CONSTRAINT"),

	OBJECT_TRANSFER_FAILED(502, "OBJECT TRANSFER FAILED"),

	STOCK_SERVICE_ERROR(503, "ITEM CENTER UNEXPECT ERROR"),

	ITEM_CENTER_ERROR(504, "ITEM CENTER UNEXPECT ERROR"),

	BARCODE_REPLICATION(505, "BARCODE REPLICATION"),

	NO_SKU_ERROR(506, "NEED AT LEAST ONE SKU"),

	PARAMETER_NOT_VALID(507, "NEED AT LEAST ONE SKU"),

	PROD_REPLICATION(508, "PRODUCT REPLICATION"),

	PROD_OFFLINE(509, "商品已下架或者被删除！"),

	/**
	 * 600~650权限管理异常
	 */
	BACKEND_AUTHORITY_MANAGEMENT_ERROR(600, "BACKEND AUTHORITY MANAGEMENT ERROR"),

	CMS_AUTHORITY_MANAGEMENT_ERROR(601, "CMS AUTHORITY MANAGEMENT ERROR"),
	/**
	 * 自定义错误异常by dj  700-799
	 */
	
	/**
	 * 不允许的操作
	 */
	PRODUCTSKU_NOT_EXIST(700,"PRODUCTSKU NOT EXIST"),
	
	NOT_ALLOWED_TO_OPERATE(701,"NOT ALLOWED TO OPERATE"),
	/**
	 * 用户未登录
	 */
	USERS_ARE_NOT_LOGIN(702,"USERS ARE NOT LOGIN"),
	/**
	 * 商家不存在
	 */
	STORE_DOES_NOT_EXIST(703,"The_STORE_DOES_NOT_EXIST"),
	
	INCOMING_PARAMETER_IS_ILLEGAL(704,"THE INCOMING PARAMETER IS ILLEGAL"),
	
	PRODUCT_SKU_LIMIT_ERROR(705,"PRODUCT_SKU_LIMIT_ERROR"),
	
	AREA_IS_NOT_OPENED(708,"对不起，您所在区域暂时还没开通，敬请期待！");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private static Map<Integer, ErrorCode> map = new HashMap<>();

	static {
		for (ErrorCode item : ErrorCode.values()) {
			map.put(item.getIntValue(), item);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private ErrorCode(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public ErrorCode genEnumByIntValue(int intValue) {
		for (ErrorCode item : ErrorCode.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
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

	public static ErrorCode getErrorCodeByIntValue(int value) {
		if (map.containsKey(value)) {
			return map.get(value);
		}
		return NULL;
	}
}
