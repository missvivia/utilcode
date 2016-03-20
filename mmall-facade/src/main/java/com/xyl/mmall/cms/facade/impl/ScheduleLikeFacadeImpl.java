package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.cms.facade.ScheduleLikeFacade;
import com.xyl.mmall.cms.vo.ScheduleLikeVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleLikeService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Facade
public class ScheduleLikeFacadeImpl implements ScheduleLikeFacade {

	@Resource
	private ScheduleLikeService likeService;

	@Override
	public ScheduleLikeVO addLike(ScheduleLikeDTO dto) {
		ScheduleLikeDTO dbDto = likeService.like(dto);
		ScheduleLikeVO vo = new ScheduleLikeVO();
		vo.setLike(dbDto);
		return vo;
	}

	@Override
	public boolean cancelLike(ScheduleLikeDTO dto) {
		return likeService.unLike(dto);
	}

	@Override
	public List<ScheduleLikeVO> getLikeListByUserId(long userId) {
		List<ScheduleLikeDTO> dtoList = likeService.getLikeListByUserId(userId);
		return _geneLikeVOList(dtoList);
	}

	@Override
	public List<ScheduleLikeVO> getLikeListByPOId(long poId) {
		List<ScheduleLikeDTO> dtoList = likeService.getLikeListByPOId(poId);
		return _geneLikeVOList(dtoList);
	}

	private List<ScheduleLikeVO> _geneLikeVOList(List<ScheduleLikeDTO> dtoList) {
		List<ScheduleLikeVO> voList = new ArrayList<ScheduleLikeVO>();
		for (ScheduleLikeDTO dto : dtoList) {
			ScheduleLikeVO vo = new ScheduleLikeVO();
			vo.setLike(dto);

			voList.add(vo);
		}
		return voList;
	}

//	@Override
//	public ScheduleListVO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long startId, int curPage,
//			int pageSize) {
//		POListDTO poList = likeService.getLikedPOListByUserIdAndSiteId(userId, supplierAreaId, startId, curPage,
//				pageSize);
//
//		ScheduleListVO vo = new ScheduleListVO();
//		vo.setPoList(poList);
//
//		return vo;
//	}

	public long getLikeCntByPOId(long poId) {
		return likeService.getLikeCntByPOId(poId);
	}

	@Autowired
	private BrandService brandSerivce;
	
	@Override
	public boolean isLike(long userId, long brandId) {
		boolean result = false;
		try {
			result = brandSerivce.isBrandInFavList(userId, brandId);
		} catch (Exception e) {
			result = false;
		}
		
		return result;
//		return likeService.isLike(userId, poId);
	}

}
