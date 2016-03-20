/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mobile.facade.MobileCouponGiftFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.facade.vo.MobileCouponVO;
import com.xyl.mmall.mobile.facade.vo.MobileGiftMoneyDetailVO;
import com.xyl.mmall.mobile.facade.vo.MobileGiftMoneyVO;
import com.xyl.mmall.order.service.OrderUnreadService;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionCartDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RedPacketOrderDTO;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobileCouponGiftFacadeImpl implements MobileCouponGiftFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private UserCouponService userCouponService;

	@Resource
	private UserRedPacketService userRedPacketService;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private CalCenterFacade calCenterFacade;
	@Resource
	private OrderUnreadService orderUnreadService;
	/**
	 * 状态码转换
	 * 
	 * @param type
	 * @return
	 */
	private static int coverStatus(int type) {
		// 0：可用 1：未启用2，过期3：用完 4失效，5.异常（不显示）
		if (type == ActivationConstants.STATE_CAN_USE)
			return 0;
		if (type == ActivationConstants.STATE_NOT_TAKE_EFFECT)
			return 1;
		if (type == ActivationConstants.STATE_LEFT_ZERO_COUNT || type == ActivationConstants.STATE_HAS_BEAN_USED)
			return 3;
		if (type == ActivationConstants.STATE_EXPIRED)
			return 2;
		if (type == ActivationConstants.STATE_NOT_MATCH || type == ActivationConstants.STATE_NOT_EXISTS)
			return 5;
		return 4;
	}

	@Override
	public BaseJsonVO getUserCouponList(long userId, MobilePageCommonAO ao,Integer status) {
		try {
			if(status == null)
				status = -1;
			//orderUnreadService.updateReadTime(userId, 10);
			List<CouponDTO> userCoupons = userCouponService.getUserCouponList(userId, ao.getTimestamp(),
					ao.getPageSize());
			if (userCoupons == null) {
				return Converter.listBaseJsonVO(null, false);
			}
			boolean hasNext = MobileChecker.checkHasNext(userCoupons, ao.getPageSize());

			List<MobileCouponVO> mobileCouponList = new ArrayList<MobileCouponVO>();

			for (CouponDTO userCoupon : userCoupons) {
				MobileCouponVO vo = genCouponVo(userCoupon);
				if(status ==-1 || status == vo.getStatus())
					mobileCouponList.add(vo);
			}
			if (ao.isMaxSize()) {
				Collections.sort(mobileCouponList);
			}
			return Converter.listBaseJsonVO(mobileCouponList, hasNext);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	public static MobileCouponVO genCouponVo(CouponDTO userCoupon) {
		MobileCouponVO vo = new MobileCouponVO();
		vo.setCouponCode(userCoupon.getCouponCode());
		//vo.setCouponId(userCoupon.getId());
		vo.setUserCouponId(userCoupon.getUserCouponId());
		vo.setEndTime(userCoupon.getEndTime());
		vo.setStartTime(userCoupon.getStartTime());
		vo.setName(userCoupon.getName());
		vo.setProvider(userCoupon.getDescription());
		//vo.setRequire(userCoupon.getReason());
		int type = coverStatus(userCoupon.getCouponState());
		vo.setStatus(type);
		return vo;
	}

	@Override
	public BaseJsonVO getUserGiftList(long userId, MobilePageCommonAO ao,Integer status,long appversion) {
		try {
			if(status == null)
				status = -1;
			
			List<MobileGiftMoneyVO> giftMoneyList = new ArrayList<MobileGiftMoneyVO>();
			if(appversion < Converter.protocolVersion("1.2.0"))
				return Converter.listBaseJsonVO(giftMoneyList, false);
			//orderUnreadService.updateReadTime(userId, 20);
			List<UserRedPacketDTO> dtos = userRedPacketService.getUserRedPacketList(userId, ao.getTimestamp(),
					ao.getPageSize());
			
			boolean hasNext = MobileChecker.checkHasNext(dtos, ao.getPageSize());
			
			if(dtos == null)
				return  Converter.listBaseJsonVO(giftMoneyList, false);
			for (UserRedPacketDTO dto : dtos) {
				MobileGiftMoneyVO vo = new MobileGiftMoneyVO();
				vo.setEndTime(dto.getValidEndTime());
				vo.setGifMoneyRemain(Converter.doubleFormat(dto.getRemainCash()));
				vo.setGiftMoney(Converter.doubleFormat(dto.getCash()));
				vo.setGiftMoneyId(dto.getId());
				// 无 vo.setName(name);
				vo.setStartTime(dto.getValidStartTime());
				int type = coverStatus(dto.getState());
				vo.setStatus(type);

				if (dto.getDtos() != null && dto.getDtos().size() > 0) {
					List<MobileGiftMoneyDetailVO> detail = new ArrayList<MobileGiftMoneyDetailVO>();
					for (RedPacketOrderDTO order : dto.getDtos()) {
						MobileGiftMoneyDetailVO vo_detail = new MobileGiftMoneyDetailVO();
						vo_detail.setUsedTime(order.getUsedTime());
						if((order.getRedPacketOrderType() == RedPacketOrderType.USE_RED_PACKET || order.getRedPacketOrderType() == RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS) 
								&& order.getRedPacketHandlerType() == ActivationHandlerType.DEFAULT)
							vo_detail.setUsedMoney(-Converter.doubleFormat(order.getCash()));
						else if((order.getRedPacketOrderType() == RedPacketOrderType.RETURN_RED_PACKET || order.getRedPacketOrderType() == RedPacketOrderType.RETURN_RED_PACKET_COMPOSE )
								&& order.getRedPacketHandlerType()  == ActivationHandlerType.GRANT )
							vo_detail.setUsedMoney(Converter.doubleFormat(order.getCash()));
						detail.add(vo_detail);
					}
					vo.setGiftMoneyDetail(detail);
				}
				if(status ==-1 || status == vo.getStatus())
					giftMoneyList.add(vo);
			}

			if (ao.isMaxSize()) {
				Collections.sort(giftMoneyList);
			}

			return Converter.listBaseJsonVO(giftMoneyList, hasNext);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

		// redPacketService.getRedPacketList(arg0, arg1, arg2)
	}

	@Override
	public BaseJsonVO bindCoupon(long userId, int areaId, String couponCode) {

		logger.info("addInDetailPage -> userId:<" + userId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkNull("COUPON CODE", couponCode);
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
		try {
			RetArg retArg = new RetArg();
			cartFacade.getCart(userId, areaId, null, retArg,PlatformType.MOBILE);
			FavorCaculateResultDTO resultDTO = RetArgUtil.get(retArg, FavorCaculateResultDTO.class);
			Map<Long, List<PromotionSkuItemDTO>> poSkuMap = new HashMap<>();

			if (resultDTO == null) {
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
			}

			poSkuMap.putAll(resultDTO.getNotSatisfySkuList());
			List<PromotionCartDTO> cartDTOs = resultDTO.getActivations();
			if (!CollectionUtils.isEmpty(cartDTOs)) {
				for (PromotionCartDTO dto : cartDTOs) {
					poSkuMap.putAll(dto.getPoSkuMap());
				}
			}
			FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
			paramDTO.setCouponCode(couponCode);
			paramDTO.setUserId(userId);

			FavorCaculateResultDTO favorCaculateResultDTO = calCenterFacade.bindCoupon(poSkuMap, paramDTO);
			MobileErrorCode code = coupStatus(favorCaculateResultDTO);
			if (code != MobileErrorCode.NULL)
				return Converter.errorBaseJsonVO(code);

			MobileCouponVO coupon = MobileCouponGiftFacadeImpl.genCouponVo(favorCaculateResultDTO.getCouponDTO());
			return Converter.genrBaseJsonVO("coupon", coupon);

		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	public static MobileErrorCode coupStatus(FavorCaculateResultDTO favorCaculateResultDTO) {
		if (favorCaculateResultDTO == null) {
			return MobileErrorCode.COUPON_NOT_EXETIS;
		}
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_NOT_EXISTS)
			return MobileErrorCode.COUPON_NOT_EXETIS;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_EXPIRED)
			return MobileErrorCode.COUPON_TIMEOUT;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_HAS_BEAN_USED)
			return MobileErrorCode.COUPON_USED_AGAIN;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_NOT_MATCH)
			return MobileErrorCode.COUPON_NOT_MATCH;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_INACTIVE)
			return MobileErrorCode.COUPON_DISABLE;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_LEFT_ZERO_COUNT)
			return MobileErrorCode.COUPON_USED;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_HAS_BIND_OTHERS)
			return MobileErrorCode.COUPON_USED;
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_NOT_TAKE_EFFECT)
			return MobileErrorCode.COUPON_NOT_OPEN;
		return MobileErrorCode.NULL;
	}
}
