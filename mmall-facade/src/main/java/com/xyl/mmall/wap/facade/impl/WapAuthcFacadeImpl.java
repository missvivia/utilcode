/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.wap.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.FilterChainResourceDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.service.FilterChainResourceService;
import com.xyl.mmall.wap.facade.WapAuthcFacade;
import com.xyl.mmall.wap.vo.WapFilterChainResourceVO;

/**
 * WapAuthcFacadeImpl.java created by yydx811 at 2015年10月22日 上午11:44:07
 * wap认证相关facade接口实现
 *
 * @author yydx811
 */
@Facade
public class WapAuthcFacadeImpl implements WapAuthcFacade {

	@Resource
	private FilterChainResourceService filterChainResourceService;
	
	@Override
	public List<WapFilterChainResourceVO> getWapFilterChainResource() {
		List<FilterChainResourceDTO> resourceDTOList = 
				filterChainResourceService.findResourceByCategory(AuthzCategory.WAP.getIntValue());
		if (CollectionUtils.isEmpty(resourceDTOList)) {
			return null;
		}
		List<WapFilterChainResourceVO> resourceVOList = new ArrayList<WapFilterChainResourceVO>();
		for (FilterChainResourceDTO resourceDTO : resourceDTOList) {
			resourceVOList.add(new WapFilterChainResourceVO(resourceDTO));
		}
		return resourceVOList;
	}

}
