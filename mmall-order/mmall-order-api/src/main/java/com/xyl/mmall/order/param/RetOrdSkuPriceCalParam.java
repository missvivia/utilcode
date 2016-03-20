package com.xyl.mmall.order.param;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 上午9:41:49
 *
 */
@Deprecated
public interface RetOrdSkuPriceCalParam {

	public long idOfOrderSku();
	
	public long countOfReturn(boolean applySituation);
	
}
