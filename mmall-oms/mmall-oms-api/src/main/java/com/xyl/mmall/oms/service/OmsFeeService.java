package com.xyl.mmall.oms.service;

import com.xyl.mmall.oms.dto.ExpressFeeDTO;
import com.xyl.mmall.oms.dto.OmsExpPriceParam;

/**
 * 仓储相关费用服务接口
 * <p>
 * 含:快递费等
 * @author hzzhaozhenzuo
 *
 */
public interface OmsFeeService {
	
	/**
	 * 根据参数计算快递费
	 * @param param
	 * @return
	 */
	public ExpressFeeDTO calExpressFee(OmsExpPriceParam param);

}
