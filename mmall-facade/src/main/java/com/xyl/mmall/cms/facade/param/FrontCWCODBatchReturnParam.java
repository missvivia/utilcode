package com.xyl.mmall.cms.facade.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务批量确认退款（COD退货）接口参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月11日 下午3:50:31
 *
 */
public class FrontCWCODBatchReturnParam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6340508444500097307L;

	public static class KVPair implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2951727846251930709L;
		private long retPkgId;
		private long userId;
		public long getRetPkgId() {
			return retPkgId;
		}
		public void setRetPkgId(long retPkgId) {
			this.retPkgId = retPkgId;
		}
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
	}
	
	private List<KVPair> batchParam = new ArrayList<KVPair>();

	public List<KVPair> getBatchParam() {
		return batchParam;
	}

	public void setBatchParam(List<KVPair> batchParam) {
		this.batchParam = batchParam;
	}
	
}
