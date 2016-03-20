package com.xyl.mmall.cms.vo.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月28日 下午4:50:41
 *
 */
public class TradeOrderInfoListVO {

	private int total = 0;
	
	private List<TradeOrderInfo> list = new ArrayList<TradeOrderInfo>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<TradeOrderInfo> getList() {
		return list;
	}

	public void setList(List<TradeOrderInfo> list) {
		this.list = list;
	}
	
	
}
