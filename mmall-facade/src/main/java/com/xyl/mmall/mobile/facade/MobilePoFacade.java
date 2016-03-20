/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;

/**
 * @author hzjiangww
 *
 */
public interface MobilePoFacade {
	//获取PO list
	BaseJsonVO getPOList(long userId ,int areaCode,Integer type,Integer online , MobilePageCommonAO pager);
	
	//PO 详情
	//TODO
	BaseJsonVO getPODetail(long userId ,Long poId,Integer sort,String filter,Integer needhead,
			MobilePageCommonAO pager,int areaCode,long appversion) ;
	//专场种类列表
	BaseJsonVO getScheduleChannel();
}