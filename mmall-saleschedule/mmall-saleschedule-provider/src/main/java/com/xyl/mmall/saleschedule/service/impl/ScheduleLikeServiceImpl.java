package com.xyl.mmall.saleschedule.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;
import com.xyl.mmall.saleschedule.meta.ScheduleLike;
import com.xyl.mmall.saleschedule.service.ScheduleLikeService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Service
public class ScheduleLikeServiceImpl extends ScheduleBaseService implements ScheduleLikeService {

	@Override
	public ScheduleLikeDTO like(ScheduleLikeDTO dto) {
		logger.debug("addLike: " + dto);
		boolean result = likeDao.saveScheduleLike(dto.getLike());
		if (result) {
			return dto;
		} else {
			ScheduleLikeDTO pDto = new ScheduleLikeDTO();
			pDto.setLike(null);
			return pDto;
		}
	}

	@Override
	public boolean unLike(ScheduleLikeDTO dto) {
		logger.debug("cancelLike: " + dto);
		return likeDao
				.deleteScheduleLikeByScheduleIdAndUserId(dto.getLike().getScheduleId(), dto.getLike().getUserId());
	}

	@Override
	public boolean unlikeByPoId(long poId) {
		logger.debug("unlikeByPoId: " + poId);
		return likeDao.deleteScheduleLikeByScheduleId(poId);
	}

	@Override
	public boolean unlikeByUserId(long userId) {
		logger.debug("unlikeByUserId: " + userId);
		return likeDao.deleteScheduleLikeByUserId(userId);
	}

	@Override
	public boolean isLike(long userId, long poId) {
		ScheduleLike like = likeDao.getScheduleLikeByScheduleIdAndUserId(poId, userId);
		return like != null;
	}

	@Override
	public List<ScheduleLikeDTO> getLikeListByUserId(long userId) {
		logger.debug("getLikeListByUserId: " + userId);
		List<ScheduleLike> iList = likeDao.getScheduleLikeListByUserId(userId);
		return _genDtoList(iList);
	}

//	@Override
//	public POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long timestamp, int limit) {
//
//		POListDTO poList = likeDao.getLikedPOListByUserIdAndSiteId(userId, supplierAreaId, timestamp, limit);
//
//		addFavField(poList, userId);
//
//		return poList;
//	}
//
//	@Override
//	public POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long startId, int curPage,
//			int pageSize) {
//		curPage = adjustCurPage(curPage);
//
//		POListDTO poList = likeDao.getLikedPOListByUserIdAndSiteId(userId, supplierAreaId, startId, curPage, pageSize);
//
//		addFavField(poList, userId);
//
//		return poList;
//	}

	@Override
	public Map<Long, Boolean> batchCheckLikeStatus(List<Long> poIds, Long userId) {
		Map<Long, Boolean> result = new ConcurrentHashMap<Long, Boolean>();
		if (userId == null || userId <= 0) {
			logger.error("User id cannot be null!!!");
			return result;
		}

		if (poIds == null || poIds.size() == 0) {
			return result;
		}

		List<ScheduleLike> likeList = likeDao.getListByUserId(userId);
		if (likeList == null || likeList.size() == 0) {
			logger.debug("The user hasn't liked any PO yet!!!");
			return result;
		}

		List<Long> likedPoIdList = new ArrayList<Long>();

		for (ScheduleLike like : likeList) {
			likedPoIdList.add(like.getScheduleId());
		}

		for (Long poId : poIds) {
			if (likedPoIdList.contains(poId)) {
				result.put(poId, true);
			} else {
				result.put(poId, false);
			}
		}

		return result;
	}

	@Override
	public List<ScheduleLikeDTO> getLikeListByPOId(long poId) {
		logger.debug("getLikeListByPOId: " + poId);
		List<ScheduleLike> iList = likeDao.getScheduleLikeListByScheduleId(poId);
		return _genDtoList(iList);
	}

	@Override
	public long getLikeCntByPOId(long poId) {
		return likeDao.getLikeCntByPOId(poId);
	}

	private List<ScheduleLikeDTO> _genDtoList(List<ScheduleLike> iList) {
		if (iList == null) {
			return new ArrayList<ScheduleLikeDTO>();
		}

		List<ScheduleLikeDTO> dtoList = new ArrayList<ScheduleLikeDTO>();
		for (ScheduleLike like : iList) {
			ScheduleLikeDTO dto = new ScheduleLikeDTO();
			dto.setLike(like);
			dtoList.add(dto);
		}
		return dtoList;
	}
}
