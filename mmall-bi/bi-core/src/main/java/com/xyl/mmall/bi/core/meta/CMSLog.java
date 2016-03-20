/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.bi.core.meta;

import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;

/**
 * CMSLog.java created by yydx811 at 2015年12月30日 上午10:26:55
 * cms日志
 *
 * @author yydx811
 */
public class CMSLog extends BasicLog {

	/** 序列化id. */
	private static final long serialVersionUID = -7607684584505316866L;

	public CMSLog() {
		super();
	}
	
	public CMSLog(BILog biLog) {
		super();
		setTime(System.currentTimeMillis());
		setClientType(ClientType.NULL.genEnumByValue(biLog.clientType()));
		setType(BIType.UNKNOWN.genEnumByValue(biLog.type()));
	}
}
