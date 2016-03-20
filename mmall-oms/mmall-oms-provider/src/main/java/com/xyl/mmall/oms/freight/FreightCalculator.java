package com.xyl.mmall.oms.freight;

import com.xyl.mmall.oms.enums.WMSExpressCompany;

/**
 * 运费计算器
 * 
 * @author hzzengchengyuan
 *
 */
public interface FreightCalculator {
	/**
	 * 获取该运费计算器的快递公司类别
	 * 
	 * @return
	 */
	WMSExpressCompany type();

	/**
	 * 计算运费
	 * 
	 * @param meta
	 * @return
	 */
	FreightCalcResult calcuate(FreightCalcMeta meta);
	
	/**
	 * 用户退货时的服务费
	 * @return
	 */
	double getUserReturnServieCharge();

}
