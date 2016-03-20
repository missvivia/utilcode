/**
 * 
 */
package com.xyl.mmall.oms.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;

/**
 * 生成二退数据，发货正品数-已发货订单的商品总数
 * 
 * @author hzzengchengyuan
 *
 */
@Service
@JobPath("/oms/genTwoReturnJob")
public class GenTwoReturnJob extends BaseJob {
	private static final Logger logger = LoggerFactory.getLogger(GenTwoReturnJob.class);

	@Autowired
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	@Override
	public boolean execute(JobParam param) {
		boolean isSucc = false;
		logger.info("genTwoReturnJob started");
		try {
			isSucc = jITSupplyManagerFacade.genTwoReturnJob();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		logger.info("genTwoReturnJob ended");
		return isSucc;
	}

}
