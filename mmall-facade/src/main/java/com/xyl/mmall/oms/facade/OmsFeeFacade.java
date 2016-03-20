package com.xyl.mmall.oms.facade;

import com.xyl.mmall.oms.dto.ExpressFeeDTO;
import com.xyl.mmall.oms.dto.OmsExpPriceParam;

public interface OmsFeeFacade {
	
	/**
	 * 根据参数计算快递费
	 * @param param
	 * @return
	 */
	public ExpressFeeDTO calExpressFee(OmsExpPriceParam param);
}
