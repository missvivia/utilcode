/**
 * 
 */
package com.xyl.mmall.oms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.meta.BociInfo;
import com.xyl.mmall.oms.meta.PickSkuItemForm;

/**
 * oms各类订单id生成工具类
 * 
 * @author hzzengchengyuan
 *
 */
public class OmsIdUtils {

	/**
	 * 注意，修改拣货单规则的同时，必须同时调整getDateStr和getPickMoldType
	 * 
	 * @param pickSkuItem
	 * @return
	 */
	public static String genPickOrdeId(PickSkuItemForm pickSkuItem) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		BociInfo bociInfo = OmsUtil.getBoci(pickSkuItem.getCreateTime(), pickSkuItem.getPickMoldType());
		String prefix = pickSkuItem.getPickMoldType() == PickMoldType.SINGLE ? "A" : "B";
		String pickOrderId = prefix + OmsConstants.DEFAULT_SPLIT_CHAR1
				+ sdf.format(new Date(bociInfo.getBociDeadLine())) + OmsConstants.DEFAULT_SPLIT_CHAR1
				+ bociInfo.getBociType().getIntValue() + OmsConstants.DEFAULT_SPLIT_CHAR1 + pickSkuItem.getSupplierId()
				+ OmsConstants.DEFAULT_SPLIT_CHAR1 + pickSkuItem.getStoreAreaId();
		return pickOrderId;
	}

	public static String getDateStr(String pickOrderId) {
		pickOrderId = backEmsOrderId(pickOrderId);
		if (pickOrderId.startsWith("A") || pickOrderId.startsWith("B")) {
			String[] arrays = pickOrderId.split(OmsConstants.DEFAULT_SPLIT_CHAR1);
			return arrays[1];
		} else {
			String[] arrays = pickOrderId.split(OmsConstants.DEFAULT_SPLIT_CHAR2);
			return arrays[0];
		}
	}

	public static long getDateLong(String pickOrdeId) {
		long dateLong = 0L;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = getDateStr(pickOrdeId);
		if (dateStr.indexOf("-") > 0) {
			try {
				dateLong = sdf2.parse(dateStr).getTime();
			} catch (ParseException e) {
			}
		} else {
			try {
				dateLong = sdf1.parse(dateStr).getTime();
			} catch (ParseException e) {
			}
		}
		return dateLong;
	}

	public static PickMoldType getPickMoldType(String pickOrderId) {
		pickOrderId = backEmsOrderId(pickOrderId);
		if (pickOrderId.startsWith("A") || pickOrderId.startsWith("B")) {
			String[] arrays = pickOrderId.split(OmsConstants.DEFAULT_SPLIT_CHAR1);
			String prefix = arrays[0];
			if (prefix.equals("A"))
				return PickMoldType.SINGLE;
			if (prefix.equals("B"))
				return PickMoldType.MANY;
		} else {
			// 根据拨次判断
			String[] arr = pickOrderId.split(OmsConstants.DEFAULT_SPLIT_CHAR2);
			int boci = Integer.valueOf(arr[1]);
			return (boci % 2 == 0) ? PickMoldType.MANY : PickMoldType.SINGLE;
		}
		return null;
	}

	public static void main(String[] args) {
		// 一期11.24拣货单格式: 年-月-日_拨次_商家id_B
		// 二期12.24拣货单格式:A/B-年月日-拨次-商家id-仓库id_B
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String[] arrays = new String[] { "2014-12-08_1_1_B", "2014-12-09_2_1", "B-20141221-2-3011-33",
				"A-20141221-1-4017-11", "A-20141221-1-4017-11_B" };
		for (String pickOrderId : arrays) {
			System.out.println("拣货单:" + pickOrderId + ",date:" + getDateStr(pickOrderId) + ",type:"
					+ getPickMoldType(pickOrderId) + ",datelong:" + sdf2.format(new Date(getDateLong(pickOrderId))));
		}
		System.out.println(Integer.parseInt("100", 2));
		
	}

	/**
	 * 生成补货单号
	 * 
	 * @param shipOrderId
	 *            需要补货的发货单号
	 * @return
	 */
	public static String genPickBuhuoId(String shipOrderId) {
		return shipOrderId.concat(OmsConstants.PICK_ID_SU);
	}

	/**
	 * 判断是否是补货单
	 * 
	 * @param orderId
	 * @return
	 */
	public static boolean isBuhuoOrder(String orderId) {
		return orderId != null && orderId.endsWith(OmsConstants.PICK_ID_SU);
	}

	/**
	 * 根据拣货单id反推出拣货单类型（单品 or 多品）
	 * 
	 * @param pickOrderId
	 * @return
	 */
	public static PickMoldType backPickMoldType(String pickOrderId) {
		return OmsIdUtils.getPickMoldType(pickOrderId);
	}

	public static WMSOrderType backWMSOrderTypeByShipOrderId(String ShipOrderId) {
		PickMoldType pickType = backPickMoldType(ShipOrderId);
		boolean isBuhuoOrder = isBuhuoOrder(ShipOrderId);
		if (isBuhuoOrder) {
			if (pickType == PickMoldType.MANY) {
				return WMSOrderType.SI_PM;
			} else {
				return WMSOrderType.SI_PS;
			}
		} else {
			if (pickType == PickMoldType.MANY) {
				return WMSOrderType.SI_M;
			} else {
				return WMSOrderType.SI_S;
			}
		}
	}

	/**
	 * 将oms的订单id加上前缀生成推送给ems的订单id
	 * 
	 * @param orderId
	 * @param type
	 * @return
	 */
	public static String genEmsOrderId(String orderId, WMSOrderType type) {
		// 先判断一下是否已经加了前缀，如果加了就直接返回
		WMSOrderType temp = judgeEmsWMSOrderType(orderId);
		if (temp != WMSOrderType.NULL) {
			return orderId;
		}
		return getEmsOrderIdPr(type).concat(orderId);
	}

	/**
	 * 将对应ems的订单id反推为oms订单id
	 * 
	 * @param orderId
	 * @return
	 */
	public static String backEmsOrderId(String orderId) {
		WMSOrderType type = judgeEmsWMSOrderType(orderId);
		if (type == WMSOrderType.NULL) {
			return orderId;
		}
		int index = orderId.indexOf(OmsConstants.DEFAULT_SPLIT_CHAR);
		if (index >= 0) {
			return orderId.substring(index + 1);
		} else {
			return orderId;
		}
	}

	/**
	 * 根据Ems回传的订单ID号，反推该订单类型（根据前缀来判断）
	 * 
	 * @param emsOrderId
	 * @return
	 */
	public static WMSOrderType judgeEmsWMSOrderType(String emsOrderId) {
		if (emsOrderId == null || emsOrderId.trim().length() == 0) {
			return WMSOrderType.NULL;
		}
		int index = emsOrderId.indexOf(OmsConstants.DEFAULT_SPLIT_CHAR);
		if (index < 0) {
			return WMSOrderType.NULL;
		}
		String pr = emsOrderId.substring(0, index + 1);
		switch (pr) {
		case OmsConstants.EMS_ID_PR_STOCKIN_SINGLE:
			return WMSOrderType.SI_S;
		case OmsConstants.EMS_ID_PR_STOCKIN_MULTI:
			return WMSOrderType.SI_M;
		case OmsConstants.EMS_ID_PR_STOCKIN_PATCH_SINGLE:
			return WMSOrderType.SI_PS;
		case OmsConstants.EMS_ID_PR_STOCKIN_PATCH_MULTI:
			return WMSOrderType.SI_PM;
		case OmsConstants.EMS_ID_PR_STOCKOUT:
			return WMSOrderType.SO;
		case OmsConstants.EMS_ID_PR_SALES:
			return WMSOrderType.SALES;
		case OmsConstants.EMS_ID_PR_RETURN:
			return WMSOrderType.RETURN;
		case OmsConstants.EMS_ID_PR_STOCKIN_UNRECEIPT:
			return WMSOrderType.R_UA;
		default:
			return WMSOrderType.NULL;
		}
	}

	public static String getEmsOrderIdPr(WMSOrderType type) {
		switch (type) {
		case SI_S:
			return OmsConstants.EMS_ID_PR_STOCKIN_SINGLE;
		case SI_M:
			return OmsConstants.EMS_ID_PR_STOCKIN_MULTI;
		case SI_PM:
			return OmsConstants.EMS_ID_PR_STOCKIN_PATCH_MULTI;
		case SI_PS:
			return OmsConstants.EMS_ID_PR_STOCKIN_PATCH_SINGLE;
		case R_UA:
			return OmsConstants.EMS_ID_PR_STOCKIN_UNRECEIPT;
		case SO:
			return OmsConstants.EMS_ID_PR_STOCKOUT;
		case SALES:
			return OmsConstants.EMS_ID_PR_SALES;
		case RETURN:
			return OmsConstants.EMS_ID_PR_RETURN;
		default:
			return "";
		}
	}
}
