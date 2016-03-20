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
public interface MobileFavoriteFacade {
	//获取收场
	BaseJsonVO getFavoriteList(Long userId,int areaCode,Integer type,
			MobilePageCommonAO pager);
	//添加收场
	BaseJsonVO addFavorite(Long userId,Integer type, Long id,long areaId);
	//取消收场
	BaseJsonVO cancelFavorite(Long userId,Integer type,Long id);
}