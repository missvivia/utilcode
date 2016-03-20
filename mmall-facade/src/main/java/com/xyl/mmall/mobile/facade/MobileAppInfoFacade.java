/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.converter.MobileOS;
import com.xyl.mmall.mobile.facade.param.MobileFeedBackAO;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;

/**
 * @author hzjiangww
 *
 */
public interface MobileAppInfoFacade {

	public BaseJsonVO getInitImage(int areaId,int os,String r);

	public BaseJsonVO getBannerImage(int areaId,String os);
	
	public BaseJsonVO getRecommend(String channel, int areaId,MobileHeaderAO ao);
	
	public BaseJsonVO feedBack(String userName,int areaId, MobileFeedBackAO feedBack);
	
	public BaseJsonVO updateVersion(MobileOS os, String channl ,boolean update) ;
}