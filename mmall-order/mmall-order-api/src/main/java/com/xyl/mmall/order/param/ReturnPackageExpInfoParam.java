package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.xyl.mmall.framework.enums.ExpressCompany;


/**
 * 退货信息写入数据库时传递的参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月18日 下午1:50:09
 *
 */
public class ReturnPackageExpInfoParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2813357959061605453L;

	// (desc = "退货-快递号")
	private String mailNO;

	// (desc = "退货-快递公司")
	private ExpressCompany expressCompany;

	// (desc = "退货地址Id")
	private String returnExpInfoId;

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getReturnExpInfoId() {
		return returnExpInfoId;
	}

	public void setReturnExpInfoId(String returnExpInfoId) {
		this.returnExpInfoId = returnExpInfoId;
	}
}
