/**
 * 
 */
package com.xyl.mmall.oms.enums;


/**
 * OMS模块常量集合
 * @author hzzengchengyuan
 *
 */
public class OmsConstants {
	/**
	 * 发送给ems的单品入库单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#SI_S}
	 */
	public static final String EMS_ID_PR_STOCKIN_SINGLE = "SIS_";
	
	/**
	 * 发送给ems的多品入库单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#SI_M}
	 */
	public static final String EMS_ID_PR_STOCKIN_MULTI = "SIM_";
	
	/**
	 * 发送给ems的单品补货入库单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#SI_PS}
	 */
	public static final String EMS_ID_PR_STOCKIN_PATCH_SINGLE = "SIPS_";
	
	/**
	 * 发送给ems的多品补货入库单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#SI_PM}
	 */
	public static final String EMS_ID_PR_STOCKIN_PATCH_MULTI = "SIPM_";
	
	/**
	 * 发送给ems的出库单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#SO}
	 */
	public static final String EMS_ID_PR_STOCKOUT = "SO_";
	
	/**
	 * 发送给ems的销售订单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#SALES}
	 */
	public static final String EMS_ID_PR_SALES = "S_";
	
	/**
	 * 发送给ems的退货(用户)单单号前所加的前缀值：{@value}，对应订单类型为{@link WMSOrderType#RETURN}
	 */
	public static final String EMS_ID_PR_RETURN = "R_";
	
	/**
	 * 未妥投入库单所加前缀值：{@value}，对应订单类型为{@link WMSOrderType#R_UA}
	 */
	public static final String EMS_ID_PR_STOCKIN_UNRECEIPT = "RUA_";
	
	
	/**
	 * 商家商品入库后货品不足或有次品时重新生成补货单的后缀:{@value}
	 */
	public static final String PICK_ID_SU = "_B";
	
	public static final String DEFAULT_SPLIT_CHAR2 = "_";
	
	public static final String DEFAULT_SPLIT_CHAR1 = "-";
	
	public static final String DEFAULT_SPLIT_CHAR = DEFAULT_SPLIT_CHAR2;
	
	/**
	 * 很早以前
	 */
	public final static String LONG_BEFORE = "1970-01-01";
	
	/**
	 * 很久以后
	 */
	public final static String LONG_AFTER = "2100-01-01";
	
	public final static long LONG_BEFORE_LONG = -28800000L;
	
	public final static long LONG_AFTER_LONG = 64060560000000L;
	
	public static final int DAY4 = 4;
	public static final long MILISEC_4DAY = 1000L * 60 * 60 * 24 * DAY4;
	public static final int DAY5 = 5;
	public static final long MILISEC_5DAY = 1000L * 60 * 60 * 24 * DAY5;
	public static final int DAY30 = 30;
	public static final long MILISEC_30DAY = 1000L * 60 * 60 * 24 * DAY30;
	
}
