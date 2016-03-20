package com.xyl.mmall.mobile.web.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.enums.CartItemType;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.common.OrderCartItemVO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.OrderSkuVO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.SkuSPVO;
import com.xyl.mmall.mobile.web.vo.OrderTraceVO;
import com.xyl.mmall.mobile.web.vo.order.ActionTagVO;
import com.xyl.mmall.mobile.web.vo.order.ExpTrackLogVO;
import com.xyl.mmall.mobile.web.vo.order.InvoiceInOrdVO;
import com.xyl.mmall.mobile.web.vo.order.OrderActionVO;
import com.xyl.mmall.mobile.web.vo.order.OrderCoupon1VO;
import com.xyl.mmall.mobile.web.vo.order.OrderExpInfoVO;
import com.xyl.mmall.mobile.web.vo.order.OrderForm1VO;
import com.xyl.mmall.mobile.web.vo.order.OrderForm2VO;
import com.xyl.mmall.mobile.web.vo.order.OrderFormBasicVO;
import com.xyl.mmall.mobile.web.vo.order.OrderFormPayMethod1VO;
import com.xyl.mmall.mobile.web.vo.order.OrderLogisticsVO;
import com.xyl.mmall.mobile.web.vo.order.OrderNoActionVO;
import com.xyl.mmall.mobile.web.vo.order.OrderYHTagVO;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderFormExtInfoDTO;
import com.xyl.mmall.order.dto.OrderFormPayMethodDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.promotion.activity.Label;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionCartDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.meta.Promotion;

/**
 * 主站VO对象的转换工具
 * 
 * @author dingmingliang
 * 
 */
public class MobileVOConvertUtil {

	/**
	 * 将List(OrderTraceVO)转换成List(ExpTrackLogVO)
	 * 
	 * @param orderTraceVOList
	 * @return
	 */
	public static List<ExpTrackLogVO> convertToExpTrackLogVOList(List<OrderTraceVO> orderTraceVOList) {
		List<ExpTrackLogVO> trackVOList = new ArrayList<>();
		if (CollectionUtil.isEmptyOfCollection(orderTraceVOList))
			return trackVOList;

		for (OrderTraceVO orderTraceVO : orderTraceVOList) {
			ExpTrackLogVO trackVO = new ExpTrackLogVO();
			trackVO.setAcceptAddress(orderTraceVO.getOperateOrg() + " " + orderTraceVO.getOperateDesc());
			trackVO.setAcceptDate(orderTraceVO.getTime());
			trackVO.setRemark(orderTraceVO.getNote());
			trackVOList.add(trackVO);
		}
		return trackVOList;
	}

	/**
	 * 将List(OrderFormDTO)转换成List(OrderForm2VO)<br>
	 * (设置canApplyReturn)
	 * 
	 * @param dtoList
	 * @param returnPackageFacade
	 * @return
	 */
	public static List<OrderForm2VO> convertToOrderForm2VOList(List<OrderFormDTO> dtoList,
			ReturnPackageFacade returnPackageFacade) {
		List<OrderForm2VO> voList = new ArrayList<>();
		if (CollectionUtil.isEmptyOfCollection(dtoList))
			return voList;

		for (OrderFormDTO dto : dtoList) {
			CollectionUtil.addOfListFilterNull(voList, convertToOrderForm2VO(dto, returnPackageFacade));
		}
		return voList;
	}

	/**
	 * 将OrderFormCalDTO转换成OrderForm1VO
	 * 
	 * @param orderCalDTO
	 * @param fcResultDTO
	 * @return
	 */
	public static OrderForm1VO convertToOrderForm1VO(OrderFormCalDTO orderCalDTO, FavorCaculateResultDTO fcResultDTO) {
		if (orderCalDTO == null || fcResultDTO == null)
			return null;

		// 0.参数准备
		// Key: skuId, Value: OrderCartItemDTO
		Map<Long, OrderCartItemDTO> skuIdAndCartDTOMap = new TreeMap<Long, OrderCartItemDTO>();
		for (OrderCartItemDTO cartDTO : orderCalDTO.getOrderCartItemDTOList()) {
			if (cartDTO.getCartType() == CartItemType.CART_SKU) {
				long skuId = cartDTO.getOrderSkuCartItemDTO().getSkuId();
				skuIdAndCartDTOMap.put(skuId, cartDTO);
			}
		}

		OrderForm1VO vo = new OrderForm1VO();
		// 1.设置可选的优惠券
		setCouponList(vo, fcResultDTO);

		// 2.设置ActionList
		List<OrderActionVO> actionList = convertToOrderActionVOList(skuIdAndCartDTOMap, fcResultDTO);
		vo.setActionList(actionList);
		// 3.设置NoActionList
		List<OrderNoActionVO> noActionList = convertToOrderNoActionVOList(skuIdAndCartDTOMap, fcResultDTO);
		vo.setNoActionList(noActionList);
		// 4.设置商品价格+邮费
		setPrice(vo, orderCalDTO);
		// 5.设置用户总共有效的红包总金额
		vo.setCanUseRedPackets(fcResultDTO.getCanUseRedPackets() != null ? fcResultDTO.getCanUseRedPackets()
				: BigDecimal.ZERO);
		if (vo.getCanUseRedPackets().compareTo(BigDecimal.ZERO) > 0) {
			vo.setCanOrderRedPackets(vo.getTotalCash().compareTo(vo.getCanUseRedPackets()) > 0 ? vo
					.getCanUseRedPackets() : vo.getTotalCash());
		}

		// 6.设置支付方式(选中的和可选的)
		setPayMethodArray(vo, orderCalDTO);

		// 7.设置订单优惠Tag
		List<OrderYHTagVO> yhTagList = new ArrayList<>();
		if (orderCalDTO.isFreeExp()) {
			OrderYHTagVO orderYHTagVO = new OrderYHTagVO();
			orderYHTagVO.setType(0);
			orderYHTagVO.setDesc("订单实付满" + ConstValueOfOrder.FREE_EXP_RPRICE + "元免运费");
			yhTagList.add(orderYHTagVO);
		}
		vo.setYhTagList(yhTagList);
		return vo;
	}

	/**
	 * 设置商品价格+邮费
	 * 
	 * @param vo
	 * @param orderDTO
	 */
	private static void setPrice(OrderFormBasicVO vo, OrderFormDTO orderDTO) {
		BigDecimal cartOriRPrice = orderDTO.getCartOriRPrice(), cartRPrice = orderDTO.getCartRPrice();
		BigDecimal hdSPrice = BigDecimal.ZERO, redPacketSPrice = orderDTO.getRedCash() != null ? orderDTO
				.getRedCash() : BigDecimal.ZERO;
//		for (OrderSkuDTO orderSkuDTO : orderDTO.getAllOrderSkuDTOList()) {
//			hdSPrice = hdSPrice.add(orderSkuDTO.getHdSPrice().multiply(new BigDecimal(orderSkuDTO.getTotalCount())));
//			couponSPrice = couponSPrice.add(orderSkuDTO.getCouponSPrice().multiply(
//					new BigDecimal(orderSkuDTO.getTotalCount())));
//		}
		BigDecimal totalCash = cartRPrice.add(orderDTO.getExpUserPrice()), realCash = totalCash
				.subtract(redPacketSPrice);
		vo.setCartOriRPrice(cartOriRPrice);
		vo.setCartRPrice(cartRPrice);
		vo.setHdSPrice(hdSPrice);
		vo.setCouponSPrice(orderDTO.getCouponDiscount());
		vo.setRedPacketSPrice(redPacketSPrice);
		vo.setExpOriPrice(orderDTO.getExpOriPrice());
		vo.setExpSysPayPrice(orderDTO.getExpSysPayPrice());
		vo.setExpUserPrice(orderDTO.getExpUserPrice());
		vo.setTotalCash(totalCash);
		vo.setRealCash(realCash);
	}

	/**
	 * 设置可选的优惠券
	 * 
	 * @param vo
	 * @param fcResultDTO
	 */
	private static void setCouponList(OrderForm1VO vo, FavorCaculateResultDTO fcResultDTO) {
		List<CouponDTO> couponList = fcResultDTO.getUserCoupons();
		if (CollectionUtil.isNotEmptyOfCollection(couponList)) {
			List<OrderCoupon1VO> couponVOList = new ArrayList<>(couponList.size());
			for (CouponDTO coupon : couponList) {
				OrderCoupon1VO couponVO = new OrderCoupon1VO();
				couponVO.setUserCouponId(coupon.getUserCouponId());
				couponVO.setTitle(coupon.getName());
				boolean isSelected = fcResultDTO.getCouponDTO() != null ? fcResultDTO.getCouponDTO().getUserCouponId() == couponVO
						.getUserCouponId() : false;
				couponVO.setSelected(isSelected);
				couponVO.setStartDate(DateFormatEnum.TYPE1.getFormatDate(coupon.getStartTime()).replaceAll("-", "."));
				couponVO.setEndDate(DateFormatEnum.TYPE1.getFormatDate(coupon.getEndTime()).replaceAll("-", "."));
				couponVOList.add(couponVO);
			}
			vo.setCouponList(couponVOList);
		}
	}

	/**
	 * 设置支付方式(选中的和可选的)
	 * 
	 * @param vo
	 * @param orderCalDTO
	 */
	private static void setPayMethodArray(OrderForm1VO vo, OrderFormCalDTO orderCalDTO) {
		List<OrderFormPayMethod1VO> payMethodList = new ArrayList<>();
		for (OrderFormPayMethodDTO payMethodDTO : orderCalDTO.getPaymethodArray()) {
			OrderFormPayMethod1VO payMethodVO = new OrderFormPayMethod1VO();
			payMethodVO.setDesc(payMethodDTO.getPayMethod().getDesc());
			payMethodVO.setInvalidMess(payMethodDTO.getInvalidMess());
			payMethodVO.setSelected(orderCalDTO.getOrderFormPayMethod() == payMethodDTO.getPayMethod());
			payMethodVO.setValid(payMethodDTO.isValid());
			payMethodVO.setValue(payMethodDTO.getPayMethod().getIntValue());

			payMethodList.add(payMethodVO);
		}
		vo.setPayMethodArray(payMethodList.toArray(new OrderFormPayMethod1VO[payMethodList.size()]));
	}

	/**
	 * @param skuIdAndCartDTOMap
	 * @param fcResultDTO
	 * @return
	 */
	private static List<OrderActionVO> convertToOrderActionVOList(Map<Long, OrderCartItemDTO> skuIdAndCartDTOMap,
			FavorCaculateResultDTO fcResultDTO) {
		// 1.参数合法性判断
		List<PromotionCartDTO> pcartDTOList = fcResultDTO.getActivations();
		if (CollectionUtil.isEmptyOfCollection(pcartDTOList))
			return null;

		// 2.循环处理数据
		List<OrderActionVO> retVOList = new ArrayList<>();
		for (PromotionCartDTO pcartDTO : pcartDTOList) {
			// 2.1 生成cartVOList
			List<OrderCartItemVO> cartVOList = new ArrayList<>();
			Map<Long, List<PromotionSkuItemDTO>> psiDTOListMap = pcartDTO.getPoSkuMap();
			for (Entry<Long, List<PromotionSkuItemDTO>> entry1 : psiDTOListMap.entrySet()) {
				List<PromotionSkuItemDTO> psiDTOList = entry1.getValue();
				CollectionUtil.addAllOfList(cartVOList, convertToOrderCartItemVOList(skuIdAndCartDTOMap, psiDTOList));
			}
			// 2.2 生成ActionTagVO
			PromotionDTO promotionDTO = pcartDTO.getPromotionDTO();
			List<ActionTagVO> tagList = convertToActionTagVOList(promotionDTO);
			// 2.3 生成其他参数
			long actId = promotionDTO.getId();
			String effectDesc = promotionDTO.getDescription(), title = promotionDTO.getName();
			// 2.4 生成OrderActionVO
			OrderActionVO actionVO = new OrderActionVO();
			actionVO.setCartList(cartVOList);
			actionVO.setActId(actId);
			actionVO.setEffectDesc(effectDesc);
			actionVO.setTitle(title);
			actionVO.setTagList(tagList);

			retVOList.add(actionVO);
		}

		return retVOList;
	}

	/**
	 * 生成活动标签
	 * 
	 * @param promotion
	 * @return
	 */
	public static List<ActionTagVO> convertToActionTagVOList(Promotion promotion) {
		List<Label> labelList = JsonUtils.parseArray(promotion.getLabels(), Label.class);
		if (CollectionUtil.isEmptyOfCollection(labelList))
			return null;
		List<ActionTagVO> tagList = new ArrayList<>();
		for (Label label : labelList) {
			if (!label.isSelect())
				continue;
			ActionTagVO actionTagVO = new ActionTagVO();
			actionTagVO.setTitle(label.getName());
			actionTagVO.setDesc(label.getDesc());
			tagList.add(actionTagVO);
		}
		return tagList;
	}

	/**
	 * @param skuIdAndCartDTOMap
	 * @param fcResultDTO
	 * @return
	 */
	private static List<OrderNoActionVO> convertToOrderNoActionVOList(Map<Long, OrderCartItemDTO> skuIdAndCartDTOMap,
			FavorCaculateResultDTO fcResultDTO) {
		// 1.参数合法性判断
		Map<Long, List<PromotionSkuItemDTO>> psiDTOListMap = fcResultDTO.getNotSatisfySkuList();
		if (CollectionUtil.isEmptyOfMap(psiDTOListMap))
			return null;

		// 2.循环处理数据
		List<OrderNoActionVO> retVOList = new ArrayList<>();
		for (Entry<Long, List<PromotionSkuItemDTO>> entry1 : psiDTOListMap.entrySet()) {
			List<PromotionSkuItemDTO> psiDTOList = entry1.getValue();
			List<OrderCartItemVO> cartVOList = convertToOrderCartItemVOList(skuIdAndCartDTOMap, psiDTOList);

			OrderNoActionVO noActionVO = new OrderNoActionVO();
			noActionVO.setCartList(cartVOList);
			retVOList.add(noActionVO);
		}
		return retVOList;
	}

	/**
	 * @param skuIdAndCartDTOMap
	 * @param psiDTOList
	 * @return
	 */
	private static List<OrderCartItemVO> convertToOrderCartItemVOList(Map<Long, OrderCartItemDTO> skuIdAndCartDTOMap,
			List<PromotionSkuItemDTO> psiDTOList) {
		List<OrderCartItemDTO> cartDTOList = new ArrayList<>();
		for (PromotionSkuItemDTO psiDTO : psiDTOList) {
			long skuId = psiDTO.getSkuId();
			OrderCartItemDTO cartDTO = skuIdAndCartDTOMap.get(skuId);
			cartDTOList.add(cartDTO);
		}
		Boolean isSnapShot = null;
		List<OrderCartItemVO> cartVOList = convertToOrderCartItemVOList(cartDTOList, isSnapShot);
		return cartVOList;
	}

	/**
	 * 根据页面需要,重新设置订单状态
	 * 
	 * @param vo
	 */
	public static void resetOrderFormState(OrderForm2VO vo) {
		OrderFormExtInfoDTO extDTO = vo.getExtDTO();
		if (OrderFormState.isInRevertOmsCancelNewStateArray(vo.getOrderFormState()) && extDTO != null
				&& extDTO.isCancelFail()) {
			vo.setOrderFormState(OrderFormState.FAKE_JJCK);
		}
	}

	/**
	 * 将OrderFormDTO转换成OrderForm2VO<br>  
	 * (设置canApplyReturn)
	 * returnPackageFacade逻辑不用
	 * @param orderDTO
	 * @param returnPackageFacade
	 * @return
	 */
	public static OrderForm2VO convertToOrderForm2VO(OrderFormDTO orderDTO, ReturnPackageFacade returnPackageFacade) {
		if (orderDTO == null)
			return null;
		boolean isFilterEnum = false;
		// 1.转换OrderForm2VO的基本属性
		OrderForm2VO vo = ReflectUtil.convertObj(OrderForm2VO.class, orderDTO, isFilterEnum);
		//备注
		if(StringUtils.isNotEmpty(orderDTO.getComment())){
			vo.setComment(orderDTO.getComment());
		}
		
		orderDTO.getUserName();
		// 2.转换OrderExpInfoVO的基本属性
		vo.setExpInfo(ReflectUtil.convertObj(OrderExpInfoVO.class, orderDTO.getOrderExpInfoDTO(), isFilterEnum));
		// 3.转化购物车Vo
		vo.setCartList(convertToOrderCartItemVOList(orderDTO.getOrderCartItemDTOList(),true));
		// 3.转换OrderPackageVO
		//vo.setPackageList(convertToOrderPackageVOList(orderDTO.getOrderPackageDTOList(), returnPackageFacade));
		
		//4. 转化物流VO
		vo.setOrderLogisticsVOs(convertToOrderLogistics(orderDTO.getOrderLogisticsDTOs()));
		
		// 5.设置PayCloseCD
		long currTime = System.currentTimeMillis(), payCloseCD = 0;
		if (orderDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME > currTime
				&& orderDTO.getOrderFormState() == OrderFormState.WAITING_PAY) {
			payCloseCD = orderDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME - currTime;
			payCloseCD = payCloseCD <= 0 ? 0 : payCloseCD;
		}
		vo.setPayCloseCD(payCloseCD);
		// 6.设置订单状态
		if (payCloseCD <= 0 && orderDTO.getOrderFormState() == OrderFormState.WAITING_PAY) {
			vo.setOrderFormState(OrderFormState.CANCEL_ED);
			
//			if (vo.isCanCancel())
//				vo.setCanCancel(false);
		}
		vo.setCancelTimeStr(orderDTO.getCancelTime()> 0 ? DateFormatEnum.TYPE5.getFormatDate(orderDTO.getCancelTime()) : "");
		vo.setCancelReason(orderDTO.getCancelReason());
		// 6.设置其他时间
//		long expTime = -1L;
//		for (OrderPackageDTO opDTO : orderDTO.getOrderPackageDTOList()) {
//			expTime = expTime <= 0 || (opDTO.getExpSTime() > 0 && opDTO.getExpSTime() < expTime) ? opDTO.getExpSTime()
//					: expTime;
//		}
		vo.setExpDate(vo.getOmsTime() > 0 ? DateFormatEnum.TYPE5.getFormatDate(vo.getOmsTime()) : "");
		vo.setOrderDate(DateFormatEnum.TYPE5.getFormatDate(vo.getOrderTime()));
		vo.setConfirmDate(vo.getConfirmTime() > 0 ? DateFormatEnum.TYPE5.getFormatDate(vo.getConfirmTime()) : "");
		vo.setPayDate(vo.getPayTime() > 0 ? DateFormatEnum.TYPE5.getFormatDate(vo.getPayTime()) : "");
		vo.setUpdateDate(orderDTO.getUpdateTime()!=null?DateUtil.dateToString(orderDTO.getUpdateTime(), DateUtil.LONG_PATTERN):"");
		// 7.设置其他属性
		// 7.1 设置发票
		vo.setInvoiceTitle(orderDTO.getInvoiceInOrdDTO()!=null?orderDTO.getInvoiceInOrdDTO().getTitle():null);
		boolean hasInvoice = orderDTO.getInvoiceDTOs() != null;
		if (hasInvoice){
			vo.setInvoiceInOrdVOs(convertToInvoiceInOrdVOs(orderDTO.getInvoiceDTOs()));
		}
		// 7.2 设置商品金额+邮费
		setPrice(vo, orderDTO);
		return vo;
	}

//	/**
//	 * 将List(OrderPackageDTO)转换成List(OrderPackageVO)
//	 * 
//	 * @param dtoList
//	 * @param returnPackageFacade
//	 * @return
//	 */
//	private static List<OrderPackageVO> convertToOrderPackageVOList(List<OrderPackageDTO> dtoList,
//			ReturnPackageFacade returnPackageFacade) {
//		List<OrderPackageVO> voList = new ArrayList<>();
//		if (CollectionUtil.isEmptyOfCollection(dtoList))
//			return voList;
//
//		int packageIndex = 0;
//		for (OrderPackageDTO dto : dtoList) {
//			OrderPackageVO vo = convertToOrderPackageVO(dto, returnPackageFacade);
//			vo.setPackageIndex(packageIndex++);
//			voList.add(vo);
//		}
//		return voList;
//
//	}
	
	public static List<InvoiceInOrdVO> convertToInvoiceInOrdVOs(List<InvoiceDTO>invoiceDTOs){
		if(CollectionUtil.isEmptyOfList(invoiceDTOs)){
			return null;
		}
		  List<InvoiceInOrdVO>invoiceInOrdVOs = new ArrayList<InvoiceInOrdVO>();
			for(InvoiceDTO invoice:invoiceDTOs){
				InvoiceInOrdVO invoiceVO = new InvoiceInOrdVO(invoice);
				invoiceInOrdVOs.add(invoiceVO);
			}
		  return invoiceInOrdVOs;
	}
	
	public static List<OrderLogisticsVO> convertToOrderLogistics(List<OrderLogisticsDTO> orderLogisticsDTOs){
		if(CollectionUtil.isEmptyOfList(orderLogisticsDTOs)){
			return null;
		}
		  List<OrderLogisticsVO> orderLogisticsVOs = new ArrayList<OrderLogisticsVO>();
		  for(OrderLogisticsDTO orderLogisticsDTO:orderLogisticsDTOs ){
			  OrderLogisticsVO orderLogisticsVO = new OrderLogisticsVO();
			  orderLogisticsVO.setId(orderLogisticsDTO.getId());
			  orderLogisticsVO.setMailNO(orderLogisticsDTO.getMailNO());
			  orderLogisticsVO.setExpressCompany(orderLogisticsDTO.getExpressCompany());
			  orderLogisticsVO.setDeliverDate(DateUtil.dateToString(orderLogisticsDTO.getCreateTime(), DateUtil.LONG_PATTERN));
			  orderLogisticsVOs.add(orderLogisticsVO);
		  }
		  return orderLogisticsVOs;
	}

	/**
	 * 将OrderPackageDTO转换成OrderPackageVO
	 * 
	 * @param packageDTO
	 * @param returnPackageFacade
	 * @return
	 */
//	private static OrderPackageVO convertToOrderPackageVO(OrderPackageDTO packageDTO,
//			ReturnPackageFacade returnPackageFacade) {
//		boolean isFilterEnum = false, isSnapShot = true;
//		long packageId = packageDTO.getPackageId(), userId = packageDTO.getUserId();
//		OrderPackageVO vo = ReflectUtil.convertObj(OrderPackageVO.class, packageDTO, isFilterEnum);
//		vo.setCartList(convertToOrderCartItemVOList(packageDTO.getOrderCartItemDTOList(), isSnapShot));
//
//		// 判断是否处于退货中
//		OrderPackageState[] opStateArrayOfRP = new OrderPackageState[] { OrderPackageState.RP_APPLY,
//				OrderPackageState.RP_DONE };
//		OrderPackageState opState = packageDTO.getOrderPackageState();
//		boolean isInReturn = CollectionUtil.isInArray(opStateArrayOfRP, opState);
//		// 读取退货状态(包裹状态是退货中or退货完成)
//		if (isInReturn || opState == OrderPackageState.SIGN_IN) {
//			ReturnPackageDTO returnPackageDTO = returnPackageFacade.queryReturnPackageByOrderPackageId(packageId,
//					userId);
//			vo.setReturnPackageState(returnPackageDTO != null ? returnPackageDTO.getReturnState() : null);
//		}
//		// 读取退货申请标记
//		if (!isInReturn) {
//			OrderPackageSimpleDTO opSDTO = OrderApiUtil.convertToOrderPackageSimpleDTO(packageDTO);
//			PackageReturnJudgement packageReturnJudgement = returnPackageFacade.getPackageReturnJudgement(opSDTO);
//			boolean canReturn = packageReturnJudgement != null && packageReturnJudgement.isCanReturn();
//			vo.setCanApplyReturn(canReturn);
//			// 设置包裹退款现金金额
//			OrderPackageRefundDTO opRefund = packageDTO.getOrderPackageRefundDTO();
//			if (opRefund != null && opRefund.getRealCash() != null)
//				vo.setRefundRealCash(opRefund.getRealCash());
//		}
//		return vo;
//	}

	/**
	 * 将List(OrderCartItemDTO)转换成List(OrderCartItemVO)
	 * 
	 * @param dtoList
	 * @param isSnapShot
	 * @return
	 */
	public static List<OrderCartItemVO> convertToOrderCartItemVOList(List<? extends OrderCartItemDTO> dtoList,
			Boolean isSnapShot) {
		List<OrderCartItemVO> voList = new ArrayList<>();
		if (CollectionUtil.isEmptyOfCollection(dtoList))
			return voList;

		for (OrderCartItemDTO dto : dtoList) {
			voList.add(convertToOrderCartItemVO(dto, isSnapShot));
		}
		return voList;
	}

	/**
	 * 将OrderCartItemDTO转换成OrderCartItemVO
	 * 
	 * @param cartDTO
	 * @param isSnapShot
	 * @return
	 */
	private static OrderCartItemVO convertToOrderCartItemVO(OrderCartItemDTO cartDTO, Boolean isSnapShot) {
		boolean isFilterEnum = false;
		OrderCartItemVO vo = ReflectUtil.convertObj(OrderCartItemVO.class, cartDTO, isFilterEnum);
		vo.setOrderSkuList(convertToOrderSkuVOList(cartDTO.getOrderSkuDTOList(), isSnapShot));

		BigDecimal totalRPrice = BigDecimal.ZERO, totalSPrice = BigDecimal.ZERO;
		for (OrderSkuVO orderSkuVO : vo.getOrderSkuList()) {
			totalRPrice = totalRPrice.add(orderSkuVO.getTotalRPrice());
			totalSPrice = totalSPrice.add(orderSkuVO.getTotalSPrice());
		}
		vo.setTotalRPrice(totalRPrice);
		vo.setTotalSPrice(totalSPrice);
		vo.setStoreId(cartDTO.getStoreId());
		return vo;
	}

	/**
	 * 将List(OrderSkuDTO)转换成List(OrderSkuVO)
	 * 
	 * @param dtoList
	 * @param isSnapShot
	 * @return
	 */
	public static List<OrderSkuVO> convertToOrderSkuVOList(List<? extends OrderSkuDTO> dtoList, Boolean isSnapShot) {
		List<OrderSkuVO> voList = new ArrayList<>();
		if (CollectionUtil.isEmptyOfCollection(dtoList))
			return voList;

		for (OrderSkuDTO dto : dtoList) {
			voList.add(convertToOrderSkuVO(dto, isSnapShot));
		}
		return voList;
	}

	/**
	 * 将OrderSkuDTO转换成OrderSkuVO
	 * 
	 * @param orderSkuDTO
	 * @param isSnapShot
	 * @return
	 */
	private static OrderSkuVO convertToOrderSkuVO(OrderSkuDTO orderSkuDTO, Boolean isSnapShot) {
		boolean isFilterEnum = false;
		OrderSkuVO vo = ReflectUtil.convertObj(OrderSkuVO.class, orderSkuDTO, isFilterEnum);
		vo.setSkuSPVO(convertToSkuSPVO(orderSkuDTO.getSkuSPDTO(), isSnapShot));
		BigDecimal totalCount = new BigDecimal(orderSkuDTO.getTotalCount());
		vo.setTotalRPrice(orderSkuDTO.getRprice().multiply(totalCount));
		vo.setTotalSPrice(orderSkuDTO.getHdSPrice().add(orderSkuDTO.getCouponSPrice()).multiply(totalCount));
		return vo;
	}

	/**
	 * 将SkuSPDTO转换为SkuSPVO
	 * 
	 * @param skuSPDTO
	 * @param isSnapShot
	 * @return
	 */
	private static SkuSPVO convertToSkuSPVO(SkuSPDTO skuSPDTO, Boolean isSnapShot) {
		boolean isFilterEnum = false;
		SkuSPVO vo = ReflectUtil.convertObj(SkuSPVO.class, skuSPDTO, isFilterEnum);
		vo = vo != null ? vo : new SkuSPVO();
		return vo;
	}
	
}
