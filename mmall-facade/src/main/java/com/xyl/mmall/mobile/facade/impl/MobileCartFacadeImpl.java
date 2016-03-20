/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.vo.CartActivationVO;
import com.xyl.mmall.mainsite.vo.CartPOVO;
import com.xyl.mmall.mainsite.vo.CartSkuItemVO;
import com.xyl.mmall.mainsite.vo.CartVO;
import com.xyl.mmall.mainsite.vo.order.ActionTagVO;
import com.xyl.mmall.mobile.facade.MobileCartFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileCartSkuList;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.converter.MobileConfig;
import com.xyl.mmall.mobile.facade.vo.MobileCartDetailVO;
import com.xyl.mmall.mobile.facade.vo.MobileCartInfoVO;
import com.xyl.mmall.mobile.facade.vo.MobileCartPoVO;
import com.xyl.mmall.mobile.facade.vo.MobileSkuVO;
import com.xyl.mmall.promotion.enums.PlatformType;

/**
 * @author hzjiangww
 *
 */
@Facade("mCartFacade")
public class MobileCartFacadeImpl implements MobileCartFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Autowired
	private CalCenterFacade calCenterFacade;

	public static MobileCartSkuList getSkuIdList(CartVO vo) {
		List<Long> skus = new ArrayList<Long>();
		int count = 0;
		int timeout = 0;
		BigDecimal totalprice = BigDecimal.ZERO;
		for (CartActivationVO cavo : vo.getActivations()) {
			if (cavo != null && cavo.getPoList() != null) {
				for (CartPOVO cpvo : cavo.getPoList()) {
					if (cpvo != null && cpvo.getSkulist() != null) {
						for (CartSkuItemVO cartSku : cpvo.getSkulist()) {
							if (CartSkuItemVO.STATE_OVERTIME.equals(cartSku.getStatus())) {
								timeout = timeout + cartSku.getCount();
							}
							if (CartSkuItemVO.STATE_NORMAL.equals(cartSku.getStatus())) {
								skus.add(Long.valueOf(cartSku.getId()));
								count = count + cartSku.getCount();
								if (cartSku.getOriginalPrice() != null)
									totalprice = totalprice.add(cartSku.getOriginalPrice().multiply(
											new BigDecimal(cartSku.getCount())));
							}
						}
					}
				}
			}

		}
		for (CartPOVO cpvo : vo.getPoList()) {
			if (cpvo != null && cpvo.getSkulist() != null) {
				for (CartSkuItemVO cartSku : cpvo.getSkulist()) {
					if (CartSkuItemVO.STATE_OVERTIME.equals(cartSku.getStatus())) {
						timeout = timeout + cartSku.getCount();
					}
					
					if (CartSkuItemVO.STATE_NORMAL.equals(cartSku.getStatus())) {
						skus.add(Long.valueOf(cartSku.getId()));
						count = count + cartSku.getCount();
						if (cartSku.getOriginalPrice() != null)
							totalprice = totalprice.add(cartSku.getOriginalPrice().multiply(
									new BigDecimal(cartSku.getCount())));
					}
				}
			}
		}

		MobileCartSkuList mcsl = new MobileCartSkuList();
		mcsl.skulist = skus;
		mcsl.size = count;
		mcsl.timeout = timeout;
		mcsl.price = Converter.doubleFormat(totalprice);
		return mcsl;
	}

	/**
	 * cartVo 转成 mobileCartInfo
	 * 
	 * @param vo
	 * @return
	 */
	private static MobileCartInfoVO coverToMobileCartInfoVO(CartVO vo, MobileCartSkuList mc) {
		MobileCartInfoVO m_vo = new MobileCartInfoVO();
		if (mc.size == 0) {
			m_vo.setCountdownTime(0);
			m_vo.setEndTime(Converter.getTime());
		} else {
			m_vo.setCountdownTime(vo.getCartInfoVO().getLeftTime());
			m_vo.setEndTime(vo.getCartInfoVO().getLeftTime() + Converter.getTime());
		}
		m_vo.setTimeOutCount(mc.timeout);
		m_vo.setCartHash(Converter.genCartHash(vo.getCartInfoVO().getUpdateTime(), getSkuIds(mc)));
		m_vo.setPrdtCount(mc.size);
		m_vo.setTotalPrice(Converter.doubleFormat(vo.getCartInfoVO().getTotalPrice()));
		return m_vo;

	}
	
	public static String getSkuIds(MobileCartSkuList mc) {
		StringBuffer args = new StringBuffer();
		List<Long> ids = mc.skulist;
		for (int i = 0; i < ids.size(); i++) {
			args.append(ids.get(i));
			if (i != ids.size() - 1)
				args.append(",");
		}
		return args.toString();
	}

	private static MobileSkuVO coverCartSku(CartSkuItemVO vo) {
		MobileSkuVO skuvo = new MobileSkuVO();
		skuvo.setBuyCount(vo.getCount());
		// skuvo.setCount(count);
		skuvo.setInvalidDesc(Converter.coverCartItemStatus(vo.getStatus(), vo.getInventroyCount()));
		skuvo.setOriginPrice(Converter.doubleFormat(vo.getOriginalPrice()));
		skuvo.setPoPrice(Converter.doubleFormat(vo.getCartPrice()));
		skuvo.setPrdtId(vo.getProductId());
		skuvo.setPrdtName(vo.getName());
		skuvo.setSkuId(Long.valueOf(vo.getId()));
		skuvo.setSkuImageUrl(vo.getThumb());
		skuvo.setSkuSizeDesc(vo.getSize());
		skuvo.setValidStatus(Converter.coverCartItemStatusCode(vo.getStatus(), vo.getInventroyCount()));
		return skuvo;
	}

	//
	//
	//
	private static MobileCartPoVO coverCartPo(CartPOVO povo, List<String> cartInfo, boolean filter) {
		MobileCartPoVO vo = new MobileCartPoVO();
		if (povo == null)
			return vo;
		vo.setCartPOId(povo.getPoId());
		vo.setCartPOInfo(cartInfo);
		vo.setCartPOName(povo.getName());
		vo.setIsValid(povo.isDeleted() ? 1 : 0);
		List<MobileSkuVO> skulist = new ArrayList<MobileSkuVO>();
		for (CartSkuItemVO sku_vo : povo.getSkulist()) {
			if (filter && !CartSkuItemVO.STATE_NORMAL.equals(sku_vo.getStatus()))
				continue;
			// 过滤删除的数据
			if (CartSkuItemVO.STATE_DELETED.equals(sku_vo.getStatus()))
				continue;
			skulist.add(coverCartSku(sku_vo));
		}
		vo.setSkulist(skulist);
		if (skulist.size() == 0)
			return null;
		return vo;
	}

	/**
	 * cartVo 转成 MobileCartDetail
	 * 
	 * @param vo
	 * @return
	 */
	public static MobileCartDetailVO coverToMobileCartDetailVO(CartVO vo, boolean filter) {
		if ((vo.getActivations() == null || vo.getActivations().size() == 0)
				&& (vo.getPoList() == null || vo.getPoList().size() == 0)
				&& (vo.getInvalidCartItemList() == null || vo.getInvalidCartItemList().size() == 0))
			return null;
		MobileCartDetailVO m_vo_detail = new MobileCartDetailVO();
		MobileCartSkuList mcl = getSkuIdList(vo);

		// 这边会提供替代方法

		m_vo_detail.setCartInfo(coverToMobileCartInfoVO(vo, mcl));
		// 这边没有原价
		m_vo_detail.setOriginTotalPrice(mcl.price);
		m_vo_detail.setPoTotalPrice(Converter.doubleFormat(vo.getCartInfoVO().getTotalPrice()));
		List<MobileCartPoVO> cartPOList = new ArrayList<MobileCartPoVO>();
		if (vo.getActivations() != null && vo.getActivations().size() > 0) {
			for (CartActivationVO c_vo : vo.getActivations()) {
				List<String> activeinfo =  new ArrayList<String>();
				if (c_vo.getTagList() != null) {
					for (ActionTagVO a : c_vo.getTagList()) {
						activeinfo.add(a.getDesc());
					}
				}
				for (CartPOVO po : c_vo.getPoList()) {
					MobileCartPoVO cpovo = coverCartPo(po, activeinfo, filter);
					if (cpovo != null)
						cartPOList.add(cpovo);
				}

			}
		}
		if (vo.getPoList() != null && vo.getPoList().size() > 0) {
			for (CartPOVO po : vo.getPoList()) {
				MobileCartPoVO cpovo = coverCartPo(po, null, filter);
				if (cpovo != null)
					cartPOList.add(cpovo);
			}
		}

		m_vo_detail.setCartPOList(cartPOList);
		//不显示平台优惠
		//m_vo_detail.setPlatformInfo(MobileConfig.active_platform_info);
		if (vo.getInvalidCartItemList() != null) {
			List<MobileSkuVO> invalidList = new ArrayList<MobileSkuVO>();
			for (CartSkuItemVO skuItem : vo.getInvalidCartItemList()) {
				invalidList.add(coverCartSku(skuItem));
			}
			m_vo_detail.setInvalidList(invalidList);
		}

		return m_vo_detail;
	}

	@Override
	public BaseJsonVO addToCart(long userId, Long skuId, int areaId, Integer count, int type) {

		logger.info("addInDetailPage -> userId:<" + userId + ">,skuId:<" + skuId + ">,areaCode:<" + areaId
				+ ">,count:<" + count + ">");
		if (count == null)
			count = 1;
		try {
			MobileChecker.checkZero("SKU", skuId);
			MobileChecker.checkZero("ADD CART COUNT", count);
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

		if (count < 0)
			return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "add count low than zero");

		try {

			int code = 0;
			if (type == 1) {
				code = cartFacade.rebuyAndReturnCode(userId, areaId, skuId, count);
			} else {
				code = cartFacade.addItemToCartAndUpdateTime(userId, areaId, skuId, count);
			}
			if (code < 0) {
				logger.info("addInDetailPage -> fail code:<" + code + ">");
				return returnErrorCode(code);
			}

			CartVO vo = cartFacade.getCart(userId, areaId, null,PlatformType.MOBILE);
			if (vo == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "empty");

			if (vo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
				code = CartService.CART_PO_INVALID;

			if (type == 1) {
				MobileCartDetailVO mobileVo = coverToMobileCartDetailVO(vo, false);
				return Converter.converterBaseJsonVO(mobileVo);
			}

			MobileCartSkuList mcl = getSkuIdList(vo);
			MobileCartInfoVO mobileVo = coverToMobileCartInfoVO(vo, mcl);
			// TODO
			return Converter.genrBaseJsonVO("cartInfo", mobileVo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	private BaseJsonVO returnErrorCode(int code) {
		if (code == CartService.CART_OVER_SINGLE_COUNT)
			return Converter.errorBaseJsonVO(MobileErrorCode.ADD_CART_FAIL_MAXCOUNT);
		if (code == CartService.CART_OVER_ITMESCOUNT)
			return Converter.errorBaseJsonVO(MobileErrorCode.ADD_CART_FAIL_CARTFULL);
		if (code == CartService.CART_PO_INVALID)
			return Converter.errorBaseJsonVO(MobileErrorCode.ADD_CART_FAIL_POOUT);
		if (code == CartService.CART_NO_INVENTORY)
			return Converter.errorBaseJsonVO(MobileErrorCode.ADD_CART_FAIL_EMPTY);
		return Converter.errorBaseJsonVO(MobileErrorCode.ADD_CART_FAIL_ERROR);
	}

	@Override
	public BaseJsonVO updateInCartPage(long userId, int areaId, Long skuId, Integer add, Integer del) {

		logger.info("addInDetailPage -> userId:<" + userId + ">,skuId:<" + skuId + ">,areaCode:<" + areaId + ">,add:<"
				+ add + ">,del:<" + del + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("SKU", skuId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		if (add == null)
			add = 0;
		if (del == null)
			del = 0;
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}

		try {
			if (add != 0 && del != 0) {
				return Converter.errorBaseJsonVO(MobileErrorCode.DATA_NOT_MATCH, "add or del must one zero");
			}

			CartVO vo_check = cartFacade.getCart(userId, areaId, null,PlatformType.MOBILE);
			MobileCartSkuList list = getSkuIdList(vo_check);
			if (list.skulist != null && !list.skulist.contains(skuId)) {
				return Converter.errorBaseJsonVO(MobileErrorCode.CART_OP_ERROR);
			}
			int count = 0;
			if (add > 0) {
				count = add;
			}
			if (del == -1) {
				List<Long> skus = new ArrayList<Long>();
				skus.add(skuId);
				boolean success = cartFacade.deleteCartItem(userId, areaId, skus);
				if (success)
					return getCartDetail(userId, areaId);
				else
					return Converter.errorBaseJsonVO(MobileErrorCode.DELETE_CART_FAIL, "delete fail");
			}
			if (del > 0) {
				count = -del;
			}
			int code = cartFacade.addItemToCart(userId, areaId, skuId, count, null);
			CartVO vo = cartFacade.getCart(userId, areaId, null,PlatformType.MOBILE);
			if (vo == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "empty");

			if (vo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
				code = CartService.CART_PO_INVALID;

			if (code < 0) {
				if (count < 0)
					return Converter.errorBaseJsonVO(MobileErrorCode.DELETE_CART_FAIL);
				return returnErrorCode(code);
			}

			MobileCartDetailVO mobileVo = coverToMobileCartDetailVO(vo, false);
			return Converter.converterBaseJsonVO(mobileVo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	@Override
	public BaseJsonVO deleteInCartPage(long userId, int areaId, List<Long> skuId) {

		logger.info("deleteInCartPage -> userId:<" + userId + ">,skuId:<" + skuId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			if (skuId == null || skuId.size() == 0)
				throw new ParamNullException("sku list is null");
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

			boolean success = true;
			// 这边等他后续返回
			success = cartFacade.deleteCartItem(userId, areaId, skuId);
			if (success)
				return getCartDetail(userId, areaId);
			else
				return Converter.errorBaseJsonVO(MobileErrorCode.DELETE_CART_FAIL);

		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	@Override
	public BaseJsonVO getCartInfo(long userId, int areaId) {

		logger.info("addInDetailPage -> userId:<" + userId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkNull("AREA CODE", areaId);
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
			CartVO vo = cartFacade.getCart(userId, areaId, null,PlatformType.MOBILE);
			if (vo == null)
				return Converter.genrBaseJsonVO("cartInfo", null);
			MobileCartSkuList mcl = getSkuIdList(vo);
			MobileCartInfoVO mobileVo = coverToMobileCartInfoVO(vo, mcl);
			return Converter.genrBaseJsonVO("cartInfo", mobileVo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	@Override
	public BaseJsonVO getCartDetail(long userId, int areaId) {

		logger.info("getCartDetail -> userId:<" + userId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
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
			CartVO vo = cartFacade.getCart(userId, areaId, null,PlatformType.MOBILE);
			if (vo == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "empty");

			MobileCartDetailVO mobileVo = coverToMobileCartDetailVO(vo, false);
			return Converter.converterBaseJsonVO(mobileVo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

}
