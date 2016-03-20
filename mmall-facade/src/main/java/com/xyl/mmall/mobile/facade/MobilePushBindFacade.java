/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import org.springframework.web.bind.annotation.RequestParam;

import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * @author hzjiangww
 *
 */
public interface MobilePushBindFacade {
	//绑定push接口
	BaseJsonVO bindReceiver(String deviceId,long userId,int areaCode,Double latitude, Double longitude);
	//经纬度定位 
	BaseJsonVO getProvinceCode(String ip,Double latitude,Double longitude);
	
}