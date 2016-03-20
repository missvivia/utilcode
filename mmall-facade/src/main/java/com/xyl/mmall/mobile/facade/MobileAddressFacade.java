/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.param.MobileAddressAO;

/**
 * @author hzjiangww
 *
 */
public interface MobileAddressFacade {
	//获得地址列表，不分页
	BaseJsonVO getConsigneeAddressList(long userId);
	
	//更新地址列表
	BaseJsonVO updateConsigneeAddress(long userId,MobileAddressAO ao);
	
	BaseJsonVO getAreaCodeList(Long timeStamp);
}