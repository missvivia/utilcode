/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.promotion.dao.PointRuleDao;
import com.xyl.mmall.promotion.dto.PointRuleDTO;
import com.xyl.mmall.promotion.meta.PointRule;
import com.xyl.mmall.promotion.service.PointRuleService;

/**
 * PointRuleServiceImpl.java created by yydx811 at 2015年12月23日 下午3:59:15
 * 积分规则service接口实现
 *
 * @author yydx811
 */
@Service("pointRuleService")
public class PointRuleServiceImpl implements PointRuleService {

	@Autowired
	private PointRuleDao pointRuleDao;

	@Override
	@Cacheable(key = "pointRuleCache", value = "#id")
	public PointRuleDTO getPointRuleById(long id) {
		return new PointRuleDTO(pointRuleDao.getObjectById(id));
	}

	@Override
	@Cacheable(key = "pointRuleCache", value = "#pointRuleDTO.siteId")
	public List<PointRuleDTO> getPointRuleList(PointRuleDTO pointRuleDTO) {
		List<PointRule> pointRuleList = pointRuleDao.getRuleList(pointRuleDTO);
		return convertToDTO(pointRuleList);
	}

	@Override
	public List<PointRuleDTO> getPointRuleListNoCache(PointRuleDTO pointRuleDTO) {
		List<PointRule> pointRuleList = pointRuleDao.getRuleList(pointRuleDTO);
		return convertToDTO(pointRuleList);
	}
	
	private List<PointRuleDTO> convertToDTO(List<PointRule> pointRuleList) {
		if (CollectionUtils.isEmpty(pointRuleList)) {
			return new ArrayList<PointRuleDTO>(0);
		}
		List<PointRuleDTO> retList = new ArrayList<PointRuleDTO>(pointRuleList.size());
		for (PointRule pointRule : pointRuleList) {
			retList.add(new PointRuleDTO(pointRule));
		}
		return retList;
	}
}
