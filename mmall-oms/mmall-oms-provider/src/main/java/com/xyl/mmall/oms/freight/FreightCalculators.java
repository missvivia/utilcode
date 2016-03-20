package com.xyl.mmall.oms.freight;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.oms.enums.WMSExpressCompany;

/**
 * @author hzzengchengyuan
 *
 */
@Component
public final class FreightCalculators {
	
	private static FreightCalculator getCalculator(WMSExpressCompany ec) {
		return INSTANCE.calculators.get(ec);
	}

	private Map<WMSExpressCompany, FreightCalculator> calculators = new HashMap<WMSExpressCompany, FreightCalculator>();

	private static FreightCalculators INSTANCE = new FreightCalculators();

	private FreightCalculators() {
	}

	@Autowired
	public void setCalculator(FreightCalculator[] p_calculators) {
		if (p_calculators != null) {
			for (FreightCalculator c : p_calculators) {
				INSTANCE.calculators.put(c.type(), c);
			}
		}
	}

	/**
	 * 计算运费，如果结果返回null，则表示无运费产生
	 * 
	 * @param meta
	 * @return
	 */
	public static FreightCalcResult calcuate(FreightCalcMeta meta) {
		if (meta.getExpressCompany() == null) {
			throw new IllegalArgumentException("快递公司类型不能为空.");
		}
		return getCalculator(meta.getExpressCompany()).calcuate(meta);
	}

	/**
	 * 获取用户退货时产生的服务费用
	 * 
	 * @return
	 */
	public static double getUserReturnServieCharge(WMSExpressCompany expressCompany) {
		if (expressCompany == null) {
			throw new IllegalArgumentException("快递公司类型不能为空或不支持.");
		}
		return getCalculator(expressCompany).getUserReturnServieCharge();
	}

}
