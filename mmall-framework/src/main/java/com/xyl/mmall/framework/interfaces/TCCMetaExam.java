package com.xyl.mmall.framework.interfaces;

import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 支持TCC服务的Meta样例
 * 
 * @author dingmingliang
 * 
 */
public class TCCMetaExam implements TCCMetaInterface {

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getCtimeOfTCC() {
		return ctimeOfTCC;
	}

	public void setCtimeOfTCC(long ctimeOfTCC) {
		this.ctimeOfTCC = ctimeOfTCC;
	}

	public long getTranId() {
		return tranId;
	}

	public void setTranId(long tranId) {
		this.tranId = tranId;
	}
}
