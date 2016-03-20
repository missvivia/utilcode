/**
 * 
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.facade.PointFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.cms.vo.PointVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.cms.vo.UserPointVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.TransformType;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.dto.UserPointDTO;
import com.xyl.mmall.promotion.enums.AuditState;
import com.xyl.mmall.promotion.meta.Point;
import com.xyl.mmall.promotion.service.PointService;
import com.xyl.mmall.promotion.service.UserPointService;

/**
 * @author jmy
 *
 */
@Facade("pointFacade")
public class PointFacadeImpl implements PointFacade {
	private static final String USERIDS_SEPARATOR = ",";
	
	@Autowired
	private PointService pointService;
	
	@Resource
	private SiteCMSService siteCMSService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private UserPointService userPointService;
	
	@Resource
	private UserProfileService userProfileService;
	
	@Autowired
	private SiteCMSFacade siteCMSFacade;

	/* (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.PointFacade#getPointList()
	 */
	@Override
	public List<PointVO> getPointList(PointDTO queryObject,BasePageParamVO<PointVO> page) {
		List<PointDTO> lstDto = pointService.getPointList(queryObject, page.getPageSize(), page.getStartRownum()-1);
		return convert2VO(lstDto);
	}

	private List<PointVO> convert2VO(List<PointDTO> lstDto) {
		if (lstDto == null) return null;
		List<PointVO> result = new ArrayList<>(lstDto.size());
		for (PointDTO dto : lstDto) {
			if (dto == null) continue;
			result.add(this.convert2VO(dto));
		}
		return result;
	}
	
	private PointVO convert2VO(PointDTO dto) {
		if (dto == null) return null;
		PointVO vo = new PointVO();
		if (dto.getApplyUserId() != null) {
			AgentDTO applyAgentDTO = agentService.findAgentById(dto.getApplyUserId());
			if (applyAgentDTO != null) vo.setApplyAgentName(applyAgentDTO.getRealName());
		}
		if (dto.getAuditUserId() != null) {
			AgentDTO agentDTO = agentService.findAgentById(dto.getAuditUserId());
			if (agentDTO != null) vo.setAuditAgentName(agentDTO.getRealName());
		}		
		vo.setAuditReason(dto.getReason());
		vo.setAuditState(dto.getAuditState());
		vo.setAuditTime(dto.getAuditTime());
		vo.setPointDelta(dto.getPointDelta());
		vo.setPointDescription(dto.getDescription());
		vo.setPointId(dto.getId());
		vo.setPointName(dto.getName());
		vo.setSiteId(dto.getSiteId());
//		//获取用户初始积分
//		if (dto.getUsers() != null && !dto.getUsers().isEmpty()) {
//			Long[] userIds = getUserIds(dto.getUsers());
//			int[] point = new int[userIds.length];
//			for (int i = 0; i < userIds.length; i ++) {
//				UserPointDTO userPointDto = userPointService.getUserPointByUserId(userIds[i]);
//				if (userPointDto != null) {
//					point[i] = userPointDto.getPoint();
//				}
//			}
//			vo.setPoint(point);
//		}
		//获取站点名称
		if (dto.getSiteId() != null && dto.getSiteId() > 0) {
			SiteCMSDTO site = siteCMSService.getSiteCMS(dto.getSiteId(), false);
			if (site != null) {
				vo.setSiteName(site.getName());
			}
		}
//		else {//前端渲染名字
//			vo.setSiteName("全部站点");
//		}
		vo.setUserAccountList(dto.getUsers());
		return vo;
	}
	
	private static Long[] getUserIds(String users) {
		return TransformType.transform(StringUtils.split(users, USERIDS_SEPARATOR), true);
	}

	@Override
	public PointVO getPoint(long id) {
		PointDTO dto = new PointDTO();
		dto.setSiteId(this.getCurrentSite().getSiteId());
		dto.setId(id);
		List<PointDTO> points = pointService.getPointList(dto, 1, 0);
		if (points == null || points.isEmpty()) 
			return null;
		return this.convert2VO(points.get(0));
	}

	@Override
	public UserPointVO getUserPoint(long userId) {
		UserProfileDTO userProfileDto = userProfileService.findUserProfileById(userId);
		return this.convert(userProfileDto);		
	}
	
	@Override
	public List<UserPointVO> getUserPointList(BasePageParamVO<UserPointVO> basePageParamVO, String searchValue) {

		BasePageParamVO<UserProfileDTO> pageParamVO = new BasePageParamVO<UserProfileDTO>();
		if (basePageParamVO != null) {
			pageParamVO = basePageParamVO.copy(pageParamVO);
		} else {
			pageParamVO.setIsPage((short)0);//不分页
		}
		pageParamVO = userProfileService.queryUserList(pageParamVO, searchValue);
		if (pageParamVO != null) {
			if (pageParamVO.getList() != null && !pageParamVO.getList().isEmpty()) {
				List<UserProfileDTO> lstUserProfileDTO = pageParamVO.getList();
				List<UserPointVO> lstUserPointVO = new ArrayList<>(lstUserProfileDTO.size());
				for (int i = 0; i < lstUserProfileDTO.size(); i ++) {
					UserProfileDTO userProfileDto = lstUserProfileDTO.get(i);
					if (userProfileDto == null) continue;
					lstUserPointVO.add(this.convert(userProfileDto));
				}
				return lstUserPointVO;
			}
		}
		return Collections.emptyList();
	}
	
	private UserPointVO convert(UserProfileDTO userProfileDto) {
		if (userProfileDto == null) return null;
		UserPointDTO userPointDto = userPointService.getUserPointByUserId(userProfileDto.getUserId());
		UserPointVO vo = null;
		if (userPointDto == null) {
			vo = new UserPointVO();
			vo.setUserId(userProfileDto.getUserId());
			vo.setPoint(0);
		} else {
			vo = new UserPointVO(userPointDto);
		}
		vo.setUserName(userProfileDto.getUserName());
		return vo;
	}

	@Override
	public boolean changeState(long id, int auditState) {
		PointDTO dto = new PointDTO();
		dto.setId(id);
		dto.setSiteId(this.getCurrentSite().getSiteId());
		List<PointDTO> lstDto = pointService.getPointList(dto, 1, 0);
		if (lstDto != null && !lstDto.isEmpty()) {
			long userId = SecurityContextUtils.getUserId();
			return pointService.updateStateById(id, AuditState.INIT.genEnumByIntValue(auditState), userId) != null;
		}
		return false;
	}

	@Override
	public Point savePoint(Point point) {
		if (point == null) throw new IllegalArgumentException();
		long userId = SecurityContextUtils.getUserId();
		if (point.getId() > 0) {//编辑
			SiteCMSVO currentSite = this.getCurrentSite();
			if (currentSite.getSiteId() != 0 //不是超级管理员
					&& point.getSiteId() != currentSite.getSiteId()) {
				throw new UnauthorizedException("未授权编辑该站点数据");
			} 
			UserPointDTO userPointDto = userPointService.getUserPointByUserId(Long.valueOf(point.getUsers()));
			if ((userPointDto == null && point.getPointDelta() < 0) 
					||(userPointDto != null && (userPointDto.getPoint() + point.getPointDelta() < 0))) {
				throw new IllegalArgumentException("积分不能小于0");
			}
			point.setApplyUserId(userId);
			return pointService.updatePointPartlyById(new PointDTO(point));
		} else {//新建
			point.setApplyUserId(userId);
			point.setAuditState(AuditState.INIT.getType());			
			if (point.getPointDelta() < 0) {
				throw new IllegalArgumentException("积分不能小于0");
			} 
			return pointService.addPoint(point);
		}
	}

	@Override
	public SiteCMSVO getCurrentSite() {
		long userId = SecurityContextUtils.getUserId();
		List<SiteCMSVO> lstSiteCMS = siteCMSFacade.getAgentSiteOf(userId, false);
		if(lstSiteCMS !=null &&  lstSiteCMS.size() > 0){
			return lstSiteCMS.get(0);
		}
		SiteCMSVO vo = new SiteCMSVO();
		vo.setSiteId(0L);
//		vo.setSiteName("全部站点");
		return vo;
	}

}
