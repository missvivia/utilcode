package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.ExtInfoFieldUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.IncrField;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.netease.space.framework.dao.sql.AbstractDaoSqlBase;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.aop.OperateLog;
import com.xyl.mmall.framework.enums.CartItemType;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.interfaces.TCCDaoInterface;
import com.xyl.mmall.framework.interfaces.TCCMetaExam;
import com.xyl.mmall.framework.util.TCCUtil;
import com.xyl.mmall.order.api.util.OrderApiUtil;
import com.xyl.mmall.order.dao.CODAuditLogDao;
import com.xyl.mmall.order.dao.InvoiceInOrdDao;
import com.xyl.mmall.order.dao.OrderCancelInfoDao;
import com.xyl.mmall.order.dao.OrderCartItemDao;
import com.xyl.mmall.order.dao.OrderExpInfoDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderPOInfoDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.OrderRefundExpDao;
import com.xyl.mmall.order.dao.OrderSkuCartItemDao;
import com.xyl.mmall.order.dao.OrderSkuDao;
import com.xyl.mmall.order.dao.SkuOrderStockDao;
import com.xyl.mmall.order.dao.TradeItemDao;
import com.xyl.mmall.order.dao.tcc.CODAuditLogTCCDao;
import com.xyl.mmall.order.dao.tcc.InvoiceInOrdTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderCancelInfoTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderCartItemTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderExpInfoTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderFormTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderPOInfoTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderSkuCartItemTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderSkuTCCDao;
import com.xyl.mmall.order.dao.tcc.OrderTCCLockDao;
import com.xyl.mmall.order.dao.tcc.SkuOrderStockTCCDao;
import com.xyl.mmall.order.dao.tcc.TradeItemTCCDao;
import com.xyl.mmall.order.dao.tcc.UpdateOrderStateTCCDao;
import com.xyl.mmall.order.dao.tcc.UpdatePackageStateTCCDao;
import com.xyl.mmall.order.dto.CODAuditLogDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.OrderTCCLockType;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.exception.OrderTCCServiceException;
import com.xyl.mmall.order.meta.CODAuditLog;
import com.xyl.mmall.order.meta.OrderCancelInfo;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPOInfo;
import com.xyl.mmall.order.meta.OrderSkuCartItem;
import com.xyl.mmall.order.meta.SkuOrderStock;
import com.xyl.mmall.order.meta.tcc.CODAuditLogTCC;
import com.xyl.mmall.order.meta.tcc.InvoiceInOrdTCC;
import com.xyl.mmall.order.meta.tcc.OrderCancelInfoTCC;
import com.xyl.mmall.order.meta.tcc.OrderCartItemTCC;
import com.xyl.mmall.order.meta.tcc.OrderExpInfoTCC;
import com.xyl.mmall.order.meta.tcc.OrderFormTCC;
import com.xyl.mmall.order.meta.tcc.OrderPOInfoTCC;
import com.xyl.mmall.order.meta.tcc.OrderSkuCartItemTCC;
import com.xyl.mmall.order.meta.tcc.OrderSkuTCC;
import com.xyl.mmall.order.meta.tcc.OrderTCCLock;
import com.xyl.mmall.order.meta.tcc.SkuOrderStockTCC;
import com.xyl.mmall.order.meta.tcc.TradeItemTCC;
import com.xyl.mmall.order.meta.tcc.UpdateOrderStateTCC;
import com.xyl.mmall.order.meta.tcc.UpdatePackageStateTCC;
import com.xyl.mmall.order.param.TryAddOrderByTCCParam;
import com.xyl.mmall.order.result.TryAddOrderByTCCResult;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.OrderTCCService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Service("orderTCCServiceInternalImpl")
public class OrderTCCServiceInternalImpl implements OrderTCCService {

	// DAO--------------------

	@Autowired
	OrderFormDao orderFormDao;

	@Autowired
	OrderFormTCCDao orderFormTCCDao;

	@Autowired
	OrderExpInfoDao orderExpInfoDao;

	@Autowired
	OrderExpInfoTCCDao orderExpInfoTCCDao;

	@Autowired
	OrderSkuDao orderSkuDao;

	@Autowired
	OrderSkuTCCDao orderSkuTCCDao;

	@Autowired
	OrderCartItemDao orderCartItemDao;

	@Autowired
	OrderCartItemTCCDao orderCartItemTCCDao;

	@Autowired
	OrderSkuCartItemDao orderSkuCartItemDao;

	@Autowired
	OrderSkuCartItemTCCDao orderSkuCartItemTCCDao;

	@Autowired
	OrderPOInfoDao orderPOInfoDao;

	@Autowired
	OrderPOInfoTCCDao orderPOInfoTCCDao;

	@Autowired
	SkuOrderStockDao skuOrderStockDao;

	@Autowired
	SkuOrderStockTCCDao skuOrderStockTCCDao;

	@Autowired
	OrderCancelInfoDao orderCancelInfoDao;

	@Autowired
	OrderCancelInfoTCCDao orderCancelInfoTCCDao;

	@Autowired
	UpdateOrderStateTCCDao updateOrderStateTCCDao;

	@Autowired
	UpdatePackageStateTCCDao updatePackageStateTCCDao;

	@Autowired
	TradeItemDao tradeItemDao;

	@Autowired
	TradeItemTCCDao tradeItemTCCDao;

	@Autowired
	OrderTCCLockDao orderTCCLockDao;

	@Autowired
	InvoiceInOrdDao invoiceInOrdDao;

	@Autowired
	InvoiceInOrdTCCDao invoiceInOrdTCCDao;

	@Autowired
	OrderRefundExpDao orderRefundExpDao;

	@Autowired
	CODAuditLogDao codAuditLogDao;

	@Autowired
	CODAuditLogTCCDao codAuditLogTCCDao;

	@Autowired
	OrderPackageDao orderPackageDao;

	// Service--------------------

	@Autowired
	OrderService orderService;

	@Autowired
	TradeService tradeService;

	@Autowired
	CODAuditService codAuditService;

	@Autowired
	OrderInstantiationUtil orderInstantiationUtil;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 下单逻辑里,需要迁移TCC数据到主Meta的Dao关系
	 */
	private List<MoveTCCToMetaInDBParam> moveTCCToMetaInDBForAddOrderByTCCList = new ArrayList<>();

	/**
	 * 下单逻辑里,需要清除的TCCDao列表
	 */
	private List<TCCDaoInterface<?>> deleteTCCInDBForAddOrderByTCCList = new ArrayList<>();

	/**
	 * 取消订单逻辑里,需要迁移TCC数据到主Meta的Dao关系
	 */
	private List<MoveTCCToMetaInDBParam> moveTCCToMetaInDBForCallOffOrderByTCCList = new ArrayList<>();

	/**
	 * 取消订单逻辑里,需要清除的TCCDao列表
	 */
	private List<TCCDaoInterface<?>> deleteTCCInDBForCallOffOrderByTCCList = new ArrayList<>();

	@PostConstruct
	public void init() {
		// 初始化:下单逻辑里,需要迁移TCC数据到主Meta的Dao关系
		moveTCCToMetaInDBForAddOrderByTCCList.add(new MoveTCCToMetaInDBParam(tradeItemTCCDao, tradeItemDao));
		moveTCCToMetaInDBForAddOrderByTCCList.add(new MoveTCCToMetaInDBParam(orderFormTCCDao, orderFormDao));
		moveTCCToMetaInDBForAddOrderByTCCList
				.add(new MoveTCCToMetaInDBParam(invoiceInOrdTCCDao, invoiceInOrdDao, false));
		moveTCCToMetaInDBForAddOrderByTCCList.add(new MoveTCCToMetaInDBParam(orderExpInfoTCCDao, orderExpInfoDao));
		moveTCCToMetaInDBForAddOrderByTCCList.add(new MoveTCCToMetaInDBParam(orderCartItemTCCDao, orderCartItemDao));
		moveTCCToMetaInDBForAddOrderByTCCList.add(new MoveTCCToMetaInDBParam(orderSkuCartItemTCCDao,
				orderSkuCartItemDao, false));
		moveTCCToMetaInDBForAddOrderByTCCList.add(new MoveTCCToMetaInDBParam(orderSkuTCCDao, orderSkuDao));
		// moveTCCToMetaInDBForAddOrderByTCCList.add(new
		// MoveTCCToMetaInDBParam(orderPOInfoTCCDao, orderPOInfoDao));
		// moveTCCToMetaInDBForAddOrderByTCCList.add(new
		// MoveTCCToMetaInDBParam(codAuditLogTCCDao, codAuditLogDao, false));

		// 初始化:下单逻辑里,需要清除的TCCDao列表
		for (MoveTCCToMetaInDBParam moveTCCToMetaInDBParam : moveTCCToMetaInDBForAddOrderByTCCList)
			deleteTCCInDBForAddOrderByTCCList.add(moveTCCToMetaInDBParam.tccDao);
		deleteTCCInDBForAddOrderByTCCList.add(orderTCCLockDao);
		deleteTCCInDBForAddOrderByTCCList.add(skuOrderStockTCCDao);

		// 初始化: 取消订单逻辑里,需要迁移TCC数据到主Meta的Dao关系
		moveTCCToMetaInDBForCallOffOrderByTCCList.add(new MoveTCCToMetaInDBParam(orderCancelInfoTCCDao,
				orderCancelInfoDao));

		// 初始化: 取消订单逻辑里,需要清除的TCCDao列表
		for (MoveTCCToMetaInDBParam moveTCCToMetaInDBParam : moveTCCToMetaInDBForCallOffOrderByTCCList)
			deleteTCCInDBForCallOffOrderByTCCList.add(moveTCCToMetaInDBParam.tccDao);
		deleteTCCInDBForCallOffOrderByTCCList.add(orderTCCLockDao);
		deleteTCCInDBForCallOffOrderByTCCList.add(updateOrderStateTCCDao);
		// deleteTCCInDBForCallOffOrderByTCCList.add(updatePackageStateTCCDao);
		deleteTCCInDBForCallOffOrderByTCCList.add(skuOrderStockTCCDao);
	}

	/**
	 * MoveTCCToMetaInDB方法的参数
	 * 
	 * @author dingmingliang
	 * 
	 */
	@SuppressWarnings("rawtypes")
	static class MoveTCCToMetaInDBParam {

		TCCDaoInterface tccDao;

		AbstractDao metaDao;

		/**
		 * tccList结果不允许为null
		 */
		boolean isTccListNotNull = true;

		/**
		 * 默认isTccListNotNull=true
		 * 
		 * @param tccDao
		 * @param metaDao
		 */
		MoveTCCToMetaInDBParam(TCCDaoInterface tccDao, AbstractDao metaDao) {
			this(tccDao, metaDao, true);
		}

		/**
		 * @param tccDao
		 * @param metaDao
		 * @param isTccListNotNull
		 */
		MoveTCCToMetaInDBParam(TCCDaoInterface tccDao, AbstractDao metaDao, boolean isTccListNotNull) {
			this.tccDao = tccDao;
			this.metaDao = metaDao;
			this.isTccListNotNull = isTccListNotNull;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#tryAddOrderByTCC(long,
	 *      com.xyl.mmall.order.param.TryAddOrderByTCCParam)
	 */
	// @Transaction
	// public TryAddOrderByTCCResult tryAddOrderByTCC(long tranId,
	// TryAddOrderByTCCParam param) {
	// // 0.参数准备
	// TryAddOrderByTCCResult result = new TryAddOrderByTCCResult();
	// param.setResult(result);
	// OrderFormCalDTO orderFormCalDTO = param.getOrderFormCalDTO();
	// long orderId = orderFormCalDTO.getOrderId(), userId = param.getUserId(),
	// currTime = System.currentTimeMillis();
	// TCCMetaExam tccExam = new TCCMetaExam();
	// tccExam.setCtimeOfTCC(currTime);
	// tccExam.setTranId(tranId);
	// // 参数有效性判断
	// if (orderId <= 0) {
	// result.setResultCodeWhenFirst(0);
	// return result;
	// }
	//
	// // 1.根据订单的OrderSku明细,生成Sku库存TCC数据
	// boolean isAddStockCount = true;
	// Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap =
	// OrderUtil.genSkuOrderStockTCCMap(orderFormCalDTO, currTime,
	// tranId, isAddStockCount);
	// // 2.更新库存数据SkuOrderStock
	// boolean isSucc = true;
	// if (isSucc) {
	// isSucc = isSucc && updateSkuOrderStockByTCC(skuOrderStockTCCMap.values(),
	// true);
	// if (!isSucc)
	// result.setResultCodeWhenFirst(2);
	// }
	//
	// // 3.添加OrderTCCLock
	// if (isSucc) {
	// OrderTCCLock orderTCCLock = OrderTCCLock.genOrderTCCLock(tranId,
	// currTime,
	// OrderTCCLockType.ADD_ORDER,
	// orderId);
	// isSucc = isSucc && orderTCCLockDao.addObject(orderTCCLock) != null;
	// }
	// // 4.添加TradeItemTCC
	// if (isSucc) {
	// List<TradeItemDTO> tradeDTOList = genTradeDTOList(currTime,
	// orderFormCalDTO);
	// List<TradeItemTCC> tradeTCCList =
	// TCCUtil.convertToTCCMetaList(TradeItemTCC.class, tradeDTOList, tccExam);
	// isSucc = isSucc && tradeItemTCCDao.addObjects(tradeTCCList);
	// }
	//
	// //
	// 5.添加订单相关数据(OrderFormTCC,OrderPOInfoTCC,InvoiceInOrdTCC,OrderExpInfoTCC,OrderCartItem,OrderSkuCartItem,OrderSku,CODAuditLog)
	// isSucc = isSucc && addOrderFormTCCInfoToDB(tccExam, orderFormCalDTO,
	// skuOrderStockTCCMap);
	//
	// // 6.添加SkuOrderStockTCC
	// isSucc = isSucc &&
	// skuOrderStockTCCDao.addObjects(skuOrderStockTCCMap.values());
	//
	// // 各种异常返回值
	// if (!isSucc) {
	// result.setResultCodeWhenFirst(0);
	// throw
	// OrderTCCServiceException.genOrderTCCServiceException("tryAddOrderByTCC fail!",
	// tranId, userId, null);
	// }
	// result.setResultCodeWhenFirst(1);
	// return result;
	//
	// }

	@Transaction
	@OperateLog
	public TryAddOrderByTCCResult tryAddOrderByTCC(long tranId, Map<Long, TryAddOrderByTCCParam> paramMap) {
		// 0.参数准备
		logger.info("####################OrderTCCServiceInternalImpl开始创建order####################");
		TryAddOrderByTCCResult result = new TryAddOrderByTCCResult();
		long currTime = System.currentTimeMillis();
		boolean isSucc = true;
		long parentId = -1;
		long userId = -1;
		TCCMetaExam tccExam = new TCCMetaExam();
		tccExam.setCtimeOfTCC(currTime);
		tccExam.setTranId(tranId);
		Map<Long, Map<Long, SkuOrderStockTCC>> skuOrderStockTCCMapMap = new HashMap<Long, Map<Long, SkuOrderStockTCC>>();

		for (Map.Entry<Long, TryAddOrderByTCCParam> entry : paramMap.entrySet()) {
			TryAddOrderByTCCParam param = entry.getValue();
			param.setResult(result);
			OrderFormCalDTO orderFormCalDTO = param.getOrderFormCalDTO();
			parentId = orderFormCalDTO.getParentId();
			long orderId = orderFormCalDTO.getOrderId();
			userId = param.getUserId();
			// 记录代客下单日志
			if (orderFormCalDTO.getAgentId() > 0) {
				logger.info("代客下单:操作者{}，指代的小B{}", orderFormCalDTO.getAgentId(), orderFormCalDTO.getUserId());
			}
			// 参数有效性判断
			if (orderId <= 0) {
				logger.info("orderId: " + orderId + ", bad parameters");
				result.setResultCodeWhenFirst(0);
				return result;
			}

			// 1.根据订单的OrderSku明细,生成Sku库存TCC数据
			boolean isAddStockCount = true;
			Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap = OrderUtil.genSkuOrderStockTCCMap(orderFormCalDTO,
					currTime, tranId, isAddStockCount);
			logger.info("####################订单的OrderSku明细,生成Sku库存TCC数据完成order####################");
			// 2.更新库存数据SkuOrderStock
			if (isSucc) {
				isSucc = isSucc && updateSkuOrderStockByTCC(skuOrderStockTCCMap.values(), true);
				if (!isSucc) {
					logger.info("####################库存不足，订单生成失败，返回错误码：2####################");
					result.setResultCodeWhenFirst(2);
					return result;
				}
			}
			logger.info("####################更新库存数据SkuOrderStock完成####################");
			skuOrderStockTCCMapMap.put(entry.getKey(), skuOrderStockTCCMap);
		}

		// 3.添加OrderTCCLock
		if (isSucc) {
			OrderTCCLock orderTCCLock = OrderTCCLock.genOrderTCCLock(tranId, currTime, OrderTCCLockType.ADD_ORDER,
					parentId);
			isSucc = isSucc && orderTCCLockDao.addObject(orderTCCLock) != null;
			if (!isSucc) {
				logger.info("####################添加OrderTCCLock失败####################");
			}
		}
		logger.info("####################添加OrderTCCLock完成####################");
		for (Map.Entry<Long, TryAddOrderByTCCParam> entry : paramMap.entrySet()) {
			TryAddOrderByTCCParam param = entry.getValue();
			OrderFormCalDTO orderFormCalDTO = param.getOrderFormCalDTO();

			// 4.添加TradeItemTCC
			if (isSucc) {
				List<TradeItemDTO> tradeDTOList = genTradeDTOList(currTime, orderFormCalDTO);
				List<TradeItemTCC> tradeTCCList = TCCUtil.convertToTCCMetaList(TradeItemTCC.class, tradeDTOList,
						tccExam);
				isSucc = isSucc && tradeItemTCCDao.addObjects(tradeTCCList);
				if (!isSucc) {
					logger.info("####################添加TradeItemTCC失败####################");
				}
			}
			logger.info("####################添加TradeItemTCC完成####################");
			// 5.添加订单相关数据(OrderFormTCC,OrderPOInfoTCC,InvoiceInOrdTCC,OrderExpInfoTCC,OrderCartItem,OrderSkuCartItem,OrderSku,CODAuditLog)
			isSucc = isSucc
					&& addOrderFormTCCInfoToDB(tccExam, orderFormCalDTO, skuOrderStockTCCMapMap.get(entry.getKey()));
			if (!isSucc) {
				logger.info("####################添加订单相关数据失败####################");
				logger.info("OrderFormCalDTO信息如下：" + ReflectUtil.genToString(orderFormCalDTO));
			}
			logger.info("####################添加订单相关数据完成####################");
			// 6.添加SkuOrderStockTCC
			isSucc = isSucc && skuOrderStockTCCDao.addObjects(skuOrderStockTCCMapMap.get(entry.getKey()).values());
			if (!isSucc) {
				logger.info("####################添加SkuOrderStockTCC失败####################");
			}
		}

		logger.info("####################添加SkuOrderStockTCC完成####################");
		// 各种异常返回值
		if (!isSucc) {
			logger.info("####################艹尼玛，什么鬼，你妹啊！！！！！！！！！！！！你绝壁是欣总在测试。####################");
			result.setResultCodeWhenFirst(0);
			throw OrderTCCServiceException.genOrderTCCServiceException("tryAddOrderByTCC fail!", tranId, userId, null);
		}
		result.setResultCodeWhenFirst(1);
		return result;
	}

	/**
	 * 添加订单相关数据(OrderFormTCC,OrderPOInfoTCC,InvoiceInOrdTCC,OrderExpInfoTCC,
	 * OrderCartItem,OrderSkuCartItem,OrderSku)
	 * 
	 * @param tccExam
	 * @param orderFormCalDTO
	 * @param skuOrderStockTCCMap
	 * @return
	 */
	private boolean addOrderFormTCCInfoToDB(TCCMetaExam tccExam, OrderFormCalDTO orderFormCalDTO,
			Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap) {
		long orderId = orderFormCalDTO.getOrderId(), currTime = System.currentTimeMillis();
		boolean isSucc = true;
		// 1.添加OrderFormTCC
		if (isSucc) {
			OrderFormTCC orderFormTCC = TCCUtil.convertToTCCMeta(OrderFormTCC.class, orderFormCalDTO, tccExam);
			isSucc = isSucc && orderFormTCCDao.addObject(orderFormTCC) != null;
		}
		// 2.添加InvoiceInOrdTCC
		if (isSucc) {
			InvoiceInOrdDTO invoiceInOrdDTO = orderFormCalDTO.getInvoiceInOrdDTO();
			if (invoiceInOrdDTO != null) {
				invoiceInOrdDTO.setOrderId(orderId);
				InvoiceInOrdTCC invoiceInOrdTCC = TCCUtil.convertToTCCMeta(InvoiceInOrdTCC.class, invoiceInOrdDTO,
						tccExam);
				isSucc = isSucc && invoiceInOrdTCCDao.addObject(invoiceInOrdTCC) != null;
			}
		}
		// 3.OrderExpInfoTCC
		if (isSucc) {
			OrderExpInfoDTO orderExpInfoDTO = orderFormCalDTO.getOrderExpInfoDTO();
			orderExpInfoDTO.setOrderId(orderId);
			OrderExpInfoTCC orderExpInfoTCC = TCCUtil.convertToTCCMeta(OrderExpInfoTCC.class, orderExpInfoDTO, tccExam);
			isSucc = isSucc && orderExpInfoTCCDao.addObject(orderExpInfoTCC) != null;
		}
		// 4.添加CODAuditLog
		// isSucc = isSucc && addCODAuditLogTCCTODB(tccExam, orderFormCalDTO,
		// currTime);
		// 5.添加OrderPOInfoTCC
		// isSucc = isSucc && addOrderPOInfoTCCToDB(tccExam, orderFormCalDTO,
		// skuOrderStockTCCMap);

		// 6.添加OrderCartItemTCC,OrderSkuCartItemTCC,OrderSkuTCC
		isSucc = isSucc && addOrderCartItemTCCListInfoToDB(tccExam, orderFormCalDTO);
		return isSucc;
	}

	/**
	 * 添加CODAuditLogTCCT到DB
	 * 
	 * @param tccExam
	 * @param orderFormCalDTO
	 * @param currTime
	 * @return
	 */
	private boolean addCODAuditLogTCCTODB(TCCMetaExam tccExam, OrderFormCalDTO orderFormCalDTO, long currTime) {
		if (orderFormCalDTO.getOrderFormPayMethod() != OrderFormPayMethod.COD)
			return true;

		long orderId = orderFormCalDTO.getOrderId(), userId = orderFormCalDTO.getUserId();
		int provinceId = orderFormCalDTO.getProvinceId();
		boolean isSucc = true;
		CODAuditLog codAuditLog = new CODAuditLog();
		codAuditLog.setId(codAuditLogDao.allocateRecordId());
		codAuditLog.setCtime(currTime);
		codAuditLog.setOrderId(orderId);
		codAuditLog.setUserId(userId);
		codAuditLog.setProvinceId(provinceId);
		codAuditLog.setAuditState(CODAuditState.WAITING);
		codAuditLog.setExtInfo("null");
		CODAuditLogTCC codAuditLogTCC = TCCUtil.convertToTCCMeta(CODAuditLogTCC.class, codAuditLog, tccExam);
		isSucc = isSucc && codAuditLogTCCDao.addObject(codAuditLogTCC) != null;

		return isSucc;
	}

	/**
	 * 添加OrderPOInfoTCC数据到DB里
	 * 
	 * @param tccExam
	 * @param orderFormCalDTO
	 * @param skuOrderStockTCCMap
	 * @return
	 */
	private boolean addOrderPOInfoTCCToDB(TCCMetaExam tccExam, OrderFormCalDTO orderFormCalDTO,
			Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap) {
		long orderId = orderFormCalDTO.getOrderId(), userId = orderFormCalDTO.getUserId(), orderTime = orderFormCalDTO
				.getOrderTime();
		boolean isSucc = true;
		for (SkuOrderStockTCC skuOrderStockTCC : skuOrderStockTCCMap.values()) {
			if (!isSucc)
				break;
			long id = orderFormDao.allocateRecordId();
			if (id < 1l) {
				throw new DBSupportRuntimeException("Get generateId failed!");
			}
			OrderPOInfo orderPOInfo = new OrderPOInfo();
			orderPOInfo.setId(id);
			orderPOInfo.setOrderId(orderId);
			orderPOInfo.setOrderTime(orderTime);
			orderPOInfo.setPoId(skuOrderStockTCC.getPoId());
			orderPOInfo.setUserId(userId);
			OrderPOInfoTCC orderPOInfoTCC = TCCUtil.convertToTCCMeta(OrderPOInfoTCC.class, orderPOInfo, tccExam);
			isSucc = isSucc && orderPOInfoTCCDao.addObject(orderPOInfoTCC) != null;
		}
		return isSucc;
	}

	/**
	 * 根据TCC数据,更新库存数据SkuOrderStock
	 * 
	 * @param skuOrderStockTCCColl
	 * @return
	 */
	private boolean updateSkuOrderStockByTCC(Collection<SkuOrderStockTCC> skuOrderStockTCCColl) {
		boolean isReversed = false;
		return updateSkuOrderStockByTCC(skuOrderStockTCCColl, isReversed);
	}

	/**
	 * 根据TCC数据,更新库存数据SkuOrderStock
	 * 
	 * @param skuOrderStockTCCColl
	 * @param isReversed
	 *            是否反转skuOrderStockTCC.isAddStockCount标记
	 * @return
	 */
	private boolean updateSkuOrderStockByTCC(Collection<SkuOrderStockTCC> skuOrderStockTCCColl, boolean isReversed) {
		boolean isSucc = true;
		if (CollectionUtil.isEmptyOfCollection(skuOrderStockTCCColl))
			return isSucc;

		for (SkuOrderStockTCC skuOrderStockTCC : skuOrderStockTCCColl) {
			// 生成SkuOrderStock
			long skuId = skuOrderStockTCC.getSkuId();
			SkuOrderStock skuOrderStock = new SkuOrderStock();
			skuOrderStock.setSkuId(skuId);
			boolean isAdd = isReversed ? !skuOrderStockTCC.isAddStockCount() : skuOrderStockTCC.isAddStockCount();
			// 设置增量更新数据
			IncrField<Integer> ifItem = new IncrField<Integer>("stockCount", isAdd ? skuOrderStockTCC.getStockCount()
					: -skuOrderStockTCC.getStockCount());
			if (isAdd)
				ifItem.setResultPlus(true);
			isSucc = isSucc && skuOrderStockDao.updateSaleCount(skuOrderStock, CollectionUtil.addOfList(null, ifItem));
		}
		return isSucc;
	}

	/**
	 * 添加OrderCartItemTCC,OrderSkuCartItemTCC,OrderSkuTCC
	 * 
	 * @param tccExam
	 * @param orderFormCalDTO
	 * @return
	 */
	// private boolean addOrderCartItemTCCListInfoToDB(TCCMetaExam tccExam,
	// OrderFormCalDTO orderFormCalDTO) {
	// boolean isSucc = true;
	// long orderId = orderFormCalDTO.getOrderId();
	// for (OrderPackageDTO orderPackageDTO :
	// orderFormCalDTO.getOrderPackageDTOList()) {
	// // 1.设置OrderPackage上的字段
	// long packageId = 0L;
	// orderPackageDTO.setOrderId(orderId);
	// orderPackageDTO.setPackageId(packageId);
	//
	// // 2.添加OrderCartItem,OrderSkuCartItem,OrderSku
	// isSucc = isSucc && addOrderCartItemTCCListInfoToDB(tccExam,
	// orderPackageDTO);
	// }
	// return isSucc;
	// }

	/**
	 * 添加OrderCartItemTCC,OrderSkuCartItemTCC,OrderSkuTCC
	 * 
	 * @param tccExam
	 * @param orderPackageDTO
	 * @return
	 */
	private boolean addOrderCartItemTCCListInfoToDB(TCCMetaExam tccExam, OrderFormCalDTO orderFormCalDTO) {
		boolean isSucc = true;
		long orderId = orderFormCalDTO.getOrderId();

		for (OrderCartItemDTO orderCartItemDTO : orderFormCalDTO.getOrderCartItemDTOList()) {
			// 1.添加OrderCartItem
			long cartId = -1L;
			if (isSucc) {
				cartId = orderFormDao.allocateRecordId();
			}
			orderCartItemDTO.setId(cartId);
			orderCartItemDTO.setOrderId(orderId);
			// orderCartItemDTO.setPackageId(packageId);
			OrderCartItemTCC orderCartItemTCC = TCCUtil.convertToTCCMeta(OrderCartItemTCC.class, orderCartItemDTO,
					tccExam);
			isSucc = isSucc && orderCartItemTCCDao.addObject(orderCartItemTCC) != null;
			// 2.添加OrderSkuCartItem
			if (orderCartItemDTO.getCartType() == CartItemType.CART_SKU) {
				OrderSkuCartItem orderSkuCartItem = orderCartItemDTO.getOrderSkuCartItemDTO();
				orderSkuCartItem.setOrderCartItemId(cartId);
				OrderSkuCartItemTCC orderSkuCartItemTCC = TCCUtil.convertToTCCMeta(OrderSkuCartItemTCC.class,
						orderSkuCartItem, tccExam);
				isSucc = isSucc && orderSkuCartItemTCCDao.addObject(orderSkuCartItemTCC) != null;
			}
			// 3.添加OrderSku
			isSucc = isSucc && addOrderSkuTCCListToDB(tccExam, orderCartItemDTO);
		}
		return isSucc;
	}

	/**
	 * 添加OrderSkuTCC
	 * 
	 * @param tccExam
	 * @param orderCartItemDTO
	 * @return
	 */
	private boolean addOrderSkuTCCListToDB(TCCMetaExam tccExam, OrderCartItemDTO orderCartItemDTO) {
		boolean isSucc = true;
		long orderId = orderCartItemDTO.getOrderId(), packageId = orderCartItemDTO.getPackageId(), cartId = orderCartItemDTO
				.getId();

		// 添加OrderSku
		for (OrderSkuDTO orderSkuDTO : orderCartItemDTO.getOrderSkuDTOList()) {
			long orderSkuId = -1L;
			if (isSucc) {
				orderSkuId = orderFormDao.allocateRecordId();
			}
			orderSkuDTO.setId(orderSkuId);
			orderSkuDTO.setOrderCartItemId(cartId);
			orderSkuDTO.setOrderId(orderId);
			orderSkuDTO.setPackageId(packageId);

			OrderSkuTCC orderSkuTCC = TCCUtil.convertToTCCMeta(OrderSkuTCC.class, orderSkuDTO, tccExam);
			isSucc = isSucc && orderSkuTCCDao.addObject(orderSkuTCC) != null;
		}
		return isSucc;
	}

	/**
	 * 根据orderFormCalDTO,生成对应的TradeItemDTO的集合
	 * 
	 * @param currTime
	 * @param orderFormCalDTO
	 * @return
	 */
	private List<TradeItemDTO> genTradeDTOList(long currTime, OrderFormCalDTO orderFormCalDTO) {
		List<TradeItemDTO> tradeDTOList = new ArrayList<>();
		// 1.生成一个TradeItemDTO样本
		TradeItemDTO tradeDTOOfExam = genTradeItemDTOOfExam(currTime, orderFormCalDTO);

		// 2.计算订单对应的实付金额
		BigDecimal realCash = orderFormCalDTO.getRealCash();
		boolean isRealCashZero = OrderApiUtil.isRealCashZero(orderFormCalDTO);

		// 3.根据订单支付方式+支付金额,生成交易TCC
		OrderFormPayMethod orderFormPayMethod = orderFormCalDTO.getOrderFormPayMethod();
		// 从数据库里,分配TradeId
		long tradeId = tradeItemDao.allocateRecordId();
		if (tradeId < 1l) {
			throw new DBSupportRuntimeException("Get generateId failed!");
		}

		TradeItemDTO tradeDTO = ReflectUtil.cloneObj(tradeDTOOfExam);
		tradeDTO.setCash(realCash);
		tradeDTO.setTradeId(tradeId);
		tradeDTO.setPayState(orderFormCalDTO.getPayState());
		// 设置支付方式
		if (isRealCashZero) {
			// CASE1: 0元订单
			tradeDTO.setTradeItemPayMethod(TradeItemPayMethod.ZERO);
			tradeDTO.setPayTime(orderFormCalDTO.getPayTime());
		} else if (orderFormPayMethod == OrderFormPayMethod.EPAY) {
			// CASE2: 网易宝支付
			tradeDTO.setTradeItemPayMethod(TradeItemPayMethod.EPAY);
		} else if (orderFormPayMethod == OrderFormPayMethod.COD) {
			// CASE3: 货到付款支付
			tradeDTO.setTradeItemPayMethod(TradeItemPayMethod.COD);
		} else if (orderFormPayMethod == OrderFormPayMethod.ALIPAY) {
			// CASE4: 支付宝
			tradeDTO.setTradeItemPayMethod(TradeItemPayMethod.ALIPAY);
		}
		tradeDTOList.add(tradeDTO);

		return tradeDTOList;
	}

	/**
	 * 根据orderFormCalDTO,生成一个TradeItemDTO的样本对象
	 * 
	 * @param tccExam
	 * @param orderFormCalDTO
	 * @return
	 */
	private TradeItemDTO genTradeItemDTOOfExam(long ctime, OrderFormCalDTO orderFormCalDTO) {
		TradeItemDTO tradeDTO = new TradeItemDTO();
		tradeDTO.setCtime(ctime);
		tradeDTO.setOrderId(orderFormCalDTO.getOrderId());
		tradeDTO.setSpSource(orderFormCalDTO.getSpSource());
		tradeDTO.setUserId(orderFormCalDTO.getUserId());
		tradeDTO.setParentId(orderFormCalDTO.getParentId());
		return tradeDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#confirmAddOrderByTCC(long)
	 */
	@Transaction
	@OperateLog
	public boolean confirmAddOrderByTCC(long tranId) {
		// 0.获得OrderTCCLock的记录锁
		OrderTCCLock tccLock = orderTCCLockDao.getLockByKey(OrderTCCLock.genOrderTCCLock(tranId));
		if (tccLock == null) {
			logger.info("OrderTCCLock==null, tranId=" + tranId);
			return true;
		}
		if (tccLock.getType() != OrderTCCLockType.ADD_ORDER) {
			logger.info("OrderTCCLock.type invalid, tranId=" + tranId);
			return false;
		}

		// 1.读取订单数据
		long userId = -1, orderId = -1;
		OrderFormTCC orderTCC = CollectionUtil.getFirstObjectOfCollection(orderFormTCCDao.getListByTranId(tranId));
		userId = orderTCC != null ? orderTCC.getUserId() : userId;
		orderId = orderTCC != null ? orderTCC.getOrderId() : orderId;
		boolean isSucc = orderId > 0;
		logger.info("####################读取订单完成####################");

		// 2.将TCC对象,转移到主Meta对象上(TradeItemTCC,OrderFormTCC,InvoiceInOrdTCC,OrderExpInfoTCC,OrderCartItem,OrderSkuCartItem,OrderSku)
		for (MoveTCCToMetaInDBParam moveTCCToMetaInDBParam : moveTCCToMetaInDBForAddOrderByTCCList) {
			isSucc = isSucc && moveTCCToMetaInDB(moveTCCToMetaInDBParam, tranId);
		}
		logger.info("####################将TCC对象,转移到主Meta对象上完成####################");
		// 3.删除所有TCC数据
		isSucc = isSucc && deleteTCCInDB(deleteTCCInDBForAddOrderByTCCList, tranId);
		logger.info("####################删除所有TCC数据结果：" + isSucc + "####################");

		// 返回
		if (!isSucc)
			throw OrderTCCServiceException.genOrderTCCServiceException("confirmAddOrderByTCC fail!", tranId, userId,
					orderId);
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#cancelAddOrderByTCC(long)
	 */
	@Transaction
	public boolean cancelAddOrderByTCC(long tranId) {
		// 0.获得OrderTCCLock的记录锁
		OrderTCCLock tccLock = orderTCCLockDao.getLockByKey(OrderTCCLock.genOrderTCCLock(tranId));
		if (tccLock == null) {
			logger.info("OrderTCCLock==null, tranId=" + tranId);
			return true;
		}
		if (tccLock.getType() != OrderTCCLockType.ADD_ORDER) {
			logger.info("OrderTCCLock.type invalid, tranId=" + tranId);
			return false;
		}

		// 1.读取订单数据
		long userId = -1, orderId = -1;
		OrderFormTCC orderTCC = CollectionUtil.getFirstObjectOfCollection(orderFormTCCDao.getListByTranId(tranId));
		userId = orderTCC != null ? orderTCC.getUserId() : userId;
		orderId = orderTCC != null ? orderTCC.getOrderId() : orderId;
		boolean isSucc = orderId > 0;
		logger.info("###################读取订单数据完成####################");
		// 2.修改SkuOrderStock
		if (isSucc) {
			// 1.1 读取tranId对应的SkuOrderStockTCC数据
			List<SkuOrderStockTCC> skuOrderStockTCCList = skuOrderStockTCCDao.getListByTranId(tranId);
			// 1.2 修改订单库存
			isSucc = isSucc && updateSkuOrderStockByTCC(skuOrderStockTCCList);
		}
		logger.info("###################修改SkuOrderStock完成####################");
		// 3.删除所有TCC数据
		isSucc = isSucc && deleteTCCInDB(deleteTCCInDBForAddOrderByTCCList, tranId);
		logger.info("###################删除所有TCC数据结果：" + isSucc + "####################");
		if (!isSucc)
			throw OrderTCCServiceException.genOrderTCCServiceException("cancelAddOrderByTCC fail!", tranId, null, null);
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#tryCallOffOrderByTCC(long,
	 *      com.xyl.mmall.order.dto.OrderCancelInfoDTO, boolean)
	 */
	@Transaction
	@OperateLog
	public boolean tryCallOffOrderByTCC(long tranId, List<OrderCancelInfoDTO> orderCancelInfoDTOs,
			boolean canRecyleStock) {
		long currTime = System.currentTimeMillis();
		boolean isSucc = true;
		int countflag = 0;
		for (OrderCancelInfoDTO orderCancelInfoDTO : orderCancelInfoDTOs) {
			++countflag;
			// 1.读取订单数据
			// 获得订单的记录锁
			long userId = orderCancelInfoDTO.getUserId(), orderId = orderCancelInfoDTO.getOrderId();
			RetArg retArgOfIsContinue = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
			Boolean isContinue = RetArgUtil.get(retArgOfIsContinue, Boolean.class);
			if (isContinue != Boolean.TRUE) {
				logger.info("order already in TCC");
				return false;
			}
			// 读取订单的数据
			OrderFormDTO orderFormDTO = orderService.queryOrderForm(userId, orderId, null);
			if (orderFormDTO == null) {
				return false;
			}

			if (!orderCancelInfoDTO.getOperateUserType().equals(OperateUserType.CMSER)
					&& (OrderFormPayMethod.isOnlinePayMethod(orderFormDTO.getOrderFormPayMethod()) && !OrderFormState.WAITING_PAY
							.equals(orderFormDTO.getOrderFormState()))
					&& (!OrderFormPayMethod.isOnlinePayMethod(orderFormDTO.getOrderFormPayMethod()) && !OrderFormState.WAITING_DELIVE
							.equals(orderFormDTO.getOrderFormState()))) {
				logger.info("orderFormDTO==null || state invalid, tranId=" + tranId + ", orderId=" + orderId
						+ ", userId=" + userId);
				return false;
			}
			TCCMetaExam tccExam = new TCCMetaExam();
			tccExam.setCtimeOfTCC(currTime);
			tccExam.setTranId(tranId);

			// 2.生成UpdateOrderStateTCC对象和UpdatePackgeStateTCC对象
			// 2.1 生成UpdateOrderStateTCC对象
			UpdateOrderStateTCC updateOrderStateTCC = genUpdateOrderStateTCC(orderFormDTO, orderCancelInfoDTO, tranId,
					currTime);
			OrderFormState oldOFState = updateOrderStateTCC.getOriOState();
			// 2.2 生成UpdatePackgeStateTCC对象
			// List<UpdatePackageStateTCC> updatePackageStateTCCList =
			// genUpdatePackageStateTCCList(orderFormDTO, tranId,
			// currTime);

			// 3.更新订单和包裹状态
			// 3.1 更新订单状态: 取消中
			if (isSucc) {
				orderFormDTO.setOrderFormState(updateOrderStateTCC.getNewOState());
				isSucc = isSucc && orderFormDao.updateOrdState(orderFormDTO, new OrderFormState[] { oldOFState });
			}
			// 3.2 尝试更新包裹状态
			// if (isSucc &&
			// CollectionUtil.isNotEmptyOfCollection(updatePackageStateTCCList))
			// {
			// Map<Long, UpdatePackageStateTCC> updatePackageStateTCCMap =
			// CollectionUtil.convertCollToMap(
			// updatePackageStateTCCList, "packageId");
			// for (OrderPackageDTO packageDTO :
			// orderFormDTO.getOrderPackageDTOList()) {
			// UpdatePackageStateTCC updatePackageStateTCC =
			// updatePackageStateTCCMap.get(packageDTO.getPackageId());
			// packageDTO.setOrderPackageState(updatePackageStateTCC.getNewState());
			// isSucc = isSucc
			// &&
			// orderPackageDao.updateOrderPackageStateAndCancelTime(packageDTO,
			// updatePackageStateTCC.getOriState());
			// }
			// }

			// 4.添加订单锁OrderTCCLock,订单状态UpdateOrderStateTCC,订单取消原因OrderCancelInfoTCC,销售数据SkuOrderStockTCC
			if (isSucc) {
				if (countflag == 1) {
					// 4.1 添加订单锁OrderTCCLock
					OrderTCCLock orderTCCLock = OrderTCCLock.genOrderTCCLock(tranId, currTime,
							OrderTCCLockType.CALLOFF_ORDER, orderFormDTO.getParentId());
					isSucc = isSucc && orderTCCLockDao.addObject(orderTCCLock) != null;
				}
				// 4.2 添加订单状态UpdateOrderStateTCC
				isSucc = isSucc && updateOrderStateTCCDao.addObject(updateOrderStateTCC) != null;
				// 4.3 添加订单状态UpdatePackageStateTCC
				// if
				// (CollectionUtil.isNotEmptyOfCollection(updatePackageStateTCCList))
				// isSucc = isSucc &&
				// updatePackageStateTCCDao.addObjects(updatePackageStateTCCList);

				// 4.4 添加订单取消原因OrderCancelInfoTCC
				// 生成后续要取消的异步任务标记位(1表示要处理)
				long retryFlag = 0;
				retryFlag = ExtInfoFieldUtil.setValueOfType1(retryFlag, OrderCancelInfoTCC.IDX_CANCEL_TRADE, true);
				// TODO.DML feature0206 之后的版本需要去掉下面这段代码
				// Start-------
				// if (OrderApiUtil.needCancelOMSOrder(oldOFState) && oldOFState
				// != OrderFormState.WAITING_CANCEL_OMSORDER)
				// retryFlag = ExtInfoFieldUtil.setValueOfType1(retryFlag,
				// OrderCancelInfoTCC.IDX_CANCEL_OMSORDER, true);
				// End-------

				OrderCancelInfoTCC orderCancelInfoTCC = TCCUtil.convertToTCCMeta(OrderCancelInfoTCC.class,
						orderCancelInfoDTO, tccExam);
				orderCancelInfoTCC.setRetryFlag(retryFlag);
				orderCancelInfoTCC.setRetryCount(0);
				isSucc = isSucc && orderCancelInfoTCCDao.addObject(orderCancelInfoTCC) != null;
				// 4.5 添加销售数据SkuOrderStockTCC
				if (canRecyleStock) {
					boolean isAddStockCount = true;
					Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap = OrderUtil.genSkuOrderStockTCCMap(orderFormDTO,
							currTime, tranId, isAddStockCount);
					isSucc = isSucc && skuOrderStockTCCDao.addObjects(skuOrderStockTCCMap.values());
				}
			}

			// 返回结果
			if (!isSucc)
				throw OrderTCCServiceException.genOrderTCCServiceException("tryCallOffOrderByTCC fail!", tranId,
						userId, orderId);
		}
		return isSucc;
	}

	/**
	 * 生成UpdateOrderStateTCC对象
	 * 
	 * @param orderFormDTO
	 * @param orderCancelInfoDTO
	 * @param tranId
	 * @param currTime
	 * @return
	 */
	private UpdateOrderStateTCC genUpdateOrderStateTCC(OrderFormDTO orderFormDTO,
			OrderCancelInfoDTO orderCancelInfoDTO, long tranId, long currTime) {
		OrderFormState newOState = OrderFormState.CANCEL_ING;
		PayState newPState = orderFormDTO.getPayState();
		UpdateOrderStateTCC updateOrderStateTCC = new UpdateOrderStateTCC();
		updateOrderStateTCC.setCtimeOfTCC(currTime);
		updateOrderStateTCC.setNewOState(newOState);
		updateOrderStateTCC.setNewPState(newPState);
		updateOrderStateTCC.setOrderId(orderFormDTO.getOrderId());
		updateOrderStateTCC.setOriOState(orderFormDTO.getOrderFormState());
		updateOrderStateTCC.setOriPState(orderFormDTO.getPayState());
		updateOrderStateTCC.setTranId(tranId);
		updateOrderStateTCC.setUserId(orderFormDTO.getUserId());

		return updateOrderStateTCC;
	}

	/**
	 * 生成UpdatePackageStateTCC对象
	 * 
	 * @param orderFormDTO
	 * @param tranId
	 * @param currTime
	 * @return
	 */
	private List<UpdatePackageStateTCC> genUpdatePackageStateTCCList(OrderFormDTO orderFormDTO, long tranId,
			long currTime) {
		List<UpdatePackageStateTCC> list = new ArrayList<>();
		if (CollectionUtil.isEmptyOfCollection(orderFormDTO.getOrderPackageDTOList()))
			return list;

		boolean isUnpay = OrderApiUtil.isUnpayForOrderCancel(orderFormDTO);
		for (OrderPackageDTO packageDTO : orderFormDTO.getOrderPackageDTOList()) {
			if (packageDTO.getPackageId() <= 0)
				continue;
			UpdatePackageStateTCC obj = new UpdatePackageStateTCC();
			obj.setCtimeOfTCC(currTime);
			obj.setNewState(isUnpay ? OrderPackageState.CANCEL_OC_UNPAY : OrderPackageState.CANCEL_OC_PAYED);
			obj.setOriState(packageDTO.getOrderPackageState());
			obj.setPackageId(packageDTO.getPackageId());
			obj.setTranId(tranId);
			obj.setUserId(packageDTO.getUserId());
			list.add(obj);
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#confirmCallOffOrderByTCC(long)
	 */
	@OperateLog
	@Transaction
	public boolean confirmCallOffOrderByTCC(long tranId) {
		// 1.判断OrderTCCLock,获取OrderForm的记录锁
		// 1.1 获得OrderTCCLock的记录锁
		OrderTCCLock tccLock = orderTCCLockDao.getLockByKey(OrderTCCLock.genOrderTCCLock(tranId));
		if (tccLock == null) {
			logger.info("OrderTCCLock==null, tranId=" + tranId);
			return true;
		}
		if (tccLock.getType() != OrderTCCLockType.CALLOFF_ORDER) {
			logger.info("OrderTCCLock.type invalid, tranId=" + tranId);
			return false;
		}
		boolean isSucc = true;
		long userId = 0, orderId = 0;
		// 1.2 读取tranId相关的数据
		// 读取UpdateOrderStateTCC记录
		List<UpdateOrderStateTCC> updateOrderStateTCCs = updateOrderStateTCCDao.getListByTranId(tranId);
		for (UpdateOrderStateTCC updateOrderStateTCC : updateOrderStateTCCs) {
			orderId = updateOrderStateTCC.getOrderId();
			userId = updateOrderStateTCC.getUserId();
			// 1.3 获得OrderForm的记录锁
			OrderForm orderForm = new OrderForm();
			orderForm.setOrderId(orderId);
			orderForm.setUserId(userId);
			orderForm = orderFormDao.getLockByKey(orderForm);
			// 1.4 读取OrderCanceInfoTCC
			// OrderCancelInfoTCC orderCancelInfoTCC =
			// CollectionUtil.getFirstObjectOfCollection(orderCancelInfoTCCDao
			// .getListByTranId(tranId));

			// 2.更新订单状态:已取消
			if (isSucc) {
				orderForm.setOrderFormState(OrderFormState.CANCEL_ED);
				OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.CANCEL_ING,
						OrderFormState.WAITING_PAY };
				isSucc = isSucc && orderFormDao.updateOrdState(orderForm, oldStateArray);
			}
		}
		// 3.修改库存
		if (isSucc) {
			List<SkuOrderStockTCC> skuOrderStockTCCList = skuOrderStockTCCDao.getListByTranId(tranId);
			isSucc = isSucc && updateSkuOrderStockByTCC(skuOrderStockTCCList);
		}
		// 4.尝试添加订单运费退款记录
		// if (isSucc) {
		// try {
		// OrderRefundExp orderRefundExp =
		// OrderRefundExp.genInstance(orderForm);
		// if (orderRefundExpDao.addObject(orderRefundExp) == null) {
		// logger.error("orderRefundExpDao.addObject fail, orderId=" + orderId);
		// }
		// } catch (Exception ex) {
		// logger.error(ex.getMessage());
		// }
		// }

		// 5.将TCC对象,转移到主Meta对象上(OrderCancelInfoTCC->OrderCancelInfo)
		if (isSucc) {
			for (MoveTCCToMetaInDBParam moveTCCToMetaInDBParam : moveTCCToMetaInDBForCallOffOrderByTCCList) {
				isSucc = isSucc && moveTCCToMetaInDB(moveTCCToMetaInDBParam, tranId);
			}
		}
		// 6.删除TCC数据
		isSucc = isSucc && deleteTCCInDB(deleteTCCInDBForCallOffOrderByTCCList, tranId);

		// 7.更新到付审核请求的状态(如果失败也不处理)
		// if (isSucc) {
		// cancelCODAuditForConfirmCallOffOrderByTCC(orderForm,
		// orderCancelInfoTCC);
		// }

		// 返回
		if (!isSucc)
			throw OrderTCCServiceException.genOrderTCCServiceException("confirmCallOffOrderByTCC fail!", tranId,
					userId, orderId);
		return isSucc;
	}

	/**
	 * 更新到付审核请求的状态(如果失败也不处理)
	 * 
	 * @param orderForm
	 * @param orderCancelInfo
	 */
	private void cancelCODAuditForConfirmCallOffOrderByTCC(OrderForm orderForm, OrderCancelInfo orderCancelInfo) {
		long userId = orderForm.getUserId(), orderId = orderForm.getOrderId();
		if (orderForm.getOrderFormPayMethod() != OrderFormPayMethod.COD)
			return;

		try {
			String reason = orderCancelInfo != null ? orderCancelInfo.getReason() : "订单取消";
			List<CODAuditLogDTO> codLogs = codAuditService.queryCODAuditLogByUserIdAndOrderId(
					new CODAuditState[] { CODAuditState.WAITING }, userId, orderId);
			if (!CollectionUtil.isEmptyOfCollection(codLogs)) {
				for (CODAuditLogDTO codLog : codLogs) {
					if (null == codLog) {
						continue;
					}
					codAuditService.cancelCODLogOfWaitingAudit(codLog, reason);
				}
			}
		} catch (Exception e) {
			logger.info("取消等待审核的到付订单时，更新到付审核请求状态失败", e);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#cancelCallOffOrderByTCC(long)
	 */
	@Transaction
	public boolean cancelCallOffOrderByTCC(long tranId) {
		// 1.判断OrderTCCLock,获取OrderForm的记录锁
		// 1.1 获得OrderTCCLock的记录锁
		OrderTCCLock tccLock = orderTCCLockDao.getLockByKey(OrderTCCLock.genOrderTCCLock(tranId));
		if (tccLock == null) {
			logger.info("OrderTCCLock==null, tranId=" + tranId);
			return true;
		}
		if (tccLock.getType() != OrderTCCLockType.CALLOFF_ORDER) {
			logger.info("OrderTCCLock.type invalid, tranId=" + tranId);
			return false;
		}
		boolean isSucc = true;
		// 1.2 读取tranId相关的数据
		// 读取UpdateOrderStateTCC记录
		List<UpdateOrderStateTCC> updateOrderStateTCCs = updateOrderStateTCCDao.getListByTranId(tranId);
		for (UpdateOrderStateTCC updateOrderStateTCC : updateOrderStateTCCs) {
			long orderId = updateOrderStateTCC.getOrderId(), userId = updateOrderStateTCC.getUserId();
			// 读取UpdatePackageStateTCC记录
			// List<UpdatePackageStateTCC> updatePackageStateTCCList =
			// updatePackageStateTCCDao.getListByTranId(tranId);
			// 1.3 获得OrderForm的记录锁
			OrderForm orderForm = new OrderForm();
			orderForm.setOrderId(orderId);
			orderForm.setUserId(userId);
			orderForm = orderFormDao.getLockByKey(orderForm);

			// 2.恢复订单状态和包裹状态
			// 2.1 恢复订单状态
			if (isSucc) {
				OrderFormDTO orderFormDTO = new OrderFormDTO();
				orderFormDTO.setOrderId(orderId);
				orderFormDTO.setUserId(userId);
				orderFormDTO.setOrderFormState(updateOrderStateTCC.getOriOState());
				OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.CANCEL_ING,
						OrderFormState.CANCEL_ED };
				isSucc = isSucc && orderFormDao.updateOrdState(orderFormDTO, oldStateArray);
			}
			// 2.2 恢复包裹状态
			// if (isSucc &&
			// CollectionUtil.isNotEmptyOfCollection(updatePackageStateTCCList))
			// {
			// for (UpdatePackageStateTCC updatePackageStateTCC :
			// updatePackageStateTCCList) {
			// OrderPackage op = new OrderPackage();
			// op.setPackageId(updatePackageStateTCC.getPackageId());
			// op.setUserId(userId);
			// op.setOrderPackageState(updatePackageStateTCC.getOriState());
			// isSucc = isSucc
			// && orderPackageDao.updateOrderPackageStateAndZeroCancelTime(op,
			// updatePackageStateTCC.getNewState());
			// }
			// if (!isSucc) {
			// logger.error("orderPackageDao.updateOrderPackageState fail, tranId="
			// + tranId + ", orderId=" + orderId);
			// }
			// }

			// 3.删除TCC数据
			isSucc = isSucc && deleteTCCInDB(deleteTCCInDBForCallOffOrderByTCCList, tranId);

			// 返回
			if (!isSucc)
				throw OrderTCCServiceException.genOrderTCCServiceException("cancelCallOffOrderByTCC fail!", tranId,
						userId, orderId);
		}
		return isSucc;
	}

	/**
	 * 将TCC对象,转移到主Meta对象上
	 * 
	 * @param tccDao
	 * @param metaDao
	 * @param tranId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T1, T2> boolean moveTCCToMetaInDB(MoveTCCToMetaInDBParam param, long tranId) {
		TCCDaoInterface<T1> tccDao = param.tccDao;
		AbstractDao<T2> metaDao = param.metaDao;
		if (!(metaDao instanceof AbstractDaoSqlBase)) {
			logger.error("metaDao instanceof AbstractDaoSqlBase false!");
			return false;
		}
		Class<T2> metaClazz = (Class<T2>) PrintDaoUtil.getGenericClass((AbstractDaoSqlBase<T2>) metaDao);

		boolean isSucc = true, isFilterEnum = false;
		// 1.读取TCC记录
		List<T1> tccList = tccDao.getListByTranId(tranId);
		// 判断是否不能为空
		if (param.isTccListNotNull && CollectionUtil.isEmptyOfCollection(tccList)) {
			logger.info("####################tccList为空，TCC Confirm失败####################");
			return false;
		}

		// 2.将TCC对象转换成Meta对象
		List<T2> metaList = ReflectUtil.convertList(metaClazz, tccList, isFilterEnum);
		// 3.添加Meta对象到数据库
		isSucc = isSucc && metaDao.addObjects(metaList);
		return true;
	}

	/**
	 * 删除所有TCC数据
	 * 
	 * @param tccDaoColl
	 * @param tranId
	 * @return
	 */
	private boolean deleteTCCInDB(Collection<TCCDaoInterface<?>> tccDaoColl, long tranId) {
		boolean isSucc = true;
		for (TCCDaoInterface<?> tccDao : tccDaoColl) {
			isSucc = isSucc
					&& (tccDao.deleteByTranId(tranId) || CollectionUtil.isEmptyOfCollection(tccDao
							.getListByTranId(tranId)));
		}
		return isSucc;
	}
}