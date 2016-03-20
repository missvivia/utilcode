package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.ExtInfoFieldUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.order.dao.OrderCartItemDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.OrderPackageRefundDao;
import com.xyl.mmall.order.dao.OrderPackageRefundTaskDao;
import com.xyl.mmall.order.dao.OrderRefundExpDao;
import com.xyl.mmall.order.dao.OrderSkuDao;
import com.xyl.mmall.order.dao.tcc.OrderTCCLockDao;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageRefundTaskDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageRefundTaskState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.OrderSkuReturnJudgement;
import com.xyl.mmall.order.enums.PackageReturnJudgement;
import com.xyl.mmall.order.meta.OrderCartItem;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.meta.OrderPackageRefund;
import com.xyl.mmall.order.meta.OrderPackageRefundTask;
import com.xyl.mmall.order.meta.OrderRefundExp;
import com.xyl.mmall.order.meta.OrderSku;
import com.xyl.mmall.order.param.OrderServiceUpdatePackageIdParam;
import com.xyl.mmall.order.param.SetPackageToOutTimeParam;
import com.xyl.mmall.order.service.OrderExpInfoService;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.TradeInternalService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * 
 */
@Service("orderPackageSimpleService")
public class OrderPackageSimpleServiceImpl implements OrderPackageSimpleService {

	private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

	private static final long THREE_DAYS = 3 * ONE_DAY;

	private static final long SEVEN_DAYS = 7 * ONE_DAY;

	private static final long TWENTY_DAYS = 20 * ONE_DAY;

	@Autowired
	protected OrderPackageDao orderPackageDao;

	@Autowired
	protected OrderSkuDao orderSkuDao;

	@Autowired
	protected OrderExpInfoService orderExpInfoService;

	@Autowired
	protected OrderCartItemDao orderCartItemDao;

	@Autowired
	protected OrderFormDao orderFormDao;

	@Autowired
	protected OrderRefundExpDao orderRefundExpDao;

	@Autowired
	protected OrderTCCLockDao orderTCCLockDao;

	@Autowired
	protected OrderPackageRefundDao orderPackageRefundDao;

	@Autowired
	protected OrderPackageRefundTaskDao orderPackageRefundTaskDao;

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected TradeInternalService tradeInternalService;

	@Autowired
	protected ReturnPackageQueryService retPkgQueryService;

	@Autowired
	protected OrderInstantiationUtil orderInstantiationUtil;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 * @param userId
	 * @param orderPackageId
	 * @return key: orderSkuId, value: orderSkuDTO
	 */
	private Map<Long, OrderSkuDTO> getOrderSkuByOrderPackageId(long userId, long orderPackageId) {
		Map<Long, OrderSkuDTO> ret = new HashMap<Long, OrderSkuDTO>();
		List<OrderSku> ordSkuList = orderSkuDao.getListByPackageId(orderPackageId, userId, null);
		if (null != ordSkuList) {
			for (OrderSku ordSku : ordSkuList) {
				if (null == ordSku) {
					continue;
				}
				// to be continued: Sku的快照信息在这里会自动生成吗？
				ret.put(ordSku.getId(), new OrderSkuDTO(ordSku));
			}
		}
		return ret;
	}

	private OrderPackageSimpleDTO convertOrderPackageMetaToDTO(OrderPackage ordPkg) {
		if (null == ordPkg) {
			return null;
		}
		OrderPackageSimpleDTO retDTO = new OrderPackageSimpleDTO(ordPkg);
		long userId = ordPkg.getUserId();
		long ordPkgId = ordPkg.getOrderId();
		long pkgId = ordPkg.getPackageId();
		OrderExpInfoDTO orderExpInfo = orderExpInfoService.queryInfoByUserIdAndOrderId(userId, ordPkgId);
		retDTO.setOrderExpInfo(orderExpInfo);
		Map<Long, OrderSkuDTO> ordSkuMap = getOrderSkuByOrderPackageId(userId, pkgId);
		retDTO.setOrderSkuMap(ordSkuMap);
		return retDTO;
	}

	private List<OrderPackageSimpleDTO> convertOrderPackageMetaListToDTOList(List<OrderPackage> ordPkgList) {
		List<OrderPackageSimpleDTO> retDTOList = new ArrayList<OrderPackageSimpleDTO>();
		if (null == ordPkgList) {
			return retDTOList;
		}
		for (OrderPackage ordPkg : ordPkgList) {
			if (null == ordPkg) {
				continue;
			}
			OrderPackageSimpleDTO retDTO = convertOrderPackageMetaToDTO(ordPkg);
			if (null != retDTO) {
				retDTOList.add(retDTO);
			}
		}
		return retDTOList;
	}

	/**
	 * 包裹是否可以退货的公共判断条件（用户、客服）
	 * 
	 * @param orderPackage
	 * @param earliestPOEndTime
	 * @return
	 */
	private PackageReturnJudgement canReturnCommonJudgement(OrderPackage orderPackage, long earliestPOEndTime,
			List<ReturnPackageDTO> retPkgList) {
		if (null == orderPackage) {
			return PackageReturnJudgement.NULL;
		}
		// 包裹已签收？
		if (orderPackage.getOrderPackageState() != OrderPackageState.SIGN_IN) {
			// to be continued: 取消包裹、拒收包裹、丢件包裹 -> 亮哥处理？
			return PackageReturnJudgement.FAILED_NOT_CONSIGNED;
		}
		// 已经退过货物？
		long ordPkgId = orderPackage.getPackageId();
		long userId = orderPackage.getUserId();
		if (null == retPkgList) {
			retPkgList = retPkgQueryService.queryReturnPackageByOrderPackageId(userId, ordPkgId, false, null);
		}
		if (null != retPkgList && retPkgList.size() > 0) {
			return PackageReturnJudgement.FAILED_ALREADY_RETURNED;
		}
		// PO结束？
		long currentTime = System.currentTimeMillis();
		long poFirstFinishedTimeExt = earliestPOEndTime + TWENTY_DAYS;
		if (currentTime >= poFirstFinishedTimeExt) {
			return PackageReturnJudgement.FAILED_PO_OVER;
		}
		return PackageReturnJudgement.PASSED;
	}

	/**
	 * 包裹是否可以退货（用户角度）
	 * 
	 * @param orderPackage
	 * @param earliestPOEndTime
	 * @return
	 */
	private PackageReturnJudgement canUserReturnPackageExec(OrderPackage orderPackage, long earliestPOEndTime,
			List<ReturnPackageDTO> retPkgList) {
		PackageReturnJudgement commonJudgement = canReturnCommonJudgement(orderPackage, earliestPOEndTime, retPkgList);
		if (!commonJudgement.isCanReturn()) {
			return commonJudgement;
		}
		long currentTime = System.currentTimeMillis();
		// 签收7天内？
		if (currentTime < orderPackage.getConfirmTime() + SEVEN_DAYS) {
			return PackageReturnJudgement.PASSED;
		}
		// 客服在后台重新开启退货申请？
		if (!orderPackage.isKfReopenReturn()) {
			return PackageReturnJudgement.FAILED_OUT_7DAYS_WITHOUT_KF;
		}
		// 重新开启后3天之内？
		long kfReopenTimeExtEnd = orderPackage.getReopenReturnTime() + THREE_DAYS;
		if (currentTime >= kfReopenTimeExtEnd) {
			return PackageReturnJudgement.FAILED_OUT_3DAYS_WITH_KF;
		}
		return PackageReturnJudgement.PASSED;
	}

	/**
	 * 包裹是否可以退货（客服角度）
	 * 
	 * @param ordPkg
	 * @param earliestPOEndTime
	 * @return
	 */
	private boolean canReopenReturnShowToKFExec(OrderPackage ordPkg, long earliestPOEndTime) {
		PackageReturnJudgement commonJudgement = canReturnCommonJudgement(ordPkg, earliestPOEndTime, null);
		if (!commonJudgement.isCanReturn()) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		// 签收7天内？
		if (currentTime < ordPkg.getConfirmTime() + SEVEN_DAYS) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#queryOrderPackageSimple(long)
	 */
	@Override
	public OrderPackageSimpleDTO queryOrderPackageSimple(long orderPackageId) {
		OrderPackage obj = orderPackageDao.getObjectById(orderPackageId);
		return null == obj ? null : convertOrderPackageMetaToDTO(obj);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#queryOrderPackageSimple(long,
	 *      long)
	 */
	@Override
	public OrderPackageSimpleDTO queryOrderPackageSimple(long userId, long orderPackageId) {
		OrderPackage obj = orderPackageDao.getObjectByIdAndUserId(orderPackageId, userId);
		return null == obj ? null : convertOrderPackageMetaToDTO(obj);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#queryOrderPackageSimpleByOrderId(long,
	 *      long, com.xyl.mmall.order.enums.OrderPackageState[])
	 */
	@Override
	public List<OrderPackageSimpleDTO> queryOrderPackageSimpleByOrderId(long userId, long ordPkgId,
			OrderPackageState[] stateArray) {
		List<OrderPackage> objList = orderPackageDao.getListByOrderIdWithState(userId, ordPkgId, stateArray);
		return convertOrderPackageMetaListToDTOList(objList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#queryOrderPackageNum(long,
	 *      long, com.xyl.mmall.order.enums.OrderPackageState[])
	 */
	@Override
	public int queryOrderPackageNum(long userId, long orderId, OrderPackageState[] stateArray) {
		return orderPackageDao.getOrderPackageNumWithState(userId, orderId, stateArray);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#queryOrderPackageSimpleByState(long,
	 *      com.xyl.mmall.order.enums.OrderPackageState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderPackageSimpleDTO> queryOrderPackageSimpleByState(long userId, OrderPackageState[] stateArray,
			DDBParam param) {
		List<OrderPackage> objList = orderPackageDao.getListByUserIdWithState(userId, stateArray, param);
		return convertOrderPackageMetaListToDTOList(objList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#queryOrderPackageSimpleByMailNO(java.lang.String)
	 */
	@Override
	public List<OrderPackageSimpleDTO> queryOrderPackageSimpleByMailNO(String mailNO) {
		if (null == mailNO || 0 == mailNO.length()) {
			return new ArrayList<OrderPackageSimpleDTO>();
		}
		List<OrderPackage> objList = orderPackageDao.getListByMailNO(mailNO);
		return convertOrderPackageMetaListToDTOList(objList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#canUserReturnPackage(long,
	 *      long, long)
	 */
	@Override
	public PackageReturnJudgement canUserReturnPackage(long userId, long orderPackageId, long earliestPOEndTime) {
		OrderPackage obj = orderPackageDao.getObjectByIdAndUserId(orderPackageId, userId);
		if (null == obj) {
			return PackageReturnJudgement.NULL;
		}
		return canUserReturnPackage(convertOrderPackageMetaToDTO(obj), earliestPOEndTime);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#canUserReturnPackage(com.xyl.mmall.order.dto.OrderPackageSimpleDTO,
	 *      long)
	 */
	@Override
	public PackageReturnJudgement canUserReturnPackage(OrderPackageSimpleDTO orderPackage, long earliestPOEndTime) {
		if (null == orderPackage) {
			return PackageReturnJudgement.NULL;
		}
		long ordPkgId = orderPackage.getPackageId();
		long userId = orderPackage.getUserId();
		List<ReturnPackageDTO> retPkgList = retPkgQueryService.queryReturnPackageByOrderPackageId(userId, ordPkgId,
				false, null);
		if (null != retPkgList && retPkgList.size() > 0) {
			return PackageReturnJudgement.FAILED_ALREADY_RETURNED;
		}
		return canUserReturnPackageExec(orderPackage, earliestPOEndTime, retPkgList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#canOrderSkuBeReturned(com.xyl.mmall.order.enums.PackageReturnJudgement,
	 *      long, long)
	 */
	@Override
	public OrderSkuReturnJudgement canOrderSkuBeReturned(PackageReturnJudgement packageJudgement, long userId,
			long orderSkuId) {
		if (null == packageJudgement || packageJudgement != PackageReturnJudgement.PASSED) {
			return OrderSkuReturnJudgement.FAILED_PARENT_PACKAGE;
		}
		OrderSku ordSku = orderSkuDao.getObjectByIdAndUserId(orderSkuId, userId);
		if (null == ordSku) {
			return OrderSkuReturnJudgement.NULL;
		}
		return canOrderSkuBeReturned(packageJudgement, new OrderSkuDTO(ordSku));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#canOrderSkuBeReturned(com.xyl.mmall.order.enums.PackageReturnJudgement,
	 *      com.xyl.mmall.order.dto.OrderSkuDTO)
	 */
	@Override
	public OrderSkuReturnJudgement canOrderSkuBeReturned(PackageReturnJudgement packageJudgement, OrderSkuDTO orderSku) {
		if (null == packageJudgement || packageJudgement != PackageReturnJudgement.PASSED) {
			return OrderSkuReturnJudgement.FAILED_PARENT_PACKAGE;
		}
		if (null == orderSku) {
			return OrderSkuReturnJudgement.NULL;
		}
		// 该商品不支持退货
		/** return _OrderSkuReturnJudgement.FAILED_NOT_SUPPORT; */
		// 3. finally
		return OrderSkuReturnJudgement.PASSED;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#canReopenReturnShowToKF(long,
	 *      long, long)
	 */
	@Override
	public boolean canReopenReturnShowToKF(long userId, long orderPackageId, long earliestPOEndTime) {
		OrderPackage ordPkg = orderPackageDao.getObjectByIdAndUserId(orderPackageId, userId);
		if (null == ordPkg) {
			return false;
		}
		return canReopenReturnShowToKF(convertOrderPackageMetaToDTO(ordPkg), earliestPOEndTime);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#canReopenReturnShowToKF(com.xyl.mmall.order.dto.OrderPackageSimpleDTO,
	 *      long)
	 */
	@Override
	public boolean canReopenReturnShowToKF(OrderPackageSimpleDTO ordPkgDTO, long earliestPOEndTime) {
		if (null == ordPkgDTO) {
			return false;
		}
		return canReopenReturnShowToKFExec(ordPkgDTO, earliestPOEndTime);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#reOpenReturn(long,
	 *      long, long)
	 */
	@Override
	@Transaction
	public boolean reOpenReturn(long userId, long ordPkgId, long earliestPOEndTime) {
		OrderPackage ordPkg = orderPackageDao.getObjectByIdAndUserId(ordPkgId, userId);
		if (null == ordPkg || !canReopenReturnShowToKFExec(ordPkg, earliestPOEndTime)) {
			return false;
		}
		// 获得记录锁
		ordPkg = orderPackageDao.getLockByKey(ordPkg);
		ordPkg.setKfReopenReturn(true);
		ordPkg.setReopenReturnTime(System.currentTimeMillis());
		return orderPackageDao.updateObjectByKey(ordPkg);
	}

	private final int IDX_MATCH_INVALID = 0;

	private final int IDX_MATCH_VALID = 1;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#updatePackageId(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Transaction
	public boolean updatePackageId(List<OrderServiceUpdatePackageIdParam> paramList) {
		long currTime = System.currentTimeMillis();
		// 1.获取订单锁(同时判断paramList是否有效)
		RetArg retArgOfGetOrderLock = getOrderLockForUpdatePackageId(paramList);
		if (!RetArgUtil.get(retArgOfGetOrderLock, Boolean.class))
			return false;
		List<OrderForm> ordListOfAll = RetArgUtil.get(retArgOfGetOrderLock, ArrayList.class);
		Map<Long, OrderForm> ordMapOfAll = CollectionUtil.convertCollToMap(ordListOfAll, "orderId");
		if (CollectionUtil.isNotEmptyOfCollection(ordListOfAll)) {
			for (OrderForm ord : ordListOfAll) {
				if (OrderFormState.isCancel(ord.getOrderFormState())
						|| ord.getOrderFormState() == OrderFormState.WAITING_CANCEL_OMSORDER) {
					logger.info("OrderFormState.isCancel(ord.getOrderFormState())==true, orderId=" + ord.getOrderId());
					return false;
				}
			}
		}

		// 2.读取相关的OrderSku信息
		RetArg retArgOfReadOSL = readOrderSkuListForUpdatePackageId(paramList, ordListOfAll);
		if (!RetArgUtil.get(retArgOfReadOSL, Boolean.class))
			return false;
		List<OrderSku> orderSkuListOfAll = RetArgUtil.get(retArgOfReadOSL, ArrayList.class);

		// 3.根据OrderForm,读取存在的OrderPackage信息(考虑到多订单的概率很小,不再优化SQL)
		List<OrderPackage> orderPackageListOfAll = new ArrayList<>();
		for (OrderForm ord : ordListOfAll) {
			long userId = ord.getUserId(), orderId = ord.getOrderId();
			CollectionUtil.addAllOfList(orderPackageListOfAll, orderPackageDao.getListByOrderId(userId, orderId));
		}

		// 4.过滤已经更新过包裹信息的OrderSku,并检查是否有不一致的包裹信息存在
		RetArg retArgOfMatch = matchOrderSkuList(paramList, orderSkuListOfAll, orderPackageListOfAll);
		if (!RetArgUtil.get(retArgOfMatch, Boolean.class)) {
			return false;
		}
		List<RetArg> retArgListOfMatchValid = RetArgUtil.get(retArgOfMatch, ArrayList.class, IDX_MATCH_VALID);
		if (CollectionUtil.isEmptyOfCollection(retArgListOfMatchValid))
			return true;

		// 5.根据Match结果,生成orderSkuMap1(Key:OrderId+MailNO, Value:List<OrderSku>)
		Map<String, List<OrderSku>> orderSkuMap1 = new LinkedHashMap<>();
		for (RetArg retArgOfTmp : retArgListOfMatchValid) {
			OrderSku orderSku = RetArgUtil.get(retArgOfTmp, OrderSku.class);
			OrderServiceUpdatePackageIdParam param = RetArgUtil
					.get(retArgOfTmp, OrderServiceUpdatePackageIdParam.class);
			String key = genKey1ForUpdatePackageId(param);
			CollectionUtil.putValueOfListMap(orderSkuMap1, key, orderSku, true);
		}

		// 6.生成后续要更新的数据orderPackageAndOrderSkuListMap+orderPackageListOfNew
		RetArg retArgOfUpdateResult = genUpdateResultForUpdatePackageId(retArgOfMatch, paramList, ordMapOfAll,
				orderPackageListOfAll);
		Map<OrderPackage, List<OrderSku>> orderPackageAndOrderSkuListMap = RetArgUtil.get(retArgOfUpdateResult,
				LinkedHashMap.class);
		List<OrderPackage> orderPackageListOfNew = RetArgUtil.get(retArgOfUpdateResult, ArrayList.class);

		// 7.添加OrderPackage记录
		boolean isSucc = true;
		for (OrderPackage orderPackageOfTmp : orderPackageListOfNew) {
			isSucc = isSucc && orderPackageDao.addObject(orderPackageOfTmp) != null;
		}
		// 8.更新OrderSku.packageId, OrderCartItem.packageId
		if (isSucc)
			isSucc = isSucc && updateOrderCartItemAndOrderSkuForUpdatePackageId(orderPackageAndOrderSkuListMap);
		// 9.更新OrderForm.OrderFormState, OrderForm.OmsTime
		boolean isOutTime = paramList.get(0).isOutTime();
		if (isSucc && !isOutTime) {
			isSucc = isSucc && updateOrderFormStateForUpdatePackageId(currTime, ordListOfAll);
		}

		if (!isSucc) {
			throw new ServiceNoThrowException("updatePackageId fail! " + paramList.toString());
		}
		return isSucc;
	}

	/**
	 * 更新OrderSku.packageId, OrderCartItem.packageId
	 * 
	 * @param orderPackageAndOrderSkuListMap
	 * @return
	 */
	private boolean updateOrderCartItemAndOrderSkuForUpdatePackageId(
			Map<OrderPackage, List<OrderSku>> orderPackageAndOrderSkuListMap) {
		boolean isSucc = true;

		for (OrderPackage orderPackageOfTmp : orderPackageAndOrderSkuListMap.keySet()) {
			if (!isSucc)
				break;
			long packageId = orderPackageOfTmp.getPackageId();

			List<OrderSku> orderSkuListOfTmp = orderPackageAndOrderSkuListMap.get(orderPackageOfTmp);
			// 8.1 更新OrderSku.packageId
			isSucc = isSucc && orderSkuDao.updatePackageId(orderSkuListOfTmp, packageId);
			// 8.2 更新OrderCartItem.packageId
			List<OrderCartItem> cartList = new ArrayList<>();
			for (OrderSku orderSkuOfTmp : orderSkuListOfTmp) {
				OrderCartItem cart = new OrderCartItem();
				cart.setId(orderSkuOfTmp.getOrderCartItemId());
				cart.setUserId(orderSkuOfTmp.getUserId());
				cartList.add(cart);
			}
			isSucc = isSucc && orderCartItemDao.updatePackageId(cartList, packageId);
		}

		return isSucc;
	}

	/**
	 * 更新OrderForm.OrderFormState, OrderForm.OmsTime
	 * 
	 * @param currTime
	 * @param ordListOfAll
	 * @return
	 */
	private boolean updateOrderFormStateForUpdatePackageId(long currTime, List<OrderForm> ordListOfAll) {
		boolean isSucc = true;
		OrderFormState[] orderStateArrayOfNeedUpdate = new OrderFormState[] { OrderFormState.WAITING_DELIVE,
				OrderFormState.PART_DELIVE };

		for (OrderForm order : ordListOfAll) {
			if (!isSucc)
				break;
			long orderId = order.getOrderId(), userId = order.getUserId();
			// 9.1 判断是否需要更新订单状态
			OrderFormState oldState = order.getOrderFormState();
			if (!CollectionUtil.isInArray(orderStateArrayOfNeedUpdate, oldState))
				continue;
			// 9.2 生成新的订单状态
			int countOfZeroPackageId = orderCartItemDao.getCountOfZeroPackageId(userId, orderId), countOfUnZeroPackageId = orderCartItemDao
					.getCountOfUnZeroPackageId(userId, orderId);
			OrderFormState newState = countOfUnZeroPackageId <= 0 ? oldState
					: (countOfZeroPackageId > 0 ? OrderFormState.PART_DELIVE : OrderFormState.ALL_DELIVE);
			if (oldState == newState)
				continue;
			// 9.3 尝试更新订单状态
			order.setOrderFormState(newState);
			order.setOmsTime(order.getOmsTime() > 0 ? order.getOmsTime() : currTime);
			isSucc = isSucc && orderFormDao.updateOrdStateWithOMSTime(order, new OrderFormState[] { oldState });
		}
		return isSucc;
	}

	/**
	 * 获取订单锁(同时判断订单是否处于TCC模型中)
	 * 
	 * @param paramList
	 * @return
	 */
	private RetArg getOrderLockForUpdatePackageId(List<OrderServiceUpdatePackageIdParam> paramList) {
		// 0.判断参数
		RetArg retArg = new RetArg();
		if (CollectionUtil.isEmptyOfCollection(paramList)) {
			logger.info("paramList is Empty!");
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 1.查询订单的参数准备
		List<OrderForm> ordListOfParam = new ArrayList<>();
		Map<Long, OrderServiceUpdatePackageIdParam> orderIdAndParamMap = CollectionUtil.convertCollToMap(paramList,
				"orderId");
		for (OrderServiceUpdatePackageIdParam param : orderIdAndParamMap.values()) {
			OrderForm ord = new OrderForm();
			ord.setOrderId(param.getOrderId());
			ord.setUserId(param.getUserId());
			ordListOfParam.add(ord);
		}
		if (CollectionUtil.isEmptyOfCollection(ordListOfParam)) {
			logger.info("ordListOfParam is Empty!");
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		// 2.获得订单锁
		List<OrderForm> ordListOfLock = orderFormDao.getLockByKeys(ordListOfParam);
		if (CollectionUtil.isEmptyOfCollection(ordListOfLock) || ordListOfLock.size() != ordListOfParam.size()) {
			logger.info("ordListOfLock is Empty or size unsame!");
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		// 3.判断订单是否处于TCC模型中
		for (OrderForm ord : ordListOfLock) {
			long orderId = ord.getOrderId();
			if (orderTCCLockDao.getObjectByOrderId(orderId) != null) {
				logger.info("orderTCCLockDao.getObjectByOrderId(orderId)!=null ,orderId=" + orderId);
				RetArgUtil.put(retArg, false);
				return retArg;
			}
		}
		// 4.正常返回
		RetArgUtil.put(retArg, true);
		RetArgUtil.put(retArg, ordListOfLock);
		return retArg;
	}

	/**
	 * 从数据库中,读取OrderSku记录集合
	 * 
	 * @param paramList
	 * @param ordListOfAll
	 * @return
	 */
	private RetArg readOrderSkuListForUpdatePackageId(List<OrderServiceUpdatePackageIdParam> paramList,
			List<OrderForm> ordListOfAll) {
		RetArg retArg = new RetArg();

		// 1.根据OrderId读取全部可能有关的OrderSku记录
		List<OrderSku> orderSkuListOfAll = new ArrayList<>();
		for (OrderForm order : ordListOfAll) {
			long orderId = order.getOrderId(), userId = order.getUserId();
			Boolean isOrder = null;
			CollectionUtil.addAllOfList(orderSkuListOfAll, orderSkuDao.getListByOrderId(orderId, userId, isOrder));
		}
		// 2.根据OrderId和SkuId做成Map
		Map<String, List<OrderSku>> orderSkuListMap = new HashMap<String, List<OrderSku>>();
		for (OrderSku orderSku : orderSkuListOfAll) {
			String key = genKey0ForUpdatePackageId(orderSku.getOrderId(), orderSku.getSkuId());
			CollectionUtil.putValueOfListMap(orderSkuListMap, key, orderSku, false);
		}

		// 3.循环读取对应的OrderSku
		int size = 0;
		List<OrderSku> orderSkuList = new ArrayList<>();
		for (OrderServiceUpdatePackageIdParam param : paramList) {
			String key = genKey0ForUpdatePackageId(param.getOrderId(), param.getSkuId());
			List<OrderSku> orderSkuListOfTmp = orderSkuListMap.get(key);
			CollectionUtil.addAllOfList(orderSkuList, orderSkuListOfTmp);
			if (CollectionUtil.isNotEmptyOfCollection(orderSkuListOfTmp))
				size++;
		}

		// 4.判断数据是否有效
		if (size != paramList.size()) {
			logger.info("orderSkuList is Empty or size unsame!");
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		// 5.生成返回值
		RetArgUtil.put(retArg, true);
		RetArgUtil.put(retArg, orderSkuList);
		return retArg;
	}

	/**
	 * 过滤已经更新过包裹信息的OrderSku,并检查是否有不一致的包裹信息存在
	 * 
	 * @param paramList
	 * @param orderSkuList
	 * @param orderPackageList
	 * @return RetArg.Boolean: 是否成功<br>
	 *         RetArg.RetArg[0].OrderSku: 和DB中的MailNO不一致的OrderSku列表<br>
	 *         RetArg.RetArg[0].OrderServiceUpdatePackageIdParam:
	 *         和DB中的MailNO不一致的Param列表<br>
	 *         RetArg.RetArg[1].OrderSku: 可以更新MailNO的OrderSku<br>
	 *         RetArg.RetArg[1].OrderServiceUpdatePackageIdParam:
	 *         可以更新MailNO的Param列表
	 */
	private RetArg matchOrderSkuList(List<OrderServiceUpdatePackageIdParam> paramList, List<OrderSku> orderSkuList,
			List<OrderPackage> orderPackageList) {
		// 0.参数准备
		RetArg retArgOfResult = new RetArg();
		List<RetArg> retArgListOfValid = new ArrayList<>(), retArgListOfInvalid = new ArrayList<>();
		RetArgUtil.put(retArgOfResult, retArgListOfValid, IDX_MATCH_VALID);
		RetArgUtil.put(retArgOfResult, retArgListOfInvalid, IDX_MATCH_INVALID);
		// Key: OrderId+SkuId
		Map<String, OrderServiceUpdatePackageIdParam> paramMap = new HashMap<>();
		for (OrderServiceUpdatePackageIdParam param : paramList) {
			paramMap.put(genKey0ForUpdatePackageId(param.getOrderId(), param.getSkuId()), param);
		}
		// Key: PackageId
		Map<Long, OrderPackage> orderPackageMap1 = CollectionUtil.convertCollToMap(orderPackageList, "packageId");

		// 循环处理OrderSku
		for (OrderSku orderSku : orderSkuList) {
			String key1 = genKey0ForUpdatePackageId(orderSku.getOrderId(), orderSku.getSkuId());
			OrderServiceUpdatePackageIdParam param = paramMap.get(key1);
			RetArg retArg = new RetArg();
			RetArgUtil.put(retArg, orderSku);
			RetArgUtil.put(retArg, param);

			// 1.判断是否已经更新过PackageId
			boolean isAlreadyUpdatePackageId = orderSku.getPackageId() > 0;
			// 2.如果没有更新过,则保存到返回结果里
			if (!isAlreadyUpdatePackageId) {
				retArgListOfValid.add(retArg);
				continue;
			}

			// 3.尝试读取DB中存在的关联OrderPackage
			OrderPackage orderPackage = orderPackageMap1.get(orderSku.getPackageId());
			// 4.判断包裹Param.MailNO和OrderPackage.MailNO是否不一致
			if (!orderPackage.getMailNO().equalsIgnoreCase(param.getMailNO())) {
				retArgListOfInvalid.add(retArg);
			}
		}

		// 5.生成返回结果
		boolean isValid = CollectionUtil.isEmptyOfCollection(retArgListOfInvalid);
		RetArgUtil.put(retArgOfResult, isValid);

		// TODO.DML 输出错误日志
		if (!isValid) {
			for (RetArg retArgOfTmp : retArgListOfInvalid) {

			}
			logger.info("matchOrderSkuList fail!");
		}

		return retArgOfResult;
	}

	/**
	 * 生成Map.Key0
	 * 
	 * @param orderId
	 * @param skuId
	 * @return orderId + "-" + skuId
	 */
	private String genKey0ForUpdatePackageId(long orderId, long skuId) {
		return orderId + "-" + skuId;
	}

	/**
	 * 生成Map.Key1
	 * 
	 * @param obj
	 * @return OrderId+"-"+MailNO
	 */
	private String genKey1ForUpdatePackageId(OrderServiceUpdatePackageIdParam obj) {
		return obj.getOrderId() + "-" + obj.getMailNO();
	}

	/**
	 * 生成Map.Key
	 * 
	 * @param obj
	 * @return OrderId+"-"+MailNO
	 */
	private String genKey1ForUpdatePackageId(OrderPackage obj) {
		return obj.getOrderId() + "-" + obj.getMailNO();
	}

	/**
	 * 生成后续要更新的数据orderPackageAndOrderSkuListMap+orderPackageListOfNew
	 * 
	 * @param retArgOfMatch
	 * @param paramList
	 * @param ordMapOfAll
	 * @param orderPackageListOfAll
	 * @return RetArg.ArrayList: 需要新增OrderPackage<br>
	 *         RetArg.LinkedHashMap: OrderPackage和List(OrderSku)的映射
	 */
	@SuppressWarnings("unchecked")
	private RetArg genUpdateResultForUpdatePackageId(RetArg retArgOfMatch,
			List<OrderServiceUpdatePackageIdParam> paramList, Map<Long, OrderForm> ordMapOfAll,
			List<OrderPackage> orderPackageListOfAll) {
		long currTime = System.currentTimeMillis();
		RetArg retArg = new RetArg();
		// 1.转换原有的参数
		// 1.1
		// 生成paramMap(Key:OrderId+SkuId,Value:OrderServiceUpdatePackageIdParam)
		Map<String, OrderServiceUpdatePackageIdParam> paramMap = new HashMap<>();
		for (OrderServiceUpdatePackageIdParam param : paramList) {
			paramMap.put(genKey0ForUpdatePackageId(param.getOrderId(), param.getSkuId()), param);
		}
		// 1.2 生成orderPackageMap1(Key:OrderId+MailNO,Value:OrderPackage)
		Map<String, OrderPackage> orderPackageMap1 = new LinkedHashMap<>();
		for (OrderPackage orderPackageOfTmp : orderPackageListOfAll) {
			orderPackageMap1.put(genKey1ForUpdatePackageId(orderPackageOfTmp), orderPackageOfTmp);
		}
		// 1.3 根据Match结果,生成orderSkuMap1(Key:OrderId+MailNO,Value:List<OrderSku>)
		List<RetArg> retArgListOfMatchValid = RetArgUtil.get(retArgOfMatch, ArrayList.class, IDX_MATCH_VALID);
		Map<String, List<OrderSku>> orderSkuMap1 = new LinkedHashMap<>();
		for (RetArg retArgOfTmp : retArgListOfMatchValid) {
			OrderSku orderSku = RetArgUtil.get(retArgOfTmp, OrderSku.class);
			OrderServiceUpdatePackageIdParam param = RetArgUtil
					.get(retArgOfTmp, OrderServiceUpdatePackageIdParam.class);
			String key = genKey1ForUpdatePackageId(param);
			CollectionUtil.putValueOfListMap(orderSkuMap1, key, orderSku, true);
		}

		// 2.生成返回数据orderPackageListOfNew+orderPackageAndOrderSkuListMap
		Map<OrderPackage, List<OrderSku>> orderPackageAndOrderSkuListMap = new LinkedHashMap<>();
		List<OrderPackage> orderPackageListOfNew = new ArrayList<>();
		for (String key1 : orderSkuMap1.keySet()) {
			// 2.1 参数准备
			List<OrderSku> orderSkuListOfTmp = orderSkuMap1.get(key1);
			OrderSku orderSkuOfTmp = CollectionUtil.getFirstObjectOfCollection(orderSkuListOfTmp);
			OrderForm ordOfTmp = ordMapOfAll.get(orderSkuOfTmp.getOrderId());
			String key0 = genKey0ForUpdatePackageId(orderSkuOfTmp.getOrderId(), orderSkuOfTmp.getSkuId());
			OrderServiceUpdatePackageIdParam paramOfTmp = paramMap.get(key0);
			// 2.2 生成OrderPackage
			OrderPackage orderPackageOfTmp = orderPackageMap1.get(key1);
			// 如果OrderPackage不存在,则创建
			if (orderPackageOfTmp == null) {
				long packageId = orderPackageDao.allocateRecordId();
				orderPackageOfTmp = new OrderPackage();
				// 完备OrderPackage
				orderPackageOfTmp.setExpressCompany(ordOfTmp.getExpressCompany());
				orderPackageOfTmp.setPackageId(packageId);
				orderPackageOfTmp.setUserId(orderSkuOfTmp.getUserId());
				orderPackageOfTmp.setOmsTime(currTime);
				orderPackageOfTmp.setOrderTime(ordOfTmp.getOrderTime());
				orderPackageOfTmp.setOrderId(orderSkuOfTmp.getOrderId());
				orderPackageOfTmp.setWarehouseId(paramOfTmp.getWarehouseId());
				orderPackageOfTmp.setWarehouseName(paramOfTmp.getWarehouseName());
				orderPackageOfTmp.setExpressCompanyReturn(paramOfTmp.getExpressCompany());
				orderPackageOfTmp.setMailNO(paramOfTmp.getMailNO());
				OrderPackageState opState = paramOfTmp.isOutTime() ? OrderPackageState.OUT_TIME
						: OrderPackageState.WAITING_SIGN_IN;
				orderPackageOfTmp.setOrderPackageState(opState);
				orderPackageOfTmp.setExpSTime(paramOfTmp.isOutTime() ? 0 : currTime);
				orderPackageListOfNew.add(orderPackageOfTmp);
			}
			// 2.3 设置OrderPackage和OrderSku的关联
			orderPackageAndOrderSkuListMap.put(orderPackageOfTmp, orderSkuListOfTmp);
		}

		// 3.返回数据
		RetArgUtil.put(retArg, orderPackageListOfNew);
		RetArgUtil.put(retArg, orderPackageAndOrderSkuListMap);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setPackageToSignIn(java.lang.String,
	 *      java.lang.String)
	 */
	@Transaction
	public boolean setPackageToSignIn(String mailNO, String expressCompanyReturn) {
		boolean isSucc = true;
		// 1.更新包裹状态
		if (isSucc) {
			OrderPackageState state = OrderPackageState.SIGN_IN;
			RetArg retArg = new RetArg();
			int retCodeOfUpdateOrderPackage = setPackageStateByOMS(mailNO, expressCompanyReturn, state, retArg);
			isSucc = isSucc && retCodeOfUpdateOrderPackage == 1;
		}

		if (!isSucc)
			throw new ServiceNoThrowException("setPackageToSignIn fail: mailNO=" + mailNO + " ,expressCompanyReturn="
					+ expressCompanyReturn);
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToOutTime(com.xyl.mmall.order.param.SetPackageToOutTimeParam)
	 */
	@Transaction
	public boolean setPackageToOutTime(SetPackageToOutTimeParam param) {
		boolean isSucc = true;
		// 1.获得订单锁
		long orderId = param.getOrderId(), userId = param.getUserId();
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId="
					+ userId);
			return false;
		}
		// 2.调用updatePackageId方法
		List<OrderServiceUpdatePackageIdParam> updateParamList = new ArrayList<>();
		long warehouseId = 0;
		for (long skuId : param.getSkuIdColl()) {
			OrderServiceUpdatePackageIdParam updateParam = new OrderServiceUpdatePackageIdParam();
			updateParam.setOrderId(orderId);
			updateParam.setOutTime(true);
			updateParam.setSkuId(skuId);
			updateParam.setUserId(userId);
			updateParam.setWarehouseId(warehouseId);
			updateParamList.add(updateParam);
		}
		isSucc = isSucc && updatePackageId(updateParamList);

		if (!isSucc)
			throw new ServiceNoThrowException("setPackageToOutTime fail: " + param.toString());
		return isSucc;
	}

	/**
	 * 更新包裹的签收状态
	 * 
	 * @param mailNO
	 * @param expressCompanyReturn
	 * @param state
	 *            OrderPackageState.SIGN_IN
	 * @param retArg
	 *            保存一些处理中的结果数据
	 * @return 1: 成功<br>
	 *         0: 失败<br>
	 *         -1: 没有包裹记录<br>
	 *         -2: 参数不正确
	 */
	private int setPackageStateByOMS(String mailNO, String expressCompanyReturn, OrderPackageState state, RetArg retArg) {
		// 0.参数判断
		OrderPackageState[] opStateArrayOfValid = new OrderPackageState[] { OrderPackageState.SIGN_IN };
		if (StringUtils.isBlank(mailNO) || StringUtils.isBlank(expressCompanyReturn)
				|| !CollectionUtil.isInArray(opStateArrayOfValid, state)) {
			logger.info("param error!");
			return -2;
		}

		// 1.查询对应的包裹记录
		List<OrderPackage> orderPackageList = orderPackageDao.getListByMailNO(mailNO);
		if (CollectionUtil.isEmptyOfCollection(orderPackageList)) {
			logger.error("orderPackageList==null: mailNO=" + mailNO + " ,expressCompanyReturn=" + expressCompanyReturn);
			return -1;
		}
		Iterator<OrderPackage> iter = orderPackageList.iterator();
		while (iter.hasNext()) {
			OrderPackage orderPackage = iter.next();
			if (!StringUtils.equalsIgnoreCase(orderPackage.getExpressCompanyReturn(), expressCompanyReturn))
				iter.remove();
		}
		RetArgUtil.put(retArg, CollectionUtil.addAllOfList(null, orderPackageList));
		boolean isSucc = true;
		if (CollectionUtil.isEmptyOfCollection(orderPackageList)) {
			isSucc = false;
			logger.error("orderPackageList==null: mailNO=" + mailNO + " ,expressCompanyReturn=" + expressCompanyReturn);
			return 0;
		}

		// 2.尝试更新包裹状态
		if (isSucc) {
			for (OrderPackage orderPackage : orderPackageList) {
				// 2.1 获得订单锁
				long orderId = orderPackage.getOrderId(), userId = orderPackage.getUserId();
				RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
				Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
				if (isContinue != Boolean.TRUE) {
					isSucc = false;
					break;
				}
				// 2.2 兼容重复更新
				OrderPackage orderPackageOfDB = orderPackageDao.getObjectByPrimaryKeyAndPolicyKey(orderPackage);
				if (orderPackageOfDB.getOrderPackageState() == state)
					continue;
				// 2.3 更新包裹状态
				orderPackageOfDB.setOrderPackageState(state);
				if (state == OrderPackageState.SIGN_IN)
					isSucc = isSucc
							&& orderPackageDao.updateOrderPackageStateAndConfirmTime(orderPackageOfDB,
									OrderPackageState.WAITING_SIGN_IN);

			}
		}
		return isSucc ? 1 : 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToRPApply(long,
	 *      long)
	 */
	@Transaction
	public boolean setPackageToRPApply(long packageId, long userId) {
		return setPackageStateByReturnPackage(packageId, userId, OrderPackageState.RP_APPLY, OrderPackageState.SIGN_IN);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToRPDone(long,
	 *      long)
	 */
	@Transaction
	public boolean setPackageToRPDone(long packageId, long userId) {
		return setPackageStateByReturnPackage(packageId, userId, OrderPackageState.RP_DONE, OrderPackageState.RP_APPLY);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToCancelRPApply(long,
	 *      long)
	 */
	@Transaction
	public boolean setPackageToCancelRPApply(long packageId, long userId) {
		return setPackageStateByReturnPackage(packageId, userId, OrderPackageState.SIGN_IN, OrderPackageState.RP_APPLY);
	}

	/**
	 * 更新包裹状态-给退货逻辑使用
	 * 
	 * @param packageId
	 * @param userId
	 * @param newState
	 * @param oldState
	 * @return
	 */
	private boolean setPackageStateByReturnPackage(long packageId, long userId, OrderPackageState newState,
			OrderPackageState oldState) {
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalServiceByPackageId(packageId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalServiceByPackageId fail, packageId=" + packageId
					+ " ,userId=" + userId);
			return false;
		}
		OrderPackage op = orderPackageDao.getObjectByIdAndUserId(packageId, userId);
		if (op.getOrderPackageState() == newState)
			return true;
		op.setOrderPackageState(newState);
		return orderPackageDao.updateOrderPackageState(op, oldState);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToCancelOT(long,
	 *      long)
	 */
	@Transaction
	public boolean setPackageToCancelOT(long packageId, long userId) {
		OrderPackageState opState = OrderPackageState.CANCEL_OT;
		return setPackageStateToCancelAndRefund(packageId, userId, opState);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToCancelOT(long,
	 *      long)
	 */
	@Transaction
	public boolean setPackageToCancelSR(long packageId, long userId) {
		OrderPackageState opState = OrderPackageState.CANCEL_SR;
		return setPackageStateToCancelAndRefund(packageId, userId, opState);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#setPackageToCancelOT(long,
	 *      long)
	 */
	@Transaction
	public boolean setPackageToCancelLost(long packageId, long userId) {
		OrderPackageState opState = OrderPackageState.CANCEL_LOST;
		return setPackageStateToCancelAndRefund(packageId, userId, opState);
	}

	/**
	 * 设置包裹为取消(private方法无法添加事务)
	 * 
	 * @param packageId
	 * @param userId
	 * @param opState
	 * @return
	 */
	private boolean setPackageStateToCancelAndRefund(long packageId, long userId, OrderPackageState opState) {
		// 0.获得订单锁
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalServiceByPackageId(packageId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalServiceByPackageId fail, packageId=" + packageId
					+ " ,userId=" + userId);
			return false;
		}

		// 1.判断输入参数,并读取相关的包裹和订单数据
		RetArg retArgOfCheck = checkPackageForCancel(packageId, userId, opState);
		Integer retCodeOfCheck = RetArgUtil.get(retArgOfCheck, Integer.class);
		if (retCodeOfCheck < 0) {
			logger.error("retCodeOfCheck=" + retCodeOfCheck + " ,packageId=" + packageId + " ,userId=" + userId);
			return false;
		} else if (retCodeOfCheck == 0)
			return true;

		boolean needRefundCash = retCodeOfCheck == 1;
		OrderFormDTO orderDTO = RetArgUtil.get(retArgOfCheck, OrderFormDTO.class);
		OrderPackage op = RetArgUtil.get(retArgOfCheck, OrderPackage.class);
		long orderId = orderDTO.getOrderId();

		// 2.判断当前订单的其他包裹是否都取消了
		boolean isAllPackageCancel = true, isCanRefundExp = true;
		for (OrderPackageDTO packageDTOOfTmp : orderDTO.getOrderPackageDTOList()) {
			OrderPackageState opOfTmp = packageDTOOfTmp.getOrderPackageState();
			if (packageDTOOfTmp.getPackageId() == op.getPackageId())
				opOfTmp = opState;
			isAllPackageCancel = isAllPackageCancel && OrderPackageState.isCancel(opOfTmp);
			isCanRefundExp = isCanRefundExp && OrderPackageState.isCanRefundExp(opOfTmp);
		}

		// 3.计算退款金额
		boolean isSucc = true;
		BigDecimal refundExpPrice = BigDecimal.ZERO, realCash = BigDecimal.ZERO, redCash = BigDecimal.ZERO;
		if (isSucc && needRefundCash) {
			boolean calCurrPackage = !isAllPackageCancel;
			RetArg retArgOfCalPrice = calPriceByPackage(orderDTO, op, calCurrPackage, isCanRefundExp);
			realCash = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 0);
			redCash = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 1);
			refundExpPrice = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 2);
			if (realCash == null && redCash == null) {
				isSucc = false;
			}
			if (!isSucc)
				logger.error("refund fail, packageId=" + packageId);
		}
		// 4.更新运费退款记录
		if (isSucc && refundExpPrice != null && refundExpPrice.compareTo(BigDecimal.ZERO) > 0) {
			OrderRefundExp orderRefundExp = OrderRefundExp.genInstance(orderDTO);
			orderRefundExp.setExpPrice(refundExpPrice);
			orderRefundExp.setPackageId(packageId);
			isSucc = orderRefundExpDao.addObject(orderRefundExp) != null;
			if (!isSucc) {
				logger.error("orderRefundExpDao.addObject fail, orderId=" + orderId);
			}
		}

		// 5.更新包裹状态
		if (isSucc) {
			OrderPackageState oldState = op.getOrderPackageState();
			op.setOrderPackageState(opState);
			isSucc = isSucc && orderPackageDao.updateOrderPackageStateAndCancelTime(op, oldState);
			if (!isSucc)
				logger.error("orderPackageDao.updateOrderPackageStateAndCancelTime, packageId=" + packageId);
		}

		// 6.记录包裹退款的金额组成
		if (isSucc) {
			isSucc = isSucc && addOrderPackageRefund(op, realCash, redCash);
		}

		// 7.记录异步退款任务
		if (isSucc) {
			BigDecimal wybCash = realCash;
			isSucc = isSucc && addOrderPackageRefundTask(op, wybCash, redCash, OrderCancelRType.ORI);
		}

		if (!isSucc)
			throw new ServiceNoThrowException("packageId=" + packageId);
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#updateRetryFlag(com.xyl.mmall.order.dto.OrderPackageRefundTaskDTO,
	 *      long)
	 */
	public boolean updateRetryFlag(OrderPackageRefundTaskDTO obj, long retryFlagOfOld) {
		return orderPackageRefundTaskDao.updateRetryFlag(obj, retryFlagOfOld);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#getOrderPackageRefundTaskListByStateWithMinId(long,
	 *      com.xyl.mmall.order.enums.OrderPackageRefundTaskState,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderPackageRefundTaskDTO> getOrderPackageRefundTaskListByStateWithMinId(long minId,
			OrderPackageRefundTaskState state, DDBParam param) {
		OrderUtil.initDDBParamWithMinId(param, "id");
		List<OrderPackageRefundTask> taskList = orderPackageRefundTaskDao.getListByStateWithMinId(minId, state, param);
		List<OrderPackageRefundTaskDTO> taskDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(taskList)) {
			for (OrderPackageRefundTask task : taskList) {
				CollectionUtil.addOfListFilterNull(taskDTOList, OrderPackageRefundTaskDTO.genInstance(task));
			}
		}
		return taskDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderPackageSimpleService#addOrderPackageRefundTask(com.xyl.mmall.order.meta.OrderPackage,
	 *      java.math.BigDecimal, java.math.BigDecimal)
	 */
	public boolean addOrderPackageRefundTask(OrderPackage op, BigDecimal wybCash, BigDecimal redCash,
			OrderCancelRType rtype) {
		// 0.参数准备
		boolean isSucc = true;
		wybCash = wybCash == null || wybCash.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : wybCash;
		redCash = redCash == null || redCash.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : redCash;
		// 计算totalCash
		BigDecimal totalCash = wybCash.add(redCash);
		if (totalCash.compareTo(BigDecimal.ZERO) <= 0)
			return true;

		// 1.计算retryFlag
		long retryFlag = 0;
		if (wybCash.compareTo(BigDecimal.ZERO) > 0)
			retryFlag = ExtInfoFieldUtil.setValueOfType1(retryFlag, OrderPackageRefundTask.IDX_REFUND_WYB, true);
		if (redCash.compareTo(BigDecimal.ZERO) > 0)
			retryFlag = ExtInfoFieldUtil.setValueOfType1(retryFlag, OrderPackageRefundTask.IDX_REFUND_HB, true);

		// 2.添加OrderPackageRefundTask
		rtype = rtype != null ? rtype : OrderCancelRType.ORI;
		OrderPackageRefundTask opRefundTask = new OrderPackageRefundTask();
		opRefundTask.setPackageId(op.getPackageId());
		opRefundTask.setOrderId(op.getOrderId());
		opRefundTask.setUserId(op.getUserId());
		opRefundTask.setRedCash(redCash);
		opRefundTask.setWybCash(wybCash);
		opRefundTask.setCtime(System.currentTimeMillis());
		opRefundTask.setRtype(rtype);
		opRefundTask.setRetryFlag(retryFlag);
		isSucc = isSucc && orderPackageRefundTaskDao.addObject(opRefundTask) != null;

		return isSucc;
	}

	/**
	 * 记录包裹退款的金额组成
	 * 
	 * @param op
	 * @param realCash
	 * @param redCash
	 * @return
	 */
	private boolean addOrderPackageRefund(OrderPackage op, BigDecimal realCash, BigDecimal redCash) {
		boolean isSucc = true;
		realCash = realCash == null || realCash.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : realCash;
		redCash = redCash == null || redCash.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : redCash;
		// 计算totalCash
		BigDecimal totalCash = realCash.add(redCash);

		// 添加OrderPackageRefund
		OrderPackageRefund opRefund = new OrderPackageRefund();
		opRefund.setPackageId(op.getPackageId());
		opRefund.setOrderId(op.getOrderId());
		opRefund.setUserId(op.getUserId());
		opRefund.setRealCash(realCash);
		opRefund.setRedCash(redCash);
		opRefund.setTotalCash(totalCash);
		isSucc = isSucc && orderPackageRefundDao.addObject(opRefund) != null;

		return isSucc;
	}

	/**
	 * 计算退款金额
	 * 
	 * @param orderDTO
	 * @param op
	 * @param calCurrPackage
	 * @param isCanRefundExp
	 * @return RetArg.BigDecimal[0]: 退款在线支付金额<br>
	 *         RetArg.BigDecimal[1]: 退款红包金额<br>
	 *         RetArg.BigDecimal[2]: 退款快递费金额
	 */
	private RetArg calPriceByPackage(OrderFormDTO orderDTO, OrderPackage op, boolean calCurrPackage,
			boolean isCanRefundExp) {
		RetArg retArg = new RetArg();
		// 计算退款金额
		BigDecimal realCash = BigDecimal.ZERO, redCash = BigDecimal.ZERO, refundExpPrice = BigDecimal.ZERO;
		BigDecimal realCashOfExp = BigDecimal.ZERO, redCashOfExp = BigDecimal.ZERO;
		// CASE1: 计算当前包裹
		if (calCurrPackage) {
			RetArg retArgOfCalPrice = calPriceByPackage(orderDTO, op);
			realCash = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 0);
			redCash = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 1);
		}
		// CASE2: 计算OrderForm-OrderPackage(Other),即最后一个包裹
		else {
			BigDecimal realCashOfOther = BigDecimal.ZERO, redCashOfOther = BigDecimal.ZERO;
			// 计算非本包裹的金额
			for (OrderPackageDTO packageDTOOfTmp : orderDTO.getOrderPackageDTOList()) {
				if (packageDTOOfTmp.getPackageId() == op.getPackageId())
					continue;
				RetArg retArgOfCalPrice = calPriceByPackage(orderDTO, packageDTOOfTmp);
				BigDecimal realCash1 = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 0);
				BigDecimal redCash1 = RetArgUtil.get(retArgOfCalPrice, BigDecimal.class, 1);
				if (realCash1 == null && redCash1 == null) {
					return null;
				}
				if (realCash1 != null)
					realCashOfOther = realCashOfOther.add(realCash1);
				if (redCash1 != null)
					redCashOfOther = redCashOfOther.add(redCash1);
			}
			// 计算邮费
			if (isCanRefundExp) {
				refundExpPrice = orderDTO.getExpUserPrice();
				redCashOfExp = orderDTO.getExpUserPriceOfRed();
				realCashOfExp = refundExpPrice.subtract(redCashOfExp);
			}
			// 本包裹的红包金额 = 订单红包总金额 - 非本包裹的红包金额 - 快递费(支付的红包部分) + 快递费(本次要退回的红包部分)
			redCash = orderDTO.getRedCash().subtract(redCashOfOther).subtract(orderDTO.getExpUserPriceOfRed());
			redCash = redCash.add(redCashOfExp);
			// 本包裹的在线支付金额 = 订单的商品总金额 - 订单的商品红包总额(订单的红包总额-邮费(红包)) - 非本包裹的在线支付金额 +
			// 快递费(本次要退回的现金部分)
			realCash = orderDTO.getCartRPrice().subtract(orderDTO.getRedCash()).add(orderDTO.getExpUserPriceOfRed())
					.subtract(realCashOfOther);
			realCash = realCash.add(realCashOfExp);
		}

		RetArgUtil.put(retArg, realCash, 0);
		RetArgUtil.put(retArg, redCash, 1);
		RetArgUtil.put(retArg, refundExpPrice, 2);
		return retArg;
	}

	/**
	 * 计算包裹名下OrderSku的实付金额
	 * 
	 * @param orderDTO
	 * @param op
	 * @return RetArg.BigDecimal[0]: 在线支付金额<br>
	 *         RetArg.BigDecimal[1]: 红包金额<br>
	 */
	private RetArg calPriceByPackage(OrderFormDTO orderDTO, OrderPackage op) {
		RetArg retArg = new RetArg();
		// 1.获得包裹名下的OrderSku明细
		Map<Long, List<OrderSkuDTO>> orderSkuDTOMap = CollectionUtil.convertCollToListMap(
				orderDTO.getAllOrderSkuDTOList(), "packageId");
		List<OrderSkuDTO> orderSkuDTOList = CollectionUtil.getValueOfMap(orderSkuDTOMap, op.getPackageId());
		if (CollectionUtil.isEmptyOfCollection(orderSkuDTOList)) {
			return null;
		}
		// 2.计算包裹名下OrderSku明细对应的金额总和
		BigDecimal totalCash = BigDecimal.ZERO, realCash = BigDecimal.ZERO, redCash = BigDecimal.ZERO;
		for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
			BigDecimal count = new BigDecimal(orderSkuDTO.getTotalCount());
			totalCash = totalCash.add(orderSkuDTO.getRprice().multiply(count));
			redCash = redCash.add(orderSkuDTO.getRedSPrice().multiply(count));
		}
		realCash = totalCash.subtract(redCash);
		// 返回结果
		RetArgUtil.put(retArg, realCash, 0);
		RetArgUtil.put(retArg, redCash, 1);
		return retArg;
	}

	/**
	 * 检查要取消的包裹的数据是否合法
	 * 
	 * @param packageId
	 * @param userId
	 * @param opState
	 * @return RetArg.Integer:
	 *         -3:包裹或订单不存在;-2:包裹旧状态不匹配;-1:opState不支持;0:包裹状态已更新;1:需要退款;2:不需要退款<BR>
	 *         RetArg.OrderPackage<BR>
	 *         RetArg.OrderFormDTO
	 */
	private RetArg checkPackageForCancel(long packageId, long userId, OrderPackageState opState) {
		RetArg retArg = new RetArg();
		// 1.判断要更新的包裹状态是否合法
		// Key:更新后的状态,Value:更新前的状态
		Map<OrderPackageState, OrderPackageState> opStateMap = new HashMap<>();
		opStateMap.put(OrderPackageState.CANCEL_OT, OrderPackageState.OUT_TIME);
		opStateMap.put(OrderPackageState.CANCEL_SR, OrderPackageState.WAITING_SIGN_IN);
		opStateMap.put(OrderPackageState.CANCEL_LOST, OrderPackageState.WAITING_SIGN_IN);
		if (!CollectionUtil.contains(opStateMap.keySet(), opState)) {
			RetArgUtil.put(retArg, -1);
			return retArg;
		}
		// 2.读取包裹数据
		OrderPackage op = orderPackageDao.getObjectByIdAndUserId(packageId, userId);
		if (op == null) {
			RetArgUtil.put(retArg, -3);
			return retArg;
		}
		RetArgUtil.put(retArg, op);
		// 3.判断包裹旧状态和新状态是否匹配
		OrderPackageState opStateOfOld = opStateMap.get(opState);
		if (opStateOfOld != null && opStateOfOld != op.getOrderPackageState()) {
			if (opState == op.getOrderPackageState())
				RetArgUtil.put(retArg, 0);
			else
				RetArgUtil.put(retArg, -2);
			return retArg;
		}

		// 4.判断是否要退款(根据支付方式判断)
		long orderId = op.getOrderId();
		OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, null);
		if (orderDTO == null) {
			RetArgUtil.put(retArg, -3);
			return retArg;
		}
		RetArgUtil.put(retArg, orderDTO);
		boolean needRefund = OrderFormPayMethod.isOnlinePayMethod(orderDTO.getOrderFormPayMethod());
		RetArgUtil.put(retArg, needRefund ? 1 : 2);
		return retArg;
	}

}
