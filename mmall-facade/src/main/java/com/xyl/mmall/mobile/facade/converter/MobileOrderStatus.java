package com.xyl.mmall.mobile.facade.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.mobile.facade.vo.MobileKeyPairVO;


public enum MobileOrderStatus {
	
	UNPAY(0, "未付款"),
	WAITVERIFY(1, "待审核"),
	WAITSEND1(2, "待发货可以取消"),
	_RETURN(5, "申请退货"),
	WAITSEND2(6, "待发货"),
	RETURNED(8, "已退款"),
	RETURNFAIL(9, "退货失败"),
	SENDED(10,"已发货"),
	RETURNING(11, "退货中"),
	FINISH(15, "交易完成"),
	CANCELING(20, "取消中"),
	CANCEL(21, "已取消"),
	VERIFYFAIL(25, "审核未通过"),
	LOGISTERROR(33, "投递问题"),
	UNKNOW(99,"");
	private final int value;

	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private MobileOrderStatus(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	public static MobileOrderStatus getOsNameById(int id){
		for(MobileOrderStatus s : MobileOrderStatus.values()){
			if(s.getIntValue() == id)
				return s;
		}
		return MobileOrderStatus.UNKNOW;
	}

}
