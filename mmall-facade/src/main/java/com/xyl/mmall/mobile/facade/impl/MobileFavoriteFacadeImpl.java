/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.ActiveTellFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mobile.facade.MobileFavoriteFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.facade.vo.MobileBrandVO;
import com.xyl.mmall.mobile.facade.vo.MobileFavoriteListVO;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ActiveTellDTO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleLikeState;
import com.xyl.mmall.saleschedule.meta.ActiveTell;
import com.xyl.mmall.saleschedule.meta.ScheduleLike;
import com.xyl.mmall.saleschedule.service.ScheduleLikeService;

/**
 * @author hzjiangww 未完成
 */
@Facade
public class MobileFavoriteFacadeImpl implements MobileFavoriteFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ScheduleLikeService scheduleLikeService;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Autowired
	private ActiveTellFacade activeTellFacade;
	
	@Override
	public BaseJsonVO getFavoriteList(Long userId, int areaId, Integer type, MobilePageCommonAO pager) {
		logger.info("addFavorite -> userId:<" + userId + ">,type:<" + type + ">,id:<" + areaId + ">");

		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkNull("TYPE", type);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}

		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}

		MobileFavoriteListVO vo = new MobileFavoriteListVO();
		switch (type.intValue()) {
		case 1:
			try {
				DDBParam param = DDBParam.genParamX(pager.getPageSize());
				param.setOrderColumn("createDate");
				RetArg retArg = mainBrandFacade.getFavbrandListApp(param, pager.getTimestamp(), userId, areaId);
				@SuppressWarnings("unchecked")
				List<BrandItemDTO> brandItemDTO = RetArgUtil.get(retArg, ArrayList.class);
				if (brandItemDTO == null) {
					return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "no favorite found");
				}
				List<MobileBrandVO> list = new ArrayList<MobileBrandVO>();
				for(BrandItemDTO dto: brandItemDTO){
					list.add(MobileBrandFacadeImpl.coverBrandVO(dto));
				}
				param = RetArgUtil.get(retArg, DDBParam.class);
				vo.setHasNext(param.isHasNext());
				vo.setList(list);
				vo.setType(1);
				return Converter.converterBaseJsonVO(vo);
			} catch (Exception e) {
				logger.error(e.toString());
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
			}
		case 2:
		/*	活动关注功能取消了
		 * try {
				vo.setType(2);
				POListDTO dtos = scheduleLikeService.getLikedPOListByUserIdAndSiteId(userId, areaId,
						pager.getTimestamp(), pager.getPageSize());

				if (dtos == null || dtos.getPoList() == null) {
					return Converter.errorBaseJsonVO(ErrorCode.CAN_NOT_FIND_OBJECT, "no favorite found");
				}
				vo.setHasNext(dtos.isHasNext());

				List<MobilePOGroupVO> list = MobilePoFacadeImpl.fullInPoGroup(dtos.getPoList());
				vo.setList(list);
				return Converter.converterBaseJsonVO(vo);
			} catch (Exception e) {
				logger.error(e.toString());
				return Converter.errorBaseJsonVO(ErrorCode.SERVICE_ERROR);
			}*/
		}

		return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "type mast in 1:brand");
	}

	@Override
	public BaseJsonVO addFavorite(Long userId, Integer type, Long id,long areaId) {
		logger.info("addFavorite -> userId:<" + userId + ">,type:<" + type + ">,id:<" + id + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkNull("TYPE", type);
			MobileChecker.checkZero("ID", id);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}

		switch (type.intValue()) {
		case 1:
			try {
				boolean success = mainBrandFacade.addBrandCollection(userId, id);
				if (!success)
					return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
				BrandItemDTO dto =  mainBrandFacade.getBrandItemDTO(id, userId);
				String title= dto.getBrandNameZh();
				if(StringUtils.isBlank(title))
					title = dto.getBrandNameEn();
				hasRecord(id, String.valueOf(userId), areaId,title);
				
				return Converter.genrBaseJsonVO("favoriteNum", dto.getFavCount());
			} catch (Exception e) {
				logger.error(e.toString());
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
			}
		case 2:
			try {
				ScheduleLikeDTO dto = genScheduleLikeDTO(userId, id);
				scheduleLikeService.like(dto);
				Long likerNum = scheduleLikeService.getLikeCntByPOId(id);
				return Converter.genrBaseJsonVO("favoriteNum", likerNum);
			} catch (Exception e) {
				logger.error(e.toString());
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
			}
		}

		return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "type mast in po or brand");
	}

	@Override
	public BaseJsonVO cancelFavorite(Long userId, Integer type, Long id) {
		logger.info("addFavorite -> userId:<" + userId + ">,type:<" + type + ">,id:<" + id + ">");

		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkNull("TYPE", type);
			MobileChecker.checkZero("ID", id);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}

		switch (type.intValue()) {
		case 1:
			try {
				if (mainBrandFacade.removeBrandCollection(userId, id))
					return new BaseJsonVO();
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
			} catch (Exception e) {
				logger.error(e.toString());
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
			}

		case 2:
			try {
				ScheduleLikeDTO dto = genScheduleLikeDTO(userId, id);
				scheduleLikeService.unLike(dto);
				return new BaseJsonVO();
			} catch (Exception e) {
				logger.error(e.toString());
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
			}
		}

		return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "type mast in po or brand");
	}

	private ScheduleLikeDTO genScheduleLikeDTO(long userId, long poId) {
		ScheduleLike like = new ScheduleLike();
		like.setUserId(userId);
		like.setCreateDate(Converter.getTime());
		like.setStatusUpdateDate(like.getCreateDate());
		like.setStatus(ScheduleLikeState.VALID);
		like.setScheduleId(poId);
		ScheduleLikeDTO like2 = new ScheduleLikeDTO();
		like2.setLike(like);
		return like2;
	}
	
	
	private boolean hasRecord( long activeId, String value, long areaId,String title) {
		int activeType = 0;
		 int type = 3;
		try{
			ActiveTellCommonParamDTO param = new ActiveTellCommonParamDTO();
			param.setAreaId(areaId);
			param.setTellActiveType(activeType);
			param.setTellTargetType(type);
			param.setTellTargetValue(value);
			param.setTellActiveId(activeId);
			List<ActiveTell> activeTellList = activeTellFacade.getActiveTellByParam(param);
			if( activeTellList != null && activeTellList.size() > 0)
				return true;
			ActiveTell activeTell = new ActiveTell();
			activeTell.setAreaId(areaId);
			activeTell.setCreateTime(Converter.getTime());
			activeTell.setTellActiveType(activeType);
			activeTell.setTellTargetType(type);
			activeTell.setTellTargetValue(value);
			activeTell.setTellActiveId(activeId);
			activeTell.setTellActiveTitle(title);
			ActiveTellDTO activeTellDTO = new ActiveTellDTO();
			activeTellFacade.saveActiveTell(activeTellDTO);
			return true;
		}catch(Exception e){
			logger.error(e.toString());
		}
		return false;
	}
}
