/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.promotion.meta.PointOrder;

/**
 * PointOrderTCC.java created by yydx811 at 2015年12月23日 上午10:10:21
 * 订单积分TCC
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "订单积分TCC表", tableName = "Mmall_Promotion_PointOrderTCC")
public class PointOrderTCC extends PointOrder implements TCCMetaInterface {

	/** 序列化id. */
	private static final long serialVersionUID = -7140211968231180736L;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getTranId() {
		return tranId;
	}

	public void setTranId(long tranId) {
		this.tranId = tranId;
	}

	public long getCtimeOfTCC() {
		return ctimeOfTCC;
	}

	public void setCtimeOfTCC(long ctimeOfTCC) {
		this.ctimeOfTCC = ctimeOfTCC;
	}
	
	public PointOrder getPointOrder() {
		return this.cloneObject();
	}
}
