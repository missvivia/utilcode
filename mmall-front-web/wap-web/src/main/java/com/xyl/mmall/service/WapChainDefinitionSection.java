/**
 * 
 */
package com.xyl.mmall.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.wap.facade.WapAuthcFacade;
import com.xyl.mmall.wap.vo.WapFilterChainResourceVO;

/**
 * WAP系统访问权限过滤配置。
 * 
 * @author lihui
 *
 */
@Service("appChainDefinitionSection")
public class WapChainDefinitionSection implements FactoryBean<Ini.Section> {

	@Autowired
	private WapAuthcFacade wapAuthcFacade;
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Section getObject() throws Exception {
		List<WapFilterChainResourceVO> resourceVOList = wapAuthcFacade.getWapFilterChainResource();
		Ini ini = new Ini();
		Ini.Section section = ini.addSection(Ini.DEFAULT_SECTION_NAME);
		for (WapFilterChainResourceVO resourceVO : resourceVOList) {
			if (!StringUtils.isEmpty(resourceVO.getFilterChainResourceDTO().getUrl())
					&& !StringUtils.isEmpty(resourceVO.getFilterChainResourceDTO().getPermission())) {
				section.put(resourceVO.getFilterChainResourceDTO().getUrl(), resourceVO.getFilterChainResourceDTO()
						.getPermission());
			}
		}
		return section;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return Ini.Section.class;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
}
