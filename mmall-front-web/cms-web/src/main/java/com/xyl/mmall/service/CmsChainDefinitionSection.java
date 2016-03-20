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

import com.xyl.mmall.cms.facade.CmsAuthcFacade;
import com.xyl.mmall.cms.vo.CmsFilterChainResourceVO;

/**
 * 运维平台系统访问权限过滤配置。
 * 
 * @author lihui
 *
 */
@Service("appChainDefinitionSection")
public class CmsChainDefinitionSection implements FactoryBean<Ini.Section> {

	@Autowired
	private CmsAuthcFacade authcFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Section getObject() throws Exception {
		List<CmsFilterChainResourceVO> resourceVOList = authcFacade.getCmsFilterChainResource();
		Ini ini = new Ini();
		Ini.Section section = ini.addSection(Ini.DEFAULT_SECTION_NAME);
		for (CmsFilterChainResourceVO resourceVO : resourceVOList) {
			if (!StringUtils.isEmpty(resourceVO.getFilterChainResource().getUrl())
					&& !StringUtils.isEmpty(resourceVO.getFilterChainResource().getPermission())) {
				section.put(resourceVO.getFilterChainResource().getUrl(), resourceVO.getFilterChainResource()
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
