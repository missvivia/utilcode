package com.xyl.mmall.order.aop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.aop.OperateLog;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.order.dao.tcc.OrderTCCLockDao;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.enums.OperateLogType;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.param.OrderExpInfoChangeParam;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.TryAddOrderByTCCParam;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderService;


@Aspect
@Component
public class OperateLogAspect {
	
	private static final Logger log = LoggerFactory.getLogger(OperateLogAspect.class);
	
    private ConcurrentHashMap<Long, Long> tranIdAndOrderIdMap = new ConcurrentHashMap<Long, Long>();
    
    private static final ThreadLocal<OrderFormDTO> orderFormThreadLocal = new ThreadLocal<OrderFormDTO>();  
    
	private static final ThreadLocal<List<OrderFormDTO>> orderFormListThreadLocal = new ThreadLocal<List<OrderFormDTO>>(); 
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderBriefService orderBriefService;
	
	@Autowired
	private OrderTCCLockDao orderTCCLockDao;
	
    @SuppressWarnings("unchecked")
    @Before(value = "within(com.xyl.mmall.order..*) && @annotation(operateLog)")
    public void before(JoinPoint joinPoint, OperateLog operateLog)
    {
        Object target = joinPoint.getTarget();
        String serviceName = target.getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        long orderId = 0;
        for(Object obj:joinPoint.getArgs()){
			if(obj instanceof OrderOperateParam){
				//取得记录订单状态,订单金额,订单备注修改日志的订单Id
				OrderOperateParam operateParam = (OrderOperateParam)obj;
				orderId = operateParam.getOrderId();
			}else if(obj instanceof OrderLogisticsDTO){
				OrderLogisticsDTO orderLogisticsDTO = (OrderLogisticsDTO)obj;
				//取得记录物流日志的订单Id
				orderId = orderLogisticsDTO.getOrderId();
			}else if(obj instanceof InvoiceDTO){
				//取得记录发票日志的订单Id
				InvoiceDTO invoiceDTO = (InvoiceDTO)obj;
				orderId = invoiceDTO.getOrderId();
			}else if(obj instanceof OrderExpInfoChangeParam){
				//取得记录收货地址日志的订单Id
				orderId = (long)joinPoint.getArgs()[0];
			}
		}
        List<OrderCancelInfoDTO> orderCancelInfoDTOs = null;
        if ("com.xyl.mmall.order.service.impl.OrderTCCServiceInternalImpl".equals(serviceName)
                && "tryCallOffOrderByTCC".equals(methodName)){
        	    List<OrderFormDTO>orderFormDTOs = new ArrayList<OrderFormDTO>();
        		orderCancelInfoDTOs = ( List<OrderCancelInfoDTO>)joinPoint.getArgs()[1];
        		if(CollectionUtil.isNotEmptyOfList(orderCancelInfoDTOs)){
        			for (OrderCancelInfoDTO orderCancelInfoDTO : orderCancelInfoDTOs) {
        				OrderFormBriefDTO orderFormBriefDTO = orderBriefService.queryOrderFormBrief(orderCancelInfoDTO.getUserId(),orderCancelInfoDTO.getOrderId(),true);
        				OrderFormDTO orderFormDTO = new OrderFormDTO(orderFormBriefDTO);
        				if(orderCancelInfoDTO!=null){
        					orderFormDTO.setCancelReason(orderCancelInfoDTO.getReason());
        					orderFormDTO.setOperateUserType(orderCancelInfoDTO.getOperateUserType());
        					orderFormDTO.setAgentId(orderCancelInfoDTO.getAgentId());
        				}
        				orderFormDTOs.add(orderFormDTO);
					}
        		}
        		orderFormListThreadLocal.set(orderFormDTOs);
        }
        if(orderId>0){
        	OrderFormDTO orderFormDTO = orderService.queryOrderFormByOrderId(orderId);
        	orderFormThreadLocal.set(orderFormDTO);
        }
        
        // TCC的第三阶段：confirm，完成订单的生成。
        // 在订单生成之前，获取tranId，以便在订单生成之后，记录”订单生成“的操作记录
        // if
        // ("com.xyl.mmall.order.service.impl.OrderTCCServiceInternalImpl".equals(serviceName)
        // && "confirmAddOrderByTCC".equals(methodName))
        if ("com.xyl.mmall.order.service.impl.OrderTCCServiceInternalImpl".equals(serviceName)
                && "tryAddOrderByTCC".equals(methodName))
        {
            Object [] args = joinPoint.getArgs();
            long tranId = (long) args[0];
            Map<Long, TryAddOrderByTCCParam> paramMap = (Map<Long, TryAddOrderByTCCParam>) args[1];
            for (Map.Entry<Long, TryAddOrderByTCCParam> entry : paramMap.entrySet())
            {
                TryAddOrderByTCCParam param = entry.getValue();
                OrderFormCalDTO orderFormCalDTO = param.getOrderFormCalDTO();
                long parentId = orderFormCalDTO.getParentId();
                tranIdAndOrderIdMap.put(tranId, parentId);
                break;
            }
            // // 获得OrderTCCLock的记录锁
            // OrderTCCLock tccLock = orderTCCLockDao.getLockByKey(OrderTCCLock
            // .genOrderTCCLock(tranId));
            // if (tccLock != null && tccLock.getType() ==
            // OrderTCCLockType.ADD_ORDER)
            // {
            // log.info("####################开始记录生成订单的操作，pranetId：" +
            // tccLock.getOrderId()
            // + ", tranId：" + tccLock.getTranId() + "####################");
            // tranIdAndOrderIdMap.put(tranId, tccLock.getOrderId());
            // }
        }
    }

	@AfterReturning(value = "within(com.xyl.mmall.order..*) && @annotation(operateLog)", returning = "result")
	public void excute(JoinPoint joinPoint,OperateLog operateLog,Object result) {
	    
	    Object target = joinPoint.getTarget();
        Object[] args = null;
        String serviceName = target.getClass().getName();
        String methodName = joinPoint.getSignature().getName();
		OrderOperateLogDTO operateLogDTO = null;
        if (log.isDebugEnabled())
            log.debug("++++ " + serviceName + "#" + methodName + " OperateLog Begin +++++");
        
	    if(result instanceof Boolean)
        {
            // 判断是否是执行生成订单的操作
            if ("com.xyl.mmall.order.service.impl.OrderTCCServiceInternalImpl".equals(serviceName)
                    && ("confirmAddOrderByTCC".equals(methodName)||"confirmCallOffOrderByTCC".equals(methodName)))
            {
                boolean isSucc = (boolean) result;
                long tranId = (long) joinPoint.getArgs()[0];
                if (isSucc)
                {
                    if (tranIdAndOrderIdMap.containsKey(tranId))
                    {
                        // 订单生成成功
                        List<OrderOperateLogDTO>operateLogDTOList = buildOperateOrderCreateLog(tranIdAndOrderIdMap.get(tranId));
                        if(operateLogDTOList != null)
                        {
                            orderService.addOrderOperateLogs(operateLogDTOList);
                        }
                    }
                }
                else
                {
                    // 订单生成失败
                    log.info("####################订单生成失败，pranetId："
                            + tranIdAndOrderIdMap.get(tranId) + ", tranId：" + tranId
                            + "####################");
                }
                
                tranIdAndOrderIdMap.remove(tranId);
            }
            
            //取消订单记录
            if ("com.xyl.mmall.order.service.impl.OrderTCCServiceInternalImpl".equals(serviceName)
                    && "confirmCallOffOrderByTCC".equals(methodName)){
            		List<OrderFormDTO> orderFormDTOs = orderFormListThreadLocal.get();
            		if(orderFormDTOs == null){
            			return;
            		}
            		for (OrderFormDTO orderFormDTO : orderFormDTOs) {
            			operateLogDTO = buildCancelOrderLog(orderFormDTO);
            			orderService.addOrderOperateLog(operateLogDTO);
					}
            		orderFormListThreadLocal.remove();
            }
            
            return;
        }
	    
		if(result instanceof Integer && (Integer)result<=0){
				return ;
		}
		OrderFormDTO orderFormDTO = orderFormThreadLocal.get();
		try {
			args = joinPoint.getArgs();
			for(Object obj:args){
				if(obj instanceof OrderOperateParam){
					OrderOperateParam orderOperateParam = (OrderOperateParam)obj; 
					//修改关联订单时
					if(orderOperateParam.getParentId()>0){
						List<OrderFormDTO> orderFormDTOs = orderService.queryOrderFormList(orderOperateParam.getUserId(),orderOperateParam.getParentId(), true);
						if(CollectionUtil.isNotEmptyOfList(orderFormDTOs)){
							 List<OrderOperateLogDTO> operateLogDTOs = new ArrayList<OrderOperateLogDTO>();
							for (OrderFormDTO orderFormDTO2 : orderFormDTOs) {
								operateLogDTOs.add(buildOperateLog(orderOperateParam,orderFormDTO2));
							}
							orderService.addOrderOperateLogs(operateLogDTOs);
						}
					}else{
						//记录订单状态,订单金额,订单备注修改日志
						operateLogDTO = buildOperateLog(orderOperateParam,orderFormDTO);
					}
				}else if(obj instanceof OrderLogisticsDTO){
					OrderLogisticsDTO orderLogisticsDTO = (OrderLogisticsDTO)obj;
					//记录物流日志   物流业务暂时不考虑
					//operateLogDTO = buildOperateLogisticsLog(orderLogisticsDTO,orderFormDTO);
					if(methodName.endsWith("deliverGoods")){//发货
						OrderOperateParam param = new OrderOperateParam();
						param.setAgentId(orderLogisticsDTO.getUpdateBy());
						OrderFormState newState = OrderFormState.ALL_DELIVE;
						OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE };
						param.setOperateLogType(OperateLogType.ORDER_STATE);
						param.setUserId(orderLogisticsDTO.getUserId());
						param.setOrderId(orderLogisticsDTO.getOrderId());
						param.setBusinessId(orderLogisticsDTO.getBusinessId());
						param.setOperateUserType(orderLogisticsDTO.getOperateUserType());
						param.setNewState(newState);
						param.setOldStateArray(oldStateArray);
						operateLogDTO = buildOperateLog(param,orderFormDTO);
					}
				}else if(obj instanceof InvoiceDTO){
					//记录发票日志
					operateLogDTO = buildOperateInvoiceLog((InvoiceDTO)obj,orderFormDTO);
				}else if(obj instanceof OrderExpInfoChangeParam){
					//记录收货地址日志
					operateLogDTO = buildOperateAddressLog((OrderExpInfoChangeParam)obj, orderFormDTO);
				}
			}
			if(operateLogDTO!=null){
				orderService.addOrderOperateLog(operateLogDTO);
			}
			orderFormThreadLocal.remove();
		}catch( ServiceException serviceException){
			log.error(serviceException.getMessage(), serviceException);
			log.error("order operate log error:",operateLogDTO.toString());
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("order operate log error:",operateLogDTO.toString());
		}

		if (log.isDebugEnabled())
			log.debug("++++ " + serviceName + "#" + methodName + " OperateLog End +++++");
	}
	
	private List<OrderOperateLogDTO> buildOperateOrderCreateLog(long orderId)
    {
        List<OrderFormDTO> orderFormDTOList = orderService.queryOrderFormListByParentId(orderId);
        if (orderFormDTOList != null)
        {
            List<OrderOperateLogDTO> result = new ArrayList<OrderOperateLogDTO>();
            for (OrderFormDTO orderFormDTO : orderFormDTOList)
            {
                OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
                operateLogDTO.setOperatorId(orderFormDTO.getUserId());
                operateLogDTO.setBusinessId(orderFormDTO.getBusinessId());
                operateLogDTO.setOrderId(orderFormDTO.getOrderId());
                operateLogDTO.setNote("");
                operateLogDTO.setOperatorType(OperateUserType.USER.getIntValue());
                operateLogDTO.setType(OperateLogType.ORDER_STATE.getIntValue());
                operateLogDTO.setPreContent("");
                operateLogDTO.setCurContent("订单已经生成");
                
                result.add(operateLogDTO);
            }
            return result;
        }
        return null;
    }
	
	private OrderOperateLogDTO buildCancelOrderLog(OrderFormDTO orderFormDTO){
		 if(orderFormDTO==null){
			return null;
		 }
		 OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
	     operateLogDTO.setOperatorId(orderFormDTO.getAgentId());
	     operateLogDTO.setBusinessId(orderFormDTO.getBusinessId());
	     operateLogDTO.setOrderId(orderFormDTO.getOrderId());
	     operateLogDTO.setNote(orderFormDTO.getCancelReason());
	     operateLogDTO.setOperatorType(OperateUserType.USER.getIntValue());
	     operateLogDTO.setType(OperateLogType.ORDER_STATE.getIntValue());
	     operateLogDTO.setPreContent(orderFormDTO.getOrderFormState().getDesc());
	     operateLogDTO.setCurContent(OrderFormState.CANCEL_ED.getDesc());
	     return operateLogDTO;
	}
	
	//记录订单状态,订单金额,订单备注修改日志
	private OrderOperateLogDTO buildOperateLog(OrderOperateParam param,OrderFormDTO orderFormDTO){
		if(orderFormDTO==null){
			return null;
		}
		//OrderFormDTO orderFormDTO = orderService.queryOrderFormByOrderId(param.getOrderId());
		OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
		operateLogDTO.setOperatorId(param.getAgentId());
		operateLogDTO.setBusinessId(orderFormDTO.getBusinessId());
		operateLogDTO.setOrderId(orderFormDTO.getOrderId());
		operateLogDTO.setNote(param.getComment());
		operateLogDTO.setOperatorType(param.getOperateUserType()==null?3:param.getOperateUserType().getIntValue());
		switch (param.getOperateLogType()) {
			case ORDER_STATE:
				operateLogDTO.setType(OperateLogType.ORDER_STATE.getIntValue());
				operateLogDTO.setPreContent(orderFormDTO.getOrderFormState().getDesc());
				operateLogDTO.setCurContent(param.getNewState().getDesc());
				break;
			case ORDER_CASH:
				operateLogDTO.setPreContent(orderFormDTO.getCartRPrice().toString());
				operateLogDTO.setCurContent(param.getTotalCash().toString());
				operateLogDTO.setType(OperateLogType.ORDER_CASH.getIntValue());
				break;
			case ORDER_COMMENT:
				operateLogDTO.setPreContent(orderFormDTO.getComment());
				operateLogDTO.setCurContent(param.getComment()==null?"":param.getComment());
				operateLogDTO.setType(OperateLogType.ORDER_COMMENT.getIntValue());
		default:
			break;
		}
		return operateLogDTO;
	}
	
	//记录物流日志
	private OrderOperateLogDTO buildOperateLogisticsLog(OrderLogisticsDTO param,OrderFormDTO orderFormDTO){
		//OrderFormDTO orderFormDTO = orderService.queryOrderFormByOrderId(param.getOrderId());
		String preContent = "", curContent = "";
		OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
		if(param.getId()>0){
			List<OrderLogisticsDTO> orderLogisticsDTOs = orderFormDTO.getOrderLogisticsDTOs();
			for(OrderLogisticsDTO orderLogisticsDTO:orderLogisticsDTOs){
				if(orderLogisticsDTO.getId()==param.getId()){
					preContent = "快递公司:"+orderLogisticsDTO.getExpressCompany().getName()+" "+"快递号:"+orderLogisticsDTO.getMailNO();
				}
			}
		}
			
		curContent = "快递公司:"+param.getExpressCompany().getName()+" "+"快递号:"+param.getMailNO();
		operateLogDTO.setOperatorId(param.getUpdateBy());
		operateLogDTO.setBusinessId(orderFormDTO.getBusinessId());
		operateLogDTO.setOrderId(param.getOrderId());
		operateLogDTO.setNote(param.getComment());
		operateLogDTO.setOperatorType(param.getOperateUserType().getIntValue());
		operateLogDTO.setPreContent(preContent);
		operateLogDTO.setCurContent( curContent);
		operateLogDTO.setType(OperateLogType.ORDER_LOGISTICS.getIntValue());
		return operateLogDTO;
	}
	
	//记录发票日志
	private OrderOperateLogDTO buildOperateInvoiceLog(InvoiceDTO param,OrderFormDTO orderFormDTO){
		if(orderFormDTO==null){
			return null;
		}
		//OrderFormDTO orderFormDTO = orderService.queryOrderFormByOrderId(param.getOrderId());
		String preContent = "", curContent = "";
		OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
		if(param.getId()>0){
			List<InvoiceDTO> invoiceDTOs = orderFormDTO.getInvoiceDTOs();
			if(CollectionUtil.isNotEmptyOfList(invoiceDTOs)){
				for(InvoiceDTO invoiceDTO:invoiceDTOs){
					if(invoiceDTO.getId()==param.getId()){
						preContent = invoiceDTO.getTitle() + " "+invoiceDTO.getInvoiceNo();
					}
				}
			}
		}
		curContent = param.getTitle()+" "+param.getInvoiceNo();
		operateLogDTO.setOperatorId(param.getUpdateBy());
		operateLogDTO.setBusinessId(orderFormDTO.getBusinessId());
		operateLogDTO.setOrderId(param.getOrderId());
		operateLogDTO.setNote(param.getComment());
		operateLogDTO.setOperatorType(param.getOperateUserType().getIntValue());
		operateLogDTO.setPreContent(preContent);
		operateLogDTO.setCurContent(curContent);
		operateLogDTO.setType(OperateLogType.ORDER_INVOICE.getIntValue());
		return operateLogDTO;
	}
	
	//记录收货地址日志
	private OrderOperateLogDTO buildOperateAddressLog(OrderExpInfoChangeParam param,OrderFormDTO orderFormDTO){
			if(orderFormDTO==null){
				return null;
			}
			//OrderFormDTO orderFormDTO = orderService.queryOrderFormByOrderId(orderId);
			OrderExpInfoDTO ordExpInfoDTO = orderFormDTO.getOrderExpInfoDTO();
			OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
			operateLogDTO.setOperatorId(param.getOperatorId());
			operateLogDTO.setBusinessId(orderFormDTO.getBusinessId());
			operateLogDTO.setOrderId(orderFormDTO.getOrderId());
			operateLogDTO.setNote(param.getComment());
			operateLogDTO.setOperatorType(param.getOperateUserType().getIntValue());
			operateLogDTO.setPreContent(preFullAddressInfo(ordExpInfoDTO));
			operateLogDTO.setCurContent(curFullAddressInfo(param));
			operateLogDTO.setType(OperateLogType.ORDER_ADDRESS.getIntValue());
			return operateLogDTO;
	}
	
	//修改后的收货地址
	private String curFullAddressInfo(OrderExpInfoChangeParam param) {
		StringBuffer strBuf = new StringBuffer(1024);
		strBuf.append(param.getConsigneeName()+","+param.getConsigneeMobile());
		if(StringUtils.isNotEmpty(param.getAreaCode())||StringUtils.isNotEmpty(param.getConsigneeTel())){
			strBuf.append(","+param.getAreaCode()+"-"+param.getConsigneeTel());
		}
	 	strBuf.append("<br/>") 
		.append(param.getProvince()) 
		.append(param.getCity())
		.append(param.getSection())
		.append(param.getAddress());
		if(StringUtils.isNotEmpty(param.getZipcode())){
			strBuf.append(","+param.getZipcode());
		}
		return strBuf.toString();
	}
	
	//修改前的收货地址
	private String preFullAddressInfo(OrderExpInfoDTO ordExpInfo) {
		StringBuffer strBuf = new StringBuffer(1024);
		strBuf.append(ordExpInfo.getConsigneeName()+","+ordExpInfo.getConsigneeMobile());
		if(StringUtils.isNotEmpty(ordExpInfo.getAreaCode())||StringUtils.isNotEmpty(ordExpInfo.getConsigneeTel())){
			strBuf.append(","+ordExpInfo.getAreaCode()+"-"+ordExpInfo.getConsigneeTel());
		}
	 	strBuf.append("<br/>") 
		.append(ordExpInfo.getProvince()) 
		.append(ordExpInfo.getCity())
		.append(ordExpInfo.getSection())
		.append(ordExpInfo.getAddress());
		if(StringUtils.isNotEmpty(ordExpInfo.getZipcode())){
			strBuf.append(","+ordExpInfo.getZipcode());
		}
		return strBuf.toString();
	}
}
