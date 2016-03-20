package com.xyl.mmall.bi.core.meta;

import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;

/**
 * 销售数据日志.<br/>
 * userId,provinceId在实现类里自行设值.
 * 
 * @author wangfeng
 *
 */
public class OrderLog extends BasicLog {

	private static final long serialVersionUID = -4893308726175198637L;

	public OrderLog() {
		super();
	}

	/**
	 * 构造函数.<br/>
	 * 设置time,action,type值.
	 * 
	 * @param biLog
	 */
	public OrderLog(BILog biLog) {
		super();
		setTime(System.currentTimeMillis());
		setClientType(ClientType.NULL.genEnumByValue(biLog.clientType()));
		setType(BIType.UNKNOWN.genEnumByValue(biLog.type()));
	}

}
